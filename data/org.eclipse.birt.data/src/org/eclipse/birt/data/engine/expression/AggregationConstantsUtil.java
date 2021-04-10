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
package org.eclipse.birt.data.engine.expression;

import org.mozilla.javascript.Node;
import org.mozilla.javascript.Token;

/**
 * This class is a utility class which is used to pass total constants.
 */
public class AggregationConstantsUtil {
	//
	private static String NO_FILTER = "NO_FILTER";
	private static String CURRENT_GROUP = "CURRENT_GROUP";
	private static String OVERALL = "OVERALL";
	private static String TOTAL = "TOTAL";

	/**
	 * Return the Constant Expression referred by the total constants.
	 * 
	 * @param child
	 * @return
	 */
	public static ConstantExpression getConstantExpression(Node child) {
		if (child.getFirstChild().getType() == Token.NAME && child.getFirstChild().getString().equalsIgnoreCase(TOTAL)
				&& child.getLastChild().getType() == Token.STRING) {
			String property = child.getLastChild().getString();
			if (CURRENT_GROUP.equalsIgnoreCase(property) || OVERALL.equalsIgnoreCase(property))
				return new ConstantExpression(property.toUpperCase());
			if (NO_FILTER.equalsIgnoreCase(property))
				return new ConstantExpression();
		}
		return null;
	}

	/**
	 * Return the group level the constant stand for.
	 * 
	 * @param constant
	 * @param currentGroupLevel
	 * @param innerMostGroupLevel
	 * @return
	 */
	public static int getGroupLevel(String constant, int currentGroupLevel, int innerMostGroupLevel,
			boolean isDetailedRow) {
		int result = -1;
		if (OVERALL.equalsIgnoreCase((String) constant)) {
			result = 0;
		} else if (CURRENT_GROUP.equalsIgnoreCase((String) constant)) {
			//
			if (currentGroupLevel == 0 && isDetailedRow) {
				result = innerMostGroupLevel;
			} else {
				result = currentGroupLevel;
			}
		}
		return result;
	}

}
