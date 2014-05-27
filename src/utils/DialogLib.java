package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class DialogLib {
	
	/**
	 * Prompts user for input.
	 * @param windowName Name of the dialog window.
	 * @param text Text in the dialog window.
	 * @return User input.
	 */
	public static String createUserInputDialog(String windowTitle) {
		// TODO		
		CustomDialog dialog = new CustomDialog(windowTitle);

		dialog.open();
		String temp = dialog.getText();
//		dialog.getParent().dispose();
//		shell.setDefaultButton(ok);			
//		shell.dispose();
//		cancel.dispose();
//		ok.dispose();
		
		return temp;
	}
	
	public static class CustomDialog extends Dialog {
		
		private Text t = null;
		
		public CustomDialog(String windowName) {
			super(new Shell());
			
		}
		
		public void open() {
			createContents();
	        getParent().pack();
	        getParent().open();
	        Display display = getParent().getDisplay();
	        while (!getParent().isDisposed())
	        {
	            if (!display.readAndDispatch())
	            {
	                display.sleep();
	            }
	        }
	    }
		
		public String getText() {
			return t.getText();
		}
		
		public void createContents() {
			GridLayout grid = new GridLayout(2, true);
			Shell shell = getParent();
			shell.setLayout(grid);
			
			t = new Text(shell, SWT.PUSH);
			t.setEditable(true);
			
			Button ok = new Button(shell, SWT.PUSH);
	        ok.setText("OK");
	        ok.addSelectionListener(new SelectionAdapter()
	        {
	            public void widgetSelected(SelectionEvent event)
	            {
	                getParent().close();
	            }
	        });
	        
	        Button cancel = new Button(shell, SWT.PUSH);
	        cancel.setText("Cancel");
	        cancel.addSelectionListener(new SelectionAdapter()
	        {
	            public void widgetSelected(SelectionEvent event)
	            {
	            	getParent().close();
	            }
	        });
	        
			shell.setDefaultButton(ok);			
		}
	}
}
