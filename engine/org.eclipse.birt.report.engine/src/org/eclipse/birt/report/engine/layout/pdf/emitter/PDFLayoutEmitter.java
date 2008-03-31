package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Iterator;
import java.util.Stack;

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
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;


public class PDFLayoutEmitter extends ContentEmitterAdapter implements IContentEmitter
{
	IContentEmitter emitter;
	Stack layoutStack = new Stack();
	ContainerLayout current;
	LayoutContextFactory factory;
	
	public void layout(IContent content)
	{
		StartVisitor start = new StartVisitor();
		EndVisitor end = new EndVisitor();
		visitContent(start, end, content);
	}
	
	public PDFLayoutEmitter(IContentEmitter emitter)
	{
		this.emitter = emitter;
	}
	

	public void initialize( IEmitterServices service )
	{
		emitter.initialize( service );
	}
	
	public String getOutputFormat( )
	{
		return emitter.getOutputFormat( );
	}

	public void start( IReportContent report )
	{
		//FIXME
	}
	
	public void end( IReportContent report )
	{
		//FIXME resolve totalpage etc
	}


	public void startContainer( IContainerContent container )
	{
		boolean isInline = PropertyUtil.isInlineElement( container );
		ContainerLayout layout;
		if(isInline)
		{
			if(current instanceof InlineStackingLayout)
			{
				layout = factory.createLayoutManager( current, container );
			}
			else
			{
				ContainerLayout lineLayout = factory.createLayoutManager( current, null );
				lineLayout.initialize( );
				current = lineLayout;
			}
		}
		else
		{
			if(current instanceof InlineStackingLayout)
			{
				while(current instanceof InlineStackingLayout)
				{
					current.closeLayout( );
					current = current.getParent( );
				}
			}
		}
		layout = factory.createLayoutManager( current, container );
		current = layout;
		layout.initialize( );
	}


	public void endContainer( IContainerContent container )
	{
		boolean isInline = PropertyUtil.isInlineElement( container );
		ContainerLayout layout;
		if(isInline)
		{
			if(current instanceof InlineStackingLayout)
			{
				
			}
			else
			{
				assert(false);
			}
		}
		else
		{
			if(current instanceof InlineStackingLayout)
			{
				assert(false);
			}
			else
			{
				
			}
		}
		current.closeLayout( );
		current = current.getParent( );
	}

	public void startContent( IContent content )
	{
		boolean isInline = PropertyUtil.isInlineElement( content );
		Layout layout;
		if(isInline)
		{
			if(current instanceof InlineStackingLayout)
			{
				layout = factory.createLayoutManager( current, content );
			}
			else
			{
				ContainerLayout lineLayout = factory.createLayoutManager( current, null );
				lineLayout.initialize( );
				current = lineLayout;
			}
		}
		else
		{
			if(current instanceof InlineStackingLayout)
			{
				while(current instanceof InlineStackingLayout)
				{
					current.closeLayout( );
					current = current.getParent( );
				}
			}
		}
		layout = factory.createLayoutManager( current, content );
		layout.initialize( );
		layout.layout( );
		layout.closeLayout( );
	}
	
	public void endContent( IContent content )
	{
		//do nothing;
	}

	public void startListBand( IListBandContent listBand )
	{
		// TODO Auto-generated method stub
		super.startListBand( listBand );
	}

	public void endListBand( IListBandContent listBand )
	{
		// TODO Auto-generated method stub
		super.endListBand( listBand );
	}

	public void startPage( IPageContent page )
	{
		// TODO Auto-generated method stub
		super.startPage( page );
	}

	public void endPage( IPageContent page )
	{
		// TODO Auto-generated method stub
		super.endPage( page );
	}

	protected void startTableContainer(IContainerContent container)
	{
		ContainerLayout layout = factory.createLayoutManager( current, container );
		current = layout;
		current.initialize( );
	}
	
	protected void  endTableContainer(IContainerContent container)
	{
		current.closeLayout( );
		current = current.getParent( );
	}
	
	public void startRow( IRowContent row )
	{
		startTableContainer(row);
	}

	public void endRow( IRowContent row )
	{
		endTableContainer(row);
	}

	public void startTableBand( ITableBandContent band )
	{
		startTableContainer(band);
	}


	public void startTableGroup( ITableGroupContent group )
	{
		startTableContainer(group);
	}

	public void endTableBand( ITableBandContent band )
	{
		endTableContainer(band);
	}


	public void endTableGroup( ITableGroupContent group )
	{
		endTableContainer(group);
	}


	public void startCell( ICellContent cell )
	{
		startTableContainer(cell);
	}
	
	public void endCell(ICellContent cell)
	{
		endTableContainer(cell);
	}
	
	protected void visitContent(StartVisitor start, EndVisitor end, IContent content)
	{
		start.visit( content, emitter );
		java.util.Collection children = content.getChildren( );
		if(children!=null && !children.isEmpty( ))
		{
			Iterator iter = children.iterator( );
			IContent child = (IContent)iter.next( );
			visitContent(start, end, child);
		}
		end.visit( content, emitter );
	}

	public void startForeign( IForeignContent foreign )
	{
		if ( IForeignContent.HTML_TYPE.equals( foreign.getRawType( ) ) )
		{
			// build content DOM tree for HTML text
			HTML2Content.html2Content( foreign );
			StartVisitor start = new StartVisitor();
			EndVisitor end = new EndVisitor();
			java.util.Collection children = foreign.getChildren( );
			if(children!=null && !children.isEmpty( ))
			{
				Iterator iter = children.iterator( );
				IContent child = (IContent)iter.next( );
				visitContent(start, end, child);
			}
		}
		else
		{
			startContent(foreign);
		}
	}
	
