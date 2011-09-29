/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class VisibleComboSelection extends ComboSelectionComposite
{

	public VisibleComboSelection( Composite parent, int styles )
	{
		super( parent,
				styles,
				Messages.getString( "ItemLabel.Visible" ), //$NON-NLS-1$
				ChartUIExtensionUtil.getTrueFalseComboItems( ) );
	}
}
