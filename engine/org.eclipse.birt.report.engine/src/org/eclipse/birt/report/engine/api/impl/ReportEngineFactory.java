
package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

public class ReportEngineFactory implements IReportEngineFactory
{

	public IReportEngine createReportEngine( EngineConfig config )
	{
		return new ReportEngine( config );
	}

}
