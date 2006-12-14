/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractScaleSheet;

/**
 * 
 */

public class AxisScaleSheet extends AbstractScaleSheet
{

	private Axis axis;

	public AxisScaleSheet( String title, ChartWizardContext context, Axis axis )
	{
		super( title, context );
		this.axis = axis;
	}

	private Axis getAxisForProcessing( )
	{
		return axis;
	}

	protected Scale getScale( )
	{
		return getAxisForProcessing( ).getScale( );
	}

	protected int getValueType( )
	{
		if ( getAxisForProcessing( ).getType( ) == AxisType.TEXT_LITERAL )
		{
			return TextEditorComposite.TYPE_NONE;
		}
		if ( getAxisForProcessing( ).getType( ) == AxisType.DATE_TIME_LITERAL )
		{
			return TextEditorComposite.TYPE_DATETIME;
		}
		return TextEditorComposite.TYPE_NUMBERIC;
	}

	protected void setState( )
	{
		// Bugzilla#103961 Marker line and range only work for non-category
		// style X-axis,
		boolean bEnabled = !( getAxisForProcessing( ).isCategoryAxis( ) || getAxisForProcessing( ).getType( ) == AxisType.TEXT_LITERAL );
		setState( bEnabled );
	}

}