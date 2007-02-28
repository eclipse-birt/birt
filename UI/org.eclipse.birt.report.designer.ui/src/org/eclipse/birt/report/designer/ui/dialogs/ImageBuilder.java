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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.runtime.GUIException;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceSelectionValidator;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.graphics.BirtImageLoader;
import org.eclipse.birt.report.designer.internal.ui.util.graphics.ImageCanvas;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Dialog to build the image element.
 */

public class ImageBuilder extends BaseDialog
{

	private static final String[] IMAGE_TYPES = new String[]{
			".bmp",
			".jpg",
			".jpeg",
			".jpe",
			".jfif",
			".gif",
			".png",
			".tif",
			".tiff",
			".ico",
			".svg"
	};

	private static final String[] IMAGE_FILEFILTERS = new String[]{
		"*.bmp;*.jpg;*.jpeg;*.jpe;*.jfif;*.gif;*.png;*.tif;*.tiff;*.ico;*.svg"
	};

	// private static final String DLG_REMOVE_BUTTON = Messages.getString(
	// "ImageBuilder.Button.Remove" ); //$NON-NLS-1$

	private static final String DLG_IMPORT_BUTTON = Messages.getString( "ImageBuilder.Button.Import" ); //$NON-NLS-1$

	private static final String DLG_PREVIEW_LABEL = Messages.getString( "ImageBuilder.Button.Preview" ); //$NON-NLS-1$

	private static final String DLG_ENTER_URI_LABEL = Messages.getString( "ImageBuilder.Label.EnterUri" ); //$NON-NLS-1$

	private static final String DLG_ENTER_RESOURCE_FILE_LABEL = Messages.getString( "ImageBuilder.Label.EnterResourceFile" ); //$NON-NLS-1$

	private static final String DLG_EMBEDDED_IMAGE_LABEL = Messages.getString( "ImageBuilder.Label.EmbededImage" ); //$NON-NLS-1$

	private static final String DLG_URI_LABEL = Messages.getString( "ImageBuilder.Label.URI" ); //$NON-NLS-1$

	private static final String DLG_SELECT_PICTURE_LABEL = Messages.getString( "ImageBuilder.Label.SelectFrom" ); //$NON-NLS-1$

	private static final String DLG_INSERT_BUTTON_MSG = Messages.getString( "ImageBuilder.Button.Insert" ); //$NON-NLS-1$

	public static final String DLG_TITLE_NEW = Messages.getString( "ImageBuilder.DialogTitle.New" ); //$NON-NLS-1$

	public static final String DLG_TITLE_EDIT = Messages.getString( "ImageBuilder.DialogTitle.Edit" ); //$NON-NLS-1$

	// private static final String SUPPORTED_IMAGE_FILE_EXTS =
	// Messages.getString( "ImageBuilder.FileDialog.FilterMessage" );
	// //$NON-NLS-1$

	private static final String DLG_TITLE_IMPORT_FAIL = Messages.getString( "ImageBuilder.DialogTitle.ImportFailed" ); //$NON-NLS-1$;

	private static final String DLG_TITLE_LOADING_FAIL = Messages.getString( "ImageBuilder.DialogTitle.LoadingFailed" ); //$NON-NLS-1$;

	private static final String DLG_ERROR_MSG_LOADING_FAIL = Messages.getString( "ImageBuilder.ErrorMessage.LoadingFailed" ); //$NON-NLS-1$;

	private static final String DLG_ERROR_MSG_FILE_EXISTS = Messages.getString( "ImageBuilder.ErrorMessage.FileLoaded" ); //$NON-NLS-1$

	private ImageHandle inputImage;

	private Button embedded, uri, previewButton, dynamic, bindingButton;

	private Button resource;

	private Composite inputArea;

	private ImageCanvas previewCanvas;

	private Text uriEditor;

	private List embeddedImageList;

	final private static int URI_TYPE = 0;

	final private static int EMBEDDED_TYPE = 1;

	final private static int BLOB_TYPE = 2;

	final private static int FILE_TYPE = 3;

	private int selectedType = -1;

	private java.util.List dataSetList = new ArrayList( );

