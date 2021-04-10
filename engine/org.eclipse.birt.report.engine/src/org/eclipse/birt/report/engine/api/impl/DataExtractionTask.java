/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public void addScriptableJavaObject(String jsName, Object obj) {
		task.addScriptableJavaObject(jsName, obj);
	}

	public void cancel() {
		task.cancel();
	}

	public void cancel(String reason) {
		task.cancel(reason);
	}

	public void close() {
		task.close();
	}

	public IExtractionResults extract() throws EngineException {
		return task.extract();
	}

	public Map getAppContext() {
		return task.getAppContext();
	}

	public boolean getCancelFlag() {
		return task.getCancelFlag();
	}

	public IReportEngine getEngine() {
		return task.getEngine();
	}

	public List getErrors() {
		return task.getErrors();
	}

	public int getID() {
		return task.getID();
	}

	public Locale getLocale() {
		return task.getLocale();
	}

	public List getMetaData() throws EngineException {
		return task.getMetaData();
	}

	public Object getParameterDisplayText(String name) {
		return task.getParameterDisplayText(name);
	}

	public Object getParameterValue(String name) {
		return task.getParameterValue(name);
	}

	public HashMap getParameterValues() {
		return task.getParameterValues();
	}

	public IReportRunnable getReportRunnable() {
		return task.getReportRunnable();
	}

	public List getResultSetList() throws EngineException {
		return task.getResultSetList();
	}

	public int getStatus() {
		return task.getStatus();
	}

	public ULocale getULocale() {
		return task.getULocale();
	}

	public void selectColumns(String[] columnNames) {
		task.selectColumns(columnNames);
	}

	public void selectResultSet(String resultSetName) {
		task.selectResultSet(resultSetName);
	}

	public void setAppContext(Map context) {
		task.setAppContext(context);
	}

	public void setDataSource(IDocArchiveReader dataSource) {
		task.setDataSource(dataSource);
	}

	public void setDataSource(IDocArchiveReader dataSource, String reportlet) {
		task.setDataSource(dataSource, reportlet);
	}

	public void setFilters(IFilterDefinition[] simpleFilterExpression) {
		task.setFilters(simpleFilterExpression);
	}

	public void setSorts(ISortDefinition[] simpleSortExpression) {
		task.setSorts(simpleSortExpression);
	}

	public void setMaxRows(int maxRows) {
		task.setMaxRows(maxRows);
	}

	public void setInstanceID(InstanceID iid) {
		task.setInstanceID(iid);
	}

	public void setLocale(Locale locale) {
		task.setLocale(locale);
	}

	public void setLocale(ULocale locale) {
		task.setLocale(locale);
	}

	public void setTimeZone(TimeZone timeZone) {
		if (timeZone != null) {
			this.timeZone = timeZone;
		}
		task.setTimeZone(this.timeZone);
	}

	public void setParameter(String name, Object value, String displayText) {
		task.setParameter(name, value, displayText);
	}

	public void setParameter(String name, Object[] values, String[] displayText) {
		task.setParameter(name, values, displayText);
	}

	public void setParameterDisplayText(String name, String displayText) {
		task.setParameterDisplayText(name, displayText);
	}

	public void setParameterDisplayText(String name, String[] displayText) {
		task.setParameterDisplayText(name, displayText);
	}

	public void setParameterValue(String name, Object value) {
		task.setParameterValue(name, value);
	}

	public void setParameterValue(String name, Object[] values) {
		task.setParameterValue(name, values);
	}

	public void setParameterValues(Map params) {
		task.setParameterValues(params);
	}

	public boolean validateParameters() {
		return task.validateParameters();
	}

	public Logger getLogger() {
		return task.getLogger();
	}

	public int getTaskType() {
		return task.getTaskType();
	}

	public void setErrorHandlingOption(int option) {
		task.setErrorHandlingOption(option);
	}

	public void setLogger(Logger logger) {
		task.setLogger(logger);
	}

	public void extract(IExtractionOption option) throws BirtException {
		task.extract(option);
	}

	public void setStartRow(int startRow) {
		task.setStartRow(startRow);
	}

	public void setUserACL(String[] acl) {
		task.setUserACL(acl);
	}

	public void setDistinctValuesOnly(boolean distinct) {
		task.setDistinctValuesOnly(distinct);
	}

	public void setCubeExportEnabled(boolean isCubeExportEnabled) {
		task.setCubeExportEnabled(isCubeExportEnabled);
	}

	public boolean isCubeExportEnabled() {
		return task.isCubeExportEnabled();
	}

	public void setProgressMonitor(IProgressMonitor monitor) {
		task.setProgressMonitor(monitor);
	}

	public void setStatusHandler(IStatusHandler handler) {
		task.setStatusHandler(handler);
	}

	@Override
	public void setSorts(ISortDefinition[] simpleSortExpression, boolean overrideExistingSorts) {
		task.setSorts(simpleSortExpression, overrideExistingSorts);
	}
}
