/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;

public class InlineStackingLayout extends ContainerLayout implements IInlineStackingLayout {

	public InlineStackingLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
	}

	@Override
	protected void addToRoot(AbstractArea area) {
		currentContext.root.addChild(area);
		area.setAllocatedPosition(currentContext.currentIP + offsetX, currentContext.currentBP + offsetY);
		currentContext.currentIP += area.getAllocatedWidth();
		if (currentContext.currentIP + area.getAllocatedWidth() > currentContext.root.getContentWidth()) {
			currentContext.root.setNeedClip(true);
		} else if (currentContext.currentBP > currentContext.maxAvaHeight) {
			currentContext.root.setNeedClip(true);
		}
	}

	@Override
	protected void closeLayout(ContainerContext currentContext, int index, boolean finished) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createRoot() {
		// TODO Auto-generated method stub
	}

	protected void verticalAlign() {
		Iterator iter = currentContext.root.getChildren();
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			IStyle childStyle = child.getStyle();
			String vAlign = childStyle.getVerticalAlign();
			if (childStyle == null) {
				continue;
			}
			int spacing = currentContext.root.getContentHeight() - child.getAllocatedHeight();
			if (spacing < 0) {
				spacing = 0;
			}
			if (CSSConstants.CSS_BASELINE_VALUE.equalsIgnoreCase(vAlign)) {
				int lineHeight = ((ContainerLayout) parent).getLineHeight();
				if (lineHeight > 0) {
					// align to middle, fix issue 164072
					child.setPosition(child.getX(),
							child.getY() + getMaxBaseLine() - child.getBaseLine() + spacing / 2);
				} else {
					child.setPosition(child.getX(), child.getY() + getMaxBaseLine() - child.getBaseLine());
				}
			} else if (CSSConstants.CSS_BOTTOM_VALUE.equalsIgnoreCase(vAlign)) {
				child.setPosition(child.getX(), child.getY() + spacing);
			} else if (CSSConstants.CSS_MIDDLE_VALUE.equalsIgnoreCase(vAlign)) {
				child.setPosition(child.getX(), child.getY() + spacing / 2);
			}
		}
	}

	/**
	 * Calculates the max baseline, and update the root height if necessary.
	 *
	 * @return the max baseline.
	 */
	private int getMaxBaseLine() {
		int maxChildrenBaseLine = currentContext.root.getMaxChildrenBaseLine();
		if (maxChildrenBaseLine == 0) {
			int maxChildrenBaseLineBelow = 0;
			Iterator iter = currentContext.root.getChildren();
			while (iter.hasNext()) {
				AbstractArea child = (AbstractArea) iter.next();
				maxChildrenBaseLine = Math.max(maxChildrenBaseLine, child.getBaseLine());
				maxChildrenBaseLineBelow = Math.max(maxChildrenBaseLineBelow,
						child.getAllocatedHeight() - child.getBaseLine());
			}
			currentContext.root.setContentHeight(
					Math.max(currentContext.root.getContentHeight(), maxChildrenBaseLine + maxChildrenBaseLineBelow));
			currentContext.root.setBaseLine(maxChildrenBaseLine);
			currentContext.root.setMaxChildrenBaseLine(maxChildrenBaseLine);
			currentContext.root.setMaxChildrenBaseLineBelow(maxChildrenBaseLineBelow);
		}
		return maxChildrenBaseLine;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean endLine() throws BirtException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getMaxLineWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmptyLine() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTextIndent(ITextContent textContent) {
		// TODO Auto-generated method stub

	}

}
