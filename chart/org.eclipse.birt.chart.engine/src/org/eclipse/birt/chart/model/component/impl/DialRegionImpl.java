/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.component.impl;

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Dial
 * Region</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialRegionImpl#getInnerRadius
 * <em>Inner Radius</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialRegionImpl#getOuterRadius
 * <em>Outer Radius</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DialRegionImpl extends MarkerRangeImpl implements DialRegion {

	/**
	 * The default value of the '{@link #getInnerRadius() <em>Inner Radius</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getInnerRadius()
	 * @generated
	 * @ordered
	 */
	protected static final double INNER_RADIUS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getInnerRadius() <em>Inner Radius</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getInnerRadius()
	 * @generated
	 * @ordered
	 */
	protected double innerRadius = INNER_RADIUS_EDEFAULT;

	/**
	 * This is true if the Inner Radius attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean innerRadiusESet;

	/**
	 * The default value of the '{@link #getOuterRadius() <em>Outer Radius</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getOuterRadius()
	 * @generated
	 * @ordered
	 */
	protected static final double OUTER_RADIUS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getOuterRadius() <em>Outer Radius</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getOuterRadius()
	 * @generated
	 * @ordered
	 */
	protected double outerRadius = OUTER_RADIUS_EDEFAULT;

	/**
	 * This is true if the Outer Radius attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean outerRadiusESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DialRegionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.DIAL_REGION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getInnerRadius() {
		return innerRadius;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setInnerRadius(double newInnerRadius) {
		double oldInnerRadius = innerRadius;
		innerRadius = newInnerRadius;
		boolean oldInnerRadiusESet = innerRadiusESet;
		innerRadiusESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL_REGION__INNER_RADIUS,
					oldInnerRadius, innerRadius, !oldInnerRadiusESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetInnerRadius() {
		double oldInnerRadius = innerRadius;
		boolean oldInnerRadiusESet = innerRadiusESet;
		innerRadius = INNER_RADIUS_EDEFAULT;
		innerRadiusESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.DIAL_REGION__INNER_RADIUS,
					oldInnerRadius, INNER_RADIUS_EDEFAULT, oldInnerRadiusESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetInnerRadius() {
		return innerRadiusESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getOuterRadius() {
		return outerRadius;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setOuterRadius(double newOuterRadius) {
		double oldOuterRadius = outerRadius;
		outerRadius = newOuterRadius;
		boolean oldOuterRadiusESet = outerRadiusESet;
		outerRadiusESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL_REGION__OUTER_RADIUS,
					oldOuterRadius, outerRadius, !oldOuterRadiusESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetOuterRadius() {
		double oldOuterRadius = outerRadius;
		boolean oldOuterRadiusESet = outerRadiusESet;
		outerRadius = OUTER_RADIUS_EDEFAULT;
		outerRadiusESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.DIAL_REGION__OUTER_RADIUS,
					oldOuterRadius, OUTER_RADIUS_EDEFAULT, oldOuterRadiusESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetOuterRadius() {
		return outerRadiusESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ComponentPackage.DIAL_REGION__INNER_RADIUS:
			return getInnerRadius();
		case ComponentPackage.DIAL_REGION__OUTER_RADIUS:
			return getOuterRadius();
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
		case ComponentPackage.DIAL_REGION__INNER_RADIUS:
			setInnerRadius((Double) newValue);
			return;
		case ComponentPackage.DIAL_REGION__OUTER_RADIUS:
			setOuterRadius((Double) newValue);
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
		case ComponentPackage.DIAL_REGION__INNER_RADIUS:
			unsetInnerRadius();
			return;
		case ComponentPackage.DIAL_REGION__OUTER_RADIUS:
			unsetOuterRadius();
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
		case ComponentPackage.DIAL_REGION__INNER_RADIUS:
			return isSetInnerRadius();
		case ComponentPackage.DIAL_REGION__OUTER_RADIUS:
			return isSetOuterRadius();
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
		if (eIsProxy()) {
			return super.toString();
		}

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (innerRadius: "); //$NON-NLS-1$
		if (innerRadiusESet) {
			result.append(innerRadius);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", outerRadius: "); //$NON-NLS-1$
		if (outerRadiusESet) {
			result.append(outerRadius);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * @return instance of <code>DialRegion</code>.
	 */
	public static final DialRegion create() {
		DialRegion dr = ComponentFactory.eINSTANCE.createDialRegion();
		((DialRegionImpl) dr).initialize();
		return dr;
	}

	public static final DialRegion createDefault() {
		DialRegion dr = ComponentFactory.eINSTANCE.createDialRegion();
		((DialRegionImpl) dr).initDefault();
		return dr;
	}

	/**
	 *
	 */
	public final void initialize() {
		LineAttributes liaOutline = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		liaOutline.setVisible(false);
		setOutline(liaOutline);
		setLabel(LabelImpl.create());
		setLabelAnchor(Anchor.NORTH_LITERAL);
	}

	public final void initDefault() {
		LineAttributes liaOutline = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, false);
		setOutline(liaOutline);
		setLabel(LabelImpl.createDefault());
		labelAnchor = Anchor.NORTH_LITERAL;
	}

	/**
	 * @generated
	 */
	@Override
	public DialRegion copyInstance() {
		DialRegionImpl dest = new DialRegionImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(DialRegion src) {

		super.set(src);

		// attributes

		innerRadius = src.getInnerRadius();

		innerRadiusESet = src.isSetInnerRadius();

		outerRadius = src.getOuterRadius();

		outerRadiusESet = src.isSetOuterRadius();

	}

} // DialRegionImpl
