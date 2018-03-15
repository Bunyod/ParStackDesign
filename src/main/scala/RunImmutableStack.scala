import stack.immutable.TSStack

import scala.util.Random

object RunImmutableStack extends App {

  var stack = TSStack[Int](List(3,2,1))

  // push from another thread
  new Thread() {
    override def run(): Unit = {
      val random = new Random()
      (1 to 10).foreach { _ =>
        stack = stack.push(random.nextInt(100))
      }
    }
  }.start()

  Thread.sleep(50)

  // pull from another thread
  new Thread() {
    override def run(): Unit = {
      (1 to 13).foreach { _ =>
        val (item, current) = stack.pop()
        stack = current
        println(item)
      }
    }
  }.start()

}
