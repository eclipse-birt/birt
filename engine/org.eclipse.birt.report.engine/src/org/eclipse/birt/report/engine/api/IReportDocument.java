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

package org.eclipse.birt.report.engine.api;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * A report document (i.e., not modifiable) that can be rendered to other
 * formats in the BIRT presentation engine
 * 
 * This is the high-level report document interface. 
 */
public interface IReportDocument extends IDocument
{
	public static int GLOBAL_VARIABLE_OPTION_ALL = 0;
	public static int GLOBAL_VARIABLE_OPTION_JAVA_ONLY = 1;
	public static int GLOBAL_VARIABLE_OPTION_JS_ONLY = 2;

	public abstract IDocArchiveReader getArchive( );
	
	/**
	 * Get report document version.
	 * 
	 * @return version
	 */
	public String getVersion();
	
	/**
	 * Get report document property.
	 * 
	 * @param key
	 * @return property
	 */
	public String getProperty( String key );

	/**
	 * @return a report design stream. This is useful for rerunning a report
	 *         based on report document
	 */
	public abstract InputStream getDesignStream( );
	
	/**
	 * 
	 * @return the report design handle. user always get a new instance from this interface.
	 */
	public abstract ReportDesignHandle getReportDesign( );

	/**
	 * The report runnable is used to create the report document while writing.
	 * If the report document is open with, the report runnable is used to
	 * render or extract data from the report document.
	 * user always get a new instance from this interface.
	 * 
	 * @return the runnable report design. It is available because a report
	 *         document must be run with a report design
	 */
	public abstract IReportRunnable getReportRunnable( );
	
	/**
	 * The report runnable is used to create the report document while writing.
	 * If the report document is open with, the report runnable is used to
	 * render or extract data from the report document.
	 * user always get a new instance from this interface.
	 * 
	 * @return the prepared runnable report design. It is available because a report
	 *         document must be run with a report design
	 */
	public abstract IReportRunnable getPreparedRunnable();
	
	/**
	 * The report runnable is used to create the report document while writing.
	 * If the report document is open with, the report runnable is used to
	 * render or extract data from the report document.
	 * The internal instance is returned and user should not modify it.
	 * 
	 * @return the prepared runnable report design. It is available because a report
	 *         document must be run with a report design
	 */
	public abstract IReportRunnable getDocumentRunnable();

	/**
	 * returns values for all the parameters that are used for generating the
	 * current report document. Useful for running the report again based on a
	 * report document
	 * 
	 * @return parameter name/value pairs for generating the current report
	 *         document.
	 * @deprecated Invoke RenderTask.getParameterValues( ) instead.
	 */
	public abstract Map getParameterValues( );
	
	/**
	 * returns display texts for all the parameters that are used for generating the
	 * current report document. Useful for running the report again based on a
	 * report document
	 * 
	 * @return parameter name/display text pairs for generating the current report
	 *         document.
	 */	
	public abstract Map getParameterDisplayTexts( );

	/**
	 * @return the page count in the report. Used for supporting page-based
	 *         viewing
	 */
	public abstract long getPageCount( );

	/**
	 * Given a report item instance idD, returns the page number that the
	 * instance starts on (to support Reportlet).
	 * 
	 * @param iid
	 *            report item instance id
	 * @return the page number that the instance appears first
	 */
	public long getPageNumber( InstanceID iid );

	/**
	 * Given a report item instance id, returns the offset of the report content
	 * (to support Reportlet).
	 * 
	 * @param iid
	 *            report item instance id
	 * @return the offset in the content stream
	 */
	public long getInstanceOffset(InstanceID iid);
	
	/**
	 * Given a report item bookmark, returns the offset of the report content
	 * (to support Reportlet).
	 * 
	 * @param bookmark
	 *            bookmark of the report item.
	 * @return the offset in the content stream
	 */
	public long getBookmarkOffset(String bookmark);
	
	/**
	 * Given a bookmark in a report, find the (first) page that the bookmark
	 * appears in (for hyperlinks to a bookmark)
	 * 
	 * @param bookmarkName
	 *            bookmark name
	 * @return the page number that the instance appears first
	 */
	public abstract long getPageNumber( String bookmark );

