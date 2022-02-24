/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The general page of the parameter group dialog
 */

public class ParameterGroupGeneralPage extends TabPage {

	private Text nameEditor, displayNameEditor;

	private static final String LABEL_NAME = Messages.getString("ParameterGroupGeneralPage.Label.Name"); //$NON-NLS-1$

	private static final String LABEL_DISPLAY_NAME = Messages.getString("ParameterGroupGeneralPage.Label.DisplayName"); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param name
	 */
	public ParameterGroupGeneralPage(String name) {
		super(name, SWT.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.TabPage#createWidgets(
	 * org.eclipse.swt.widgets.Composite)
	 */
	protected void createWidgets(Composite composite) {
		Label name = new Label(composite, SWT.NONE);
		name.setText(LABEL_NAME);
//		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
//		gd.widthHint = 100;
//		name.setLayoutData( gd );
		nameEditor = new Text(composite, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 250;
		nameEditor.setLayoutData(gd);
		nameEditor.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				applyDialog();
			}
		});

		Label displayName = new Label(composite, SWT.NONE);
		displayName.setText(LABEL_DISPLAY_NAME);
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.verticalAlignment = GridData.BEGINNING;
		gd.heightHint = 100;
		displayName.setLayoutData(gd);
		displayNameEditor = new Text(composite, SWT.BORDER | SWT.SINGLE);
		displayNameEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
	}

	/**
	 * Creates the top level control of the page under the given parent
	 * 
	 * @param parent the parent composite
	 * 
	 * @return Returns the control
	 */
	public Composite createControl(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.PARAMETER_GROUP_DIALOG_ID);
		return super.createControl(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.TabPage#setInput(java.
	 * lang.Object)
	 */
	public void setInput(Object input) {
		ParameterGroupHandle group = (ParameterGroupHandle) input;
		nameEditor.setText(group.getName());
		String displayName = group.getStringProperty(ParameterGroupHandle.DISPLAY_NAME_PROP);
		if (displayName != null) {
			displayNameEditor.setText(displayName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.TabPage#saveTo(java.lang
	 * .Object)
	 */
	public void saveTo(Object result) throws SemanticException {
		ParameterGroupHandle group = (ParameterGroupHandle) result;
		group.setName(nameEditor.getText().trim());
		group.setStringProperty(ParameterGroupHandle.DISPLAY_NAME_PROP, displayNameEditor.getText().trim());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.TabPage#isPageComplete()
	 */
	public boolean isPageComplete() {
		return !StringUtil.isBlank(nameEditor.getText());
	}
}
