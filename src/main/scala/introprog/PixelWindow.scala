package introprog

/** PixelWindow events and application management. */
object PixelWindow {
  /** Immediately exit running application, close all windows, kills all threads. */
  def exit(): Unit = System.exit(0)

  /** Idle waiting for `millis` milliseconds. */
  def delay(millis: Long): Unit = Thread.sleep(millis)

  /** An object with strings describing events that can happen in a PixelWindow, see [[introprog.PixelWindow.Event]] */
  object Event {
    /** A key down event. */
    val KeyPressed    = "KeyPressed"

   /** A key up event. */
    val KeyReleased   = "KeyReleased"

    /** A left mouse button down event. */
    val MousePressed  = "MousePressed"

    /** A left mouse button up event. */
    val MouseReleased = "MouseReleased"

    /** A window close event. */
    val WindowClosed  = "WindowClosed"

    /** No event is available.
      *
      * This value is used when timeout is reached in [[introprog.PixelWindow.awaitEvent]]
      * or when [[introprog.PixelWindow.lastEventType]] is called before awaiting any event.
      * The value of the string is `"Undefined"`.
      */
    val Undefined     = "Undefined"
  }
}

/** A window with a canvas for pixel-based drawing. Example usage:
  {{{
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
      println("Drawing a red square in a PixelWindow. Close window to exit.")
      w.lineWidth = 2
      w.color = java.awt.Color.red
      square(200, 100)(50)
      while (w.lastEventType != PixelWindow.Event.WindowClosed) {
        w.awaitEvent(10)  // wait for next event for max 10 milliseconds
        println(s"lastEventType == " + w.lastEventType)
        w.lastEventType match {
          case PixelWindow.Event.KeyPressed    => println("lastKey == " + w.lastKey)
          case PixelWindow.Event.KeyReleased   => println("lastKey == " + w.lastKey)
          case PixelWindow.Event.MousePressed  => println("lastMousePos == " + w.lastMousePos)
          case PixelWindow.Event.MouseReleased => println("lastMousePos == " + w.lastMousePos)
          case PixelWindow.Event.WindowClosed  => println("Goodbye!")
          case _ =>
        }
        PixelWindow.delay(1000) // wait for 1 second
      }
    }
  }
  }}}
  *
  * @constructor Create a new window for pixel-based drawing.
  * @param width the number of horizontal pixels of the drawing canvas inside the window
  * @param height number of vertical pixels of the drawing canvas inside the window
  * @param title the title of the window
  * @param background the backgrouund color of the window; default is black
  */
