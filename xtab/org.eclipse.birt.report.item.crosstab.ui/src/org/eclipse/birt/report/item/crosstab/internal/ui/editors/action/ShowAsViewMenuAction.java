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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.item.crosstab.ui.extension.SwitchCellInfo;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.action.IMenuManager;

/**
 * 
 */

public class ShowAsViewMenuAction extends AbstractCrosstabAction
{

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.ShowAsViewAction"; //$NON-NLS-1$

	private MeasureViewHandle measureViewHandle;
	private AggregationCellProviderWrapper providerWrapper;
	/**
	 * Trans name
	 */
	// private static final String NAME = "Add measure handle";
	public static final String NAME = Messages.getString( "ShowAsViewAction.DisplayName" );//$NON-NLS-1$
	private static final String ACTION_MSG_MERGE = Messages.getString( "ShowAsViewAction.TransName" );//$NON-NLS-1$
	IMenuManager menu;

	public ShowAsViewMenuAction( DesignElementHandle handle )
	{
		super( handle );
		// TODO Auto-generated constructor stub
		this.menu = menu;
		setId( ID );
		// setText( NAME );
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle( handle );
		setHandle( extendedHandle );
		measureViewHandle = CrosstabAdaptUtil.getMeasureViewHandle( extendedHandle );
		providerWrapper = new AggregationCellProviderWrapper( measureViewHandle.getCrosstab( ) );

	}

	public boolean isEnabled( )
	{

		if ( measureViewHandle instanceof ComputedMeasureViewHandle )
		{
			return false;
		}
		else
		{
			return !DEUtil.isReferenceElement( measureViewHandle.getCrosstabHandle( ) );
		}
	}

	public void updateMenu( IMenuManager menu )
	{
		this.menu = menu;
		run( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		menu.removeAll( );
		IAggregationCellViewProvider[] providers = providerWrapper.getAllProviders( );
		for ( int i = 0; i < providers.length; i++ )
		{
			if ( ( providers[i] == null )
					|| ( providers[i].getViewName( ).length( ) == 0 ) )
			{
				continue;
			}
			menu.add( new ShowAsViewAction( getHandle( ),
					providers[i].getViewName( ) ) );
		}

	}

	class ShowAsViewAction extends AbstractCrosstabAction
	{

		private String viewName;

		public ShowAsViewAction( DesignElementHandle handle, String viewName )
		{
			super( handle );
			// TODO Auto-generated constructor stub
			this.viewName = new String( viewName );
			setText( this.viewName );
		}

		/*
		 * (non-Javadoc) Method declared on IAction.
		 */
		public boolean isEnabled( )
		{
			boolean enabled = true;

			if ( measureViewHandle instanceof ComputedMeasureViewHandle )
			{
				enabled = false;
			}
			else
			{
				IAggregationCellViewProvider provider = providerWrapper.getProvider( viewName );
				SwitchCellInfo info = new SwitchCellInfo( measureViewHandle.getCrosstab( ),
						SwitchCellInfo.MEASURE );
				info.setMeasureInfo( true,
						measureViewHandle.getCubeMeasureName( ),
						viewName );
				enabled = provider.canSwitch( info );
			}

			setEnabled( enabled );
			return enabled;
		}

		public void run( )
		{
			// do nothing
			transStar( ACTION_MSG_MERGE + " " + viewName );
			providerWrapper.switchView( viewName, measureViewHandle.getCell( ) );
			transEnd( );
		}

	}
}
