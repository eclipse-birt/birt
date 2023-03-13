/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;

import com.ibm.icu.util.Calendar;

/**
 * Utility class to mirror the DateTime level
 *
 */
class TimeMemberUtil {

	private static final String DATE_TIME_LEVEL_TYPE_MONTH = "month"; //$NON-NLS-1$
	private static final String DATE_TIME_LEVEL_TYPE_QUARTER = "quarter"; //$NON-NLS-1$

	private static final String DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR = "day-of-year"; //$NON-NLS-1$
	private static final String DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH = "day-of-month"; //$NON-NLS-1$
	private static final String DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK = "day-of-week"; //$NON-NLS-1$

	private static final String DATE_TIME_LEVEL_TYPE_HOUR = "hour"; //$NON-NLS-1$
	private static final String DATE_TIME_LEVEL_TYPE_MINUTE = "minute"; //$NON-NLS-1$
	private static final String DATE_TIME_LEVEL_TYPE_SECOND = "second"; //$NON-NLS-1$

	// not support
	private static final String DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH = "week-of-month"; //$NON-NLS-1$
	private static final String DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR = "week-of-year"; //$NON-NLS-1$
	private static final String DATE_TIME_LEVEL_TYPE_YEAR = "year"; //$NON-NLS-1$

	private static Calendar calendar;

