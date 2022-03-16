/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.TimeDimensionUtil;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionRow;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;

/**
 *
 */

public class DimensionRowAccessor extends AbstractRowAccessor {

	protected IDimension dimension;
	protected DimensionRow dimRow;

	/**
	 *
	 * @param dimension
	 */
	public DimensionRowAccessor(IDimension dimension) {
		this.dimension = dimension;
		if (!dimension.isTime()) {
			populateFieldIndexMap();
		}
	}

	public List getLevelNames() {
		List levelName = new ArrayList();
		ILevel[] levels = dimension.getHierarchy().getLevels();
		for (int i = 0; i < levels.length; i++) {
			levelName.add(levels[i].getName());
		}
		return levelName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.
	 * AbstractRowAccessor#populateFieldIndexMap()
	 */
	@Override
	protected void populateFieldIndexMap() {
		fieldIndexMap = new HashMap();
		ILevel[] levels = dimension.getHierarchy().getLevels();
		for (int i = 0; i < levels.length; i++) {
			String[] keyNames = levels[i].getKeyNames();
			if (keyNames != null) {
				for (int j = 0; j < keyNames.length; j++) {
					String name = OlapExpressionUtil.getAttrReference(dimension.getName(), levels[i].getName(),
							keyNames[j]);
					fieldIndexMap.put(name, new DimensionKeyIndex(i, j));
				}
			}

			String[] attrNames = levels[i].getAttributeNames();
			if (attrNames != null) {
				for (int j = 0; j < attrNames.length; j++) {
					String attrName = parseAttributeName(attrNames[j]);
					String name = OlapExpressionUtil.getAttrReference(dimension.getName(), levels[i].getName(),
							attrName);
					fieldIndexMap.put(name, new DimensionAttrIndex(i, j));
				}
			}
		}

	}

	/**
	 *
	 * @param position
	 * @throws IOException
	 */
	public void seek(int position) throws IOException {
		dimRow = ((Dimension) dimension).getRowByPosition(position);
	}

	/**
	 *
	 * @return
	 */
	public DimensionRow getCurrentRow() {
		return dimRow;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getAggrValue(java.
	 * lang.String)
	 */
	@Override
	public Object getAggrValue(String aggrName) throws DataException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getFieldValue(java.
	 * lang.String)
	 */
	@Override
	public Object getFieldValue(String fieldName) throws DataException {
		if (dimRow == null) {
			throw new DataException(ResourceConstants.CANNOT_ACCESS_NULL_DIMENSION_ROW);
		}
		if (!dimension.isTime()) {
			FieldIndex index = (FieldIndex) fieldIndexMap.get(fieldName);
			return index != null ? index.getValue() : null;
		} else {
			return TimeDimensionUtil.getFieldVaule((Date) (dimRow.getMembers()[0].getKeyValues()[0]), fieldName);
		}
	}

	@Override
	public boolean isTimeDimensionRow() {
		return dimension.isTime();
	}

	/**
	 *
	 */
	class DimensionKeyIndex extends KeyIndex {

		/**
		 *
		 * @param levelIndex
		 * @param keyIndex
		 */
		DimensionKeyIndex(int levelIndex, int keyIndex) {
			super(levelIndex, keyIndex);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.
		 * AbstractRowAccessor.FieldIndex#getValue()
		 */
		@Override
		Object getValue() throws DataException {
			return dimRow.getMembers()[levelIndex].getKeyValues()[keyIndex];
		}
	}

	/**
	 *
	 */
	class DimensionAttrIndex extends AttributeIndex {

		/**
		 *
		 * @param levelIndex
		 * @param keyIndex
		 */
		DimensionAttrIndex(int levelIndex, int keyIndex) {
			super(levelIndex, keyIndex);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.
		 * AbstractRowAccessor.FieldIndex#getValue()
		 */
		@Override
		Object getValue() throws DataException {
			return dimRow.getMembers()[levelIndex].getAttributes()[attrIndex];
		}
	}

}
