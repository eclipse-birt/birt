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
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Creates a preference page for number format.
 *  
 */

public class FormatNumberPreferencePage extends BaseStylePreferencePage
{

	private String name;

	private FormatNumberPage formatPage;

	/**
	 * Constructs a format number preference page.
	 * 
	 * @param model
	 *            The model
	 */
	public FormatNumberPreferencePage( Object model )
	{
		super( model );
		setTitle( Messages.getString( "FormatNumberPreferencePage.formatNumber.title" ) ); //$NON-NLS-1$
		setPreferenceName( Style.NUMBER_FORMAT_PROP );
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
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
	 */
	protected void adjustGridLayout( )
	{
		( (GridLayout) getFieldEditorParent( ).getLayout( ) ).numColumns = 1;
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

		formatPage = new FormatNumberPage( parent,
				SWT.NULL,
				FormatNumberPage.SOURCE_TYPE_STYLE );
		formatPage.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		formatPage.setInput( ( (StylePreferenceStore) getPreferenceStore( ) ).getNumberFormat( ) );
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
		if ( formatPage == null || !formatPage.isFormatStrModified( ) )
		{
			return true;
		}
		try
		{
			( (StylePreferenceStore) getPreferenceStore( ) ).getNumberFormat( )
					.setCategory( formatPage.getCategory( ) );
			( (StylePreferenceStore) getPreferenceStore( ) ).getNumberFormat( )
					.setPattern( formatPage.getPatternStr( ) );
			return true;
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
			return false;
		}
	}

}