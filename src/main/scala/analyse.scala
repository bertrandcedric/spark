import org.apache.spark.mllib.linalg.Matrices
import org.apache.spark.rdd.RDD

import scala.io.Source

/**
  * Created by fsznajderman on 04/02/2016.
  */
object analyse  {



  def display(line: String): Unit = {
    val range = computeTypeWriter(line)
    val work: Array[String] = line.split(",").map(p => {
      p match {
        case "0" => "0"
        case x if range.contains(x.toInt) => "*"
        case x if !range.contains(x.toInt) => "-"
      }
    })
    work.grouped(28).foreach { l =>
      println
      l.foreach(print)
    }
    println
  }

  def computeTypeWriter(line: String): Range = {
    val work: Array[Int] = line.split(",").filter(i => i != "0").map(p => p.toInt)
    val mean: Int = work.sum / work.size
    Range(mean - 50, mean + 50)
  }


  def filterDigitwithNotEnoughtAccuracy(rdd: RDD[String]): RDD[Array[Double]] = {

    rdd.map(l => l.split(",").map(i => i.toDouble)).filter(all => Matrices.dense(28, 28, all.tail).numNonzeros > 300)
    // Test Error = 0.06880733944954129 -> 50

  }

}
