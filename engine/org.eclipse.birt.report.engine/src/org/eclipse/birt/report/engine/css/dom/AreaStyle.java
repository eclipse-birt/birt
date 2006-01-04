package org.eclipse.birt.report.engine.css.dom;

import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.w3c.dom.css.CSSValue;

public class AreaStyle extends AbstractStyle 
{
	protected HashMap vs = new HashMap();
	protected IStyle parent;
	
	public AreaStyle(ComputedStyle style)
	{
		super(style.engine);
		this.parent = style;
	}
	
	public AreaStyle(AreaStyle style)
	{
		super(style.engine);
		this.parent = style.parent;
		vs.putAll(style.vs);
	}
	
	public AreaStyle()
	{
		super(BIRTCSSEngine.getInstance());
	}

	public CSSValue getProperty(int index)
	{
		CSSValue value = (CSSValue)vs.get(new Integer(index));
		if(value==null &&parent!=null)
		{
			return parent.getProperty(index);
		}
		return value;
	}

	public void setProperty(int index, CSSValue value)
	{
		vs.put(new Integer(index), value);

	}

	public boolean isEmpty()
	{
		return false;
	}

}
