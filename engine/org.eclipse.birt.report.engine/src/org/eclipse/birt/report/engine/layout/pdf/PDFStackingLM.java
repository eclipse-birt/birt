/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutContext;
import org.eclipse.birt.report.engine.layout.IStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public abstract class PDFStackingLM extends PDFAbstractLM implements IStackingLayoutManager, ILayoutContext {

	protected int maxWidth = -1;

	protected int maxHeight = -1;

	protected int maxAvaHeight = 0;

	protected int maxAvaWidth = 0;

	protected int currentIP = 0;

	protected int currentBP = 0;

	protected int offsetX = 0;

	protected int offsetY = 0;

	protected ContainerArea root;

	protected PDFAbstractLM child;

	protected int minHeight = 0;

	protected int minWidth = 0;

	protected boolean pageBreakAvoid = false;

	public int getMaxAvaHeight() {
		return this.maxAvaHeight;
	}

	public int getCurrentMaxContentWidth() {
		return maxAvaWidth - currentIP;// FIXME
	}

	public int getCurrentMaxContentHeight() {
		return maxAvaHeight - currentBP;
	}

	public int getCurrentIP() {
		return this.currentIP;
	}

	public int getCurrentBP() {
		return this.currentBP;
	}

	public void setCurrentBP(int bp) {
		this.currentBP = bp;
	}

	public void setCurrentIP(int ip) {
		this.currentIP = ip;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int x) {
		this.offsetX = x;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int y) {
		this.offsetY = y;
	}

	public PDFStackingLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
		pageBreakAvoid = pageBreakInsideAvoid() || pageBreakAfterAvoid();
	}

	public boolean isPageBreakAvoidInside() {
		if (parent == null) {
			return false;
		} else {
			if (parent.pageBreakAvoid) {
				return true;
			} else {
				return parent.isPageBreakAvoidInside();
			}
		}
	}

	protected boolean layoutChildren() throws BirtException {
		initialize();
		boolean hasNextPage = false;
		hasNextPage = traverseChildren();
		if (!hasNextPage) {
			isLast = true;
			clearCache();
		} else if (!context.isAutoPageBreak()) {
			clearCache();
		}
		// if ( !isRootEmpty( ) &&!(isPageBreakAvoidInside( )&&context.isAutoPageBreak(
		// )&&hasNextPage))
		if (!isRootEmpty()) {
			closeLayout();
			hasNextPage = !submitRoot() || hasNextPage;
		}
		return hasNextPage;
	}

	protected boolean clearCache() {
		return true;
	}

	protected boolean isRootEmpty() {
		return !(root != null && root.getChildrenCount() > 0);
	}

	protected abstract boolean traverseChildren() throws BirtException;

	/**
	 * submit the current layout result
	 * 
	 * @return
	 */
	protected boolean submitRoot() {
		if (root == null) {
			return true;
		}
		boolean success = true;
		if (parent != null) {
			// FIXME to support keepWithPrevious
			success = parent.addArea(root, false, isKeepWithNext());
			// parent.submit( root );
			if (success) {
				isFirst = false;
				root = null;
			}
		} else {
			if (content != null) {
				content.setExtension(IContent.LAYOUT_EXTENSION, root);
				root = null;
			}
		}
		return success;
	}

	protected boolean isKeepWithNext() {
		return isLast && isFirst && pageBreakAfterAvoid();
	}

	/**
	 * initialize dynamic layout information
	 * <ul>
	 * <li>create root area</li>
	 * <li>set MaxAvaHeight and MaxAvaWidth</li>
	 * <li>set OffsetX and OffsetY</li>
	 * <li>set CurrentIP and CurrentBP</li>
	 * </ul>
	 * 
	 * @throws BirtException
	 */
	protected abstract void initialize() throws BirtException;

	/**
	 * end current area if it is the last area of content, add bottom box property
	 * 
	 */
	protected abstract void closeLayout();

	protected abstract void createRoot();

	protected int getIntrisicHeight() {
		if (root != null) {
			return root.getIntrisicHeight();
		}
		return 0;
	}

	public boolean isPageEmpty() {
		if (!isRootEmpty()) {
			return false;
		} else {
			if (parent != null) {
				return parent.isPageEmpty();
			}
		}
		return true;
	}

	public int getLineHeight() {
		if (content != null) {
			IStyle contentStyle = content.getComputedStyle();
			return PropertyUtil.getLineHeight(contentStyle.getLineHeight());
		}
		return 0;
	}

	public abstract void submit(AbstractArea area);

}
