/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.core.ui.frameworks.errordisplay;

import org.eclipse.birt.core.ui.frameworks.taskwizard.composites.MessageComposite;
import org.eclipse.birt.core.ui.i18n.Messages;
import org.eclipse.birt.core.ui.utils.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * ErrorDialog
 */
public class ErrorDialog implements SelectionListener {

	// CONSTANTS
	public static final String OPTION_ACCEPT = "ACCEPT"; //$NON-NLS-1$
	public static final String OPTION_CANCEL = "CANCEL"; //$NON-NLS-1$
	public static final int MAX_TRACE_DEPTH = 2;
	public static final int DEFAULT_WIDTH = 450;
	public static final int DEFAULT_HEIGHT = 0;
	public static final int MAX_HEIGHT = 200;

	// UI COMPONENTS
	private transient Display display = Display.getDefault();
	private transient Shell shell = null;
	private transient Label lblImage = null;
	private transient Composite cmpContainer = null;
	private transient Composite cmpDetails = null;
	private transient StackLayout slDetails = null;
	private transient Group grpDetails = null;
	private transient Text txtDetails = null;
	private transient Composite cmpDummy = null;
	private transient MessageComposite mcSheetHeading = null;
	private transient Button btnOK = null;
	private transient Button btnDetails = null;
	private transient Text txtProblems = null;
	private transient Button btnCancel = null;
	private transient Group grpProblems = null;

	// COMMON DATA FIELDS
	private transient String sMessage = null;
	private transient String sSelection = OPTION_CANCEL;
	private transient boolean bError = true;

	// ERROR DIALOG DATA FIELDS
	private transient String sErrors = null;
	private transient String sFixes = null;

	// EXCEPTION DIALOG DATA FIELDS
	private transient String sExceptionMessage = null;
	private transient String sTrace = null;
	private transient Shell shellParent = null;

