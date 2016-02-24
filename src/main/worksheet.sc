import breeze.linalg.max
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionModel, LinearRegressionWithSGD}
import org.apache.spark.sql.Row
import org.apache.spark.{SparkConf, SparkContext}

val conf = new SparkConf().setAppName("test").setMaster("local[*]")
val sc = new SparkContext(conf)
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
val sales = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load("src/main/resources/data/home_data.csv").cache()

val splits = sales.randomSplit(Array(.8, .2), 0)
val training = sales
val test = splits(1)

val max_bedrooms = 0//test.agg(max(test.col("bedrooms"))).head.getInt(0).toDouble;
val max_bathrooms = 0//test.agg(max(test.col("bathrooms"))).head.getDouble(0).toDouble;
val max_sqft_living = 0//test.agg(max(test.col("sqft_living"))).head.getInt(0).toDouble;
val max_sqft_lot = 0//test.agg(max(test.col("sqft_lot"))).head.getInt(0).toDouble;
val max_floors = 0//test.agg(max(test.col("floors"))).head.getDouble(0).toDouble;
val max_zipcode = 0//test.agg(max(test.col("zipcode"))).head.getInt(0).toDouble;

val testData = test.map(sale => LabeledPoint(sale.getAs[Integer]("price").toDouble, Vectors.dense(
  sale.getAs[Integer]("bedrooms").toDouble / max_bedrooms,
  sale.getAs[Double]("bathrooms").toDouble / max_bathrooms,
  sale.getAs[Integer]("sqft_living").toDouble / max_sqft_living,
  sale.getAs[Integer]("sqft_lot").toDouble / max_sqft_lot,
  sale.getAs[Double]("floors").toDouble / max_floors,
  sale.getAs[Integer]("zipcode").toDouble / max_zipcode
)))

testData.take(10)

val trainingData = test.map(sale => LabeledPoint(sale.getAs[Integer]("price").toDouble, Vectors.dense(
  sale.getAs[Integer]("bedrooms").toDouble / max_bedrooms,
  sale.getAs[Double]("bathrooms").toDouble / max_bathrooms,
  sale.getAs[Integer]("sqft_living").toDouble / max_sqft_living,
  sale.getAs[Integer]("sqft_lot").toDouble / max_sqft_lot,
  sale.getAs[Double]("floors").toDouble / max_floors,
  sale.getAs[Integer]("zipcode").toDouble / max_zipcode
)))

testData.take(10)

val numIterations = 10
val model = LinearRegressionWithSGD.train(trainingData, numIterations)

model.weights

val valuesAndPreds = testData.map { point =>
  val prediction = model.predict(point.features)
  (point.label, prediction)
}

valuesAndPreds.take(10)

val MSE = valuesAndPreds.map{case(v, p) => math.pow((v - p), 2)}.mean()
println("training Mean Squared Error = " + MSE)
