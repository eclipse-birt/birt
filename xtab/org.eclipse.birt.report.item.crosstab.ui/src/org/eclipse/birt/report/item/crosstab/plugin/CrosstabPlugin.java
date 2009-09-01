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

package org.eclipse.birt.report.item.crosstab.plugin;

import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.internal.CrosstabModelUtil;
import org.eclipse.birt.report.item.crosstab.core.de.internal.ICrosstabModelListener;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * 
 */

public class CrosstabPlugin extends AbstractUIPlugin
{

	/** Plugin ID */
	public static final String ID = "org.eclipse.birt.report.item.crosstab.ui"; //$NON-NLS-1$

	/** Preference ID */
	public static final String PREFERENCE_FILTER_LIMIT = "Filter.Limit"; //$NON-NLS-1$

	public static final String PREFERENCE_AUTO_DEL_BINDINGS = "Auto.Del.Bindings"; //$NON-NLS-1$

	public static final String CUBE_BUILDER_WARNING_PREFERENCE = "org.eclipse.birt.report.designer.ui.cubebuilder.warning"; //$NON-NLS-1$

	public static final int FILTER_LIMIT_DEFAULT = 100;

	public static final String AUTO_DEL_BINDING_DEFAULT = MessageDialogWithToggle.PROMPT;

	// The shared instance.
	private static CrosstabPlugin plugin;

	/**
	 * The constructor.
	 */
	public CrosstabPlugin( )
	{
		super( );
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static CrosstabPlugin getDefault( )
	{
		return plugin;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );

		PreferenceFactory.getInstance( )
				.getPreferences( CrosstabPlugin.getDefault( ) )
				.setDefault( PREFERENCE_FILTER_LIMIT, FILTER_LIMIT_DEFAULT );
		PreferenceFactory.getInstance( )
				.getPreferences( CrosstabPlugin.getDefault( ) )
				.setDefault( PREFERENCE_AUTO_DEL_BINDINGS,
						AUTO_DEL_BINDING_DEFAULT );
		PreferenceFactory.getInstance( )
				.getPreferences( CrosstabPlugin.getDefault( ) )
				.setDefault( CUBE_BUILDER_WARNING_PREFERENCE,
						MessageDialogWithToggle.PROMPT );
		
		//There add a listener, when create a measure head, add a lable to the head cell
		CrosstabModelUtil.setCrosstabModelListener( new ICrosstabModelListener()
		{

			public void onCreated( int type, Object model )
			{
				if (type == ICrosstabModelListener.MEASURE_HEADER && model instanceof CrosstabCellHandle)
				{
					CrosstabCellHandle cellHandle = ((CrosstabCellHandle)model);
					if (cellHandle.getContents( ).size( ) > 0)
					{
						return;
					}
					LabelHandle labelHandle = DesignElementFactory.getInstance( cellHandle.getModuleHandle( ))
						.newLabel( null );
					try
					{
						labelHandle.setText( ((MeasureViewHandle)cellHandle.getContainer( )).getCubeMeasureName( ) );

						cellHandle.addContent( labelHandle );
					}
					catch ( SemanticException e )
					{
						ExceptionUtil.handle( e );
					}
				}
			}
			
		});
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop( BundleContext context ) throws Exception
	{
		super.stop( context );
		plugin = null;
		CrosstabModelUtil.setCrosstabModelListener(null);
	}
}
