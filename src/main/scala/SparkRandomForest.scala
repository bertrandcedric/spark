import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkRandomForest {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("test").setMaster("local")
    val sc: SparkContext = new SparkContext(conf)

    val data: Array[Int] = Array(1, 2, 3, 4, 5)
    val distData: RDD[Int] = sc.parallelize(data)

    val filter: RDD[Int] = distData.filter(x => x > 3)

    filter.foreach(x => println(x))
  }
}
