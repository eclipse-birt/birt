/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.layout.pdf.text.Chunk;
import org.eclipse.birt.report.engine.layout.pdf.text.ChunkGenerator;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;


public class PDFLayoutEmitter extends ContentEmitterAdapter implements IContentEmitter
{
	IContentEmitter emitter;
	Stack layoutStack = new Stack();
	ContainerLayout current;
	LayoutContextFactory factory;
	LayoutEngineContext context;
	Map options;
	
	/*public void layout(LayoutEngineContext context, IContent content, IContainerArea root)
	{
		StartVisitor start = new StartVisitor();
		EndVisitor end = new EndVisitor();
		current = new RegionLayout(context, null, content, root);
		visitContent(start, end, content);
	}*/
	
	public PDFLayoutEmitter( IContentEmitter emitter,
			IRenderOption renderOptions, Locale locale, long totalPage )
	{
		this.emitter = emitter;
		context = new LayoutEngineContext( );
		if ( renderOptions != null )
		{
			String format = renderOptions.getOutputFormat( );
			context.setFormat( format );
		}
		context.setLocale( locale );
		factory = new LayoutContextFactory( context );
		context.totalPage = totalPage;
		if ( renderOptions != null )
		{
			this.options = renderOptions.getOptions( );
		}
	}
	
	public PDFLayoutEmitter(LayoutEngineContext context)
	{
		factory = new LayoutContextFactory(context);
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
		setupLayoutOptions();
		emitter.start( report );
	}
	
	protected void setupLayoutOptions()
	{
		if(options!=null)
		{
			Object fitToPage = options.get(IPDFRenderOption.FIT_TO_PAGE);
			if(fitToPage!=null && fitToPage instanceof Boolean)
			{
				if(((Boolean)fitToPage).booleanValue())
				{
					context.setFitToPage(true);
				}
			}
			Object pageBreakOnly = options.get(IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY);
			if(pageBreakOnly!=null && pageBreakOnly instanceof Boolean)
			{
				if(((Boolean)pageBreakOnly).booleanValue())
				{
					context.setPagebreakPaginationOnly(true);
				}
			}
			Object pageOverflow = options.get(IPDFRenderOption.PAGE_OVERFLOW);
			if( pageOverflow!=null )
			{
				int pageOverflowType = ((Integer)pageOverflow).intValue();
				context.setPageOverflow(pageOverflowType);
			}
			else
			{
				if ( context.fitToPage() )
				{
					context.setPageOverflow(IPDFRenderOption.FIT_TO_PAGE_SIZE);
				}
			}
			/*Object outputDisplayNone = options
					.get( IPDFRenderOption.OUTPUT_DISPLAY_NONE );
			if ( outputDisplayNone instanceof Boolean )
			{
				if ( ( (Boolean) outputDisplayNone ).booleanValue( ) )
				{
					context.setOutputDisplayNone( true );
				}
			}*/
	
			Object textWrapping = options.get(IPDFRenderOption.PDF_TEXT_WRAPPING);
			if(textWrapping!=null && textWrapping instanceof Boolean)
			{
				if(!((Boolean)textWrapping).booleanValue())
				{
					context.setTextWrapping(false);
				}
			}
			Object fontSubstitution = options.get(IPDFRenderOption.PDF_FONT_SUBSTITUTION);
			if(fontSubstitution!=null && fontSubstitution instanceof Boolean)
			{
				if(!((Boolean)fontSubstitution).booleanValue())
				{
					context.setFontSubstitution(false);
				}
			}
			Object bidiProcessing = options.get(IPDFRenderOption.PDF_BIDI_PROCESSING);
			if(bidiProcessing!=null && bidiProcessing instanceof Boolean)
			{
				if(!((Boolean)bidiProcessing).booleanValue())
				{
					context.setBidiProcessing(false);
				}
			}
			Object hyhenation = options.get(IPDFRenderOption.PDF_HYPHENATION);
			if(hyhenation!=null && hyhenation instanceof Boolean)
			{
				if(!((Boolean)hyhenation).booleanValue())
				{
					context.setEnableHyphenation(false);
				}
			}
		}
	}
	
	public void end( IReportContent report )
	{
		resolveTotalPage( emitter );
		emitter.end( report );
	}
	
