/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.io.IOException;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineException;

public class ListingElementExecutorTest extends EngineCase {
	/**
	 * Test page break interval counter will be reset when pages are broken between
	 * the interval. Refer to
	 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=157418">Bugzilla bug
	 * 157418</a>.
	 * 
	 * @throws EngineException
	 * @throws IOException
	 * 
	 */
	public void testPageBreakInterval() throws EngineException, IOException {
		String designFile = "org/eclipse/birt/report/engine/executor/ListingElementExecutorTest.xml";
		checkResult(render(designFile));
		checkResult(runAndRender(designFile));
	}

	private void checkResult(String htmlResult) {
		String[] pageContents = htmlResult.split("page header");

		String key = "outer table";
		// Check page 8, 14, 18, 20, 28, if page broken after desired
		// interval 5.
		assertEquals(5, getCount(pageContents[8], key));
		assertEquals(5, getCount(pageContents[14], key));
		assertEquals(5, getCount(pageContents[18], key));
		assertEquals(5, getCount(pageContents[20], key));
		assertEquals(5, getCount(pageContents[28], key));
	}

}
