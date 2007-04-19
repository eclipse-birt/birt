
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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.ITemplateArea;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.script.internal.OnPageBreakScriptVisitor;

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

		public void visitText( ITextArea textArea )
		{
			addContent( textArea.getContent( ) );
		}

		public void visitAutoText( ITemplateArea templateArea )
		{
			addContent( templateArea.getContent( ) );
		}

		public void visitImage( IImageArea imageArea )
		{
			addContent( imageArea.getContent( ) );
		}

		public void visitContainer( IContainerArea container )
		{
			addContent( container.getContent( ) );
			Iterator iter = container.getChildren( );
			while ( iter.hasNext( ) )
			{
				IArea child = (IArea) iter.next( );
				child.accept( this );
			}
		}
	}
	
	
	
	public void onPage( long page, Object context )
	{
		if ( executionContext == null )
		{
			return;
		}
		
		OnPageBreakScriptVisitor onPageBreakVisitor = new OnPageBreakScriptVisitor( executionContext );
		
		LinkedContent nextContent = firstContent.next;
		while (nextContent != lastContent)
		{
			IContent content = nextContent.content;
			nextContent = nextContent.next;
			onPageBreakVisitor.onPageBreak( content );			
		}
		//clear all contents
		firstContent.next = lastContent;
		lastContent.prev = firstContent;
		designs.clear( );
	}

}
