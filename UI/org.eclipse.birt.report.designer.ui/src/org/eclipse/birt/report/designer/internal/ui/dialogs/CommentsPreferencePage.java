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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

/**
 * Preference page for highlights style.
 */

public class CommentsPreferencePage extends BaseStylePreferencePage {

	private Object model;
	private TextFieldEditor comments;

	/**
	 * The constructor.
	 *
	 * @param model the model of preference page.
	 */
	public CommentsPreferencePage(Object model) {
		super(model);
		setTitle(Messages.getString("CommentsPreferencePage.displayname.Title")); //$NON-NLS-1$
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents
	 * (org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createFieldEditors() {
		// super.createFieldEditors( );

		comments = new TextFieldEditor(
				StyleHandle.COMMENTS_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.COMMENTS_PROP).getDefn().getDisplayNameID()),
				SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER, getFieldEditorParent());
		addField(comments);
		UIUtil.bindHelp(getFieldEditorParent().getParent(), IHelpContextIds.STYLE_BUILDER_COMMENTS_ID);

	}

	@Override
	protected void adjustGridLayout() {
		super.adjustGridLayout();
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
		gd.heightHint = 200;
		comments.getTextControl(getFieldEditorParent()).setLayoutData(gd);
	}

	@Override
	protected String[] getPreferenceNames() {
		return new String[] { StyleHandle.COMMENTS_PROP };
	}

}
