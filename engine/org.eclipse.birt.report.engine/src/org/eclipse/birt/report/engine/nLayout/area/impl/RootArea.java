/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.layout.pdf.emitter.LayoutEmitterAdapter;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;

public class RootArea extends BlockContainerArea
{

	protected transient LayoutEmitterAdapter emitter;
	
	protected PageArea page;

	public RootArea( LayoutContext context, IContent content,
			LayoutEmitterAdapter emitter )
	{
		super( null, context, content );
		this.emitter = emitter;
	}

	public RootArea( RootArea area )
	{
		super( area );
	}
	
	public void autoPageBreak( ) throws BirtException
	{
		int height = context.getMaxBP( );
		SplitResult result = split( height, false) ;
		
		if(result==SplitResult.BEFORE_AVOID_WITH_NULL || result == SplitResult.SUCCEED_WITH_NULL)
		{
			result = split(height, true);
		}
		if ( result.getResult( )!= null )
		{
			page.setBody( result.getResult( ) );
			context.setFinished( false );
			page.close( );
		}
		updateChildrenPosition( );
		initialize( );
	}

	public RootArea cloneArea( )
	{
		return new RootArea( this );
	}
	
	public void initialize( ) throws BirtException
	{
		IPageContent pageContent = (IPageContent) content;
		if ( context.isAutoPageBreak( ) )
		{
			pageContent = createPageContent( pageContent );
			context.setPageNumber( context.getPageNumber( ) + 1 );
		}
		createNewPage( pageContent );
		maxAvaWidth = page.getBody( ).getWidth( );
		width = maxAvaWidth;
	}
	
	protected void createNewPage(IPageContent pageContent) throws BirtException
	{
		page = new PageArea(context, pageContent, emitter);
		page.initialize();
	}
	
	protected IPageContent createPageContent( IPageContent htmlPageContent )
	{
		if ( context.getPageNumber( ) == htmlPageContent.getPageNumber( ) )
		{
			return htmlPageContent;
		}
		else
		{
			IPageContent pageContent = createPageContent( htmlPageContent,
					context.getPageNumber( ), context.getTotalPage( ) );
			return pageContent;
		}
	}

	protected IPageContent createPageContent( IPageContent pageContent,
			long pageNumber, long totalPageNumber )
	{
		return (IPageContent) cloneContent( pageContent, pageNumber,
				totalPageNumber );
	}

	protected IContent cloneContent( IContent content, long pageNumber,
			long totalPageNumber )
	{
		IContent newContent = content.cloneContent( false );
		if ( newContent.getContentType( ) == IContent.AUTOTEXT_CONTENT )
		{
			IAutoTextContent autoText = (IAutoTextContent) newContent;
			int type = autoText.getType( );
			if ( type == IAutoTextContent.PAGE_NUMBER
					|| type == IAutoTextContent.UNFILTERED_PAGE_NUMBER )
			{
				String pattern = autoText.getComputedStyle( ).getNumberFormat( );
				NumberFormatter nf = new NumberFormatter( pattern );
				autoText.setText( nf.format( pageNumber ) );
			}
		}
		Iterator iter = content.getChildren( ).iterator( );
		while ( iter.hasNext( ) )
		{
			IContent child = (IContent) iter.next( );
			IContent newChild = cloneContent( child, pageNumber,
					totalPageNumber );
			newChild.setParent( newContent );
			newContent.getChildren( ).add( newChild );
		}
		return newContent;
	}


	public void close( ) throws BirtException
	{
			page.setBody( this );
			context.setFinished( true );
			page.close( );
			finished = true;
	}
}
