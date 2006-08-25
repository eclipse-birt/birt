
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class ListBandExecutor extends StyledItemExecutor
{

	protected ListBandExecutor( ExecutorManager manager )
	{
		super( manager );
	}

	public IContent execute( )
	{
		ListBandDesign bandDesign = (ListBandDesign) getDesign();

		IListBandContent bandContent = report.createListBandContent( );
		setContent(bandContent);

		restoreResultSet( );
		
		initializeContent( bandDesign, bandContent );

		startTOCEntry(bandContent);
		if (emitter != null)
		{
			emitter.startListBand( bandContent );
		}
		
		//prepare to execute the children
		currentItem = 0;

		return bandContent;
	}
	
	public void close( )
	{
		IListBandContent bandContent = (IListBandContent) getContent();
		if (emitter != null)
		{
			emitter.endListBand( bandContent );
		}
		finishTOCEntry( );
		manager.releaseExecutor( ExecutorManager.LISTBANDITEM, this );
	}

	int currentItem;

	public boolean hasNextChild()
	{
		ListBandDesign bandDesign = (ListBandDesign) design;
		return currentItem < bandDesign.getContentCount( );
	}
	
	public IReportItemExecutor getNextChild( )
	{
		ListBandDesign bandDesign = (ListBandDesign) design;
		if ( currentItem < bandDesign.getContentCount( ) )
		{
			ReportItemDesign itemDesign = bandDesign.getContent( currentItem++ );
			ReportItemExecutor executor = manager.createExecutor( this,
					itemDesign);
			return executor;
		}
		return null;
	}
}