
package org.eclipse.birt.report.engine.emitter;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;

public class ContentEmitterAdapter implements IContentEmitter
{

	public String getOutputFormat( )
	{
		return null;
	}

	public void initialize( IEmitterServices service )
	{
	}

	public void start( IReportContent report )
	{
	}

	public void end( IReportContent report )
	{
	}

	public void startContent( IContent content )
	{
	}

	public void endContent( IContent content )
	{
	}

	public void startPage( IPageContent page )
	{
		startContent( page );
	}

	public void endPage( IPageContent page )
	{
		endContent( page );
	}

	public void startTable( ITableContent table )
	{
		startContent( table );
	}

	public void endTable( ITableContent table )
	{
		endContent( table );
	}

	public void startTableHeader( ITableBandContent band )
	{
		startContent( band );
	}

	public void endTableHeader( ITableBandContent band )
	{
		endContent( band );
	}

	public void startTableBody( ITableBandContent band )
	{
		startContent( band );
	}

	public void endTableBody( ITableBandContent band )
	{
		endContent( band );
	}

	public void startTableFooter( ITableBandContent band )
	{
		startContent( band );
	}

	public void endTableFooter( ITableBandContent band )
	{
		endContent( band );
	}

	public void startRow( IRowContent row )
	{
		startContent( row );
	}

	public void endRow( IRowContent row )
	{
		endContent( row );
	}

	public void startCell( ICellContent cell )
	{
		startContent( cell );
	}

	public void endCell( ICellContent cell )
	{
		endContent( cell );

	}

	public void startContainer( IContainerContent container )
	{
		startContent( container );
	}

	public void endContainer( IContainerContent container )
	{
		endContent( container );
	}

	public void startText( ITextContent text )
	{
		startContent( text );
		endContent( text );
	}

	public void startLabel( ILabelContent label )
	{
		startText( label );
	}

	public void startData( IDataContent data )
	{
		startText( data );
	}

	public void startForeign( IForeignContent foreign )
	{
		startContent( foreign );
		endContent( foreign );
	}

	public void startImage( IImageContent image )
	{
		startContent( image );
		endContent( image );

	}

}
