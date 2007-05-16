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

package org.eclipse.birt.report.engine.executor;

import java.net.URL;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;

/**
 * <code>ImageItemExecutor</code> is a concrete subclass of
 * <code>StyledItemExecutor</code> that manipulate different types of images.
 * An image can be represented in a design file in one of the following forms:
 * 
 * <li>
 * <h4>URL</h4>
 * <ul>
 * The identifier for the image if the type is File or URL. The type of image is
 * inferred from the image file content or HTTP response.
 * </ul>
 * <li>
 * <h4>Embedded</h4>
 * <ul>
 * The image is embedded in the design file. The <code>ImageItemExecutor</code>
 * gets the binary data of the image content (DE is responsible for decoding
 * BASE64 image content), and saves it to a temporary file before forwarding to
 * the corresponding emitter.
 * </ul>
 * <li>
 * <h4>Database</h4>
 * <ul>
 * The image comes from a BLOB field in the query. It also required to save the
 * image content to a temporary file.
 * </ul>
 * 
 */
public class ImageItemExecutor extends QueryItemExecutor
{

	/**
	 * Creates an ImageItemExecutor using this constructor.
	 * 
	 * @param context
	 *            The execution context.
	 * @param visitor
	 *            The report visitor.
	 */
	public ImageItemExecutor( ExecutorManager manager )
	{
		super( manager, ExecutorManager.IMAGEITEM );
	}

	/**
	 * execute the image. The execution process is:
	 * 
	 * <li> create the image content
	 * <li> push it into the stack
	 * <li> open the query and seek to the first record
	 * <li> intialize the content
	 * <li> process action, bookmark, style and visibility
	 * <li> process the image content
	 * <li> execute the onCreate if necessary
	 * <li> call emitter to output the image
	 * <li> close query
	 * <li> popup the image
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#excute(org.eclipse.birt.report.engine.ir.ReportItemDesign,
	 *      org.eclipse.birt.report.engine.emitter.IReportEmitter)
	 */
	public IContent execute( )
	{
		ImageItemDesign imageDesign = (ImageItemDesign) getDesign();
		
		IImageContent imageContent = report.createImageContent( );
		setContent(imageContent);
		
		executeQuery( );
		
		initializeContent( imageDesign, imageContent );

		// handle the Action and Bookmark values
		processAction( imageDesign, imageContent );
		processBookmark( imageDesign, imageContent );
		processStyle( imageDesign, imageContent );
		processVisibility( imageDesign, imageContent );
		
		handleImage( imageDesign, imageContent );
		
		// execute the onCreate
		if ( context.isInFactory( ) )
		{
			handleOnCreate( imageContent );
		}
		
		startTOCEntry( imageContent );
		
		return imageContent;
	}
	
	public void close( )
	{
		finishTOCEntry( );
		closeQuery( );
		super.close( );
	}

	protected void handleImage( ImageItemDesign imageDesign,
			IImageContent imageContent )
	{
		// Handles the image according to its type
		switch ( imageDesign.getImageSource( ) )
		{
			case ImageItemDesign.IMAGE_URI : // URI
				String imageExpr = imageDesign.getImageUri( );
				if ( imageExpr != null )
				{
					handleURIImage( imageExpr, imageContent );
				}
				break;

			case ImageItemDesign.IMAGE_FILE : // File
				String fileExpr = imageDesign.getImageUri( );
				assert fileExpr != null;
				handleFileExpressionImage( fileExpr, imageContent );
				break;

			case ImageItemDesign.IMAGE_NAME : // embedded image
				String imageName = imageDesign.getImageName( );
				assert imageName != null;

				handleNamedImage( imageName, imageContent );
				break;

			case ImageItemDesign.IMAGE_EXPRESSION : // get image from
				// database

				String imgExpr = imageDesign.getImageExpression( );
				String fmtExpr = imageDesign.getImageFormat( );
				assert imgExpr != null;

				handleValueImage( imgExpr, fmtExpr, imageContent );

				break;
			default :
				logger.log( Level.SEVERE,
						"[ImageItemExecutor] invalid image source" ); //$NON-NLS-1$
				context.addException( imageDesign.getHandle( ), new EngineException(
						MessageConstants.INVALID_IMAGE_SOURCE_TYPE_ERROR ) );

				assert false;
		}
	}

