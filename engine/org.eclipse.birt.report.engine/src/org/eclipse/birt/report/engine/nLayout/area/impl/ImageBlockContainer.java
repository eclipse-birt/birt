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
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;

public class ImageBlockContainer extends BlockContainerArea implements IContainerArea {

	public ImageBlockContainer(ContainerArea parent, LayoutContext context, IContent content) {
		super(parent, context, content);
	}

	public void initialize() throws BirtException {
		IStyle style = content.getStyle();

		if (style == null || style.isEmpty()) {
			hasStyle = false;
			boxStyle = BoxStyle.DEFAULT;
			localProperties = LocalProperties.DEFAULT;
			action = content.getHyperlinkAction();
			bookmark = content.getBookmark();
		} else {
			buildProperties(content, context);
			pageBreakInside = IStyle.AVOID_VALUE;
		}

	}

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

}
