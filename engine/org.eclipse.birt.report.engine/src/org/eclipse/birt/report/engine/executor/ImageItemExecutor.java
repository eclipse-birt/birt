/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import static org.eclipse.birt.report.engine.ir.DimensionType.UNITS_IN;

import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
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
		processUserProperties( imageDesign, imageContent );
		
		try
		{
			handleImage( imageDesign, imageContent );
			// process proportional scale property
			processProportionalScale( imageDesign, imageContent );
		}
		catch ( BirtException ex )
		{
			context.addException( ex );
		}
		
		// execute the onCreate
		if ( context.isInFactory( ) )
		{
			handleOnCreate( imageContent );
		}
		
		startTOCEntry( imageContent );
		
		return imageContent;
	}

	private void processProportionalScale( ImageItemDesign imageDesign,
			IImageContent imageContent )
	{
		DimensionType width = imageDesign.getWidth( );
		DimensionType height = imageDesign.getHeight( );

		if ( imageDesign.isProportionalScale( )
				&& width != null && height != null )
		{
			com.itextpdf.text.Image imageData = EmitterUtil.getImage( imageContent );

			if ( imageData != null )
			{
				double iw = imageData.getWidth( );
				double ih = imageData.getHeight( );
				iw = iw < 0 ? 1 : iw;
				ih = ih < 0 ? 1 : ih;

				double rw = 0;
				double rh = 0;
				String targetUnit = UNITS_IN;
				if ( width.getUnits( ).equalsIgnoreCase( height.getUnits( ) ) )
				{
					targetUnit = width.getUnits( );
					rw = width.getMeasure( );
					rh = height.getMeasure( );
				}
				else
				{
					try
					{
						rw = width.convertTo( UNITS_IN );
						rh = height.convertTo( UNITS_IN );
					}
					catch ( IllegalArgumentException e )
					{
						rw = PropertyUtil.getDimensionValue( imageContent,
								width ) / 1000.0;
						rh = PropertyUtil.getDimensionValue( imageContent,
								height ) / 1000.0;
						if ( rw == 0 || rh == 0 )
						{
							return;
						}
						targetUnit = DimensionType.UNITS_PT;
					}
				}

				double wRatio = rw / iw;
				double hRatio = rh / ih;

				if ( wRatio < hRatio )
				{
					height = new DimensionType( ih * wRatio, targetUnit );
					imageContent.setHeight( height );
				}
				else
				{
					width = new DimensionType( iw * hRatio, targetUnit );
					imageContent.setWidth( width );
				}
			}
		}
	}


	public void close( ) throws BirtException
	{
		finishTOCEntry( );
		closeQuery( );
		super.close( );
	}

	protected void handleImage( ImageItemDesign imageDesign,
			IImageContent imageContent ) throws BirtException
	{
		// Handles the image according to its type
		switch ( imageDesign.getImageSource( ) )
		{
			case ImageItemDesign.IMAGE_URI : // URI
				Expression uriExpr = imageDesign.getImageUri( );
				if ( uriExpr != null )
				{
					handleURIImage( uriExpr, imageContent );
				}
				break;

			case ImageItemDesign.IMAGE_FILE : // File
				Expression fileExpr = imageDesign.getImageUri( );
				assert fileExpr != null;
				handleFileExpressionImage( fileExpr, imageContent );
				break;

			case ImageItemDesign.IMAGE_NAME : // embedded image
				Expression imageName = imageDesign.getImageName( );
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
				getLogger( ).log( Level.SEVERE,
						"[ImageItemExecutor] invalid image source" ); //$NON-NLS-1$
				context.addException( imageDesign.getHandle( ), new EngineException(
						MessageConstants.INVALID_IMAGE_SOURCE_TYPE_ERROR ) );

				assert false;
		}
	}

	protected void handleURIImage( Expression uriExpr,
			IImageContent imageContent ) throws BirtException
	{
		// the expression is an expression, but UI may use
		// the expression as the string constants, so first try
		// to evaluate, if there are some errors, use it as a
		// string.
		imageContent.setImageSource( IImageContent.IMAGE_URL );
		assert uriExpr != null;

		String strUri = evaluateString( uriExpr );
		if ( strUri == null )
		{
			strUri = uriExpr.getScriptText( );
		}
		imageContent.setURI( strUri );
	}

	protected void handleNamedImage( Expression nameExpr,
			IImageContent imageContent ) throws BirtException
	{
		imageContent.setImageSource( IImageContent.IMAGE_NAME );
		imageContent.setURI( null );

		String imageName = evaluateString( nameExpr );
		if ( imageName == null )
		{
			imageName = nameExpr.getScriptText( );
		}

		EmbeddedImage embeddedImage = context.getReport( ).getReportDesign( )
				.findImage( imageName );
		if ( embeddedImage != null )
		{
			imageContent.setData( embeddedImage.getData( context.getDesign( )
					.getModule( ) ) );
			String mimeType = embeddedImage.getType( context.getDesign( )
					.getModule( ) );
			if ( null != mimeType )
			{
				imageContent.setMIMEType( mimeType );
				String extension = FileUtil.getExtFromType( mimeType );
				if ( extension != null )
				{
					imageContent.setExtension( extension );
				}
			}
			else
			{
				String extension = FileUtil.getExtFromFileName( imageName );
				if ( extension != null )
				{
					imageContent.setExtension( extension );
					mimeType = FileUtil.getTypeFromExt( extension );
					if ( null != mimeType )
					{
						imageContent.setMIMEType( mimeType );
					}
				}
			}
			imageContent.setName( imageName );
		}
	}

	protected void handleValueImage( Expression imgExpr, Expression fmtExpr,
			IImageContent imageContent ) throws BirtException
	{
		byte[] imgData = null;
		String imgExt = null;
		String mimeType = null;

		imageContent.setImageSource( IImageContent.IMAGE_EXPRESSION );

		Object value = evaluate( imgExpr ); // throw directly in case of
											// exceptions
		if ( value instanceof byte[] )
		{
			imgData = (byte[]) value;
		}

		if ( fmtExpr != null )
		{
			mimeType = evaluateString( fmtExpr );
			if ( mimeType != null )
			{
				imgExt = FileUtil.getExtFromType( mimeType );
			}
		}

		if ( imgData != null )
		{
			imageContent.setData( imgData );
			imageContent.setExtension( imgExt );
			imageContent.setURI( null );
			imageContent.setMIMEType( mimeType );
		}
	}

	protected void handleFileExpressionImage( Expression fileExpr,
			IImageContent imageContent ) throws BirtException
	{
		String imageFile = evaluateString( fileExpr );
		if ( imageFile == null )
		{
			imageFile = fileExpr.getScriptText( );
		}
		// Here uses the absolute file name as the URI for the image
		// resource. The content of the file is loaded lazily to
		// improve the performance.
		imageContent.setURI( imageFile );
		imageContent.setImageSource( IImageContent.IMAGE_FILE );
		String imgExt = FileUtil.getExtFromFileName( imageFile );
		if ( null != imgExt )
		{
			imageContent.setExtension( imgExt );
			String mimeType = FileUtil.getTypeFromExt( imgExt );
			if ( null != mimeType )
			{
				imageContent.setMIMEType( mimeType );
			}
		}

		if ( imageFile == null )
		{
			getLogger( ).log( Level.SEVERE,
					"[ImageItemExecutor] Source image file is missing" ); //$NON-NLS-1$
			context.addException( design.getHandle( ), new EngineException(
					MessageConstants.MISSING_IMAGE_FILE_ERROR ) );
		}
	}
}