package hello

object Main {
  val w = new lu.gui.PixelWindow(400,300,"HEJ pixelfönster!!")

  def square(topLeft: (Int, Int))(side: Int): Unit = {
    w.moveTo( topLeft._1,        topLeft._2        )
    w.lineTo( topLeft._1 + side, topLeft._2        )
    w.lineTo( topLeft._1 + side, topLeft._2 + side )
    w.lineTo( topLeft._1,        topLeft._2 + side )
    w.lineTo( topLeft._1,        topLeft._2        )
  }

  def main(args: Array[String]): Unit = {
    println("Rita kvadrat")
    w.lineWidth = 2
    square(300,100)(50)
  }
}
