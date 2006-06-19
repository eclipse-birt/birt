
package org.eclipse.birt.report.tests.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.tests.engine.EngineCase;

public class RenderTaskTest extends EngineCase
{

	private String report_design;
	private String report_document;
	private IReportDocument reportDoc;
	private String outputFileName;
	private String separator = System.getProperty( "file.separator" );
	protected String path = getClassFolder( ) + separator;
	private String outputPath = path + OUTPUT_FOLDER + separator;
	private String inputPath = path + INPUT_FOLDER + separator;

	/*
	 * protected String path =
	 * "D:/TEMP/workspace3.1/org.eclipse.birt.report.tests.engine/";
	 * 
	 * protected String input = "input", output = "output";
	 */
	public RenderTaskTest( String name )
	{
		super( name );
	}

	public static Test Suite( )
	{
//		TestSuite suite = new TestSuite( );
//		suite.addTest(  TestSuite.createTest( RenderTaskTest.class, "testMethod" ) );
		return new TestSuite( RenderTaskTest.class );
	}

	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}
	public void testRender0( )
	{
		renderReport( "OrderReport", "All" );
	}
	
	/**
	 * Test RenderTask with different input design files
	 */
	public void testRender1( )
	{
		renderReport( "case1", "All" );
	}

	public void testRender2( )
	{
		renderReport( "table_pages", "All" );
	}

	public void testRender3( )
	{
		renderReport( "long_text", "All" );
	}

	public void testRender4( )
	{

		renderReport( "multiple_datasets", "All" );
	}

	public void testRender5( )
	{
		renderReport( "table_nest_pages", "All" );
	}

	public void testRender6( )
	{
		renderReport( "oncreate-style-label", "All" );
	}

	public void testRender7( )
	{
		renderReport( "javascript-support-data", "All" );
	}

	public void testRender8( )
	{
		renderReport( "master_page", "All" );
	}

	public void testRender9( )
	{
		renderReport( "chart", "All" );
	}

	public void testRender10( )
	{
		renderReport( "complex_report", "All" );
	}

	public void testRender11( )
	{
		renderReport( "area3dChart", "All" );
	}

	public void testRender12( )
	{
		renderReport( "MeterChart", "All" );
	}

	public void testRender13( )
	{
		renderReport( "image_in_DB", "All" );
	}

	public void testRender14( )
	{
		renderReport( "multiple_masterpage", "All" ); 
	}
	
	public void testRender15(){
		renderReport( "smoke_data", "All" );
	}
	
	public void testRender16( )
	{
		File fLib=new File(inputPath+"library1.rptlibrary");
		if(fLib.exists( )){
			try{
				new File(outputPath).mkdirs( );
				File tLib=new File(outputPath+"library1.rptlibrary");
				FileInputStream fis=new FileInputStream(fLib);
				FileOutputStream fos=new FileOutputStream(tLib);
				byte[] contents=new byte[1024];
				int len;
				while((len=fis.read( contents ))>0){
					fos.write( contents,0,len );
				}
				fis.close( );
				fos.close( );
				
				renderReport( "report_from_library1", "All" );
			}catch(Exception e){
				e.printStackTrace( );
				fail("Render library file failed. "+e.getLocalizedMessage( ));
			}
		}else{
			fail("Library file doesn't exist!");
		}
	}
	
	/*
	 * Test RenderTask when set page range
	 */
	public void testRenderPageRange1( )
	{
		renderReport( "pages9", "All" );
	}

	public void testRenderPageRange2( )
	{
		renderReport( "pages9", null );
	}

	public void testRenderPageRange3( )
	{
		renderReport( "pages9", "" );
	}

	public void testRenderPageRange4( )
	{
		renderReport( "pages9", "2" );
	}

	public void testRenderPageRange5( )
	{
		renderReport( "pages9", "3,5" );
	}

	public void testRenderPageRange6( )
	{
		renderReport( "pages9", "2-9" );
	}

	public void testRenderPageRange7( )
	{
		renderReport( "pages9", "0-100" );
	}

	public void testRenderPageRange8( )
	{
		renderReport( "pages9", "0" );
	}

	public void testRenderPageRange9( )
	{
		renderReport( "pages9", "abc" );
	}
	
	
     /*
	 * Test Rendertask when set bookmark
	 */
	public void testRenderBookmark_label( )
	{
		renderReport( "items_bookmark", "bookmark_label" );
		renderReport( "multiple_masterpage", "bookmark_label" );
	}

	public void testRenderBookmark_text( )
	{
		renderReport( "items_bookmark", "bookmark_text" );
		renderReport( "multiple_masterpage", "bookmark_text" );
	}

	public void testRenderBookmark_image( )
	{
		renderReport( "items_bookmark", "bookmark_image" );
		renderReport( "multiple_masterpage", "bookmark_image" );
	}

	public void testRenderBookmark_gridrow( )
	{
		renderReport( "items_bookmark", "bookmark_gridrow" );
		renderReport( "multiple_masterpage", "bookmark_gridrow" );
	}

	public void testRenderBookmark_chart( )
	{
		renderReport( "items_bookmark", "bookmark_chart" );
		renderReport( "multiple_masterpage", "bookmark_chart" );
	}

	/*
	 * Test RenderTask when set instanceid
	 */
	public void testRenderReportlet_list( )
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet", "LIST" );
		renderReportlet( "iid_reportlet", iid, "LIST" );
	}

	public void testRenderReportlet_table( )
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet", "TABLE" );
		renderReportlet( "iid_reportlet", iid, "TABLE" );
	}

	public void testRenderReportlet_chart( )
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet", "EXTENDED" );
		renderReportlet( "iid_reportlet", iid, "EXTENDED" );
	}
	
	public void testRenderReportlet_bookmark( )
	{
		InstanceID iid;
		renderReportlet( "reportlet_bookmark_toc", "bk_table" );
	}
	
	public void testRenderReportlet_toc( )
	{
		renderReportlet( "reportlet_bookmark_toc", "toc_chart" );
	}

	public void testRenderReportlet_complex_list( )
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet_complex", "LIST" );
		renderReportlet( "iid_reportlet_complex", iid, "LIST" );
	}
	
	
	public void testRenderReportlet_complex_table( )
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet_complex", "TABLE" );
		renderReportlet( "iid_reportlet_complex", iid, "TABLE" );
	}

	/*
	 * This case is for bug 137817
	 * NPE when generated pdf because target folder doesn't exist
	 */
	public void testRenderPDFNPE(){
			report_design = inputPath + "case1.rptdesign";
			report_document = outputPath + "pdfbug_reportdocument";

			IRenderTask task;
			try
			{
				createReportDocument( report_design, report_document );
				reportDoc = engine.openReportDocument( report_document );

				// Set IRenderOption
				IRenderOption pdfRenderOptions = new HTMLRenderOption( );
				HTMLRenderContext renderContext = new HTMLRenderContext( );
				renderContext.setImageDirectory( "image" );
				HashMap appContext = new HashMap( );
				appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
						renderContext );

				pdfRenderOptions.setOutputFormat( "pdf" );
				pdfRenderOptions.getOutputSetting( ).put(
						HTMLRenderOption.URL_ENCODING, "UTF-8" );

				outputFileName = outputPath + "pdfbug/pdf/page1"
						+ ".pdf";
				removeFile( outputFileName );
				pdfRenderOptions.setOutputFileName( outputFileName );
				task = engine.createRenderTask( reportDoc );
				task.setLocale( Locale.ENGLISH );
				task.setAppContext( appContext );
				task.setRenderOption( pdfRenderOptions );
				task.render( );
				task.close( );
				File pdfFile = new File( outputFileName );
				assertTrue( "Render pdf failed when target path doesn't exist",
						pdfFile.exists( ) );
				assertTrue( "Render pdf failed when target path doesn't exist",
						pdfFile.length( ) != 0 );
			}catch(Exception e){
				e.printStackTrace( );
				fail("Render pdf failed when target path doesn't exist");
			}

	}

	
	
	
	/*
	 * Find instance id according to element type and design file.
	 */
	private InstanceID findIid( String fileName, String type )
	{
		InstanceID iid = null;
		report_document = inputPath + fileName + ".rptdocument";
		report_design = inputPath + fileName + ".rptdesign";
		IRenderTask task;

		try
		{
			createReportDocument( report_design, report_document );
			reportDoc = engine.openReportDocument( report_document );
			task = engine.createRenderTask( reportDoc );
			task.setLocale( Locale.ENGLISH );
			IRenderOption htmlRenderOptions = new HTMLRenderOption( );
			HashMap appContext = new HashMap( );
			task.setAppContext( appContext );

			ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
			htmlRenderOptions.setOutputStream( ostream );
			htmlRenderOptions.setOutputFormat( "html" );

			task.setRenderOption( htmlRenderOptions );
			task.render( );
			task.close( );

			String content = ostream.toString( "utf-8" );
			Pattern typePattern = Pattern.compile( "(element_type=\"" + type
					+ "\".*iid=\".*\")" );
			Matcher matcher = typePattern.matcher( content );

			if ( matcher.find( ) )
			{
				String tmp_type = matcher.group( 1 ), strIid;
				strIid = tmp_type.substring( tmp_type.indexOf( "iid" ) );

				strIid = strIid.substring( 5, strIid.indexOf( "\"", 6 ) );
				iid = InstanceID.parse( strIid );
				return iid;
			}

		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			assertFalse( "Failed to find instance id of " + type, true );
		}

		return iid;
	}

	/*
	 * render reportlet according to docfile and instance id
	 */
	protected void renderReportlet( String docName, InstanceID iid, String type )
	{
		if ( iid == null )
		{
			assertFalse( "Failed to find instance id of " + type, true );
		}
		else
		{
			report_document = inputPath + docName + ".rptdocument";

			IRenderTask task;

			// create directories to deposit output files
			createDir( docName );
			try
			{
				reportDoc = engine.openReportDocument( report_document );
				task = engine.createRenderTask( reportDoc );
				IRenderOption htmlRenderOptions = new HTMLRenderOption( );
				HTMLRenderContext renderContext = new HTMLRenderContext( );
				renderContext.setImageDirectory( "image" );
				HashMap appContext = new HashMap( );
				appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
						renderContext );
				task.setAppContext( appContext );
				outputFileName = outputPath + docName + "/html/" + type
						+ ".html";
				htmlRenderOptions.setOutputFileName( outputFileName );
				htmlRenderOptions.setOutputFormat( "html" );
				task.setRenderOption( htmlRenderOptions );
				task.setInstanceID( iid );
				task.render( );
				assertTrue( "Render reportlet-" + docName + " to html failed. ",
						new File(outputFileName).exists( ) );
				
				outputFileName = outputPath + docName + "/pdf/" + type
				+ ".pdf";
				htmlRenderOptions.setOutputFileName( outputFileName );
				htmlRenderOptions.setOutputFormat( "pdf" );
				task.setRenderOption( htmlRenderOptions );
				task.setInstanceID( iid );
				task.render( );
				task.close( );
				assertTrue( "Render reportlet-" + docName + " to pdf failed. ",
						new File(outputFileName).exists( ) );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
				assertTrue( "Render reportlet " + type + "from" + docName
						+ " failed. " + e.getLocalizedMessage( ), false );
			}
		}
	}

	/*
	 * render reportlet according to docfile and instance id
	 */
	protected void renderReportlet( String docName, String bookmark )
	{
		report_document = inputPath + docName + ".rptdocument";
		report_design = inputPath + docName + ".rptdesign";

		IRenderTask task;

		boolean toc=false;
		String s_toc=null;
		if(bookmark.substring( 0, 3 ).equals( "toc" )){
			toc=true;
		}
		// create directories to deposit output files
		createDir( docName );
		try
		{
			createReportDocument( report_design, report_document );
			reportDoc = engine.openReportDocument( report_document );
			task = engine.createRenderTask( reportDoc );
			IRenderOption htmlRenderOptions = new HTMLRenderOption( );
			HTMLRenderContext renderContext = new HTMLRenderContext( );
			renderContext.setImageDirectory( "image" );
			HashMap appContext = new HashMap( );
			appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );
			task.setAppContext( appContext );
			outputFileName = outputPath + docName + "/html/" + bookmark
					+ ".html";
			htmlRenderOptions.setOutputFileName( outputFileName );
			htmlRenderOptions.setOutputFormat( "html" );
			
			if(toc){
				s_toc=((TOCNode)(reportDoc.findTOCByName( bookmark ).get( 0 ))).getBookmark( );
			}else{
				s_toc=bookmark;
			}
			
			task.setReportlet( s_toc );
			task.setRenderOption( htmlRenderOptions );
			task.render( );
			assertTrue( "Render reportlet-" + docName + " to html failed. ",
					new File(outputFileName).exists( ) );
			
			outputFileName = outputPath + docName + "/pdf/" + bookmark
			+ ".pdf";
			htmlRenderOptions.setOutputFileName( outputFileName );
			htmlRenderOptions.setOutputFormat( "pdf" );
			task.setRenderOption( htmlRenderOptions );
			task.setReportlet( s_toc );
			task.render( );
			task.close( );
			assertTrue( "Render reportlet-" + docName + " to pdf failed. ",
					new File(outputFileName).exists( ) );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			assertTrue( "Render reportlet " + bookmark + "from" + docName
					+ " failed. " + e.getLocalizedMessage( ), false );
		}
		
	}

	
	protected void renderReport( String fileName, String pageRange )
	{
		report_design = inputPath + fileName + ".rptdesign";
		report_document = outputPath + fileName + "_reportdocument";

		IRenderTask task;

		// create directories to deposit output files
		createDir( fileName );
		try
		{
			createReportDocument( report_design, report_document );
			// open the document in the archive.
			reportDoc = engine.openReportDocument( report_document );

			// Set IRenderOption
			IRenderOption htmlRenderOptions = new HTMLRenderOption( );
			IRenderOption pdfRenderOptions = new HTMLRenderOption( );

			HTMLRenderContext renderContext = new HTMLRenderContext( );
			renderContext.setImageDirectory( "image" );
			HashMap appContext = new HashMap( );
			appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );

			htmlRenderOptions.setOutputFormat( "html" );
			pdfRenderOptions.setOutputFormat( "pdf" );
			htmlRenderOptions.getOutputSetting( ).put(
					HTMLRenderOption.URL_ENCODING, "UTF-8" );
			pdfRenderOptions.getOutputSetting( ).put(
					HTMLRenderOption.URL_ENCODING, "UTF-8" );

			int i_bookmark = -1;
			if ( pageRange != null )
			{
				i_bookmark = pageRange.indexOf( "bookmark_" );
			}
			if ( i_bookmark == -1 )
			{
				if ( pageRange != null && pageRange.equals( "no" ) )
				{
					/* set page number 1 and then render the first page */
					// render html output
					outputFileName = outputPath + fileName + "/html/page1"
							+ ".html";
					removeFile( outputFileName );
					htmlRenderOptions.setOutputFileName( outputFileName );

					task = engine.createRenderTask( reportDoc );
					task.setLocale( Locale.ENGLISH );
					task.setAppContext( appContext );
					task.setRenderOption( htmlRenderOptions );
					task.setPageNumber( 1 );
					task.render( );
					task.close( );

					File htmlFile = new File( outputFileName );
					assertTrue( "Render " + fileName + " to html failed. ",
							htmlFile.exists( ) );
					assertTrue( "Render " + fileName + " to html failed. ",
							htmlFile.length( ) != 0 );
					// render pdf output
					outputFileName = outputPath + fileName + "/pdf/page1"
							+ ".pdf";
					removeFile( outputFileName );
					pdfRenderOptions.setOutputFileName( outputFileName );
					task = engine.createRenderTask( reportDoc );
					task.setLocale( Locale.ENGLISH );
					task.setAppContext( appContext );
					task.setRenderOption( pdfRenderOptions );
					task.setPageNumber( 1 );
					task.render( );
					task.close( );
					File pdfFile = new File( outputFileName );
					assertTrue( "Render " + fileName + " to pdf failed. ",
							pdfFile.exists( ) );
					assertTrue( "Render " + fileName + " to pdf failed. ",
							pdfFile.length( ) != 0 );

				}
				else
				{
					/* set page range and then render according to range */

					// render html output
					outputFileName = outputPath + fileName + "/html/page"
							+ pageRange + ".html";
					removeFile( outputFileName );
					htmlRenderOptions.setOutputFileName( outputFileName );
					/**/
					((HTMLRenderOption)htmlRenderOptions).setMasterPageContent( false );
					/**/
					task = engine.createRenderTask( reportDoc );
					task.setLocale( Locale.ENGLISH );
					task.setAppContext( appContext );
					task.setRenderOption( htmlRenderOptions );
					task.setPageRange( pageRange );
					task.render( );
					task.close( );

					File htmlFile = new File( outputFileName );
					if ( pageRange != null
							&& ( pageRange.equals( "0" ) || pageRange
									.equals( "abc" ) ) )
					{
						assertFalse( htmlFile.exists( ) );
					}
					else
					{
						assertTrue( "Render " + fileName + " to html failed. "
								+ pageRange, htmlFile.exists( ) );
						assertTrue( "Render " + fileName + " to html failed. "
								+ pageRange, htmlFile.length( ) != 0 );
					}
					// render pdf output
					outputFileName = outputPath + fileName + "/pdf/page"
							+ pageRange + ".pdf";
					removeFile( outputFileName );
					pdfRenderOptions.setOutputFileName( outputFileName );

					task = engine.createRenderTask( reportDoc );
					task.setLocale( Locale.ENGLISH );
					task.setAppContext( new HashMap( ) );
					task.setRenderOption( pdfRenderOptions );
					task.setPageRange( pageRange );
					task.render( );
					task.close( );

					File pdfFile = new File( outputFileName );
					if ( pageRange != null
							&& ( pageRange.equals( "0" ) || pageRange
									.equals( "abc" ) ) )
					{
						assertFalse( pdfFile.exists( ) );
					}
					else
					{
						assertTrue( "Render " + fileName + " to pdf failed. "
								+ pageRange, pdfFile.exists( ) );
						assertTrue( "Render " + fileName + " to pdf failed. "
								+ pageRange, pdfFile.length( ) != 0 );
					}
				}
			}
			else
			{
				String bookmark = pageRange.substring( 9 );
				// render html output
				outputFileName = outputPath + fileName + "/html/bookmark_"
						+ bookmark + ".html";
				removeFile( outputFileName );
				htmlRenderOptions.setOutputFileName( outputFileName );

				task = engine.createRenderTask( reportDoc );
				task.setLocale( Locale.ENGLISH );
				task.setAppContext( appContext );
				task.setBookmark( bookmark );
				task.setRenderOption( htmlRenderOptions );
				task.render( );
				task.close( );

				File htmlFile = new File( outputFileName );
				assertTrue( "Render " + fileName + " to html failed. "
						+ pageRange, htmlFile.exists( ) );
				assertTrue( "Render " + fileName + " to html failed. "
						+ pageRange, htmlFile.length( ) != 0 );
				// render pdf output
				outputFileName = outputPath + fileName + "/pdf/bookmark_"
						+ bookmark + ".pdf";
				removeFile( outputFileName );
				pdfRenderOptions.setOutputFileName( outputFileName );

				task = engine.createRenderTask( reportDoc );
				task.setLocale( Locale.ENGLISH );
				task.setAppContext( appContext );
				task.setBookmark( bookmark );
				task.setRenderOption( pdfRenderOptions );
				task.render( );
				task.close( );

				File pdfFile = new File( outputFileName );
				assertTrue( "Render " + fileName + " to pdf failed. "
						+ pageRange, pdfFile.exists( ) );
				assertTrue( "Render " + fileName + " to pdf failed. "
						+ pageRange, pdfFile.length( ) != 0 );

			}
			task.close( );			

		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			assertTrue( "Render " + fileName + " failed. "
					+ e.getLocalizedMessage( ), false );
		}
	}


	
	
	
	/**
	 * create the report document.
	 * 
	 * @throws Exception
	 */
	protected void createReportDocument( String reportdesign,
			String reportdocument ) throws Exception
	{
		// open an report archive, it is a folder archive.
		IDocArchiveWriter archive = new FileArchiveWriter( reportdocument );
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign( reportdesign );
		// create an IRunTask
		IRunTask runTask = engine.createRunTask( report );
		// execute the report to create the report document.
		runTask.setAppContext( new HashMap( ) );
		runTask.run( archive );
		// close the task, release the resource.
		runTask.close( );
	}


	/**
	 * create need directory creat html and pdf directory under the need
	 * directory
	 */
	protected void createDir( String name )
	{
		String out = OUTPUT_FOLDER;
		File fdir = new File( path + out + "/" + name + "/" );
		if ( !fdir.mkdir( ) )
		{
			// System.err.println( "Cannot create output directories" );
		}
		fdir = new File( path + out + "/" + name + "/html/" );
		if ( !fdir.mkdir( ) )
		{
			// System.err.println( "Cannot create output html directories" );
		}
		fdir = new File( path + out + "/" + name + "/pdf/" );
		if ( !fdir.mkdir( ) )
		{
			// System.err.println( "Cannot create output pdf directories" );
		}
	}

}
