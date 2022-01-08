/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script.instance;

public interface IRowInstance extends IReportElementInstance {
	/**
	 * Get the style of the row
	 * 
	 * @return style of the row
	 */
	IScriptStyle getStyle();

	/**
	 * Get the value of bookmark
	 * 
	 * @return value of bookmark
	 */
	String getBookmarkValue();

	/**
	 * Set the bookmark
	 * 
	 * @param bookmark
	 */
	void setBookmark(String bookmark);

	/**
	 * Get the height of the row
	 * 
	 * @return the height of the row
	 */
	String getHeight();

	/**
	 * Set the height of the row
	 * 
	 * @param height
	 */
	void setHeight(String height);

	/*
	 * IRowData getRowData( );
	 */

}
