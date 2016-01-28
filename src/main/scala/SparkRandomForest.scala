import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.{SparkConf, SparkContext}

object SparkRandomForest {

  def main(args: Array[String]): Unit = {
    // Init spark context
    val conf = new SparkConf().setAppName("test").setMaster("local")
    val sc = new SparkContext(conf)

    //load train data
    val data = sc.textFile("data/train.csv")
    val transformedData = data.map(d => {
      val l = d.split(",").map(s => s.toDouble)
      LabeledPoint(l.head, new DenseVector(l.tail))
    })

    val testData = sc.textFile("data/trainCopie.csv")
    val transformedTestData = testData.map(d => {
      val l = d.split(";").map(s => s.toDouble)
      LabeledPoint(l.head, new DenseVector(l.tail))
    })

    val numClasses = 10
    val categoricalFeaturesInfo = Map[Int, Int]()
    val numTrees = 5 // Use more in practice.
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
      maxBins)

    println("Learned classification forest model:\n" + model.toDebugString)

    val labelAndPreds = transformedTestData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    val testErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / testData.count()
    println("Test Error = " + testErr)
    labelAndPreds.collect().foreach(println)

  }
}
