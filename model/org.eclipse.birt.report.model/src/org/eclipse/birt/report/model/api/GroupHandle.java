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
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * Represents both list and table groups in the design. Groups provide a way of
 * showing common headings for a group of related rows.
 * <p>
 * A group is defined by a group key. The key is a column from the query. If the
 * group key is a time field then user often want to group on an interval such
 * as month or quarter.
 *
 * @see org.eclipse.birt.report.model.elements.GroupElement
 * @see SlotHandle
 */

public abstract class GroupHandle extends ReportElementHandle implements IGroupElementModel {

	/**
	 * Constructs a group handle with the given design and the design element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public GroupHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the header slot in the group. The header slot represents subsections
	 * that print at the start of the group.
	 *
	 * @return a slot handle to the header
	 */

	public SlotHandle getHeader() {
		return getSlot(IGroupElementModel.HEADER_SLOT);
	}

	/**
	 * Returns the footer slot. The footer slot represents subsections that print at
	 * the end of the group.
	 *
	 * @return a slot handle to the footer
	 */

	public SlotHandle getFooter() {
		return getSlot(IGroupElementModel.FOOTER_SLOT);
	}

	/**
	 * Gets the expression that defines the group. This is normally simply a
	 * reference to a data set column.
	 *
	 * @return the expression as a string
	 *
	 * @see #setKeyExpr(String)
	 */

	public String getKeyExpr() {
		return getStringProperty(IGroupElementModel.KEY_EXPR_PROP);
	}

	/**
	 * Gets the name of the group.
	 *
	 * @return the name of the group
	 */

	@Override
	public String getName() {
		return getStringProperty(IGroupElementModel.GROUP_NAME_PROP);
	}

	/**
	 * Sets the group name.
	 *
	 * @param theName the group name to set
	 */

	@Override
	public void setName(String theName) throws NameException {

		// trim the name, have the same behavior as Name property.

		try {
			setProperty(IGroupElementModel.GROUP_NAME_PROP, StringUtil.trimString(theName));
		} catch (NameException e) {
			throw e;
		} catch (SemanticException e) {
			assert false;
		}

	}

	/**
	 * Sets the group expression.
	 *
	 * @param expr the expression to set
	 * @throws SemanticException If the expression is invalid.
	 *
	 * @see #getKeyExpr()
	 */

	public void setKeyExpr(String expr) throws SemanticException {
		setProperty(IGroupElementModel.KEY_EXPR_PROP, expr);
	}

	/**
	 * Returns the iterator for Sort list defined on the group. The element in the
	 * iterator is the corresponding <code>StructureHandle</code>.
	 *
	 * @return the iterator for <code>SortKey</code> structure list defined on the
	 *         group.
	 */

