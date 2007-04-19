/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt;

import java.io.IOException;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
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
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;

/**
 * The class passes the report content object to PPT output stream.
 */
public class PPTEmitter implements IContentEmitter
{

	/** The Logger instance is used to log messages. */
	protected static Logger logger = Logger.getLogger( PPTEmitter.class.getName( ) );

	/** The PPT render */
	protected final PPTRender render = new PPTRender( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#getOutputFormat()
	 */
	public String getOutputFormat( )
	{
		return render.getOutputFormat( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#initialize(org.eclipse.birt.report.engine.emitter.IEmitterServices)
	 */
	public void initialize( IEmitterServices service )
	{
		render.initialize( service );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startPage(org.eclipse.birt.report.engine.content.IPageContent)
	 */
	public void startPage( IPageContent page )
	{
		PageArea pageArea = (PageArea) page.getExtension( IContent.LAYOUT_EXTENSION );

		if ( pageArea != null )
		{
			pageArea.accept( render );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#start(org.eclipse.birt.report.engine.content.IReportContent)
	 */
	public void start( IReportContent report )
	{
		render.start( report );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#end(org.eclipse.birt.report.engine.content.IReportContent)
	 */
	public void end( IReportContent report )
	{
		render.end( report );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startAutoText(org.eclipse.birt.report.engine.content.IAutoTextContent)
	 */
	public void startAutoText( IAutoTextContent autoText )
	{
		ITextArea totalPage = (ITextArea) autoText.getExtension( IContent.LAYOUT_EXTENSION );

		render.setTotalPage( totalPage );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endCell(org.eclipse.birt.report.engine.content.ICellContent)
	 */
	public void endCell( ICellContent cell )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endContainer(org.eclipse.birt.report.engine.content.IContainerContent)
	 */
	public void endContainer( IContainerContent container )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endContent(org.eclipse.birt.report.engine.content.IContent)
	 */
	public void endContent( IContent content )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endList(org.eclipse.birt.report.engine.content.IListContent)
	 */
	public void endList( IListContent list )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endListBand(org.eclipse.birt.report.engine.content.IListBandContent)
	 */
	public void endListBand( IListBandContent listBand )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endPage(org.eclipse.birt.report.engine.content.IPageContent)
	 */
	public void endPage( IPageContent page )
	{
		if ( render != null )
		{
			try
			{
				render.endPage( );
			}
			catch ( IOException e )
			{
				e.printStackTrace( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endRow(org.eclipse.birt.report.engine.content.IRowContent)
	 */
	public void endRow( IRowContent row )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endTable(org.eclipse.birt.report.engine.content.ITableContent)
	 */
	public void endTable( ITableContent table )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startCell(org.eclipse.birt.report.engine.content.ICellContent)
	 */
	public void startCell( ICellContent cell )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startContainer(org.eclipse.birt.report.engine.content.IContainerContent)
	 */
	public void startContainer( IContainerContent container )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startContent(org.eclipse.birt.report.engine.content.IContent)
	 */
	public void startContent( IContent content )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startData(org.eclipse.birt.report.engine.content.IDataContent)
	 */
	public void startData( IDataContent data )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startForeign(org.eclipse.birt.report.engine.content.IForeignContent)
	 */
	public void startForeign( IForeignContent foreign )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startImage(org.eclipse.birt.report.engine.content.IImageContent)
	 */
	public void startImage( IImageContent image )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startLabel(org.eclipse.birt.report.engine.content.ILabelContent)
	 */
	public void startLabel( ILabelContent label )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startList(org.eclipse.birt.report.engine.content.IListContent)
	 */
	public void startList( IListContent list )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startListBand(org.eclipse.birt.report.engine.content.IListBandContent)
	 */
	public void startListBand( IListBandContent listBand )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startRow(org.eclipse.birt.report.engine.content.IRowContent)
	 */
	public void startRow( IRowContent row )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startTable(org.eclipse.birt.report.engine.content.ITableContent)
	 */
	public void startTable( ITableContent table )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startText(org.eclipse.birt.report.engine.content.ITextContent)
	 */
	public void startText( ITextContent text )
	{
		return;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endGroup(org.eclipse.birt.report.engine.content.IGroupContent)
	 */
	public void endGroup( IGroupContent group )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endListGroup(org.eclipse.birt.report.engine.content.IListGroupContent)
	 */
	public void endListGroup( IListGroupContent group )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endTableBand(org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void endTableBand( ITableBandContent band )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endTableGroup(org.eclipse.birt.report.engine.content.ITableGroupContent)
	 */
	public void endTableGroup( ITableGroupContent group )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startGroup(org.eclipse.birt.report.engine.content.IGroupContent)
	 */
	public void startGroup( IGroupContent group )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startListGroup(org.eclipse.birt.report.engine.content.IListGroupContent)
	 */
	public void startListGroup( IListGroupContent group )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startTableBand(org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void startTableBand( ITableBandContent band )
	{
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startTableGroup(org.eclipse.birt.report.engine.content.ITableGroupContent)
	 */
	public void startTableGroup( ITableGroupContent group )
	{
		return;
	}
}