	protected void handleURIImage( String uriExpr,
			IImageContent imageContent )
	{
		// the expression is an expression, but UI may use
		// the expression as the string constants, so first try
		// to evaluate, if there are some errors, use it as a
		// string.
		imageContent.setImageSource( IImageContent.IMAGE_URL );

		assert uriExpr != null;
		Object uriObj = evaluate( uriExpr );
		String strUri = null;
		if ( uriObj != null )
		{
			strUri = uriObj.toString( );
		}
		else if ( uriExpr != null && uriExpr.length( ) > 0 )
		{
			strUri = uriExpr;
		}
		
		ReportDesignHandle reportDesign = context.getDesign( );
		URL uri = reportDesign.findResource( strUri,
				IResourceLocator.IMAGE );
		if ( uri != null )
		{
			if ( "file".equals( uri.getProtocol( ) ) )
			{
				handleFileImage( strUri, imageContent );
			}
			else
			{
				imageContent.setURI( uri.toExternalForm( ) );
			}
		}
		else
		{
			handleFileImage( strUri, imageContent );
		}
	}

	protected void handleNamedImage( String imageName,
			IImageContent imageContent )
	{
		imageContent.setImageSource( IImageContent.IMAGE_NAME );
		imageContent.setURI( null );

		EmbeddedImage embeddedImage = context.getReport( ).getReportDesign( )
				.findImage( imageName );
		if ( embeddedImage != null )
		{
			imageContent.setData( embeddedImage.getData( context.getDesign( )
					.getModule( ) ) );
			String extension = FileUtil.getExtFromType( embeddedImage
					.getType( context.getDesign( ).getModule( ) ) );
			if ( extension != null )
			{
				imageContent.setExtension( extension );
			}

			imageContent.setURI( imageName );
		}

	}

	protected void handleValueImage( String imgExpr, String fmtExpr,
			IImageContent imageContent )
	{
		byte[] imgData = null;
		String imgExt = "";

		imageContent.setImageSource( IImageContent.IMAGE_EXPRESSION );

		Object value = evaluate( imgExpr );
		if ( value instanceof byte[] )
		{
			imgData = (byte[]) value;
		}
		if ( fmtExpr != null )
		{
			Object strValue = evaluate( fmtExpr );
			if ( strValue != null )
			{
				imgExt = strValue.toString( );
				imgExt = FileUtil.getExtFromType( imgExt );
			}
		}

		if ( imgData != null )
		{
			imageContent.setData( imgData );
			imageContent.setExtension( imgExt );
			imageContent.setURI( null );
		}
	}

	protected void handleFileExpressionImage( String fileExpr,
			IImageContent imageContent )
	{
		String imageFile = "";
		Object file = evaluate( fileExpr );
		if ( file != null )
		{
			imageFile = file.toString( );
		}
		else if ( fileExpr != null && fileExpr.length( ) > 0 )
		{
			imageFile = fileExpr;
		}
		handleFileImage( imageFile, imageContent );
	}

	protected void handleFileImage( String imageFile, IImageContent imageContent )
	{

		// image file may be file: or a file path.
		ReportDesignHandle reportDesign = context.getDesign( );
		if ( reportDesign != null )
		{
			URL url = reportDesign.findResource( imageFile,
					IResourceLocator.IMAGE );
			if ( url != null )
			{
				imageFile = url.toString( );
			}
		}

		// Here uses the absolute file name as the URI for the image
		// resource. The content of the file is loaded lazily to
		// improve the performance.
		imageContent.setURI( imageFile );
		imageContent.setImageSource( IImageContent.IMAGE_FILE );
		imageContent.setExtension( FileUtil.getExtFromFileName( imageFile ) );

		if ( imageFile == null )
		{
			logger.log( Level.SEVERE,
					"[ImageItemExecutor] Source image file is missing" ); //$NON-NLS-1$
			context.addException( design.getHandle( ), new EngineException(
					MessageConstants.MISSING_IMAGE_FILE_ERROR ) );

		}
	}
	
}