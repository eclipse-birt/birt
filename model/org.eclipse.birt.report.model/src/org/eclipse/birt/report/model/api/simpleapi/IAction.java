/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;

/**
 * Script wrapper of <code>ActionHandle</code>
 * 
 */

public interface IAction {

	/**
	 * Gets the identifier of the hyperlink if the link type is
	 * <code>ACTION_LINK_TYPE_HYPERLINK</code>. Otherwise, return null.
	 * 
	 * @return the URI link expression in a string
	 */

	public String getURI();

	/**
	 * Gets the name of the target browser window for the link. (Optional.) Used
	 * only for the Hyperlink and Drill Through options. Otherwise, return null.
	 * 
	 * @return the window name
	 */

	public String getTargetWindow();

	/**
	 * Gets the link type of the action. The link type are defined in
	 * DesignChoiceConstants and can be one of the following:
	 * <p>
	 * <ul>
	 * <li><code>ACTION_LINK_TYPE_NONE</code>
	 * <li><code>ACTION_LINK_TYPE_HYPERLINK</code>
	 * <li><code>ACTION_LINK_TYPE_DRILLTHROUGH</code>
	 * <li><code>ACTION_LINK_TYPE_BOOKMARK_LINK</code>
	 * </ul>
	 * 
	 * @return the string value of the link type
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getLinkType();

	/**
	 * Sets the link type of the action. The link type are defined in
	 * DesignChoiceConstants and can be one of the following:
	 * <p>
	 * <ul>
	 * <li><code>ACTION_LINK_TYPE_NONE</code>
	 * <li><code>ACTION_LINK_TYPE_HYPERLINK</code>
	 * <li><code>ACTION_LINK_TYPE_DRILLTHROUGH</code>
	 * <li><code>ACTION_LINK_TYPE_BOOKMARK_LINK</code>
	 * </ul>
	 * 
	 * @param type type of the action.
	 * @throws SemanticException if the <code>type</code> is not one of the above.
	 */

	public void setLinkType(String type) throws SemanticException;

	/**
	 * Sets the format type of the action. The format type for action are defined in
	 * DesignChoiceConstants and can be one of the following: *
	 * <p>
	 * <ul>
	 * <li><code>ACTION_FORMAT_TYPE_HTML</code>
	 * <li><code>ACTION_FORMAT_TYPE_PDF</code>
	 * </ul>
	 * 
	 * @param type the type of the action
	 * @throws SemanticException
	 */
	public void setFormatType(String type) throws SemanticException;

	/**
	 * Gets the format type of the action. The format type for action are defined in
	 * DesignChoiceConstants and can be one of the following: *
	 * <p>
	 * <ul>
	 * <li><code>ACTION_FORMAT_TYPE_HTML</code>
	 * <li><code>ACTION_FORMAT_TYPE_PDF</code>
	 * </ul>
	 * 
	 * @return the format type of the action
	 */
	public String getFormatType();

	/**
	 * Sets the target window of the action.
	 * 
	 * @param window the target window name
	 * @throws SemanticException if this property is locked.
	 */

	public void setTargetWindow(String window) throws SemanticException;

	/**
	 * 
	 * Sets the hyperlink of this action. The link type will be changed to
	 * <code>ACTION_LINK_TYPE_HYPERLINK</code>.
	 * 
	 * @param uri the hyperlink to set
	 * @throws SemanticException if the property is locked.
	 */

	public void setURI(String uri) throws SemanticException;

	/**
	 * Gets the name of the target report document if the link type is
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. Otherwise, return null.
	 * 
	 * @return the name of the target report document
	 * @see #setReportName(String)
	 */

	public String getReportName();

	/**
	 * Sets target report name for a drill-though link. The link type will be
	 * changed to <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. The report name can
	 * include relative or absolute names. If the suffix is omitted, it is computed
	 * on the server by looking for a matching report. BIRT reports are searched in
	 * the following order: 1) a BIRT report document or 2) a BIRT report design.
	 * 
	 * @param reportName the name of the target report
	 * @throws SemanticException if the property is locked.
	 * @see #getReportName()
	 */

	public void setReportName(String reportName) throws SemanticException;

	/**
	 * Gets the bookmark link if the link type is
	 * <code>ACTION_LINK_TYPE_BOOKMARK_LINK</code>. Otherwise, return null.
	 * 
	 * @return the bookmark link
	 */

	public String getTargetBookmark();

	/**
	 * Sets the target bookmark defined within this same report, or another report
	 * for a drill-though link. Call {@link #setLinkType(String)}to do the link type
	 * change, it can either be <code>ACTION_LINK_TYPE_DRILLTHROUGH</code> or
	 * <code>ACTION_LINK_TYPE_BOOKMARK_LINK</code>.
	 * 
	 * 
	 * @param bookmark the bookmark value.
	 * @throws SemanticException if the property is locked.
	 * @see #getTargetBookmark()
	 */

	public void setTargetBookmark(String bookmark) throws SemanticException;

	/**
	 * Gets the internal structure instance of this action.
	 * 
	 * @return Action structure instance.
	 */
	public IStructure getStructure();

}
