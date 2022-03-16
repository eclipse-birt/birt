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

package org.eclipse.birt.data.engine.impl.document;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.impl.document.stream.WrapperedRAInputStream;

/**
 * This class handles the storage of aggregation results in report document.
 */

public class RDAggrUtil implements IRDAggrUtil {

	private HashMap<String, RDAggrValueHolder> holders = new HashMap<>();
	private IBaseQueryDefinition qd;
	private RAInputStream aggrIndexStream;
	private DataInputStream valueStream;

	public RDAggrUtil(StreamManager manager, IBaseQueryDefinition qd) throws DataException {
		this.qd = qd;
		try {
			aggrIndexStream = manager.getInStream(DataEngineContext.AGGR_INDEX_STREAM, StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE);
			int aggrSize = IOUtil.readInt(aggrIndexStream);
			DataInputStream aggrIndexDis = new DataInputStream(aggrIndexStream);
			valueStream = new DataInputStream(new WrapperedRAInputStream(manager.getInStream(
					DataEngineContext.AGGR_VALUE_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE), 0, -1));

			for (int i = 0; i < aggrSize; i++) {
				RDAggrValueHolder holder = new RDAggrValueHolder(valueStream);
				holders.put(holder.getName(), holder);
				if (i < aggrSize - 1) {
					long offset = IOUtil.readLong(aggrIndexDis);
					// for backward compatibilty issue
					if (manager.getVersion() >= VersionManager.VERSION_2_5_2_1) {
						valueStream = new DataInputStream(
								new WrapperedRAInputStream(manager.getInStream(DataEngineContext.AGGR_VALUE_STREAM,
										StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE), offset, -1));
					} else {
						valueStream = new DataInputStream(
								new WrapperedRAInputStream(manager.getInStream(DataEngineContext.AGGR_VALUE_STREAM,
										StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE), offset + 1, -1));
					}
				}
			}
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.IRDAggrUtil#contains(java.lang.
	 * String)
	 */
	@Override
	public boolean contains(String aggrName) {
		return holders.containsKey(aggrName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.IRDAggrUtil#getGroupLevel(java.
	 * lang.String)
	 */
	@Override
	public int getGroupLevel(String aggrName) {
		if (this.contains(aggrName)) {
			return this.holders.get(aggrName).getGroupLevel();
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.IRDAggrUtil#isRunningAggr(java.
	 * lang.String)
	 */
	@Override
	public boolean isRunningAggr(String aggrName) {
		if (this.contains(aggrName)) {
			return this.holders.get(aggrName).isRunningAggr();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.IRDAggrUtil#getValue(java.lang.
	 * String, int)
	 */
	@Override
	public Object getValue(String aggrName, int groupInstanceIndex) throws DataException {
		try {
			if (this.contains(aggrName)) {
				Object value = holders.get(aggrName).get(groupInstanceIndex);

				if (value instanceof BirtException) {
					throw (BirtException) value;
				}

				if (qd != null && qd.getBindings().containsKey(aggrName)) {
					IBinding b = (IBinding) qd.getBindings().get(aggrName);

					// convert the value to the target type got from the original binding
					value = DataTypeUtil.convert(value, b.getDataType());
				}
				return value;
			}

			return null;
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		} catch (BirtException e) {
			if (e instanceof DataException) {
				throw new DataException(e.getErrorCode(), ((DataException) e).getArgument());
			}
			throw new DataException(e.getErrorCode(), e);
		}
	}

	/**
	 *
	 * @author Administrator
	 *
	 */
	private static class RDAggrValueHolder {

		private int groupInstanceIndex = 0;
		private int size = 0;
		private int groupLevel;
		private DataInputStream valueStream;
		private boolean isRunningAggr;

		private String bindingName;
		private Object currentValue;

		public RDAggrValueHolder(DataInputStream valueStream) throws IOException, DataException {
			this.bindingName = IOUtil.readString(valueStream);
			populateRunningAggrInfo(valueStream);
			this.groupLevel = IOUtil.readInt(valueStream);
			this.size = IOUtil.readInt(valueStream);
			this.valueStream = valueStream;
			if (size > 0) {
				this.currentValue = IOUtil.readObject(valueStream, DataEngineSession.getCurrentClassLoader());
			}
		}

		private void populateRunningAggrInfo(DataInputStream valueStream) throws IOException, DataException {
			String aggrName = IOUtil.readString(valueStream);
			if (AggregationManager.getInstance().getAggregation(aggrName) == null) {
				throw new DataException(ResourceConstants.INVALID_AGGR, aggrName);
			}
			this.isRunningAggr = AggregationManager.getInstance().getAggregation(aggrName)
					.getType() == IAggrFunction.RUNNING_AGGR;
		}

		public String getName() {
			return this.bindingName;
		}

		public boolean isRunningAggr() {
			return this.isRunningAggr;
		}

		public int getGroupLevel() {
			return this.groupLevel;
		}

		public Object get(int index) throws IOException {
			if (index == groupInstanceIndex) {
				return currentValue;
			}
			// If try to go backward, simply return null;
			if (index < groupInstanceIndex || index >= size) {
				return null;
			}
			while (groupInstanceIndex < index) {
				this.currentValue = IOUtil.readObject(valueStream, DataEngineSession.getCurrentClassLoader());
				groupInstanceIndex++;
			}

			return this.currentValue;
		}

		public void close() throws IOException {
			if (valueStream != null) {
				valueStream.close();
			}
		}
	}

	@Override
	public void close() throws DataException {
		try {
			if (!this.holders.isEmpty()) {
				Collection<RDAggrValueHolder> values = this.holders.values();
				Iterator<RDAggrValueHolder> iter = values.iterator();
				while (iter.hasNext()) {
					iter.next().close();
				}
			}
			if (aggrIndexStream != null) {
				aggrIndexStream.close();
			}
		} catch (IOException ex) {
		}
	}
}
