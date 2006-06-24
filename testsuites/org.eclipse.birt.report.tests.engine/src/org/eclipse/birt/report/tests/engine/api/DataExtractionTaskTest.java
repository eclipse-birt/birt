package org.eclipse.birt.report.tests.engine.api;

import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.tests.engine.EngineCase;


public class DataExtractionTaskTest extends EngineCase
{
	private String report_design;
	private String report_document;
	private IReportDocument reportDoc;
	private String separator = System.getProperty( "file.separator" );
	protected String path = getClassFolder( ) + separator;
	private String outputPath = path + OUTPUT_FOLDER + separator;
	private String inputPath = path + INPUT_FOLDER + separator;
	
	public DataExtractionTaskTest( String name )

	{
		super( name );
	}

	public static Test Suite( )
	{
		return new TestSuite( DataExtractionTaskTest.class );
	}

	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	public void testDataExtractionWithFilter(){
		report_design=inputPath+"DataExtraction_table.rptdesign";
		report_document=outputPath+"DataExtraction_table.rptdocument";
		try{
			createReportDocument(report_design,report_document);
			
			reportDoc=engine.openReportDocument( report_document );
			IDataExtractionTask extractTask=engine.createDataExtractionTask( reportDoc );
			
			extractTask.selectResultSet( "t1" );
			IFilterDefinition[] filterExpression=new IFilterDefinition[1];
			filterExpression[0]=new FilterDefinition(new ConditionalExpression(
					                    "row[\"territory\"]",ConditionalExpression.OP_EQ, "\"EMEA\"",null));
			extractTask.setFilters( filterExpression );
			
			IExtractionResults result=extractTask.extract( );
			
			if(result!=null){
				int officecode=0;
				IDataIterator data=result.nextResultIterator( );
				if(data!=null){
					data.next( );
					officecode=Integer.parseInt(data.getValue("code").toString( ));
					assertEquals("Fail to extract filtered data",4,officecode);
					if(data.next()){
						officecode=Integer.parseInt(data.getValue("code").toString( ));
						assertEquals("Fail to extract filtered data",7,officecode);
					}
				}
			}else{
				fail("Fail to extract filtered data");
			}
			
		}catch(Exception e){
			e.printStackTrace( );
			fail("Fail to extract filtered data");
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


}
