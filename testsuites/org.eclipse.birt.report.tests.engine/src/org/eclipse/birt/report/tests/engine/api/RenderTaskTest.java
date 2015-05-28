
package org.eclipse.birt.report.tests.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
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

/**
 * <b>IRenderTask test</b>
 * <p>
 * This case tests methods in IRenderTask API.
 */
public class RenderTaskTest extends EngineCase
{

	private String report_design;
	private String report_document;
	private IReportDocument reportDoc;
	private String outputFileName;
	private String separator = System.getProperty( "file.separator" );
	private List errors = null;

	protected String path = this.getInputResourceFolder( ) + File.separator
			+ getFullQualifiedClassName( ) //$NON-NLS-1$
			+ File.separator;
	// private String outputPath = path + OUTPUT_FOLDER + separator;
	private String inputPath = path + INPUT_FOLDER + separator;

	// String tempDir = System.getProperty( "java.io.tmpdir" ); //$NON-NLS-1$
	String outputPath = this.genOutputFolder( ) + "/";

	public RenderTaskTest( String name )
	{
		super( name );
	}

	public static Test Suite( )
	{
		return new TestSuite( RenderTaskTest.class );
	}

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		engine.getConfig( ).setLogConfig(
				this.genOutputFolder( ) + "/",
				Level.WARNING );
	}

	public void tearDown( )
	{
		removeResource( );
	}

	public void testRender_orderreport( )
	{
		errors = renderReport( "OrderReport", "All" );
		assertNull( errors );
	}

	/**
	 * Test RenderTask with different input design files
	 */
	public void testRender_simple( )
	{
		errors = renderReport( "case1", "All" );
		assertNull( errors );
	}

	public void testRender_table( )
	{
		errors = renderReport( "table_pages", "All" );
		assertNull( errors );
	}

	public void testRender_longtext( )
	{
		errors = renderReport( "long_text", "All" );
		assertNull( errors );
	}

	public void testRender_multiple_datasets( )
	{

		errors = renderReport( "multiple_datasets", "All" );
		assertNull( errors );
	}

	public void testRender_nesttable( )
	{
		errors = renderReport( "table_nest_pages", "All" );
		assertNull( errors );
	}

	public void testRender_oncreate( )
	{
		errors = renderReport( "oncreate-style-label", "All" );
		assertNull( errors );
	}

	public void testRender_script( )
	{
		errors = renderReport( "javascript-support-data", "All" );
		assertNull( errors );
	}

	public void testRender_masterpage( )
	{
		errors = renderReport( "master_page", "All" );
		assertNull( errors );
	}

	public void testRender_chart( )
	{
		errors = renderReport( "chart", "All" );
		assertNull( errors );
	}

	public void testRender_complex( )
	{
		errors = renderReport( "complex_report", "All" );
		assertNull( errors );
	}

	public void testRender_areachart( )
	{
		errors = renderReport( "area3dChart", "All" );
		assertNull( errors );
	}

	public void testRender_meterchart( )
	{
		errors = renderReport( "MeterChart", "All" );
		assertNull( errors );
	}

	public void testRender_dynamic_image( )
	{
		errors = renderReport( "image_in_DB", "All" );
		assertNull( errors );
	}

	public void testRender_multiple_masterpage( )
	{
		errors = renderReport( "multiple_masterpage", "All" );
		assertNull( errors );
	}

	public void testRender_data( )
	{
		errors = renderReport( "smoke_data", "All" );
		assertNull( errors );
	}

	public void testRender_library( )
	{
		copyResource_INPUT( "library1.rptlibrary", "library1.rptlibrary" );
		renderReport( "report_from_library1", "All" );
		assertNull( errors );
	}

	/*
	 * Test RenderTask when set page range
	 */
	public void testRenderPageRange_all( )
	{
		errors = renderReport( "pages9", "All" );
		assertNull( errors );
	}

	public void testRenderPageRange_null( )
	{
		errors = renderReport( "pages9", null );
		assertNull( errors );
	}

	public void testRenderPageRange_blank( )
	{
		errors = renderReport( "pages9", "" );
		assertNull( errors );
	}

	public void testRenderPageRange_number( )
	{
		errors = renderReport( "pages9", "2" );
		assertNull( errors );
	}

	public void testRenderPageRange_comma( )
	{
		errors = renderReport( "pages9", "3,5" );
		assertNull( errors );
	}

	public void testRenderPageRange_dash1( )
	{
		errors = renderReport( "pages9", "2-8" );
		assertNull( errors );
	}

	/*
	 * Test Rendertask when set bookmark
	 */
	public void testRenderBookmark_label( )
	{
		renderReportBookmark( "items_bookmark", "label" );
		renderReportBookmark( "multiple_masterpage", "label" );
	}

	public void testRenderBookmark_text( )
	{
		renderReportBookmark( "items_bookmark", "text" );
		renderReportBookmark( "multiple_masterpage", "text" );
	}

	public void testRenderBookmark_image( )
	{
		renderReportBookmark( "items_bookmark", "image" );
		renderReportBookmark( "multiple_masterpage", "image" );
	}

	public void testRenderBookmark_gridrow( )
	{
		renderReportBookmark( "items_bookmark", "gridrow" );
		renderReportBookmark( "multiple_masterpage", "gridrow" );
	}

	public void testRenderBookmark_chart( )
	{
		renderReportBookmark( "items_bookmark", "chart" );
		renderReportBookmark( "multiple_masterpage", "chart" );
	}

	/*
	 * Test RenderTask when set instanceid
	 */
	public void testRenderReportlet_list( ) throws Exception
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet", "LIST" );
		renderReportlet( "iid_reportlet", iid, "LIST" );
	}

	public void testRenderReportlet_table( ) throws Exception
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet", "TABLE" );
		renderReportlet( "iid_reportlet", iid, "TABLE" );
	}

	public void testRenderReportlet_chart( ) throws Exception
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet", "Chart" );
		renderReportlet( "iid_reportlet", iid, "Chart" );
	}

	public void testRenderReportlet_bookmark( )
	{
		renderReportlet( "reportlet_bookmark_toc", "bk_table" );
	}

	public void testRenderReportlet_toc( )
	{
		renderReportlet( "reportlet_bookmark_toc", "toc_chart" );
	}

	public void testRenderReportlet_complex_list( ) throws Exception
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet_complex", "LIST" );
		renderReportlet( "iid_reportlet_complex", iid, "LIST" );
	}

	public void testRenderReportlet_complex_table( ) throws Exception
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet_complex", "TABLE" );
		renderReportlet( "iid_reportlet_complex", iid, "TABLE" );
	}

	/*
	 * This case is for bug 137817 NPE when generated pdf because target folder
	 * doesn't exist
	 */
	public void testRenderPDFNPE( )
	{
		report_design = inputPath + "case1.rptdesign";
		report_document = outputPath + "pdfbug_reportdocument";
		copyResource_INPUT( "case1.rptdesign", "case1.rptdesign" );

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
			appContext.put(
					EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );

			pdfRenderOptions.setOutputFormat( "pdf" );
			pdfRenderOptions.getOutputSetting( ).put(
					HTMLRenderOption.URL_ENCODING,
					"UTF-8" );

			outputFileName = outputPath + "pdfbug/pdf/page1" + ".pdf";
			removeFile( outputFileName );
			pdfRenderOptions.setOutputFileName( outputFileName );
			task = engine.createRenderTask( reportDoc );
			task.setLocale( Locale.ENGLISH );
			task.setAppContext( appContext );
			task.setRenderOption( pdfRenderOptions );
			task.render( );
			task.close( );
			File pdfFile = new File( outputFileName );
			assertTrue(
					"Render pdf failed when target path doesn't exist",
					pdfFile.exists( ) );
			assertTrue(
					"Render pdf failed when target path doesn't exist",
					pdfFile.length( ) != 0 );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			fail( "Render pdf failed when target path doesn't exist" );
		}

	}

	/*
	 * Find instance id according to element type and design file.
	 */
	private InstanceID findIid( String fileName, String type )
	{
		InstanceID iid = null;
		report_document = outputPath + fileName + ".rptdocument";
		report_design = inputPath + fileName + ".rptdesign";
		copyResource_INPUT( fileName + ".rptdesign", fileName + ".rptdesign" );
		IRenderTask task;

		try
		{
			createReportDocument( report_design, report_document );
			reportDoc = engine.openReportDocument( report_document );
			task = engine.createRenderTask( reportDoc );
			task.setLocale( Locale.ENGLISH );
			IRenderOption htmlRenderOptions = new HTMLRenderOption( );
			HashMap appContext = new HashMap( );
			HTMLRenderContext renderContext = new HTMLRenderContext( );
			renderContext.setImageDirectory( outputPath + "image" );
			appContext.put(
					EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );
			task.setAppContext( appContext );

			ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
			htmlRenderOptions.setOutputStream( ostream );
			htmlRenderOptions.setOutputFormat( "html" );
			( (HTMLRenderOption) htmlRenderOptions ).setEnableMetadata( true );
			task.setRenderOption( htmlRenderOptions );
			task.render( );
			task.close( );

			String content = ostream.toString( "utf-8" );
			Pattern typePattern = DataExtractionTaskTest.buildPattern( type );
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
			throws Exception
	{
		if ( iid == null )
		{
			assertFalse( "Failed to find instance id of " + type, true );
		}
		else
		{

			copyFile( this.genOutputFile( docName + ".rptdocument" ), inputPath
					+ docName + ".rptdocument" );
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
				appContext.put(
						EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
						renderContext );
				outputFileName = outputPath + docName + "/html/" + type
						+ ".html";
				htmlRenderOptions.setOutputFileName( outputFileName );
				htmlRenderOptions.setEmitterID( null );
				htmlRenderOptions.setOutputFormat( "html" );
				task.setAppContext( appContext );
				task.setRenderOption( htmlRenderOptions );
				task.setInstanceID( iid );
				task.render( );
				assertEquals( "Exception when render reportlet-" + docName
						+ " to html.", 0, task.getErrors( ).size( ) );
				assertTrue(
						"Render reportlet-" + docName + " to html failed. ",
						new File( outputFileName ).exists( ) );

				outputFileName = outputPath + docName + "/pdf/" + type + ".pdf";
				htmlRenderOptions.setOutputFileName( outputFileName );
				htmlRenderOptions.setOutputFormat( "pdf" );
				htmlRenderOptions.setEmitterID( null );
				task.setRenderOption( htmlRenderOptions );
				task.setInstanceID( iid );
				task.setAppContext( appContext );
				task.render( );
				assertEquals( "Exception when render reportlet-" + docName
						+ " to pdf.", 0, task.getErrors( ).size( ) );
				task.close( );
				assertTrue(
						"Render reportlet-" + docName + " to pdf failed. ",
						new File( outputFileName ).exists( ) );
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
		report_document = outputPath + docName + ".rptdocument";
		report_design = inputPath + docName + ".rptdesign";
		copyResource_INPUT( docName + ".rptdesign", docName + ".rptdesign" );

		IRenderTask task;

		boolean toc = false;
		String s_toc = null;
		if ( bookmark.substring( 0, 3 ).equals( "toc" ) )
		{
			toc = true;
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
			appContext.put(
					EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );
			task.setAppContext( appContext );
			outputFileName = outputPath + docName + "/html/" + bookmark
					+ ".html";
			htmlRenderOptions.setOutputFileName( outputFileName );
			htmlRenderOptions.setOutputFormat( "html" );
			htmlRenderOptions.setEmitterID( null );

			if ( toc )
			{
				s_toc = ( (TOCNode) ( reportDoc.findTOCByName( bookmark )
						.get( 0 ) ) ).getBookmark( );
			}
			else
			{
				s_toc = bookmark;
			}

			task.setReportlet( s_toc );
			task.setRenderOption( htmlRenderOptions );
			task.render( );
			assertEquals( "Exception when render reportlet-" + docName
					+ " to html.", 0, task.getErrors( ).size( ) );
			assertTrue(
					"Render reportlet-" + docName + " to html failed. ",
					new File( outputFileName ).exists( ) );

			outputFileName = outputPath + docName + "/pdf/" + bookmark + ".pdf";
			htmlRenderOptions.setOutputFileName( outputFileName );
			htmlRenderOptions.setOutputFormat( "pdf" );
			htmlRenderOptions.setEmitterID( null );
			task.setRenderOption( htmlRenderOptions );
			task.setReportlet( s_toc );
			task.render( );
			assertEquals( "Exception when render reportlet-" + docName
					+ " to pdf.", 0, task.getErrors( ).size( ) );
			task.close( );
			assertTrue(
					"Render reportlet-" + docName + " to pdf failed. ",
					new File( outputFileName ).exists( ) );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			assertTrue( "Render reportlet " + bookmark + "from" + docName
					+ " failed. " + e.getLocalizedMessage( ), false );
		}

	}

	protected List renderReport( String fileName, String pageRange )
	{
		report_design = inputPath + fileName + ".rptdesign";
		report_document = outputPath + fileName + "_reportdocument";
		copyResource_INPUT( fileName + ".rptdesign", fileName + ".rptdesign" );

		IRenderTask task;

		// create directories to deposit output files
		createDir( fileName );
		try
		{
			createReportDocument( report_design, report_document );
			reportDoc = engine.openReportDocument( report_document );
			IRenderOption htmlRenderOptions = new HTMLRenderOption( );
			IRenderOption pdfRenderOptions = new HTMLRenderOption( );
			HTMLRenderContext renderContext = new HTMLRenderContext( );
			renderContext.setImageDirectory( "image" );
			HashMap appContext = new HashMap( );
			appContext.put(
					EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );
			htmlRenderOptions.setOutputFormat( "html" );
			pdfRenderOptions.setOutputFormat( "pdf" );
			htmlRenderOptions.getOutputSetting( ).put(
					HTMLRenderOption.URL_ENCODING,
					"UTF-8" );
			pdfRenderOptions.getOutputSetting( ).put(
					HTMLRenderOption.URL_ENCODING,
					"UTF-8" );

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
				assertEquals(
						"Exception when render the first page to html from "
								+ fileName,
						0,
						task.getErrors( ).size( ) );
				task.close( );

				File htmlFile = new File( outputFileName );
				assertTrue(
						"Render " + fileName + " to html failed. ",
						htmlFile.exists( ) );
				assertTrue(
						"Render " + fileName + " to html failed. ",
						htmlFile.length( ) != 0 );
				// render pdf output
				outputFileName = outputPath + fileName + "/pdf/page1" + ".pdf";
				removeFile( outputFileName );
				pdfRenderOptions.setOutputFileName( outputFileName );
				task = engine.createRenderTask( reportDoc );
				task.setLocale( Locale.ENGLISH );
				task.setAppContext( appContext );
				task.setRenderOption( pdfRenderOptions );
				task.setPageNumber( 1 );
				task.render( );
				assertEquals(
						"Exception when render the first page to pdf from "
								+ fileName,
						0,
						task.getErrors( ).size( ) );
				task.close( );
				File pdfFile = new File( outputFileName );
				assertTrue( "Render " + fileName + " to pdf failed. ", pdfFile
						.exists( ) );
				assertTrue( "Render " + fileName + " to pdf failed. ", pdfFile
						.length( ) != 0 );

			}
			else
			{
				/* set page range and then render according to range */

				// render html output
				outputFileName = outputPath + fileName + "/html/page"
						+ pageRange + ".html";
				removeFile( outputFileName );
				htmlRenderOptions.setOutputFileName( outputFileName );
				task = engine.createRenderTask( reportDoc );
				task.setLocale( Locale.ENGLISH );
				task.setAppContext( appContext );
				task.setRenderOption( htmlRenderOptions );
				task.setPageRange( pageRange );
				task.render( );
				if ( task.getErrors( ).size( ) > 0 )
					return task.getErrors( );
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
				if ( task.getErrors( ).size( ) > 0 )
					return task.getErrors( );
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
			return null;
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			assertTrue( "Render " + fileName + " failed. "
					+ e.getLocalizedMessage( ), false );
		}
		return null;
	}

	protected void renderReportBookmark( String fileName, String bookmark )
	{
		report_design = inputPath + fileName + ".rptdesign";
		report_document = outputPath + fileName + "_reportdocument";
		copyResource_INPUT( fileName + ".rptdesign", fileName + ".rptdesign" );

		IRenderTask task;

		// create directories to deposit output files
		createDir( fileName );
		try
		{
			createReportDocument( report_design, report_document );
			reportDoc = engine.openReportDocument( report_document );
			IRenderOption options = new HTMLRenderOption( );
			HTMLRenderContext renderContext = new HTMLRenderContext( );
			renderContext.setImageDirectory( "image" );
			HashMap appContext = new HashMap( );
			appContext.put(
					EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );
			options.setOutputFormat( "html" );
			options.setEmitterID( null );
			options.getOutputSetting( ).put(
					HTMLRenderOption.URL_ENCODING,
					"UTF-8" );

			outputFileName = outputPath + fileName + "/html/bookmark_"
					+ bookmark + ".html";
			removeFile( outputFileName );
			options.setOutputFileName( outputFileName );

			task = engine.createRenderTask( reportDoc );
			task.setLocale( Locale.ENGLISH );
			task.setAppContext( appContext );
			task.setBookmark( bookmark );
			task.setRenderOption( options );
			task.render( );
			assertEquals(
					"Exception when render html with given bookmark from "
							+ fileName,
					0,
					task.getErrors( ).size( ) );
			task.close( );

			File htmlFile = new File( outputFileName );
			assertTrue( "Render item with bookmark " + bookmark
					+ " to html failed. ", htmlFile.exists( ) );
			assertTrue( "Render item with bookmark " + bookmark
					+ " to html failed. ", htmlFile.length( ) != 0 );

			// render pdf output
			outputFileName = outputPath + fileName + "/pdf/bookmark_"
					+ bookmark + ".pdf";
			options.setOutputFileName( outputFileName );
			options.setOutputFormat( "pdf" );
			options.setEmitterID( null );
			task = engine.createRenderTask( reportDoc );
			task.setLocale( Locale.ENGLISH );
			task.setAppContext( appContext );
			task.setBookmark( bookmark );
			task.setRenderOption( options );
			task.render( );
			assertEquals( "Exception when render pdf with given bookmark from "
					+ fileName, 0, task.getErrors( ).size( ) );
			task.close( );

			File pdfFile = new File( outputFileName );
			assertTrue( "Render item with bookmark " + bookmark
					+ " to pdf failed. ", pdfFile.exists( ) );
			assertTrue( "Render item with bookmark " + bookmark
					+ " to pdf failed. ", pdfFile.length( ) != 0 );

			removeFile( outputPath + fileName );
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

		int i = runTask.getErrors( ).size( );
		if ( i > 0 )
			System.out.println( "error is "
					+ runTask.getErrors( ).get( 0 ).toString( ) );
		assertEquals(
				"Exception when generate document from " + reportdesign,
				0,
				i );

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