	/**
	 * @return a list of bookmark strings
	 */
	public abstract List getBookmarks( );

	/**
	 * @param tocNodeId
	 *            the id of the parent TOC node. Pass null as the root TOC node
	 * @return A list of TOC nodes thata re direct child of the parent node
	 * @deprecated Invoke following code instead:
	 * <code><pre>
	 *     IReportDocument document = ...
	 *     RenderTask renderTask = engine.createRenderTask( document );
	 *     ITOCTree tocTree = renderTask.getTOCTree( );
	 *     TOCNode node = tocTree.findTOC(tocNodeId);
	 *     List result = node.getChildren( );
	 * </pre></code>
	 */
	public abstract List getChildren( String tocNodeId );

	/**
	 * get the TOCNode have the id.
	 * 
	 * @param tocNodeId
	 *            the id of the toc.
	 * @return TOCNode with such an Id. NULL if not founded.
	 * @deprecated Invoke following code instead:
	 * <code><pre>
	 *     IReportDocument document = ...
	 *     RenderTask renderTask = engine.createRenderTask( document );
	 *     ITOCTree tocTree = renderTask.getTOCTree( );
	 *     TOCNode result = tocTree.findTOC(tocNodeId);
	 * </pre></code>
	 */
	public abstract TOCNode findTOC( String tocNodeId );
	
	/**
	 * Gets the TOCNodes with the given name.
	 * 
	 * @param tocName
	 *            the name of the toc.
	 * @return List of all tocs with the specified name.
	 * @deprecated Invoke following code instead:
	 * <code><pre>
	 *     IReportDocument document = ...
	 *     RenderTask renderTask = engine.createRenderTask( document );
	 *     ITOCTree tocTree = renderTask.getTOCTree( );
	 *     List result = tocTree.findTOCByValue(tocName);
	 * </pre></code>
	 */
	public abstract List findTOCByName( String tocName );
	
	/**
	 * @return a map for all the global variables defined in JavaScript or Java
	 * @deprecated
	 */
	public abstract Map getGlobalVariables( String option);
	
	/**
	 * @return whether the document has all been written.
	 */
	public boolean isComplete( );
	
	/**
	 * check the current readed checkpoint and the current writed check point.
	 * if equal, do nothing.
	 * otherwise, reload the core stream, the checkpoint and page count.
	 */
	public void refresh( );

	/**
	 * Get the TOC tree
	 * 
	 * @param format
	 *            the format to generate the report
	 * @param locale
	 *            the locale information to generate the report
	 * @deprecated Invoke following code instead:
	 * <code><pre>
	 *     IReportDocument document = ...
	 *     RenderTask renderTask = engine.createRenderTask( document );
	 *     ITOCTree tocTree = renderTask.getTOCTree( );
	 *     ITOCTree result = new TOCView( tocTree.getRoot( ), document.getReportDesgin( ), locale,
	 *	                     TimeZone.getDefault( ), format );
	 * </pre></code>
	 */
	ITOCTree getTOCTree( String format, ULocale locale );
	
	/**
	 * Get the TOC tree
	 * 
	 * @param format
	 *            the format to generate the report
	 * @param locale
	 *            the locale information to generate the report
	 * @param timeZone
	 *            the time zone information to generate the report
	 * @deprecated Invoke following code instead:
	 * <code><pre>
	 *     IReportDocument document = ...
	 *     RenderTask renderTask = engine.createRenderTask( document );
	 *     ITOCTree tocTree = renderTask.getTOCTree( );
	 *     ITOCTree result = new TOCView( tocTree.getRoot( ), document.getReportDesgin( ), locale,
	 *	                     timeZone, format );
	 * </pre></code>
	 */
	ITOCTree getTOCTree(String format, ULocale locale, TimeZone timeZone);

	/**
	 * Return the instance id of report item with the specified bookmark
	 * @param bookmark the bookmark of the report item
	 * @return the instance id of the report item
	 */
	InstanceID getBookmarkInstance( String bookmark );
	
	/**
	 * return the system id assigned to the document
	 * @return the system id
	 */
	String getSystemId( );
	
	/**
	 * return the errors in the document. The errors are recorded
	 * during document generation phase.
	 * 
	 * @return the error list.
	 */
	List<String> getDocumentErrors( );
}
