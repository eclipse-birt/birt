/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.chart;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import utility.ImageUtil;
import utility.ImageUtil.ImageCompParam;

/**
 * Base chart test case.
 */
public class ChartTestCase extends TestCase {

	protected static final String TEST_FOLDER = "src"; //$NON-NLS-1$
	protected static final String OUTPUT_FOLDER = "output"; //$NON-NLS-1$
	protected static final String INPUT_FOLDER = "input"; //$NON-NLS-1$
	protected static final String GOLDEN_FOLDER = "golden" + getOSName(); //$NON-NLS-1$
	protected static final String PLUGIN_NAME = "org.eclipse.birt.report.tests.chart"; //$NON-NLS-1$

	private static String getOSName() {
		String name = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
		if (name.indexOf("win") >= 0) //$NON-NLS-1$
		{
			return ""; //$NON-NLS-1$
		}
		return "_" + name; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// make the output directory.

		String tempDir = System.getProperty("java.io.tmpdir");
		if (!tempDir.endsWith(File.separator)) {
			tempDir += File.separator;
		}

		String outputPath = this.genOutputFolder();
		outputPath = outputPath.replace('\\', '/');

		File outputFolder = new File(outputPath);

		File parent = new File(outputPath).getParentFile();

		if (parent != null) {
			parent.mkdirs();
		}

		if (!outputFolder.exists() && !outputFolder.mkdir()) {
			throw new IOException("Can not create the output folder"); //$NON-NLS-1$
		}
	}

