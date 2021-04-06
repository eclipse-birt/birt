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
package org.eclipse.birt.report.engine.layout.area;

import java.util.Iterator;

public interface IContainerArea extends IArea {
	Iterator getChildren();

	int getChildrenCount();

	public void addChild(IArea area);

	public boolean needClip();

	public void setNeedClip(boolean needClip);

}
