/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.swt.layout.GridData;

/**
 * Preference page for page break styles.
 */

public class PageBreakPreferencePage extends BaseStylePreferencePage {

	private Object model;

	/**
	 * Default constructor.
	 * 
	 * @param model the model of preference page.
	 */
	public PageBreakPreferencePage(Object model) {
		super(model);
		this.model = model;
		setTitle(Messages.getString("PageBreakPreferencePage.displayname.Title")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.BaseStylePreferencePage
	 * #createFieldEditors()
	 */
	protected void createFieldEditors() {
		// super.createFieldEditors( );

		GridData gdata;

		EditableComboFieldEditor widows = new EditableComboFieldEditor(StyleHandle.WIDOWS_PROP,
				Messages.getString(
						((StyleHandle) model).getPropertyHandle(StyleHandle.WIDOWS_PROP).getDefn().getDisplayNameID()),
				getChoiceArray(StyleHandle.WIDOWS_PROP), getFieldEditorParent());
		gdata = new GridData();
		gdata.widthHint = 96;
		widows.getComboBoxControl(getFieldEditorParent()).setLayoutData(gdata);

		addField(widows);

		EditableComboFieldEditor orphans = new EditableComboFieldEditor(StyleHandle.ORPHANS_PROP,
				Messages.getString(
						((StyleHandle) model).getPropertyHandle(StyleHandle.ORPHANS_PROP).getDefn().getDisplayNameID()),
				getChoiceArray(StyleHandle.ORPHANS_PROP), getFieldEditorParent());
		gdata = new GridData();
		gdata.widthHint = 96;
		orphans.getComboBoxControl(getFieldEditorParent()).setLayoutData(gdata);

		addField(orphans);

		addField(new SeparatorFieldEditor(getFieldEditorParent(), false));

		ComboBoxFieldEditor before = new ComboBoxFieldEditor(StyleHandle.PAGE_BREAK_BEFORE_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.PAGE_BREAK_BEFORE_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(StyleHandle.PAGE_BREAK_BEFORE_PROP), getFieldEditorParent());
		gdata = new GridData();
		gdata.widthHint = 120;
		before.getComboBoxControl(getFieldEditorParent()).setLayoutData(gdata);

		addField(before);

		// ComboBoxFieldEditor inside = new ComboBoxFieldEditor(
		// StyleHandle.PAGE_BREAK_INSIDE_PROP,
		// Messages.getString( ( (StyleHandle) model ).getPropertyHandle(
		// StyleHandle.PAGE_BREAK_INSIDE_PROP )
		// .getDefn( )
		// .getDisplayNameID( ) ),
		// getChoiceArray( StyleHandle.PAGE_BREAK_INSIDE_PROP ),
		// getFieldEditorParent( ) );
		// gdata = new GridData( );
		// gdata.widthHint = 120;
		// inside.getComboBoxControl( getFieldEditorParent( ) )
		// .setLayoutData( gdata );
		//
		// addField( inside );

		ComboBoxFieldEditor after = new ComboBoxFieldEditor(StyleHandle.PAGE_BREAK_AFTER_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.PAGE_BREAK_AFTER_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(StyleHandle.PAGE_BREAK_AFTER_PROP), getFieldEditorParent());
		gdata = new GridData();
		gdata.widthHint = 120;
		after.getComboBoxControl(getFieldEditorParent()).setLayoutData(gdata);

		addField(after);
		UIUtil.bindHelp(getFieldEditorParent().getParent(), IHelpContextIds.STYLE_BUILDER_PAGEBREAK_ID);

	}

	private String[][] getChoiceArray(String propName) {
		IChoiceSet ci = ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT, propName);

		if (ci != null) {
			IChoice[] cs = ci.getChoices();

			String[][] rt = new String[cs.length][2];

			for (int i = 0; i < cs.length; i++) {
				rt[i][0] = cs[i].getDisplayName();
				rt[i][1] = cs[i].getName();
			}

			return rt;
		}

		return new String[0][2];
	}

	protected String[] getPreferenceNames() {
		return new String[] { StyleHandle.WIDOWS_PROP, StyleHandle.ORPHANS_PROP, StyleHandle.PAGE_BREAK_BEFORE_PROP,
				StyleHandle.PAGE_BREAK_AFTER_PROP, };
	}

}