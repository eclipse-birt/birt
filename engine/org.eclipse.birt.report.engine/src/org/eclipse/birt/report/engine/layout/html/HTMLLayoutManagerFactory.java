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

package org.eclipse.birt.report.engine.layout.html;

import java.util.LinkedList;

import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;

public class HTMLLayoutManagerFactory
{

	private HTMLReportLayoutEngine engine;

	public HTMLLayoutManagerFactory( HTMLReportLayoutEngine engine )
	{
		this.engine = engine;
	}

	public HTMLReportLayoutEngine getLayoutEngine( )
	{
		return engine;
	}

	public HTMLAbstractLM createLayoutManager( HTMLAbstractLM parent,
			IContent content, IReportItemExecutor executor,
			IContentEmitter emitter )
	{
		HTMLAbstractLM layout = getLayoutManager( content );
		layout.initialize( parent, content, executor, emitter );
		return layout;
	}

	LinkedList freeLeaf = new LinkedList( );
	LinkedList freeBlock = new LinkedList( );
	LinkedList freeTable = new LinkedList( );
	LinkedList freeTableBand = new LinkedList( );
	LinkedList freeRow = new LinkedList( );
	LinkedList freeList = new LinkedList( );
	LinkedList freeGroup = new LinkedList( );
	LinkedList freeListBand = new LinkedList( );

	public void releaseLayoutManager( HTMLAbstractLM manager )
	{
		switch ( manager.getType( ) )
		{
			case HTMLAbstractLM.LAYOUT_MANAGER_LEAF :
				freeLeaf.addLast( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_BLOCK :
				freeBlock.addLast( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_TABLE :
				freeTable.addLast( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_TABLE_BAND :
				freeTableBand.addLast( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_ROW :
				freeRow.addLast( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_LIST :
				freeList.addLast( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_GROUP :
				freeGroup.addLast( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_LIST_BAND :
				freeListBand.addLast( manager );
				break;
		}
	}

	private HTMLAbstractLM getLayoutManager( IContent content )
	{
		HTMLAbstractLM layoutManager = (HTMLAbstractLM) visitor.visit( content,
				null );
		return layoutManager;
	}

	private IContentVisitor visitor = new ContentVisitorAdapter( ) {

		public Object visit( IContent content, Object value )
		{
			return content.accept( this, value );
		}

		public Object visitContent( IContent content, Object value )
		{
			if ( !freeLeaf.isEmpty( ) )
			{
				return (HTMLLeafItemLM) freeLeaf.removeFirst( );
			}
			return new HTMLLeafItemLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitPage( IPageContent page, Object value )
		{
			assert false;
			return null;
		}

		public Object visitContainer( IContainerContent container, Object value )
		{
			if ( !freeBlock.isEmpty( ) )
			{
				return (HTMLBlockStackingLM) freeBlock.removeFirst( );
			}
			return new HTMLBlockStackingLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitTable( ITableContent table, Object value )
		{
			if ( !freeTable.isEmpty( ) )
			{
				return (HTMLTableLM) freeTable.removeFirst( );
			}
			return new HTMLTableLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitTableGroup( ITableGroupContent group, Object value )
		{
			if ( !freeGroup.isEmpty( ) )
			{
				return (HTMLGroupLM) freeGroup.removeFirst( );
			}
			return new HTMLGroupLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitTableBand( ITableBandContent tableBand, Object value )
		{
			if ( !freeTableBand.isEmpty( ) )
			{
				return (HTMLTableBandLM) freeTableBand.removeFirst( );
			}
			return new HTMLTableBandLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitRow( IRowContent row, Object value )
		{
			if ( !freeRow.isEmpty( ) )
			{
				return (HTMLRowLM) freeRow.removeFirst( );
			}
			return new HTMLRowLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitList( IListContent list, Object value )
		{
			if ( !freeList.isEmpty( ) )
			{
				return (HTMLListLM) freeList.removeFirst( );
			}
			return new HTMLListLM( HTMLLayoutManagerFactory.this );

		}

		public Object visitListGroup( IListGroupContent group, Object value )
		{
			if ( !freeGroup.isEmpty( ) )
			{
				return (HTMLGroupLM) freeGroup.removeFirst( );
			}
			return new HTMLGroupLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitListBand( IListBandContent listBand, Object value )
		{
			if ( !freeListBand.isEmpty( ) )
			{
				return (HTMLListingBandLM) freeListBand.removeFirst( );
			}
			return new HTMLListingBandLM( HTMLLayoutManagerFactory.this );
		}
	};
}
