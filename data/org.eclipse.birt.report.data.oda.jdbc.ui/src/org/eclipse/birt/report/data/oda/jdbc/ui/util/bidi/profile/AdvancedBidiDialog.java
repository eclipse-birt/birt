/***********************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util.bidi.profile;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiFormat;
import org.eclipse.birt.report.data.bidi.utils.i18n.Messages;
import org.eclipse.birt.report.data.bidi.utils.ui.BidiGUIUtility;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.bidi.preference.JDBCDataSourcePreferencePage;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * A dialog that subclasses <code>TitleAreaDialog</code> and used to pick Bidi
 * properties for a data set.
 * 
 * @author bidi_hcg
 */

public class AdvancedBidiDialog extends TitleAreaDialog
{
	public final static String ADVANCED_DIALOG_TITLE = Messages
			.getString( "advancedbididialog.title" );
	public final static String ADVANCED_DIALOG_MSG = Messages
			.getString( "advancedbididialog.msg" );
	final private static String EMPTY_STR = ""; //$NON-NLS-1$
	final private static String EXTERNAL_SYSTEM_METADATA_BIDI_FORMAT = Messages
			.getString( "metadata.bidiframe.title" );//$NON-NLS-1$
	final private static String EXTERNAL_SYSTEM_CONTENT_BIDI_FORMAT = Messages
			.getString( "content.bidiframe.title" );//$NON-NLS-1$
	final private static String DISABLE_BIDI_CHECKBOX_TEXT = Messages
			.getString( "disablebidi.checkbox" );//$NON-NLS-1$

	private Group bidiMetadataFormatFrame, bidiContentFormatFrame;
	private Object parentDialog;
	private Button disableTransformButton = null;
	private boolean disableTransform = false;
	private BidiFormat metadataBidiFormat = null;
	private BidiFormat contentBidiFormat = null;

	public AdvancedBidiDialog( Object parentDialog )
	{
		this( PlatformUI.getWorkbench( ).getDisplay( ).getActiveShell( ), parentDialog);
	}

	public AdvancedBidiDialog( Shell parentShell, Object parentDialog)
	{
		super( parentShell );
		this.parentDialog = parentDialog;
		if (parentDialog instanceof BidiSettingsSupport){
			this.contentBidiFormat = ((BidiSettingsSupport)parentDialog).getContentBidiFormat();
			this.metadataBidiFormat = ((BidiSettingsSupport)parentDialog).getMetadataBidiFormat();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite area = (Composite) super.createDialogArea( parent );
		Composite contents = new Composite( area, SWT.NONE );
		contents.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		contents.setLayout( new GridLayout( ) );

		this.setTitle( ADVANCED_DIALOG_TITLE );
		this.setMessage( ADVANCED_DIALOG_MSG );
		getShell( ).setText( ADVANCED_DIALOG_TITLE );

		applyDialogFont( contents );
		initializeDialogUnits( area );
		if ( contentBidiFormat != null
			&& metadataBidiFormat != null
				&& ( EMPTY_STR
						.equals( contentBidiFormat.getBiDiFormatString( ) ) || EMPTY_STR
						.equals( metadataBidiFormat.getBiDiFormatString( ) ) ) )
			disableTransform = true;

		if ( contentBidiFormat == null || metadataBidiFormat == null
				|| disableTransform )
		{
			BidiFormat externalDefaultBDiFormat = new BidiFormat(JdbcPlugin.getDefault( ).getPluginPreferences( ).getString(JDBCDataSourcePreferencePage.EXTERNAL_BIDI_FORMAT));
			this.contentBidiFormat = externalDefaultBDiFormat;
			this.metadataBidiFormat = externalDefaultBDiFormat;
		}
		 
		Composite bidiArea = new Composite(area, SWT.NONE);
		GridLayout bidiGridLayout = new GridLayout();
		bidiGridLayout.numColumns = 4;
		bidiGridLayout.marginHeight = 10;
		bidiGridLayout.marginWidth = 5;
		bidiGridLayout.horizontalSpacing = 5;
		bidiGridLayout.verticalSpacing = 10;
		bidiGridLayout.makeColumnsEqualWidth = true;
		bidiArea.setLayout(bidiGridLayout);
		
		bidiMetadataFormatFrame = BidiGUIUtility.INSTANCE.addBiDiFormatFrame(
			bidiArea, EXTERNAL_SYSTEM_METADATA_BIDI_FORMAT, metadataBidiFormat );
		
		bidiContentFormatFrame = BidiGUIUtility.INSTANCE.addBiDiFormatFrame(
				bidiArea, EXTERNAL_SYSTEM_CONTENT_BIDI_FORMAT, contentBidiFormat );

		disableTransformButton = new Button( bidiArea, SWT.CHECK );
		disableTransformButton.setText( DISABLE_BIDI_CHECKBOX_TEXT );
		disableTransformButton.setSelection( disableTransform );

		if ( disableTransform )
		{
			handleDisableTransform( );
		}
		disableTransformButton.addSelectionListener( new SelectionAdapter( )
		{

			public void widgetSelected( SelectionEvent e )
			{
				disableTransform = !disableTransform;
				handleDisableTransform( );
			}
		} );

		return area;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		if ( !disableTransform )
		{
			metadataBidiFormat = BidiGUIUtility.INSTANCE
					.getBiDiFormat( bidiMetadataFormatFrame );
			contentBidiFormat = BidiGUIUtility.INSTANCE
					.getBiDiFormat( bidiContentFormatFrame );
		}
		else
		{
			metadataBidiFormat = new BidiFormat(
					BidiConstants.DEFAULT_BIDI_FORMAT_STR );
			contentBidiFormat = new BidiFormat(
					BidiConstants.DEFAULT_BIDI_FORMAT_STR );
		}

		if (parentDialog instanceof BidiSettingsSupport){
			((BidiSettingsSupport)parentDialog).setBidiFormats( metadataBidiFormat, contentBidiFormat );
		}
		super.okPressed( );
	}
	public boolean close( ){
		return super.close();
	}
	public int open( )
	{
		try
		{
			if ( getShell( ) == null )
			{
				// create the window
				create( );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
		return super.open( );
	}

	private void handleDisableTransform( )
	{
		Control[] children = bidiContentFormatFrame.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			children[i].setEnabled( !disableTransform );
		}

		children = bidiMetadataFormatFrame.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			children[i].setEnabled( !disableTransform );
		}
	}

}