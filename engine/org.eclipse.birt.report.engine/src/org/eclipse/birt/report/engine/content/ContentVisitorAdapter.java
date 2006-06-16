
package org.eclipse.birt.report.engine.content;

public class ContentVisitorAdapter implements IContentVisitor
{

	public Object visit(IContent content, Object value)
	{
		return content.accept(this, value);
	}
	
	public Object visitContent( IContent content, Object value )
	{
		return value;
	}

	public Object visitPage( IPageContent page, Object value )
	{
		return visitContent( page, value );
	}

	public Object visitContainer( IContainerContent container, Object value )
	{
		return visitContent( container, value );
	}

	public Object visitTable( ITableContent table, Object value )
	{
		return visitContent( table, value );
	}

	public Object visitTableBand( ITableBandContent tableBand, Object value )
	{
		return visitContent( tableBand, value );
	}
	
	public Object visitList( IListContent list, Object value )
	{
		return visitContainer( list, value );
	}

	public Object visitListBand( IListBandContent listBand, Object value )
	{
		return visitContainer( listBand, value );
	}

	public Object visitRow( IRowContent row, Object value )
	{
		return visitContent( row, value );
	}

	public Object visitCell( ICellContent cell, Object value )
	{
		return visitContainer( cell, value );
	}

	public Object visitText( ITextContent text, Object value )
	{
		return visitContent( text, value );
	}
	
	public Object visitLabel(ILabelContent label, Object value)
	{
		return visitText(label, value);
	}
	
	public Object visitAutoText(IAutoTextContent autoText, Object value)
	{
		return visitText(autoText, value);
	}

	public Object visitData(IDataContent data, Object value)
	{
		return visitText(data, value);
	}
	
	public Object visitImage( IImageContent image, Object value )
	{
		return visitContent( image, value );
	}

	public Object visitForeign( IForeignContent content, Object value )
	{
		return visitContent( content, value );
	}

	public Object visitGroup( IGroupContent group, Object value )
	{
		return visitContent( group, value );
	}

	public Object visitListGroup( IListGroupContent group, Object value )
	{
		return visitGroup( group, value );
	}

	public Object visitTableGroup( ITableGroupContent group, Object value )
	{
		return visitGroup( group, value );
	}

}