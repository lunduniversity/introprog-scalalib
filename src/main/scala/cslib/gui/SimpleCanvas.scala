package cslib.gui

/** A module ready to use in the Scala REPL or in a main Scala program */
object SimpleCanvas  {
  def systemExit(): Unit = System.exit(0)

  def stopAllWindows(): Unit= Fx.exit()

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

  /* Predefined opaque colors. */
  object Color {
    val Black = new Color(0,0,0)
    val White = new Color(255, 255,255)
    val Red   = new Color(255, 0, 0)
    val Green = new Color(0, 255, 0)
    val Blue  = new Color(0, 0, 255)
  }

  /* RGB color where alpha = 0 is fully transparent and 255 is opaque. */
  class Color(val red: Int, val green: Int, val blue: Int, val alpha: Int = 255){
    require(0 to 255 contains red,   s"red=$red must be within 0 to 255")
    require(0 to 255 contains green, s"green=$green must be within 0 to 255")
    require(0 to 255 contains blue,  s"blue=$blue must be within 0 to 255")
    require(0 to 255 contains alpha, s"alpha=$alpha must be within 0 to 255")

    override def toString = s"Color(red=$red, green=$green, blue=$blue, alpha=$alpha)"
  }
}

class SimpleCanvas(
  val initTitle: String      = "Another Canvas Window",
  val initSize: (Double, Double)   = (1000.0, 1000.0),
  val initBackground: SimpleCanvas.Color  = SimpleCanvas.Color.Black,
  val initBasicMenu: Boolean = false,
) {
  import SimpleCanvas._
  protected val eventQueueCapacity = 1000
  protected var eventQueue =
    new java.util.concurrent.LinkedBlockingQueue[javafx.event.Event](eventQueueCapacity)

  protected var _lastEventType = Event.Undefined
  def lastEventType: String = _lastEventType

  protected var _lastKeyCode: javafx.scene.input.KeyCode = javafx.scene.input.KeyCode.UNDEFINED
  def lastKeyCode: String =
    if (_lastKeyCode == javafx.scene.input.KeyCode.UNDEFINED && _lastKeyText != "")
      _lastKeyText.toUpperCase // This makes åäö show as ÅÄÖ and not UNDEFINED
    else _lastKeyCode.toString

  protected var _lastKeyText = ""
  def lastKeyText: String = _lastKeyText

  protected var _lastMousePos = (0.0, 0.0)
  def lastMousePos: (Double, Double) = _lastMousePos

  protected def handleEvent(e: javafx.event.Event): Unit = e match {
    case ke: javafx.scene.input.KeyEvent =>
      _lastKeyCode = ke.getCode
      _lastKeyText = ke.getText   // TODO Handle undefined...
      ke.getEventType match {
        case javafx.scene.input.KeyEvent.KEY_PRESSED => _lastEventType = Event.KeyPressed
        case javafx.scene.input.KeyEvent.KEY_RELEASED => _lastEventType = Event.KeyReleased
        case _ => _lastEventType = Event.Undefined
      }

    case me: javafx.scene.input.MouseEvent =>
      _lastMousePos = (me.getX, me.getY)
      me.getEventType match {
        case javafx.scene.input.MouseEvent.MOUSE_PRESSED => _lastEventType = Event.MousePressed
        case javafx.scene.input.MouseEvent.MOUSE_RELEASED => _lastEventType = Event.MouseReleased
        case _ => _lastEventType = Event.Undefined
      }

    case we: javafx.stage.WindowEvent
      if we.getEventType == javafx.stage.WindowEvent.WINDOW_HIDING =>
        _lastEventType = Event.WindowClosed

    case _ => _lastEventType = Event.Undefined
  }

  def awaitEvent(timeoutInMillis: Long): Unit = {
    val e = eventQueue.poll(timeoutInMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
    if (e != null) handleEvent(e)
    else _lastEventType = Event.Undefined
  }

  def isFullScreen: Boolean = stage.isFullScreen
  def setFullScreen(isFull: Boolean): Unit = Fx(stage.setFullScreen(isFull))

  protected def fxColor(c: Color): javafx.scene.paint.Color =
    javafx.scene.paint.Color.rgb(c.red, c.green, c.blue, c.alpha / 255.0)

  protected lazy val canvas = new javafx.scene.canvas.Canvas(initSize._1, initSize._2)
  protected lazy val root = new javafx.scene.layout.VBox
  protected lazy val scene =
    new javafx.scene.Scene(root, initSize._1, initSize._2, fxColor(initBackground))

  protected def withGC(callback: javafx.scene.canvas.GraphicsContext => Unit): Unit =
    Fx(callback(canvas.getGraphicsContext2D))

  def line(x1: Double, y1: Double, x2: Double, y2: Double): Unit =  withGC(_.strokeLine(x1,y1,x2,y2))
  def setLineWidth(width: Double): Unit = withGC(_.setLineWidth(width))
  def setLineColor(c: Color): Unit = withGC(_.setStroke(fxColor(c)))

  def setFillColor(c: Color): Unit = withGC(_.setFill(fxColor(c)))
  def rect(x: Double, y: Double, w: Double, h: Double): Unit =  withGC { _.strokeRect(x, y , w, h) }

  def fillRect(x1: Double, y1: Double, x2: Double, y2: Double): Unit =  withGC(_.fillRect(x1,y1,x2,y2))

  def writeText(text: String, x: Double, y: Double): Unit = withGC(_.strokeText(text, x, y))

  def size: (Double, Double) = (stage.getWidth, stage.getHeight)
  def clear(): Unit = withGC(_.clearRect(0, 0, size._1, size._2))

  def show():  Unit = Fx { stage.show; stage.requestFocus }

  protected lazy val basicMenuBar =
    Fx.menuBar(
      Fx.menu("File", Fx.menuItem("Quit", "Ctrl+Q", () => System.exit(0))),
      Fx.menu("View", Fx.menuItem("Toggle Full Screen", "F11",
        () => stage.setFullScreen(!stage.isFullScreen))
      )
    )

  protected val stage: javafx.stage.Stage = Fx.newStage { s =>
      s.show
      s.setTitle(initTitle)
      root.setBackground(javafx.scene.layout.Background.EMPTY)
      canvas.getGraphicsContext2D.setStroke(fxColor(initBackground).invert)
      s.setScene(scene)
      root.getChildren.add(canvas)
      if (initBasicMenu) root.getChildren.add(0, basicMenuBar)
      scene.setOnKeyPressed    (e => { Fx.log(e); eventQueue.offer(e)} )
      scene.setOnKeyReleased   (e => { Fx.log(e); eventQueue.offer(e)} )
      canvas.setOnMousePressed (e => { Fx.log(e); eventQueue.offer(e)} )
      canvas.setOnMouseReleased(e => { Fx.log(e); eventQueue.offer(e)} )
      s.setOnHiding( e =>  { Fx.log(e); eventQueue.clear(); eventQueue.offer(e) })
  }
}
