/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;
import org.eclipse.birt.report.engine.presentation.PageSection;

public class ReportPageExecutorV4 extends AbstractReportExecutor
{

	private boolean paged;
	private ArrayList outputPages = new ArrayList( );

	private PageRangeIterator pageIter;
	private ReportBodyExecutor bodyExecutor;

	public ReportPageExecutorV4( ExecutionContext context, List pages,
			boolean paged ) throws IOException
	{
		super( context );
		this.outputPages.addAll( pages );
		this.paged = paged;
		pageIter = new PageRangeIterator( outputPages );
		if ( !paged )
		{
			Fragment fragment = loadPageHints( outputPages );
			bodyExecutor = new ReportBodyExecutor( manager, fragment );
		}
	}

	public void close( )
	{
		pageIter = null;
		if ( bodyExecutor != null )
		{
			bodyExecutor.close( );
			bodyExecutor = null;
		}
		super.close( );
	}

	public IReportItemExecutor getNextChild( )
	{
		if ( hasNextChild( ) )
		{
			try
			{
				if ( paged )
				{
					long pageNumber = pageIter.next( );
					Fragment fragment = loadPageHint( pageNumber );
					return new ReportBodyExecutor( manager, fragment );
				}
				else
				{
					return bodyExecutor.getNextChild( );
				}
			}
			catch ( IOException ex )
			{
				context.addException( new EngineException(
						"can't load the pages", ex ) );
			}
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		if ( paged )
		{
			return pageIter.hasNext( );
		}
		return bodyExecutor.hasNextChild( );
	}

	protected Fragment loadPageHints( List pageSequence ) throws IOException
	{

		Fragment fragment = new Fragment( new InstanceIDComparator( ) );

		PageRangeIterator iter = new PageRangeIterator( pageSequence );
		while ( iter.hasNext( ) )
		{
			long pageNumber = iter.next( );
			IPageHint pageHint = hintsReader.getPageHint( pageNumber );
			int sectCount = pageHint.getSectionCount( );
			for ( int i = 0; i < sectCount; i++ )
			{
				PageSection section = pageHint.getSection( i );
				InstanceIndex[] leftEdges = section.starts;
				InstanceIndex[] rightEdges = section.ends;
				fragment.addFragment( leftEdges, rightEdges );
			}
		}
		return fragment;
	}

	protected Fragment loadPageHint( long pageNumber ) throws IOException
	{
		Fragment fragment = new Fragment( new InstanceIDComparator( ) );

		IPageHint pageHint = hintsReader.getPageHint( pageNumber );
		int sectCount = pageHint.getSectionCount( );
		for ( int i = 0; i < sectCount; i++ )
		{
			PageSection section = pageHint.getSection( i );
			InstanceIndex[] leftEdges = section.starts;
			InstanceIndex[] rightEdges = section.ends;
			fragment.addFragment( leftEdges, rightEdges );
		}
		return fragment;

	}
	
	//FIXME: throw the exception out.
	public IPageHint getPageHint(long pageNumber) throws IOException
	{
		return hintsReader.getPageHint( pageNumber );
	}
}
