
package org.eclipse.birt.report.engine.emitter;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;

public class ContentEmitterUtil
{

	static IContentVisitor starter = new StartContentVisitor( );
	static IContentVisitor ender = new EndContentVisitor( );

	static public void startContent( IContent content, IContentEmitter emitter )
	{
		String vformat = content.getStyle( ).getVisibleFormat( );
		
		if ( vformat == null )
		{
			starter.visit( content, emitter );
		}
		else
		{
			if ( vformat.toLowerCase( ).indexOf(
					emitter.getOutputFormat( ).toLowerCase( ) ) > 0
					|| vformat.toLowerCase( ).indexOf( "all" ) > 0 )
			{
				starter.visit( content, emitter );
			}
		}
	}

	static public void endContent( IContent content, IContentEmitter emitter )
	{
        String vformat = content.getStyle( ).getVisibleFormat( );
		
        String format = emitter.getOutputFormat( );
		if ( vformat == null )
		{
			ender.visit( content, emitter );
		}
		else
		{
			if ( vformat.toLowerCase( ).indexOf(
					emitter.getOutputFormat( ).toLowerCase( ) ) > 0
					|| vformat.toLowerCase( ).indexOf( "all" ) > 0 )
			{
				ender.visit( content, emitter );
			}
		}
		
	}

	private static class StartContentVisitor implements IContentVisitor
	{

		public Object visit( IContent content, Object value )
		{
			return content.accept( this, value );
		}

		public Object visitContent( IContent content, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startContent( content );
			return value;
		}

		public Object visitPage( IPageContent page, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startPage( page );
			return value;
		}

		public Object visitContainer( IContainerContent container, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startContainer( container );
			return value;
		}

		public Object visitTable( ITableContent table, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTable( table );
			return value;
		}

		public Object visitTableBand( ITableBandContent tableBand, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTableBand( tableBand );
			return value;
		}

		public Object visitRow( IRowContent row, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startRow( row );
			return value;
		}

		public Object visitCell( ICellContent cell, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startCell( cell );
			return value;
		}

		public Object visitText( ITextContent text, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startText( text );
			return value;
		}

		public Object visitLabel( ILabelContent label, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startLabel( label );
			return value;
		}

		public Object visitAutoText( IAutoTextContent autoText, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startAutoText( autoText );
			return value;
		}

		public Object visitData( IDataContent data, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startData( data );
			return value;
		}

		public Object visitImage( IImageContent image, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startImage( image );
			return value;
		}

		public Object visitForeign( IForeignContent content, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startForeign( content );
			return value;
		}

		public Object visitList( IListContent list, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startList( list );
			return value;
		}

		public Object visitListBand( IListBandContent listBand, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startListBand( listBand );
			return value;
		}

		public Object visitGroup( IGroupContent group, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startGroup( group );
			return value;
		}

		public Object visitListGroup( IListGroupContent group, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startListGroup( group );
			return value;
		}

		public Object visitTableGroup( ITableGroupContent group, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTableGroup( group );
			return value;
		}
	}

	static private class EndContentVisitor implements IContentVisitor
	{

		public Object visit( IContent content, Object value )
		{
			return content.accept( this, value );
		}

		public Object visitContent( IContent content, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endContent( content );
			return value;
		}

		public Object visitPage( IPageContent page, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endPage( page );
			return value;
		}

		public Object visitContainer( IContainerContent container, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endContainer( container );
			return value;
		}

		public Object visitTable( ITableContent table, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTable( table );
			return value;
		}

		public Object visitTableBand( ITableBandContent tableBand, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTableBand( tableBand );
			return value;
		}

		public Object visitRow( IRowContent row, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endRow( row );
			return value;
		}

		public Object visitCell( ICellContent cell, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endCell( cell );
			return value;
		}

		public Object visitText( ITextContent text, Object value )
		{
			return value;
		}

		public Object visitLabel( ILabelContent label, Object value )
		{
			return value;
		}

		public Object visitAutoText( IAutoTextContent autoText, Object value )
		{
			return value;
		}

		public Object visitData( IDataContent data, Object value )
		{
			return value;
		}

		public Object visitImage( IImageContent image, Object value )
		{
			return value;
		}

		public Object visitForeign( IForeignContent content, Object value )
		{
			return value;
		}

		public Object visitList( IListContent list, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endList( list );
			return value;
		}

		public Object visitListBand( IListBandContent listBand, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endListBand( listBand );
			return value;
		}

		public Object visitGroup( IGroupContent group, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endGroup( group );
			return value;
		}

		public Object visitListGroup( IListGroupContent group, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endListGroup( group );
			return value;
		}

		public Object visitTableGroup( ITableGroupContent group, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTableGroup( group );
			return value;
		}
	}
}
