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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryChangeEvent;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelectionChangedListener;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * 
 */

public class ExportElementDialog extends ResourceFileFolderSelectionDialog
{

	private Text text;
	private String ext = ".rptlibrary"; //$NON-NLS-1$
	private Object designElement;

	protected String newFileName = ""; //$NON-NLS-1$

	private Status OKStatus = new Status( IStatus.OK,
			ReportPlugin.REPORT_UI,
			IStatus.OK,
			"", null ); //$NON-NLS-1$
	private Status ErrorStatus = new Status( IStatus.ERROR,
			ReportPlugin.REPORT_UI,
			IStatus.ERROR,
			Messages.getString( "ExportElementDialog.ErrorMessage" ), //$NON-NLS-1$
			null );
	private Status ErrorStatusNoSelection = new Status( IStatus.ERROR,
			ReportPlugin.REPORT_UI,
			IStatus.ERROR,
			Messages.getString( "" ), //$NON-NLS-1$
			null );
	private Status ErrorStatusInvalid = new Status( IStatus.ERROR,
			ReportPlugin.REPORT_UI,
			IStatus.ERROR,
			Messages.getString( "ExportElementDialog.ErrorMessageInvalid" ), //$NON-NLS-1$
			null );

	private Status ErrorStatusDuplicate = new Status( IStatus.ERROR,
			ReportPlugin.REPORT_UI,
			IStatus.ERROR,
			Messages.getString( "ExportElementDialog.ErrorMessageDuplicate" ), //$NON-NLS-1$
			null );

	private Status ErrorStatusCanNotExport = new Status( IStatus.ERROR,
			ReportPlugin.REPORT_UI,
			IStatus.ERROR,
			Messages.getString( "ExportElementDialog.ErrorStatusCanNotExport" ), //$NON-NLS-1$
			null );

	private class Validator implements ISelectionStatusValidator
	{

		public IStatus validate( Object[] selection )
		{
			Status status = OKStatus;
			int nSelected = selection.length;
			if ( nSelected == 0 )
			{
				return ErrorStatusNoSelection;
			}
			else if ( nSelected > 1 )
			{
				return ErrorStatus;
			}
			else if ( selection[0] instanceof ResourceEntry
					&& ( (ResourceEntry) selection[0] ).isFile( ) )
			{
				status =  OKStatus;
			}
			else if ( newFileName == null
					|| !newFileName.toLowerCase( )
							.endsWith( ext.toLowerCase( ) ) )
			{
				return ErrorStatus;
			}
			else if ( newFileName == null
					|| newFileName.toLowerCase( ).equals( ext.toLowerCase( ) ) )
			{
				return ErrorStatusInvalid;
			}
			
			if(status == OKStatus)
			// check if the element can be exported
			{
				String path = getPath( );
				try
				{
					ModuleHandle handle = SessionHandleAdapter.getInstance( )
							.getSessionHandle( )
							.openLibrary( path );
					LibraryHandle libraryHandle = (LibraryHandle) handle;
					boolean can = false;
					boolean override = false;
					if ( designElement instanceof DesignElementHandle )
					{
						can = ElementExportUtil.canExport( (DesignElementHandle) designElement,
								libraryHandle,
								false );
						override = ElementExportUtil.canExport( (DesignElementHandle) designElement,
								libraryHandle,
								true );
					}
					else if ( designElement instanceof StructureHandle )
					{
						can = ElementExportUtil.canExport( (StructureHandle) designElement,
								libraryHandle,
								false );
						override = ElementExportUtil.canExport( (StructureHandle) designElement,
								libraryHandle,
								false );
					}
					libraryHandle.close( );
					if ( can == false )						
					{
						if(override)
						{
							return ErrorStatusDuplicate;
						}else
						{
							return ErrorStatusCanNotExport;
						}
						
					}

				}
				catch ( DesignFileException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace( );
//					return ErrorStatusDuplicate;
				}
			}
			return OKStatus;
		}
	}

