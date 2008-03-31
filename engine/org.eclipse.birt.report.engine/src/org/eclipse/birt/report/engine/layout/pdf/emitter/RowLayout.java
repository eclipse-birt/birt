package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.pdf.PDFAbstractLM;
import org.eclipse.birt.report.engine.layout.pdf.PDFLayoutEngineContext;
import org.eclipse.birt.report.engine.layout.pdf.PDFStackingLM;
import org.eclipse.birt.report.engine.layout.pdf.PDFTableLM;


public class RowLayout extends ContainerLayout
{
	protected TableLayout tbl;

	public RowLayout( LayoutEngineContext context,
			ContainerLayout parentContext, IContent content )
	{
		super( context, parentContext, content );
	}

	protected void createRoot( )
	{
		root = AreaFactory.createRowArea( (IRowContent) content );
	}

	protected void initialize( )
	{
		tbl = getTableLayoutManager( );
		calculateSpecifiedHeight( );
		createRoot( );
		maxAvaWidth = parent.getCurrentMaxContentWidth( );
		root.setWidth( getCurrentMaxContentWidth( ) );
		root.setAllocatedHeight( parent.getCurrentMaxContentHeight( ) );
		maxAvaHeight = root.getContentHeight( );
	}


	protected void closeLayout( )
	{
		if ( root != null )
		{
			tbl.updateRow( (RowArea) root, specifiedHeight);
		}
	}
	

	public boolean addArea( AbstractArea area )
	{
		CellArea cArea = (CellArea) area;
		root.addChild( area );
		cArea.setPosition( tbl.getXPos( cArea.getColumnID( ) ), 0 );
		tbl.addRow( (RowArea)root );
		return true;
	}


}
