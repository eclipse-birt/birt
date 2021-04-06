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

package org.eclipse.birt.report.model.util;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.report.model.api.util.URIUtil;

/**
 * TestCases for URIUtil.
 * 
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testFileProtocol()}</td>
 * <td>Tests the disk file.</td>
 * <td><code>getLocalPath</code> returns the filepath.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Tests the http and ftp protocols.</td>
 * <td><code>getLocalPath</code> returns null.</td>
 * </tr>
 * 
 * </table>
 * 
 */

public class URIUtilTest extends BaseTestCase {

	/**
	 * Tests whether the uri is a file protocol.
	 * <p>
	 * The file protocol allows following ways to refer a disk file:
	 * 
	 * For examples, following uri are supported:
	 * 
	 * <ul>
	 * <li>file://C:/disk/test/data.file
	 * <li>/C:/disk/test/data.file
	 * <li>/usr/local/disk/test/data.file
	 * <li>C:\\disk\\test/data.file
	 * <li>C:/disk/test/data.file
	 * <li>./test/data.file
	 * <li>ftp://hello/test/test.html
	 * <li>http://hello/test/test.html
	 * </ul>
	 * 
	 * @throws IOException
	 * 
	 */
	public void testFileProtocol() throws IOException {
		assertEquals("//C:/disk/test/data.file", //$NON-NLS-1$
				URIUtil.getLocalPath("file://C:/disk/test/data.file")); //$NON-NLS-1$

		assertEquals("/C:/disk/test/data.file", //$NON-NLS-1$
				URIUtil.getLocalPath("/C:/disk/test/data.file")); //$NON-NLS-1$

		assertEquals("/usr/local/disk/test/data.file", //$NON-NLS-1$
				URIUtil.getLocalPath("/usr/local/disk/test/data.file")); //$NON-NLS-1$

		// this is not a valid url

		assertEquals("C:\\disk\\test/data.file", //$NON-NLS-1$
				URIUtil.getLocalPath("C:\\disk\\test/data.file")); //$NON-NLS-1$

		assertEquals("C:/disk/test/data.file", //$NON-NLS-1$
				URIUtil.getLocalPath("C:/disk/test/data.file")); //$NON-NLS-1$

		assertEquals("./test/data.file", //$NON-NLS-1$
				URIUtil.getLocalPath("./test/data.file")); //$NON-NLS-1$

		assertNull(URIUtil.getLocalPath("http://hello/test/test.html")); //$NON-NLS-1$

		assertNull(URIUtil.getLocalPath("ftp://hello/test/test.html")); //$NON-NLS-1$

		assertNull(URIUtil.getLocalPath("http://hp.msn.com/2I/_XM9~_`9ZW5I]T9{Q29,@+.jpg")); //$NON-NLS-1$

		assertEquals("hp.msn.com/2I/_XM9~_`9ZW5I]T9{Q29,@+.jpg", //$NON-NLS-1$
				URIUtil.getLocalPath("file://hp.msn.com/2I/_XM9~_`9ZW5I]T9{Q29,@+.jpg")); //$NON-NLS-1$

		assertEquals("C:\\hp.msn.com/2I/_XM9~_`9ZW5I]T9{Q29,@+.jpg", //$NON-NLS-1$
				URIUtil.getLocalPath("C:\\hp.msn.com/2I/_XM9~_`9ZW5I]T9{Q29,@+.jpg")); //$NON-NLS-1$

		assertEquals("/C:/hp.msn.com/2I/_XM9~_`9ZW5I]T9{Q29,@+.jpg", //$NON-NLS-1$
				URIUtil.getLocalPath("file:///C:/hp.msn.com/2I/_XM9~_`9ZW5I]T9{Q29,@+.jpg")); //$NON-NLS-1$

	}

	/**
	 * Tests the method to get relative path. Test cases:
	 * 
	 * <ul>
	 * <li>1. absolute path with different path prefix
	 * <li>2. absolute path with same path prefix
	 * <li>3. relative path with different path prefix
	 * <li>4. relative path with same path prefix
	 * <li>5. the base path with file name. Filename is also treated as a directory.
	 * </ul>
	 */

