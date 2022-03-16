/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.emitter.ods;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GroupInfo {

	private String name;

	private String[] columnExpressions;

	// the size of the columns[i] is the number of records in the current group
	// the elements in a column list are positions info for the records of this
	// column.
	private List[] columns;
	private List subGroupInfos;
	private GroupInfo nextSibling;

	// for threading groups with same name
	private static GroupInfo head = new GroupInfo();
	private static List positions = new ArrayList();

	private GroupInfo() {

	}

	public GroupInfo(String gname, String[] cn) {
		name = gname;
		subGroupInfos = new ArrayList();

		columnExpressions = cn;
		columns = new ArrayList[columnExpressions.length];
	}

	public boolean addPosition(int index, int c, int r) {
		if (index < 0 || index >= columns.length) {
			return false;
		}
		if (columns[index] == null) {
			columns[index] = new ArrayList();
		}

		columns[index].add(new Position(r, c));
		return true;
	}

	public void addSubGroupInfo(GroupInfo child) {
		if (subGroupInfos == null) {
			subGroupInfos = new ArrayList();
		}
		subGroupInfos.add(child);
	}

	// be sure to obey the protocol : gname and cname must be valid
	// current group will always be the root of the groups recursion
	public List getByGroup(String gname, String cname, int index) {
		GroupInfo g = findFirstGroupByName(gname);

		if (g.nextSibling == null) {
			threadGroupsByName(gname);
		}

		g = g.getGroupByIndex(index);

		positions.clear();
		g.getPositions(cname);
		return positions;
	}

	private GroupInfo getGroupByIndex(int index) {
		GroupInfo g = this;
		for (int i = 0; i < index && g.nextSibling != null; i++) {
			g = g.nextSibling;
		}
		return g;
	}

	private GroupInfo findFirstGroupByName(String gname) {
		GroupInfo g = this;
		while (!g.name.equals(gname) && g.subGroupInfos.size() > 0) {
			g = (GroupInfo) g.subGroupInfos.get(0);
		}
		return g;
	}

	private void threadGroupsByName(String gname) {
		if (name.equals(gname)) {
			head.nextSibling = this;
			head = this;
		} else {
			for (int i = 0; i < subGroupInfos.size(); i++) {
				((GroupInfo) subGroupInfos.get(i)).threadGroupsByName(gname);
			}
		}
	}

	private void getPositions(String exp) {
		if (subGroupInfos.size() == 0) {
			positions.addAll(getPsByColExp(exp));
		} else {
			for (int i = 0; i < subGroupInfos.size(); i++) {
				((GroupInfo) subGroupInfos.get(i)).getPositions(exp);
			}
		}
	}

	private List getPsByColExp(String exp) {
		List x = columns[getIndex(exp)];
		if (x == null) {
			return new ArrayList();
		}

		return columns[getIndex(exp)];
	}

	private int getIndex(String exp) {
		for (int i = 0; i < columnExpressions.length; i++) {
			if (columnExpressions[i].equals(exp)) {
				return i;
			}
		}
		throw new NoSuchElementException("No such expression in TableBinding");
	}

	public String[] getColExps() {
		return columnExpressions;
	}

	protected static class Position {

		int row;
		int column;

		public Position(int r, int c) {
			row = r;
			column = c;
		}
	}

}
