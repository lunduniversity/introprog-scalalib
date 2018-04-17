package cslib.gui

/** run in `sbt:cslib>` with command `Test/runMain cslib.gui.TestSimpleCanvas` */
object TestSimpleCanvas {
  def main(args: Array[String]): Unit = {
    println("You should see two black windows with a white in line each.")
    println("The focus be in the second window after startup.")
    println("The focus should alternate to the first window after 1 sec.")
    println("The windows should close and the app should exit after 2 sec.")

    val w1 = new SimpleCanvas("TestSimpleCanvas1")
    w1.line(0,0,100,100)
    w1.writeText("Hello SimpleCanvas FIRST!", 105, 105)

    val w2 = new SimpleCanvas("TestSimpleCanvas2")
    w2.line(0,0,100,100)
    w2.writeText("Hello SimpleCanvas SECOND!", 105, 105)

    Thread.sleep(1000)
    w1.show
    Thread.sleep(1000)

    Fx.exit()
  }
}
