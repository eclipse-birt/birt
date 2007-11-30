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

import org.eclipse.birt.report.designer.internal.ui.util.PixelConverter;
import org.eclipse.birt.report.designer.ui.preferences.IStatusChangeListener;
import org.eclipse.birt.report.designer.ui.preferences.OptionsConfigurationBlock;
import org.eclipse.birt.report.designer.ui.preferences.StatusInfo;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 */
public class CrosstabConfigurationBlock extends OptionsConfigurationBlock
{

	private final Key PREF_FILTER_ROW_LIMIT = getKey( CrosstabPlugin.ID,
			CrosstabPlugin.PREFERENCE_FILTER_ROW_LIMIT );
	private final Key PREF_FILTER_COLUMN_LIMIT = getKey( CrosstabPlugin.ID,
			CrosstabPlugin.PREFERENCE_FILTER_COLUMN_LIMIT );
	private final Key PREF_VALUE_LIST_LIMIT = getKey( CrosstabPlugin.ID,
			CrosstabPlugin.PREFERENCE_VALUE_LIST_LIMIT );
	private final Key PREF_CUBE_BUILDER_WARNING = getKey( CrosstabPlugin.ID,
			CrosstabPlugin.CUBE_BUILDER_WARNING_PREFERENCE );
	private final Key PREF_AUTO_DEL_BINDINGS = getKey( CrosstabPlugin.ID,
			CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS );
	private static final String ENABLED = MessageDialogWithToggle.PROMPT;
	private static final String DISABLED = MessageDialogWithToggle.NEVER;
	private static final int MAX_FILTER_ROW_LIMIT = 10000;
	private static final int MAX_FILTER_COLUMN_LIMIT = 10000;
	private static final int MAX_VALUE_LIST_LIMIT = 10000;
	private PixelConverter fPixelConverter;

	public CrosstabConfigurationBlock( IStatusChangeListener context,
			IProject project )
	{
		super( context, CrosstabPlugin.getDefault( ), project );
		setKeys( getKeys( ) );
	}

	private Key[] getKeys( )
	{
		Key[] keys = null;
		if ( fProject == null )
		{
			keys = new Key[]{
					PREF_FILTER_ROW_LIMIT,
					PREF_FILTER_COLUMN_LIMIT,
					PREF_VALUE_LIST_LIMIT,
					PREF_AUTO_DEL_BINDINGS,
					PREF_CUBE_BUILDER_WARNING
			};
		}
		else
			keys = new Key[]{
					PREF_FILTER_ROW_LIMIT,
					PREF_FILTER_COLUMN_LIMIT,
					PREF_VALUE_LIST_LIMIT
			};
		return keys;
	}

