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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * 
 */

public class ExportStyleDialog extends BaseDialog {

	private StyleHandle style;
	private HashMap themeMap;
	private Combo themeCombo;

	public ExportStyleDialog(StyleHandle style, LibraryHandle library) {
		super(Messages.getString("ExportStyleDialog.Title")); //$NON-NLS-1$
		this.style = style;
		themeMap = new HashMap();
		List themes = library.getVisibleThemes(IAccessControl.DIRECTLY_INCLUDED_LEVEL);
		for (int i = 0; i < themes.size(); i++) {
			ThemeHandle theme = (ThemeHandle) themes.get(i);
			themeMap.put(theme.getName(), theme);
		}
	}

	protected boolean initDialog() {
		if (themeCombo.getItemCount() == 0)
			this.getOkButton().setEnabled(false);
		else
			themeCombo.select(0);
		return super.initDialog();
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = layout.marginWidth = 10;
		layout.verticalSpacing = 20;
		composite.setLayout(layout);

		Label messageLine = new Label(composite, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		messageLine.setLayoutData(gd);
		messageLine.setText(Messages.getString("ExportStyleDialog.Message")); //$NON-NLS-1$

		new Label(composite, SWT.NONE).setText(Messages.getString("ExportStyleDialog.Label.Text")); //$NON-NLS-1$
		themeCombo = new Combo(composite, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		themeCombo.setVisibleItemCount(30);
		themeCombo.setItems((String[]) themeMap.keySet().toArray(new String[0]));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		themeCombo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (themeCombo.getSelectionIndex() == -1)
					getOkButton().setEnabled(false);
			}

		});
		gd.widthHint = 200;
		themeCombo.setLayoutData(gd);
		return composite;
	}

	protected void okPressed() {
		ThemeHandle theme = (ThemeHandle) themeMap.get(themeCombo.getText());
		boolean notExist = ElementExportUtil.canExport(style, theme, false);
		setResult(new Object[] { theme, Boolean.valueOf(notExist) });
		super.okPressed();
	}

}
