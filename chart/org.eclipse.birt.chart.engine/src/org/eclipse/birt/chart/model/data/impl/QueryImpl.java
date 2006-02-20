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

import java.util.Collection;

import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.Rule;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Query</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.data.impl.QueryImpl#getDefinition <em>Definition</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.data.impl.QueryImpl#getRules <em>Rules</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QueryImpl extends EObjectImpl implements Query
{

	/**
	 * The default value of the '{@link #getDefinition() <em>Definition</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDefinition()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFINITION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefinition() <em>Definition</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDefinition()
	 * @generated
	 * @ordered
	 */
	protected String definition = DEFINITION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRules() <em>Rules</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getRules()
	 * @generated
	 * @ordered
	 */
	protected EList rules = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected QueryImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return DataPackage.Literals.QUERY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefinition( )
	{
		return definition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefinition( String newDefinition )
	{
		String oldDefinition = definition;
		definition = newDefinition;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					DataPackage.QUERY__DEFINITION,
					oldDefinition,
					definition ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList getRules( )
	{
		if ( rules == null )
		{
			rules = new EObjectContainmentEList( Rule.class,
					this,
					DataPackage.QUERY__RULES );
		}
		return rules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, NotificationChain msgs )
	{
		switch ( featureID )
		{
			case DataPackage.QUERY__RULES :
				return ( (InternalEList) getRules( ) ).basicRemove( otherEnd,
						msgs );
		}
		return super.eInverseRemove( otherEnd, featureID, msgs );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet( int featureID, boolean resolve, boolean coreType )
	{
		switch ( featureID )
		{
			case DataPackage.QUERY__DEFINITION :
				return getDefinition( );
			case DataPackage.QUERY__RULES :
				return getRules( );
		}
		return super.eGet( featureID, resolve, coreType );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet( int featureID, Object newValue )
	{
		switch ( featureID )
		{
			case DataPackage.QUERY__DEFINITION :
				setDefinition( (String) newValue );
				return;
			case DataPackage.QUERY__RULES :
				getRules( ).clear( );
				getRules( ).addAll( (Collection) newValue );
				return;
		}
		super.eSet( featureID, newValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset( int featureID )
	{
		switch ( featureID )
		{
			case DataPackage.QUERY__DEFINITION :
				setDefinition( DEFINITION_EDEFAULT );
				return;
			case DataPackage.QUERY__RULES :
				getRules( ).clear( );
				return;
		}
		super.eUnset( featureID );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet( int featureID )
	{
		switch ( featureID )
		{
			case DataPackage.QUERY__DEFINITION :
				return DEFINITION_EDEFAULT == null ? definition != null
						: !DEFINITION_EDEFAULT.equals( definition );
			case DataPackage.QUERY__RULES :
				return rules != null && !rules.isEmpty( );
		}
		return super.eIsSet( featureID );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (definition: " ); //$NON-NLS-1$
		result.append( definition );
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenience method provided to create a new initialized query instance
	 * 
	 * @param sDefinition
	 * @return
	 */
	public static final Query create( String sDefinition )
	{
		final Query q = DataFactory.eINSTANCE.createQuery( );
		q.setDefinition( sDefinition );
		return q;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.data.Query#isDefined()
	 */
	public final boolean isDefined( )
	{
		final String sDefinition = getDefinition( );
		return sDefinition != null && sDefinition.trim( ).length( ) != 0;
	}

} //QueryImpl
