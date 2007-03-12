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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;

/**
 * The default cell adapter
 */
public class NormalCrosstabCellAdapter extends CrosstabCellAdapter
{

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public NormalCrosstabCellAdapter( CrosstabCellHandle handle )
	{
		super( handle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode( )
	{
		return getCrosstabItemHandle( ).hashCode( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj )
	{
		if ( obj == getCrosstabItemHandle( ) )
		{
			return true;
		}
		if ( obj instanceof CrosstabHandleAdapter )
		{
			return getCrosstabItemHandle( ) == ( (CrosstabHandleAdapter) obj ).getCrosstabItemHandle( );
		}
		return super.equals( obj );
	}
}
