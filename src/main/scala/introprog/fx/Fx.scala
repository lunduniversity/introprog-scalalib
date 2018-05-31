package introprog.fx

/** A wrapper with utils for simpler access to javafx. The main feature is the
  * ability to create stages without having to treat the first stage as special:
  * Simply do: `val myStage = Fx.newStage { s => ... }` and initialize a stage `s`
  * The underlying javafx app ceremony is hidden. */
private[fx] object Fx {

  def toFxColor(c: java.awt.Color): javafx.scene.paint.Color =
    javafx.scene.paint.Color.rgb(c.getRed, c.getGreen, c.getBlue, c.getAlpha / 255.0)

  def toAwtColor(c: javafx.scene.paint.Color): java.awt.Color =
      new java.awt.Color(
        c.getRed.toFloat, c.getGreen.toFloat, c.getBlue.toFloat, c.getOpacity.toFloat
      )


  /** Switch on/off log output on application start and stop. Initially false. */
  @volatile var isLogging = false

  /** Print `any.toString` to standard out if `isLogging` is true. */
  def log[T](any: T) = if (isLogging) println(any.toString)

  @volatile private var _primaryStage: javafx.stage.Stage = _

  /** The primary stage, initialized on first call to newStage. */
  def primaryStage: javafx.stage.Stage = _primaryStage

  @volatile private var delayedAppInit: javafx.stage.Stage => Unit = _

  private class FXState {
    private final val Unstarted = 0
    private final val Starting  = 1
    private final val Started   = 2

    private var state = Unstarted

    def attemptStart(): Boolean = this.synchronized {
      if (state == Unstarted) { // is this the first time asking
        state = Starting
        true
      } else {
        waitUntilStarted() // you have to wait for toolkit to start
        false
      }
    }

    def waitUntilStarted(): Unit = this.synchronized {
      while (state != Started) wait()
    }

    def setStarted(): Unit = this.synchronized {
      state = Started
      notifyAll
    }
  }

  private val fxState = new FXState

  private def launchApp(initPrimaryStage: javafx.stage.Stage => Unit): Unit = {
    delayedAppInit = initPrimaryStage  // only assigned once here
    new Thread( () => { // spawn new thread so that caller does not block
      javafx.application.Application.launch(classOf[UnderlyingApp]) // blocks until exit
    }).start
  }

  def runInFxThread(callback: => Unit): Unit =
    javafx.application.Platform.runLater { () => callback }

  def apply(callback: => Unit): Unit = runInFxThread(callback)

  def await[T: scala.reflect.ClassTag](callback: => T): T = {
    val ready = new java.util.concurrent.CountDownLatch(1)
    val result = new Array[T](1)
    runInFxThread {
      result(0) = callback
      ready.countDown
    }
    ready.await
    result(0)
  }

  /** Create a new window and at first call launch underlying javafx application. */
  def newStage(init: javafx.stage.Stage => Unit): javafx.stage.Stage = {
    def elapsed(since: Long): Long = (System.nanoTime - since)/1000000  // ms
    if (fxState.attemptStart) {
      val t0 = System.nanoTime
      launchApp(init)
      fxState.waitUntilStarted()  // blocks until UnderlyingApp.start is called
      log(s"JavaFX Toolkit launched in ${elapsed(t0)} ms")
      primaryStage
    } else {
      val t0 = System.nanoTime
      var nonPrimaryStage: javafx.stage.Stage = null
      await {
        nonPrimaryStage = new javafx.stage.Stage;
        init(nonPrimaryStage)
      }
      log(s"JavaFX Stage constructed in ${elapsed(t0)} ms")
      nonPrimaryStage
    }
  }

  private class UnderlyingApp extends javafx.application.Application {
    override def start(primaryStage: javafx.stage.Stage): Unit = {
      _primaryStage = primaryStage  // only assigned once here
      delayedAppInit(primaryStage)  // only called once here
      javafx.application.Platform.setImplicitExit(false) // don't System.exit on javafx exit
      fxState.setStarted  // release all threads waiting for toolkit to start
    }
    override def stop(): Unit = {
      log("JavaFX Toolkit Application stopped.")
    }
  }

  def menuItem(
    name: String,
    shortcut: String = "",
    action: () => Unit
  ): javafx.scene.control.MenuItem = {
    val item = javafx.scene.control.MenuItemBuilder.create()
      .text(name)
      .onAction(e => action())
      .build()
    if (shortcut.nonEmpty) {
      item.setAccelerator(javafx.scene.input.KeyCombination.keyCombination(shortcut))
    }
    item
  }

  def menu(name: String, items: javafx.scene.control.MenuItem*): javafx.scene.control.Menu = {
    val menu = new javafx.scene.control.Menu(name)
    menu.getItems.addAll(items: _*)
    menu
  }

  def menuBar(items: javafx.scene.control.Menu*) = new javafx.scene.control.MenuBar(items:_*)

  def exit(): Unit = javafx.application.Platform.exit
}