	public void testGetRelativePath() {
		if (isWindowsPlatform())
			getRelativePathOnWindows();
		else
			getRelativePathOnUnix();

		// common case for both platforms

		assertEquals("../lib/lib1.rptlibrary", URIUtil.getRelativePath( //$NON-NLS-1$
				"./birt/sampleReports/reportdesigns/", //$NON-NLS-1$
				"./birt/sampleReports/lib/lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.getRelativePath("./reportdesigns", //$NON-NLS-1$
						"./reportdesigns/lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("../lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.getRelativePath("./reportdesigns/hello.rptdesign", //$NON-NLS-1$
						"./reportdesigns/lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("", //$NON-NLS-1$
				URIUtil.getRelativePath("./reportdesigns/lib1", //$NON-NLS-1$
						"./reportdesigns/lib1")); //$NON-NLS-1$

		assertNull(URIUtil.getRelativePath(null, null));

		assertEquals("", URIUtil.getRelativePath("", "")); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

		assertEquals("./birt/lib1.rptlibrary", URIUtil.getRelativePath( //$NON-NLS-1$
				"", "./birt/lib1.rptlibrary")); //$NON-NLS-1$ //$NON-NLS-2$

		// relative path supports for fragment

		assertEquals("fragments/new_library.rptlibrary", URIUtil //$NON-NLS-1$
				.getRelativePath("bundleentry://385", //$NON-NLS-1$
						"bundleentry://385/fragments/new_library.rptlibrary")); //$NON-NLS-1$

		assertEquals("../../test.rptdesign", URIUtil.getRelativePath( //$NON-NLS-1$
				"../test/report", "../test.rptdesign")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Test cases on the unix-like platform.
	 * 
	 */

	private void getRelativePathOnUnix() {
		assertEquals("../lib/lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.getRelativePath("/birt/sampleReports/Reportdesigns/", //$NON-NLS-1$
						"/birt/sampleReports/lib/lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("../../../birt/sampleReports/lib/lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.getRelativePath("/usr/sampleReports/reportdesigns/", //$NON-NLS-1$
						"/birt/sampleReports/lib/lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("lib/lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.getRelativePath("/sampleReports//reportdesigns//", //$NON-NLS-1$
						"/sampleReports//reportdesigns//lib//lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("lib1.rptlibrary", URIUtil.getRelativePath( //$NON-NLS-1$
				"/sampleReports//reportdesigns//", //$NON-NLS-1$
				"/sampleReports//reportdesigns//lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("..", URIUtil.getRelativePath( //$NON-NLS-1$
				"//sampleReports//reportdesigns//lib1.rptlibrary", //$NON-NLS-1$
				"//sampleReports//reportdesigns//")); //$NON-NLS-1$
	}

	/**
	 * Test cases on the windows platform.
	 * 
	 */

	private void getRelativePathOnWindows() {
		assertEquals("../lib/lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.getRelativePath("E://birt//sampleReports//Reportdesigns//", //$NON-NLS-1$
						"E://birt//sampleReports//lib//lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("../lib/lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.getRelativePath("/E:/birt/sampleReports/reportdesigns/", //$NON-NLS-1$
						"/E:/birt/sampleReports/lib/lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("../lib/lib1.rptlibrary", URIUtil.getRelativePath( //$NON-NLS-1$
				"E:/birt/sampleReports/reportdesigns/", //$NON-NLS-1$
				"/E:/birt/sampleReports/lib/lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("E:/birt/sampleReports/lib/lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.getRelativePath("D:/birt/sampleReports/reportdesigns/", //$NON-NLS-1$
						"E:/birt/sampleReports/lib/lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("lib/lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.getRelativePath("E://birt//sampleReports//reportdesigns//", //$NON-NLS-1$
						"E://birt//sampleReports//reportdesigns//lib//lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.getRelativePath("E://birt//sampleReports//reportdesigns//", //$NON-NLS-1$
						"E://birt//sampleReports//reportdesigns//lib1.rptlibrary")); //$NON-NLS-1$

		assertEquals("..", URIUtil.getRelativePath( //$NON-NLS-1$
				"E://birt//sampleReports//reportdesigns//lib1.rptlibrary", //$NON-NLS-1$
				"E://birt//sampleReports//reportdesigns//")); //$NON-NLS-1$

		// test directory name with spaces
		assertEquals("spaced directory name/aa/lib.xml", URIUtil.getRelativePath( //$NON-NLS-1$
				"D://", "D://spaced directory name//aa//lib.xml")); //$NON-NLS-1$ //$NON-NLS-2$

		// test directory name with spaces, and resource is on different disk
		// from the base
		assertEquals("D:/spaced directory name/lib.xml", URIUtil.getRelativePath( //$NON-NLS-1$
				"C://", "D://spaced directory name//lib.xml")); //$NON-NLS-1$ //$NON-NLS-2$

		// from bug 160808

		assertEquals("test.library", URIUtil.getRelativePath("c:\\", //$NON-NLS-1$ //$NON-NLS-2$
				"c:\\test.library")); //$NON-NLS-1$
	}

	/**
	 * Tests the method to get relative path. Test cases:
	 * 
	 * <ul>
	 * <li>1. absolute base path
	 * <li>2. relative base path
	 * </ul>
	 */

	public void testResolveAbsolutePath() {
		if (isWindowsPlatform())
			resolveAbsolutePathOnWindows();
		else
			resolveAbsolutePathOnUnix();

		// relative path supports for fragment

		assertEquals("bundleentry://385/fragments/new_library.rptlibrary", //$NON-NLS-1$
				URIUtil.resolveAbsolutePath("bundleentry://385", //$NON-NLS-1$
						"/fragments/new_library.rptlibrary")); //$NON-NLS-1$

		assertEquals("bundleentry://385/fragments/new_library.rptlibrary", //$NON-NLS-1$
				URIUtil.resolveAbsolutePath("bundleentry://385/", //$NON-NLS-1$
						"fragments/new_library.rptlibrary")); //$NON-NLS-1$

		assertEquals("bundleentry://385/fragments/new_library.rptlibrary", //$NON-NLS-1$
				URIUtil.resolveAbsolutePath("bundleentry://385", //$NON-NLS-1$
						"fragments/new_library.rptlibrary")); //$NON-NLS-1$

		assertEquals("http://localhost:8080/c.rptdesign", //$NON-NLS-1$
				URIUtil.resolveAbsolutePath("http://localhost:8080/rp/", //$NON-NLS-1$
						"../c.rptdesign")); //$NON-NLS-1$

		assertEquals("http://localhost:8080/../c.rptdesign", //$NON-NLS-1$
				URIUtil.resolveAbsolutePath("http://localhost:8080", //$NON-NLS-1$
						"../c.rptdesign")); //$NON-NLS-1$

		assertEquals("http://sub", //$NON-NLS-1$
				URIUtil.resolveAbsolutePath("http://server/path", //$NON-NLS-1$
						"http://sub")); //$NON-NLS-1$

		assertEquals("jndi://abc/../sub", //$NON-NLS-1$
				URIUtil.resolveAbsolutePath("jndi://abc", //$NON-NLS-1$
						"../sub")); //$NON-NLS-1$

		assertEquals("jndi://abc/sub", //$NON-NLS-1$
				URIUtil.resolveAbsolutePath("jndi://abc/test", //$NON-NLS-1$
						"../sub")); //$NON-NLS-1$

		assertEquals("jndi://sub", URIUtil.resolveAbsolutePath("jndi://abc", //$NON-NLS-1$ //$NON-NLS-2$
				"jndi://sub")); //$NON-NLS-1$

	}

	/**
	 * Tests the method to get relative path on unix platform.
	 * 
	 */

	private void resolveAbsolutePathOnUnix() {
		assertEquals(File.separator + "birt" + File.separator //$NON-NLS-1$
				+ "sampleReports" + File.separator + "lib" //$NON-NLS-1$ //$NON-NLS-2$
				+ File.separator + "lib1.rptlibrary", //$NON-NLS-1$
				URIUtil.resolveAbsolutePath("/birt//sampleReports//reportdesigns//", //$NON-NLS-1$
								"../lib/lib1.rptlibrary")); //$NON-NLS-1$

		String tmpAbsolutePath = URIUtil.resolveAbsolutePath( // $NON-NLS-1$
				"./reportdesigns", //$NON-NLS-1$
				"./lib/lib1.rptlibrary"); //$NON-NLS-1$
		String tmpPath = File.separator + "reportdesigns" + File.separator //$NON-NLS-1$ //$NON-NLS-2$
				+ "lib" + File.separator //$NON-NLS-1$
				+ "lib1.rptlibrary"; //$NON-NLS-1$
		assertTrue(tmpAbsolutePath.endsWith(tmpPath));

	}

	/**
	 * Tests the method to get relative path on windows platform.
	 * 
	 */

	private void resolveAbsolutePathOnWindows() {
		// test valid device
		assertEquals(("C:" + File.separator + "birt" + File.separator //$NON-NLS-1$ //$NON-NLS-2$
				+ "sampleReports" + File.separator + "lib" //$NON-NLS-1$ //$NON-NLS-2$
				+ File.separator + "lib1.rptlibrary").toLowerCase(), //$NON-NLS-1$
				(URIUtil.resolveAbsolutePath("C://birt//sampleReports//reportdesigns//", //$NON-NLS-1$
						"..\\lib\\lib1.rptlibrary")).toLowerCase()); //$NON-NLS-1$

		String filePath = "reportdesigns" + File.separator + "lib" //$NON-NLS-1$ //$NON-NLS-2$
				+ File.separator + "lib1.rptlibrary"; //$NON-NLS-1$
		assertEquals((new File(filePath).getAbsolutePath()).toLowerCase(),
				(URIUtil.resolveAbsolutePath(".\\reportdesigns", //$NON-NLS-1$
						".\\lib\\lib1.rptlibrary")).toLowerCase()); //$NON-NLS-1$

		assertEquals(("C:" + File.separator + "new_report_3.rptdocument") //$NON-NLS-1$ //$NON-NLS-2$
				.toLowerCase(),
				(URIUtil.resolveAbsolutePath("./reportdesigns", "C:" + File.separator //$NON-NLS-1$//$NON-NLS-2$
						+ "new_report_3.rptdocument")).toLowerCase()); //$NON-NLS-1$

	}
}
