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

package org.eclipse.birt.report.engine.internal.presentation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.impl.IInternalReportDocument;
import org.eclipse.birt.report.engine.api.impl.RunStatusReader;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.engine.IReportDocumentExtension;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.toc.TOCReader;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class TransientReportDocument implements IInternalReportDocument {

	protected IReportDocument document;
	protected Map globalVariables;
	protected Map parameters;
	protected Map parameterDisplayTexts;
	protected long pageNumber;
	protected boolean isComplete;
	protected ExecutionContext context;

	TransientReportDocument(IReportDocument document, ExecutionContext context, long pageNumber, Map paramters,
			Map parameterDisplayTexts, Map globalVariables, boolean isComplete) {
		this.document = document;
		this.context = context;
		this.pageNumber = pageNumber;
		this.parameters = paramters;
		this.parameterDisplayTexts = parameterDisplayTexts;
		this.globalVariables = globalVariables;
		this.isComplete = isComplete;
	}

	@Override
	public IDocArchiveReader getArchive() {
		return document.getArchive();
	}

	@Override
	public void close() {
		document.close();
	}

	@Override
	public String getVersion() {
		return document.getVersion();
	}

	@Override
	public String getProperty(String key) {
		return document.getProperty(key);
	}

	@Override
	public String getName() {
		return document.getName();
	}

	@Override
	public InputStream getDesignStream() {
		return document.getDesignStream();
	}

	@Override
	public IReportRunnable getReportRunnable() {
		return document.getReportRunnable();
	}

	@Override
	public IReportRunnable getPreparedRunnable() {
		return document.getPreparedRunnable();
	}

	@Override
	public Map getParameterValues() {
		return parameters;
	}

	@Override
	public Map getParameterDisplayTexts() {
		return parameterDisplayTexts;
	}

	@Override
	public long getPageCount() {
		return pageNumber;
	}

	@Override
	public long getPageNumber(InstanceID iid) {
		return -1;
	}

	@Override
	public long getInstanceOffset(InstanceID iid) {
		return -1;
	}

	@Override
	public long getPageNumber(String bookmark) {
		return -1;
	}

	@Override
	public List getBookmarks() {
		return new ArrayList();
	}

	@Override
	public List getChildren(String tocNodeId) {
		return new ArrayList();
	}

	@Override
	public TOCNode findTOC(String tocNodeId) {
		return null;
	}

	@Override
	public List findTOCByName(String tocName) {
		return new ArrayList();
	}

	@Override
	public Map getGlobalVariables(String option) {
		return globalVariables;
	}

	@Override
	public long getBookmarkOffset(String bookmark) {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#isComplete()
	 */
	@Override
	public boolean isComplete() {
		return isComplete;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#refresh()
	 */
	@Override
	public void refresh() {
	}

	@Override
	public ITOCTree getTOCTree(String format, ULocale locale) {
		return null;
	}

	@Override
	public ITOCTree getTOCTree(String format, ULocale locale, TimeZone timeZone) {
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		return context.getApplicationClassLoader();
	}

	@Override
	public ReportDesignHandle getReportDesign() {
		return document.getReportDesign();
	}

	@Override
	public Report getReportIR(ReportDesignHandle designHandle) {
		return ((IInternalReportDocument) document).getReportIR(designHandle);
	}

	@Override
	public IReportRunnable getOnPreparedRunnable() {
		return ((IInternalReportDocument) document).getOnPreparedRunnable();
	}

	@Override
	public IReportRunnable getDocumentRunnable() {
		return ((IInternalReportDocument) document).getDocumentRunnable();
	}

	@Override
	public InstanceID getBookmarkInstance(String bookmark) {
		return null;
	}

	@Override
	public IReportDocumentExtension getDocumentExtension(String name) throws EngineException {
		return ((IInternalReportDocument) document).getDocumentExtension(name);
	}

	@Override
	public Map<String, ParameterAttribute> loadParameters(ClassLoader loader) throws EngineException {
		return new HashMap<>();
	}

	@Override
	public Map<String, Object> loadVariables(ClassLoader loader) throws EngineException {
		return new HashMap<>();
	}

	@Override
	public TOCReader getTOCReader(ClassLoader loader) throws EngineException {
		return null;
	}

	@Override
	public String getSystemId() {
		return document.getSystemId();
	}

	@Override
	public List<String> getDocumentErrors() {
		RunStatusReader statusReader = new RunStatusReader(this);
		try {
			return statusReader.getGenerationErrors();
		} finally {
			statusReader.close();
		}
	}

}
