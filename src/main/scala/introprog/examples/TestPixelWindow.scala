package introprog.examples

/** Example of a simple PixelWindow app with an event loop that inspects key typing
  * and mouse clicking by the user. See source code for inspiration on how to use
  * PixelWindow for easy 2D game programming.
  */
object TestPixelWindow {
  import introprog._

  /** A reference to an instance of class `PixelWindow`. */
  val w = new PixelWindow(400, 300, "Hello PixelWindow!")

  /** Draw a square starting in `topLeft` with size `side`. */
  def square(topLeft: (Int, Int))(side: Int): Unit = {
    w.moveTo( topLeft._1,        topLeft._2        )
    w.lineTo( topLeft._1 + side, topLeft._2        )
    w.lineTo( topLeft._1 + side, topLeft._2 + side )
    w.lineTo( topLeft._1,        topLeft._2 + side )
    w.lineTo( topLeft._1,        topLeft._2        )
  }

  /** Draw a red square and start an event loop that prints events in terminal. */
  def main(args: Array[String]): Unit = {
    println("Drawing a red square in a PixelWindow. Close window to exit.")
    w.lineWidth = 3
    w.color = java.awt.Color.red
    square(200, 100)(50)

    while (w.lastEventType != PixelWindow.Event.WindowClosed) {
      w.awaitEvent(10)  // wait for next event for max 10 milliseconds

      if (w.lastEventType != PixelWindow.Event.Undefined)
        println(s"lastEventType == " + w.lastEventType)

      w.lastEventType match {
        case PixelWindow.Event.KeyPressed    => println("lastKey == " + w.lastKey)
        case PixelWindow.Event.KeyReleased   => println("lastKey == " + w.lastKey)
        case PixelWindow.Event.MousePressed  => println("lastMousePos == " + w.lastMousePos)
        case PixelWindow.Event.MouseReleased => println("lastMousePos == " + w.lastMousePos)
        case PixelWindow.Event.WindowClosed  => println("Goodbye!")
        case _ =>
      }

      PixelWindow.delay(100) // wait for 0.1 seconds
    }
  }
}
