/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.aggregation.impl.rank;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.data.aggregation.impl.RunningAccumulator;
import org.eclipse.birt.data.engine.api.DataEngineThreadLocal;
import org.eclipse.birt.data.engine.api.ICloseListener;
import org.eclipse.birt.data.engine.cache.BasicCachedArray;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * The most common part of all Top and Bottom accumulator.
 * 
 */
public abstract class BaseTopBottomAccumulator extends RunningAccumulator implements ICloseListener {

	//
	protected BasicCachedListExt cachedValues;

	private double N;
	private int passNo = 0;
	private BasicCachedArray targetValue;
	private int currentIndex = -1;
	private Object value = null;
	private static Boolean trueValue = Boolean.TRUE;
	private static Boolean falseValue = Boolean.FALSE;
	private String tempDir;

	public BaseTopBottomAccumulator() {
		if (DataEngineThreadLocal.getInstance().getPathManager() != null) {
			tempDir = DataEngineThreadLocal.getInstance().getPathManager().getTempFileName("AggregationPlugin_temp",
					this.hashCode(), null) + File.separator;
		} else {
			tempDir = System.getProperty("java.io.tmpdir") + "AggregationPlugin_temp" + this.hashCode()
					+ File.separator;
		}
		targetValue = new BasicCachedArray(tempDir, 0);
		DataEngineThreadLocal.getInstance().getCloseListener().add(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.Accumulator#start()
	 */
	public void start() throws DataException {
		super.start();
		passNo++;

		if (passNo == 1) {
			cachedValues = new BasicCachedListExt(tempDir);
			N = -1;
		} else {
			this.targetValue = this.getTargetValueIndex();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
	 */
	private BasicCachedArray getTargetValueIndex() throws DataException {
		int n = adjustNValue(N);
		BasicCachedArray result = new BasicCachedArray(tempDir,
				(int) (n < cachedValues.size() ? n : cachedValues.size()));

		for (int i = 0; i < n && i < cachedValues.size(); i++) {
			int ind = getNextIndex();
			if (ind == -1)
				return result;

			result.set(i, Integer.valueOf(ind));
		}
		this.cachedValues = null;
		return result;
	}

	public Object getValue() throws DataException {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[]
	 * )
	 */
	public void onRow(Object[] args) throws DataException {
		assert (args.length == 2);
		if (passNo == 1) {
			if (args[0] != null) {
				cachedValues.add(args[0]);
			} else {
				cachedValues.add(RankAggregationUtil.getNullObject());
			}
			if (N == -1) {
				if (args.length < 2)
					throw new DataException(ResourceConstants.INVALID_TOP_BOTTOM_N_ARGUMENT);
				N = populateNValue(args[1]);
			}
		} else {
			this.currentIndex++;
			this.value = populateValue();
		}
	}

	/**
	 * Populate the return value.
	 * 
	 * @return
	 */
	private Boolean populateValue() {
		for (int i = 0; i < this.targetValue.length(); i++) {
			if (this.currentIndex == ((Integer) this.targetValue.get(i)).intValue())
				return trueValue;
		}

		return falseValue;
	}

	/**
	 * Get index of next topmost or bottommost value in cachedValues.
	 * 
	 * @return
	 * @throws DataException
	 */
	protected abstract int getNextIndex() throws DataException;

	/**
	 * Populate the N value get from argument.
	 * 
	 * @param N
	 * @return
	 * @throws DataException
	 */
	protected abstract double populateNValue(Object N) throws DataException;

	/**
	 * Adjust the N value.
	 * 
	 * @param N
	 * @return
	 */
	protected abstract int adjustNValue(double N);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.ICloseListener#close()
	 */
	public void close() throws IOException {
		File tempFile = new File(tempDir);
		if (!FileSecurity.fileExist(tempFile) || !FileSecurity.fileIsDirectory(tempFile)) {
			return;
		}
		deleteDirectory(tempFile);
	}

	/**
	 * 
	 * @param dir
	 */
	private static void deleteDirectory(File dir) {
		File[] subFiles = FileSecurity.fileListFiles(dir);
		if (subFiles != null) {
			for (int i = 0; i < subFiles.length; i++) {
				if (FileSecurity.fileIsDirectory(subFiles[i])) {
					deleteDirectory(subFiles[i]);
				} else {
					safeDelete(subFiles[i]);
				}
			}
		}
		safeDelete(dir);
	}

	/**
	 * 
	 * @param file
	 */
	private static void safeDelete(File file) {
		if (!FileSecurity.fileDelete(file)) {
			FileSecurity.fileDeleteOnExit(file);
		}
	}
}
