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

import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Choose the Line decorator of needle
 */
public class HeadStyleChooserComposite extends CustomChooserComposite
{

	private static final int[] iLineDecorator = new int[]{
			LineDecorator.ARROW, LineDecorator.NONE, LineDecorator.CIRCLE
	};

	static class HeaderStyleChoice extends HeadStyleCanvas
			implements
				ICustomChoice
	{

		HeaderStyleChoice( Composite parent, int iStyle, int iLineDecorator )
		{
			super( parent, iStyle, iLineDecorator );
		}

		public int getValue( )
		{
			return getHeadStyle( );
		}

		public void setValue( int value )
		{
			setHeadStyle( value );
		}

	}

	public HeadStyleChooserComposite( Composite parent, int style,
			int iLineDecorator )
	{
		super( parent, style, iLineDecorator );
	}

	protected ICustomChoice createChoice( Composite parent, int choiceValue )
	{
		return new HeaderStyleChoice( parent, SWT.NONE, choiceValue );
	}

	protected int[] getChoiceValues( )
	{
		return iLineDecorator;
	}

	/**
	 * Returns the current selected head style as an integer.
	 * 
	 */
	public int getHeadStyle( )
	{
		return getChoiceValue( );
	}

	public void setHeadStyle( int iStyle )
	{
		setChoiceValue( iStyle );
	}
}
