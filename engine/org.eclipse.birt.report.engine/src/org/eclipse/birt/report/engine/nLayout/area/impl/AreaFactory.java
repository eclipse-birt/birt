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

import java.util.HashMap;

import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.layout.pdf.emitter.LayoutEmitterAdapter;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;

/**
 * 
 * area factory. create area object by content or area
 * 
 */
public class AreaFactory
{

	// FIXME should take account of listing and inline style
	protected int nestCount = 0;
	
	protected LayoutEmitterAdapter emitter;

	protected HashMap<Object, AbstractArea> areaCache = new HashMap<Object, AbstractArea>( );
	
	public AreaFactory(LayoutEmitterAdapter emitter)
	{
		this.emitter = emitter;
	}

	public LineArea createLineArea( ContainerArea parent, LayoutContext context )
	{
		LineArea line = new LineArea(parent, context );
		return line;
	}
	
	public ILayout createLayout(ContainerArea parent, LayoutContext context, IContent content)
	{
		switch ( content.getContentType( ) )
		{
			case IContent.DATA_CONTENT :
			case IContent.LABEL_CONTENT :
			case IContent.TEXT_CONTENT :
				if ( PropertyUtil.isInlineElement( content ) )
				{
					DimensionType width = content.getWidth( );
					if ( width != null )
					{
						return new BlockTextArea( parent, context, content );
					}
					else
					{
						return new InlineTextArea( parent, context, content );
					}
				}
				else
				{
					return new BlockTextArea(parent, context, content);
				}


			case IContent.IMAGE_CONTENT :
				return new ImageAreaLayout(parent, context, (IImageContent)content);

			case IContent.AUTOTEXT_CONTENT :
				int type = ( (IAutoTextContent) content ).getType( );
				if ( ( type == IAutoTextContent.TOTAL_PAGE || type == IAutoTextContent.UNFILTERED_TOTAL_PAGE )
						&& "pdf".equalsIgnoreCase( context.getFormat( ) )
						&& ( context.getEngineTaskType( ) == IEngineTask.TASK_RUNANDRENDER || ( !context
								.isReserveDocumentPageNumbers( ) && context
								.getEngineTaskType( ) == IEngineTask.TASK_RENDER ) ) )
				{
					context.addUnresolvedContent( content );
					return new TemplateAreaLayout( parent, context, content );
				}
				else
				{
					if ( PropertyUtil.isInlineElement( content ) )
					{
						return new InlineTextArea( parent, context, content );
					}
					else
					{
						return new BlockTextArea( parent, context, content );
					}
				}
			default :
				return null;
		}

	}

	public AbstractArea createArea( ContainerArea parent,
			LayoutContext context, IContent content )
	{
		AbstractArea area = null;
		if ( nestCount > 0 )
		{
			IStyle inlineStyle = content.getInlineStyle( );
			if ( inlineStyle == null || inlineStyle.isEmpty( ) )
			{
				Object design = content.getGenerateBy( );
				if ( design != null )
				{
					AbstractArea cache = areaCache.get( design );
					if ( cache != null )
					{
						area = cache.cloneArea( );
					}
					else
					{
						area = createNewArea( parent, context, content );
						areaCache.put( design, area );
					}
				}
			}
		}
		if ( area == null )
		{
			area = createNewArea( parent, context, content );
		}
		return area;
	}

	public void startListing( )
	{
		nestCount++;
	}

	public void endListing( )
	{
		nestCount--;
	}
	
	

	protected AbstractArea createNewArea( ContainerArea parent,
			LayoutContext context, IContent content )
	{
		switch ( content.getContentType( ) )
		{
			case IContent.CELL_CONTENT :
				return new CellArea( parent, context, content );
			case IContent.CONTAINER_CONTENT :
				if ( PropertyUtil.isInlineElement( content ) )
				{
					return new InlineContainerArea( parent, context, content );
				}
				else
				{
					return new BlockContainerArea( parent, context, content );
				}
			case IContent.LIST_CONTENT :
				if ( PropertyUtil.isInlineElement( content ) )
				{
					return new ListArea( parent, context, content );
				}
				else
				{
					return new ListArea( parent, context, content );
				}
			case IContent.DATA_CONTENT :
			case IContent.LABEL_CONTENT :
			case IContent.TEXT_CONTENT :
				break;

			case IContent.FOREIGN_CONTENT :
				ContainerArea area;
				if ( PropertyUtil.isInlineElement( content ) )
				{
					area = new InlineContainerArea( parent, context, content );
				}
				else
				{
					area = new BlockContainerArea( parent, context, content );
				}
				if ( context.isFixedLayout( ) )
				{
					area.setPageBreakInside( IStyle.AVOID_VALUE );
				}
				return area;


			case IContent.IMAGE_CONTENT :
				break;

			case IContent.PAGE_CONTENT :
				return new RootArea( context, content, emitter);
				
			case IContent.ROW_CONTENT :
				return new RowArea(parent, context, content);

			case IContent.TABLE_BAND_CONTENT :
				break;

			case IContent.TABLE_CONTENT :
				return new TableArea(parent, context, content);

			case IContent.AUTOTEXT_CONTENT :
				break;

			case IContent.LIST_BAND_CONTENT :
				break;

			case IContent.LIST_GROUP_CONTENT :
				return new ListGroupArea(parent, context, content);
			case IContent.TABLE_GROUP_CONTENT :
				return new TableGroupArea(parent, context, content);
			default :
				break;
		}
		return null;
	}

	protected CellArea createCellArea( ICellContent cellContent,
			ContainerArea parent )
	{
		return new CellArea( );
	}

	public static IImageArea createImageArea( IImageContent image,
			ContainerArea parent )
	{
		return null;
	}

	public static IArea createTableGroupArea( IContent group,
			ContainerArea parent )
	{
		return null;
	}

	public static IArea createTemplateArea( IAutoTextContent autoText,
			ContainerArea parent )
	{
		return null;
	}

}
