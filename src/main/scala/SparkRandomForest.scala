import java.io.{File, PrintWriter}

import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.{SparkConf, SparkContext}

object SparkRandomForest {

  def mapTo1(d: Double): Double = {
    d match {
      case 0 => d
      case _ => 1
    }
  }

  def main(args: Array[String]): Unit = {
    // Init spark context
    val conf = new SparkConf().setAppName("test").setMaster("local")
    val sc = new SparkContext(conf)

    //load train data
    val data = sc.textFile("data/train.csv")
    val transformedData = data.map(d => {
      val l = d.replaceAll(";", ",").split(",")
      LabeledPoint(l.head.toDouble, new DenseVector(l.tail.map(s => mapTo1(s.toDouble))))
    })

    val numClasses = 10
    val categoricalFeaturesInfo = Map[Int, Int]()
    val numTrees = 25 // Use more in practice.
    val featureSubsetStrategy = "auto" // Let the algorithm choose.
    val impurity = "gini"
    val maxDepth = 4
    val maxBins = 100

    val model = RandomForest.trainClassifier(
      transformedData,
      numClasses,
      categoricalFeaturesInfo,
      numTrees,
      featureSubsetStrategy,
      impurity,
      maxDepth,
      maxBins,
      0)

    println("Learned classification forest model:\n" + model.toDebugString)

    // Verification des labels avec un sous ensemble de données
    val dataCopie = sc.textFile("data/trainCopie.csv")
    val transformedCopieData = dataCopie.map(d => {
      val l = d.replaceAll(";", ",").split(",")
      LabeledPoint(l.head.toDouble, new DenseVector(l.tail.map(s => mapTo1(s.toDouble))))
    })

    val checkLabel = transformedCopieData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    checkLabel.collect().foreach(println)
    val testErr = checkLabel.filter(r => r._1 != r._2).count.toDouble / dataCopie.count()
    println("Test Error = " + testErr)

    // Prediction avec le jeu de test
    val testData = sc.textFile("data/test.csv")
    val transformedTestData = testData.map(d => {
      val l = d.replaceAll(";", ",").split(",").map(s => mapTo1(s.toDouble))
      new DenseVector(l)
    })

    val prediction = transformedTestData.map { point =>
      model.predict(point)
    }

    val writer = new PrintWriter(new File("data/results.csv"))
    writer.write("ImageId,Label" + "\n")
    prediction.collect().zipWithIndex foreach {
      case(l, i) => writer.write((i + 1) + "," + l.toInt + "\n")}
    writer.close()
  }
}
