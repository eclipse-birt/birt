/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.executor.ReportExecutor;

abstract public class AbstractRunTask extends EngineTask {

	ReportExecutor executor;
	EngineEmitterServices services;

	AbstractRunTask(ReportEngine engine, IReportRunnable runnable, int taskType) {
		super(engine, runnable, taskType);
	}

	/*
	 * protected void setupExecutionContext( ) { IReportRunnable runnable =
	 * executionContext.getRunnable( ); // setup runtime configurations // user
	 * defined configs are overload using system properties.
	 * executionContext.getConfigs( ).putAll( runnable.getTestConfig( ) );
	 * executionContext.getConfigs( ).putAll( SecurityUtil.getSystemProperties( ) );
	 * }
	 */
}