	public static MemberTreeNode[] getDateTimeNodes(DimLevel[] dimLevels, Object dateTimeValue, int index,
			MirrorMetaInfo service) {
		String[] dateTypes = new String[dimLevels.length - index];
		for (int i = index; i < dimLevels.length; i++) {
			dateTypes[i - index] = service.getLevelType(dimLevels[i]);
		}

		calendar = Calendar.getInstance(service.getSession().getEngineContext().getLocale());
		calendar.setTimeZone(service.getSession().getEngineContext().getTimeZone());
		calendar.clear();

		MemberTreeNode[] secondsNode = null;
		MemberTreeNode[] minutesNode = null;
		MemberTreeNode[] hoursNode = null;
		MemberTreeNode[] dayOfYearNode = null;
		MemberTreeNode[] dayOfMonthNode = null;
		MemberTreeNode[] dayOfWeekNode = null;
		MemberTreeNode[] monthNode = null;

		MemberTreeNode[] parent = null;
		List temp = new ArrayList();
		List parentList = new ArrayList();

		for (int i = 0; i < dateTypes.length; i++) {
			temp.clear();

			if (DATE_TIME_LEVEL_TYPE_SECOND.equals(dateTypes[i])) {
				if (parentList.size() > 0) {
					for (int t = 0; t < parentList.size(); t++) {
						MemberTreeNode[] nodes = (MemberTreeNode[]) parentList.get(t);
						for (int j = 0; j < nodes.length; j++) {
							secondsNode = createSecond();
							nodes[j].addAllNodes(secondsNode);
							temp.add(secondsNode);
						}
					}
					parentList.clear();
					parentList.addAll(temp);
				} else {
					secondsNode = createSecond();
					parent = secondsNode;
					parentList.clear();
					parentList.add(secondsNode);
				}
			} else if (DATE_TIME_LEVEL_TYPE_MINUTE.equals(dateTypes[i])) {
				if (parentList.size() > 0) {
					for (int t = 0; t < parentList.size(); t++) {
						MemberTreeNode[] nodes = (MemberTreeNode[]) parentList.get(t);
						for (int j = 0; j < nodes.length; j++) {
							minutesNode = createMinute();
							nodes[j].addAllNodes(minutesNode);
							temp.add(minutesNode);
						}
					}
					parentList.clear();
					parentList.addAll(temp);
				} else {
					minutesNode = createMinute();
					parent = minutesNode;
					parentList.clear();
					parentList.add(minutesNode);
				}
			} else if (DATE_TIME_LEVEL_TYPE_HOUR.equals(dateTypes[i])) {
				if (parentList.size() > 0) {
					for (int t = 0; t < parentList.size(); t++) {
						MemberTreeNode[] nodes = (MemberTreeNode[]) parentList.get(t);
						for (int j = 0; j < nodes.length; j++) {
							hoursNode = createHour();
							nodes[j].addAllNodes(hoursNode);
							temp.add(hoursNode);
						}
					}
					parentList.clear();
					parentList.addAll(temp);
				} else {
					hoursNode = createHour();
					parent = hoursNode;
					parentList.clear();
					parentList.add(hoursNode);
				}
			} else if (DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals(dateTypes[i])) {
				int year = getCalendar((Date) dateTimeValue).get(Calendar.YEAR);

				if (parentList.size() > 0) {
					for (int t = 0; t < parentList.size(); t++) {
						MemberTreeNode[] nodes = (MemberTreeNode[]) parentList.get(t);
						for (int j = 0; j < nodes.length; j++) {
							dayOfYearNode = createDayOfYearNode(year);
							nodes[j].addAllNodes(dayOfYearNode);
							temp.add(dayOfYearNode);
						}
					}
					parentList.clear();
					parentList.addAll(temp);
				} else {
					dayOfYearNode = createDayOfYearNode(year);
					parent = dayOfYearNode;
					parentList.clear();
					parentList.add(dayOfYearNode);
				}
			} else if (DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals(dateTypes[i])) {
				int year = getCalendar((Date) dateTimeValue).get(Calendar.YEAR);
				int month = getCalendar((Date) dateTimeValue).get(Calendar.MONTH);

				boolean isUnderMonthHierarchy = false;
				if (i > 0) {
					isUnderMonthHierarchy = isDayMonth(dateTypes[i - 1]);
				}

				if (parentList.size() > 0) {
					for (int t = 0; t < parentList.size(); t++) {
						MemberTreeNode[] nodes = (MemberTreeNode[]) parentList.get(t);
						for (int j = 0; j < nodes.length; j++) {
							if (isUnderMonthHierarchy) {
								dayOfMonthNode = createDayOfMonth(year,
										(Integer) (((Member) nodes[j].key).getKeyValues()[0]));
							} else {
								dayOfMonthNode = createDayOfMonth(year, month + 1);
							}
							nodes[j].addAllNodes(dayOfMonthNode);
							temp.add(dayOfMonthNode);
						}
					}
					parentList.clear();
					parentList.addAll(temp);
				} else {
					dayOfMonthNode = createDayOfMonth(year, month + 1);
					parent = dayOfMonthNode;
					parentList.clear();
					parentList.add(dayOfMonthNode);
				}
			} else if (DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals(dateTypes[i])) {
				if (parentList.size() > 0) {
					for (int t = 0; t < parentList.size(); t++) {
						MemberTreeNode[] nodes = (MemberTreeNode[]) parentList.get(t);
						for (int j = 0; j < nodes.length; j++) {
							dayOfWeekNode = createDayOfWeek();
							nodes[j].addAllNodes(dayOfWeekNode);
							temp.add(dayOfWeekNode);
						}
					}
					parentList.clear();
					parentList.addAll(temp);
				} else {
					dayOfWeekNode = createDayOfWeek();
					parent = dayOfWeekNode;
					parentList.clear();
					parentList.add(dayOfWeekNode);
				}
			} else if (DATE_TIME_LEVEL_TYPE_MONTH.equals(dateTypes[i])) {
				if (isQuarterMonth(dateTypes)) {
					for (int t = 0; t < parentList.size(); t++) {
						MemberTreeNode[] nodes = (MemberTreeNode[]) parentList.get(t);
						for (int k = 0; k < nodes.length; k++) {
							monthNode = createQuarterMonthNode(k);
							nodes[k].addAllNodes(monthNode);
							temp.add(monthNode);
						}
					}
					parentList.clear();
					parentList.addAll(temp);
				} else {
					monthNode = createMonthNode();
					parent = monthNode;
					parentList.add(parent);
				}
			} else if (DATE_TIME_LEVEL_TYPE_QUARTER.equals(dateTypes[i])) {
				parent = createQuarterNode();
				parentList.add(parent);
			}
		}

		return parent;
	}

	private static Calendar getCalendar(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}

	private static boolean isQuarterMonth(String[] types) {
		for (int i = 0; i < types.length; i++) {
			if (DATE_TIME_LEVEL_TYPE_QUARTER.equals(types[i])) {
				return true;
			}
		}
		return false;
	}

