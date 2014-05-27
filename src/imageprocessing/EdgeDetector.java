package imageprocessing;

import main.PicsiSWT;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

import utils.InterpolationLib;

public class EdgeDetector implements IImageProcessor {

	@Override
	public boolean isEnabled(int imageType) {
		return imageType != PicsiSWT.IMAGE_TYPE_INDEXED;
	}

	@Override
	public Image run(Image input, int imageType) {
		ImageData inData = input.getImageData();
		
		int[][] filter = {
//				{-1, 0, 1},
//				{-1, 0, 1},
//				{-1, 0, 1},
				{ 0, 1, 0},
				{ 1,-4, 1},
				{ 0, 1, 0},
		};
		
		Image out = new Image(input.getDevice(), InterpolationLib.convolve(inData, imageType, filter, 1, 5));
				
		return 	out;
	}

}
