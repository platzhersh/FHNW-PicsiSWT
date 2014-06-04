package imageprocessing;

import main.PicsiSWT;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

public class Inverter implements imageprocessing.IImageProcessor {

	@Override
	public boolean isEnabled(int imageType) {
		return imageType != PicsiSWT.IMAGE_TYPE_INDEXED;
	}

	@Override
	public Image run(Image input, int imageType) {
		ImageData inData = input.getImageData();

//		byte[] data = inData.data;
//		for (int i = 0; i < inData.data.length; i++) {
//			inData.data[i] = (byte) ~inData.data[i];
//		}
		
		if (imageType == PicsiSWT.IMAGE_TYPE_GRAY) {
			for (int v = 0; v < inData.height; v++) {
				for (int u = 0; u < inData.width; u++) {
					int pixel = inData.getPixel(u, v);
					RGB rgb = inData.palette.getRGB(pixel);
					int gray = (rgb.red + rgb.green + rgb.blue) / 3;
					int invGray = 255 - gray;
					RGB inv = new RGB(invGray, invGray, invGray);
					inData.setPixel(u, v, inData.palette.getPixel(inv));
				}
			}
		} else {
			for (int v = 0; v < inData.height; v++) {
				for (int u = 0; u < inData.width; u++) {
					int pixel = inData.getPixel(u, v);
					RGB rgb = inData.palette.getRGB(pixel);
					rgb.blue = 255 - rgb.blue;
					rgb.red = 255 - rgb.red;
					rgb.green = 255 - rgb.green;
					inData.setPixel(u, v, inData.palette.getPixel(rgb));
				}
			}
		}

		return new Image(input.getDevice(), inData);
	}
}
