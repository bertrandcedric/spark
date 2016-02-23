import org.apache.spark.{SparkConf, SparkContext}

val conf = new SparkConf().setAppName("test").setMaster("local[*]")
val sc = new SparkContext(conf)
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
val df = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load("src/main/resources/data/home_data.csv").cache()

df.printSchema()
