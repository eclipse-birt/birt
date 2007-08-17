/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * PreviewDataPreferencePage
 */
public class PreviewDataPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage
{

	public static final int MAX_DATASET_ROW_DEFAULT = 100;

	public static final int MAX_DATASET_ROW = 10000;

	public static final int MAX_CUBE_LEVEL_MEMBER_DEFAULT = 10;

	public static final int MAX_CUBE_LEVEL_MEMBER = 10000;

	/** max Row preference name */
	public static final String PREVIEW_MAXROW = WebViewer.PREVIEW_MAXROW;

	public static final String PREVIEW_MAX_LEVEL_MEMBER = WebViewer.PREVIEW_MAXCUBELEVEL;

	private transient IntegerFieldEditor txtMaxDataSetRow;

	private transient IntegerFieldEditor txtMaxLevelMember;

	protected Control createContents( Composite parent )
	{
		UIUtil.bindHelp( parent,
				IHelpContextIds.PREFERENCE_BIRT_PREVIEW_DATA_ID );

		Composite cmpTop = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 1, false );
		cmpTop.setLayout( layout );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		cmpTop.setLayoutData( gd );

		txtMaxDataSetRow = new IntegerFieldEditor( PREVIEW_MAXROW,
				Messages.getString( "designer.preview.preference.resultset.maxrow.description" ), //$NON-NLS-1$
				cmpTop );
		txtMaxDataSetRow.setPage( this );
		txtMaxDataSetRow.setTextLimit( Integer.toString( MAX_DATASET_ROW )
				.length( ) );
		txtMaxDataSetRow.setErrorMessage( Messages.getFormattedString( "designer.preview.preference.resultset.maxrow.errormessage", //$NON-NLS-1$
				new Object[]{
					new Integer( MAX_DATASET_ROW )
				} ) );
		txtMaxDataSetRow.setValidateStrategy( StringFieldEditor.VALIDATE_ON_KEY_STROKE );
		txtMaxDataSetRow.setValidRange( 1, MAX_DATASET_ROW );
		txtMaxDataSetRow.setEmptyStringAllowed( false );
		txtMaxDataSetRow.setPropertyChangeListener( new IPropertyChangeListener( ) {

			public void propertyChange( PropertyChangeEvent event )
			{
				if ( event.getProperty( ).equals( FieldEditor.IS_VALID ) )
					setValid( txtMaxDataSetRow.isValid( ) );
			}
		} );

		txtMaxLevelMember = new IntegerFieldEditor( PREVIEW_MAX_LEVEL_MEMBER,
				Messages.getString( "designer.preview.preference.resultset.maxlevelmember.description" ), cmpTop ); //$NON-NLS-1$ 
		txtMaxLevelMember.setPage( this );
		txtMaxLevelMember.setTextLimit( Integer.toString( MAX_CUBE_LEVEL_MEMBER )
				.length( ) );
		txtMaxLevelMember.setErrorMessage( Messages.getFormattedString( "designer.preview.preference.resultset.maxlevelmember.errormessage", //$NON-NLS-1$
				new Object[]{
					new Integer( MAX_CUBE_LEVEL_MEMBER )
				} ) );
		txtMaxLevelMember.setValidateStrategy( StringFieldEditor.VALIDATE_ON_KEY_STROKE );
		txtMaxLevelMember.setValidRange( 1, MAX_CUBE_LEVEL_MEMBER );
		txtMaxLevelMember.setEmptyStringAllowed( false );
		txtMaxLevelMember.setPropertyChangeListener( new IPropertyChangeListener( ) {

			public void propertyChange( PropertyChangeEvent event )
			{
				if ( event.getProperty( ).equals( FieldEditor.IS_VALID ) )
					setValid( txtMaxLevelMember.isValid( ) );
			}
		} );

		initControlValues( );

		return cmpTop;
	}

	private void initControlValues( )
	{
		String defaultMaxRow = ViewerPlugin.getDefault( )
				.getPluginPreferences( )
				.getString( PREVIEW_MAXROW );

		if ( defaultMaxRow == null || defaultMaxRow.trim( ).length( ) <= 0 )
		{
			defaultMaxRow = String.valueOf( MAX_DATASET_ROW_DEFAULT );
		}
		txtMaxDataSetRow.setStringValue( defaultMaxRow );

		defaultMaxRow = ViewerPlugin.getDefault( )
				.getPluginPreferences( )
				.getString( PREVIEW_MAX_LEVEL_MEMBER );

		if ( defaultMaxRow == null || defaultMaxRow.trim( ).length( ) <= 0 )
		{
			defaultMaxRow = String.valueOf( MAX_CUBE_LEVEL_MEMBER_DEFAULT );
		}
		txtMaxLevelMember.setStringValue( defaultMaxRow );

	}

	public void init( IWorkbench workbench )
	{

	}

	protected void performDefaults( )
	{
		txtMaxDataSetRow.setStringValue( String.valueOf( MAX_DATASET_ROW_DEFAULT ) );
		txtMaxLevelMember.setStringValue( String.valueOf( MAX_CUBE_LEVEL_MEMBER_DEFAULT ) );

		super.performDefaults( );
	}

	public boolean performOk( )
	{
		ViewerPlugin.getDefault( )
				.getPluginPreferences( )
				.setValue( PREVIEW_MAXROW, txtMaxDataSetRow.getIntValue( ) );

		ViewerPlugin.getDefault( )
				.getPluginPreferences( )
				.setValue( PREVIEW_MAX_LEVEL_MEMBER,
						txtMaxLevelMember.getIntValue( ) );

		ViewerPlugin.getDefault( ).savePluginPreferences( );

		return super.performOk( );
	}

}
