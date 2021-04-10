/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;

/**
 * 
 */

public abstract class AbstractRowAccessor implements IResultRow {

	protected Map fieldIndexMap = new HashMap();

	/**
	 * parse the complex attribute name to get the original name since it's composed
	 * of level name and original attribute name.
	 * 
	 * @param attrName
	 * @return
	 */
	protected String parseAttributeName(String attrName) {
		final int index = attrName.indexOf("/");//$NON-NLS-1$
		return index > 0 ? attrName.substring(index + 1) : attrName;
	}

	/**
	 * populate the field indices to the fieldIndexMap, which should be called only
	 * one time previous to the access operations.
	 */
	protected abstract void populateFieldIndexMap();

	/**
	 * 
	 */
	abstract class FieldIndex {

		int levelIndex;

		/**
		 * get the value of current index.
		 * 
		 * @return
		 * @throws DataException
		 */
		abstract Object getValue() throws DataException;
	}

	/**
	 * 
	 */
	abstract class KeyIndex extends FieldIndex {

		int keyIndex;

		/**
		 * 
		 * @param levelIndex
		 * @param keyIndex
		 */
		KeyIndex(int levelIndex, int keyIndex) {
			this.levelIndex = levelIndex;
			this.keyIndex = keyIndex;
		}
	}

	/**
	 * 
	 */
	abstract class AttributeIndex extends FieldIndex {

		int attrIndex;

		/**
		 * 
		 * @param levelIndex
		 * @param keyIndex
		 */
		AttributeIndex(int levelIndex, int keyIndex) {
			this.levelIndex = levelIndex;
			this.attrIndex = keyIndex;
		}
	}
}