	public Iterator sortsIterator() {
		PropertyHandle propHandle = getPropertyHandle(IGroupElementModel.SORT_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Returns an iterator for the filter list defined on the group. Each object
	 * returned is of type <code>StructureHandle</code>.
	 *
	 * @return the iterator for <code>FilterCond</code> structure list defined on
	 *         the group.
	 */

	@SuppressWarnings("unchecked")
	public Iterator<FilterConditionHandle> filtersIterator() {
		PropertyHandle propHandle = getPropertyHandle(IGroupElementModel.FILTER_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Sets group start property of this group. Group start, in conjunction with
	 * Interval and IntervalRange, determines how data is divided into groups.
	 *
	 * @param groupStart group start property value.
	 * @throws SemanticException if the property is locked.
	 *
	 * @deprecated by {@link #setIntervalBase(String)}
	 */

	@Deprecated
	public void setGroupStart(String groupStart) throws SemanticException {
		setIntervalBase(groupStart);
	}

	/**
	 * Return the group start property value of this group.
	 *
	 * @return group start property value of this group.
	 *
	 * @deprecated by {@link #getIntervalBase()}
	 */

	@Deprecated
	public String getGroupStart() {
		return getIntervalBase();
	}

	/**
	 * Sets the base of the interval property of this group.IntervalBase, in
	 * conjunction with Interval and IntervalRange, determines how data is divided
	 * into groups.
	 *
	 * @param intervalBase interval base property value.
	 * @throws SemanticException if the property is locked.
	 */

	public void setIntervalBase(String intervalBase) throws SemanticException {
		setStringProperty(IGroupElementModel.INTERVAL_BASE_PROP, intervalBase);
	}

	/**
	 * Return the interval base property value of this group.
	 *
	 * @return interval baseF property value of this group.
	 */

	public String getIntervalBase() {
		return getStringProperty(IGroupElementModel.INTERVAL_BASE_PROP);
	}

	/**
	 * Returns the interval of this group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>INTERVAL_NONE</code>
	 * <li><code>INTERVAL_PREFIX</code>
	 * <li><code>INTERVAL_YEAR</code>
	 * <li><code>INTERVAL_QUARTER</code>
	 * <li><code>INTERVAL_MONTH</code>
	 * <li><code>INTERVAL_WEEK</code>
	 * <li><code>INTERVAL_DAY</code>
	 * <li><code>INTERVAL_HOUR</code>
	 * <li><code>INTERVAL_MINUTE</code>
	 * <li><code>INTERVAL_SECOND</code>
	 * <li><code>INTERVAL_INTERVAL</code>
	 *
	 * </ul>
	 *
	 * @return the interval value as a string
	 */

	public String getInterval() {
		return getStringProperty(IGroupElementModel.INTERVAL_PROP);
	}

	/**
	 * Returns the interval of this group. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>INTERVAL_NONE</code>
	 * <li><code>INTERVAL_PREFIX</code>
	 * <li><code>INTERVAL_YEAR</code>
	 * <li><code>INTERVAL_QUARTER</code>
	 * <li><code>INTERVAL_MONTH</code>
	 * <li><code>INTERVAL_WEEK</code>
	 * <li><code>INTERVAL_DAY</code>
	 * <li><code>INTERVAL_HOUR</code>
	 * <li><code>INTERVAL_MINUTE</code>
	 * <li><code>INTERVAL_SECOND</code>
	 * <li><code>INTERVAL_INTERVAL</code>
	 *
	 * </ul>
	 *
	 * @param interval the interval value as a string
	 * @throws SemanticException if the property is locked or the input value is not
	 *                           one of the above.
	 */

	public void setInterval(String interval) throws SemanticException {
		setStringProperty(IGroupElementModel.INTERVAL_PROP, interval);
	}

	/**
	 * Returns the interval range of this group.
	 *
	 * @return the interval range value as a double
	 */

	public double getIntervalRange() {
		return this.getFloatProperty(IGroupElementModel.INTERVAL_RANGE_PROP);
	}

	/**
	 * Returns the interval range of this group.
	 *
	 * @param intervalRange the interval range value as a double
	 * @throws SemanticException if the property is locked.
	 */

	public void setIntervalRange(double intervalRange) throws SemanticException {
		setFloatProperty(IGroupElementModel.INTERVAL_RANGE_PROP, intervalRange);
	}

	/**
	 * Sets the interval range of group.
	 *
	 * @param intervalRange the interval range value as a string.value is locale
	 *                      dependent.
	 * @throws SemanticException if the property is locked.
	 */

	public void setIntervalRange(String intervalRange) throws SemanticException {
		setStringProperty(IGroupElementModel.INTERVAL_RANGE_PROP, intervalRange);
	}

	/**
	 * Returns the sort direction of this group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>SORT_DIRECTION_ASC</code>
	 * <li><code>SORT_DIRECTION_DESC</code>
	 *
	 * </ul>
	 *
	 * @return the sort direction of this group
	 */

	public String getSortDirection() {
		return getStringProperty(IGroupElementModel.SORT_DIRECTION_PROP);
	}

	/**
	 * Sets the sort direction of this group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>SORT_DIRECTION_ASC</code>
	 * <li><code>SORT_DIRECTION_DESC</code>
	 *
	 * </ul>
	 *
	 * @param direction the sort direction of this group
	 * @throws SemanticException if the property is locked or the input value is not
	 *                           one of the above.
	 *
	 */

	public void setSortDirection(String direction) throws SemanticException {
		setStringProperty(IGroupElementModel.SORT_DIRECTION_PROP, direction);
	}

	/**
	 * Checks whether the group header slot is empty.
	 *
	 * @return true is the header slot is not empty, otherwise, return false.
	 *
	 */

	public boolean hasHeader() {
		return (getHeader().getCount() != 0);
	}

	/**
	 * Checks whether the group footer slot is empty.
	 *
	 * @return true is the footer slot is not empty, otherwise, return false.
	 *
	 */

	public boolean hasFooter() {
		return (getFooter().getCount() != 0);
	}

	/**
	 * Sets a table of contents entry for this item. The TOC property defines an
	 * expression that returns a string that is to appear in the Table of Contents
	 * for this item or its container.
	 *
	 * @param expression the expression that returns a string
	 * @throws SemanticException if the TOC property is locked by the property mask.
	 *
	 * @see #getTocExpression()
	 * @deprecated
	 */

	@Deprecated
	public void setTocExpression(String expression) throws SemanticException {
		if (StringUtil.isEmpty(expression)) {
			setProperty(IGroupElementModel.TOC_PROP, null);
			return;
		}
		TOCHandle tocHandle = getTOC();
		if (tocHandle == null) {
			TOC toc = StructureFactory.createTOC(expression);
			addTOC(toc);
		} else {
			tocHandle.setExpression(expression);
		}

	}

	/**
	 * Returns the expression evalueated as a table of contents entry for this item.
	 *
	 * @return the expression evaluated as a table of contents entry for this item
	 * @see #setTocExpression(String)
	 * @deprecated
	 */

	@Deprecated
	public String getTocExpression() {
		TOCHandle tocHandle = getTOC();
		if (tocHandle == null) {
			return null;
		}
		return tocHandle.getExpression();
	}

	/**
	 * Sets the sort type, which indicates the way of sorting
	 *
	 * @param sortType sort type.
	 * @throws SemanticException if the property is locked.
	 */

	public void setSortType(String sortType) throws SemanticException {
		setStringProperty(IGroupElementModel.SORT_TYPE_PROP, sortType);
	}

	/**
	 * Return the sort type.
	 *
	 * @return the sort type.
	 */

	public String getSortType() {
		return getStringProperty(IGroupElementModel.SORT_TYPE_PROP);
	}

	/**
	 * Gets the on-prepare script of the group. Startup phase. No data binding yet.
	 * The design of an element can be changed here.
	 *
	 * @return the on-prepare script of the group
	 *
	 */

	public String getOnPrepare() {
		return getStringProperty(IGroupElementModel.ON_PREPARE_METHOD);
	}

	/**
	 * Sets the on-prepare script of the group element.
	 *
	 * @param script the script to set
	 * @throws SemanticException if the method is locked.
	 *
	 * @see #getOnPrepare()
	 */

	public void setOnPrepare(String script) throws SemanticException {
		setProperty(IGroupElementModel.ON_PREPARE_METHOD, script);
	}

	/**
	 * Tests whether to repeat the headings at the top of each page.
	 *
	 * @return <code>true</code> if repeat the headings, otherwise
	 *         <code>false</code>.
	 */

	public boolean repeatHeader() {
		return getBooleanProperty(REPEAT_HEADER_PROP);
	}

	/**
	 * Sets whether to repeat the headings at the top of each page.
	 *
	 * @param value <code>true</code> if repeat the headings, otherwise
	 *              <code>false</code>.
	 * @throws SemanticException if the property is locked.
	 */

	public void setRepeatHeader(boolean value) throws SemanticException {
		setBooleanProperty(REPEAT_HEADER_PROP, value);
	}

	/**
	 * Gets page break after property value of this group.
	 *
	 * @return page break after property value of this group.
	 */

	public String getPageBreakAfter() {
		return getStringProperty(IStyleModel.PAGE_BREAK_AFTER_PROP);
	}

	/**
	 * Sets page break after property value of this group.
	 *
	 * @param value value of page break after property
	 * @throws SemanticException if the property is locked.
	 */

	public void setPageBreakAfter(String value) throws SemanticException {
		setProperty(IStyleModel.PAGE_BREAK_AFTER_PROP, value);
	}

	/**
	 * Gets page break inside property value of this group.
	 *
	 * @return page break inside property value of this group.
	 */

	public String getPageBreakInside() {
		return getStringProperty(IStyleModel.PAGE_BREAK_INSIDE_PROP);
	}

	/**
	 * Sets page break inside property value of this group.
	 *
	 * @param value value of page break inside property
	 * @throws SemanticException if the property is locked.
	 */

	public void setPageBreakInside(String value) throws SemanticException {
		setProperty(IStyleModel.PAGE_BREAK_INSIDE_PROP, value);
	}

	/**
	 * Gets page break before property value of this group.
	 *
	 * @return page break before property value of this group.
	 */

	public String getPageBreakBefore() {
		return getStringProperty(IStyleModel.PAGE_BREAK_BEFORE_PROP);
	}

	/**
	 * Sets page break before property value of this group.
	 *
	 * @param value value of page break before property
	 * @throws SemanticException if the property is locked.
	 */

	public void setPageBreakBefore(String value) throws SemanticException {
		setProperty(IStyleModel.PAGE_BREAK_BEFORE_PROP, value);
	}

	/**
	 * Tests whether to hide the detail rows of this group.
	 *
	 * @return <code>true</code> if hide the detail rows, otherwise
	 *         <code>false</code>.
	 */

	public boolean hideDetail() {
		return getBooleanProperty(HIDE_DETAIL_PROP);
	}

	/**
	 * Sets whether to hide the detail rows of this group.
	 *
	 * @param value <code>true</code> if hide the detail rows, otherwise
	 *              <code>false</code>.
	 * @throws SemanticException if the property is locked.
	 */

	public void setHideDetail(boolean value) throws SemanticException {
		setBooleanProperty(HIDE_DETAIL_PROP, value);
	}

	/**
	 * Gets the on-pageBreak script of the group element. Presentation phase. It is
	 * for a script executed when the element is prepared for page breaking in the
	 * Presentation engine.
	 *
	 * @return the on-pageBreak script of the group element
	 *
	 */

	public String getOnPageBreak() {
		return getStringProperty(ON_PAGE_BREAK_METHOD);
	}

	/**
	 * Sets the on-pageBreak script of the group element.
	 *
	 * @param script the script to set
	 * @throws SemanticException if the method is locked.
	 *
	 * @see #getOnPageBreak()
	 */

	public void setOnPageBreak(String script) throws SemanticException {
		setProperty(ON_PAGE_BREAK_METHOD, script);
	}

	/**
	 * Gets the onCreate script of the group element. Presentation phase. It is for
	 * a script executed when the element is prepared for creating in the
	 * Presentation engine.
	 *
	 * @return the onCreate script of the group element
	 *
	 */
	public String getOnCreate() {
		return getStringProperty(ON_CREATE_METHOD);
	}

	/**
	 * Sets create property value of this group.
	 *
	 * @param value value of create property
	 * @throws SemanticException if the property is locked.
	 */
	public void setOnCreate(String script) throws SemanticException {
		setProperty(ON_CREATE_METHOD, script);
	}

	/**
	 * Gets the onRender script of the group element. Presentation phase. It is for
	 * a script executed when the element is prepared for rendering in the
	 * Presentation engine.
	 *
	 * @return the onCreate script of the group element
	 *
	 */
	public String getOnRender() {
		return getStringProperty(ON_RENDER_METHOD);
	}

	/**
	 * Sets render property value of this group.
	 *
	 * @param value value of render property
	 * @throws SemanticException if the property is locked.
	 */
	public void setOnRender(String script) throws SemanticException {
		setProperty(ON_RENDER_METHOD, script);
	}

	/**
	 * Returns the bound columns that binds the data set columns. The item in the
	 * iterator is the corresponding <code>ComputedColumnHandle</code>.
	 *
	 * @return a list containing the bound columns.
	 *
	 * @deprecated since BIRT 2.1 RC2
	 */

	@Deprecated
	public Iterator columnBindingsIterator() {
		return Collections.EMPTY_LIST.iterator();
	}

	/**
	 * Get a handle to deal with the bound column.
	 *
	 * @return a handle to deal with the boudn data column.
	 *
	 * @deprecated since BIRT 2.1 RC2
	 */

	@Deprecated
	public PropertyHandle getColumnBindings() {
		return null;
	}

	/**
	 * Adds a bound column to the list.
	 *
	 * @param addColumn the bound column to add
	 * @param inForce   <code>true</code> the column is added to the list regardless
	 *                  of duplicate expression. <code>false</code> do not add the
	 *                  column if the expression already exist
	 * @param column    the bound column
	 * @return the newly created <code>ComputedColumnHandle</code> or the existed
	 *         <code>ComputedColumnHandle</code> in the list
	 * @throws SemanticException if expression is not duplicate but the name
	 *                           duplicates the exsiting bound column. Or, if the
	 *                           both name/expression are duplicate, but
	 *                           <code>inForce</code> is <code>true</code>.
	 *
	 * @deprecated since BIRT 2.1 RC2
	 */

	@Deprecated
	public ComputedColumnHandle addColumnBinding(ComputedColumn addColumn, boolean inForce) throws SemanticException {
		return null;
	}

	/**
	 * Gets TOC handle.
	 *
	 * @return toc handle
	 */

	public TOCHandle getTOC() {
		PropertyHandle propHandle = getPropertyHandle(IGroupElementModel.TOC_PROP);
		TOC toc = (TOC) propHandle.getValue();

		if (toc == null) {
			return null;
		}

		return (TOCHandle) toc.getHandle(propHandle);
	}

	/**
	 * Adds toc structure.
	 *
	 * @param expression toc expression
	 * @return toc handle
	 * @throws SemanticException
	 */

	public TOCHandle addTOC(String expression) throws SemanticException {
		if (StringUtil.isEmpty(expression)) {
			return null;
		}

		TOC toc = StructureFactory.createTOC(expression);
		setProperty(IReportItemModel.TOC_PROP, toc);

		return (TOCHandle) toc.getHandle(getPropertyHandle(IReportItemModel.TOC_PROP));
	}

	/**
	 * Adds toc structure.
	 *
	 * @param toc toc structure
	 * @return toc handle
	 * @throws SemanticException
	 */

	public TOCHandle addTOC(TOC toc) throws SemanticException {
		setProperty(IGroupElementModel.TOC_PROP, toc);

		if (toc == null) {
			return null;
		}
		return (TOCHandle) toc.getHandle(getPropertyHandle(IGroupElementModel.TOC_PROP));
	}

	/**
	 * Returns the bookmark of listing group.
	 *
	 * @return the book mark as a string
	 */

	public String getBookmark() {
		return getStringProperty(IGroupElementModel.BOOKMARK_PROP);
	}

	/**
	 * Sets the bookmark of listing group.
	 *
	 * @param value the property value to be set.
	 * @throws SemanticException if the property is locked.
	 */

	public void setBookmark(String value) throws SemanticException {
		setStringProperty(IGroupElementModel.BOOKMARK_PROP, value);
	}

	/**
	 * Returns the ACL expression associated with the report element instance.
	 *
	 * @return the expression in string
	 *
	 */

	public String getACLExpression() {
		return getStringProperty(IGroupElementModel.ACL_EXPRESSION_PROP);
	}

	/**
	 * Sets the ACL expression associated with the report element instance.
	 *
	 * @param expr the expression in string
	 * @throws SemanticException if the property is locked by masks
	 *
	 */

	public void setACLExpression(String expr) throws SemanticException {
		setStringProperty(IGroupElementModel.ACL_EXPRESSION_PROP, expr);
	}

	/**
	 * Returns <code>true</code> (the default), a report element's ACL is
	 * automatically propagated to all its directly contained child elements and are
	 * added to their ACLs. Otherwise <code>false</code>.
	 *
	 * @return the flag to control whether to cascade ACL
	 *
	 */

	public boolean cascadeACL() {
		return getBooleanProperty(IGroupElementModel.CASCADE_ACL_PROP);
	}

	/**
	 * Sets the flag to control whether to cascade ACL
	 *
	 * @param cascadeACL <code>true</code> (the default), a report element's ACL is
	 *                   automatically propagated to all its directly contained
	 *                   child elements and are added to their ACLs. Otherwise
	 *                   <code>false</code>.
	 * @throws SemanticException if the property is locked by masks
	 *
	 */

	public void setCascadeACL(boolean cascadeACL) throws SemanticException {
		setBooleanProperty(IGroupElementModel.CASCADE_ACL_PROP, cascadeACL);
	}

	/**
	 * Gets the display name of the bookmark.
	 *
	 * @return the display name of the bookmark.
	 */
	public String getBookmarkDisplayName() {
		return getStringProperty(BOOKMARK_DISPLAY_NAME_PROP);
	}

	/**
	 * Sets the display name of the bookmark.
	 *
	 * @param bookmarkDisplayName the display name of the bookmark to set
	 * @throws SemanticException
	 */
	public void setBookmarkDisplayName(String bookmarkDisplayName) throws SemanticException {
		setStringProperty(BOOKMARK_DISPLAY_NAME_PROP, bookmarkDisplayName);
	}

	/**
	 * Gets the flag which indicates whether to show the detail filter or not.
	 *
	 * @return true if the group shows the detail filter, otherwise false.
	 */
	public boolean showDetailFilter() {
		return getBooleanProperty(SHOW_DETAIL_FILTER_PROP);
	}

	/**
	 * Sets the flag which indicates whether to show the detail filter or not.
	 *
	 * @param showDetailFilter the new flag to set
	 * @throws SemanticException
	 */
	public void setShowDetailFilter(boolean showDetailFilter) throws SemanticException {
		setBooleanProperty(SHOW_DETAIL_FILTER_PROP, showDetailFilter);
	}
}
