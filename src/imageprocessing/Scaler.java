package imageprocessing;

import main.PicsiSWT;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

import utils.InterpolationLib;
import utils.Matrix;
import utils.Vector;

public class Scaler implements IImageProcessor {

	@Override
	public boolean isEnabled(int imageType) {
		return imageType != PicsiSWT.IMAGE_TYPE_INDEXED;
	}

	@Override
	public Image run(Image input, int imageType) {
		ImageData inData = (ImageData) input.getImageData().clone();
		ImageData outData = input.getImageData();

		double scale = 0.75;
		Matrix trl = Matrix.scale(scale);

		for (int v = 0; v < outData.height; v++) {
			for (int u = 0; u < outData.width; u++) {
				Vector trlPixel = trl.times(new Vector(u, v, 1));
				if (trlPixel.x(0) > -1 && trlPixel.x(2) > -1 
						&& trlPixel.x(0) < inData.width && trlPixel.x(1) < inData.height) {
					
					int pixel = 0;
					pixel = inData.getPixel((int) trlPixel.x(0), (int) trlPixel.x(1));
					RGB A = inData.palette.getRGB(pixel);
					pixel = inData.getPixel(((int) trlPixel.x(0)) + 1, (int) trlPixel.x(1));
					RGB B = inData.palette.getRGB(pixel);
					pixel = inData.getPixel((int) trlPixel.x(0), ((int) trlPixel.x(1)) + 1);
					RGB C = inData.palette.getRGB(pixel);
					pixel = inData.getPixel(((int) trlPixel.x(0)) + 1, ((int) trlPixel.x(1)) + 1);
					RGB D = inData.palette.getRGB(pixel);

					outData.setPixel(u,v,
							inData.palette.getPixel(InterpolationLib.bilinearInterpolation(A, B, C, D, trlPixel.x(0)
									- (int) trlPixel.x(0), trlPixel.x(1) - (int) trlPixel.x(1))));
				} else {
					outData.setPixel(u, v, 0x0);
				}
			}
		}
		return new Image(input.getDevice(), outData);
	}
}
