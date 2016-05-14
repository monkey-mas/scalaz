package scalaz
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.{State => AtState} // Name clash with State(scalaz)???
import scala.collection._
import scala.util.Random

import scalaz.==>>
import std.anyVal._

object MapBenchmark {
  val oneMillion     = 1000000
  val tenMillion     = 10000000

  /**
   * Smple Data:
   *   500,000 odd numbers  (1, 3, ..., 999,999)
   *   500,000 even numbers (2, 4, ..., 1,000,000)
   */
  // val l_1M = (1 to oneMillion).map(i => (i, i))
  // val o_1M = ==>>.fromList(l_1M.filter(kv => kv._1%2 == 1).toList)
  // val e_1M = ==>>.fromList(l_1M.filter(kv => kv._1%2 == 0).toList)
  // val a_1M = ==>>.fromList(l_1M.toList)

  /**
   * Smple Data:
   *   5,000,000 odd numbers  (1, 3, ..., 9,999,999)
   *   5,000,000 even numbers (2, 4, ..., 10,000,000)
   */
  // val l_10M = (1 to tenMillion).map(i => (i, i))
  // val o_10M = ==>>.fromList(l_10M.filter(kv => kv._1%2 == 1).toList)
  // val e_10M = ==>>.fromList(l_10M.filter(kv => kv._1%2 == 0).toList)
  // val a_10M = ==>>.fromList(l_10M.toList)

  /**
   * Smple Data:
   *   50,000,000 odd numbers  (1, 3, ..., 99,999,999)
   *   50,000,000 even numbers (2, 4, ..., 100,000,000)
   */
  // val l_100M = (1 to hundredMillion).map(i => (i, i))
  // val o_100M = ==>>.fromList(l_100M.filter(kv => kv._1%2 == 1).toList)
  // val e_100M = ==>>.fromList(l_100M.filter(kv => kv._1%2 == 0).toList)
  // val a_100M = ==>>.fromList(l_100M.toList)

  // class BenchmarkState {
  //   val li = Random.shuffle((1 to 1000000)).toVector
  // }

  // @AtState(Scope.Benchmark)
  // class BenchmarkStateDel {
  //   val numElems = 1000000
  //   val vec = Random.shuffle(1 to numElems).toVector
  //   val li = (1 to numElems).map(i => (i, i)).toList
  //   val m = ==>>.fromList(li)
  // }

  @AtState(Scope.Benchmark)
  class BenchmarkState {
    val ascVec = (1 to oneMillion).toVector.map(i => (i, i))
    val dscVec = ascVec.reverse

    val asc = ==>>.fromList(ascVec.toList)
    val dsc = ==>>.fromList(dscVec.toList)
  }

  /**
   * Test data: union
   * 500,000 odd numbers  (1 to 999,999)
   * 500,000 even numbers (2 to 1,000,000)
   */
  @AtState(Scope.Benchmark)
  class BenchmarkState1M_E_O {
    val v = (1 to oneMillion).map(i => (i, i))
    val all = ==>>.fromList(v.toList)
    val o = ==>>.fromList(v.filter(kv => kv._1%2 == 1).toList)
    val e = ==>>.fromList(v.filter(kv => kv._1%2 == 0).toList)
  }

  @AtState(Scope.Benchmark)
  class BenchmarkState10M_E_O {
    val v = (1 to tenMillion).map(i => (i, i))
    val all = ==>>.fromList(v.toList)
    val o = ==>>.fromList(v.filter(kv => kv._1%2 == 1).toList)
    val e = ==>>.fromList(v.filter(kv => kv._1%2 == 0).toList)
  }

  @AtState(Scope.Benchmark)
  class BenchmarkState1M_Rand {
    val allVec = Random.shuffle((1 to oneMillion)).toVector.map(i => (i, i))
    val aVec = allVec.take(oneMillion / 2)
    val bVec = allVec.drop(oneMillion / 2)

    val all = ==>>.fromList(allVec.toList)
    val a = ==>>.fromList(aVec.toList)
    val b = ==>>.fromList(bVec.toList)
  }

  @AtState(Scope.Benchmark)
  class BenchmarkState10M_Rand {
    val allVec = Random.shuffle((1 to tenMillion)).toVector.map(i => (i, i))
    val aVec = allVec.take(tenMillion / 2)
    val bVec = allVec.drop(tenMillion / 2)

    val all = ==>>.fromList(allVec.toList)
    val a = ==>>.fromList(aVec.toList)
    val b = ==>>.fromList(bVec.toList)
  }
}

class MapBenchmark {
  import MapBenchmark._

  @Benchmark
  def insert1M_Asc(state: BenchmarkState): Unit = {
    var m: Int ==>> Int = ==>>.empty
    for (kv <- state.ascVec) {
      m = m.insert(kv._1, kv._2)
    }
  }

  @Benchmark
  def insert1M_Dsc(state: BenchmarkState): Unit = {
    var m: Int ==>> Int = ==>>.empty
    for (kv <- state.dscVec) {
      m = m.insert(kv._1, kv._2)
    }
  }

  @Benchmark
  def insert1M_Rand(state: BenchmarkState1M_Rand): Unit = {
    var m: Int ==>> Int = ==>>.empty
    for (kv <- state.allVec) {
      m = m.insert(kv._1, kv._2)
    }
  }

  @Benchmark
  def deleteJmh(state: BenchmarkStateDel): Unit = {
    var md = state.m
    for (i <- state.vec) {
      md = md.delete(i)
    }
  }

  @Benchmark
  def union1M_E_O(state: BenchmarkState1M_E_O): Unit = {
    var o = state.o
    o = o union (state.e)
  }

  @Benchmark
  def union1M_Rand(state: BenchmarkState1M_Rand): Unit = {
    var a = state.a
    a = a union (state.b)
  }

  @Benchmark
  def union1M_Rand2(state: BenchmarkState1M_Rand): Unit = {
    var b = state.b
    b = b union (state.a)
  }

  @Benchmark
  def difference1M_E_O(state: BenchmarkState1M_E_O): Unit = {
    var all = state.all
    all = all difference (state.e)
  }

  @Benchmark
  def difference1M_RandA(state: BenchmarkState1M_Rand): Unit = {
    var all = state.all
    all = all difference (state.a)
  }

  @Benchmark
  def difference1M_RandB(state: BenchmarkState1M_Rand): Unit = {
    var all = state.all
    all = all difference (state.b)
  }

  @Benchmark
  def intersection1M_E_O(state: BenchmarkState1M_E_All): Unit = {
    var all = state.all
    all = all intersection (state.e)
  }

  @Benchmark
  def intersection1M_RandA(state: BenchmarkState1M_Rand): Unit = {
    var all = state.all
    all = all intersection (state.a)
  }

  @Benchmark
  def intersection1M_RandB(state: BenchmarkState1M_Rand): Unit = {
    var all = state.all
    all = all intersection (state.b)
  }
}
