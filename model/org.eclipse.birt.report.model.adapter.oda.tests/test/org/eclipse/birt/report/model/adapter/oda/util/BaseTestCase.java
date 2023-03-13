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

package org.eclipse.birt.report.model.adapter.oda.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.util.SerializerImpl;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.elements.ReportDesign;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 * This class is abstract class used for tests, which contains the design file
 * name and report design handle, and provides the basic design file reading
 * methods.
 *
 * This class performs mainly the following functionalities:
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
 *
 */
public abstract class BaseTestCase extends TestCase {

	/**
	 * The report design handle.
	 */

	protected ReportDesignHandle designHandle = null;

	/**
	 * The report design handle.
	 */

	protected LibraryHandle libraryHandle = null;

	/**
	 * The opened module handle.
	 */

	protected ModuleHandle moduleHandle = null;

	/**
	 * The session handle.
	 */

	protected SessionHandle sessionHandle = null;

	/**
	 * Byte array output stream.
	 */

	protected ByteArrayOutputStream os = null;

	/**
	 * The file name of metadata file.
	 */
	protected static final String ROM_DEF_NAME = "rom.def"; //$NON-NLS-1$

	protected static final String TEST_FOLDER = "test/"; //$NON-NLS-1$
	protected static final String OUTPUT_FOLDER = "/output/"; //$NON-NLS-1$
	protected static final String INPUT_FOLDER = "input/"; //$NON-NLS-1$
	protected static final String GOLDEN_FOLDER = "golden/"; //$NON-NLS-1$

