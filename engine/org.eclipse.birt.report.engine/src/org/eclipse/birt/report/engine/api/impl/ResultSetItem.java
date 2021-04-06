/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.impl;

import java.util.Locale;

import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportElementHandle;

import com.ibm.icu.util.ULocale;

public class ResultSetItem implements IResultSetItem {
	/*
	 * the result set display name
	 */
	private String resultSetName;
	/*
	 * the result set meta data, which contains only column name and column count.
	 */
	private IResultMetaData resultSetMetaData;
	/*
	 * handle is used for localization
	 */
	private DesignElementHandle handle;
	private Locale locale;

	/*
	 * prevent default construction.
	 */
	private ResultSetItem() {

	}

	/**
	 * construct result set meta data from result name and IResultMetaData
	 * 
	 * @param resultSetName
	 * @param metaData
	 */
	public ResultSetItem(String resultSetName, IResultMetaData metaData) {
		this.resultSetName = resultSetName;
		resultSetMetaData = metaData;
	}

	public ResultSetItem(String resultSetName, IResultMetaData metaData, DesignElementHandle handle, Locale loc) {
		this.resultSetName = resultSetName;
		resultSetMetaData = metaData;
		this.handle = handle;
		this.locale = loc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.impl.IResultSetItem#getResultSetName()
	 */
	public String getResultSetName() {
		return resultSetName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.impl.IResultSetItem#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData() {
		return resultSetMetaData;
	}

	public String getResultSetDisplayName() {
		if (handle instanceof ReportElementHandle) {
			ReportElementHandle tmpHandle = (ReportElementHandle) handle;
			if (tmpHandle.getDisplayName() != null) {
				return ModuleUtil.getExternalizedValue(tmpHandle, tmpHandle.getDisplayNameKey(),
						tmpHandle.getDisplayName(), ULocale.forLocale(locale));
			}
		} else if (handle != null) {
			return ModuleUtil.getExternalizedValue(handle, handle.getName(), resultSetName, ULocale.forLocale(locale));
		}
		return resultSetName;
	}

	public DesignElementHandle getHandle() {
		return handle;
	}
}