	private java.util.List inputDataSetList = new ArrayList( );

	/**
	 * The constructor.
	 * 
	 * @param parentShell
	 */
	public ImageBuilder( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	/**
	 * The constructor.
	 * 
	 * @param parentShell
	 */
	public ImageBuilder( Shell parentShell, String title,
			java.util.List dataSetList )
	{
		super( parentShell, title );
		this.inputDataSetList = dataSetList;
		this.dataSetList = new ArrayList( inputDataSetList );
	}

	private ModuleHandle getModuleHandle( )
	{
		return SessionHandleAdapter.getInstance( ).getReportDesignHandle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		if ( inputImage == null )
		{
			// inputImage = getModuleHandle().getElementFactory( ).newImage(
			// null );
			inputImage = DesignElementFactory.getInstance( ).newImage( null );
			setOKLabel( DLG_INSERT_BUTTON_MSG );
		}

		Composite topCompostie = (Composite) super.createDialogArea( parent );
		createSelectionArea( topCompostie );

		new Label( topCompostie, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite composite = new Composite( topCompostie, SWT.NONE );
		composite.setLayout( new GridLayout( 2, false ) );

		createInputArea( composite );
		createPreviewArea( composite );

		new Label( topCompostie, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		UIUtil.bindHelp( parent, IHelpContextIds.IMAGE_BUIDLER_ID );
		return topCompostie;
	}

	private void createSelectionArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayout( new GridLayout( 2, false ) );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Label label = new Label( composite, SWT.NONE );
		label.setText( DLG_SELECT_PICTURE_LABEL );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );

		Composite selectionArea = new Composite( composite, SWT.NONE );
		selectionArea.setLayout( new FillLayout( SWT.VERTICAL ) );

		uri = new Button( selectionArea, SWT.RADIO );
		uri.setText( DLG_URI_LABEL );
		uri.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( URI_TYPE );
			}

		} );
		embedded = new Button( selectionArea, SWT.RADIO );
		embedded.setText( DLG_EMBEDDED_IMAGE_LABEL );
		embedded.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( EMBEDDED_TYPE );
			}
		} );

		dynamic = new Button( selectionArea, SWT.RADIO );
		dynamic.setText( Messages.getString( "ImageBuilder.Button.Dynamic" ) ); //$NON-NLS-1$
		dynamic.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( BLOB_TYPE );
			}
		} );

		resource = new Button( selectionArea, SWT.RADIO );
		resource.setText( Messages.getString( "ImageBuilder.ResourceFileType" ) ); //$NON-NLS-1$
		resource.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( FILE_TYPE );
			}
		} );

	}

	private void createInputArea( Composite parent )
	{
		inputArea = new Composite( parent, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_BOTH
				| GridData.HORIZONTAL_ALIGN_BEGINNING );
		gd.widthHint = 300;
		gd.heightHint = 300;
		inputArea.setLayoutData( gd );
		inputArea.setLayout( new GridLayout( ) );
	}

	private void createPreviewArea( Composite composite )
	{
		Composite previewArea = new Composite( composite, SWT.BORDER );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 250;
		gd.heightHint = 300;
		previewArea.setLayoutData( gd );
		previewArea.setLayout( new FillLayout( ) );

		previewCanvas = new ImageCanvas( previewArea );
	}

	private void switchTo( int type )
	{
		if ( type == selectedType )
		{// the same type,nothing needed to do
			return;
		}
		selectedType = type;
		Control[] controls = inputArea.getChildren( );
		for ( int i = 0; i < controls.length; i++ )
		{
			controls[i].dispose( );
		}

		clearPreview( );
		switch ( type )
		{
			case URI_TYPE :
				swtichToExprType( );
				break;
			case EMBEDDED_TYPE :
				swtichToEmbeddedType( );
				break;
			case BLOB_TYPE :
				swtichToExprType( );
				break;
			case FILE_TYPE :
				swtichToResourceType( );
				break;

		}
		inputArea.layout( );
	}

	/**
	 * Switch to select file in BIRT resource folder.
	 */
	private void swtichToResourceType( )
	{
		Label title = new Label( inputArea, SWT.NONE );

		uriEditor = new Text( inputArea, SWT.SINGLE
				| SWT.BORDER
				| SWT.READ_ONLY );
		uriEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		uriEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		Composite innerComp = new Composite( inputArea, SWT.NONE );
		innerComp.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		innerComp.setLayout( new GridLayout( 2, false ) );
		title.setText( DLG_ENTER_RESOURCE_FILE_LABEL );

		Button inputButton = new Button( innerComp, SWT.PUSH );
		inputButton.setText( Messages.getString( "ImageBuilder.ButtonBrowser" ) ); //$NON-NLS-1$
		inputButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		inputButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				openResourceBrowser( );
			}
		} );

		previewButton = new Button( innerComp, SWT.PUSH );
		previewButton.setText( DLG_PREVIEW_LABEL );
		previewButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		previewButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				uriEditor.setText( uriEditor.getText( ).trim( ) );
				preview( removeQuoteString( uriEditor.getText( ) ) );
			}
		} );

		initURIEditor( );
	}

	private void swtichToExprType( )
	{

		Label title = new Label( inputArea, SWT.NONE );

		uriEditor = new Text( inputArea, SWT.SINGLE | SWT.BORDER );
		uriEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		uriEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		Composite innerComp = new Composite( inputArea, SWT.NONE );
		innerComp.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		if ( selectedType == BLOB_TYPE )
		{
			innerComp.setLayout( new GridLayout( 3, false ) );
			title.setText( Messages.getString( "ImageBuilder.Label.EnterExpr" ) ); //$NON-NLS-1$
		}
		else
		{
			innerComp.setLayout( new GridLayout( 2, false ) );
			title.setText( DLG_ENTER_URI_LABEL );
		}

		Button inputButton = new Button( innerComp, SWT.PUSH );
//		inputButton.setText( Messages.getString( "ImageBuilder.Label.Open" ) ); //$NON-NLS-1$
//		inputButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		setExpressionButtonImage(inputButton);
		inputButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				openExpression( );
			}
		} );

		if ( selectedType == BLOB_TYPE )
		{
			bindingButton = new Button( innerComp, SWT.PUSH );
			bindingButton.setText( Messages.getString( "ImageBuilder.Label.Binding" ) ); //$NON-NLS-1$
			bindingButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
			bindingButton.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent event )
				{
					openBidingDialog( );
				}

			} );
		}

		previewButton = new Button( innerComp, SWT.PUSH );
		previewButton.setText( DLG_PREVIEW_LABEL );
		previewButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		previewButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				switch ( selectedType )
				{
					case URI_TYPE :
						uriEditor.setText( uriEditor.getText( ).trim( ) );
						preview( removeQuoteString( uriEditor.getText( ) ) );
						break;
				}

			}
		} );

		initURIEditor( );
	}

	protected void openResourceBrowser( )
	{
		// SelectResourceDialog dialog = new SelectResourceDialog( false );
		// if ( dialog.open( ) == Window.OK )
		// {
		// uriEditor.setText( dialog.getPath( ) );
		// }

		// ResourceFileLabelProvider labelProvider = new
		// ResourceFileLabelProvider( );
		// ResourceFileContentProvider contentProvider = new
		// ResourceFileContentProvider( IMAGE_TYPES );
		ResourceSelectionValidator validator = new ResourceSelectionValidator( IMAGE_TYPES );

		ResourceFileFolderSelectionDialog dialog = new ResourceFileFolderSelectionDialog( true,
				true,
				IMAGE_FILEFILTERS );
		dialog.setTitle( Messages.getString( "ImageBuilder.BrowserResourceDialog.Title" ) ); //$NON-NLS-1$
		dialog.setMessage( Messages.getString( "ImageBuilder.BrowserResourceDialog.Message" ) ); //$NON-NLS-1$
		dialog.setValidator( validator );
		// dialog.setInput( ReportPlugin.getDefault( ).getResourcePreference( )
		// );

		if ( dialog.open( ) == Window.OK )
		{
			uriEditor.setText( "\"" + dialog.getPath( ) + "\"" );  //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * @param value
	 * @return
	 */
	private String removeQuoteString( String value )
	{
		if ( value != null
				&& value.length( ) > 1
				&& value.charAt( 0 ) == '\"'
				&& value.charAt( value.length( ) - 1 ) == '\"' )
		{
			return value.substring( 1, value.length( ) - 1 );
		}
		return value;
	}

	private void swtichToEmbeddedType( )
	{
		embeddedImageList = new List( inputArea, SWT.NONE
				| SWT.SINGLE
				| SWT.BORDER
				| SWT.V_SCROLL
				| SWT.H_SCROLL );
		embeddedImageList.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		embeddedImageList.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				preview( );
				updateButtons( );
			}
		} );
		Composite buttonBar = new Composite( inputArea, SWT.NONE );
		buttonBar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		buttonBar.setLayout( new GridLayout( 2, false ) );
		Button importButton = new Button( buttonBar, SWT.PUSH );
		importButton.setText( DLG_IMPORT_BUTTON );
		GridData gd = new GridData( GridData.HORIZONTAL_ALIGN_END );
		gd.grabExcessHorizontalSpace = true;
		importButton.setLayoutData( gd );
		importButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				FileDialog fileChooser = new FileDialog( getShell( ), SWT.OPEN );
				fileChooser.setText( Messages.getString( "ImageBuilder.Chooser.Title" ) ); //$NON-NLS-1$
				fileChooser.setFilterExtensions( IMAGE_FILEFILTERS );
				try
				{
					String fullPath = fileChooser.open( );
					if ( fullPath != null )
					{
						String fileName = fileChooser.getFileName( );
						if ( fileName != null )
						{
							if ( embeddedImageList.indexOf( fileName ) != -1 )
							{
								ExceptionHandler.openMessageBox( DLG_TITLE_IMPORT_FAIL,
										DLG_ERROR_MSG_FILE_EXISTS,
										SWT.ICON_WARNING );
								return;
							}

							if ( checkExtensions( fileName ) == false )
							{
								ExceptionHandler.openErrorMessageBox( Messages.getString( "EmbeddedImagesNodeProvider.FileNameError.Title" ), //$NON-NLS-1$
										Messages.getString( "EmbeddedImagesNodeProvider.FileNameError.Message" ) ); //$NON-NLS-1$
								return;
							}

							previewCanvas.loadImage( ImageManager.getInstance( )
									.loadImage( fullPath ) );
							BirtImageLoader imageLoader = new BirtImageLoader( );
							EmbeddedImage image = imageLoader.save( getModuleHandle( ),
									fullPath,
									fileName );
							embeddedImageList.add( image.getName( ) );
							embeddedImageList.select( embeddedImageList.indexOf( image.getName( ) ) );

							updateButtons( );
						}
					}
				}
				catch ( Throwable e )
				{
					preview( );
					if ( e instanceof OutOfMemoryError )
					{
						e = GUIException.createGUIException( ReportPlugin.REPORT_UI,
								e );
					}
					ExceptionHandler.handle( e,
							DLG_TITLE_LOADING_FAIL,
							DLG_ERROR_MSG_LOADING_FAIL );

				}
			}
		} );

		// removeButton = new Button( buttonBar, SWT.PUSH );
		// removeButton.setText( DLG_REMOVE_BUTTON );
		// removeButton.setLayoutData( new GridData(
		// GridData.HORIZONTAL_ALIGN_END ) );
		// removeButton.addSelectionListener( new SelectionAdapter( ) {
		//
		// public void widgetSelected( SelectionEvent event )
		// {
		// int index = embeddedImageList.getSelectionIndex( );
		// String imageName = embeddedImageList.getItem( index );
		// try
		// {
		// reportHandle.dropImage( imageName );
		// embeddedImageList.remove( imageName );
		// }
		// catch ( SemanticException e )
		// {
		// ExceptionHandler.handle( e );
		// }
		// if ( index >= embeddedImageList.getItemCount( ) )
		// {//if the image
		// // removed is
		// // the last
		// // image
		// index = embeddedImageList.getItemCount( ) - 1;
		// }
		// if ( index != -1 )
		// {//if there are images remained
		// embeddedImageList.select( index );
		// preview( );
		// }
		// else
		// {// there's no image remained,update buttons
		// clearPreivew( );
		// updateButtons( );
		// }
		// }
		// } );

		initList( );
	}

	private void preview( String uri )
	{
		try
		{
			Image image = ImageManager.getInstance( ).loadImage( uri );
			previewCanvas.loadImage( image );
		}
		catch ( Exception e )
		{
			clearPreview( );
			ExceptionHandler.handle( e,
					DLG_TITLE_LOADING_FAIL,
					DLG_ERROR_MSG_LOADING_FAIL );
		}
	}

	private void preview( )
	{
		if ( embeddedImageList.getSelectionCount( ) > 0 )
		{
			Image image = ImageManager.getInstance( )
					.getImage( inputImage.getModuleHandle( ),
							embeddedImageList.getSelection( )[0] );
			try
			{
				previewCanvas.loadImage( image );
				return;
			}
			catch ( Exception e )
			{
				ExceptionHandler.handle( e,
						DLG_TITLE_LOADING_FAIL,
						DLG_ERROR_MSG_LOADING_FAIL );
			}
		}
		clearPreview( );
	}

	private void clearPreview( )
	{
		previewCanvas.clear( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */

	protected void okPressed( )
	{
		try
		{
			switch ( selectedType )
			{
				case FILE_TYPE :
					inputImage.setFile( uriEditor.getText( ).trim( ) );
					break;
				case URI_TYPE :
					inputImage.setURL( uriEditor.getText( ).trim( ) );
					break;
				case EMBEDDED_TYPE :
					inputImage.setImageName( embeddedImageList.getSelection( )[0] );
					break;
				case BLOB_TYPE :
					inputImage.setValueExpression( uriEditor.getText( ).trim( ) );
			}
			setResult( inputImage );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
		super.okPressed( );
	}

	/**
	 * Sets the model input.
	 * 
	 * @param input
	 */
	public void setInput( Object input )
	{
		Assert.isTrue( input instanceof ImageHandle );
		inputImage = (ImageHandle) input;
		if ( DesignChoiceConstants.IMAGE_REF_TYPE_NONE.equals( inputImage.getSource( ) ) )
		{
			setOKLabel( DLG_INSERT_BUTTON_MSG );
		}
	}

	protected boolean initDialog( )
	{
		if ( DesignChoiceConstants.IMAGE_REF_TYPE_EMBED.equals( inputImage.getSource( ) ) )
		{
			embedded.setSelection( true );
			switchTo( EMBEDDED_TYPE );
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_EXPR.equals( inputImage.getSource( ) ) )
		{
			dynamic.setSelection( true );
			switchTo( BLOB_TYPE );
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equals( inputImage.getSource( ) ) )
		{
			resource.setSelection( true );
			switchTo( FILE_TYPE );
		}
		else
		{// initialize as URI mode by default
			uri.setSelection( true );
			switchTo( URI_TYPE );
		}

		return true;
	}

	private void initURIEditor( )
	{
		String uri = ""; //$NON-NLS-1$
		if ( DesignChoiceConstants.IMAGE_REF_TYPE_URL.equals( inputImage.getSource( ) ) )
		{
			if ( inputImage.getURI( ) != null && selectedType == URI_TYPE )
			{
				uri = inputImage.getURI( );
			}
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equals( inputImage.getSource( ) ) )
		{
			if ( inputImage.getURI( ) != null && selectedType == FILE_TYPE )
			{
				uri = inputImage.getURI( );
			}
		}

		if ( DesignChoiceConstants.IMAGE_REF_TYPE_EXPR.equals( inputImage.getSource( ) )
				&& selectedType == BLOB_TYPE )
		{
			if ( inputImage.getValueExpression( ) != null )
			{
				uri = inputImage.getValueExpression( );
			}
		}
		uriEditor.setText( uri );
		uriEditor.setFocus( );
		// Listener will be called automatically
		clearPreview( );
	}

	private void initList( )
	{
		for ( Iterator itor = getModuleHandle( ).getVisibleImages( ).iterator( ); itor.hasNext( ); )
		{
			EmbeddedImageHandle handle = (EmbeddedImageHandle) itor.next( );
			embeddedImageList.add( handle.getQualifiedName( ) );
		}
		int index = -1;
		if ( DesignChoiceConstants.IMAGE_REF_TYPE_EMBED.equals( inputImage.getSource( ) ) )
		{
			String imageName = inputImage.getImageName( );
			if ( imageName != null )
			{
				index = embeddedImageList.indexOf( imageName );
			}
		}
		if ( index != -1 )
		{
			embeddedImageList.select( index );
			preview( );
		}
		else
		{// the original image cannot be resolved
			clearPreview( );
		}
		updateButtons( );
	}

	private void updateButtons( )
	{
		boolean complete = false;
		switch ( selectedType )
		{
			case BLOB_TYPE :
				complete = !StringUtil.isBlank( uriEditor.getText( ) );
				previewButton.setEnabled( false );
				if ( SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getVisibleDataSets( )
						.isEmpty( ) )
				{
					bindingButton.setEnabled( false );
				}
				else
				{
					bindingButton.setEnabled( true );
				}
				break;
			case URI_TYPE :
				complete = !StringUtil.isBlank( uriEditor.getText( ) );
				previewButton.setEnabled( complete );
				break;
			case EMBEDDED_TYPE :
				complete = ( embeddedImageList.getSelectionCount( ) > 0 );
				// removeButton.setEnabled( complete );
				break;
			case FILE_TYPE :
				complete = !StringUtil.isBlank( uriEditor.getText( ) );
				previewButton.setEnabled( complete );
				break;
		}
		getOkButton( ).setEnabled( complete );
	}

	private void openExpression( )
	{

		ExpressionBuilder expressionBuilder = new ExpressionBuilder( uriEditor.getText( ) );

		// if ( inputImage.getDataSet( ) != null )
		// {
		// ArrayList dataSetList = new ArrayList( );
		// dataSetList.add( inputImage.getDataSet( ) );

		unionDataSets( );
		ExpressionProvider provider = new ExpressionProvider( inputImage );
		expressionBuilder.setExpressionProvier( provider );
		// }
		if ( expressionBuilder.open( ) == OK )
		{
			uriEditor.setText( expressionBuilder.getResult( ) );
		}
	}

	private void openBidingDialog( )
	{
		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getCommandStack( );
		ReportItemHandle handle = (ReportItemHandle) inputImage;
		stack.startTrans( Messages.getString( "DesignerActionBarContributor.menu.element.editDataBinding" ) ); //$NON-NLS-1$
		DataBindingDialog dialog = new DataBindingDialog( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ),
				handle );

		if ( dialog.open( ) == Dialog.OK )
		{
			stack.commit( );
		}
		else
		{
			stack.rollback( );
		}
	}

	private java.util.List unionDataSets( )
	{
		dataSetList = new ArrayList( inputDataSetList );
		if ( inputImage == null || inputImage.getDataSet( ) == null )
		{
			return dataSetList;
		}
		int i = inputDataSetList.indexOf( inputImage.getDataSet( ) );
		if ( i == -1 )
		{
			dataSetList.add( inputImage.getDataSet( ) );
		}

		return dataSetList;
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

	protected void setExpressionButtonImage(Button button) {
		String imageName;
		if (button.isEnabled()) {
			imageName = IReportGraphicConstants.ICON_ENABLE_EXPRESSION_BUILDERS;
		} else {
			imageName = IReportGraphicConstants.ICON_DISABLE_EXPRESSION_BUILDERS;
		}
		Image image = ReportPlatformUIImages.getImage(imageName);

		GridData gd = new GridData(GridData.VERTICAL_ALIGN_END);
		gd.widthHint = 20;
		gd.heightHint = 20;
		button.setLayoutData(gd);

		button.setImage(image);
		if (button.getImage() != null) {
			button.getImage().setBackground(button.getBackground());
		}

	}
	
}