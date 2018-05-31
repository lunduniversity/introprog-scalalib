package introprog.fx

/** A window for pixel-based drawing in an underlying javafx window. */
object FxPixelWindow  {
  def exit(): Unit = System.exit(0)

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

class FxPixelWindow(
  val width: Int = 800,
  val height: Int = 640,
  val title: String      = "FxPixelWindow",
  val background: java.awt.Color = java.awt.Color.black,
  val foreground: java.awt.Color = java.awt.Color.green
) {
  import FxPixelWindow._
  protected val eventQueueCapacity = 1000
  protected var eventQueue =
    new java.util.concurrent.LinkedBlockingQueue[javafx.event.Event](eventQueueCapacity)

  @volatile private var _lastEventType = Event.Undefined
  def lastEventType: String = _lastEventType

 @volatile private var _lastKeyCode: javafx.scene.input.KeyCode = javafx.scene.input.KeyCode.UNDEFINED
  //
  // def lastKeyCode: String =
  //   if (_lastKeyCode == javafx.scene.input.KeyCode.UNDEFINED && _lastKeyText != "")
  //     _lastKeyText.toUpperCase // This makes åäö show as ÅÄÖ and not UNDEFINED
  //   else _lastKeyCode.toString

  @volatile private var _lastKeyText = ""
  //def lastKeyText: String = _lastKeyText
  def lastKey: String = {
    //println(s"_lastKeyCode '${_lastKeyCode}'   _lastKeyText '${_lastKeyText}'")
    if (_lastKeyCode == javafx.scene.input.KeyCode.UNDEFINED && _lastKeyText != "")
      _lastKeyText // Handle åäö
    else if (_lastKeyText < " " || _lastKeyCode == javafx.scene.input.KeyCode.DELETE)
      _lastKeyCode.toString.toLowerCase.capitalize
    else _lastKeyText
  }
  @volatile private var _lastMousePos = (0.0, 0.0)
  def lastMousePos: (Double, Double) = _lastMousePos

  private def handleEvent(e: javafx.event.Event): Unit = e match {
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

  private lazy val initSize = (width.toDouble, height.toDouble)

  protected lazy val canvas = new javafx.scene.canvas.Canvas(initSize._1, initSize._2)
  protected lazy val root = new javafx.scene.layout.BorderPane
  protected lazy val scene =
    new javafx.scene.Scene(root, initSize._1, initSize._2,
      Fx.toFxColor(java.awt.Color.BLACK.brighter.brighter))

  /*protected*/ def withGC(callback: javafx.scene.canvas.GraphicsContext => Unit): Unit =
    Fx(callback(canvas.getGraphicsContext2D))

  // def line(x1: Double, y1: Double, x2: Double, y2: Double): Unit =  withGC(_.strokeLine(x1,y1,x2,y2))

  /** Draw a line from (`x1`, `y1`) to (`x2`, `y2`) using `color` and `lineWidth`. */
  def line(x1: Int, y1: Int, x2: Int, y2: Int, color: java.awt.Color = foreground, lineWidth: Int = 1): Unit = withGC { gc =>
    gc.setStroke(Fx.toFxColor(color))
    gc.setLineWidth(lineWidth.toDouble)
    gc.strokeLine(x1.toDouble,y1.toDouble,x2.toDouble,y2.toDouble)
    gc.strokeLine(x1.toDouble,y1.toDouble,x2.toDouble,y2.toDouble)
  }

  // def setLineWidth(width: Double): Unit = withGC(_.setLineWidth(width))
  // def setLineColor(c: java.awt.Color): Unit = withGC(_.setStroke(Fx.toFxColor(c)))
  //
  // def setFillColor(c: java.awt.Color): Unit = withGC(_.setFill(Fx.toFxColor(c)))
  // def rect(x: Double, y: Double, w: Double, h: Double): Unit =  withGC { _.strokeRect(x, y , w, h) }

  // def fillRect(x1: Double, y1: Double, x2: Double, y2: Double): Unit =  withGC(_.fillRect(x1,y1,x2,y2))

  def fill(x: Int, y: Int, width: Int, height: Int, color: java.awt.Color = foreground): Unit = withGC { gc =>
    //gc.setStroke(Fx.toFxColor(color))
    gc.setFill(Fx.toFxColor(color))
    gc.fillRect(x.toDouble,y.toDouble,width.toDouble,height.toDouble)
  }

  def setPixel(x: Int, y: Int, color: java.awt.Color = foreground): Unit =
    withGC { gc =>
      val pw = gc.getPixelWriter
      pw.setColor(x, y,Fx.toFxColor(color))
    }

  def getPixel(x: Int, y: Int): java.awt.Color = Fx.await {
    val gc = canvas.getGraphicsContext2D
    val writableImage = canvas.snapshot(null, null)
    val pixelReader = writableImage.getPixelReader
    val c = pixelReader.getColor(x, y)
    Fx.toAwtColor(c)
  }

  def drawText(
    text: String,
    x: Int,
    y: Int,
    color: java.awt.Color = foreground,
    size: Int = 16,
    fontName: String = "Monospaced Bold"
  ) = withGC { gc =>
    gc.setFill(Fx.toFxColor(color))
    gc.setFont(new javafx.scene.text.Font(fontName, size.toDouble))
    gc.setFontSmoothingType(javafx.scene.text.FontSmoothingType.LCD)
    gc.fillText(text, x.toDouble, y.toDouble + size.toDouble)
  }


  //def writeText(text: String, x: Double, y: Double): Unit = withGC(_.strokeText(text, x, y))

  //def size: (Double, Double) = (stage.getWidth, stage.getHeight)
  //def clear(): Unit = withGC(_.clearRect(0, 0, size._1, size._2))

  def show():  Unit = Fx { stage.show; stage.requestFocus }

  // protected lazy val basicMenuBar =
  //   Fx.menuBar(
  //     Fx.menu("File", Fx.menuItem("Quit", "Ctrl+Q", () => System.exit(0))),
  //     Fx.menu("View", Fx.menuItem("Toggle Full Screen", "F11",
  //       () => stage.setFullScreen(!stage.isFullScreen))
  //     )
  //   )

  protected val stage: javafx.stage.Stage = Fx.newStage { s =>
      s.setTitle(title)
      root.setBackground(javafx.scene.layout.Background.EMPTY)
      canvas.getGraphicsContext2D.setFill(Fx.toFxColor(background))
      canvas.getGraphicsContext2D.fillRect(0.0,0.0,width.toDouble,height.toDouble)
      canvas.getGraphicsContext2D.setStroke(Fx.toFxColor(foreground))
      s.setScene(scene)
      root.setCenter(canvas)
      s.setMinWidth(width.toDouble)
      s.setMinHeight(height.toDouble)
      s.show

      //if (initBasicMenu) root.getChildren.add(0, basicMenuBar)
      scene.setOnKeyPressed    (e => { Fx.log(e); eventQueue.offer(e)} )
      scene.setOnKeyReleased   (e => { Fx.log(e); eventQueue.offer(e)} )
      canvas.setOnMousePressed (e => { Fx.log(e); eventQueue.offer(e)} )
      canvas.setOnMouseReleased(e => { Fx.log(e); eventQueue.offer(e)} )
      s.setOnHiding( e =>  { Fx.log(e); eventQueue.clear(); eventQueue.offer(e) })
  }
}
