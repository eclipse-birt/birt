/*******************************************************************************
 * Copyright (c) 2017 Actuate Corporation.
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

package org.eclipse.birt.core.archive;

import org.eclipse.birt.core.archive.cache.FileCacheManagerTest;
import org.eclipse.birt.core.archive.compound.ArchiveEntryInputStreamTest;
import org.eclipse.birt.core.archive.compound.ArchiveFileFactoryTest;
import org.eclipse.birt.core.archive.compound.ArchiveFileTest;
import org.eclipse.birt.core.archive.compound.ArchivePerformanceTest;
import org.eclipse.birt.core.archive.compound.ArchiveRemoveTest;
import org.eclipse.birt.core.archive.compound.ArchiveViewTest;
import org.eclipse.birt.core.archive.compound.UpgradeArchiveTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for archive package
 */

public class AllArchiveTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTestSuite(FileCacheManagerTest.class);
		test.addTestSuite(ArchiveEntryInputStreamTest.class);
		test.addTestSuite(ArchiveFileFactoryTest.class);
		test.addTestSuite(ArchiveFileTest.class);
		test.addTestSuite(ArchivePerformanceTest.class);
		test.addTestSuite(ArchiveRemoveTest.class);
		test.addTestSuite(ArchiveViewTest.class);
		test.addTestSuite(UpgradeArchiveTest.class);
		test.addTestSuite(ArchiveFileCacheTest.class);
		test.addTestSuite(ArchiveFileSaveTest.class);
		test.addTestSuite(ArchiveFlushTest.class);
		test.addTestSuite(ArchiveUtilTest.class);
		test.addTestSuite(DocArchiveLockManagerTest.class);
		test.addTestSuite(DocumentArchiveTest.class);
		test.addTestSuite(FileArchiveTest.class);
		test.addTestSuite(FolderArchiveTest.class);
		test.addTestSuite(FolderToArchiveTest.class);
		test.addTestSuite(InputStreamRefreshTest.class);
		test.addTestSuite(SpecialCharacterTest.class);
		test.addTestSuite(BufferTest.class);
		// add all test classes here

		return test;
	}
}
