package introprog

class Image (val underlying: java.awt.image.BufferedImage):
  import java.awt.Color
  import java.awt.image.BufferedImage

  def apply(x: Int, y: Int): Color = new Color(underlying.getRGB(x, y)) //getRGB ger en Int
  def update(x: Int, y: Int, c: Color): Unit = underlying.setRGB(x, y, c.getRGB)

  def update(f: (Int, Int) => Color): Unit = 
    for x <- 0 until width; y <- 0 until height do
        update(x, y, f(x, y))

  /** Extract and return image pixels.*/
  def toMatrix: Array[Array[Color]] = 
    val xs: Array[Array[Color]] = Array.ofDim(width, height)
    for x <- 0 until width; y <- 0 until height do
      xs(x)(y) = apply(x, y)
    xs

  /** Copy subsection of image defined by top left corner `(x, y)` and `(width, height)`.*/
  def subsection(x: Int, y: Int, width: Int, height: Int): Image =   
    val bi = BufferedImage(width, height, underlying.getType)
    bi.createGraphics().drawImage(underlying, 0, 0, width, height, x, y, x+width, y+height, null)
    Image(bi)

  /** Copy image and scale to `(width, height)`.*/
  def scaled(width: Int, height: Int): Image = 
    val bi = BufferedImage(width, height, underlying.getType)
    bi.createGraphics().drawImage(underlying, 0, 0, width, height, null)
    Image(bi)
  
  /** Copy image and change image type ex. BufferedImage.TYPE_INT_RGB*/
  def toImageType(imageType: Int): Image =
    val bi = BufferedImage(width, height, imageType)
    bi.createGraphics().drawImage(underlying, 0, 0, width, height, null)
    Image(bi)

  val hasAlpha = underlying.getColorModel.hasAlpha
  val height = underlying.getHeight
  val width = underlying.getWidth