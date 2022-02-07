/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id: MultiURLValuesImpl.java,v 1.8 2009/07/23 05:33:17 heli Exp $
 */

package org.eclipse.birt.chart.model.attribute.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.MenuStylesKeyType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Multi
 * URL Values</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl#getURLValues
 * <em>URL Values</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl#getTooltip
 * <em>Tooltip</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl#getPropertiesMap
 * <em>Properties Map</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MultiURLValuesImpl extends ActionValueImpl implements MultiURLValues {

	private static final String MENU_PROPERTIES = "zIndex:99999;backgroundColor:#FAFFF8;width:auto;height:auto;border:1px solid #333333;filter:progid:DXImageTransform.Microsoft.Shadow(Color:#333333,Direction=120,strength=5)"; //$NON-NLS-1$
	private static final String MENU_ITEM_PROPERTIES = "color:Black;fontSize:10pt;paddingLeft:10px;paddingRight:10px;paddingTop:2px;paddingBottom:2px"; //$NON-NLS-1$
	private static final String ON_MOUSE_OVER_PROPERTIES = "backgroundColor:#245DDB;color:#FFFFFF"; //$NON-NLS-1$
	private static final String ON_MOUSE_OUT_PROPERTIES = "backgroundColor:#FAFFF8;color:#000000"; //$NON-NLS-1$
	public static final Map<String, String> DEFAULT_PROPERTIES_MAP = new HashMap<String, String>();
	static {
		DEFAULT_PROPERTIES_MAP.put(MenuStylesKeyType.MENU.getName(), MENU_PROPERTIES);
		DEFAULT_PROPERTIES_MAP.put(MenuStylesKeyType.MENU_ITEM.getName(), MENU_ITEM_PROPERTIES);
		DEFAULT_PROPERTIES_MAP.put(MenuStylesKeyType.ON_MOUSE_OVER.getName(), ON_MOUSE_OVER_PROPERTIES);
		DEFAULT_PROPERTIES_MAP.put(MenuStylesKeyType.ON_MOUSE_OUT.getName(), ON_MOUSE_OUT_PROPERTIES);
	}

	/**
	 * The cached value of the '{@link #getURLValues() <em>URL Values</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getURLValues()
	 * @generated
	 * @ordered
	 */
	protected EList<URLValue> uRLValues;

	/**
	 * The default value of the '{@link #getTooltip() <em>Tooltip</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTooltip()
	 * @generated
	 * @ordered
	 */
	protected static final String TOOLTIP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTooltip() <em>Tooltip</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTooltip()
	 * @generated
	 * @ordered
	 */
	protected String tooltip = TOOLTIP_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPropertiesMap() <em>Properties Map</em>}'
	 * map. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPropertiesMap()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> propertiesMap;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MultiURLValuesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.MULTI_URL_VALUES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<URLValue> getURLValues() {
		if (uRLValues == null) {
			uRLValues = new EObjectContainmentEList<URLValue>(URLValue.class, this,
					AttributePackage.MULTI_URL_VALUES__URL_VALUES);
		}
		return uRLValues;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTooltip(String newTooltip) {
		String oldTooltip = tooltip;
		tooltip = newTooltip;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.MULTI_URL_VALUES__TOOLTIP,
					oldTooltip, tooltip));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EMap<String, String> getPropertiesMap() {
		if (propertiesMap == null) {
			propertiesMap = new EcoreEMap<String, String>(AttributePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
					EStringToStringMapEntryImpl.class, this, AttributePackage.MULTI_URL_VALUES__PROPERTIES_MAP);
		}
		return propertiesMap;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AttributePackage.MULTI_URL_VALUES__URL_VALUES:
			return ((InternalEList<?>) getURLValues()).basicRemove(otherEnd, msgs);
		case AttributePackage.MULTI_URL_VALUES__PROPERTIES_MAP:
			return ((InternalEList<?>) getPropertiesMap()).basicRemove(otherEnd, msgs);
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
		case AttributePackage.MULTI_URL_VALUES__URL_VALUES:
			return getURLValues();
		case AttributePackage.MULTI_URL_VALUES__TOOLTIP:
			return getTooltip();
		case AttributePackage.MULTI_URL_VALUES__PROPERTIES_MAP:
			if (coreType)
				return getPropertiesMap();
			else
				return getPropertiesMap().map();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AttributePackage.MULTI_URL_VALUES__URL_VALUES:
			getURLValues().clear();
			getURLValues().addAll((Collection<? extends URLValue>) newValue);
			return;
		case AttributePackage.MULTI_URL_VALUES__TOOLTIP:
			setTooltip((String) newValue);
			return;
		case AttributePackage.MULTI_URL_VALUES__PROPERTIES_MAP:
			((EStructuralFeature.Setting) getPropertiesMap()).set(newValue);
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
		case AttributePackage.MULTI_URL_VALUES__URL_VALUES:
			getURLValues().clear();
			return;
		case AttributePackage.MULTI_URL_VALUES__TOOLTIP:
			setTooltip(TOOLTIP_EDEFAULT);
			return;
		case AttributePackage.MULTI_URL_VALUES__PROPERTIES_MAP:
			getPropertiesMap().clear();
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
		case AttributePackage.MULTI_URL_VALUES__URL_VALUES:
			return uRLValues != null && !uRLValues.isEmpty();
		case AttributePackage.MULTI_URL_VALUES__TOOLTIP:
			return TOOLTIP_EDEFAULT == null ? tooltip != null : !TOOLTIP_EDEFAULT.equals(tooltip);
		case AttributePackage.MULTI_URL_VALUES__PROPERTIES_MAP:
			return propertiesMap != null && !propertiesMap.isEmpty();
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
		result.append(" (tooltip: "); //$NON-NLS-1$
		result.append(tooltip);
		result.append(')');
		return result.toString();
	}

	/**
	 * Returns a new instance of MultiURLValues.
	 * 
	 * @return
	 */
	public static MultiURLValues create() {
		MultiURLValues muv = AttributeFactory.eINSTANCE.createMultiURLValues();
		muv.getPropertiesMap().putAll(DEFAULT_PROPERTIES_MAP);
		return muv;
	}

	/**
	 * @generated
	 */
	public MultiURLValues copyInstance() {
		MultiURLValuesImpl dest = new MultiURLValuesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(MultiURLValues src) {

		// children

		if (src.getURLValues() != null) {
			EList<URLValue> list = getURLValues();
			for (URLValue element : src.getURLValues()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getPropertiesMap() != null) {
			EMap<String, String> map = getPropertiesMap();
			for (Map.Entry<String, String> entry : src.getPropertiesMap().entrySet()) {

				map.put(entry.getKey(), entry.getValue());

			}
		}

		// attributes

		tooltip = src.getTooltip();

	}

} // MultiURLValuesImpl
