package scalaz

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.{State => JmhState} // Name collision with State(scalaz)???

import scala.util.Random

import std.anyVal._

object MapBenchmark {
  val oneMillion = 1000000
  val seed = 123456789L

  @JmhState(Scope.Benchmark)
  class BenchmarkState {
    val allVec = new Random(seed).shuffle((1 to oneMillion)).toVector.map(i => (i, i))
    val ascVec = (1 to oneMillion).toVector.map(i => (i, i))
    val dscVec = ascVec.reverse

    val all = ==>>.fromList(allVec.toList)
  }

  /**
   * Sample data
   * 500,000 odd numbers  (1 to 999,999)
   * 500,000 even numbers (2 to 1,000,000)
   */
  @JmhState(Scope.Benchmark)
  class BenchmarkState1M_E_O {
    val v = (1 to oneMillion).map(i => (i, i))
    val vl = v.toList
    val ol = v.filter(kv => kv._1%2 == 1).toList
    val el = v.filter(kv => kv._1%2 == 0).toList

    val all = ==>>.fromList(vl)
    val o = ==>>.fromList(ol)
    val e = ==>>.fromList(el)
  }

  @JmhState(Scope.Benchmark)
  class BenchmarkState1M_Rand {
    val allVec = new Random(seed).shuffle((1 to oneMillion)).toVector.map(i => (i, i))
    val aVec = allVec.take(oneMillion / 2)
    val bVec = allVec.drop(oneMillion / 2)

    val allL = allVec.toList
    val aL = aVec.toList
    val bL = bVec.toList

    val all = ==>>.fromList(allL)
    val a = ==>>.fromList(aL)
    val b = ==>>.fromList(bL)
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
  def delete1M_Asc(state: BenchmarkState): Unit = {
    var m = state.all
    for (kv <- state.ascVec) {
      m = m.delete(kv._1)
    }
  }

  @Benchmark
  def delete1M_Dsc(state: BenchmarkState): Unit = {
    var m = state.all
    for (kv <- state.dscVec) {
      m = m.delete(kv._1)
    }
  }

  @Benchmark
  def delete1M_Rand(state: BenchmarkState1M_Rand): Unit = {
    var m = state.all
    for (kv <- state.allVec) {
      m = m.delete(kv._1)
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
  def difference1M_Rand(state: BenchmarkState1M_Rand): Unit = {
    var all = state.all
    all = all difference (state.b)
  }

  @Benchmark
  def difference1M_Rand2(state: BenchmarkState1M_Rand): Unit = {
    var all = state.all
    all = all difference (state.a)
  }

  @Benchmark
  def intersection1M_E_O(state: BenchmarkState1M_E_O): Unit = {
    var all = state.all
    all = all intersection (state.e)
  }

  @Benchmark
  def intersection1M_Rand(state: BenchmarkState1M_Rand): Unit = {
    var all = state.all
    all = all intersection (state.a)
  }

  @Benchmark
  def intersection1M_Rand2(state: BenchmarkState1M_Rand): Unit = {
    var all = state.all
    all = all intersection (state.b)
  }
}
