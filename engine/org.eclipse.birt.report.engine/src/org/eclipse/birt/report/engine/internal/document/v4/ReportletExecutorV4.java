/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.internal.executor.doc.Segment;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;

public class ReportletExecutorV4 extends AbstractReportExecutor
{

	private Fragment fragment;
	private ReportItemExecutor bodyExecutor;

	public ReportletExecutorV4( ExecutionContext context, long offset )
			throws IOException
	{

		super( context );
		fragment = createFragment( offset );
		bodyExecutor = new ReportBodyExecutor( manager, fragment );
	}

	public void close( )
	{
		bodyExecutor.close( );
		super.close( );
	}

	public IReportContent execute( )
	{
		bodyExecutor.execute( );
		return reportContent;
	}

	public IReportItemExecutor getNextChild( )
	{
		return bodyExecutor.getNextChild( );
	}

	public boolean hasNextChild( )
	{
		return bodyExecutor.hasNextChild( );
	}

	protected Fragment createFragment( long offset ) throws IOException
	{
		Object[] leftEdge = createIndexes( offset );
		Object[] rightEdge = new Object[leftEdge.length + 1];
		System.arraycopy( leftEdge, 0, rightEdge, 0, leftEdge.length );
		rightEdge[leftEdge.length] = Segment.RIGHT_MOST_EDGE;
		Fragment fragment = new Fragment( new InstanceIDComparator( ) );
		fragment.addFragment( leftEdge, rightEdge );
		return fragment;
	}

	protected InstanceIndex[] createIndexes( long offset ) throws IOException
	{
		LinkedList parents = new LinkedList( );
		IContent content = reader.loadContent( offset );

		while ( content != null )
		{
			InstanceID iid = content.getInstanceID( );
			DocumentExtension docExt = (DocumentExtension) content
					.getExtension( IContent.DOCUMENT_EXTENSION );
			if ( docExt != null )
			{
				long index = docExt.getIndex( );
				parents.addFirst( new InstanceIndex( iid, index ) );
			}
			content = (IContent) content.getParent( );
		}
		InstanceIndex[] edges = new InstanceIndex[parents.size( )];
		Iterator iter = parents.iterator( );
		int length = 0;
		while ( iter.hasNext( ) )
		{
			InstanceIndex index = (InstanceIndex) iter.next( );

			edges[length++] = index;
		}
		return edges;
	}

}
