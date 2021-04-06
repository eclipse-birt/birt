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
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * Util for row ID
 */
public class RowIDUtil {
	public final static int MODE_NORMAL = 1;
	public final static int MODE_IV = 2;

	private int mode = -1;
	private int rowIDPos;

	/**
	 * 
	 * @param ri
	 * @return
	 * @throws DataException
	 */
	public int getMode(IResultIterator ri) throws DataException {
		if (mode == -1)
			init(ri);
		return mode;
	}

	/**
	 * @param ri
	 * @return
	 * @throws DataException
	 *//*
		 * public int getRowID( IResultIterator ri, int currIndex ) throws DataException
		 * {
		 * 
		 * 
		 * if ( mode == MODE_NORMAL ) { return currIndex; } else { IResultObject ob =
		 * ri.getCurrentResult( ); if ( ob == null ) return -1; else return ( (Integer)
		 * ob.getFieldValue( rowIDPos ) ).intValue( ); } }
		 */

	/**
	 * @throws DataException
	 */
	private void init(IResultIterator ri) throws DataException {
		rowIDPos = ri.getResultClass().getFieldCount();
		if (rowIDPos > 0 && ri.getResultClass().getFieldName(rowIDPos).equals(ExprMetaUtil.POS_NAME)) {
			mode = MODE_IV;
		} else {
			mode = MODE_NORMAL;
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getRowIdPos() {
		return this.rowIDPos;
	}
}
