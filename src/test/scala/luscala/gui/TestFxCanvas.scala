package luscala.gui

/** run in `sbt:luscala>` with command `Test/runMain luscala.gui.TestSimpleCanvas` */
object TestFxCanvas {
  def main(args: Array[String]): Unit = {
    println("You should see two black windows with a white in line each.")
    println("The focus be in the second window after startup.")
    println("The focus should alternate to the first window after 1 sec.")
    println("The windows should close and the app should exit after 2 sec.")

    val w1 = new FxCanvas("TestFxCanvas1")
    w1.line(0,0,100,100)
    w1.writeText("Hello FxCanvas FIRST!", 105, 105)

    val w2 = new FxCanvas("TestFxCanvas2")
    w2.line(0,0,100,100)
    w2.writeText("Hello FxCanvas SECOND!", 105, 105)

    Thread.sleep(1000)
    w1.show
    Thread.sleep(1000)

    Fx.exit()
  }
}
