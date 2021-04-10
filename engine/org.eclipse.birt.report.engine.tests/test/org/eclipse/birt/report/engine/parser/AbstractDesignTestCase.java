
package org.eclipse.birt.report.engine.parser;

import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.ir.Report;

abstract public class AbstractDesignTestCase extends TestCase {

	protected Report report = null;

	void loadDesign(String design) {
		try {
			InputStream in = this.getClass().getResourceAsStream(design);
			assertTrue(in != null);
			ReportParser parser = new ReportParser();
			report = parser.parse("", in);
			assertTrue(report != null);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}
}
