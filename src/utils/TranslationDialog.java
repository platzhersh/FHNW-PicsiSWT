package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class TranslationDialog extends Dialog {

	public static enum IntOptions {NONE_S2T, NONE_T2S, NEAREST_NEIBOR, BILINEAR};
	
    protected Object result;
    protected Shell shell;

    public TranslationDialog(Shell parent, int style) {
        super(parent, style);
    }

    public TranslationDialog(Shell parent) {
        this(parent, SWT.NONE);
    }

    public Object open() {
        createContents();
        shell.pack();
        shell.open();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    protected void createContents() {
    	GridData data = new GridData();
        data.horizontalSpan = 3;
    	
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setSize(450, 300);
        shell.setText("Translation");
        shell.setLayout(new GridLayout());
        
        Label rotLabel = new Label(shell, SWT.NONE);
        rotLabel.setText("Rotation");
        rotLabel.pack();
        
        final Spinner rotate = new Spinner(shell, SWT.BORDER);
        rotate.setMaximum(360);
        rotate.setMinimum(-360);
        rotate.setIncrement(5);
        rotate.setSelection(0);
        rotate.setLayoutData(data);
        rotate.pack();
        
        Label scaLabel = new Label(shell, SWT.NONE);
        scaLabel.setText("Scale");
        scaLabel.pack();
        
        final Spinner scale = new Spinner(shell, SWT.BORDER);
        scale.setDigits(3);	// Anzahl Nachkommastellen
        scale.setMaximum(10000);
        scale.setMinimum(1);
        scale.setIncrement(10);
        scale.setSelection(1000);
        scale.setLayoutData(data);
        scale.pack();
        
        Label intLabel = new Label(shell, SWT.NONE);
        intLabel.setText("Interpolation");
        intLabel.pack();
        
        final Button[] interpolate = new Button[3];
        
        interpolate[0] = new Button(shell, SWT.RADIO);
        interpolate[0].setData(IntOptions.NONE_S2T);
        interpolate[0].setText("None (Source-to-Target)");
        interpolate[0].setSelection(true);
        interpolate[0].pack();
        
        interpolate[0] = new Button(shell, SWT.RADIO);
        interpolate[0].setData(IntOptions.NONE_T2S);
        interpolate[0].setText("None (Target-to-Source)");
        interpolate[0].pack();
        
        interpolate[1] = new Button(shell, SWT.RADIO);
        interpolate[1].setData(IntOptions.NEAREST_NEIBOR);
        interpolate[1].setText("Nearest Neighbor");
        interpolate[1].pack();
        
        interpolate[2] = new Button(shell, SWT.RADIO);
        interpolate[2].setData(IntOptions.BILINEAR);
        interpolate[2].setText("Bilinear");
        interpolate[2].pack();
        
        Button ok = new Button(shell, SWT.PUSH);
        ok.setText("OK");
        ok.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent event) {
        		TranslationData ret = new TranslationData();
        		ret.setScale(((double) scale.getSelection()) / 1000);
        		ret.setRotation(rotate.getSelection());
        		for (Button b : interpolate) {
        			if (b.getSelection() == true)
        				ret.setInterpolation((IntOptions) b.getData());
        		}
        		result = ret;
        		shell.close();
        	}
		});
        ok.setSize(100, 100);
        ok.pack();
    }
    
    public class TranslationData {
    	int rotation = 0;
    	double scale = 1.0;
    	IntOptions interpolation = IntOptions.NONE_S2T;
    	
    	private void setRotation(int r) {
    		rotation = r;
    	}
    	
    	public int getRotation() {
    		return rotation;
    	}
    	
    	private void setScale(double s) {
    		scale = s;
    	}
    	
    	public double getScale() {
    		return scale;
    	}
    	
    	private void setInterpolation(IntOptions i) {
    		interpolation = i;
    	}
    	
    	public IntOptions getInterpolation() {
    		return interpolation;
    	}
    }
}