	protected void resolveTotalPage( IContentEmitter emitter )
	{
		IContent con = context.getUnresolvedContent( );
		if ( !( con instanceof IAutoTextContent ) )
		{
			return;
		}

		IAutoTextContent totalPageContent = (IAutoTextContent) con;
		if ( null != totalPageContent )
		{
			NumberFormatter nf = new NumberFormatter( );
			String patternStr = totalPageContent.getComputedStyle( )
					.getNumberFormat( );
			nf.applyPattern( patternStr );
			
			long totalPageCount = context.totalPage>0 ? context.totalPage : context.pageCount;
			totalPageContent.setText( nf.format( totalPageCount ));

			AbstractArea totalPageArea = null;
			ChunkGenerator cg = new ChunkGenerator( context.getFontManager( ),
					totalPageContent, true, true );
			if ( cg.hasMore( ) )
			{
				Chunk c = cg.getNext( );
				Dimension d = new Dimension(
						(int) ( c.getFontInfo( ).getWordWidth( c.getText( ) ) * PDFConstants.LAYOUT_TO_PDF_RATIO ),
						(int) ( c.getFontInfo( ).getWordHeight( ) * PDFConstants.LAYOUT_TO_PDF_RATIO ) );
				totalPageArea = (AbstractArea)AreaFactory.createTextArea( totalPageContent, c.getFontInfo( ), false );
				totalPageArea.setWidth( Math.min( context.getMaxWidth( ), d.getWidth()) );
				totalPageArea.setHeight( Math.min( context.getMaxHeight( ), d.getHeight()) );
			}
			totalPageContent.setExtension( IContent.LAYOUT_EXTENSION,
					totalPageArea );
			emitter.startAutoText( totalPageContent );
		}
	}
	
	public void startContainer( IContainerContent container )
	{
		_startContainer( container );
	}

	public void _startContainer( IContent container )
	{
		boolean isInline = PropertyUtil.isInlineElement( container );
		Layout layout;
		if(isInline)
		{
			if(current instanceof InlineStackingLayout)
			{
				//layout = factory.createLayoutManager( current, container );
			}
			else
			{
				Layout lineLayout = factory.createLayoutManager( current, null );
				lineLayout.initialize( );
				current = (ContainerLayout)lineLayout;
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
		if ( layout != null )
		{
			current = (ContainerLayout) layout;
			layout.initialize( );
		}
	}
	
	public void endContainer( IContainerContent container )
	{
		_endContainer( container );
	}

	private void _endContainer( IContent container )
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
				while(current instanceof InlineStackingLayout)
				{
					current.closeLayout( );
					current = current.getParent( );
				}
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
				//layout = factory.createLayoutManager( current, content );
			}
			else
			{
				ContainerLayout lineLayout = (ContainerLayout)factory.createLayoutManager( current, null );
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
		if(layout!=null)
		{
			layout.initialize( );
			layout.layout( );
			layout.closeLayout( );
		}
	}
	
	public void endContent( IContent content )
	{
		//do nothing;
	}

	public void startListBand( IListBandContent listBand )
	{
		
	}

	public void endListBand( IListBandContent listBand )
	{
		
	}

	public void startPage( IPageContent page )
	{
		// TODO Auto-generated method stub
		super.startPage( page );
		long number = page.getPageNumber( );
		if ( number > 0 )
		{
			context.pageNumber = number;
		}
		context.pageCount++;
		
	}
	
	public void endPage( IPageContent page )
	{
		// TODO Auto-generated method stub
		super.endPage( page );
		MasterPageDesign mp = (MasterPageDesign)page.getGenerateBy( );

		if ( mp instanceof SimpleMasterPageDesign )
		{
			//FIXME ShowHeaderOnFirst and ShowFooterOnLast
			if ( ( (SimpleMasterPageDesign) mp ).isFloatingFooter( ) )
			{
				floatingFooter( page );
			}
		}
		emitter.startPage( page );
		emitter.endPage( page );
	}
	
	public void floatingFooter( IPageContent pageContent )
	{
		Object obj = pageContent.getExtension( IContent.LAYOUT_EXTENSION );
		
		if(obj!=null && obj instanceof PageArea)
		{
			PageArea page = (PageArea)obj;
			ContainerArea footer = (ContainerArea) page.getFooter( );
			IContainerArea body = page.getBody( );
			IContainerArea header = page.getHeader( );
			if ( footer != null )
			{
				footer.setPosition( footer.getX( ), ( header == null ? 0 : header
						.getHeight( ) )
						+ ( body == null ? 0 : body.getHeight( ) ) );
			}
		}
	}
		

	protected void startTableContainer(IContainerContent container)
	{
		ContainerLayout layout = (ContainerLayout)factory.createLayoutManager( current, container );
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
		endContainer( cell );
	}
	
	protected void visitContent( IContent content, IContentEmitter emitter)
	{
		ContentEmitterUtil.startContent( content, emitter );
		java.util.Collection children = content.getChildren( );
		if ( children != null && !children.isEmpty( ) )
		{
			Iterator iter = children.iterator( );
			while(iter.hasNext( ))
			{
				IContent child = (IContent) iter.next( );
				visitContent(  child, emitter );
			}
		}
		ContentEmitterUtil.endContent(  content, emitter );
	}

	public void startForeign( IForeignContent foreign )
	{
		if ( IForeignContent.HTML_TYPE.equals( foreign.getRawType( ) ) )
		{
			_startContainer(foreign);
			// build content DOM tree for HTML text
			HTML2Content.html2Content( foreign );
			java.util.Collection children = foreign.getChildren( );
			if ( children != null && !children.isEmpty( ) )
			{
				Iterator iter = children.iterator( );
				IContent child = (IContent) iter.next( );
				visitContent( child, this );
			}
			//FIXME
			foreign.getChildren( ).clear( );
			_endContainer(foreign);
		}
		else
		{
			startContent( foreign );
		}
	}
	

}