	public ExportElementDialog( Object selection )
	{
		super( false, new String[]{
			"*.rptlibrary" //$NON-NLS-1$
			} );
		setDoubleClickSelects( true );
		setValidator( new Validator( ) );
		setAllowMultiple( false );
		// setInput( getResourceRootFile( ) );
		setTitle( Messages.getString( "ExportElementDialog.Dialog.Title" ) ); //$NON-NLS-1$
		setMessage( Messages.getString( "ExportElementDialog.Dialog.Message" ) ); //$NON-NLS-1$
		designElement = selection;
		if(designElement instanceof StructuredSelection)
		{
			designElement = ((StructuredSelection)designElement).getFirstElement();
		}

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
		lb.setText( Messages.getString( "ExportElementDialog.label.NewFile" ) );//$NON-NLS-1$

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
		// UIUtil.bindHelp( parent,
		// IHelpContextIds.NEW_ADD_RESOURCE_FILES_DIALOG_ID );
		return rt;
	}

	private void configViewer( )
	{

		getTreeViewer( ).addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				Object object = ( (StructuredSelection) event.getSelection( ) ).getFirstElement( );
				if ( object instanceof ResourceEntry )
				{
					ResourceEntry entry = (ResourceEntry) object;
					if ( entry.getURL( ) != null
							&& entry.getURL( ).getProtocol( ).equals( "file" ) ) //$NON-NLS-1$
					{
						File file = new File( entry.getURL( ).getPath( ) );
						text.setEnabled( file.isDirectory( ) );
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
				if(!text.isEnabled())
				{
					newFileName = "";
				}else
				{
					newFileName = text.getText();
				}
			}
		} );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#okPressed()
	 */
	protected void okPressed( )
	{		
		Object firstElement = designElement;
		if(firstElement instanceof StructuredSelection)
		{
			firstElement = ((StructuredSelection)firstElement).getFirstElement();
		}
		
		try
		{
			if ( firstElement instanceof DesignElementHandle )
			{

				ElementExportUtil.exportElement( (DesignElementHandle) firstElement,
						getPath(),
						false );

			}
			else if ( firstElement instanceof StructureHandle )
			{
				ElementExportUtil.exportStructure( (StructureHandle) firstElement,
						getPath(),
						false );
			}
			
			fireDesigFileChangeEvent(getPath());

		}
		catch ( DesignFileException e )
		{
			// TODO Auto-generated catch block
			ExceptionHandler.handle( e );
			e.printStackTrace( );
		}
		catch ( SemanticException e )
		{
			// TODO Auto-generated catch block
			ExceptionHandler.handle( e );
			e.printStackTrace( );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			ExceptionHandler.handle( e );
			e.printStackTrace( );
		}

		super.okPressed( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.resource.FileFolderSelectionDialog#getPath()
	 */
	public String getPath( )
	{
		Object[] selected = getResult( );
		String fullPath = "";
		if ( selected.length > 0 ) //$NON-NLS-1$
		{
			ResourceEntry entry = (ResourceEntry) selected[0];
			fullPath = entry.getURL( ).getPath( );
			File file = new File( fullPath);
			if(file.isDirectory())
			{
				fullPath = fullPath
				+ ( ( fullPath.equals( "" ) || fullPath.endsWith( "/" ) ) ? "" : "/" ) //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
				+ newFileName;
			}
		}else
		{
			// rcp should be different
			String resouceFolder = ReportPlugin.getDefault( )
			.getResourceFolder( );
			fullPath = resouceFolder
			+ ( ( resouceFolder.equals( "" ) || resouceFolder.endsWith( "/" ) ) ? "" : "/" ) //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
			+ newFileName;
		}
		return fullPath;
	}
	
	private void fireDesigFileChangeEvent( String filename )
	{
		SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.fireResourceChange( new LibraryChangeEvent( filename ) );

	}
}
