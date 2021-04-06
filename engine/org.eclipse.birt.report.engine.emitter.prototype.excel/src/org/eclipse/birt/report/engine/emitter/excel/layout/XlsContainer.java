
package org.eclipse.birt.report.engine.emitter.excel.layout;

import org.eclipse.birt.report.engine.emitter.excel.HyperlinkDef;
import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;

public class XlsContainer {
	private StyleEntry style;
	private ContainerSizeInfo sizeInfo;
	private HyperlinkDef link;
	private int startRow;
	private boolean empty;
	private XlsContainer parent;
	private int endRow;
	private String bookmark;
	private boolean isFirstChild = true;

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public XlsContainer(StyleEntry style, XlsContainer parent) {
		this(style, parent.getSizeInfo(), parent);
	}

	public XlsContainer(StyleEntry style, ContainerSizeInfo sizeInfo, XlsContainer parent) {
		this.style = style;
		this.sizeInfo = sizeInfo;
		this.parent = parent;
		this.endRow = parent != null ? parent.endRow : 0;
		empty = true;
		this.startRow = endRow;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public StyleEntry getStyle() {
		return style;
	}

	public void setStyle(StyleEntry style) {
		this.style = style;
	}

	public ContainerSizeInfo getSizeInfo() {
		return sizeInfo;
	}

	public void setSizeInfo(ContainerSizeInfo sizeInfo) {
		this.sizeInfo = sizeInfo;
	}

	public HyperlinkDef getLink() {
		return link;
	}

	public void setLink(HyperlinkDef link) {
		this.link = link;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public XlsContainer getParent() {
		return parent;
	}

	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}

	public String getBookmark() {
		return bookmark;
	}

	public boolean isFirstChild() {
		return isFirstChild;
	}

	public void setIsFirstChild(boolean isFirstChild) {
		this.isFirstChild = isFirstChild;
	}
}
