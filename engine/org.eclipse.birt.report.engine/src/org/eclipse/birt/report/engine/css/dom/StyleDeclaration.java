
package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.w3c.dom.css.CSSValue;

/**
 * @author yanwei
 *
 */
public class StyleDeclaration extends AbstractStyle implements IStyle
{
	protected CSSValue[] values;
	protected boolean shared;
	protected int propertyCount = 0;
	
	/**
	 * set the property 
	 */
	public void setProperty( int index, CSSValue value )
	{
		assert index >= 0 && index < NUMBER_OF_STYLE;
		if( values[index] != value )
		{
			if (shared)
			{
				decouple();
			}
			if( values[index] == null )
			{
				propertyCount ++;
			}
			else if( value == null )
			{
				propertyCount --;
			}
			values[index] = value;
		}
	}
	/**
	 * set the property 
	 */
	public CSSValue getProperty( int index )
	{
		assert index >= 0 && index < NUMBER_OF_STYLE;
		return values[index];
	}
	
	public StyleDeclaration( StyleDeclaration style )
	{
		super(style.engine);
		this.engine = style.engine;
		this.values = style.values;
		this.shared = true;
	}
	
	public StyleDeclaration(CSSEngine engine)
	{
		super(engine);
		this.engine = engine;
		this.values = new CSSValue[NUMBER_OF_STYLE];
		this.shared = false;
	}
	
	protected void decouple()
	{
		CSSValue[] newValues = new CSSValue[NUMBER_OF_STYLE];
		System.arraycopy(values, 0, newValues, 0, NUMBER_OF_STYLE);
		values = newValues;
		shared = false;
	}
	
	public boolean isEmpty()
	{
		return propertyCount == 0;
	}

	public int getLength() {
		return propertyCount;
	}

	public boolean equals(Object aStyle)
	{
		if (aStyle instanceof StyleDeclaration)
		{
			StyleDeclaration style = (StyleDeclaration)aStyle;
			if (propertyCount == style.propertyCount)
			{
				for (int i = 0; i < NUMBER_OF_STYLE; i++)
				{
					CSSValue value1 = values[i];
					CSSValue value2 = style.values[i];
					if ( value1 != value2 &&
						(value1 == null || !value1.equals( value2 ) ) )
					{
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
}
