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

package org.eclipse.birt.report.service.api;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A service used by the viewer for running and rendering a report
 * 
 */
public interface IViewerReportService
{

	/**
	 * Set the context
	 * 
	 * @param context
	 * @param options
	 */
	void setContext( Object context, InputOptions options );

	/**
	 * Run a report
	 * 
	 * @param design
	 * @param outputDocName
	 * @param runOptions
	 * @param parameters,
	 *            a map of unparsed parameters (i.e. name (String) -> value
	 *            (String))
	 * @return
	 * @throws ReportServiceException
	 */
	String runReport( IViewerReportDesignHandle design, String outputDocName,
			InputOptions runOptions, Map parameters )
			throws ReportServiceException;

	/**
	 * Run a report
	 * 
	 * @param design
	 * @param outputDocName
	 * @param runOptions
	 * @param parameters,
	 *            a map of unparsed parameters (i.e. name (String) -> value
	 *            (String))
	 * @param displayTexts
	 * @return
	 * @throws ReportServiceException
	 */
	String runReport( IViewerReportDesignHandle design, String outputDocName,
			InputOptions runOptions, Map parameters, Map displayTexts )
			throws ReportServiceException;

	/**
	 * Get the report design handle from the document
	 * 
	 * @param docName
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	IViewerReportDesignHandle getReportDesignHandle( String docName,
			InputOptions options ) throws ReportServiceException;

	/**
	 * Get a page
	 * 
	 * @param docName
	 * @param pageID
	 * @param renderOptions
	 * @param activeIds
	 * @return
	 * @throws ReportServiceException
	 */
	ByteArrayOutputStream getPage( String docName, String pageID,
			InputOptions renderOptions, List activeIds )
			throws ReportServiceException;

	/**
	 * Get the first page containing the bookmark
	 * 
	 * @param docName
	 * @param bookmark
	 * @param renderOptions
	 * @param activeIds
	 * @return
	 * @throws ReportServiceException
	 */
	ByteArrayOutputStream getPageByBookmark( String docName, String bookmark,
			InputOptions renderOptions, List activeIds )
			throws ReportServiceException;

	/**
	 * Get the first page containing the object id
	 * 
	 * @param docName
	 * @param objectId
	 * @param renderOptions
	 * @param activeIds
	 * @return
	 * @throws ReportServiceException
	 */
	ByteArrayOutputStream getPageByObjectId( String docName, String objectId,
			InputOptions renderOptions, List activeIds )
			throws ReportServiceException;

	/**
	 * Render a reportlet. This is similar to getPageByObjectId. The difference
	 * is that while getPageByObjectId returns the first page containing the
	 * object, this method return all pages containing the object.
	 * 
	 * @param docName
	 * @param objectId
	 * @param renderOptions
	 * @param activeIds
	 * @return
	 * @throws ReportServiceException
	 */
	void renderReportlet( String docName, String objectId,
			InputOptions renderOptions, List activeIds, OutputStream out )
			throws ReportServiceException;

	/**
	 * Render the whole report to the OutputStream
	 * 
	 * @param docName
	 * @param pageRange
	 * @param renderOptions
	 * @param out
	 * @throws ReportServiceException
	 */
	void renderReport( String docName, String pageRange,
			InputOptions renderOptions, OutputStream out )
			throws ReportServiceException;

	/**
	 * Run a report, then render it to the OutputStream
	 * 
	 * @param design
	 * @param outputDocName
	 * @param options
	 * @param parameters,
	 *            a map of unparsed parameters (i.e. name (String) -> value
	 *            (String))
	 * @param out
	 * @param activeIds
	 * @throws ReportServiceException
	 */
	void runAndRenderReport( IViewerReportDesignHandle design,
			String outputDocName, InputOptions options, Map parameters,
			OutputStream out, List activeIds ) throws ReportServiceException;

