/***********************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.util;

import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.emf.ecore.EObject;


/**
 * This class override base chart value updater, it overrides super methods.
 */

public class ChartValueUpdater extends BaseChartValueUpdater
{
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.model.util.BaseChartValueUpdater#updateCurveFitting(java.lang.String, org.eclipse.emf.ecore.EObject, org.eclipse.birt.chart.model.component.CurveFitting, org.eclipse.birt.chart.model.component.CurveFitting, org.eclipse.birt.chart.model.component.CurveFitting)
	 */
	@Override
	public void updateCurveFitting( String name, EObject eParentObj,
			CurveFitting eObj, CurveFitting eRefObj, CurveFitting eDefObj, boolean eDefOverride )
	{
		// As default, curve fitting is different from other chart element, if
		// current curve fitting is null, just use non-null reference curve
		// fitting to override, not use non-null default curve fitting to
		// override. And for properties in curve fitting, it can use values in
		// default curve fitting to override if those values in current curve
		// fitting are 'auto'

		if ( eObj == null )
		{
			if ( eRefObj != null )
			{
				eObj = eRefObj.copyInstance( );
				ChartElementUtil.setEObjectAttribute( eParentObj,
						name,
						eObj,
						false );
				eRefObj = null;
			}
		}
		if ( eObj == null || ( eRefObj == null && eDefObj == null) )
		{
			return;
		}
		
		super.updateCurveFitting( name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.model.util.BaseChartValueUpdater#updateMarkerLine(java.lang.String, org.eclipse.emf.ecore.EObject, org.eclipse.birt.chart.model.component.MarkerLine, org.eclipse.birt.chart.model.component.MarkerLine, org.eclipse.birt.chart.model.component.MarkerLine)
	 */
	@Override
	public void updateMarkerLine( String name, EObject eParentObj,
			MarkerLine eObj, MarkerLine eRefObj, MarkerLine eDefObj, boolean eDefOverride )
	{
		// Same reason with updateCurveFitting method.
		if ( eObj == null )
		{
			if ( eRefObj != null )
			{
				eObj = eRefObj.copyInstance( );
				ChartElementUtil.setEObjectAttribute( eParentObj,
						name,
						eObj,
						false );
				eRefObj = null;
			}
		}
		if ( eObj == null || ( eRefObj == null && eDefObj == null ) )
		{
			return;
		}

		super.updateMarkerLine( name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.model.util.BaseChartValueUpdater#updateMarkerRange(java.lang.String, org.eclipse.emf.ecore.EObject, org.eclipse.birt.chart.model.component.MarkerRange, org.eclipse.birt.chart.model.component.MarkerRange, org.eclipse.birt.chart.model.component.MarkerRange)
	 */
	@Override
	public void updateMarkerRange( String name, EObject eParentObj,
			MarkerRange eObj, MarkerRange eRefObj, MarkerRange eDefObj, boolean eDefOverride )
	{
		// Same reason with updateCurveFitting method.
		if ( eObj == null )
		{
			if ( eRefObj != null )
			{
				eObj = eRefObj.copyInstance( );
				ChartElementUtil.setEObjectAttribute( eParentObj,
						name,
						eObj,
						false );
				eRefObj = null;
			}
		}
		if ( eObj == null || ( eRefObj == null && eDefObj == null ) )
		{
			return;
		}

		super.updateMarkerRange( name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride );
	}
	
	/**
	 * Updates chart element DialRegion.
	 *
	 * @param eObj
	 *        chart element object.
	 * @param eRefObj
	 *        reference chart element object.
	 * @param eDefObj
	 *        default chart element object.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateDialRegion( String name, EObject eParentObj,
			DialRegion eObj, DialRegion eRefObj, DialRegion eDefObj, boolean eDefOverride )
	{
		// Same reason with updateCurveFitting method.
		if ( eObj == null )
		{
			if ( eRefObj != null )
			{
				eObj = eRefObj.copyInstance( );
				ChartElementUtil.setEObjectAttribute( eParentObj,
						name,
						eObj,
						false );
				eRefObj = null;
			}
		}
		if ( eObj == null || ( eRefObj == null && eDefObj == null ) )
		{
			return;
		}

		super.updateDialRegion( name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride );

	}
	
	protected void updateMarkerRangeImpl( String name, EObject eParentObj,
			MarkerRange eObj, MarkerRange eRefObj, MarkerRange eDefObj, boolean eDefOverride )
	{
		// Same reason with updateCurveFitting method.
		if ( eObj == null )
		{
			if ( eRefObj != null )
			{
				eObj = eRefObj.copyInstance( );
				ChartElementUtil.setEObjectAttribute( eParentObj,
						name,
						eObj,
						false );
				eRefObj = null;
			}
		}
		if ( eObj == null || ( eRefObj == null && eDefObj == null ) )
		{
			return;
		}

		super.updateMarkerRangeImpl( name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride );
	}
}