	private static boolean isDayMonth(String types) {
		if (DATE_TIME_LEVEL_TYPE_MONTH.equals(types)) {
			return true;
		} else {
			return false;
		}
	}

	private static MemberTreeNode[] createQuarterNode() {
		MemberTreeNode[] nodes = new MemberTreeNode[4];
		calendar.clear();
		for (int i = 1; i <= nodes.length; i++) {
			Member member = new Member();
			member.setKeyValues(new Object[] { Integer.valueOf(i) });
			calendar.set(Calendar.MONTH, (i - 1) * 3);
			member.setAttributes(new Object[] { calendar.getTime() });
			nodes[i - 1] = new MemberTreeNode(member);
		}
		return nodes;
	}

	private static MemberTreeNode[] createMonthNode() {
		MemberTreeNode[] nodes = new MemberTreeNode[12];
		calendar.clear();
		for (int i = 1; i <= nodes.length; i++) {
			Member member = new Member();
			member.setKeyValues(new Object[] { Integer.valueOf(i) });
			calendar.set(Calendar.MONTH, i - 1);
			member.setAttributes(new Object[] { calendar.getTime() });
			nodes[i - 1] = new MemberTreeNode(member);
		}
		return nodes;
	}

	private static MemberTreeNode[] createQuarterMonthNode(int quarter) {
		MemberTreeNode[] nodes = new MemberTreeNode[3];
		calendar.clear();
		switch (quarter) {
		case 0:
			for (int i = 1; i <= nodes.length; i++) {
				Member member = new Member();
				member.setKeyValues(new Object[] { Integer.valueOf(i) });
				calendar.set(Calendar.MONTH, i - 1);
				member.setAttributes(new Object[] { calendar.getTime() });
				nodes[i - 1] = new MemberTreeNode(member);
			}
			break;
		case 1:
			for (int i = 4; i <= nodes.length + 3; i++) {
				Member member = new Member();
				member.setKeyValues(new Object[] { Integer.valueOf(i) });
				calendar.set(Calendar.MONTH, i - 1);
				member.setAttributes(new Object[] { calendar.getTime() });
				nodes[i - 4] = new MemberTreeNode(member);
			}
			break;
		case 2:
			for (int i = 7; i <= nodes.length + 6; i++) {
				Member member = new Member();
				member.setKeyValues(new Object[] { Integer.valueOf(i) });
				calendar.set(Calendar.MONTH, i - 1);
				member.setAttributes(new Object[] { calendar.getTime() });

				nodes[i - 7] = new MemberTreeNode(member);
			}
			break;
		case 3:
			for (int i = 10; i <= nodes.length + 9; i++) {
				Member member = new Member();
				member.setKeyValues(new Object[] { Integer.valueOf(i) });
				calendar.set(Calendar.MONTH, i - 1);
				member.setAttributes(new Object[] { calendar.getTime() });
				nodes[i - 10] = new MemberTreeNode(member);
			}
			break;
		}
		return nodes;
	}

	private static MemberTreeNode[] createDayOfYearNode(int year) {
		calendar.clear();
		int count = 0;
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			count = 366;
		} else {
			count = 365;
		}
		MemberTreeNode[] nodes = new MemberTreeNode[count];

