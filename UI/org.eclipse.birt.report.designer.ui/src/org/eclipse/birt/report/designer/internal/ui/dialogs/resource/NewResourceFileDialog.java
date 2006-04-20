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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * A dialog which can browser all properties in BIRT resource folder. User can
 * select a properties or enter new file name to creat a new one.
 * 
 */

public class NewResourceFileDialog extends ResourceFileFolderSelectionDialog
{

	private Text text;
	private String ext = ".properties"; //$NON-NLS-1$

	protected String newFileName = ""; //$NON-NLS-1$

	private Status OKStatus = new Status( IStatus.OK,
			ReportPlugin.REPORT_UI,
			IStatus.OK,
			"", null ); //$NON-NLS-1$
	private Status ErrorStatus = new Status( IStatus.ERROR,
			ReportPlugin.REPORT_UI,
			IStatus.ERROR,
			Messages.getString( "NewResourceFileDialog.ErrorMessage" ), //$NON-NLS-1$
			null );

	private class Validator implements ISelectionStatusValidator
	{

		public IStatus validate( Object[] selection )
		{
			int nSelected = selection.length;
			if ( nSelected == 0 || nSelected > 1 )
			{
				return ErrorStatus;
			}
			if ( selection[0] instanceof File
					&& ( (File) selection[0] ).isFile( ) )
			{
				return OKStatus;
			}
			if ( newFileName == null
					|| !newFileName.toLowerCase( )
							.endsWith( ext.toLowerCase( ) ) )
			{
				return ErrorStatus;
			}
			return OKStatus;
		}
	}

	public NewResourceFileDialog( Shell parent, ILabelProvider labelProvider,
			ITreeContentProvider contentProvider )
	{
		super( parent, labelProvider, contentProvider );
		setDoubleClickSelects( true );
		setValidator( new Validator( ) );
		setAllowMultiple( false );
		setInput( getResourceRootFile( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite rt = (Composite) super.createDialogArea( parent );
		rt.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Composite pane = new Composite( rt, 0 );
		pane.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		pane.setLayout( new GridLayout( 2, false ) );
		pane.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Label lb = new Label( pane, 0 );
		lb.setText( Messages.getString( "NewResourceFileDialog.label.NewFile" ) );//$NON-NLS-1$

		text = new Text( pane, SWT.BORDER );
		text.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		text.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				newFileName = text.getText( );
				updateOKStatus( );
			}
		} );

		configViewer( );

		return rt;
	}

	private void configViewer( )
	{

		getTreeViewer( ).addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				Object object = ( (StructuredSelection) event.getSelection( ) ).getFirstElement( );
				if ( object instanceof File )
				{
					File file = (File) object;
					if ( file.isDirectory( ) )
					{
						text.setEnabled( true );
					}
					else
					{
						text.setText( "" ); //$NON-NLS-1$
						text.setEnabled( false );
					}
				}
				else
				{
					text.setText( "" ); //$NON-NLS-1$
					text.setEnabled( false );
				}
			}
		} );

	}

	private String getResourceRootFile( )
	{
		return ReportPlugin.getDefault( ).getResourcePreference( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#okPressed()
	 */
	protected void okPressed( )
	{
		super.okPressed( );
		Object[] selected = getResult( );
		if ( selected.length > 0 )
		{
			File file = (File) selected[0];
			try
			{
				new File( file, newFileName ).createNewFile( );
			}
			catch ( IOException e )
			{
				ExceptionHandler.handle( e );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.resource.FileFolderSelectionDialog#getPath()
	 */
	public String getPath( )
	{
		if ( !newFileName.equals( "" ) ) //$NON-NLS-1$
		{
			return super.getPath( ) + newFileName;
		}
		else
		{
			return super.getPath( );
		}
	}
}
