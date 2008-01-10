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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ReportGraphicsViewPainter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.AddImageResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.graphics.ImageCanvas;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * ThumbnailBuilder
 */
public class ThumbnailBuilder extends BaseDialog
{

	private Listener currentListener;
	private final static int GENERATE_TYPE = 0;
	private final static int BROWSER_TYPE = 1;
	private final static int IMPORT_TYPE = 2;

	private static String DEFAULT_TEXT = Messages.getString( "ThumbnailBuilder.Text.Title" ); //$NON-NLS-1$

	private static String BUTTON_TEXT_GENERATE = Messages.getString( "ThumbnailBuilder.Button.Text.Generate" ); //$NON-NLS-1$

	private static String BUTTON_TEXT_REMOVE = Messages.getString( "ThumbnailBuilder.Button.Text.Remove" ); //$NON-NLS-1$

	private static String BUTTON_TEXT_IMPORT = Messages.getString( "ThumbnailBuilder.Button.Text.Import" ); //$NON-NLS-1$

	private ImageCanvas previewCanvas;

	private Button radioBtnGenerate, radioBtnBrowse, radioBtnImport;

	private Button btnImport, btnRemove;

	private String imageName;

	private Image image;

	private boolean hasThumbnail;

	private static final String IMAGE_FILTER[] = new String[]{
		"*.gif;*.jpg;*.jpeg;*.png;*.ico;*.bmp" //$NON-NLS-1$
	};

	private static final String[] IMAGE_TYPES = new String[]{
			".bmp", ".jpg", ".jpeg", ".gif", ".png", ".ico" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	};

	private ReportDesignHandle handle;

	/**
	 * @param title
	 */
	public ThumbnailBuilder( )
	{
		super( UIUtil.getDefaultShell( ), DEFAULT_TEXT );
	}

	/**
	 * @param parentShell
	 * @param title
	 */
	public ThumbnailBuilder( Shell parentShell, String title )
	{
		super( parentShell, title );

	}

	public Image getImage( )
	{
		return image;
	}

	public String getImageName( )
	{
		return imageName;
	}