	/**
	 * Compares two byte arrays. Disallow <code>null</code> values.
	 *
	 * @param bytes1
	 * @param bytes2
	 * @return <code>true</code> if <code>bytes1</code> and <code>bytes2</code> have
	 *         the same lengths and content. In all other cases returns
	 *         <code>false</code>.
	 */
	protected boolean compare(byte[] bytes1, byte[] bytes2) {
		if (bytes1.length != bytes2.length) {
			return false;
		}

		for (int i = 0; i < bytes1.length; i++) {
			if (bytes1[i] != bytes2[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Compare golden with output byte by byte.
	 *
	 * @param golden
	 * @param output
	 * @return
	 * @throws Exception
	 */

	protected boolean compareBytes(String golden, String output) throws Exception {
		String className = getFullQualifiedClassName();
		className = className.replace('.', '/');
		golden = className + "/" + GOLDEN_FOLDER + "/" + golden;

		InputStream is1 = getClass().getClassLoader().getResourceAsStream(golden);
		InputStream is2 = new FileInputStream(this.genOutputFile(output));

		boolean compareResult = this.compare(is1, is2);

		if (!compareResult) {
			String goldenFrom = golden;
			// String goldenTo = this.genOutputFile( "diffGolden/" + output );
			String goldenTo = this.getOutputResourceFolder() + "/" + this.getFullQualifiedClassName() + "/"
					+ "diffGolden/" + output;
			String outputFrom = this.genOutputFile(output);
			String outputTo = this.getOutputResourceFolder() + "/" + this.getFullQualifiedClassName() + "/diffOutput/"
					+ output;

			File parentOutput = new File(outputTo).getParentFile();

			if (parentOutput != null) {
				parentOutput.mkdirs();
			}

			this.copyFile(outputFrom, outputTo);

			File parentGolden = new File(goldenTo).getParentFile();

			if (parentGolden != null) {
				parentGolden.mkdirs();
			}

			InputStream in = getClass().getClassLoader().getResourceAsStream(goldenFrom);
			assertTrue(in != null);
			try {
				FileOutputStream fos = new FileOutputStream(goldenTo);
				byte[] fileData = new byte[5120];
				int readCount = -1;
				while ((readCount = in.read(fileData)) != -1) {
					fos.write(fileData, 0, readCount);
				}
				fos.close();
				in.close();

			} catch (Exception ex) {
				ex.printStackTrace();
				fail();
			}

		}

		return compareResult;
	}

	/**
	 * computeFiles method compares two input streams and returns whether the
	 * contents of the input streams match.
	 *
	 * @param left  - inputstream of the first resource
	 * @param right - inputstream of the second resource
	 * @return true if the contents match; otherwise, false is returned.
	 * @throws Exception thrown if io errors occur
	 */
	protected boolean compare(InputStream golden, InputStream output) throws Exception {
		int goldenChar = -1;
		while ((goldenChar = golden.read()) != -1) {
			if (goldenChar != output.read()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 *
	 * @return the path name where the test java source file locates.
	 */

	protected String getClassFolder() {
		String pathBase = null;

		ProtectionDomain domain = this.getClass().getProtectionDomain();
		if (domain != null) {
			CodeSource source = domain.getCodeSource();
			if (source != null) {
				URL url = source.getLocation();
				pathBase = url.getPath();

				if (pathBase.endsWith("bin/")) { //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 4);
				}
				if (pathBase.endsWith("bin")) { //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 3);
				}
			}
		}

		pathBase = pathBase + TEST_FOLDER;
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);
		className = pathBase + "/" + className.replace('.', '/');

		return className;
	}

	/**
	 * Locates the folder where the unit test java source file is saved, used in
	 * standalone test case.
	 *
	 * @return the path name where the test java source file locates.
	 */

	protected String getClassFolder2() {
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);
		className = TEST_FOLDER + className.replace('.', '/');

		return className;
	}

	/**
	 * Get the class name.
	 *
	 * @return the class name
	 */
	protected String getFullQualifiedClassName() {
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = PLUGIN_NAME + className.substring(PLUGIN_NAME.length(), lastDotIndex).replace('.', '/');
		// className = className.substring( 0 , lastDotIndex );

		return className;
	}

	/**
	 * Set the output path. And the path will set in java.io.tmpdir.
	 *
	 * @return the the output path
	 */
	public String tempFolder() {
		String tempDir = System.getProperty("java.io.tmpdir");
		if (!tempDir.endsWith(File.separator)) {
			tempDir += File.separator;
		}
		return tempDir;
	}

	public String getInputResourceFolder() {
		String resourceFolder = this.tempFolder() + PLUGIN_NAME + ".RESOURCE";
		return resourceFolder;
	}

	public String getOutputResourceFolder() {
		String outputFolder = this.tempFolder() + PLUGIN_NAME + ".OUTPUT";
		return outputFolder;
	}

	protected String genOutputFolder() {
		String outputFolder = this.getOutputResourceFolder() + File.separator + getFullQualifiedClassName() // $NON-NLS-1$
				+ "/" + OUTPUT_FOLDER;
		return outputFolder;
	}

	protected String genInputFolder() {
		String inputFolder = this.getInputResourceFolder() + File.separator + getFullQualifiedClassName() // $NON-NLS-1$
				+ "/" + INPUT_FOLDER;
		return inputFolder;
	}

	protected String genGoldenFolder() {
		String goldenFolder = this.getInputResourceFolder() + File.separator + getFullQualifiedClassName() // $NON-NLS-1$
				+ "/" + GOLDEN_FOLDER;
		return goldenFolder;
	}

	protected String genOutputFile(String output) {
		String outputFile = this.genOutputFolder() + File.separator + output;
		return outputFile;
	}

	protected String genInputFile(String input) {
		String inputFile = this.genInputFolder() + File.separator + input;
		return inputFile;
	}

	protected String genGoldenFile(String golden) {
		String goldenFile = this.genGoldenFolder() + File.separator + golden;
		return goldenFile;
	}

	/**
	 * Make a copy of a given file to the target file.
	 *
	 * @param src:    the file where to copy from
	 * @param tgt:    the target file to copy to
	 * @param folder: the folder that the copied file in.
	 */
	protected void copyResource(String src, String tgt, String folder) {

		String className = getFullQualifiedClassName();
		tgt = this.getInputResourceFolder() + File.separator + className + "/" + folder + "/" + tgt;
		className = className.replace('.', '/');

		src = className + "/" + folder + "/" + src;

		File parent = new File(tgt).getParentFile();

		if (parent != null) {
			parent.mkdirs();
		}

		InputStream in = getClass().getClassLoader().getResourceAsStream(src);
		assertTrue(in != null);
		try {
			FileOutputStream fos = new FileOutputStream(tgt);
			byte[] fileData = new byte[5120];
			int readCount = -1;
			while ((readCount = in.read(fileData)) != -1) {
				fos.write(fileData, 0, readCount);
			}
			fos.close();
			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	protected void copyResource_INPUT(String input_resource, String input) {
		this.copyResource(input_resource, input, INPUT_FOLDER);
	}

	protected void copyResource_GOLDEN(String input_resource, String golden) {
		this.copyResource(input_resource, golden, GOLDEN_FOLDER);
	}

	protected void copyResource_SCRIPT(String input_resource, String script) {
		this.copyResource(input_resource, script, "input/scripts");
	}

	public void removeFile(File file) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (int i = 0; i < children.length; i++) {
				removeFile(children[i]);
			}
		}
		if (file.exists()) {
			if (!file.delete()) {
				System.out.println(file.toString() + " can't be removed"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Remove a given file or directory recursively.
	 *
	 * @param file
	 */

	public void removeFile(String file) {
		removeFile(new File(file));
	}

	public void removeResource() {
//		String className = getFullQualifiedClassName( );
		removeFile(this.getInputResourceFolder());
	}

	protected final void copyFile(String from, String to) throws IOException {
		File parent = new File(to).getParentFile();

		if (parent != null) {
			parent.mkdirs();
		}

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			new File(to).createNewFile();

			bis = new BufferedInputStream(new FileInputStream(from));
			bos = new BufferedOutputStream(new FileOutputStream(to));

			int nextByte = 0;
			while ((nextByte = bis.read()) != -1) {
				bos.write(nextByte);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}

				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				// ignore
			}

		}
	}

	protected boolean compareImages(String golden, String output) throws Exception {
		String goldenFile = TEST_FOLDER + File.separator + getFullQualifiedClassName().replace('.', '/')
				+ File.separator + GOLDEN_FOLDER + File.separator + golden;

		String outputFile = genOutputFolder() + File.separator + output;

		Map<ImageCompParam, Integer> params = new HashMap<>();
		params.put(ImageCompParam.TOLERANCE, 4);

		Image result = ImageUtil.compare(new File(goldenFile).getAbsolutePath(), new File(outputFile).getAbsolutePath(),
				params);
		if (result == null) {
			return true;
		}

		// ImageUtil.saveJPG(result, new File(outputFile + ".diff").getAbsolutePath());

		// Overwrite so we can compare the old and the new easily in the IDE.
		System.err.println("saving>" + new File(goldenFile).getAbsolutePath());
		Files.copy(Path.of(new File(outputFile).getAbsolutePath()), Path.of(new File(goldenFile).getAbsolutePath()),
				StandardCopyOption.REPLACE_EXISTING);

		return false;
	}

}
