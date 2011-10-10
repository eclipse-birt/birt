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

package org.eclipse.birt.chart.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * This class defines common chart UI methods.
 */

public class ChartUIExtensionUtil
{
	public static int PROPERTY_UPDATE = ChartElementUtil.PROPERTY_UPDATE;
	public static int PROPERTY_UNSET = ChartElementUtil.PROPERTY_UNSET;
	
	/**
	 * Returns 'auto' string.
	 * 
	 * @return
	 */
	public static String getAutoMessage( )
	{
		return Messages.getString( "ItemLabel.Auto" ); //$NON-NLS-1$
	}

	/**
	 * Returns an string array and start with 'auto' item.
	 * 
	 * @param items
	 * @return
	 */
	public static String[] getItemsWithAuto( String[] items )
	{
		List<String> names = new ArrayList<String>( Arrays.asList( items ) );
		names.add( 0, getAutoMessage( ) );
		return names.toArray( new String[]{} );
	}

	/**
	 * Creates a combo list with specified items. 
	 * 
	 * @param parent
	 * @param items
	 * @return
	 */
	public static Combo createCombo( Composite parent, String[] items )
	{
		Combo c = new Combo( parent, SWT.DROP_DOWN | SWT.READ_ONLY );
		c.setItems( items );
		return c;
	}

	/**
	 * Checks if the 'auto' item is selected in specified combo list.
	 * 
	 * @param combo
	 * @return
	 */
	public static boolean isAutoSelection( Combo combo )
	{
		return combo != null && ( combo.getSelectionIndex( ) == 0 );
	}
}
