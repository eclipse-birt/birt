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

import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.ITemplateArea;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

/**
 * 
 * area factory.
 * create area object by content or area
 * FIXME support backgroud-image offset
 *
 */
public class AreaFactory
{
	/**
	 * create image area by image content
	 * @param image the image content
	 * @param contentDimension content dimension
	 * @return
	 */
	public static IImageArea createImageArea(IImageContent image, Dimension contentDimension)
	{
		ImageArea imageArea = new ImageArea(image);
		imageArea.setWidth(contentDimension.getWidth());
		imageArea.setHeight(contentDimension.getHeight());
		return imageArea;
	}
	
	/**
	 * create template area by autoText content
	 * @param autoText the autoText content
	 * @param contentDimension content dimension
	 * @return
	 */
	public static ITemplateArea createTemplateArea(IAutoTextContent autoText, Dimension contentDimension)
	{
		TemplateArea templateArea = new TemplateArea(autoText);
		templateArea.setWidth(contentDimension.getWidth());
		templateArea.setHeight(contentDimension.getHeight());
		return templateArea;
	}
	
	/**
	 * create inline text area by text content
	 * @param content the text content
	 * @param text the text string
	 * @param contentDimension the content dimension
	 * @param isFirst if this area is the first area of the content
	 * @param isLast if this area is the last area of the content
	 * @return
	 */
	public static IArea createInlineTextArea(String text, ITextContent content, int startOffset, int endOffset, FontInfo fi, Dimension contentDimension)
	{
		IStyle style = content.getComputedStyle();

		ContainerArea con = (ContainerArea)createInlineContainer(content, false, false);
		int textHeight = contentDimension.getHeight();
		int textWidth =  contentDimension.getWidth();
		con.setWidth(textWidth); 
		con.setHeight(textHeight + 
				PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_TOP)) +
				PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH)) +
				PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_BOTTOM)) +
				PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH)));
		
		TextArea textArea = new TextArea(content, text, fi);
		con.addChild(textArea);
		textArea.setHeight(textHeight);
		textArea.setWidth(textWidth);
		textArea.setPosition( 0,
				 PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_TOP))
				 + PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH)));
		return con;
	}
		
	/**
	 * create block text area by text content
	 * @param content the text content
	 * @param text the text string
	 * @param contentDimension the content dimension
	 * @param isFirst if this area is the first area of the content
	 * @param isLast if this area is the last area of the content
	 * @return
	 */
	public static IArea createBlockTextArea(String text, ITextContent content, int startOffset, int endOffset, FontInfo fi, Dimension contentDimension)
	{
		TextArea textArea = new TextArea(content, text, fi);
		textArea.setWidth( contentDimension.getWidth() );
		textArea.setHeight( contentDimension.getHeight() );
//		IStyle dest = textArea.getStyle();
//		IStyle orginal = content.getComputedStyle();
//
//		dest.setMarginTop(orginal.getPaddingTop());
//		dest.setMarginBottom(orginal.getPaddingBottom());
		return textArea;
	}
	
	
	/**
	 * create inline container area by content
	 * @param content
	 * @return
	 */
	public static IContainerArea createInlineContainer(IContent content)
	{
		return createInlineContainer(content, true, true);
	}

	/**
	 * create inline container area by content
	 * @param content the content object
	 * @param isFirst if this area is the first area of the content
	 * @param isLast if this area is the last area of the content
	 * @return
	 */
	public static IContainerArea createInlineContainer(IContent content, boolean isFirst, boolean isLast)
	{
		IContainerArea containerArea = new InlineContainerArea(content);
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
	
	
	/**
	 * create block container area by content
	 * @param content
	 * @return
	 */
	public static IContainerArea createBlockContainer(IContent content)
	{
		return createBlockContainer(content, true, true);
	}

	/**
	 * create block container area by content
	 * @param content the content object
	 * @param isFirst if this area is the first area of the content
	 * @param isLast if this area is the last area of the content
	 * @return
	 */
	public static IContainerArea createBlockContainer(IContent content, boolean isFirst, boolean isLast)
	{
		IContainerArea containerArea = new BlockContainerArea(content);
		IStyle style = containerArea.getStyle();
		//remove top padding, border and margin if it is not the first child
		if(!isFirst)
		{
			style.setProperty(IStyle.STYLE_BORDER_TOP_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_TOP, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0);
		}
		//remove bottom padding, border and margin if it is not the last child
		if(!isLast)
		{
			style.setProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_BOTTOM, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_BOTTOM, IStyle.NUMBER_0);
		}
		return containerArea;
	}
	
	/**
	 * create container area by container area
	 * @param area
	 * @param isFirst
	 * @param isLast
	 * @return
	 */
	public static IContainerArea createBlockContainer(IContainerArea area, boolean isFirst, boolean isLast)
	{
		ContainerArea containerArea = (ContainerArea)area.copyArea();
		
		IStyle style = containerArea.getStyle();
		//remove top padding, border and margin if it is not the first child
		if(!isFirst)
		{
			style.setProperty(IStyle.STYLE_BORDER_TOP_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_TOP, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0);
		}
		//remove bottom padding, border and margin if it is not the last child
		if(!isLast)
		{
			style.setProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_BOTTOM, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_BOTTOM, IStyle.NUMBER_0);
		}
		return containerArea;
	}
	
	/**
	 * create lobic container area by content
	 * @param content
	 * @return
	 */
	public static IContainerArea createLogicContainer()
	{
		LogicContainerArea con = new LogicContainerArea();
		return con;
	}
	
	/**
	 * create cell area by cell content
	 * @param cell
	 * @return
	 */
	public static CellArea createCellArea(ICellContent cell)
	{
		CellArea cellArea = new CellArea(cell);
		return cellArea;
	}
	
	/**
	 * create page area by page content
	 * @param pageContent
	 * @return
	 */
	public static IContainerArea createPageArea(IPageContent pageContent)
	{
		PageArea page = new PageArea(pageContent);
		return page;
	}
	
	/**
	 * create table area by table content
	 * @param table
	 * @return
	 */
	public static TableArea createTableArea(ITableContent table)
	{
		TableArea tableArea = new TableArea(table);
		return tableArea;
	}
	
	/**
	 * create row area by row content
	 * @param row
	 * @return
	 */
	public static RowArea createRowArea(IRowContent row)
	{
		RowArea rowArea = new RowArea(row);
		return rowArea;
	}
	
	public static LineArea createLineArea()
	{
		LineArea line = new LineArea();
		return line;
	}

}
