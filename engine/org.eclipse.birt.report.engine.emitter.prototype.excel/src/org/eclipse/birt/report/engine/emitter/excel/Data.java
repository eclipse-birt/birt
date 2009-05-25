/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import org.eclipse.birt.report.engine.emitter.excel.layout.XlsContainer;



public class Data extends SheetData
{

	public Data( )
	{
	}

	public Data( SheetData data )
	{
		this( data.getValue( ), data.getStyle( ), data.getDataType( ), data
				.getContainer( ) );
		this.rowIndex = data.getRowIndex( );
	}
	
	public Data( final Object value, final int datatype, XlsContainer container )
	{
		this( value, null, datatype, container );
	}

	public Data( final Object value, final StyleEntry s, final int datatype,
			XlsContainer container )
	{
		this( value, s, datatype, container, 0 );
	}

	public Data( final Object value, final StyleEntry s, final int datatype,
			XlsContainer container, int rowSpanOfDesign )
	{
		this.value = value;
		this.style = s;
		this.dataType = datatype;
		this.container = container;
		this.rowSpanInDesign = rowSpanOfDesign;
	}

	public Object getValue( )
	{
		return value;
	}
}