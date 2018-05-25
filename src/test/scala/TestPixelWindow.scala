object TestPixelWindow {
  import introprog._

  val w = new PixelWindow(400, 300, "Hello PixelWindow!")

  def square(topLeft: (Int, Int))(side: Int): Unit = {
    w.moveTo( topLeft._1,        topLeft._2        )
    w.lineTo( topLeft._1 + side, topLeft._2        )
    w.lineTo( topLeft._1 + side, topLeft._2 + side )
    w.lineTo( topLeft._1,        topLeft._2 + side )
    w.lineTo( topLeft._1,        topLeft._2        )
  }

  def main(args: Array[String]): Unit = {
    println("Drawing a red square in a PixelWindow.")
    println("Press F11 for full screen. Close window to exit.")
    w.lineWidth = 2
    w.color = java.awt.Color.red
    square(200, 100)(50)
    while (w.lastEventType != PixelWindow.Event.WindowClosed) {
      w.awaitEvent(10)  // wait for next event for max 10 milliseconds
      //println(s"lastEventType == " + w.lastEventType)
      w.lastEventType match {
        case PixelWindow.Event.KeyPressed    => println("lastKey == " + w.lastKey)
        case PixelWindow.Event.KeyReleased   => println("lastKey == " + w.lastKey)
        case PixelWindow.Event.MousePressed  => println("lastMousePos == " + w.lastMousePos)
        case PixelWindow.Event.MouseReleased => println("lastMousePos == " + w.lastMousePos)
        case PixelWindow.Event.WindowClosed  => println("Goodbye!")
        case _ =>
      }
      if (w.lastEventType == PixelWindow.Event.KeyPressed && w.lastKey == "F11")
        Swing.Screen.toggleFullScreen(w.frame)
      PixelWindow.delay(10) // wait for 1 second
    }
  }
}
