package datastructures.stack

import scala.collection.generic.{CanCombineFrom, GenericParTemplate}
import scala.collection.immutable.VectorBuilder
import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.{Combiner, ParSeqLike, SeqSplitter, immutable}

//A Last In First Out (LIFO) collection implemented as a linked list.
// T is the type of item contained in the stack

class ParStack[T] (val elements: List[T]) extends immutable.ParSeq[T]
  with GenericParTemplate[T, ParStack]
  with ParSeqLike[T, ParStack[T], scala.collection.immutable.Seq[T]] {

  override def head = elements.head
  override def tail = new ParStack(elements.tail)
  override def size = elements.size

  override def apply(i: Int): T = elements(i)

  def splitter: SeqSplitter[T] = new ParStackSplitter[T](elements, 0, size)

  override def length: Int = elements.size

  def seq: Seq[T] = elements

  protected[this] override def newCombiner: Combiner[T, ParStack[T]] = new LazyParStackCombiner[T]

  def push(elem: T) = new ParStack(elem :: elements)
  def top: T =
    if (elements.nonEmpty) elements.head
    else throw new NoSuchElementException("top of empty stack")

  def pop(): ParStack[T] =
    if (elements.nonEmpty) new ParStack(elements.tail)
    else throw new NoSuchElementException("pop of empty stack")

}

object ParStack {

  implicit def canBuildFrom[T]: CanCombineFrom[ParStack[T], T, ParStack[T]] =
    new CanCombineFrom[ParStack[T], T, ParStack[T]] {
      def apply(from: ParStack[T]) = newCombiner
      def apply() = newCombiner
    }

  def newBuilder[T]: Combiner[T, ParStack[T]] = newCombiner
  def newCombiner[T]: Combiner[T, ParStack[T]] = new LazyParStackCombiner[T]

}

class ParStackSplitter[T](private var lst: List[T], private var i: Int, private val ntl: Int) extends SeqSplitter[T] {
  final def hasNext = i < ntl

  final def next = {
    val r = lst(i)
    i += 1
    r
  }

  override def dup: SeqSplitter[T] = new ParStackSplitter[T](lst, i, ntl)

  override def split: Seq[SeqSplitter[T]] = {
    val rem = remaining
    if (rem >= 2) psplit(rem / 2, rem - rem / 2)
    else Seq(this)
  }

  override def psplit(sizes: Int*): Seq[ParStackSplitter[T]] = {
    val splitted = new ArrayBuffer[ParStackSplitter[T]]
    for (sz <- sizes) {
      val next = (i + sz) min ntl
      splitted += new ParStackSplitter(lst, i, next)
      i = next
    }
    if (remaining > 0) splitted += new ParStackSplitter(lst, i, ntl)
    splitted
  }

  override def remaining: Int = ntl - i
}

class LazyParStackCombiner[T] extends Combiner[T, ParStack[T]] {
  var sz = 0
  val vectors = new ArrayBuffer[VectorBuilder[T]] += new VectorBuilder[T]

  def size: Int = sz

  def +=(elem: T): this.type = {
    vectors.last += elem
    sz += 1
    this
  }

  def clear() = {
    vectors.clear()
    vectors += new VectorBuilder[T]
    sz = 0
  }

  def result: ParStack[T] = {
    val rvb = new VectorBuilder[T]
    for (vb <- vectors) {
      rvb ++= vb.result
    }
    new ParStack(rvb.result.toList)
  }

  def combine[U <: T, NewTo >: ParStack[T]](other: Combiner[U, NewTo]) = if (other eq this) this else {
    val that = other.asInstanceOf[LazyParStackCombiner[T]]
    sz += that.sz
    vectors ++= that.vectors
    this
  }
}


object CheckParStack extends App {
  val s = new ParStack(List(1,2,3))
  val k = s.push(5)
  println(k)
}

