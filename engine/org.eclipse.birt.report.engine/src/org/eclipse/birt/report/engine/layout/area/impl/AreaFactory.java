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

package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

/**
 * 
 * area factory. create area object by content or area FIXME support
 * backgroud-image offset
 * 
 */
public class AreaFactory
{

	public static IImageArea createImageArea( IImageContent image )
	{
		return new ImageArea( image );
	}
	
	public static IArea createTableGroupArea(IContent group)
	{
		return new BlockContainerArea(group);
	}

	public static IArea createTemplateArea( IAutoTextContent autoText )
	{
		return new TemplateArea( autoText );
	}

	public static IArea createTextArea( ITextContent content, String text,
			FontInfo fi )
	{
		return new TextArea( content, text, fi );
	}

	/**
	 * create block container area by content
	 * 
	 * @param content
	 * @return
	 */
	public static IContainerArea createBlockContainer( IContent content )
	{
		return new BlockContainerArea( content );
	}

	/**
	 * create lobic container area by content
	 * 
	 * @param content
	 * @return
	 */
	public static IContainerArea createLogicContainer( IReportContent report )
	{
		return new LogicContainerArea( report );
	}

	/**
	 * create cell area by cell content
	 * 
	 * @param cell
	 * @return
	 */
	public static CellArea createCellArea( ICellContent cell )
	{
		return new CellArea( cell );
	}

	/**
	 * create page area by page content
	 * 
	 * @param pageContent
	 * @return
	 */
	public static IContainerArea createPageArea( IPageContent pageContent )
	{
		return new PageArea( pageContent );
	}

	/**
	 * create table area by table content
	 * 
	 * @param table
	 * @return
	 */
	public static TableArea createTableArea( ITableContent table )
	{
		return new TableArea( table );
	}

	/**
	 * create row area by row content
	 * 
	 * @param row
	 * @return
	 */
	public static RowArea createRowArea( IRowContent row )
	{
		return new RowArea( row );
	}

	public static LineArea createLineArea( IReportContent report )
	{
		return new LineArea( report );
	}

	public static IContainerArea createInlineContainer( IContent content )
	{
		return new InlineContainerArea( content );
	}

}
