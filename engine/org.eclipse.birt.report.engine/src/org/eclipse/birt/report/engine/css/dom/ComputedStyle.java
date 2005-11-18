
package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.AbstractContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.dom.css.CSSValue;

public class ComputedStyle extends AbstractStyle implements IStyle
{

	boolean[] caculated;
	IContent elt;
	CSSValue[] values;

	public ComputedStyle( IContent elt )
	{
		super( ( (ReportContent) elt.getReportContent( ) ).getCSSEngine( ) );
		this.elt = elt;
	}

	public CSSValue getProperty( int index )
	{
		if ( values == null )
		{
			values = new CSSValue[NUMBER_OF_STYLE];
			caculated = new boolean[NUMBER_OF_STYLE];
		}
		if ( caculated[index] )
		{
			return values[index];
		}

		IContent parent = (IContent) elt.getParent( );
		IStyle pcs = null;
		if ( parent != null )
		{
			pcs = parent.getComputedStyle( );
		}

		// get the specified style
		IStyle s = ( (AbstractContent) elt ).getStyle( );

		Value sv = (Value) s.getProperty( index );
		Value cv = engine.resolveStyle( elt, index, sv, pcs );

		values[index] = cv;
		caculated[index] = true;

		return cv;
	}

	public boolean isEmpty( )
	{
		return false;
	}

	public void setProperty( int index, CSSValue value )
	{
		values[index] = value;
		elt.getStyle( ).setProperty( index, value );
		elt.getInlineStyle( ).setProperty( index, value );
	}
}
