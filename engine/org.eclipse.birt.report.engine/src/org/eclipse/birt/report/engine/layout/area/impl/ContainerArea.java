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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public abstract class ContainerArea extends AbstractArea implements IContainerArea
{
	public int getContentX()
	{
		return PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH)) + PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_LEFT));
	}
	
	public int getContentY()
	{
		return PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH)) + PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_TOP));
	}
	
	ContainerArea(IContent content)
	{
		super(content);
	}

	protected ArrayList children = new ArrayList();

	public Iterator getChildren()
	{
		return children.iterator();
	}
	
	public void addChild(IArea area)
	{
		children.add(area);
	}
	
	public void removeAll()
	{
		children.clear();
	}
	
	public void removeChild(IArea area)
	{
		children.remove(area);
	}

	public void accept(IAreaVisitor visitor)
	{
		visitor.startContainer(this);
		Iterator iter = getChildren();
		while(iter.hasNext())
		{
			IArea child = (IArea)iter.next();
			child.accept(visitor);
		}
		visitor.endContainer(this);
		
	}

	
	protected void copyPropertyTo(ContainerArea area)
	{
		area.setPosition(x, y);
		area.setHeight(height);
		area.setWidth(width);
	}
	
	public int getChildrenCount()
	{
		return children.size();
	}

}
