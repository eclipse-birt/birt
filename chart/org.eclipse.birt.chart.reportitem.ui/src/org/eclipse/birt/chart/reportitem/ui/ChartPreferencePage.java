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

package org.eclipse.birt.chart.reportitem.ui;

import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.report.designer.ui.preferences.PropertyAndPreferencePage;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Preference page for Charting
 */

public class ChartPreferencePage extends PropertyAndPreferencePage
{

	public static final String PREF_ID = "org.eclipse.birt.chart.reportitem.ui.ChartPreferencePage"; //$NON-NLS-1$

	public ChartPreferencePage( )
	{
		super( );
	}

	/**
	 * @param title
	 */
	public ChartPreferencePage( String title )
	{
		super( title );
	}

	/**
	 * @param title
	 * @param image
	 */
	public ChartPreferencePage( String title, ImageDescriptor image )
	{
		super( title, image );
	}

	private ChartConfigurationBlock fConfigurationBlock;

	public void createControl( Composite parent )
	{
		
		fConfigurationBlock = ChartReportItemUIFactory.instance( )
				.createChartConfigurationBlock( getNewStatusChangedListener( ),
						getProject( ) );
		super.createControl( parent );

		ChartUIUtil.bindHelp( getControl( ),
				ChartHelpContextIds.PREFERENCE_CHART );

	}

	protected Control createPreferenceContent( Composite composite )
	{
		return fConfigurationBlock.createContents( composite );
	}

	protected boolean hasProjectSpecificOptions( IProject project )
	{
		return fConfigurationBlock.hasProjectSpecificOptions( project );
	}

	protected String getPreferencePageID( )
	{
		return PREF_ID;
	}

	protected String getPropertyPageID( )
	{
		return PREF_ID;
	}

	public void dispose( )
	{
		if ( fConfigurationBlock != null )
		{
			fConfigurationBlock.dispose( );
		}
		super.dispose( );
	}

	protected void enableProjectSpecificSettings(
			boolean useProjectSpecificSettings )
	{
		super.enableProjectSpecificSettings( useProjectSpecificSettings );
		if ( fConfigurationBlock != null )
		{
			fConfigurationBlock.useProjectSpecificSettings( useProjectSpecificSettings );
		}
	}

	protected void performDefaults( )
	{
		super.performDefaults( );
		if ( fConfigurationBlock != null )
		{
			fConfigurationBlock.performDefaults( );
		}
	}

	public boolean performOk( )
	{
		if ( fConfigurationBlock != null && !fConfigurationBlock.performOk( ) )
		{
			return false;
		}
		return super.performOk( );
	}

	public void performApply( )
	{
		if ( fConfigurationBlock != null )
		{
			fConfigurationBlock.performApply( );
		}
	}

	public void setElement( IAdaptable element )
	{
		super.setElement( element );
		setDescription( null ); // no description for property page
	}

}
