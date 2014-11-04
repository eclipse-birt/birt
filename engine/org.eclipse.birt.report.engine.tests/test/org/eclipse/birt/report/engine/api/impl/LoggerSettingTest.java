package org.eclipse.birt.report.engine.api.impl;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineConfig;

import junit.framework.TestCase;

public class LoggerSettingTest  extends TestCase{
	static Logger birtLogger = Logger.getLogger("org.eclipse.birt");
	
	private ReportEngine createReportEngine(Level logLevel, String fileName)
	{
		EngineConfig engineConfig = new EngineConfig();
		engineConfig.setLogConfig(null, logLevel);
		engineConfig.setLogFile(fileName);
		return new ReportEngine(engineConfig);
	}
	
	private void verifyResult(Level level, int handlerNum)
	{
		assertEquals( level, birtLogger.getLevel( ) );
		assertEquals( handlerNum, birtLogger.getHandlers().length );
	}
	
	public void test1()
	{
		verifyResult(null, 0);
		ReportEngine r1 = createReportEngine(Level.WARNING, null);
		verifyResult(Level.WARNING, 1);
		ReportEngine r2 = createReportEngine(Level.INFO, null);
		verifyResult(Level.INFO, 1);
		ReportEngine r3 = createReportEngine(Level.SEVERE, null);
		verifyResult(Level.SEVERE, 1);
		ReportEngine r4 = createReportEngine(null, null);
		verifyResult(Level.SEVERE, 1);
		r4.destroy();
		verifyResult(Level.SEVERE, 1);
		r2.destroy();
		verifyResult(Level.SEVERE, 1);
		r3.destroy();
		verifyResult(Level.WARNING, 1);
		r1.destroy();
		verifyResult(Level.WARNING, 0);
	}
}
