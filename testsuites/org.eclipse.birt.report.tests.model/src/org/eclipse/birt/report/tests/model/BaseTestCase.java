/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import com.ibm.icu.util.ULocale;

/**
 * This class is abstract class used for tests, which contains the design file
 * name and report design handle, and provides the basic design file reading
 * methods. This class performs mainly the following functionalities:
 * <p>
 * <ul>
 * <li>In Setup(), initialize the meda data and store information about meta
 * data in MetaDataDictionary</li>
 * <li>Open a design file, and store the
 * {@link org.eclipse.birt.report.model.elements.ReportDesign}instance and its
 * handle, {@link org.eclipse.birt.report.model.api.ReportDesignHandle}</li>
 * <li>After opening the design file, if the design file contains some syntax or
 * semantic error, the error list can be accessed by this class. This is to make
 * it easy when developing the test cases</li>
 * </ul>
 * <p>
 * Note:
 * <li>The derived unit test class must call 'super.setUp()' in their 'setUp'
 * method.</li>
 * <li>In testing environment, when open a design file by calling openDesign(
 * String fileName, InputStream is ), you can simply pass 'null' as the file
 * name; but, when printing out the error list, to make the file name appear in
 * the message, you can call 'design.setfileName( fileName )' in the child test
 * case.</li>
 */
public abstract class BaseTestCase extends TestCase {
	/**
	 * Byte array output stream.
	 */

	protected ByteArrayOutputStream os = null;

	/**
	 * The report design handle.
	 */

	protected ReportDesignHandle designHandle = null;

	/**
	 * The library handle.
	 */

	protected LibraryHandle libraryHandle = null;

	/**
	 * The session handle.
	 */

	protected SessionHandle sessionHandle = null;

	/**
	 * the root element for this design.
	 */

	protected ReportDesign design = null;

	/**
	 * the root element for this library.
	 */
	protected Library library = null;

	/**
	 * The file name of metadata file.
	 */

	private String caseName;

	// protected static final String BUNDLE_NAME =
	// "org.eclipse.birt.report.tests.model.messages";//$NON-NLS-1$

	// protected static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
	// .getBundle( BUNDLE_NAME );

	protected static final String ROM_DEF_NAME = "rom.def"; //$NON-NLS-1$

	protected static final String PLUGIN_NAME = "org.eclipse.birt.report.tests.model"; //$NON-NLS-1$

	protected static final String PLUGINLOC = "/org.eclipse.birt.report.tests.model/"; //$NON-NLS-1$

	protected static final String PLUGIN_PATH = System.getProperty("user.dir") //$NON-NLS-1$
			+ "/plugins/" + PLUGINLOC.substring(PLUGINLOC.indexOf("/") + 1) //$NON-NLS-1$//$NON-NLS-2$
			+ "bin/"; //$NON-NLS-1$

	protected static final String TEST_FOLDER = "src/"; //$NON-NLS-1$
	protected static final String OUTPUT_FOLDER = "output"; //$NON-NLS-1$
	protected static final String INPUT_FOLDER = "input"; //$NON-NLS-1$
	protected static final String GOLDEN_FOLDER = "golden"; //$NON-NLS-1$

	protected static final ULocale TEST_LOCALE = new ULocale("aa"); //$NON-NLS-1$

	/**
	 * Default constructor.
	 */
	public BaseTestCase() {
		this(null);
	}

	/**
	 * Constructor with a case name.
	 * 
	 * @param name
	 */
	public BaseTestCase(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();

		ThreadResources.setLocale(ULocale.ENGLISH);
//		MetaDataDictionary.reset();
//
//		try {
//			MetaDataReader.read(ReportDesign.class
//					.getResourceAsStream(ROM_DEF_NAME));
//		} catch (MetaDataParserException e) {
//			super.fail();
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		if (designHandle != null)
			designHandle.close();

		super.tearDown();
	}

	protected void setCase(String caseName) {
		// set the case and emitter manager accroding to caseName.
		this.caseName = caseName;
	}

