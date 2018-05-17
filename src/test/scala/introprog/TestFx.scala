package introprog

/** run in `sbt:introprog>` with command `Test/runMain introprog.TestFx` */
object TestFx {
  def main(args: Array[String]): Unit = {
    println("You should see a black window with a green line for 1 second.")
    Fx.newStage { s =>
        s.show
        s.setTitle("TestFx")
        val black = javafx.scene.paint.Color.BLACK
        val green = javafx.scene.paint.Color.GREEN
        val root = new javafx.scene.layout.VBox
        s.setScene(new javafx.scene.Scene(root, 800, 600, black))
        root.setBackground(javafx.scene.layout.Background.EMPTY)
        val canvas = new javafx.scene.canvas.Canvas(800, 600)
        canvas.getGraphicsContext2D.setStroke(green)
        canvas.getGraphicsContext2D.strokeLine(0,0,100,100)
        canvas.getGraphicsContext2D.strokeText("Hello Fx!", 105, 105)
        root.getChildren.add(canvas)
    }
    Thread.sleep(1000)
    Fx.exit()
  }
}
