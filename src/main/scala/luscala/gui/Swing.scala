package luscala.gui

object Swing {
  def installedLookAndFeels: Vector[String] =
    javax.swing.UIManager.getInstalledLookAndFeels.toVector.map(_.getClassName)

  def findLookAndFeel(partOfName: String): Option[String] =
    installedLookAndFeels.find(_.toLowerCase contains partOfName)

  def isOS(partOfName: String): Boolean =
    scala.sys.props("os.name").toLowerCase.contains(partOfName.toLowerCase)

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
}
