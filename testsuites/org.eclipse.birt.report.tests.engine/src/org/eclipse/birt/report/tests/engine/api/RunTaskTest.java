package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.tests.engine.EngineCase;

public class RunTaskTest extends EngineCase {

	private String separator=System.getProperty("file.separator");
	private String INPUT=getClassFolder()+separator+INPUT_FOLDER + separator ;
	private String OUTPUT=getClassFolder()+separator+OUTPUT_FOLDER + separator ;
	private String report_design, report_document,name;
	private IReportRunnable runnable;
	public RunTaskTest(String name) {
		super(name);
	}
	
	public static Test Suite(){
		return new TestSuite(RunTaskTest.class);
	}

	/**
	 * Test two Run method with different argument. 
	 *
	 */
	public void test1(){
		runReport("case1");
	}
	public void test2(){
		runReport("long_text");
	}
	public void test3(){
		runReport("master_page");
	}
	public void test4(){
		runReport("multiple_datasets");
	}
	public void test5(){
		runReport("pages9");
	}
	public void test6(){
		runReport("table_nest_pages");
	}
	public void test7(){
		runReport("chart");
	}
	public void test8(){
		runReport("complex_report");
	}
	public void test9(){
		runReport("area3dChart");
	}
	public void test10(){
		runReport("image_in_DB");
	}
	public void test11(){
		runReport("MeterChart");
	}
	

	private void runReport(String report){
		report_design=INPUT+report+".rptdesign";
		String fileDocument=OUTPUT+report+".rptdocument";
		String folderDocument=OUTPUT+"runtask_folderdocument_"+report+separator;
		try{
			runnable=engine.openReportDesign(report_design);
			IRunTask task=engine.createRunTask(runnable);
			task.run(fileDocument);
			task.run(folderDocument);
			task.close();
			
			assertTrue("Fail to generate file archive for "+report, new File(fileDocument).exists());
			assertTrue("Fail to generate folder archive for "+report, new File(folderDocument).exists());
		}catch(EngineException ee){
			ee.printStackTrace();
			assertTrue("Failed to generate document for "+report+ee.getLocalizedMessage(),false);
		}
	}
	protected void setUp() throws Exception {
		super.setUp();
	}

	
}
