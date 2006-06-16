/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutManager;

public abstract class HTMLInlineStackingLM extends HTMLStackingLM
{

	protected List childrenLayouts = new ArrayList( );
	protected List childrenExecutors = new ArrayList( );
	protected List childrenResults = new ArrayList( );

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
		childrenResults.clear( );
		super.close( );
	}

	public void cancel( )
	{
		Iterator iter = childrenLayouts.iterator( );
		while ( iter.hasNext( ) )
		{
			ILayoutManager layout = (ILayoutManager) iter.next( );
			layout.cancel( );
			layout.close( );
		}
		iter = childrenExecutors.iterator( );
		while ( iter.hasNext( ) )
		{
			IReportItemExecutor executor = (IReportItemExecutor) iter.next( );
			executor.close( );
		}
		childrenLayouts.clear( );
		childrenExecutors.clear( );
		childrenResults.clear( );
		super.cancel( );
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
			childrenResults.add( Boolean.TRUE );
		}
	}

	protected boolean resumeLayout( )
	{
		boolean hasNext = false;
		for ( int i = 0; i < childrenLayouts.size( ); i++ )
		{
			boolean childHasNext = ( (Boolean) childrenResults.get( i ) )
					.booleanValue( );
			if ( childHasNext )
			{
				ILayoutManager childLayout = (ILayoutManager) childrenLayouts
						.get( i );
				childHasNext = childLayout.layout( );
				if ( childHasNext )
				{
					hasNext = true;
				}
				else
				{
					childLayout.close( );
					IReportItemExecutor childExecutor = (IReportItemExecutor) childrenExecutors
							.get( i );
					childExecutor.close( );
				}
				childrenResults.set( i, Boolean.valueOf( childHasNext ) );
			}
		}
		return hasNext;
	}

	boolean initializedChildren = false;

	protected boolean layoutChildren( )
	{
		if ( !initializedChildren )
		{
			initializedChildren = true;
			initalizeChildren( );
		}
		return resumeLayout( );
	}
}
