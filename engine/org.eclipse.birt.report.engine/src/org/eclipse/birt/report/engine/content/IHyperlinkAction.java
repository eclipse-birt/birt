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

package org.eclipse.birt.report.engine.content;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Defines an interface that allows engine to pass hyperlink information to an
 * emitter, if the emitter determines to customize the hyperlinks calculated in
 * engine, or it wants to use a totally different hyperlink string
 * <p>
 * Because it is allowed to customize hyperlinks through emitters, hyperlink
 * customization in presentation engine itself is not supported now. It could be
 * added later if it deems necessary.
 * 
 */
public interface IHyperlinkAction
{

	/**
	 * hyperlink action
	 */
	public final static int ACTION_HYPERLINK = 1;
	/**
	 * bookmark action
	 */
	public final static int ACTION_BOOKMARK = 2;
	/**
	 * drillthrough action
	 */
	public final static int ACTION_DRILLTHROUGH = 3;

	/**
	 * @return the type of the hyperlink
	 */
	public int getType( );

	/**
	 * @return the bookmark string (not the bookmark expression) when action
	 *         type is bookmark or drillthrough, or null whe action type is
	 *         hyperlink.
	 */
	public String getBookmark( );

	/**
	 * @return the action string that is calculated using the engine's default
	 *         algorithm. valid for all three action types.
	 */
	public String getHyperlink( );

	/**
	 * @return the report name if action type is drillthrough, null otherwise
	 */
	public String getReportName( );

	/**
	 * @return a set of name/value pairs for running the report in a
	 *         drillthrough link; null when the action type is not drillthrough,
	 *         or no parameters are defined for the drillthrough report to run.
	 *         In the future, when the drillthrough is against a report
	 *         document, the parameter binding map is also null.
	 */
	public Map getParameterBindings( );

	/**
	 * @return a set of name/value pairs for searching the report in a
	 *         drillthrough link; null when the action type is not drillthrough,
	 *         or no search criteria is used
	 */
	public Map getSearchCriteria( );

	/**
	 * @return The name of a frame where a document is to be opened.
	 */
	public String getTargetWindow( );

	public void setHyperlink( String hyperlink, String target );

	public void setBookmark( String bookmark );

	public void setDrillThrough( String bookmark, String reportName,
			Map parameterBindings, Map searchCriteria, String target );

	void readContent( ObjectInputStream in ) throws IOException, ClassNotFoundException;

	void writeContent( ObjectOutputStream out ) throws IOException;
}