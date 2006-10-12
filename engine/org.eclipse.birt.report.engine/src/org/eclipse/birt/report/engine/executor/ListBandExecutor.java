
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.w3c.dom.css.CSSValue;

public class ListBandExecutor extends StyledItemExecutor
{

	protected ListBandExecutor( ExecutorManager manager )
	{
		super( manager );
	}
	
	protected DataID getDataID( )
	{
		IResultSet curRset = getResultSet( );
		if (curRset == null)
		{
			curRset = getParentResultSet( );
		}
		if ( curRset != null )
		{
			DataSetID dataSetID = curRset.getID( );
			long position = curRset.getCurrentPosition( );
			return new DataID( dataSetID, position );
		}		
		return null;
	}

	ListingElementExecutor listExecutor;
	
	void setListingExecutor(ListingElementExecutor listExecutor)
	{
		this.listExecutor = listExecutor;
	}
	public IContent execute( )
	{
		ListBandDesign bandDesign = (ListBandDesign) getDesign();

		IListBandContent bandContent = report.createListBandContent( );
		setContent(bandContent);

		restoreResultSet( );
		
		initializeContent( bandDesign, bandContent );

		int type = bandDesign.getBandType( );
		if((type == BandDesign.BAND_DETAIL || type == BandDesign.GROUP_HEADER )&& listExecutor.needSoftBreakBefore( ))
		{
			IStyle style = content.getStyle( );
			if(style!=null)
			{
				CSSValue pageBreak = style.getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE);
				if(pageBreak==null || IStyle.AUTO_VALUE.equals( pageBreak ))
				{
					style.setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.SOFT_VALUE );
				}
			}
		}
		
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