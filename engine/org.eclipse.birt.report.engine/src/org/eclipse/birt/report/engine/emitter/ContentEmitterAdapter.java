
package org.eclipse.birt.report.engine.emitter;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
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
		startContainer( page );
	}

	public void endPage( IPageContent page )
	{
		endContainer( page );
	}

	public void startTable( ITableContent table )
	{
		startContainer( table );
	}

	public void endTable( ITableContent table )
	{
		endContainer( table );
	}

	public void startTableBand(ITableBandContent band)
	{
		startContainer(band);
	}
	
	public void endTableBand(ITableBandContent band)
	{
		endContainer(band);
	}
	
	public void startList( IListContent list )
	{
		startContainer( list );
	}

	public void endList( IListContent list )
	{
		endContainer( list );
	}

	public void startListBand( IListBandContent listBand )
	{
		startContainer( listBand );
	}

	public void endListBand( IListBandContent listBand )
	{
		endContainer( listBand );
	}
	public void startRow( IRowContent row )
	{
		startContainer( row );
	}

	public void endRow( IRowContent row )
	{
		endContainer( row );
	}

	public void startCell( ICellContent cell )
	{
		startContainer( cell );
	}

	public void endCell( ICellContent cell )
	{
		endContainer( cell );

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
	
	public void startAutoText( IAutoTextContent autoText )
	{
		startText ( autoText );
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

	public void endGroup( IGroupContent group )
	{
		endContainer( group );
	}

	public void startGroup( IGroupContent group )
	{
		startContainer( group );
	}

	public void endListGroup( IListGroupContent group )
	{
		endGroup(group);
	}

	public void endTableGroup( ITableGroupContent group )
	{
		endGroup(group);
	}

	public void startListGroup( IListGroupContent group )
	{
		startGroup(group);
	}

	public void startTableGroup( ITableGroupContent group )
	{
		startGroup(group);
	}

}