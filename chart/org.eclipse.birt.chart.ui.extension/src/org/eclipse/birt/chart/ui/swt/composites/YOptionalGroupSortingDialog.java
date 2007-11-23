/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog is used to set grouping and sort condition of Y optional.
 */
public class YOptionalGroupSortingDialog extends GroupSortingDialog
{
	/** The field indicates if enabled check box is still selection. */
	private boolean fStillEnableGroup = false;
	
	/** The field indicates if aggregation is enabled. */
	private boolean fbAggEnabled = true;

	/**
	 * @param shell
	 * @param wizardContext
	 * @param sd
	 * @param stillEnableGroup
	 *            the field indicates if the grouping is still enabled.
	 * @since 2.3
	 */
	public YOptionalGroupSortingDialog( Shell shell, ChartWizardContext wizardContext,
			SeriesDefinition sd, boolean stillEnableGroup, boolean bAggEnabled )
	{
		super( shell, wizardContext, sd );
		fbAggEnabled = bAggEnabled;
		fStillEnableGroup = stillEnableGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog#createGroupArea(org.eclipse.swt.widgets.Composite)
	 */
	protected void createGroupArea( Composite cmpBasic )
	{
		Composite cmpGrouping = new Composite( cmpBasic, SWT.NONE );
		GridData gdCMPGrouping = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPGrouping.horizontalSpan = 2;
		cmpGrouping.setLayoutData( gdCMPGrouping );
		cmpGrouping.setLayout( new FillLayout( ) );

		SeriesGroupingComposite sgc = new SeriesGroupingComposite( cmpGrouping,
				SWT.NONE,
				getSeriesDefinitionForProcessing( ),
				wizardContext.getModel( ) instanceof ChartWithoutAxes,
				fbAggEnabled );
		if ( fStillEnableGroup )
		{
			sgc.stillEnableGroupingSelection( );
		}
	}
}
