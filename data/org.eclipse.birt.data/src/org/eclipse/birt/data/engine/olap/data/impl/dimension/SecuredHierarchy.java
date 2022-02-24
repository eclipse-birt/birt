
/*******************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Arrays;
import java.util.Set;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;

import com.ibm.icu.util.Calendar;

/**
 * 
 */

public class SecuredHierarchy extends Hierarchy {
	private Set<String> notAccessibleLevels;
	private long nullTime;

	public SecuredHierarchy(IDocumentManager documentManager, String dimensionName, String hierarchyName,
			Set<String> notAccessibleLevels) {
		super(documentManager, dimensionName, hierarchyName);
		this.notAccessibleLevels = notAccessibleLevels;
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(0, 0, 1, 0, 0, 0);
		this.nullTime = calendar.getTimeInMillis();
	}

	protected Member[] populateLevelMembers() throws IOException {
		Member[] levelMembers = new Member[levels.length];
		for (int i = 0; i < levelMembers.length; i++) {
			levelMembers[i] = readLevelMember(levels[i]);
			if (notAccessibleLevels.contains(levels[i].getName())) {
				if (levelMembers[i].getKeyValues() != null) {
					for (int j = 0; j < levelMembers[i].getKeyValues().length; j++) {
						levelMembers[i].getKeyValues()[j] = this
								.createNullValueReplacer(levelMembers[i].getKeyValues()[j]);
					}
				}
				if (levelMembers[i].getAttributes() != null)
					Arrays.fill(levelMembers[i].getAttributes(), null);
			}

		}

		return levelMembers;
	}

	/**
	 * 
	 * @param fieldType
	 * @return
	 */
	private Object createNullValueReplacer(Object o) {
		switch (DataTypeUtil.toApiDataType(o.getClass())) {
		case DataType.DATE_TYPE:
			return new java.util.Date(nullTime);
		case DataType.SQL_DATE_TYPE:
			return new java.sql.Date(nullTime);
		case DataType.SQL_TIME_TYPE:
			return new Time(nullTime);
		case DataType.BOOLEAN_TYPE:
			return Boolean.FALSE;
		case DataType.DECIMAL_TYPE:
			return new BigDecimal(0);
		case DataType.DOUBLE_TYPE:
			return new Double(0);
		case DataType.INTEGER_TYPE:
			return Integer.valueOf(0);
		case DataType.STRING_TYPE:
			return "";
		default:
			return "";
		}
	}
}
