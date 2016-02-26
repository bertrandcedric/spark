import org.apache.spark.{SparkConf, SparkContext}

val conf = new SparkConf().setAppName("test").setMaster("local[*]")
val sc = new SparkContext(conf)
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
val sales = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load("src/main/resources/data/home_data.csv").cache()

val splits = sales.randomSplit(Array(.8, .2), 0)
val training = sales
val test = splits(1)

test.take(1)
