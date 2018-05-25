package introprog

object Swing {

  private def runInSwingThread(callback: => Unit): Unit =
    javax.swing.SwingUtilities.invokeLater(() => callback)

  /** Run `callback` in Swing thread using `javax.swing.SwingUtilities.invokeLater`. */
  def apply(callback: => Unit): Unit = runInSwingThread(callback)

  private def installedLookAndFeels: Vector[String] =
    javax.swing.UIManager.getInstalledLookAndFeels.toVector.map(_.getClassName)

  private def findLookAndFeel(partOfName: String): Option[String] =
    installedLookAndFeels.find(_.toLowerCase contains partOfName)

  private def isOS(partOfName: String): Boolean =
    scala.sys.props("os.name").toLowerCase.contains(partOfName.toLowerCase)

  private var isInit = false

  /** Init the Swing GUI toolkit and set platform-specific look and feel.*/
  def init(): Unit = if (!isInit) {
    setPlatformSpecificLookAndFeel()
    isInit = true
  }

  def setPlatformSpecificLookAndFeel(): Unit = {
    import javax.swing.UIManager.setLookAndFeel
    if (isOS("linux")) findLookAndFeel("gtk").foreach(setLookAndFeel)
    else if (isOS("win")) findLookAndFeel("win").foreach(setLookAndFeel)
    else if (isOS("mac")) findLookAndFeel("apple").foreach(setLookAndFeel)
    else javax.swing.UIManager.setLookAndFeel(
      javax.swing.UIManager.getSystemLookAndFeelClassName()
    )
  }

  def fileDialog(buttonText: String = "Open", startDir: String = "~"): String = {
    val fs = new javax.swing.JFileChooser(new java.io.File(startDir))
    fs.showDialog(null, buttonText) match {
      case 0 => Option(fs.getSelectedFile.toString).getOrElse("")
      case _ => ""
    }
  }

  /** Invert each RGB component of `c: java.awt.Color` by subraction from 255. */
  def invertColor(c: java.awt.Color): java.awt.Color =
    new java.awt.Color(255 - c.getRed, 255 - c.getGreen, 255 - c.getBlue)

  /** A Swing `JPanel` to create drawing windows for 2D graphics. */
  class ImagePanel(
    val initWidth: Int,
    val initHeight: Int,
    val initBackground: java.awt.Color
  ) extends javax.swing.JPanel {
    val img: java.awt.image.BufferedImage = java.awt.GraphicsEnvironment
      .getLocalGraphicsEnvironment
      .getDefaultScreenDevice
      .getDefaultConfiguration
      .createCompatibleImage(initWidth, initHeight, java.awt.Transparency.OPAQUE)

    setBackground(initBackground)
		setDoubleBuffered(true)
		setPreferredSize(new java.awt.Dimension(initWidth, initHeight))
		setMinimumSize(new java.awt.Dimension(initWidth, initHeight))
		setMaximumSize(new java.awt.Dimension(initWidth, initHeight))

    override def paintComponent(g: java.awt.Graphics): Unit = g.drawImage(img, 0, 0, this)

  	override def imageUpdate(img: java.awt.Image, infoFlags: Int, x: Int, y: Int, width: Int, height: Int): Boolean = {
  		repaint()
      true
  	}

    def withGraphics(action: java.awt.Graphics2D => Unit) = { action(img.createGraphics())
      repaint()
    }

    def withImage(action: java.awt.image.BufferedImage => Unit) = {
      action(img)
      repaint()
    }
  }

  /** A module for changing window full screen mode in the main display. */
  object Screen {
    private def device = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice

    /** Test if full screen is supported bu the current main display. */
    def isSupported = device.isFullScreenSupported

    /** Test if full screen is supported. */
    def isFullScreen = device.getFullScreenWindow != null

    /** Test if window `w` is undecorated. */
    def isUndecorated(w: java.awt.Window): Boolean = w match {
      case f: javax.swing.JFrame => f.isUndecorated
      case _ => false
    }

    /** Set decoration `state` of window `w`. */
    def setUndecorated(w: java.awt.Window, state: Boolean): Unit =  {
      w match {
        case f: javax.swing.JFrame =>
          f.dispose()
          f.setUndecorated(state)
          f.pack()
          f.setVisible(true)
        case _ =>
      }
    }

    /** Toggle decorations (such as window top and borders) of window `w` on or off. */
    def toggleDecorations(w: java.awt.Window): Unit = setUndecorated(w, !isUndecorated(w))

    /** If window `w` is in full screen mode then exit full screen. */
    def exitFullScreen(w: java.awt.Window): Unit =  {
      if (isSupported) {
        device.setFullScreenWindow(null)
        setUndecorated(w, false)
      }
    }

     /** If window `w` is not in full screen mode then enter full screen. */
     def enterFullScreen(w: java.awt.Window): Unit =  {
       if (isSupported) {
         setUndecorated(w, true)
         device.setFullScreenWindow(w)
       }
     }

    /** Toggle the full screen state of window `w` on or off. */
    def toggleFullScreen(w: java.awt.Window): Unit = runInSwingThread {
      if (isFullScreen) exitFullScreen(w) else enterFullScreen(w)
    }
  }
}
