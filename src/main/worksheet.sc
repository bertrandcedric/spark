import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.regression.LabeledPoint

import scala.io.Source

val checkData = Source.fromFile("C:\\dev\\perso\\spark\\data\\train.csv").getLines().take(1)
val transformedCopieData = checkData.map(d => {
  val l = d.replaceAll(";", ",").split(",")
  LabeledPoint(l.head.toInt, new DenseVector(l.tail.map(s => s.toDouble)))
})
val checkLabel = transformedCopieData.map { point =>
  (point.label, point.label + 1, point.features)
}
val result = checkLabel.toArray
result.take(10).filter(x => x._1 != x._2).groupBy(l => (l._1, l._2)).map(t => (t._1, t._2.length, t._2)).foreach{
  t => {
    println("")
    println("(label , prediction) => " + t._1)
    println("Nombre de prediction fausse => " + t._2)
    t._3.foreach{
      x => {
        analyse.display(x._3.toArray.map(d => d.toInt).mkString(","))
      }
    }
  }
}
