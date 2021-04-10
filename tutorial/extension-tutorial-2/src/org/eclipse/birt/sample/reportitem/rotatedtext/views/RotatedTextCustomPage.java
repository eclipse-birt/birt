/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.sample.reportitem.rotatedtext.views;

import org.eclipse.birt.sample.reportitem.rotatedtext.RotatedTextItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * RotatedTextCustomPage
 */
public class RotatedTextCustomPage extends RotatedTextGeneralPage {

	private Label lbText, lbAngle;

	public void buildUI(Composite parent) {
		if (toolkit == null) {
			toolkit = new FormToolkit(Display.getCurrent());
			toolkit.setBorderStyle(SWT.NULL);
		}

		Control[] children = parent.getChildren();

		if (children != null && children.length > 0) {
			contentpane = (Composite) children[children.length - 1];

			GridLayout layout = new GridLayout(2, false);
			layout.marginTop = 8;
			layout.marginLeft = 8;
			layout.verticalSpacing = 12;
			contentpane.setLayout(layout);

			toolkit.createLabel(contentpane, "Text Content:"); //$NON-NLS-1$
			lbText = toolkit.createLabel(contentpane, ""); //$NON-NLS-1$
			GridData gd = new GridData();
			gd.widthHint = 200;
			lbText.setLayoutData(gd);

			toolkit.createLabel(contentpane, "Rotation Angle:"); //$NON-NLS-1$
			lbAngle = toolkit.createLabel(contentpane, ""); //$NON-NLS-1$
			gd = new GridData();
			gd.widthHint = 200;
			lbAngle.setLayoutData(gd);

		}
	}

	protected void updateUI() {
		RotatedTextItem item = getItem();

		if (item != null) {
			String text = item.getText();
			lbText.setText(text == null ? "" : text); //$NON-NLS-1$

			lbAngle.setText(String.valueOf(item.getRotationAngle()));
		}
	}

}
