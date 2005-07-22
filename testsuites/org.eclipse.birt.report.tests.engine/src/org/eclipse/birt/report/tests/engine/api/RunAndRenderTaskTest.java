/*
 * Created on Jul 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.RunAndRenderTask;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * @author lchen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RunAndRenderTaskTest extends EngineCase {

	/**
	 * @param name
	 */
	public RunAndRenderTaskTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Test methods in RunAndRenderTask class
	 */
	public void testRunAndRenderTask() throws EngineException
	{
		EngineConfig config=new EngineConfig();
		ReportEngine engine=new ReportEngine(config);
		
		//run reports
		String input = PLUGIN_PATH + RESOURCE_BUNDLE.getString("CASE_INPUT");
		input += System.getProperty("file.separator") + "api.rptdesign";
		try{
			IReportRunnable runnable=engine.openReportDesign(new FileInputStream(new File(input)));
			RunAndRenderTask task=new RunAndRenderTask(engine,runnable);
			//validateParameters
			assertTrue(task.validateParameters());
			//set/getRenderOption
			RenderOptionBase option=new RenderOptionBase(),optionGet;
			task.setRenderOption(option);
			optionGet=(RenderOptionBase)task.getRenderOption();
			assertEquals("set/getRenderOption fail",option,optionGet);
			//parameters
			task.setParameterValue("p1","p1value");
			assertEquals("Set/getParameterValues fail",task.getParameterValues().get("p1"),"p1value");
			
			HashMap hm=new HashMap(),hmGet;
			task.setParameterValues(hm);
			hmGet=task.getParameterValues();
			assertEquals("set/getParameterValues fail",hm,hmGet);
			
					
		//shut down engine
			engine.destroy();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	
	
}