	/**
	 * Run a report, then render it to the OutputStream
	 * 
	 * @param design
	 * @param outputDocName
	 * @param options
	 * @param parameters,
	 *            a map of unparsed parameters (i.e. name (String) -> value
	 *            (String))
	 * @param out
	 * @param activeIds
	 * @param displayTexts
	 * @throws ReportServiceException
	 */
	void runAndRenderReport( IViewerReportDesignHandle design,
			String outputDocName, InputOptions options, Map parameters,
			OutputStream out, List activeIds, Map displayTexts )
			throws ReportServiceException;

	/**
	 * Extract a result set
	 * 
	 * @param docName
	 * @param resultSetId
	 * @param columns,
	 *            a set of column names (String)
	 * @param filters
	 * @param options
	 * @param out
	 * @throws ReportServiceException
	 */
	void extractResultSet( String docName, String resultSetId,
			Collection columns, Set filters, InputOptions options,
			OutputStream out ) throws ReportServiceException;

	/**
	 * Get the metadata for the result sets
	 * 
	 * @param docName
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	List getResultSetsMetadata( String docName, InputOptions options )
			throws ReportServiceException;

	/**
	 * 
	 * @param docName
	 * @param instanceId
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	List getResultSetsMetadata( String docName, String instanceId,
			InputOptions options ) throws ReportServiceException;

	/**
	 * Render an image to the OutputStream
	 * 
	 * @param docName
	 * @param imageId
	 * @param out
	 * @param options
	 * @throws ReportServiceException
	 */
	void getImage( String docName, String imageId, OutputStream out,
			InputOptions options ) throws ReportServiceException;

	/**
	 * Get a toc node TODO: This method will be changed to return a TOCNode
	 * (engineapi)
	 * 
	 * @param docName
	 * @param tocId
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	ToC getTOC( String docName, String tocId, InputOptions options )
			throws ReportServiceException;

	/**
	 * Get total page count
	 * 
	 * @param docName
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	long getPageCount( String docName, InputOptions options,
			OutputOptions outputOptions ) throws ReportServiceException;

	/**
	 * Get the parameter definitions
	 * 
	 * @param design
	 * @param runOptions
	 * @return a Collection of ScalarParameter
	 * @throws ReportServiceException
	 */
	Collection getParameterDefinitions( IViewerReportDesignHandle design,
			InputOptions runOptions, boolean includeGroups )
			throws ReportServiceException;

	/**
	 * Get a map of parameter values (name (String) -> value (Object))
	 * 
	 * @param docName
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	Map getParameterValues( String docName, InputOptions options )
			throws ReportServiceException;

	/**
	 * Get parameter selection list for a cascading parameter group
	 * 
	 * @param design
	 * @param groupName
	 * @param groupKeys
	 * @param options
	 * @return a Collection of ParameterSelectionChoice
	 * @throws ReportServiceException
	 */
	Collection getSelectionListForCascadingGroup(
			IViewerReportDesignHandle design, String groupName,
			Object[] groupKeys, InputOptions options )
			throws ReportServiceException;

	/**
	 * Get parameter selection list
	 * 
	 * @param design
	 * @param options
	 * @param paramName
	 * @return a Collection of IParameterSelectionChoice
	 * @throws ReportServiceException
	 */
	Collection getParameterSelectionList( IViewerReportDesignHandle design,
			InputOptions options, String paramName )
			throws ReportServiceException;

	/**
	 * Get the default value for a parameter
	 * 
	 * @param design
	 * @param parameterName
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	Object getParameterDefaultValue( IViewerReportDesignHandle design,
			String parameterName, InputOptions options )
			throws ReportServiceException;

	/**
	 * Get the page number of the first page containing the bookmark
	 * 
	 * @param docName
	 * @param bookmark
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	long getPageNumberByBookmark( String docName, String bookmark,
			InputOptions options ) throws ReportServiceException;

	/**
	 * Get the page number of the first page containing the object id
	 * 
	 * @param docName
	 * @param objectId
	 * @param options
	 * @return
	 * @throws ReportServiceException
	 */
	long getPageNumberByObjectId( String docName, String objectId,
			InputOptions options ) throws ReportServiceException;

	/**
	 * Gets the toc id by the toc name.
	 * 
	 * @param docName
	 * @param name
	 * @param options
	 * @return
	 */

	public String findTocByName( String docName, String name,
			InputOptions options );

}
