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

import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.report.model.api.SortKeyHandle;

/**
 * Definition of a sort condition, which comprises of a sort key expression and 
 * a sort direction based on that key
 */
public class SortAdapter extends SortDefinition
{
	/**
	 * Creates a new sort based on the provided key and direction
	 * Direction contains a String value defined in Model
	 */
	public SortAdapter( String keyExpression, String direction )
	{
		this.setExpression( keyExpression );
		this.setSortDirection( sortDirectionFromModel(direction) );
	}
	
	/**
	 * Creates a new sort based on model sort key definition
	 */
	public SortAdapter( SortKeyHandle keyHandle )
	{
		this( keyHandle.getKey(), 
			  keyHandle.getDirection() );
		this.setSortStrength( keyHandle.getStrength( ) );
		if( keyHandle.getLocale( )!= null )
			this.setSortLocale( keyHandle.getLocale( ) );
	}
	
	/**
	 * Converts a model sort direction string to equivalent enumeration 
	 * constant
	 */
	public static int sortDirectionFromModel( String modelDirectionStr )
	{
		if ( "asc".equals( modelDirectionStr ) ) //$NON-NLS-1$
			return IGroupDefinition.SORT_ASC;
		if ( "desc".equals( modelDirectionStr ) ) //$NON-NLS-1$
			return IGroupDefinition.SORT_DESC;

		return IGroupDefinition.SORT_ASC;
	}
}
