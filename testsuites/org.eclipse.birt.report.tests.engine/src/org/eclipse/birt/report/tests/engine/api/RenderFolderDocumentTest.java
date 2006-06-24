package org.eclipse.birt.report.tests.engine.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.core.archive.FolderArchiveReader;
import org.eclipse.birt.core.archive.FolderArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * 
 * Test render output from folder-based report documents
 *
 */
public class RenderFolderDocumentTest extends EngineCase {
	
	private String separator =System.getProperty("file.separator");
	private String INPUT = getClassFolder( ) + separator + INPUT_FOLDER + separator ;
	private String OUTPUT = getClassFolder( ) + separator + OUTPUT_FOLDER + separator ;
	private String folderArchive, htmlOutput;
	private IReportDocument reportDoc;
	private IRenderTask renderTask;
	private IRenderOption htmlOption, pdfOption;
	
	public RenderFolderDocumentTest(String name) {
		super(name);
	}

	public static Test Suite() {
		return new TestSuite(RenderTaskTest.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		htmlOption=new HTMLRenderOption();
		htmlOption.setOutputFormat(HTMLRenderOption.HTML);
	}

	public void test1(){
		String renderDoc="folderdocument_case1";
		renderFolderDocument(renderDoc);		
	}
	

	public void test2(){
		String renderDoc="folderdocument_long_text";
		renderFolderDocument(renderDoc);
		
	}
		

	public void test3(){
		String renderDoc="folderdocument_master_page";
		renderFolderDocument(renderDoc);
		
	}
	

	public void test4(){
		String renderDoc="folderdocument_multiple_datasets";
		renderFolderDocument(renderDoc);
		
	}
	

	public void test5(){
		String renderDoc="folderdocument_pages9";
		renderFolderDocument(renderDoc);
		
	}
	

	public void test6(){
		String renderDoc="folderdocument_table_nest_pages";
		renderFolderDocument(renderDoc);
		
	}
	

	public void test7(){
		String renderDoc="folderdocument_chart";
		renderFolderDocument(renderDoc);
		
	}
	

	private void renderFolderDocument(String docName){
		
		IRunTask runTask;
		String designName, report_design;
		designName=docName.substring( 15 );
		report_design=INPUT+designName+".rptdesign";

		folderArchive=INPUT+docName+separator;
		htmlOutput=OUTPUT+docName+".html";
		try{
			FolderArchiveWriter writer=new FolderArchiveWriter(folderArchive);
			IReportRunnable runnable=engine.openReportDesign( report_design );
			runTask=engine.createRunTask( runnable );
			runTask.run( writer );
			
			FolderArchiveReader reader=new FolderArchiveReader(folderArchive);
			reportDoc=engine.openReportDocument(folderArchive);
			renderTask=engine.createRenderTask(reportDoc);
			
			htmlOption.setOutputFileName(htmlOutput);
			HTMLRenderContext renderContext = new HTMLRenderContext( );
			renderContext.setImageDirectory( "image" );
			HashMap appContext = new HashMap( );
			appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );
			
			renderTask.setRenderOption(htmlOption);
			renderTask.setAppContext( appContext );
			renderTask.setLocale( Locale.ENGLISH );
			renderTask.setPageRange( "All" );
			renderTask.render();
			renderTask.close();
			
			assertNotNull(docName+".html failed to render from folder-based document",htmlOutput);
			
		}catch(IOException ioe){
			ioe.printStackTrace();
			assertTrue("IOException (when render "+docName+" folder-based document)",false);
		}catch(EngineException ee){
			ee.printStackTrace();
			assertTrue("EngineException (when render "+docName+" folder-based document)",false);
		}
		
	}

}
