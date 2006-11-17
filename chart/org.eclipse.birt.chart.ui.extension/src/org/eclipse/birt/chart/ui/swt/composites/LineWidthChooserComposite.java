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
 * LineWidthChooserComposite
 */
public class LineWidthChooserComposite extends CustomChooserComposite
{

	private static final Integer[] iLineWidths = new Integer[]{
			new Integer( 1 ),
			new Integer( 2 ),
			new Integer( 3 ),
			new Integer( 4 )
	};

	static class LineWidthChoice extends LineCanvas implements ICustomChoice
	{

		LineWidthChoice( Composite parent, int iStyle, int iLineWidth )
		{
			super( parent, iStyle, SWT.LINE_SOLID, iLineWidth );
		}

		public Object getValue( )
		{
			return new Integer( getLineWidth( ) );
		}

		public void setValue( Object value )
		{
			setLineWidth( ( (Integer) value ).intValue( ) );
		}

	}

	public LineWidthChooserComposite( Composite parent, int style, int iWidth )
	{
		super( parent, style, new Integer( iWidth ) );
		setItems( iLineWidths );
	}

	protected ICustomChoice createChoice( Composite parent, Object choiceValue )
	{
		if ( choiceValue == null )
		{
			choiceValue = new Integer( 0 );
		}
		return new LineWidthChoice( parent,
				SWT.NONE,
				( (Integer) choiceValue ).intValue( ) );
	}

	/**
	 * Returns the currently selected line width
	 * 
	 * @return currently selected line width
	 */
	public int getLineWidth( )
	{
		return ( (Integer) getChoiceValue( ) ).intValue( );
	}

	public void setLineWidth( int iWidth )
	{
		setChoiceValue( new Integer( iWidth ) );
	}

}