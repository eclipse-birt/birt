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

package org.eclipse.birt.report.engine.ir;

/**
 * 
 */
public class PageSequenceTest extends ReportElementTestCase {
	public PageSequenceTest() {
		super(new PageSequenceDesign());
	}

	/**
	 * test getRole, addRule function.
	 */
	public void testSetGetPage() {
		PageSequenceDesign pageSequence = new PageSequenceDesign();
		MasterPageDesign first = new GraphicMasterPageDesign();
		MasterPageDesign body = new GraphicMasterPageDesign();
		pageSequence.setPage("first", first);
		pageSequence.setPage("body", body);
		assertEquals(pageSequence.getPage("first"), first);
		assertEquals(pageSequence.getPage("body"), body);
	}
}