	private void init(String sTitle) {
		display = Display.getDefault();
		if (shellParent == null) {
			shell = new Shell(display, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		} else {
			shell = new Shell(shellParent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		}
		shell.setText(sTitle);
		shell.setLayout(new GridLayout());
		placeComponents();
		// CENTER THE DIALOG ON SCREEN
		// Make the dialog always display on primary monitor when display
		// adapter supports multiple monitors.
		UIHelper.centerOnScreen(shell);

		shell.setDefaultButton(btnOK);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public ErrorDialog(Shell shellParent, String sTitle, String sMessage, String[] sErrors, String[] sFixes) {
		this.shellParent = shellParent;
		this.sMessage = sMessage;
		this.sErrors = getOrganizedErrors(sErrors);
		this.sFixes = getOrganizedFixes(sFixes);
		this.bError = true;
		init(sTitle);
	}

	public ErrorDialog(Shell shellParent, String sTitle, String sMessage, Throwable t) {
		this.shellParent = shellParent;
		this.sMessage = sMessage;
		this.sExceptionMessage = t.getLocalizedMessage();
		if (sExceptionMessage == null) {
			sExceptionMessage = t.toString();
		}
		this.sTrace = getOrganizedTrace(t);
		this.bError = false;
		init(sTitle);
	}

	private void placeComponents() {
		// CONTAINER
		cmpContainer = new Composite(shell, SWT.NONE);
		{
			// CONTAINER LAYOUT
			GridLayout glDialog = new GridLayout(3, false);
			// glDialog.numColumns = 3;
			glDialog.marginWidth = 6;
			glDialog.marginHeight = 6;
			glDialog.horizontalSpacing = 5;
			glDialog.verticalSpacing = 2;
			cmpContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
			cmpContainer.setLayout(glDialog);
		}

		// MESSAGE LABEL
		mcSheetHeading = new MessageComposite(cmpContainer, "", sMessage, "", true); //$NON-NLS-1$ //$NON-NLS-2$
		GridData gdMSGHeading = new GridData(GridData.FILL_HORIZONTAL);
		gdMSGHeading.heightHint = 25;
		gdMSGHeading.horizontalSpan = 3;
		mcSheetHeading.setLayoutData(gdMSGHeading);
		mcSheetHeading.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		// ICON
		lblImage = new Label(cmpContainer, SWT.NONE);
		lblImage.setImage(display.getSystemImage(SWT.ICON_ERROR));
		// lblImage.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );

		// PROBLEMS LABEL
		grpProblems = new Group(cmpContainer, SWT.NONE);
		{
			GridData gdGrpProblems = new GridData(GridData.FILL_HORIZONTAL);
			gdGrpProblems.horizontalSpan = 2;
			gdGrpProblems.heightHint = 60;
			grpProblems.setLayoutData(gdGrpProblems);
			GridLayout layout = new GridLayout();
			layout.marginWidth = 2;
			layout.marginHeight = 2;
			grpProblems.setLayout(layout);
		}

		txtProblems = new Text(grpProblems, SWT.WRAP | SWT.V_SCROLL);
		{
			txtProblems.setEditable(false);
			txtProblems.setLayoutData(new GridData(GridData.FILL_BOTH));
		}

		// DETAILS BUTTON
		new Label(cmpContainer, SWT.NONE);

		btnDetails = new Button(cmpContainer, SWT.TOGGLE);
		GridData gdBtnDetails = new GridData();
		gdBtnDetails.horizontalSpan = 2;
		btnDetails.setLayoutData(gdBtnDetails);
		btnDetails.addSelectionListener(this);

		// SOLUTIONS LABEL
		new Label(cmpContainer, SWT.NONE);

		slDetails = new StackLayout();

		cmpDetails = new Composite(cmpContainer, SWT.NONE);
		GridData gdCmpDetails = new GridData(GridData.FILL_BOTH);
		gdCmpDetails.horizontalSpan = 2;
		gdCmpDetails.heightHint = DEFAULT_HEIGHT;
		cmpDetails.setLayoutData(gdCmpDetails);
		cmpDetails.setLayout(slDetails);

		cmpDummy = new Composite(cmpDetails, SWT.NONE);

		grpDetails = new Group(cmpDetails, SWT.NONE);
		FillLayout flSolutions = new FillLayout();
		flSolutions.marginWidth = 5;
		flSolutions.marginHeight = 5;
		grpDetails.setLayout(flSolutions);

		txtDetails = new Text(grpDetails, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		txtDetails.setEditable(false);

		// BUTTON PANEL LAYOUT
		GridLayout glButtons = new GridLayout();
		glButtons.numColumns = 2;
		glButtons.horizontalSpacing = 5;
		glButtons.marginHeight = 5;
		glButtons.marginWidth = 5;

		// BUTTON PANEL
		Composite cmpButtons = new Composite(cmpContainer, SWT.NONE);
		GridData gdCmpButtons = new GridData(GridData.FILL_HORIZONTAL);
		gdCmpButtons.horizontalSpan = 3;
		cmpButtons.setLayoutData(gdCmpButtons);
		cmpButtons.setLayout(glButtons);

		// ACCEPT BUTTON
		btnOK = new Button(cmpButtons, SWT.NONE);
		GridData gdBtnOK = null;
		if (bError) {
			gdBtnOK = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
		} else {
			gdBtnOK = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER);
		}
		btnOK.setLayoutData(gdBtnOK);
		btnOK.addSelectionListener(this);

		// CANCEL BUTTON
		if (bError) {
			btnCancel = new Button(cmpButtons, SWT.NONE);
			GridData gdBtnCancel = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);
			btnCancel.setLayoutData(gdBtnCancel);
			btnCancel.addSelectionListener(this);
		}

		if (bError) {
			grpProblems.setText(Messages.getString("ErrorDialog.text.ErrorList")); //$NON-NLS-1$
			txtProblems.setText(sErrors);
			btnDetails.setText(Messages.getString("ErrorDialog.text.ShowDetails")); //$NON-NLS-1$
			grpDetails.setText(Messages.getString("ErrorDialog.text.SuggestedFixes")); //$NON-NLS-1$
			btnOK.setText(Messages.getString("ErrorDialog.text.FixIt")); //$NON-NLS-1$
			btnCancel.setText(Messages.getString("ErrorDialog.text.ProceedWithoutFixing")); //$NON-NLS-1$

			if (sFixes == null || sFixes.length() == 0) {
				btnDetails.setEnabled(false);
			} else {
				btnDetails.setEnabled(true);
				txtDetails.setText(sFixes);
			}
		} else {
			// Commented out for the sake of clarity to users
			// grpProblems.setText( Messages.getString(
			// "ErrorDialog.text.Exception" ) ); //$NON-NLS-1$
			txtProblems.setText(sExceptionMessage);
			btnDetails.setText(Messages.getString("ErrorDialog.text.ShowTrace")); //$NON-NLS-1$
			grpDetails.setText(Messages.getString("ErrorDialog.text.StackTrace")); //$NON-NLS-1$
			btnOK.setText(Messages.getString("ErrorDialog.text.Ok")); //$NON-NLS-1$
			if (sTrace == null || sTrace.length() == 0) {
				btnDetails.setEnabled(false);
			} else {
				btnDetails.setEnabled(true);
				txtDetails.setText(sTrace);
			}
		}

		slDetails.topControl = cmpDummy;
		shell.pack();
		shell.layout();
	}

	public String getOption() {
		return sSelection;
	}

	private String getOrganizedErrors(String[] errors) {
		if (errors.length == 1) {
			return errors[0];
		}
		StringBuffer sbErrors = new StringBuffer(""); //$NON-NLS-1$
		for (int i = 0; i < errors.length; i++) {
			if (i > 0) {
				sbErrors.append("\n"); //$NON-NLS-1$
			}
			sbErrors.append((i + 1) + "] "); //$NON-NLS-1$
			sbErrors.append(errors[i]);
		}
		return sbErrors.toString();
	}

	private String getOrganizedFixes(String[] fixes) {
		StringBuffer sbFixes = new StringBuffer(""); //$NON-NLS-1$
		for (int i = 0; i < fixes.length; i++) {
			if (i > 0) {
				sbFixes.append("\n"); //$NON-NLS-1$
			}
			sbFixes.append(fixes[i]);
		}
		return sbFixes.toString();
	}

	private String getOrganizedTrace(Throwable t) {
		StringBuffer sbTrace = new StringBuffer(t.getClass().getName());
		sbTrace.append(" at:\n"); //$NON-NLS-1$
		for (int d = 0; d < MAX_TRACE_DEPTH; d++) {
			if (d > 0) {
				while (t.getCause() != null) {
					t = t.getCause();
				}
				sbTrace.append(Messages.getString("ErrorDialog.text.CausedBy") //$NON-NLS-1$
						+ t.getLocalizedMessage() + "\n"); //$NON-NLS-1$
			}
			StackTraceElement[] se = t.getStackTrace();
			for (int i = 0; i < se.length; i++) {
				if (i > 0) {
					sbTrace.append("\n"); //$NON-NLS-1$
				}
				sbTrace.append(se[i].toString());
			}
		}
		return sbTrace.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(btnOK)) {
			this.sSelection = OPTION_ACCEPT;
			shell.dispose();
		} else if (e.getSource().equals(btnCancel)) {
			this.sSelection = OPTION_CANCEL;
			shell.dispose();
		} else if (e.getSource().equals(btnDetails)) {
			toggleDetails(btnDetails.getSelection());
		}
	}

	/**
	 * 
	 */
	private void toggleDetails(boolean bVisible) {
		if (bVisible) {
			slDetails.topControl = grpDetails;
			((GridData) cmpDetails.getLayoutData()).heightHint = MAX_HEIGHT;
		} else {
			slDetails.topControl = cmpDummy;
			((GridData) cmpDetails.getLayoutData()).heightHint = DEFAULT_HEIGHT;
		}
		shell.pack();
		shell.layout();
	}
}
