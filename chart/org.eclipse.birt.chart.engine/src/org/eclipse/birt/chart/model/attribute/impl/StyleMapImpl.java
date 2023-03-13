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

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Style
 * Map</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl#getComponentName
 * <em>Component Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl#getStyle
 * <em>Style</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StyleMapImpl extends EObjectImpl implements StyleMap {

	/**
	 * The default value of the ' {@link #getComponentName() <em>Component
	 * Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getComponentName()
	 * @generated
	 * @ordered
	 */
	protected static final StyledComponent COMPONENT_NAME_EDEFAULT = StyledComponent.CHART_ALL_LITERAL;

	/**
	 * The cached value of the ' {@link #getComponentName() <em>Component
	 * Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getComponentName()
	 * @generated
	 * @ordered
	 */
	protected StyledComponent componentName = COMPONENT_NAME_EDEFAULT;

	/**
	 * This is true if the Component Name attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean componentNameESet;

	/**
	 * The cached value of the '{@link #getStyle() <em>Style</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getStyle()
	 * @generated
	 * @ordered
	 */
	protected Style style;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected StyleMapImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.STYLE_MAP;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StyledComponent getComponentName() {
		return componentName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setComponentName(StyledComponent newComponentName) {
		StyledComponent oldComponentName = componentName;
		componentName = newComponentName == null ? COMPONENT_NAME_EDEFAULT : newComponentName;
		boolean oldComponentNameESet = componentNameESet;
		componentNameESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.STYLE_MAP__COMPONENT_NAME,
					oldComponentName, componentName, !oldComponentNameESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetComponentName() {
		StyledComponent oldComponentName = componentName;
		boolean oldComponentNameESet = componentNameESet;
		componentName = COMPONENT_NAME_EDEFAULT;
		componentNameESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.STYLE_MAP__COMPONENT_NAME,
					oldComponentName, COMPONENT_NAME_EDEFAULT, oldComponentNameESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetComponentName() {
		return componentNameESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Style getStyle() {
		return style;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetStyle(Style newStyle, NotificationChain msgs) {
		Style oldStyle = style;
		style = newStyle;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.STYLE_MAP__STYLE, oldStyle, newStyle);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setStyle(Style newStyle) {
		if (newStyle != style) {
			NotificationChain msgs = null;
			if (style != null) {
				msgs = ((InternalEObject) style).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE_MAP__STYLE, null, msgs);
			}
			if (newStyle != null) {
				msgs = ((InternalEObject) newStyle).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.STYLE_MAP__STYLE, null, msgs);
			}
			msgs = basicSetStyle(newStyle, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.STYLE_MAP__STYLE, newStyle,
					newStyle));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AttributePackage.STYLE_MAP__STYLE:
			return basicSetStyle(null, msgs);
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
		case AttributePackage.STYLE_MAP__COMPONENT_NAME:
			return getComponentName();
		case AttributePackage.STYLE_MAP__STYLE:
			return getStyle();
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
		case AttributePackage.STYLE_MAP__COMPONENT_NAME:
			setComponentName((StyledComponent) newValue);
			return;
		case AttributePackage.STYLE_MAP__STYLE:
			setStyle((Style) newValue);
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
		case AttributePackage.STYLE_MAP__COMPONENT_NAME:
			unsetComponentName();
			return;
		case AttributePackage.STYLE_MAP__STYLE:
			setStyle((Style) null);
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
		case AttributePackage.STYLE_MAP__COMPONENT_NAME:
			return isSetComponentName();
		case AttributePackage.STYLE_MAP__STYLE:
			return style != null;
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
		result.append(" (componentName: "); //$NON-NLS-1$
		if (componentNameESet) {
			result.append(componentName);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * @param name
	 * @param style
	 * @return
	 */
	public static StyleMap create(StyledComponent name, Style style) {
		StyleMap sm = AttributeFactory.eINSTANCE.createStyleMap();
		sm.setComponentName(name);
		sm.setStyle(style);
		return sm;
	}

	/**
	 * @generated
	 */
	@Override
	public StyleMap copyInstance() {
		StyleMapImpl dest = new StyleMapImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(StyleMap src) {

		// attributes

		componentName = src.getComponentName();

		componentNameESet = src.isSetComponentName();

		style = src.getStyle();

	}

} // StyleMapImpl
