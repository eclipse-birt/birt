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

package org.eclipse.birt.chart.ui.swt.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.TasksManager;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * Chart builder for BIRT designer.
 * 
 */
public class ChartWizard extends WizardBase
{

	public static final String WIZARD_ID = "org.eclipse.birt.chart.ui.ChartWizard"; //$NON-NLS-1$

	private static final String WIZARD_TITLE_NEW = Messages.getString( "ChartWizard.Title.NewChart" ); //$NON-NLS-1$

	private static final String WIZARD_TITLE_EDIT = Messages.getString( "ChartWizard.Title.EditChart" ); //$NON-NLS-1$

	private static final int CHART_WIZARD_WIDTH_MINMUM = 690;

	private static final int CHART_WIZARD_HEIGHT_MINMUM = 670;

	public static final double DEFAULT_CHART_BLOCK_HEIGHT = 130;

	public static final double DEFAULT_CHART_BLOCK_WIDTH = 212;

	public static final double DEFAULT_CHART_WITHOUT_AXIS_BLOCK_HEIGHT = 130;

	public static final double DEFAULT_CHART_WITHOUT_AXIS_BLOCK_WIDTH = 212;

	/**
	 * Indicates whether the popup is being closed by users
	 */
	public static boolean POPUP_CLOSING_BY_USER = true;

	/**
	 * Caches last opened task of each wizard
	 */
	private static Map lastTask = new HashMap( 3 );

	private ChartAdapter adapter = null;

	public ChartWizard( )
	{
		this( null );
	}

	/**
	 * Creates the chart wizard using a specified shell, such as a workbench
	 * shell
	 * 
	 * @param parentShell
	 *            parent shell. Null indicates using a new shell
	 */
	public ChartWizard( Shell parentShell )
	{
		super( parentShell,
				WIZARD_ID,
				CHART_WIZARD_WIDTH_MINMUM,
				CHART_WIZARD_HEIGHT_MINMUM,
				Messages.getString( "ChartWizard.Title.ChartBuilder" ), //$NON-NLS-1$
				UIHelper.getImage( "icons/obj16/chartselector.gif" ), //$NON-NLS-1$
				Messages.getString( "ChartWizard.Label.SelectChartTypeDataFormat" ), //$NON-NLS-1$
				UIHelper.getImage( "icons/wizban/chartwizardtaskbar.gif" ) ); //$NON-NLS-1$
		adapter = new ChartAdapter( this );
	}

	public void addTask( String sTaskID )
	{
		super.addTask( sTaskID );
		ITask task = TasksManager.instance( ).getTask( sTaskID );
		if ( task instanceof ITaskChangeListener )
		{
			adapter.addListener( (ITaskChangeListener) task );
		}
	}

	/**
	 * Resizes chart with a default value when the size is zero or null
	 * 
	 * @param chartModelCurrent
	 *            chart model
	 */
	public void resizeChart( Chart chartModelCurrent )
	{
		if ( chartModelCurrent.getBlock( ).getBounds( ) == null
				|| chartModelCurrent.getBlock( ).getBounds( ).getWidth( ) == 0
				|| chartModelCurrent.getBlock( ).getBounds( ).getHeight( ) == 0 )
		{
			if ( chartModelCurrent instanceof ChartWithoutAxesImpl )
			{
				chartModelCurrent.getBlock( ).setBounds( BoundsImpl.create( 0,
						0,
						DEFAULT_CHART_WITHOUT_AXIS_BLOCK_WIDTH,
						DEFAULT_CHART_WITHOUT_AXIS_BLOCK_HEIGHT ) );
			}
			else
			{
				chartModelCurrent.getBlock( ).setBounds( BoundsImpl.create( 0,
						0,
						DEFAULT_CHART_BLOCK_WIDTH,
						DEFAULT_CHART_BLOCK_HEIGHT ) );
			}
		}
	}

	private void removeAllAdapters( Chart chart )
	{
		chart.eAdapters( ).remove( adapter );
		TreeIterator iterator = chart.eAllContents( );
		while ( iterator.hasNext( ) )
		{
			Object oModel = iterator.next( );
			if ( oModel instanceof EObject )
			{
				( (EObject) oModel ).eAdapters( ).remove( adapter );
			}
		}
	}

	public void dispose( )
	{
		if ( getContext( ) != null )
		{
			// Dispose IDataServiceProvider
			getContext( ).getDataServiceProvider( ).dispose( );

			Chart chart = getContext( ).getModel( );
			if ( chart != null )
			{
				// Remove all adapters
				removeAllAdapters( chart );

				// Remove cache data
				ChartCacheManager.getInstance( ).dispose( );
			}
		}
		super.dispose( );
	}

	public EContentAdapter getAdapter( )
	{
		return adapter;
	}

	protected ChartWizardContext getContext( )
	{
		return (ChartWizardContext) context;
	}

	public String[] validate( )
	{
		return getContext( ).getUIServiceProvider( )
				.validate( getContext( ).getModel( ),
						getContext( ).getExtendedItem( ) );
	}

	public IWizardContext open( String[] sTasks, String topTaskId,
			IWizardContext initialContext )
	{
		Chart chart = ( (ChartWizardContext) initialContext ).getModel( );
		if ( chart == null )
		{
			setTitle( WIZARD_TITLE_NEW );
		}
		else
		{
			setTitle( WIZARD_TITLE_EDIT );
			// Add adapters to chart model
			chart.eAdapters( ).add( adapter );
		}

		if ( chart == null )
		{
			// If no chart model, always open the first task
			topTaskId = null;
		}
		else if ( topTaskId == null )
		{
			// Try to get last opened task if no task specified
			topTaskId = (String) lastTask.get( initialContext.getWizardID( ) );
		}
		return super.open( sTasks, topTaskId, initialContext );
	}

	/**
	 * Updates wizard title as Edit chart.
	 * 
	 */
	public void updateTitleAsEdit( )
	{
		if ( getTitle( ).equals( WIZARD_TITLE_NEW ) )
		{
			setTitle( WIZARD_TITLE_EDIT );
			getDialog( ).setTitle( WIZARD_TITLE_EDIT );
			getDialog( ).getShell( ).setText( WIZARD_TITLE_EDIT );
		}
	}
	
	/**
	 * Updates Apply button with enabled status.
	 * 
	 */
	public void updateApplayButton( )
	{
		List buttonList = getCustomButtons( );
		for ( int i = 0; i < buttonList.size( ); i++ )
		{
			if ( buttonList.get( i ) instanceof ApplyButtonHandler )
			{
				Button applyButton = ( (ApplyButtonHandler) buttonList.get( i ) ).getButton( );
				if ( !applyButton.isEnabled( ) )
				{
					applyButton.setEnabled( true );
				}
			}
		}
	}

	public void detachPopup( )
	{
		POPUP_CLOSING_BY_USER = false;
		super.detachPopup( );
		POPUP_CLOSING_BY_USER = true;
	}

	public void switchTo( String sTaskID )
	{
		lastTask.put( getContext( ).getWizardID( ), sTaskID );
		super.switchTo( sTaskID );
	}
}
