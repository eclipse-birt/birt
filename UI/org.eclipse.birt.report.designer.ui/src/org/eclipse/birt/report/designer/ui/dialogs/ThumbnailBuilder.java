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
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ReportGraphicsViewComposite;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.graphics.ImageCanvas;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
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
 * 
 */

public class ThumbnailBuilder extends BaseDialog
{

	private static String DEFAULT_TEXT = Messages.getString( "ThumbnailBuilder.Text.Title" );

	private static String BUTTON_TEXT_GENERATE = Messages.getString( "ThumbnailBuilder.Button.Text.Generate" );

	private static String BUTTON_TEXT_REMOVE = Messages.getString( "ThumbnailBuilder.Button.Text.Remove" );

	private static String BUTTON_TEXT_IMPORT = Messages.getString( "ThumbnailBuilder.Button.Text.Import" );

	private ImageCanvas previewCanvas;

	private Composite previewThumbnail;

	private Button btnGenerate, btnRemove, btnImport;

	private Image image;

	boolean isGenerated;

	boolean hasThumbnail;

	String IMAGE_FILTER[] = new String[]{
		"*.gif;*.jpg;*.png;*.ico;*.bmp" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	};

	public Image getImage( )
	{
		return image;
	}

	private static final String[] IMAGE_TYPES = new String[]{
			".bmp", ".jpg", ".gif", ".png", ".ico"
	};

	private ReportDesignHandle handle;

	/**
	 * @param parentShell
	 * @param title
	 */
	public ThumbnailBuilder( Shell parentShell, String title )
	{
		super( parentShell, title );

	}

	/**
	 * @param title
	 */
	public ThumbnailBuilder( )
	{
		super( UIUtil.getDefaultShell( ), DEFAULT_TEXT );
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
		createPreviewArea( composite );
		createButtons( composite );

		new Label( topComposite, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		return topComposite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */

	protected void okPressed( )
	{

		if ( hasThumbnail )
		{
			if ( isGenerated )
			{
				image = UIUtil.newImageFromComposite( previewThumbnail );
			}
			Assert.isNotNull( image );
		}

		super.okPressed( );
	}

	public boolean shouldSetThumbnail( )
	{
		return hasThumbnail;
	}

	private void createPreviewArea( Composite composite )
	{
		Composite previewArea = new Composite( composite, SWT.BORDER );
		GridData gd = new GridData( GridData.BEGINNING );
		gd.widthHint = 180;
		gd.heightHint = 229;
		previewArea.setLayoutData( gd );
		previewArea.setLayout( new FormLayout( ) );

		previewCanvas = new ImageCanvas( previewArea );
		previewThumbnail = new Composite( previewArea, SWT.NONE );

		FormData formData = new FormData( 180, 229 );
		formData.left = new FormAttachment( previewArea );
		formData.top = new FormAttachment( previewArea );

		previewCanvas.setLayoutData( formData );

		previewThumbnail.setLayoutData( formData );
		previewThumbnail.setLayout( new FillLayout( ) );
	}

	protected boolean initDialog( )
	{
		isGenerated = false;
		hasThumbnail = false;
		ModuleHandle moduleHandle = getModuleHandle( );
		if ( ( moduleHandle == null )
				|| ( !( moduleHandle instanceof ReportDesignHandle ) ) )
		{
			btnGenerate.setEnabled( false );
			btnRemove.setEnabled( false );
			btnImport.setEnabled( false );
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
		// have image data
		{
			ByteArrayInputStream inputStream = new ByteArrayInputStream( thumbnailData );
			image = new Image( null, inputStream );
			previewCanvas.setVisible( true );
			previewThumbnail.setVisible( false );

			previewCanvas.clear( );
			previewCanvas.loadImage( ( (Image) image ) );
			// previewCanvas.showOriginal( );
			hasThumbnail = true;
		}

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

		btnGenerate = new Button( buttonArea, SWT.PUSH );
		btnGenerate.setText( BUTTON_TEXT_GENERATE );
		btnGenerate.setLayoutData( gd );
		btnGenerate.addListener( SWT.Selection, btnGenerateListener );

		btnRemove = new Button( buttonArea, SWT.PUSH );
		btnRemove.setText( BUTTON_TEXT_REMOVE );
		btnRemove.setLayoutData( gd );
		btnRemove.addListener( SWT.Selection, btnRemoveListener );

		btnImport = new Button( buttonArea, SWT.PUSH );
		btnImport.setText( BUTTON_TEXT_IMPORT );
		btnImport.setLayoutData( gd );
		btnImport.addListener( SWT.Selection, btnImportListener );
	}

	private Listener btnGenerateListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			removeImage( );

			previewCanvas.setVisible( false );
			previewThumbnail.setVisible( true );

			ReportGraphicsViewComposite thumbnail = new ReportGraphicsViewComposite( previewThumbnail,
					SWT.NULL,
					getModuleHandle( ) );

			previewThumbnail.layout( );
			btnRemove.setEnabled( true );
			isGenerated = true;
			hasThumbnail = true;
		}
	};

	private Listener btnRemoveListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			removeImage( );
			hasThumbnail = false;
		}
	};

	private Listener btnImportListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			String fileName = null;
			FileDialog dialog = new FileDialog( getShell( ), SWT.OPEN );
			dialog.setText( Messages.getString( "ThumbnailBuilder.FileDialog.Title" ) ); //$NON-NLS-1$
			dialog.setFilterExtensions( IMAGE_FILTER );
			fileName = dialog.open( ).trim( );
			if ( fileName == null || fileName.length( ) == 0 )
			{
				return;
			}

			if ( checkExtensions( fileName ) == false )
			{
				ExceptionHandler.openErrorMessageBox( Messages.getString( "ThumbnailBuilder.FileDialog.FileNameError.Title" ),
						Messages.getString( "ThumbnailBuilder.FileDialog.FileNameError.Message" ) );
				return;
			}

			removeImage( );
			isGenerated = false;
			hasThumbnail = true;

			image = new Image( null, fileName );

			previewThumbnail.setVisible( false );
			previewCanvas.setVisible( true );

			previewCanvas.loadImage( image );
			// previewCanvas.showOriginal( );
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
		
		if ( isGenerated == false )
		{
			previewCanvas.clear( );
		}
		else // if it's generated
		if ( isGenerated )
		{
			Control[] children = previewThumbnail.getChildren( );
			for ( int i = 0; i < children.length; i++ )
			{
				children[i].dispose( );
			}
		}
		
		isGenerated = false;
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
