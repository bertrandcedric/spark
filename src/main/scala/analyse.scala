import org.apache.commons.lang.math.IntRange

import scala.io.Source

/**
  * Created by fsznajderman on 04/02/2016.
  */
object analyse extends App {

  Source.fromFile("data/test.csv").getLines().take(1).foreach(display)

  def display(line: String): Unit = {

    val work: Array[String] = line.split(",").map(p => {
      val range = computeTypeWriter(line)
      p match {
        case "0" => "0"
        case x if range.contains(x.toInt) => "*"
        case x if !range.contains(x.toInt) => "-" +
          "" +
          ""
      }
    })
    work.grouped(28).foreach{l =>
      println
      l.foreach(print)
    }
    println
  }

  def computeTypeWriter(line:String): Range ={
    val work: Array[Int] = line.split(",").filter(i => i != "0" ).map(p => p.toInt)
    val mean: Int = work.sum / work.size
    Range(mean - 50, mean + 50)
  }
}
