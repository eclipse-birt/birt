/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.document.util;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.viewing.DataSetResultSet;

public class EmptyExprResultSet implements IExprResultSet {

	public boolean next() throws DataException {
		return false;
	}

	public Object getValue(String name) throws DataException {
		return null;
	}

	public void moveTo(int rowIndex) throws DataException {
	}

	public int getCurrentId() {
		return 0;
	}

	public int getCurrentIndex() {
		return 0;
	}

	public int getStartingGroupLevel() throws DataException {
		return 0;
	}

	public int getEndingGroupLevel() throws DataException {
		return 0;
	}

	public void skipToEnd(int groupLevel) throws DataException {
	}

	public void close() throws DataException {
	}

	public boolean isEmpty() {
		return true;
	}

	public int[] getGroupStartAndEndIndex(int groupIndex) throws DataException {
		return null;
	}

	public DataSetResultSet getDataSetResultSet() {
		return null;
	}

	public List[] getGroupInfos() throws DataException {
		return null;
	}

}
