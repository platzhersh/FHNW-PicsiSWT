package imageprocessing;

import main.PicsiSWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Shell;
import utils.InterpolationLib;
import utils.TranslationDialog;
import utils.TranslationDialog.IntOptions;
import utils.TranslationDialog.TranslationData;

public class Translator implements IImageProcessor {

	@Override
	public boolean isEnabled(int imageType) {
		return imageType != PicsiSWT.IMAGE_TYPE_INDEXED;
	}

	@Override
	public Image run(Image input, int imageType) {
		
		ImageData outData = input.getImageData();
		
		TranslationDialog dialog = new TranslationDialog(new Shell());
		TranslationData data = (TranslationData) dialog.open();
		
		if (data != null) {
			int alpha = data.getRotation();
			double scale = data.getScale();
				
			if (data.getInterpolation() == IntOptions.NONE_S2T) {
				outData = InterpolationLib.sourceToTargetTranslation(input, imageType, alpha, scale);
			} else if (data.getInterpolation() == IntOptions.NONE_T2S) {
				outData = InterpolationLib.targetToSourceTranslation(input, imageType, alpha, scale);
			}  else if (data.getInterpolation() == IntOptions.NEAREST_NEIBOR) {
				outData = InterpolationLib.nearestNeighbor(input, imageType, alpha, scale);
			} else if (data.getInterpolation() == IntOptions.BILINEAR) {
				outData = InterpolationLib.bilinearInterpolation(input, imageType, alpha, scale);
			}
		}
		
		return new Image(input.getDevice(), outData);
	}

}
