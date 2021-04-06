/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.executor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;

/**
 * Help to manage Clob and Blob information of ResultClass.
 */
class ResultClassHelper {
	private IResultClass resultClass;

	private boolean hasClobOrBlob = false;
	private int[] clobIndex = null;
	private int[] blobIndex = null;

	/**
	 * @param resultClass
	 * @throws DataException
	 */
	ResultClassHelper(IResultClass resultClass) throws DataException {
		this.resultClass = resultClass;
		initIndex();
	}

	/**
	 * 
	 * @throws DataException
	 */
	private void initIndex() throws DataException {
		ArrayList clobIndexList = new ArrayList();
		ArrayList blobIndexList = new ArrayList();
		for (int i = 0; i < resultClass.getFieldCount(); i++) {
			Class valueClass = resultClass.getFieldValueClass(i + 1);
			assert valueClass != null;
			if (valueClass.isAssignableFrom(IClob.class))
				clobIndexList.add(Integer.valueOf(i));
			else if (valueClass.isAssignableFrom(IBlob.class))
				blobIndexList.add(Integer.valueOf(i));
			;
		}
		clobIndex = toIntArray(clobIndexList);
		blobIndex = toIntArray(blobIndexList);
		hasClobOrBlob = (clobIndex.length > 0) || (blobIndex.length > 0);
	}

	/**
	 * 
	 * @param integerList
	 * @return
	 */
	private int[] toIntArray(List integerList) {
		int[] reArray = new int[integerList.size()];
		for (int i = 0; i < reArray.length; i++) {
			reArray[i] = ((Integer) (integerList.get(i))).intValue();
		}
		return reArray;
	}

	/**
	 * 
	 * @return
	 */
	boolean hasClobOrBlob() {
		return hasClobOrBlob;
	}

	/**
	 * 
	 * @return
	 */
	int[] getClobIndexArray() {
		return clobIndex;
	}

	/**
	 * 
	 * @return
	 */
	int[] getBlobIndexArray() {
		return blobIndex;
	}

}
