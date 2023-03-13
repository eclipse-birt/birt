/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IExtractionOption;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IStatusHandler;
import org.eclipse.birt.report.engine.api.InstanceID;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class DataExtractionTask implements IDataExtractionTask {

	IDataExtractionTask task;

	TimeZone timeZone = TimeZone.getDefault();

	public DataExtractionTask(ReportEngine engine, IReportDocument reader) throws EngineException {
		String version = reader.getProperty(ReportDocumentConstants.DATA_EXTRACTION_TASK_VERSION_KEY);
		if (ReportDocumentConstants.DATA_EXTRACTION_TASK_VERSION_0.equals(version)) {
			task = new DataExtractionTaskV0(engine, reader);
		} else if (ReportDocumentConstants.DATA_EXTRACTION_TASK_VERSION_1.equals(version)) {
			task = new DataExtractionTaskV1(engine, reader);
		}
	}

	@Override
	public void addScriptableJavaObject(String jsName, Object obj) {
		task.addScriptableJavaObject(jsName, obj);
	}

	@Override
	public void cancel() {
		task.cancel();
	}

	@Override
	public void cancel(String reason) {
		task.cancel(reason);
	}

	@Override
	public void close() {
		task.close();
	}

	@Override
	public IExtractionResults extract() throws EngineException {
		return task.extract();
	}

	@Override
	public Map getAppContext() {
		return task.getAppContext();
	}

	@Override
	public boolean getCancelFlag() {
		return task.getCancelFlag();
	}

	@Override
	public IReportEngine getEngine() {
		return task.getEngine();
	}

	@Override
	public List getErrors() {
		return task.getErrors();
	}

	@Override
	public int getID() {
		return task.getID();
	}

	@Override
	public Locale getLocale() {
		return task.getLocale();
	}

	@Override
	public List getMetaData() throws EngineException {
		return task.getMetaData();
	}

	@Override
	public Object getParameterDisplayText(String name) {
		return task.getParameterDisplayText(name);
	}

	@Override
	public Object getParameterValue(String name) {
		return task.getParameterValue(name);
	}

	@Override
	public HashMap getParameterValues() {
		return task.getParameterValues();
	}

	@Override
	public IReportRunnable getReportRunnable() {
		return task.getReportRunnable();
	}

	@Override
	public List getResultSetList() throws EngineException {
		return task.getResultSetList();
	}

	@Override
	public int getStatus() {
		return task.getStatus();
	}

	@Override
	public ULocale getULocale() {
		return task.getULocale();
	}

	@Override
	public void selectColumns(String[] columnNames) {
		task.selectColumns(columnNames);
	}

	@Override
	public void selectResultSet(String resultSetName) {
		task.selectResultSet(resultSetName);
	}

	@Override
	public void setAppContext(Map context) {
		task.setAppContext(context);
	}

	@Override
	public void setDataSource(IDocArchiveReader dataSource) {
		task.setDataSource(dataSource);
	}

	@Override
	public void setDataSource(IDocArchiveReader dataSource, String reportlet) {
		task.setDataSource(dataSource, reportlet);
	}

	@Override
	public void setFilters(IFilterDefinition[] simpleFilterExpression) {
		task.setFilters(simpleFilterExpression);
	}

	@Override
	public void setSorts(ISortDefinition[] simpleSortExpression) {
		task.setSorts(simpleSortExpression);
	}

	@Override
	public void setMaxRows(int maxRows) {
		task.setMaxRows(maxRows);
	}

	@Override
	public void setInstanceID(InstanceID iid) {
		task.setInstanceID(iid);
	}

	@Override
	public void setLocale(Locale locale) {
		task.setLocale(locale);
	}

	@Override
	public void setLocale(ULocale locale) {
		task.setLocale(locale);
	}

	@Override
	public void setTimeZone(TimeZone timeZone) {
		if (timeZone != null) {
			this.timeZone = timeZone;
		}
		task.setTimeZone(this.timeZone);
	}

	@Override
	public void setParameter(String name, Object value, String displayText) {
		task.setParameter(name, value, displayText);
	}

	@Override
	public void setParameter(String name, Object[] values, String[] displayText) {
		task.setParameter(name, values, displayText);
	}

	@Override
	public void setParameterDisplayText(String name, String displayText) {
		task.setParameterDisplayText(name, displayText);
	}

	@Override
	public void setParameterDisplayText(String name, String[] displayText) {
		task.setParameterDisplayText(name, displayText);
	}

	@Override
	public void setParameterValue(String name, Object value) {
		task.setParameterValue(name, value);
	}

	@Override
	public void setParameterValue(String name, Object[] values) {
		task.setParameterValue(name, values);
	}

	@Override
	public void setParameterValues(Map params) {
		task.setParameterValues(params);
	}

	@Override
	public boolean validateParameters() {
		return task.validateParameters();
	}

	@Override
	public Logger getLogger() {
		return task.getLogger();
	}

	@Override
	public int getTaskType() {
		return task.getTaskType();
	}

	@Override
	public void setErrorHandlingOption(int option) {
		task.setErrorHandlingOption(option);
	}

	@Override
	public void setLogger(Logger logger) {
		task.setLogger(logger);
	}

	@Override
	public void extract(IExtractionOption option) throws BirtException {
		task.extract(option);
	}

	@Override
	public void setStartRow(int startRow) {
		task.setStartRow(startRow);
	}

	@Override
	public void setUserACL(String[] acl) {
		task.setUserACL(acl);
	}

	@Override
	public void setDistinctValuesOnly(boolean distinct) {
		task.setDistinctValuesOnly(distinct);
	}

	@Override
	public void setCubeExportEnabled(boolean isCubeExportEnabled) {
		task.setCubeExportEnabled(isCubeExportEnabled);
	}

	@Override
	public boolean isCubeExportEnabled() {
		return task.isCubeExportEnabled();
	}

	@Override
	public void setProgressMonitor(IProgressMonitor monitor) {
		task.setProgressMonitor(monitor);
	}

	@Override
	public void setStatusHandler(IStatusHandler handler) {
		task.setStatusHandler(handler);
	}

	@Override
	public void setSorts(ISortDefinition[] simpleSortExpression, boolean overrideExistingSorts) {
		task.setSorts(simpleSortExpression, overrideExistingSorts);
	}
}
