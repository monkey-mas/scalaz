package scalaz

import org.scalacheck.Prop.forAll
import scala.util.Random

object MapBench extends SpecLite {
  import org.scalacheck.Arbitrary
  import scalaz.scalacheck.ScalazProperties._
  import scalaz.scalacheck.ScalazArbitrary._
  import std.anyVal._
  import std.list._
  import std.string._
  import std.option._
  import std.tuple._
  import syntax.std.option._

  import ==>>._

  "==>> insertion" should {
  //   "insert benchmark" in {
  //     val l1 = (1 to 10000) toVector
  //     val l2 = (1 to 100000) toVector
  //     val l3 = (1 to 1000000) toVector
  //     val testCases = List(l1, l2, l3)

  //     def bench(v: Vector[Int]): Long = {
  //       val results = scala.collection.mutable.ArrayBuffer.empty[Long]
  //       val N = 200
  //       for(i <- 1 to N) {
  //         val now = System.nanoTime
  //         var m: Int ==>> Int = empty
  //         for (i <- v) {
  //           m = m.insert(i, i)
  //         }
  //         val result = (System.nanoTime - now)
  //         results += result
  //       }
  //       results.toArray.sum / N
  //     }

  //     for (c <- testCases) {
  //       val result = bench(c)
  //       println(s"""testcase: ${c.size} -> $result""")
  //     }
  //   }
  // }

    "insert benchmark" in {
      val l1 = util.Random.shuffle((1 to 10000)) toVector
      val l2 = util.Random.shuffle((1 to 100000)) toVector
      val l3 = util.Random.shuffle((1 to 1000000)) toVector
      val testCases = List(l1, l2, l3)

      def bench(v: Vector[Int]): Long = {
        val results = scala.collection.mutable.ArrayBuffer.empty[Long]
        val N = 200
        for(i <- 1 to N) {
          val now = System.nanoTime
          var m: Int ==>> Int = empty
          for (i <- v) {
            m = m.insert(i, i)
          }
          val result = (System.nanoTime - now)
          results += result
        }
        results.toArray.sum / N
      }

      for (c <- testCases) {
        val result = bench(c)
        println(s"""testcase: ${c.size} -> $result""")
      }
    }
  }


}