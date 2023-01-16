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
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;

/**
 * Definition of region area
 *
 * @since 3.3
 *
 */
public class RegionArea extends BlockContainerArea implements IContainerArea {
	/**
	 *
	 */
	public RegionArea() {
		super();
	}

	RegionArea(RegionArea area) {
		super(area);
	}

	@Override
	public void initialize() throws BirtException {
		calculateSpecifiedWidth(content);
		calculateSpecifiedHeight(content);
	}

	// override this method because for region area, the reference height for
	// percentage calculating can be retrieved in advance.
	@Override
	protected void calculateSpecifiedHeight(IContent content) {
		if (content == null) {
			return;
		}
		DimensionType height = content.getHeight();
		if (height != null) {
			specifiedHeight = getDimensionValue(content, height, parent.getHeight());
		}
	}

	@Override
	public void close() {
		if (context.isFixedLayout()) {
			setContentHeight(specifiedHeight);
		} else {
			setContentHeight(Math.max(specifiedHeight, currentBP));
		}
		finished = true;
	}

	@Override
	public void update(AbstractArea area) throws BirtException {
		int aHeight = area.getAllocatedHeight();
		currentBP += aHeight;
		if (currentIP + area.getAllocatedWidth() > maxAvaWidth) {
			setNeedClip(true);
		}
	}

}
