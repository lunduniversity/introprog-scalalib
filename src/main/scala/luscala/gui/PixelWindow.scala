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
  val width: Int = 1000,
  val height: Int = 1000,
  val title: String = "PixelWindow",
  val background: java.awt.Color = java.awt.Color.BLACK
) {

  import PixelWindow.Event

  var x = 0
  var y = 0
  /*private*/ val frame = new javax.swing.JFrame(title)
  /*private*/ val canvas = new Swing.CanvasPanel(width, height, background)

  private val queueCapacity = 1000
  private val eventQueue =
    new java.util.concurrent.LinkedBlockingQueue[java.awt.AWTEvent](queueCapacity)

  private var _lastEventType = Event.Undefined
  def lastEventType: String = _lastEventType

  protected var _lastKeyText = ""
  def lastKey: String = _lastKeyText

  protected var _lastMousePos = (0, 0)
  def lastMousePos: (Int, Int) = _lastMousePos

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

  def moveTo(pos: (Int, Int)): Unit = { x = pos._1; y = pos._2 }

  def lineTo(pos: (Int, Int)): Unit = {
    canvas.line(x, y, pos._1, pos._2)
    x = pos._1; y = pos._2
  }

  def open(): Unit = frame.setVisible(true)

  def close(): Unit = { frame.setVisible(false); frame.dispose() }

  def clear(): Unit = canvas.clear()

  def drawString(s: String) = canvas.drawString(s, x, y)

  def setTextSize(pts: Int) = canvas.textSize = pts

  def setLineColor(c: java.awt.Color): Unit = canvas.lineColor = c

  def setLineWidth(w: Int): Unit = canvas.lineWidth = w

  def setFullScreen(isActivate: Boolean): Unit =
    if (isActivate) Swing.screen.enterFullScreen(frame)
    else Swing.screen.exitFullScreen(frame)

  Swing.init() // to setPlatformSpecificLookAndFeel
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
