/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutManager;

public abstract class HTMLInlineStackingLM extends HTMLStackingLM
{

	/**
	 * does the children has been intialized.
	 */
	protected boolean initializedChildren = false;
	/**
	 * all the inline children layouts
	 */
	protected List childrenLayouts = new ArrayList( );
	/**
	 * children executor.
	 */
	protected List childrenExecutors = new ArrayList( );

	/**
	 * the current finish status of all the chidren.
	 */
	protected List childrenFinished = new ArrayList();

	public HTMLInlineStackingLM( HTMLLayoutManagerFactory factory )
	{
		super( factory );
	}

	public void initialize( HTMLAbstractLM parent, IContent content,
			IReportItemExecutor executor, IContentEmitter emitter )
	{
		super.initialize( parent, content, executor, emitter );
		initializedChildren = false;
	}

	public void close( )
	{
		childrenLayouts.clear( );
		childrenExecutors.clear( );
		childrenFinished.clear();
		super.close( );
	}

	private void initalizeChildren( )
	{
		while ( executor.hasNextChild( ) )
		{
			IReportItemExecutor childExecutor = (IReportItemExecutor) executor
					.getNextChild( );
			IContent childContent = childExecutor.execute( );
			ILayoutManager childLayout = engine.createLayoutManager( this,
					childContent, childExecutor, emitter );
			childrenLayouts.add( childLayout );
			childrenExecutors.add( childExecutor );
			childrenFinished.add( Boolean.FALSE );
		}
	}

	/**
	 * layout the children, return if it should create a 
	 * new page after this layout.
	 * @return
	 */
	protected boolean resumeLayout( )
	{
		boolean hasNextPage = false;
		for ( int i = 0; i < childrenLayouts.size( ); i++ )
		{
			boolean childFinished = ( (Boolean) childrenFinished.get( i ) )
					.booleanValue( );
			if ( !childFinished )
			{
				ILayoutManager childLayout = (ILayoutManager) childrenLayouts
						.get( i );
				boolean childHasNewPage = childLayout.layout( );
				if (childHasNewPage)
				{
					hasNextPage = true;
				}
				childFinished = childLayout.isFinished( );
				if (childFinished)
				{
					childLayout.close( );
					IReportItemExecutor childExecutor = (IReportItemExecutor) childrenExecutors
							.get( i );
					childExecutor.close( );
				}
				childrenFinished.set( i, Boolean.valueOf( childFinished ) );
			}
		}
		return hasNextPage;
	}
	
	protected boolean isChildrenFinished( )
	{
		for ( int i = 0; i < childrenLayouts.size( ); i++ )
		{
			boolean childFinished = ( (Boolean) childrenFinished.get( i ) )
					.booleanValue( );
			if ( !childFinished )
			{
				return false;
			}
		}
		return true;
	}
	
	protected boolean layoutNodes( )
	{
		if ( !initializedChildren )
		{
			initializedChildren = true;
			initalizeChildren( );
		}
		return resumeLayout( );
	}
	
}
