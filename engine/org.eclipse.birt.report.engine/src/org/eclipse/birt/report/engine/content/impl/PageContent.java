/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;

public class PageContent extends AbstractContent implements IPageContent {

	protected String orientation;
	protected String pageType;
	protected DimensionType pageHeight;
	protected DimensionType pageWidth;
	protected DimensionType headerHeight;
	protected DimensionType footerHeight;
	protected DimensionType leftWidth;
	protected DimensionType rightWidth;
	protected DimensionType marginTop;
	protected DimensionType marginLeft;
	protected DimensionType marginRight;
	protected DimensionType marginBottom;
	transient protected IImageContent waterMark;

	protected long pageNumber = -1;

	public int getContentType() {
		return PAGE_CONTENT;
	}

	protected PageContent(ReportContent report) {
		super(report);
		children = new ArrayList();
	}

	protected PageContent(IPageContent page) {
		super(page);
		this.orientation = page.getOrientation();
		this.pageType = page.getPageType();
		this.pageHeight = page.getPageHeight();
		this.pageWidth = page.getPageWidth();
		this.headerHeight = page.getHeaderHeight();
		this.footerHeight = page.getFooterHeight();
		this.leftWidth = page.getLeftWidth();
		this.rightWidth = page.getRightWidth();
		this.marginTop = page.getMarginTop();
		this.marginLeft = page.getMarginLeft();
		this.marginRight = page.getMarginRight();
		this.marginBottom = page.getMarginBottom();
		this.waterMark = page.getWaterMark();
		this.pageNumber = page.getPageNumber();
	}

