
package org.eclipse.birt.report.engine.executor.optimize;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.impl.ReportRunnable;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class ExecutionOptimizeTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/executor/optimize/test.xml";

	public void setUp( )
	{
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		// create the report engine using default config
		engine = createReportEngine( );
	}

	public void tearDown( )
	{
		// shut down the engine.
		engine.shutdown( );
		removeFile( REPORT_DESIGN );
	}

	public void testExecutionOptimize( ) throws Exception
	{
		// open the report runnable to execute.
		ReportRunnable reportRunnable = (ReportRunnable) engine
				.openReportDesign( REPORT_DESIGN );
		Report report = new ReportParser( )
				.parse( (ReportDesignHandle) reportRunnable.getDesignHandle( ) );
		ExecutionOptimize executionOpt = new ExecutionOptimize( );
		ExecutionPolicy policy = executionOpt.optimize( report );
		validatePolicy( report, policy );
	}

	protected void validatePolicy( Report report, ExecutionPolicy policy )
	{
		TableItemDesign table = (TableItemDesign) report.getContent( 0 );
		assertTrue( policy.needExecute( table ) );

		TableBandDesign tableHeaderBand = (TableBandDesign) table.getHeader( );
		assertTrue( policy.needExecute( tableHeaderBand ) );

		RowDesign tableHeaderRow = tableHeaderBand.getRow( 0 );
		assertTrue( policy.needExecute( tableHeaderRow ) );

		int cellCount = tableHeaderRow.getCellCount( );
		CellDesign firstCell = tableHeaderRow.getCell( 0 );
		ReportItemDesign firstLable = firstCell.getContent( 0 );
		assertFalse( policy.needExecute( firstCell ) );
		assertFalse( policy.needExecute( firstLable ) );

		CellDesign lastCell = tableHeaderRow.getCell( cellCount - 1 );
		ReportItemDesign lastLable = lastCell.getContent( 0 );
		assertTrue( policy.needExecute( lastCell ) );
		assertTrue( policy.needExecute( lastLable ) );

		TableGroupDesign group = (TableGroupDesign) table.getGroup( 0 );
		assertTrue( policy.needExecute( group ) );

		TableBandDesign groupHeaderBand = (TableBandDesign) group.getHeader( );
		assertTrue( policy.needExecute( groupHeaderBand ) );

		RowDesign groupHeaderRow = groupHeaderBand.getRow( 0 );
		assertTrue( policy.needExecute( groupHeaderRow ) );

		firstCell = groupHeaderRow.getCell( 0 );
		ReportItemDesign firstData = firstCell.getContent( 0 );
		assertFalse( policy.needExecute( firstCell ) );
		assertFalse( policy.needExecute( firstData ) );

		lastCell = groupHeaderRow.getCell( cellCount - 1 );
		assertTrue( policy.needExecute( lastCell ) );

		TableBandDesign detailBand = (TableBandDesign) table.getDetail( );
		assertTrue( policy.needExecute( detailBand ) );

		RowDesign detailRow = detailBand.getRow( 0 );
		assertTrue( policy.needExecute( detailRow ) );

		firstCell = detailRow.getCell( 0 );
		firstData = firstCell.getContent( 0 );
		assertFalse( policy.needExecute( firstCell ) );
		assertFalse( policy.needExecute( firstData ) );

		lastCell = detailRow.getCell( cellCount - 1 );
		ReportItemDesign lastData = lastCell.getContent( 0 );
		assertTrue( policy.needExecute( lastCell ) );
		assertTrue( policy.needExecute( lastData ) );

		TableBandDesign groupFooterBand = (TableBandDesign) group.getFooter( );
		assertTrue( policy.needExecute( groupFooterBand ) );

		RowDesign groupFooterRow = groupFooterBand.getRow( 0 );
		assertTrue( policy.needExecute( groupFooterRow ) );

		firstCell = groupFooterRow.getCell( 0 );
		assertFalse( policy.needExecute( firstCell ) );

		lastCell = groupFooterRow.getCell( cellCount - 1 );
		assertTrue( policy.needExecute( lastCell ) );

		TableBandDesign tableFooterBand = (TableBandDesign) table.getFooter( );
		assertTrue( policy.needExecute( tableFooterBand ) );

		RowDesign tableFooterRow = tableFooterBand.getRow( 0 );
		assertTrue( policy.needExecute( tableFooterRow ) );

		firstCell = tableFooterRow.getCell( 0 );
		assertFalse( policy.needExecute( firstCell ) );

		lastCell = tableFooterRow.getCell( cellCount - 1 );
		assertTrue( policy.needExecute( lastCell ) );

	}

}
