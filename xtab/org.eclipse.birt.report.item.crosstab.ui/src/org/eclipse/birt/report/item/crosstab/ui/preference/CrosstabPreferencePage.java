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

package org.eclipse.birt.report.item.crosstab.ui.preference;

import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 
 */

public class CrosstabPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage,IPropertyChangeListener
{

	private transient IntegerFieldEditor txtMaxLimit;

	private static final int FILTER_LIMIT_DEFAULT = 100;

	private static final int MAX_FILTER_LIMIT = 10000;

	protected Control createContents( Composite parent )
	{
		// PlatformUI.getWorkbench( ).getHelpSystem( ).setHelp( parent,
		// ChartHelpContextIds.PREFERENCE_CHART );

		Composite cmpTop = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 1, false );
		cmpTop.setLayout( layout );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		cmpTop.setLayoutData( gd );

		Group group = new Group( cmpTop, SWT.NONE );
		group.setText( Messages.getString( "CrosstabPreferencePage.filterLimit" ) );
		group.setLayout( new GridLayout( 1, false ) );
		group.setLayoutData( gd );

		txtMaxLimit = new IntegerFieldEditor( CrosstabPlugin.PREFERENCE_FILTER_LIMIT,
				Messages.getString( "CrosstabPreferencePage.filterLimit.prompt" ), group ); //$NON-NLS-1$ 
		txtMaxLimit.setErrorMessage( Messages.getString( "CrosstabPreferencePage.Error.MaxRowInvalid", //$NON-NLS-1$
				new Object[]{
					new Integer( MAX_FILTER_LIMIT )
				} ) );
		txtMaxLimit.setValidateStrategy( StringFieldEditor.VALIDATE_ON_KEY_STROKE );
		txtMaxLimit.setValidRange( 1, MAX_FILTER_LIMIT );
		txtMaxLimit.setEmptyStringAllowed( false );
		txtMaxLimit.setStringValue( CrosstabPlugin.getDefault( )
				.getPluginPreferences( )
				.getString( CrosstabPlugin.PREFERENCE_FILTER_LIMIT ) );
		txtMaxLimit.setPage( this );
		txtMaxLimit.setPropertyChangeListener( this );

		return cmpTop;
	}

	public void init( IWorkbench workbench )
	{
		init( );
	}

	public static void init( )
	{
		int maxLimit = CrosstabPlugin.getDefault( )
				.getPluginPreferences( )
				.getInt( CrosstabPlugin.PREFERENCE_FILTER_LIMIT );
		if ( maxLimit <= 0 )
		{
			CrosstabPlugin.getDefault( )
					.getPluginPreferences( )
					.setValue( CrosstabPlugin.PREFERENCE_FILTER_LIMIT,
							FILTER_LIMIT_DEFAULT );
		}
	}

	protected void performDefaults( )
	{
		txtMaxLimit.setStringValue( String.valueOf( FILTER_LIMIT_DEFAULT ) );
		super.performDefaults( );
	}

	public boolean performOk( )
	{
		CrosstabPlugin.getDefault( )
				.getPluginPreferences( )
				.setValue( CrosstabPlugin.PREFERENCE_FILTER_LIMIT,
						txtMaxLimit.getIntValue( ) );
		CrosstabPlugin.getDefault( ).savePluginPreferences( );
		return super.performOk( );
	}
	
	public void propertyChange( PropertyChangeEvent event )
	{
		if ( event.getProperty( ).equals( FieldEditor.IS_VALID ) )
		{
			setValid( txtMaxLimit.isValid( ) );
		}
	}

}
