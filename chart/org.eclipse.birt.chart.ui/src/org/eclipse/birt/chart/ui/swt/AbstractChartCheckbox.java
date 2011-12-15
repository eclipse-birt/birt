/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;


/**AbstractChartCheckbox
 * 
 */

abstract public class AbstractChartCheckbox extends Composite
{
	public AbstractChartCheckbox(Composite parent, int styles )
	{
		super( parent, styles );
	}
	
	/**
	 * Set if default state is selection for this button.
	 * 
	 * @param defSelection
	 */
	abstract public void setDefaultSelection( boolean defSelection );
	
	/**
	 * Set button text.
	 * 
	 * @param text
	 */
	abstract public void setText( String text );

	/**
	 * Returns checkbox state, 0 means grayed state, 1 means checked state, 2
	 * means unchecked state.
	 * 
	 * @return selection state.
	 */
	abstract public int getSelectionState( );

	/**
	 * Sets checkbox state.
	 * 
	 * @param state
	 *            the state value, 0 means grayed state, 1 means checked state,
	 *            2 means unchecked state.
	 */
	abstract public void setSelectionState( int state );

	abstract public void addSelectionListener( SelectionListener listener );

}
