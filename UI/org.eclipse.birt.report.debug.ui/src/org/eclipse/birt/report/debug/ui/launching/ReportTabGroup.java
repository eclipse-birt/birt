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

package org.eclipse.birt.report.debug.ui.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.*;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.pde.internal.ui.launcher.AdvancedLauncherTab;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

/**
 * Class for report Debug tab group
 * 
 */
public class ReportTabGroup extends AbstractLaunchConfigurationTabGroup
{

	/**
	 * 
	 */
	public ReportTabGroup( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(org.eclipse.debug.ui.ILaunchConfigurationDialog,
	 *      java.lang.String)
	 */
	public void createTabs( ILaunchConfigurationDialog dialog, String mode )
	{
		ILaunchConfigurationTab tabs[] = (ILaunchConfigurationTab[]) null;
		tabs = ( new ILaunchConfigurationTab[]{
				new ReportAdvancedLauncherTab( ), new AdvancedLauncherTab( )
		} );
		setTabs( tabs );
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults( ILaunchConfigurationWorkingCopy configuration )
	{
		super.setDefaults( configuration );
	}

	/**
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup#initializeFrom(ILaunchConfiguration)
	 */
	public void initializeFrom( ILaunchConfiguration configuration )
	{
		final ILaunchConfiguration config = configuration;
		final ILaunchConfigurationTab[] tabs = getTabs( );
		BusyIndicator.showWhile( Display.getCurrent( ), new Runnable( ) {

			public void run( )
			{
				try
				{
					String id = config.getAttribute( IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH_PROVIDER,
							(String) null );
					if ( id == null
							&& config instanceof ILaunchConfigurationWorkingCopy )
					{
						ILaunchConfigurationWorkingCopy wc = (ILaunchConfigurationWorkingCopy) config;
						wc.setAttribute( IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH_PROVIDER,
								"org.eclipse.pde.ui.workbenchClasspathProvider" ); //$NON-NLS-1$
					}
				}
				catch ( CoreException e )
				{
				}
				for ( int i = 0; i < tabs.length; i++ )
				{
					tabs[i].initializeFrom( config );
				}
			}
		} );
	}
}