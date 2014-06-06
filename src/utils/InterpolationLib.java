package utils;

import java.awt.Point;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

public class InterpolationLib {

	/**
	 * Returns the RGB-value of a pixel for bilinear interpolation.
	 * @param A RGB-value of a pixel at location (u / v).
	 * @param B RGB-value of a pixel at location (u+1 / v).
	 * @param C RGB-value of a pixel at location (u / v+1).
	 * @param D RGB-value of a pixel at location (u+1 / v+1).
	 * @param a Delta x - u.
	 * @param b Delta y - v.
	 * @return RGB value of the pixel at (x/y)
	 */
	private static RGB getInterpolatedRGB(RGB A, RGB B, RGB C, RGB D, double a, double b) {
		
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
	 * Does a Source-To-Target Translation of an Image around it's center with bilinear interpolation.
	 * @param input Image to rotate.
	 * @param imageType Type of the image.
	 * @param alpha Degrees to rotate.
	 * @param scale Scale to zoom.
	 * @return ImageData of rotated image.
	 */
	public static ImageData bilinearInterpolation(Image input, int imageType, int alpha, double scale) {
		ImageData source = input.getImageData();
		ImageData target = generateBlackImage(source, source.width, source.height);
		
		Point center = new Point(source.width / 2, source.height / 2); // Center of the picture to rotate around
		
		Matrix trl = Matrix.translation(center.x, center.y)
				.times(Matrix.scale(scale))
				.times(Matrix.rotation(-alpha))	// Inverse rotation
				.times(Matrix.translation(-center.x, -center.y));
				
		for (int v = 0; v < target.height; v++) {
			for (int u = 0; u < target.width; u++) {
				
				Vector trlPixel = trl.times(new Vector(u, v, 1));
				int uSource = (int) trlPixel.x(0);
				int vSource = (int) trlPixel.x(1);
				
				if (uSource > 0 && vSource > 0 && uSource < source.width - 1 && vSource < source.height - 1) {
										
					int pixel = 0;
					pixel = source.getPixel(uSource, vSource);
					RGB A = source.palette.getRGB(pixel);
					pixel = source.getPixel(uSource + 1, vSource);
					RGB B = source.palette.getRGB(pixel);
					pixel = source.getPixel(uSource, vSource + 1);
					RGB C = source.palette.getRGB(pixel);
					pixel = source.getPixel(uSource + 1, vSource + 1);
					RGB D = source.palette.getRGB(pixel);
					
					target.setPixel(u, v, source.palette.getPixel(getInterpolatedRGB(A, B, C, D,
									trlPixel.x(0) - uSource, trlPixel.x(1) - vSource)));
				}
			}
		}
		
		return target;
	}
	
	/**
	 * Does a Target-To-Source Translation of an Image around it's center.
	 * @param input Image to rotate.
	 * @param imageType Type of the image.
	 * @param alpha Degrees to rotate.
	 * @param scale Scale to zoom.
	 * @return ImageData of rotated image.
	 */
	public static ImageData sourceToTargetTranslation(Image input, int imageType, int alpha, double scale) {
		ImageData source = input.getImageData();
		ImageData target = generateBlackImage(source, source.width, source.height);
		
		Point center = new Point(source.width / 2, source.height / 2); // Center of the picture to rotate around
		
		Matrix trl = Matrix.translation(center.x, center.y)
				.times(Matrix.scale(scale))
				.times(Matrix.rotation(alpha))
				.times(Matrix.translation(-center.x, -center.y));
		
		for (int v = 0; v < source.height; v++) {
			for (int u = 0; u < source.width; u++) {
				
				Vector trlPixel = trl.times(new Vector(u, v, 1));
				
				if (trlPixel.x(0) > -1 && trlPixel.x(1) > -1 
						&& trlPixel.x(0) < target.width - 1 && trlPixel.x(1) < target.height - 1) {
					int pixel = source.getPixel(u, v);
					target.setPixel((int) trlPixel.x(0), (int) trlPixel.x(1), pixel);
				}
			}
		}
				
		return target;
	}
	
	private static ImageData generateBlackImage(ImageData input, int width, int height) {
		ImageData data = (ImageData) input.clone();
		
		for (int v = 0; v < data.height; v++) {
			for (int u = 0; u < data.width; u++) {
				data.setPixel(u, v, 0x0);
			}
		}
		return data;
	}
	
	/**
	 * Does a Source-To-Target Translation of an Image around it's center with nearest neighbor interpolation.
	 * @param input Image to rotate.
	 * @param imageType Type of the image.
	 * @param alpha Degrees to rotate.
	 * @param scale Scale to zoom.
	 * @return ImageData of rotated image.
	 */
	public static ImageData nearestNeighbor(Image input, int imageType, int alpha, double scale) {
		ImageData source = input.getImageData();
		ImageData target = generateBlackImage(source, source.width, source.height);
		
		Point center = new Point(source.width / 2, source.height / 2); // Center of the picture to rotate around
		
		Matrix trl = Matrix.translation(center.x, center.y)
				.times(Matrix.scale(scale))
				.times(Matrix.rotation(-alpha))	// Inverse rotation
				.times(Matrix.translation(-center.x, -center.y));
		
		for (int v = 0; v < target.height; v++) {
			for (int u = 0; u < target.width; u++) {
				
				Vector trlPixel = trl.times(new Vector(u, v, 1));
				
				if (trlPixel.x(0) > -1 && trlPixel.x(1) > -1 
						&& trlPixel.x(0) < source.width - 1 && trlPixel.x(1) < source.height - 1) {
					int pixel = source.getPixel((int) (trlPixel.x(0) + 0.5), (int) (trlPixel.x(1) + 0.5));
					target.setPixel(u, v, pixel);
				}
			}
		}
		
		return target;
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
				
				int lenFilter = filter.length/2;
								
				for (int i = 0; i < filter.length; i++) {
					for (int j = 0; j < filter[i].length; j++) {

						int locationX = (u-lenFilter)+i, locationY = (v-lenFilter)+j;						
						
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
