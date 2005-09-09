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

package org.eclipse.birt.chart.model.component.impl;

import java.util.Collection;

import org.eclipse.birt.chart.model.component.ChartPreferences;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Chart Preferences</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.ChartPreferencesImpl#getLabels <em>Labels</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.ChartPreferencesImpl#getBlocks <em>Blocks</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ChartPreferencesImpl extends EObjectImpl implements
		ChartPreferences
{

	/**
	 * The cached value of the '{@link #getLabels() <em>Labels</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLabels()
	 * @generated
	 * @ordered
	 */
	protected EList labels = null;

	/**
	 * The cached value of the '{@link #getBlocks() <em>Blocks</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getBlocks()
	 * @generated
	 * @ordered
	 */
	protected EList blocks = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ChartPreferencesImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return ComponentPackage.eINSTANCE.getChartPreferences( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList getLabels( )
	{
		if ( labels == null )
		{
			labels = new EObjectContainmentEList( Label.class,
					this,
					ComponentPackage.CHART_PREFERENCES__LABELS );
		}
		return labels;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList getBlocks( )
	{
		if ( blocks == null )
		{
			blocks = new EObjectContainmentEList( Block.class,
					this,
					ComponentPackage.CHART_PREFERENCES__BLOCKS );
		}
		return blocks;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, Class baseClass, NotificationChain msgs )
	{
		if ( featureID >= 0 )
		{
			switch ( eDerivedStructuralFeatureID( featureID, baseClass ) )
			{
				case ComponentPackage.CHART_PREFERENCES__LABELS :
					return ( (InternalEList) getLabels( ) ).basicRemove( otherEnd,
							msgs );
				case ComponentPackage.CHART_PREFERENCES__BLOCKS :
					return ( (InternalEList) getBlocks( ) ).basicRemove( otherEnd,
							msgs );
				default :
					return eDynamicInverseRemove( otherEnd,
							featureID,
							baseClass,
							msgs );
			}
		}
		return eBasicSetContainer( null, featureID, msgs );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet( EStructuralFeature eFeature, boolean resolve )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case ComponentPackage.CHART_PREFERENCES__LABELS :
				return getLabels( );
			case ComponentPackage.CHART_PREFERENCES__BLOCKS :
				return getBlocks( );
		}
		return eDynamicGet( eFeature, resolve );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet( EStructuralFeature eFeature, Object newValue )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case ComponentPackage.CHART_PREFERENCES__LABELS :
				getLabels( ).clear( );
				getLabels( ).addAll( (Collection) newValue );
				return;
			case ComponentPackage.CHART_PREFERENCES__BLOCKS :
				getBlocks( ).clear( );
				getBlocks( ).addAll( (Collection) newValue );
				return;
		}
		eDynamicSet( eFeature, newValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case ComponentPackage.CHART_PREFERENCES__LABELS :
				getLabels( ).clear( );
				return;
			case ComponentPackage.CHART_PREFERENCES__BLOCKS :
				getBlocks( ).clear( );
				return;
		}
		eDynamicUnset( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case ComponentPackage.CHART_PREFERENCES__LABELS :
				return labels != null && !labels.isEmpty( );
			case ComponentPackage.CHART_PREFERENCES__BLOCKS :
				return blocks != null && !blocks.isEmpty( );
		}
		return eDynamicIsSet( eFeature );
	}

} // ChartPreferencesImpl
