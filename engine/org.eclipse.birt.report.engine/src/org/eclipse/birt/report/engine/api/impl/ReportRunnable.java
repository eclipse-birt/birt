/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.birt.core.script.ICompiledScript;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.script.internal.element.ReportDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * Engine implementation of IReportRunnable interface
 */
public class ReportRunnable extends DesignRunnable implements IReportRunnable {

	/**
	 * report file name
	 */
	protected String reportName;

	protected boolean prepared = false;

	protected Hashtable<String, Map<String, ICompiledScript>> cachedScripts = new Hashtable<String, Map<String, ICompiledScript>>();

	public void setPrepared(boolean prepared) {
		this.prepared = prepared;
	}

	public boolean isPrepared() {
		return this.prepared;
	}

	public Map<String, Map<String, ICompiledScript>> getScriptCache() {
		return cachedScripts;
	}

	public ICompiledScript getScript(String scriptName, String source) {
		Map<String, ICompiledScript> scriptCache = cachedScripts.get(scriptName);
		if (scriptCache == null) {
			return null;
		}
		return scriptCache.get(source);
	}

	public void putScript(String scriptName, String source, ICompiledScript script) {
		Map<String, ICompiledScript> cachedScript = cachedScripts.get(scriptName);
		if (cachedScript == null) {
			cachedScript = new Hashtable<String, ICompiledScript>();
			cachedScripts.put(scriptName, cachedScript);
		}
		cachedScript.put(source, script);
	}

	/**
	 * constructor
	 * 
	 * @param report reference to report
	 */
	public ReportRunnable(IReportEngine engine, ModuleHandle designHandle) {
		super(engine, designHandle);
	}

	/**
	 * @param name report file name
	 */
	public void setReportName(String name) {
		this.reportName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#getReportName()
	 */
	public String getReportName() {
		return this.reportName;
	}

	/**
	 * @return reference to the report object
	 */
	public ReportDesignHandle getReport() {
		return (ReportDesignHandle) designHandle;
	}

	public void setDesignHandle(DesignElementHandle handle) {
		if (!(handle instanceof ReportDesignHandle)) {
			throw new IllegalArgumentException("the argument must be a instanceof ReportDesignHandle");
		}
		this.designHandle = handle;
	}

	/**
	 * Returns the report design
	 * 
	 * @return the report design
	 */

	public IReportDesign getDesignInstance() {
		ReportDesign design = new ReportDesign((ReportDesignHandle) designHandle);
		return design;
	}

	public ReportRunnable cloneRunnable() {
		ReportDesignHandle newDesign = (ReportDesignHandle) designHandle.copy().getHandle(null);
		newDesign.setFileName(getReportName());
		ReportRunnable newRunnable = new ReportRunnable(engine, newDesign);
		newRunnable.setReportName(reportName);
		newRunnable.setPrepared(prepared);
		return newRunnable;
	}
}
