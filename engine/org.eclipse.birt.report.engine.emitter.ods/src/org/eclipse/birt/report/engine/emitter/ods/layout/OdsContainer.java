/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.emitter.ods.layout;

import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public class OdsContainer {
	private StyleEntry style;
	private ContainerSizeInfo sizeInfo;
	private HyperlinkInfo link;
	private int startRowId;
	private boolean empty;
	private OdsContainer parent;
	private int rowIndex;

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public OdsContainer(StyleEntry style, OdsContainer parent) {
		this(style, parent.getSizeInfo(), parent);
	}

	public OdsContainer(StyleEntry style, ContainerSizeInfo sizeInfo, OdsContainer parent) {
		this.style = style;
		this.sizeInfo = sizeInfo;
		this.parent = parent;
		this.rowIndex = parent != null ? parent.rowIndex : 0;
		empty = true;
		this.startRowId = rowIndex;
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

	public HyperlinkInfo getLink() {
		return link;
	}

	public void setLink(HyperlinkInfo link) {
		this.link = link;
	}

	public int getStartRowId() {
		return startRowId;
	}

	public void setStartRowId(int startRowId) {
		this.startRowId = startRowId;
	}

	public OdsContainer getParent() {
		return parent;
	}
}
