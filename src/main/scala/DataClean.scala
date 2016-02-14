import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by fs24818n on 03/02/2016.
  */
object DataClean extends App {

  // Init spark context
  val conf = new SparkConf().setAppName("test").setMaster("local[*]")
  val sc = new SparkContext(conf)

  val sqlContext = new org.apache.spark.sql.SQLContext(sc)

  val df = sqlContext.read
    .format("com.databricks.spark.csv")
    .option("header", "false") // Use first line of all files as header
    .option("inferSchema", "true") // Automatically infer data types
    .load("data/train.csv")

  //df.columns.foreach(println)

  df.col("C1")



}
