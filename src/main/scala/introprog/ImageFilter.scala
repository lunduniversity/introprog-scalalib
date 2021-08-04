

package introprog


/**
 * The super class to all filters.
 * 
 * (2021-07-25) translate from java.
 * (Theodor Lundqvist)
 * 
 * (2016-07-17) nbrOfArgs attribut has been added.
 * (Casper Schreiter, Björn Regnell)
 * 
 * Create a filter object with a given name and the number of arguments the filter needs.
 * @param name
 *            the name of the filter.
 * @param nbrOfArgs
 *            number of arguments.
 */
abstract class ImageFilter(val name: String, val nbrOfArgs: Int):

	/**
	 * Apply the filter on `img` and return the result as a new Image using the arguments in `args`.
	 * 
	 * @param img
	 *            the original image.
	 * @param args
	 *            arguments
	 * @return the resulting image.
	 */
	def apply(img: Image, args: Array[Double]): Image;
	
	/**
	 * Calculate the intensity in each pixel of `img`.
	 * 
	 * @param img
	 *           the image
	 * @return intensitymatrix, values ranging from 0 to 255
	 */
	protected def computeIntensity(img: Image): Array[Array[Short]] = 
		val intensity : Array[Array[Short]] = Array.ofDim(img.height, img.width)
		for 
			h <- 0 until img.height 
			w <- 0 until img.width
		do
			val c = img(h, w)
			intensity(h)(w) = ((c.getRed()+c.getGreen+c.getBlue())/3).toShort
		intensity

	/**
	 * Convolute `p[i][j]` with the convolutionkernel `kernel`.
	 * 
	 * @param p
	 *            matrix with numbervalues
	 * @param i
	 *            current row index
	 * @param j
	 *            current coloumn index
	 * @param kernel
	 *            convolutionkernel, a 3x3-matrix
	 * @param weight
	 *            the sum of the element in `kernel`
	 * @return result of the convolution
	 */
	protected def convolve(p: Array[Array[Short]], i: Int, j: Int, kernel: Array[Array[Short]],
			weight: Int): Short =
		var sum : Double = 0;
		
		for ii <- -1 to 1 do
			for jj <- -1 to 1 do
				sum += p(i + ii)(j + jj) * kernel(ii + 1)(jj + 1);
		
		Math.round(sum / weight).toShort;
	

  

