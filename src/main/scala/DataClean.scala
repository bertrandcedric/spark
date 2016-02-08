import scala.io.Source

/**
  * Created by fs24818n on 03/02/2016.
  */
object DataClean  {


  val train: Iterator[String] = Source.fromFile("data/train.csv").getLines()
  val all = train.flatMap(f => f.split(",").map(d => d.toInt).filter(e => e != 0)).toSeq

  //println(all.sum)



  println(all.sum / all.size)


  //println(all.sum / all.size)


}
