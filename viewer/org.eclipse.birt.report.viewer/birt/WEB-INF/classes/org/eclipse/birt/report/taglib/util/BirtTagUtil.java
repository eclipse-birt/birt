/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.taglib.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtViewerReportDesignHandle;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.taglib.component.ViewerField;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Utilities for Birt tags
 *
 */
public class BirtTagUtil {

	/**
	 * Convert String to correct boolean value.
	 *
	 * @param bool
	 * @return
	 */
	public static String convertBooleanValue(String bool) {
		boolean b = Boolean.parseBoolean(bool);
		return String.valueOf(b);
	}

	/**
	 * Convert String to boolean.
	 *
	 * @param bool
	 * @return
	 */
	public static boolean convertToBoolean(String bool) {
		if (bool == null) {
			return false;
		}

		return Boolean.parseBoolean(bool);
	}

	/**
	 * Returns the output format.Default value is html.
	 *
	 * @param format
	 * @return
	 */
	public static String getFormat(String format) {
		if (format == null || format.length() <= 0 || format.equalsIgnoreCase(ParameterAccessor.PARAM_FORMAT_HTM)) {
			return ParameterAccessor.PARAM_FORMAT_HTML;
		}

		return format;
	}

	/**
	 * Get report locale.
	 *
	 * @param request HttpServletRequest
	 * @param locale  String
	 * @return locale
	 */

	public static Locale getLocale(HttpServletRequest request, String sLocale) {
		Locale locale;

		// Get Locale from String value
		locale = ParameterAccessor.getLocaleFromString(sLocale);

		// Get Locale from client browser
		if (locale == null) {
			locale = request.getLocale();
		}

		// Get Locale from Web Context
		if (locale == null) {
			locale = ParameterAccessor.getWebAppLocale();
		}

		return locale;
	}

	/**
	 * Get report time zone.
	 *
	 * @param request  HttpServletRequest
	 * @param timeZone time zone String
	 * @return locale
	 */

	public static TimeZone getTimeZone(HttpServletRequest request, String sTimeZone) {
		TimeZone timeZone;

		// Get Locale from String value
		timeZone = ParameterAccessor.getTimeZoneFromString(sTimeZone);

		// Get Locale from Web Context
		if (timeZone == null) {
			timeZone = ParameterAccessor.getWebAppTimeZone();
		}

		return timeZone;
	}

	/**
	 * If a report file name is a relative path, it is relative to document folder.
	 * So if a report file path is relative path, it's absolute path is synthesized
	 * by appending file path to the document folder path.
	 *
	 * @param file
	 * @return
	 */

	public static String createAbsolutePath(String filePath) {
		if (filePath != null && filePath.trim().length() > 0 && ParameterAccessor.isRelativePath(filePath)) {
			return ParameterAccessor.workingFolder + File.separator + filePath;
		}
		return filePath;
	}

	/**
	 * Returns report design handle
	 *
	 * @param request
	 * @param viewer
	 * @return
	 * @throws Exception
	 */
	public static IViewerReportDesignHandle getDesignHandle(HttpServletRequest request, ViewerField viewer)
			throws Exception {
		if (viewer == null) {
			return null;
		}

		IViewerReportDesignHandle design = null;
		IReportRunnable reportRunnable = null;

		// Get the absolute report design and document file path
		String designFile = ParameterAccessor.getReport(request, viewer.getReportDesign());
		String documentFile = ParameterAccessor.getReportDocument(request, viewer.getReportDocument(), false);

		// check if document file path is valid
		boolean isValidDocument = ParameterAccessor.isValidFilePath(request, viewer.getReportDocument());
		if (documentFile != null && isValidDocument) {
			// open the document instance
			try {
				IReportDocument reportDocumentInstance = ReportEngineService.getInstance()
						.openReportDocument(designFile, documentFile, getModuleOptions(viewer));

				if (reportDocumentInstance != null) {
					viewer.setDocumentInUrl(true);
					reportRunnable = reportDocumentInstance.getReportRunnable();
					reportDocumentInstance.close();
				}
			} catch (Exception e) {
			}
		}

		// if report runnable is null, then get it from design file
		if (reportRunnable == null) {
			// if only set __document parameter, throw exception directly
			if (documentFile != null && designFile == null) {
				if (isValidDocument) {
					throw new ViewerException(ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_FILE_ERROR,
							new String[] { documentFile });
				} else {
					throw new ViewerException(ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_ACCESS_ERROR,
							new String[] { documentFile });
				}
			}

			// check if the report file path is valid
			if (!ParameterAccessor.isValidFilePath(request, viewer.getReportDesign())) {
				throw new ViewerException(ResourceConstants.GENERAL_EXCEPTION_REPORT_ACCESS_ERROR,
						new String[] { designFile });
			} else {
				reportRunnable = BirtUtility.getRunnableFromDesignFile(request, designFile, getModuleOptions(viewer));

				if (reportRunnable == null) {
					throw new ViewerException(ResourceConstants.GENERAL_EXCEPTION_REPORT_FILE_ERROR,
							new String[] { new File(designFile).getName() });
				}
			}
		}

		if (reportRunnable != null) {
			design = new BirtViewerReportDesignHandle(IViewerReportDesignHandle.RPT_RUNNABLE_OBJECT, reportRunnable);
		}

		return design;
	}

	/**
	 * Create Module Options
	 *
	 * @param viewer
	 * @return
	 */
	public static Map getModuleOptions(ViewerField viewer) {
		if (viewer == null) {
			return null;
		}

		Map options = new HashMap();
		String resourceFolder = viewer.getResourceFolder();
		if (resourceFolder == null || resourceFolder.trim().length() <= 0) {
			resourceFolder = ParameterAccessor.birtResourceFolder;
		}

		options.put(IModuleOption.RESOURCE_FOLDER_KEY, resourceFolder);
		options.put(IModuleOption.PARSER_SEMANTIC_CHECK_KEY, Boolean.FALSE);
		return options;
	}

	public static void writeScript(JspWriter writer, String content) throws IOException {
		writer.write("\n<script language=\"JavaScript\">\n"); //$NON-NLS-1$
		writer.write(content);
		writer.write("</script>\n"); //$NON-NLS-1$
	}

	public static void writeExtScript(JspWriter writer, String fileName) throws IOException {
		writer.write("<script src=\"" //$NON-NLS-1$
				+ fileName + "\" type=\"text/javascript\"></script>\n"); //$NON-NLS-1$
	}

	public static void writeExtScripts(JspWriter writer, String baseUrl, String[] files) throws IOException {
		for (int i = 0; i < files.length; i++) {
			writeExtScript(writer, baseUrl + files[i]);
		}
	}

	public static void writeOption(JspWriter writer, String label, String value, boolean selected) throws IOException {
		writer.write("<option "); //$NON-NLS-1$
		writer.write(" value=\"" + ParameterAccessor.htmlEncode(value) + "\" "); //$NON-NLS-1$ //$NON-NLS-2$
		if (selected) {
			writer.write(" selected "); //$NON-NLS-1$
		}
		writer.write(">"); //$NON-NLS-1$
		writer.write(ParameterAccessor.htmlEncode(label) + "</option>\n"); //$NON-NLS-1$
	}
}
