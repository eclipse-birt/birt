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
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;

/**
 * Definition of the HTML region area
 *
 * @since 3.3
 *
 */
public class HtmlRegionArea extends RegionArea implements IContainerArea {

	/**
	 * Constructor
	 */
	public HtmlRegionArea() {
		super();
	}

	HtmlRegionArea(HtmlRegionArea area) {
		super(area);
	}

	@Override
	public void close() {
		if (specifiedHeight >= currentBP) {
			finished = true;
		} else {
			finished = false;
		}
		setContentHeight(specifiedHeight);
	}

	@Override
	public void update(AbstractArea area) throws BirtException {
		int aHeight = area.getAllocatedHeight();
		currentBP += aHeight;
		if (currentIP + area.getAllocatedWidth() > maxAvaWidth) {
			setNeedClip(true);
		}
	}

	/**
	 * Verify if the HTML region is finished
	 *
	 * @return true, HTML region is finished
	 */
	public boolean isFinished() {
		return finished;
	}

}
