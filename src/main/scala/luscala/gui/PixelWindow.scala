object PixelWindow {
  def systemExit(): Unit = System.exit(0)

  def delay(millis: Long): Unit = Thread.sleep(millis)

  object Event {
    val KeyPressed    = "KeyPressed"
    val KeyReleased   = "KeyReleased"
    val MousePressed  = "MousePressed"
    val MouseReleased = "MouseReleased"
    val WindowClosed  = "WindowClosed"

    /** Used to indicate that no event is available */
    val Undefined     = "Undefined"
  }
}

class PixelWindow {
  private lazy val frame = ???

  private class UnderlyingCanvas(size: (Int, Int)) extends javax.swing.JPanel {

  }

}
