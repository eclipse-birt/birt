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
	
	public static String getAutoMessage( )
	{
		return Messages.getString( "ItemLabel.Auto" ); //$NON-NLS-1$
	}

	public static String[] getItemsWithAuto( String[] items )
	{
		List<String> names = new ArrayList<String>( Arrays.asList( items ) );
		names.add( 0, getAutoMessage( ) );
		return names.toArray( new String[]{} );
	}
	
	public static String[] getTrueFalseComboItems( )
	{
		String[] strs = new String[]{
				getAutoMessage( ), Messages.getString( "ItemLabel.True" ),//$NON-NLS-1$
				Messages.getString( "ItemLabel.False" )//$NON-NLS-1$
		};
		return strs;
	}

	public static String[] getShowHideComboItems( )
	{
		String[] strs = new String[]{
				getAutoMessage( ), Messages.getString( "ItemLabel.Show" ),//$NON-NLS-1$
				Messages.getString( "ItemLabel.Hide" )//$NON-NLS-1$
		};
		return strs;
	}
	
	public static String[] getEnableDisableComboItemds()
	{
		String[] strs = new String[]{
				getAutoMessage( ), Messages.getString( "ItemLabel.Enable" ),//$NON-NLS-1$
				Messages.getString( "ItemLabel.Disable" )//$NON-NLS-1$
		};
		return strs;
	}

	public static Combo createCombo( Composite parent, String[] items )
	{
		Combo c = new Combo( parent, SWT.DROP_DOWN | SWT.READ_ONLY );
		c.setItems( items );
		return c;
	}

	public static Combo createTrueFalseItemsCombo( Composite parent )
	{
		Combo c = new Combo( parent, SWT.DROP_DOWN | SWT.READ_ONLY );
		c.setItems( getTrueFalseComboItems( ) );
		return c;
	}

	public static boolean isAutoSelection( Combo combo )
	{
		return combo != null && ( combo.getSelectionIndex( ) == 0 );
	}

}
