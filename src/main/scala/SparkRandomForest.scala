import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}

object SparkRandomForest {

  def mapTo1(d: Double): Double = {
    d match {
      case 0 => d
      case x if x <= 127 => 0
      case _ => 1
    }
  }

  def main(args: Array[String]): Unit = {
    // Init spark context
    val conf = new SparkConf().setAppName("test").setMaster("local")
    val sc = new SparkContext(conf)

    //load train data
    val data = sc.textFile("data/train.csv")
    val splits = data.randomSplit(Array(0.98, 0.02))
    val (trainingData, checkData) = (splits(0), splits(1))

    val train = trainingData.map(d => {
      val l = d.replaceAll(";", ",").split(",")
      LabeledPoint(l.head.toInt, new DenseVector(l.tail.map(s => mapTo1(s.toInt))))
    })

//    val numClasses = 10
//    val categoricalFeaturesInfo = Map[Int, Int]()
//    val impurity = "gini"
//    val maxDepth = 5
//    val maxBins = 100
//    val model = DecisionTree.trainClassifier(train, numClasses, categoricalFeaturesInfo,
//      impurity, maxDepth, maxBins)

//    val numClasses = 10
//    val categoricalFeaturesInfo = Map[Int, Int]()
//    val numTrees = 30 // Use more in practice.
//    val featureSubsetStrategy = "auto" // Let the algorithm choose.
//    val impurity = "gini"
//    val maxDepth = 7
//    val maxBins = 100
//    val model = RandomForest.trainClassifier(
//      train,
//      numClasses,
//      categoricalFeaturesInfo,
//      numTrees,
//      featureSubsetStrategy,
//      impurity,
//      maxDepth,
//      maxBins,
//      0)

    val model = new LogisticRegressionWithLBFGS()
      .setNumClasses(10)
      .run(train)

    // Verification des labels avec un sous ensemble de données
    val transformedCopieData = checkData.map(d => {
      val l = d.replaceAll(";", ",").split(",")
      LabeledPoint(l.head.toInt, new DenseVector(l.tail.map(s => mapTo1(s.toInt))))
    })

    val checkLabel = transformedCopieData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    checkLabel.collect().filter(x => x._1 != x._2).groupBy(l => l).map(t => (t._1, t._2.length)).foreach(println)

    val testErr = checkLabel.filter(r => r._1 != r._2).count.toDouble / checkData.count()
    println("Test Error = " + testErr)

//    // Prediction avec le jeu de test
//    val testData = sc.textFile("data/test.csv")
//    val test = testData.map(d => {
//      val l = d.replaceAll(";", ",").split(",").map(s => mapTo1(s.toInt))
//      new DenseVector(l)
//    })
//
//    val prediction = test.map { point =>
//      model.predict(point)
//    }
//
//    val writer = new PrintWriter(new File("data/results.csv"))
//    writer.write("ImageId,Label" + "\n")
//    prediction.collect().zipWithIndex foreach {
//      case(l, i) => writer.write((i + 1) + "," + l.toInt + "\n")}
//    writer.close()
  }
}
