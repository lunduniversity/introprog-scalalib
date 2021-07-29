package introprog.examples

/** Example of serializing objects to and from binary files on disk. */
object TestIO {
  import introprog.IO

  case class Person(name: String)

  def main(args: Array[String]): Unit = {
    println("Test of IO of serializable objects to/from disk:")
    val highscores = Map(Person("Sandra") -> 42, Person("Björn") -> 5)

    // serialize to disk:
    IO.saveObject(highscores,"highscores.ser")

    // de-serialize back from disk:
    val highscores2 = IO.loadObject[Map[Person, Int]]("highscores.ser")

    val isSameContents = highscores2 == highscores
    val testResult = if (isSameContents) "SUCCESS :)" else "FAILURE :("
    assert(isSameContents, s"$highscores != $highscores2")
    println(s"$highscores == $highscores2\n$testResult")

    testImageLoadAndDraw
  }

  def testImageLoadAndDraw = {
    import introprog.*
    import java.awt.Color
    import java.awt.Color.*

    val w = new PixelWindow(4*128, 3*128);
    val w2 = new PixelWindow(4*128, 3*128)
    //draw text top right
    val testMatrix = Array[Array[Color]](Array[Color](blue, yellow, blue), 
                                        Array[Color](yellow, yellow,  yellow),
                                        Array[Color](blue, yellow,  blue),
                                        Array[Color](blue, yellow,  blue))
    var flagPos = (0, 0)
    var flagSize = (4, 3)
    
    //rita pytteliten flagga                
    w.drawMatrix(testMatrix, 0, 0)

    for i <- 1 to 7 do
      //klipp ut och spara förra flaggan (via ColorMatrix)
      val cm = w.getMatrix(flagPos._1, flagPos._2, flagSize._1, flagSize._2)
      IO.saveImage("screenshot.png", cm.toImage)
      //rita ut på det andra fönstret med `drawMatrix`
      w2.drawMatrix(cm.toMatrix, flagPos._1, flagPos._2)
      //uppdatera pos och size
      flagPos = (flagPos._1 + flagSize._1,flagPos._2 + flagSize._2)
      flagSize = (flagSize._1 * 2,flagSize._2 * 2)
      //rita nya flagga från fil
      if(i != 7) w.drawImage(IO.loadImage("screenshot.png"), flagPos._1, flagPos._2, flagSize._1, flagSize._2)
    
    println("if there are 7 flags in each window everything should be working fine")
    
    //delete screenshot file
    IO.delete("screenshot.png")
  }



// for file extension choice see:
// https://stackoverflow.com/questions/10433214/file-extension-for-a-serialized-object

}
