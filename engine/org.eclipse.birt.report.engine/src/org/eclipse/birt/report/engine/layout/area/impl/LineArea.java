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
package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.css.dom.AreaStyle;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;

public class LineArea extends LogicContainerArea
{

	public IContainerArea copyArea( )
	{
		LineArea area =  new LineArea();
		area.style = new AreaStyle((AreaStyle)style);
		copyPropertyTo(area);
		return area;
	}
}
