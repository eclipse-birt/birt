/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FileFormPropertyDescriptor extends FormPropertyDescriptor {

	public FileFormPropertyDescriptor(boolean formStyle) {
		super(formStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void fullLayout() {
		super.fullLayout();

		btnEdit.setVisible(false);

		FormData data = new FormData();
		data.top = new FormAttachment(btnAdd, 0, SWT.BOTTOM);
		data.left = new FormAttachment(btnAdd, 0, SWT.LEFT);
		data.width = Math.max(60, btnDel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		btnDel.setLayoutData(data);
	}

	@Override
	public Control createControl(Composite parent) {
		Control control = super.createControl(parent);
		btnEdit.setVisible(false);
		return control;
	}

}
