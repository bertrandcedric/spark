import org.apache.spark.mllib.linalg.{Matrix, Matrices}

import scala.io.Source

/**
  * Created by fs24818n on 03/02/2016.
  */
object DataClean  {


  // Create a dense matrix ((1.0, 2.0), (3.0, 4.0), (5.0, 6.0))
  val dm = Seq(Matrices.dense(3, 2, Array(0.0, 0.0, 0.0, 0.0, 0.0, 6.0)),Matrices.dense(3, 2, Array(0.0, 2.0, 3.0, 0.0, 0.0, 6.0)),Matrices.dense(3, 2, Array(0.0, 0.0, 0.0, 0.0, 0.0, 6.0)))

  dm.groupBy(d => d.numNonzeros).foreach(el => println(el._1  + "," + el._2.size))







}
