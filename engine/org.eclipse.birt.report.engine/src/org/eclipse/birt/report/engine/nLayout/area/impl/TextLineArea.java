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
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;

public class TextLineArea extends LineArea {

	public TextLineArea(ContainerArea parent, LayoutContext context) {
		super(parent, context);
		// support widow and orphans, do not auto-pagebreak in textlineArea
		isInInlineStacking = true;
	}

	public TextLineArea(TextLineArea area) {
		super(area);
	}

	@Override
	public TextLineArea cloneArea() {
		return new TextLineArea(this);
	}

	@Override
	public SplitResult splitLines(int lineCount) throws BirtException {
		if (pageBreakBefore == IStyle.AVOID_VALUE) {
			return SplitResult.BEFORE_AVOID_WITH_NULL;
		}
		return SplitResult.SUCCEED_WITH_NULL;
	}

	@Override
	public SplitResult split(int height, boolean force) throws BirtException {
		if (force) {
			TextLineArea newArea = cloneArea();
			newArea.children.addAll(children);
			children.clear();
			this.height = 0;
			return new SplitResult(newArea, SplitResult.SPLIT_SUCCEED_WITH_PART);
		}
		if (pageBreakBefore == IStyle.AVOID_VALUE) {
			return SplitResult.BEFORE_AVOID_WITH_NULL;
		}
		return SplitResult.SUCCEED_WITH_NULL;
	}

	@Override
	public boolean isPageBreakInsideAvoid() {
		return true;
	}
}
