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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IExtendedItemContent;
import org.eclipse.birt.report.engine.content.impl.ExtendedItemContent;
import org.eclipse.birt.report.engine.content.impl.ImageItemContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.data.dte.DteResultSet;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.IReportItemEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemGeneration;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.extension.internal.RowSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.elements.Style;

/**
 * Processes an extended item.
 */
public class ExtendedItemExecutor extends StyledItemExecutor
{

	protected static Logger logger = Logger
			.getLogger( ExtendedItemExecutor.class.getName( ) );

	/**
	 * @param context
	 *            the engine execution context
	 * @param visitor
	 *            visitor class used to visit the extended item
	 */
	public ExtendedItemExecutor( ExecutionContext context,
			ReportExecutorVisitor visitor )
	{
		super( context, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute()
	 */
	public void execute( ReportItemDesign item1, IReportEmitter emitter )
	{
		assert item1 instanceof ExtendedItemDesign;
		ExtendedItemDesign item = (ExtendedItemDesign) item1;
		String name = item.getName();
		IExtendedItemContent content = ContentFactory
				.createExtendedItemContent( (ExtendedItemDesign) item , context.getContentObject( ));
		
		// handle common properties, such as
		//
		//1) style (Not supported now as we only support image extension)
		//2) highlight (Not supported now as we only support image extension)
		//3) x,y
		//4) actions
		//
		// other report item-supported features.

		//create user-defined generation-time helper object
		ExtendedItemHandle handle = (ExtendedItemHandle) item.getHandle( );
		String tagName = handle.getExtensionName( );

		IReportItemGeneration itemGeneration = ExtensionManager.getInstance( )
				.createGenerationItem( tagName );
		byte[] generationStatus = null;
		if ( itemGeneration != null )
		{
			itemGeneration.setModelObject( handle );
			itemGeneration.setReportQueries(((ExtendedItemDesign)item).getQueries());
			IRowSet[] rowSets = null;
			try
			{
				rowSets = executeQueries( item );
				if ( rowSets != null )
				{
					try
					{
						itemGeneration.onRowSets( rowSets );
					}
					catch ( BirtException ex )
					{
						logger.log( Level.SEVERE, ex.getMessage( ), ex );
						context.addException( new EngineException(
								MessageConstants.EXTENDED_ITEM_GENERATION_ERROR,
										handle.getExtensionName( )
										+ ( name != null ? " "
												+ name : "" ), ex ) );//$NON-NLS-1$

					}
				}
				if ( itemGeneration.needSerialization( ) )
				{
					try
					{
						ByteArrayOutputStream out = new ByteArrayOutputStream( );
						itemGeneration.serialize( out );
						generationStatus = out.toByteArray( );
					}
					catch ( BirtException ex )
					{
						logger.log( Level.SEVERE, ex.getMessage( ), ex );
						context.addException( new EngineException(
								MessageConstants.EXTENDED_ITEM_GENERATION_ERROR,
										handle.getExtensionName( )
										+ ( name != null ? " "
												+ name : "" ), ex ) );//$NON-NLS-1$
					}
					itemGeneration.finish( );
				}
			}
			catch ( Throwable t )
			{
				logger.log( Level.SEVERE, "Error:", t );//$NON-NLS-1$
				context.addException( new EngineException(
						MessageConstants.EXTENDED_ITEM_GENERATION_ERROR,
								handle.getExtensionName( )
								+ ( name != null ? " "
										+ name : "" ), t ) );//$NON-NLS-1$
			}
			finally
			{
				closeQueries( rowSets );
			}			
		}

		//call the presentation peer to create the content object
		IReportItemPresentation itemPresentation = ExtensionManager
				.getInstance( ).createPresentationItem( tagName );
		if ( itemPresentation != null )
		{
			itemPresentation.setModelObject( handle );
			itemPresentation.setReportQueries(((ExtendedItemDesign)item).getQueries());
			//itemPresentation.setResolution();
			itemPresentation.setLocale(context.getLocale());

			Object renderContext = null;
			if ( context.getAppContext() instanceof Map )
				renderContext = ((Map)context.getAppContext()).get( HTMLRenderContext.CONTEXT_NAME );
			else
				renderContext = context.getAppContext(); // Handle the old-style render context, follow the same code path as before.
				
			if ( (renderContext instanceof HTMLRenderContext) &&
				 ((HTMLRenderContext)renderContext).getSupportedImageFormats() != null )				
				itemPresentation.setSupportedImageFormats( ((HTMLRenderContext)renderContext).getSupportedImageFormats() );
			else
				itemPresentation.setSupportedImageFormats( "PNG;GIF;JPG;BMP" ); // Default value
			
			itemPresentation.setOutputFormat( emitter.getOutputFormat( ) );
			if ( generationStatus != null )
			{
				itemPresentation.deserialize( new ByteArrayInputStream(
						generationStatus ) );
			}

			Object output = null;
			IRowSet[] rowSets = null;
			try
			{
				rowSets = executeQueries( item );
				try
				{
					output = itemPresentation.onRowSets( rowSets );
				}
				catch ( BirtException ex )
				{
					logger.log( Level.SEVERE, ex.getMessage( ), ex );
					
					context.addException( new EngineException(
							MessageConstants.EXTENDED_ITEM_RENDERING_ERROR,
							new String[]{handle.getExtensionName( ), ( name != null ? " " + name : "" )}, ex ) );//$NON-NLS-1$ //$NON-NLS-2$
					

				}

				if ( output != null )
				{
					int type = itemPresentation.getOutputType( );
					String imageMIMEType = itemPresentation.getImageMIMEType();
					handleItemContent( item, emitter, content, type, imageMIMEType, output );
				}
				itemPresentation.finish( );
			}
			catch ( Throwable t )
			{
				logger.log( Level.SEVERE, "Error:", t );//$NON-NLS-1$
				context.addException(new EngineException(
						MessageConstants.EXTENDED_ITEM_RENDERING_ERROR,
						new String[]{handle.getExtensionName( ), ( name != null ? " " + name : "" )}, t ) );//$NON-NLS-1$ //$NON-NLS-2$
				
			}
			finally
			{
				closeQueries( rowSets );
			}			
		}
	}

	/**
	 * handle the content created by the IPresentation
	 * 
	 * @param item
	 *            extended item design
	 * @param emitter
	 *            emitter used to output the contnet
	 * @param content
	 *            ext content
	 * @param type
	 *            output type
	 * @param output
	 *            output
	 */
	protected void handleItemContent( ExtendedItemDesign item,
			IReportEmitter emitter, IExtendedItemContent content, int type, String imageMIMEType,
			Object outputObject )
	{
		switch ( type )
		{
			case IReportItemPresentation.OUTPUT_NONE :
				break;
			case IReportItemPresentation.OUTPUT_AS_IMAGE :
			case IReportItemPresentation.OUTPUT_AS_IMAGE_WITH_MAP :
				// the output object is a image, so create a image content
				// object
				ImageItemContent image = (ImageItemContent) ContentFactory
						.createImageContent( item , context.getContentObject() );
				
				Object output = null;
				Object imageMap = null;
				if ( type == IReportItemPresentation.OUTPUT_AS_IMAGE )
				{
					output = outputObject;
				}
				else
				{	// OUTPUT_AS_IMAGE_WITH_MAP
					output = ((Object[])outputObject)[0];
					imageMap = ((Object[])outputObject)[1];
				}
				
				if ( output instanceof InputStream )
				{
					image.setData( readContent( (InputStream) output ) );
				}
				else if ( output instanceof byte[] )
				{
					image.setData( (byte[]) output );
				}
				else
				{
					assert false;
					logger.log( Level.SEVERE,
							"unsupported image type:{0}", output ); //$NON-NLS-1$

				}
				
				// Set image map
				image.setImageMap( imageMap );
				image.setMIMEType( imageMIMEType );
				
				setStyles( image, item );
				setVisibility( item, image );				
				String bookmarkStr = evalBookmark( item );
				if ( bookmarkStr != null )
					image.setBookmarkValue( bookmarkStr );
				
				image.setImageSource( ImageItemDesign.IMAGE_EXPRESSION );
				IReportItemEmitter imageEmitter = emitter.getEmitter( "image" ); //$NON-NLS-1$
				if ( imageEmitter != null )
				{
					imageEmitter.start( image );
					imageEmitter.end( );
				}
				break;
			case IReportItemPresentation.OUTPUT_AS_CUSTOM :
				ExtendedItemContent customContent= (ExtendedItemContent) content ;
				
				customContent.setContent( outputObject );
				Object value = getMapVal( customContent.getContent(), item );
				StringBuffer formattedString = new StringBuffer( );
				formatValue( value, null, item.getStyle( ), formattedString ,customContent);
				customContent.setContent( formattedString.toString( ) );
				setStyles( customContent, item );
				if ( value != null )
				{
					if ( value instanceof Number )
					{
						String numberAlign = customContent
								.getStyle( ).getNumberAlign( );
						if ( numberAlign != null )
						{
							// set number alignment
							customContent.setStyleProperty( Style.TEXT_ALIGN_PROP,
									numberAlign );
						}
					}
				}				
				setVisibility( item, customContent );
				bookmarkStr = evalBookmark( item );
				if ( bookmarkStr != null )
					customContent.setBookmarkValue( bookmarkStr );
				
				//get the emmiter type, and give it to others type
				IReportItemEmitter itemEmitter = emitter
						.getEmitter( "extendedItem" ); //$NON-NLS-1$
				if ( itemEmitter != null )
				{
					itemEmitter.start( content );
					itemEmitter.end( );
				}
				break;
			case IReportItemPresentation.OUTPUT_AS_DRAWING :
			case IReportItemPresentation.OUTPUT_AS_HTML_TEXT :
			case IReportItemPresentation.OUTPUT_AS_TEXT :
				assert false;
				break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		// TODO Auto-generated method stub

	}

	/**
	 * read the content of input stream.
	 * 
	 * @param in
	 *            input content
	 * @return content in the stream.
	 */
	static protected byte[] readContent( InputStream in )
	{
		BufferedInputStream bin = in instanceof BufferedInputStream
				? (BufferedInputStream) in
				: new BufferedInputStream( in );
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		byte[] buffer = new byte[1024];
		int readSize = 0;
		try
		{
			readSize = bin.read( buffer );
			while ( readSize != -1 )
			{
				out.write( buffer, 0, readSize );
				readSize = bin.read( buffer );
			}
		}
		catch ( IOException ex )
		{
			logger.log( Level.SEVERE, ex.getMessage( ), ex );
		}
		return out.toByteArray( );
	}

	protected IRowSet[] executeQueries( ReportItemDesign item )
	{
		assert item instanceof ExtendedItemDesign;
		ExtendedItemDesign extItem = (ExtendedItemDesign) item;
		IRowSet[] rowSets = null;
		IBaseQueryDefinition[] queries = extItem.getQueries( );
		if ( queries != null )
		{
			//context.newScope( );
			rowSets = new IRowSet[queries.length];
			for ( int i = 0; i < rowSets.length; i++ )
			{
				IResultSet rset = context.dataEngine.execute( queries[i] );
				if(rset!=null)
				{
					rowSets[i] = new RowSet( (DteResultSet) rset );
				}
				else
				{
					rowSets[i] = null;
				}
			}
		}
		return rowSets;
	}

	protected void closeQueries( IRowSet[] rowSets )
	{
		if ( rowSets != null )
		{
			for ( int i = 0; i < rowSets.length; i++ )
			{
				if ( rowSets[i] != null )
				{
					rowSets[i].close( );
				}
			}
			//context.exitScope( );
		}
	}
}