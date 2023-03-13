/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor.transform.group;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.cache.CachedList;
import org.eclipse.birt.data.engine.cache.ICachedObject;
import org.eclipse.birt.data.engine.cache.ICachedObjectCreator;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.cache.SortSpec;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.expression.CompareHints;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * The instance of this class is used by CachedResultSet to deal with
 * group-related data transformation operations.
 */

public class GroupCalculationUtil {

	private BaseQuery query;

	private IResultClass rsMeta;

	/**
	 * Group definitions. Each entry has name of key column plus interval
	 * information. groupKeys[0] is the highest group level
	 */
	private GroupBy[] groupDefs;

	/** data rows holds real data */
	private ResultSetCache smartCache;

	private GroupInformationUtil groupInformationUtil;

	/*
	 * groups[level] is an ArrayList of GroupInfo objects at the specified level.
	 * Level is a 0-based group index, with 0 denoting the outermost group, etc.
	 * Example: Row GroupKey1 GroupKey2 GroupKey3 Column4 Column5 0: CHINA BEIJING
	 * 2003 Cola $100 1: CHINA BEIJING 2003 Pizza $320 2: CHINA BEIJING 2004 Cola
	 * $402 3: CHINA SHANGHAI 2003 Cola $553 4: CHINA SHANGHAI 2003 Pizza $223 5:
	 * CHINA SHANGHAI 2004 Cola $226 6: USA CHICAGO 2004 Pizza $133 7: USA NEW YORK
	 * 2004 Cola $339 8: USA NEW YORK 2004 Cola $297
	 *
	 * groups: (parent, child) LEVEL 0 LEVEL 1 LEVEL 2
	 * ============================================ 0: -,0 0,0 0,0 1: -,2 0,2 0,2 2:
	 * 1,4 1,3 3: 1,5 1,5 4: 2,6 5: 3,7
	 */

	private ResultSetPopulator resultPopoulator;

	/**
	 *
	 * @param query
	 * @param rsMeta
	 * @throws DataException
	 */
	GroupCalculationUtil(BaseQuery query, ResultSetPopulator resultPopoulator, DataEngineSession session)
			throws DataException {
		this.query = query;
		this.resultPopoulator = resultPopoulator;
		this.rsMeta = resultPopoulator.getResultSetMetadata();
		this.groupInformationUtil = new GroupInformationUtil(this, session);
		this.initGroupSpec();
	}

	/**
	 * @param inputStream
	 * @param rsMeta
	 * @param rsCache
	 * @throws DataException
	 */
	/*
	 * GroupCalculationUtil( InputStream inputStream, IResultClass rsMeta,
	 * ResultSetCache rsCache ) throws DataException { try {
	 * this.groupInformationUtil.readGroupsFromStream( inputStream ); } catch (
	 * IOException e ) { throw new DataException( ResourceConstants.RD_LOAD_ERROR,
	 * e, "Group Info" ); }
	 *
	 * this.rsMeta = rsMeta; this.smartCache = rsCache; this.initGroupSpec( ); }
	 */

	/**
	 *
	 * @return
	 */
	public GroupInformationUtil getGroupInformationUtil() {
		return this.groupInformationUtil;
	}

