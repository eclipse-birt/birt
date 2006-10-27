
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.content;

import java.util.Map;

/**
 * 
 */

public interface IDrillThroughAction
{
	/**
	 * @return the report name if action type is drillthrough.
	 */
	String getReportName( );
	
	/**
	 * If action type is drillthrough, set the report name.
	 * @param reportName
	 */
	void setReportName( String reportName );
	
	/**
	 * @return a set of name/value pairs for running the report in a
	 *         drillthrough link. 
	 */
	Map getParameterBindings( );

	/**
	 * @return a set of name/value pairs for searching the report in a
	 *         drillthrough link.
	 */
	Map getSearchCriteria( );

	/**
	 * @return the format of output report if action type is drillthrough. 
	 * 
	 */
	String getFormat( );
	
	/**
	 * @return the bookmark type set in the drillThrough if action type is drillthrough. 
	 *         True, the bookmark is a bookmark.
	 *         False, the bookmark is indicated to be a toc.  
	 */
	boolean isBookmark();
	
	/**
	 * Set the bookmark of this drillThrough if action type is drillthrough. 
	 * @param bookmark
	 */
	void setBookmark( String bookmark );
	
	/**
	 * @return the bookmark string if the bookmark type is Bookmark and action type is drillthrough.
	 *         NULL if  the bookmark type is TOC and action type is drillthrough.
	 */
	String getBookmark( );	
		
	/**
	 * @return the targetWindow string if action type is drillthrough.
	 */
	String getTargetWindow( );

	void setBookmarkType( boolean isBookmark );

	void setParameterBindings( Map parameterBindings );

	void setSearchCriteria( Map searchCriteria );
	
	void setTargetWindow( String target );

	void setFormat( String format );
}
