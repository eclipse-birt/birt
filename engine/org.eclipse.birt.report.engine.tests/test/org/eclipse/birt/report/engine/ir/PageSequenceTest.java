/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
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