	/**
	 * @param inputStream
	 * @throws DataException
	 */
	public void doSave(OutputStream outputStream) throws DataException {
		try {
			this.groupInformationUtil.saveGroupsToStream(outputStream);
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_SAVE_ERROR, e, "Group Information");
		}
	}

	/**
	 * Everytime in CachedResultSet the result set cache changed, it must be reset
	 * to GroupCalculationUtil. Set the value of smartCache.
	 *
	 * @param rsc
	 */
	public void setResultSetCache(ResultSetCache rsc) {
		this.smartCache = rsc;
	}

	/**
	 * Gets group count
	 *
	 * @return number of groupKeys
	 */
	int getGroupCount() {
		return groupDefs.length;
	}

	/**
	 * Sort the group array according to the values in sortKeys[] of
	 * GroupBoundaryInfo intances. within them.
	 *
	 * @param groupArray
	 */
	void sortGroupBoundaryInfos(List[] groupArray) {
		for (int i = 0; i < groupArray.length; i++) {
			Object[] toBeSorted = new Object[groupArray[i].size()];
			for (int j = 0; j < toBeSorted.length; j++) {
				toBeSorted[j] = groupArray[i].get(j);
			}
			Arrays.sort(toBeSorted, new GroupBoundaryInfoComparator());
			groupArray[i].clear();
			for (int j = 0; j < toBeSorted.length; j++) {
				groupArray[i].add(toBeSorted[j]);
			}
		}
	}

	public BaseQuery getQuery() {
		return this.query;
	}

	/**
	 * This method is used to filter out the GroupBoundaryInfo instances that are
	 * marked as "not accepted" from GroupBoundaryInfos.
	 *
	 * @param groupArray
	 * @return
	 */
	List[] filterGroupBoundaryInfos(List[] groupArray) {
		List[] result = new List[groupArray.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new CachedList(resultPopoulator.getSession().getTempDir(),
					DataEngineSession.getCurrentClassLoader(), GroupBoundaryInfo.getCreator());
		}
		for (int i = 0; i < groupArray.length; i++) {
			for (int j = 0; j < groupArray[i].size(); j++) {
				if (((GroupBoundaryInfo) groupArray[i].get(j)).isAccpted()) {
					result[i].add(groupArray[i].get(j));
				}
			}
		}
		return result;
	}

	/**
	 *
	 * @return
	 */
	GroupBy[] getGroupDefn() {
		return this.groupDefs;
	}

	ResultSetCache getResultSetCache() {
		return this.smartCache;
	}

	ResultSetPopulator getResultSetPopoulator() {
		return resultPopoulator;
	}

	/**
	 * Performs data transforms and starts the iterator
	 */
	private void initGroupSpec() throws DataException {
		// Set up the GroupDefs structure
		IQuery.GroupSpec[] groupSpecs = query.getGrouping();
		if (groupSpecs != null) {
			groupDefs = new GroupBy[groupSpecs.length];
			for (int i = 0; i < groupSpecs.length; ++i) {
				int keyIndex = groupSpecs[i].getKeyIndex();
				String keyColumn = groupSpecs[i].getKeyColumn();

				// Convert group key name to index for faster future access
				// assume priority of keyColumn is higher than keyIndex
				if (keyColumn != null) {
					keyIndex = rsMeta.getFieldIndex(keyColumn);
				}

				if (keyIndex < 1 || keyIndex > rsMeta.getFieldCount()) {
					// Invalid group key name
					throw new DataException(ResourceConstants.INVALID_GROUP_KEY_COLUMN, keyColumn);
				}
				groupDefs[i] = GroupBy.newInstance(groupSpecs[i], keyIndex, keyColumn,
						rsMeta.getFieldValueClass(keyIndex));
			}
		} else {
			groupDefs = new GroupBy[0];
		}
	}

	/**
	 * Sort data rows by group information and sort specification
	 *
	 * @throws DataException
	 */
	public SortSpec getSortSpec() throws DataException {
		assert groupDefs != null;

		// Create an array of column indexes. We first sort by Group keys, then
		// sort keys
		int groupCount = 0;
		int sortCount = 0;

		for (int i = 0; i < groupDefs.length; i++) {
			// When group column is rowid, sort on it should not
			// take effect.
			if (groupDefs[i].getColumnIndex() >= 0) {
				groupCount++;
			}
		}

		if (query.getOrdering() != null) {
			sortCount = query.getOrdering().length;
		}

		boolean doGroupSort = false;
		for (int i = 0; i < groupCount; i++) {
			if (groupDefs[i].getGroupSpec().getSortDirection() != IGroupDefinition.NO_SORT) {
				doGroupSort = true;
				break;
			}
		}

		if (!doGroupSort && !needSortingOnGroupKeys()) {
			groupCount = 0;
		}

		int[] sortKeyIndexes = new int[groupCount + sortCount];
		String[] sortKeyColumns = new String[groupCount + sortCount];
		int[] sortAscending = new int[groupCount + sortCount];
		CompareHints[] comparator = new CompareHints[groupCount + sortCount];
		for (int i = 0; i < groupCount; i++) {
			int index = groupDefs[i].getColumnIndex();
			if (index >= 0) {
				sortKeyIndexes[i] = groupDefs[i].getColumnIndex();
				sortKeyColumns[i] = groupDefs[i].getColumnName();
				sortAscending[i] = groupDefs[i].getGroupSpec().getSortDirection();
				// TODO:support collation sort in grouping. At current stage
				// we only support collation sort in sort definition.
				comparator[i] = null;
			}
		}
		for (int i = 0; i < sortCount; i++) {
			int keyIndex = query.getOrdering()[i].getIndex();
			String keyName = query.getOrdering()[i].getField();

			// If sort key name exist (not null) then depend on key name, else
			// depend on key index
			if (keyName != null) {
				keyIndex = rsMeta.getFieldIndex(keyName);
			}

//			if ( keyIndex < 1 || keyIndex > rsMeta.getFieldCount( ) )
//				// Invalid sort key name
//				throw new DataException( ResourceConstants.INVALID_KEY_COLUMN,
//						keyName );
			sortKeyIndexes[groupCount + i] = keyIndex;
			sortKeyColumns[groupCount + i] = keyName;
			sortAscending[groupCount + i] = query.getOrdering()[i].isAscendingOrder() ? SortSpec.SORT_ASC
					: SortSpec.SORT_DESC;
			comparator[groupCount + i] = new CompareHints(query.getOrdering()[i].getComparator(), null);
		}

		return new SortSpec(sortKeyIndexes, sortKeyColumns, sortAscending, comparator);
	}

	private boolean needSortingOnGroupKeys() {
		// Now do sorting before group is false.
		List<IGroupDefinition> groups = this.query.getQueryDefinition().getGroups();
		List<ISortDefinition> sorts = this.query.getQueryDefinition().getSorts();

		if (sorts == null || sorts.size() == 0) {
			return false;
		}

		int i = 0;
		for (; i < groups.size() && i < sorts.size();) {
			String groupKey = groups.get(i).getKeyColumn() != null
					? ExpressionUtil.createJSRowExpression(groups.get(i).getKeyColumn())
					: groups.get(i).getKeyExpression();
			String sortKey = sorts.get(i).getColumn() != null
					? ExpressionUtil.createJSRowExpression(sorts.get(i).getColumn())
					: sorts.get(i).getExpression().getText();
			if (groupKey.equals(sortKey)) {
				i++;
			} else {
				break;
			}
		}

		if (i == groups.size()) {
			return false;
		} else {
			return true;
		}
	}
}

