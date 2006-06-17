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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.dom.AreaStyle;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;


public class TableArea extends ContainerArea
{
	protected ArrayList header = new ArrayList();
	TableArea(ITableContent table)
	{
		super(table);
		removeBorder();
		removePadding();

	}

	public List getTableHeader()
	{
		return header;
	}
	
	public void addHeaderArea(RowArea row)
	{
		header.add(row);
	}
	
	public void setHeader(ArrayList list)
	{
		header.addAll(list);
	}
	
	
	
	public IContainerArea copyArea()
	{
		TableArea area =  new TableArea((ITableContent)content);
		area.style = new AreaStyle((AreaStyle)style);
		copyPropertyTo(area);
		return area;
	}

	protected void copyPropertyTo(TableArea area)
	{
		super.copyPropertyTo(area);
		area.setHeader(header);
	}

}
