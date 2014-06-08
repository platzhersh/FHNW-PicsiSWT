package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import utils.TranslationDialog.IntOptions;
import utils.TranslationDialog.TranslationData;

public class FilterDialog extends Dialog {

	public static enum IntOptions {PREWITT, LoG, BOX, GAUSS};
	
    protected Object result;
    protected Shell shell;

    public FilterDialog(Shell parent, int style) {
        super(parent, style);
    }

    public FilterDialog(Shell parent) {
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
        
                       
        final Button[] interpolate = new Button[4];
        
        interpolate[0] = new Button(shell, SWT.BUTTON1);
        interpolate[0].setData(IntOptions.PREWITT);
        interpolate[0].setText("Prewitt");
        interpolate[0].setSelection(true);
        interpolate[0].addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent event) {
        		FilterData ret = new FilterData();
        		ret.setFilterOption(IntOptions.PREWITT);
        		result = ret;
        		shell.close();
        	}
		});        
        interpolate[0].pack();
        
        interpolate[1] = new Button(shell, SWT.BUTTON1);
        interpolate[1].setData(IntOptions.LoG);
        interpolate[1].setText("Laplace Gauss (LoG)");
        interpolate[1].addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent event) {
        		FilterData ret = new FilterData();
        		ret.setFilterOption(IntOptions.LoG);
        		result = ret;
        		shell.close();
        	}
		});
        interpolate[1].pack();
        
        interpolate[2] = new Button(shell, SWT.BUTTON1);
        interpolate[2].setData(IntOptions.BOX);
        interpolate[2].setText("Boxfilter");
        interpolate[2].addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent event) {
        		FilterData ret = new FilterData();
        		ret.setFilterOption(IntOptions.BOX);
        		result = ret;
        		shell.close();
        	}
		});
        interpolate[2].pack();
        
        interpolate[3] = new Button(shell, SWT.BUTTON1);
        interpolate[3].setData(IntOptions.GAUSS);
        interpolate[3].setText("Gauss");
        interpolate[3].addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent event) {
        		FilterData ret = new FilterData();
        		ret.setFilterOption(IntOptions.GAUSS);
        		result = ret;
        		shell.close();
        	}
		});
        interpolate[3].pack();
    }
    
    public class FilterData {
    	IntOptions filterOption = IntOptions.PREWITT;
    	
    	
    	private void setFilterOption(IntOptions i) {
    		filterOption = i;
    	}
    	
    	public IntOptions getInterpolation() {
    		return filterOption; 
    	}
    }
    
}