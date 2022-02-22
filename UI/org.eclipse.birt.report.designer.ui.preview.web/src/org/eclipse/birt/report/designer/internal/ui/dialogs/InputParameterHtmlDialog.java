/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * When tries to delete the data source and data set, if other element
 * references them, this dialog will show up. Click the node and position the
 * node in editor to choose the node to be deleted.
 *
 *
 */
public class InputParameterHtmlDialog extends Dialog {

	/**
	 * The title of the dialog
	 */
	private String title;

	/**
	 * Default dialog title
	 */
	public static final String TITLE = Messages.getString("InputParameterDailog.dialog.title"); //$NON-NLS-1$

	/**
	 * File URI String
	 */
	private String uri;

	/**
	 * Browser object
	 */
	private Browser browser;

	/**
	 * Display options
	 */
	private HashMap options;

	/**
	 * Taget Browser object
	 */
	// private Browser target;

	// parameter viewer model.
	public static final String VIEWER_PARAMETER = "parameter"; //$NON-NLS-1$

	// frameset viewer model.
	public static final String VIEWER_FRAMESET = "frameset"; //$NON-NLS-1$

	// running viewer model.
	public static final String VIEWER_RUN = "run"; //$NON-NLS-1$

	// return from Browser Closed
	public static final int RETURN_CODE_BROWSER_CLOSED = 1001;

	// status text for Close event
	public static final String STATUS_CLOSE = "close"; //$NON-NLS-1$

	// status text for Cancel event
	public static final String STATUS_CANCEL = "cancel"; //$NON-NLS-1$

	/**
	 * Constructor.
	 *
	 * @param parent The parent shell
	 * @param title  The title of the dialog
	 */
	public InputParameterHtmlDialog(Shell parent, String title, String uri, Browser target) {
		super(parent);
		this.title = title;
		this.uri = uri;
		// this.target = target;
	}

	/**
	 * Self-defined string escaper
	 */
	protected String stringEscape(String string) {
		return string.replace("&", "&&"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * override the super method.
	 *
	 * @param parent The parent
	 */
	@Override
	protected Control createButtonBar(Composite parent) {
		return parent;
	}

	/**
	 * Creates the dialog area.
	 *
	 * @param parent The parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(ColorManager.getColor(219, 228, 238));
		Composite composite = (Composite) super.createDialogArea(parent);

		GridData gd = new GridData();
		gd.widthHint = 520;
		gd.heightHint = 395;

		composite.setLayoutData(gd);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;

		composite.setLayout(layout);

		browser = new Browser(composite, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		browser.setLayoutData(gd);

		// Listen window close event
		browser.addCloseWindowListener(new CloseWindowListener() {

			@Override
			public void close(final WindowEvent event) {
				((Browser) event.widget).getShell().close();
				List parameters = SessionHandleAdapter.getInstance().getReportDesignHandle().getParameters()
						.getContents();
				if (parameters != null && parameters.size() > 0) {
					setReturnCode(RETURN_CODE_BROWSER_CLOSED);
				}
			}
		});

		// Listen window status changed event
		browser.addStatusTextListener(new StatusTextListener() {

			@Override
			public void changed(StatusTextEvent event) {
				if (STATUS_CLOSE.equalsIgnoreCase(event.text)) {
					((Browser) event.widget).getShell().close();
					List parameters = SessionHandleAdapter.getInstance().getReportDesignHandle().getParameters()
							.getContents();
					if (parameters != null && parameters.size() > 0) {
						// refresh the report
						setReturnCode(RETURN_CODE_BROWSER_CLOSED);
					}
				} else if (STATUS_CANCEL.equalsIgnoreCase(event.text)) {
					// If fire cancel event, close parameter dialog directly
					if (!((Browser) event.widget).getShell().isDisposed()) {
						((Browser) event.widget).getShell().close();
					}
				}
			}
		});

		browser.addTitleListener(new TitleListener() {

			@Override
			public void changed(TitleEvent event) {
				if (STATUS_CANCEL.equalsIgnoreCase(event.title)) {
					// If fire cancel event, close parameter dialog directly
					if (!((Browser) event.widget).getShell().isDisposed()) {
						((Browser) event.widget).getShell().close();
					}
				}
			}
		});

		display();

		return composite;
	}

	/**
	 * Refresh swt browser
	 */
	public void display() {
		if (browser != null && uri != null && uri.length() > 0) {
			if (this.options == null) {
				this.options = new HashMap();
				this.options.put(WebViewer.SERVLET_NAME_KEY, VIEWER_PARAMETER);
				this.options.put(WebViewer.FORMAT_KEY, WebViewer.HTML);
			}
			this.options.put(WebViewer.RESOURCE_FOLDER_KEY, ReportPlugin.getDefault().getResourceFolder());
			WebViewer.display(uri, browser, this.options);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog#initDialog()
	 */

	protected boolean initDialog() {
		return true;
	}

	/**
	 * Configures the given shell in preparation for opening this window in it.
	 * <p>
	 * The <code>BaseDialog</code> overrides this framework method sets in order to
	 * set the title of the dialog.
	 * </p>
	 *
	 * @param shell the shell
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	/**
	 * Sets the title of the dialog
	 */

	public void setTitle(String newTitle) {
		title = newTitle;
		if (getShell() != null) {
			getShell().setText(newTitle);
		}
	}

	/**
	 * Gets the title of the dialog
	 *
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
