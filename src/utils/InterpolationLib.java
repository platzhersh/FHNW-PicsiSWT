package utils;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

public class InterpolationLib {

	/**
	 * Returns the RGB-value for a with interpolation.
	 * @param A RGB-value of a pixel at location (u / v).
	 * @param B RGB-value of a pixel at location (u+1 / v).
	 * @param C RGB-value of a pixel at location (u / v+1).
	 * @param D RGB-value of a pixel at location (u+1 / v+1).
	 * @param a Delta x - u.
	 * @param b Delta y - v.
	 * @return RGB value of the pixel at (x/y)
	 */
	public static RGB bilinearInterpolation(RGB A, RGB B, RGB C, RGB D, double a, double b) {
		A.red 	= (int) ((a - 1) * (b - 1) * A.red);
		A.green = (int) ((a - 1) * (b - 1) * A.green);
		A.blue 	= (int) ((a - 1) * (b - 1) * A.blue);

		B.red 	= (int) ((1 - b) * a * B.red);
		B.green = (int) ((1 - b) * a * B.green);
		B.blue 	= (int) ((1 - b) * a * B.blue);

		C.red 	= (int) ((1 - a) * b * C.red);
		C.green = (int) ((1 - a) * b * C.green);
		C.blue 	= (int) ((1 - a) * b * C.blue);

		D.red 	= (int) (a * b * D.red);
		D.green = (int) (a * b * D.green);
		D.blue 	= (int) (a * b * D.blue);

		return new RGB(A.red + B.red + C.red + D.red, 
				A.green + B.green + C.green + D.green, 
				A.blue + B.blue + C.blue + D.blue);
	}
	
	/**
	 * Calculates an Image from an given Image applying an Filter overlay.
	 * @param inData Input image.
	 * @param imageType Type of input Image.
	 * @param filter Filter array.
	 * @param norm Value to divide each pixel in Filter with.
	 * @param offset Changes the intensity of each pixel in new Image.
	 * @return Filtered Image.
	 */
	public static ImageData convolve(ImageData inData, int imageType, int[][] filter, int norm, int offset) {
		
		ImageData outData = (ImageData) inData.clone();
				
		for (int v = 0; v < outData.height; v++) {
			for (int u = 0; u < outData.width; u++) {
				
				RGB sum = new RGB(0, 0, 0);
				
				int pixel = 0;
				RGB currentPixel = null;
								
				for (int i = 0; i < filter.length; i++) {
					for (int j = 0; j < filter[i].length; j++) {

						int locationX = (u-1)+i, locationY = (v-1)+j;						
						
						// Miroring of out-of-bounds pixels
						if (locationX < 0) locationX = locationX+filter.length-1;
						if (locationY < 0) locationY = locationY+filter[i].length-1;
						if (locationX > outData.width-1) locationX = locationX-filter.length-1;
						if (locationY > outData.height-1) locationY = locationY-filter[i].length-1;
						
//						System.out.println(locationX + " | " + locationY);
						
						pixel = inData.getPixel(locationX, locationY);
						currentPixel = inData.palette.getRGB(pixel);
						sum.red += (currentPixel.red * filter[i][j]) / norm + offset;
						sum.green += (currentPixel.green * filter[i][j]) / norm + offset;
						sum.blue += (currentPixel.blue * filter[i][j]) / norm + offset;
					}
				}
								
				sum = clamp(sum);
								
				outData.setPixel(u, v, inData.palette.getPixel(sum));
			}
		}
		
		return outData;
	}
	
	/**
	 * Moves the color values for the pixel into bounds between 0 and 255.
	 * @param raw Color values of the pixel.
	 * @return Color values within acceptable intensity limits.
	 */
	private static RGB clamp(RGB raw) {
		int MaxIntensity = 0xff;
		int MinIntensity = 0x0;
		
		if (raw.red < MinIntensity) raw.red = MinIntensity;
		else if (raw.red > MaxIntensity) raw.red = MaxIntensity;
		if (raw.green < MinIntensity) raw.green = MinIntensity;
		else if (raw.green > MaxIntensity) raw.green = MaxIntensity;
		if (raw.blue < MinIntensity) raw.blue = MinIntensity;
		else if (raw.blue > MaxIntensity) raw.blue = MaxIntensity;
		
		return raw;
	}
	
}
