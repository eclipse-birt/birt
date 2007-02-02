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

import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Scale</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getMin <em>Min</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getMax <em>Max</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getStep <em>Step</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getUnit <em>Unit</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getMinorGridsPerUnit <em>Minor Grids Per Unit</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getStepNumber <em>Step Number</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#isShowOutside <em>Show Outside</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#isTickBetweenCategories <em>Tick Between Categories</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScaleImpl extends EObjectImpl implements Scale
{

	/**
	 * The cached value of the '{@link #getMin() <em>Min</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMin()
	 * @generated
	 * @ordered
	 */
	protected DataElement min = null;

	/**
	 * The cached value of the '{@link #getMax() <em>Max</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMax()
	 * @generated
	 * @ordered
	 */
	protected DataElement max = null;

	/**
	 * The default value of the '{@link #getStep() <em>Step</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getStep()
	 * @generated
	 * @ordered
	 */
	protected static final double STEP_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getStep() <em>Step</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getStep()
	 * @generated
	 * @ordered
	 */
	protected double step = STEP_EDEFAULT;

	/**
	 * This is true if the Step attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean stepESet = false;

	/**
	 * The default value of the '{@link #getUnit() <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected static final ScaleUnitType UNIT_EDEFAULT = ScaleUnitType.SECONDS_LITERAL;

	/**
	 * The cached value of the '{@link #getUnit() <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected ScaleUnitType unit = UNIT_EDEFAULT;

	/**
	 * This is true if the Unit attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean unitESet = false;

	/**
	 * The default value of the '{@link #getMinorGridsPerUnit() <em>Minor Grids Per Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMinorGridsPerUnit()
	 * @generated
	 * @ordered
	 */
	protected static final int MINOR_GRIDS_PER_UNIT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinorGridsPerUnit() <em>Minor Grids Per Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMinorGridsPerUnit()
	 * @generated
	 * @ordered
	 */
	protected int minorGridsPerUnit = MINOR_GRIDS_PER_UNIT_EDEFAULT;

	/**
	 * This is true if the Minor Grids Per Unit attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean minorGridsPerUnitESet = false;

	/**
	 * The default value of the '{@link #getStepNumber() <em>Step Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStepNumber()
	 * @generated
	 * @ordered
	 */
	protected static final int STEP_NUMBER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStepNumber() <em>Step Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStepNumber()
	 * @generated
	 * @ordered
	 */
	protected int stepNumber = STEP_NUMBER_EDEFAULT;

	/**
	 * This is true if the Step Number attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean stepNumberESet = false;

	/**
	 * The default value of the '{@link #isShowOutside() <em>Show Outside</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowOutside()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_OUTSIDE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isShowOutside() <em>Show Outside</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowOutside()
	 * @generated
	 * @ordered
	 */
	protected boolean showOutside = SHOW_OUTSIDE_EDEFAULT;

	/**
	 * This is true if the Show Outside attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean showOutsideESet = false;

	/**
	 * The default value of the '{@link #isTickBetweenCategories() <em>Tick Between Categories</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTickBetweenCategories()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TICK_BETWEEN_CATEGORIES_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isTickBetweenCategories() <em>Tick Between Categories</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTickBetweenCategories()
	 * @generated
	 * @ordered
	 */
	protected boolean tickBetweenCategories = TICK_BETWEEN_CATEGORIES_EDEFAULT;

	/**
	 * This is true if the Tick Between Categories attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean tickBetweenCategoriesESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ScaleImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return ComponentPackage.Literals.SCALE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataElement getMin( )
	{
		return min;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMin( DataElement newMin,
			NotificationChain msgs )
	{
		DataElement oldMin = min;
		min = newMin;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__MIN,
					oldMin,
					newMin );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setMin( DataElement newMin )
	{
		if ( newMin != min )
		{
			NotificationChain msgs = null;
			if ( min != null )
				msgs = ( (InternalEObject) min ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SCALE__MIN,
						null,
						msgs );
			if ( newMin != null )
				msgs = ( (InternalEObject) newMin ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SCALE__MIN,
						null,
						msgs );
			msgs = basicSetMin( newMin, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__MIN,
					newMin,
					newMin ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataElement getMax( )
	{
		return max;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMax( DataElement newMax,
			NotificationChain msgs )
	{
		DataElement oldMax = max;
		max = newMax;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__MAX,
					oldMax,
					newMax );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setMax( DataElement newMax )
	{
		if ( newMax != max )
		{
			NotificationChain msgs = null;
			if ( max != null )
				msgs = ( (InternalEObject) max ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SCALE__MAX,
						null,
						msgs );
			if ( newMax != null )
				msgs = ( (InternalEObject) newMax ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SCALE__MAX,
						null,
						msgs );
			msgs = basicSetMax( newMax, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__MAX,
					newMax,
					newMax ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getStep( )
	{
		return step;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setStep( double newStep )
	{
		double oldStep = step;
		step = newStep;
		boolean oldStepESet = stepESet;
		stepESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__STEP,
					oldStep,
					step,
					!oldStepESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetStep( )
	{
		double oldStep = step;
		boolean oldStepESet = stepESet;
		step = STEP_EDEFAULT;
		stepESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.SCALE__STEP,
					oldStep,
					STEP_EDEFAULT,
					oldStepESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetStep( )
	{
		return stepESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ScaleUnitType getUnit( )
	{
		return unit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnit( ScaleUnitType newUnit )
	{
		ScaleUnitType oldUnit = unit;
		unit = newUnit == null ? UNIT_EDEFAULT : newUnit;
		boolean oldUnitESet = unitESet;
		unitESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__UNIT,
					oldUnit,
					unit,
					!oldUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetUnit( )
	{
		ScaleUnitType oldUnit = unit;
		boolean oldUnitESet = unitESet;
		unit = UNIT_EDEFAULT;
		unitESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.SCALE__UNIT,
					oldUnit,
					UNIT_EDEFAULT,
					oldUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetUnit( )
	{
		return unitESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinorGridsPerUnit( )
	{
		return minorGridsPerUnit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinorGridsPerUnit( int newMinorGridsPerUnit )
	{
		int oldMinorGridsPerUnit = minorGridsPerUnit;
		minorGridsPerUnit = newMinorGridsPerUnit;
		boolean oldMinorGridsPerUnitESet = minorGridsPerUnitESet;
		minorGridsPerUnitESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT,
					oldMinorGridsPerUnit,
					minorGridsPerUnit,
					!oldMinorGridsPerUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMinorGridsPerUnit( )
	{
		int oldMinorGridsPerUnit = minorGridsPerUnit;
		boolean oldMinorGridsPerUnitESet = minorGridsPerUnitESet;
		minorGridsPerUnit = MINOR_GRIDS_PER_UNIT_EDEFAULT;
		minorGridsPerUnitESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT,
					oldMinorGridsPerUnit,
					MINOR_GRIDS_PER_UNIT_EDEFAULT,
					oldMinorGridsPerUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMinorGridsPerUnit( )
	{
		return minorGridsPerUnitESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getStepNumber( )
	{
		return stepNumber;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStepNumber( int newStepNumber )
	{
		int oldStepNumber = stepNumber;
		stepNumber = newStepNumber;
		boolean oldStepNumberESet = stepNumberESet;
		stepNumberESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__STEP_NUMBER,
					oldStepNumber,
					stepNumber,
					!oldStepNumberESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetStepNumber( )
	{
		int oldStepNumber = stepNumber;
		boolean oldStepNumberESet = stepNumberESet;
		stepNumber = STEP_NUMBER_EDEFAULT;
		stepNumberESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.SCALE__STEP_NUMBER,
					oldStepNumber,
					STEP_NUMBER_EDEFAULT,
					oldStepNumberESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetStepNumber( )
	{
		return stepNumberESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isShowOutside( )
	{
		return showOutside;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShowOutside( boolean newShowOutside )
	{
		boolean oldShowOutside = showOutside;
		showOutside = newShowOutside;
		boolean oldShowOutsideESet = showOutsideESet;
		showOutsideESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__SHOW_OUTSIDE,
					oldShowOutside,
					showOutside,
					!oldShowOutsideESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetShowOutside( )
	{
		boolean oldShowOutside = showOutside;
		boolean oldShowOutsideESet = showOutsideESet;
		showOutside = SHOW_OUTSIDE_EDEFAULT;
		showOutsideESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.SCALE__SHOW_OUTSIDE,
					oldShowOutside,
					SHOW_OUTSIDE_EDEFAULT,
					oldShowOutsideESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetShowOutside( )
	{
		return showOutsideESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isTickBetweenCategories( )
	{
		return tickBetweenCategories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTickBetweenCategories( boolean newTickBetweenCategories )
	{
		boolean oldTickBetweenCategories = tickBetweenCategories;
		tickBetweenCategories = newTickBetweenCategories;
		boolean oldTickBetweenCategoriesESet = tickBetweenCategoriesESet;
		tickBetweenCategoriesESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__TICK_BETWEEN_CATEGORIES,
					oldTickBetweenCategories,
					tickBetweenCategories,
					!oldTickBetweenCategoriesESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetTickBetweenCategories( )
	{
		boolean oldTickBetweenCategories = tickBetweenCategories;
		boolean oldTickBetweenCategoriesESet = tickBetweenCategoriesESet;
		tickBetweenCategories = TICK_BETWEEN_CATEGORIES_EDEFAULT;
		tickBetweenCategoriesESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.SCALE__TICK_BETWEEN_CATEGORIES,
					oldTickBetweenCategories,
					TICK_BETWEEN_CATEGORIES_EDEFAULT,
					oldTickBetweenCategoriesESet ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetTickBetweenCategories( )
	{
		return tickBetweenCategoriesESet;
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
			case ComponentPackage.SCALE__MIN :
				return basicSetMin( null, msgs );
			case ComponentPackage.SCALE__MAX :
				return basicSetMax( null, msgs );
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
			case ComponentPackage.SCALE__MIN :
				return getMin( );
			case ComponentPackage.SCALE__MAX :
				return getMax( );
			case ComponentPackage.SCALE__STEP :
				return new Double( getStep( ) );
			case ComponentPackage.SCALE__UNIT :
				return getUnit( );
			case ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT :
				return new Integer( getMinorGridsPerUnit( ) );
			case ComponentPackage.SCALE__STEP_NUMBER :
				return new Integer( getStepNumber( ) );
			case ComponentPackage.SCALE__SHOW_OUTSIDE :
				return isShowOutside( ) ? Boolean.TRUE : Boolean.FALSE;
			case ComponentPackage.SCALE__TICK_BETWEEN_CATEGORIES :
				return isTickBetweenCategories( ) ? Boolean.TRUE
						: Boolean.FALSE;
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
			case ComponentPackage.SCALE__MIN :
				setMin( (DataElement) newValue );
				return;
			case ComponentPackage.SCALE__MAX :
				setMax( (DataElement) newValue );
				return;
			case ComponentPackage.SCALE__STEP :
				setStep( ( (Double) newValue ).doubleValue( ) );
				return;
			case ComponentPackage.SCALE__UNIT :
				setUnit( (ScaleUnitType) newValue );
				return;
			case ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT :
				setMinorGridsPerUnit( ( (Integer) newValue ).intValue( ) );
				return;
			case ComponentPackage.SCALE__STEP_NUMBER :
				setStepNumber( ( (Integer) newValue ).intValue( ) );
				return;
			case ComponentPackage.SCALE__SHOW_OUTSIDE :
				setShowOutside( ( (Boolean) newValue ).booleanValue( ) );
				return;
			case ComponentPackage.SCALE__TICK_BETWEEN_CATEGORIES :
				setTickBetweenCategories( ( (Boolean) newValue ).booleanValue( ) );
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
			case ComponentPackage.SCALE__MIN :
				setMin( (DataElement) null );
				return;
			case ComponentPackage.SCALE__MAX :
				setMax( (DataElement) null );
				return;
			case ComponentPackage.SCALE__STEP :
				unsetStep( );
				return;
			case ComponentPackage.SCALE__UNIT :
				unsetUnit( );
				return;
			case ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT :
				unsetMinorGridsPerUnit( );
				return;
			case ComponentPackage.SCALE__STEP_NUMBER :
				unsetStepNumber( );
				return;
			case ComponentPackage.SCALE__SHOW_OUTSIDE :
				unsetShowOutside( );
				return;
			case ComponentPackage.SCALE__TICK_BETWEEN_CATEGORIES :
				unsetTickBetweenCategories( );
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
			case ComponentPackage.SCALE__MIN :
				return min != null;
			case ComponentPackage.SCALE__MAX :
				return max != null;
			case ComponentPackage.SCALE__STEP :
				return isSetStep( );
			case ComponentPackage.SCALE__UNIT :
				return isSetUnit( );
			case ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT :
				return isSetMinorGridsPerUnit( );
			case ComponentPackage.SCALE__STEP_NUMBER :
				return isSetStepNumber( );
			case ComponentPackage.SCALE__SHOW_OUTSIDE :
				return isSetShowOutside( );
			case ComponentPackage.SCALE__TICK_BETWEEN_CATEGORIES :
				return isSetTickBetweenCategories( );
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
		result.append( " (step: " ); //$NON-NLS-1$
		if ( stepESet )
			result.append( step );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", unit: " ); //$NON-NLS-1$
		if ( unitESet )
			result.append( unit );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", minorGridsPerUnit: " ); //$NON-NLS-1$
		if ( minorGridsPerUnitESet )
			result.append( minorGridsPerUnit );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", stepNumber: " ); //$NON-NLS-1$
		if ( stepNumberESet )
			result.append( stepNumber );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", showOutside: " ); //$NON-NLS-1$
		if ( showOutsideESet )
			result.append( showOutside );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", tickBetweenCategories: " ); //$NON-NLS-1$
		if ( tickBetweenCategoriesESet )
			result.append( tickBetweenCategories );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

} // ScaleImpl
