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

package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.v3.CachedReportContentReaderV3;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

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
	 * the executed report design
	 */
	protected ReportItemDesign design;

	/**
	 * the instance id of the content
	 */
	private InstanceID instanceId;

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
	protected IResultSet rset;

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
		this.rset = null;
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

	void setParent( ReportItemExecutor parent )
	{
		this.parent = parent;
		if ( parent != null )
		{
			this.reader = parent.reader;
		}
	}

	ReportItemExecutor getParent( )
	{
		return parent;
	}

	void setDesign( ReportItemDesign design )
	{
		this.design = design;
		context.setItemDesign( design );
	}

	ReportItemDesign getDesign( )
	{
		return design;
	}

	IContent getContent( )
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

	private IContent getParentContent( )
	{
		while ( parent != null )
		{
			IContent content = parent.getContent( );
			if ( content != null )
			{
				return content;
			}
			parent = parent.getParent( );
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
				ex.printStackTrace( );
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

	IResultSet getResultSet( )
	{
		return rset;
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
		this.rset = null;
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
		if ( rset != null )
		{
			rset.close( );
			rset = null;
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
		IBaseQueryDefinition query = design.getQuery( );
		if ( query != null )
		{
			try
			{
				IResultSet pRset = getParentResultSet( );
				rset = context.executeQuery( pRset, query );
				rsetEmpty = true;
				if ( rset != null )
				{
					rsetEmpty = !rset.next( );
					return;
				}
			}
			catch ( BirtException ex )
			{
				context.addException( ex );
			}
		}
	}

	IResultSet getParentResultSet( )
	{
		ReportItemExecutor pExecutor = getParent( );
		while ( pExecutor != null )
		{
			IResultSet rset = pExecutor.getResultSet( );
			if ( rset != null )
			{
				return rset;
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

	protected InstanceID getInstanceID( )
	{
		if ( instanceId == null )
		{
			long uid = getUniqueID( );
			InstanceID pid = parent == null ? null : parent.getInstanceID( );
			long id = design == null ? -1 : design.getID( );
			DataID dataId = null;
			if ( parent != null )
			{
				IResultSet rset = parent.getResultSet( );
				if ( rset != null )
				{
					DataSetID dataSetID = rset.getID( );
					long position = rset.getCurrentPosition( );
					dataId = new DataID( dataSetID, position );
				}
			}
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
