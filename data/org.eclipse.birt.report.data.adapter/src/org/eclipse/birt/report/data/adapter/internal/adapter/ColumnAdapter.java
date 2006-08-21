/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.report.data.adapter.internal.adapter;

import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.report.data.adapter.impl.ModelAdapter;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;

/**
 * Adapts a Model Column definition
 */
public class ColumnAdapter extends ColumnDefinition
{
	/**
	 * Adapts a column from Model ResultSetColumnHandle
	 */
	public ColumnAdapter( ResultSetColumnHandle modelColumn )
	{
		super( modelColumn.getColumnName( ) );
		if ( modelColumn.getPosition( ) != null )
			setColumnPosition( modelColumn.getPosition( ).intValue( ) );
		if( modelColumn.getNativeDataType( )!= null )
			setNativeDataType( modelColumn.getNativeDataType( ).intValue( ) );			
		setDataType( ModelAdapter.adaptModelDataType( modelColumn.getDataType( ) ) );
	}

	/**
	 * Adapts a column from Model ColumnHintHandle
	 */
	public ColumnAdapter( ColumnHintHandle modelColumnHint )
	{
		super( modelColumnHint.getColumnName( ) );
		DataAdapterUtil.updateColumnDefn( this, modelColumnHint );
	}
}
