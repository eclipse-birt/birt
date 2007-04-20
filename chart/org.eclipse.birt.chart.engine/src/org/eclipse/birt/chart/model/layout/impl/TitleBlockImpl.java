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

package org.eclipse.birt.chart.model.layout.impl;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Title Block</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class TitleBlockImpl extends LabelBlockImpl implements TitleBlock
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected TitleBlockImpl( )
	{
		super( );
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isTitle( )
	{
		return true;
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isCustom( )
	{
		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return LayoutPackage.Literals.TITLE_BLOCK;
	}

	/**
	 * A convenience method to create an initialized 'TitleBlock' instance
	 * 
	 * @return
	 */
	public static Block create( )
	{
		final TitleBlock tb = LayoutFactory.eINSTANCE.createTitleBlock( );
		( (TitleBlockImpl) tb ).initialize( );
		return tb;
	}
	
	protected BoundingBox computeBox( IDisplayServer xs, RunTimeContext rtc )
			throws ChartException
	{
		final String sPreviousValue = getLabel( ).getCaption( ).getValue( );
		getLabel( ).getCaption( )
				.setValue( rtc.externalizedMessage( sPreviousValue ) );
		
		double dWrapping = 0;
		EObject container = eContainer( );
		if ( container instanceof Block )
		{
			dWrapping = ( (Block) container ).getBounds( ).getWidth( )
					/ 72 * xs.getDpiResolution( );
		}
		try
		{
			return Methods.computeBox( xs,
					IConstants.TOP,
					getLabel( ),
					0,
					0,
					dWrapping );
		}
		catch ( IllegalArgumentException uiex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					uiex );
		}
		
		// Do not set the text back because of wrapping
	}
} //TitleBlockImpl
