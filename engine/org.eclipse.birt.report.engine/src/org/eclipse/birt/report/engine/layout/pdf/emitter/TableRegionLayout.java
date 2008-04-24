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

import org.eclipse.birt.report.engine.content.IContent;

public class TableRegionLayout extends TableLayout
{

	public TableRegionLayout( LayoutEngineContext context,
			 IContent content )
	{
		super( context, null, content );
		// TODO Auto-generated constructor stub
	}

	IContent row;

	
	public void initialize(IContent row, TableLayoutInfo layoutInfo, TableAreaLayout areaLayout)
	{
		this.layoutInfo = layoutInfo;
		this.layout = areaLayout;
		this.row = row;
	}

	protected void initialize( )
	{
		createRoot( );
		root.setWidth( layoutInfo.getTableWidth( ) );
		maxAvaWidth = layoutInfo.getTableWidth( );
	}	
	
	public void layout( )
	{
		initialize( );
		PDFLayoutEmitter emitter = new PDFLayoutEmitter( context );
		emitter.current = this;
		visitContent( row, emitter );
		closeLayout( );
	}

	protected void closeLayout( )
	{
		root.setHeight( getCurrentBP( ) + getOffsetY( ) );
		this.content.setExtension( IContent.LAYOUT_EXTENSION, root );
	}

}