	/*
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
	protected Control createContents( Composite parent )
	{
		fPixelConverter = new PixelConverter( parent );
		setShell( parent.getShell( ) );

		Composite mainComp = new Composite( parent, SWT.NONE );
		mainComp.setFont( parent.getFont( ) );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		mainComp.setLayout( layout );

		Composite othersComposite = createBuildPathTabContent( mainComp );
		GridData gridData = new GridData( GridData.FILL,
				GridData.FILL,
				true,
				true );
		gridData.heightHint = fPixelConverter.convertHeightInCharsToPixels( 20 );
		othersComposite.setLayoutData( gridData );

		validateSettings( null, null, null );

		return mainComp;
	}

	private Composite createBuildPathTabContent( Composite parent )
	{

		Composite pageContent = new Composite( parent, SWT.NONE );

		GridData data = new GridData( GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		data.grabExcessHorizontalSpace = true;
		pageContent.setLayoutData( data );

		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 3;
		pageContent.setLayout( layout );

		Group filterGroup = new Group( pageContent, SWT.NONE );
		filterGroup.setText( Messages.getString( "CrosstabPreferencePage.filterLimit" ) );
		filterGroup.setLayout( new GridLayout( 3, false ) );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 3;
		filterGroup.setLayoutData( gd );

		addTextField( filterGroup,
				Messages.getString( "CrosstabPreferencePage.filterRowLimit.prompt" ),
				PREF_FILTER_ROW_LIMIT,
				0,
				0 );
		addTextField( filterGroup,
				Messages.getString( "CrosstabPreferencePage.filterColumnLimit.prompt" ),
				PREF_FILTER_COLUMN_LIMIT,
				0,
				0 );

		Group valueListGroup = new Group( pageContent, SWT.NONE );
		valueListGroup.setText( Messages.getString( "CrosstabPreferencePage.valueListLimit" ) );
		valueListGroup.setLayout( new GridLayout( 3, false ) );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 3;
		valueListGroup.setLayoutData( gd );

		addTextField( valueListGroup,
				Messages.getString( "CrosstabPreferencePage.valueListLimit.prompt" ),
				PREF_VALUE_LIST_LIMIT,
				0,
				0 );

		if ( fProject == null )
		{
			Group promptGroup = new Group( pageContent, SWT.NONE );
			promptGroup.setText( Messages.getString( "CrosstabPreferencePage.promptGroup" ) );
			promptGroup.setLayout( new GridLayout( 3, false ) );
			gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 3;
			promptGroup.setLayoutData( gd );

			String[] enableDisableValues = new String[]{
					ENABLED, DISABLED
			};

			addCheckBox( promptGroup,
					Messages.getString( "CrosstabPreferencePage.autoDelBindings.Text" ),
					PREF_AUTO_DEL_BINDINGS,
					enableDisableValues,
					0 );

			addCheckBox( promptGroup,
					Messages.getString( "CrosstabPreferencePage.cubePopup.Text" ),
					PREF_CUBE_BUILDER_WARNING,
					enableDisableValues,
					0 );

		}
		return pageContent;
	}

	protected void validateSettings( Key changedKey, String oldValue,
			String newValue )
	{
		if ( !getValidateKeyStatus( PREF_FILTER_ROW_LIMIT ) )
			return;
		else if ( !getValidateKeyStatus( PREF_FILTER_COLUMN_LIMIT ) )
			return;
		else if ( ! getValidateKeyStatus( PREF_VALUE_LIST_LIMIT ))
			return;
		else {
			 StatusInfo status = new StatusInfo( );
			 status.setOK( );
			 fContext.statusChanged( status );
		}
	}

	private boolean getValidateKeyStatus( Key key )
	{
		IStatus status = validatePositiveNumber( key );
		if ( status.getSeverity( ) != IStatus.OK )
		{
			fContext.statusChanged( status );
			return false;
		}
		return true;
	}

	protected IStatus validatePositiveNumber( Key key )
	{

		final StatusInfo status = new StatusInfo( );
		String errorMessage;
		if ( key == PREF_FILTER_ROW_LIMIT )
		{
			errorMessage = Messages.getString( "CrosstabPreferencePage.Error.MaxRowInvalid", //$NON-NLS-1$
					new Object[]{
						new Integer( MAX_FILTER_ROW_LIMIT )
					} );
			configStatus( key, errorMessage, MAX_FILTER_ROW_LIMIT, status );
		}
		else if ( key == PREF_FILTER_COLUMN_LIMIT )
		{
			errorMessage = Messages.getString( "CrosstabPreferencePage.Error.MaxColumnInvalid", //$NON-NLS-1$
					new Object[]{
						new Integer( MAX_FILTER_COLUMN_LIMIT )
					} );
			configStatus( key, errorMessage, MAX_FILTER_ROW_LIMIT, status );
		}
		else if ( key == PREF_VALUE_LIST_LIMIT )
		{
			errorMessage = Messages.getString( "CrosstabPreferencePage.Error.MaxValueListInvalid", //$NON-NLS-1$
					new Object[]{
						new Integer( MAX_VALUE_LIST_LIMIT )
					} );
			configStatus( key, errorMessage, MAX_FILTER_ROW_LIMIT, status );
		}
		return status;
	}

	private void configStatus( Key key, String errorMessage, int maxValue,
			final StatusInfo status )
	{
		if ( key.getStoredValue( fPref ).length( ) == 0 )
		{
			status.setError( errorMessage );
		}
		else
		{
			try
			{
				final int value = Integer.parseInt( key.getStoredValue( fPref ) );
				if ( value < 1 || value > maxValue )
				{
					status.setError( errorMessage );
				}
			}
			catch ( NumberFormatException exception )
			{
				status.setError( errorMessage );
			}
		}
	}
}
