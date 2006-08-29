package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.w3c.dom.css.CSSValue;

public class AreaStyle extends AbstractStyle 
{
	protected IStyle parent;
	CSSValue[] values = new CSSValue[NUMBER_OF_STYLE];
	
	public AreaStyle(ComputedStyle style)
	{
		super(style.engine);
		this.parent = style;
	}
	
	public AreaStyle(CSSEngine engine)
	{
		super(engine);
	}

	public CSSValue getProperty(int index)
	{
		if(values[index]!=null)
		{
			return values[index];
		}

		if(parent!=null)
		{
			return parent.getProperty( index );
		}
		return null;
		
	}

	public void setProperty(int index, CSSValue value)
	{
		values[index] = value;
	}

	public boolean isEmpty()
	{
		return false;
	}

}
