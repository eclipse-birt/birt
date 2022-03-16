/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.SearchKey;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.StructureContext;

/**
 * Represents an "action" (hyperlink) attached to an element. Obtain an instance
 * of this class by calling the <code>getActionHandle</code> method on the
 * handle of an element that defines an action.
 * <p>
 * The link type of an Action can be only one of hyperlink, bookmark Link or
 * drill-through.
 * <ul>
 * <li>The hyperlink property returns a standard web-style link with "http:" or
 * "mailto:" prefix.
 * <li>The bookmark link simply identifies a bookmark identified within this
 * report.
 * <li>The drill-though link runs and/or views another report. A drill-through
 * action can include parameters (used when the hyperlink is used to run a
 * report), search keys (an optional list of search criteria) and a bookmark
 * destination within the target report.
 * </ul>
 *
 *
 * @see DataItemHandle#getActionHandle()
 * @see ImageHandle#getActionHandle()
 * @see LabelHandle#getActionHandle()
 *
 * @see org.eclipse.birt.report.model.api.elements.structures.Action
 */

public class ActionHandle extends StructureHandle {

	/**
	 * Construct an handle to deal with the action structure.
	 *
	 * @param element the element that defined the action.
	 * @param context reference to the action property.
	 */

	public ActionHandle(DesignElementHandle element, StructureContext context) {
		super(element, context);
	}

	/**
	 * Constructs the handle of action.
	 *
	 * @param valueHandle the value handle for action list of one property
	 * @param index       the position of this action in the list
	 */

	public ActionHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Construct an handle to deal with the action structure.
	 *
	 * @param element the element that defined the action.
	 * @param ref     reference to the action property.
	 * @deprecated
	 */

	@Deprecated
	public ActionHandle(DesignElementHandle element, MemberRef ref) {
		super(element, ref);
	}

	/**
	 * Gets the hyperlink if the link type is
	 * <code>ACTION_LINK_TYPE_HYPERLINK</code>. Otherwise, return null.
	 *
	 * @return the link expression in a string
	 * @deprecated use {@link #getURI()}instead.
	 */

	@Deprecated
	public String getHyperlink() {
		return getURI();
	}

	/**
	 * Gets the identifier of the hyperlink if the link type is
	 * <code>ACTION_LINK_TYPE_HYPERLINK</code>. Otherwise, return null.
	 *
	 * @return the URI link expression in a string
	 */

	public String getURI() {
		if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equalsIgnoreCase(getLinkType())) {
			return getStringProperty(Action.URI_MEMBER);
		}

