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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

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
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
			".bmp", //$NON-NLS-1$
			".jpg", //$NON-NLS-1$
			".jpeg", //$NON-NLS-1$
			".jpe", //$NON-NLS-1$
			".jfif", //$NON-NLS-1$
			".gif", //$NON-NLS-1$
			".png", //$NON-NLS-1$
			".tif", //$NON-NLS-1$
			".tiff", //$NON-NLS-1$
			".ico", //$NON-NLS-1$
			".svg" //$NON-NLS-1$
	};

	private static final String[] IMAGE_FILEFILTERS = new String[]{
		"*.bmp;*.jpg;*.jpeg;*.jpe;*.jfif;*.gif;*.png;*.tif;*.tiff;*.ico;*.svg" //$NON-NLS-1$
	};

	public static final String DLG_TITLE_NEW = Messages.getString( "ImageBuilder.DialogTitle.New" ); //$NON-NLS-1$

	public static final String DLG_TITLE_EDIT = Messages.getString( "ImageBuilder.DialogTitle.Edit" ); //$NON-NLS-1$

	private static final String BUTTON_BROWSE_TOOLTIP = Messages.getString( "ImageBuilder.Button.Browser.Tooltip" ); //$NON-NLS-1$

	private static final String BUTTON_IMPORT = Messages.getString( "ImageBuilder.Button.Import" ); //$NON-NLS-1$

	private static final String BUTTON_SELECT_IMAGE_DATA = Messages.getString( "ImageBuilder.Button.SelectImageData" ); //$NON-NLS-1$

	private static final String TYPE_URI = Messages.getString( "ImageBuilder.Type.URI" ); //$NON-NLS-1$

	private static final String TYPE_RESOURCE_FILE = Messages.getString( "ImageBuilder.Type.ResourceFile" ); //$NON-NLS-1$

	private static final String TYPE_EMBEDDED_IMAGE = Messages.getString( "ImageBuilder.Type.EmbededImage" ); //$NON-NLS-1$

	private static final String TYPE_DYNAMIC_IMAGE = Messages.getString( "ImageBuilder.Type.DynamicImage" ); //$NON-NLS-1$

	private static final String DLG_SELECT_PICTURE_LABEL = Messages.getString( "ImageBuilder.Label.SelectFrom" ); //$NON-NLS-1$

	private static final String DLG_INSERT_BUTTON_MSG = Messages.getString( "ImageBuilder.Button.Insert" ); //$NON-NLS-1$

	private static final String DLG_TITLE_IMPORT_FAIL = Messages.getString( "ImageBuilder.DialogTitle.ImportFailed" ); //$NON-NLS-1$;

	private static final String DLG_TITLE_LOADING_FAIL = Messages.getString( "ImageBuilder.DialogTitle.LoadingFailed" ); //$NON-NLS-1$;

	private static final String DLG_ERROR_MSG_LOADING_FAIL = Messages.getString( "ImageBuilder.ErrorMessage.LoadingFailed" ); //$NON-NLS-1$;

	private static final String DLG_ERROR_MSG_FILE_EXISTS = Messages.getString( "ImageBuilder.ErrorMessage.FileLoaded" ); //$NON-NLS-1$

	private ImageHandle inputImage;

	private Button embedded, uri, dynamic, resource, inputButton, importButton,
			expressionButton;

	private Composite inputArea;

	private Label description;

	private ImageCanvas previewCanvas;

	private Text uriEditor;

	private List embeddedImageList;

	private static final int URI_TYPE = 0;

	private static final int FILE_TYPE = 1;

	private static final int EMBEDDED_TYPE = 2;

	private static final int BLOB_TYPE = 3;

	private static final Map descriptionMap = new HashMap( );

	static
	{
		descriptionMap.put( new Integer( URI_TYPE ),
				Messages.getString( "ImageBuilder.Description.URI" ) ); //$NON-NLS-1$
		descriptionMap.put( new Integer( FILE_TYPE ),
				Messages.getString( "ImageBuilder.Description.ResourceFile" ) ); //$NON-NLS-1$
		descriptionMap.put( new Integer( EMBEDDED_TYPE ),
				Messages.getString( "ImageBuilder.Description.Embedded" ) ); //$NON-NLS-1$
		descriptionMap.put( new Integer( BLOB_TYPE ),
				Messages.getString( "ImageBuilder.Description.Dynamic" ) ); //$NON-NLS-1$
	}

	private static final Map uriEditorLabelMap = new HashMap( );

	static
	{
		uriEditorLabelMap.put( new Integer( URI_TYPE ),
				Messages.getString( "ImageBuilder.Label.EnterURI" ) ); //$NON-NLS-1$
		uriEditorLabelMap.put( new Integer( FILE_TYPE ),
				Messages.getString( "ImageBuilder.Label.EnterResourceFile" ) ); //$NON-NLS-1$
		uriEditorLabelMap.put( new Integer( BLOB_TYPE ),
				Messages.getString( "ImageBuilder.Label.EnterExpr" ) ); //$NON-NLS-1$
	}

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
			inputImage = DesignElementFactory.getInstance( ).newImage( null );
			setOKLabel( DLG_INSERT_BUTTON_MSG );
		}

		Composite topCompostie = (Composite) super.createDialogArea( parent );

		createSelectionArea( topCompostie );

		GridData topGd = new GridData( );
		int width = topCompostie.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		topGd.widthHint = width > 432 ? width : 432;
		topCompostie.setLayoutData( topGd );

		new Label( topCompostie, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite composite = new Composite( topCompostie, SWT.NONE );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		composite.setLayout( new GridLayout( ) );

		description = new Label( composite, SWT.NONE );
		description.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

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
		uri.setText( TYPE_URI );
		uri.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( URI_TYPE );
			}
		} );

		resource = new Button( selectionArea, SWT.RADIO );
		resource.setText( TYPE_RESOURCE_FILE ); //$NON-NLS-1$
		resource.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( FILE_TYPE );
			}
		} );

		embedded = new Button( selectionArea, SWT.RADIO );
		embedded.setText( TYPE_EMBEDDED_IMAGE );
		embedded.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( EMBEDDED_TYPE );
			}
		} );

		dynamic = new Button( selectionArea, SWT.RADIO );
		dynamic.setText( TYPE_DYNAMIC_IMAGE ); //$NON-NLS-1$
		dynamic.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchTo( BLOB_TYPE );
			}
		} );
	}

	private void createInputArea( Composite parent )
	{
		inputArea = new Composite( parent, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.heightHint = 80;
		inputArea.setLayoutData( gd );
		inputArea.setLayout( new GridLayout( 3, false ) );
	}

	private void createPreviewArea( Composite composite )
	{
		Composite previewArea = new Composite( composite, SWT.BORDER );
		GridData gd = new GridData( );
		gd.heightHint = 240;
		gd.widthHint = 315;
		previewArea.setLayoutData( gd );
		previewArea.setLayout( new FillLayout( ) );

		previewCanvas = new ImageCanvas( previewArea );
	}

	private void switchTo( int type )
	{
		if ( type == selectedType )
		{ // the same type,nothing needed to do
			return;
		}
		selectedType = type;

		clearPreview( );
		description.setText( (String) descriptionMap.get( new Integer( type ) ) );

		Control[] controls = inputArea.getChildren( );
		for ( int i = 0; i < controls.length; i++ )
		{
			controls[i].dispose( );
		}
		buildInputAreaUI( type );
		inputArea.layout( );
	}

	private void buildInputAreaUI( int type )
	{
		// Text editor
		if ( type == EMBEDDED_TYPE )
		{
			buildEmbeddedImageList( );
		}
		else
		{
			buildURIEditor( type );
		}
		// Button
		buildInputAreaButton( type );
	}

	private void buildURIEditor( int type )
	{
		// Indication Label
		Label uriEditorLabel = new Label( inputArea, SWT.NONE );
		GridData labelGd = new GridData( GridData.FILL_HORIZONTAL );
		labelGd.horizontalSpan = 3;
		uriEditorLabel.setLayoutData( labelGd );
		uriEditorLabel.setText( (String) uriEditorLabelMap.get( new Integer( type ) ) );

		uriEditor = new Text( inputArea, SWT.SINGLE | SWT.BORDER );
		GridData textGd = new GridData( GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL );
		// textGd.widthHint = 308;
		// if ( type == BLOB_TYPE )
		// {
		// textGd.widthHint = 270;
		// textGd.grabExcessHorizontalSpace = true;
		// }
		uriEditor.setLayoutData( textGd );
		uriEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );
		uriEditor.addFocusListener( new FocusListener( ) {

			public void focusGained( FocusEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void focusLost( FocusEvent e )
			{
				// TODO Auto-generated method stub
				preview( DEUtil.RemoveQuote( uriEditor.getText( ) ) );
			}
		} );

		initURIEditor( );
	}

	private void buildEmbeddedImageList( )
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

		initList( );
	}

	public void setOpenFileButtonImage( Button button )
	{
		String imageName = IReportGraphicConstants.ICON_OPEN_FILE;
		Image image = ReportPlatformUIImages.getImage( imageName );

		GridData gd = new GridData( );
		if ( !Platform.getOS( ).equals( Platform.OS_MACOSX ) )
		{
			gd.widthHint = 20;
			gd.heightHint = 20;
		}
		button.setLayoutData( gd );

		button.setImage( image );
		if ( button.getImage( ) != null )
		{
			button.getImage( ).setBackground( button.getBackground( ) );
		}

	}

	private void buildInputAreaButton( int type )
	{

		if ( type == URI_TYPE )
		{
			inputButton = new Button( inputArea, SWT.PUSH );
			// inputButton.setEnabled( true );
			UIUtil.setExpressionButtonImage( inputButton );
			inputButton.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent event )
				{
					openExpression( );
				}
			} );
			new Label( inputArea, SWT.NONE );
		}
		else if ( type == FILE_TYPE )
		{
			inputButton = new Button( inputArea, SWT.PUSH );
			// inputButton.setText( BUTTON_BROWSE ); //$NON-NLS-1$
			setOpenFileButtonImage( inputButton );
			inputButton.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent event )
				{
					openResourceBrowser( );
				}
			} );
			inputButton.setToolTipText( BUTTON_BROWSE_TOOLTIP );

			expressionButton = new Button( inputArea, SWT.PUSH );
			UIUtil.setExpressionButtonImage( expressionButton );
			expressionButton.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent event )
				{
					ExpressionBuilder expressionBuilder = new ExpressionBuilder( uriEditor.getText( ) );
					unionDataSets( );

					expressionBuilder.setExpressionProvier( new ExpressionProvider( inputImage ) );
					if ( expressionBuilder.open( ) == OK )
					{
						String uri = expressionBuilder.getResult( );
						uriEditor.setText( uri );
						preview( DEUtil.RemoveQuote( uri ) );
					}
				}
			} );
		}
		else if ( type == EMBEDDED_TYPE )
		{
			importButton = new Button( inputArea, SWT.PUSH );
			importButton.setText( BUTTON_IMPORT );
			importButton.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_END ) );
			importButton.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent event )
				{
					FileDialog fileChooser = new FileDialog( getShell( ),
							SWT.OPEN );
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
										.loadImage( inputImage.getModuleHandle( ),
												fullPath ) );
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

			new Label( inputArea, SWT.NONE );
		}
		else if ( type == BLOB_TYPE )
		{
			inputButton = new Button( inputArea, SWT.PUSH );
			inputButton.setText( BUTTON_SELECT_IMAGE_DATA ); //$NON-NLS-1$
			inputButton.setEnabled( !getModuleHandle( ).getVisibleDataSets( )
					.isEmpty( ) );
			inputButton.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent event )
				{
					openBidingDialog( );
				}
			} );
			new Label( inputArea, SWT.NONE );
		}
	}

	protected void openResourceBrowser( )
	{

		ResourceSelectionValidator validator = new ResourceSelectionValidator( IMAGE_TYPES );

		ResourceFileFolderSelectionDialog dialog = new ResourceFileFolderSelectionDialog( true,
				true,
				IMAGE_FILEFILTERS );
		dialog.setTitle( Messages.getString( "ImageBuilder.BrowserResourceDialog.Title" ) ); //$NON-NLS-1$
		dialog.setMessage( Messages.getString( "ImageBuilder.BrowserResourceDialog.Message" ) ); //$NON-NLS-1$
		dialog.setValidator( validator );
		// dialog.setInput( ReportPlugin.getDefault( ).getResourcePreference( )

		if ( dialog.open( ) == Window.OK )
		{
			uriEditor.setText( "\"" + dialog.getPath( ) + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$
			preview( dialog.getPath( ) );
		}
	}

	private void preview( String uri )
	{
		try
		{
			Image image = ImageManager.getInstance( )
					.loadImage( inputImage.getModuleHandle( ), uri );
			previewCanvas.loadImage( image );
		}
		catch ( Exception e )
		{
			clearPreview( );
			logger.log( Level.WARNING, e.getLocalizedMessage( ) );
			// ExceptionHandler.handle( e,
			// DLG_TITLE_LOADING_FAIL,
			// DLG_ERROR_MSG_LOADING_FAIL );
		}
	}

	private void preview( )
	{
		if ( embeddedImageList.getSelectionCount( ) > 0 )
		{
			Image image = ImageManager.getInstance( )
					.getEmbeddedImage( inputImage.getModuleHandle( ),
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
			initURIEditor( );
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equals( inputImage.getSource( ) ) )
		{
			resource.setSelection( true );
			switchTo( FILE_TYPE );
			initURIEditor( );
		}
		else
		{// initialize as URI mode by default
			uri.setSelection( true );
			switchTo( URI_TYPE );
			initURIEditor( );
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
			};
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
		if ( !uri.equals( "" ) && selectedType != BLOB_TYPE ) //$NON-NLS-1$
		{
			preview( DEUtil.RemoveQuote( uri ) );
		}
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
				break;
			case URI_TYPE :
				complete = !StringUtil.isBlank( uriEditor.getText( ) );
				break;
			case EMBEDDED_TYPE :
				complete = ( embeddedImageList.getSelectionIndex( ) != -1 );
				break;
			case FILE_TYPE :
				complete = !StringUtil.isBlank( uriEditor.getText( ) );
				break;
		}
		getOkButton( ).setEnabled( complete );
	}

	private void openExpression( )
	{
		ExpressionBuilder expressionBuilder = new ExpressionBuilder( uriEditor.getText( ) );
		unionDataSets( );

		expressionBuilder.setExpressionProvier( new ExpressionProvider( inputImage ) );
		if ( expressionBuilder.open( ) == OK )
		{
			String uri = expressionBuilder.getResult( );
			uriEditor.setText( uri );
			preview( DEUtil.RemoveQuote( uri ) );
		}
	}

	private void openBidingDialog( )
	{
		ReportItemHandle handle = (ReportItemHandle) inputImage;
		ColumnBindingDialog dialog = new ColumnBindingDialog( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ),
				true );
		dialog.setInput( handle );
		if ( dialog.open( ) == Dialog.OK )
		{
			String columnExpr;
			Object obj = DEUtil.getFirstDataSource( handle );
			if ( obj != null && obj instanceof CubeHandle )
				columnExpr = DEUtil.getDataExpression( (String) dialog.getResult( ) );
			else
				columnExpr = DEUtil.getColumnExpression( (String) dialog.getResult( ) );
			uriEditor.setText( columnExpr );
			try
			{
				inputImage.setValueExpression( uriEditor.getText( ).trim( ) );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
		}
		/*
		 * I don't understand the downward code, the dialog cancels then we
		 * don't should do anything. The author hlin had left company now.
		 * Comments it. / /* else { if ( inputImage.getDataSet( ) == null ) {
		 * uriEditor.setText( "" ); } }
		 */
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

}