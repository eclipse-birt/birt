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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;

public class ProgressiveViewingRDGroupUtil implements IRDGroupUtil {
	private CacheProvider cacheProvider;
	private RAInputStream inputStream;
	private List<int[]> groupStartingEndingIndex;
	private int groupCount;

	public ProgressiveViewingRDGroupUtil(RAInputStream groupStream) throws DataException {
		this.inputStream = groupStream;
		try {
			this.groupCount = IOUtil.readInt(groupStream);
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	public void setCacheProvider(CacheProvider cacheProvider) {
		this.cacheProvider = cacheProvider;

	}

	public void next(boolean hasNext) throws DataException {
		// Do nothing
	}

	public int getCurrentGroupIndex(int groupLevel) throws DataException {
		throw new UnsupportedOperationException();
	}

	public void move() throws DataException {
		// Do nothing
	}

	private int getEndingGroupLevel(int index) throws DataException {
		try {
			this.inputStream.seek(((index * 2) + 1) * IOUtil.INT_LENGTH + IOUtil.INT_LENGTH);
			int result = IOUtil.readInt(this.inputStream);
			return result;

		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	private int getStartingGroupLevel(int index) throws DataException {
		try {
			this.inputStream.seek(index * 2 * IOUtil.INT_LENGTH + IOUtil.INT_LENGTH);
			int result = IOUtil.readInt(inputStream);
			return result;
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	public void last(int groupLevel) throws DataException {
		if (this.getEndingGroupLevel() <= groupLevel)
			return;
		else {
			while (this.cacheProvider.next()) {
				if (this.getEndingGroupLevel() <= groupLevel)
					return;
			}
		}

	}

	public void close() throws DataException {
		try {
			if (this.inputStream != null) {
				// TODO: Drop the stream;
				this.inputStream.close();
				this.inputStream = null;
			}
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	public int[] getGroupStartAndEndIndex(int groupIndex) throws DataException {
		if (this.groupStartingEndingIndex != null)
			return this.groupStartingEndingIndex.get(groupIndex);
		this.groupStartingEndingIndex = new ArrayList<int[]>();
		List<List<Integer>> temp = new ArrayList<List<Integer>>();
		for (int i = 0; i <= this.groupCount; i++)
			temp.add(new ArrayList<Integer>());
		for (int i = 0; i < this.cacheProvider.getCount(); i++) {
			int starting = this.getStartingGroupLevel(i);
			int ending = this.getEndingGroupLevel(i);
			for (int j = starting; j < temp.size(); j++) {
				temp.get(j).add(i);
			}
			for (int j = ending; j < temp.size(); j++) {
				temp.get(j).add(i + 1);
			}
		}

		for (int i = 0; i < temp.size(); i++) {
			List<Integer> tempArray = temp.get(i);
			int[] startingEnding = new int[tempArray.size()];
			for (int j = 0; j < tempArray.size(); j++)
				startingEnding[j] = tempArray.get(j);
			this.groupStartingEndingIndex.add(startingEnding);
		}
		return this.groupStartingEndingIndex.get(groupIndex);
	}

	public int getEndingGroupLevel() throws DataException {
		return this.getEndingGroupLevel(this.cacheProvider.getCurrentIndex());
	}

	public int getStartingGroupLevel() throws DataException {
		return this.getStartingGroupLevel(this.cacheProvider.getCurrentIndex());
	}

	public List[] getGroups() throws DataException {
		return null;
	}

}
