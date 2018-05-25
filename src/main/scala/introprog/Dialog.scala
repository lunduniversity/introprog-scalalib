package introprog

object Dialog {
  def chooseFile(buttonText: String = "Open", startDir: String = "~"): String = {
    val fs = new javax.swing.JFileChooser(new java.io.File(startDir))
    fs.showDialog(null, buttonText) match {
      case 0 => Option(fs.getSelectedFile.toString).getOrElse("")
      case _ => ""
    }
  }
}
