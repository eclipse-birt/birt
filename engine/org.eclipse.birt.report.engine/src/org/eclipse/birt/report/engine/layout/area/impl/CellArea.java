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

import org.eclipse.birt.report.engine.content.ICellContent;

public class CellArea extends ContainerArea
{
	protected int rowSpan = -1;
	
	CellArea(ICellContent cell)
	{
		super(cell);
		//remove all border
		removeBorder();
	}

	public int getColumnID()
	{
		if(content!=null)
		{
			return ((ICellContent)content).getColumn();
		}
		return 0;
	}
	
	public int getRowID()
	{
		if(content!=null)
		{
			return ((ICellContent)content).getRow();
		}
		return 0;
	}
	
	public int getColSpan()
	{
		if(content!=null)
		{
			return ((ICellContent)content).getColSpan();
		}
		return 1;
	}
	
	public int getRowSpan()
	{
		if(content!=null)
		{
			return ((ICellContent)content).getRowSpan();
		}
		return 1;
	}

}
