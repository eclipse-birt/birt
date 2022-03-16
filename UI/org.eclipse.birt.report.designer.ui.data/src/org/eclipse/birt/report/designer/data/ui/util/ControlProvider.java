/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.data.ui.util;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 *
 */

public class ControlProvider {

	/**
	 *
	 * @param parent
	 * @param text
	 * @return
	 */
	public static Label createLabel(Composite parent, String text) {
		Label lb = new Label(parent, SWT.NONE);
		lb.setText(Utility.getNonNullString(text));

		return lb;
	}

	/**
	 *
	 * @param parent
	 * @param text
	 * @return
	 */
	public static Text createText(Composite parent, String text) {
		final Text tx = new Text(parent, SWT.SINGLE | SWT.BORDER);
		tx.setText(Utility.getNonNullString(text));

		return tx;
	}

	/**
	 *
	 * @param parent
	 * @param style
	 * @return
	 */
	public static Combo createCombo(Composite parent, int style) {
		Combo cb = new Combo(parent, style);

		return cb;
	}

	/**
	 *
	 * @param parent
	 * @param style
	 * @param listener
	 * @return
	 */
	public static Button createButton(Composite parent, int style, SelectionListener listener) {
		Button bt = new Button(parent, style);
		UIUtil.setExpressionButtonImage(bt);
		bt.addSelectionListener(listener);

		return bt;
	}

	/**
	 *
	 * @param parent
	 * @return
	 */
	public static Composite getDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(getGridDataWithHSpan(2));
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);

		return composite;
	}

	/**
	 *
	 * @param hSpan
	 * @return
	 */
	public static GridData getGridDataWithHSpan(int hSpan) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = hSpan;

		return gd;
	}

}
