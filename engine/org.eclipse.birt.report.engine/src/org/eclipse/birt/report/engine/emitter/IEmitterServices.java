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
 * Defines interface to supply emitters with necessary information
 */
public interface IEmitterServices {

	/**
	 * TODO: review, return IEmitterConfig, by format.
	 *
	 * @return emitter configuration of engine
	 */
	HashMap getEmitterConfig();

	/**
	 * @return render options
	 */
	IRenderOption getRenderOption();

	/**
	 * @return the current report name
	 */
	String getReportName();

	/**
	 *
	 * @deprecated the user should use getReportContext().getRenderContext() to get
	 *             the render options.
	 * @return render context
	 */
	@Deprecated
	Object getRenderContext();

	/**
	 * @return the report runnable
	 */
	IReportRunnable getReportRunnable();

	/**
	 * @param name option name
	 * @return option value
	 */
	Object getOption(String name);

	IReportContext getReportContext();

	IReportEngine getReportEngine();
}
