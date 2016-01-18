import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("test").setMaster("local")
    val sc = new SparkContext(conf)

    val data = Array(1, 2, 3, 4, 5)
    val distData = sc.parallelize(data)

    val filter: RDD[Int] = distData.filter(x => x > 3)

    filter.foreach(x => println(x))
  }
}
