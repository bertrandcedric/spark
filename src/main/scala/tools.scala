import org.apache.spark.mllib.linalg.{Matrix, Matrices}
import org.apache.spark.rdd.RDD



/**
  * Created by fsznajderman on 04/02/2016.
  */
object tools {

  /**
    * Format line as matrix
    * @param line
    */
  def display(line: String): Unit = {
    val range = computeTypeWriter(line)
    val work: Array[String] = line.split(",").map(p => {
      p match {
        case "0.0" => "0"
        case x if range.contains(x) => "*"
        case x if !range.contains(x) => "-"
      }
    })
    work.grouped(28).foreach { l =>
      println
      l.foreach(print)
    }
    println
  }

  /**
    * try to manage the manner that writer write the numer (strong or not)
    * @param line
    * @return
    */
  def computeTypeWriter(line: String): Range = {
    val work: Array[Double] = line.split(",").filter(i => i != "0.0").map(p => p.toDouble)
    val mean = (work.sum / work.size.toDouble).toInt
    Range(mean - 50, mean + 50)
  }

  /**
    * transform pixel value into value from 0 to 1
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
    * @param rdd
    * @return
    */
  def t(rdd: RDD[String]): RDD[Array[Double]] = {
    rdd.map(l => l.replaceAll(";",",").split(",").map(i => i.toDouble))
  }

  def filterDigitwithNotEnoughtAccuracy(rdd: RDD[String]): RDD[Array[Double]] = {

    val r: RDD[(Array[Double], Matrix)] = t(rdd).map(l => (l, Matrices.dense(28, 28, l.tail)))



    r.filter(d => d._2.numNonzeros == 1).groupBy(r => {
      r._2.numNonzeros
    }).foreach(el => println(el._1 + "," + el._2.size))

    r.filter(t => t._2.numNonzeros > 10).map(r => r._1)
    // Test Error = 0.06880733944954129 -> 50

  }

}
