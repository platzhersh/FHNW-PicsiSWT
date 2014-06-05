package imageprocessing;

import java.awt.Point;

import main.PicsiSWT;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import utils.Matrix;
import utils.Vector;

public class Rotator implements imageprocessing.IImageProcessor {

	@Override
	public boolean isEnabled(int imageType) {
		return imageType != PicsiSWT.IMAGE_TYPE_INDEXED;
	}

	@Override
	public Image run(Image input, int imageType) {
		
		ImageData inData = (ImageData) input.getImageData().clone();
		ImageData outData = input.getImageData();

		int alpha = 45; // Drehwinkel
		double scale = 0.5;
		
		Point center = new Point(inData.width / 2, inData.height / 2); // Koordinaten des Zentrums
		
		Matrix trl = Matrix.translation(center.x, center.y)
				.times(Matrix.scale(scale))
				.times(Matrix.rotation(alpha))
				.times(Matrix.translation(-center.x, -center.y));
		
		for (int v = 0; v < outData.height; v++) {
			for (int u = 0; u < outData.width; u++) {
				
				Vector pixel = new Vector(u, v, 1);
				Vector pixelI = trl.times(pixel);
				
				// nearest neighbor interpolation
				if ((int) pixelI.x(0) > -1 && (int) pixelI.x(1) > -1
						&& (int) pixelI.x(0) < inData.width - 1 && (int) pixelI.x(1) < inData.height - 1) {
					outData.setPixel(u, v, inData.getPixel((int) pixelI.x(0), (int) pixelI.x(1))); // gültiges pixel in der Quelle
				} else {
					outData.setPixel(u, v, 0x0); // kein gültiges Pixel, definiere schwarz (Ecke)
				}
			}
		}
		
		return new Image(input.getDevice(), outData);
	}
}