		return null;
	}

	/**
	 * Gets the name of the target browser window for the link. (Optional.) Used
	 * only for the Hyperlink and Drill Through options. Otherwise, return null.
	 *
	 * @return the window name
	 */

	public String getTargetWindow() {
		if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equalsIgnoreCase(getLinkType())
				|| DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equalsIgnoreCase(getLinkType())) {
			return getStringProperty(Action.TARGET_WINDOW_MEMBER);
		}

		return null;
	}

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

	public String getLinkType() {
		return getStringProperty(Action.LINK_TYPE_MEMBER);
	}

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

	public void setLinkType(String type) throws SemanticException {
		setProperty(Action.LINK_TYPE_MEMBER, type);
	}

	/**
	 * Sets the format type of the action. The format type for action are defined in
	 * DesignChoiceConstants and can be one of the following:
	 * <p>
	 * <ul>
	 * <li><code>ACTION_FORMAT_TYPE_HTML</code>
	 * <li><code>ACTION_FORMAT_TYPE_PDF</code>
	 * </ul>
	 * Or, it may be one format that defined by users.
	 *
	 * @param type the type of the action
	 * @throws SemanticException
	 */
	public void setFormatType(String type) throws SemanticException {
		setProperty(Action.FORMAT_TYPE_MEMBER, type);
	}

	/**
	 * Gets the format type of the action. The format type for action are defined in
	 * DesignChoiceConstants and can be one of the following: *
	 * <p>
	 * <ul>
	 * <li><code>ACTION_FORMAT_TYPE_HTML</code>
	 * <li><code>ACTION_FORMAT_TYPE_PDF</code>
	 * </ul>
	 * Or, it may be one format that defined by users.
	 *
	 * @return the format type of the action
	 */
	public String getFormatType() {

		return getStringProperty(Action.FORMAT_TYPE_MEMBER);
	}

	/**
	 * Gets the value of tool tip.
	 *
	 * @return the value of tool tip.
	 */
	public String getToolTip() {
		return getStringProperty(Action.TOOLTIP_MEMBER);
	}

	/**
	 * Sets the value of the tool tip.
	 *
	 * @param toolTip the value of tool tip.
	 * @throws SemanticException
	 */
	public void setToolTip(String toolTip) throws SemanticException {
		setProperty(Action.TOOLTIP_MEMBER, toolTip);
	}

	/**
	 * Get a handle to deal with the parameter binding list member if the link type
	 * is <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. Otherwise, return null.
	 *
	 * @return a handle to deal with the parameter binding list member
	 */

	public MemberHandle getParamBindings() {
		if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equalsIgnoreCase(getLinkType())) {
			return getMember(Action.PARAM_BINDINGS_MEMBER);
		}

		return null;
	}

	/**
	 * Add a new parameter binding to the action.
	 *
	 * @param paramBinding a new parameter binding to be added.
	 * @throws SemanticException if the parameter binding is not valid
	 */

	public void addParamBinding(ParamBinding paramBinding) throws SemanticException {
		MemberHandle memberHandle = getMember(Action.PARAM_BINDINGS_MEMBER);
		memberHandle.addItem(paramBinding);
	}

	/**
	 * Get a handle to deal with the search key list member if the link type is
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. Otherwise, return null.
	 *
	 * @return a handle to deal with the search key list member
	 */

	public MemberHandle getSearch() {
		if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equalsIgnoreCase(getLinkType())) {
			return getMember(Action.SEARCH_MEMBER);
		}

		return null;
	}

	/**
	 * Add a new search key to the action.
	 *
	 * @param key a new search key to be added.
	 * @throws SemanticException if the value is not valid.
	 */

	public void addSearch(SearchKey key) throws SemanticException {
		MemberHandle memberHandle = getMember(Action.SEARCH_MEMBER);
		memberHandle.addItem(key);
	}

	/**
	 * Sets the target window of the action.
	 *
	 * @param window the target window name
	 * @throws SemanticException if this property is locked.
	 */

	public void setTargetWindow(String window) throws SemanticException {
		setProperty(Action.TARGET_WINDOW_MEMBER, window);
	}

	/**
	 * Sets the hyperlink of this action. The link type will be changed to
	 * <code>ACTION_LINK_TYPE_HYPERLINK</code>.
	 *
	 * @param hyperlink the hyperlink to set
	 * @throws SemanticException if the property is locked.
	 * @see #getHyperlink()
	 * @deprecated {@link #setURI(String)}
	 */

	@Deprecated
	public void setHyperlink(String hyperlink) throws SemanticException {
		setURI(hyperlink);
	}

	/**
	 *
	 * Sets the hyperlink of this action. The link type will be changed to
	 * <code>ACTION_LINK_TYPE_HYPERLINK</code>.
	 *
	 * @param uri the hyperlink to set
	 * @throws SemanticException if the property is locked.
	 */

	public void setURI(String uri) throws SemanticException {
		setProperty(Action.URI_MEMBER, uri);
	}

	/**
	 * Gets the name of the target report document if the link type is
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. Otherwise, return null.
	 *
	 * @return the name of the target report document
	 * @see #setDrillThroughReportName(String)
	 * @deprecated use {@link #getReportName()}instead.
	 */

	@Deprecated
	public String getDrillThroughReportName() {
		return getReportName();
	}

	/**
	 * Gets the name of the target report document if the link type is
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. Otherwise, return null.
	 *
	 * @return the name of the target report document
	 * @see #setReportName(String)
	 */

	public String getReportName() {
		if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equalsIgnoreCase(getLinkType())) {
			return getStringProperty(Action.REPORT_NAME_MEMBER);
		}

		return null;
	}

	/**
	 * Sets target report name for a drill-though link. The link type willl be
	 * changed to <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. The report name can
	 * include relative or absolute names. If the suffix is omitted, it is computed
	 * on the server by looking for a matching report. BIRT reports are searched in
	 * the following order: 1) a BIRT report document or 2) a BIRT report design.
	 *
	 * @param reportName the name of the target report
	 * @throws SemanticException if the property is locked.
	 * @see #getDrillThroughReportName()
	 * @deprecated use {@link #setReportName(String)}instead.
	 */

	@Deprecated
	public void setDrillThroughReportName(String reportName) throws SemanticException {
		setReportName(reportName);
	}

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

	public void setReportName(String reportName) throws SemanticException {
		setProperty(Action.REPORT_NAME_MEMBER, reportName);
	}

	/**
	 * Sets the drill-through bookmark. The link type will be changed to
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>, and drill-through type will be
	 * changed to <code>DRILL_THROUGH_LINK_TYPE_BOOKMARK_LINK</code>.
	 *
	 * @param bookmark the bookmark to set.
	 * @throws SemanticException if the property is locked.
	 * @see #getBookmarkLink()
	 * @deprecated use {@link #setTargetBookmark(String)}instead.
	 */

	@Deprecated
	public void setDrillThroughBookmarkLink(String bookmark) throws SemanticException {
		setTargetBookmark(bookmark);
	}

	/**
	 * Gets the bookmark link if the link type is
	 * <code>ACTION_LINK_TYPE_BOOKMARK_LINK</code>. Otherwise, return null.
	 *
	 * @return the bookmark link
	 * @deprecated use {@link #getTargetBookmark()}instead.
	 */

	@Deprecated
	public String getBookmarkLink() {
		return getTargetBookmark();
	}

	/**
	 * Gets the bookmark link if the link type is
	 * <code>ACTION_LINK_TYPE_BOOKMARK_LINK</code>. Otherwise, return null.
	 *
	 * @return the bookmark link
	 */

	public String getTargetBookmark() {
		if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equalsIgnoreCase(getLinkType())
				|| DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equalsIgnoreCase(getLinkType())) {
			return getStringProperty(Action.TARGET_BOOKMARK_MEMBER);
		}

		return null;

	}

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

	public void setTargetBookmark(String bookmark) throws SemanticException {
		setProperty(Action.TARGET_BOOKMARK_MEMBER, bookmark);
	}

	/**
	 * Sets the drill-through bookmark. The link type will be changed to
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>.
	 *
	 * @param bookmark the bookmark to set.
	 * @throws SemanticException if the property is locked.
	 * @see #getTargetBookmark()
	 * @deprecated use {@link #setTargetBookmark(String)}instead.
	 */

	@Deprecated
	public void setDrillThroughTargetBookmark(String bookmark) throws SemanticException {
		setTargetBookmark(bookmark);
	}

	/**
	 * Sets the bookmark link of this action. The link type will be changed to
	 * <code>ACTION_LINK_TYPE_BOOKMARK_LINK</code>.
	 *
	 * @param bookmark the expression value.
	 * @throws SemanticException if the property is locked.
	 * @see #getBookmarkLink()
	 * @deprecated use {@link #setTargetBookmark(String)}instead.
	 */

	@Deprecated
	public void setBookmarkLink(String bookmark) throws SemanticException {
		setTargetBookmark(bookmark);
	}

	/**
	 * Gets the parameter binding list of a drill-through action if the link type is
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. Each one is the instance of
	 * <code>ParameBindingHandle</code>
	 * <p>
	 * Action binds a data value in the report to a report parameter defined in the
	 * target report.
	 * <p>
	 * Note that this is a parameter binding, not a parameter definition. The report
	 * makes no attempt to check that the parameters listed here are accurate in
	 * name or type for the target report. Also, it is legal to bind the same
	 * parameter multiple times; the meaning depends on the semantics of the target
	 * report.
	 *
	 * @return the iterator over parameters of a drill-through action.
	 *
	 */

	public Iterator paramBindingsIterator() {
		if (!DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equalsIgnoreCase(getLinkType())) {
			return Collections.EMPTY_LIST.iterator();
		}

		MemberHandle memberHandle = getMember(Action.PARAM_BINDINGS_MEMBER);
		return memberHandle.iterator();
	}

	/**
	 * Gets the search key list for a drill-through action if the link type is
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code> and the drill through type is
	 * <code>DRILL_THROUGH_LINK_TYPE_SEARCH</code>. Each one is the instance of
	 * <code>SearchKeyHandle</code>
	 * <p>
	 * The search key list identifies search criteria in the target report and is
	 * used for drill-though links. The search is assumed to be quality. That is,
	 * identify a column defined in the target report and a data value defined in
	 * this report. The link will then search for this value.
	 *
	 * @return the iterator over search keys of a drill-through action.
	 *
	 */

	public Iterator searchIterator() {
		if (!DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equalsIgnoreCase(getLinkType())) {
			return Collections.EMPTY_LIST.iterator();
		}

		MemberHandle memberHandle = getMember(Action.SEARCH_MEMBER);
		return memberHandle.iterator();
	}

	/**
	 * Sets target report file type for a drill-through action. The format type for
	 * action are defined in DesignChoiceConstants and can be one of the following:
	 * *
	 * <p>
	 * <ul>
	 * <li><code>ACTION_TARGET_FILE_TYPE_REPORT_DESIGN</code>
	 * <li><code>ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT</code>
	 * </ul>
	 *
	 * @param targetFileType the type of the target report file
	 * @throws SemanticException if type of the target report file is not in the
	 *                           choice list.
	 * @see #getTargetFileType()
	 */

	public void setTargetFileType(String targetFileType) throws SemanticException {
		setProperty(Action.TARGET_FILE_TYPE_MEMBER, targetFileType);
	}

	/**
	 * Gets target report file type for a drill-through action. The format type for
	 * action are defined in DesignChoiceConstants and can be one of the following:
	 * *
	 * <p>
	 * <ul>
	 * <li><code>ACTION_TARGET_FILE_TYPE_REPORT_DESIGN</code>
	 * <li><code>ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT</code>
	 * </ul>
	 *
	 * @return target report file type for a drill-through action
	 */

	public String getTargetFileType() {
		String linkType = getLinkType();
		if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equalsIgnoreCase(linkType)
				|| DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equalsIgnoreCase(linkType)) {
			return getStringProperty(Action.TARGET_FILE_TYPE_MEMBER);
		}

		return null;
	}

	/**
	 * Sets target bookmark type for a drill-through or bookmark-link action. The
	 * bookmark type for action are defined in DesignChoiceConstants and can be one
	 * of the following:
	 *
	 * <p>
	 * <ul>
	 * <li><code>ACTION_BOOKMARK_TYPE_BOOKMARK</code>
	 * <li><code>ACTION_BOOKMARK_TYPE_TOC</code>
	 * </ul>
	 *
	 * @param targetBookmarkType the type of the target bookmark
	 * @throws SemanticException if type of the target bookmark is not in the choice
	 *                           list.
	 * @see #getTargetBookmarkType()
	 */

	public void setTargetBookmarkType(String targetBookmarkType) throws SemanticException {
		setProperty(Action.TARGET_BOOKMARK_TYPE_MEMBER, targetBookmarkType);
	}

	/**
	 * Gets target bookmark type for a drill-through or bookmark-link action. The
	 * bookmark type for action are defined in DesignChoiceConstants and can be one
	 * of the following:
	 *
	 * <p>
	 * <ul>
	 * <li><code>ACTION_BOOKMARK_TYPE_BOOKMARK</code>
	 * <li><code>ACTION_BOOKMARK_TYPE_TOC</code>
	 * </ul>
	 *
	 * @return target bookmark type for a drill-through or bookmark-link action
	 */

	public String getTargetBookmarkType() {
		String linkType = getLinkType();
		if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equalsIgnoreCase(linkType)
				|| DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equalsIgnoreCase(linkType)) {
			return getStringProperty(Action.TARGET_BOOKMARK_TYPE_MEMBER);
		}

		return null;
	}
}
