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

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.impl.ImageContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.ImageScriptExecutor;
import org.eclipse.birt.report.engine.util.FileUtil;
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
 * @version $Revision: 1.28 $ $Date: 2005/12/03 02:01:49 $
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
	public ImageItemExecutor( ExecutionContext context,
			IReportItemVisitor visitor )
	{
		super( context, visitor );
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
	public void execute( ReportItemDesign item, IContentEmitter emitter )
	{
		assert item instanceof ImageItemDesign;
		ImageItemDesign imageItem = (ImageItemDesign) item;
		IImageContent imageContent = report.createImageContent( );
		IContent parent = context.getContent( );
		context.pushContent( imageContent );

		openResultSet( item );
		accessQuery( item, emitter );

		initializeContent( parent, item, imageContent );

		// handle the Action and Bookmark values
		processAction( item, imageContent );
		processBookmark( item, imageContent );
		processStyle( item, imageContent );
		processVisibility( item, imageContent );

		handleImage( imageItem, imageContent );

		// execute the onCreate
		if ( context.isInFactory( ) )
		{
			ImageScriptExecutor.handleOnCreate( (ImageContent) imageContent,
					context );
		}
		startTOCEntry( imageContent );
		// forward to emitter for further processing
		if ( emitter != null )
		{
			emitter.startImage( imageContent );
		}
		finishTOCEntry( );
		closeResultSet( );
		context.popContent( );

	}

	protected void handleImage( ImageItemDesign imageDesign,
			IImageContent imageContent )
	{
		// Handles the image according to its type
		switch ( imageDesign.getImageSource( ) )
		{
			case ImageItemDesign.IMAGE_URI : // URI
				Expression imageExpr = imageDesign.getImageUri( );
				if ( imageExpr != null )
				{
					handleURIImage( imageExpr, imageContent );
				}
				break;

			case ImageItemDesign.IMAGE_FILE : // File
				Expression fileExpr = imageDesign.getImageUri( );
				assert fileExpr != null;
				handleFileImage( fileExpr, imageContent );
				break;

			case ImageItemDesign.IMAGE_NAME : // embedded image
				String imageName = imageDesign.getImageName( );
				assert imageName != null;

				handleNamedImage( imageName, imageContent );
				break;

			case ImageItemDesign.IMAGE_EXPRESSION : // get image from
				// database

				Expression imgExpr = imageDesign.getImageExpression( );
				Expression fmtExpr = imageDesign.getImageFormat( );
				assert imgExpr != null;

				handleValueImage( imgExpr, fmtExpr, imageContent );

				break;
			default :
				logger.log( Level.SEVERE,
						"[ImageItemExecutor] invalid image source" ); //$NON-NLS-1$
				context.addException( new EngineException(
						MessageConstants.INVALID_IMAGE_SOURCE_TYPE_ERROR ) );

				assert false;
		}
	}

	protected void handleURIImage( Expression uriExpr,
			IImageContent imageContent )
	{
		// the expression is an expression, but UI may use
		// the expression as the string constants, so first try
		// to evaluate, if there are some errors, use it as a
		// string.
		imageContent.setImageSource( IImageContent.IMAGE_URI );

		assert uriExpr != null;
		Object uriObj = context.evaluate( uriExpr );
		String strUri = null;
		if ( uriObj != null )
		{
			strUri = uriObj.toString( );
		}
		else if ( uriExpr.getExpression( ) != null
				&& uriExpr.getExpression( ).length( ) > 0 )
		{
			strUri = uriExpr.getExpression( ).toString( );
		}
		URL uri = null;
		try
		{
			uri = new URL( strUri );
		}
		catch ( MalformedURLException e1 )
		{
		}
		if ( uri != null )
		{
			if ( uri.getProtocol( ).equals( "file" ) )
			{
				handleFileImage( strUri, imageContent );
			}
			else
			{
				imageContent.setURI( strUri );
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

	protected void handleValueImage( Expression imgExpr, Expression fmtExpr,
			IImageContent imageContent )
	{
		byte[] imgData = null;
		String imgExt = "";

		imageContent.setImageSource( IImageContent.IMAGE_EXPRESSION );

		Object value = context.evaluate( imgExpr );
		if ( value instanceof byte[] )
		{
			imgData = (byte[]) value;
		}
		if ( fmtExpr != null )
		{
			Object strValue = context.evaluate( fmtExpr );
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

	protected void handleFileImage( Expression fileExpr,
			IImageContent imageContent )
	{
		String imageFile = "";
		Object file = context.evaluate( fileExpr );
		if ( file != null )
		{
			imageFile = file.toString( );
		}
		else if ( fileExpr.getExpression( ) != null
				&& fileExpr.getExpression( ).length( ) > 0 )
		{
			imageFile = fileExpr.getExpression( ).toString( );
		}
		handleFileImage( imageFile, imageContent );
	}

	protected void handleFileImage( String imageFile, IImageContent imageContent )
	{

		// image file may be file: or a file path.
		try
		{
			URL url = new URL( imageFile );
			if ( url.getProtocol( ).equals( "file" ) ) //$NON-NLS-1$
			{
				try
				{
					imageFile = URLDecoder.decode( url.getFile( ), "UTF-8" );
				}
				catch ( Exception ex )
				{
					imageFile = url.getFile( );
				}
			}
		}
		catch ( MalformedURLException ex )
		{
			// imageFile is a file name
		}

		imageFile = FileUtil.getAbsolutePath( context.getReport( )
				.getBasePath( ), imageFile );
		imageContent.setExtension( FileUtil.getExtFromFileName( imageFile,
				FileUtil.SEPARATOR_PATH ) );
		// Here uses the absolute file name as the URI for the image
		// resource. The content of the file is loaded lazily to
		// improve
		// the performance.
		imageContent.setURI( imageFile );
		imageContent.setImageSource( IImageContent.IMAGE_FILE );

		if ( imageFile == null )
		{
			logger.log( Level.SEVERE,
					"[ImageItemExecutor] Source image file is missing" ); //$NON-NLS-1$
			context.addException( new EngineException(
					MessageConstants.MISSING_IMAGE_FILE_ERROR ) );

		}
	}
}