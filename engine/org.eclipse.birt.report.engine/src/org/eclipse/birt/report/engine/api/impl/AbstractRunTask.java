
package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.executor.ReportExecutor;

abstract public class AbstractRunTask extends EngineTask
{

	ReportExecutor executor;
	EngineEmitterServices services;

	AbstractRunTask( IReportEngine engine, IReportRunnable runnable )
	{
		super( engine, runnable );
	}

	protected void setupExecutionContext( )
	{
		// setup runtime configurations
		// user defined configs are overload using system properties.
		executionContext.getConfigs( ).putAll( runnable.getTestConfig( ) );
		executionContext.getConfigs( ).putAll( System.getProperties( ) );
	}
}
