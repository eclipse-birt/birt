/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

/**
 * An engine task that renders a Report Document to one of the output formats
 * supported by the engine.
 */
public interface IRenderTask extends IEngineTask {
	/**
	 * set the rendering options
	 *
	 * @param settings the rendering options
	 */
	void setRenderOption(IRenderOption options);

	/**
	 * @return the render option
	 */
	IRenderOption getRenderOption();

	/**
	 * sets a specific emitter to use when generate output. Used when there are more
	 * than one emitters that support a single format. One example is the FO-based
	 * PDF emitter and the new PDF emitter added in BIRT2.0. If this function is not
	 * called when there are more than 1 emitters that support a format, engine may
	 * arbitrarily pick one.
	 *
	 * @param id the identifier for the emitter
	 */
	void setEmitterID(String id);

	/**
	 * Sets number of the page to be rendered.
	 *
	 * @param pageNumber number of the page.
	 * @throws EngineException if <code>pageNumber</code> is invalid.
	 */
	void setPageNumber(long pageNumber) throws EngineException;

	/**
	 * Sets id of instance. If instance id is set, render method will render the
	 * page which contains this instance.
	 *
	 * @param iid id of the instance.
	 * @throws EngineException if <code>iid</code> is invalid.
	 */
	void setInstanceID(InstanceID iid) throws EngineException;

	/**
	 * Sets id of instance which is a string type. If instance id is set, render
	 * method will render the page which contains this instance.
	 *
	 * @param iid the string type instance id of the instance.
	 * @throws EngineException if <code>iid</code> is invalid.
	 */
	void setInstanceID(String iid) throws EngineException;

	/**
	 * Sets range of the pages to be rendered.
	 *
	 * @param pageRange range of the pages.
	 * @throws EngineException if <code>pageRange</code> is invalid.
	 */
	void setPageRange(String pageRange) throws EngineException;

	/**
	 * Sets bookmark. If bookmark is set, render method will render the page which
	 * contains this bookmark.
	 *
	 * @param bookmark the bookmark.
	 * @throws EngineException if <code>bookmark</code> is invalid.
	 */
	void setBookmark(String bookmark) throws EngineException;

	/**
	 * Sets reportlet by bookmark. The reportlet represented by the bookmark will be
	 * render.
	 *
	 * @param bookmark the bookmark.
	 * @throws EngineException if <code>bookmark</code> is invalid.
	 */
	void setReportlet(String bookmark) throws EngineException;

	/**
	 * render the whole report document or an output format
	 *
	 * @throws EngineException if rendering fails
	 */
	void render() throws EngineException;

	/**
	 * @param pageNumber
	 * @throws EngineException
	 * @deprecated A page with speicfic page number can be rendered like this:<br>
	 *             &nbsp;&nbsp;&nbsp;&nbsp;<code>setPageNumber( pageNumber );</code><br>
	 *             &nbsp;&nbsp;&nbsp;&nbsp;<code>render( );</code>
	 */
	@Deprecated
	void render(long pageNumber) throws EngineException;

	/**
	 * Render the page from startPageNumber to endPageNumber in the Report Doucment
	 * to an output format.
	 *
	 * @throws EngineException
	 * @deprecated A range of pages can be rendered like this:<br>
	 *             &nbsp;&nbsp;&nbsp;&nbsp;<code>setPageRange( pageRange );</code><br>
	 *             &nbsp;&nbsp;&nbsp;&nbsp;<code>render( );</code>
	 */
	@Deprecated
	void render(String pageRange) throws EngineException;

	/**
	 * Render the Reportlet whose container is identified by iid. Useful for
	 * Reportlet support
	 *
	 * @param itemInstanceID the report iteminstance to be rendered
	 * @throws EngineException
	 * @deprecated A page which contains the instance can be rendered like this:<br>
	 *             &nbsp;&nbsp;&nbsp;&nbsp;<code>setInstanceID( instanceID );</code><br>
	 *             &nbsp;&nbsp;&nbsp;&nbsp;<code>render( );</code>
	 */
	@Deprecated
	void render(InstanceID iid) throws EngineException;

	/**
	 * Gets count of the pages that is output. This method can only be invoked after
	 * render task is finished otherwise an engine exception will be thrown.
	 */
	long getPageCount() throws EngineException;

	/**
	 * @return the visible page count in the report.
	 */
	long getTotalPage() throws EngineException;

	/**
	 * Given a bookmark in a report, find the (first) page that the bookmark appears
	 * in (for hyperlinks to a bookmark)
	 *
	 * @param bookmarkName bookmark name
	 * @return the page number that the instance appears first
	 */
	long getPageNumber(String bookmark) throws EngineException;

	/**
	 * Get the TOC tree
	 *
	 * @param format the format to generate the report
	 * @param locale the locale information to generate the report
	 */
	ITOCTree getTOCTree() throws EngineException;

	/**
	 * set up event handler to be called after each page is generated
	 *
	 * @param callback a callback function that is called after each checkpoint
	 */
	void setPageHandler(IPageHandler callback);
}
