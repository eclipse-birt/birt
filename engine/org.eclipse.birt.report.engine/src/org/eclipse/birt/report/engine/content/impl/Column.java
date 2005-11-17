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

import java.io.Serializable;

import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 * column content object
 * 
 * @version $Revision: 1.1 $ $Date: 2005/11/11 06:26:46 $
 */
public class Column implements IColumn, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 591656342008905721L;

	protected DimensionType width;

	protected String styleClass;

	/**
	 * constructor use by serialize and deserialize
	 */
	public Column( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IColumn#getStyle()
	 */
	public IStyle getStyle( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IColumn#getWidth()
	 */
	public DimensionType getWidth( )
	{
		return width;
	}

	public void setWidth( DimensionType width )
	{
		this.width = width;
	}

	public String getStyleClass( )
	{
		return styleClass;
	}

	public void setStyleClass( String styleClass )
	{
		this.styleClass = styleClass;
	}
}