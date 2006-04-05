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

/**
 * A report document (i.e., not modifiable) that can be rendered to other
 * formats in the BIRT presentation engine
 * 
 * This is the high-level report document interface. 
 */
public interface IReportDocument
{
	public static int GLOBAL_VARIABLE_OPTION_ALL = 0;
	public static int GLOBAL_VARIABLE_OPTION_JAVA_ONLY = 1;
	public static int GLOBAL_VARIABLE_OPTION_JS_ONLY = 2;

	public abstract IDocArchiveReader getArchive( );
	
	/**
	 * close the report document, rlease all resources.
	 */
	public abstract void close();

	public String getVersion();
	
	/**
	 * @return the report document (archive) name
	 */
	public abstract String getName( );

	/**
	 * @return a report design stream. This is useful for rerunning a report
	 *         based on report document
	 */
	public abstract InputStream getDesignStream( );

	/**
	 * The report runnable is used to create the report document while writing.
	 * If the report document is open with, the report runnable is used to
	 * render or extract data from the report document.
	 * 
	 * @return the runnable report design. It is available because a report
	 *         document must be run with a report design
	 */
	public abstract IReportRunnable getReportRunnable( );

	/**
	 * returns values for all the parameters that are used for generating the
	 * current report document. Useful for running the report again based on a
	 * report document
	 * 
	 * @return parameter name/value pairs for generating the current report
	 *         document.
	 */
	public abstract Map getParameterValues( );

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
	 * Given a report item instance idD, returns the offset of the report content
	 * (to support Reportlet).
	 * 
	 * @param iid
	 *            report item instance id
	 * @return the offset in the content stream
	 */
	public long getInstanceOffset(InstanceID iid);
	
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
	 */
	public abstract List getChildren( String tocNodeId );

	/**
	 * get the TOCNode have the id.
	 * 
	 * @param tocNodeId
	 *            the id of the toc.
	 * @return TOCNode with sucn an Id. NULL if not founded.
	 */
	public abstract TOCNode findTOC( String tocNodeId );
	
	/**
	 * Gets the TOCNodes with the given name.
	 * 
	 * @param tocName
	 *            the name of the toc.
	 * @return List of all tocs with the name.
	 */
	public abstract List findTOCByName( String tocName );
	
	/**
	 * @return a map for all the global variables defined in JavaScript or Java
	 */
	public abstract Map getGlobalVariables( String option);
}
