
package org.eclipse.birt.report.tests.engine.emitter.html;

import java.util.HashMap;

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
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;

/**
 * <b>HTMLEmitter is an extended emitter for test</b>
 * <p>
 * Format: emitter_html
 */
public class HTMLEmitter implements IContentEmitter
{

	protected IContentEmitter emitter;

	public void initialize( IEmitterServices service )
	{
		emitter = (IContentEmitter) ( (HashMap) service.getRenderContext( ) )
				.get( "emitter_class" );
	}

	public void end( IReportContent report )
	{
		emitter.end( report );
	}

	public void endCell( ICellContent cell )
	{
		emitter.endCell( cell );
	}

	public void endContainer( IContainerContent container )
	{
		emitter.endContainer( container );
	}

	public void endContent( IContent content )
	{
		emitter.endContent( content );
	}

	public void endGroup( IGroupContent group )
	{
		emitter.endGroup( group );
	}

	public void endList( IListContent list )
	{
		emitter.endList( list );
	}

	public void endListBand( IListBandContent listBand )
	{
		emitter.endListBand( listBand );
	}

	public void endListGroup( IListGroupContent group )
	{
		emitter.endListGroup( group );
	}

	public void endPage( IPageContent page )
	{
		emitter.endPage( page );
	}

	public void endRow( IRowContent row )
	{
		emitter.endRow( row );
	}

	public void endTable( ITableContent table )
	{
		emitter.endTable( table );
	}

	public void endTableBand( ITableBandContent band )
	{
		emitter.endTableBand( band );
	}

	public void endTableGroup( ITableGroupContent group )
	{
		emitter.endTableGroup( group );
	}

	public String getOutputFormat( )
	{
		return emitter.getOutputFormat( );
	}

	public void start( IReportContent report )
	{
		emitter.start( report );
	}

	public void startAutoText( IAutoTextContent autoText )
	{
		emitter.startAutoText( autoText );
	}

	public void startCell( ICellContent cell )
	{
		emitter.startCell( cell );
	}

	public void startContainer( IContainerContent container )
	{
		emitter.startContainer( container );
	}

	public void startContent( IContent content )
	{
		emitter.startContent( content );

	}

	public void startData( IDataContent data )
	{
		emitter.startData( data );
	}

	public void startForeign( IForeignContent foreign )
	{
		emitter.startForeign( foreign );
	}

	public void startGroup( IGroupContent group )
	{
		emitter.startGroup( group );
	}

	public void startImage( IImageContent image )
	{
		emitter.startImage( image );
	}

	public void startLabel( ILabelContent label )
	{
		emitter.startLabel( label );
	}

	public void startList( IListContent list )
	{
		emitter.startList( list );
	}

	public void startListBand( IListBandContent listBand )
	{
		emitter.startListBand( listBand );
	}

	public void startListGroup( IListGroupContent group )
	{
		emitter.startListGroup( group );
	}

	public void startPage( IPageContent page )
	{
		emitter.startPage( page );
	}

	public void startRow( IRowContent row )
	{
		emitter.startRow( row );
	}

	public void startTable( ITableContent table )
	{
		emitter.startTable( table );
	}

	public void startTableBand( ITableBandContent band )
	{
		emitter.startTableBand( band );
	}

	public void startTableGroup( ITableGroupContent group )
	{
		emitter.startTableGroup( group );
	}

	public void startText( ITextContent text )
	{
		emitter.startText( text );
	}

}
