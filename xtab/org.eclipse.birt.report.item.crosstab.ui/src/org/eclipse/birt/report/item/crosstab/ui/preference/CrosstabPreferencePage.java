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

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 
 */

public class CrosstabPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage,
		IPropertyChangeListener
{

	private transient IntegerFieldEditor txtMaxLimit;

	private Button autoDelBindings;

	public static final int FILTER_LIMIT_DEFAULT = 100;

	public static final int MAX_FILTER_LIMIT = 10000;

	public static final boolean AUTO_DEL_BINDING_DEFAULT = true;

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
		txtMaxLimit.setPage( this );
		txtMaxLimit.setPropertyChangeListener( this );

		Group promptGroup = new Group(cmpTop, SWT.NONE);
		promptGroup.setText( Messages.getString( "CrosstabPreferencePage.promptGroup" ) );
		promptGroup.setLayout( new GridLayout( 1, false ) );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		promptGroup.setLayoutData( gd );
		
		autoDelBindings = new Button( promptGroup, SWT.CHECK );
		autoDelBindings.setText( Messages.getString( "CrosstabPreferencePage.autoDelBindings.Text" ) );
//		new Label(promptGroup, SWT.NONE);
		
		initControlValues( );

		return cmpTop;
	}

	private void initControlValues( )
	{
		txtMaxLimit.setStringValue( CrosstabPlugin.getDefault( )
				.getPluginPreferences( )
				.getString( CrosstabPlugin.PREFERENCE_FILTER_LIMIT ) );
		autoDelBindings.setSelection( CrosstabPlugin.getDefault( )
				.getPluginPreferences( )
				.getBoolean( CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS ) );
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
		autoDelBindings.setSelection( AUTO_DEL_BINDING_DEFAULT );

		super.performDefaults( );
	}

	public boolean performOk( )
	{
		CrosstabPlugin.getDefault( )
				.getPluginPreferences( )
				.setValue( CrosstabPlugin.PREFERENCE_FILTER_LIMIT,
						txtMaxLimit.getIntValue( ) );

		CrosstabPlugin.getDefault( )
				.getPluginPreferences( )
				.setValue( CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS,
						autoDelBindings.getSelection( ) );

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
	
	
	private void temp()
	{
		if(!CrosstabPlugin.getDefault( )
				.getPluginPreferences( ).getBoolean( CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS ))
		{
			return;
		}
		MessageDialogWithToggle msgDlg = MessageDialogWithToggle.openYesNoQuestion(  UIUtil.getDefaultShell( ),
				"Remove Unused Bindings?",
				"This action will result in unused data bindings.\n\nDo you wish to remove unused bindings?", 
				"Don't show this message again", 
				!CrosstabPlugin.getDefault( )
				.getPluginPreferences( ).getBoolean( CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS ),
				null,
				null);
		
		if(msgDlg.getReturnCode( ) == IDialogConstants.YES_ID || msgDlg.getReturnCode( ) == IDialogConstants.NO_ID)
		{
			CrosstabPlugin.getDefault( )
			.getPluginPreferences( )
			.setValue( CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS,
					!msgDlg.getToggleState( ) );
		}
		
	}

}
