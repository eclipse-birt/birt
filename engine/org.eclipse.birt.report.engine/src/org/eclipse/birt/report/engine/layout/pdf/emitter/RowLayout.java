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
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.pdf.PDFAbstractLM;
import org.eclipse.birt.report.engine.layout.pdf.PDFLayoutEngineContext;
import org.eclipse.birt.report.engine.layout.pdf.PDFStackingLM;
import org.eclipse.birt.report.engine.layout.pdf.PDFTableLM;
import org.eclipse.birt.report.model.api.ReportDesignHandle;


public class RowLayout extends ContainerLayout
{
	protected TableLayout tbl;

	public RowLayout( LayoutEngineContext context,
			ContainerLayout parentContext, IContent content )
	{
		super( context, parentContext, content );
	}

	protected void createRoot( )
	{
		root = AreaFactory.createRowArea( (IRowContent) content );
	}

	protected void initialize( )
	{
		tbl = getTableLayoutManager( );
		calculateSpecifiedHeight( );
		createRoot( );
		maxAvaWidth = parent.getCurrentMaxContentWidth( );
		root.setWidth( getCurrentMaxContentWidth( ) );
		root.setAllocatedHeight( parent.getCurrentMaxContentHeight( ) );
		maxAvaHeight = root.getContentHeight( );
	}


	protected void closeLayout( )
	{
		if ( root != null )
		{
			tbl.updateRow( (RowArea) root, specifiedHeight);
			tbl.addRow( (RowArea)root );
			parent.addArea( root );
		}
	}
	

	public boolean addArea( AbstractArea area )
	{
		CellArea cArea = (CellArea) area;
		root.addChild( area );

		int columnID = cArea.getColumnID( );
		int colSpan = cArea.getColSpan( );
		// Retrieve direction from the top-level content.
		if ( colSpan > 1 && content.isRTL( ) )
		{
			columnID += colSpan - 1;
		}

		cArea.setPosition( tbl.getXPos( columnID ), 0 );
		//tbl.addRow( (RowArea)root );
		return true;
	}


}
