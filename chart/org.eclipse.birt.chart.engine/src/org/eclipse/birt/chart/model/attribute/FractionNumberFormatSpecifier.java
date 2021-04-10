/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

import java.util.Locale;

import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Fraction
 * Number Format Specifier</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> FractionNumberFormatSpecifier extends type
 * FormatSpecifier and is specialized for displaying nummeric value as fraction
 * number.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#isPrecise
 * <em>Precise</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getFractionDigits
 * <em>Fraction Digits</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getNumerator
 * <em>Numerator</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getPrefix
 * <em>Prefix</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getSuffix
 * <em>Suffix</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getDelimiter
 * <em>Delimiter</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFractionNumberFormatSpecifier()
 * @model extendedMetaData="name='FractionNumberFormatSpecifier'
 *        kind='elementOnly'"
 * @generated
 */
public interface FractionNumberFormatSpecifier extends FormatSpecifier {

	/**
	 * Returns the value of the '<em><b>Precise</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Precies" specifies if the fraction result is precise with the decimal. <!--
	 * end-model-doc -->
	 * 
	 * @return the value of the '<em>Precise</em>' attribute.
	 * @see #isSetPrecise()
	 * @see #unsetPrecise()
	 * @see #setPrecise(boolean)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFractionNumberFormatSpecifier_Precise()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element' name='Precise'"
	 * @generated
	 */
	boolean isPrecise();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#isPrecise
	 * <em>Precise</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Precise</em>' attribute.
	 * @see #isSetPrecise()
	 * @see #unsetPrecise()
	 * @see #isPrecise()
	 * @generated
	 */
	void setPrecise(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#isPrecise
	 * <em>Precise</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetPrecise()
	 * @see #isPrecise()
	 * @see #setPrecise(boolean)
	 * @generated
	 */
	void unsetPrecise();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#isPrecise
	 * <em>Precise</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Precise</em>' attribute is set.
	 * @see #unsetPrecise()
	 * @see #isPrecise()
	 * @see #setPrecise(boolean)
	 * @generated
	 */
	boolean isSetPrecise();

	/**
	 * Returns the value of the '<em><b>Fraction Digits</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "FractionDigits" specifies the fixed length of the denominator when in
	 * imprecise mode. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Fraction Digits</em>' attribute.
	 * @see #isSetFractionDigits()
	 * @see #unsetFractionDigits()
	 * @see #setFractionDigits(int)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFractionNumberFormatSpecifier_FractionDigits()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        required="true" extendedMetaData="kind='element'
	 *        name='FractionDigits'"
	 * @generated
	 */
	int getFractionDigits();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getFractionDigits
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
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getFractionDigits
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
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getFractionDigits
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
	 * Returns the value of the '<em><b>Numerator</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Numerator" specifies a fixed numerator for the fraction result when in
	 * imprecise mode. Zero means no effect. This setting will take the precedence
	 * than the FractionDigits setting. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Numerator</em>' attribute.
	 * @see #isSetNumerator()
	 * @see #unsetNumerator()
	 * @see #setNumerator(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFractionNumberFormatSpecifier_Numerator()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        required="true" extendedMetaData="kind='element' name='Numerator'"
	 * @generated
	 */
	double getNumerator();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getNumerator
	 * <em>Numerator</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Numerator</em>' attribute.
	 * @see #isSetNumerator()
	 * @see #unsetNumerator()
	 * @see #getNumerator()
	 * @generated
	 */
	void setNumerator(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getNumerator
	 * <em>Numerator</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetNumerator()
	 * @see #getNumerator()
	 * @see #setNumerator(double)
	 * @generated
	 */
	void unsetNumerator();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getNumerator
	 * <em>Numerator</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Numerator</em>' attribute is set.
	 * @see #unsetNumerator()
	 * @see #getNumerator()
	 * @see #setNumerator(double)
	 * @generated
	 */
	boolean isSetNumerator();

	/**
	 * Returns the value of the '<em><b>Prefix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Prefix" specifies the prefix of the result. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Prefix</em>' attribute.
	 * @see #setPrefix(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFractionNumberFormatSpecifier_Prefix()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Prefix'"
	 * @generated
	 */
	String getPrefix();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getPrefix
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
	 * "Suffix" specifies the suffix of the result. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Suffix</em>' attribute.
	 * @see #setSuffix(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFractionNumberFormatSpecifier_Suffix()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Suffix'"
	 * @generated
	 */
	String getSuffix();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getSuffix
	 * <em>Suffix</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Suffix</em>' attribute.
	 * @see #getSuffix()
	 * @generated
	 */
	void setSuffix(String value);

	/**
	 * Returns the value of the '<em><b>Delimiter</b></em>' attribute. The default
	 * value is <code>"/"</code>. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc --> Attribute "Delimiter" specifies the string as the
	 * fraction stroke, by default it'is "/". <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Delimiter</em>' attribute.
	 * @see #isSetDelimiter()
	 * @see #unsetDelimiter()
	 * @see #setDelimiter(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFractionNumberFormatSpecifier_Delimiter()
	 * @model default="/" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Delimiter'"
	 * @generated
	 */
	String getDelimiter();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getDelimiter
	 * <em>Delimiter</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Delimiter</em>' attribute.
	 * @see #isSetDelimiter()
	 * @see #unsetDelimiter()
	 * @see #getDelimiter()
	 * @generated
	 */
	void setDelimiter(String value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getDelimiter
	 * <em>Delimiter</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetDelimiter()
	 * @see #getDelimiter()
	 * @see #setDelimiter(String)
	 * @generated
	 */
	void unsetDelimiter();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getDelimiter
	 * <em>Delimiter</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Delimiter</em>' attribute is set.
	 * @see #unsetDelimiter()
	 * @see #getDelimiter()
	 * @see #setDelimiter(String)
	 * @generated
	 */
	boolean isSetDelimiter();

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
	 * @generated
	 */
	FractionNumberFormatSpecifier copyInstance();

} // FractionNumberFormatSpecifier
