package introprog

/** PixelWindow events and application management. */
object PixelWindow {
  /** Immediately exit running application, close all windows, kills all threads. */
  def exit(): Unit = System.exit(0)

  /** Idle waiting for `millis` milliseconds. */
  def delay(millis: Long): Unit = Thread.sleep(millis)

  /** An object with strings describing events that can happen in a PixelWindow, see [[introprog.PixelWindow.Event]] */
  object Event {
    /** A string representing a key down event.
    *
    * This value is returned by [[introprog.PixelWindow.lastEventType]] when
    * the last event was that a user pressed a key on the keyboard.
    * You can get a text describing the key by calling [[introprog.PixelWindow.lastKey]]
    * The value of the string is `"KeyPressed"`.
    */
    val KeyPressed    = "KeyPressed"

   /** A string representing a key up event.
   *
   * This value is returned by [[introprog.PixelWindow.lastEventType]] when
   * the last event was that a user released a key on the keyboard.
   * You can get a text describing the key by calling [[introprog.PixelWindow.lastKey]]
   * The value of the string is `"KeyReleased"`.
   */
    val KeyReleased   = "KeyReleased"

    /** A string representing a mouse button down event.
    *
    * This value is returned by [[introprog.PixelWindow.lastEventType]] when
    * the last event was that a user pressed a mouse button.
    * You can get the mouse position by calling [[introprog.PixelWindow.lastMousePos]]
    * The value of the string is `"MousePressed"`.
    */
    val MousePressed  = "MousePressed"

    /** A string representing a mouse button up event.
    *
    * This value is returned by [[introprog.PixelWindow.lastEventType]] when
    * the last thing that happened was that a user released a mouse button.
    * You can get the mouse position by calling [[introprog.PixelWindow.lastMousePos]]
    * The value of the string is `"MouseReleased"`.
    */
    val MouseReleased = "MouseReleased"

    /** A string representing that a window close event has happened.
    *
    * This value is returned by [[introprog.PixelWindow.lastEventType]] when
    * the last event was that a user has closed a window.
    * The value of the string is `"WindowClosed"`.
    */
    val WindowClosed  = "WindowClosed"

    /** A string representing that no event is available.
      *
      * This value is returned by [[introprog.PixelWindow.lastEventType]] when
      * [[introprog.PixelWindow.awaitEvent]] has waited until its timeout
      * or if [[introprog.PixelWindow.lastEventType]] was called before awaiting any event.
      * The value of the string is `"Undefined"`.
      */
    val Undefined     = "Undefined"
  }
}

/** A window with a canvas for pixel-based drawing.
  *
  * @constructor Create a new window for pixel-based drawing.
  * @param width the number of horizontal pixels of the drawing canvas inside the window
  * @param height number of vertical pixels of the drawing canvas inside the window
  * @param title the title of the window
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

  /*private*/ val frame = new javax.swing.JFrame(title)
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
    new java.awt.Color(canvas.img.getRGB(x, y))  // TODO: is not thread safe???

  def open(): Unit = Swing { frame.setVisible(true) }

  def close(): Unit = Swing { frame.setVisible(false); frame.dispose() }

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

  private def initFrame(): Unit = Swing {
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
    box.add(javax.swing.Box.createVerticalGlue())
    box.add(canvas)
    box.add(javax.swing.Box.createVerticalGlue())
    frame.add(box)

    val outsideOfCanvasColorShownIfResized = java.awt.Color.BLACK.brighter.brighter
    frame.getContentPane().setBackground(outsideOfCanvasColorShownIfResized)
    frame.pack()
    frame.setVisible(true)
  }

}
