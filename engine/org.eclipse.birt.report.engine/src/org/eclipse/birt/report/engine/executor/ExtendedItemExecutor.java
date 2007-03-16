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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
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
		ExtendedItemHandle handle = (ExtendedItemHandle) design.getHandle( );
		String tagName = handle.getExtensionName( );

		executor = ExtensionManager.getInstance( ).createReportItemExecutor(
				tagName );
		if ( executor != null )
		{
			//user implement the IReportItemExecutor.
			executor.setContext( executorContext );
			executor.setModelObject( handle );
			if (parent instanceof ExtendedItemExecutor)
			{
				executor.setParent( ((ExtendedItemExecutor)parent).executor );
			}
			else
			{
				executor.setParent( parent );
			}
			
			if (executor instanceof ExtendedItemGenerationExecutor)
			{
				ExtendedItemGenerationExecutor gExecutor = (ExtendedItemGenerationExecutor) executor;
				gExecutor.context = context;
				gExecutor.report = report;
				gExecutor.design = design;
			}
			
			content = executor.execute( );
			
			if ( context.isInFactory( ) )
			{
				//context.execute( design.getOnCreate( ) );
				handleOnCreate( content );
			}

			startTOCEntry( content );
			
			if ( emitter != null )
			{
				ContentEmitterUtil.startContent( content, emitter );
			}
			return content;
		}
		return null;
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
			return executor.getNextChild( );
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
		manager.releaseExecutor( ExecutorManager.EXTENDEDITEM, this );
	}
	
	
}