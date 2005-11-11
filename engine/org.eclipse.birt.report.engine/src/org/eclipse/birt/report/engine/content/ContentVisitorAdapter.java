
package org.eclipse.birt.report.engine.content;

public class ContentVisitorAdapter implements IContentVisitor
{

	public void visit(IContent content, Object value)
	{
		content.accept(this, value);
	}
	
	public void visitContent( IContent content, Object value )
	{
	}

	public void visitPage( IPageContent page, Object value )
	{
		visitContent( page, value );
	}

	public void visitContainer( IContainerContent container, Object value )
	{
		visitContent( container, value );
	}

	public void visitTable( ITableContent table, Object value )
	{
		visitContent( table, value );
	}

	public void visitTableBand( ITableBandContent tableBand, Object value )
	{
		visitContent( tableBand, value );
	}

	public void visitRow( IRowContent row, Object value )
	{
		visitContent( row, value );
	}

	public void visitCell( ICellContent cell, Object value )
	{
		visitContent( cell, value );
	}

	public void visitText( ITextContent text, Object value )
	{
		visitContent( text, value );
	}
	
	public void visitLabel(ILabelContent label, Object value)
	{
		visitText(label, value);
	}

	public void visitData(IDataContent data, Object value)
	{
		visitText(data, value);
	}
	
	public void visitImage( IImageContent image, Object value )
	{
		visitContent( image, value );
	}

	public void visitForeign( IForeignContent content, Object value )
	{
		visitContent( content, value );
	}

}
