/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.pdf.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * FIXME remove this class
 * 
 *
 */
public class ClonedCellContent implements ICellContent {
	protected ICellContent cellContent;
	protected int rowSpan = -1;
	protected int colSpan = -1;
	protected int column = -1;

	public ICellContent getCellContent() {
		return cellContent;
	}

	public ClonedCellContent(ICellContent cellContent, int rowSpan) {
		if (cellContent instanceof ClonedCellContent) {
			this.cellContent = ((ClonedCellContent) cellContent).cellContent;
			this.rowSpan = rowSpan;
		} else {
			this.cellContent = cellContent;
			this.rowSpan = rowSpan;
		}
	}

	public int getColSpan() {
		return cellContent.getColSpan();
	}

	public int getColumn() {
		return cellContent.getColumn();
	}

	public IColumn getColumnInstance() {
		return cellContent.getColumnInstance();
	}

	public boolean getDisplayGroupIcon() {
		return cellContent.getDisplayGroupIcon();
	}

	public int getRow() {
		return cellContent.getRow();
	}

	public int getRowSpan() {
		if (rowSpan == -1) {
			return cellContent.getRowSpan();
		}
		return rowSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public void setColumn(int column) {
		this.column = column;

	}

	public void setDisplayGroupIcon(boolean displayGroupIcon) {
		cellContent.setDisplayGroupIcon(displayGroupIcon);
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return cellContent.accept(visitor, value);
	}

	public String getBookmark() {
		return cellContent.getBookmark();
	}

	public int getContentType() {
		return cellContent.getContentType();
	}

	public Object getExtension(int extension) {
		return cellContent.getExtension(extension);
	}

	public Object getGenerateBy() {
		return cellContent.getGenerateBy();
	}

	public DimensionType getHeight() {
		return cellContent.getHeight();
	}

	public String getHelpText() {
		return cellContent.getHelpText();
	}

	public IHyperlinkAction getHyperlinkAction() {
		return cellContent.getHyperlinkAction();
	}

	public IStyle getInlineStyle() {
		return cellContent.getInlineStyle();
	}

	public InstanceID getInstanceID() {
		return cellContent.getInstanceID();
	}

	public String getName() {
		return cellContent.getName();
	}

	public IReportContent getReportContent() {
		return cellContent.getReportContent();
	}

	public String getStyleClass() {
		return cellContent.getStyleClass();
	}

	public Object getTOC() {
		return cellContent.getTOC();
	}

	public DimensionType getWidth() {
		return cellContent.getWidth();
	}

	public DimensionType getX() {
		return cellContent.getX();
	}

	public DimensionType getY() {
		return cellContent.getY();
	}

	public void readContent(DataInputStream in, ClassLoader loader) throws IOException {
		cellContent.readContent(in, loader);

	}

	public void setBookmark(String bookmark) {
		cellContent.setBookmark(bookmark);

	}

	public void setExtension(int extension, Object value) {
		cellContent.setExtension(extension, value);

	}

	public void setGenerateBy(Object generateBy) {
		cellContent.setGenerateBy(generateBy);
	}

	public void setHeight(DimensionType height) {
		cellContent.setHeight(height);

	}

	public void setHelpText(String help) {
		cellContent.setHelpText(help);

	}

	public void setHyperlinkAction(IHyperlinkAction hyperlink) {
		cellContent.setHyperlinkAction(hyperlink);

	}

	public void setInlineStyle(IStyle style) {
		cellContent.setInlineStyle(style);

	}

	public void setInstanceID(InstanceID id) {
		cellContent.setInstanceID(id);

	}

	public void setName(String name) {
		cellContent.setName(name);

	}

	public void setReportContent(IReportContent report) {
		cellContent.setReportContent(report);

	}

	public void setStyleClass(String styleClass) {
		cellContent.setStyleClass(styleClass);

	}

	public void setTOC(Object toc) {
		cellContent.setTOC(toc);

	}

	public void setWidth(DimensionType width) {
		cellContent.setWidth(width);

	}

	public void setX(DimensionType x) {
		cellContent.setX(x);

	}

	public void setY(DimensionType y) {
		cellContent.setY(y);

	}

	public void writeContent(DataOutputStream out) throws IOException {
		cellContent.writeContent(out);

	}

	public Collection getChildren() {
		return cellContent.getChildren();
	}

	public IElement getParent() {
		return cellContent.getParent();
	}

	public void setParent(IElement parent) {
		cellContent.setParent(parent);
	}

	public CSSEngine getCSSEngine() {
		return cellContent.getCSSEngine();
	}

	public IStyle getComputedStyle() {
		return cellContent.getComputedStyle();
	}

	public IStyle getStyle() {
		return cellContent.getStyle();
	}

	public IContent cloneContent(boolean isDeep) {
		return new ClonedCellContent(this, rowSpan);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#isOrientationRTL()
	 */
	public boolean isRTL() {
		return cellContent.isRTL();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IContent#isTextDirectionRTL()
	 */
	public boolean isDirectionRTL() {
		return cellContent.isDirectionRTL();
	}

	public String getACL() {
		return cellContent.getACL();
	}

	public void setACL(String acl) {
		throw new UnsupportedOperationException("setACL");
	}

	public boolean hasDiagonalLine() {
		return cellContent.hasDiagonalLine();
	}

	public void setDiagonalNumber(int diagonalNumber) {
		cellContent.setDiagonalNumber(diagonalNumber);
	}

	public int getDiagonalNumber() {
		return cellContent.getDiagonalNumber();
	}

	public void setDiagonalStyle(String diagonalStyle) {
		cellContent.setDiagonalStyle(diagonalStyle);
	}

	public String getDiagonalStyle() {
		return cellContent.getDiagonalStyle();
	}

	public void setDiagonalWidth(DimensionType diagonalWidth) {
		cellContent.setDiagonalWidth(diagonalWidth);
	}

	public DimensionType getDiagonalWidth() {
		return cellContent.getDiagonalWidth();
	}

	public void setDiagonalColor(String diagonalColor) {
		cellContent.setDiagonalColor(diagonalColor);
	}

	public String getDiagonalColor() {
		return cellContent.getDiagonalColor();
	}

	public void setAntidiagonalNumber(int antidiagonalNumber) {
		cellContent.setAntidiagonalNumber(antidiagonalNumber);
	}

	public int getAntidiagonalNumber() {
		return cellContent.getAntidiagonalNumber();
	}

	public void setAntidiagonalStyle(String antidiagonalStyle) {
		cellContent.setAntidiagonalStyle(antidiagonalStyle);
	}

	public String getAntidiagonalStyle() {
		return cellContent.getAntidiagonalStyle();
	}

	public void setAntidiagonalWidth(DimensionType antidiagonalWidth) {
		cellContent.setAntidiagonalWidth(antidiagonalWidth);
	}

	public DimensionType getAntidiagonalWidth() {
		return cellContent.getAntidiagonalWidth();
	}

	public void setAntidiagonalColor(String antidiagonalColor) {
		cellContent.setAntidiagonalColor(antidiagonalColor);
	}

	public String getAntidiagonalColor() {
		return cellContent.getAntidiagonalColor();
	}

	public String getHeaders() {
		return cellContent.getHeaders();
	}

	public String getScope() {
		return cellContent.getScope();
	}

	public void setHeaders(String headers) {
		cellContent.setHeaders(headers);
	}

	public void setScope(String scope) {
		cellContent.setScope(scope);
	}

	public boolean repeatContent() {
		return cellContent.repeatContent();
	}

	public void setRepeatContent(boolean repeatContent) {
		cellContent.setRepeatContent(repeatContent);
	}

	public IBaseResultSet getResultSet() {
		return cellContent.getResultSet();
	}

	public boolean isLastChild() {
		return cellContent.isLastChild();
	}

	public void setLastChild(boolean isLastChild) {
		cellContent.setLastChild(isLastChild);
	}

	public boolean hasChildren() {
		return cellContent.hasChildren();
	}

	public void setHasChildren(boolean hasChildren) {
		cellContent.setHasChildren(hasChildren);
	}

	public Map<String, Object> getUserProperties() {
		return cellContent.getUserProperties();
	}

	public void setUserProperties(Map<String, Object> properties) {
		cellContent.setUserProperties(properties);
	}

	public Map<String, Object> getExtensions() {
		return cellContent.getExtensions();
	}

	public void setExtensions(Map<String, Object> properties) {
		cellContent.setExtensions(properties);
	}

	public String getAltText() {
		return cellContent.getAltText();
	}

	public String getAltTextKey() {
		return cellContent.getAltTextKey();
	}

	public void setAltText(String alt) {
		cellContent.setAltText(alt);
	}

	public void setAltTextKey(String altKey) {
		cellContent.setAltText(altKey);
	}
}