		for (int i = 1; i <= count; i++) {
			Member member = new Member();
			member.setKeyValues(new Object[] { Integer.valueOf(i) });
			calendar.set(Calendar.DAY_OF_YEAR, i);
			member.setAttributes(new Object[] { calendar.getTime() });
			nodes[i - 1] = new MemberTreeNode(member);
		}
		return nodes;
	}

	private static MemberTreeNode[] createDayOfMonth(int year, int month) {
		boolean isLeapYear = false;
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			isLeapYear = true;
		}
		MemberTreeNode[] nodes = {};
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			nodes = new MemberTreeNode[31];
			for (int i = 1; i <= nodes.length; i++) {
				Member member = new Member();
				member.setKeyValues(new Object[] { new Integer(i) });
				calendar.set(Calendar.MONTH, month - 1);
				calendar.set(Calendar.DATE, i);
				member.setAttributes(new Object[] { calendar.getTime() });
				nodes[i - 1] = new MemberTreeNode(member);
			}
		} else if (month == 2) {
			if (isLeapYear) {
				nodes = new MemberTreeNode[29];
				for (int i = 1; i <= nodes.length; i++) {
					Member member = new Member();
					member.setKeyValues(new Object[] { Integer.valueOf(i) });
					calendar.set(Calendar.MONTH, month - 1);
					calendar.set(Calendar.DATE, i);
					member.setAttributes(new Object[] { calendar.getTime() });
					nodes[i - 1] = new MemberTreeNode(member);
				}
			} else {
				nodes = new MemberTreeNode[28];
				for (int i = 1; i <= nodes.length; i++) {
					Member member = new Member();
					member.setKeyValues(new Object[] { Integer.valueOf(i) });
					calendar.set(Calendar.MONTH, month - 1);
					calendar.set(Calendar.DATE, i);
					member.setAttributes(new Object[] { calendar.getTime() });
					nodes[i - 1] = new MemberTreeNode(member);
				}
			}
		} else {
			nodes = new MemberTreeNode[30];
			for (int i = 1; i <= nodes.length; i++) {
				Member member = new Member();
				member.setKeyValues(new Object[] { Integer.valueOf(i) });
				calendar.set(Calendar.MONTH, month - 1);
				calendar.set(Calendar.DATE, i);
				member.setAttributes(new Object[] { calendar.getTime() });
				nodes[i - 1] = new MemberTreeNode(member);
			}
		}
		return nodes;
	}

	private static MemberTreeNode[] createDayOfWeek() {
		MemberTreeNode[] nodes = new MemberTreeNode[7];
		calendar.clear();
		for (int i = 1; i <= nodes.length; i++) {
			Member member = new Member();
			member.setKeyValues(new Object[] { Integer.valueOf(i) });
			calendar.set(Calendar.DAY_OF_WEEK, i);
			member.setAttributes(new Object[] { calendar.getTime() });
			nodes[i - 1] = new MemberTreeNode(member);
		}
		return nodes;
	}

	private static MemberTreeNode[] createHour() {
		MemberTreeNode[] nodes = new MemberTreeNode[24];
		calendar.clear();
		for (int i = 1; i <= nodes.length; i++) {
			Member member = new Member();
			member.setKeyValues(new Object[] { Integer.valueOf(i) });
			calendar.set(Calendar.HOUR_OF_DAY, i);
			member.setAttributes(new Object[] { calendar.getTime() });
			nodes[i - 1] = new MemberTreeNode(member);
		}
		return nodes;
	}

	private static MemberTreeNode[] createMinute() {
		MemberTreeNode[] nodes = new MemberTreeNode[60];
		calendar.clear();
		for (int i = 1; i <= nodes.length; i++) {
			Member member = new Member();
			member.setKeyValues(new Object[] { Integer.valueOf(i) });
			calendar.set(Calendar.MINUTE, i);
			member.setAttributes(new Object[] { calendar.getTime() });
			nodes[i - 1] = new MemberTreeNode(member);
		}
		return nodes;
	}

	private static MemberTreeNode[] createSecond() {
		MemberTreeNode[] nodes = new MemberTreeNode[60];
		calendar.clear();
		for (int i = 1; i <= nodes.length; i++) {
			Member member = new Member();
			member.setKeyValues(new Object[] { Integer.valueOf(i) });
			calendar.set(Calendar.SECOND, i);
			member.setAttributes(new Object[] { calendar.getTime() });
			nodes[i - 1] = new MemberTreeNode(member);
		}
		return nodes;
	}

	public static boolean isTimeMirror(IAggregationResultSet rs, int index, MirrorMetaInfo service) {
		for (int i = index; i < rs.getLevelCount(); i++) {
			if (rs.getLevelAttributes(i) == null || !isTimeMirrorAttributes(service.getLevelType(rs.getLevel(i)))) {
				return false;
			}
		}
		return true;
	}

	public static boolean containsTimeMirror(IAggregationResultSet rs, MirrorMetaInfo service) {
		int index = service.getMirrorStartPosition();
		if (isTimeMirror(rs, index, service)) {
			return true;
		}
		return false;
	}

	private static boolean isTimeMirrorAttributes(String types) {
		if (types == null || types.equals(DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH)
				|| types.equals(DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR) || types.equals(DATE_TIME_LEVEL_TYPE_YEAR)) {
			return false;
		}
		return true;
	}
}
