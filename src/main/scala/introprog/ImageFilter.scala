

package introprog


/**
 * Superklassen till alla filterklasser.
 * @version 1.3 (2021-07-25) översättning från java
 * (Theodor Lundqvist)
 * 
 * Skapar ett filterobjekt med ett givet namn och antalet argument filtret behöver.
 * 
 * @param name
 *            filtrets namn
 * @param nbrOfArgs
 *            antal argument
 */
abstract class ImageFilter(val name: String, val nbrOfArgs: Int):
	import java.awt.Color;

    	/**
	 * Filtrerar bilden i matrisen inPixels och returnerar resultatet i en ny
	 * matris. Utnyttjar eventuellt värdena i args
	 * 
	 * @param inPixels
	 *            den ursprungliga bilden
	 * @param args
	 *            argument
	 * @return den filtrerade bilden
	 */
	def apply(inPixels: ColorMatrix, args: Array[Double]): ColorMatrix;
	
	/**
	 * Beräknar intensiteten hos alla pixlarna i pixels, returnerar resultatet i
	 * en ny matris.
	 * 
	 * @param pixels
	 *            matris med pixlar
	 * @return intensiteten i varje pixel (matris med shorts)
	 */
	protected def computeIntensity(cm: ColorMatrix): Array[Array[Short]] = 
		val intensity : Array[Array[Short]] = Array.ofDim(cm.height, cm.width)
		for 
			h <- 0 until cm.height 
			w <- 0 until cm.width
		do
			val c = cm(h, w)
			intensity(h)(w) = ((c.getRed()+c.getGreen+c.getBlue())/3).toShort
		intensity


	/**
	 * Faltar punkten p[i][j] med faltningskärnan kernel.
	 * 
	 * @param p
	 *            matris med talvärden
	 * @param i
	 *            radindex för den aktuella punkten
	 * @param j
	 *            kolonnindex för den aktuella punkten
	 * @param kernel
	 *            faltningskärnan, en 3x3-matris
	 * @param weight
	 *            summan av elementen i kernel
	 * @return resultatet av faltningen
	 */
	protected def convolve(p: Array[Array[Short]], i: Int, k: Int, kernel: Array[Array[Short]],
			weight: Int): Short =
		var sum : Double = 0;
		
		for ii <- -1 to 1 do
			for jj <- -1 to 1 do
				sum += p(i + ii)(k + jj) * kernel(ii + 1)(jj + 1);
		
		Math.round(sum / weight).toShort;
	

  
