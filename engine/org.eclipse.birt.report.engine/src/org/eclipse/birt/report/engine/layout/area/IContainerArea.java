/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.report.engine.layout.area;

import java.util.Iterator;

public interface IContainerArea extends IArea {
	Iterator getChildren();

	int getChildrenCount();

	public void addChild(IArea area);

	public boolean needClip();

	public void setNeedClip(boolean needClip);

}
