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
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>URL
 * Value</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getBaseUrl
 * <em>Base Url</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getTarget
 * <em>Target</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getBaseParameterName
 * <em>Base Parameter Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getValueParameterName
 * <em>Value Parameter Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getSeriesParameterName
 * <em>Series Parameter Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getTooltip
 * <em>Tooltip</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class URLValueImpl extends ActionValueImpl implements URLValue {

	/**
	 * The default value of the '{@link #getBaseUrl() <em>Base Url</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBaseUrl()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBaseUrl() <em>Base Url</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBaseUrl()
	 * @generated
	 * @ordered
	 */
	protected String baseUrl = BASE_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getTarget() <em>Target</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected static final String TARGET_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected String target = TARGET_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseParameterName() <em>Base Parameter
	 * Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBaseParameterName()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_PARAMETER_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBaseParameterName() <em>Base Parameter
	 * Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBaseParameterName()
	 * @generated
	 * @ordered
	 */
	protected String baseParameterName = BASE_PARAMETER_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getValueParameterName() <em>Value Parameter
	 * Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValueParameterName()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_PARAMETER_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValueParameterName() <em>Value Parameter
	 * Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValueParameterName()
	 * @generated
	 * @ordered
	 */
	protected String valueParameterName = VALUE_PARAMETER_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getSeriesParameterName() <em>Series
	 * Parameter Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getSeriesParameterName()
	 * @generated
	 * @ordered
	 */
	protected static final String SERIES_PARAMETER_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSeriesParameterName() <em>Series
	 * Parameter Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getSeriesParameterName()
	 * @generated
	 * @ordered
	 */
	protected String seriesParameterName = SERIES_PARAMETER_NAME_EDEFAULT;

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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected URLValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.URL_VALUE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setBaseUrl(String newBaseUrl) {
		String oldBaseUrl = baseUrl;
		baseUrl = newBaseUrl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__BASE_URL, oldBaseUrl,
					baseUrl));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTarget(String newTarget) {
		String oldTarget = target;
		target = newTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__TARGET, oldTarget,
					target));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getBaseParameterName() {
		return baseParameterName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setBaseParameterName(String newBaseParameterName) {
		String oldBaseParameterName = baseParameterName;
		baseParameterName = newBaseParameterName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__BASE_PARAMETER_NAME,
					oldBaseParameterName, baseParameterName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getValueParameterName() {
		return valueParameterName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setValueParameterName(String newValueParameterName) {
		String oldValueParameterName = valueParameterName;
		valueParameterName = newValueParameterName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__VALUE_PARAMETER_NAME,
					oldValueParameterName, valueParameterName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getSeriesParameterName() {
		return seriesParameterName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSeriesParameterName(String newSeriesParameterName) {
		String oldSeriesParameterName = seriesParameterName;
		seriesParameterName = newSeriesParameterName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__SERIES_PARAMETER_NAME,
					oldSeriesParameterName, seriesParameterName));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__TOOLTIP, oldTooltip,
					tooltip));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AttributePackage.URL_VALUE__BASE_URL:
			return getBaseUrl();
		case AttributePackage.URL_VALUE__TARGET:
			return getTarget();
		case AttributePackage.URL_VALUE__BASE_PARAMETER_NAME:
			return getBaseParameterName();
		case AttributePackage.URL_VALUE__VALUE_PARAMETER_NAME:
			return getValueParameterName();
		case AttributePackage.URL_VALUE__SERIES_PARAMETER_NAME:
			return getSeriesParameterName();
		case AttributePackage.URL_VALUE__TOOLTIP:
			return getTooltip();
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
		case AttributePackage.URL_VALUE__BASE_URL:
			setBaseUrl((String) newValue);
			return;
		case AttributePackage.URL_VALUE__TARGET:
			setTarget((String) newValue);
			return;
		case AttributePackage.URL_VALUE__BASE_PARAMETER_NAME:
			setBaseParameterName((String) newValue);
			return;
		case AttributePackage.URL_VALUE__VALUE_PARAMETER_NAME:
			setValueParameterName((String) newValue);
			return;
		case AttributePackage.URL_VALUE__SERIES_PARAMETER_NAME:
			setSeriesParameterName((String) newValue);
			return;
		case AttributePackage.URL_VALUE__TOOLTIP:
			setTooltip((String) newValue);
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
		case AttributePackage.URL_VALUE__BASE_URL:
			setBaseUrl(BASE_URL_EDEFAULT);
			return;
		case AttributePackage.URL_VALUE__TARGET:
			setTarget(TARGET_EDEFAULT);
			return;
		case AttributePackage.URL_VALUE__BASE_PARAMETER_NAME:
			setBaseParameterName(BASE_PARAMETER_NAME_EDEFAULT);
			return;
		case AttributePackage.URL_VALUE__VALUE_PARAMETER_NAME:
			setValueParameterName(VALUE_PARAMETER_NAME_EDEFAULT);
			return;
		case AttributePackage.URL_VALUE__SERIES_PARAMETER_NAME:
			setSeriesParameterName(SERIES_PARAMETER_NAME_EDEFAULT);
			return;
		case AttributePackage.URL_VALUE__TOOLTIP:
			setTooltip(TOOLTIP_EDEFAULT);
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
		case AttributePackage.URL_VALUE__BASE_URL:
			return BASE_URL_EDEFAULT == null ? baseUrl != null : !BASE_URL_EDEFAULT.equals(baseUrl);
		case AttributePackage.URL_VALUE__TARGET:
			return TARGET_EDEFAULT == null ? target != null : !TARGET_EDEFAULT.equals(target);
		case AttributePackage.URL_VALUE__BASE_PARAMETER_NAME:
			return BASE_PARAMETER_NAME_EDEFAULT == null ? baseParameterName != null
					: !BASE_PARAMETER_NAME_EDEFAULT.equals(baseParameterName);
		case AttributePackage.URL_VALUE__VALUE_PARAMETER_NAME:
			return VALUE_PARAMETER_NAME_EDEFAULT == null ? valueParameterName != null
					: !VALUE_PARAMETER_NAME_EDEFAULT.equals(valueParameterName);
		case AttributePackage.URL_VALUE__SERIES_PARAMETER_NAME:
			return SERIES_PARAMETER_NAME_EDEFAULT == null ? seriesParameterName != null
					: !SERIES_PARAMETER_NAME_EDEFAULT.equals(seriesParameterName);
		case AttributePackage.URL_VALUE__TOOLTIP:
			return TOOLTIP_EDEFAULT == null ? tooltip != null : !TOOLTIP_EDEFAULT.equals(tooltip);
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
		result.append(" (baseUrl: "); //$NON-NLS-1$
		result.append(baseUrl);
		result.append(", target: "); //$NON-NLS-1$
		result.append(target);
		result.append(", baseParameterName: "); //$NON-NLS-1$
		result.append(baseParameterName);
		result.append(", valueParameterName: "); //$NON-NLS-1$
		result.append(valueParameterName);
		result.append(", seriesParameterName: "); //$NON-NLS-1$
		result.append(seriesParameterName);
		result.append(", tooltip: "); //$NON-NLS-1$
		result.append(tooltip);
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method provided to create a new URLValue instance
	 * 
	 * NOTE: Manually written
	 * 
	 * @param sBaseUrl
	 * @param sTarget
	 * @param sBaseParameterName
	 * @param sValueParameterName
	 * @param sSeriesParameterName
	 * @return
	 */
	public static final URLValue create(String sBaseUrl, String sTarget, String sBaseParameterName,
			String sValueParameterName, String sSeriesParameterName) {
		final URLValue uv = AttributeFactory.eINSTANCE.createURLValue();
		uv.setBaseUrl(sBaseUrl);
		uv.setTarget(sTarget);
		uv.setBaseParameterName(sBaseParameterName);
		uv.setValueParameterName(sValueParameterName);
		uv.setSeriesParameterName(sSeriesParameterName);
		return uv;
	}

	/**
	 * 
	 */
	private static final char cEncode = '%';

	/**
	 * A static/fast lookup table provided for
	 */
	private final static String[] hex = { "00", //$NON-NLS-1$
			"01", //$NON-NLS-1$
			"02", //$NON-NLS-1$
			"03", //$NON-NLS-1$
			"04", //$NON-NLS-1$
			"05", //$NON-NLS-1$
			"06", //$NON-NLS-1$
			"07", //$NON-NLS-1$
			"08", //$NON-NLS-1$
			"09", //$NON-NLS-1$
			"0a", //$NON-NLS-1$
			"0b", //$NON-NLS-1$
			"0c", //$NON-NLS-1$
			"0d", //$NON-NLS-1$
			"0e", //$NON-NLS-1$
			"0f", //$NON-NLS-1$
			"10", //$NON-NLS-1$
			"11", //$NON-NLS-1$
			"12", //$NON-NLS-1$
			"13", //$NON-NLS-1$
			"14", //$NON-NLS-1$
			"15", //$NON-NLS-1$
			"16", //$NON-NLS-1$
			"17", //$NON-NLS-1$
			"18", //$NON-NLS-1$
			"19", //$NON-NLS-1$
			"1a", //$NON-NLS-1$
			"1b", //$NON-NLS-1$
			"1c", //$NON-NLS-1$
			"1d", //$NON-NLS-1$
			"1e", //$NON-NLS-1$
			"1f", //$NON-NLS-1$
			"20", //$NON-NLS-1$
			"21", //$NON-NLS-1$
			"22", //$NON-NLS-1$
			"23", //$NON-NLS-1$
			"24", //$NON-NLS-1$
			"25", //$NON-NLS-1$
			"26", //$NON-NLS-1$
			"27", //$NON-NLS-1$
			"28", //$NON-NLS-1$
			"29", //$NON-NLS-1$
			"2a", //$NON-NLS-1$
			"2b", //$NON-NLS-1$
			"2c", //$NON-NLS-1$
			"2d", //$NON-NLS-1$
			"2e", //$NON-NLS-1$
			"2f", //$NON-NLS-1$
			"30", //$NON-NLS-1$
			"31", //$NON-NLS-1$
			"32", //$NON-NLS-1$
			"33", //$NON-NLS-1$
			"34", //$NON-NLS-1$
			"35", //$NON-NLS-1$
			"36", //$NON-NLS-1$
			"37", //$NON-NLS-1$
			"38", //$NON-NLS-1$
			"39", //$NON-NLS-1$
			"3a", //$NON-NLS-1$
			"3b", //$NON-NLS-1$
			"3c", //$NON-NLS-1$
			"3d", //$NON-NLS-1$
			"3e", //$NON-NLS-1$
			"3f", //$NON-NLS-1$
			"40", //$NON-NLS-1$
			"41", //$NON-NLS-1$
			"42", //$NON-NLS-1$
			"43", //$NON-NLS-1$
			"44", //$NON-NLS-1$
			"45", //$NON-NLS-1$
			"46", //$NON-NLS-1$
			"47", //$NON-NLS-1$
			"48", //$NON-NLS-1$
			"49", //$NON-NLS-1$
			"4a", //$NON-NLS-1$
			"4b", //$NON-NLS-1$
			"4c", //$NON-NLS-1$
			"4d", //$NON-NLS-1$
			"4e", //$NON-NLS-1$
			"4f", //$NON-NLS-1$
			"50", //$NON-NLS-1$
			"51", //$NON-NLS-1$
			"52", //$NON-NLS-1$
			"53", //$NON-NLS-1$
			"54", //$NON-NLS-1$
			"55", //$NON-NLS-1$
			"56", //$NON-NLS-1$
			"57", //$NON-NLS-1$
			"58", //$NON-NLS-1$
			"59", //$NON-NLS-1$
			"5a", //$NON-NLS-1$
			"5b", //$NON-NLS-1$
			"5c", //$NON-NLS-1$
			"5d", //$NON-NLS-1$
			"5e", //$NON-NLS-1$
			"5f", //$NON-NLS-1$
			"60", //$NON-NLS-1$
			"61", //$NON-NLS-1$
			"62", //$NON-NLS-1$
			"63", //$NON-NLS-1$
			"64", //$NON-NLS-1$
			"65", //$NON-NLS-1$
			"66", //$NON-NLS-1$
			"67", //$NON-NLS-1$
			"68", //$NON-NLS-1$
			"69", //$NON-NLS-1$
			"6a", //$NON-NLS-1$
			"6b", //$NON-NLS-1$
			"6c", //$NON-NLS-1$
			"6d", //$NON-NLS-1$
			"6e", //$NON-NLS-1$
			"6f", //$NON-NLS-1$
			"70", //$NON-NLS-1$
			"71", //$NON-NLS-1$
			"72", //$NON-NLS-1$
			"73", //$NON-NLS-1$
			"74", //$NON-NLS-1$
			"75", //$NON-NLS-1$
			"76", //$NON-NLS-1$
			"77", //$NON-NLS-1$
			"78", //$NON-NLS-1$
			"79", //$NON-NLS-1$
			"7a", //$NON-NLS-1$
			"7b", //$NON-NLS-1$
			"7c", //$NON-NLS-1$
			"7d", //$NON-NLS-1$
			"7e", //$NON-NLS-1$
			"7f", //$NON-NLS-1$
			"80", //$NON-NLS-1$
			"81", //$NON-NLS-1$
			"82", //$NON-NLS-1$
			"83", //$NON-NLS-1$
			"84", //$NON-NLS-1$
			"85", //$NON-NLS-1$
			"86", //$NON-NLS-1$
			"87", //$NON-NLS-1$
			"88", //$NON-NLS-1$
			"89", //$NON-NLS-1$
			"8a", //$NON-NLS-1$
			"8b", //$NON-NLS-1$
			"8c", //$NON-NLS-1$
			"8d", //$NON-NLS-1$
			"8e", //$NON-NLS-1$
			"8f", //$NON-NLS-1$
			"90", //$NON-NLS-1$
			"91", //$NON-NLS-1$
			"92", //$NON-NLS-1$
			"93", //$NON-NLS-1$
			"94", //$NON-NLS-1$
			"95", //$NON-NLS-1$
			"96", //$NON-NLS-1$
			"97", //$NON-NLS-1$
			"98", //$NON-NLS-1$
			"99", //$NON-NLS-1$
			"9a", //$NON-NLS-1$
			"9b", //$NON-NLS-1$
			"9c", //$NON-NLS-1$
			"9d", //$NON-NLS-1$
			"9e", //$NON-NLS-1$
			"9f", //$NON-NLS-1$
			"a0", //$NON-NLS-1$
			"a1", //$NON-NLS-1$
			"a2", //$NON-NLS-1$
			"a3", //$NON-NLS-1$
			"a4", //$NON-NLS-1$
			"a5", //$NON-NLS-1$
			"a6", //$NON-NLS-1$
			"a7", //$NON-NLS-1$
			"a8", //$NON-NLS-1$
			"a9", //$NON-NLS-1$
			"aa", //$NON-NLS-1$
			"ab", //$NON-NLS-1$
			"ac", //$NON-NLS-1$
			"ad", //$NON-NLS-1$
			"ae", //$NON-NLS-1$
			"af", //$NON-NLS-1$
			"b0", //$NON-NLS-1$
			"b1", //$NON-NLS-1$
			"b2", //$NON-NLS-1$
			"b3", //$NON-NLS-1$
			"b4", //$NON-NLS-1$
			"b5", //$NON-NLS-1$
			"b6", //$NON-NLS-1$
			"b7", //$NON-NLS-1$
			"b8", //$NON-NLS-1$
			"b9", //$NON-NLS-1$
			"ba", //$NON-NLS-1$
			"bb", //$NON-NLS-1$
			"bc", //$NON-NLS-1$
			"bd", //$NON-NLS-1$
			"be", //$NON-NLS-1$
			"bf", //$NON-NLS-1$
			"c0", //$NON-NLS-1$
			"c1", //$NON-NLS-1$
			"c2", //$NON-NLS-1$
			"c3", //$NON-NLS-1$
			"c4", //$NON-NLS-1$
			"c5", //$NON-NLS-1$
			"c6", //$NON-NLS-1$
			"c7", //$NON-NLS-1$
			"c8", //$NON-NLS-1$
			"c9", //$NON-NLS-1$
			"ca", //$NON-NLS-1$
			"cb", //$NON-NLS-1$
			"cc", //$NON-NLS-1$
			"cd", //$NON-NLS-1$
			"ce", //$NON-NLS-1$
			"cf", //$NON-NLS-1$
			"d0", //$NON-NLS-1$
			"d1", //$NON-NLS-1$
			"d2", //$NON-NLS-1$
			"d3", //$NON-NLS-1$
			"d4", //$NON-NLS-1$
			"d5", //$NON-NLS-1$
			"d6", //$NON-NLS-1$
			"d7", //$NON-NLS-1$
			"d8", //$NON-NLS-1$
			"d9", //$NON-NLS-1$
			"da", //$NON-NLS-1$
			"db", //$NON-NLS-1$
			"dc", //$NON-NLS-1$
			"dd", //$NON-NLS-1$
			"de", //$NON-NLS-1$
			"df", //$NON-NLS-1$
			"e0", //$NON-NLS-1$
			"e1", //$NON-NLS-1$
			"e2", //$NON-NLS-1$
			"e3", //$NON-NLS-1$
			"e4", //$NON-NLS-1$
			"e5", //$NON-NLS-1$
			"e6", //$NON-NLS-1$
			"e7", //$NON-NLS-1$
			"e8", //$NON-NLS-1$
			"e9", //$NON-NLS-1$
			"ea", //$NON-NLS-1$
			"eb", //$NON-NLS-1$
			"ec", //$NON-NLS-1$
			"ed", //$NON-NLS-1$
			"ee", //$NON-NLS-1$
			"ef", //$NON-NLS-1$
			"f0", //$NON-NLS-1$
			"f1", //$NON-NLS-1$
			"f2", //$NON-NLS-1$
			"f3", //$NON-NLS-1$
			"f4", //$NON-NLS-1$
			"f5", //$NON-NLS-1$
			"f6", //$NON-NLS-1$
			"f7", //$NON-NLS-1$
			"f8", //$NON-NLS-1$
			"f9", //$NON-NLS-1$
			"fa", //$NON-NLS-1$
			"fb", //$NON-NLS-1$
			"fc", //$NON-NLS-1$
			"fd", //$NON-NLS-1$
			"fe", //$NON-NLS-1$
			"ff" //$NON-NLS-1$
	};

	/**
	 * Encodes portions of a URL as needed
	 * 
	 * @param sText
	 * @return
	 */
	public static final String encode(String sText) {
		if (sText == null) {
			return null;
		}

		final StringBuffer sb = new StringBuffer();
		final char chrarry[] = sText.toCharArray();
		int ch = 0;

		for (int i = 0; i < chrarry.length; i++) {
			ch = chrarry[i];
			if (('A' <= ch && ch <= 'Z') // 'A'..'Z'
					|| ('a' <= ch && ch <= 'z') // 'a'..'z'
					|| ('0' <= ch && ch <= '9')) // '0'..'9'
			{
				sb.append(chrarry[i]);
			} else if (ch <= 0x007f) // other ASCII
			{
				sb.append(cEncode + hex[ch]);
			} else if (ch <= 0x07FF) // non-ASCII <= 0x7FF
			{
				sb.append(cEncode + hex[0xc0 | (ch >> 6)]);
				sb.append(cEncode + hex[0x80 | (ch & 0x3F)]);
			} else
			// 0x7FF < ch <= 0xFFFF
			{
				sb.append(cEncode + hex[0xe0 | (ch >> 12)]);
				sb.append(cEncode + hex[0x80 | ((ch >> 6) & 0x3F)]);
				sb.append(cEncode + hex[0x80 | (ch & 0x3F)]);
			}
		}
		return sb.toString();
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	public URLValue copyInstance() {
		URLValueImpl dest = new URLValueImpl();
		dest.set(this);
		return dest;
	}

	protected void set(URLValue src) {
		super.set(src);

		baseUrl = src.getBaseUrl();
		target = src.getTarget();
		baseParameterName = src.getBaseParameterName();
		valueParameterName = src.getValueParameterName();
		seriesParameterName = src.getSeriesParameterName();
		tooltip = src.getTooltip();
	}

} // URLValueImpl
