package imageprocessing;

import javax.swing.JOptionPane;

import main.PicsiSWT;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Shell;

import utils.FilterDialog;
import utils.FilterDialog.FilterData;
import utils.FilterDialog.IntOptions;
import utils.InterpolationLib;
import utils.TranslationDialog;
import utils.TranslationDialog.TranslationData;

public class Filter implements IImageProcessor {

	@Override
	public boolean isEnabled(int imageType) {
		return imageType != PicsiSWT.IMAGE_TYPE_INDEXED;
	}

	@Override
	public Image run(Image input, int imageType) {

		ImageData inData = input.getImageData();
		Image out = null;
		
		FilterDialog dialog = new FilterDialog(new Shell());
		FilterData data = (FilterData) dialog.open();	
		
		
		if (data.getInterpolation() == IntOptions.PREWITT) {
			int[][] filter = {
					{-1, 0, 1},
					{-1, 0, 1},
					{-1, 0, 1}
			};
			out = new Image(input.getDevice(), InterpolationLib.convolve(inData, imageType, filter, 1, 0));	// for Blur filter
		} else if (data.getInterpolation()== IntOptions.LoG) {
			int[][] filter = {
				{ 0, 0,-1, 0, 0},
				{ 0,-1, 2,-1, 0},
				{-1, 2,16, 2,-1},
				{ 0,-1, 2,-1, 0},
				{ 0, 0,-1, 0, 0}
			};
			out = new Image(input.getDevice(), InterpolationLib.convolve(inData, imageType, filter, 10, 0));	// for Laplace Filter
		} else if (data.getInterpolation()== IntOptions.BOX) {
			int[][] filter = {
					{ 1,1,1},
					{ 1,1,1},
					{ 1,1,1}
				};
			out = new Image(input.getDevice(), InterpolationLib.convolve(inData, imageType, filter, 10, 0));
		} else if (data.getInterpolation()== IntOptions.GAUSS) {
			int[][] filter = {
					{ 0, 1, 2, 1, 0},
					{ 1,3,5,3,1},
					{2,5,9,5,2},
					{1,3,5,3,1},
					{0,1,2,1,0}
				};
			out = new Image(input.getDevice(), InterpolationLib.convolve(inData, imageType, filter, 10, 0));
		}
				
		return 	out;
	}

}
