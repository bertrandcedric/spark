import org.apache.log4j.Level
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.{SparkConf, SparkContext}

object Test extends App {

  val conf = new SparkConf().setAppName("test").setMaster("local[*]")
  val sc = new SparkContext(conf)
  sc.setLogLevel(Level.WARN.toString)
  val sqlContext = new org.apache.spark.sql.SQLContext(sc)

  val s = sqlContext.read
                    .format("com.databricks.spark.csv")
                    .option("header", "true")
                    .option("inferSchema", "true")
                    .load("spark/src/test/resources/home_data.csv")
                    .cache()
  val sales = s.withColumn("label", s("price").cast(DoubleType))
  val splits = sales.randomSplit(Array(.8, .2), 0)
  val training = sales
  val test = splits(1)

  test.printSchema()

  val extractFeatures = new VectorAssembler()
    .setInputCols(Array("sqft_living"))
    .setOutputCol("features")

  val lr = new LinearRegression()
    .setMaxIter(40)
    .setFeaturesCol("features")
    .setLabelCol("label")

  val pipeline = new Pipeline().setStages(Array(extractFeatures, lr))

  val model = pipeline.fit(training)

  val prediction = model.transform(test).select("id", "label", "prediction")

  prediction
    .take(10)
    .foreach { case Row(id: Long, label: Double, prediction: Double) =>
      println(s"($id) --> label=$label, prediction=$prediction")
    }
}
