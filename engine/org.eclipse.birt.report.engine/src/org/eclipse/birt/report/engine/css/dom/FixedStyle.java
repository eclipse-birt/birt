package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.content.IStyle;
import org.w3c.dom.css.CSSValue;

/**
 * FixedStyle is a fixed image of styles. It simply holds all the style references.<br>
 * It avoids the cache and cascade computing facility provided by the ComputedStyle, which leads to unwanted behavior.<br>
 * The main drawback of this class is the memory consumption, since no cache is used. Use it with caution.
 */
public class FixedStyle extends AbstractStyle
{
	
	private CSSValue[] values = new CSSValue[NUMBER_OF_STYLE];

	public FixedStyle( AbstractStyle style )
	{
		super( style.engine );
		mergeStyle( style );
	}

	/**
	 * Merge style with current styles. New styles always overwrite the old ones, except the null values.
	 * @param style
	 */
	public void mergeStyle( IStyle style )
	{
		for(int i = 0; i < NUMBER_OF_STYLE; i++) 
		{
			CSSValue value = style.getProperty(i);
			if(value != null)
			{
				values[i] = style.getProperty( i );				
			}
		}
	}

	/**
	 * One should never change the value returned, may cause obscure bugs.
	 */
	@Override
	public CSSValue getProperty( int index )
	{
		return values[index];
	}

	@Override
	public void setProperty( int index, CSSValue value ) 
	{
		values[index] = value;
	}

	@Override
	public boolean isEmpty( ) 
	{
		for( CSSValue value : values )
		{
			if( value != null )
				return false;
		}
		return true;
	}

}
