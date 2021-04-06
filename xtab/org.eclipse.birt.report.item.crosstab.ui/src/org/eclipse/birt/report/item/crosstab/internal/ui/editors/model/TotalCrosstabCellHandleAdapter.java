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
 * Total cell adapter ,include sub total and grand total
 */
public class TotalCrosstabCellHandleAdapter extends CrosstabCellAdapter {

	// TODO mat be add a type, dub total or grand total
	public static final int SUB_TOTAL = 0;
	public static final int GRAND_TOTAL = 1;

	public static final int NO_TYPE = -1;

	private int type = NO_TYPE;

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public TotalCrosstabCellHandleAdapter(CrosstabCellHandle handle) {
		this(handle, NO_TYPE);
	}

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param type
	 */
	public TotalCrosstabCellHandleAdapter(CrosstabCellHandle handle, int type) {
		super(handle);
		this.type = type;
	}

	/**
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getCrosstabItemHandle().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
//		if ( obj == getCrosstabItemHandle( ) )
//		{
//			return true;
//		}
//		if ( obj instanceof CrosstabHandleAdapter )
//		{
//			return getCrosstabItemHandle( ) == ( (CrosstabHandleAdapter) obj ).getCrosstabItemHandle( );
//		}
		return super.equals(obj);
	}
}
