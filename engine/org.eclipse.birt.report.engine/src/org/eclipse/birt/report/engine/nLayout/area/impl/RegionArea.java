/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;

public class RegionArea extends BlockContainerArea implements IContainerArea {
	public RegionArea() {
		super();
	}

	RegionArea(RegionArea area) {
		super(area);
	}

	public void initialize() throws BirtException {
		calculateSpecifiedWidth(content);
		calculateSpecifiedHeight(content);
	}

	// override this method because for region area, the reference height for
	// percentage calculating can be retrieved in advance.
	protected void calculateSpecifiedHeight(IContent content) {
		if (content == null)
			return;
		DimensionType height = content.getHeight();
		if (height != null) {
			specifiedHeight = getDimensionValue(content, height, parent.getHeight());
		}
	}

	public void close() throws BirtException {
		if (context.isFixedLayout()) {
			setContentHeight(specifiedHeight);
		} else {
			setContentHeight(Math.max(specifiedHeight, currentBP));
		}
		finished = true;
	}

	public void update(AbstractArea area) throws BirtException {
		int aHeight = area.getAllocatedHeight();
		currentBP += aHeight;
		if (currentIP + area.getAllocatedWidth() > maxAvaWidth) {
			setNeedClip(true);
		}
	}

}
