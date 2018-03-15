import org.scalatest.{FlatSpec, Matchers}
import stack.immutable.TSStack

class ImmutableStackSpec  extends FlatSpec with Matchers  {

  var stack = TSStack[Int](List(3,2,1))

}
