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
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Provides default implementation for all Style preference pages
 */

public abstract class BaseStylePreferencePage extends FieldEditorPreferencePage
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors( )
	{
		noDefaultAndApplyButton( );

	}

	/**
	 * The constructor.
	 * 
	 * @param style
	 */
	protected BaseStylePreferencePage( Object model )
	{
		super( FieldEditorPreferencePage.GRID );
		setTitle( Messages.getString( "BaseStylePreferencePage.displayname.Title" ) ); //$NON-NLS-1$

		// Set the preference store for the preference page.
		IPreferenceStore store = new StylePreferenceStore( model );
		setPreferenceStore( store );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	public boolean performOk( )
	{
		IPreferenceStore ps = getPreferenceStore( );

		if ( ps instanceof StylePreferenceStore )
		{
			( (StylePreferenceStore) ps ).clearError( );
		}

		boolean rt = super.performOk( );

		if ( ps instanceof StylePreferenceStore )
		{
			return !( (StylePreferenceStore) ps ).hasError( );
		}

		return rt;
	}

}