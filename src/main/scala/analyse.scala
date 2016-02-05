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
        case x if range.contains(x) => "*"
        case _ => "0"
      }
    })
    work.grouped(28).foreach{line =>
      println
      line.foreach(print)
    }
  }

  def computeTypeWriter(line:String): Range ={

    val work: Array[Int] = line.split(",").filter(i => i !=0 ).map(p => p.toInt)

    val mean = work.sum / work.size

    Range(mean + 10,mean-10)


  }


}
