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
	private String INPUT=getBaseFolder()+separator+INPUT_FOLDER + separator ;
	private String OUTPUT=getBaseFolder()+separator+OUTPUT_FOLDER + separator ;
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
	public void testRun(){
		name="runtask_test";
		report_design=INPUT+name+".rptdesign";//path+file_path;
		report_document=OUTPUT+"runtask_"+name+".rptdocument";//path+document_path;
	
		removeFile( report_document );
		
		try{
			IReportRunnable runnable=engine.openReportDesign(report_design);
			IDocArchiveWriter archive=new FileArchiveWriter(report_document);
			IRunTask task=engine.createRunTask(runnable);
			
			//test run(IDocumentArchive archive)
			//normal archive
			task.run(archive);
			
			File documents=new File(report_document);
			assertNotNull("reportdocument directory should exist!",documents);
			removeFile( report_document );
			
			//null archive
			archive=null;
			task.run(archive);

			//test run(string docname)
			task.run(report_document);
			documents=new File(report_document);
			assertNotNull("reportdocument directory should exist!",documents);
			removeFile( report_document );
			task.close();
			
			
		}catch(EngineException ee){
			assertEquals("return wrong error","Report archive is not specified when running a report.",ee.getErrorCode());

		}catch(IOException ie){
			ie.printStackTrace();
		}
	}

	public void testRunFolderDocument1(){
		report_design=INPUT+"case1.rptdesign";
		String folderDocument=OUTPUT+"runtask_folderdocument_case1"+separator;
		try{
			runnable=engine.openReportDesign(report_design);
			IRunTask task=engine.createRunTask(runnable);
			task.run(folderDocument);
			task.close();
		}catch(EngineException ee){
			ee.printStackTrace();
			assertTrue("EngineException in testRunFolderDocument1",false);
		}
	}
	
	public void testRunFolderDocument2(){
		report_design=INPUT+"long_text.rptdesign";
		String folderDocument=OUTPUT+"runtask_folderdocument_long_text"+separator;
		try{
			runnable=engine.openReportDesign(report_design);
			IRunTask task=engine.createRunTask(runnable);
			task.run(folderDocument);
			task.close();
		}catch(EngineException ee){
			ee.printStackTrace();
			assertTrue("EngineException in testRunFolderDocument2",false);
		}
	}
	
	
	public void testRunFolderDocument3(){
		report_design=INPUT+"master_page.rptdesign";
		String folderDocument=OUTPUT+"runtask_folderdocument_master_page"+separator;
		try{
			runnable=engine.openReportDesign(report_design);
			IRunTask task=engine.createRunTask(runnable);
			task.run(folderDocument);
			task.close();
		}catch(EngineException ee){
			ee.printStackTrace();
			assertTrue("EngineException in testRunFolderDocument3",false);
		}
	}

	public void testRunFolderDocument4(){
		report_design=INPUT+"multiple_datasets.rptdesign";
		String folderDocument=OUTPUT+"runtask_folderdocument_multiple_datasets"+separator;
		try{
			runnable=engine.openReportDesign(report_design);
			IRunTask task=engine.createRunTask(runnable);
			task.run(folderDocument);
			task.close();
		}catch(EngineException ee){
			ee.printStackTrace();
			assertTrue("EngineException in testRunFolderDocument4",false);
		}
	}
	
	
	public void testRunFolderDocument5(){
		report_design=INPUT+"pages9.rptdesign";
		String folderDocument=OUTPUT+"runtask_folderdocument_pages9"+separator;
		try{
			runnable=engine.openReportDesign(report_design);
			IRunTask task=engine.createRunTask(runnable);
			task.run(folderDocument);
			task.close();
		}catch(EngineException ee){
			ee.printStackTrace();
			assertTrue("EngineException in testRunFolderDocument5",false);
		}
	}

	public void testRunFolderDocument6(){
		report_design=INPUT+"table_nest_pages.rptdesign";
		String folderDocument=OUTPUT+"runtask_folderdocument_table_nest_pages"+separator;
		try{
			runnable=engine.openReportDesign(report_design);
			IRunTask task=engine.createRunTask(runnable);
			task.run(folderDocument);
			task.close();
		}catch(EngineException ee){
			ee.printStackTrace();
			assertTrue("EngineException in testRunFolderDocument6",false);
		}
	}
	
		
	public void testRunFolderDocument7(){
		report_design=INPUT+"chart.rptdesign";
		String folderDocument=OUTPUT+"runtask_folderdocument_chart"+separator;
		try{
			runnable=engine.openReportDesign(report_design);
			IRunTask task=engine.createRunTask(runnable);
			task.run(folderDocument);
			task.close();
		}catch(EngineException ee){
			ee.printStackTrace();
			assertTrue("EngineException in testRunFolderDocument7",false);
		}
	}
	
	
	public void testRunFolderDocument8(){
		report_design=INPUT+"complex_report.rptdesign";
		String folderDocument=OUTPUT+"runtask_folderdocument_complex_report"+separator;
		try{
			runnable=engine.openReportDesign(report_design);
			IRunTask task=engine.createRunTask(runnable);
			task.run(folderDocument);
			task.close();
		}catch(EngineException ee){
			ee.printStackTrace();
			assertTrue("EngineException in testRunFolderDocument8",false);
		}
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	
}
