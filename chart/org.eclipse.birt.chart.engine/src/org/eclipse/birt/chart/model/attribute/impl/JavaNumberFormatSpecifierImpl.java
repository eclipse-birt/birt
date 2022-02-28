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

import java.util.Locale;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.util.NumberUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Java
 * Number Format Specifier</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl#getPattern
 * <em>Pattern</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl#getMultiplier
 * <em>Multiplier</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JavaNumberFormatSpecifierImpl extends FormatSpecifierImpl implements JavaNumberFormatSpecifier {

	/**
	 * The default value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getPattern()
	 * @generated
	 * @ordered
	 */
	protected static final String PATTERN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getPattern()
	 * @generated
	 * @ordered
	 */
	protected String pattern = PATTERN_EDEFAULT;

	/**
	 * The default value of the '{@link #getMultiplier() <em>Multiplier</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMultiplier()
	 * @generated
	 * @ordered
	 */
	protected static final double MULTIPLIER_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMultiplier() <em>Multiplier</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMultiplier()
	 * @generated
	 * @ordered
	 */
	protected double multiplier = MULTIPLIER_EDEFAULT;

	/**
	 * This is true if the Multiplier attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean multiplierESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected JavaNumberFormatSpecifierImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.JAVA_NUMBER_FORMAT_SPECIFIER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getPattern() {
		return pattern;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setPattern(String newPattern) {
		String oldPattern = pattern;
		pattern = newPattern;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__PATTERN, oldPattern, pattern));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getMultiplier() {
		return multiplier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setMultiplier(double newMultiplier) {
		double oldMultiplier = multiplier;
		multiplier = newMultiplier;
		boolean oldMultiplierESet = multiplierESet;
		multiplierESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER, oldMultiplier, multiplier,
					!oldMultiplierESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetMultiplier() {
		double oldMultiplier = multiplier;
		boolean oldMultiplierESet = multiplierESet;
		multiplier = MULTIPLIER_EDEFAULT;
		multiplierESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER, oldMultiplier, MULTIPLIER_EDEFAULT,
					oldMultiplierESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetMultiplier() {
		return multiplierESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__PATTERN:
			return getPattern();
		case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER:
			return getMultiplier();
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
		case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__PATTERN:
			setPattern((String) newValue);
			return;
		case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER:
			setMultiplier((Double) newValue);
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
		case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__PATTERN:
			setPattern(PATTERN_EDEFAULT);
			return;
		case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER:
			unsetMultiplier();
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
		case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__PATTERN:
			return PATTERN_EDEFAULT == null ? pattern != null : !PATTERN_EDEFAULT.equals(pattern);
		case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER:
			return isSetMultiplier();
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
		result.append(" (pattern: "); //$NON-NLS-1$
		result.append(pattern);
		result.append(", multiplier: "); //$NON-NLS-1$
		if (multiplierESet) {
			result.append(multiplier);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience methods provided to create an initialized
	 * JavaNumberFormatSpecifier instance
	 *
	 * NOTE: Manually written
	 *
	 * @param sJavaPattern
	 * @return
	 */
	public static JavaNumberFormatSpecifier create(String sJavaPattern) {
		final JavaNumberFormatSpecifier jnfs = AttributeFactory.eINSTANCE.createJavaNumberFormatSpecifier();
		jnfs.setPattern(sJavaPattern);
		// jnfs.setMultiplier(1); // UNDEFINED SUGGESTS A DEFAULT OF '1'
		return jnfs;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#format(
	 * double)
	 */
	@Override
	public String format(double dValue, ULocale lo) {
		final DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(lo);
		df.applyPattern(getPattern());
		return isSetMultiplier() ? df.format(dValue * getMultiplier()) : df.format(dValue);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#format(java.
	 * lang.Number, com.ibm.icu.util.ULocale)
	 */
	@Override
	public String format(Number number, ULocale lo) {
		Number n = NumberUtil.transformNumber(number);
		if (n instanceof Double) {
			return format(((Double) n).doubleValue(), lo);
		}

		// Format big decimal
		BigDecimal bdNum = (BigDecimal) n;
		final DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(lo);
		String vPattern = NumberUtil.adjustBigNumberFormatPattern(getPattern());
		if (vPattern.indexOf('E') < 0) {
			vPattern = vPattern + NumberUtil.BIG_DECIMAL_FORMAT_SUFFIX;
		}
		df.applyPattern(vPattern);
		return isSetMultiplier()
				? df.format(bdNum.multiply(BigDecimal.valueOf(getMultiplier()), NumberUtil.DEFAULT_MATHCONTEXT))
				: df.format(bdNum);
	}

	@Override
	public String format(double dValue, Locale lo) {
		return format(dValue, ULocale.forLocale(lo));
	}

	/**
	 * @generated
	 */
	@Override
	public JavaNumberFormatSpecifier copyInstance() {
		JavaNumberFormatSpecifierImpl dest = new JavaNumberFormatSpecifierImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(JavaNumberFormatSpecifier src) {

		super.set(src);

		// attributes

		pattern = src.getPattern();

		multiplier = src.getMultiplier();

		multiplierESet = src.isSetMultiplier();

	}

} // JavaNumberFormatSpecifierImpl
