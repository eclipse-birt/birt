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

	@Override
	public int getColSpan() {
		return cellContent.getColSpan();
	}

	@Override
	public int getColumn() {
		return cellContent.getColumn();
	}

	@Override
	public IColumn getColumnInstance() {
		return cellContent.getColumnInstance();
	}

	@Override
	public boolean getDisplayGroupIcon() {
		return cellContent.getDisplayGroupIcon();
	}

	@Override
	public int getRow() {
		return cellContent.getRow();
	}

	@Override
	public int getRowSpan() {
		if (rowSpan == -1) {
			return cellContent.getRowSpan();
		}
		return rowSpan;
	}

	@Override
	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	@Override
	public void setColumn(int column) {
		this.column = column;

	}

	@Override
	public void setDisplayGroupIcon(boolean displayGroupIcon) {
		cellContent.setDisplayGroupIcon(displayGroupIcon);
	}

	@Override
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	@Override
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return cellContent.accept(visitor, value);
	}

	@Override
	public String getBookmark() {
		return cellContent.getBookmark();
	}

	@Override
	public int getContentType() {
		return cellContent.getContentType();
	}

	@Override
	public Object getExtension(int extension) {
		return cellContent.getExtension(extension);
	}

	@Override
	public Object getGenerateBy() {
		return cellContent.getGenerateBy();
	}

	@Override
	public DimensionType getHeight() {
		return cellContent.getHeight();
	}

	@Override
	public String getHelpText() {
		return cellContent.getHelpText();
	}

	@Override
	public IHyperlinkAction getHyperlinkAction() {
		return cellContent.getHyperlinkAction();
	}

	@Override
	public IStyle getInlineStyle() {
		return cellContent.getInlineStyle();
	}

	@Override
	public InstanceID getInstanceID() {
		return cellContent.getInstanceID();
	}

	@Override
	public String getName() {
		return cellContent.getName();
	}

	@Override
	public IReportContent getReportContent() {
		return cellContent.getReportContent();
	}

	@Override
	public String getStyleClass() {
		return cellContent.getStyleClass();
	}

	@Override
	public Object getTOC() {
		return cellContent.getTOC();
	}

	@Override
	public DimensionType getWidth() {
		return cellContent.getWidth();
	}

	@Override
	public DimensionType getX() {
		return cellContent.getX();
	}

	@Override
	public DimensionType getY() {
		return cellContent.getY();
	}

	@Override
	public void readContent(DataInputStream in, ClassLoader loader) throws IOException {
		cellContent.readContent(in, loader);

	}

	@Override
	public void setBookmark(String bookmark) {
		cellContent.setBookmark(bookmark);

	}

	@Override
	public void setExtension(int extension, Object value) {
		cellContent.setExtension(extension, value);

	}

	@Override
	public void setGenerateBy(Object generateBy) {
		cellContent.setGenerateBy(generateBy);
	}

	@Override
	public void setHeight(DimensionType height) {
		cellContent.setHeight(height);

	}

	@Override
	public void setHelpText(String help) {
		cellContent.setHelpText(help);

	}

	@Override
	public void setHyperlinkAction(IHyperlinkAction hyperlink) {
		cellContent.setHyperlinkAction(hyperlink);

	}

	@Override
	public void setInlineStyle(IStyle style) {
		cellContent.setInlineStyle(style);

	}

	@Override
	public void setInstanceID(InstanceID id) {
		cellContent.setInstanceID(id);

	}

	@Override
	public void setName(String name) {
		cellContent.setName(name);

	}

	@Override
	public void setReportContent(IReportContent report) {
		cellContent.setReportContent(report);

	}

	@Override
	public void setStyleClass(String styleClass) {
		cellContent.setStyleClass(styleClass);

	}

	@Override
	public void setTOC(Object toc) {
		cellContent.setTOC(toc);

	}

	@Override
	public void setWidth(DimensionType width) {
		cellContent.setWidth(width);

	}

	@Override
	public void setX(DimensionType x) {
		cellContent.setX(x);

	}

	@Override
	public void setY(DimensionType y) {
		cellContent.setY(y);

	}

	@Override
	public void writeContent(DataOutputStream out) throws IOException {
		cellContent.writeContent(out);

	}

	@Override
	public Collection getChildren() {
		return cellContent.getChildren();
	}

	@Override
	public IElement getParent() {
		return cellContent.getParent();
	}

	@Override
	public void setParent(IElement parent) {
		cellContent.setParent(parent);
	}

	@Override
	public CSSEngine getCSSEngine() {
		return cellContent.getCSSEngine();
	}

	@Override
	public IStyle getComputedStyle() {
		return cellContent.getComputedStyle();
	}

	@Override
	public IStyle getStyle() {
		return cellContent.getStyle();
	}

	@Override
	public IContent cloneContent(boolean isDeep) {
		return new ClonedCellContent(this, rowSpan);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IContent#isOrientationRTL()
	 */
	@Override
	public boolean isRTL() {
		return cellContent.isRTL();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IContent#isTextDirectionRTL()
	 */
	@Override
	public boolean isDirectionRTL() {
		return cellContent.isDirectionRTL();
	}

	@Override
	public String getACL() {
		return cellContent.getACL();
	}

	@Override
	public void setACL(String acl) {
		throw new UnsupportedOperationException("setACL");
	}

	@Override
	public boolean hasDiagonalLine() {
		return cellContent.hasDiagonalLine();
	}

	@Override
	public void setDiagonalNumber(int diagonalNumber) {
		cellContent.setDiagonalNumber(diagonalNumber);
	}

	@Override
	public int getDiagonalNumber() {
		return cellContent.getDiagonalNumber();
	}

	@Override
	public void setDiagonalStyle(String diagonalStyle) {
		cellContent.setDiagonalStyle(diagonalStyle);
	}

	@Override
	public String getDiagonalStyle() {
		return cellContent.getDiagonalStyle();
	}

	@Override
	public void setDiagonalWidth(DimensionType diagonalWidth) {
		cellContent.setDiagonalWidth(diagonalWidth);
	}

	@Override
	public DimensionType getDiagonalWidth() {
		return cellContent.getDiagonalWidth();
	}

	@Override
	public void setDiagonalColor(String diagonalColor) {
		cellContent.setDiagonalColor(diagonalColor);
	}

	@Override
	public String getDiagonalColor() {
		return cellContent.getDiagonalColor();
	}

	@Override
	public void setAntidiagonalNumber(int antidiagonalNumber) {
		cellContent.setAntidiagonalNumber(antidiagonalNumber);
	}

	@Override
	public int getAntidiagonalNumber() {
		return cellContent.getAntidiagonalNumber();
	}

	@Override
	public void setAntidiagonalStyle(String antidiagonalStyle) {
		cellContent.setAntidiagonalStyle(antidiagonalStyle);
	}

	@Override
	public String getAntidiagonalStyle() {
		return cellContent.getAntidiagonalStyle();
	}

	@Override
	public void setAntidiagonalWidth(DimensionType antidiagonalWidth) {
		cellContent.setAntidiagonalWidth(antidiagonalWidth);
	}

	@Override
	public DimensionType getAntidiagonalWidth() {
		return cellContent.getAntidiagonalWidth();
	}

	@Override
	public void setAntidiagonalColor(String antidiagonalColor) {
		cellContent.setAntidiagonalColor(antidiagonalColor);
	}

	@Override
	public String getAntidiagonalColor() {
		return cellContent.getAntidiagonalColor();
	}

	@Override
	public String getHeaders() {
		return cellContent.getHeaders();
	}

	@Override
	public String getScope() {
		return cellContent.getScope();
	}

	@Override
	public void setHeaders(String headers) {
		cellContent.setHeaders(headers);
	}

	@Override
	public void setScope(String scope) {
		cellContent.setScope(scope);
	}

	@Override
	public boolean repeatContent() {
		return cellContent.repeatContent();
	}

	@Override
	public void setRepeatContent(boolean repeatContent) {
		cellContent.setRepeatContent(repeatContent);
	}

	@Override
	public IBaseResultSet getResultSet() {
		return cellContent.getResultSet();
	}

	@Override
	public boolean isLastChild() {
		return cellContent.isLastChild();
	}

	@Override
	public void setLastChild(boolean isLastChild) {
		cellContent.setLastChild(isLastChild);
	}

	@Override
	public boolean hasChildren() {
		return cellContent.hasChildren();
	}

	@Override
	public void setHasChildren(boolean hasChildren) {
		cellContent.setHasChildren(hasChildren);
	}

	@Override
	public Map<String, Object> getUserProperties() {
		return cellContent.getUserProperties();
	}

	@Override
	public void setUserProperties(Map<String, Object> properties) {
		cellContent.setUserProperties(properties);
	}

	@Override
	public Map<String, Object> getExtensions() {
		return cellContent.getExtensions();
	}

	@Override
	public void setExtensions(Map<String, Object> properties) {
		cellContent.setExtensions(properties);
	}

	@Override
	public String getAltText() {
		return cellContent.getAltText();
	}

	@Override
	public String getAltTextKey() {
		return cellContent.getAltTextKey();
	}

	@Override
	public void setAltText(String alt) {
		cellContent.setAltText(alt);
	}

	private String tagType;

	@Override
	public String getTagType() {
		return tagType;
	}

	@Override
	public void setTagType(String tagType) {
		this.tagType = tagType;

	}

	@Override
	public void setAltTextKey(String altKey) {
		cellContent.setAltText(altKey);
	}
}
