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

import org.eclipse.birt.chart.model.attribute.AngleType;
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
	private int axisAngleType;

	public AxisScaleSheet( String title, ChartWizardContext context, Axis axis,
			int axisAngleType )
	{
		super( title, context );
		this.axis = axis;
		this.axisAngleType = axisAngleType;
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
		// Show outside is only available in Y axis
		if ( axisAngleType != AngleType.Y )
		{
			btnShowOutside.setEnabled( false );
			// Unselect 'ShowOutSide'.
			btnShowOutside.setSelection( false );
			getScale().setShowOutside( false );
		}
		else
		{
			btnAutoExpand.setSelection( true );
			getScale( ).setAutoExpand( true );
		}

		boolean bAxisX = ( axisAngleType == AngleType.X );
		boolean bEnableAutoExpand = btnStepAuto.getSelection( )
				&& bAxisX
				&& !( getAxisForProcessing( ).getType( ) == AxisType.TEXT_LITERAL )
				&& !( getAxisForProcessing( ).isCategoryAxis( ) );

		btnAutoExpand.setEnabled( bEnableAutoExpand );

		if ( getAxisForProcessing( ).getType( ) == AxisType.LINEAR_LITERAL
				&& !getAxisForProcessing( ).isCategoryAxis( ) )
		{
			if ( !getAxisForProcessing( ).getScale( ).isSetStepNumber( ) )
			{
				btnFactor.setEnabled( true );
				if ( btnFactor.getSelection( ) )
				{
					txtFactor.setEnabled( true );
				}
				else
				{
					txtFactor.setEnabled( false );
				}

			}
			if ( btnFactor.getSelection( ) )
			{
				btnStepNumber.setEnabled( false );
				spnStepNumber.setEnabled( false );
				lblMax.setEnabled( false );
				txtScaleMax.setEnabled( false );
				// lblMin.setEnabled( false );
				// txtScaleMin.setEnabled( false );
			}
		}
	}

}