/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.sample.reportitem.rotatedtext.views;

import java.util.List;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.sample.reportitem.rotatedtext.RotatedTextItem;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * RotatedTextGeneralPage
 */
public class RotatedTextGeneralPage extends AttributesUtil.PageWrapper {

	protected FormToolkit toolkit;
	protected Object input;
	protected Composite contentpane;

	private Text txtText, txtAngle;

	public void buildUI(Composite parent) {
		if (toolkit == null) {
			toolkit = new FormToolkit(Display.getCurrent());
			toolkit.setBorderStyle(SWT.NULL);
		}

		Control[] children = parent.getChildren();

		if (children != null && children.length > 0) {
			contentpane = (Composite) children[children.length - 1];

			GridLayout layout = new GridLayout(3, false);
			layout.marginLeft = 8;
			layout.verticalSpacing = 12;
			contentpane.setLayout(layout);

			toolkit.createLabel(contentpane, "Text Content:"); //$NON-NLS-1$
			txtText = toolkit.createText(contentpane, ""); //$NON-NLS-1$
			GridData gd = new GridData();
			gd.widthHint = 200;

			// XXX comment for expression support
			// gd.horizontalSpan = 2;

			txtText.setLayoutData(gd);
			txtText.addFocusListener(new FocusAdapter() {

				public void focusLost(org.eclipse.swt.events.FocusEvent e) {
					updateModel(RotatedTextItem.TEXT_PROP);
				};
			});

			// XXX uncomment this block for expression support
			// /*
			Button btnExp = toolkit.createButton(contentpane, "...", SWT.PUSH); //$NON-NLS-1$
			btnExp.setToolTipText("Invoke Expression Builder"); //$NON-NLS-1$
			btnExp.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					openExpression(txtText);
				}
			});
			// */

			toolkit.createLabel(contentpane, "Rotation Angle:"); //$NON-NLS-1$
			txtAngle = toolkit.createText(contentpane, ""); //$NON-NLS-1$
			gd = new GridData();
			gd.widthHint = 200;
			gd.horizontalSpan = 2;
			txtAngle.setLayoutData(gd);
			txtAngle.addFocusListener(new FocusAdapter() {

				public void focusLost(org.eclipse.swt.events.FocusEvent e) {
					updateModel(RotatedTextItem.ROTATION_ANGLE_PROP);
				};
			});

		}
	}

	private void openExpression(Text textControl) {
		RotatedTextItem item = getItem();

		if (item != null) {
			String oldValue = textControl.getText();

			ExpressionBuilder eb = new ExpressionBuilder(textControl.getShell(), oldValue);
			eb.setExpressionProvier(new ExpressionProvider(item.getModelHandle()));

			String result = oldValue;

			if (eb.open() == Window.OK) {
				result = eb.getResult();
			}

			if (!oldValue.equals(result)) {
				textControl.setText(result);

				updateModel(RotatedTextItem.TEXT_PROP);
			}
		}
	}

	public void setInput(Object input) {
		this.input = input;
	}

	public void dispose() {
		if (toolkit != null) {
			toolkit.dispose();
		}
	}

	private void adaptFormStyle(Composite comp) {
		Control[] children = comp.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof Composite) {
				adaptFormStyle((Composite) children[i]);
			}
		}

		toolkit.paintBordersFor(comp);
		toolkit.adapt(comp);
	}

	protected RotatedTextItem getItem() {
		Object element = input;

		if (input instanceof List && ((List) input).size() > 0) {
			element = ((List) input).get(0);
		}

		if (element instanceof ExtendedItemHandle) {
			try {
				return (RotatedTextItem) ((ExtendedItemHandle) element).getReportItem();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public void refresh() {
		if (contentpane != null && !contentpane.isDisposed()) {
			if (toolkit == null) {
				toolkit = new FormToolkit(Display.getCurrent());
				toolkit.setBorderStyle(SWT.NULL);
			}

			adaptFormStyle(contentpane);

			updateUI();
		}
	}

	public void postElementEvent() {
		if (contentpane != null && !contentpane.isDisposed()) {
			updateUI();
		}
	}

	private void updateModel(String prop) {
		RotatedTextItem item = getItem();

		if (item != null) {
			try {
				if (RotatedTextItem.ROTATION_ANGLE_PROP.equals(prop)) {
					item.setRotationAngle(Integer.parseInt(txtAngle.getText()));
				} else if (RotatedTextItem.TEXT_PROP.equals(prop)) {
					item.setText(txtText.getText());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void updateUI() {
		RotatedTextItem item = getItem();

		if (item != null) {
			String text = item.getText();
			txtText.setText(text == null ? "" : text); //$NON-NLS-1$

			txtAngle.setText(String.valueOf(item.getRotationAngle()));
		}
	}
}
