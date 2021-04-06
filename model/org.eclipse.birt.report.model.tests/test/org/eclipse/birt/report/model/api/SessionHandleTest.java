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

package org.eclipse.birt.report.model.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.activity.ReadOnlyActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryChangeEvent;
import org.eclipse.birt.report.model.api.command.ResourceChangeEvent;
import org.eclipse.birt.report.model.api.core.IResourceChangeListener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * This test case is used to test <code>SessionHandle<code> and 
 * <code>DesignSession</code>.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" * bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th> <th * width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testCreateOpenAndClose()}</td>
 * <td>Open design file</td>
 * <td>needSave is false, isValid is true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Set property</td>
 * <td>needSave is true, isValid is true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Save as it</td>
 * <td>needSave is false, isValid is true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Add new element</td>
 * <td>needSave is true, isValid is true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Close this design file</td>
 * <td>needSave is false, isValid is false</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Create new design file</td>
 * <td>needSave is false, isValid is true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Add new element</td>
 * <td>needSave is true, isValid is true</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Close the new design file</td>
 * <td>needSave is false, isValid is false</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testOpenNonExistedFile()}</td>
 * <td>Tests opening the design file which doesn't exist.</td>
 * <td>Exception with FILE_NOT_FOUND should be thrown.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testOpenWithWrongTag()}</td>
 * <td>Tests opening the design file with the tag which is not defined in
 * DE.</td>
 * <td>Exception with SYNTAX_ERROR should be thrown.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testOpenWithMissingStartingTagError()}</td>
 * <td>Tests opening the design file with the tag which is not started
 * correctly.</td>
 * <td>Exception with SAX_EXCEPTION should be thrown.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testOpenWithUnmatchedTagError()}</td>
 * <td>Tests opening the design file with the tag which does not match the end
 * one.</td>
 * <td>Exception with SAX_EXCEPTION should be thrown.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testOpenWithInvalidAttrError()}</td>
 * <td>Tests opening the design file with the attribue which is not defined in
 * DE.</td>
 * <td>No error will be found for this invalid attribute will be ignored.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testOpenWithSemanticError()}</td>
 * <td>Tests whether the design is valid after opening a design file with
 * semantic error</td>
 * <td>The design should be invalid</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testOpenWithUndefinedProperty()}</td>
 * <td>Tests whether the design is valid when it has undefined properties.</td>
 * <td>The design should be valid but errors are saved in the design.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testOpenWithBOM()}</td>
 * <td>Tests to read a design file with byte order mark (BOM) in a windows
 * compatible UTF file.</td>
 * <td>The design should be valid.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSessionProperties()}</td>
 * <td>Tests properties of the session</td>
 * <td>Gets and sets properties correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testULocale()}</td>
 * <td>Tests locales of sessions.</td>
 * <td>Gets locales correctly.</td>
 * </tr>
 * 
 * </table>
 * 
 * @see org.eclipse.birt.report.model.api.SessionHandle
 * @see org.eclipse.birt.report.model.core.DesignSession
 */

public class SessionHandleTest extends BaseTestCase {

	private final String fileName = "SessionHandleTest.xml"; //$NON-NLS-1$
	private final String outFileName = "SessionHandleTest_out.xml"; //$NON-NLS-1$
	private final String wrongTagFileName = "SessionHandleTest_1.xml"; //$NON-NLS-1$
	private final String semanticErrorFileName = "SessionHandleTest_2.xml"; //$NON-NLS-1$
	private final String missingStartingTagFileName = "SessionHandleTest_3.xml"; //$NON-NLS-1$
	private final String unmatchedTagFileName = "SessionHandleTest_4.xml"; //$NON-NLS-1$
	private final String missingEndingTagFileName = "SessionHandleTest_5.xml"; //$NON-NLS-1$
	private final String invalidAttrFileName = "SessionHandleTest_6.xml"; //$NON-NLS-1$
	private final String undefinedPropertyFileName = "SessionHandleTest_7.xml"; //$NON-NLS-1$
	private final String notExistedFileName = "NotExistedFile.xml"; //$NON-NLS-1$
	private final String UTF8BOMFileName = "SessionHandleTest_UTF8BOM.xml";//$NON-NLS-1$

	private final String simpleDesignFile = "SessionHandleTest_8.xml"; //$NON-NLS-1$
	private final String simpleLibraryFile = "SessionHandleTest_9.xml"; //$NON-NLS-1$

