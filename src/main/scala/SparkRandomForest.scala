import java.io.{File, PrintWriter}

import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.{RandomForest, DecisionTree}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import tools._
import run._

object SparkRandomForest extends App {


  // Init spark context
  val conf = new SparkConf().setAppName("test").setMaster("local[*]")
  val sc = new SparkContext(conf)

  //load train data

  val splits = t(sc.textFile("data/train.csv")).map(l => {
    val max = l.max
    LabeledPoint(l.head.toInt, new DenseVector(l.tail.map(s => mapTo1(s.toInt, max))))
  }).randomSplit(Array(0.98, 0.02))


  val (trainingData, checkData) = (splits(0), splits(1))


  //val model = trainClassifier(trainingData)

  //val model = randomForest((trainingData))

  val model = logisticRegressionWithLBFGS(trainingData)

  // Verification des labels avec un sous ensemble de données
  val checkLabel = checkData.map { point =>
    val prediction = model.predict(point.features)
    (point.label, prediction, point.features)
  }

  checkLabel.collect().filter(x => x._1 != x._2).groupBy(l => (l._1, l._2)).map(t => (t._1, t._2.length, t._2)).foreach {
    t => {
      println("(label , prediction) => " + t._1)
      println("Nombre de prediction fausse => " + t._2)
      t._3.foreach {
        x => display(x._3.toArray.mkString(","))
      }
    }
  }

  val testErr = checkLabel.filter(r => r._1 != r._2).count.toDouble / checkData.count()
  println("Test Error = " + testErr)

  /*// Prediction avec le jeu de test
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
*/



}
