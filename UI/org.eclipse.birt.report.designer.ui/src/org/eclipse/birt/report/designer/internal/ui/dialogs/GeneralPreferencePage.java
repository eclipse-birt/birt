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

import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Style;

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
		StringFieldEditor name = new StringFieldEditor( StyleElement.NAME_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.NAME_PROP )
						.getDefn( )
						.getDisplayName( ),
				getFieldEditorParent( ) );

		addField( name );

		addField( new SeparatorFieldEditor( getFieldEditorParent( ) ) );

		BooleanFieldEditor shrink = new BooleanFieldEditor( Style.CAN_SHRINK_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.CAN_SHRINK_PROP )
						.getDefn( )
						.getDisplayName( ),
				getFieldEditorParent( ) );
		addField( shrink );

		BooleanFieldEditor blank = new BooleanFieldEditor( Style.SHOW_IF_BLANK_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.SHOW_IF_BLANK_PROP )
						.getDefn( )
						.getDisplayName( ),
				getFieldEditorParent( ) );
		addField( blank );
	}
}