/**
 * Structure to hold a group instance with its startIndex, endIndex, filter
 * result, sortKeys and Sort directions.
 *
 */
final class GroupBoundaryInfo implements ICachedObject {

	// The start index and end index of a Group
	private int startIndex;
	private int endIndex;

	// Used by Group sorting
	private Object[] sortKeys;
	private boolean[] sortDirections;

	// Used by Group filtering
	private boolean accept = true;
	private Collator[] comparator;
	private CompareHints[] compareHints;

	/**
	 *
	 * @return
	 */
	public static ICachedObjectCreator getCreator() {
		return new GroupBoundaryInfoCreator();
	}

	/**
	 * @param start The start index of a group
	 * @param end   The end index of a group
	 */
	GroupBoundaryInfo(int start, int end) {
		this.startIndex = start;
		this.endIndex = end;
		sortKeys = new Object[0];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.cache.ICachedObject#getFieldValues()
	 */
	@Override
	public Object[] getFieldValues() {
		ArrayList fields = new ArrayList();
		fields.add(new Integer(startIndex)); // idx 0
		fields.add(new Integer(endIndex)); // idx 1

		if (sortKeys != null) {
			fields.add(new Integer(sortKeys.length)); // idx 2
			for (int i = 0; i < sortKeys.length; i++) {
				fields.add(sortKeys[i]);
			}
		} else {
			fields.add(null);
		}

		if (sortDirections != null) {
			fields.add(Integer.valueOf(sortDirections.length)); // idx 2 + n + 1
			for (int i = 0; i < sortDirections.length; i++) {
				fields.add(Boolean.valueOf(sortDirections[i]));
			}
		} else {
			fields.add(null);
		}

		if (this.comparator != null) {
			fields.add(Integer.valueOf(comparator.length)); // idx 2 + n + 1 + n + 1
			for (int i = 0; i < comparator.length; i++) {
				fields.add(comparator[i] == null ? ISortDefinition.ASCII_SORT_STRENGTH
						: Integer.valueOf(comparator[i].getStrength()));
			}

			for (int i = 0; i < comparator.length; i++) {
				fields.add(comparator[i] == null ? null : comparator[i].getLocale(ULocale.ACTUAL_LOCALE).getBaseName());
			}
		} else {
			fields.add(null);
		}

		fields.add(Boolean.valueOf(accept));
		return fields.toArray();
	}

	/**
	 * Return the start index.
	 *
	 * @return
	 */
	int getStartIndex() {
		return this.startIndex;
	}

	/**
	 * Return the end index.
	 *
	 * @return
	 */
	int getEndIndex() {
		return this.endIndex;
	}

	/**
	 * Detect whether the given GroupBoundaryInfo consists of the startIdx and
	 * endIdx that included in current GroupBoundaryInfo instance.
	 *
	 * @param gbi
	 * @return
	 */
	boolean isInBoundary(GroupBoundaryInfo gbi) {
		if (gbi.getStartIndex() >= this.getStartIndex() && gbi.getEndIndex() <= this.getEndIndex()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Set the sort conditions
	 *
	 * @param sortKeys
	 * @param sortOrderings
	 */
	void setSortCondition(Object[] sortKeys, boolean[] sortOrderings, int[] sortStrength, ULocale[] sortLocale) {
		this.sortKeys = sortKeys;
		this.sortDirections = sortOrderings;
		this.comparator = new Collator[this.sortKeys.length];
		this.compareHints = new CompareHints[this.sortKeys.length];
		for (int i = 0; i < this.comparator.length; i++) {
			this.comparator[i] = sortStrength[i] == ISortDefinition.ASCII_SORT_STRENGTH ? null
					: Collator.getInstance(sortLocale[i]);
			this.compareHints[i] = new CompareHints(this.comparator[i], null);
		}
	}

	/**
	 * Return the sort keys array.
	 *
	 * @return
	 */
	Object[] getSortKeys() {
		return this.sortKeys;
	}

	/**
	 * Return the sort direction array.
	 *
	 * @return
	 */
	boolean[] getSortDirection() {
		return this.sortDirections;
	}

	/**
	 * Return the sort strength;
	 *
	 * @return
	 */
	CompareHints[] getCollarComparator() {
		return this.compareHints;
	}

	/**
	 * Set the filter value of GroupBoundaryInfo.
	 *
	 * @param accept
	 */
	void setAccepted(boolean accept) {
		this.accept = accept;
	}

	/**
	 * Return whether the GroupBoundaryInfo intance is accpeted or not.
	 *
	 * @return
	 */
	boolean isAccpted() {
		return this.accept;
	}

}

/**
 * The Comparator instance which is used to compare two GroupBoundaryInfo
 * instance.
 *
 */
final class GroupBoundaryInfoComparator implements Comparator {

	/**
	 *
	 */

	@Override
	public int compare(Object o1, Object o2) {
		Object[] sortKeys1 = ((GroupBoundaryInfo) o1).getSortKeys();
		Object[] sortKeys2 = ((GroupBoundaryInfo) o2).getSortKeys();
		boolean[] sortDirection = ((GroupBoundaryInfo) o1).getSortDirection();
		CompareHints[] comparator = ((GroupBoundaryInfo) o1).getCollarComparator();
		int result = 0;
		for (int i = 0; i < sortKeys1.length; i++) {
			try {
				result = ScriptEvalUtil.compare(sortKeys1[i], sortKeys2[i], comparator[i]);
			} catch (DataException e) {
				result = 0;
			}
			if (result != 0) {
				if (!sortDirection[i]) {
					result = result * -1;
				}
				break;
			}

		}

		return result;
	}
}

/**
 * A creator class implemented ICachedObjectCreator. This class is used to
 * create GroupBoundaryInfo object.
 *
 * @author Administrator
 *
 */
class GroupBoundaryInfoCreator implements ICachedObjectCreator {
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.cache.ICachedObjectCreator#createInstance(java.
	 * lang.Object[])
	 */
	@Override
	public ICachedObject createInstance(Object[] fields) {
		GroupBoundaryInfo groupBoundaryInfo = new GroupBoundaryInfo(((Integer) fields[0]).intValue(),
				((Integer) fields[1]).intValue());
		Object[] sortKeys = null;
		int sortKeysTotalLength = 1;
		if (fields[2] != null) {
			sortKeys = new Object[((Integer) fields[2]).intValue()];
			System.arraycopy(fields, 3, sortKeys, 0, sortKeys.length);
			sortKeysTotalLength = sortKeys.length + 1;
		}

		boolean[] sortDirections = null;
		if (fields[2 + sortKeysTotalLength] != null) {
			sortDirections = new boolean[((Integer) fields[2 + sortKeysTotalLength]).intValue()];

			for (int i = 0; i < sortDirections.length; i++) {
				sortDirections[i] = ((Boolean) fields[3 + sortKeysTotalLength + i]).booleanValue();
			}
		}
		int[] sortStrength = null;
		ULocale[] locales = null;
		if (fields[2 + sortKeysTotalLength * 2] != null) {
			sortStrength = new int[((Integer) fields[2 + sortKeysTotalLength * 2]).intValue()];

			for (int i = 0; i < sortStrength.length; i++) {
				sortStrength[i] = ((Integer) fields[3 + sortKeysTotalLength * 2 + i]).intValue();

			}
			locales = new ULocale[sortStrength.length];
			for (int i = 0; i < sortStrength.length; i++) {
				Object locale = fields[2 + sortKeysTotalLength * 3 + i];
				if (locale != null) {
					locales[i] = new ULocale((String) locale);
				}
			}
		}

		groupBoundaryInfo.setSortCondition(sortKeys, sortDirections, sortStrength, locales);

		groupBoundaryInfo.setAccepted(((Boolean) fields[fields.length - 1]).booleanValue());

		return groupBoundaryInfo;
	}
}
