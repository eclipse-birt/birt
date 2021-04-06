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

package org.eclipse.birt.report.engine.internal.util;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.model.api.FilterConditionHandle;

public class HTMLUtil {
	/**
	 * Generates descrition text for a filter condition.
	 * 
	 * @param filterCondition the filter condition.
	 * @return description text.
	 */
	public static String getFilterDescription(FilterConditionHandle filterCondition) {
		if (filterCondition == null) {
			return null;
		}
		char space = ' ';
		StringBuffer result = new StringBuffer();
		result.append(filterCondition.getExpr());
		result.append(space);
		result.append(filterCondition.getOperator());
		String operator1 = filterCondition.getValue1();
		String operator2 = filterCondition.getValue2();
		if (operator1 != null) {
			result.append(space);
			result.append(operator1);
		}
		if (operator2 != null) {
			if (operator1 != null) {
				result.append(" or "); //$NON-NLS-1$
			}
			result.append(space);
			result.append(operator2);
		}
		return result.toString();
	}

	/**
	 * Gets group level of a cell content.
	 * 
	 * @param cellContent the cell content.
	 * @return group level of the cell content.
	 */
	public static int getGroupLevel(ICellContent cellContent) {
		IRowContent row = (IRowContent) cellContent.getParent();
		return getGroupLevel(row);
	}

	/**
	 * Gets group level of a row content.
	 * 
	 * @param rowContent the row content
	 * @return group level of the row contnet.
	 */
	public static int getGroupLevel(IRowContent rowContent) {
		IGroupContent group = rowContent.getGroup();
		IBandContent band = rowContent.getBand();
		if (group != null && band != null) {
			int bandType = band.getBandType();
			if (bandType == IBandContent.BAND_DETAIL) {
				return group.getGroupLevel() + 2;
			} else if (bandType == IBandContent.BAND_GROUP_HEADER || bandType == IBandContent.BAND_GROUP_FOOTER) {
				return group.getGroupLevel() + 1;
			}
		}
		return -1;
	}
}
