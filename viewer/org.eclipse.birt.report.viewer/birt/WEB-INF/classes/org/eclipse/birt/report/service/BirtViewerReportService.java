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

package org.eclipse.birt.report.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.ICascadingParameterGroup;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.api.ExportedColumn;
import org.eclipse.birt.report.service.api.ExportedResultSet;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.OutputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterGroupDefinition;
import org.eclipse.birt.report.service.api.ParameterSelectionChoice;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.service.api.ToC;
import org.eclipse.birt.report.soapengine.api.Column;
import org.eclipse.birt.report.soapengine.api.ResultSet;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.DataExtractionParameterUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

import com.ibm.icu.util.ULocale;

/**
 * The main entrance for BIRT viewer to invoke report service APIs.
 */
public class BirtViewerReportService implements IViewerReportService {

	/**
	 * Default constructor
	 *
	 * @param context
	 */
	public BirtViewerReportService(ServletContext context) {
		try {
			ReportEngineService.initEngineInstance(context);
		} catch (BirtException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#runReport(org.eclipse.birt.report.service.api.IViewerReportDesignHandle,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.util.Map)
	 */
	@Override
	public String runReport(IViewerReportDesignHandle design, String outputDocName, InputOptions runOptions,
			Map parameters) throws ReportServiceException {
		return runReport(design, outputDocName, runOptions, parameters, null);
	}

	@Override
	public String runReport(IViewerReportDesignHandle design, String outputDocName, InputOptions runOptions,
			Map parameters, Map displayTexts) throws ReportServiceException {
		return runReport(design, outputDocName, runOptions, parameters, displayTexts, null);
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#runReport(org.eclipse.birt.report.service.api.IViewerReportDesignHandle,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.util.Map, java.util.Map)
	 */
	@Override
	public String runReport(IViewerReportDesignHandle design, String outputDocName, InputOptions runOptions,
			Map parameters, Map displayTexts, List<Exception> errorList) throws ReportServiceException {
		if (design == null || design.getDesignObject() == null) {
			throw new ReportServiceException(
					BirtResources.getMessage(ResourceConstants.GENERAL_EXCEPTION_NO_REPORT_DESIGN));
		}

		IReportRunnable runnable;
		HttpServletRequest request = (HttpServletRequest) runOptions.getOption(InputOptions.OPT_REQUEST);
		Locale locale = (Locale) runOptions.getOption(InputOptions.OPT_LOCALE);
		TimeZone timeZone = (TimeZone) runOptions.getOption(InputOptions.OPT_TIMEZONE);

		ViewerAttributeBean attrBean = (ViewerAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		// Set parameters
		Map parsedParams = attrBean.getParameters();
		if (parameters != null) {
			parsedParams.putAll(parameters);
		}

		// Set display Text of select parameters
		Map displayTextMap = attrBean.getDisplayTexts();
		if (displayTexts != null) {
			displayTextMap.putAll(displayTexts);
		}

		runnable = (IReportRunnable) design.getDesignObject();

		try {
			// get maxRows
			Integer maxRows = null;
			if (ParameterAccessor.isReportParameterExist(request, ParameterAccessor.PARAM_MAXROWS)) {
				maxRows = ParameterAccessor.getMaxRows(request);
			}

			List<Exception> errors = ReportEngineService.getInstance().runReport(request, runnable, outputDocName,
					locale, timeZone, parsedParams, displayTextMap, maxRows);
			if (errors != null && !errors.isEmpty()) {
				errorList.addAll(errors);
			}
		} catch (RemoteException e) {
			if (e.getCause() instanceof ReportServiceException) {
				throw (ReportServiceException) e.getCause();
			} else {
				throw new ReportServiceException(e.getLocalizedMessage(), e.getCause());
			}
		}
		return outputDocName;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getPage(java.lang.String,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.util.List)
	 */
	@Override
	public ByteArrayOutputStream getPage(String docName, String pageID, InputOptions renderOptions, List activeIds)
			throws ReportServiceException {
		IReportDocument doc = null;
		ByteArrayOutputStream os = null;
		try {
			doc = openReportDocument(docName, renderOptions);
			long pageNum = Long.parseLong(pageID);

			os = new ByteArrayOutputStream();
			ReportEngineService.getInstance().renderReport(os, doc, pageNum, null, renderOptions, activeIds);
		} catch (RemoteException e) {
			throwReportServiceException(e);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
		return os;
	}

	/**
	 * Returns whether a given report document has right-to-left orientation.
	 *
	 * @param docName       document file name
	 * @param renderOptions render options
	 * @return true if the report document is right-to-left, false otherwise
	 * @throws ReportServiceException
	 * @deprecated
	 */
	@Deprecated
	@Override
	public boolean isDocumentRtl(String docName, InputOptions renderOptions) throws ReportServiceException {
		IReportDocument doc = null;
		try {
			doc = openReportDocument(docName, renderOptions);
			String bidiOrientation = doc.getReportDesign().getBidiOrientation();
			return (DesignChoiceConstants.BIDI_DIRECTION_RTL.equalsIgnoreCase(bidiOrientation)); // $NON-NLS-1$
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	/**
	 * Opens the given report document name and returns the document instance. The
	 * document must be closed after it has been used.
	 *
	 * @param docName       document file name
	 * @param renderOptions render options
	 * @return document instance
	 * @throws RemoteException
	 */
	private IReportDocument openReportDocument(String docName, InputOptions renderOptions)
			throws ReportServiceException {
		IReportDocument doc = null;
		try {
			doc = ReportEngineService.getInstance().openReportDocument(getReportDesignName(renderOptions), docName,
					getModuleOptions(renderOptions));
		} catch (RemoteException e) {
			throwReportServiceException(e);
		}
		return doc;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getPageByBookmark(java.lang.String,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.util.List)
	 */
	@Override
	public ByteArrayOutputStream getPageByBookmark(String docName, String bookmark, InputOptions renderOptions,
			List activeIds) throws ReportServiceException {
		long pageNum = getPageNumberByBookmark(docName, bookmark, renderOptions);
		return getPage(docName, pageNum + "", renderOptions, activeIds); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getPageByObjectId(java.lang.String,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.util.List)
	 */
	@Override
	public ByteArrayOutputStream getPageByObjectId(String docName, String objectId, InputOptions renderOptions,
			List activeIds) throws ReportServiceException {
		// TODO: Implement
		return null;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getReportlet(java.lang.String,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.util.List)
	 */
	@Override
	public ByteArrayOutputStream getReportlet(String docName, String objectId, InputOptions renderOptions,
			List activeIds) throws ReportServiceException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		renderReportlet(docName, objectId, renderOptions, activeIds, out);
		return out;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#renderReportlet(java.lang.String,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.util.List, java.io.OutputStream)
	 */
	@Override
	public void renderReportlet(String docName, String objectId, InputOptions renderOptions, List activeIds,
			OutputStream out) throws ReportServiceException {
		IReportDocument doc = null;
		try {
			doc = openReportDocument(docName, renderOptions);

			ReportEngineService.getInstance().renderReportlet(out, doc, renderOptions, objectId, null);
		} catch (RemoteException e) {
			throwReportServiceException(e);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#renderReport(java.lang.String,
	 *      int,java.lang.String, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.io.OutputStream)
	 */
	@Override
	public void renderReport(String docName, int pageNum, String pageRange, InputOptions renderOptions,
			OutputStream out) throws ReportServiceException {
		IReportDocument doc = null;
		try {
			doc = openReportDocument(docName, renderOptions);

			ReportEngineService.getInstance().renderReport(out, doc, pageNum, pageRange, renderOptions, null);
		} catch (RemoteException e) {
			throwReportServiceException(e);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#renderReport(java.lang.String,
	 *      int, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.io.OutputStream)
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void renderReport(String docName, int pageNum, InputOptions renderOptions, OutputStream out)
			throws ReportServiceException {
		renderReport(docName, pageNum, null, renderOptions, out);
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#extractData(java.lang.String,
	 *      org.eclipse.birt.report.service.api.InputOptions,java.io.OutputStream)
	 */
	@Override
	public void extractData(String docName, InputOptions options, OutputStream out) throws ReportServiceException {
		IReportDocument doc = null;
		try {
			doc = openReportDocument(docName, options);

			Locale locale = (Locale) options.getOption(InputOptions.OPT_LOCALE);
			TimeZone timeZone = (TimeZone) options.getOption(InputOptions.OPT_TIMEZONE);
			HttpServletRequest request = (HttpServletRequest) options.getOption(InputOptions.OPT_REQUEST);

			String extractFormat = ParameterAccessor.getExtractFormat(request);
			String extractExtension = ParameterAccessor.getExtractExtension(request);

			String resultSetName = ParameterAccessor.getResultSetName(request);

			// first, try to get instanceid from bookmark
			String instanceId = null;
			String bookmark = ParameterAccessor.getBookmark(request);
			if (bookmark != null && doc != null) {
				InstanceID iidObj = doc.getBookmarkInstance(bookmark);
				if (iidObj != null) {
					instanceId = iidObj.toString();
				}
			}

			// get instanceid from request
			if (instanceId == null) {
				instanceId = ParameterAccessor.getInstanceId(request);
			}

			Collection columns = ParameterAccessor.getSelectedColumns(request);
			Map paramMap = ParameterAccessor.getParameterAsMap(request);

			ReportEngineService.getInstance().extractDataEx(doc, extractFormat, extractExtension, resultSetName,
					instanceId, columns, locale, timeZone, paramMap, out);
		} catch (RemoteException e) {
			throwReportServiceException(e);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	/**
	 * Temporary method for extracting the exception from the DummyRemoteException
	 * and throwing it.
	 */
	private void throwReportServiceException(RemoteException e) throws ReportServiceException {
		Throwable wrappedException = e;
		if (e instanceof ReportEngineService.DummyRemoteException) {
			wrappedException = e.getCause();
		}
		if (wrappedException instanceof ReportServiceException) {
			throw (ReportServiceException) wrappedException;
		} else if (wrappedException != null) {
			throw new ReportServiceException(wrappedException.getLocalizedMessage(), wrappedException);
		} else {
			throw new ReportServiceException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#extractResultSet(java.lang.String,
	 *      java.lang.String, java.util.Collection, java.util.Set,
	 *      org.eclipse.birt.report.service.api.InputOptions, java.io.OutputStream)
	 */
	@Override
	public void extractResultSet(String docName, String resultSetId, Collection columns, Set filters,
			InputOptions options, OutputStream out) throws ReportServiceException {
		IReportDocument doc = null;
		try {
			doc = openReportDocument(docName, options);
			Locale locale = (Locale) options.getOption(InputOptions.OPT_LOCALE);
			TimeZone timeZone = (TimeZone) options.getOption(InputOptions.OPT_TIMEZONE);
			HttpServletRequest request = (HttpServletRequest) options.getOption(InputOptions.OPT_REQUEST);

			Map paramMap = ParameterAccessor.getParameterAsMap(request);
			ReportEngineService.getInstance().extractDataEx(doc, DataExtractionParameterUtil.EXTRACTION_FORMAT_CSV,
					DataExtractionParameterUtil.EXTRACTION_EXTENSION_CSV, resultSetId, null, columns, locale, timeZone,
					paramMap, out);
		} catch (RemoteException e) {
			throwReportServiceException(e);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getResultSetsMetadata(java.lang.String,
	 *      org.eclipse.birt.report.service.api.InputOptions)
	 */
	@Override
	public List getResultSetsMetadata(String docName, InputOptions options) throws ReportServiceException {
		IReportDocument doc = null;
		ResultSet[] resultSetArray = null;
		try {
			doc = openReportDocument(docName, options);

			resultSetArray = ReportEngineService.getInstance().getResultSets(doc);
		} catch (RemoteException e) {
			throwReportServiceException(e);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}

		if (resultSetArray == null || resultSetArray.length < 0) {
			throw new ReportServiceException(
					BirtResources.getMessage(ResourceConstants.REPORT_SERVICE_EXCEPTION_EXTRACT_DATA_NO_RESULT_SET));
		}

		return transformResultSetArray(resultSetArray);
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getResultSetsMetadata(java.lang.String,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions)
	 */
	@Override
	public List getResultSetsMetadata(String docName, String instanceId, InputOptions options)
			throws ReportServiceException {
		// TODO: Implement
		return null;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getImage(java.lang.String,
	 *      java.lang.String, java.io.OutputStream,
	 *      org.eclipse.birt.report.service.api.InputOptions)
	 */
	@Override
	public void getImage(String docName, String imageId, OutputStream out, InputOptions options)
			throws ReportServiceException {
		try {
			HttpServletRequest request = (HttpServletRequest) options.getOption(InputOptions.OPT_REQUEST);

			ReportEngineService.getInstance().renderImage(imageId, request, out);
		} catch (RemoteException e) {
			throwReportServiceException(e);
		}

	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getTOC(java.lang.String,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions)
	 */
	@Override
	public ToC getTOC(String docName, String tocId, InputOptions options) throws ReportServiceException {
		ToC tableOfContents = null;
		IReportDocument doc = null;
		try {
			doc = openReportDocument(docName, options);
			TOCNode node = null;
			if (doc != null) {
				Locale locale = null;
				TimeZone timeZone = null;
				if (options != null) {
					locale = (Locale) options.getOption(InputOptions.OPT_LOCALE);
					timeZone = (TimeZone) options.getOption(InputOptions.OPT_TIMEZONE);
				}
				if (locale == null) {
					locale = Locale.getDefault();
				}
				ITOCTree tocTree = null;
				if (timeZone != null) {
					tocTree = doc.getTOCTree(DesignChoiceConstants.FORMAT_TYPE_VIEWER, ULocale.forLocale(locale),
							BirtUtility.toICUTimeZone(timeZone));
				} else {
					tocTree = doc.getTOCTree(DesignChoiceConstants.FORMAT_TYPE_VIEWER, ULocale.forLocale(locale));
				}

				if (tocId != null) {
					node = tocTree.findTOC(tocId);

				} else {
					node = tocTree.findTOC(null);
				}
			}

			if (node == null) {
				throw new ReportServiceException(
						BirtResources.getMessage(ResourceConstants.REPORT_SERVICE_EXCEPTION_INVALID_TOC));
			}

			tableOfContents = transformTOCNode(node);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
		return tableOfContents;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#findTocByName(java.lang.String,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions)
	 */
	@Override
	public String findTocByName(String docName, String name, InputOptions options) {
		IReportDocument doc = null;
		try {
			doc = openReportDocument(docName, options);

			return BirtUtility.findTocByName(doc, name, options);
		} catch (ReportServiceException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getPageCount(java.lang.String,
	 *      org.eclipse.birt.report.service.api.InputOptions,
	 *      org.eclipse.birt.report.service.api.OutputOptions)
	 */
	@Override
	public long getPageCount(String docName, InputOptions options, OutputOptions outputOptions)
			throws ReportServiceException {
		IReportDocument doc = null;
		long count = 1L;

		try {
			doc = openReportDocument(docName, options);
			if (doc != null) {
				count = doc.getPageCount();
			}
		} finally {
			if (doc != null) {
				doc.close();
			}
		}

		return count;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getParameterDefinitions(org.eclipse.birt.report.service.api.IViewerReportDesignHandle,
	 *      org.eclipse.birt.report.service.api.InputOptions, boolean)
	 */
	@Override
	public Collection getParameterDefinitions(IViewerReportDesignHandle design, InputOptions runOptions,
			boolean includeGroups) throws ReportServiceException {
		IGetParameterDefinitionTask task = null;
		try {
			task = getParameterDefinitionTask(design, runOptions);
			if (task != null) {
				Collection params = task.getParameterDefns(true);
				return convertEngineParameters(params, includeGroups);
			}
		} finally {
			if (task != null) {
				task.close();
			}
		}

		return null;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getSelectionListForCascadingGroup(org.eclipse.birt.report.service.api.IViewerReportDesignHandle,
	 *      java.lang.String, java.lang.Object[],
	 *      org.eclipse.birt.report.service.api.InputOptions)
	 */
	@Override
	public Collection getSelectionListForCascadingGroup(IViewerReportDesignHandle design, String groupName,
			Object[] groupKeys, InputOptions options) throws ReportServiceException {
		IGetParameterDefinitionTask task = null;
		HttpServletRequest request = (HttpServletRequest) options.getOption(InputOptions.OPT_REQUEST);
		try {
			task = getParameterDefinitionTask(design, options);
			if (task != null) {
				ViewerAttributeBean bean = getViewerAttrBean(options);
				if (bean != null) {
					com.ibm.icu.util.TimeZone tz = BirtUtility.toICUTimeZone(bean.getTimeZone());
					if (tz != null) {
						task.setTimeZone(tz);
					}
					task.setLocale(bean.getLocale());
					task.setParameterValues(bean.getParameters());
				}

				// Add task into session
				BirtUtility.addTask(request, task);

				task.evaluateQuery(groupName);
				Collection selectionList = task.getSelectionListForCascadingGroup(groupName, groupKeys);
				return convertEngineParameterSelectionChoice(selectionList);
			}

		} finally {
			// Remove task from http session
			BirtUtility.removeTask(request);

			if (task != null) {
				task.close();
			}
		}

		return null;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getParameterSelectionList(org.eclipse.birt.report.service.api.IViewerReportDesignHandle,
	 *      org.eclipse.birt.report.service.api.InputOptions, java.lang.String)
	 */
	@Override
	public Collection getParameterSelectionList(IViewerReportDesignHandle design, InputOptions runOptions,
			String paramName) throws ReportServiceException {
		IGetParameterDefinitionTask task = null;
		try {
			task = getParameterDefinitionTask(design, runOptions);
			if (task != null) {
				ViewerAttributeBean bean = getViewerAttrBean(runOptions);
				if (bean != null) {
					com.ibm.icu.util.TimeZone tz = BirtUtility.toICUTimeZone(bean.getTimeZone());
					if (tz != null) {
						task.setTimeZone(tz);
					}
					task.setLocale(bean.getLocale());
					task.setParameterValues(bean.getParameters());
				}

				Collection selectionList = task.getSelectionList(paramName);
				return convertEngineParameterSelectionChoice(selectionList);
			}
		} finally {
			if (task != null) {
				task.close();
			}
		}

		return null;
	}

	/**
	 * Returns the GetParameterDefinitionTask
	 *
	 * @param design
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	private IGetParameterDefinitionTask getParameterDefinitionTask(IViewerReportDesignHandle design,
			InputOptions options) throws ReportServiceException {
		IGetParameterDefinitionTask task;
		if (design.getContentType() == IViewerReportDesignHandle.RPT_RUNNABLE_OBJECT) {
			// IReportRunnable is specified in IViewerReportDesignHandle.
			IReportRunnable runnable = (IReportRunnable) design.getDesignObject();
			task = ReportEngineService.getInstance().createGetParameterDefinitionTask(runnable, options);
		} else {
			// report design name is specified in IViewerReportDesignHandle.
			try {
				task = getParameterDefinitionTask(design.getFileName(), options);
			} catch (EngineException e) {
				throw new ReportServiceException(e.getLocalizedMessage(), e.getCause());
			}
		}

		// set AppConext
		if (task != null) {
			HttpServletRequest request = (HttpServletRequest) options.getOption(InputOptions.OPT_REQUEST);
			HashMap context = new HashMap();
			context.put(EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST, request);
			ParameterAccessor.pushAppContext(context, request);
			task.setAppContext(context);
		}

		return task;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getPageNumberByBookmark(java.lang.String,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions)
	 */
	@Override
	public long getPageNumberByBookmark(String docName, String bookmark, InputOptions options)
			throws ReportServiceException {
		IReportDocument doc = null;
		long pageNumber = -1L;
		try {
			doc = openReportDocument(docName, options);
			if (doc != null) {
				pageNumber = doc.getPageNumber(bookmark);
			}
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
		return pageNumber;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getPageNumberByObjectId(java.lang.String,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions)
	 */
	@Override
	public long getPageNumberByObjectId(String docName, String objectId, InputOptions options)
			throws ReportServiceException {
		IReportDocument doc = null;
		long pageNumber = -1L;
		try {
			doc = openReportDocument(docName, options);
			pageNumber = doc.getPageNumber(objectId);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
		return pageNumber;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#runAndRenderReport(org.eclipse.birt.report.service.api.IViewerReportDesignHandle,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.util.Map, java.io.OutputStream, java.util.List)
	 */
	@Override
	public void runAndRenderReport(IViewerReportDesignHandle design, String outputDocName, InputOptions options,
			Map parameters, OutputStream out, List activeIds) throws ReportServiceException {
		runAndRenderReport(design, outputDocName, options, parameters, out, activeIds, null);
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#runAndRenderReport(org.eclipse.birt.report.service.api.IViewerReportDesignHandle,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions,
	 *      java.util.Map, java.io.OutputStream, java.util.List, java.util.Map)
	 */
	@Override
	public void runAndRenderReport(IViewerReportDesignHandle design, String outputDocName, InputOptions options,
			Map parameters, OutputStream out, List activeIds, Map displayTexts) throws ReportServiceException {
		if (design == null || design.getDesignObject() == null) {
			throw new ReportServiceException(
					BirtResources.getMessage(ResourceConstants.GENERAL_EXCEPTION_NO_REPORT_DESIGN));
		}

		HttpServletRequest request = (HttpServletRequest) options.getOption(InputOptions.OPT_REQUEST);

		try {
			ViewerAttributeBean attrBean = (ViewerAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
			String reportTitle = ParameterAccessor.htmlDecode(attrBean.getReportTitle());
			IReportRunnable runnable = (IReportRunnable) design.getDesignObject();

			// get maxRows
			Integer maxRows = null;
			if (ParameterAccessor.isReportParameterExist(request, ParameterAccessor.PARAM_MAXROWS)) {
				maxRows = ParameterAccessor.getMaxRows(request);
			}

			ReportEngineService.getInstance().runAndRenderReport(runnable, out, options, parameters, null, null, null,
					displayTexts, reportTitle, maxRows);
		} catch (RemoteException e) {
			throwReportServiceException(e);
		}
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#getParameterDefaultValue(org.eclipse.birt.report.service.api.IViewerReportDesignHandle,
	 *      java.lang.String, org.eclipse.birt.report.service.api.InputOptions)
	 */
	@Override
	public Object getParameterDefaultValue(IViewerReportDesignHandle design, String parameterName, InputOptions options)
			throws ReportServiceException {
		IGetParameterDefinitionTask task = null;
		Object defaultValue = null;
		try {
			task = getParameterDefinitionTask(design, options);
			if (task != null) {
				defaultValue = task.getDefaultValue(parameterName);
			}
		} finally {
			if (task != null) {
				task.close();
			}
		}

		return defaultValue;
	}

	/**
	 * @see org.eclipse.birt.report.service.api.IViewerReportService#setContext(java.lang.Object,
	 *      org.eclipse.birt.report.service.api.InputOptions)
	 */
	@Override
	public void setContext(Object context, InputOptions options) throws BirtException {
		ReportEngineService.getInstance().setEngineContext((ServletContext) context);
	}

	/**
	 * Returns report runnable object
	 *
	 * @param design
	 * @param moduleOptions
	 * @return
	 * @throws ReportServiceException
	 */
	public IReportRunnable getReportRunnable(IViewerReportDesignHandle design, Map moduleOptions)
			throws ReportServiceException {
		IReportRunnable runnable;
		if (design.getContentType() == IViewerReportDesignHandle.RPT_RUNNABLE_OBJECT) {
			// IReportRunnable is specified in IViewerReportDesignHandle.
			runnable = (IReportRunnable) design.getDesignObject();
		} else {
			// report design name is specified in IViewerReportDesignHandle.
			try {
				runnable = ReportEngineService.getInstance().openReportDesign(design.getFileName(), moduleOptions);
			} catch (EngineException e) {
				throw new ReportServiceException(e.getLocalizedMessage(), e.getCause());
			}
		}
		return runnable;
	}

	/**
	 * Transform TOC Node
	 *
	 * @param node
	 * @return
	 */
	private static ToC transformTOCNode(TOCNode node) {
		ToC toc = new ToC(node.getNodeID(), node.getDisplayString(), node.getBookmark(), BirtUtility.getTOCStyle(node));
		toc.setChildren(getToCChildren(node));
		return toc;
	}

	/**
	 * Returns the TOC Children from a TOCNode
	 *
	 * @param node
	 * @return
	 */
	private static List getToCChildren(TOCNode node) {
		if (node.getChildren() == null) {
			return null;
		}
		List children = node.getChildren();
		List ret = new ArrayList();
		Iterator it = children.iterator();
		while (it.hasNext()) {
			TOCNode childNode = (TOCNode) it.next();
			ToC child = new ToC(childNode.getNodeID(), childNode.getDisplayString(), childNode.getBookmark(),
					BirtUtility.getTOCStyle(childNode));
			// Recursion to transform all children etc...
			child.setChildren(getToCChildren(childNode));
			ret.add(child);
		}
		return ret;
	}

	/**
	 * Returns the GetParameterDefinitionTask
	 *
	 * @param reportDesignName
	 * @param options
	 * @return
	 * @throws EngineException
	 */
	private IGetParameterDefinitionTask getParameterDefinitionTask(String reportDesignName, InputOptions options)
			throws EngineException {

		IReportRunnable runnable = ReportEngineService.getInstance().openReportDesign(reportDesignName,
				getModuleOptions(options));
		IGetParameterDefinitionTask paramTask = ReportEngineService.getInstance()
				.createGetParameterDefinitionTask(runnable, options);
		return paramTask;
	}

	/**
	 * Handle ResultSet array
	 *
	 * @param resultSetArray
	 * @return
	 */
	private List transformResultSetArray(ResultSet[] resultSetArray) {
		List ret = new ArrayList();
		for (int i = 0; i < resultSetArray.length; i++) {
			ResultSet rs = resultSetArray[i];
			String queryName = rs.getQueryName();
			Column[] columnArray = rs.getColumn();
			List columns = new ArrayList();
			for (int j = 0; j < columnArray.length; j++) {
				Column column = columnArray[j];
				ExportedColumn exportedColumn = new ExportedColumn(column.getName(), column.getLabel(),
						column.getVisibility().booleanValue());
				columns.add(exportedColumn);
			}
			ExportedResultSet exportedResultSet = new ExportedResultSet(queryName, columns);
			ret.add(exportedResultSet);
		}
		return ret;
	}

	/**
	 * Gets the report design name from the input options.
	 *
	 * @param options the input options
	 * @return the report design name if the request contains a valid name,
	 *         otherwise null
	 */

	private String getReportDesignName(InputOptions options) {
		String reportDesignName = null;

		if (options != null) {
			HttpServletRequest request = (HttpServletRequest) options.getOption(InputOptions.OPT_REQUEST);
			if (request != null) {
				ViewerAttributeBean attrBean = (ViewerAttributeBean) request
						.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
				assert attrBean != null;

				reportDesignName = attrBean.getReportDesignName();
				if (reportDesignName != null) {
					// if the report design name is not a valid file, then set
					// it to null

					if (reportDesignName.endsWith("\\") //$NON-NLS-1$
							|| reportDesignName.endsWith("/")) {
						reportDesignName = null;
					}
				}
			}
		}
		return reportDesignName;
	}

	/**
	 * Convert engine parameters (IScalarParameterDefn and IParameterGroupDefn) into
	 * service api parameters (ParameterDefinition and ParameterGroupDefinition)
	 *
	 * @param params        a Collection of IScalarParameterDefn or
	 *                      IParameterGroupDefn
	 * @param includeGroups if true, include groups (ParameterGroupDefinition) in
	 *                      the result, otherwise flatten the result (i.e. include
	 *                      the contents of the groups in the result)
	 *
	 * @return a Collection of ParameterDefinition and ParameterGroupDefinition, or
	 *         a Collection of only ParameterDefinition if includeGroups == false
	 */
	private static Collection convertEngineParameters(Collection params, boolean includeGroups) {
		if (params == null) {
			return Collections.EMPTY_LIST;
		}
		List ret = new ArrayList();
		for (Iterator it = params.iterator(); it.hasNext();) {
			Object o = it.next();
			if (o instanceof IScalarParameterDefn) {
				IScalarParameterDefn engineParam = (IScalarParameterDefn) o;
				ParameterGroupDefinition group = null;
				ParameterDefinition param = convertScalarParameter(engineParam, group);

				ret.add(param);
			} else if (o instanceof IParameterGroupDefn) {
				IParameterGroupDefn engineParam = (IParameterGroupDefn) o;
				ParameterGroupDefinition paramGroup = convertParameterGroup(engineParam);
				ret.add(paramGroup);
			}
		}

		if (includeGroups) {
			return ret;
		}
		// If we are not including the groups, flatten the results
		return flattenGroups(ret);
	}

	/**
	 * Flatten the results
	 *
	 * @param params
	 */
	private static List flattenGroups(List params) {
		if (params == null || params.size() == 0) {
			return Collections.EMPTY_LIST;
		}
		List ret = new ArrayList();
		for (Iterator it = params.iterator(); it.hasNext();) {
			Object o = it.next();
			if (o instanceof ParameterGroupDefinition) {
				ParameterGroupDefinition group = (ParameterGroupDefinition) o;
				ret.addAll(group.getParameters());
			} else {
				ret.add(o);
			}
		}

		return ret;
	}

	/**
	 * Convert parameters in parameter group
	 *
	 * @param scalarParameters
	 * @param group
	 * @return
	 */
	private static List convertParametersInGroup(Collection scalarParameters, ParameterGroupDefinition group) {
		if (scalarParameters == null) {
			return null;
		}
		List ret = new ArrayList();
		for (Iterator it = scalarParameters.iterator(); it.hasNext();) {
			IScalarParameterDefn engineParam = (IScalarParameterDefn) it.next();
			ParameterDefinition param = convertScalarParameter(engineParam, group);
			ret.add(param);
		}
		return ret;
	}

	/**
	 * Convert Parameter Group Definition
	 *
	 * @param engineParam
	 * @return
	 */
	private static ParameterGroupDefinition convertParameterGroup(IParameterGroupDefn engineParam) {
		boolean cascade = engineParam instanceof ICascadingParameterGroup;
		String name = engineParam.getName();
		String displayName = engineParam.getDisplayName();
		String helpText = engineParam.getHelpText();
		String promptText = engineParam.getPromptText();
		ParameterGroupDefinition paramGroup = new ParameterGroupDefinition(name, displayName, promptText, null, cascade,
				helpText);
		List contents = convertParametersInGroup(engineParam.getContents(), paramGroup);
		paramGroup.setParameters(contents);

		return paramGroup;
	}

	/**
	 * Convert Scalar Parameter Definition
	 *
	 * @param engineParam
	 * @param group
	 * @return
	 */
	private static ParameterDefinition convertScalarParameter(IScalarParameterDefn engineParam,
			ParameterGroupDefinition group) {
		Object handle = engineParam.getHandle();
		ScalarParameterHandle scalarParamHandle = null;
		if (handle instanceof ScalarParameterHandle) {
			scalarParamHandle = (ScalarParameterHandle) handle;
		}
		String name = engineParam.getName();
		long id = scalarParamHandle != null ? scalarParamHandle.getID() : 0L;
		String pattern = scalarParamHandle == null ? "" //$NON-NLS-1$
				: scalarParamHandle.getPattern();
		String displayFormat = engineParam.getDisplayFormat();
		String displayName = engineParam.getDisplayName();
		String helpText = engineParam.getHelpText();
		String promptText = engineParam.getPromptText();
		int dataType = engineParam.getDataType();
		String valueExpr = scalarParamHandle == null ? null : scalarParamHandle.getValueExpr();
		int controlType = engineParam.getControlType();
		boolean hidden = engineParam.isHidden();
		boolean allowNull = !engineParam.isRequired();
		boolean allowBlank = (engineParam.getDataType() == IScalarParameterDefn.TYPE_STRING
				|| engineParam.getDataType() == IScalarParameterDefn.TYPE_ANY);
		boolean isRequired = engineParam.isRequired();
		boolean mustMatch = scalarParamHandle == null ? false : scalarParamHandle.isMustMatch();
		boolean concealValue = engineParam.isValueConcealed();
		boolean distinct = scalarParamHandle == null ? false : scalarParamHandle.distinct();
		boolean isMultiValue = false;
		if (scalarParamHandle != null) {
			isMultiValue = DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE
					.equalsIgnoreCase(scalarParamHandle.getParamType());
		}
		ParameterDefinition param = new ParameterDefinition(id, name, pattern, displayFormat, displayName, helpText,
				promptText, dataType, valueExpr, controlType, hidden, allowNull, allowBlank, isRequired, mustMatch,
				concealValue, distinct, isMultiValue, group, null);
		return param;
	}

	/**
	 * Convert collection of IParameterSelectionChoice to collection of
	 * ParameterSelectionChoice
	 *
	 * @param params
	 * @return
	 */
	private static Collection convertEngineParameterSelectionChoice(Collection params) {
		if (params == null) {
			return Collections.EMPTY_LIST;
		}
		List ret = new ArrayList();
		for (Iterator it = params.iterator(); it.hasNext();) {
			IParameterSelectionChoice engineChoice = (IParameterSelectionChoice) it.next();
			ParameterSelectionChoice paramChoice = new ParameterSelectionChoice(engineChoice.getLabel(),
					engineChoice.getValue());
			ret.add(paramChoice);
		}
		return ret;
	}

	/**
	 * Get Module options from ViewerAttributeBean
	 *
	 * @param options
	 * @return
	 */
	private Map getModuleOptions(InputOptions options) {
		ViewerAttributeBean bean = getViewerAttrBean(options);
		if (bean != null) {
			return bean.getModuleOptions();
		}
		return null;
	}

	/**
	 *
	 * Get ViewerAttributeBean from InputOptions
	 *
	 * @param options
	 * @return
	 */
	private ViewerAttributeBean getViewerAttrBean(InputOptions options) {
		if (options != null) {
			HttpServletRequest request = (HttpServletRequest) options.getOption(InputOptions.OPT_REQUEST);
			if (request != null) {
				return (ViewerAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
			}
		}
		return null;
	}
}
