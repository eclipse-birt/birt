
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
