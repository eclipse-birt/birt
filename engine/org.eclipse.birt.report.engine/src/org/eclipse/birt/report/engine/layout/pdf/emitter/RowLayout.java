/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.pdf.emitter.TableAreaLayout.Row;
import org.eclipse.birt.report.engine.layout.pdf.emitter.TableLayout.TableContext;


public class RowLayout extends ContainerLayout
{
	protected TableLayout tbl;
	
	protected Row unresolvedRow = null;

	public RowLayout( LayoutEngineContext context,
			ContainerLayout parent, IContent content )
	{
		super( context, parent, content );
		tbl = getTableLayoutManager( );
	}

	protected void createRoot( )
	{
		currentContext.root = AreaFactory.createRowArea( (IRowContent) content );
	}

	protected void initialize( )
	{
		currentContext = new ContainerContext( );
		contextList.add( currentContext );
		calculateSpecifiedHeight( );
		createRoot( );
		currentContext.maxAvaWidth = parent.getCurrentMaxContentWidth( );
		currentContext.root.setWidth( getCurrentMaxContentWidth( ) );
		currentContext.root.setAllocatedHeight( parent.getCurrentMaxContentHeight( ) );
		currentContext.maxAvaHeight = currentContext.root.getContentHeight( );
	}

	protected void closeLayout( )
	{
		super.closeLayout();
		parent.gotoLastPage();
	}
	
	protected void closeLayout( ContainerContext currentContext, int index, boolean finished )
	{
		if ( currentContext.root != null )
		{
			boolean added = false;
			if ( unresolvedRow != null )
			{
				TableContext tc = (TableContext) ( tbl.contextList.get( index ) );
				tc.layout.setUnresolvedRow( unresolvedRow );
			}
			tbl.updateRow( (RowArea) currentContext.root, specifiedHeight,
					index );
			if ( finished || !isRowEmpty( currentContext ) )
			{
				tbl.addRow( (RowArea) currentContext.root, index );
				parent.addToRoot( currentContext.root, index );
				added = true;
			}
			if ( !finished && unresolvedRow == null )
			{
				
				TableContext tc = (TableContext) ( tbl.contextList.get( index ) );
				if(added)
				{
					unresolvedRow = tc.layout.getUnresolvedRow( );
				}
				else
				{
					unresolvedRow = tc.layout.getUnresolvedRow( (RowArea) currentContext.root);
				}
			}
		}
	}
	
	protected boolean isRowEmpty( ContainerContext currentContext )
	{
		Iterator iter = currentContext.root.getChildren( );
		while ( iter.hasNext( ) )
		{
			ContainerArea area = (ContainerArea) iter.next( );
			if ( area.getChildrenCount( ) > 0 )
			{
				return false;
			}
		}
		return true;
	}
	

	protected void addToRoot( AbstractArea area )
	{
		CellArea cArea = (CellArea) area;
		currentContext.root.addChild( area );

		int columnID = cArea.getColumnID( );
		int colSpan = cArea.getColSpan( );
		// Retrieve direction from the top-level content.
		if ( colSpan > 1 && content.isRTL( ) )
		{
			columnID += colSpan - 1;
		}

		cArea.setPosition( tbl.getXPos( columnID ), 0 );
	}


}
