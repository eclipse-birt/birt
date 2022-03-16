/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;

public class ForeignHtmlRegionArea extends RegionArea implements IContainerArea {

	public ForeignHtmlRegionArea() {
		super();
	}

	public ForeignHtmlRegionArea(IForeignContent content, LayoutContext context) {
		super();
		this.context = context;
		this.content = content;
		this.setPageBreakInside(IStyle.AVOID_VALUE);
	}

	ForeignHtmlRegionArea(ForeignHtmlRegionArea area) {
		super(area);
	}

	@Override
	public void initialize() throws BirtException {
		calculateSpecifiedWidth(content);
		calculateSpecifiedHeight(content);
		buildProperties(content, context);
	}

	@Override
	public void close() throws BirtException {
		finished = true;
		if (specifiedHeight > 0) {
			height = specifiedHeight;

		} else {
			height = currentBP;
		}

		if (null != parent) {
			if (height > parent.getMaxAvaHeight()) {
				height = parent.getMaxAvaHeight();
			}
		}
	}

	@Override
	public SplitResult split(int height, boolean force) throws BirtException {
		if (force) {
			ContainerArea newArea = cloneArea();
			newArea.children.addAll(children);
			children.clear();
			this.height = 0;
			return new SplitResult(newArea, SplitResult.SPLIT_SUCCEED_WITH_PART);
		}
		return SplitResult.SUCCEED_WITH_NULL;
	}

	@Override
	public void update(AbstractArea area) throws BirtException {
		int aHeight = area.getAllocatedHeight();
		currentBP += aHeight;
		if (currentIP + area.getAllocatedWidth() > maxAvaWidth) {
			setNeedClip(true);
		}
	}

	public boolean isFinished() {
		return finished;
	}

}