	protected static final ULocale TEST_LOCALE = new ULocale("aa"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		if (designHandle != null) {
			designHandle.close();
		}

		if (libraryHandle != null) {
			libraryHandle.close();
		}

		if (os != null) {
			os.close();
		}

		super.tearDown();
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
	 * @param locale the user locale
	 * @return the handle for new report
	 */

	protected ReportDesignHandle createDesign(ULocale locale) {
		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(locale);
		designHandle = sessionHandle.createDesign();

		removeExtensionStyles();
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
	 * @param locale the user locale
	 * @return the handle for new library
	 */

	protected LibraryHandle createLibrary(ULocale locale) {
		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(locale);
		libraryHandle = sessionHandle.createLibrary();

		return libraryHandle;
	}

	/**
	 * Opens design file with default locale.
	 *
	 * @param fileName design file name
	 * @throws DesignFileException if any exception
	 */

	protected void openDesign(String fileName) throws DesignFileException {
		openDesign(fileName, ULocale.getDefault());
	}

	/**
	 * Opens design file providing the file name and the locale.
	 *
	 * @param fileName the design file to be opened
	 * @param locale   the user locale
	 * @throws DesignFileException if any exception.
	 */

	protected void openDesign(String fileName, ULocale locale) throws DesignFileException {
		openDesign(fileName, ULocale.getDefault(), true);
	}

	/**
	 * Opens design file providing the file name and the locale.
	 *
	 * @param fileName        the design file to be opened
	 * @param locale          the user locale
	 * @param inSingleJarMode <code>true</code> if open the design that is in the
	 *                        single jar. Otherwise <code>false</code>.
	 * @throws DesignFileException if any exception.
	 */

	protected void openDesign(String fileName, ULocale locale, boolean inSingleJarMode) throws DesignFileException {
		if (inSingleJarMode) {
			fileName = INPUT_FOLDER + fileName;
		}

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(locale);
		assertNotNull(sessionHandle);

		if (inSingleJarMode) {
			designHandle = sessionHandle.openDesign(getResource(fileName).toString());
		} else {
			designHandle = sessionHandle.openDesign(fileName);
		}

	}

	/**
	 * gets the url of the resource.
	 *
	 * @param name name of the resource
	 * @return the url of the resource
	 */

	protected URL getResource(String name) {
		return this.getClass().getResource(name);
	}

	/**
	 * Gets the input stream of the given name resources.
	 *
	 * @name resource name
	 * @return input stream of resource.
	 *
	 */

	protected InputStream getResourceAStream(String name) {
		return this.getClass().getResourceAsStream(name);
	}

	/**
	 * Opens design file as resource with default locale.
	 *
	 * @param fileName the file name without path
	 * @throws DesignFileException if any exception.
	 */

	protected void openDesignAsResource(Class theClass, String fileName) throws DesignFileException {
		openDesignAsResource(theClass, fileName, ULocale.getDefault());
	}

	/**
	 * Opens design file as resource with the given locale.
	 *
	 * @param fileName the file name without path
	 * @param locale   the given locale
	 * @throws DesignFileException if any exception.
	 */

	protected void openDesignAsResource(Class theClass, String fileName, ULocale locale) throws DesignFileException {
		fileName = getFullQualifiedClassName() + INPUT_FOLDER + fileName;
		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		assertNotNull(sessionHandle);

		InputStream stream = theClass.getResourceAsStream(fileName);
		designHandle = sessionHandle.openDesign(fileName, stream);
	}

	/**
	 * Reads design file as InputStream.
	 *
	 * @param fileName Design file name
	 * @param is       InputStream of this design file
	 * @throws DesignFileException if any exception.
	 */

	protected void openDesign(String fileName, InputStream is) throws DesignFileException {
		openDesign(fileName, is, ULocale.getDefault());
	}

	/**
	 * Opens a design file.
	 *
	 * @param fileName the design file name
	 * @param is       the input stream of the design file.
	 * @param locale   the user locale.
	 * @throws DesignFileException if any exception.
	 */
	protected void openDesign(String fileName, InputStream is, ULocale locale) throws DesignFileException {
		sessionHandle = DesignEngine.newSession(locale);
		designHandle = sessionHandle.openDesign(fileName, is);
	}

	/**
	 * Compares two text file. The comparison will ignore the line containing
	 * "modificationDate".
	 *
	 * @param goldenFileName the 1st file name to be compared.
	 * @param os             the 2nd output stream to be compared.
	 * @return true if two text files are same char by char
	 * @throws Exception if any exception.
	 */
	protected boolean compareTextFile(String goldenFileName) throws Exception {
		String tmpFileName = GOLDEN_FOLDER + goldenFileName;

		InputStream streamA = getResourceAStream(tmpFileName);
		if (os == null) {
			return false;
		}

		String outContent = os.toString("utf-8"); //$NON-NLS-1$
		InputStream streamB = new ByteArrayInputStream(os.toByteArray());
		InputStreamReader readerA = new InputStreamReader(streamA);
		InputStreamReader readerB = new InputStreamReader(streamB);

		boolean ok = true;
		try {
			ok = compareTextFile(readerA, readerB);
		} catch (Exception e) {
			String outFileName = goldenFileName.replace("golden", "out");
			saveOutputFile(outFileName, outContent);

			throw e;
		}

		return ok;
	}

	/**
	 * Compares the two text files.
	 *
	 * @param goldenReader the reader for golden file
	 * @param outputReader the reader for output file
	 * @return true if two text files are same.
	 * @throws Exception if any exception
	 */
	private boolean compareTextFile(Reader goldenReader, Reader outputReader) throws Exception {
		StringBuilder errorText = new StringBuilder();

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
					StringBuilder message = new StringBuilder();

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
			same = strA == null && strB == null;
		} finally {
			try {
				if (lineReaderA != null) {
					lineReaderA.close();
				}
				if (lineReaderB != null) {
					lineReaderB.close();
				}
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
	 * Prints out all semantic errors stored in the error list during parsing the
	 * design file.
	 *
	 * @param design report design
	 */

	protected void printSemanticError(ReportDesign design) {
		if (design != null) {
			printErrorList(design.getAllErrors());
		}
	}

	/**
	 * Prints out all syntax errors stored in the error list during parsing the
	 * design file.
	 *
	 * @param e <code>DesignFileException</code> containing syntax error list.
	 */

	protected void printSyntaxError(DesignFileException e) {
		if (e != null) {
			printErrorList(e.getErrorList());
		}
	}

	/**
	 * Prints error list.
	 *
	 * @param errors error list
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
	 * Saves library as the given file name.
	 *
	 * @param filename the file name for saving
	 * @throws IOException if any exception
	 */

	protected void saveLibrary() throws IOException {
		save(libraryHandle);
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
	 * @param values
	 * @param fileName
	 * @throws IOException
	 *
	 */

	protected void saveDesignValuesToFile(DesignValues values) throws IOException {
		if (os != null) {
			os.close();
			os = null;
		}
		os = new ByteArrayOutputStream();
		SerializerImpl.instance().write(values, os);
		os.close();
	}

	/**
	 * Parses an input file as the design values instance.
	 *
	 * @param fileName
	 * @return
	 * @throws IOException
	 *
	 *
	 */

	protected DesignValues readDesignValuesFromFile(String fileName) throws IOException {
		fileName = INPUT_FOLDER + fileName;

		InputStream is = getResource(fileName).openStream();

		BufferedInputStream baIs = new BufferedInputStream(is);

		byte[] b = new byte[8192];
		baIs.read(b);

		String strDesignValues = new String(b, "utf-8");

		DesignValues tmpValues = SerializerImpl.instance().read(strDesignValues);

		baIs.close();
		is.close();

		return tmpValues;
	}

	/**
	 * Eventually, this method will call
	 * {@link ReportDesignHandle#serialize(java.io.OutputStream)}to save the output
	 * file of some unit test.
	 *
	 * @param filename the test output file to be saved.
	 * @throws IOException if error occurs while saving the file.
	 */

	protected void save() throws IOException {
		save(designHandle);
	}

	/**
	 * Eventually, this method will call
	 * {@link ReportDesignHandle#serialize(java.io.OutputStream)}to save the output
	 * file of some unit test.
	 *
	 * @param moduleHandle the module to save, either a report design or a library
	 * @throws IOException if error occurs while saving the file.
	 */

	protected void save(ModuleHandle moduleHandle) throws IOException {
		if (os != null) {
			os.close();
			os = null;
		}
		os = new ByteArrayOutputStream();
		if (moduleHandle != null) {
			moduleHandle.serialize(os);
		}
		os.close();
	}

	/**
	 * Saves the output stream into the output file.
	 *
	 * @param fileName the resource name. Based on the class folder.
	 * @throws Exception
	 */

	protected void saveOutputFile(String fileName) throws Exception {
		String strDesign = os.toString();
		saveOutputFile(fileName, strDesign);
	}

	/**
	 * Saves the output stream into the output file.
	 *
	 * @param fileName the resource name. Based on the class folder.
	 * @throws Exception
	 */

	protected void saveOutputFile(String fileName, String content) throws Exception {
		String folder = getTempFolder() + OUTPUT_FOLDER;
		File tmpFolder = new File(folder);
		if (!tmpFolder.exists()) {
			tmpFolder.mkdirs();
		}

		FileOutputStream fos = new FileOutputStream(folder + fileName);
		fos.write(content.getBytes("UTF-8")); //$NON-NLS-1$

		fos.close();
	}

	/**
	 * Gets the temp folder of this class.
	 *
	 * @return temp folder of this class
	 */

	protected String getTempFolder() {
		String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		if (!tempDir.endsWith(File.separator)) {
			tempDir += File.separator;
		}

		String outputPath = tempDir + "org.eclipse.birt.report.model.adapter.oda" //$NON-NLS-1$
				+ getFullQualifiedClassName();
		return outputPath;
	}

	/**
	 *
	 */

	private void removeExtensionStyles() {
		ContainerSlot styles = designHandle.getModule().getSlot(ReportDesign.STYLE_SLOT);
		styles.clear();
	}

	/**
	 * Saves the output stream into the temp file for verification.
	 *
	 * @param fileName the resource name. Based on the class folder.
	 * @throws Exception
	 */

	protected String saveTempFile() {
		try {
			save();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String folder = getTempFolder() + OUTPUT_FOLDER;
		File tmpFolder = new File(folder);
		if (!tmpFolder.exists()) {
			tmpFolder.mkdirs();
		}

		String fileName = folder + "tmp_" + os.hashCode();

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(os.toString().getBytes("UTF-8")); //$NON-NLS-1$
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileName;
	}

	/**
	 * Save and open design
	 *
	 * @param fileName
	 * @param locale
	 * @param inSingleJarMode
	 * @throws Exception
	 */
	protected void saveAndOpenDesign() throws Exception {
		String fileName = saveTempFile();
		File file = new File(fileName);

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.getDefault());
		assertNotNull(sessionHandle);

		designHandle = sessionHandle.openDesign(fileName);

		file.delete();

	}

}
