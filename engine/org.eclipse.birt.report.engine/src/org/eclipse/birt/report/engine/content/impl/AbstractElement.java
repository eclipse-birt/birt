package org.eclipse.birt.report.engine.content.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.content.IElement;

class AbstractElement implements IElement {

	final static List EMPTY_CHILDREN_LIST = new ArrayList();
	IElement parent;
	ArrayList children;
	
	public AbstractElement ()
	{
	}
	
	public IElement getParent()
	{
		return parent;
	}
	
	public void setParent(IElement parent)
	{
		this.parent = parent;
	}
	
	public List getChildren()
	{
		if (children == null)
		{
			children = new ArrayList();
		}
		return children;
	}
}