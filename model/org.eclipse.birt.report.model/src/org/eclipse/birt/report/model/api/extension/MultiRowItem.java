/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.extension;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IDataBinding;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.IFilterCondition;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;
import org.eclipse.birt.report.model.api.simpleapi.IHighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.IReportItem;
import org.eclipse.birt.report.model.api.simpleapi.ISimpleElementFactory;
import org.eclipse.birt.report.model.api.simpleapi.ISortCondition;
import org.eclipse.birt.report.model.api.simpleapi.IStyle;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of multi row report item. Used for extension multi row items. The
 * constructor in sub class must call super(ExtendedItemHandle).
 *
 */

public class MultiRowItem implements IMultiRowItem {

	private final IReportItem item;

	/**
	 * Constructors.
	 *
	 * @param item
	 */

	public MultiRowItem(ExtendedItemHandle item) {
		this.item = SimpleElementFactory.getInstance().wrapExtensionElement(item, ISimpleElementFactory.MULTI_ROW_ITEM);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem#addFilterCondition(
	 * org.eclipse.birt.report.model.api.simpleapi.IFilterCondition)
	 */

	@Override
	public void addFilterCondition(IFilterCondition condition) throws SemanticException {
		if (!(item instanceof IMultiRowItem)) {
			return;
		}

		((IMultiRowItem) item).addFilterCondition(condition);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem#addSortCondition(
	 * org.eclipse.birt.report.model.api.simpleapi.ISortCondition)
	 */
	@Override
	public void addSortCondition(ISortCondition condition) throws SemanticException {
		if (!(item instanceof IMultiRowItem)) {
			return;
		}

		((IMultiRowItem) item).addSortCondition(condition);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem#getFilterConditions
	 * ()
	 */

	@Override
	public IFilterCondition[] getFilterConditions() {
		if (!(item instanceof IMultiRowItem)) {
			return null;
		}

		return ((IMultiRowItem) item).getFilterConditions();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem#getSortConditions()
	 */

	@Override
	public ISortCondition[] getSortConditions() {
		if (!(item instanceof IMultiRowItem)) {
			return null;
		}

		return ((IMultiRowItem) item).getSortConditions();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem#
	 * removeFilterCondition(org.eclipse.birt.report.model.api.simpleapi.
	 * IFilterCondition)
	 */
	@Override
	public void removeFilterCondition(IFilterCondition condition) throws SemanticException {
		if (!(item instanceof IMultiRowItem)) {
			return;
		}

		((IMultiRowItem) item).removeFilterCondition(condition);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem#
	 * removeFilterConditions()
	 */

	@Override
	public void removeFilterConditions() throws SemanticException {
		if (!(item instanceof IMultiRowItem)) {
			return;
		}

		((IMultiRowItem) item).removeFilterConditions();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem#removeSortCondition
	 * (org.eclipse.birt.report.model.api.simpleapi.ISortCondition)
	 */
	@Override
	public void removeSortCondition(ISortCondition condition) throws SemanticException {
		if (!(item instanceof IMultiRowItem)) {
			return;
		}

		((IMultiRowItem) item).removeSortCondition(condition);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem#
	 * removeSortConditions()
	 */
	@Override
	public void removeSortConditions() throws SemanticException {
		if (!(item instanceof IMultiRowItem)) {
			return;
		}

		((IMultiRowItem) item).removeSortConditions();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#addDataBinding(org.
	 * eclipse.birt.report.model.api.simpleapi.IDataBinding)
	 */
	@Override
	public void addDataBinding(IDataBinding binding) throws SemanticException {
		item.addDataBinding(binding);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#addHideRule(org.
	 * eclipse.birt.report.model.api.simpleapi.IHideRule)
	 */
	@Override
	public void addHideRule(IHideRule rule) throws SemanticException {
		item.addHideRule(rule);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#addHighlightRule(org.
	 * eclipse.birt.report.model.api.simpleapi.IHighlightRule)
	 */
	@Override
	public void addHighlightRule(IHighlightRule rule) throws SemanticException {
		item.addHighlightRule(rule);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getBookmark()
	 */
	@Override
	public String getBookmark() {

		return item.getBookmark();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#getDataBinding(java.
	 * lang.String)
	 */
	@Override
	public String getDataBinding(String bindingName) {
		return item.getDataBinding(bindingName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#getDataBindings()
	 */
	@Override
	public IDataBinding[] getDataBindings() {

		return item.getDataBindings();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getHeight()
	 */
	@Override
	public String getHeight() {

		return item.getHeight();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getHideRules()
	 */
	@Override
	public IHideRule[] getHideRules() {

		return item.getHideRules();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#getHighlightRules()
	 */
	@Override
	public IHighlightRule[] getHighlightRules() {

		return item.getHighlightRules();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#getTocExpression()
	 */
	@Override
	public String getTocExpression() {

		return item.getTocExpression();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getWidth()
	 */
	@Override
	public String getWidth() {

		return item.getWidth();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getX()
	 */
	@Override
	public String getX() {

		return item.getX();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getY()
	 */
	@Override
	public String getY() {

		return item.getY();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setX(double)
	 */
	@Override
	public void setX(double dimension) throws SemanticException {

		item.setX(dimension);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setX(java.lang.
	 * String)
	 */
	@Override
	public void setX(String dimension) throws SemanticException {

		item.setX(dimension);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setY(double)
	 */
	@Override
	public void setY(double dimension) throws SemanticException {

		item.setY(dimension);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setY(java.lang.
	 * String)
	 */
	@Override
	public void setY(String dimension) throws SemanticException {

		item.setY(dimension);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#removeDataBinding(
	 * java.lang.String)
	 */
	@Override
	public void removeDataBinding(String bindingName) throws SemanticException {

		item.removeDataBinding(bindingName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#removeDataBindings()
	 */
	@Override
	public void removeDataBindings() throws SemanticException {

		item.removeDataBindings();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#removeHideRule(org.
	 * eclipse.birt.report.model.api.simpleapi.IHideRule)
	 */
	@Override
	public void removeHideRule(IHideRule rule) throws SemanticException {

		item.removeHideRule(rule);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#removeHideRules()
	 */
	@Override
	public void removeHideRules() throws SemanticException {

		item.removeHideRules();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#removeHighlightRule(
	 * org.eclipse.birt.report.model.api.simpleapi.IHighlightRule)
	 */
	@Override
	public void removeHighlightRule(IHighlightRule rule) throws SemanticException {

		item.removeHighlightRule(rule);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#removeHighlightRules(
	 * )
	 */
	@Override
	public void removeHighlightRules() throws SemanticException {

		item.removeHighlightRules();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#setBookmark(java.lang
	 * .String)
	 */
	@Override
	public void setBookmark(String value) throws SemanticException {

		item.setBookmark(value);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#setHeight(double)
	 */
	@Override
	public void setHeight(double dimension) throws SemanticException {

		item.setHeight(dimension);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#setHeight(java.lang.
	 * String)
	 */
	@Override
	public void setHeight(String dimension) throws SemanticException {

		item.setHeight(dimension);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#setTocExpression(java
	 * .lang.String)
	 */
	@Override
	public void setTocExpression(String expression) throws SemanticException {

		item.setTocExpression(expression);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setWidth(double)
	 */
	@Override
	public void setWidth(double dimension) throws SemanticException {

		item.setWidth(dimension);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#setWidth(java.lang.
	 * String)
	 */
	@Override
	public void setWidth(String dimension) throws SemanticException {

		item.setWidth(dimension);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportElement#getComments()
	 */
	@Override
	public String getComments() {

		return item.getComments();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportElement#getCustomXml()
	 */
	@Override
	public String getCustomXml() {

		return item.getCustomXml();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportElement#getDisplayName()
	 */
	@Override
	public String getDisplayName() {

		return item.getDisplayName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportElement#getDisplayNameKey(
	 * )
	 */
	@Override
	public String getDisplayNameKey() {

		return item.getDisplayNameKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportElement#getName()
	 */
	@Override
	public String getName() {

		return item.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportElement#setComments(java.
	 * lang.String)
	 */
	@Override
	public void setComments(String theComments) throws SemanticException {

		item.setComments(theComments);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportElement#setCustomXml(java.
	 * lang.String)
	 */
	@Override
	public void setCustomXml(String customXml) throws SemanticException {

		item.setCustomXml(customXml);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportElement#setDisplayName(
	 * java.lang.String)
	 */
	@Override
	public void setDisplayName(String displayName) throws SemanticException {

		item.setDisplayName(displayName);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportElement#setDisplayNameKey(
	 * java.lang.String)
	 */
	@Override
	public void setDisplayNameKey(String displayNameKey) throws SemanticException {

		item.setDisplayNameKey(displayNameKey);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportElement#setName(java.lang.
	 * String)
	 */
	@Override
	public void setName(String name) throws SemanticException {

		item.setName(name);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getNamedExpression
	 * (java.lang.String)
	 */
	@Override
	public String getNamedExpression(String name) {

		return item.getNamedExpression(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getParent()
	 */
	@Override
	public IDesignElement getParent() {

		return item.getParent();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getQualifiedName()
	 */
	@Override
	public String getQualifiedName() {

		return item.getQualifiedName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getStyle()
	 */
	@Override
	public IStyle getStyle() {

		return item.getStyle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getUserProperty(
	 * java.lang.String)
	 */
	@Override
	public Object getUserProperty(String name) {

		return item.getUserProperty(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#setNamedExpression
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void setNamedExpression(String name, String exp) throws SemanticException {

		item.setNamedExpression(name, exp);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#setUserProperty(
	 * java.lang.String, java.lang.Object, java.lang.String)
	 */

	@Override
	public void setUserProperty(String name, Object value, String type) throws SemanticException {

		item.setUserProperty(name, value, type);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#setUserProperty(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void setUserProperty(String name, String value) throws SemanticException {

		item.setUserProperty(name, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#setCurrentView(org.
	 * eclipse.birt.report.model.api.simpleapi.IDesignElement)
	 */

	@Override
	public void setCurrentView(IDesignElement viewElement) throws SemanticException {
		item.setCurrentView(viewElement);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getReport()
	 */

	@Override
	public IReportDesign getReport() {
		return item.getReport();
	}

	/**
	 * Gets the corresponding extension item that is wrapped by the
	 * <code>MultiRowItem</code>.
	 *
	 * @return the corresponding extension item in
	 *         <code>simpleapi.IReportItem</code>
	 */

	public IReportItem getExtensionElement() {
		return item;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#
	 * getUserPropertyExpression(java.lang.String)
	 */
	@Override
	public Object getUserPropertyExpression(String name) {
		return item.getUserPropertyExpression(name);
	}
}
