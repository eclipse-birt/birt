/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.api.timefunction;

public class TimeMember {
	public static final String TIME_LEVEL_TYPE_MONTH = "month"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_QUARTER = "quarter"; //$NON-NLS-1$

	public static final String TIME_LEVEL_TYPE_DAY_OF_YEAR = "day-of-year"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_DAY_OF_MONTH = "day-of-month"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_DAY_OF_WEEK = "day-of-week"; //$NON-NLS-1$

	public static final String TIME_LEVEL_TYPE_HOUR = "hour"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_MINUTE = "minute"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_SECOND = "second"; //$NON-NLS-1$

	// not support
	public static final String TIME_LEVEL_TYPE_WEEK_OF_MONTH = "week-of-month"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_WEEK_OF_YEAR = "week-of-year"; //$NON-NLS-1$
	public static final String TIME_LEVEL_TYPE_YEAR = "year"; //$NON-NLS-1$

	private int[] memberValue;
	private String[] levelType;

	public TimeMember(int[] memberValue, String[] levelType) {
		this.memberValue = memberValue;
		this.levelType = levelType;
	}

	public int[] getMemberValue() {
		return memberValue;
	}

	public String[] getLevelType() {
		return levelType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int h = 0;

		for (int i = 0; i < memberValue.length; i++) {
			if (i == 1) {
				h = 365 * h + memberValue[i];
			} else {
				h = 31 * h + memberValue[i];
			}
		}
		return h;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object anObject) {
		int[] oMemberValue = ((TimeMember) anObject).memberValue;
		for (int i = 0; i < memberValue.length; i++) {
			if (oMemberValue[i] != memberValue[i]) {
				return false;
			}
		}
		return true;
	}
}
