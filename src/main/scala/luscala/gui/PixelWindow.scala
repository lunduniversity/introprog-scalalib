package luscala.gui

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

class PixelWindow(
  val width: Int = 800,
  val height: Int = 640,
  val title: String = "PixelWindow",
  val background: java.awt.Color = java.awt.Color.BLACK
) {
  import PixelWindow.Event
  var x = 0
  var y = 0
  var lineWidth: Int = 1
  var textSize: Int = 20
  var color: java.awt.Color = Swing.invertColor(background)

  /*private*/ val frame = new javax.swing.JFrame(title)
  /*private*/ val canvas = new Swing.ImagePanel(width, height, background)

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

  def awaitEvent(timeoutInMillis: Long): Unit = {
    val e = eventQueue.poll(timeoutInMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
    if (e != null) handleEvent(e) else _lastEventType = Event.Undefined
  }

  def moveTo(newX: Int, newY: Int): Unit = { x = newX; y = newY }

  def lineTo(newX: Int, newY: Int): Unit = canvas.withGraphics { g =>
    import java.awt.BasicStroke
    val s = new BasicStroke(lineWidth.toFloat, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER)
    g.setStroke(s)
    g.setColor(color)
    g.drawLine(x, y, newX, newY)
    x = newX; y = newY
  }

  def setPixel(): Unit = canvas.withImage { img =>
    img.setRGB(x, y, color.getRGB)
  }

  def clearPixel(): Unit = canvas.withImage { img =>
    img.setRGB(x, y, background.getRGB)
  }

  def getPixel: java.awt.Color =
    new java.awt.Color(canvas.img.getRGB(x,y))

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

  def isFullScreen: Boolean = Swing.screen.isFullScreen

  def setFullScreen(activate: Boolean): Unit =
    if (activate) Swing.screen.enterFullScreen(frame)
    else Swing.screen.exitFullScreen(frame)

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
