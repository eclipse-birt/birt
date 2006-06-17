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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.css.dom.AreaStyle;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;

public class InlineContainerArea extends ContainerArea
{

	InlineContainerArea(IContent content)
	{
		super(content);
		// TODO Auto-generated constructor stub
	}

	public IContainerArea copyArea()
	{
		ContainerArea area =  new InlineContainerArea(content);
		area.style = new AreaStyle((AreaStyle)style);
		copyPropertyTo(area);
		return area;
	}


}
