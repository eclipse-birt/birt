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
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * A preference page for formatting DateTime.
 */

public class FormatDateTimePreferencePage extends BaseStylePreferencePage
{

	private String name;
	private IFormatPage formatPage;

	/**
	 * Constructs a format datetime preference page.
	 * 
	 * @param model
	 *            The model
	 */
	public FormatDateTimePreferencePage( Object model )
	{
		super( model );
		setTitle( Messages.getString( "FormatDateTimePreferencePage.formatDateTime.title" ) ); //$NON-NLS-1$
		setPreferenceName( DateTimeFormatValue.FORMAT_VALUE_STRUCT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
	 */
	protected void adjustGridLayout( )
	{
		( (GridLayout) getFieldEditorParent( ).getLayout( ) ).numColumns = 1;
	}

	/**
	 * Sets the preference name.
	 */
	private void setPreferenceName( String name )
	{
		this.name = name;
	}

	/**
	 * Gets the preference name.
	 */
	public String getPreferenceName( )
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.BaseStylePreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors( )
	{
		super.createFieldEditors( );
		final Composite parent = getFieldEditorParent( );
		formatPage = new FormatDateTimePage( parent, SWT.NULL );
		( (Composite) formatPage ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		initiateFormatPage( );
	}

	private void initiateFormatPage( )
	{
		String category = ( (StylePreferenceStore) getPreferenceStore( ) ).getDateTimeFormatCategory( );
		String pattern = ( (StylePreferenceStore) getPreferenceStore( ) ).getDateTimeFormat( );

		formatPage.setInput( category, pattern );
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.BaseStylePreferencePage#performOk()
	 */
	public boolean performOk( )
	{
		IPreferenceStore ps = getPreferenceStore( );
		if ( ps instanceof StylePreferenceStore )
		{
			( (StylePreferenceStore) ps ).clearError( );
		}
		boolean rt = doStore( );
		if ( ps instanceof StylePreferenceStore )
		{
			return !( (StylePreferenceStore) ps ).hasError( );
		}
		return rt;
	}

	/**
	 * Stores the result pattern string into Preference Store.
	 * 
	 * @return
	 */
	protected boolean doStore( )
	{
		if ( formatPage == null
				|| !formatPage.isFormatModified( )
				|| !formatPage.isDirty( ) )
		{
			return true;
		}
		try
		{
			( (StylePreferenceStore) getPreferenceStore( ) ).setDateTimeFormatCategory( formatPage.getCategory( ) );
			( (StylePreferenceStore) getPreferenceStore( ) ).setDateTimeFormat( formatPage.getPattern( ) );
			return true;
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
			return false;
		}
	}

}