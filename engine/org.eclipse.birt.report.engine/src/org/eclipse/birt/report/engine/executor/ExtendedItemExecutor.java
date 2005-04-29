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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
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
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Processes an extented item.
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
	public void execute( ReportItemDesign item, IReportEmitter emitter )
	{
		assert item instanceof ExtendedItemDesign;

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
					}
					itemGeneration.finish( );
				}
			}
			catch ( Throwable t )
			{
				logger.log( Level.SEVERE, "Error:", t );//$NON-NLS-1$
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
			itemPresentation.setSupportedImageFormats( "GIF;PNG;JPG;BMP" );
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
				if ( rowSets != null )
				{
					try
					{
						output = itemPresentation.onRowSets( rowSets );
					}
					catch ( BirtException ex )
					{
						logger.log( Level.SEVERE, ex.getMessage( ), ex );
					}
				}
				if ( output != null )
				{
					int type = itemPresentation.getOutputType( );
					handleItemContent( item, emitter, content, type, output );
				}
				itemPresentation.finish( );
			}
			catch ( Throwable t )
			{
				logger.log( Level.SEVERE, "Error:", t );//$NON-NLS-1$
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
	protected void handleItemContent( ReportItemDesign item,
			IReportEmitter emitter, IExtendedItemContent content, int type,
			Object output )
	{
		switch ( type )
		{
			case IReportItemPresentation.OUTPUT_NONE :
				break;
			case IReportItemPresentation.OUTPUT_AS_IMAGE :
				// the output object is a image, so create a image content
				// object
				ImageItemContent image = (ImageItemContent) ContentFactory
						.createImageContent( item , context.getContentObject() );
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
							"unsupport image type:{0}", output ); //$NON-NLS-1$

				}
				image.setImageSource( ImageItemDesign.IMAGE_EXPRESSION );
				IReportItemEmitter imageEmitter = emitter.getEmitter( "image" ); //$NON-NLS-1$
				if ( imageEmitter != null )
				{
					imageEmitter.start( image );
					imageEmitter.end( );
				}
				break;
			case IReportItemPresentation.OUTPUT_AS_CUSTOM :
				( (ExtendedItemContent) content ).setContent( output );
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
			rowSets = new IRowSet[queries.length];
			for ( int i = 0; i < rowSets.length; i++ )
			{
				IResultSet rset = context.dataEngine.execute( queries[i] );
				rowSets[i] = new RowSet( (DteResultSet) rset );
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
		}
	}
}