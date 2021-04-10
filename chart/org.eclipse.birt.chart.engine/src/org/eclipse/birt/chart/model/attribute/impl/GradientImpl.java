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

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Gradient</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.GradientImpl#getStartColor
 * <em>Start Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.GradientImpl#getEndColor
 * <em>End Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.GradientImpl#getDirection
 * <em>Direction</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.GradientImpl#isCyclic
 * <em>Cyclic</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.GradientImpl#getTransparency
 * <em>Transparency</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GradientImpl extends FillImpl implements Gradient {

	/**
	 * The cached value of the '{@link #getStartColor() <em>Start Color</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStartColor()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition startColor;

	/**
	 * The cached value of the '{@link #getEndColor() <em>End Color</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEndColor()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition endColor;

	/**
	 * The default value of the '{@link #getDirection() <em>Direction</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDirection()
	 * @generated
	 * @ordered
	 */
	protected static final double DIRECTION_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getDirection() <em>Direction</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDirection()
	 * @generated
	 * @ordered
	 */
	protected double direction = DIRECTION_EDEFAULT;

	/**
	 * This is true if the Direction attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean directionESet;

	/**
	 * The default value of the '{@link #isCyclic() <em>Cyclic</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isCyclic()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CYCLIC_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCyclic() <em>Cyclic</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isCyclic()
	 * @generated
	 * @ordered
	 */
	protected boolean cyclic = CYCLIC_EDEFAULT;

	/**
	 * This is true if the Cyclic attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean cyclicESet;

	/**
	 * The default value of the ' {@link #getTransparency() <em>Transparency</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTransparency()
	 * @generated
	 * @ordered
	 */
	protected static final int TRANSPARENCY_EDEFAULT = 0;

	/**
	 * The cached value of the ' {@link #getTransparency() <em>Transparency</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTransparency()
	 * @generated
	 * @ordered
	 */
	protected int transparency = TRANSPARENCY_EDEFAULT;

	/**
	 * This is true if the Transparency attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean transparencyESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected GradientImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.GRADIENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ColorDefinition getStartColor() {
		return startColor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetStartColor(ColorDefinition newStartColor, NotificationChain msgs) {
		ColorDefinition oldStartColor = startColor;
		startColor = newStartColor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.GRADIENT__START_COLOR, oldStartColor, newStartColor);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStartColor(ColorDefinition newStartColor) {
		if (newStartColor != startColor) {
			NotificationChain msgs = null;
			if (startColor != null)
				msgs = ((InternalEObject) startColor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.GRADIENT__START_COLOR, null, msgs);
			if (newStartColor != null)
				msgs = ((InternalEObject) newStartColor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.GRADIENT__START_COLOR, null, msgs);
			msgs = basicSetStartColor(newStartColor, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.GRADIENT__START_COLOR, newStartColor,
					newStartColor));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ColorDefinition getEndColor() {
		return endColor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetEndColor(ColorDefinition newEndColor, NotificationChain msgs) {
		ColorDefinition oldEndColor = endColor;
		endColor = newEndColor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.GRADIENT__END_COLOR, oldEndColor, newEndColor);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setEndColor(ColorDefinition newEndColor) {
		if (newEndColor != endColor) {
			NotificationChain msgs = null;
			if (endColor != null)
				msgs = ((InternalEObject) endColor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.GRADIENT__END_COLOR, null, msgs);
			if (newEndColor != null)
				msgs = ((InternalEObject) newEndColor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.GRADIENT__END_COLOR, null, msgs);
			msgs = basicSetEndColor(newEndColor, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.GRADIENT__END_COLOR, newEndColor,
					newEndColor));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDirection(double newDirection) {
		double oldDirection = direction;
		direction = newDirection;
		boolean oldDirectionESet = directionESet;
		directionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.GRADIENT__DIRECTION, oldDirection,
					direction, !oldDirectionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetDirection() {
		double oldDirection = direction;
		boolean oldDirectionESet = directionESet;
		direction = DIRECTION_EDEFAULT;
		directionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.GRADIENT__DIRECTION, oldDirection,
					DIRECTION_EDEFAULT, oldDirectionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetDirection() {
		return directionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isCyclic() {
		return cyclic;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCyclic(boolean newCyclic) {
		boolean oldCyclic = cyclic;
		cyclic = newCyclic;
		boolean oldCyclicESet = cyclicESet;
		cyclicESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.GRADIENT__CYCLIC, oldCyclic, cyclic,
					!oldCyclicESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetCyclic() {
		boolean oldCyclic = cyclic;
		boolean oldCyclicESet = cyclicESet;
		cyclic = CYCLIC_EDEFAULT;
		cyclicESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.GRADIENT__CYCLIC, oldCyclic,
					CYCLIC_EDEFAULT, oldCyclicESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetCyclic() {
		return cyclicESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getTransparency() {
		return transparency;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTransparency(int newTransparency) {
		int oldTransparency = transparency;
		transparency = newTransparency;
		boolean oldTransparencyESet = transparencyESet;
		transparencyESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.GRADIENT__TRANSPARENCY,
					oldTransparency, transparency, !oldTransparencyESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetTransparency() {
		int oldTransparency = transparency;
		boolean oldTransparencyESet = transparencyESet;
		transparency = TRANSPARENCY_EDEFAULT;
		transparencyESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.GRADIENT__TRANSPARENCY,
					oldTransparency, TRANSPARENCY_EDEFAULT, oldTransparencyESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetTransparency() {
		return transparencyESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AttributePackage.GRADIENT__START_COLOR:
			return basicSetStartColor(null, msgs);
		case AttributePackage.GRADIENT__END_COLOR:
			return basicSetEndColor(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AttributePackage.GRADIENT__START_COLOR:
			return getStartColor();
		case AttributePackage.GRADIENT__END_COLOR:
			return getEndColor();
		case AttributePackage.GRADIENT__DIRECTION:
			return getDirection();
		case AttributePackage.GRADIENT__CYCLIC:
			return isCyclic();
		case AttributePackage.GRADIENT__TRANSPARENCY:
			return getTransparency();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AttributePackage.GRADIENT__START_COLOR:
			setStartColor((ColorDefinition) newValue);
			return;
		case AttributePackage.GRADIENT__END_COLOR:
			setEndColor((ColorDefinition) newValue);
			return;
		case AttributePackage.GRADIENT__DIRECTION:
			setDirection((Double) newValue);
			return;
		case AttributePackage.GRADIENT__CYCLIC:
			setCyclic((Boolean) newValue);
			return;
		case AttributePackage.GRADIENT__TRANSPARENCY:
			setTransparency((Integer) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case AttributePackage.GRADIENT__START_COLOR:
			setStartColor((ColorDefinition) null);
			return;
		case AttributePackage.GRADIENT__END_COLOR:
			setEndColor((ColorDefinition) null);
			return;
		case AttributePackage.GRADIENT__DIRECTION:
			unsetDirection();
			return;
		case AttributePackage.GRADIENT__CYCLIC:
			unsetCyclic();
			return;
		case AttributePackage.GRADIENT__TRANSPARENCY:
			unsetTransparency();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case AttributePackage.GRADIENT__START_COLOR:
			return startColor != null;
		case AttributePackage.GRADIENT__END_COLOR:
			return endColor != null;
		case AttributePackage.GRADIENT__DIRECTION:
			return isSetDirection();
		case AttributePackage.GRADIENT__CYCLIC:
			return isSetCyclic();
		case AttributePackage.GRADIENT__TRANSPARENCY:
			return isSetTransparency();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (direction: "); //$NON-NLS-1$
		if (directionESet)
			result.append(direction);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", cyclic: "); //$NON-NLS-1$
		if (cyclicESet)
			result.append(cyclic);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", transparency: "); //$NON-NLS-1$
		if (transparencyESet)
			result.append(transparency);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated
	 */
	protected void set(Gradient src) {

		// children

		if (src.getStartColor() != null) {
			setStartColor(src.getStartColor().copyInstance());
		}

		if (src.getEndColor() != null) {
			setEndColor(src.getEndColor().copyInstance());
		}

		// attributes

		direction = src.getDirection();

		directionESet = src.isSetDirection();

		cyclic = src.isCyclic();

		cyclicESet = src.isSetCyclic();

		transparency = src.getTransparency();

		transparencyESet = src.isSetTransparency();

	}

	/**
	 * A convenience method provided to create a gradient instance with all member
	 * variables initialized
	 * 
	 * @param cdStart
	 * @param cdEnd
	 * @param dDirectionInDegrees Must lie within the range of (90 >= 0 >= -90)
	 * @param bCyclic
	 * 
	 * @return grandient instance with setting 'isSet' flag.
	 */
	public static final Gradient create(ColorDefinition cdStart, ColorDefinition cdEnd, double dDirectionInDegrees,
			boolean bCyclic) {
		final Gradient g = AttributeFactory.eINSTANCE.createGradient();
		g.setStartColor(cdStart);
		g.setEndColor(cdEnd);
		g.setDirection(dDirectionInDegrees);
		g.setCyclic(bCyclic);
		return g;
	}

	public static final Gradient create(ColorDefinition cdStart, ColorDefinition cdEnd) {
		final Gradient g = AttributeFactory.eINSTANCE.createGradient();
		g.setStartColor(cdStart);
		g.setEndColor(cdEnd);
		return g;
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	public Gradient copyInstance() {
		GradientImpl dest = new GradientImpl();

		ColorDefinition tStartColor = getStartColor();
		if (tStartColor != null) {
			dest.startColor = tStartColor.copyInstance();
		}

		ColorDefinition tEndColor = getEndColor();
		if (tEndColor != null) {
			dest.endColor = tEndColor.copyInstance();
		}

		dest.direction = getDirection();
		dest.directionESet = isSetDirection();
		dest.cyclic = isCyclic();
		dest.cyclicESet = isSetCyclic();
		dest.transparency = getTransparency();
		dest.transparencyESet = isSetTransparency();
		return dest;
	}

} // GradientImpl
