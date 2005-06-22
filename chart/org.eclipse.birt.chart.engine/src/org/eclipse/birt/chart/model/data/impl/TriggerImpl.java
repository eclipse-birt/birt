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

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Trigger</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.birt.chart.model.data.impl.TriggerImpl#getCondition <em>Condition</em>}
 * </li>
 * <li>
 * {@link org.eclipse.birt.chart.model.data.impl.TriggerImpl#getAction <em>Action</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class TriggerImpl extends EObjectImpl implements Trigger
{

	/**
	 * The default value of the '{@link #getCondition() <em>Condition</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCondition()
	 * @generated
	 * @ordered
	 */
	protected static final TriggerCondition CONDITION_EDEFAULT = TriggerCondition.MOUSE_HOVER_LITERAL;

	/**
	 * The cached value of the '{@link #getCondition() <em>Condition</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCondition()
	 * @generated
	 * @ordered
	 */
	protected TriggerCondition condition = CONDITION_EDEFAULT;

	/**
	 * This is true if the Condition attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean conditionESet = false;

	/**
	 * The cached value of the '{@link #getAction() <em>Action</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAction()
	 * @generated
	 * @ordered
	 */
	protected Action action = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TriggerImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return DataPackage.eINSTANCE.getTrigger( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TriggerCondition getCondition( )
	{
		return condition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCondition( TriggerCondition newCondition )
	{
		TriggerCondition oldCondition = condition;
		condition = newCondition == null ? CONDITION_EDEFAULT : newCondition;
		boolean oldConditionESet = conditionESet;
		conditionESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					DataPackage.TRIGGER__CONDITION,
					oldCondition,
					condition,
					!oldConditionESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetCondition( )
	{
		TriggerCondition oldCondition = condition;
		boolean oldConditionESet = conditionESet;
		condition = CONDITION_EDEFAULT;
		conditionESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					DataPackage.TRIGGER__CONDITION,
					oldCondition,
					CONDITION_EDEFAULT,
					oldConditionESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetCondition( )
	{
		return conditionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Action getAction( )
	{
		return action;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetAction( Action newAction,
			NotificationChain msgs )
	{
		Action oldAction = action;
		action = newAction;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					DataPackage.TRIGGER__ACTION,
					oldAction,
					newAction );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAction( Action newAction )
	{
		if ( newAction != action )
		{
			NotificationChain msgs = null;
			if ( action != null )
				msgs = ( (InternalEObject) action ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE - DataPackage.TRIGGER__ACTION,
						null,
						msgs );
			if ( newAction != null )
				msgs = ( (InternalEObject) newAction ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE - DataPackage.TRIGGER__ACTION,
						null,
						msgs );
			msgs = basicSetAction( newAction, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					DataPackage.TRIGGER__ACTION,
					newAction,
					newAction ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, Class baseClass, NotificationChain msgs )
	{
		if ( featureID >= 0 )
		{
			switch ( eDerivedStructuralFeatureID( featureID, baseClass ) )
			{
				case DataPackage.TRIGGER__ACTION :
					return basicSetAction( null, msgs );
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
	 * 
	 * @generated
	 */
	public Object eGet( EStructuralFeature eFeature, boolean resolve )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case DataPackage.TRIGGER__CONDITION :
				return getCondition( );
			case DataPackage.TRIGGER__ACTION :
				return getAction( );
		}
		return eDynamicGet( eFeature, resolve );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eSet( EStructuralFeature eFeature, Object newValue )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case DataPackage.TRIGGER__CONDITION :
				setCondition( (TriggerCondition) newValue );
				return;
			case DataPackage.TRIGGER__ACTION :
				setAction( (Action) newValue );
				return;
		}
		eDynamicSet( eFeature, newValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eUnset( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case DataPackage.TRIGGER__CONDITION :
				unsetCondition( );
				return;
			case DataPackage.TRIGGER__ACTION :
				setAction( (Action) null );
				return;
		}
		eDynamicUnset( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean eIsSet( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case DataPackage.TRIGGER__CONDITION :
				return isSetCondition( );
			case DataPackage.TRIGGER__ACTION :
				return action != null;
		}
		return eDynamicIsSet( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (condition: " ); //$NON-NLS-1$
		if ( conditionESet )
			result.append( condition );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * This convenience method initializes and provides a trigger instance for
	 * use
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	public static final Trigger create( TriggerCondition tc, Action a )
	{
		final Trigger tg = DataFactory.eINSTANCE.createTrigger( );
		if ( tc == null )
		{
			tc = TriggerCondition.MOUSE_CLICK_LITERAL;
		}
		tg.setCondition( tc );
		tg.setAction( a );
		return tg;
	}

} //TriggerImpl
