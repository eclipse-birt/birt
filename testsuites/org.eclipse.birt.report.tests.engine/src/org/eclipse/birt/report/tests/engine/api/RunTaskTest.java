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
/*		
		String plug_path=EngineCase.PLUGIN_PATH;
		String file_path=EngineCase.RESOURCE_BUNDLE.getString("CASE_INPUT")
					+System.getProperty("file.separator")+"runtask_test.rptdesign";
		String document_path=EngineCase.RESOURCE_BUNDLE.getString("CASE_OUTPUT")
		+System.getProperty("file.separator")+"reportdocument";
*/		
		String path=getBaseFolder()+System.getProperty("file.separator");
		String file_path=INPUT_FOLDER+System.getProperty("file.separator")+"runtask_test.rptdesign";
		String document_path=OUTPUT_FOLDER+System.getProperty("file.separator")+"runtask_test_reportdocument";
		
		String report_design=path+file_path;
		String report_document=path+document_path;
	
/*
		String report_design="D:/TEMP/workspace3.1/org.eclipse.birt.report.tests.engine/input/runtask_test.rptdesign";
		String report_document="D:/TEMP/workspace3.1/org.eclipse.birt.report.tests.engine/output/reportdocument";
*/		
		removeFile( report_document );
		
		try{
			EngineConfig config=new EngineConfig();
			ReportEngine engine=new ReportEngine(config);
			IReportRunnable runnable=engine.openReportDesign(report_design);
			IDocArchiveWriter archive=new FileArchiveWriter(report_document);
			IRunTask task=engine.createRunTask(runnable);
			
			//test run(IDocumentArchive archive)
			//normal archive
			task.run(archive);
			//task.close();

			
			File documents=new File(report_document);
			assertNotNull("reportdocument directory should exist!",documents);
/*			String[] files=documents.list();
			assertEquals("Generated report document should include 6 files and 2 directories",8,files.length);
			File design=new File(report_document+"/design");
			File name=new File(report_document+"/name");
			File bookmark=new File(report_document+"/bookmark");
			File pages=new File(report_document+"/pages");
			File parameter=new File(report_document+"/parameter");
			File toc=new File(report_document+"/toc");
			File content=new File(report_document+"/content");
			assertNotNull("design file should exist under report document directory",design);
			assertNotNull("name file should exist under report document directory",name);
			assertNotNull("bookmark file should exist under report document directory",bookmark);
			assertNotNull("pages file should exist under report document directory",pages);
			assertNotNull("parameter file should exist under report document directory",parameter);
			assertNotNull("toc file should exist under report document directory",toc);
			assertNotNull("content directory should exist under report document directory",content);
			assertEquals("content directory should contain two files",2,content.list().length );*/
			removeFile( report_document );
			
			//null archive
			
			archive=null;
//			task=engine.createRunTask(runnable);
			task.run(archive);
//			task.close();
			

			//test run(string docname)
//			task=engine.createRunTask(runnable);
			task.run(report_document);
			task.close();
			documents=new File(report_document);
			assertNotNull("reportdocument directory should exist!",documents);
/*			String[] files1=documents.list();
			assertEquals("Generated report document should include 6 files and 2 directories",8,files1.length);
			design=new File(report_document+"/design");
			name=new File(report_document+"/name");
			bookmark=new File(report_document+"/bookmark");
			pages=new File(report_document+"/pages");
			parameter=new File(report_document+"/parameter");
			toc=new File(report_document+"/toc");
			content=new File(report_document+"/content");
			assertNotNull("design file should exist under report document directory",design);
			assertNotNull("name file should exist under report document directory",name);
			assertNotNull("bookmark file should exist under report document directory",bookmark);
			assertNotNull("pages file should exist under report document directory",pages);
			assertNotNull("parameter file should exist under report document directory",parameter);
			assertNotNull("toc file should exist under report document directory",toc);
			assertNotNull("content directory should exist under report document directory",content);
			assertEquals("content directory should contain two files",2,content.list().length );
*/			removeFile( report_document );
/*			//test null docname
			String reportdoc=null;
//			task=engine.createRunTask(runnable);
			task.run(reportdoc);
*/			
			task.close();
			
			
			engine.shutdown();
		}catch(EngineException ee){
			assertEquals("return wrong error","Report archive is not specified when running a report.",ee.getErrorCode());

		}catch(IOException ie){
			ie.printStackTrace();
		}
	}


	
	
	
}
