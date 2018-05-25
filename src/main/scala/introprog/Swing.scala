package introprog

/** A module with Swing utilities. */
private[introprog] object Swing {

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

    def withGraphics(action: java.awt.Graphics2D => Unit) = runInSwingThread {
      action(img.createGraphics())
      repaint()
    }

    def withImage(action: java.awt.image.BufferedImage => Unit) = runInSwingThread {
      action(img)
      repaint()
    }
  }
}
