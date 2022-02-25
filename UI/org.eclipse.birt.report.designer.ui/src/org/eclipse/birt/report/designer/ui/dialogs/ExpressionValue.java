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

package org.eclipse.birt.report.designer.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * ExpressionValue
 *
 * @deprecated not used anymore
 */
@Deprecated
public class ExpressionValue {
	private Text valueText;
	private Button btnPopup;

	public void setVisible(boolean visible) {
		valueText.setVisible(visible);
		btnPopup.setVisible(visible);
	}

	public void setEnabled(boolean enabled) {
		valueText.setEnabled(enabled);
		btnPopup.setEnabled(enabled);
	}

	public boolean getVisible() {
		return valueText.getVisible();
	}

	public boolean isVisible() {
		return valueText.isVisible();
	}

	public boolean getEnabled() {
		return valueText.getEnabled();
	}

	public Text getTextControl() {
		return valueText;
	}

//	public Button getButtonControl( )
//	{
//		return btnPopup;
//	}

	public void addTextControlListener(int eventType, Listener listener) {
		valueText.addListener(eventType, listener);
	}

	public void addButtonControlListener(int eventType, Listener listener) {
		btnPopup.addListener(eventType, listener);
	}

	public String getText() {
		return valueText.getText();
	}

	public void setText(String string) {
		valueText.setText(string);
	}

	private Text createText(Composite parent) {
		Text txt = new Text(parent, SWT.BORDER);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.widthHint = 100;
		txt.setLayoutData(gdata);
		return txt;
	}

	public ExpressionValue(Composite parent, GridData gd, final Combo expressionText) {
		if (gd == null) {
			gd = new GridData(GridData.END | GridData.FILL_HORIZONTAL);
			gd.heightHint = 20;
		}

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new ExpressionLayout());
		composite.setLayoutData(gd);
		valueText = createText(composite);
		btnPopup = new Button(composite, SWT.ARROW | SWT.DOWN);
	}

	public ExpressionValue(Composite parent, final Combo expressionText) {
		this(parent, null, expressionText);
	}

	private class ExpressionLayout extends Layout {

		@Override
		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point size = btnPopup.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			valueText.setBounds(0, 0, bounds.width - size.x, bounds.height);
			btnPopup.setBounds(bounds.width - size.x, 0, size.x, bounds.height);
		}

		@Override
		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point contentsSize = valueText.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			Point buttonSize = btnPopup.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			// Just return the button width to ensure the button is not
			// clipped
			// if the label is long.
			// The label will just use whatever extra width there is
			Point result = new Point(buttonSize.x, Math.max(contentsSize.y, buttonSize.y));
			return result;
		}
	}

}
