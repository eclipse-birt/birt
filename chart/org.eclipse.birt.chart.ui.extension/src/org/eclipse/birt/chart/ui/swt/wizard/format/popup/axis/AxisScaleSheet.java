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
import org.eclipse.swt.widgets.Composite;

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

	private void setState( String sType )
	{
		// Bugzilla#103961 Marker line and range only work for non-category
		// style X-axis,
		boolean bEnabled = !( getAxisForProcessing( ).isCategoryAxis( ) || sType.equals( AxisType.TEXT_LITERAL.getName( ) ) );
		lblMin.setEnabled( bEnabled );
		txtScaleMin.setEnabled( bEnabled );
		lblMax.setEnabled( bEnabled );
		txtScaleMax.setEnabled( bEnabled );
		lblStep.setEnabled( bEnabled );
		txtScaleStep.setEnabled( bEnabled );

		// lblUnit.setEnabled( sType.equals( "DateTime" ) ); //$NON-NLS-1$
		// cmbScaleUnit.setEnabled( sType.equals( "DateTime" ) ); //$NON-NLS-1$
		lblStep.setEnabled( bEnabled
				&& !sType.equals( AxisType.DATE_TIME_LITERAL.getName( ) ) );
		txtScaleStep.setEnabled( bEnabled
				&& !sType.equals( AxisType.DATE_TIME_LITERAL.getName( ) ) );
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

	protected Composite getComponent( Composite parent )
	{
		Composite composite = super.getComponent( parent );
		setState( getAxisForProcessing( ).getType( ).getName( ) );
		return composite;
	}

}