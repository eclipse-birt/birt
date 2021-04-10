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

package org.eclipse.birt.report.model.api.extension;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IDataBinding;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;
import org.eclipse.birt.report.model.api.simpleapi.IHighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.IReportItem;
import org.eclipse.birt.report.model.api.simpleapi.ISimpleElementFactory;
import org.eclipse.birt.report.model.api.simpleapi.IStyle;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of simple item that has no data rows.The constructor in sub class
 * must call super(ExtendedItemHandle).
 * 
 */

public class SimpleRowItem implements IReportItem {

	private final IReportItem item;

	/**
	 * Constructors.
	 * 
	 * @param item
	 */

	public SimpleRowItem(ExtendedItemHandle item) {
		this.item = SimpleElementFactory.getInstance().wrapExtensionElement(item,
				ISimpleElementFactory.SIMPLE_ROW_ITEM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#addDataBinding(org.
	 * eclipse.birt.report.model.api.simpleapi.IDataBinding)
	 */
	public void addDataBinding(IDataBinding binding) throws SemanticException {
		item.addDataBinding(binding);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#addHideRule(org.
	 * eclipse.birt.report.model.api.simpleapi.IHideRule)
	 */
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
	public void addHighlightRule(IHighlightRule rule) throws SemanticException {
		item.addHighlightRule(rule);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getBookmark()
	 */
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
	public String getDataBinding(String bindingName) {
		return item.getDataBinding(bindingName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#getDataBindings()
	 */
	public IDataBinding[] getDataBindings() {

		return item.getDataBindings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getHeight()
	 */
	public String getHeight() {

		return item.getHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getHideRules()
	 */
	public IHideRule[] getHideRules() {

		return item.getHideRules();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#getHighlightRules()
	 */
	public IHighlightRule[] getHighlightRules() {

		return item.getHighlightRules();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#getTocExpression()
	 */
	public String getTocExpression() {

		return item.getTocExpression();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getWidth()
	 */
	public String getWidth() {

		return item.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getX()
	 */
	public String getX() {

		return item.getX();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#getY()
	 */
	public String getY() {

		return item.getY();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#removeDataBinding(
	 * java.lang.String)
	 */
	public void removeDataBinding(String bindingName) throws SemanticException {

		item.removeDataBinding(bindingName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#removeDataBindings()
	 */
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
	public void removeHideRule(IHideRule rule) throws SemanticException {

		item.removeHideRule(rule);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#removeHideRules()
	 */
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
	public void setBookmark(String value) throws SemanticException {

		item.setBookmark(value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportItem#setHeight(double)
	 */
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
	public void setTocExpression(String expression) throws SemanticException {

		item.setTocExpression(expression);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setWidth(double)
	 */
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
	public void setWidth(String dimension) throws SemanticException {

		item.setWidth(dimension);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setX(double)
	 */
	public void setX(double dimension) throws SemanticException {

		item.setX(dimension);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setX(java.lang.
	 * String)
	 */
	public void setX(String dimension) throws SemanticException {

		item.setX(dimension);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setY(double)
	 */
	public void setY(double dimension) throws SemanticException {

		item.setY(dimension);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportItem#setY(java.lang.
	 * String)
	 */
	public void setY(String dimension) throws SemanticException {

		item.setY(dimension);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportElement#getComments()
	 */
	public String getComments() {

		return item.getComments();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportElement#getCustomXml()
	 */
	public String getCustomXml() {

		return item.getCustomXml();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportElement#getDisplayName()
	 */
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
	public String getDisplayNameKey() {

		return item.getDisplayNameKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportElement#getName()
	 */
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
	public String getNamedExpression(String name) {

		return item.getNamedExpression(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getParent()
	 */
	public IDesignElement getParent() {

		return item.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getQualifiedName()
	 */
	public String getQualifiedName() {

		return item.getQualifiedName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getStyle()
	 */
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

	public void setCurrentView(IDesignElement viewElement) throws SemanticException {
		item.setCurrentView(viewElement);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getReport()
	 */
	public IReportDesign getReport() {
		return item.getReport();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#
	 * getUserPropertyExpression(java.lang.String)
	 */
	public Object getUserPropertyExpression(String name) {
		return item.getUserPropertyExpression(name);
	}

}