	/*
	 * protected void runCase( String args[] ) { Vector runArgs = new Vector( );
	 * // invoke the report runner. String input = PLUGIN_PATH +
	 * System.getProperty( "file.separator" ) //$NON-NLS-1$ +
	 * RESOURCE_BUNDLE.getString( "CASE_INPUT" ); //$NON-NLS-1$ input +=
	 * System.getProperty( "file.separator" ) + caseName //$NON-NLS-1$ +
	 * ".rptdesign"; //$NON-NLS-1$ System.out.println( "input is : " + input );
	 * //$NON-NLS-1$ // run report runner.
	 * 
	 * if ( args != null ) { for ( int i = 0; i < args.length; i++ ) {
	 * runArgs.add( args[i] ); } } runArgs.add( "-f" ); //$NON-NLS-1$
	 * runArgs.add( "test" ); //$NON-NLS-1$ runArgs.add( input );
	 * 
	 * //args = (String[]) runArgs.toArray( new String[runArgs.size( )] );
	 * //ReportRunner.main( args ); }
	 */
	protected final void copyFile(String from, String to) throws IOException {

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
				if (bis != null)
					bis.close();

				if (bos != null)
					bos.close();
			} catch (IOException e) {
				// ignore
			}

		}
	}

	/**
	 * Locates the temporary path that to save the resource folder and output
	 * folder
	 * 
	 * @return tempDir testing temporary path.
	 */
	public String tempFolder() {
		String tempDir = System.getProperty("java.io.tmpdir");
		if (!tempDir.endsWith(File.separator))
			tempDir += File.separator;
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

	protected void copyResource(String src, String tgt, String folder) {

		String className = getFullQualifiedClassName();
		tgt = this.getInputResourceFolder() + File.separator + className + "/"
				+ folder + "/" + tgt;
		className = className.replace('.', '/');

		// String inputPath =
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

	protected void copyResource_OUTPUT(String output_resource, String output) {
		this.copyResource(output_resource, output, OUTPUT_FOLDER);
	}

	/**
	 * Remove a given file or directory recursively.
	 * 
	 * @param file
	 */
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
	 * Copies the file to the temporary folder.
	 * 
	 * @param resourceName
	 *            the resource name. Based on the class folder.
	 * @return the file path on the disk
	 * @throws Exception
	 */

	protected String copyInputToFile(String resourceName) throws Exception {
		URL url = getResource(resourceName);
		InputStream is = url.openStream();
		// InputStream is = getResourceAStream( resourceName );

		String folder = getTempFolder();

		int index = resourceName.lastIndexOf(INPUT_FOLDER);
		if (index > 0) {
			String relateDir = resourceName.substring(0, index - 1);
			folder = folder + "/" + relateDir; //$NON-NLS-1$
		}

		folder = folder + "/" + INPUT_FOLDER + "/"; //$NON-NLS-1$

		File tmpFolder = new File(folder);
		if (!tmpFolder.exists())
			tmpFolder.mkdirs();

		String filename = ""; //$NON-NLS-1$
		int lastSlash = resourceName.lastIndexOf("/"); //$NON-NLS-1$
		if (lastSlash != -1) {
			filename = resourceName.substring(lastSlash + 1);
		}

		FileOutputStream fos = new FileOutputStream(folder + filename);
		// System.out.println(folder + filename);
		byte[] fileData = new byte[5120];
		int readCount = -1;
		while ((readCount = is.read(fileData)) != -1) {
			fos.write(fileData, 0, readCount);
		}

		fos.close();
		is.close();

		return folder + filename;
	}

	/**
	 * Copies the file to the temporary folder.
	 * 
	 * @param resourceName
	 *            the resource name. Based on the class folder.
	 * @return the file path on the disk
	 * @throws Exception
	 */

	protected String copyGoldenToFile(String resourceName) throws Exception {
		URL url = getResource(resourceName);
		InputStream is = url.openStream();

		String folder = getTempFolder();
		//
		/*
		 * TODO - delete -this code create extra [GoldenFileFolder]/golden, but
		 * why not happen with input int index = resourceName.lastIndexOf(
		 * GOLDEN_FOLDER ); if ( index > 0 ) { String relateDir =
		 * resourceName.substring( 0, index - 1 ); folder = folder + "/" +
		 * relateDir; //$NON-NLS-1$ }
		 */

		folder = folder + "/" + GOLDEN_FOLDER + "/"; //$NON-NLS-1$

		File tmpFolder = new File(folder);
		if (!tmpFolder.exists())
			tmpFolder.mkdirs();

		String filename = ""; //$NON-NLS-1$
		int lastSlash = resourceName.lastIndexOf("/"); //$NON-NLS-1$
		if (lastSlash != -1) {
			filename = resourceName.substring(lastSlash + 1);
		}
		//
		/*
		 * String filename = "/" + resourceName; File tmpFolder = new File(
		 * folder+filename ); if ( !tmpFolder.exists( ) ) tmpFolder.mkdirs( );
		 */

		FileOutputStream fos = new FileOutputStream(folder + filename);

		byte[] fileData = new byte[5120];
		int readCount = -1;
		while ((readCount = is.read(fileData)) != -1) {
			fos.write(fileData, 0, readCount);
		}

		fos.close();
		is.close();

		return folder + filename;

	}

	/**
	 * gets the url of the resource.
	 * 
	 * @param name
	 *            name of the resource
	 * @return the url of the resource
	 */

	protected URL getResource(String name) {
		return this.getClass().getResource(name);
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
		String className = getFullQualifiedClassName();
		removeFile(className);
	}

	/**
	 * Creates a new report.
	 * 
	 * @return the handle for new report
	 */

	protected ReportDesignHandle createDesign() {
		return createDesign(null);
	}

	/**
	 * Creates a new report with given locale.
	 * 
	 * @param locale
	 *            the user locale
	 * @return the handle for new report
	 */

	protected ReportDesignHandle createDesign(ULocale locale) {
		sessionHandle = new DesignEngine(new DesignConfig())
				.newSessionHandle(locale);
		designHandle = sessionHandle.createDesign();
		design = (ReportDesign) designHandle.getModule();

		return designHandle;
	}

	/**
	 * Creates a new library with default locale.
	 * 
	 * @return the handle for new library
	 */

	protected LibraryHandle createLibrary() {
		return createLibrary(null);
	}

	/**
	 * Creates library with given locale.
	 * 
	 * @param locale
	 *            the user locale
	 * @return the handle for new library
	 */

	protected LibraryHandle createLibrary(ULocale locale) {
		sessionHandle = new DesignEngine(new DesignConfig())
				.newSessionHandle(locale);
		libraryHandle = sessionHandle.createLibrary();

		return libraryHandle;
	}

	/**
	 * Opens design file as resource with the given locale.
	 * 
	 * @param fileName
	 *            the file name without path
	 * @param locale
	 *            the given locale
	 * @throws DesignFileException
	 *             if any exception.
	 */

	protected void openDesignAsResource(Class theClass, String fileName,
			ULocale locale) throws DesignFileException {
		fileName = PLUGIN_PATH + this.getFullQualifiedClassName()
				+ INPUT_FOLDER + fileName;
		sessionHandle = new DesignEngine(new DesignConfig())
				.newSessionHandle(ULocale.ENGLISH);
		assertNotNull(sessionHandle);

		InputStream stream = theClass.getResourceAsStream(fileName);
		designHandle = sessionHandle.openDesign(fileName, stream);
		design = (ReportDesign) designHandle.getModule();
	}

	/**
	 * Opens design file with default locale.
	 * 
	 * @param fileName
	 *            design file name
	 * @throws DesignFileException
	 *             if any exception
	 */

	protected void openDesign(String fileName) throws DesignFileException {
		openDesign(fileName, true);
	}

	/**
	 * Opens design file with default locale.
	 * 
	 * @param fileName
	 *            design file name
	 * @param inSingleJarMode
	 *            <code>true</code> if open the design that is in the single
	 *            jar. Otherwise <code>false</code>.
	 * @throws DesignFileException
	 *             if any exception
	 */

	protected void openDesign(String fileName, boolean inSingleJarMode)
			throws DesignFileException {
		openDesign(fileName, ULocale.getDefault(), inSingleJarMode);
	}

	/**
	 * Opens design file providing the file name and the locale.
	 * 
	 * @param fileName
	 *            the design file to be opened
	 * @param locale
	 *            the user locale
	 * @param inSingleJarMode
	 *            <code>true</code> if open the design that is in the single
	 *            jar. Otherwise <code>false</code>.
	 * @throws DesignFileException
	 *             if any exception.
	 */

	protected void openDesign(String fileName, ULocale locale,
			boolean inSingleJarMode) throws DesignFileException {
		String file;
		if (inSingleJarMode)
			file = INPUT_FOLDER + "/" + fileName;
		else
			file = fileName;
		sessionHandle = new DesignEngine(new DesignConfig())
				.newSessionHandle(locale);
		assertNotNull(sessionHandle);

		if (inSingleJarMode) {
			URL url = getResource(file);
			System.out.println("URL = " + url);
			designHandle = sessionHandle.openDesign(url.toString());
		} else {
			designHandle = sessionHandle.openDesign(file);
		}

		design = (ReportDesign) designHandle.getModule();
	}

	/**
	 * Opens design file providing the file name and the locale.
	 * 
	 * @param fileName
	 *            the design file to be opened
	 * @param locale
	 *            the user locale
	 * @param inSingleJarMode
	 *            <code>true</code> if open the design that is in the single
	 *            jar. Otherwise <code>false</code>.
	 * @throws DesignFileException
	 *             if any exception.
	 */

	protected void openDesign(String fileName, ULocale locale)
			throws DesignFileException {
		openDesign(fileName, locale, true);
	}

	/**
	 * Opens library file with given file name.
	 * 
	 * @param fileName
	 *            the library file name
	 * @throws DesignFileException
	 *             if any exception
	 */

	protected void openLibrary(String fileName) throws DesignFileException {
		openLibrary(fileName, true);
	}

	/**
	 * Opens library file with given file name.
	 * 
	 * @param fileName
	 *            the library file name
	 * @param inSingleJarMode
	 *            <code>true</code> if open the design that is in the single
	 *            jar. Otherwise <code>false</code>.
	 * @throws DesignFileException
	 *             if any exception
	 */

	protected void openLibrary(String fileName, boolean inSingleJarMode)
			throws DesignFileException {
		openLibrary(fileName, ULocale.getDefault(), inSingleJarMode);
	}

	/**
	 * Opens library file with given file name and locale.
	 * 
	 * @param fileName
	 *            the library file name
	 * @param locale
	 *            the user locale
	 * @throws DesignFileException
	 *             if any exception
	 */

	protected void openLibrary(String fileName, ULocale locale)
			throws DesignFileException {
		openLibrary(fileName, locale, true);
	}

	/**
	 * Opens library file with given file name and locale.
	 * 
	 * @param fileName
	 *            the library file name
	 * @param locale
	 *            the user locale
	 * @param inSingleJarMode
	 *            <code>true</code> if open the design that is in the single
	 *            jar. Otherwise <code>false</code>.
	 * @throws DesignFileException
	 *             if any exception
	 */

	protected void openLibrary(String fileName, ULocale locale,
			boolean inSingleJarMode) throws DesignFileException {
		if (inSingleJarMode)
			fileName = INPUT_FOLDER + "/" + fileName;

		sessionHandle = new DesignEngine(new DesignConfig())
				.newSessionHandle(locale);
		assertNotNull(sessionHandle);

		if (inSingleJarMode)
			libraryHandle = sessionHandle.openLibrary(getResource(fileName)
					.toString(), getResourceAStream(fileName));
		else
			libraryHandle = sessionHandle.openLibrary(fileName);
	}

	/**
	 * Reads design file as InputStream.
	 * 
	 * @param fileName
	 *            Design file name
	 * @param is
	 *            InputStream of this design file
	 * @throws DesignFileException
	 *             if any exception.
	 */

	protected void openDesign(String fileName, InputStream is)
			throws DesignFileException {
		openDesign(fileName, is, ULocale.getDefault());
	}

	/**
	 * Opens a design file.
	 * 
	 * @param fileName
	 *            the design file name
	 * @param is
	 *            the input stream of the design file.
	 * @param locale
	 *            the user locale.
	 * @throws DesignFileException
	 *             if any exception.
	 */
	protected void openDesign(String fileName, InputStream is, ULocale locale)
			throws DesignFileException {
		sessionHandle = new DesignEngine(new DesignConfig())
				.newSessionHandle(locale);
		designHandle = sessionHandle.openDesign(fileName, is);
		design = (ReportDesign) designHandle.getModule();
	}

	/**
	 * Gets the temp folder of this class.
	 * 
	 * @return temp folder of this class
	 */

	protected String getTempFolder() {
		String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		if (!tempDir.endsWith(File.separator))
			tempDir += File.separator;

		String outputPath = tempDir + "org.eclipse.birt.report.tests.model" //$NON-NLS-1$
				+ getFullQualifiedClassName();
		return outputPath;
	}

	/**
	 * Gets the input stream of the given name resources.
	 */

	protected InputStream getResourceAStream(String name) {
		return this.getClass().getResourceAsStream(name);
	}

	/**
	 * Compares the two text files.
	 * 
	 * @param goldenReader
	 *            the reader for golden file
	 * @param outputReader
	 *            the reader for output file
	 * @return true if two text files are same.
	 * @throws Exception
	 *             if any exception
	 */
	private boolean compareFile(Reader goldenReader, Reader outputReader)
			throws Exception {
		StringBuffer errorText = new StringBuffer();

		BufferedReader lineReaderA = null;
		BufferedReader lineReaderB = null;
		boolean same = true;
		int lineNo = 1;
		try {
			lineReaderA = new BufferedReader(goldenReader);
			lineReaderB = new BufferedReader(outputReader);

			String strA = lineReaderA.readLine().trim();
			String strB = lineReaderB.readLine().trim();
			while (strA != null) {
				same = strA.trim().equals(strB.trim());
				if (!same) {
					StringBuffer message = new StringBuffer();

					message.append("line="); //$NON-NLS-1$
					message.append(lineNo);
					message.append(" is different:\n");//$NON-NLS-1$
					message.append(" The line from golden file: ");//$NON-NLS-1$
					message.append(strA);
					message.append("\n");//$NON-NLS-1$
					message.append(" The line from output file: ");//$NON-NLS-1$
					message.append(strB);
					message.append("\n");//$NON-NLS-1$
					throw new Exception(message.toString());
				}

				strA = lineReaderA.readLine();
				strB = lineReaderB.readLine();
				lineNo++;
			}
			same = strB == null;
		} finally {
			try {
				if (lineReaderA != null)
					lineReaderA.close();
				if (lineReaderB != null)
					lineReaderB.close();
			} catch (Exception e) {
				lineReaderA = null;
				lineReaderB = null;

				errorText.append(e.toString());

				throw new Exception(errorText.toString());
			}
		}

		return same;
	}

	/**
	 * Compares two text file. The comparison will ignore the line containing
	 * "modificationDate".
	 * 
	 * @param goldenFileName
	 *            the 1st file name to be compared.
	 * @param outputFileName
	 *            the 2nd file name to be compared.
	 * @return true if two text files are same line by line
	 * @throws Exception
	 *             if any exception.
	 */
	protected boolean compareFile(String goldenFileName, String outputFileName)
			throws Exception {
		Reader readerA = null;
		FileReader readerB = null;
		boolean same = true;
		StringBuffer errorText = new StringBuffer();

		try {
			goldenFileName = GOLDEN_FOLDER + "/" + goldenFileName;
			outputFileName = getTempFolder() + "/" + OUTPUT_FOLDER + "/"
					+ outputFileName;

			readerA = new InputStreamReader(getResourceAStream(goldenFileName));
			readerB = new FileReader(outputFileName);

			same = compareFile(readerA, readerB);
		} catch (IOException e) {
			errorText.append(e.toString());
			errorText.append("\n"); //$NON-NLS-1$
			e.printStackTrace();
		} finally {
			try {
				if (readerA != null)
					readerA.close();
				if (readerB != null)
					readerB.close();
			} catch (Exception e) {
				readerA = null;
				readerB = null;

				errorText.append(e.toString());

				throw new Exception(errorText.toString());
			}
		}

		return same;
	}

	/**
	 * Compares two text file. The comparison will ignore the line containing
	 * "modificationDate".
	 * 
	 * @param goldenFileName
	 *            the 1st file name to be compared.
	 * @param os
	 *            the 2nd output stream to be compared.
	 * @return true if two text files are same char by char
	 * @throws Exception
	 *             if any exception.
	 */
	protected boolean compareFile(String goldenFileName) throws Exception {
		goldenFileName = GOLDEN_FOLDER + "/" + goldenFileName;

		InputStream streamA = getResourceAStream(goldenFileName);
		if (os == null)
			return false;
		InputStream streamB = new ByteArrayInputStream(os.toByteArray());
		InputStreamReader readerA = new InputStreamReader(streamA);
		InputStreamReader readerB = new InputStreamReader(streamB);
		return compareFile(readerA, readerB);
	}

	/**
	 * Compares two text file. The comparison will ignore the line containing
	 * "modificationDate".
	 * 
	 * @param goldenFileName
	 *            the 1st file name to be compared.
	 * @param outputFileName
	 *            the 2nd file name to be compared.
	 * @return true if two text files are same line by line
	 * @throws Exception
	 *             if any exception.
	 */
	protected boolean compareTextFile(String goldenFileName,
			String outputFileName) throws Exception {
		FileReader readerA = null;
		FileReader readerB = null;
		boolean same = true;
		StringBuffer errorText = new StringBuffer();

		try {
			String resourceName = GOLDEN_FOLDER + java.io.File.separator
					+ goldenFileName;
			String folder = getTempFolder();

			goldenFileName = folder + "/" + resourceName;

			outputFileName = this.genOutputFile(outputFileName);

			readerA = new FileReader(goldenFileName);
			readerB = new FileReader(outputFileName);

			same = compareTextFile(readerA, readerB);
		} catch (IOException e) {
			errorText.append(e.toString());
			errorText.append("\n"); //$NON-NLS-1$
			e.printStackTrace();
		} finally {
			try {
				readerA.close();
				readerB.close();
			} catch (Exception e) {
				readerA = null;
				readerB = null;

				errorText.append(e.toString());

				throw new Exception(errorText.toString());
			}
		}

		return same;
	}

	/**
	 * Compares two text files. The comparison will ignore the line containing
	 * "modificationDate".
	 * 
	 * @param goldenFileName
	 *            the golden file name. The golden file should be located with
	 *            class loader.
	 * @param outputFileName
	 *            the output file name. The output file should be in temperary
	 *            directory of Java VM.
	 * @return true if two text files are same line by line
	 * @throws Exception
	 *             if any exception.
	 */

	protected boolean compareTextFileAsResource(Class theClass,
			String goldenFileName, String outputFileName) throws Exception {
		InputStreamReader readerA = null;
		FileReader readerB = null;
		StringBuffer errorText = new StringBuffer();
		boolean same = false;

		String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		if (!tempDir.endsWith(File.separator))
			tempDir += File.separator;

		try {
			goldenFileName = getFullQualifiedClassName()
					+ "/golden/" + goldenFileName; //$NON-NLS-1$
			InputStream goldenStream = theClass
					.getResourceAsStream(goldenFileName);

			outputFileName = tempDir
					+ "org.eclipse.birt.report.model" //$NON-NLS-1$
					+ getFullQualifiedClassName() + "/" + OUTPUT_FOLDER + "/"
					+ outputFileName;

			readerA = new InputStreamReader(goldenStream);
			readerB = new FileReader(outputFileName);

			same = compareTextFile(readerA, readerB);
		} catch (IOException e) {
			errorText.append(e.toString());
			errorText.append("\n"); //$NON-NLS-1$
			e.printStackTrace();
		} finally {
			try {
				readerA.close();
				readerB.close();
			} catch (Exception e) {
				readerA = null;
				readerB = null;

				errorText.append(e.toString());

				throw new Exception(errorText.toString());
			}
		}

		return same;
	}

	/**
	 * Compares the two text files.
	 * 
	 * @param goldenReader
	 *            the reader for golden file
	 * @param outputReader
	 *            the reader for output file
	 * @return true if two text files are same.
	 * @throws Exception
	 *             if any exception
	 */
	private boolean compareTextFile(Reader goldenReader, Reader outputReader)
			throws Exception {
		StringBuffer errorText = new StringBuffer();

		BufferedReader lineReaderA = null;
		BufferedReader lineReaderB = null;
		boolean same = true;
		int lineNo = 1;
		try {
			lineReaderA = new BufferedReader(goldenReader);
			lineReaderB = new BufferedReader(outputReader);

			String strA = lineReaderA.readLine().trim();
			String strB = lineReaderB.readLine().trim();

			while (strA != null) {
				if ((strA.startsWith("<report xmlns=") && strA //$NON-NLS-1$
						.indexOf("version=") != -1) || //$NON-NLS-1$
						(strA.startsWith("<library xmlns=") && strA //$NON-NLS-1$
								.indexOf("version=") != -1) || //$NON-NLS-1$
						strA.startsWith("<property name=\"fileName\">")) //$NON-NLS-1$ 
				{
					// ignore the comparasion of this line.

					strA = lineReaderA.readLine();
					strB = lineReaderB.readLine();
					if (strA != null)
						strA = strA.trim();
					if (strB != null)
						strB = strB.trim();

					lineNo++;
					continue;
				}

				same = strA.equals(strB);
				if (!same) {
					StringBuffer message = new StringBuffer();

					message.append("line="); //$NON-NLS-1$
					message.append(lineNo);
					message.append(" is different:\n");//$NON-NLS-1$
					message.append(" The line from golden file: ");//$NON-NLS-1$
					message.append(strA);
					message.append("\n");//$NON-NLS-1$
					message.append(" The line from output file: ");//$NON-NLS-1$
					message.append(strB);
					message.append("\n");//$NON-NLS-1$
					throw new Exception(message.toString());
				}

				strA = lineReaderA.readLine();
				strB = lineReaderB.readLine();
				if (strA != null)
					strA = strA.trim();
				if (strB != null)
					strB = strB.trim();

				lineNo++;
			}

			same = strA == null && strB == null;
		} finally {
			try {
				lineReaderA.close();
				lineReaderB.close();
			} catch (Exception e) {
				lineReaderA = null;
				lineReaderB = null;

				errorText.append(e.toString());

				throw new Exception(errorText.toString());
			}
		}

		return same;
	}

	/**
	 * Make a copy of a given file to the target file.
	 * 
	 * @param from
	 *            the file where to copy from
	 * @param to
	 *            the target file to copy to.
	 * @throws IOException
	 */
	/*
	 * protected final void copyFile( String from, String to ) throws
	 * IOException {
	 * 
	 * BufferedInputStream bis = null; BufferedOutputStream bos = null;
	 * 
	 * try { new File( to ).createNewFile( );
	 * 
	 * bis = new BufferedInputStream( new FileInputStream( from ) ); bos = new
	 * BufferedOutputStream( new FileOutputStream( to ) );
	 * 
	 * int nextByte = 0; while ( ( nextByte = bis.read( ) ) != -1 ) { bos.write(
	 * nextByte ); } } catch ( IOException e ) { throw e; } finally { try { if (
	 * bis != null ) bis.close( );
	 * 
	 * if ( bos != null ) bos.close( ); } catch ( IOException e ) { // ignore }
	 * } }
	 */
	/**
	 * Prints out all semantic errors stored in the error list during parsing
	 * the design file.
	 */

	protected void printSemanticErrors() {
		printSemanticError(design);
	}

	/**
	 * Prints out all semantic errors stored in the error list during parsing
	 * the design file.
	 * 
	 * @param design
	 *            report design
	 */

	protected void printSemanticError(ReportDesign design) {
		if (design != null)
			printErrorList(design.getAllErrors());
	}

	/**
	 * Prints out all syntax errors stored in the error list during parsing the
	 * design file.
	 * 
	 * @param e
	 *            <code>DesignFileException</code> containing syntax error list.
	 */

	protected void printSyntaxError(DesignFileException e) {
		if (e != null)
			printErrorList(e.getErrorList());
	}

	/**
	 * Prints error list.
	 * 
	 * @param errors
	 *            error list
	 */
	private void printErrorList(List errors) {
		if (errors != null && !errors.isEmpty()) {
			for (Iterator iter = errors.iterator(); iter.hasNext();) {
				ErrorDetail ex = (ErrorDetail) iter.next();
				System.out.println(ex);
			}
		}
	}

	/**
	 * Dumps the parsing errors into a text file.
	 * 
	 * @param filename
	 *            the file name into which the error will be dumped.
	 * @throws Exception
	 *             if any exception.
	 */

	protected void dumpErrors(String filename) throws Exception {
		String outputFolder = PLUGIN_PATH + getClassFolder() + OUTPUT_FOLDER;
		File f = new File(outputFolder);
		if (!f.exists() && !f.mkdir()) {
			throw new Exception("Can not create the output folder!"); //$NON-NLS-1$
		}
		filename = outputFolder + filename;

		if (design == null)
			return;
		PrintWriter writer = new PrintWriter(new FileOutputStream(filename));
		List errors = design.getAllErrors();
		ErrorDetail ex = null;
		for (int i = 0; i < errors.size(); i++) {
			ex = (ErrorDetail) errors.get(i);
			writer.print(design.getFileName());
			writer.println(ex);
		}
		writer.close();
	}

	/**
	 * Eventually, this method will call
	 * {@link ReportDesignHandle#saveAs(String)}to save the output file of some
	 * unit test. The output test file will be saved in the folder of 'output'
	 * under the folder where the unit test java source file locates, so before
	 * calling {@link ReportDesignHandle#saveAs(String)}, the file name will be
	 * modified to include the path information. For example, in a unit test
	 * class, it can call saveAs( "PropertyCommandTest.out" ).
	 * 
	 * @param filename
	 *            the test output file to be saved.
	 * @throws IOException
	 *             if error occurs while saving the file.
	 */

	protected void save() throws IOException {
		save(designHandle);
	}

	/**
	 * Eventually, this method will call
	 * {@link ReportDesignHandle#serialize(java.io.OutputStream)}to save the
	 * output file of some unit test.
	 * 
	 * @param moduleHandle
	 *            the module to save, either a report design or a library
	 * @throws IOException
	 *             if error occurs while saving the file.
	 */

	protected void save(ModuleHandle moduleHandle) throws IOException {
		os = new ByteArrayOutputStream();
		if (moduleHandle != null)
			moduleHandle.serialize(os);
		os.close();
	}

	/**
	 * Saves library as the given file name.
	 * 
	 * @param filename
	 *            the file name for saving
	 * @throws IOException
	 *             if any exception
	 */

	protected void saveLibrary() throws IOException {
		save(libraryHandle);
	}

	/**
	 * Eventually, this method will call
	 * {@link ReportDesignHandle#serialize(java.io.OutputStream)}to save the
	 * output file of some unit test.
	 * 
	 * @param filename
	 *            the test output file to be saved.
	 * @throws IOException
	 *             if error occurs while saving the file.
	 */

	protected void saveAs(String filename) throws IOException {
		saveAs(designHandle, filename);
	}

	/**
	 * Eventually, this method will call
	 * {@link ReportDesignHandle#saveAs(String)}to save the output file of some
	 * unit test. The output test file will be saved in the folder of 'output'
	 * under the folder where the unit test java source file locates, so before
	 * calling {@link ReportDesignHandle#saveAs(String)}, the file name will be
	 * modified to include the path information. For example, in a unit test
	 * class, it can call saveAs( "PropertyCommandTest.out" ).
	 * 
	 * @param moduleHandle
	 *            the module to save, either a report design or a library
	 * @param filename
	 *            the test output file to be saved.
	 * @throws IOException
	 *             if error occurs while saving the file.
	 */

	protected void saveAs(ModuleHandle moduleHandle, String filename)
			throws IOException {
		if (moduleHandle == null)
			return;

		// makeOutputDir( );
		moduleHandle.saveAs(this.genOutputFile(filename));
	}

	/**
	 * Create output folder under current class folder.
	 * 
	 * @throws IOException
	 */

	protected void makeOutputDir() throws IOException {
		// String outputPath = getClassFolder( ) + "/" + OUTPUT_FOLDER;
		String outputPath = this.getFullQualifiedClassName() + "/"
				+ OUTPUT_FOLDER;

		File parent = new File(outputPath).getParentFile();

		if (parent != null) {
			parent.mkdirs();
		}

		outputPath = parent + "/" + OUTPUT_FOLDER;

		File outputFolder = new File(outputPath);
		if (!outputFolder.exists() && !outputFolder.mkdir()) {
			throw new IOException("Can not create the output folder"); //$NON-NLS-1$
		}

	}

	/**
	 * Saves the design file to temp directory.
	 * 
	 * @param filename
	 *            the new file name to save
	 * @throws IOException
	 *             if any exception
	 */
	protected void saveLibraryAs(String filename) throws IOException {
		if (libraryHandle == null)
			return;
		String outputPath = getTempFolder() + "/" + OUTPUT_FOLDER;
		File outputFolder = new File(outputPath);
		if (!outputFolder.exists() && !outputFolder.mkdir()) {
			throw new IOException("Can not create the output folder"); //$NON-NLS-1$
		}
		libraryHandle.saveAs(outputPath + "/" + filename);
	}

	/**
	 * Saves the library file to temp directory.
	 * 
	 * @param filename
	 *            the new file name to save
	 * @throws IOException
	 *             if any exception
	 */

	protected void saveAsInTempDir(String filename) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		if (!tempDir.endsWith(File.separator))
			tempDir += File.separator;

		if (designHandle == null)
			return;
		String outputPath = tempDir + "org.eclipse.birt.report.model" //$NON-NLS-1$
				+ getFullQualifiedClassName() + OUTPUT_FOLDER;
		File outputFolder = new File(outputPath);
		if (!outputFolder.exists() && !outputFolder.mkdirs()) {
			throw new IOException("Can not create the output folder"); //$NON-NLS-1$
		}
		designHandle.saveAs(outputPath + filename);
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

				if (pathBase.endsWith("bin/")) //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 4);
				if (pathBase.endsWith("bin")) //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 3);
			}
		}

		pathBase = pathBase + TEST_FOLDER;
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);
		className = pathBase + className.replace('.', '/');

		return className;
	}

	/**
	 * Returns the full qualified class name. For example,
	 * "/org/eclipse/birt/report/model".
	 * 
	 * @return the full qualified class name
	 */

	protected String getFullQualifiedClassName() {
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);
		className = "/" + className.replace('.', '/'); //$NON-NLS-1$

		return className;
	}

	/**
	 * Convert input stream to a byte array.
	 * 
	 * @param is
	 * @return byte array
	 * @throws IOException
	 */

	protected byte[] streamToBytes(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[100];
		int len;
		while ((len = is.read(buffer)) > 0) {
			bos.write(buffer, 0, len);
		}

		byte[] bytes = bos.toByteArray();
		return bytes;
	}

	/**
	 * Compares the error messages against the golden file.
	 * 
	 * @param filename
	 *            the golden file name which contains the error messages.
	 * @throws Exception
	 *             if any exception
	 */

	protected void compareErrors(String filename) throws Exception {
		filename = this.genOutputFile(filename);

		if (design == null)
			return;
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		List errors = design.getAllErrors();
		String msg = null;
		String msgLine = null;
		ErrorDetail ex = null;
		for (int i = 0; i < errors.size(); i++) {
			ex = (ErrorDetail) errors.get(i);
			msg = design.getFileName() + ex;

			msgLine = reader.readLine();
			assertTrue(msgLine != null && msg.equals(msgLine));
		}
		reader.close();
	}

	protected String genOutputFile(String output) {
		final String SEPARATOR = File.separator;
		String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		if (!tempDir.endsWith(File.separator))
			tempDir += File.separator;
		String outputFileName = tempDir + getFullQualifiedClassName() //$NON-NLS-1$
				+ SEPARATOR + OUTPUT_FOLDER + SEPARATOR + output;
		File outputFile = new File(outputFileName);
		// add these code to create new file
		try {
			outputFile.getParentFile().mkdirs();
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
		return outputFileName;
	}

	protected void saveOutputFile(String fileName) throws Exception {
		String folder = getTempFolder() + OUTPUT_FOLDER;
		File tmpFolder = new File(folder);
		if (!tmpFolder.exists())
			tmpFolder.mkdirs();

		String strDesign = os.toString();
		FileOutputStream fos = new FileOutputStream(folder + "/" + fileName);
		fos.write(strDesign.getBytes("UTF-8")); //$NON-NLS-1$

		fos.close();
	}

}