	public void setImageName( String imageName )
	{
		this.imageName = imageName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite topComposite = (Composite) super.createDialogArea( parent );
		Composite composite = new Composite( topComposite, SWT.NONE );
		composite.setLayout( new GridLayout( 2, false ) );

		createSelectionArea( composite );
		createPreviewArea( composite );
		createButtons( composite );

		new Label( topComposite, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		UIUtil.bindHelp( parent, IHelpContextIds.THUMBNAIL_BUIDLER_ID );

		return topComposite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */

	protected void okPressed( )
	{
		if ( currentListener != null )
		{
			btnImport.removeListener( SWT.Selection, currentListener );
		}
		super.okPressed( );
	}

	public boolean shouldSetThumbnail( )
	{
		return hasThumbnail;
	}

	private void createSelectionArea( Composite composite )
	{
		Composite selectionArea = new Composite( composite, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		selectionArea.setLayoutData( gd );
		selectionArea.setLayout( new GridLayout( ) );

		radioBtnGenerate = new Button( selectionArea, SWT.RADIO );
		radioBtnGenerate.setText( Messages.getString( "ThumbnailBuilder.Button.GenerateFromReport" ) ); //$NON-NLS-1$
		radioBtnGenerate.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( GENERATE_TYPE );
			}
		} );

		radioBtnBrowse = new Button( selectionArea, SWT.RADIO );
		radioBtnBrowse.setText( Messages.getString( "ThumbnailBuilder.Button.BrowseFromFileSystem" ) ); //$NON-NLS-1$
		radioBtnBrowse.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( BROWSER_TYPE );
			}
		} );

		radioBtnImport = new Button( selectionArea, SWT.RADIO );
		radioBtnImport.setText( Messages.getString( "ThumbnailBuilder.Button.ImportFromResource" ) ); //$NON-NLS-1$
		radioBtnImport.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( IMPORT_TYPE );
			}
		} );

	}

	private void switchTo( int type )
	{
		if ( currentListener != null )
		{
			btnImport.removeListener( SWT.Selection, currentListener );
		}

		switch ( type )
		{
			case GENERATE_TYPE :

				radioBtnGenerate.setSelection( true );
				radioBtnBrowse.setSelection( false );
				radioBtnImport.setSelection( false );

				btnImport.setText( BUTTON_TEXT_GENERATE );
				LayoutButtons( );
				currentListener = btnGenerateListener;
				btnImport.addListener( SWT.Selection, currentListener );
				break;
			case BROWSER_TYPE :
				radioBtnGenerate.setSelection( false );
				radioBtnBrowse.setSelection( true );
				radioBtnImport.setSelection( false );

				btnImport.setText( Messages.getString( "ThumbnailBuilder.Button.Text.Browse" ) ); //$NON-NLS-1$
				LayoutButtons( );
				currentListener = btnBrowseListener;
				btnImport.addListener( SWT.Selection, currentListener );
				break;
			case IMPORT_TYPE :
				radioBtnGenerate.setSelection( false );
				radioBtnBrowse.setSelection( false );
				radioBtnImport.setSelection( true );
				btnImport.setText( BUTTON_TEXT_IMPORT );
				LayoutButtons( );
				currentListener = btnImportListener;
				btnImport.addListener( SWT.Selection, currentListener );
				break;
			default :
				break;
		}

	}

	private void createPreviewArea( Composite composite )
	{
		Composite previewArea = new Composite( composite, SWT.BORDER );
		GridData gd = new GridData( GridData.BEGINNING );
		gd.widthHint = 184;
		gd.heightHint = 229;
		previewArea.setLayoutData( gd );
		previewArea.setLayout( new FormLayout( ) );

		previewCanvas = new ImageCanvas( previewArea );

		FormData formData = new FormData( 180, 229 );
		formData.left = new FormAttachment( previewArea );
		formData.top = new FormAttachment( previewArea );

		previewCanvas.setLayoutData( formData );
	}

	protected boolean initDialog( )
	{
		hasThumbnail = false;
		ModuleHandle moduleHandle = getModuleHandle( );
		if ( ( moduleHandle == null )
				|| ( !( moduleHandle instanceof ReportDesignHandle ) ) )
		{
			btnImport.setEnabled( false );
			btnRemove.setEnabled( false );

			return true;
		}

		ReportDesignHandle handle = (ReportDesignHandle) moduleHandle;
		byte[] thumbnailData = handle.getThumbnail( );
		if ( thumbnailData == null || thumbnailData.length == 0 )
		{
			btnRemove.setEnabled( false );
			return true;
		}
		else
		{
			ByteArrayInputStream inputStream = new ByteArrayInputStream( thumbnailData );
			image = new Image( null, inputStream );

			previewCanvas.clear( );
			previewCanvas.loadImage( image );
			hasThumbnail = true;
		}
		switchTo( GENERATE_TYPE );
		return true;
	}

	public ReportDesignHandle getModuleHandle( )
	{
		if ( handle == null )
		{
			return (ReportDesignHandle) SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
		}
		else
		{
			return handle;
		}

	}

	public void setReportDesignHandle( ReportDesignHandle handle )
	{
		this.handle = handle;
	}

	private void createButtons( Composite composite )
	{
		Composite buttonArea = new Composite( composite, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_VERTICAL );
		buttonArea.setLayoutData( gd );
		buttonArea.setLayout( new GridLayout( 1, false ) );

		gd = new GridData( );
		gd.horizontalAlignment = GridData.FILL;

		btnImport = new Button( buttonArea, SWT.PUSH );
		btnImport.setLayoutData( gd );

		btnRemove = new Button( buttonArea, SWT.PUSH );
		btnRemove.setText( BUTTON_TEXT_REMOVE );
		btnRemove.setLayoutData( gd );
		btnRemove.addListener( SWT.Selection, btnRemoveListener );

	}

	private void LayoutButtons( )
	{

		GridData gd = new GridData( );
		gd.horizontalAlignment = GridData.FILL_HORIZONTAL;

		Point pnt1 = btnImport.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
		Point pnt2 = btnRemove.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
		gd.widthHint = Math.max( pnt1.x, pnt2.x );

		btnImport.setLayoutData( gd );
		btnRemove.setLayoutData( gd );

		Control control = getDialogArea( );

		if ( control instanceof Composite )
		{
			( (Composite) control ).layout( );
		}
		getShell( ).pack( );
	}

	private Listener btnGenerateListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			removeImage( );

			ReportGraphicsViewPainter painter = new ReportGraphicsViewPainter( getModuleHandle( ) );

			previewCanvas.layout( );

			Rectangle rect = previewCanvas.getBounds( );
			image = new Image( null, rect.width, rect.height );

			painter.paint( image, previewCanvas.getDisplay( ), rect );

			painter.dispose( );

			previewCanvas.loadImage( image );

			btnRemove.setEnabled( true );
			hasThumbnail = true;
			imageName = Messages.getString( "ThumbnailBuilder.Image.DefaultName" ); //$NON-NLS-1$
		}
	};

	private Listener btnRemoveListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			removeImage( );
			hasThumbnail = false;
			imageName = ""; //$NON-NLS-1$
		}
	};

	private Listener btnBrowseListener = new Listener( ) {

		public void handleEvent( Event event )
		{

			String fileName = null;
			FileDialog dialog = new FileDialog( getShell( ), SWT.OPEN );
			dialog.setText( Messages.getString( "ThumbnailBuilder.FileDialog.Title" ) ); //$NON-NLS-1$
			dialog.setFilterExtensions( IMAGE_FILTER );
			fileName = dialog.open( );
			if ( fileName == null || fileName.trim( ).length( ) == 0 )
			{
				return;
			}

			if ( checkExtensions( fileName ) == false )
			{
				ExceptionHandler.openErrorMessageBox( Messages.getString( "ThumbnailBuilder.FileDialog.FileNameError.Title" ), //$NON-NLS-1$
						Messages.getString( "ThumbnailBuilder.FileDialog.FileNameError.Message" ) ); //$NON-NLS-1$
				return;
			}

			removeImage( );
			hasThumbnail = true;

			image = new Image( null, fileName );

			previewCanvas.loadImage( image );
			btnRemove.setEnabled( true );

			imageName = fileName;
		}
	};

	private Listener btnImportListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			String fileName = null;

			AddImageResourceFileFolderSelectionDialog dlg = new AddImageResourceFileFolderSelectionDialog( );
			if ( dlg.open( ) != Window.OK )
			{
				return;
			}
			fileName = dlg.getPath( );
			if ( fileName == null || fileName.trim( ).length( ) == 0 )
			{
				return;
			}
			URL url = getModuleHandle( ).findResource( fileName,
					IResourceLocator.IMAGE );
			imageName = fileName;
			try
			{
				fileName = FileLocator.resolve( url ).getPath( );
			}
			catch ( IOException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}
			if ( checkExtensions( fileName ) == false )
			{
				ExceptionHandler.openErrorMessageBox( Messages.getString( "ThumbnailBuilder.FileDialog.FileNameError.Title" ), //$NON-NLS-1$
						Messages.getString( "ThumbnailBuilder.FileDialog.FileNameError.Message" ) ); //$NON-NLS-1$
				return;
			}

			removeImage( );
			hasThumbnail = true;

			image = new Image( null, fileName );

			previewCanvas.loadImage( image );
			btnRemove.setEnabled( true );

		}
	};

	private void removeImage( )
	{
		// if it's an image
		if ( image != null )
		{
			image.dispose( );
			image = null;
		}

		previewCanvas.clear( );

		hasThumbnail = false;

		btnRemove.setEnabled( false );
	}

	private boolean checkExtensions( String fileName )
	{
		for ( int i = 0; i < IMAGE_TYPES.length; i++ )
		{
			if ( fileName.toLowerCase( ).endsWith( IMAGE_TYPES[i] ) )
			{
				return true;
			}
		}
		return false;
	}

}
