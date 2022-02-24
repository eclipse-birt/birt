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

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.birt.chart.model.IChartObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Font
 * Definition</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> FontDefinition holds all information about a certain
 * font used by chart.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getName
 * <em>Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getSize
 * <em>Size</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isBold
 * <em>Bold</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isItalic
 * <em>Italic</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isStrikethrough
 * <em>Strikethrough</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isUnderline
 * <em>Underline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isWordWrap
 * <em>Word Wrap</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getAlignment
 * <em>Alignment</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getRotation
 * <em>Rotation</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFontDefinition()
 * @model extendedMetaData="name='FontDefinition' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface FontDefinition extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the font name. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Holds the font name.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFontDefinition_Name()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> Sets the font name. <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Size</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the font size. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Holds the font size. This can be a floating point number.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Size</em>' attribute.
	 * @see #isSetSize()
	 * @see #unsetSize()
	 * @see #setSize(float)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFontDefinition_Size()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Float" required="true"
	 * @generated
	 */
	float getSize();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getSize
	 * <em>Size</em>}' attribute. <!-- begin-user-doc --> Sets the font size. <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Size</em>' attribute.
	 * @see #isSetSize()
	 * @see #unsetSize()
	 * @see #getSize()
	 * @generated
	 */
	void setSize(float value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getSize
	 * <em>Size</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetSize()
	 * @see #getSize()
	 * @see #setSize(float)
	 * @generated
	 */
	void unsetSize();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.FontDefinition#getSize
	 * <em>Size</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Size</em>' attribute is set.
	 * @see #unsetSize()
	 * @see #getSize()
	 * @see #setSize(float)
	 * @generated
	 */
	boolean isSetSize();

	/**
	 * Returns the value of the '<em><b>Bold</b></em>' attribute. <!--
	 * begin-user-doc --> Gets whether the font is to be in bold. <!-- end-user-doc
	 * --> <!-- begin-model-doc -->
	 * 
	 * This specifies if the font is to be in bold.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Bold</em>' attribute.
	 * @see #isSetBold()
	 * @see #unsetBold()
	 * @see #setBold(boolean)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFontDefinition_Bold()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isBold();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isBold
	 * <em>Bold</em>}' attribute. <!-- begin-user-doc --> Gets whether the font is
	 * to be in bold. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Bold</em>' attribute.
	 * @see #isSetBold()
	 * @see #unsetBold()
	 * @see #isBold()
	 * @generated
	 */
	void setBold(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isBold
	 * <em>Bold</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetBold()
	 * @see #isBold()
	 * @see #setBold(boolean)
	 * @generated
	 */
	void unsetBold();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.FontDefinition#isBold
	 * <em>Bold</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Bold</em>' attribute is set.
	 * @see #unsetBold()
	 * @see #isBold()
	 * @see #setBold(boolean)
	 * @generated
	 */
	boolean isSetBold();

	/**
	 * Returns the value of the '<em><b>Italic</b></em>' attribute. <!--
	 * begin-user-doc --> Gets whether the font is to be rendered in italics. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * This specifies if the font is to be in italics.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Italic</em>' attribute.
	 * @see #isSetItalic()
	 * @see #unsetItalic()
	 * @see #setItalic(boolean)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFontDefinition_Italic()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isItalic();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isItalic
	 * <em>Italic</em>}' attribute. <!-- begin-user-doc --> Sets whether the font is
	 * to be rendered in italics. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Italic</em>' attribute.
	 * @see #isSetItalic()
	 * @see #unsetItalic()
	 * @see #isItalic()
	 * @generated
	 */
	void setItalic(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isItalic
	 * <em>Italic</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetItalic()
	 * @see #isItalic()
	 * @see #setItalic(boolean)
	 * @generated
	 */
	void unsetItalic();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.FontDefinition#isItalic
	 * <em>Italic</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Italic</em>' attribute is set.
	 * @see #unsetItalic()
	 * @see #isItalic()
	 * @see #setItalic(boolean)
	 * @generated
	 */
	boolean isSetItalic();

	/**
	 * Returns the value of the '<em><b>Strikethrough</b></em>' attribute. <!--
	 * begin-user-doc --> Gets whether the font is to be rendered with
	 * strikethrough. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * This specifies if the font is to be in strikethrough.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Strikethrough</em>' attribute.
	 * @see #isSetStrikethrough()
	 * @see #unsetStrikethrough()
	 * @see #setStrikethrough(boolean)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFontDefinition_Strikethrough()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isStrikethrough();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isStrikethrough
	 * <em>Strikethrough</em>}' attribute. <!-- begin-user-doc --> Sets whether the
	 * font is to be rendered with strikethrough. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Strikethrough</em>' attribute.
	 * @see #isSetStrikethrough()
	 * @see #unsetStrikethrough()
	 * @see #isStrikethrough()
	 * @generated
	 */
	void setStrikethrough(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isStrikethrough
	 * <em>Strikethrough</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetStrikethrough()
	 * @see #isStrikethrough()
	 * @see #setStrikethrough(boolean)
	 * @generated
	 */
	void unsetStrikethrough();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isStrikethrough
	 * <em>Strikethrough</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Strikethrough</em>' attribute is set.
	 * @see #unsetStrikethrough()
	 * @see #isStrikethrough()
	 * @see #setStrikethrough(boolean)
	 * @generated
	 */
	boolean isSetStrikethrough();

	/**
	 * Returns the value of the '<em><b>Underline</b></em>' attribute. <!--
	 * begin-user-doc --> Gets whether the font is to be underlined. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * This specifies if the font is to be underlined.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Underline</em>' attribute.
	 * @see #isSetUnderline()
	 * @see #unsetUnderline()
	 * @see #setUnderline(boolean)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFontDefinition_Underline()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isUnderline();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.FontDefinition#isUnderline
	 * <em>Underline</em>}' attribute. <!-- begin-user-doc --> Sets whether the font
	 * is to be underlined. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Underline</em>' attribute.
	 * @see #isSetUnderline()
	 * @see #unsetUnderline()
	 * @see #isUnderline()
	 * @generated
	 */
	void setUnderline(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.FontDefinition#isUnderline
	 * <em>Underline</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetUnderline()
	 * @see #isUnderline()
	 * @see #setUnderline(boolean)
	 * @generated
	 */
	void unsetUnderline();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isUnderline
	 * <em>Underline</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Underline</em>' attribute is set.
	 * @see #unsetUnderline()
	 * @see #isUnderline()
	 * @see #setUnderline(boolean)
	 * @generated
	 */
	boolean isSetUnderline();

	/**
	 * Returns the value of the '<em><b>Word Wrap</b></em>' attribute. <!--
	 * begin-user-doc --> Gets whether the word wrapping is enabled. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Indicates if wordwrapping is to be used.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Word Wrap</em>' attribute.
	 * @see #isSetWordWrap()
	 * @see #unsetWordWrap()
	 * @see #setWordWrap(boolean)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFontDefinition_WordWrap()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isWordWrap();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.FontDefinition#isWordWrap
	 * <em>Word Wrap</em>}' attribute. <!-- begin-user-doc --> Sets whether the word
	 * wrapping is enabled. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Word Wrap</em>' attribute.
	 * @see #isSetWordWrap()
	 * @see #unsetWordWrap()
	 * @see #isWordWrap()
	 * @generated
	 */
	void setWordWrap(boolean value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.FontDefinition#isWordWrap
	 * <em>Word Wrap</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetWordWrap()
	 * @see #isWordWrap()
	 * @see #setWordWrap(boolean)
	 * @generated
	 */
	void unsetWordWrap();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isWordWrap
	 * <em>Word Wrap</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Word Wrap</em>' attribute is set.
	 * @see #unsetWordWrap()
	 * @see #isWordWrap()
	 * @see #setWordWrap(boolean)
	 * @generated
	 */
	boolean isSetWordWrap();

	/**
	 * Returns the value of the '<em><b>Alignment</b></em>' containment reference.
	 * <!-- begin-user-doc --> Gets the text alignment for the font. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the alignment to be used to render the text in the element.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Alignment</em>' containment reference.
	 * @see #setAlignment(TextAlignment)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFontDefinition_Alignment()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	TextAlignment getAlignment();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getAlignment
	 * <em>Alignment</em>}' containment reference. <!-- begin-user-doc --> Sets the
	 * text alignment for the font. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Alignment</em>' containment reference.
	 * @see #getAlignment()
	 * @generated
	 */
	void setAlignment(TextAlignment value);

	/**
	 * Returns the value of the '<em><b>Rotation</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the text rotation angle (in degrees). <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the angle (in degrees) through which the text is to be rotated while
	 * rendering.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Rotation</em>' attribute.
	 * @see #isSetRotation()
	 * @see #unsetRotation()
	 * @see #setRotation(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getFontDefinition_Rotation()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 * @generated
	 */
	double getRotation();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.FontDefinition#getRotation
	 * <em>Rotation</em>}' attribute. <!-- begin-user-doc --> Sets the text rotation
	 * angle (in degrees). <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Rotation</em>' attribute.
	 * @see #isSetRotation()
	 * @see #unsetRotation()
	 * @see #getRotation()
	 * @generated
	 */
	void setRotation(double value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.FontDefinition#getRotation
	 * <em>Rotation</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetRotation()
	 * @see #getRotation()
	 * @see #setRotation(double)
	 * @generated
	 */
	void unsetRotation();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getRotation
	 * <em>Rotation</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Rotation</em>' attribute is set.
	 * @see #unsetRotation()
	 * @see #getRotation()
	 * @see #setRotation(double)
	 * @generated
	 */
	boolean isSetRotation();

	/**
	 * @generated
	 */
	FontDefinition copyInstance();

} // FontDefinition
