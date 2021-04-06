/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.utility;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.utility.ParameterAccessor;
import org.eclipse.birt.report.viewer.util.BaseTestCase;

/**
 * TestCases for ParameterAccessor class. For test, mock some objects over http:
 * ServletContext, HttpServletRequest, HttpSession.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * 
 * <tr>
 * <td>{@link #testInitParameter()}</td>
 * <td>Initialize parameters from ServletContext</td>
 * <td>Parameter value should be same as the one that put into
 * ServletContext</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetParameter()}</td>
 * <td>Get parameter from HttpServletRequest</td>
 * <td>Parameter value is correct from HttpServletRequest. The encoding is
 * UTF-8.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGenerateFileName()}</td>
 * <td>Generate output pdf file name</td>
 * <td>The file name can be generated from report name or document name(Only
 * support ASCII).Else,use the default file name "BIRTReport.pdf".</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetConfigFileName()}</td>
 * <td>Generate cached config file name</td>
 * <td>The config file name should be same as the report name.Use the different
 * suffix.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetOutputFormat()}</td>
 * <td>Get output format of report</td>
 * <td>Output format should be from http request. If it is HTM, regard it as
 * HTML also.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetParameterFormat()}</td>
 * <td>Get parameter format of report</td>
 * <td>Parameter format should be from http request.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetLocaleFromString()}</td>
 * <td>Generate Locale Object from String</td>
 * <td>Should generate correct Locale object from String.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetLocale()}</td>
 * <td>Get Locale Object from http request</td>
 * <td>Locale should be generated from URL parameter.If not,returns request
 * locale.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetParameterValues()}</td>
 * <td>Get parameter values from http request</td>
 * <td>Parameter values should be correct from HttpServletRequest. The encoding
 * is UTF-8.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetReportParameter()}</td>
 * <td>Get report parameter value from http request</td>
 * <td>The parameter value should be according to the defined logic. The
 * encoding is UTF-8.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetBookmark()}</td>
 * <td>Get target bookmark from http request</td>
 * <td>If set page information, ignore bookmark and return null</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetPage()}</td>
 * <td>Get target report page from http request</td>
 * <td>Return correct page number,if null or less then 1, return 1.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testIsDisplayText()}</td>
 * <td>Return parameter name</td>
 * <td>If it is display text of parameter, return parameter name.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testIsOverwrite()}</td>
 * <td>Return isOverwrite setting</td>
 * <td>This setting can be from URL.If not, use the default setting from context
 * config.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testIsOverwrite()}</td>
 * <td>Return isOverwrite setting</td>
 * <td>This setting can be from URL.If not, use the default setting from context
 * config.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testIsReportParameterExist()}</td>
 * <td>Return if parameter is in URL</td>
 * <td>Return true or false</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testIsIidReportlet()}</td>
 * <td>Return if reportlet is from instanceid</td>
 * <td>Return true or false</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testIsBookmarkReportlet()}</td>
 * <td>Return if reportlet is from bookmark</td>
 * <td>Return true or false</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetReportletId()}</td>
 * <td>Return reportlet id</td>
 * <td>Reportlet id should be from instanceid or bookmark.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetReportletId()}</td>
 * <td>Return reportlet id</td>
 * <td>Reportlet id should be from instanceid or bookmark.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetMaxRows()}</td>
 * <td>Return maxRows setting</td>
 * <td>MaxRows setting can be set in URL.If not, use the default setting from
 * context.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetResourceFolder()}</td>
 * <td>Return reourceFolder setting</td>
 * <td>ResourceFolder setting can be set in URL.If not, use the default setting
 * from context.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testIsValidFilePath()}</td>
 * <td>Validate the current file path.</td>
 * <td>Return true of false</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetReport()}</td>
 * <td>Return the report file path.</td>
 * <td>Return absolute path.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetReportDocument()}</td>
 * <td>Return the report document file path.</td>
 * <td>Return absolute path.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetSelectedColumns()}</td>
 * <td>Get selected column list.</td>
 * <td>Return correct list.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testHtmlEncode()}</td>
 * <td>Return html encoded string.</td>
 * <td>Return correct string.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testPushAppContext()}</td>
 * <td>Push user-defined application context object into engine context .</td>
 * <td>Return engine context map.</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class ParameterAccessorTest extends BaseTestCase {

	private static final String DEFAULT_TEST_REPORT = "test.rptdesign"; //$NON-NLS-1$

	public void setUp() throws Exception {
		ParameterAccessor.reset();
		super.setUp();
		verifyInitParameter();
	}

	/**
	 * TestCase for initParameter method.
	 * <P>
	 * Initialize parameters should be from ServletContext.
	 * 
	 */
	public void verifyInitParameter() {
		String root_folder = root.getAbsolutePath();
		assertEquals(root_folder + File.separator + IBirtConstants.DEFAULT_DOCUMENT_FOLDER,
				ParameterAccessor.getResourceFolder(request) + File.separator + IBirtConstants.DEFAULT_DOCUMENT_FOLDER);
		assertEquals(root_folder, ParameterAccessor.getResourceFolder(request));

		assertEquals(DEFAULT_LOCALE, ParameterAccessor.webAppLocale.toString());

		assertTrue(ParameterAccessor.isOverWrite);
		assertTrue(!ParameterAccessor.isWorkingFolderAccessOnly());
	}

	/**
	 * TestCase for generateFileName method
	 * <p>
	 * When output format is pdf, generate pdf file name.
	 * <p>
	 * <ol>
	 * <li>If attribute bean is null, default file name is BIRTReport.pdf</li>
	 * <li>If report name is not null, file name is from report name.(Only support
	 * ASCII)</li>
	 * <li>If report name is null, file name is from report document name.(Only
	 * support ASCII)</li>
	 * <li>If report name or document name is not ASCII, use default file name</li>
	 * </ol>
	 */
	public void testGenerateFileName() {
		String testDocumentName = "D:\\test\\documents\\test.rptdocument";
		// UNIX?
		if (File.separatorChar == '/') {
			testDocumentName = "/test/documents/test.rptdocument";
		}

		request.addParameter(ParameterAccessor.PARAM_FORMAT, "pdf"); //$NON-NLS-1$

		// Default file name
		assertEquals("BIRTReport.pdf", //$NON-NLS-1$
				generateFileName(request, "pdf")); //$NON-NLS-1$

		// File name from report name
		request.addParameter(ParameterAccessor.PARAM_REPORT, DEFAULT_TEST_REPORT);
		ViewerAttributeBean bean = new ViewerAttributeBean(request);
		request.setAttribute(IBirtConstants.ATTRIBUTE_BEAN, bean);
		assertEquals("test.pdf", //$NON-NLS-1$
				generateFileName(request, "pdf")); //$NON-NLS-1$
		request.removeParameter(ParameterAccessor.PARAM_REPORT);
		request.removeParameter(IBirtConstants.ATTRIBUTE_BEAN);

		// File name from report document name
		request.addParameter(ParameterAccessor.PARAM_REPORT_DOCUMENT, testDocumentName); // $NON-NLS-1$
		bean = new ViewerAttributeBean(request);
		request.setAttribute(IBirtConstants.ATTRIBUTE_BEAN, bean);
		assertEquals("test.pdf", //$NON-NLS-1$
				generateFileName(request, "pdf")); //$NON-NLS-1$
		request.removeParameter(ParameterAccessor.PARAM_REPORT_DOCUMENT);
		request.removeParameter(IBirtConstants.ATTRIBUTE_BEAN);

		// Non ASCII, use default file name
		request.addParameter(ParameterAccessor.PARAM_REPORT, "\u4e2d\u6587report.rptdesign"); //$NON-NLS-1$
		request.setCharacterEncoding(ENCODING_UTF8);
		bean = new ViewerAttributeBean(request);
		request.setAttribute(IBirtConstants.ATTRIBUTE_BEAN, bean);
		assertEquals("BIRTReport.pdf", //$NON-NLS-1$
				generateFileName(request, "pdf")); //$NON-NLS-1$
		request.removeParameter(ParameterAccessor.PARAM_REPORT);
		request.removeParameter(IBirtConstants.ATTRIBUTE_BEAN);
		request.setCharacterEncoding(null);
	}

	private String generateFileName(HttpServletRequest request, String format) {
		return ParameterAccessor.getExportFilename(new BirtContext(request, response), format, null);
	}

	/**
	 * TestCase for getConfigFileName method
	 * <p>
	 * The cached config file should be side of the current report design file.
	 * Current,support two types design file: rptdesign, rpttemplate
	 * 
	 */
	public void testGetConfigFileName() {
		// preview a report
		String reportFile = System.getProperty("java.io.tmpdir") + "/test.rptdesign"; //$NON-NLS-1$ //$NON-NLS-2$
		String configFile = System.getProperty("java.io.tmpdir") + "/test.rptconfig"; //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(configFile, ParameterAccessor.getConfigFileName(reportFile));

		// preview a template report
		String templateFile = System.getProperty("java.io.tmpdir") + "/test.rpttemplate"; //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(configFile, ParameterAccessor.getConfigFileName(templateFile));
	}

	/**
	 * TestCase for getFormat( HttpServletRequest ) method
	 * <p>
	 * Get output format from URL.
	 * <p>
	 * <ol>
	 * <li>If format is null from URL, use default output format as HTML.</li>
	 * <li>If format is HTM from URL, return the format as HTML also.</li>
	 * <li>Else, return the received format.</li>
	 * </ol>
	 * 
	 */
	public void testGetOutputFormat() {
		// format is null
		assertEquals(ParameterAccessor.PARAM_FORMAT_HTML, ParameterAccessor.getFormat(request));

		// format is HTM
		request.addParameter(ParameterAccessor.PARAM_FORMAT, ParameterAccessor.PARAM_FORMAT_HTM);
		assertEquals(ParameterAccessor.PARAM_FORMAT_HTML, ParameterAccessor.getFormat(request));
		request.removeParameter(ParameterAccessor.PARAM_FORMAT);

		// format is the other
		request.addParameter(ParameterAccessor.PARAM_FORMAT, "SpecialFormat"); //$NON-NLS-1$
		assertEquals("SpecialFormat", ParameterAccessor.getFormat(request)); //$NON-NLS-1$
		request.removeParameter(ParameterAccessor.PARAM_FORMAT);
	}

	/**
	 * TestCase for getFormat( HttpServletRequest, String ) method
	 * <p>
	 * Get current parameter format from URL.
	 * 
	 */
	public void testGetParameterFormat() {
		String paramName = "SampleParam"; //$NON-NLS-1$
		String paramFormat = "yyyy-MM-dd"; //$NON-NLS-1$

		// no parameter format
		request.addParameter(paramName, "parameter value"); //$NON-NLS-1$
		assertNull(ParameterAccessor.getFormat(request, paramName));

		// set parameter format
		request.addParameter(paramName + "_format", paramFormat); //$NON-NLS-1$
		assertEquals(paramFormat, ParameterAccessor.getFormat(request, paramName));

		request.removeParameter(paramName);
		request.removeParameter(paramName + "_format"); //$NON-NLS-1$
	}

	/**
	 * TestCase for getLocaleFromString method
	 * <p>
	 * Returns correct Locale
	 * 
	 */
	public void testGetLocaleFromString() {
		assertNull(ParameterAccessor.getLocaleFromString(null));
		assertEquals(Locale.US, ParameterAccessor.getLocaleFromString("en_US")); //$NON-NLS-1$
		assertEquals(new Locale("test"), ParameterAccessor //$NON-NLS-1$
				.getLocaleFromString("test")); //$NON-NLS-1$
	}

	/**
	 * TestCase for getLocale method
	 * <p>
	 * Returns correct Locale
	 * <p>
	 * <ol>
	 * <li>If define a certain locale in URL, return this locale.</li>
	 * <li>Else, get the locale from request.</li>
	 * <li>If locale is null yet, use the default locale from ServletContext.</li>
	 * </ol>
	 */
	public void testGetLocale() {
		// Locale in URL
		request.addParameter(ParameterAccessor.PARAM_LOCALE, "zh_CN"); //$NON-NLS-1$
		assertEquals(Locale.PRC, ParameterAccessor.getLocale(request));
		request.removeParameter(ParameterAccessor.PARAM_LOCALE);

		// Get Locale from Request
		request.setLocale(Locale.UK);
		assertEquals(Locale.UK, ParameterAccessor.getLocale(request));
		request.setLocale(null);

		// Get Locale from ServletContext
		assertEquals(new Locale(DEFAULT_LOCALE).toString().toLowerCase(),
				ParameterAccessor.getLocale(request).toString().toLowerCase());
	}

	/**
	 * TestCase for getParameterValues method
	 * <p>
	 * Get parameter values collection by name from http request
	 * 
	 */
	public void testGetParameterValues() {
		String paramName = "param"; //$NON-NLS-1$
		String[] values = { "value1", "value2", "value3" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		request.addParameterValues(paramName, values);

		// Wrong parameter name
		Collection params = ParameterAccessor.getParameterValues(request, "WrongParamName"); //$NON-NLS-1$
		assertNull(params);

		// Correct parameter name
		params = ParameterAccessor.getParameterValues(request, paramName);
		assertNotNull(params);

		for (int i = 0; i < values.length; i++) {
			assertTrue(params.contains(values[i]));
		}
		request.removeParameter(paramName);
	}

	/**
	 * TestCase for getReportParameter method
	 * <p>
	 * Returns correct parameter value
	 * <p>
	 * <ol>
	 * <li>If parameter is in request,return parameter value.If value is null,
	 * return blank string.</li>
	 * <li>If parameter isn't in request,return default value as parameter
	 * value.</li>
	 * <li>If parameter is a null parameter, return null as parameter value.</li>
	 * </ol>
	 */
	public void testGetReportParameter() {
		String paramName = "param"; //$NON-NLS-1$
		String paramValue = "value";//$NON-NLS-1$
		String WrongParamName = "WrongParamName"; //$NON-NLS-1$
		String defaultValue = "defaultValue"; //$NON-NLS-1$

		// parameter in request
		request.addParameter(paramName, paramValue);
		assertEquals(paramValue, ParameterAccessor.getReportParameter(request, paramName, defaultValue));

		// parameter not in request
		assertEquals(defaultValue, ParameterAccessor.getReportParameter(request, WrongParamName, defaultValue));

		// parameter is a null parameter
		request.addParameterValues(ParameterAccessor.PARAM_ISNULL, new String[] { paramName, WrongParamName });
		assertNull(ParameterAccessor.getReportParameter(request, paramName, defaultValue));
		assertNull(ParameterAccessor.getReportParameter(request, WrongParamName, defaultValue));

		request.removeParameter(paramName);
		request.removeParameter(ParameterAccessor.PARAM_ISNULL);

	}

	/**
	 * TestCase for getBookmark method
	 * <p>
	 * Returns correct bookmard from http request.If set page information, returns
	 * null.
	 * 
	 */
	public void testGetBookmark() {
		String bookmark = "bookmark"; //$NON-NLS-1$

		// Don't set bookmark
		assertNull(ParameterAccessor.getBookmark(request));

		// Set bookmark
		request.addParameter(ParameterAccessor.PARAM_BOOKMARK, bookmark);
		assertEquals(bookmark, ParameterAccessor.getBookmark(request));

		// Set page
		request.addParameter(ParameterAccessor.PARAM_PAGE, "2"); //$NON-NLS-1$
		assertNull(ParameterAccessor.getBookmark(request));

		request.removeParameter(ParameterAccessor.PARAM_BOOKMARK);
		request.removeParameter(ParameterAccessor.PARAM_PAGE);
	}

	/**
	 * TestCase for getPage method
	 * <p>
	 * Returns correct report page.If page is null or less then 1, set as 1.
	 * 
	 */
	public void testGetPage() {
		request.setServletPath("/frameset");
		// Don't set page
		assertEquals(1, ParameterAccessor.getPage(request));

		// set page
		request.addParameter(ParameterAccessor.PARAM_PAGE, "3"); //$NON-NLS-1$
		assertEquals(3, ParameterAccessor.getPage(request));

		// set wrong page number
		request.addParameter(ParameterAccessor.PARAM_PAGE, "aaa"); //$NON-NLS-1$
		assertEquals(1, ParameterAccessor.getPage(request));

		request.removeParameter(ParameterAccessor.PARAM_PAGE);
	}

	/**
	 * TestCase for isDisplayText method
	 * <p>
	 * Returns parameter name.
	 * 
	 */
	public void testIsDisplayText() {
		String paramName = "param"; //$NON-NLS-1$

		assertNull(ParameterAccessor.isDisplayText(paramName));
		assertEquals(paramName, ParameterAccessor.isDisplayText(ParameterAccessor.PREFIX_DISPLAY_TEXT + paramName));
	}

	/**
	 * TestCase for isOverwrite method
	 * <p>
	 * Returns correct isOverwrite setting. If don't set it in http request,use
	 * default setting from context
	 * 
	 */
	public void testIsOverwrite() {
		// Don't set in http request
		assertTrue(ParameterAccessor.isOverwrite(request));

		// set in http request
		request.addParameter(ParameterAccessor.PARAM_OVERWRITE, "false"); //$NON-NLS-1$
		assertTrue(!ParameterAccessor.isOverwrite(request));

		request.removeParameter(ParameterAccessor.PARAM_OVERWRITE);
	}

	/**
	 * TestCase for isReportParameterExist method
	 * <p>
	 * Check if parameter is in http request. If parameter is a null parameter,also
	 * return true.
	 * 
	 */
	public void testIsReportParameterExist() {
		String paramName = "param"; //$NON-NLS-1$

		// Don't set in request
		assertFalse(ParameterAccessor.isReportParameterExist(request, paramName));

		// Set in request
		request.addParameter(paramName, "value"); //$NON-NLS-1$
		assertTrue(ParameterAccessor.isReportParameterExist(request, paramName));
		request.removeParameter(paramName);

		// Set as null parameter in request
		request.addParameter(ParameterAccessor.PARAM_ISNULL, paramName);
		assertTrue(ParameterAccessor.isReportParameterExist(request, paramName));
		request.removeParameter(ParameterAccessor.PARAM_ISNULL);
	}

	/**
	 * TestCase for isIidReportlet method
	 * <p>
	 * Check if reportlet is from instanceid.
	 * 
	 */
	public void testIsIidReportlet() {
		// Don't set anything
		assertFalse(ParameterAccessor.isIidReportlet(request));

		// Set instanceid as blank
		request.addParameter(ParameterAccessor.PARAM_INSTANCEID, ""); //$NON-NLS-1$
		assertFalse(ParameterAccessor.isIidReportlet(request));

		// Set instanceid not blank
		request.addParameter(ParameterAccessor.PARAM_INSTANCEID, "instanceid"); //$NON-NLS-1$
		assertTrue(ParameterAccessor.isIidReportlet(request));

		request.removeParameter(ParameterAccessor.PARAM_INSTANCEID);
	}

	/**
	 * TestCase for isBookmarkReportlet method
	 * <p>
	 * Check if reportlet is from bookmark.
	 * 
	 */
	public void testIsBookmarkReportlet() {
		// Don't set anything
		assertFalse(ParameterAccessor.isBookmarkReportlet(request));

		// Only set bookmark
		request.addParameter(ParameterAccessor.PARAM_BOOKMARK, "bookmark"); //$NON-NLS-1$
		assertFalse(ParameterAccessor.isBookmarkReportlet(request));

		// Set isReportlet
		request.addParameter(ParameterAccessor.PARAM_ISREPORTLET, "wrong"); //$NON-NLS-1$
		assertFalse(ParameterAccessor.isBookmarkReportlet(request));
		request.addParameter(ParameterAccessor.PARAM_ISREPORTLET, "true"); //$NON-NLS-1$
		assertTrue(ParameterAccessor.isBookmarkReportlet(request));

		request.removeParameter(ParameterAccessor.PARAM_BOOKMARK);
		request.removeParameter(ParameterAccessor.PARAM_ISREPORTLET);
	}

	/**
	 * TestCase for getReportletId method
	 * <p>
	 * Returns correct reportlet id.
	 * <ol>
	 * <li>If isIidReportlet is true, return instanceid as reportlet id.</li>
	 * <li>Else if isBookmarkReportlet is true,return bookmark as reportlet id.</li>
	 * <ol>
	 * 
	 */
	public void testGetReportletId() {
		String instanceid = "instanceid"; //$NON-NLS-1$
		String bookmark = "bookmark"; //$NON-NLS-1$
		// Don't set anything
		assertNull(ParameterAccessor.getReportletId(request));

		// Set instanceid
		request.addParameter(ParameterAccessor.PARAM_INSTANCEID, ""); //$NON-NLS-1$
		assertNull(ParameterAccessor.getReportletId(request));
		request.addParameter(ParameterAccessor.PARAM_INSTANCEID, instanceid);
		assertEquals(instanceid, ParameterAccessor.getReportletId(request));

		// Set bookmark
		request.addParameter(ParameterAccessor.PARAM_BOOKMARK, bookmark);
		request.addParameter(ParameterAccessor.PARAM_ISREPORTLET, "true"); //$NON-NLS-1$
		assertEquals(instanceid, ParameterAccessor.getReportletId(request));
		request.removeParameter(ParameterAccessor.PARAM_INSTANCEID);
		assertEquals(bookmark, ParameterAccessor.getReportletId(request));

		request.removeParameter(ParameterAccessor.PARAM_BOOKMARK);
		request.removeParameter(ParameterAccessor.PARAM_ISREPORTLET);
	}

	/**
	 * TestCase for getMaxRows method
	 * <p>
	 * Returns the maxrows setting
	 * 
	 */
	public void testGetMaxRows() {
		int DEFAULT_MAX_ROWS = 500;

		// Reset
		ParameterAccessor.reset();
		context.setInitParameter(ParameterAccessor.INIT_PARAM_VIEWER_MAXROWS, "" + DEFAULT_MAX_ROWS); //$NON-NLS-1$
		ParameterAccessor.initParameters(context);

		// Don't set anything
		assertEquals(DEFAULT_MAX_ROWS, ParameterAccessor.getMaxRows(request));

		// Set in request
		request.addParameter(ParameterAccessor.PARAM_MAXROWS, "WrongNumber"); //$NON-NLS-1$
		assertEquals(DEFAULT_MAX_ROWS, ParameterAccessor.getMaxRows(request));
		request.addParameter(ParameterAccessor.PARAM_MAXROWS, "200"); //$NON-NLS-1$
		assertEquals(200, ParameterAccessor.getMaxRows(request));

		request.removeParameter(ParameterAccessor.PARAM_MAXROWS);
	}

	/**
	 * TestCase for getResourceFolder method
	 * <p>
	 * Returns the resource folder
	 * 
	 */
	public void testGetResourceFolder() {
		// Don't set anything
		assertEquals(root.getAbsolutePath(), ParameterAccessor.getResourceFolder(request));

		// Set in request
		String resourceFolder = System.getProperty("java.io.tmpdir") + "/resource"; //$NON-NLS-1$ //$NON-NLS-2$
		request.addParameter(ParameterAccessor.PARAM_RESOURCE_FOLDER, resourceFolder);
		assertEquals(resourceFolder, ParameterAccessor.getResourceFolder(request));

		request.removeParameter(ParameterAccessor.PARAM_RESOURCE_FOLDER);
	}

	/**
	 * TestCase for isValidFilePath method
	 * <p>
	 * Validate current file path
	 * <ol>
	 * <li>If INIT_PARAM_DOCUMENT_FOLDER_ACCESS_ONLY is false, returns true.</li>
	 * <li>Else, validate current file if exist in document folder.</li>
	 * <ol>
	 */
	public void testIsValidFilePath() {
		String reportFile = System.getProperty("java.io.tmpdir") + "/report1.rptdesign"; //$NON-NLS-1$ //$NON-NLS-2$

		// INIT_PARAM_DOCUMENT_FOLDER_ACCESS_ONLY is false
		assertTrue(ParameterAccessor.isValidFilePath(request, reportFile));

		// INIT_PARAM_DOCUMENT_FOLDER_ACCESS_ONLY is true
		ParameterAccessor.reset();
		context.setInitParameter(ParameterAccessor.INIT_PARAM_WORKING_FOLDER_ACCESS_ONLY, "true"); //$NON-NLS-1$
		ParameterAccessor.initParameters(context);
		assertFalse(ParameterAccessor.isValidFilePath(request, reportFile));

		reportFile = new File(root, "report1.rptdesign").getAbsolutePath(); //$NON-NLS-1$
		assertTrue(ParameterAccessor.isValidFilePath(request, reportFile));
	}

	/**
	 * TestCase for getReport method
	 * <p>
	 * Returns the report file path.
	 * <ol>
	 * <li>If report file is absolute path, returns it directly.</li>
	 * <li>If it is relative path, returns the absolute path that is relative to the
	 * document folder.</li>
	 * <ol>
	 * 
	 */
	public void testGetReport() {
		// Absolute path
		String reportFile = System.getProperty("java.io.tmpdir") + "/report1.rptdesign"; //$NON-NLS-1$ //$NON-NLS-2$
		request.addParameter(ParameterAccessor.PARAM_REPORT, reportFile);
		assertEquals(reportFile, ParameterAccessor.getReport(request, null));

		// Relative path
		request.addParameter(ParameterAccessor.PARAM_REPORT, "report1.rptdesign"); //$NON-NLS-1$
		reportFile = new File(root, "report1.rptdesign").getAbsolutePath().replace('\\', '/'); //$NON-NLS-1$
		String returnValue = ParameterAccessor.getReport(request, null).replace('\\', '/');
		assertEquals(reportFile, returnValue);

		request.removeParameter(ParameterAccessor.PARAM_REPORT);
	}

	/**
	 * TestCase for getReportDocument method
	 * <p>
	 * Returns the report document file path
	 * <ol>
	 * <li>If document file is absolute path, returns it directly.</li>
	 * <li>If it is relative path, returns the absolute path that is relative to the
	 * working folder.</li>
	 * <li>If null, generate the document file that follows the current session id
	 * in the document folder.</li>
	 * <ol>
	 */
	public void testGetReportDocument() {
		// Absolute path
		String documentFile = System.getProperty("java.io.tmpdir") + "/report1.rptdocument"; //$NON-NLS-1$ //$NON-NLS-2$
		request.addParameter(ParameterAccessor.PARAM_REPORT_DOCUMENT, documentFile);
		try {
			assertEquals(documentFile, ParameterAccessor.getReportDocument(request, null, false));
		} catch (ViewerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Relative path
		request.addParameter(ParameterAccessor.PARAM_REPORT_DOCUMENT, "report1.rptdocument"); //$NON-NLS-1$
		documentFile = new File(root, "report1.rptdocument").getAbsolutePath().replace('\\', '/'); //$NON-NLS-1$
		try {
			String docName = ParameterAccessor.getReportDocument(request, null, false).replace('\\', '/');
			assertEquals(documentFile, docName);
		} catch (ViewerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * // Don't exist document file in request request.removeParameter(
		 * ParameterAccessor.PARAM_REPORT_DOCUMENT ); String reportFile =
		 * "myproject\\report1.rptdesign"; //$NON-NLS-1$ request.addParameter(
		 * ParameterAccessor.PARAM_REPORT, reportFile ); try { documentFile =
		 * ParameterAccessor .getReportDocument( request, null, true ); } catch
		 * (ViewerException e) { // TODO Auto-generated catch block e.printStackTrace();
		 * } assertNotNull( documentFile ); assertTrue( documentFile.indexOf(
		 * session.getId( ) ) > 0 ); assertTrue( documentFile.indexOf(
		 * "report1.rptdocument" ) > 0 ); //$NON-NLS-1$
		 */
		request.removeParameter(ParameterAccessor.PARAM_REPORT);
		request.removeParameter(ParameterAccessor.PARAM_REPORT_DOCUMENT);
	}

	/**
	 * TestCase for getSelectedColumns method
	 * <p>
	 * Get selected column list.
	 * 
	 */
	public void testGetSelectedColumns() {
		assertNotNull(ParameterAccessor.getSelectedColumns(request));
		assertTrue(ParameterAccessor.getSelectedColumns(request).size() <= 0);

		request.addParameter(ParameterAccessor.PARAM_SELECTEDCOLUMNNUMBER, "3"); //$NON-NLS-1$
		request.addParameter(ParameterAccessor.PARAM_SELECTEDCOLUMN + "0", "column1"); //$NON-NLS-1$//$NON-NLS-2$
		request.addParameter(ParameterAccessor.PARAM_SELECTEDCOLUMN + "1", "column2"); //$NON-NLS-1$ //$NON-NLS-2$
		request.addParameter(ParameterAccessor.PARAM_SELECTEDCOLUMN + "2", "column3"); //$NON-NLS-1$//$NON-NLS-2$

		Collection columns = ParameterAccessor.getSelectedColumns(request);
		assertNotNull(columns);
		assertTrue(columns.size() == 3);
		assertTrue(columns.contains("column1")); //$NON-NLS-1$
		assertTrue(columns.contains("column2")); //$NON-NLS-1$
		assertTrue(columns.contains("column3")); //$NON-NLS-1$

		request.removeParameter(ParameterAccessor.PARAM_SELECTEDCOLUMNNUMBER);
		request.removeParameter(ParameterAccessor.PARAM_SELECTEDCOLUMN + "0"); //$NON-NLS-1$
		request.removeParameter(ParameterAccessor.PARAM_SELECTEDCOLUMN + "1"); //$NON-NLS-1$
		request.removeParameter(ParameterAccessor.PARAM_SELECTEDCOLUMN + "2"); //$NON-NLS-1$
	}

	/**
	 * TestCase for htmlEncode method
	 * <p>
	 * Returns HTML encoded string
	 * 
	 */
	public void testHtmlEncode() {
		assertNull(ParameterAccessor.htmlEncode(null));
		assertTrue(ParameterAccessor.htmlEncode("").length() <= 0); //$NON-NLS-1$

		String plain = "\t\n\r \"\'<>`&"; //$NON-NLS-1$
		String encoded = "&#09;<br>&#13;&#32;&#34;&#39;&#60;&#62;&#96;&#38;"; //$NON-NLS-1$

		assertEquals(encoded, ParameterAccessor.htmlEncode(plain));
	}

	/**
	 * TestCase for pushAppContext method
	 * <p>
	 * Push user-defined application context object into engine context
	 * 
	 */
	public void testPushAppContext() {
		Map map = null;
		assertNotNull(ParameterAccessor.pushAppContext(map, request));

		String contextKey = "appContextKey"; //$NON-NLS-1$
		request.setAttribute(ParameterAccessor.ATTR_APPCONTEXT_KEY, contextKey);
		assertNull(ParameterAccessor.pushAppContext(map, request).get(contextKey));

		Map appContext = new HashMap();
		appContext.put("key1", "value1"); //$NON-NLS-1$//$NON-NLS-2$
		request.setAttribute(ParameterAccessor.ATTR_APPCONTEXT_VALUE, appContext);

		assertNotNull(ParameterAccessor.pushAppContext(map, request).get(contextKey));
		assertTrue(((HashMap) (ParameterAccessor.pushAppContext(map, request).get(contextKey))).containsKey("key1")); //$NON-NLS-1$
	}
}
