/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 * column content object
 * 
 * @version $Revision: 1.2 $ $Date: 2005/11/10 08:55:18 $
 */
public class Column implements IColumn
{
	DimensionType width;
	String styleClass;
	public Column()
	{
	}
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.content.IColumn#getStyle()
	 */
	public IStyle getStyle( )
	{
		return null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.content.IColumn#getWidth()
	 */
	public DimensionType getWidth( )
	{
		return width;
	}
	
	public void setWidth(DimensionType width)
	{
		this.width = width;
	}
	
	public String getStyleClass()
	{
		return styleClass;
	}
	
	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}
}