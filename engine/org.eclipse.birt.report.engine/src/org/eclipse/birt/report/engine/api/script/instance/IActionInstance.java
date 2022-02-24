
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.report.engine.api.script.instance;

import java.util.Map;

/**
 * 
 */

public interface IActionInstance {

	/**
	 * @return the type of the hyperlink. Can be one of the following:
	 *         org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_HYPERLINK
	 *         org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_BOOKMARK
	 *         org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_DRILLTHROUGH
	 *         ACTION NULL: -1
	 */
	int getType();

	/**
	 * @return the bookmark string when action type is bookmark. Otherwise, throw
	 *         RuntimeException.
	 */
	String getBookmark();

	/**
	 * Set bookmark. And set the type be
	 * org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_BOOKMARK Throw
	 * IllegalArgumentException if the bookmark be set to null.
	 * 
	 * @param bookmark
	 */
	void setBookmark(String bookmark);

	/**
	 * @return the action string when action type is hyperlink. Otherwise, throw
	 *         RuntimeException.
	 */
	String getHyperlink();

	/**
	 * Set hyperlink string and target. And set the type be
	 * org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_HYPERLINK
	 * Throw IllegalArgumentException if the hyperlink be set to null.
	 * 
	 * @param hyperlink
	 * @param target
	 */
	void setHyperlink(String hyperlink, String target);

	/**
	 * @return The name of a frame where a target hyperlink or drillThrough to be
	 *         opened.
	 */
	String getTargetWindow();

	/**
	 * @deprecated Create a drillThrough instance. And set the parameters of the
	 *             drillthrough:
	 * @param bookmark
	 * @param isBookmark
	 * @param reportName
	 * @param parameterBindings
	 * @param searchCriteria
	 * @param target
	 * @param format
	 */
	IDrillThroughInstance createDrillThrough(String bookmark, boolean isBookmark, String reportName,
			Map parameterBindings, Map searchCriteria, String target, String format);

	/**
	 * Create a drillThrough instance. And set the parameters of the drillthrough:
	 * 
	 * @param bookmark
	 * @param isBookmark
	 * @param reportName
	 * @param parameterBindings
	 * @param searchCriteria
	 * @param target
	 * @param format
	 * @param targetFileType
	 */
	IDrillThroughInstance createDrillThrough(String bookmark, boolean isBookmark, String reportName,
			Map parameterBindings, Map searchCriteria, String target, String format, String targetFileType);

	/**
	 * create a empty drillThrough instance.
	 * 
	 * @return
	 */
	IDrillThroughInstance createDrillThrough();

	/**
	 * @return the drillThrouthInstance when action type is drillThrough. Otherwise,
	 *         throw RuntimeException.
	 */
	IDrillThroughInstance getDrillThrough();

	/**
	 * Set IDrillThroughInstance. And set the type be
	 * org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_DRILLTHROUGH
	 * Throw IllegalArgumentException if the drillThroughInstance be set to null.
	 * 
	 * @param drillThroughInstance
	 */
	void setDrillThrough(IDrillThroughInstance drillThroughInstance);

	void setTooltip(String tooltip);

	String getTooltip();

}
