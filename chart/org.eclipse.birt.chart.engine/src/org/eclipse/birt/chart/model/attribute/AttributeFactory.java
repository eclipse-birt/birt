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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory </b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage
 * @generated
 */
public interface AttributeFactory extends EFactory {

	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	AttributeFactory eINSTANCE = org.eclipse.birt.chart.model.attribute.impl.AttributeFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Accessibility Value</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Accessibility Value</em>'.
	 * @generated
	 */
	AccessibilityValue createAccessibilityValue();

	/**
	 * Returns a new object of class '<em>Action Value</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Action Value</em>'.
	 * @generated
	 */
	ActionValue createActionValue();

	/**
	 * Returns a new object of class '<em>Angle3 D</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Angle3 D</em>'.
	 * @generated
	 */
	Angle3D createAngle3D();

	/**
	 * Returns a new object of class '<em>Axis Origin</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Axis Origin</em>'.
	 * @generated
	 */
	AxisOrigin createAxisOrigin();

	/**
	 * Returns a new object of class '<em>Bounds</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Bounds</em>'.
	 * @generated
	 */
	Bounds createBounds();

	/**
	 * Returns a new object of class '<em>Call Back Value</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Call Back Value</em>'.
	 * @generated
	 */
	CallBackValue createCallBackValue();

	/**
	 * Returns a new object of class '<em>Color Definition</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Color Definition</em>'.
	 * @generated
	 */
	ColorDefinition createColorDefinition();

	/**
	 * Returns a new object of class '<em>Cursor</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Cursor</em>'.
	 * @generated
	 */
	Cursor createCursor();

	/**
	 * Returns a new object of class '<em>Data Point</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Data Point</em>'.
	 * @generated
	 */
	DataPoint createDataPoint();

	/**
	 * Returns a new object of class '<em>Data Point Component</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Data Point Component</em>'.
	 * @generated
	 */
	DataPointComponent createDataPointComponent();

	/**
	 * Returns a new object of class '<em>Date Format Specifier</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Date Format Specifier</em>'.
	 * @generated
	 */
	DateFormatSpecifier createDateFormatSpecifier();

	/**
	 * Returns a new object of class '<em>Embedded Image</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Embedded Image</em>'.
	 * @generated
	 */
	EmbeddedImage createEmbeddedImage();

	/**
	 * Returns a new object of class '<em>Extended Property</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Extended Property</em>'.
	 * @generated
	 */
	ExtendedProperty createExtendedProperty();

	/**
	 * Returns a new object of class '<em>Fill</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Fill</em>'.
	 * @generated
	 */
	Fill createFill();

	/**
	 * Returns a new object of class '<em>Font Definition</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Font Definition</em>'.
	 * @generated
	 */
	FontDefinition createFontDefinition();

	/**
	 * Returns a new object of class '<em>Format Specifier</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Format Specifier</em>'.
	 * @generated
	 */
	FormatSpecifier createFormatSpecifier();

	/**
	 * Returns a new object of class '<em>Fraction Number Format Specifier</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Fraction Number Format Specifier</em>'.
	 * @generated
	 */
	FractionNumberFormatSpecifier createFractionNumberFormatSpecifier();

	/**
	 * Returns a new object of class '<em>Gradient</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Gradient</em>'.
	 * @generated
	 */
	Gradient createGradient();

	/**
	 * Returns a new object of class '<em>Image</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Image</em>'.
	 * @generated
	 */
	Image createImage();

	/**
	 * Returns a new object of class '<em>Insets</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Insets</em>'.
	 * @generated
	 */
	Insets createInsets();

	/**
	 * Returns a new object of class '<em>Interactivity</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Interactivity</em>'.
	 * @generated
	 */
	Interactivity createInteractivity();

	/**
	 * Returns a new object of class '<em>Java Date Format Specifier</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Java Date Format Specifier</em>'.
	 * @generated
	 */
	JavaDateFormatSpecifier createJavaDateFormatSpecifier();

	/**
	 * Returns a new object of class '<em>Java Number Format Specifier</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Java Number Format Specifier</em>'.
	 * @generated
	 */
	JavaNumberFormatSpecifier createJavaNumberFormatSpecifier();

	/**
	 * Returns a new object of class '<em>Line Attributes</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Line Attributes</em>'.
	 * @generated
	 */
	LineAttributes createLineAttributes();

	/**
	 * Returns a new object of class '<em>Location</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Location</em>'.
	 * @generated
	 */
	Location createLocation();

	/**
	 * Returns a new object of class '<em>Location3 D</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Location3 D</em>'.
	 * @generated
	 */
	Location3D createLocation3D();

	/**
	 * Returns a new object of class '<em>Marker</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Marker</em>'.
	 * @generated
	 */
	Marker createMarker();

	/**
	 * Returns a new object of class '<em>Multiple Fill</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Multiple Fill</em>'.
	 * @generated
	 */
	MultipleFill createMultipleFill();

	/**
	 * Returns a new object of class '<em>Multi URL Values</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Multi URL Values</em>'.
	 * @generated
	 */
	MultiURLValues createMultiURLValues();

	/**
	 * Returns a new object of class '<em>Number Format Specifier</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Number Format Specifier</em>'.
	 * @generated
	 */
	NumberFormatSpecifier createNumberFormatSpecifier();

	/**
	 * Returns a new object of class '<em>Palette</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Palette</em>'.
	 * @generated
	 */
	Palette createPalette();

	/**
	 * Returns a new object of class '<em>Pattern Image</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Pattern Image</em>'.
	 * @generated
	 */
	PatternImage createPatternImage();

	/**
	 * Returns a new object of class '<em>Rotation3 D</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Rotation3 D</em>'.
	 * @generated
	 */
	Rotation3D createRotation3D();

	/**
	 * Returns a new object of class '<em>Script Value</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Script Value</em>'.
	 * @generated
	 */
	ScriptValue createScriptValue();

	/**
	 * Returns a new object of class '<em>Series Value</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Series Value</em>'.
	 * @generated
	 */
	SeriesValue createSeriesValue();

	/**
	 * Returns a new object of class '<em>Size</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Size</em>'.
	 * @generated
	 */
	Size createSize();

	/**
	 * Returns a new object of class '<em>String Format Specifier</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>String Format Specifier</em>'.
	 * @generated
	 */
	StringFormatSpecifier createStringFormatSpecifier();

	/**
	 * Returns a new object of class '<em>Style</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Style</em>'.
	 * @generated
	 */
	Style createStyle();

	/**
	 * Returns a new object of class '<em>Style Map</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Style Map</em>'.
	 * @generated
	 */
	StyleMap createStyleMap();

	/**
	 * Returns a new object of class '<em>Text</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>Text</em>'.
	 * @generated
	 */
	Text createText();

	/**
	 * Returns a new object of class '<em>Text Alignment</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Text Alignment</em>'.
	 * @generated
	 */
	TextAlignment createTextAlignment();

	/**
	 * Returns a new object of class '<em>Tooltip Value</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Tooltip Value</em>'.
	 * @generated
	 */
	TooltipValue createTooltipValue();

	/**
	 * Returns a new object of class '<em>URL Value</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>URL Value</em>'.
	 * @generated
	 */
	URLValue createURLValue();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	AttributePackage getAttributePackage();

} // AttributeFactory