	private class StartVisitor implements IContentVisitor
	{

		public Object visit( IContent content, Object value )
		{

			( (IContentEmitter) value ).startContent( content );
			return null;

		}

		public Object visitContent( IContent content, Object value )
		{
			( (IContentEmitter) value ).startContent( content );
			return null;
		}

		public Object visitPage( IPageContent page, Object value )
		{
			( (IContentEmitter) value ).startPage( page );
			return null;
		}

		public Object visitContainer( IContainerContent container, Object value )
		{
			( (IContentEmitter) value ).startContainer( container );
			return null;
		}

		public Object visitTable( ITableContent table, Object value )
		{
			( (IContentEmitter) value ).startTable( table );
			return null;
		}

		public Object visitTableBand( ITableBandContent tableBand, Object value )
		{
			// ((IContentEmitter)value).startTableBand(tableBand);
			return null;
		}

		public Object visitRow( IRowContent row, Object value )
		{
			( (IContentEmitter) value ).startRow( row );
			return null;
		}

		public Object visitCell( ICellContent cell, Object value )
		{
			( (IContentEmitter) value ).startCell( cell );
			return null;
		}

		public Object visitText( ITextContent text, Object value )
		{
			( (IContentEmitter) value ).startText( text );
			return null;
		}

		public Object visitLabel( ILabelContent label, Object value )
		{
			( (IContentEmitter) value ).startLabel( label );
			return null;
		}

		public Object visitData( IDataContent data, Object value )
		{
			( (IContentEmitter) value ).startData( data );
			return null;
		}

		public Object visitImage( IImageContent image, Object value )
		{
			( (IContentEmitter) value ).startImage( image );
			return null;
		}

		public Object visitForeign( IForeignContent foreign, Object value )
		{
			( (IContentEmitter) value ).startForeign( foreign );
			return null;
		}

		public Object visitAutoText( IAutoTextContent autoText, Object value )
		{
			( (IContentEmitter) value ).startAutoText( autoText );
			return null;
		}

		public Object visitList( IListContent list, Object value )
		{
			( (IContentEmitter) value ).startList( list );
			return null;

		}

		public Object visitListBand( IListBandContent listBand, Object value )
		{
			// ((IContentEmitter)value).startListBand( listBand );
			return null;
		}

		public Object visitGroup( IGroupContent group, Object value )
		{
			// ((IContentEmitter)value).startGroup( group );
			return null;
		}

		public Object visitListGroup( IListGroupContent group, Object value )
		{
			// ((IContentEmitter)value).startListGroup( group );
			return null;
		}

		public Object visitTableGroup( ITableGroupContent group, Object value )
		{
			// ((IContentEmitter)value).startTableGroup( group );
			return null;
		}

	}

	private class EndVisitor implements IContentVisitor
	{

		public Object visit( IContent content, Object value )
		{
			( (IContentEmitter) value ).endContent( content );
			return null;
		}

		public Object visitContent( IContent content, Object value )
		{
			( (IContentEmitter) value ).endContent( content );
			return null;
		}

		public Object visitPage( IPageContent page, Object value )
		{
			( (IContentEmitter) value ).endPage( page );
			return null;
		}

		public Object visitContainer( IContainerContent container, Object value )
		{
			( (IContentEmitter) value ).endContainer( container );
			return null;
		}

		public Object visitTable( ITableContent table, Object value )
		{
			( (IContentEmitter) value ).endTable( table );
			return null;
		}

		public Object visitTableBand( ITableBandContent tableBand, Object value )
		{
			// ((IContentEmitter)value).endTableBand(tableBand);
			return null;
		}

		public Object visitRow( IRowContent row, Object value )
		{
			( (IContentEmitter) value ).endRow( row );
			return null;
		}

		public Object visitCell( ICellContent cell, Object value )
		{
			( (IContentEmitter) value ).endCell( cell );
			return null;
		}

		public Object visitText( ITextContent text, Object value )
		{
			return null;
		}

		public Object visitLabel( ILabelContent label, Object value )
		{
			return null;
		}

		public Object visitData( IDataContent data, Object value )
		{
			return null;
		}

		public Object visitImage( IImageContent image, Object value )
		{
			return null;
		}

		public Object visitForeign( IForeignContent foreign, Object value )
		{
			return null;
		}

		public Object visitAutoText( IAutoTextContent autoText, Object value )
		{
			return null;
		}

		public Object visitList( IListContent list, Object value )
		{
			( (IContentEmitter) value ).endList( list );
			return null;
		}

		public Object visitListBand( IListBandContent listBand, Object value )
		{
			// ((IContentEmitter)value).endListBand( listBand );
			return null;
		}

		public Object visitGroup( IGroupContent group, Object value )
		{
			// ((IContentEmitter)value).endGroup( group );
			return null;
		}

		public Object visitListGroup( IListGroupContent group, Object value )
		{
			// ((IContentEmitter)value).endListGroup(group) ;
			return null;
		}

		public Object visitTableGroup( ITableGroupContent group, Object value )
		{
			// ((IContentEmitter)value).endTableGroup(group) ;
			return null;
		}
	}

}
