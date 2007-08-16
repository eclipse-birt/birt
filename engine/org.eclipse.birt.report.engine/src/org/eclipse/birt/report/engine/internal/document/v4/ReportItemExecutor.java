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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.v3.CachedReportContentReaderV3;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;

/**
 * Abstract class, Represents a report item executor. Report item executor
 * execute a report item design, generate a report item instance, and pass the
 * instance to <code>emitter</code>.
 * <p>
 * According to the report item design and current context information, executor
 * calculate expression in report item design, get data instance from data
 * source, and fill it into the report item instance, and set property for the
 * report item instance.
 * <p>
 * Reset the state of report item executor by calling <code>reset()</code>
 * 
 */
public abstract class ReportItemExecutor implements IReportItemExecutor
{
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger( ReportItemExecutor.class.getName( ) );
	
	/**
	 * executor manager used to create this executor.
	 */
	protected ExecutorManager manager;

	/**
	 * the type of the executor, used to reuse it in the manager.
	 */
	protected int executorType;

	/**
	 * the reader used to read the contents
	 */
	protected CachedReportContentReaderV3 reader;
	/**
	 * the report content
	 */
	protected IReportContent report;

	/**
	 * the executor context
	 */
	protected ExecutionContext context;

	/**
	 * parent executor
	 */
	protected ReportItemExecutor parent;

	/**
	 * the design object used to create the report item.
	 */
	protected Object designHandle;

	/**
	 * the executed report design
	 */
	protected ReportItemDesign design;

	/**
	 * the instance id of the content
	 */
	protected InstanceID instanceId;

	/**
	 * the created report content
	 */
	protected IContent content;

	/**
	 * the offset of the content if exits.
	 */
	protected long offset;
	/**
	 * the fragment used to limit the executor
	 */
	protected Fragment fragment;

	/**
	 * rset created by this executor.
	 */
	protected IBaseResultSet[] rsets;

	protected boolean rsetEmpty;
	/**
	 * does the executor has been executed.
	 */
	protected boolean executed;

	protected long uniqueId;

	/**
	 * construct a report item executor by giving execution context and report
	 * executor visitor
	 * 
	 * @param context
	 *            the executor context
	 * @param visitor
	 *            the report executor visitor
	 */
	protected ReportItemExecutor( ExecutorManager manager, int type )
	{
		this.manager = manager;
		this.executorType = type;
		this.context = manager.getExecutionContext( );
		this.reader = manager.getReportReader( );
		this.report = context.getReportContent( );

		this.parent = null;
		this.design = null;
		this.offset = -1;
		this.fragment = null;
		this.rsets = null;
		this.rsetEmpty = true;
		this.executed = false;
		this.content = null;
		this.instanceId = null;
		this.uniqueId = 0;
	}

	int getExecutorType( )
	{
		return this.executorType;
	}

	public void setParent( IReportItemExecutor parent )
	{
		this.parent = (ReportItemExecutor) parent;
		if ( parent != null )
		{
			this.reader = this.parent.reader;
		}
	}

	public IReportItemExecutor getParent( )
	{
		return parent;
	}

	public IExecutorContext getContext( )
	{
		throw new UnsupportedOperationException( );
	}

	public Object getModelObject( )
	{
		return design;
	}

	public void setContext( IExecutorContext context )
	{
		throw new IllegalStateException(
				"should never call setContext for system executor" );
	}

	public void setModelObject( Object handle )
	{
		this.designHandle = handle;
		if ( handle instanceof ReportElementHandle )
		{
			design = report.getDesign( ).findDesign(
					(ReportElementHandle) handle );
			context.setItemDesign( design );
		}
	}

	void setDesign( ReportItemDesign design )
	{
		this.design = design;
	}

	ReportItemDesign getDesign( )
	{
		return design;
	}

	public IContent getContent( )
	{
		return content;
	}

	void setOffset( long offset )
	{
		this.offset = offset;
	}

	void setFragment( Fragment fragment )
	{
		this.fragment = fragment;
	}

	Fragment getFragment( )
	{
		return fragment;
	}

	protected IContent doCreateContent( )
	{
		long id = design == null ? -1 : design.getID( );
		throw new IllegalStateException(
				"can't re-generate content for design " + id );
	}

	protected void doExecute( ) throws Exception
	{
	}

	protected IContent getParentContent( )
	{
		while ( parent != null )
		{
			IContent content = parent.getContent( );
			if ( content != null )
			{
				return content;
			}
			parent = parent.parent;
		}
		return null;
	}

	public IContent execute( )
	{

		if ( !executed )
		{
			executed = true;
			try
			{
				InstanceID instanceId = getInstanceID( );
				if ( offset != -1 )
				{
					content = reader.loadContent( offset );
					InstanceID id = content.getInstanceID( );
					if ( !isSameInstance( instanceId, id ) )
					{
						content = doCreateContent( );
						content.setInstanceID( instanceId );
					}
				}
				else
				{
					content = doCreateContent( );
				}
				content.setGenerateBy( design );
				content.setInstanceID( instanceId );
				IContent pContent = getParentContent( );
				if ( pContent != null )
				{
					content.setParent( pContent );
				}
				doExecute( );
			}
			catch ( Exception ex )
			{
				logger.log( Level.WARNING, ex.getMessage( ), ex );
				context.addException( new EngineException( ex
						.getLocalizedMessage( ) ) );
			}
		}
		return content;
	}

	/**
	 * does the executor has child executor
	 * 
	 * @return
	 */
	public boolean hasNextChild( )
	{
		return false;
	}

	public IReportItemExecutor getNextChild( )
	{
		return null;
	}

	public IBaseResultSet[] getQueryResults( )
	{
		return rsets;
	}


