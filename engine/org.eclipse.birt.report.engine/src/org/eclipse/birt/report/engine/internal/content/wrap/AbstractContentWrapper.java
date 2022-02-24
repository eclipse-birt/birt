/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.content.wrap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.AbstractElement;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.ir.DimensionType;

abstract public class AbstractContentWrapper extends AbstractElement implements IContent {
	protected IContent content;

	public AbstractContentWrapper(IContent content) {
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#accept(org.eclipse.birt.
	 * report.engine.content.IContentVisitor, java.lang.Object)
	 */
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return content.accept(visitor, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getBookmark()
	 */
	public String getBookmark() {
		return content.getBookmark();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.css.engine.CSSStylableElement#getComputedStyle
	 * ()
	 */
	public IStyle getComputedStyle() {
		return content.getComputedStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getContentType()
	 */
	public int getContentType() {
		return content.getContentType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.css.engine.CSSStylableElement#getCSSEngine()
	 */
	public CSSEngine getCSSEngine() {
		return content.getCSSEngine();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getGenerateBy()
	 */
	public Object getGenerateBy() {
		return content.getGenerateBy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getHeight()
	 */
	public DimensionType getHeight() {
		return content.getHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getAltText()
	 */
	public String getAltText() {
		return content.getAltText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getAltTextKey()
	 */
	public String getAltTextKey() {
		return content.getAltTextKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getHelpText()
	 */
	public String getHelpText() {
		return content.getHelpText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getHyperlinkAction()
	 */
	public IHyperlinkAction getHyperlinkAction() {
		return content.getHyperlinkAction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getInlineStyle()
	 */
	public IStyle getInlineStyle() {
		return content.getInlineStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getInstanceID()
	 */
	public InstanceID getInstanceID() {
		return content.getInstanceID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getName()
	 */
	public String getName() {
		return content.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IElement#getParent()
	 */
	public IElement getParent() {
		return content.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getReportContent()
	 */
	public IReportContent getReportContent() {
		return content.getReportContent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.css.engine.CSSStylableElement#getStyle()
	 */
	public IStyle getStyle() {
		return content.getStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getStyleClass()
	 */
	public String getStyleClass() {
		return content.getStyleClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getTOC()
	 */
	public Object getTOC() {
		return content.getTOC();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getWidth()
	 */
	public DimensionType getWidth() {
		return content.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getX()
	 */
	public DimensionType getX() {
		return content.getX();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#getY()
	 */
	public DimensionType getY() {
		return content.getY();
	}

	public Object getExtension(int extension) {
		return content.getExtension(extension);
	}

	public void setExtension(int extension, Object value) {
		content.setExtension(extension, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#readContent(java.io.
	 * DataInputStream)
	 */
	public void readContent(DataInputStream in, ClassLoader loader) throws IOException {
		throw new IOException("Not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setBookmark(java.lang.String)
	 */
	public void setBookmark(String bookmark) {
		content.setBookmark(bookmark);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#setGenerateBy(java.lang.
	 * Object)
	 */
	public void setGenerateBy(Object generateBy) {
		content.setGenerateBy(generateBy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setHeight(org.eclipse.birt.
	 * report.engine.ir.DimensionType)
	 */
	public void setHeight(DimensionType height) {
		content.setHeight(height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setAltText(java.lang.String)
	 */
	public void setAltText(String alt) {
		content.setAltText(alt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#setAltTextKey(java.lang.
	 * String)
	 */
	public void setAltTextKey(String altKey) {
		content.setAltText(altKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setHelpText(java.lang.String)
	 */
	public void setHelpText(String help) {
		content.setHelpText(help);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#setHyperlinkAction(org.
	 * eclipse.birt.report.engine.content.IHyperlinkAction)
	 */
	public void setHyperlinkAction(IHyperlinkAction hyperlink) {
		content.setHyperlinkAction(hyperlink);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setInlineStyle(org.eclipse.
	 * birt.report.engine.content.IStyle)
	 */
	public void setInlineStyle(IStyle style) {
		content.setInlineStyle(style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setInstanceID(org.eclipse.
	 * birt.report.engine.api.InstanceID)
	 */
	public void setInstanceID(InstanceID id) {
		content.setInstanceID(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setName(java.lang.String)
	 */
	public void setName(String name) {
		content.setName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IElement#setParent(org.eclipse.birt.
	 * report.engine.content.IElement)
	 */
	public void setParent(IElement parent) {
		content.setParent(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setReportContent(org.eclipse.
	 * birt.report.engine.content.IReportContent)
	 */
	public void setReportContent(IReportContent report) {
		content.setReportContent(report);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#setStyleClass(java.lang.
	 * String)
	 */
	public void setStyleClass(String styleClass) {
		content.setStyleClass(styleClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#setTOC(java.lang.String)
	 */
	public void setTOC(Object toc) {
		content.setTOC(toc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setWidth(org.eclipse.birt.
	 * report.engine.ir.DimensionType)
	 */
	public void setWidth(DimensionType width) {
		content.setWidth(width);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setX(org.eclipse.birt.report.
	 * engine.ir.DimensionType)
	 */
	public void setX(DimensionType x) {
		content.setX(x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IContent#setY(org.eclipse.birt.report.
	 * engine.ir.DimensionType)
	 */
	public void setY(DimensionType y) {
		content.setY(y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#writeContent(java.io.
	 * DataOutputStream)
	 */
	public void writeContent(DataOutputStream out) throws IOException {
		throw new IOException("not supported");
	}

	protected void throwUnsupportedException() {
		throw new UnsupportedOperationException("Unsupported Exception");
	}

	public IContent cloneContent(boolean isDeep) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#isOrientationRTL()
	 */
	public boolean isRTL() {
		return content.isRTL();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#isTextDirectionRTL()
	 */
	public boolean isDirectionRTL() {
		return content.isDirectionRTL();
	}

	public String getACL() {
		return content.getACL();
	}

	public void setACL(String acl) {
		throw new UnsupportedOperationException("setACL");
	}

	public IBaseResultSet getResultSet() {
		return content.getResultSet();
	}

	public boolean isLastChild() {
		return content.isLastChild();
	}

	public void setLastChild(boolean isLastChild) {
		content.setLastChild(isLastChild);
	}

	public boolean hasChildren() {
		return content.hasChildren();
	}

	public void setHasChildren(boolean hasChildren) {
		content.setHasChildren(hasChildren);
	}

	public IContent getContent() {
		return content;
	}

	public Map<String, Object> getUserProperties() {
		return content.getUserProperties();
	}

	public void setUserProperties(Map<String, Object> properties) {
		content.setUserProperties(properties);
	}

	public Map<String, Object> getExtensions() {
		return content.getExtensions();
	}

	public void setExtensions(Map<String, Object> properties) {
		content.setExtensions(properties);
	}
}