	public void setGenerateBy(Object design) {
		super.setGenerateBy(design);
		if (design instanceof MasterPageDesign) {
			MasterPageDesign page = (MasterPageDesign) design;

			orientation = page.getOrientation();
			pageType = page.getPageType();
			pageHeight = page.getPageHeight();
			pageWidth = page.getPageWidth();
			marginTop = page.getTopMargin();
			marginLeft = page.getLeftMargin();
			marginRight = page.getRightMargin();
			marginBottom = page.getBottomMargin();
			if (page instanceof SimpleMasterPageDesign) {
				headerHeight = ((SimpleMasterPageDesign) page).getHeaderHeight();
				footerHeight = ((SimpleMasterPageDesign) page).getFooterHeight();
			}
			if (headerHeight == null) {
				headerHeight = new DimensionType(0.25f, EngineIRConstants.UNITS_IN);
			}
			if (footerHeight == null) {
				footerHeight = new DimensionType(0.25f, EngineIRConstants.UNITS_IN);
			}
		}
	}

	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitPage(this, value);
	}

	MasterPageDesign getMasterPage() {
		return (MasterPageDesign) this.generateBy;
	}

	public String getOrientation() {
		return orientation;
	}

	public String getPageType() {
		return pageType;
	}

	public DimensionType getPageHeight() {
		return pageHeight;
	}

	public DimensionType getPageWidth() {
		return pageWidth;
	}

	public IImageContent getWaterMark() {
		return waterMark;
	}

	/**
	 * @deprecated
	 */
	public Collection getHeader() {
		IContent header = getPageHeader();
		if (header != null) {
			return header.getChildren();
		}
		return null;
	}

	/**
	 * @deprecated
	 */
	public Collection getFooter() {
		IContent footer = getPageFooter();
		if (footer != null) {
			return footer.getChildren();
		}
		return null;
	}

	public void setPageHeader(IContent header) {
		ArrayList cs = (ArrayList) children;
		if (cs.size() >= 1) {
			cs.set(0, header);
		} else {
			cs.ensureCapacity(1);
			cs.add(0, header);
		}
	}

	public void setPageBody(IContent body) {
		ArrayList cs = (ArrayList) children;
		if (cs.size() >= 2) {
			cs.set(1, body);
		} else {
			cs.ensureCapacity(2);
			cs.add(1, body);
		}
	}

	public void setPageFooter(IContent footer) {
		ArrayList cs = (ArrayList) children;
		if (cs.size() >= 3) {
			cs.set(2, footer);
		} else {
			cs.ensureCapacity(3);
			cs.add(2, footer);
		}
	}

	public IContent getPageHeader() {
		return (IContent) ((ArrayList) children).get(0);
	}

	public IContent getPageBody() {
		return (IContent) ((ArrayList) children).get(1);
	}

	public IContent getPageFooter() {
		return (IContent) ((ArrayList) children).get(2);
	}

	/**
	 * @param orientation The orientation to set.
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	/**
	 * @param pageHeight The pageHeight to set.
	 */
	public void setPageHeight(DimensionType pageHeight) {
		this.pageHeight = pageHeight;
	}

	/**
	 * @param pageType The pageType to set.
	 */
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	/**
	 * @param pageWidth The pageWidth to set.
	 */
	public void setPageWidth(DimensionType pageWidth) {
		this.pageWidth = pageWidth;
	}

	/**
	 * @param waterMark The waterMark to set.
	 */
	public void setWaterMark(IImageContent waterMark) {
		this.waterMark = waterMark;
	}

	public DimensionType getMarginTop() {
		return this.marginTop;
	}

	public DimensionType getMarginBottom() {
		return this.marginBottom;
	}

	public DimensionType getMarginLeft() {
		return this.marginLeft;
	}

	public DimensionType getMarginRight() {
		return this.marginRight;
	}

	public DimensionType getHeaderHeight() {
		return headerHeight;
	}

	public DimensionType getFooterHeight() {
		return footerHeight;
	}

	public DimensionType getLeftWidth() {
		return leftWidth;
	}

	public DimensionType getRightWidth() {
		return rightWidth;
	}

	/**
	 * @deprecated
	 */
	public IStyle getContentComputedStyle() {
		IContent body = getPageBody();
		return body.getComputedStyle();
	}

	/**
	 * @deprecated
	 */
	public IStyle getContentStyle() {
		IContent body = getPageBody();
		return body.getStyle();
	}

	public void setPageNumber(long pn) {
		this.pageNumber = pn;
	}

	public long getPageNumber() {
		return this.pageNumber;
	}

	static final protected short FIELD_ORIENTATION = 700;
	static final protected short FIELD_PAGETYPE = 701;
	static final protected short FIELD_PAGEHEIGHT = 702;
	static final protected short FIELD_PAGEWIDTH = 703;
	static final protected short FIELD_HEADERHEIGHT = 704;
	static final protected short FIELD_FOOTERHEIGHT = 705;
	static final protected short FIELD_LEFTWIDTH = 706;
	static final protected short FIELD_RIGHTWIDTH = 707;
	static final protected short FIELD_MARGINTOP = 708;
	static final protected short FIELD_MARGINLEFT = 709;
	static final protected short FIELD_MARGINRIGHT = 710;
	static final protected short FIELD_MARGINBUTTOM = 711;
	static final protected short FIELD_PAGENUMBER = 712;

	protected void writeFields(DataOutputStream out) throws IOException {
		super.writeFields(out);
		if (orientation != null) {
			IOUtil.writeShort(out, FIELD_ORIENTATION);
			IOUtil.writeString(out, orientation);
		}
		if (pageType != null) {
			IOUtil.writeShort(out, FIELD_PAGETYPE);
			IOUtil.writeString(out, pageType);
		}
		if (pageHeight != null) {
			IOUtil.writeShort(out, FIELD_PAGEHEIGHT);
			pageHeight.writeObject(out);
		}
		if (pageWidth != null) {
			IOUtil.writeShort(out, FIELD_PAGEWIDTH);
			pageWidth.writeObject(out);
		}
		if (headerHeight != null) {
			IOUtil.writeShort(out, FIELD_HEADERHEIGHT);
			headerHeight.writeObject(out);
		}
		if (footerHeight != null) {
			IOUtil.writeShort(out, FIELD_FOOTERHEIGHT);
			footerHeight.writeObject(out);
		}
		if (leftWidth != null) {
			IOUtil.writeShort(out, FIELD_LEFTWIDTH);
			leftWidth.writeObject(out);
		}
		if (rightWidth != null) {
			IOUtil.writeShort(out, FIELD_RIGHTWIDTH);
			rightWidth.writeObject(out);
		}
		if (marginTop != null) {
			IOUtil.writeShort(out, FIELD_MARGINTOP);
			marginTop.writeObject(out);
		}
		if (marginLeft != null) {
			IOUtil.writeShort(out, FIELD_MARGINLEFT);
			marginLeft.writeObject(out);
		}
		if (marginRight != null) {
			IOUtil.writeShort(out, FIELD_MARGINRIGHT);
			marginRight.writeObject(out);
		}
		if (marginBottom != null) {
			IOUtil.writeShort(out, FIELD_MARGINBUTTOM);
			marginBottom.writeObject(out);
		}
		if (pageNumber != -1) {
			IOUtil.writeShort(out, FIELD_PAGENUMBER);
			IOUtil.writeLong(out, pageNumber);
		}
	}

	public boolean needSave() {
		return true;
	}

	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_ORIENTATION:
			orientation = IOUtil.readString(in);
			break;
		case FIELD_PAGETYPE:
			pageType = IOUtil.readString(in);
			break;
		case FIELD_PAGEHEIGHT:
			pageHeight = new DimensionType();
			pageHeight.readObject(in);
			break;
		case FIELD_PAGEWIDTH:
			pageWidth = new DimensionType();
			pageWidth.readObject(in);
			break;
		case FIELD_HEADERHEIGHT:
			headerHeight = new DimensionType();
			headerHeight.readObject(in);
			break;
		case FIELD_FOOTERHEIGHT:
			footerHeight = new DimensionType();
			footerHeight.readObject(in);
			break;
		case FIELD_LEFTWIDTH:
			leftWidth = new DimensionType();
			leftWidth.readObject(in);
			break;
		case FIELD_RIGHTWIDTH:
			rightWidth = new DimensionType();
			rightWidth.readObject(in);
			break;
		case FIELD_MARGINTOP:
			marginTop = new DimensionType();
			marginTop.readObject(in);
			break;
		case FIELD_MARGINLEFT:
			marginLeft = new DimensionType();
			marginLeft.readObject(in);
			break;
		case FIELD_MARGINRIGHT:
			marginRight = new DimensionType();
			marginRight.readObject(in);
			break;
		case FIELD_MARGINBUTTOM:
			marginBottom = new DimensionType();
			marginBottom.readObject(in);
			break;
		case FIELD_PAGENUMBER:
			pageNumber = IOUtil.readLong(in);
			break;
		default:
			super.readField(version, filedId, in, loader);
		}
	}

	protected IContent cloneContent() {
		return new PageContent(this);
	}

}