	public void close( )
	{
		if ( offset != -1 )
		{
			reader.unloadContent( offset );
			offset = -1;
		}
		this.parent = null;
		this.reader = null;
		this.design = null;
		this.fragment = null;
		this.rsets = null;
		this.rsetEmpty = true;
		this.executed = false;
		this.content = null;
		this.instanceId = null;
		this.uniqueId = 0;
		if ( executorType != -1 )
		{
			manager.releaseExecutor( this );
		}
	}

	/**
	 * close dataset if the dataset is not null:
	 * <p>
	 * <ul>
	 * <li>close the dataset.
	 * <li>exit current script scope.
	 * </ul>
	 * 
	 * @param ds
	 *            the dataset object, null is valid
	 */
	protected void closeQuery( )
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
			rsets = null;
		}
	}

	/**
	 * register dataset of this item.
	 * <p>
	 * if dataset design of this item is not null, create a new
	 * <code>DataSet</code> object by the dataset design. open the dataset,
	 * move cursor to the first record , register the first row to script
	 * context, and return this <code>DataSet</code> object if dataset design
	 * is null, or open error, or empty resultset, return null.
	 * 
	 * @param item
	 *            the report item design
	 * @return the DataSet object if not null, else return null
	 */
	protected void executeQuery( )
	{
		if ( design != null )
		{
			IDataQueryDefinition[] queries = design.getQueries( );			
			
			boolean useCache = false;
			if ( queries == null )
			{
				DesignElementHandle elementHandle = design.getHandle( );
				if ( elementHandle instanceof ReportElementHandle )
				{
					queries = report.getDesign( ).getQueryByReportHandle(
							(ReportElementHandle) elementHandle );
					if ( queries != null && queries.length > 0 )
					{
						useCache = true;
					}
				}
			}
			
			if ( queries != null )
			{
				rsets = new IBaseResultSet[queries.length];
				try
				{
					IBaseResultSet prset = restoreParentResultSet( );
					for ( int i = 0; i < queries.length; i++ )
					{
						rsets[i] = context.executeQuery( prset, queries[i],
								useCache );
					}
					context.setResultSets( rsets );
					rsetEmpty = true;
					if ( rsets[0] != null
							&& rsets[0] instanceof IQueryResultSet )
					{
						rsetEmpty = !( (IQueryResultSet) rsets[0] ).next( );
					}
				}
				catch ( BirtException ex )
				{
					context.addException( ex );
				}
			}
		}
	}
	
	IBaseResultSet getResultSet( )
	{
		if ( rsets != null && rsets.length != 0 )
		{
			return rsets[0];
		}
		return null;
	}

	IBaseResultSet restoreParentResultSet( )
	{
		ReportItemExecutor pExecutor = parent;
		DataID dataId = getContent( ).getInstanceID( ).getDataID( );
		while ( pExecutor != null )
		{
			if ( dataId == null )
			{
				IContent pContent = pExecutor.getContent( );
				if ( pContent != null )
				{
					InstanceID pIID = pContent.getInstanceID( );
					if ( pIID != null )
					{
						dataId = pIID.getDataID( );
					}
				}
			}
			IBaseResultSet[] rsets = pExecutor.getQueryResults( );
			if ( rsets != null )
			{
				if ( rsets.length > 0 )
				{
					IBaseResultSet rset = rsets[0];
					if ( dataId != null )
					{
						if ( rset instanceof ICubeResultSet )
						{
							ICubeResultSet cset = (ICubeResultSet) rset;
							String cellId = dataId.getCellID( );
							if ( cellId != null )
							{
								cset.skipTo( dataId.getCellID( ) );
							}
						}
						// we need handle the IQueryResultSet in future if we
						// support horz-page-break.
					}
					return rset;
				}
				return null;
			}
			pExecutor = pExecutor.parent;
		}
		return null;
	}

	long getUniqueID( )
	{
		if ( parent != null )
		{
			return parent.uniqueId++;
		}
		return manager.generateUniqueID( );
	}

	protected DataID getDataID( )
	{
		if ( parent != null )
		{
			IBaseResultSet[] rsets = parent.getQueryResults( );
			if ( ( rsets != null ) && ( rsets.length > 0 )
					&& ( rsets[0] != null ) )
			{
				if ( rsets[0] instanceof IQueryResultSet )
				{
					IQueryResultSet rset = (IQueryResultSet) rsets[0];
					DataSetID dataSetID = rset.getID( );
					long position = rset.getRowIndex( );
					return new DataID( dataSetID, position );
				}
				if ( rsets[0] instanceof ICubeResultSet )
				{
					ICubeResultSet rset = (ICubeResultSet) rsets[0];
					DataSetID dataSetID = rset.getID( );
					String cellId = rset.getCellIndex( );
					return new DataID( dataSetID, cellId );
				}
			}
		}
		return null;
	}

	protected long generateUniqueID( )
	{
		if ( parent != null )
		{
			return parent.uniqueId++;
		}
		return manager.generateUniqueID( );
	}

	protected long getElementId( )
	{
		if ( design != null )
		{
			return design.getID( );
		}
		return -1;
	}

	protected InstanceID getInstanceID( )
	{
		if ( instanceId == null )
		{
			InstanceID pid = parent == null ? null : parent.getInstanceID( );
			long uid = generateUniqueID( );
			long id = getElementId( );
			DataID dataId = getDataID( );
			instanceId = new InstanceID( pid, uid, id, dataId );
		}
		return instanceId;
	}	

	protected boolean isSameInstance( InstanceID a, InstanceID b )
	{
		if ( a == b )
		{
			return true;
		}
		if ( a != null && b != null )
		{
			if ( a.getUniqueID( ) == b.getUniqueID( ) )
			{
				return true;
			}
		}
		return false;
	}

}
