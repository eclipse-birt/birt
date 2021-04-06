
/*******************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.document;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class ProgressiveViewingRDAggrUtil implements IRDAggrUtil {
	private Map<String, IAggrStorageInfo> aggrInfo;

	public ProgressiveViewingRDAggrUtil(RAInputStream combinedAggrIndex, RAInputStream combinedAggr,
			List<RAInputStream> aggrIndexStreams, List<RAInputStream> aggrStreams) throws DataException {
		try {
			assert aggrIndexStreams.size() == aggrStreams.size();
			this.aggrInfo = new HashMap<String, IAggrStorageInfo>();
			for (int i = 0; i < aggrIndexStreams.size(); i++) {
				AggrStorageInfo asi = new AggrStorageInfo(aggrIndexStreams.get(i), aggrStreams.get(i));
				for (int j = 0; j < asi.aggrNames.length; j++) {
					this.aggrInfo.put(asi.aggrNames[j], asi);
				}
			}
			if (combinedAggrIndex != null) {
				RunningAggrStorageInfo running = new RunningAggrStorageInfo(combinedAggrIndex, combinedAggr);
				for (int j = 0; j < running.getAggrNames().length; j++) {
					this.aggrInfo.put(running.getAggrNames()[j], running);
				}

				OverallAggrStorageInfo overall = new OverallAggrStorageInfo(combinedAggrIndex, combinedAggr);
				for (int j = 0; j < overall.getAggrNames().length; j++) {
					this.aggrInfo.put(overall.getAggrNames()[j], overall);
				}
			}
		} catch (Exception e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	interface IAggrStorageInfo {
		public Object getAggrValue(String aggrName, int groupInstanceIndex) throws DataException;

		public String[] getAggrNames();

		public int getGroupLevel();

		public void close();
	}

	private class OverallAggrStorageInfo implements IAggrStorageInfo {
		private String[] overallAggregations;
		private Map<String, Object> overallAggregationValues;
		private RAInputStream aggrStream;
		private DataInputStream aggrDIStream;
		private RAInputStream aggrIndexStream;
		private DataInputStream aggrIndexDIStream;

		public OverallAggrStorageInfo(RAInputStream aggrIndexStream, RAInputStream aggrStream) throws DataException {
			try {
				this.aggrStream = aggrStream;
				this.aggrDIStream = new DataInputStream(aggrStream);
				this.aggrIndexStream = aggrIndexStream;
				this.aggrIndexDIStream = new DataInputStream(aggrIndexStream);
				this.aggrStream.seek(0);
				int overallAggrSize = IOUtil.readInt(this.aggrDIStream);
				this.overallAggregations = new String[overallAggrSize];

				for (int i = 0; i < this.overallAggregations.length; i++) {
					this.overallAggregations[i] = IOUtil.readString(this.aggrDIStream);
				}

			} catch (IOException e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
		}

		private long getOverallAggrOffset() throws DataException {
			try {
				this.aggrIndexStream.seek(0);
				return IOUtil.readLong(this.aggrIndexDIStream);
			} catch (IOException e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
		}

		public Object getAggrValue(String aggrName, int groupInstanceIndex) throws DataException {
			try {
				if (this.overallAggregationValues != null)
					return this.overallAggregationValues.get(aggrName);

				long overallOffset = 0;

				if ((overallOffset = this.getOverallAggrOffset()) == -1) {
					// assume the progressive viewing is not finish yet. Return null;
					return null;
				}
				this.overallAggregationValues = new HashMap<String, Object>();
				this.aggrStream.seek(overallOffset);
				for (int i = 0; i < this.overallAggregations.length; i++) {
					this.overallAggregationValues.put(this.overallAggregations[i],
							IOUtil.readObject(this.aggrDIStream));
				}
				return this.overallAggregationValues.get(aggrName);
			} catch (Exception e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
		}

		public String[] getAggrNames() {
			return this.overallAggregations;
		}

		public int getGroupLevel() {
			return 0;
		}

		public void close() {
			try {
				this.aggrDIStream.close();
				this.aggrIndexDIStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class RunningAggrStorageInfo implements IAggrStorageInfo {
		private RAInputStream aggrStream;
		private DataInputStream aggrDIStream;
		private RAInputStream aggrIndexStream;
		private DataInputStream aggrIndexDIStream;

		private String[] runningAggregations;
		private Map<String, Object> runningAggregationValues;

		private int currentIndex;

		public RunningAggrStorageInfo(RAInputStream aggrIndexStream, RAInputStream aggrStream) throws DataException {
			try {
				this.aggrStream = aggrStream;
				this.aggrDIStream = new DataInputStream(aggrStream);
				this.aggrIndexStream = aggrIndexStream;
				this.aggrIndexDIStream = new DataInputStream(aggrIndexStream);
				this.aggrIndexStream.seek(0);

				this.currentIndex = -1;

				int overallAggrSize = IOUtil.readInt(this.aggrDIStream);

				for (int i = 0; i < overallAggrSize; i++) {
					IOUtil.readString(this.aggrDIStream);
				}

				int runningAggrSize = IOUtil.readInt(this.aggrDIStream);
				this.runningAggregations = new String[runningAggrSize];
				for (int i = 0; i < this.runningAggregations.length; i++) {
					this.runningAggregations[i] = IOUtil.readString(this.aggrDIStream);
				}
				this.runningAggregationValues = new HashMap<String, Object>();
			} catch (IOException e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
		}

		public Object getAggrValue(String aggrName, int groupInstanceIndex) throws DataException {
			try {
				if (this.currentIndex == groupInstanceIndex && this.runningAggregationValues.containsKey(aggrName))
					return this.runningAggregationValues.get(aggrName);

				this.currentIndex = groupInstanceIndex;
				this.aggrIndexStream.seek(IOUtil.LONG_LENGTH * (this.currentIndex + 1));
				long offset = IOUtil.readLong(this.aggrIndexDIStream);
				this.aggrStream.seek(offset);
				for (int i = 0; i < this.runningAggregations.length; i++) {
					this.runningAggregationValues.put(this.runningAggregations[i],
							IOUtil.readObject(this.aggrDIStream));
				}
				return this.runningAggregationValues.get(aggrName);
			} catch (IOException e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
		}

		public String[] getAggrNames() {
			return this.runningAggregations;
		}

		public int getGroupLevel() {
			return -1;
		}

		public void close() {
			try {
				this.aggrDIStream.close();
				this.aggrIndexDIStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class AggrStorageInfo implements IAggrStorageInfo {
		private HashMap<String, Object> aggrNameValueMap;
		private String[] aggrNames;
		private int currentGroupLevel;
		private RAInputStream aggrStream;
		private DataInputStream aggrDIStream;
		private RAInputStream aggrIndexStream;
		private DataInputStream aggrIndexDIStream;
		private int groupLevel;

		AggrStorageInfo(RAInputStream aggrIndexStream, RAInputStream aggrStream) throws DataException {
			try {
				this.currentGroupLevel = -1;
				this.aggrStream = aggrStream;
				this.aggrDIStream = new DataInputStream(aggrStream);
				this.aggrIndexStream = aggrIndexStream;
				this.aggrIndexDIStream = new DataInputStream(aggrIndexStream);
				this.groupLevel = IOUtil.readInt(this.aggrDIStream);
				this.aggrNames = new String[IOUtil.readInt(this.aggrDIStream)];
				this.aggrNameValueMap = new HashMap<String, Object>();
				for (int i = 0; i < this.aggrNames.length; i++) {
					this.aggrNames[i] = IOUtil.readString(this.aggrDIStream);
				}
			} catch (IOException e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
		}

		public Object getAggrValue(String aggrName, int groupInstanceIndex) throws DataException {
			try {
				if (groupInstanceIndex != this.currentGroupLevel) {
					this.currentGroupLevel = groupInstanceIndex;
					if (this.aggrIndexStream.length() < IOUtil.LONG_LENGTH * this.currentGroupLevel) {
						throw new DataException("The aggregation results are not ready yet.");
					}

					this.aggrIndexStream.seek(IOUtil.LONG_LENGTH * this.currentGroupLevel);
					long offset = IOUtil.readLong(this.aggrIndexDIStream);
					this.aggrStream.seek(offset);
					for (int i = 0; i < this.aggrNames.length; i++) {
						this.aggrNameValueMap.put(this.aggrNames[i], IOUtil.readObject(this.aggrDIStream));
					}

				}
			} catch (Exception e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
			return this.aggrNameValueMap.get(aggrName);
		}

		public String[] getAggrNames() {
			return this.aggrNames;
		}

		public int getGroupLevel() {
			return this.groupLevel;
		}

		public void close() {
			try {
				this.aggrDIStream.close();
				this.aggrIndexDIStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public boolean contains(String aggrName) {
		return this.aggrInfo.containsKey(aggrName);
	}

	public int getGroupLevel(String aggrName) {
		return this.aggrInfo.get(aggrName).getGroupLevel();
	}

	public boolean isRunningAggr(String aggrName) {
		return this.aggrInfo.get(aggrName) instanceof RunningAggrStorageInfo;
	}

	public Object getValue(String aggrName, int groupInstanceIndex) throws DataException {
		return this.aggrInfo.get(aggrName).getAggrValue(aggrName, groupInstanceIndex);
	}

	public void close() throws DataException {
		for (IAggrStorageInfo info : this.aggrInfo.values()) {
			info.close();
		}

	}

}