class PixelWindow(
  val width: Int = 800,
  val height: Int = 640,
  val title: String = "PixelWindow",
) {
  import PixelWindow.Event

  /** The horizontal starting position used by `lineTo`. Higher values are more to the right. */
  var x = 0

  /** The vertical starting position used by `lineTo`. Higher values are further downwards.*/
  var y = 0

  var lineWidth: Int = 1
  var textSize: Int = 20
  var background: java.awt.Color = java.awt.Color.BLACK
  var color: java.awt.Color = java.awt.Color.WHITE

  private val frame = new javax.swing.JFrame(title)
  private val canvas = new Swing.ImagePanel(width, height, background)

  private val queueCapacity = 1000
  private val eventQueue =
    new java.util.concurrent.LinkedBlockingQueue[java.awt.AWTEvent](queueCapacity)

  private var _lastEventType = Event.Undefined
  def lastEventType: String = _lastEventType

  protected var _lastKeyText = ""
  def lastKey: String = _lastKeyText

  protected var _lastMousePos = (0, 0)
  def lastMousePos: (Int, Int) = _lastMousePos

  initFrame()

  private def handleEvent(e: java.awt.AWTEvent): Unit = e match {
    case me: java.awt.event.MouseEvent =>
      _lastMousePos = (me.getX, me.getY)
      me.getID match {
        case java.awt.event.MouseEvent.MOUSE_PRESSED =>
          _lastEventType = Event.MousePressed
        case java.awt.event.MouseEvent.MOUSE_RELEASED =>
            _lastEventType = Event.MouseReleased
        case _ => println(s"Unknown mouse event: $e")
      }

    case ke: java.awt.event.KeyEvent =>
      if (ke.getKeyChar == java.awt.event.KeyEvent.CHAR_UNDEFINED || ke.getKeyChar < ' ')
        _lastKeyText = java.awt.event.KeyEvent.getKeyText(ke.getKeyCode)
      else _lastKeyText = ke.getKeyChar.toString

      ke.getID match {
        case java.awt.event.KeyEvent.KEY_PRESSED =>
          _lastEventType = Event.KeyPressed
        case java.awt.event.KeyEvent.KEY_RELEASED =>
          _lastEventType = Event.KeyReleased
        case _ => println(s"Unknown key event: $e")
      }

    case we: java.awt.event.WindowEvent =>
      we.getID match {
        case java.awt.event.WindowEvent.WINDOW_CLOSING =>
          _lastEventType = Event.WindowClosed
        case _ => println(s"Unknown window event: $e")
      }
    case _ => println(s"Unknown event: $e")
  }

  /** Wait for next event until `timeoutInMillis` and if time is out `lastEventType` is `Undefined`*/
  def awaitEvent(timeoutInMillis: Long): Unit = {
    val e = eventQueue.poll(timeoutInMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
    if (e != null) handleEvent(e) else _lastEventType = Event.Undefined
  }

  def moveTo(newX: Int, newY: Int): Unit = {
    x = newX
    y = newY
  }

  /** Draw a line from (`x`, `y`) to (`newX`, `newY`). */
  def lineTo(newX: Int, newY: Int): Unit = canvas.withGraphics { g =>
    import java.awt.BasicStroke
    val s = new BasicStroke(lineWidth.toFloat, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER)
    g.setStroke(s)
    g.setColor(color)
    g.drawLine(x, y, newX, newY)
    x = newX
    y = newY
  }

  def setPixel(x: Int, y: Int): Unit = canvas.withImage { img =>
    img.setRGB(x, y, color.getRGB)
  }

  def clearPixel(x: Int, y: Int): Unit = canvas.withImage { img =>
    img.setRGB(x, y, background.getRGB)
  }

  def getPixel(x: Int, y: Int): java.awt.Color =
    new java.awt.Color(canvas.img.getRGB(x, y))

  def open(): Unit = frame.setVisible(true)

  def close(): Unit = { frame.setVisible(false); frame.dispose() }

  def clear(): Unit = canvas.withGraphics { g =>
    g.setColor(background)
    g.fillRect(0, 0, width, height)
  }

  def drawText(text: String) = canvas.withGraphics { g =>
    import java.awt.RenderingHints._
    // https://docs.oracle.com/javase/tutorial/2d/text/renderinghints.html
    g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON)
    val f = g.getFont
    g.setFont(new java.awt.Font(f.getName, f.getStyle, textSize))
    g.setColor(color)
    g.drawString(text, x, y)
  }

  private def initFrame(): Unit = {
    Swing.init() // first time calls setPlatformSpecificLookAndFeel
    javax.swing.JFrame.setDefaultLookAndFeelDecorated(true)

    frame.addWindowListener(new java.awt.event.WindowAdapter {
      override def windowClosing(e: java.awt.event.WindowEvent): Unit = {
        frame.setVisible(false)
        frame.dispose()
        eventQueue.offer(e)
      }
    })

    frame.addKeyListener(new java.awt.event.KeyAdapter {
      override def keyPressed(e: java.awt.event.KeyEvent): Unit = eventQueue.offer(e)
      override def keyReleased(e: java.awt.event.KeyEvent): Unit = eventQueue.offer(e)
    })

    canvas.addMouseListener(new java.awt.event.MouseAdapter {
      override def mousePressed(e: java.awt.event.MouseEvent): Unit = eventQueue.offer(e)
      override def mouseReleased(e: java.awt.event.MouseEvent): Unit = eventQueue.offer(e)
    })

    val box = new javax.swing.Box(javax.swing.BoxLayout.Y_AXIS)
    box.setBackground(java.awt.Color.RED)
    box.add(javax.swing.Box.createVerticalGlue())
    box.add(canvas)
    box.add(javax.swing.Box.createVerticalGlue())
    frame.add(box)

    frame.getContentPane().setBackground(java.awt.Color.BLACK.brighter.brighter)
    frame.pack()
    frame.setVisible(true)
  }


}
