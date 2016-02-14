import java.io.{File, PrintWriter}

import org.apache.spark.SparkContext
import org.apache.spark.mllib.classification._
import org.apache.spark.mllib.linalg.{DenseVector, Matrices, Matrix}
import org.apache.spark.rdd.RDD


/**
  * Created by fsznajderman on 04/02/2016.
  */
object tools {



  def refineMatrix(line: Array[Double]):Array[Double] = {
    val mean = computeTypeWriter(line)
    line.map(p => {
      p match {
        case x if x < (mean) => 0.0
        case _ => p
      }
    })

  }


  /**
    * Format line as matrix
    *
    * @param line
    */
  def display(line: Array[Double]): Unit = {
    val mean = computeTypeWriter(line)
    val work = line.map(p => {
      p match {
        case 0.0 => "0"
        case x if x < mean => "0"
        case _ => "*"
      }
    })
    work.grouped(28).foreach { l =>
      println
      if(l.filter{ p => p!="0"}.length >0 ) {
        l.foreach(print)
      }
    }
    println

  }

  /**
    * try to manage the manner that writer write the numer (strong or not)
    *
    * @param line
    * @return
    */
  def computeTypeWriter(line: Array[Double]): Double = {
    val work = line.filter(i => i != 0.0)
    (work.sum / work.size.toDouble)

  }

  /**
    * transform pixel value into value from 0 to 1
    *
    * @param d
    * @param max
    * @return
    */
  def mapTo1(d: Double, max: Double): Double = {
    d match {
      case 0 => d
      case x => x / max
    }
  }

  /**
    * Initiale transformation
    *
    * @param rdd
    * @return
    */
  def t(rdd: RDD[String]): RDD[Array[Double]] = {
    rdd.map(l => l.replaceAll(";", ",").split(",").map(i => i.toDouble))
  }

  def filterDigitwithNotEnoughtAccuracy(rdd: RDD[String]): RDD[Array[Double]] = {

    val r: RDD[(Array[Double], Matrix)] = t(rdd).map(l => (l, Matrices.dense(28, 28, l.tail)))



    r.filter(d => d._2.numNonzeros == 1).groupBy(r => {
      r._2.numNonzeros
    }).foreach(el => println(el._1 + "," + el._2.size))

    r.map(r => r._1)

  }

  def submit(sc: SparkContext, model: LogisticRegressionModel): Unit = {
    val testData = sc.textFile("data/test.csv")
    val test = t(testData).map(d => {
      val max = d.max
      val l = d.map(s => mapTo1(s, max))
      new DenseVector(l)
    })

    val prediction = test.map { point =>
      model.predict(point)
    }

    val writer = new PrintWriter(new File("data/results.csv"))
    writer.write("ImageId,Label" + "\n")
    prediction.collect().zipWithIndex foreach {
      case (l, i) => writer.write((i + 1) + "," + l.toInt + "\n")
    }
    writer.close()
  }

}
