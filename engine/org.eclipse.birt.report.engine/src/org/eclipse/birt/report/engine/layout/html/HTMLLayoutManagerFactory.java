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
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.util.FastPool;

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

	FastPool freeLeaf = new FastPool( );
	FastPool freeBlock = new FastPool( );
	FastPool freeTable = new FastPool( );
	FastPool freeTableBand = new FastPool( );
	FastPool freeRow = new FastPool( );
	FastPool freeList = new FastPool( );
	FastPool freeGroup = new FastPool( );
	FastPool freeListBand = new FastPool( );

	public void releaseLayoutManager( HTMLAbstractLM manager )
	{
		switch ( manager.getType( ) )
		{
			case HTMLAbstractLM.LAYOUT_MANAGER_LEAF :
				freeLeaf.add( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_BLOCK :
				freeBlock.add( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_TABLE :
				freeTable.add( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_TABLE_BAND :
				freeTableBand.add( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_ROW :
				freeRow.add( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_LIST :
				freeList.add( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_GROUP :
				freeGroup.add( manager );
				break;
			case HTMLAbstractLM.LAYOUT_MANAGER_LIST_BAND :
				freeListBand.add( manager );
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
				return (HTMLLeafItemLM) freeLeaf.remove( );
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
				return (HTMLBlockStackingLM) freeBlock.remove( );
			}
			return new HTMLBlockStackingLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitTable( ITableContent table, Object value )
		{
			if ( !freeTable.isEmpty( ) )
			{
				return (HTMLTableLM) freeTable.remove( );
			}
			return new HTMLTableLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitTableGroup( ITableGroupContent group, Object value )
		{
			if ( !freeGroup.isEmpty( ) )
			{
				return (HTMLGroupLM) freeGroup.remove( );
			}
			return new HTMLGroupLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitTableBand( ITableBandContent tableBand, Object value )
		{
			if ( !freeTableBand.isEmpty( ) )
			{
				return (HTMLTableBandLM) freeTableBand.remove( );
			}
			return new HTMLTableBandLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitRow( IRowContent row, Object value )
		{
			if ( !freeRow.isEmpty( ) )
			{
				return (HTMLRowLM) freeRow.remove( );
			}
			return new HTMLRowLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitList( IListContent list, Object value )
		{
			if ( !freeList.isEmpty( ) )
			{
				return (HTMLListLM) freeList.remove( );
			}
			return new HTMLListLM( HTMLLayoutManagerFactory.this );

		}

		public Object visitListGroup( IListGroupContent group, Object value )
		{
			if ( !freeGroup.isEmpty( ) )
			{
				return (HTMLGroupLM) freeGroup.remove( );
			}
			return new HTMLGroupLM( HTMLLayoutManagerFactory.this );
		}

		public Object visitListBand( IListBandContent listBand, Object value )
		{
			if ( !freeListBand.isEmpty( ) )
			{
				return (HTMLListingBandLM) freeListBand.remove( );
			}
			return new HTMLListingBandLM( HTMLLayoutManagerFactory.this );
		}
	};
}
