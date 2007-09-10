
package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.internal.executor.doc.Segment;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;

public class ReportBodyExecutor extends ContainerExecutor
{

	private Report reportDesign;
	private int nextItem;

	ReportBodyExecutor( ExecutorManager manager, Fragment fragment )
	{
		super( manager, -1 );
		this.reportDesign = context.getReport( );
		this.reader = manager.getReportReader( );
		this.nextItem = 0;
		// if fragment is null, we starts from the first element, so set
		// the next offset to 0, else use the one defined in the fragment.
		if ( fragment == null )
		{
			if ( !reader.isEmpty( ) )
			{
				nextOffset = 0;
			}
		}
		else
		{
			setFragment( fragment );
			// the first nextOffset alwasy start from 0 or fragment.leftEdge.
			Object[][] sections = fragment.getSections( );
			if ( sections != null && sections.length > 0 )
			{
				Object[] edges = sections[0];
				if ( edges[0] == Segment.LEFT_MOST_EDGE )
				{
					if ( !reader.isEmpty( ) )
					{
						nextOffset = 0;
					}
				}
				else
				{
					InstanceIndex leftEdge = (InstanceIndex) edges[0];
					if ( leftEdge.getOffset( ) == -1 )
					{
						if ( !reader.isEmpty( ) )
						{
							nextOffset = 0;
						}
					}
				}
			}
		}
		this.content = report.getRoot( );
	}

	public void close( )
	{
		nextItem = 0;
		super.close( );
	}

	public IContent execute( )
	{
		return content;
	}

	protected InstanceID getInstanceID( )
	{
		return null;
	}
	
	protected ReportItemExecutor doCreateExecutor( long offset )
			throws Exception
	{
		int itemCount = reportDesign.getContentCount( );
		if ( nextItem < itemCount )
		{
			ReportItemDesign itemDesign = reportDesign.getContent( nextItem );
			nextItem++;
			return manager.createExecutor( this, itemDesign, offset );
		}
		return null;
	}

	protected void doSkipToExecutor( InstanceID id, long offset )
			throws Exception
	{
		int itemCount = reportDesign.getContentCount( );
		long designId = id.getComponentID( );
		for ( int i = 0; i < itemCount; i++ )
		{
			ReportItemDesign itemDesign = reportDesign.getContent( i );
			if ( designId == itemDesign.getID( ) )
			{
				nextItem = i;
				return;
			}
		}
		nextItem = itemCount;
	}

	protected void doExecute( ) throws Exception
	{
	}
}
