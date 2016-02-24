import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionModel, LinearRegressionWithSGD}
import org.apache.spark.sql.Row
import org.apache.spark.{SparkConf, SparkContext}

val conf = new SparkConf().setAppName("test").setMaster("local[*]")
val sc = new SparkContext(conf)
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
val sales = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load("src/main/resources/data/home_data.csv").cache()

sales.printSchema()

val splits = sales.randomSplit(Array(.8, .2), 0)
val (training, test) = (splits(0), splits(1))

training.select("price", "sqft_living").head

val trainingData = training.map((sale: Row) => LabeledPoint(sale.getInt(0).toDouble, Vectors.dense(sale.getDouble(1))))

val model: LinearRegressionModel = LinearRegressionWithSGD.train(trainingData,10)
