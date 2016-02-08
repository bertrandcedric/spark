import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.mllib.util.MLUtils

object SparkRandomForrestRegressor {


    println("########## Init context Spark")
    val sc: SparkContext = new SparkContext(new SparkConf().setAppName("test").setMaster("local"))

    println("########## Load and parse the data file")
    val trainingData: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, "src/main/resources/SparkRandomForrestRegressor/training.txt")
    val testData: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, "src/main/resources/SparkRandomForrestRegressor/test.txt")

    println("########## Show data from RDD")
    trainingData.take(1).foreach(println)

    println("########## Train a RandomForest model")
    val categoricalFeaturesInfo: Map[Int, Int] = Map[Int, Int]()
    val numTrees: Int = 3 // Use more in practice.
    val featureSubsetStrategy: String = "auto" // Let the algorithm choose.
    val impurity: String = "variance"
    val maxDepth: Int = 4
    val maxBins: Int = 32

    val model: RandomForestModel = RandomForest.trainRegressor(trainingData, categoricalFeaturesInfo,
      numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)

    println("########## Evaluate model on test instances")
    val labelAndPreds: RDD[(Double, Double)] = testData.map { point =>
      val prediction: Double = model.predict(point.features)
      (point.label, prediction)
    }

    println("########## Compute test error")
    val testErr: Double = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / testData.count()
    println("########## Test Error = " + testErr)
    println("########## Learned classification forest model:\n" + model.toDebugString)


}
