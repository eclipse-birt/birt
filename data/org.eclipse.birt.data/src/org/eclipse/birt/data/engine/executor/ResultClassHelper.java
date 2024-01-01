/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
		ArrayList<Integer> clobIndexList = new ArrayList<>();
		ArrayList<Integer> blobIndexList = new ArrayList<>();
		for (int i = 0; i < resultClass.getFieldCount(); i++) {
			Class valueClass = resultClass.getFieldValueClass(i + 1);
			assert valueClass != null;
			if (valueClass.isAssignableFrom(IClob.class)) {
				clobIndexList.add(Integer.valueOf(i));
			} else if (valueClass.isAssignableFrom(IBlob.class)) {
				blobIndexList.add(Integer.valueOf(i));
			}

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
	private int[] toIntArray(List<Integer> integerList) {
		int[] reArray = new int[integerList.size()];
		for (int i = 0; i < reArray.length; i++) {
			reArray[i] = integerList.get(i);
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
