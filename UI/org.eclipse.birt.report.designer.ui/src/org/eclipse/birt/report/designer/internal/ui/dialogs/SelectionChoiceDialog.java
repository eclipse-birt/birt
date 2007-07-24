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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

/**
 * Dialog to add and edit selection choice for parameter
 */

public class SelectionChoiceDialog extends BaseDialog
{

	public static interface ISelectionChoiceValidator
	{

		String validate( String displayLabelKey, String displayLabel,
				String value );
	}

	private Text labelEditor, valueEditor;

	private SelectionChoice selectionChoice;

	private CLabel messageLine;

	private ISelectionChoiceValidator validator;

	private Text resourceText;

	public SelectionChoiceDialog( String title )
	{
		this( UIUtil.getDefaultShell( ), title );
	}

	public SelectionChoiceDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	protected boolean initDialog( )
	{
		Assert.isNotNull( selectionChoice );
		labelEditor.setText( UIUtil.convertToGUIString( selectionChoice.getLabel( ) ) );
		valueEditor.setText( UIUtil.convertToGUIString( selectionChoice.getValue( ) ) );
		resourceText.setText( UIUtil.convertToGUIString( selectionChoice.getLabelResourceKey( ) ) );
		if ( validator != null )
		{
			updateStatus( );
		}
		return true;
	}

	protected Control createDialogArea( Composite parent )
	{
		String[] labels = new String[]{
				Messages.getString( "ParameterDialog.SelectionDialog.Label.DisplayTextKey" ),
				Messages.getString( "ParameterDialog.SelectionDialog.Label.DisplayText" ),
				Messages.getString( "ParameterDialog.SelectionDialog.Label.Value" )
		};
		Composite composite = (Composite) super.createDialogArea( parent );
		GridLayout layout = new GridLayout( 3, false );
		layout.marginWidth = 15;
		layout.marginHeight = 15;
		composite.setLayout( layout );
		new Label( composite, SWT.NONE ).setText( labels[0] );
		resourceText = new Text( composite, SWT.BORDER );
		GridData gd = new GridData( );
		gd.widthHint = 200;
		resourceText.setLayoutData( gd );
		resourceText.setEditable( false );
		Button resourceBtn = new Button( composite, SWT.PUSH );
		resourceBtn.setText( Messages.getString( "ParameterDialog.SelectionDialog.Button.Resource" ) );
		resourceBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleBrowserSelectedEvent( );
			}
		} );
		resourceBtn.setEnabled( enableResourceKey( ) );
		new Label( composite, SWT.NONE ).setText( labels[1] );
		labelEditor = new Text( composite, SWT.BORDER );
		gd = new GridData( );
		gd.widthHint = 200;
		labelEditor.setLayoutData( gd );
		new Label( composite, SWT.NONE );
		new Label( composite, SWT.NONE ).setText( labels[2] );
		valueEditor = new Text( composite, SWT.BORDER );
		gd = new GridData( );
		gd.widthHint = 200;
		valueEditor.setLayoutData( gd );
		new Label( composite, SWT.NONE );

		Composite noteContainer = new Composite( composite, SWT.NONE );
		gd = new GridData( );
		gd.horizontalSpan = 3;
		gd.widthHint = UIUtil.getMaxStringWidth( labels, composite )
				+ 200
				+ layout.horizontalSpacing
				* 2
				+ resourceBtn.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		noteContainer.setLayoutData( gd );

		layout = new GridLayout( 3, false );
		layout.marginWidth = 0;
		noteContainer.setLayout( layout );

		Label note = new Label( noteContainer, SWT.WRAP );
		note.setText( Messages.getString( "ParameterDialog.SelectionDialog.Label.Note" ) );
		gd = new GridData( );
		gd.widthHint = UIUtil.getMaxStringWidth( labels, composite )
				+ 200
				+ layout.horizontalSpacing
				* 2
				+ resourceBtn.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		note.setLayoutData( gd );

		messageLine = new CLabel( composite, SWT.NONE );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 3;
		messageLine.setLayoutData( gd );
		if ( validator != null )
		{
			ModifyListener listener = new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					updateStatus( );
				}

			};
			labelEditor.addModifyListener( listener );
			valueEditor.addModifyListener( listener );

		}

		UIUtil.bindHelp( composite, IHelpContextIds.SELECTION_CHOICE_DIALOG );
		return composite;
	}

	protected void okPressed( )
	{
		selectionChoice.setLabel( UIUtil.convertToModelString( labelEditor.getText( ),
				false ) );
		selectionChoice.setValue( UIUtil.convertToModelString( valueEditor.getText( ),
				false ) );
		selectionChoice.setLabelResourceKey( UIUtil.convertToModelString( resourceText.getText( ),
				false ) );
		setResult( selectionChoice );
		super.okPressed( );
	}

	private void updateStatus( )
	{
		String erroeMessage = validator.validate( UIUtil.convertToModelString( resourceText.getText( ),
				false ),
				UIUtil.convertToModelString( labelEditor.getText( ), false ),
				UIUtil.convertToModelString( valueEditor.getText( ), false ) );
		if ( erroeMessage != null )
		{
			messageLine.setText( erroeMessage );
			messageLine.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK ) );
			getOkButton( ).setEnabled( false );
		}
		else
		{
			messageLine.setText( "" );
			messageLine.setImage( null );
			getOkButton( ).setEnabled( true );
		}
	}

	public void setInput( SelectionChoice selectionChoice )
	{
		this.selectionChoice = selectionChoice;
	}

	public void setValidator( ISelectionChoiceValidator validator )
	{
		this.validator = validator;
	}

	private String getBaseName( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getIncludeResource( );
	}

	private URL getResourceURL( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.findResource( getBaseName( ), IResourceLocator.MESSAGE_FILE );
	}

	private boolean enableResourceKey( )
	{
		URL resource = getResourceURL( );
		String path = null;
		try
		{
			if ( resource != null )
			{
				path = FileLocator.resolve( resource ).getFile( );
			}

		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		if ( resource == null || path == null || !new File( path ).exists( ) )
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	protected void handleBrowserSelectedEvent( )
	{
		ResourceEditDialog dlg = new ResourceEditDialog( getShell( ),
				Messages.getString( "ResourceKeyDescriptor.title.SelectKey" ) ); //$NON-NLS-1$

		dlg.setResourceURL( getResourceURL( ) );

		if ( dlg.open( ) == Window.OK )
		{
			handleSelectedEvent( (String[]) dlg.getDetailResult( ) );
		}
	}

	private void handleSelectedEvent( String[] values )
	{
		if ( values.length == 2 )
		{
			if ( values[0] != null )
				resourceText.setText( values[0] );
			if ( values[1] != null )
				labelEditor.setText( values[1] );
		}
	}
}
