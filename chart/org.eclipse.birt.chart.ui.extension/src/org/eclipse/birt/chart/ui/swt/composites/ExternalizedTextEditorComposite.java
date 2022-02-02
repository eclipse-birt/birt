/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.chart.ui.swt.composites;

import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class ExternalizedTextEditorComposite extends Canvas implements SelectionListener, Listener {
	private transient TextEditorComposite txtSelection = null;

	private transient Button btnDown = null;

	private transient int iStyle = SWT.SINGLE;

	private transient int iHeightHint = -1;

	private transient int iWidthHint = -1;

	private transient Vector<Listener> vListeners = null;

	public static final int TEXT_CHANGED_EVENT = 1;

	public static final String SEPARATOR = "="; //$NON-NLS-1$

	private transient String sKey = null;

	private transient String sCurrent = ""; //$NON-NLS-1$

	private String sDisplyText = ""; //$NON-NLS-1$

	private transient List<String> keys = null;

	private transient IUIServiceProvider serviceprovider = null;

	private transient boolean bEnabled = true;

	public ExternalizedTextEditorComposite(Composite parent, int style, int iHeightHint, int iWidthHint,
			List<String> keys, IUIServiceProvider serviceprovider, String sText) {
		super(parent, SWT.NONE);
		this.iStyle = style;
		this.iHeightHint = iHeightHint;
		this.iWidthHint = iWidthHint;
		this.keys = keys;
		this.serviceprovider = serviceprovider;
		init();
		placeComponents();
		setText(sText);

		ChartUIUtil.addScreenReaderAccessibility(this, txtSelection.getTextControl());
	}

	public void addScreenReaderAccessbility(String description) {
		ChartUIUtil.addScreenReaderAccessbility(txtSelection.getTextControl(), description);
	}

	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
		vListeners = new Vector<Listener>();
	}

	private void placeComponents() {
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 1;
		glContent.marginHeight = 0;
		glContent.marginWidth = 0;

		this.setLayout(glContent);

		txtSelection = new TextEditorComposite(this, iStyle);

		GridData gdTXTSelection = new GridData(GridData.FILL_HORIZONTAL);
		if (iHeightHint > 0) {
			gdTXTSelection.heightHint = iHeightHint - 10;
		}
		if (iWidthHint > 0) {
			gdTXTSelection.widthHint = iWidthHint;
		}
		txtSelection.setLayoutData(gdTXTSelection);
		txtSelection.addListener(this);

		btnDown = new Button(this, SWT.PUSH);
		GridData gdBTNDown = new GridData(GridData.VERTICAL_ALIGN_END);
		ChartUIUtil.setChartImageButtonSizeByPlatform(gdBTNDown);
		btnDown.setImage(UIHelper.getImage("icons/obj16/externalizetext.gif")); //$NON-NLS-1$
		btnDown.setToolTipText(Messages.getString("ExternalizedTextEditorComposite.Lbl.EditText")); //$NON-NLS-1$
		btnDown.setLayoutData(gdBTNDown);
		btnDown.addSelectionListener(this);
		ChartUIUtil.addScreenReaderAccessbility(btnDown, btnDown.getToolTipText());
	}

	public void setEnabled(boolean bState) {
		if (bState) {
			// check compatibility with externalization
			if (sKey == null || sKey.length() == 0) {
				txtSelection.setEnabled(true);
			}
		} else {
			this.txtSelection.setEnabled(bState);
		}
		this.btnDown.setEnabled(bState);
		this.bEnabled = bState;
	}

	public boolean isEnabled() {
		return this.bEnabled;
	}

	public void setText(String str) {
		sKey = getKey(str);
		sCurrent = getValue(str);
		txtSelection.setText(getLocalizedValue(str));
		sDisplyText = txtSelection.getText();
	}

	public String getText() {
		return buildString();
	}

	private String buildString() {
		if (sKey != null && sKey.length() > 0) {
			return sKey + ExternalizedTextEditorComposite.SEPARATOR + sCurrent;
		} else {
			return ChartUtil.prefixExternalizeSeperator(sCurrent);
		}
	}

	String getKey(String str) {
		int iSeparator = str.indexOf(SEPARATOR);
		if (iSeparator == -1) {
			iSeparator = 0;
		}
		return str.substring(0, iSeparator);
	}

	String getValue(String str) {
		int iSeparator = str.indexOf(SEPARATOR) + SEPARATOR.length();
		if (iSeparator == (-1 + SEPARATOR.length())) {
			iSeparator = 0;
		}
		return str.substring(iSeparator);
	}

	String getLocalizedValue(String str) {
		String sTmp = ""; //$NON-NLS-1$
		sTmp = getKey(str);
		if ("".equals(sTmp)) //$NON-NLS-1$
		{
			return getValue(str);
		}
		sTmp = serviceprovider.getValue(sTmp);
		if (sTmp == null || "".equals(sTmp)) //$NON-NLS-1$
		{
			sTmp = Messages.getString("ExternalizedTextEditorComposite.Warn.KeyNotFound"); //$NON-NLS-1$
		}
		return sTmp;
	}

	public void addListener(Listener listener) {
		vListeners.add(listener);
	}

	private void fireEvent() {
		Event event = new Event();
		event.widget = this;
		event.type = TEXT_CHANGED_EVENT;
		event.data = buildString();
		for (int iL = 0; iL < vListeners.size(); iL++) {
			vListeners.elementAt(iL).handleEvent(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		ExternalizedTextEditorDialog editor = new ExternalizedTextEditorDialog(getShell(), buildString(), keys,
				serviceprovider, sCurrent);
		if (editor.open() == Window.OK) {
			String sTxt = editor.getResult();
			if (sTxt != null) {
				this.setText(sTxt);
				fireEvent();
			}
		}
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
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		// If display text doesn't changed, don't update it.
		if (!sDisplyText.equals(txtSelection.getText()) || !sCurrent.equals(txtSelection.getText())) {
			sCurrent = txtSelection.getText();
			fireEvent();
		}
	}
}
