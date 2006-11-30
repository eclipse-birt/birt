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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * A dialog which can browser all properties in BIRT resource folder. User can
 * select a properties or enter new file name to creat a new one.
 * 
 */

public class ProjectFileDialog  extends ElementTreeSelectionDialog
{


	protected String newFileName = ""; //$NON-NLS-1$

	private Status OKStatus = new Status( IStatus.OK,
			ReportPlugin.REPORT_UI,
			IStatus.OK,
			"", null ); //$NON-NLS-1$
	private Status ErrorStatus = new Status( IStatus.ERROR,
			ReportPlugin.REPORT_UI,
			IStatus.ERROR,
			Messages.getString( "ProjectFileDialog.ErrorMessage" ), //$NON-NLS-1$
			null );
	private Status ErrorStatusNoSelection = new Status( IStatus.ERROR,
			ReportPlugin.REPORT_UI,
			IStatus.ERROR,
			Messages.getString( "" ), //$NON-NLS-1$
			null );
	

	private class Validator implements ISelectionStatusValidator
	{

		public IStatus validate( Object[] selection )
		{
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
				return OKStatus;
			}
			else 
				return ErrorStatus;
		}
	}
	
	
	public String getPath( )
	{
		Object[] selected = getResult( );
		if ( selected.length > 0  )
		{
			return getPath(0);
		}
		return null;
	}

	public String getPath( int index )
	{
		Object[] selected = getResult( );
		if ( index < 0 || index >= selected.length ||  input == null )
		{
			return null;
		}
		FilePathEntry entry = (FilePathEntry) selected[index];
		
		String path = entry.getURL( ).getFile( );
		if ( entry.getURL( ) .getProtocol( ).equals( "file" ) ) //$NON-NLS-1$
		{
			return URIUtil.resolveAbsolutePath( input, URIUtil.getRelativePath( input , path ));
		}
		return path;

	}

	private String input;
	public ProjectFileDialog( String input)
	{
		super(UIUtil.getDefaultShell( ),
				new FileLabelProvider( input ),
				new FileContentProvider( true ));
		this.input = input;
		setInput( new ResourceEntry[]{ new FilePathEntry( input ) } );
		setDoubleClickSelects( true );
		setValidator( new Validator( ) );
		setAllowMultiple( false );
		setTitle( Messages.getString( "ProjectFileDialog.Title" ) ); //$NON-NLS-1$
		setMessage( Messages.getString( "ProjectFileDialog.Message" ) ); //$NON-NLS-1$
	}
	
	public ProjectFileDialog( String input ,String[] filePattern)
	{
		super(UIUtil.getDefaultShell( ),
				new FileLabelProvider( input ),
				new FileContentProvider( true ));
		this.input = input;
		setInput( new ResourceEntry[]{ new FilePathEntry( input ,filePattern ) } );
		setDoubleClickSelects( true );
		setValidator( new Validator( ) );
		setAllowMultiple( false );
		setTitle( Messages.getString( "ProjectFileDialog.Title" ) ); //$NON-NLS-1$
		setMessage( Messages.getString( "ProjectFileDialog.Message" ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite rt = (Composite) super.createDialogArea( parent );
		addToolTip( );
		rt.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		getTreeViewer( ).expandToLevel( 2 );
		UIUtil.bindHelp( parent,
				IHelpContextIds.PROJECT_FILES_DIALOG_ID );
		return rt;
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
		if ( selected.length > 0 && !newFileName.equals( "" ) ) //$NON-NLS-1$
		{
			ResourceEntry entry = (ResourceEntry) selected[0];
			File file = new File( entry.getURL( ).getPath( ) );
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



	/**
	 * Add Tooltip for root TreeItem.
	 */
	protected void addToolTip( )
	{
		final Tree tree = getTreeViewer( ).getTree( );
		tree.addMouseTrackListener( new MouseTrackAdapter( ) {

			public void mouseHover( MouseEvent event )
			{
				Widget widget = event.widget;
				if ( widget == tree )
				{
					Point pt = new Point( event.x, event.y );
					TreeItem item = tree.getItem( pt );

					if ( item == null )
					{
						tree.setToolTipText( null );
					}
					else
					{
						if ( getTreeViewer( ).getLabelProvider( ) instanceof FileLabelProvider )
						{
							tree.setToolTipText( ( (FileLabelProvider) getTreeViewer( ).getLabelProvider( ) ).getToolTip( item.getData( ) ) );
						}
						else
						{
							tree.setToolTipText( null );
						}
					}
				}
			}
		} );
	}


}
