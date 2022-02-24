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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * 
 */

public abstract class AbstractPopupSheet implements ITaskPopupSheet {

	protected transient ChartWizardContext context;

	private transient Composite cmpTop = null;

	private boolean needRefresh = false;

	private String strTitle;

	public AbstractPopupSheet(String title, ChartWizardContext context, boolean needRefresh) {
		super();
		this.strTitle = title;
		this.context = context;
		this.needRefresh = needRefresh;
	}

	abstract protected Composite getComponent(Composite parent);

	/**
	 * Registers context help to this popup sheet.
	 * 
	 * @param parent top composite
	 */
	protected void bindHelp(Composite parent) {
		// Do nothing here to override
	}

	public Composite getUI(Composite parent) {
		// Cache the top composite for refresh later
		cmpTop = new Composite(parent, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			layout.horizontalSpacing = 0;
			layout.verticalSpacing = 0;
			cmpTop.setLayout(layout);
		}

		bindHelp(cmpTop);
		Composite components = getComponent(cmpTop);
		{
			GridData gd = ((GridData) components.getLayoutData());
			if (gd == null) {
				components.setLayoutData(new GridData(GridData.FILL_BOTH));
			} else {
				gd.grabExcessHorizontalSpace = true;
				gd.grabExcessVerticalSpace = true;
			}
		}

		Label lblSeparator = new Label(cmpTop, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite cmpBar = new Composite(cmpTop, SWT.NONE);
		{
			GridLayout layout = new GridLayout(2, false);
			cmpBar.setLayout(layout);
		}

		createCloseButton(cmpBar);
		createHelpControl(cmpBar);

		return cmpTop;
	}

	private ToolBar createCloseButton(Composite parent) {
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.NO_FOCUS);
		toolBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		final Cursor cursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
		toolBar.setCursor(cursor);
		toolBar.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				cursor.dispose();
			}
		});
		ToolItem item = new ToolItem(toolBar, SWT.NONE);
		item.setImage(UIHelper.getImage("icons/obj16/arrow.gif")); //$NON-NLS-1$
		item.setToolTipText(Messages.getString("AbstractPopupSheet.Label.Close")); //$NON-NLS-1$
		item.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (cmpTop != null && !cmpTop.isDisposed()) {
					cmpTop.getShell().close();
				}
			}
		});
		return toolBar;
	}

	/**
	 * Creates a new help control that provides access to context help.
	 * <p>
	 * The <code>TrayDialog</code> implementation of this method creates the
	 * control, registers it for selection events including selection, Note that the
	 * parent's layout is assumed to be a <code>GridLayout</code> and the number of
	 * columns in this layout is incremented. Subclasses may override.
	 * </p>
	 * 
	 * @param parent the parent composite
	 * @return the help control
	 */
	private Control createHelpControl(Composite parent) {
		Image helpImage = JFaceResources.getImage(Dialog.DLG_IMG_HELP);
		if (helpImage != null) {
			return createHelpImageButton(parent, helpImage);
		}
		return createHelpLink(parent);
	}

	/*
	 * Creates a button with a help image. This is only used if there is an image
	 * available.
	 */
	private ToolBar createHelpImageButton(Composite parent, Image image) {
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.NO_FOCUS);
		toolBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		final Cursor cursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
		toolBar.setCursor(cursor);
		toolBar.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				cursor.dispose();
			}
		});
		ToolItem item = new ToolItem(toolBar, SWT.NONE);
		item.setImage(image);
		item.setToolTipText(Messages.getString("AbstractPopupSheet.Label.Help")); //$NON-NLS-1$
		item.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				helpPressed();
			}
		});
		return toolBar;
	}

	/*
	 * Creates a help link. This is used when there is no help image available.
	 */
	private Link createHelpLink(Composite parent) {
		Link link = new Link(parent, SWT.WRAP | SWT.NO_FOCUS);
		link.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		link.setText("<a>" + IDialogConstants.HELP_LABEL + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		link.setToolTipText(IDialogConstants.HELP_LABEL);
		link.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				helpPressed();
			}
		});
		return link;
	}

	/*
	 * Called when the help control is invoked. This emulates the keyboard context
	 * help behavior (e.g. F1 on Windows). It traverses the widget tree upward until
	 * it finds a widget that has a help listener on it, then invokes a help event
	 * on that widget.
	 */
	private void helpPressed() {
		Control c = cmpTop.getDisplay().getFocusControl();
		while (c != null) {
			if (c.isListening(SWT.Help)) {
				c.notifyListeners(SWT.Help, new Event());
				break;
			}
			c = c.getParent();
		}
	}

	public void refreshComponent(Composite parent) {
		if (needRefresh) {
			if (cmpTop != null && !cmpTop.isDisposed()) {
				cmpTop.dispose();
			}
			getUI(parent);
			// Refresh popup sheet
			parent.layout();
			// Resize popup sheet using preferred size.
			parent.pack();
		}
	}

	protected ChartWizardContext getContext() {
		return context;
	}

	protected Chart getChart() {
		return getContext().getModel();
	}

	protected void setTitle(String title) {
		this.strTitle = title;
	}

	public String getTitle() {
		return strTitle;
	}
}
