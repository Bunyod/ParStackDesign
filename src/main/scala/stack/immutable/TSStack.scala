package stack.immutable

case class TSStack[T](elements: List[T]) {

  def push(item: T): TSStack[T] = {
    TSStack[T](item :: elements)
  }

  def pop(): (T, TSStack[T]) = {
    (elements.head, TSStack(elements.tail))
  }

  def peek(): T = {
    if (!isEmpty) elements.head
    else throw new NoSuchElementException("top of empty stack")
  }

  def isEmpty: Boolean = elements.isEmpty

}
