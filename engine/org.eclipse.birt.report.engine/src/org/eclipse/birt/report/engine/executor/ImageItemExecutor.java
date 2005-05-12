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
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.impl.ImageItemContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.IReportItemEmitter;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
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
 * @version $Revision: 1.16 $ $Date: 2005/05/12 06:32:12 $
 */
public class ImageItemExecutor extends StyledItemExecutor
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
			ReportExecutorVisitor visitor )
	{
		super( context, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#excute(org.eclipse.birt.report.engine.ir.ReportItemDesign,
	 *      org.eclipse.birt.report.engine.emitter.IReportEmitter)
	 */
	public void execute( ReportItemDesign item, IReportEmitter emitter )
	{
		assert item instanceof ImageItemDesign;
		ImageItemDesign imageItem = (ImageItemDesign) item;
		String itemName = item.getName( );

		IReportItemEmitter imageEmitter = emitter.getEmitter( "image" ); //$NON-NLS-1$
		if ( imageEmitter == null )
		{
			return;
		}

		// Initializes
		ImageItemContent imageContent = (ImageItemContent)ContentFactory
				.createImageContent( imageItem, context.getContentObject( ) );
		imageContent.setHelpText( getLocalizedString( imageItem
				.getHelpTextKey( ), imageItem.getHelpText( ) ) );
		imageContent.setAltText( getLocalizedString(
				imageItem.getAltTextKey( ), imageItem.getAltText( ) ) );		
		String fileExt = null;

		// Handles the image according to its type
		switch ( imageItem.getImageSource( ) )
		{
			case ImageItemDesign.IMAGE_URI : // URI
				assert imageItem.getImageUri( ) != null;
				imageContent.setUri( imageItem.getImageUri( ) );
				break;

			case ImageItemDesign.IMAGE_FILE : // File
				String imageFile = imageItem.getImageFile( );
				assert imageFile != null;
				//image file may be file: or a file path.
				try
				{
					URL url = new URL( imageFile );
					if ( url.getProtocol( ).equals( "file" ) ) //$NON-NLS-1$
					{
						imageFile = URLDecoder.decode( url.getFile( ) );
					}
				}
				catch ( MalformedURLException ex )
				{
					//imageFile is a file name
				}

				imageFile = FileUtil.getAbsolutePath( context.getReport( )
						.getBasePath( ), imageFile );
				imageContent.setExtension( FileUtil.getExtFromFileName(
						imageFile, FileUtil.SEPARATOR_PATH ) );
				//Here uses the absolute file name as the URI for the image
				// resource. The content of the file is loaded lazily to improve
				// the performance.
				imageContent.setUri( imageFile );

				if ( imageFile == null )
				{
					logger.log( Level.SEVERE,
							"[ImageItemExecutor] Source image file is missing" ); //$NON-NLS-1$
					context.addException( new EngineException(
							"Failed to render Image "
									+ ( itemName != null ? itemName : "" )
									+ ":Cannot find the image file" ) );//$NON-NLS-1$

				}
				break;

			case ImageItemDesign.IMAGE_NAME : // embedded image
				String imageName;

				imageName = imageItem.getImageName( );
				assert imageName != null;
				imageContent.setUri( null );

				try
				{
					EmbeddedImage embeddedImage = context.getReport( )
							.getReportDesign( ).findImage( imageName );
					if ( embeddedImage != null )
					{
						imageContent.setData( embeddedImage.getData( ) );
						String extension = FileUtil
								.getExtFromType( embeddedImage.getType( ) );
						if ( extension != null )
						{
							imageContent.setExtension( extension );
						}
						
						imageContent.setUri( imageName );
					}
				}
				catch ( Exception e )
				{
				    logger.log( Level.SEVERE, "[ImageItemExecutor] Fail to handle embedded image with an exception below:", e ); //$NON-NLS-1$
				    context
							.addException( new EngineException(
									"Failed to render Image "
											+ ( itemName != null
													? itemName
													: "" ), e ) );//$NON-NLS-1$
				}

				break;

			case ImageItemDesign.IMAGE_EXPRESSION : // get image from database
				assert imageItem.getImageExpression( ) != null;

				IResultSet rs = null;
				try
				{
					rs = openResultSet( item );
					if ( rs != null )
					{
						rs.next( );
					}
					Expression imgExpr = imageItem.getImageExpression( );
					Object value = context.evaluate( imgExpr );
					byte[] blob = null;
					if ( value != null
							&& value.getClass( ).isArray( )
							&& value.getClass( ).getComponentType( ) == byte.class )
					{
						blob = (byte[]) value;
					}
					Expression fmtExpr = imageItem.getImageFormat( );
					if ( fmtExpr != null )
					{
						Object strValue = context.evaluate( fmtExpr );
						if ( strValue != null )
						{
							fileExt = strValue.toString( );
							fileExt = FileUtil.getExtFromType( fileExt );
						}
					}

					if ( blob != null )
					{
						imageContent.setData( blob );
						imageContent.setExtension( fileExt );
						imageContent.setUri( null );
					}
					else
					{
					    logger.log( Level.SEVERE, "[ImageItemExecutor] cannot query image data from database"); //$NON-NLS-1$
					    
					    context
								.addException( new EngineException(
										"Failed to render Image " + itemName != null
												? itemName
												: ""
														+ ": Cannot query data from database." ) );//$NON-NLS-1$

					}					
				}
				catch ( Exception e )
				{
				    logger.log( Level.SEVERE,"[ImageItemExecutor] fail to handle database image with an exception below:", e ); //$NON-NLS-1$
				    context
							.addException( new EngineException(
									"Failed to render Image "
											+ ( itemName != null
													? itemName
													: "" ), e ) );//$NON-NLS-1$

				}
				finally
				{
					closeResultSet( rs );
				}

				break;

			default :
			    logger.log( Level.SEVERE, "[ImageItemExecutor] invalid image source" ); //$NON-NLS-1$
				context.addException( new EngineException(
						"Failed to render Image "
								+ ( itemName != null ? itemName : "" )
								+ ": Invalid image source type." ) );//$NON-NLS-1$

				assert false;
		}

		// handle the Action and Bookmark values
		processAction( imageItem.getAction( ), imageContent );
		String bookmarkStr = evalBookmark( item );
		if ( bookmarkStr != null )
			imageContent.setBookmarkValue( bookmarkStr );

		setStyles( imageContent, item );
		setVisibility( item, imageContent );
		// forward to emitter for further processing

		imageEmitter.start( imageContent );
		imageEmitter.end( );
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{

	}
}