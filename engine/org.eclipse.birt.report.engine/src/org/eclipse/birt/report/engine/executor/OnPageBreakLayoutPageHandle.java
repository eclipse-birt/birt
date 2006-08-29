
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.executor;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.ListGroupContent;
import org.eclipse.birt.report.engine.content.impl.TableGroupContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.MultiLineItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.ITemplateArea;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.script.internal.CellScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.DataItemScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.DynamicTextScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.GridScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ImageScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.LabelScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ListGroupScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ListScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.RowScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TableGroupScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TableScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TextItemScriptExecutor;

/**
 * 
 */

public class OnPageBreakLayoutPageHandle implements ILayoutPageHandler
{

	private class LinkedContent 
	{
		IContent content;
		LinkedContent next;
		LinkedContent prev;
	}
	
	protected ExecutionContext executionContext;
	protected IContentEmitter emitter;
	protected HashMap designs = new HashMap( );
	protected LinkedContent firstContent;
	protected LinkedContent lastContent;
	
	public OnPageBreakLayoutPageHandle( ExecutionContext executionContext )
	{
		this.executionContext = executionContext;
		emitter = new PageContentEmitter();
		firstContent = new LinkedContent();
		lastContent = new LinkedContent();
		firstContent.next = lastContent;
		lastContent.prev = firstContent;
	}
	
	public IContentEmitter getEmitter()
	{
		return emitter;
	}
	
	protected void addContent(IContent content)
	{
		if (content == null)
			return;
		Object generateBy = content.getGenerateBy( );
		if ( !( generateBy instanceof ReportItemDesign ) )
		{
			return;
		}
		ReportItemDesign design = (ReportItemDesign) generateBy;
		if ( design.getOnPageBreak( ) != null
				|| design.getJavaClass( ) != null )
		{
			//add it into the list
			 LinkedContent cachedContent = (LinkedContent)designs.get( design );
			 if (cachedContent != null)
			 {
				 LinkedContent prev = cachedContent.prev;
				 LinkedContent next = cachedContent.next;
				 prev.next = next;
				 next.prev = prev;
			 }
			 else
			 {
				 cachedContent = new LinkedContent();
			 }
			 cachedContent.content = content;
			 cachedContent.prev = lastContent.prev;
			 lastContent.prev.next = cachedContent;
			 cachedContent.next = lastContent;
			 lastContent.prev = cachedContent;
			 designs.put( design, cachedContent);
		}
	}
	
	
	private class PageContentEmitter extends ContentEmitterAdapter
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startPage(org.eclipse.birt.report.engine.content.IPageContent)
		 */
		public void startPage( IPageContent page )
		{
			//for PDF output
			Object ext = page.getExtension( IContent.LAYOUT_EXTENSION );
			if ( ext instanceof PageArea ) // this is PDF layout extension
			{
				PageArea pageArea = (PageArea) ext;
				IContainerArea container = pageArea.getBody( );
				container.accept( new PageBreakContentCollector( ) );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startContent(org.eclipse.birt.report.engine.content.IContent)
		 */
		public void startContent( IContent content )
		{
			//for HTML output
			addContent( content );
		}
	}
	
	private class PageBreakContentCollector implements IAreaVisitor
	{

		public void start( IReportContent report )
		{
		}

		public String getOutputFormat( )
		{
			return null;
		}

		public void initialize( IEmitterServices service )
		{
		}

		public void visitText( ITextArea textArea )
		{
			addContent( textArea.getContent( ) );
		}

		public void visitAutoText( ITemplateArea templateArea )
		{
			addContent( templateArea.getContent( ) );
		}

		public void setTotalPage( ITextArea totalPage )
		{

		}

		public void visitImage( IImageArea imageArea )
		{
			addContent( imageArea.getContent( ) );
		}

		public void startContainer( IContainerArea containerArea )
		{
			addContent( containerArea.getContent( ) );
		}

		public void endContainer( IContainerArea containerArea )
		{
		}

		public void end( IReportContent report )
		{
		}

	}
	
	
	
	public void onPage( long page, Object context )
	{
		if ( executionContext == null )
		{
			return;
		}

		LinkedContent nextContent = firstContent.next;
		while (nextContent != lastContent)
		{
			IContent content = nextContent.content;
			nextContent = nextContent.next;
			ReportItemDesign design = (ReportItemDesign) content
					.getGenerateBy( );
			int contentType = content.getContentType( );
			switch ( contentType )
			{
				case IContent.CELL_CONTENT :
					CellScriptExecutor.handleOnPageBreak(
							(ICellContent) content, executionContext );
					break;
				case IContent.DATA_CONTENT :
					DataItemScriptExecutor.handleOnPageBreak(
							(IDataContent) content, executionContext );
					break;
				case IContent.FOREIGN_CONTENT :
					// FIXME: handle the onPageBreak for other items
					if ( design instanceof MultiLineItemDesign )
					{
						DynamicTextScriptExecutor.handleOnPageBreak(
								(IForeignContent) content, executionContext );
					}
					break;
				case IContent.IMAGE_CONTENT :
					ImageScriptExecutor.handleOnPageBreak(
							(IImageContent) content, executionContext );
					break;
				case IContent.LABEL_CONTENT :
					LabelScriptExecutor.handleOnPageBreak(
							(ILabelContent) content, executionContext );
					break;
				case IContent.ROW_CONTENT :
					RowScriptExecutor.handleOnPageBreak( (IRowContent) content,
							executionContext );
					break;
				case IContent.LIST_CONTENT :
					ListScriptExecutor.handleOnPageBreak(
							(IListContent) content, executionContext );
					break;
				case IContent.TABLE_CONTENT :
					if ( design instanceof TableItemDesign )
					{
						TableScriptExecutor.handleOnPageBreak(
								(ITableContent) content, executionContext );
					}
					else if ( design instanceof GridItemDesign )
					{
						GridScriptExecutor.handleOnPageBreak(
								(ITableContent) content, executionContext );
					}
					break;
				case IContent.TABLE_GROUP_CONTENT :
					TableGroupScriptExecutor.handleOnPageBreak(
							(TableGroupContent) content, executionContext );
					break;
				case IContent.LIST_GROUP_CONTENT :
					ListGroupScriptExecutor.handleOnPageBreak(
							(ListGroupContent) content, executionContext );
					break;
				case IContent.TEXT_CONTENT :
					TextItemScriptExecutor.handleOnPageBreak(
							(ITextContent) content, executionContext );
					break;
			}
		}
		//clear all contents
		firstContent.next = lastContent;
		lastContent.prev = firstContent;
		designs.clear( );
	}

}
