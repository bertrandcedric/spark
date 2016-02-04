List(1,2,3).filter(x => x >= 2)

List((1,2), (1,2), (2,1), (1,1), (1,1), (1,1), (1,1)).filter(x => x._1 != x._2).groupBy(l => l).map(t => (t._1, t._2.length))
