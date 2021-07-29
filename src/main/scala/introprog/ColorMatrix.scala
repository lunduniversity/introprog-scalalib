package introprog

class ColorMatrix private (underlying: java.awt.image.BufferedImage):
  import java.awt.Color

  def apply(x: Int, y: Int): Color = new Color(underlying.getRGB(x, y)) //getRGB ger en Int
  def update(x: Int, y: Int, c: Color): Unit = underlying.setRGB(x, y, c.getRGB)

  def update(f: (Int, Int) => Color): Unit = 
    for x <- 0 until width; y <- 0 until height do
        update(x, y, f(x, y))
  
  /** Return the underlying `BufferedImage` */
  def toImage: java.awt.image.BufferedImage = underlying

  /** Extract and return image pixels*/
  def toMatrix: Array[Array[Color]] = 
    val xs: Array[Array[Color]] = Array.ofDim(width, height)
    for x <- 0 until width; y <- 0 until height do
      xs(x)(y) = apply(x, y)
    xs

  lazy val height = underlying.getHeight
  lazy val width = underlying.getWidth

object ColorMatrix:
  def fromImage(img: java.awt.image.BufferedImage): ColorMatrix = new ColorMatrix(img)

extension (img: java.awt.image.BufferedImage) def toColorMatrix = ColorMatrix.fromImage(img)