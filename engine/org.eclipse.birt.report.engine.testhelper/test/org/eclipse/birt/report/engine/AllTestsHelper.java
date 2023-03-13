/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.report.engine;

import junit.framework.TestSuite;

public class AllTestsHelper {

	public static void suite(TestSuite suite) {
		suite.addTestSuite(org.eclipse.birt.report.engine.executor.optimize.ExecutionOptimizeTest.class);
		suite.addTestSuite(org.eclipse.birt.report.engine.internal.document.v2.ContentTreeCacheTest.class);
		suite.addTestSuite(org.eclipse.birt.report.engine.internal.executor.doc.FragmentTest.class);
		suite.addTestSuite(org.eclipse.birt.report.engine.internal.executor.doc.SegmentTest.class);
		suite.addTestSuite(org.eclipse.birt.report.engine.internal.executor.doc.TreeFragmentTest.class);
		suite.addTestSuite(org.eclipse.birt.report.engine.internal.executor.load.PageSequenceIteratorTest.class);
		suite.addTestSuite(org.eclipse.birt.report.engine.internal.index.v2.IndexReadWriteTest.class);
	}

}
