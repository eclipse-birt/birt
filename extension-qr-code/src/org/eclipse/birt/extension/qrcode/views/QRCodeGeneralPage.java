/*******************************************************************************
 * Copyright (c) 2022 Henning von Bargen
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Henning von Bargen - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.extension.qrcode.views;

import java.util.List;

import org.eclipse.birt.extension.qrcode.QRCodeItem;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
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
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * QRCodeGeneralPage
 */
public class QRCodeGeneralPage extends AttributesUtil.PageWrapper {

	protected FormToolkit toolkit;
	protected Object input;
	protected Composite contentpane;

	private Text txtText;
	private Spinner spDotsWidth;
	private Text txtEncoding;

	@Override
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

			toolkit.createLabel(contentpane, "Text Content:");
			txtText = toolkit.createText(contentpane, ""); //$NON-NLS-1$
			GridData gd = new GridData();
			gd.widthHint = 200;

			txtText.setLayoutData(gd);
			txtText.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(org.eclipse.swt.events.FocusEvent e) {
					updateModel(QRCodeItem.TEXT_PROP);
				}
			});

			Button btnExp = toolkit.createButton(contentpane, "...", SWT.PUSH); //$NON-NLS-1$
			btnExp.setToolTipText("Invoke Expression Builder");
			btnExp.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					openExpression(txtText);
				}
			});

			toolkit.createLabel(contentpane, "Width (dots):");
			spDotsWidth = new Spinner(contentpane, SWT.None);
			spDotsWidth.setMinimum(21);
			spDotsWidth.setMaximum(2000);
			spDotsWidth.setDigits(0);

			gd = new GridData();
			gd.widthHint = 200;
			gd.horizontalSpan = 2;
			spDotsWidth.setLayoutData(gd);
			spDotsWidth.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(org.eclipse.swt.events.FocusEvent e) {
					updateModel(QRCodeItem.DOTS_WIDTH_PROP);
				}
			});

			toolkit.createLabel(contentpane, "Encoding:");
			txtEncoding = toolkit.createText(contentpane, ""); //$NON-NLS-1$
			gd = new GridData();
			gd.widthHint = 200;
			gd.horizontalSpan = 2;
			txtEncoding.setLayoutData(gd);
			txtEncoding.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(org.eclipse.swt.events.FocusEvent e) {
					updateModel(QRCodeItem.ENCODING_PROP);
				}
			});

		}
	}

	private void openExpression(Text textControl) {
		QRCodeItem item = getItem();

		if (item != null) {
			String oldValue = textControl.getText();

			ExpressionBuilder eb = new ExpressionBuilder(textControl.getShell(), oldValue);
			eb.setExpressionProvider(new ExpressionProvider(item.getModelHandle()));

			String result = oldValue;

			if (eb.open() == Window.OK) {
				result = eb.getResult();
			}

			if (!oldValue.equals(result)) {
				textControl.setText(result);

				updateModel(QRCodeItem.TEXT_PROP);
			}
		}
	}

	@Override
	public void setInput(Object input) {
		this.input = input;
	}

	@Override
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

	protected QRCodeItem getItem() {
		Object element = input;

		if (input instanceof List && ((List) input).size() > 0) {
			element = ((List) input).get(0);
		}

		if (element instanceof ExtendedItemHandle) {
			try {
				return (QRCodeItem) ((ExtendedItemHandle) element).getReportItem();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
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

	@Override
	public void postElementEvent() {
		if (contentpane != null && !contentpane.isDisposed()) {
			updateUI();
		}
	}

	private void updateModel(String prop) {
		QRCodeItem item = getItem();

		if (item != null) {
			try {
				if (QRCodeItem.DOTS_WIDTH_PROP.equals(prop)) {
					item.setDotsWidth(Integer.parseInt(spDotsWidth.getText()));
				} else if (QRCodeItem.TEXT_PROP.equals(prop)) {
					item.setText(txtText.getText());
				} else if (QRCodeItem.ENCODING_PROP.equals(prop)) {
					item.setEncoding(txtEncoding.getText());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void updateUI() {
		QRCodeItem item = getItem();

		if (item != null) {
			String text = item.getText();
			txtText.setText(text == null ? "" : text); //$NON-NLS-1$
			spDotsWidth.setSelection(item.getDotsWidth());
			String encoding = item.getEncoding();
			txtEncoding.setText(encoding == null ? "" : encoding); //$NON-NLS-1$
		}
	}
}
