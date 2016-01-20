javaOptions in run += "-Xmx1024M"

scalaVersion := "2.11.7"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "1.6.0"

libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "1.6.0"
