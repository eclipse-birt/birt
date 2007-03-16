/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public abstract class PDFLeafItemLM extends PDFAbstractLM
{

	public PDFLeafItemLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content,  executor );
	}

	protected void cancelChildren( )
	{

	}

	/**
	 * need overwrite this mothod to change default behavour.
	 * For inline leaf elements, page-break is handled by this layout manager.
	 * For block leaf elements. page-break is handled by it's block container
	 */
	protected boolean handlePageBreakBefore( )
	{
		if ( content != null )
		{
			if ( PropertyUtil.isInlineElement( content ) )
			{
				return super.handlePageBreakBefore( );
			}
		}
		return false;
	}

	/**
	 * need overwrite this mothod to change default behavour.
	 * For inline leaf elements, page-break is handled by this layout manager.
	 * For block leaf elements. page-break is handled by it's block container
	 */
	protected boolean handlePageBreakAfter( )
	{
		if ( content != null )
		{
			if ( PropertyUtil.isInlineElement( content ) )
			{
				return super.handlePageBreakAfter( );
			}
		}
		return false;
	}

	public boolean allowPageBreak( )
	{
		return false;
	}
	
	 
	
	/**
	 * create inline container area by content
	 * @param content the content object
	 * @param isFirst if this area is the first area of the content
	 * @param isLast if this area is the last area of the content
	 * @return
	 */
	protected IContainerArea createInlineContainer(IContent content, boolean isFirst, boolean isLast)
	{
		IContainerArea containerArea = AreaFactory.createInlineContainer( content );
		IStyle style = containerArea.getStyle();
		//remove left padding, border and margin if it is not the first child
		if(!isFirst)
		{
			style.setProperty(IStyle.STYLE_BORDER_LEFT_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_LEFT, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_LEFT, IStyle.NUMBER_0);
		}
		//remove right padding, border and margin if it is not the last child
		if(!isLast)
		{
			style.setProperty(IStyle.STYLE_BORDER_RIGHT_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_RIGHT, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_RIGHT, IStyle.NUMBER_0);
		}
		return containerArea;
	}
	
	protected boolean hasNextChild()
	{
		return true;
	}
	
	public void autoPageBreak()
	{
		return;
	}

}
