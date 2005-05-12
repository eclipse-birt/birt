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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.StyleHandle;



/**
 * Provides general preference page.
 */

public class GeneralPreferencePage extends BaseStylePreferencePage
{

	private Object model;

	/**
	 * Default constructor.
	 * 
	 * @param model,
	 *            the model of preference page.
	 */
	public GeneralPreferencePage( Object model )
	{
		super( model );

		this.model = model;
	}

	/**
	 * @see org.eclipse.jface.preference.
	 *      FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors( )
	{
		super.createFieldEditors( );
		StringFieldEditor name = new StringFieldEditor( StyleHandle.NAME_PROP,
				Messages.getString( ( (StyleHandle) model ).getPropertyHandle( StyleHandle.NAME_PROP )
						.getDefn( )
						.getDisplayNameID( ) ),
				getFieldEditorParent( ) );

		addField( name );

		addField( new SeparatorFieldEditor( getFieldEditorParent( ) ) );

		BooleanFieldEditor shrink = new BooleanFieldEditor( StyleHandle.CAN_SHRINK_PROP,
				Messages.getString( ( (StyleHandle) model ).getPropertyHandle( StyleHandle.CAN_SHRINK_PROP )
						.getDefn( )
						.getDisplayNameID( ) ),
				getFieldEditorParent( ) );
		addField( shrink );

		BooleanFieldEditor blank = new BooleanFieldEditor( StyleHandle.SHOW_IF_BLANK_PROP,
				Messages.getString( ( (StyleHandle) model ).getPropertyHandle( StyleHandle.SHOW_IF_BLANK_PROP )
						.getDefn( )
						.getDisplayNameID( ) ),
				getFieldEditorParent( ) );
		addField( blank );
	}
}

