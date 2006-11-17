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

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * LineStyleChooserComposite
 */
public class LineStyleChooserComposite extends CustomChooserComposite
{

	private static final Integer[] iLineStyles = new Integer[]{
			new Integer( SWT.LINE_SOLID ),
			new Integer( SWT.LINE_DASH ),
			new Integer( SWT.LINE_DASHDOT ),
			new Integer( SWT.LINE_DOT )
	};

	static class LineStyleChoice extends LineCanvas implements ICustomChoice
	{

		LineStyleChoice( Composite parent, int iStyle, int iLineStyle )
		{
			super( parent, iStyle, iLineStyle, 1 );
		}

		public Object getValue( )
		{
			return new Integer( getLineStyle( ) );
		}

		public void setValue( Object value )
		{
			setLineStyle( ( (Integer) value ).intValue( ) );
		}

	}

	public LineStyleChooserComposite( Composite parent, int style,
			int iLineStyle )
	{
		super( parent, style, new Integer( iLineStyle ) );
		setItems( iLineStyles );

	}

	protected ICustomChoice createChoice( Composite parent, Object choiceValue )
	{
		if ( choiceValue == null )
		{
			choiceValue = new Integer( 0 );
		}
		return new LineStyleChoice( parent,
				SWT.NONE,
				( (Integer) choiceValue ).intValue( ) );
	}

	/**
	 * Returns the current selected line style as an integer corresponding to
	 * the appropriate SWT constants.
	 * 
	 */
	public int getLineStyle( )
	{
		return ( (Integer) getChoiceValue( ) ).intValue( );
	}

	public void setLineStyle( int iStyle )
	{
		setChoiceValue( new Integer( iStyle ) );
	}

}
