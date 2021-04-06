/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
