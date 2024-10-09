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

package org.eclipse.birt.report.engine.nLayout.area;

import java.util.Iterator;

import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;

public interface IContainerArea extends IArea, ITagType {

	Iterator<IArea> getChildren();

	int getChildrenCount();

	void addChild(IArea area);

	boolean needClip();

	void setNeedClip(boolean needClip);

	BoxStyle getBoxStyle();

	String getHelpText();

	String getTagType();

}
