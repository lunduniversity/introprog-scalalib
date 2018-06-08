package introprog

/** PixelWindow events and application management. */
object PixelWindow {
  /** Immediately exit running application, close all windows, kills all threads. */
  def exit(): Unit = System.exit(0)

  /** Idle waiting for `millis` milliseconds. */
  def delay(millis: Long): Unit = Thread.sleep(millis)

  /** An object with strings describing events that can happen in a PixelWindow, see [[introprog.PixelWindow.Event]] */
  object Event {
    /** An integer representing a key down event.
    *
    * This value is returned by [[introprog.PixelWindow.lastEventType]] when
    * the last event was that a user pressed a key on the keyboard.
    * You can get a text describing the key by calling [[introprog.PixelWindow.lastKey]]
    */
    val KeyPressed    = 1

   /** An integer representing a key up event.
   *
   * This value is returned by [[introprog.PixelWindow.lastEventType]] when
   * the last event was that a user released a key on the keyboard.
   * You can get a text describing the key by calling [[introprog.PixelWindow.lastKey]]
   */
    val KeyReleased   = 2

    /** An integer representing a mouse button down event.
    *
    * This value is returned by [[introprog.PixelWindow.lastEventType]] when
    * the last event was that a user pressed a mouse button.
    * You can get the mouse position by calling [[introprog.PixelWindow.lastMousePos]]
    */
    val MousePressed  = 3

    /** An integer representing a mouse button up event.
    *
    * This value is returned by [[introprog.PixelWindow.lastEventType]] when
    * the last thing that happened was that a user released a mouse button.
    * You can get the mouse position by calling [[introprog.PixelWindow.lastMousePos]]
    */
    val MouseReleased = 4

    /** An integer representing that a window close event has happened.
    *
    * This value is returned by [[introprog.PixelWindow.lastEventType]] when
    * the last event was that a user has closed a window.
    */
    val WindowClosed  = 5

    /** An integer representing that no event is available.
      *
      * This value is returned by [[introprog.PixelWindow.lastEventType]] when
      * [[introprog.PixelWindow.awaitEvent]] has waited until its timeout
      * or if [[introprog.PixelWindow.lastEventType]] was called before awaiting any event.
      */
    val Undefined     = 0

    /** Returns a descriptive string with name of the `event` number */
    def show(event: Int): String = event match {
      case KeyPressed    => "KeyPressed"
      case KeyReleased   => "KeyReleased"
      case MousePressed  => "MousePressed"
      case MouseReleased => "MouseReleased"
      case WindowClosed  => "WindowClosed"
      case Undefined     => "Undefined"
      case _             =>
        throw new IllegalArgumentException(s"Unknown event number: $event")
    }
  }
}

/** A window with a canvas for pixel-based drawing.
  *
  * @constructor Create a new window for pixel-based drawing.
  * @param width the number of horizontal pixels of the drawing canvas inside the window
  * @param height number of vertical pixels of the drawing canvas inside the window
  * @param title the title of the window
  * @param background the background color filling the canvas and used when clearing pixels
  * @param foreground the foreground color used as default argument for color parameters
  */
class PixelWindow(
  val width: Int = 800,
  val height: Int = 640,
  val title: String = "PixelWindow",
  val background: java.awt.Color = java.awt.Color.black,
  val foreground: java.awt.Color = java.awt.Color.green
) {
  import PixelWindow.Event

  private val frame = new javax.swing.JFrame(title)
  private val canvas = new Swing.ImagePanel(width, height, background)

  private val queueCapacity = 1000
  private val eventQueue =
    new java.util.concurrent.LinkedBlockingQueue[java.awt.AWTEvent](queueCapacity)

  @volatile private var _lastEventType = Event.Undefined
  def lastEventType: Int = _lastEventType

  @volatile private var _lastKeyText = ""
  def lastKey: String = _lastKeyText

  @volatile private var _lastMousePos = (0, 0)
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
        case _ =>
          throw new IllegalArgumentException(s"Unknown MouseEvent: $e")
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
          case _ =>
            throw new IllegalArgumentException(s"Unknown KeyEvent: $e")
      }

    case we: java.awt.event.WindowEvent =>
      we.getID match {
        case java.awt.event.WindowEvent.WINDOW_CLOSING =>
          _lastEventType = Event.WindowClosed
        case _ =>
          throw new IllegalArgumentException(s"Unknown WindowEvent: $e")
      }
      case _ =>
        throw new IllegalArgumentException(s"Unknown AWTEvent: $e")
  }

  /** Wait for next event until `timeoutInMillis` and if time is out `lastEventType` is `Undefined`*/
  def awaitEvent(timeoutInMillis: Long): Unit = {
    val e = eventQueue.poll(timeoutInMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
    if (e != null) handleEvent(e) else _lastEventType = Event.Undefined
  }

  /** Draw a line from (`x1`, `y1`) to (`x2`, `y2`) using `color` and `lineWidth`. */
  def line(x1: Int, y1: Int, x2: Int, y2: Int, color: java.awt.Color = foreground, lineWidth: Int = 1): Unit =
    canvas.withGraphics { g =>
      import java.awt.BasicStroke
      val s = new BasicStroke(lineWidth.toFloat, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER)
      g.setStroke(s)
      g.setColor(color)
      g.drawLine(x1, y1, x2, y2)
    }

  def fill(x: Int, y: Int, width: Int, height: Int, color: java.awt.Color = foreground): Unit =
    canvas.withGraphics { g =>
      g.setColor(color)
      g.fillRect(x, y, width, height)
    }

  def setPixel(x: Int, y: Int, color: java.awt.Color = foreground): Unit =
    canvas.withImage { img =>
      img.setRGB(x, y, color.getRGB)
    }

  def clearPixel(x: Int, y: Int): Unit =
    canvas.withImage { img =>
      img.setRGB(x, y, background.getRGB)
    }

  def getPixel(x: Int, y: Int): java.awt.Color = Swing.await {
    new java.awt.Color(canvas.img.getRGB(x, y))
  }

  def open(): Unit = Swing { frame.setVisible(true) }

  def close(): Unit = Swing { frame.setVisible(false); frame.dispose() }

  def clear(): Unit = canvas.withGraphics { g =>
    g.setColor(background)
    g.fillRect(0, 0, width, height)
  }

  def drawText(
    text: String,
    x: Int,
    y: Int,
    color: java.awt.Color = foreground,
    size: Int = 16,
    style: Int = java.awt.Font.BOLD,
    fontName: String = java.awt.Font.MONOSPACED
  ) = {
    canvas.withGraphics { g =>
      import java.awt.RenderingHints._
      // https://docs.oracle.com/javase/tutorial/2d/text/renderinghints.html
      g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON)
      //val f = g.getFont
      g.setFont(new java.awt.Font(fontName /*f.getName*/, style, size))
      g.setColor(color)
      g.drawString(text, x, y + size)
    }
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
