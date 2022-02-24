/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter;

import java.util.HashMap;

import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.script.IReportContext;

/**
 * Provides necessray information to emitters
 */
public class EngineEmitterServices implements IEmitterServices {

	/**
	 * emitter configuration information
	 */
	protected HashMap configs;

	/**
	 * rendering context
	 */
	protected IRenderOption renderOptions;

	/**
	 * the context used to execute the rport
	 */
	protected IReportContext reportContext;

	/**
	 * @param task he engine task that results in the creation of emitter
	 */
	public EngineEmitterServices(IReportContext reportContext, IRenderOption renderOptions, HashMap configs) {
		this.configs = configs;
		this.reportContext = reportContext;
		this.renderOptions = renderOptions;
	}

	/**
	 * @return Returns the emitterConfig.
	 */
	public HashMap getEmitterConfig() {
		return configs;
	}

	/**
	 * @return Returns the rendering options.
	 */
	public IRenderOption getRenderOption() {
		return renderOptions;
	}

	/**
	 * @return Returns the reportName.
	 */
	public String getReportName() {
		IReportRunnable runnable = reportContext.getReportRunnable();
		if (runnable != null) {
			return runnable.getReportName();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEmitterServices#getOption(java.lang.
	 * String)
	 */
	public Object getOption(String name) {
		if (renderOptions != null) {
			return renderOptions.getOption(name);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEmitterServices#getRenderContext()
	 */
	public Object getRenderContext() {
		if (reportContext != null) {
			return reportContext.getAppContext();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEmitterServices#getReportRunnable()
	 */
	public IReportRunnable getReportRunnable() {
		if (reportContext != null) {
			return reportContext.getReportRunnable();
		}
		return null;
	}

	public IReportContext getReportContext() {
		return reportContext;
	}

	public IReportEngine getReportEngine() {
		if (reportContext != null) {
			return reportContext.getReportRunnable().getReportEngine();
		}
		return null;
	}
}
