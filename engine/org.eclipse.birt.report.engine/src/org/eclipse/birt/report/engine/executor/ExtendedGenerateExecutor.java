/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemGeneration;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.internal.RowSet;
import org.eclipse.birt.report.engine.extension.internal.SingleRowSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

public class ExtendedGenerateExecutor extends QueryItemExecutor
{

	IReportItemGeneration itemGeneration;
	IBaseResultSet[] rsets;

	public ExtendedGenerateExecutor( ExecutorManager manager,
			IReportItemGeneration itemGeneration )
	{
		super( manager, -1 );
		this.itemGeneration = itemGeneration;
	}
	
	public void close( )
	{
		closeQueries( );
		super.close( );
	}

	public IContent execute( )
	{
		ExtendedItemDesign extDesign = (ExtendedItemDesign) design;

		IForeignContent extContent = report.createForeignContent( );
		setContent( extContent );

		executeQueries( );
		
		initializeContent( extDesign, extContent );

		processAction( extDesign, extContent );
		processBookmark( extDesign, extContent );
		processStyle( extDesign, extContent );
		processVisibility( extDesign, extContent );

		generateContent( extDesign, extContent );

		return extContent;
	}

	protected void generateContent( ExtendedItemDesign item,
			IForeignContent content )
	{
		// create user-defined generation-time helper object
		ExtendedItemHandle handle = (ExtendedItemHandle) item.getHandle( );
		String name = item.getName( );

		byte[] generationStatus = null;
		if ( itemGeneration != null )
		{
			itemGeneration.setModelObject( handle );
			itemGeneration.setApplicationClassLoader( context
					.getApplicationClassLoader( ) );
			itemGeneration.setScriptContext( context.getReportContext( ) );
			IBaseQueryDefinition[] queries = (IBaseQueryDefinition[]) ( (ExtendedItemDesign) item )
					.getQueries( );
			itemGeneration.setReportQueries( queries );
			try
			{
				if ( rsets != null )
				{
					IRowSet[] rowSets = new IRowSet[rsets.length];
					for ( int i = 0; i < rsets.length; i++ )
					{
						IBaseResultSet rset = rsets[i];
						if ( rset != null
								&& rset.getType( ) == IBaseResultSet.QUERY_RESULTSET )
						{
							rowSets[i] = new RowSet( context,
									(IQueryResultSet) rset );
						}
						else
						{
							rowSets[i] = null;
						}
					}
					itemGeneration.onRowSets( rowSets );
				}
				else
				{
					IBaseResultSet prset = getParentResultSet( );
					if ( prset instanceof IQueryResultSet )
					{
						IRowSet[] rowSets = new IRowSet[1];
						rowSets[0] = new SingleRowSet( context,
								(IQueryResultSet) prset );
						itemGeneration.onRowSets( rowSets );
					}
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
		}
		else
		{
			// TODO: review. If itemGeneration is null. we should create a text
			// item for it. and set the alttext as its text.
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

	protected void executeQueries( )
	{
		ExtendedItemDesign extItem = (ExtendedItemDesign) design;
		IDataEngine dataEngine = context.getDataEngine( );
		IBaseResultSet parent = getParentResultSet( );

		boolean useCache = extItem.useCachedResult( );
		
		IDataQueryDefinition[] queries = extItem.getQueries( );
		if ( queries != null )
		{
			rsets = new IBaseResultSet[queries.length];
			for ( int i = 0; i < rsets.length; i++ )
			{
				rsets[i] = dataEngine.execute( parent, queries[i], useCache );
			}
			context.setResultSets( rsets );
		}
	}

	protected void closeQueries( )
	{
		if ( rsets != null )
		{
			for ( int i = 0; i < rsets.length; i++ )
			{
				if ( rsets[i] != null )
				{
					rsets[i].close( );
				}
			}
		}
		rsets = null;
	}
}
