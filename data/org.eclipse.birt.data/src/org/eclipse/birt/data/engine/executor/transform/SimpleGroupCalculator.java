/*
 *************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.executor.transform;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.aggregation.IProgressiveAggregationHelper;
import org.eclipse.birt.data.engine.executor.cache.RowResultSet;
import org.eclipse.birt.data.engine.executor.transform.group.GroupBy;
import org.eclipse.birt.data.engine.executor.transform.group.GroupInfo;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.odi.IAggrInfo;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

public class SimpleGroupCalculator implements IGroupCalculator {
	private IResultObject previous;
	private IResultObject next;
	private IResultObject current;
	private GroupBy[] groupBys;
	private Integer[] groupInstanceIndex;
	private int[] latestAggrAvailableIndex;
	private StreamManager streamManager;
	private RAOutputStream[] groupOutput;
	private RAOutputStream[] aggrRAOutput;
	private DataOutputStream[] aggrOutput;
	private DataOutputStream[] aggrIndexOutput;
	// Output Stream for Overall index + Running index
	private RAOutputStream combinedAggrIndexRAOutput;
	private DataOutputStream combinedAggrIndexOutput;
	private RAOutputStream combinedAggrRAOutput;
	private DataOutputStream combinedAggrOutput;

	private IProgressiveAggregationHelper aggrHelper;
	private List<List<String>> groupAggrs;
	private List<String> runningAggrs;
	private List<String> overallAggrs;
	private GroupInfo[] previousGroupInstances;
	private Object[][] previousGroupAggrs;
	private Object[] previousRunningAggrs;
	private Object[] previousOverallAggrs;

	public SimpleGroupCalculator(DataEngineSession session, GroupSpec[] groups, IResultClass rsMeta)
			throws DataException {
		this.groupBys = new GroupBy[groups.length];
		this.latestAggrAvailableIndex = new int[groups.length];
		Arrays.fill(this.latestAggrAvailableIndex, -1);
		for (int i = 0; i < groups.length; ++i) {
			int keyIndex = groups[i].getKeyIndex();
			String keyColumn = groups[i].getKeyColumn();

			// Convert group key name to index for faster future access
			// assume priority of keyColumn is higher than keyIndex
			if (keyColumn != null)
				keyIndex = rsMeta.getFieldIndex(keyColumn);

			this.groupBys[i] = GroupBy.newInstance(groups[i], keyIndex, keyColumn, rsMeta.getFieldValueClass(keyIndex));
		}
		this.groupInstanceIndex = new Integer[groupBys.length];
		Arrays.fill(this.groupInstanceIndex, 0);
		this.groupAggrs = new ArrayList<List<String>>();
		this.runningAggrs = new ArrayList<String>();
		this.overallAggrs = new ArrayList<String>();
		this.aggrOutput = new DataOutputStream[0];
		for (int i = 0; i < groups.length; i++) {
			this.groupAggrs.add(new ArrayList<String>());
		}
	}

	public void setAggrHelper(IProgressiveAggregationHelper aggrHelper) throws DataException {
		this.aggrHelper = aggrHelper;
		for (String aggrName : this.aggrHelper.getAggrNames()) {
			IAggrInfo aggrInfo = this.aggrHelper.getAggrInfo(aggrName);

			if (aggrInfo.getAggregation().getType() == IAggrFunction.RUNNING_AGGR) {
				this.runningAggrs.add(aggrName);
			} else if (aggrInfo.getGroupLevel() == 0) {
				this.overallAggrs.add(aggrName);
			} else if (this.aggrHelper.getAggrInfo(aggrName).getGroupLevel() <= this.groupAggrs.size()) {
				this.groupAggrs.get(this.aggrHelper.getAggrInfo(aggrName).getGroupLevel() - 1).add(aggrName);
			}
		}
	}

	private int getBreakingGroup(IResultObject obj1, IResultObject obj2) throws DataException {
		if (obj1 == null)
			return 0;

		if (obj2 == null)
			return 0;

		for (int i = 0; i < this.groupBys.length; i++) {
			int columnIndex = groupBys[i].getColumnIndex();
			if (!groupBys[i].isInSameGroup(obj1.getFieldValue(columnIndex), obj2.getFieldValue(columnIndex))) {
				return i + 1;
			}
		}

		return this.groupBys.length + 1;
	}

	public int getStartingGroup() throws DataException {
		return this.getBreakingGroup(previous, current);
	}

	public int getEndingGroup() throws DataException {
		return this.getBreakingGroup(current, next);
	}

	public void registerPreviousResultObject(IResultObject previous) {
		this.previous = previous;
	}

	public void registerCurrentResultObject(IResultObject current) {
		this.current = current;
	}

	public void registerNextResultObject(RowResultSet rowResultSet) throws DataException {
		this.next = rowResultSet.getNext();
	}

	private void saveGroupInfo(GroupInfo group, int level, int rowId) throws DataException {
		try {
			if (this.streamManager != null) {
				IOUtil.writeInt(this.groupOutput[level], group.parent);
				IOUtil.writeInt(this.groupOutput[level], group.firstChild);
				// Add this per report engine's request.
				// See ted 41188
				this.groupOutput[level].flush();
				/*
				 * tobePrint += "[" + level + ":" + group.firstChild + "," + group.parent + "]";
				 */
			}
		} catch (IOException ioex) {
			throw new DataException(ioex.getLocalizedMessage(), ioex);
		}
	}

	private void savePreviousGroupAggrs(int level, int rowId) throws DataException {
		try {
			if (this.streamManager != null && this.previous != null) {
				saveToAggrValuesToDocument(level, rowId);
			}
		} catch (IOException ioex) {
			throw new DataException(ioex.getLocalizedMessage(), ioex);
		}
	}

	private void savePreviousOverallAggrs() throws DataException {
		if (previousOverallAggrs != null && this.streamManager != null) {
			try {
				this.combinedAggrIndexRAOutput.seek(0);
				IOUtil.writeLong(this.combinedAggrIndexOutput, this.combinedAggrRAOutput.getOffset());
				for (int i = 0; i < previousOverallAggrs.length; i++) {
					IOUtil.writeObject(this.combinedAggrOutput, previousOverallAggrs[i]);
				}
				previousOverallAggrs = null;
			} catch (IOException ioex) {
				throw new DataException(ioex.getLocalizedMessage(), ioex);
			}
		}
	}

	private void savePreviousRunningAggrs() throws DataException {
		if (previousRunningAggrs != null && this.streamManager != null) {
			try {
				for (int i = 0; i < this.previousRunningAggrs.length; i++) {
					IOUtil.writeObject(this.combinedAggrOutput, previousRunningAggrs[i]);
				}
				IOUtil.writeLong(this.combinedAggrIndexOutput, this.combinedAggrRAOutput.getOffset());
				previousRunningAggrs = null;
			} catch (IOException ioex) {
				throw new DataException(ioex.getLocalizedMessage(), ioex);
			}
		}
	}

	private void savePreviousGroupInfos() throws DataException {
		if (previousGroupInstances != null && this.streamManager != null) {
			for (int level = 0; level < groupInstanceIndex.length; level++) {
				saveGroupInfo(previousGroupInstances[level], level, 0);
			}
			previousGroupInstances = null;
		}
	}

	private void savePreviousGroupAggrs() throws DataException {
		if (this.previousGroupAggrs != null && this.streamManager != null) {
			try {
				for (int i = 0; i < this.previousGroupAggrs.length; i++) {
					if (this.previousGroupAggrs[i] == null)
						continue;
					for (int j = 0; j < this.previousGroupAggrs[i].length; j++) {
						IOUtil.writeObject(this.aggrOutput[i], previousGroupAggrs[i][j]);
					}
					if (this.aggrIndexOutput[i] != null) {
						IOUtil.writeLong(this.aggrIndexOutput[i], this.aggrRAOutput[i].getOffset());
					}
				}
				previousGroupAggrs = null;
			} catch (IOException ex) {
				throw new DataException(ex.getLocalizedMessage(), ex);
			}
		}
	}

	/**
	 * Do grouping, and fill group indexes
	 * 
	 * @param stopsign
	 * @throws DataException
	 */
	public void next(int rowId) throws DataException {
		savePreviousGroupInfos();
		savePreviousGroupAggrs();
		savePreviousRunningAggrs();
		savePreviousOverallAggrs();

		// breakLevel is the outermost group number to differentiate row
		// data
		int breakLevel;
		if (this.previous == null) {
			breakLevel = 0; // Special case for first row
			if (this.streamManager == null) {
				this.previousGroupInstances = new GroupInfo[groupBys.length];
				this.previousRunningAggrs = runningAggrs.size() > 0 ? new Object[runningAggrs.size()] : null;
				this.previousOverallAggrs = overallAggrs.size() > 0 && this.next == null
						? new Object[overallAggrs.size()]
						: null;
				if (this.next == null) {
					this.previousGroupAggrs = new Object[groupBys.length][];
					for (int i = 0; i < groupBys.length; i++) {
						if (!groupAggrs.get(i).isEmpty()) {
							this.previousGroupAggrs[i] = new Object[groupAggrs.get(i).size()];
						}
					}
				}
			}
		} else {
			breakLevel = getBreakLevel(this.current, this.previous);
		}

		// String tobePrint = "";
		try {
			// Create a new group in each group level between
			// [ breakLevel ... groupDefs.length - 1]
			for (int level = breakLevel; level < groupInstanceIndex.length; level++) {
				GroupInfo group = new GroupInfo();
				if (previousGroupInstances != null)
					previousGroupInstances[level] = group;

				if (level != 0)
					group.parent = groupInstanceIndex[level - 1] - 1;
				if (level == groupInstanceIndex.length - 1) {
					// at leaf group level, first child is the first row, which
					// is current row
					group.firstChild = rowId;
				} else {
					// Our first child is the group to be created at the next
					// level
					// in the next loop
					group.firstChild = groupInstanceIndex[level + 1];
				}

				groupInstanceIndex[level]++;

				saveGroupInfo(group, level, rowId);
				savePreviousGroupAggrs(level, rowId);
			}

			this.aggrHelper.onRow(this.getStartingGroup(), this.getEndingGroup(), this.current, rowId);

			for (int i = 0; previousRunningAggrs != null && i < this.runningAggrs.size(); i++) {
				previousRunningAggrs[i] = this.aggrHelper.getLatestAggrValue(this.runningAggrs.get(i));
			}

			if (this.runningAggrs.size() > 0 && this.combinedAggrOutput != null && this.combinedAggrRAOutput != null
					&& this.combinedAggrIndexOutput != null) {
				for (String aggrName : this.runningAggrs) {
					IOUtil.writeObject(this.combinedAggrOutput, this.aggrHelper.getLatestAggrValue(aggrName));
				}

				IOUtil.writeLong(this.combinedAggrIndexOutput, this.combinedAggrRAOutput.getOffset());

			}

			if (this.next == null) {
				for (int i = 0; i < this.aggrOutput.length; i++) {
					saveToAggrValuesToDocument(i, rowId);
				}

				if (this.overallAggrs.size() > 0 && this.combinedAggrIndexOutput != null
						&& this.combinedAggrIndexRAOutput != null && this.combinedAggrRAOutput != null
						&& this.combinedAggrOutput != null) {
					this.combinedAggrIndexRAOutput.seek(0);
					IOUtil.writeLong(this.combinedAggrIndexOutput, this.combinedAggrRAOutput.getOffset());
					for (String aggrName : overallAggrs) {
						IOUtil.writeObject(this.combinedAggrOutput, this.aggrHelper.getLatestAggrValue(aggrName));
					}
				}

				for (int i = 0; previousOverallAggrs != null && i < this.overallAggrs.size(); i++) {
					this.previousOverallAggrs[i] = this.aggrHelper.getLatestAggrValue(this.overallAggrs.get(i));
				}

				for (int i = 0; previousGroupAggrs != null && i < previousGroupAggrs.length; i++) {
					if (previousGroupAggrs[i] == null)
						continue;
					for (int j = 0; j < groupAggrs.get(i).size(); j++) {
						previousGroupAggrs[i][j] = this.aggrHelper.getLatestAggrValue(groupAggrs.get(i).get(j));
					}
				}
			}
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
		/*
		 * if ( !tobePrint.isEmpty( ) ) System.out.println( tobePrint );
		 */

	}

	private void saveToAggrValuesToDocument(int i, int rowId) throws IOException, DataException {
		if (this.aggrOutput[i] != null) {
			for (String aggrName : this.groupAggrs.get(i)) {
				IOUtil.writeObject(this.aggrOutput[i], this.aggrHelper.getLatestAggrValue(aggrName));
			}
			if (this.aggrIndexOutput[i] != null) {
				IOUtil.writeLong(this.aggrIndexOutput[i], this.aggrRAOutput[i].getOffset());
			}
		}
		this.latestAggrAvailableIndex[i] = rowId - 1;

	}

	/**
	 * Helper method to get the group break level between 2 rows
	 * 
	 * @param currRow
	 * @param prevRow
	 * @return
	 * @throws DataException
	 */
	private int getBreakLevel(IResultObject currRow, IResultObject prevRow) throws DataException {
		assert currRow != null;
		assert prevRow != null;

		int breakLevel = 0;
		for (; breakLevel < this.groupBys.length; breakLevel++) {
			int colIndex = this.groupBys[breakLevel].getColumnIndex();

			Object currObjectValue = null;
			Object prevObjectValue = null;
			if (colIndex >= 0) {
				currObjectValue = currRow.getFieldValue(colIndex);
				prevObjectValue = prevRow.getFieldValue(colIndex);
			}

			GroupBy groupBy = this.groupBys[breakLevel];
			if (!groupBy.isInSameGroup(currObjectValue, prevObjectValue)) {
				// current group is the break level
				// reset the groupBys of the inner groups within current group for the following
				// compare
				for (int i = breakLevel + 1; i < this.groupBys.length; i++) {
					this.groupBys[i].reset();
				}
				break;
			}
		}
		return breakLevel;
	}

	public void close() throws DataException {
		try {
			savePreviousGroupInfos();
			savePreviousGroupAggrs();
			savePreviousRunningAggrs();
			savePreviousOverallAggrs();

			if (this.groupOutput != null) {
				for (int i = 0; i < this.groupOutput.length; i++) {
					this.groupOutput[i].seek(0);
					IOUtil.writeInt(this.groupOutput[i], this.groupInstanceIndex[i]);
					this.groupOutput[i].close();
				}
				this.groupOutput = null;
			}
			if (this.aggrOutput != null) {
				for (int i = 0; i < this.aggrOutput.length; i++) {
					if (this.aggrOutput[i] != null)
						this.aggrOutput[i].close();
				}
				this.aggrOutput = null;
			}
			if (this.aggrIndexOutput != null) {
				for (int i = 0; i < this.aggrIndexOutput.length; i++) {
					if (this.aggrIndexOutput[i] != null)
						this.aggrIndexOutput[i].close();
				}
				this.aggrIndexOutput = null;
			}
			if (this.overallAggrs.size() > 0 && this.combinedAggrIndexOutput != null) {
				this.combinedAggrIndexRAOutput.close();
				this.combinedAggrOutput.close();
			}
			if (this.aggrHelper != null) {
				this.aggrHelper.close();
				this.aggrHelper = null;
			}
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	public void doSave(StreamManager manager) throws DataException {
		try {
			this.streamManager = manager;
			if (this.streamManager != null) {
				this.groupOutput = new RAOutputStream[this.groupBys.length];
				this.aggrOutput = new DataOutputStream[this.groupBys.length];
				this.aggrRAOutput = new RAOutputStream[this.groupBys.length];
				this.aggrIndexOutput = new DataOutputStream[this.groupBys.length];

				if (this.overallAggrs.size() > 0 || this.runningAggrs.size() > 0) {
					this.combinedAggrIndexRAOutput = (RAOutputStream) streamManager.getOutStream(
							DataEngineContext.COMBINED_AGGR_INDEX_STREAM, StreamManager.ROOT_STREAM,
							StreamManager.BASE_SCOPE);
					this.combinedAggrRAOutput = (RAOutputStream) streamManager.getOutStream(
							DataEngineContext.COMBINED_AGGR_VALUE_STREAM, StreamManager.ROOT_STREAM,
							StreamManager.BASE_SCOPE);
					this.combinedAggrOutput = new DataOutputStream(this.combinedAggrRAOutput);
					this.combinedAggrIndexOutput = new DataOutputStream(this.combinedAggrIndexRAOutput);

					// write place holder for Overall aggregation index
					IOUtil.writeLong(this.combinedAggrIndexOutput, -1);

					// write the size of overall aggregation
					IOUtil.writeInt(this.combinedAggrOutput, this.overallAggrs.size());
					for (int i = 0; i < this.overallAggrs.size(); i++) {
						IOUtil.writeString(this.combinedAggrOutput, this.overallAggrs.get(i));
					}

					IOUtil.writeInt(this.combinedAggrOutput, this.runningAggrs.size());
					for (int i = 0; i < this.runningAggrs.size(); i++) {
						IOUtil.writeString(this.combinedAggrOutput, this.runningAggrs.get(i));
					}

					// write the starting index of first running aggregation
					IOUtil.writeLong(this.combinedAggrIndexOutput, this.combinedAggrRAOutput.getOffset());
				}

				for (int i = 0; i < this.groupBys.length; i++) {

					this.groupOutput[i] = (RAOutputStream) streamManager
							.getOutStream(DataEngineContext.PROGRESSIVE_VIEWING_GROUP_STREAM, i);

					IOUtil.writeInt(this.groupOutput[i], Integer.MAX_VALUE);
					if (!this.groupAggrs.get(i).isEmpty()) {
						this.aggrRAOutput[i] = streamManager.getOutStream(DataEngineContext.AGGR_VALUE_STREAM, i);
						this.aggrIndexOutput[i] = new DataOutputStream(
								streamManager.getOutStream(DataEngineContext.AGGR_INDEX_STREAM, i));
						this.aggrOutput[i] = new DataOutputStream(this.aggrRAOutput[i]);
						// The group level
						IOUtil.writeInt(this.aggrOutput[i], i + 1);
						// The number of aggregations in the group
						IOUtil.writeInt(this.aggrOutput[i], this.groupAggrs.get(i).size());
						for (String aggrName : this.groupAggrs.get(i)) {
							IOUtil.writeString(new DataOutputStream(this.aggrOutput[i]), aggrName);
						}
						IOUtil.writeLong(this.aggrIndexOutput[i], this.aggrRAOutput[i].getOffset());
					}

				}
			}
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	public boolean isAggrAtIndexAvailable(String aggrName, int currentIndex) throws DataException {
		assert this.aggrHelper != null;
		if (this.aggrHelper.getAggrInfo(aggrName).getAggregation().getType() == IAggrFunction.RUNNING_AGGR)
			return true;
		if (this.aggrHelper.getAggrInfo(aggrName).getGroupLevel() == 0)
			return this.current == null;
		return this.latestAggrAvailableIndex[this.aggrHelper.getAggrInfo(aggrName).getGroupLevel() - 1] >= currentIndex;

	}

	public Integer[] getGroupInstanceIndex() {
		return this.groupInstanceIndex;
	}
}
