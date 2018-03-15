package stack.threadsafe

class TSStack[T] {

  private var elements = List.empty[T]
  def push(item: T): Unit = this.synchronized {
    elements = item :: elements
  }

  def pop(): T = this.synchronized {
    if (!isEmpty) {
      val res = elements.head
      elements = elements.tail
      res
    } else throw new NoSuchElementException("top of empty stack")
  }

  def peek(): T = {
    if (!isEmpty) elements.head
    else throw new NoSuchElementException("top of empty stack")
  }

  def isEmpty: Boolean = elements.isEmpty

}

//object TSJavaStack {
//
//}
