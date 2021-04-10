
package org.eclipse.birt.report.engine.data.dte;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.DesignFileException;

public class NamedExpressionTest extends TestCase {

	private static final int MODE_GENERATION = 0;

	private String NAMED_EXPRESSION_DESIGN = "NamedExpression.xml";
	private String NAMED_EXPRESSION_FILENAME = "NamedExpression";

	private Report getReport(String designName) throws DesignFileException {
		InputStream in = this.getClass().getResourceAsStream(designName);
		assertTrue(in != null);
		ReportParser parser = new ReportParser();
		Report report = parser.parse("", in);
		assertTrue(report != null);

		return report;
	}

	private IDataEngine getDataEngine(Report report, IDocArchiveWriter arch, String archiveMetaName, int mode)
			throws Exception {
		ExecutionContext context = new ExecutionContext();

		if (mode == MODE_GENERATION) {
			DataGenerationEngine dataGenEngine = new DataGenerationEngine(null, context, arch);
			dataGenEngine.prepare(report, null);
			return dataGenEngine;
		} else {
			return null;
		}
	}

	protected String loadResource(String resourceName) throws Exception {
		InputStream in = this.getClass().getResourceAsStream(resourceName);
		assertTrue(in != null);
		byte[] buffer = new byte[in.available()];
		in.read(buffer);
		return new String(buffer);
	}

	protected void setUp() throws Exception {
		super.setUp();
		delete(NAMED_EXPRESSION_FILENAME);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		delete(NAMED_EXPRESSION_FILENAME);
	}

	public void testNamedExpression() throws Exception {
		Report report = getReport(NAMED_EXPRESSION_DESIGN);
		assert report.getContentCount() == 5;
		Map namedExpr = report.getContent(0).getUserProperties();
		assertEquals(1, namedExpr.size());

		namedExpr = report.getContent(1).getUserProperties();
		assertEquals(2, namedExpr.size());

		namedExpr = report.getUserProperties();
		assertEquals(2, namedExpr.size());

		ReportItemDesign reportItem = report.getContent(2);
		namedExpr = reportItem.getUserProperties();
		assertEquals(1, namedExpr.size());

		assert reportItem instanceof TableItemDesign;
		TableItemDesign tableItem = (TableItemDesign) reportItem;
		TableBandDesign tableBand = (TableBandDesign) tableItem.getHeader();
		for (int i = 0; i < tableBand.getRowCount(); i++) {
			assertRow(tableBand.getRow(i), 1);
		}

		tableBand = (TableBandDesign) tableItem.getFooter();
		for (int i = 0; i < tableBand.getRowCount(); i++) {
			assertRow(tableBand.getRow(i), 1);
		}

		tableBand = (TableBandDesign) tableItem.getDetail();
		for (int i = 0; i < tableBand.getRowCount(); i++) {
			assertRow(tableBand.getRow(i), 1);
		}

		GroupDesign tableGroup = tableItem.getGroup(0);
		tableBand = (TableBandDesign) tableGroup.getHeader();
		for (int i = 0; i < tableBand.getRowCount(); i++) {
			assertRow(tableBand.getRow(i), 1);
		}

		tableBand = (TableBandDesign) tableGroup.getFooter();
		for (int i = 0; i < tableBand.getRowCount(); i++) {
			assertRow(tableBand.getRow(i), 1);
		}

		reportItem = report.getContent(3);
		namedExpr = reportItem.getUserProperties();
		assertEquals(1, namedExpr.size());

		assert reportItem instanceof ListItemDesign;
		ListItemDesign listItem = (ListItemDesign) reportItem;

		ListBandDesign listBand = (ListBandDesign) listItem.getHeader();
		for (int i = 0; i < listBand.getContentCount(); i++) {
			namedExpr = listBand.getContent(i).getUserProperties();
			assertEquals(1, namedExpr.size());
		}

		listBand = (ListBandDesign) listItem.getFooter();
		for (int i = 0; i < listBand.getContentCount(); i++) {
			namedExpr = listBand.getContent(i).getUserProperties();
			assertEquals(1, namedExpr.size());
		}

		listBand = (ListBandDesign) listItem.getDetail();
		for (int i = 0; i < listBand.getContentCount(); i++) {
			namedExpr = listBand.getContent(i).getUserProperties();
			assertEquals(1, namedExpr.size());
		}

		GroupDesign listGroup = listItem.getGroup(0);
		listBand = (ListBandDesign) listGroup.getHeader();
		for (int i = 0; i < listBand.getContentCount(); i++) {
			namedExpr = listBand.getContent(i).getUserProperties();
			assertEquals(1, namedExpr.size());
		}

		listBand = (ListBandDesign) listGroup.getFooter();
		for (int i = 0; i < listBand.getContentCount(); i++) {
			namedExpr = listBand.getContent(i).getUserProperties();
			assertEquals(1, namedExpr.size());
		}

		reportItem = report.getContent(4);
		assert reportItem instanceof GridItemDesign;
		GridItemDesign gridItem = (GridItemDesign) reportItem;

		namedExpr = gridItem.getUserProperties();
		assertEquals(1, namedExpr.size());

		for (int i = 0; i < gridItem.getRowCount(); i++) {
			RowDesign row = gridItem.getRow(i);
			namedExpr = row.getUserProperties();
			assertEquals(1, namedExpr.size());
		}
	}

	private void assertRow(RowDesign row, int expected) {
		Map namedExpr = row.getUserProperties();
		assertTrue(namedExpr != null);
		assertEquals(expected, namedExpr.size());
	}

	private void delete(String fileName) {
		File delFile = new File(fileName);
		if (delFile.exists()) {
			delFile.delete();
		}
	}
}
