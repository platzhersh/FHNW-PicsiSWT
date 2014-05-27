package imageprocessing;

import main.PicsiSWT;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import utils.InterpolationLib;

public class Filter implements IImageProcessor {

	@Override
	public boolean isEnabled(int imageType) {
		return imageType != PicsiSWT.IMAGE_TYPE_INDEXED;
	}

	@Override
	public Image run(Image input, int imageType) {

		ImageData inData = input.getImageData();
		Image out = null;
		
		// Blur
//		int[][] filter = {
//				{-1, 0, 1},
//				{-1, 0, 1},
//				{-1, 0, 1}
//		};
		
		// Laplace Filter
		int[][] filter = {
				{ 0, 0,-1, 0, 0},
				{ 0,-1, 2,-1, 0},
				{-1, 2,10, 2,-1},
				{ 0,-1, 2,-1, 0},
				{ 0, 0,-1, 0, 0}
		};
		
//		out = new Image(input.getDevice(), InterpolationLib.convolve(inData, imageType, filter, 9, 0));	// for Blur filter
		out = new Image(input.getDevice(), InterpolationLib.convolve(inData, imageType, filter, 10, 0));	// for Laplace Filter
				
		return 	out;
	}

}
