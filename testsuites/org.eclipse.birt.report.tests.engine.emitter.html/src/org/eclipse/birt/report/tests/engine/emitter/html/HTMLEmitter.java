
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
 * 
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
		// TODO Auto-generated method stub

	}

	public void startGroup( IGroupContent group )
	{
		// TODO Auto-generated method stub

	}

	public void startImage( IImageContent image )
	{
		// TODO Auto-generated method stub

	}

	public void startLabel( ILabelContent label )
	{
		emitter.startLabel( label );
	}

	public void startList( IListContent list )
	{
		// TODO Auto-generated method stub

	}

	public void startListBand( IListBandContent listBand )
	{
		// TODO Auto-generated method stub

	}

	public void startListGroup( IListGroupContent group )
	{
		// TODO Auto-generated method stub

	}

	public void startPage( IPageContent page )
	{
		// TODO Auto-generated method stub

	}

	public void startRow( IRowContent row )
	{
		// TODO Auto-generated method stub

	}

	public void startTable( ITableContent table )
	{
		// TODO Auto-generated method stub

	}

	public void startTableBand( ITableBandContent band )
	{
		// TODO Auto-generated method stub

	}

	public void startTableGroup( ITableGroupContent group )
	{
		// TODO Auto-generated method stub

	}

	public void startText( ITextContent text )
	{
		// TODO Auto-generated method stub

	}

}
