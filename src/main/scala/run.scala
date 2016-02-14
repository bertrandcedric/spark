import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.{RandomForest, DecisionTree}
import org.apache.spark.rdd.RDD

/**
  * Created by fs24818n on 12/02/2016.
  */
object run {


  def trainClassifier(train: RDD[LabeledPoint]) = {
    val numClasses = 10
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "gini"
    val maxDepth = 5
    val maxBins = 100
    DecisionTree.trainClassifier(train, numClasses, categoricalFeaturesInfo,
      impurity, maxDepth, maxBins)
  }

  def randomForest(train: RDD[LabeledPoint]) = {
    val numClasses = 10
    val categoricalFeaturesInfo = Map[Int, Int]()
    val numTrees = 30 // Use more in practice.
    val featureSubsetStrategy = "auto" // Let the algorithm choose.
    val impurity = "gini"
    val maxDepth = 7
    val maxBins = 100
    RandomForest.trainClassifier(
      train,
      numClasses,
      categoricalFeaturesInfo,
      numTrees,
      featureSubsetStrategy,
      impurity,
      maxDepth,
      maxBins,
      0)
  }

  def logisticRegressionWithLBFGS(train: RDD[LabeledPoint]) = {
   new LogisticRegressionWithLBFGS()
      .setNumClasses(10)
      .run(train)

  }
}
