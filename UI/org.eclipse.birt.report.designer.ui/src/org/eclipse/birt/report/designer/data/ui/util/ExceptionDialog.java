
package org.eclipse.birt.report.designer.data.ui.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Exception dialog that displays an exception message in a message box, and
 * prints a stack trace in an expandable text box.
 * 
 * @author xwu
 */
public class ExceptionDialog extends IconAndMessageDialog {

	private Button _detailsButton;
	private Text _textArea;
	private String _title;
	private boolean _textCreated = false;
	private Throwable _exception;
	private Display _display;

	/**
	 * @param parentShell
	 */
	public ExceptionDialog(Shell parentShell, String title, String msg, Throwable ex) {
		super(parentShell);
		this._title = title;
		this.message = msg;
		this._exception = ex;
		if (parentShell != null)
			this._display = parentShell.getDisplay();
		else
			this._display = PlatformUI.getWorkbench().getDisplay().getActiveShell().getDisplay();

		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	/*
	 * (non-Javadoc) Method declared in Window.
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(_title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IconAndMessageDialog#getImage()
	 */
	protected Image getImage() {
		return _display.getSystemImage(SWT.ICON_ERROR);
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Details buttons
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		_detailsButton = createButton(parent, IDialogConstants.DETAILS_ID, IDialogConstants.SHOW_DETAILS_LABEL, false);
	}

	// If Details button is pressed, toggle the detail area
	// Otherwise, call the super handler
	protected void buttonPressed(int id) {
		if (id == IDialogConstants.DETAILS_ID) {
			// was the details button pressed?
			toggleDetailsArea();
		} else {
			super.buttonPressed(id);
		}
	}

	/**
	 * Toggles the unfolding of the details area. This is triggered by the user
	 * pressing the details button.
	 */
	private void toggleDetailsArea() {
		Point windowSize = getShell().getSize();
		Point oldSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);

		if (_textCreated) {
			_textArea.dispose();
			_textCreated = false;
			_detailsButton.setText(IDialogConstants.SHOW_DETAILS_LABEL);
		} else {
			_textArea = createTextArea((Composite) getContents());
			_detailsButton.setText(IDialogConstants.HIDE_DETAILS_LABEL);
		}

		Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);

		getShell().setSize(new Point(windowSize.x, windowSize.y + (newSize.y - oldSize.y)));

	}

	protected Control createDialogArea(Composite parent) {

		createMessageArea(parent);

		// create a composite with standard margins and spacing
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.numColumns = 2;
		composite.setLayout(layout);
		GridData childData = new GridData(GridData.FILL_BOTH);
		childData.horizontalSpan = 2;
		composite.setLayoutData(childData);
		composite.setFont(parent.getFont());
		return composite;

	}

	protected Text createTextArea(Composite parent) {
		_textArea = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		_exception.printStackTrace(pw);

		_textArea.setText(sw.toString());

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
		data.heightHint = 200;
		data.horizontalSpan = 2;
		_textArea.setLayoutData(data);
		_textArea.setFont(parent.getFont());
		_textArea.setEditable(false);
		_textCreated = true;
		return _textArea;
	}
}
