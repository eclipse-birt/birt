/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;

public class PageArea extends ContainerArea {
	final static int DEFAULT_PAGE_WIDTH = 595275;
	final static int DEFAULT_PAGE_HEIGHT = 841889;

	private boolean extendToMultiplePages = false;

	private boolean enlargePageSize = false;

	protected IContainerArea root;
	protected IContainerArea body;
	protected IContainerArea header;
	protected IContainerArea footer;
	protected IContainerArea left;
	protected IContainerArea right;

//	protected int position;

	public PageArea(IPageContent page) {
		super(page);
		// style = new AreaStyle((ComputedStyle)page.getPageBody().getComputedStyle());
	}

	public int getColumnNumber() {
		// FIXME support multi-column page
		return 1;
	}

	public IContainerArea getHeader() {
		return header;
	}

	public void removeHeader() {
		((ContainerArea) root).removeChild(header);
		header = null;
	}

	public void removeFooter() {
		((ContainerArea) root).removeChild(footer);
		footer = null;
	}

	public IContainerArea getFooter() {
		return footer;
	}

	public IContainerArea getLeft() {
		return left;
	}

	public IContainerArea getRight() {
		return right;
	}

	public IContainerArea getBody() {
		return body;
	}

	public void addChild(IArea area) {
		if (area != null) {
			body.addChild(area);
		}
	}

	public IContainerArea copyArea() {
		throw new UnsupportedOperationException();
	}

	public IContainerArea getRoot() {
		return root;
	}

	public void setRoot(IContainerArea root) {
		this.root = root;
		this.children.add(root);
	}

	public void setBody(IContainerArea body) {
		this.body = body;
	}

	public void setHeader(IContainerArea header) {
		this.header = header;
	}

	public void setFooter(IContainerArea footer) {
		this.footer = footer;
	}

	public void setLeft(IContainerArea left) {
		this.left = left;
	}

	public void setRight(IContainerArea right) {
		this.right = right;
	}

	public boolean isExtendToMultiplePages() {
		return extendToMultiplePages;
	}

	public void setExtendToMultiplePages(boolean extendToMultiplePages) {
		this.extendToMultiplePages = extendToMultiplePages;
	}

	public boolean isEnlargePageSize() {
		return enlargePageSize;
	}

	public void setEnlargePageSize(boolean enlargePageSize) {
		this.enlargePageSize = enlargePageSize;
	}

}
