package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TranslationDialog extends Dialog {
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
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    protected void createContents() {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setSize(450, 300);
        shell.setText("Translation");
        shell.setLayout(new GridLayout());
        
        Button rotate = new Button(shell, SWT.CHECK);
        rotate.setText("Rotaton");
        rotate.pack();
        
        Button[] interpolate = new Button[3];
        
        interpolate[0] = new Button(shell, SWT.RADIO);
        interpolate[0].setText("None");
        interpolate[0].setSelection(true);
        interpolate[0].pack();
        
        interpolate[1] = new Button(shell, SWT.RADIO);
        interpolate[1].setText("Nearest Neighbor");
        interpolate[1].pack();
        
        interpolate[2] = new Button(shell, SWT.RADIO);
        interpolate[2].setText("Bilinear");
        interpolate[2].pack();
    }
}