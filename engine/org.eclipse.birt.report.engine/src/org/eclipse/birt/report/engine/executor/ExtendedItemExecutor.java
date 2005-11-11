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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.data.dte.DteResultSet;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemGeneration;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.extension.internal.RowSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

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
			IReportItemVisitor visitor )
	{
		super( context, visitor );
	}

	/**
	 * <li> create an foreign content
	 * <li> push it to content
	 * <li> execute the query if any
	 * <li> intialize the content object
	 * <li> process the stylies, visiblity, bookmark and actions.
	 * <li> create the generator to process the object
	 * <li> call the onCreate if needed.
	 * <li> save the generate states into the foreign object
	 * <li>
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute()
	 */
	public void execute( ReportItemDesign aItem, IContentEmitter emitter )
	{
		assert aItem instanceof ExtendedItemDesign;
		ExtendedItemDesign item = (ExtendedItemDesign) aItem;

		IForeignContent content = report.createForeignContent( );

		IContent parent = context.getContent( );
		context.pushContent( content );

		initializeContent( parent, item, content );

		processAction( item, content );
		processBookmark( item, content );
		processStyle( item, content );
		processVisibility( item, content );

		generateContent( item, content );

		if ( context.isInFactory( ) )
		{
			context.execute( item.getOnCreate( ) );
		}

		if ( emitter != null )
		{
			emitter.startForeign( content );
		}

		context.popContent( );
	}

	protected void generateContent( ExtendedItemDesign item,
			IForeignContent content )
	{
		if ( context.isInFactory( ) )
		{
			generateInFactory( item, content );
		}
		else
		{
			loadFromArchive( item, content );
		}
	}

	protected void loadFromArchive( ExtendedItemDesign item,
			IForeignContent content )
	{
	}

	protected void generateInFactory( ExtendedItemDesign item,
			IForeignContent content )
	{
		// create user-defined generation-time helper object
		ExtendedItemHandle handle = (ExtendedItemHandle) item.getHandle( );
		String tagName = handle.getExtensionName( );
		String name = item.getName( );

		IReportItemGeneration itemGeneration = ExtensionManager.getInstance( )
				.createGenerationItem( tagName );
		byte[] generationStatus = null;
		if ( itemGeneration != null )
		{
			itemGeneration.setModelObject( handle );
			itemGeneration.setReportQueries( ( (ExtendedItemDesign) item )
					.getQueries( ) );
			IRowSet[] rowSets = executeQueries( item );
			try
			{
				if ( rowSets != null )
				{
					itemGeneration.onRowSets( rowSets );
				}
				if ( itemGeneration.needSerialization( ) )
				{
					ByteArrayOutputStream out = new ByteArrayOutputStream( );
					itemGeneration.serialize( out );
					generationStatus = out.toByteArray( );
				}
				itemGeneration.finish( );
			}
			catch ( BirtException ex )
			{
				logger.log( Level.SEVERE, ex.getMessage( ), ex );
				context.addException( new EngineException(
						MessageConstants.EXTENDED_ITEM_GENERATION_ERROR, handle
								.getExtensionName( )
								+ ( name != null ? " " + name : "" ), ex ) );//$NON-NLS-1$
			}
			closeQueries( rowSets );
		}
		content.setRawType( IForeignContent.EXTERNAL_TYPE );
		content.setRawValue( generationStatus );
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
				if ( rset != null )
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
		}
	}
}