	private final String streamFileName = "SessionHandleTest_Stream.xml";//$NON-NLS-1$

	private SessionHandle session = null;

	private DesignEngine designEngine = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();

		designEngine = new DesignEngine(new DesignConfig());
		session = designEngine.newSessionHandle((ULocale) null);
		assertEquals(ULocale.getDefault(), session.getULocale());

		IMetaDataDictionary dic = designEngine.getMetaData();

		assertFalse(dic.getElements().isEmpty());
	}

	/**
	 * Tests the method of getLocale().
	 * 
	 */

	public void testULocale() {
		session = designEngine.newSessionHandle(ULocale.ENGLISH);
		assertEquals(ULocale.ENGLISH, session.getULocale());

		session = designEngine.newSessionHandle(ULocale.KOREA);
		assertEquals(ULocale.KOREA, session.getULocale());
	}

	/**
	 * Create and open one design file, and close them.
	 * 
	 * @throws Exception if any exception
	 */

	public void testCreateOpenAndClose() throws Exception {
		String outputPath = getTempFolder() + OUTPUT_FOLDER;
		File outputFolder = new File(outputPath);
		if (!outputFolder.exists() && !outputFolder.mkdirs()) {
			throw new IOException("Can not create the output folder"); //$NON-NLS-1$
		}

		// Open design file A

		designHandle = session.openDesign(getResource(INPUT_FOLDER + fileName).toString());
		assertTrue(designHandle.needsSave());
		assertTrue(designHandle.getModule().isValid());
		assertEquals(1, getDesignCount());

		// Set one property in design file A

		designHandle.setStringProperty(ReportDesign.AUTHOR_PROP, "abc"); //$NON-NLS-1$
		assertTrue(designHandle.needsSave());
		assertTrue(designHandle.getModule().isValid());

		// save design file A

		designHandle.saveAs(outputPath + outFileName);
		assertFalse(designHandle.needsSave());
		assertTrue(designHandle.getModule().isValid());
		assertEquals(1, getDesignCount());

		// Add new element in design file A

		DesignElementHandle freeForm = designHandle.getElementFactory().newFreeForm("My Form"); //$NON-NLS-1$
		designHandle.getBody().add(freeForm);
		assertTrue(designHandle.needsSave());
		assertTrue(designHandle.getModule().isValid());

		// Close design file A

		designHandle.close();
		assertFalse(designHandle.needsSave());
		assertFalse(designHandle.getModule().isValid());
		assertEquals(0, getDesignCount());

		// Create new design file B

		ReportDesignHandle newDesign = session.createDesign();
		assertFalse(newDesign.needsSave());
		assertTrue(newDesign.getModule().isValid());
		assertEquals(1, getDesignCount());

		// Save and close the new design file B
		newDesign.saveAs(outputPath + "newdesign.xml"); //$NON-NLS-1$
		newDesign.close();
		assertFalse(newDesign.needsSave());
		assertFalse(newDesign.getModule().isValid());
		assertEquals(0, getDesignCount());

		// Open the new design file B
		session = null;
		designHandle = null;
		session = designEngine.newSessionHandle((ULocale) null);
		designHandle = session.openDesign(outputPath + "newdesign.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);
		assertFalse(designHandle.needsSave());
		assertTrue(designHandle.getModule().isValid());
		assertEquals(1, getDesignCount());

		ReportDesignHandle anotherDesignHandle = session.openDesign(getResource(INPUT_FOLDER + fileName).toString());
		assertNotNull(anotherDesignHandle);
		assertTrue(anotherDesignHandle.needsSave());
		assertTrue(anotherDesignHandle.getModule().isValid());
		assertEquals(2, getDesignCount());

		ModuleOption options = new ModuleOption();
		options.setProperty(ModuleOption.BLANK_CREATION_KEY, Boolean.TRUE);
		String newFileName = getResource(INPUT_FOLDER + fileName).toString();

		ReportDesignHandle simpleDesignHandle = session.createDesign(newFileName, options);
		newFileName = newFileName.replaceAll(fileName, "SessionHandleTest_golden.xml"); //$NON-NLS-1$
		save(simpleDesignHandle);
		assertTrue(compareFile("SessionHandleTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Open the design file which does not exist.
	 */

	public void testOpenNonExistedFile() {
		try {
			designHandle = session.openDesign(getResource(INPUT_FOLDER).toString() + notExistedFileName);
			fail();
		} catch (DesignFileException e) {
			assertNull(designHandle);
			assertEquals(DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR, e.getErrorCode());
			assertEquals(1, e.getErrorList().size());
			ErrorDetail error = (ErrorDetail) e.getErrorList().get(0);
			assertEquals(DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND, error.getErrorCode());
		}
	}

	/**
	 * Open the design file with wrong tag.
	 */

	public void testOpenWithWrongTag() {
		designHandle = null;
		try {
			designHandle = session.openDesign(getResource(INPUT_FOLDER + wrongTagFileName).toString());
			fail();
		} catch (DesignFileException e) {
			System.out.println(e);

			assertNull(designHandle);
			assertEquals(DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR, e.getErrorCode());
			assertEquals(1, e.getErrorList().size());

		}
	}

	/**
	 * Open the design file with undefined tag.
	 * 
	 * @throws DesignFileException if the design file is invalid
	 */

	public void testOpenWithUndefinedProperty() throws DesignFileException {
		designHandle = null;

		designHandle = session.openDesign(getResource(INPUT_FOLDER + undefinedPropertyFileName).toString());

		List errors = designHandle.getErrorList();
		assertEquals(3, errors.size());

		ErrorDetail error = (ErrorDetail) errors.get(0);

		assertEquals(DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY, error.getErrorCode());

		error = (ErrorDetail) errors.get(1);
		assertEquals(DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY, error.getErrorCode());

		error = (ErrorDetail) errors.get(2);
		assertEquals(DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY, error.getErrorCode());

	}

	/**
	 * Open the design file with syntax error.
	 */

	public void testOpenWithMissingStartingTagError() {
		try {
			designHandle = session.openDesign(getResource(INPUT_FOLDER + missingStartingTagFileName).toString());
			fail();
		} catch (DesignFileException e) {
			System.out.println(e);

			assertNull(designHandle);
			assertEquals(DesignFileException.DESIGN_EXCEPTION_INVALID_XML, e.getErrorCode());
			assertEquals(2, e.getErrorList().size());
		}
	}

	/**
	 * Open the design file with syntax error.
	 */

	public void testOpenWithUnmatchedTagError() {
		designHandle = null;
		try {
			designHandle = session.openDesign(getResource(INPUT_FOLDER + unmatchedTagFileName).toString());
			fail();
		} catch (DesignFileException e) {
			System.out.println(e);

			assertNull(designHandle);
			assertEquals(DesignFileException.DESIGN_EXCEPTION_INVALID_XML, e.getErrorCode());
			assertEquals(3, e.getErrorList().size());

			ErrorDetail error = (ErrorDetail) e.getErrorList().get(0);
			assertEquals("wrong-tag", error.getTagName()); //$NON-NLS-1$
			error = (ErrorDetail) e.getErrorList().get(1);
			error = (ErrorDetail) e.getErrorList().get(2);
		}
	}

	/**
	 * Open the design file with syntax error.
	 */

	public void testOpenWithInvalidAttrError() {
		designHandle = null;
		try {
			designHandle = session.openDesign(getResource(INPUT_FOLDER + invalidAttrFileName).toString());
		} catch (DesignFileException e) {
			fail();
		}

		// No error is found in this case.

		assertTrue(designHandle.getModule().isValid());
		assertEquals(0, designHandle.getModule().getErrorList().size());
	}

	/**
	 * Open the design file with syntax error.
	 */

	public void testOpenWithMissingEndingTagError() {
		designHandle = null;
		try {
			designHandle = session.openDesign(getResource(INPUT_FOLDER + missingEndingTagFileName).toString());
			fail();
		} catch (DesignFileException e) {
			System.out.println(e);

			assertNull(designHandle);
			assertEquals(DesignFileException.DESIGN_EXCEPTION_INVALID_XML, e.getErrorCode());
			assertEquals(3, e.getErrorList().size());

			ErrorDetail error = (ErrorDetail) e.getErrorList().get(0);
			assertEquals("body", error.getTagName()); //$NON-NLS-1$

			error = (ErrorDetail) e.getErrorList().get(1);
			error = (ErrorDetail) e.getErrorList().get(2);
		}
	}

	/**
	 * Open the design file with semantic error.
	 * 
	 * @throws Exception if any exception
	 */

	public void testOpenWithSemanticError() throws Exception {
		designHandle = null;

		designHandle = session.openDesign(getResource(INPUT_FOLDER + semanticErrorFileName).toString());
		assertEquals(3, designHandle.getModule().getErrorList().size());
		ErrorDetail error = (ErrorDetail) designHandle.getModule().getErrorList().get(0);
		assertEquals(DesignFileException.DESIGN_EXCEPTION_SEMANTIC_ERROR, error.getType());
		assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_MASTER_PAGE, error.getErrorCode());
		assertEquals("ReportDesign", error.getElement().getElementName()); //$NON-NLS-1$
		assertEquals(null, error.getElement().getName());

		error = (ErrorDetail) designHandle.getErrorList().get(1);
		assertEquals(DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY, error.getErrorCode());

		error = (ErrorDetail) designHandle.getErrorList().get(2);
		assertEquals(DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY, error.getErrorCode());

		assertTrue(designHandle.getModule().isValid());
		assertEquals(1, getDesignCount());

		designHandle.getModule().close();
		assertFalse(designHandle.getModule().isValid());
		assertEquals(0, getDesignCount());

		designHandle = null;
	}

	/**
	 * Tests to read a design file with byte order mark (BOM) in a windows
	 * compatible UTF file.
	 * 
	 * @throws Exception if errors occur during opening the design file.
	 */

	public void testOpenWithBOM() throws Exception {
		designHandle = null;

		designHandle = session.openDesign(getResource(INPUT_FOLDER + UTF8BOMFileName).toString());
		assertNotNull(designHandle);

		designHandle = null;

		try {
			designHandle = session
					.openDesign(getResource(INPUT_FOLDER + "SessionHandleTest_INVALIDBOM.xml").toString()); //$NON-NLS-1$
		} catch (DesignFileException e) {
			List errors = e.getErrorList();
			assertEquals(1, errors.size());
			assertEquals(DesignParserException.DESIGN_EXCEPTION_UNSUPPORTED_ENCODING,
					((ErrorDetail) errors.get(0)).getErrorCode());
		}

		assertNull(designHandle);
	}

	/**
	 * Tests the session properties.
	 * 
	 * @throws Exception
	 */

	public void testSessionProperties() throws Exception {
		session.setApplicationUnits(DesignChoiceConstants.UNITS_MM);
		assertEquals(DesignChoiceConstants.UNITS_MM, session.getApplicationUnits());

		session.setColorFormat(ColorUtil.CSS_RELATIVE_FORMAT);
		assertEquals(ColorUtil.CSS_RELATIVE_FORMAT, session.getColorFormat());

		IResourceLocator testLocator = new DefaultResourceLocator() {

			/**
			 * 
			 */
			public URL findResource(ModuleHandle moduleHandle, String filename, int type) {
				return null;
			}

			public URL findResource(ModuleHandle moduleHandle, String fileName, int type, Map appContext) {
				return null;
			}
		};
		session.setResourceLocator(testLocator);
		assertEquals(testLocator, session.getResourceLocator());
	}

	/**
	 * Tests default value of session.
	 * 
	 * @throws Exception if any exception.
	 */

	public void testDefaultValue() throws Exception {
		session.setDefaultValue(Style.BORDER_BOTTOM_COLOR_PROP, "#00ffff"); //$NON-NLS-1$
		session.setDefaultValue(Style.BORDER_TOP_COLOR_PROP, "#ff0000"); //$NON-NLS-1$
		session.setDefaultValue(Style.BORDER_LEFT_COLOR_PROP, "#0000ff"); //$NON-NLS-1$

		assertEquals("65535", session.getDefaultValue(Style.BORDER_BOTTOM_COLOR_PROP).toString()); //$NON-NLS-1$
		assertEquals("16711680", session.getDefaultValue(Style.BORDER_TOP_COLOR_PROP).toString()); //$NON-NLS-1$
		assertEquals("255", session.getDefaultValue(Style.BORDER_LEFT_COLOR_PROP).toString()); //$NON-NLS-1$
	}

	/**
	 * Tests to open the design as streams with the system id. Test cases are:
	 * 
	 * <ul>
	 * <li></li>
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testOpenWithStream() throws Exception {
		// open the file as a local file, the system id is set.

		openDesign(fileName);

		URL url = getResource(INPUT_FOLDER + fileName);
		URL parentUrl = getResource(INPUT_FOLDER);

		assertEquals(parentUrl.toString(), designHandle.getSystemId().toExternalForm());

		// open the design as the file input stream. work as a file url

		url = getResource(INPUT_FOLDER + streamFileName);
		InputStream is = url.openStream();

		designHandle = session.openDesign(parentUrl, is);
		is.close();
		design = (ReportDesign) designHandle.getModule();
		assertNull(design.getFileName());
		testSystemIdAndFileName(designHandle, url, parentUrl);

		// open the design as a file path.

		openDesign(streamFileName);

		url = getResource(INPUT_FOLDER + streamFileName);
		is = url.openStream();

		testSystemIdAndFileName(designHandle, url, parentUrl);

		// open the design as a stream with its file path

		// is = new FileInputStream( file );
		openDesign(url.toExternalForm(), is);
		assertEquals(url.toExternalForm(), design.getFileName());
		testSystemIdAndFileName(designHandle, url, parentUrl);

		is.close();
	}

	/**
	 * Tests values of system id and filename after open a design file.
	 * 
	 * @param designHandle the report design handle
	 * @param url
	 * @param file         the <code>File</code> instance of the design file
	 * @throws Exception
	 */

	private void testSystemIdAndFileName(ReportDesignHandle designHandle, URL url, URL parentUrl) throws Exception {
		assertEquals(parentUrl.toString(), designHandle.getSystemId().toString());

		List libraries = designHandle.getLibraries();
		assertEquals(2, libraries.size());

		LibraryHandle libHandle = (LibraryHandle) libraries.get(0);
		assertTrue(libHandle.isValid());
		assertEquals(parentUrl.toString(), libHandle.getSystemId().toString());

		libHandle = (LibraryHandle) libraries.get(1);
		assertTrue(libHandle.isValid());

		// check the file getCanonicalPath() to make sure they are the same
		// file.

		URL tmpUrl = getResource("/org/eclipse/birt/report/model/library/" //$NON-NLS-1$
				+ INPUT_FOLDER);

		assertTrue(tmpUrl.sameFile(libHandle.getSystemId()));
	}

	/**
	 * Test open a generic module file
	 * 
	 * @throws DesignFileException
	 */

	public void testOpenModule() throws DesignFileException {
		openModule(simpleDesignFile);
		assertTrue(moduleHandle instanceof ReportDesignHandle);

		openModule(simpleLibraryFile);
		assertTrue(moduleHandle instanceof LibraryHandle);
	}

	/**
	 * Test open a report design, library and module with the important parameter
	 * value null.
	 * 
	 * @throws DesignFileException
	 * @throws IOException
	 */

	public void testOpenWithNull() throws DesignFileException, IOException {
		try {
			session.openDesign(null);
			fail();
		} catch (IllegalArgumentException e) {

		}

		try {
			session.openDesign((URL) null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// test open module.

		try {
			session.openModule(null);
			fail();
		} catch (IllegalArgumentException e) {

		}

		try {
			session.openLibrary((URL) null, null);
			fail();
		} catch (IllegalArgumentException e) {

		}

	}

	/**
	 * Returns the number of designs in this session.
	 * 
	 * @return the total design number.
	 */

	private int getDesignCount() {
		int count = 0;
		Iterator iter = ApiTestUtil.getDesignSession(session).getDesignIterator();
		while (iter.hasNext()) {
			iter.next();
			count++;
		}

		return count;
	}

	class MockupLibraryExplorer implements IResourceChangeListener {

		private String status = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.core.IDisposeListener#elementDisposed
		 * (org.eclipse.birt.report.model.api.ModuleHandle,
		 * org.eclipse.birt.report.model.api.core.DisposeEvent)
		 */
		public void resourceChanged(ModuleHandle targetElement, ResourceChangeEvent ev) {
			status = "refresh"; //$NON-NLS-1$
		}

		/**
		 * 
		 * @return
		 */
		public String getStatus() {
			return status;
		}

	}

	class MockupLayoutListener implements IResourceChangeListener {

		private String status = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.core.IDisposeListener#elementDisposed
		 * (org.eclipse.birt.report.model.api.ModuleHandle,
		 * org.eclipse.birt.report.model.api.core.DisposeEvent)
		 */
		public void resourceChanged(ModuleHandle targetElement, ResourceChangeEvent ev) {
			status = "reload"; //$NON-NLS-1$
			try {
				targetElement.reloadLibrary(ev.getChangedResourcePath());
			} catch (SemanticException e) {
				fail();
			} catch (DesignFileException e) {
				fail();
			}
		}

		/**
		 * 
		 * @return status
		 */
		public String getStatus() {
			return status;
		}

	}

	/**
	 * Tests fireResourceChange method on session Handle.
	 * 
	 * @throws DesignFileException
	 */

	public void testFireResourceChange() throws DesignFileException {
		libraryHandle = session.openLibrary(getResource(INPUT_FOLDER + "Library_1.xml").toString()); //$NON-NLS-1$
		MockupLibraryExplorer libListener = new MockupLibraryExplorer();
		libraryHandle.addResourceChangeListener(libListener);

		designHandle = session.openDesign(getResource(INPUT_FOLDER + "SessionHandleTest_10.xml").toString()); //$NON-NLS-1$
		designHandle.addResourceChangeListener(new MockupLayoutListener());
		List libs = ((Module) designHandle.getElement()).getLibraries();
		assertEquals(2, libs.size());
		Library instance1 = (Library) libs.get(0);
		Library instance2 = (Library) libs.get(1);

		session.fireResourceChange(
				new LibraryChangeEvent(getResource(INPUT_FOLDER + "Library_1.xml").toExternalForm())); //$NON-NLS-1$
		assertEquals("refresh", libListener.getStatus()); //$NON-NLS-1$
		assertNotSame(instance1, ((Module) designHandle.getElement()).getLibraries().get(0));
		assertSame(instance2, ((Module) designHandle.getElement()).getLibraries().get(1));

		session.fireResourceChange(new LibraryChangeEvent(getResource(INPUT_FOLDER + "Grandson.xml").toExternalForm())); //$NON-NLS-1$
		assertNotSame(instance2, ((Module) designHandle.getElement()).getLibraries().get(1));
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testParserSemanticCheckControl() throws Exception {
		ModuleOption options = new ModuleOption();
		options.setSemanticCheck(false);
		options.setMarkLineNumber(true);

		designHandle = session.openDesign(getResource(INPUT_FOLDER + "SessionHandleTest_11.xml").toString(), options); //$NON-NLS-1$
		assertTrue(options.markLineNumber());
		assertEquals(0, designHandle.getModule().getAllErrors().size());
		assertEquals(0, designHandle.getLibrary("lib").getModule().getAllErrors().size()); //$NON-NLS-1$
		designHandle.close();

		options = null;
		designHandle = session.openDesign(getResource(INPUT_FOLDER + "SessionHandleTest_11.xml").toString(), options); //$NON-NLS-1$
		assertEquals(1, designHandle.getModule().getAllErrors().size());
		assertEquals(1, designHandle.getLibrary("lib").getModule().getAllErrors().size()); //$NON-NLS-1$

	}

	/**
	 * Test getDefaultTOCStyle method.
	 * 
	 * @throws Exception
	 */

	public void testGetDefaultTOCStyle() throws Exception {
		StyleHandle styleHandle = session.getDefaultTOCStyle(TOCHandle.defaultTOCPrefixName + "0");//$NON-NLS-1$

		assertNotNull(styleHandle);

		assertEquals("12pt", styleHandle.getFontSize().getStringValue()); //$NON-NLS-1$

		try {
			styleHandle.setCanShrink(false);
			fail();

		} catch (IllegalOperationException e) {
			assertEquals(ReadOnlyActivityStack.MESSAGE, e.getMessage());
		}

	}

	/**
	 * Tests the function of simple parser. Only read simple properties set in
	 * report root rather than the whole design tree. Neither do the semantic check.
	 * 
	 * @throws Exception
	 */
	public void testOpenForSimpleParser() throws Exception {
		ModuleOption options = new ModuleOption();
		options.setProperty(ModuleOption.READ_ONLY_MODULE_PROPERTIES, Boolean.TRUE);
		designHandle = session.openDesign(getResource(INPUT_FOLDER + "SessionHandleTest_10.xml").toString(), options); //$NON-NLS-1$

		// the module is read-only
		assertTrue(designHandle.getModule().isReadOnly());

		// simple property is read properly
		assertEquals("Eclipse BIRT Designer Version 2.1.0.qualifier Build <@BUILD@>", //$NON-NLS-1$
				designHandle.getCreatedBy());

		// other element is not parsed
		assertEquals(0, designHandle.getMasterPages().getCount());

		// complicated properties in report design is not read either
		assertEquals(0, designHandle.getLibraries().size());

		// not semantic check: for there is no master page, if check there will
		// be one error
		assertEquals(0, designHandle.getModule().getErrorList().size());

	}
}