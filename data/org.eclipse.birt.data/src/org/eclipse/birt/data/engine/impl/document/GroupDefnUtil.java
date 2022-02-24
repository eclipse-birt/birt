/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.document;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.QueryCompUtil;

/**
 * 
 */
public class GroupDefnUtil {
	/**
	 * @param outputStream
	 * @param filterList
	 * @throws DataException
	 */
	static void saveGroupDefn(OutputStream outputStream, List groupList, int version, String bundleVersion)
			throws DataException {
		DataOutputStream dos = new DataOutputStream(outputStream);

		int size = groupList == null ? 0 : groupList.size();
		try {
			IOUtil.writeInt(dos, size);
			for (int i = 0; i < size; i++) {
				IGroupDefinition groupDefn = (IGroupDefinition) groupList.get(i);
				IOUtil.writeString(dos, groupDefn.getName());
				IOUtil.writeString(dos, groupDefn.getKeyColumn());
				IOUtil.writeString(dos, groupDefn.getKeyExpression());
				IOUtil.writeInt(dos, groupDefn.getInterval());
				IOUtil.writeDouble(dos, groupDefn.getIntervalRange());
				IOUtil.writeInt(dos, groupDefn.getSortDirection());
				FilterDefnUtil.saveFilterDefn(dos, groupDefn.getFilters(), version);
				QueryDefnIOUtil.saveSorts(dos, groupDefn.getSorts(), version);
				QueryDefnIOUtil.saveSubQuery(dos, groupDefn.getSubqueries(), version, bundleVersion);
			}

			dos.flush();
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_SAVE_ERROR, e);
		}
	}

	/**
	 * @param inputStream
	 * @return
	 * @throws DataException
	 */
	static List loadGroupDefn(InputStream inputStream, IBaseQueryDefinition parent, int version, String bundleVersion)
			throws DataException {
		List groupList = new ArrayList();
		DataInputStream dis = new DataInputStream(inputStream);
		try {
			int size = IOUtil.readInt(inputStream);
			for (int i = 0; i < size; i++) {
				GroupDefinition groupDefn = new GroupDefinition(IOUtil.readString(dis));
				String keyColumn = IOUtil.readString(dis);
				String keyExpr = IOUtil.readString(dis);
				if (keyColumn != null)
					groupDefn.setKeyColumn(keyColumn);
				else
					groupDefn.setKeyExpression(keyExpr);
				groupDefn.setInterval(IOUtil.readInt(dis));
				groupDefn.setIntervalRange(IOUtil.readDouble(dis));
				groupDefn.setSortDirection(IOUtil.readInt(dis));
				groupDefn.getFilters().addAll(FilterDefnUtil.loadFilterDefn(dis, version));
				groupDefn.getSorts().addAll(QueryDefnIOUtil.loadSorts(dis, version));
				groupDefn.getSubqueries().addAll(QueryDefnIOUtil.loadSubQuery(dis, parent, version, bundleVersion));
				groupList.add(groupDefn);
			}
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_LOAD_ERROR, e);
		}

		return groupList;
	}

	/**
	 * @param groupDefn1
	 * @param groupDefn2
	 * @return
	 */
	public static boolean isEqualGroups(List list1, List list2) {
		if (list1 == list2)
			return true;

		if (list1 == null || list2 == null)
			return false;

		if (list1.size() != list2.size())
			return false;

		for (int i = 0; i < list1.size(); i++)
			if (isEqualGroup((IGroupDefinition) list1.get(i), (IGroupDefinition) list2.get(i)) == false)
				return false;

		return true;
	}

	/**
	 * @param filterDefn1
	 * @param filterDefn2
	 * @return
	 */
	private static boolean isEqualGroup(IGroupDefinition groupDefn1, IGroupDefinition groupDefn2) {
		if (groupDefn1 == groupDefn2)
			return true;

		if (groupDefn1 == null || groupDefn2 == null)
			return false;

		return isEqualStr(groupDefn1.getName(), groupDefn2.getName())
				&& isEqualStr(groupDefn1.getKeyColumn(), groupDefn2.getKeyColumn())
				&& isEqualStr(groupDefn1.getKeyExpression(), groupDefn2.getKeyExpression())
				&& groupDefn1.getSortDirection() == groupDefn2.getSortDirection()
				&& QueryCompUtil.isEqualSorts(groupDefn1.getSorts(), groupDefn2.getSorts())
				&& FilterDefnUtil.isEqualFilter(groupDefn1.getFilters(), groupDefn2.getFilters())
				&& QueryCompUtil.isCompatibleSQs(groupDefn1.getSubqueries(), groupDefn2.getSubqueries());
	}

	/**
	 * @param str1
	 * @param str2
	 * @return
	 */
	private static boolean isEqualStr(String str1, String str2) {
		if (str1 == str2)
			return true;

		if (str1 == null || str2 == null)
			return false;

		return str1.equals(str2);
	}

}
