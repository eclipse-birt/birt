
package org.eclipse.birt.report.engine.api;

import junit.framework.TestCase;

public class ReportRunnerTest extends TestCase {

	public void testRunner() {
		new ReportRunner(new String[] {}).execute();
	}
}
