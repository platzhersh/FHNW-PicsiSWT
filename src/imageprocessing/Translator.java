package imageprocessing;

import java.awt.Point;

import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import main.PicsiSWT;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import utils.InterpolationLib;
import utils.Matrix;
import utils.TranslationDialog;
import utils.Vector;

public class Translator implements IImageProcessor {

	@Override
	public boolean isEnabled(int imageType) {
		return imageType != PicsiSWT.IMAGE_TYPE_INDEXED;
	}

	@Override
	public Image run(Image input, int imageType) {
		
		ImageData inData = (ImageData) input.getImageData().clone();
		ImageData outData = input.getImageData();
		
		int alpha = 0;
		double scale = 1.0;
		
		Shell shell = new Shell();
		TranslationDialog dialog = new TranslationDialog(shell);
				
		System.out.println(dialog.open().getClass());
		
		
//		int alpha = Integer.parseInt(JOptionPane.showInputDialog("Enter alpha"));
//		double scale = Double.parseDouble(JOptionPane.showInputDialog("Enter scale"));
		
		Point center = new Point(inData.width / 2, inData.height / 2); // Center of the picture to rotate around
		
		Matrix trl = Matrix.translation(center.x, center.y)
				.times(Matrix.scale(scale))
				.times(Matrix.rotation(alpha))
				.times(Matrix.translation(-center.x, -center.y));
						
		for (int v = 0; v < outData.height; v++) {
			for (int u = 0; u < outData.width; u++) {
				
				Vector trlPixel = trl.times(new Vector(u, v, 1));
				
				if (trlPixel.x(0) > -1 && trlPixel.x(1) > -1 
						&& trlPixel.x(0) < inData.width - 1 && trlPixel.x(1) < inData.height - 1) {
										
					int pixel = 0;
					pixel = inData.getPixel((int) trlPixel.x(0), (int) trlPixel.x(1));
					RGB A = inData.palette.getRGB(pixel);
					pixel = inData.getPixel(((int) trlPixel.x(0)) + 1, (int) trlPixel.x(1));
					RGB B = inData.palette.getRGB(pixel);
					pixel = inData.getPixel((int) trlPixel.x(0), ((int) trlPixel.x(1)) + 1);
					RGB C = inData.palette.getRGB(pixel);
					pixel = inData.getPixel(((int) trlPixel.x(0)) + 1, ((int) trlPixel.x(1)) + 1);
					RGB D = inData.palette.getRGB(pixel);

					outData.setPixel(u, v, inData.palette.getPixel(
							InterpolationLib.bilinearInterpolation(A, B, C, D,
									trlPixel.x(0) - (int) trlPixel.x(0), trlPixel.x(1) - (int) trlPixel.x(1))));
				} else {
					outData.setPixel(u, v, 0x0);
				}
			}
		}
						
		return new Image(input.getDevice(), outData);
	}

}
