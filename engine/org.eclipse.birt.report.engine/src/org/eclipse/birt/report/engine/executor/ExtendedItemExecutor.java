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

import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Processes an extended item.
 */
public class ExtendedItemExecutor extends ReportItemExecutor
{

	protected static Logger logger = Logger
			.getLogger( ExtendedItemExecutor.class.getName( ) );

	protected IReportItemExecutor executor;

	/**
	 * @param context
	 *            the engine execution context
	 * @param visitor
	 *            visitor class used to visit the extended item
	 */
	public ExtendedItemExecutor( ExecutorManager manager )
	{
		super( manager );
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
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute(IContentEmitter)
	 */

	public IContent execute( )
	{
		// create user-defined generation-time helper object

		if ( executor == null )
		{
			ExtendedItemHandle handle = (ExtendedItemHandle) design.getHandle( );
			String tagName = handle.getExtensionName( );

			executor = ExtensionManager.getInstance( )
					.createReportItemExecutor( tagName );
			executor.setContext( executorContext );
			executor.setModelObject( handle );
			if ( parent instanceof ExtendedItemExecutor )
			{
				executor.setParent( ( (ExtendedItemExecutor) parent ).executor );
			}
			else
			{
				executor.setParent( parent );
			}
			// user implement the IReportItemExecutor.
			if ( executor instanceof ExtendedGenerateExecutor )
			{
				ExtendedGenerateExecutor gExecutor = (ExtendedGenerateExecutor) executor;
				gExecutor.context = context;
				gExecutor.report = report;
				gExecutor.design = design;
			}
		}

		if ( executor == null )
		{
			return null;
		}

		DataID dataId = createDataID( );
		InstanceID pid = null;
		IReportItemExecutor parent = getParent( );
		if ( parent != null )
		{
			IContent pContent = parent.getContent( );
			if ( pContent != null )
			{
				pid = pContent.getInstanceID( );
			}
		}

		content = executor.execute( );

		if ( content != null )
		{

			Object modelObject = executor.getModelObject( );

			long id = -1;
			if ( modelObject instanceof DesignElementHandle )
			{
				DesignElementHandle designHandle = (DesignElementHandle) modelObject;
				id = designHandle.getID( );
			}

			if ( content.getInstanceID( ) == null )
			{
				InstanceID iid = new InstanceID( pid, id, dataId );
				content.setInstanceID( iid );
			}

			if ( context.isInFactory( ) )
			{
				// context.execute( design.getOnCreate( ) );
				handleOnCreate( content );
			}

			startTOCEntry( content );

			if ( emitter != null )
			{
				ContentEmitterUtil.startContent( content, emitter );
			}
		}
		return content;
	}

	public boolean hasNextChild( )
	{
		if ( executor != null )
		{
			return executor.hasNextChild( );
		}
		return false;
	}

	public IReportItemExecutor getNextChild( )
	{
		if ( executor != null )
		{
			IReportItemExecutor child = executor.getNextChild( );
			if ( child != null )
			{
				if ( child instanceof ReportItemExecutor )
				{
					return child;
				}
				return manager.createExecutor( this, child );
			}
		}
		return null;
	}

	public void close( )
	{
		if ( executor != null )
		{
			executor.close( );
			finishTOCEntry( );
			if ( emitter != null )
			{
				ContentEmitterUtil.endContent( content, emitter );
			}
		}
		executor = null;
		manager.releaseExecutor( ExecutorManager.EXTENDEDITEM, this );
	}

	public IBaseResultSet[] getResultSets( )
	{
		if ( executor != null )
		{
			return executor.getQueryResults( );
		}
		return null;
	}

	protected DataID createDataID( )
	{
		IReportItemExecutor parent = getParent( );
		IBaseResultSet[] rsets = null;
		while ( parent != null )
		{
			rsets = parent.getQueryResults( );
			if ( rsets != null && rsets.length > 0 )
			{
				break;
			}
			parent = parent.getParent( );
		}
		if ( rsets != null && rsets.length > 0 )
		{
			if ( rsets[0] instanceof IQueryResultSet )
			{
				IQueryResultSet rset = (IQueryResultSet) rsets[0];
				if ( rset != null )
				{
					DataSetID dataSetID = rset.getID( );
					long position = rset.getRowIndex( );
					return new DataID( dataSetID, position );
				}
			}
			else if ( rsets[0] instanceof ICubeResultSet )
			{
				ICubeResultSet cset = (ICubeResultSet) rsets[0];
				if ( cset != null )
				{
					DataSetID dataSetID = cset.getID( );
					String position = cset.getCellIndex( );
					return new DataID( dataSetID, position );
				}
			}

		}
		return null;
	}
}