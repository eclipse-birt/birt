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

package org.eclipse.birt.chart.ui.swt.wizard.data;

import java.util.List;

import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.window.Window;


/**
 * The component is used to set value series grouping. 
 * 
 * @since 2.3
 */
public class YOptionalDataDefinitionComponent extends BaseDataDefinitionComponent
{

	/**
	 * @param queryType
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param title
	 */
	public YOptionalDataDefinitionComponent( String queryType,
			SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String title )
	{
		super( queryType, seriesdefinition, query, context, title );
	}

	/**
	 * @param style
	 * @param queryType
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param title
	 */
	public YOptionalDataDefinitionComponent( int style, String queryType,
			SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String title )
	{
		super( style, queryType, seriesdefinition, query, context, title );
	}

	/**
	 * Create instance of <code>GroupSortingDialog</code> for Y
	 * series.
	 * 
	 * @param sdBackup
	 * @return
	 */
	protected GroupSortingDialog createGroupSortingDialog(
			SeriesDefinition sdBackup )
	{
		return new GroupSortingDialog( cmpTop.getShell( ),
				context,
				sdBackup,
				false );
	}
	
	protected void handleGroupAction( )
	{
		SeriesDefinition sdBackup = (SeriesDefinition) EcoreUtil.copy( seriesdefinition );
		GroupSortingDialog groupDialog = createGroupSortingDialog( sdBackup );

		if ( groupDialog.open( ) == Window.OK )
		{
			if ( !sdBackup.eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) ) )
			{
				seriesdefinition.eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
			}
			else
			{
				seriesdefinition.setSorting( sdBackup.getSorting( ) );
			}
			
			// Update the query sorting of other series.
			ChartAdapter.beginIgnoreNotifications( );
			List sds = ChartUIUtil.getAllOrthogonalSeriesDefinitions( context.getModel( ) );
			for ( int i = 0; i < sds.size( ); i++ )
			{
				if ( i != 0 )
				{
					// Except for the first, which should be
					// changed manually.
					SeriesDefinition sdf = (SeriesDefinition) sds.get( i );
					if ( !sdBackup.eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) ) )
					{
						sdf.eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
					}
					else
					{
						sdf.setSorting( sdBackup.getSorting( ) );
					}
				}
			}
			ChartAdapter.endIgnoreNotifications( );
			
			seriesdefinition.setGrouping( sdBackup.getGrouping( ) );
			seriesdefinition.getGrouping( )
					.eAdapters( )
					.addAll( seriesdefinition.eAdapters( ) );
		}
	}
}
