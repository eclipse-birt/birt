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

package org.eclipse.birt.chart.model.attribute;

import java.util.Locale;

import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Number
 * Format Specifier</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> NumberFormatSpecifier extends FormatSpecifier
 * specially for formatting numeric values.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getPrefix
 * <em>Prefix</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getSuffix
 * <em>Suffix</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getMultiplier
 * <em>Multiplier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getFractionDigits
 * <em>Fraction Digits</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getNumberFormatSpecifier()
 * @model extendedMetaData="name='NumberFormatSpecifier' kind='elementOnly'"
 * @generated
 */
public interface NumberFormatSpecifier extends FormatSpecifier {

	/**
	 * Returns the value of the '<em><b>Prefix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Prefix" specifies the prefix of the output text.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Prefix</em>' attribute.
	 * @see #setPrefix(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getNumberFormatSpecifier_Prefix()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Prefix'"
	 * @generated
	 */
	String getPrefix();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getPrefix
	 * <em>Prefix</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Prefix</em>' attribute.
	 * @see #getPrefix()
	 * @generated
	 */
	void setPrefix(String value);

	/**
	 * Returns the value of the '<em><b>Suffix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Suffix" specifies the suffix of the output text.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Suffix</em>' attribute.
	 * @see #setSuffix(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getNumberFormatSpecifier_Suffix()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Suffix'"
	 * @generated
	 */
	String getSuffix();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getSuffix
	 * <em>Suffix</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Suffix</em>' attribute.
	 * @see #getSuffix()
	 * @generated
	 */
	void setSuffix(String value);

	/**
	 * Returns the value of the '<em><b>Multiplier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the multiplier.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Multiplier</em>' attribute.
	 * @see #isSetMultiplier()
	 * @see #unsetMultiplier()
	 * @see #setMultiplier(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getNumberFormatSpecifier_Multiplier()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 * @generated
	 */
	double getMultiplier();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getMultiplier
	 * <em>Multiplier</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Multiplier</em>' attribute.
	 * @see #isSetMultiplier()
	 * @see #unsetMultiplier()
	 * @see #getMultiplier()
	 * @generated
	 */
	void setMultiplier(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getMultiplier
	 * <em>Multiplier</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetMultiplier()
	 * @see #getMultiplier()
	 * @see #setMultiplier(double)
	 * @generated
	 */
	void unsetMultiplier();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getMultiplier
	 * <em>Multiplier</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Multiplier</em>' attribute is set.
	 * @see #unsetMultiplier()
	 * @see #getMultiplier()
	 * @see #setMultiplier(double)
	 * @generated
	 */
	boolean isSetMultiplier();

	/**
	 * Returns the value of the '<em><b>Fraction Digits</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the number of fractional digits to be shown.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Fraction Digits</em>' attribute.
	 * @see #isSetFractionDigits()
	 * @see #unsetFractionDigits()
	 * @see #setFractionDigits(int)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getNumberFormatSpecifier_FractionDigits()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getFractionDigits();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getFractionDigits
	 * <em>Fraction Digits</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Fraction Digits</em>' attribute.
	 * @see #isSetFractionDigits()
	 * @see #unsetFractionDigits()
	 * @see #getFractionDigits()
	 * @generated
	 */
	void setFractionDigits(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getFractionDigits
	 * <em>Fraction Digits</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetFractionDigits()
	 * @see #getFractionDigits()
	 * @see #setFractionDigits(int)
	 * @generated
	 */
	void unsetFractionDigits();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getFractionDigits
	 * <em>Fraction Digits</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Fraction Digits</em>' attribute is set.
	 * @see #unsetFractionDigits()
	 * @see #getFractionDigits()
	 * @see #setFractionDigits(int)
	 * @generated
	 */
	boolean isSetFractionDigits();

	/**
	 * Formats a value using the internally defined format specifier rules
	 * 
	 * @param dValue
	 * 
	 * @return A formatted string representation of the numerical value provided
	 * @deprecated
	 */
	String format(double dValue, Locale lo);

	/**
	 * Formats a value using the internally defined format specifier rules
	 * 
	 * @param dValue
	 * 
	 * @return A formatted string representation of the numerical value provided
	 * @since 2.1
	 */
	String format(double dValue, ULocale lo);

	/**
	 * Returns a formatted string representation of specified number.
	 * 
	 * @param number
	 * @param lo
	 * @return
	 * @since 2.6
	 */
	String format(Number number, ULocale lo);

	/**
	 * @generated
	 */
	NumberFormatSpecifier copyInstance();

} // NumberFormatSpecifier
