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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package </b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * Schema file for the chart.model package.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributeFactory
 * @generated
 */
public interface AttributePackage extends EPackage {

	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "attribute"; //$NON-NLS-1$

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.birt.eclipse.org/ChartModelAttribute"; //$NON-NLS-1$

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "attribute"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	AttributePackage eINSTANCE = org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl.init();

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.ActionValueImpl
	 * <em>Action Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.ActionValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getActionValue()
	 * @generated
	 */
	int ACTION_VALUE = 1;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACTION_VALUE__LABEL = 0;

	/**
	 * The number of structural features of the '<em>Action Value</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACTION_VALUE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.AccessibilityValueImpl
	 * <em>Accessibility Value</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.AccessibilityValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAccessibilityValue()
	 * @generated
	 */
	int ACCESSIBILITY_VALUE = 0;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACCESSIBILITY_VALUE__LABEL = ACTION_VALUE__LABEL;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACCESSIBILITY_VALUE__TEXT = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Accessibility</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACCESSIBILITY_VALUE__ACCESSIBILITY = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Accessibility Value</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACCESSIBILITY_VALUE_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl <em>Angle3
	 * D</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAngle3D()
	 * @generated
	 */
	int ANGLE3_D = 2;

	/**
	 * The feature id for the '<em><b>XAngle</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ANGLE3_D__XANGLE = 0;

	/**
	 * The feature id for the '<em><b>YAngle</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ANGLE3_D__YANGLE = 1;

	/**
	 * The feature id for the '<em><b>ZAngle</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ANGLE3_D__ZANGLE = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ANGLE3_D__TYPE = 3;

	/**
	 * The number of structural features of the '<em>Angle3 D</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ANGLE3_D_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.AxisOriginImpl <em>Axis
	 * Origin</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.AxisOriginImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAxisOrigin()
	 * @generated
	 */
	int AXIS_ORIGIN = 3;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS_ORIGIN__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS_ORIGIN__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Axis Origin</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS_ORIGIN_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.BoundsImpl
	 * <em>Bounds</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.BoundsImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getBounds()
	 * @generated
	 */
	int BOUNDS = 4;

	/**
	 * The feature id for the '<em><b>Left</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOUNDS__LEFT = 0;

	/**
	 * The feature id for the '<em><b>Top</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOUNDS__TOP = 1;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOUNDS__WIDTH = 2;

	/**
	 * The feature id for the '<em><b>Height</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOUNDS__HEIGHT = 3;

	/**
	 * The number of structural features of the '<em>Bounds</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOUNDS_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.CallBackValueImpl
	 * <em>Call Back Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.CallBackValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getCallBackValue()
	 * @generated
	 */
	int CALL_BACK_VALUE = 5;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CALL_BACK_VALUE__LABEL = ACTION_VALUE__LABEL;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.FillImpl <em>Fill</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.FillImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFill()
	 * @generated
	 */
	int FILL = 14;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl
	 * <em>Color Definition</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getColorDefinition()
	 * @generated
	 */
	int COLOR_DEFINITION = 6;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.DataPointImpl <em>Data
	 * Point</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.DataPointImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPoint()
	 * @generated
	 */
	int DATA_POINT = 8;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl
	 * <em>Data Point Component</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPointComponent()
	 * @generated
	 */
	int DATA_POINT_COMPONENT = 9;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl
	 * <em>Font Definition</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFontDefinition()
	 * @generated
	 */
	int FONT_DEFINITION = 15;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.FormatSpecifierImpl
	 * <em>Format Specifier</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.FormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFormatSpecifier()
	 * @generated
	 */
	int FORMAT_SPECIFIER = 16;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.DateFormatSpecifierImpl
	 * <em>Date Format Specifier</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.DateFormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatSpecifier()
	 * @generated
	 */
	int DATE_FORMAT_SPECIFIER = 10;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.ExtendedPropertyImpl
	 * <em>Extended Property</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.ExtendedPropertyImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getExtendedProperty()
	 * @generated
	 */
	int EXTENDED_PROPERTY = 13;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.GradientImpl
	 * <em>Gradient</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.GradientImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getGradient()
	 * @generated
	 */
	int GRADIENT = 18;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.ImageImpl
	 * <em>Image</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.ImageImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getImage()
	 * @generated
	 */
	int IMAGE = 19;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.EmbeddedImageImpl
	 * <em>Embedded Image</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.EmbeddedImageImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getEmbeddedImage()
	 * @generated
	 */
	int EMBEDDED_IMAGE = 11;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl
	 * <em>Fraction Number Format Specifier</em>}' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFractionNumberFormatSpecifier()
	 * @generated
	 */
	int FRACTION_NUMBER_FORMAT_SPECIFIER = 17;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.InsetsImpl
	 * <em>Insets</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.InsetsImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getInsets()
	 * @generated
	 */
	int INSETS = 20;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl
	 * <em>Interactivity</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getInteractivity()
	 * @generated
	 */
	int INTERACTIVITY = 21;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl
	 * <em>Java Date Format Specifier</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getJavaDateFormatSpecifier()
	 * @generated
	 */
	int JAVA_DATE_FORMAT_SPECIFIER = 22;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl
	 * <em>Java Number Format Specifier</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getJavaNumberFormatSpecifier()
	 * @generated
	 */
	int JAVA_NUMBER_FORMAT_SPECIFIER = 23;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl
	 * <em>Line Attributes</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineAttributes()
	 * @generated
	 */
	int LINE_ATTRIBUTES = 24;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.LocationImpl
	 * <em>Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.LocationImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLocation()
	 * @generated
	 */
	int LOCATION = 25;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.Location3DImpl
	 * <em>Location3 D</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.Location3DImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLocation3D()
	 * @generated
	 */
	int LOCATION3_D = 26;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl
	 * <em>Marker</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.MarkerImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMarker()
	 * @generated
	 */
	int MARKER = 27;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl
	 * <em>Multiple Fill</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMultipleFill()
	 * @generated
	 */
	int MULTIPLE_FILL = 28;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl
	 * <em>Number Format Specifier</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getNumberFormatSpecifier()
	 * @generated
	 */
	int NUMBER_FORMAT_SPECIFIER = 30;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.PaletteImpl
	 * <em>Palette</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.PaletteImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPalette()
	 * @generated
	 */
	int PALETTE = 31;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl
	 * <em>Rotation3 D</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRotation3D()
	 * @generated
	 */
	int ROTATION3_D = 33;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.ScriptValueImpl
	 * <em>Script Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.ScriptValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getScriptValue()
	 * @generated
	 */
	int SCRIPT_VALUE = 34;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl
	 * <em>Series Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSeriesValue()
	 * @generated
	 */
	int SERIES_VALUE = 35;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.TextImpl <em>Text</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.TextImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getText()
	 * @generated
	 */
	int TEXT = 40;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.SizeImpl <em>Size</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.SizeImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSize()
	 * @generated
	 */
	int SIZE = 36;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.StyleImpl
	 * <em>Style</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.StyleImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyle()
	 * @generated
	 */
	int STYLE = 38;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl <em>Style
	 * Map</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyleMap()
	 * @generated
	 */
	int STYLE_MAP = 39;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl
	 * <em>Text Alignment</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTextAlignment()
	 * @generated
	 */
	int TEXT_ALIGNMENT = 41;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl
	 * <em>Tooltip Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTooltipValue()
	 * @generated
	 */
	int TOOLTIP_VALUE = 42;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl <em>URL
	 * Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.URLValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getURLValue()
	 * @generated
	 */
	int URL_VALUE = 43;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CALL_BACK_VALUE__IDENTIFIER = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Call Back Value</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CALL_BACK_VALUE_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILL__TYPE = 0;

	/**
	 * The number of structural features of the '<em>Fill</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILL_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION__TYPE = FILL__TYPE;

	/**
	 * The feature id for the '<em><b>Transparency</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION__TRANSPARENCY = FILL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Red</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION__RED = FILL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Green</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION__GREEN = FILL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Blue</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION__BLUE = FILL_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Color Definition</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION_FEATURE_COUNT = FILL_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.CursorImpl
	 * <em>Cursor</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.CursorImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getCursor()
	 * @generated
	 */
	int CURSOR = 7;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CURSOR__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Image</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CURSOR__IMAGE = 1;

	/**
	 * The number of structural features of the '<em>Cursor</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CURSOR_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Components</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT__COMPONENTS = 0;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT__PREFIX = 1;

	/**
	 * The feature id for the '<em><b>Suffix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT__SUFFIX = 2;

	/**
	 * The feature id for the '<em><b>Separator</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT__SEPARATOR = 3;

	/**
	 * The number of structural features of the '<em>Data Point</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT_FEATURE_COUNT = 4;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT_COMPONENT__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT_COMPONENT__FORMAT_SPECIFIER = 1;

	/**
	 * The feature id for the '<em><b>Orthogonal Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT_COMPONENT__ORTHOGONAL_TYPE = 2;

	/**
	 * The number of structural features of the '<em>Data Point Component</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT_COMPONENT_FEATURE_COUNT = 3;

	/**
	 * The number of structural features of the '<em>Format Specifier</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FORMAT_SPECIFIER_FEATURE_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATE_FORMAT_SPECIFIER__TYPE = FORMAT_SPECIFIER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Detail</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATE_FORMAT_SPECIFIER__DETAIL = FORMAT_SPECIFIER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Date Format Specifier</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATE_FORMAT_SPECIFIER_FEATURE_COUNT = FORMAT_SPECIFIER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int IMAGE__TYPE = FILL__TYPE;

	/**
	 * The feature id for the '<em><b>URL</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int IMAGE__URL = FILL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int IMAGE__SOURCE = FILL_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Image</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int IMAGE_FEATURE_COUNT = FILL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMBEDDED_IMAGE__TYPE = IMAGE__TYPE;

	/**
	 * The feature id for the '<em><b>URL</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMBEDDED_IMAGE__URL = IMAGE__URL;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMBEDDED_IMAGE__SOURCE = IMAGE__SOURCE;

	/**
	 * The feature id for the '<em><b>Data</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMBEDDED_IMAGE__DATA = IMAGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Embedded Image</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EMBEDDED_IMAGE_FEATURE_COUNT = IMAGE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.EStringToStringMapEntryImpl
	 * <em>EString To String Map Entry</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.EStringToStringMapEntryImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getEStringToStringMapEntry()
	 * @generated
	 */
	int ESTRING_TO_STRING_MAP_ENTRY = 12;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_STRING_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_STRING_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To String Map
	 * Entry</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_STRING_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXTENDED_PROPERTY__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXTENDED_PROPERTY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Extended Property</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXTENDED_PROPERTY_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__SIZE = 1;

	/**
	 * The feature id for the '<em><b>Bold</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__BOLD = 2;

	/**
	 * The feature id for the '<em><b>Italic</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__ITALIC = 3;

	/**
	 * The feature id for the '<em><b>Strikethrough</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__STRIKETHROUGH = 4;

	/**
	 * The feature id for the '<em><b>Underline</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__UNDERLINE = 5;

	/**
	 * The feature id for the '<em><b>Word Wrap</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__WORD_WRAP = 6;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__ALIGNMENT = 7;

	/**
	 * The feature id for the '<em><b>Rotation</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__ROTATION = 8;

	/**
	 * The number of structural features of the '<em>Font Definition</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION_FEATURE_COUNT = 9;

	/**
	 * The feature id for the '<em><b>Precise</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FRACTION_NUMBER_FORMAT_SPECIFIER__PRECISE = FORMAT_SPECIFIER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Fraction Digits</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FRACTION_NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS = FORMAT_SPECIFIER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Numerator</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FRACTION_NUMBER_FORMAT_SPECIFIER__NUMERATOR = FORMAT_SPECIFIER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FRACTION_NUMBER_FORMAT_SPECIFIER__PREFIX = FORMAT_SPECIFIER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Suffix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FRACTION_NUMBER_FORMAT_SPECIFIER__SUFFIX = FORMAT_SPECIFIER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Delimiter</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FRACTION_NUMBER_FORMAT_SPECIFIER__DELIMITER = FORMAT_SPECIFIER_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Fraction Number Format
	 * Specifier</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FRACTION_NUMBER_FORMAT_SPECIFIER_FEATURE_COUNT = FORMAT_SPECIFIER_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRADIENT__TYPE = FILL__TYPE;

	/**
	 * The feature id for the '<em><b>Start Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRADIENT__START_COLOR = FILL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>End Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRADIENT__END_COLOR = FILL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Direction</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRADIENT__DIRECTION = FILL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cyclic</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRADIENT__CYCLIC = FILL_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Transparency</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRADIENT__TRANSPARENCY = FILL_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Gradient</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRADIENT_FEATURE_COUNT = FILL_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Top</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSETS__TOP = 0;

	/**
	 * The feature id for the '<em><b>Left</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSETS__LEFT = 1;

	/**
	 * The feature id for the '<em><b>Bottom</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSETS__BOTTOM = 2;

	/**
	 * The feature id for the '<em><b>Right</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSETS__RIGHT = 3;

	/**
	 * The number of structural features of the '<em>Insets</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSETS_FEATURE_COUNT = 4;

	/**
	 * The feature id for the '<em><b>Enable</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INTERACTIVITY__ENABLE = 0;

	/**
	 * The feature id for the '<em><b>Legend Behavior</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INTERACTIVITY__LEGEND_BEHAVIOR = 1;

	/**
	 * The number of structural features of the '<em>Interactivity</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INTERACTIVITY_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Pattern</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAVA_DATE_FORMAT_SPECIFIER__PATTERN = FORMAT_SPECIFIER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Java Date Format
	 * Specifier</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAVA_DATE_FORMAT_SPECIFIER_FEATURE_COUNT = FORMAT_SPECIFIER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Pattern</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAVA_NUMBER_FORMAT_SPECIFIER__PATTERN = FORMAT_SPECIFIER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Multiplier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER = FORMAT_SPECIFIER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Java Number Format
	 * Specifier</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAVA_NUMBER_FORMAT_SPECIFIER_FEATURE_COUNT = FORMAT_SPECIFIER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Style</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_ATTRIBUTES__STYLE = 0;

	/**
	 * The feature id for the '<em><b>Thickness</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_ATTRIBUTES__THICKNESS = 1;

	/**
	 * The feature id for the '<em><b>Color</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_ATTRIBUTES__COLOR = 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_ATTRIBUTES__VISIBLE = 3;

	/**
	 * The number of structural features of the '<em>Line Attributes</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_ATTRIBUTES_FEATURE_COUNT = 4;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LOCATION__X = 0;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LOCATION__Y = 1;

	/**
	 * The number of structural features of the '<em>Location</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LOCATION_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LOCATION3_D__X = LOCATION__X;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LOCATION3_D__Y = LOCATION__Y;

	/**
	 * The feature id for the '<em><b>Z</b></em>' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LOCATION3_D__Z = LOCATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Location3 D</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LOCATION3_D_FEATURE_COUNT = LOCATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER__SIZE = 1;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER__VISIBLE = 2;

	/**
	 * The feature id for the '<em><b>Fill</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER__FILL = 3;

	/**
	 * The feature id for the '<em><b>Icon Palette</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER__ICON_PALETTE = 4;

	/**
	 * The feature id for the '<em><b>Outline</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER__OUTLINE = 5;

	/**
	 * The number of structural features of the '<em>Marker</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER_FEATURE_COUNT = 6;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_FILL__TYPE = FILL__TYPE;

	/**
	 * The feature id for the '<em><b>Fills</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_FILL__FILLS = FILL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Multiple Fill</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_FILL_FEATURE_COUNT = FILL_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl
	 * <em>Multi URL Values</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMultiURLValues()
	 * @generated
	 */
	int MULTI_URL_VALUES = 29;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MULTI_URL_VALUES__LABEL = ACTION_VALUE__LABEL;

	/**
	 * The feature id for the '<em><b>URL Values</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MULTI_URL_VALUES__URL_VALUES = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Tooltip</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MULTI_URL_VALUES__TOOLTIP = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Properties Map</b></em>' map. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MULTI_URL_VALUES__PROPERTIES_MAP = ACTION_VALUE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Multi URL Values</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MULTI_URL_VALUES_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NUMBER_FORMAT_SPECIFIER__PREFIX = FORMAT_SPECIFIER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Suffix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NUMBER_FORMAT_SPECIFIER__SUFFIX = FORMAT_SPECIFIER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Multiplier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NUMBER_FORMAT_SPECIFIER__MULTIPLIER = FORMAT_SPECIFIER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Fraction Digits</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS = FORMAT_SPECIFIER_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Number Format Specifier</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NUMBER_FORMAT_SPECIFIER_FEATURE_COUNT = FORMAT_SPECIFIER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PALETTE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PALETTE__ENTRIES = 1;

	/**
	 * The number of structural features of the '<em>Palette</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PALETTE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.PatternImageImpl
	 * <em>Pattern Image</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.PatternImageImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPatternImage()
	 * @generated
	 */
	int PATTERN_IMAGE = 32;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATTERN_IMAGE__TYPE = IMAGE__TYPE;

	/**
	 * The feature id for the '<em><b>URL</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATTERN_IMAGE__URL = IMAGE__URL;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATTERN_IMAGE__SOURCE = IMAGE__SOURCE;

	/**
	 * The feature id for the '<em><b>Bitmap</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATTERN_IMAGE__BITMAP = IMAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Fore Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATTERN_IMAGE__FORE_COLOR = IMAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Back Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATTERN_IMAGE__BACK_COLOR = IMAGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Pattern Image</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PATTERN_IMAGE_FEATURE_COUNT = IMAGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Angles</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ROTATION3_D__ANGLES = 0;

	/**
	 * The number of structural features of the '<em>Rotation3 D</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ROTATION3_D_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCRIPT_VALUE__LABEL = ACTION_VALUE__LABEL;

	/**
	 * The feature id for the '<em><b>Script</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCRIPT_VALUE__SCRIPT = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Script Value</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCRIPT_VALUE_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES_VALUE__LABEL = ACTION_VALUE__LABEL;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES_VALUE__NAME = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Series Value</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES_VALUE_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Height</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SIZE__HEIGHT = 0;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SIZE__WIDTH = 1;

	/**
	 * The number of structural features of the '<em>Size</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SIZE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.impl.StringFormatSpecifierImpl
	 * <em>String Format Specifier</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.StringFormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStringFormatSpecifier()
	 * @generated
	 */
	int STRING_FORMAT_SPECIFIER = 37;

	/**
	 * The feature id for the '<em><b>Pattern</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STRING_FORMAT_SPECIFIER__PATTERN = FORMAT_SPECIFIER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>String Format Specifier</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STRING_FORMAT_SPECIFIER_FEATURE_COUNT = FORMAT_SPECIFIER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Font</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STYLE__FONT = 0;

	/**
	 * The feature id for the '<em><b>Color</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STYLE__COLOR = 1;

	/**
	 * The feature id for the '<em><b>Background Color</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STYLE__BACKGROUND_COLOR = 2;

	/**
	 * The feature id for the '<em><b>Background Image</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STYLE__BACKGROUND_IMAGE = 3;

	/**
	 * The feature id for the '<em><b>Padding</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STYLE__PADDING = 4;

	/**
	 * The number of structural features of the '<em>Style</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STYLE_FEATURE_COUNT = 5;

	/**
	 * The feature id for the '<em><b>Component Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STYLE_MAP__COMPONENT_NAME = 0;

	/**
	 * The feature id for the '<em><b>Style</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STYLE_MAP__STYLE = 1;

	/**
	 * The number of structural features of the '<em>Style Map</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STYLE_MAP_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Font</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT__FONT = 1;

	/**
	 * The feature id for the '<em><b>Color</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT__COLOR = 2;

	/**
	 * The number of structural features of the '<em>Text</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Horizontal Alignment</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT = 0;

	/**
	 * The feature id for the '<em><b>Vertical Alignment</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_ALIGNMENT__VERTICAL_ALIGNMENT = 1;

	/**
	 * The number of structural features of the '<em>Text Alignment</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_ALIGNMENT_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TOOLTIP_VALUE__LABEL = ACTION_VALUE__LABEL;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TOOLTIP_VALUE__TEXT = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Delay</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TOOLTIP_VALUE__DELAY = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TOOLTIP_VALUE__FORMAT_SPECIFIER = ACTION_VALUE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Tooltip Value</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TOOLTIP_VALUE_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__LABEL = ACTION_VALUE__LABEL;

	/**
	 * The feature id for the '<em><b>Base Url</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__BASE_URL = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__TARGET = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Base Parameter Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__BASE_PARAMETER_NAME = ACTION_VALUE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Value Parameter Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__VALUE_PARAMETER_NAME = ACTION_VALUE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Series Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__SERIES_PARAMETER_NAME = ACTION_VALUE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Tooltip</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__TOOLTIP = ACTION_VALUE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>URL Value</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int URL_VALUE_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.ActionType <em>Action
	 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.ActionType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getActionType()
	 * @generated
	 */
	int ACTION_TYPE = 44;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.Anchor <em>Anchor</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAnchor()
	 * @generated
	 */
	int ANCHOR = 45;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.AngleType <em>Angle
	 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.AngleType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAngleType()
	 * @generated
	 */
	int ANGLE_TYPE = 46;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.ChartType <em>Chart
	 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.ChartType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartType()
	 * @generated
	 */
	int CHART_TYPE = 49;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.CursorType <em>Cursor
	 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.CursorType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getCursorType()
	 * @generated
	 */
	int CURSOR_TYPE = 50;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * <em>Data Point Component Type</em>}' enum. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPointComponentType()
	 * @generated
	 */
	int DATA_POINT_COMPONENT_TYPE = 51;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.DataType <em>Data Type</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataType()
	 * @generated
	 */
	int DATA_TYPE = 52;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatDetail <em>Date
	 * Format Detail</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatDetail()
	 * @generated
	 */
	int DATE_FORMAT_DETAIL = 53;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatType <em>Date Format
	 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatType()
	 * @generated
	 */
	int DATE_FORMAT_TYPE = 54;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.Direction <em>Direction</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Direction
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDirection()
	 * @generated
	 */
	int DIRECTION = 55;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.GroupingUnitType <em>Grouping
	 * Unit Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getGroupingUnitType()
	 * @generated
	 */
	int GROUPING_UNIT_TYPE = 56;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * <em>Horizontal Alignment</em>}' enum. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getHorizontalAlignment()
	 * @generated
	 */
	int HORIZONTAL_ALIGNMENT = 57;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.ImageSourceType <em>Image
	 * Source Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.ImageSourceType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getImageSourceType()
	 * @generated
	 */
	int IMAGE_SOURCE_TYPE = 58;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.IntersectionType
	 * <em>Intersection Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getIntersectionType()
	 * @generated
	 */
	int INTERSECTION_TYPE = 59;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.LeaderLineStyle <em>Leader
	 * Line Style</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLeaderLineStyle()
	 * @generated
	 */
	int LEADER_LINE_STYLE = 60;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.LegendBehaviorType <em>Legend
	 * Behavior Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.LegendBehaviorType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLegendBehaviorType()
	 * @generated
	 */
	int LEGEND_BEHAVIOR_TYPE = 61;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.LegendItemType <em>Legend Item
	 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLegendItemType()
	 * @generated
	 */
	int LEGEND_ITEM_TYPE = 62;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.LineDecorator <em>Line
	 * Decorator</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineDecorator()
	 * @generated
	 */
	int LINE_DECORATOR = 63;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.LineStyle <em>Line
	 * Style</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.LineStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineStyle()
	 * @generated
	 */
	int LINE_STYLE = 64;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.MarkerType <em>Marker
	 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.MarkerType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMarkerType()
	 * @generated
	 */
	int MARKER_TYPE = 65;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.MenuStylesKeyType <em>Menu
	 * Styles Key Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.MenuStylesKeyType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMenuStylesKeyType()
	 * @generated
	 */
	int MENU_STYLES_KEY_TYPE = 66;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.Orientation
	 * <em>Orientation</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getOrientation()
	 * @generated
	 */
	int ORIENTATION = 67;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.Position <em>Position</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPosition()
	 * @generated
	 */
	int POSITION = 68;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.RiserType <em>Riser
	 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.RiserType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRiserType()
	 * @generated
	 */
	int RISER_TYPE = 69;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.RuleType <em>Rule Type</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.RuleType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRuleType()
	 * @generated
	 */
	int RULE_TYPE = 70;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.ScaleUnitType <em>Scale Unit
	 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getScaleUnitType()
	 * @generated
	 */
	int SCALE_UNIT_TYPE = 71;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.SortOption <em>Sort
	 * Option</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSortOption()
	 * @generated
	 */
	int SORT_OPTION = 72;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.Stretch <em>Stretch</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Stretch
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStretch()
	 * @generated
	 */
	int STRETCH = 73;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.StyledComponent <em>Styled
	 * Component</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyledComponent()
	 * @generated
	 */
	int STYLED_COMPONENT = 74;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.TickStyle <em>Tick
	 * Style</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.TickStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTickStyle()
	 * @generated
	 */
	int TICK_STYLE = 75;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.TriggerCondition <em>Trigger
	 * Condition</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTriggerCondition()
	 * @generated
	 */
	int TRIGGER_CONDITION = 76;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.TriggerFlow <em>Trigger
	 * Flow</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.TriggerFlow
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTriggerFlow()
	 * @generated
	 */
	int TRIGGER_FLOW = 77;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement <em>Units
	 * Of Measurement</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getUnitsOfMeasurement()
	 * @generated
	 */
	int UNITS_OF_MEASUREMENT = 78;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.VerticalAlignment <em>Vertical
	 * Alignment</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getVerticalAlignment()
	 * @generated
	 */
	int VERTICAL_ALIGNMENT = 79;

	/**
	 * The meta object id for the '<em>Action Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.ActionType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getActionTypeObject()
	 * @generated
	 */
	int ACTION_TYPE_OBJECT = 80;

	/**
	 * The meta object id for the '<em>Anchor Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAnchorObject()
	 * @generated
	 */
	int ANCHOR_OBJECT = 81;

	/**
	 * The meta object id for the '<em>Angle Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.AngleType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAngleTypeObject()
	 * @generated
	 */
	int ANGLE_TYPE_OBJECT = 82;

	/**
	 * The meta object id for the '<em>Axis Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAxisTypeObject()
	 * @generated
	 */
	int AXIS_TYPE_OBJECT = 83;

	/**
	 * The meta object id for the '<em>Chart Dimension Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartDimensionObject()
	 * @generated
	 */
	int CHART_DIMENSION_OBJECT = 84;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.AxisType <em>Axis Type</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAxisType()
	 * @generated
	 */
	int AXIS_TYPE = 47;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.chart.model.attribute.ChartDimension <em>Chart
	 * Dimension</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartDimension()
	 * @generated
	 */
	int CHART_DIMENSION = 48;

	/**
	 * The meta object id for the '<em>Chart Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.ChartType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartTypeObject()
	 * @generated
	 */
	int CHART_TYPE_OBJECT = 85;

	/**
	 * The meta object id for the '<em>Cursor Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.CursorType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getCursorTypeObject()
	 * @generated
	 */
	int CURSOR_TYPE_OBJECT = 86;

	/**
	 * The meta object id for the '<em>Data Point Component Type Object</em>' data
	 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPointComponentTypeObject()
	 * @generated
	 */
	int DATA_POINT_COMPONENT_TYPE_OBJECT = 87;

	/**
	 * The meta object id for the '<em>Data Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataTypeObject()
	 * @generated
	 */
	int DATA_TYPE_OBJECT = 88;

	/**
	 * The meta object id for the '<em>Date Format Detail Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatDetailObject()
	 * @generated
	 */
	int DATE_FORMAT_DETAIL_OBJECT = 89;

	/**
	 * The meta object id for the '<em>Date Format Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatTypeObject()
	 * @generated
	 */
	int DATE_FORMAT_TYPE_OBJECT = 90;

	/**
	 * The meta object id for the '<em>Direction Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Direction
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDirectionObject()
	 * @generated
	 */
	int DIRECTION_OBJECT = 91;

	/**
	 * The meta object id for the '<em>Grouping Unit Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getGroupingUnitTypeObject()
	 * @generated
	 */
	int GROUPING_UNIT_TYPE_OBJECT = 92;

	/**
	 * The meta object id for the '<em>Horizontal Alignment Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getHorizontalAlignmentObject()
	 * @generated
	 */
	int HORIZONTAL_ALIGNMENT_OBJECT = 93;

	/**
	 * The meta object id for the '<em>ID</em>' data type. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see java.lang.String
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getID()
	 * @generated
	 */
	int ID = 94;

	/**
	 * The meta object id for the '<em>Image Source Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.ImageSourceType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getImageSourceTypeObject()
	 * @generated
	 */
	int IMAGE_SOURCE_TYPE_OBJECT = 95;

	/**
	 * The meta object id for the '<em>Intersection Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getIntersectionTypeObject()
	 * @generated
	 */
	int INTERSECTION_TYPE_OBJECT = 96;

	/**
	 * The meta object id for the '<em>Leader Line Style Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLeaderLineStyleObject()
	 * @generated
	 */
	int LEADER_LINE_STYLE_OBJECT = 97;

	/**
	 * The meta object id for the '<em>Legend Behavior Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.LegendBehaviorType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLegendBehaviorTypeObject()
	 * @generated
	 */
	int LEGEND_BEHAVIOR_TYPE_OBJECT = 98;

	/**
	 * The meta object id for the '<em>Legend Item Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLegendItemTypeObject()
	 * @generated
	 */
	int LEGEND_ITEM_TYPE_OBJECT = 99;

	/**
	 * The meta object id for the '<em>Line Decorator Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineDecoratorObject()
	 * @generated
	 */
	int LINE_DECORATOR_OBJECT = 100;

	/**
	 * The meta object id for the '<em>Line Style Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.LineStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineStyleObject()
	 * @generated
	 */
	int LINE_STYLE_OBJECT = 101;

	/**
	 * The meta object id for the '<em>Marker Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.MarkerType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMarkerTypeObject()
	 * @generated
	 */
	int MARKER_TYPE_OBJECT = 102;

	/**
	 * The meta object id for the '<em>Menu Styles Key Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.MenuStylesKeyType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMenuStylesKeyTypeObject()
	 * @generated
	 */
	int MENU_STYLES_KEY_TYPE_OBJECT = 103;

	/**
	 * The meta object id for the '<em>Orientation Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getOrientationObject()
	 * @generated
	 */
	int ORIENTATION_OBJECT = 104;

	/**
	 * The meta object id for the '<em>Pattern Bitmap</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPatternBitmap()
	 * @generated
	 */
	int PATTERN_BITMAP = 105;

	/**
	 * The meta object id for the '<em>Pattern Bitmap Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see java.lang.Long
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPatternBitmapObject()
	 * @generated
	 */
	int PATTERN_BITMAP_OBJECT = 106;

	/**
	 * The meta object id for the '<em>Percentage</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPercentage()
	 * @generated
	 */
	int PERCENTAGE = 107;

	/**
	 * The meta object id for the '<em>Percentage Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see java.lang.Double
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPercentageObject()
	 * @generated
	 */
	int PERCENTAGE_OBJECT = 108;

	/**
	 * The meta object id for the '<em>Position Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPositionObject()
	 * @generated
	 */
	int POSITION_OBJECT = 109;

	/**
	 * The meta object id for the '<em>RGB Value</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRGBValue()
	 * @generated
	 */
	int RGB_VALUE = 110;

	/**
	 * The meta object id for the '<em>RGB Value Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see java.lang.Integer
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRGBValueObject()
	 * @generated
	 */
	int RGB_VALUE_OBJECT = 111;

	/**
	 * The meta object id for the '<em>Riser Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.RiserType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRiserTypeObject()
	 * @generated
	 */
	int RISER_TYPE_OBJECT = 112;

	/**
	 * The meta object id for the '<em>Rule Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.RuleType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRuleTypeObject()
	 * @generated
	 */
	int RULE_TYPE_OBJECT = 113;

	/**
	 * The meta object id for the '<em>Scale Unit Type Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getScaleUnitTypeObject()
	 * @generated
	 */
	int SCALE_UNIT_TYPE_OBJECT = 114;

	/**
	 * The meta object id for the '<em>Sort Option Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSortOptionObject()
	 * @generated
	 */
	int SORT_OPTION_OBJECT = 115;

	/**
	 * The meta object id for the '<em>Stretch Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Stretch
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStretchObject()
	 * @generated
	 */
	int STRETCH_OBJECT = 116;

	/**
	 * The meta object id for the '<em>Styled Component Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyledComponentObject()
	 * @generated
	 */
	int STYLED_COMPONENT_OBJECT = 117;

	/**
	 * The meta object id for the '<em>Tick Style Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.TickStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTickStyleObject()
	 * @generated
	 */
	int TICK_STYLE_OBJECT = 118;

	/**
	 * The meta object id for the '<em>Trigger Condition Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTriggerConditionObject()
	 * @generated
	 */
	int TRIGGER_CONDITION_OBJECT = 119;

	/**
	 * The meta object id for the '<em>Trigger Flow Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.TriggerFlow
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTriggerFlowObject()
	 * @generated
	 */
	int TRIGGER_FLOW_OBJECT = 120;

	/**
	 * The meta object id for the '<em>Units Of Measurement Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getUnitsOfMeasurementObject()
	 * @generated
	 */
	int UNITS_OF_MEASUREMENT_OBJECT = 121;

	/**
	 * The meta object id for the '<em>Vertical Alignment Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getVerticalAlignmentObject()
	 * @generated
	 */
	int VERTICAL_ALIGNMENT_OBJECT = 122;

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.AccessibilityValue
	 * <em>Accessibility Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Accessibility Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AccessibilityValue
	 * @generated
	 */
	EClass getAccessibilityValue();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.AccessibilityValue#getText
	 * <em>Text</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AccessibilityValue#getText()
	 * @see #getAccessibilityValue()
	 * @generated
	 */
	EAttribute getAccessibilityValue_Text();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.AccessibilityValue#getAccessibility
	 * <em>Accessibility</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Accessibility</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AccessibilityValue#getAccessibility()
	 * @see #getAccessibilityValue()
	 * @generated
	 */
	EAttribute getAccessibilityValue_Accessibility();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.ActionValue <em>Action
	 * Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Action Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ActionValue
	 * @generated
	 */
	EClass getActionValue();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.ActionValue#getLabel
	 * <em>Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Label</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ActionValue#getLabel()
	 * @see #getActionValue()
	 * @generated
	 */
	EReference getActionValue_Label();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D <em>Angle3 D</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Angle3 D</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D
	 * @generated
	 */
	EClass getAngle3D();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getXAngle
	 * <em>XAngle</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>XAngle</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D#getXAngle()
	 * @see #getAngle3D()
	 * @generated
	 */
	EAttribute getAngle3D_XAngle();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getYAngle
	 * <em>YAngle</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>YAngle</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D#getYAngle()
	 * @see #getAngle3D()
	 * @generated
	 */
	EAttribute getAngle3D_YAngle();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getZAngle
	 * <em>ZAngle</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZAngle</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D#getZAngle()
	 * @see #getAngle3D()
	 * @generated
	 */
	EAttribute getAngle3D_ZAngle();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getType
	 * <em>Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D#getType()
	 * @see #getAngle3D()
	 * @generated
	 */
	EAttribute getAngle3D_Type();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.AxisOrigin <em>Axis
	 * Origin</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Axis Origin</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AxisOrigin
	 * @generated
	 */
	EClass getAxisOrigin();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.AxisOrigin#getType
	 * <em>Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AxisOrigin#getType()
	 * @see #getAxisOrigin()
	 * @generated
	 */
	EAttribute getAxisOrigin_Type();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.AxisOrigin#getValue
	 * <em>Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AxisOrigin#getValue()
	 * @see #getAxisOrigin()
	 * @generated
	 */
	EReference getAxisOrigin_Value();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds <em>Bounds</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Bounds</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds
	 * @generated
	 */
	EClass getBounds();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getLeft
	 * <em>Left</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Left</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#getLeft()
	 * @see #getBounds()
	 * @generated
	 */
	EAttribute getBounds_Left();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getTop <em>Top</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Top</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#getTop()
	 * @see #getBounds()
	 * @generated
	 */
	EAttribute getBounds_Top();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getWidth
	 * <em>Width</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#getWidth()
	 * @see #getBounds()
	 * @generated
	 */
	EAttribute getBounds_Width();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Bounds#getHeight
	 * <em>Height</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Height</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#getHeight()
	 * @see #getBounds()
	 * @generated
	 */
	EAttribute getBounds_Height();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.CallBackValue <em>Call Back
	 * Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Call Back Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.CallBackValue
	 * @generated
	 */
	EClass getCallBackValue();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.CallBackValue#getIdentifier
	 * <em>Identifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.CallBackValue#getIdentifier()
	 * @see #getCallBackValue()
	 * @generated
	 */
	EAttribute getCallBackValue_Identifier();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition <em>Color
	 * Definition</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Color Definition</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition
	 * @generated
	 */
	EClass getColorDefinition();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getTransparency
	 * <em>Transparency</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Transparency</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#getTransparency()
	 * @see #getColorDefinition()
	 * @generated
	 */
	EAttribute getColorDefinition_Transparency();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getRed
	 * <em>Red</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Red</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#getRed()
	 * @see #getColorDefinition()
	 * @generated
	 */
	EAttribute getColorDefinition_Red();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getBlue
	 * <em>Blue</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Blue</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#getBlue()
	 * @see #getColorDefinition()
	 * @generated
	 */
	EAttribute getColorDefinition_Blue();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Cursor <em>Cursor</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Cursor</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Cursor
	 * @generated
	 */
	EClass getCursor();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Cursor#getType
	 * <em>Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Cursor#getType()
	 * @see #getCursor()
	 * @generated
	 */
	EAttribute getCursor_Type();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.attribute.Cursor#getImage
	 * <em>Image</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Image</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Cursor#getImage()
	 * @see #getCursor()
	 * @generated
	 */
	EReference getCursor_Image();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getGreen
	 * <em>Green</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Green</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#getGreen()
	 * @see #getColorDefinition()
	 * @generated
	 */
	EAttribute getColorDefinition_Green();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPoint <em>Data
	 * Point</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Data Point</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint
	 * @generated
	 */
	EClass getDataPoint();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPoint#getComponents
	 * <em>Components</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list
	 *         '<em>Components</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint#getComponents()
	 * @see #getDataPoint()
	 * @generated
	 */
	EReference getDataPoint_Components();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPoint#getPrefix
	 * <em>Prefix</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Prefix</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint#getPrefix()
	 * @see #getDataPoint()
	 * @generated
	 */
	EAttribute getDataPoint_Prefix();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPoint#getSuffix
	 * <em>Suffix</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Suffix</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint#getSuffix()
	 * @see #getDataPoint()
	 * @generated
	 */
	EAttribute getDataPoint_Suffix();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPoint#getSeparator
	 * <em>Separator</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Separator</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint#getSeparator()
	 * @see #getDataPoint()
	 * @generated
	 */
	EAttribute getDataPoint_Separator();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPointComponent <em>Data
	 * Point Component</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Data Point Component</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponent
	 * @generated
	 */
	EClass getDataPointComponent();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getType
	 * <em>Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponent#getType()
	 * @see #getDataPointComponent()
	 * @generated
	 */
	EAttribute getDataPointComponent_Type();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getFormatSpecifier
	 * <em>Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponent#getFormatSpecifier()
	 * @see #getDataPointComponent()
	 * @generated
	 */
	EReference getDataPointComponent_FormatSpecifier();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getOrthogonalType
	 * <em>Orthogonal Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Orthogonal Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponent#getOrthogonalType()
	 * @see #getDataPointComponent()
	 * @generated
	 */
	EAttribute getDataPointComponent_OrthogonalType();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier <em>Date
	 * Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Date Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatSpecifier
	 * @generated
	 */
	EClass getDateFormatSpecifier();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getType
	 * <em>Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getType()
	 * @see #getDateFormatSpecifier()
	 * @generated
	 */
	EAttribute getDateFormatSpecifier_Type();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getDetail
	 * <em>Detail</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Detail</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getDetail()
	 * @see #getDateFormatSpecifier()
	 * @generated
	 */
	EAttribute getDateFormatSpecifier_Detail();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.EmbeddedImage <em>Embedded
	 * Image</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Embedded Image</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.EmbeddedImage
	 * @generated
	 */
	EClass getEmbeddedImage();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.EmbeddedImage#getData
	 * <em>Data</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Data</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.EmbeddedImage#getData()
	 * @see #getEmbeddedImage()
	 * @generated
	 */
	EAttribute getEmbeddedImage_Data();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To
	 * String Map Entry</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>EString To String Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.xml.type.String" keyRequired="true"
	 *        keyExtendedMetaData="kind='element' name='Key'"
	 *        valueDataType="org.eclipse.emf.ecore.xml.type.String"
	 *        valueRequired="true" valueExtendedMetaData="kind='element'
	 *        name='Value'" extendedMetaData="name='EStringToStringMapEntry'
	 *        kind='elementOnly'"
	 * @generated
	 */
	EClass getEStringToStringMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry
	 * <em>Key</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToStringMapEntry()
	 * @generated
	 */
	EAttribute getEStringToStringMapEntry_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry
	 * <em>Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToStringMapEntry()
	 * @generated
	 */
	EAttribute getEStringToStringMapEntry_Value();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty <em>Extended
	 * Property</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Extended Property</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ExtendedProperty
	 * @generated
	 */
	EClass getExtendedProperty();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty#getName
	 * <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ExtendedProperty#getName()
	 * @see #getExtendedProperty()
	 * @generated
	 */
	EAttribute getExtendedProperty_Name();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty#getValue
	 * <em>Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ExtendedProperty#getValue()
	 * @see #getExtendedProperty()
	 * @generated
	 */
	EAttribute getExtendedProperty_Value();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.attribute.Fill <em>Fill</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Fill</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Fill
	 * @generated
	 */
	EClass getFill();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Fill#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Fill#getType()
	 * @see #getFill()
	 * @generated
	 */
	EAttribute getFill_Type();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition <em>Font
	 * Definition</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Font Definition</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition
	 * @generated
	 */
	EClass getFontDefinition();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getName
	 * <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#getName()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Name();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getSize
	 * <em>Size</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Size</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#getSize()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Size();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isBold
	 * <em>Bold</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Bold</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#isBold()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Bold();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isItalic
	 * <em>Italic</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Italic</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#isItalic()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Italic();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isStrikethrough
	 * <em>Strikethrough</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Strikethrough</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#isStrikethrough()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Strikethrough();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isUnderline
	 * <em>Underline</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Underline</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#isUnderline()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Underline();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isWordWrap
	 * <em>Word Wrap</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Word Wrap</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#isWordWrap()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_WordWrap();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getAlignment
	 * <em>Alignment</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#getAlignment()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EReference getFontDefinition_Alignment();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getRotation
	 * <em>Rotation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Rotation</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#getRotation()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Rotation();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.FormatSpecifier <em>Format
	 * Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FormatSpecifier
	 * @generated
	 */
	EClass getFormatSpecifier();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier
	 * <em>Fraction Number Format Specifier</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Fraction Number Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier
	 * @generated
	 */
	EClass getFractionNumberFormatSpecifier();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#isPrecise
	 * <em>Precise</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Precise</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#isPrecise()
	 * @see #getFractionNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getFractionNumberFormatSpecifier_Precise();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getFractionDigits
	 * <em>Fraction Digits</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Fraction Digits</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getFractionDigits()
	 * @see #getFractionNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getFractionNumberFormatSpecifier_FractionDigits();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getNumerator
	 * <em>Numerator</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Numerator</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getNumerator()
	 * @see #getFractionNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getFractionNumberFormatSpecifier_Numerator();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getPrefix
	 * <em>Prefix</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Prefix</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getPrefix()
	 * @see #getFractionNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getFractionNumberFormatSpecifier_Prefix();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getSuffix
	 * <em>Suffix</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Suffix</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getSuffix()
	 * @see #getFractionNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getFractionNumberFormatSpecifier_Suffix();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getDelimiter
	 * <em>Delimiter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Delimiter</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier#getDelimiter()
	 * @see #getFractionNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getFractionNumberFormatSpecifier_Delimiter();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient <em>Gradient</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Gradient</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient
	 * @generated
	 */
	EClass getGradient();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#getStartColor
	 * <em>Start Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Start Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient#getStartColor()
	 * @see #getGradient()
	 * @generated
	 */
	EReference getGradient_StartColor();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#getEndColor <em>End
	 * Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>End Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient#getEndColor()
	 * @see #getGradient()
	 * @generated
	 */
	EReference getGradient_EndColor();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#getDirection
	 * <em>Direction</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Direction</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient#getDirection()
	 * @see #getGradient()
	 * @generated
	 */
	EAttribute getGradient_Direction();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#isCyclic
	 * <em>Cyclic</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Cyclic</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient#isCyclic()
	 * @see #getGradient()
	 * @generated
	 */
	EAttribute getGradient_Cyclic();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#getTransparency
	 * <em>Transparency</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Transparency</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient#getTransparency()
	 * @see #getGradient()
	 * @generated
	 */
	EAttribute getGradient_Transparency();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Image <em>Image</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Image</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Image
	 * @generated
	 */
	EClass getImage();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Image#getURL <em>URL</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>URL</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Image#getURL()
	 * @see #getImage()
	 * @generated
	 */
	EAttribute getImage_URL();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Image#getSource
	 * <em>Source</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Image#getSource()
	 * @see #getImage()
	 * @generated
	 */
	EAttribute getImage_Source();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Insets <em>Insets</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Insets</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Insets
	 * @generated
	 */
	EClass getInsets();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Insets#getTop <em>Top</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Top</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Insets#getTop()
	 * @see #getInsets()
	 * @generated
	 */
	EAttribute getInsets_Top();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Insets#getLeft
	 * <em>Left</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Left</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Insets#getLeft()
	 * @see #getInsets()
	 * @generated
	 */
	EAttribute getInsets_Left();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Insets#getBottom
	 * <em>Bottom</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Bottom</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Insets#getBottom()
	 * @see #getInsets()
	 * @generated
	 */
	EAttribute getInsets_Bottom();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Insets#getRight
	 * <em>Right</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Right</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Insets#getRight()
	 * @see #getInsets()
	 * @generated
	 */
	EAttribute getInsets_Right();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Interactivity
	 * <em>Interactivity</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Interactivity</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Interactivity
	 * @generated
	 */
	EClass getInteractivity();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Interactivity#isEnable
	 * <em>Enable</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Enable</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Interactivity#isEnable()
	 * @see #getInteractivity()
	 * @generated
	 */
	EAttribute getInteractivity_Enable();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Interactivity#getLegendBehavior
	 * <em>Legend Behavior</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Legend Behavior</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Interactivity#getLegendBehavior()
	 * @see #getInteractivity()
	 * @generated
	 */
	EAttribute getInteractivity_LegendBehavior();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier
	 * <em>Java Date Format Specifier</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Java Date Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier
	 * @generated
	 */
	EClass getJavaDateFormatSpecifier();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier#getPattern
	 * <em>Pattern</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Pattern</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier#getPattern()
	 * @see #getJavaDateFormatSpecifier()
	 * @generated
	 */
	EAttribute getJavaDateFormatSpecifier_Pattern();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier
	 * <em>Java Number Format Specifier</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Java Number Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier
	 * @generated
	 */
	EClass getJavaNumberFormatSpecifier();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getPattern
	 * <em>Pattern</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Pattern</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getPattern()
	 * @see #getJavaNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getJavaNumberFormatSpecifier_Pattern();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getMultiplier
	 * <em>Multiplier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Multiplier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getMultiplier()
	 * @see #getJavaNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getJavaNumberFormatSpecifier_Multiplier();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.LineAttributes <em>Line
	 * Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Line Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes
	 * @generated
	 */
	EClass getLineAttributes();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getStyle
	 * <em>Style</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes#getStyle()
	 * @see #getLineAttributes()
	 * @generated
	 */
	EAttribute getLineAttributes_Style();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getThickness
	 * <em>Thickness</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Thickness</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes#getThickness()
	 * @see #getLineAttributes()
	 * @generated
	 */
	EAttribute getLineAttributes_Thickness();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getColor
	 * <em>Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes#getColor()
	 * @see #getLineAttributes()
	 * @generated
	 */
	EReference getLineAttributes_Color();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#isVisible
	 * <em>Visible</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes#isVisible()
	 * @see #getLineAttributes()
	 * @generated
	 */
	EAttribute getLineAttributes_Visible();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Location <em>Location</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Location</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Location
	 * @generated
	 */
	EClass getLocation();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Location#getX <em>X</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Location#getX()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_X();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Location#getY <em>Y</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Location#getY()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_Y();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Location3D <em>Location3
	 * D</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Location3 D</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Location3D
	 * @generated
	 */
	EClass getLocation3D();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Location3D#getZ <em>Z</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Z</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Location3D#getZ()
	 * @see #getLocation3D()
	 * @generated
	 */
	EAttribute getLocation3D_Z();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Marker <em>Marker</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Marker</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker
	 * @generated
	 */
	EClass getMarker();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Marker#getType
	 * <em>Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker#getType()
	 * @see #getMarker()
	 * @generated
	 */
	EAttribute getMarker_Type();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Marker#getSize
	 * <em>Size</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Size</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker#getSize()
	 * @see #getMarker()
	 * @generated
	 */
	EAttribute getMarker_Size();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Marker#isVisible
	 * <em>Visible</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker#isVisible()
	 * @see #getMarker()
	 * @generated
	 */
	EAttribute getMarker_Visible();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Marker#getFill
	 * <em>Fill</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Fill</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker#getFill()
	 * @see #getMarker()
	 * @generated
	 */
	EReference getMarker_Fill();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Marker#getIconPalette <em>Icon
	 * Palette</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Icon
	 *         Palette</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker#getIconPalette()
	 * @see #getMarker()
	 * @generated
	 */
	EReference getMarker_IconPalette();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Marker#getOutline
	 * <em>Outline</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Outline</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker#getOutline()
	 * @see #getMarker()
	 * @generated
	 */
	EReference getMarker_Outline();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.MultipleFill <em>Multiple
	 * Fill</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Multiple Fill</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MultipleFill
	 * @generated
	 */
	EClass getMultipleFill();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.attribute.MultipleFill#getFills
	 * <em>Fills</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Fills</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MultipleFill#getFills()
	 * @see #getMultipleFill()
	 * @generated
	 */
	EReference getMultipleFill_Fills();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.MultiURLValues <em>Multi URL
	 * Values</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Multi URL Values</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MultiURLValues
	 * @generated
	 */
	EClass getMultiURLValues();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.attribute.MultiURLValues#getURLValues
	 * <em>URL Values</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>URL
	 *         Values</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MultiURLValues#getURLValues()
	 * @see #getMultiURLValues()
	 * @generated
	 */
	EReference getMultiURLValues_URLValues();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.MultiURLValues#getTooltip
	 * <em>Tooltip</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Tooltip</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MultiURLValues#getTooltip()
	 * @see #getMultiURLValues()
	 * @generated
	 */
	EAttribute getMultiURLValues_Tooltip();

	/**
	 * Returns the meta object for the map
	 * '{@link org.eclipse.birt.chart.model.attribute.MultiURLValues#getPropertiesMap
	 * <em>Properties Map</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the map '<em>Properties Map</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MultiURLValues#getPropertiesMap()
	 * @see #getMultiURLValues()
	 * @generated
	 */
	EReference getMultiURLValues_PropertiesMap();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier
	 * <em>Number Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Number Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier
	 * @generated
	 */
	EClass getNumberFormatSpecifier();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getPrefix
	 * <em>Prefix</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Prefix</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getPrefix()
	 * @see #getNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getNumberFormatSpecifier_Prefix();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getSuffix
	 * <em>Suffix</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Suffix</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getSuffix()
	 * @see #getNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getNumberFormatSpecifier_Suffix();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getMultiplier
	 * <em>Multiplier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Multiplier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getMultiplier()
	 * @see #getNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getNumberFormatSpecifier_Multiplier();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getFractionDigits
	 * <em>Fraction Digits</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Fraction Digits</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getFractionDigits()
	 * @see #getNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getNumberFormatSpecifier_FractionDigits();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Palette <em>Palette</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Palette</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Palette
	 * @generated
	 */
	EClass getPalette();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Palette#getName
	 * <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Palette#getName()
	 * @see #getPalette()
	 * @generated
	 */
	EAttribute getPalette_Name();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.attribute.Palette#getEntries
	 * <em>Entries</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list
	 *         '<em>Entries</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Palette#getEntries()
	 * @see #getPalette()
	 * @generated
	 */
	EReference getPalette_Entries();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.PatternImage <em>Pattern
	 * Image</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Pattern Image</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.PatternImage
	 * @generated
	 */
	EClass getPatternImage();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.PatternImage#getBitmap
	 * <em>Bitmap</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Bitmap</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.PatternImage#getBitmap()
	 * @see #getPatternImage()
	 * @generated
	 */
	EAttribute getPatternImage_Bitmap();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.PatternImage#getForeColor
	 * <em>Fore Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Fore Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.PatternImage#getForeColor()
	 * @see #getPatternImage()
	 * @generated
	 */
	EReference getPatternImage_ForeColor();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.PatternImage#getBackColor
	 * <em>Back Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Back Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.PatternImage#getBackColor()
	 * @see #getPatternImage()
	 * @generated
	 */
	EReference getPatternImage_BackColor();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Rotation3D <em>Rotation3
	 * D</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Rotation3 D</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Rotation3D
	 * @generated
	 */
	EClass getRotation3D();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.chart.model.attribute.Rotation3D#getAngles
	 * <em>Angles</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Angles</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Rotation3D#getAngles()
	 * @see #getRotation3D()
	 * @generated
	 */
	EReference getRotation3D_Angles();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.ScriptValue <em>Script
	 * Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Script Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ScriptValue
	 * @generated
	 */
	EClass getScriptValue();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.ScriptValue#getScript
	 * <em>Script</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Script</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ScriptValue#getScript()
	 * @see #getScriptValue()
	 * @generated
	 */
	EAttribute getScriptValue_Script();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.SeriesValue <em>Series
	 * Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Series Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.SeriesValue
	 * @generated
	 */
	EClass getSeriesValue();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.SeriesValue#getName
	 * <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.SeriesValue#getName()
	 * @see #getSeriesValue()
	 * @generated
	 */
	EAttribute getSeriesValue_Name();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.attribute.Size <em>Size</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Size</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Size
	 * @generated
	 */
	EClass getSize();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Size#getHeight
	 * <em>Height</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Height</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Size#getHeight()
	 * @see #getSize()
	 * @generated
	 */
	EAttribute getSize_Height();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Size#getWidth
	 * <em>Width</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Size#getWidth()
	 * @see #getSize()
	 * @generated
	 */
	EAttribute getSize_Width();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.StringFormatSpecifier
	 * <em>String Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>String Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StringFormatSpecifier
	 * @generated
	 */
	EClass getStringFormatSpecifier();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.StringFormatSpecifier#getPattern
	 * <em>Pattern</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Pattern</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StringFormatSpecifier#getPattern()
	 * @see #getStringFormatSpecifier()
	 * @generated
	 */
	EAttribute getStringFormatSpecifier_Pattern();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.Style <em>Style</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Style
	 * @generated
	 */
	EClass getStyle();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Style#getFont <em>Font</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Font</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Style#getFont()
	 * @see #getStyle()
	 * @generated
	 */
	EReference getStyle_Font();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Style#getColor
	 * <em>Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Style#getColor()
	 * @see #getStyle()
	 * @generated
	 */
	EReference getStyle_Color();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Style#getBackgroundColor
	 * <em>Background Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Background
	 *         Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Style#getBackgroundColor()
	 * @see #getStyle()
	 * @generated
	 */
	EReference getStyle_BackgroundColor();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Style#getBackgroundImage
	 * <em>Background Image</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Background
	 *         Image</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Style#getBackgroundImage()
	 * @see #getStyle()
	 * @generated
	 */
	EReference getStyle_BackgroundImage();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Style#getPadding
	 * <em>Padding</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Padding</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Style#getPadding()
	 * @see #getStyle()
	 * @generated
	 */
	EReference getStyle_Padding();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.StyleMap <em>Style Map</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Style Map</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StyleMap
	 * @generated
	 */
	EClass getStyleMap();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.StyleMap#getComponentName
	 * <em>Component Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Component Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StyleMap#getComponentName()
	 * @see #getStyleMap()
	 * @generated
	 */
	EAttribute getStyleMap_ComponentName();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.StyleMap#getStyle
	 * <em>Style</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StyleMap#getStyle()
	 * @see #getStyleMap()
	 * @generated
	 */
	EReference getStyleMap_Style();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.attribute.Text <em>Text</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Text</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Text
	 * @generated
	 */
	EClass getText();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.Text#getValue
	 * <em>Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Text#getValue()
	 * @see #getText()
	 * @generated
	 */
	EAttribute getText_Value();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Text#getFont <em>Font</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Font</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Text#getFont()
	 * @see #getText()
	 * @generated
	 */
	EReference getText_Font();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.Text#getColor
	 * <em>Color</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Text#getColor()
	 * @see #getText()
	 * @generated
	 */
	EReference getText_Color();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.TextAlignment <em>Text
	 * Alignment</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Text Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TextAlignment
	 * @generated
	 */
	EClass getTextAlignment();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getHorizontalAlignment
	 * <em>Horizontal Alignment</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Horizontal Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TextAlignment#getHorizontalAlignment()
	 * @see #getTextAlignment()
	 * @generated
	 */
	EAttribute getTextAlignment_HorizontalAlignment();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getVerticalAlignment
	 * <em>Vertical Alignment</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Vertical Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TextAlignment#getVerticalAlignment()
	 * @see #getTextAlignment()
	 * @generated
	 */
	EAttribute getTextAlignment_VerticalAlignment();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.TooltipValue <em>Tooltip
	 * Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Tooltip Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TooltipValue
	 * @generated
	 */
	EClass getTooltipValue();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getText
	 * <em>Text</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TooltipValue#getText()
	 * @see #getTooltipValue()
	 * @generated
	 */
	EAttribute getTooltipValue_Text();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay
	 * <em>Delay</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Delay</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay()
	 * @see #getTooltipValue()
	 * @generated
	 */
	EAttribute getTooltipValue_Delay();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getFormatSpecifier
	 * <em>Format Specifier</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Format
	 *         Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TooltipValue#getFormatSpecifier()
	 * @see #getTooltipValue()
	 * @generated
	 */
	EReference getTooltipValue_FormatSpecifier();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue <em>URL Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>URL Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue
	 * @generated
	 */
	EClass getURLValue();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getBaseUrl <em>Base
	 * Url</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Base Url</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getBaseUrl()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_BaseUrl();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getTarget
	 * <em>Target</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Target</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getTarget()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_Target();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getBaseParameterName
	 * <em>Base Parameter Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Base Parameter Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getBaseParameterName()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_BaseParameterName();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getValueParameterName
	 * <em>Value Parameter Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Value Parameter Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getValueParameterName()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_ValueParameterName();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getSeriesParameterName
	 * <em>Series Parameter Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Series Parameter Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getSeriesParameterName()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_SeriesParameterName();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.chart.model.attribute.URLValue#getTooltip
	 * <em>Tooltip</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Tooltip</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getTooltip()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_Tooltip();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.ActionType <em>Action
	 * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Action Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ActionType
	 * @generated
	 */
	EEnum getActionType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.Anchor <em>Anchor</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Anchor</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @generated
	 */
	EEnum getAnchor();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.AngleType <em>Angle
	 * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Angle Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AngleType
	 * @generated
	 */
	EEnum getAngleType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.ChartType <em>Chart
	 * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Chart Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ChartType
	 * @generated
	 */
	EEnum getChartType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.CursorType <em>Cursor
	 * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Cursor Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.CursorType
	 * @generated
	 */
	EEnum getCursorType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * <em>Data Point Component Type</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Data Point Component Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * @generated
	 */
	EEnum getDataPointComponentType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.DataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Data Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 * @generated
	 */
	EEnum getDataType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatDetail <em>Date
	 * Format Detail</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Date Format Detail</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
	 * @generated
	 */
	EEnum getDateFormatDetail();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatType <em>Date Format
	 * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Date Format Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
	 * @generated
	 */
	EEnum getDateFormatType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.Direction
	 * <em>Direction</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Direction</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Direction
	 * @generated
	 */
	EEnum getDirection();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.GroupingUnitType <em>Grouping
	 * Unit Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Grouping Unit Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 * @generated
	 */
	EEnum getGroupingUnitType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * <em>Horizontal Alignment</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for enum '<em>Horizontal Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * @generated
	 */
	EEnum getHorizontalAlignment();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.ImageSourceType <em>Image
	 * Source Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Image Source Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ImageSourceType
	 * @generated
	 */
	EEnum getImageSourceType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.IntersectionType
	 * <em>Intersection Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Intersection Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
	 * @generated
	 */
	EEnum getIntersectionType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.LeaderLineStyle <em>Leader
	 * Line Style</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Leader Line Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
	 * @generated
	 */
	EEnum getLeaderLineStyle();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.LegendBehaviorType <em>Legend
	 * Behavior Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Legend Behavior Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LegendBehaviorType
	 * @generated
	 */
	EEnum getLegendBehaviorType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.LegendItemType <em>Legend Item
	 * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Legend Item Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
	 * @generated
	 */
	EEnum getLegendItemType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.LineDecorator <em>Line
	 * Decorator</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Line Decorator</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
	 * @generated
	 */
	EEnum getLineDecorator();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.LineStyle <em>Line
	 * Style</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Line Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineStyle
	 * @generated
	 */
	EEnum getLineStyle();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.MarkerType <em>Marker
	 * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Marker Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MarkerType
	 * @generated
	 */
	EEnum getMarkerType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.MenuStylesKeyType <em>Menu
	 * Styles Key Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Menu Styles Key Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MenuStylesKeyType
	 * @generated
	 */
	EEnum getMenuStylesKeyType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.Orientation
	 * <em>Orientation</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Orientation</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @generated
	 */
	EEnum getOrientation();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.Position <em>Position</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Position</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @generated
	 */
	EEnum getPosition();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.RiserType <em>Riser
	 * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Riser Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.RiserType
	 * @generated
	 */
	EEnum getRiserType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.RuleType <em>Rule Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Rule Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.RuleType
	 * @generated
	 */
	EEnum getRuleType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.ScaleUnitType <em>Scale Unit
	 * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Scale Unit Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
	 * @generated
	 */
	EEnum getScaleUnitType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.SortOption <em>Sort
	 * Option</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Sort Option</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @generated
	 */
	EEnum getSortOption();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.Stretch <em>Stretch</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Stretch</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Stretch
	 * @generated
	 */
	EEnum getStretch();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.StyledComponent <em>Styled
	 * Component</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Styled Component</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
	 * @generated
	 */
	EEnum getStyledComponent();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.TickStyle <em>Tick
	 * Style</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Tick Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TickStyle
	 * @generated
	 */
	EEnum getTickStyle();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.TriggerCondition <em>Trigger
	 * Condition</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Trigger Condition</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
	 * @generated
	 */
	EEnum getTriggerCondition();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.TriggerFlow <em>Trigger
	 * Flow</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Trigger Flow</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TriggerFlow
	 * @generated
	 */
	EEnum getTriggerFlow();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement <em>Units
	 * Of Measurement</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Units Of Measurement</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement
	 * @generated
	 */
	EEnum getUnitsOfMeasurement();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.VerticalAlignment <em>Vertical
	 * Alignment</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Vertical Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
	 * @generated
	 */
	EEnum getVerticalAlignment();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.ActionType <em>Action Type
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Action Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ActionType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.ActionType"
	 *        extendedMetaData="name='ActionType:Object' baseType='ActionType'"
	 * @generated
	 */
	EDataType getActionTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.Anchor <em>Anchor
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Anchor Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.Anchor"
	 *        extendedMetaData="name='Anchor:Object' baseType='Anchor'"
	 * @generated
	 */
	EDataType getAnchorObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.AngleType <em>Angle Type
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Angle Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AngleType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.AngleType"
	 *        extendedMetaData="name='AngleType:Object' baseType='AngleType'"
	 * @generated
	 */
	EDataType getAngleTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.AxisType <em>Axis Type
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Axis Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.AxisType"
	 *        extendedMetaData="name='AxisType:Object' baseType='AxisType'"
	 * @generated
	 */
	EDataType getAxisTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.ChartDimension <em>Chart
	 * Dimension Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Chart Dimension Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.ChartDimension"
	 *        extendedMetaData="name='ChartDimension:Object'
	 *        baseType='ChartDimension'"
	 * @generated
	 */
	EDataType getChartDimensionObject();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.AxisType <em>Axis Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Axis Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 * @generated
	 */
	EEnum getAxisType();

	/**
	 * Returns the meta object for enum
	 * '{@link org.eclipse.birt.chart.model.attribute.ChartDimension <em>Chart
	 * Dimension</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Chart Dimension</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
	 * @generated
	 */
	EEnum getChartDimension();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.ChartType <em>Chart Type
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Chart Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ChartType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.ChartType"
	 *        extendedMetaData="name='ChartType:Object' baseType='ChartType'"
	 * @generated
	 */
	EDataType getChartTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.CursorType <em>Cursor Type
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Cursor Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.CursorType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.CursorType"
	 *        extendedMetaData="name='CursorType:Object' baseType='CursorType'"
	 * @generated
	 */
	EDataType getCursorTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * <em>Data Point Component Type Object</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Data Point Component Type
	 *         Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.DataPointComponentType"
	 *        extendedMetaData="name='DataPointComponentType:Object'
	 *        baseType='DataPointComponentType'"
	 * @generated
	 */
	EDataType getDataPointComponentTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.DataType <em>Data Type
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Data Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.DataType"
	 *        extendedMetaData="name='DataType:Object' baseType='DataType'"
	 * @generated
	 */
	EDataType getDataTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatDetail <em>Date
	 * Format Detail Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Date Format Detail Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.DateFormatDetail"
	 *        extendedMetaData="name='DateFormatDetail:Object'
	 *        baseType='DateFormatDetail'"
	 * @generated
	 */
	EDataType getDateFormatDetailObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatType <em>Date Format
	 * Type Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Date Format Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.DateFormatType"
	 *        extendedMetaData="name='DateFormatType:Object'
	 *        baseType='DateFormatType'"
	 * @generated
	 */
	EDataType getDateFormatTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.Direction <em>Direction
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Direction Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Direction
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.Direction"
	 *        extendedMetaData="name='Direction:Object' baseType='Direction'"
	 * @generated
	 */
	EDataType getDirectionObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.GroupingUnitType <em>Grouping
	 * Unit Type Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Grouping Unit Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.GroupingUnitType"
	 *        extendedMetaData="name='GroupingUnitType:Object'
	 *        baseType='GroupingUnitType'"
	 * @generated
	 */
	EDataType getGroupingUnitTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * <em>Horizontal Alignment Object</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Horizontal Alignment Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.HorizontalAlignment"
	 *        extendedMetaData="name='HorizontalAlignment:Object'
	 *        baseType='HorizontalAlignment'"
	 * @generated
	 */
	EDataType getHorizontalAlignmentObject();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>ID</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>ID</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String" extendedMetaData="name='ID'
	 *        baseType='http://www.eclipse.org/emf/2003/XMLType#string'
	 *        pattern='[A-Z]'"
	 * @generated
	 */
	EDataType getID();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.ImageSourceType <em>Image
	 * Source Type Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Image Source Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ImageSourceType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.ImageSourceType"
	 *        extendedMetaData="name='ImageSourceType:Object'
	 *        baseType='ImageSourceType'"
	 * @generated
	 */
	EDataType getImageSourceTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.IntersectionType
	 * <em>Intersection Type Object</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Intersection Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.IntersectionType"
	 *        extendedMetaData="name='IntersectionType:Object'
	 *        baseType='IntersectionType'"
	 * @generated
	 */
	EDataType getIntersectionTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.LeaderLineStyle <em>Leader
	 * Line Style Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Leader Line Style Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.LeaderLineStyle"
	 *        extendedMetaData="name='LeaderLineStyle:Object'
	 *        baseType='LeaderLineStyle'"
	 * @generated
	 */
	EDataType getLeaderLineStyleObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.LegendBehaviorType <em>Legend
	 * Behavior Type Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Legend Behavior Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LegendBehaviorType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.LegendBehaviorType"
	 *        extendedMetaData="name='LegendBehaviorType:Object'
	 *        baseType='LegendBehaviorType'"
	 * @generated
	 */
	EDataType getLegendBehaviorTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.LegendItemType <em>Legend Item
	 * Type Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Legend Item Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.LegendItemType"
	 *        extendedMetaData="name='LegendItemType:Object'
	 *        baseType='LegendItemType'"
	 * @generated
	 */
	EDataType getLegendItemTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.LineDecorator <em>Line
	 * Decorator Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Line Decorator Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.LineDecorator"
	 *        extendedMetaData="name='LineDecorator:Object'
	 *        baseType='LineDecorator'"
	 * @generated
	 */
	EDataType getLineDecoratorObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.LineStyle <em>Line Style
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Line Style Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineStyle
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.LineStyle"
	 *        extendedMetaData="name='LineStyle:Object' baseType='LineStyle'"
	 * @generated
	 */
	EDataType getLineStyleObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.MarkerType <em>Marker Type
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Marker Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MarkerType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.MarkerType"
	 *        extendedMetaData="name='MarkerType:Object' baseType='MarkerType'"
	 * @generated
	 */
	EDataType getMarkerTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.MenuStylesKeyType <em>Menu
	 * Styles Key Type Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Menu Styles Key Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MenuStylesKeyType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.MenuStylesKeyType"
	 *        extendedMetaData="name='MenuStylesKeyType:Object'
	 *        baseType='MenuStylesKeyType'"
	 * @generated
	 */
	EDataType getMenuStylesKeyTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.Orientation <em>Orientation
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Orientation Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.Orientation"
	 *        extendedMetaData="name='Orientation:Object' baseType='Orientation'"
	 * @generated
	 */
	EDataType getOrientationObject();

	/**
	 * Returns the meta object for data type '<em>Pattern Bitmap</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Pattern Bitmap</em>'.
	 * @model instanceClass="long" extendedMetaData="name='PatternBitmap'
	 *        baseType='http://www.eclipse.org/emf/2003/XMLType#long'"
	 * @generated
	 */
	EDataType getPatternBitmap();

	/**
	 * Returns the meta object for data type '{@link java.lang.Long <em>Pattern
	 * Bitmap Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Pattern Bitmap Object</em>'.
	 * @see java.lang.Long
	 * @model instanceClass="java.lang.Long"
	 *        extendedMetaData="name='PatternBitmap:Object'
	 *        baseType='PatternBitmap'"
	 * @generated
	 */
	EDataType getPatternBitmapObject();

	/**
	 * Returns the meta object for data type '<em>Percentage</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Percentage</em>'.
	 * @model instanceClass="double"
	 * @generated
	 */
	EDataType getPercentage();

	/**
	 * Returns the meta object for data type ' {@link java.lang.Double
	 * <em>Percentage Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Percentage Object</em>'.
	 * @see java.lang.Double
	 * @model instanceClass="java.lang.Double"
	 * @generated
	 */
	EDataType getPercentageObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.Position <em>Position
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Position Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.Position"
	 *        extendedMetaData="name='Position:Object' baseType='Position'"
	 * @generated
	 */
	EDataType getPositionObject();

	/**
	 * Returns the meta object for data type '<em>RGB Value</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>RGB Value</em>'.
	 * @model instanceClass="int"
	 * @generated
	 */
	EDataType getRGBValue();

	/**
	 * Returns the meta object for data type ' {@link java.lang.Integer <em>RGB
	 * Value Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>RGB Value Object</em>'.
	 * @see java.lang.Integer
	 * @model instanceClass="java.lang.Integer"
	 * @generated
	 */
	EDataType getRGBValueObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.RiserType <em>Riser Type
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Riser Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.RiserType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.RiserType"
	 *        extendedMetaData="name='RiserType:Object' baseType='RiserType'"
	 * @generated
	 */
	EDataType getRiserTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.RuleType <em>Rule Type
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Rule Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.RuleType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.RuleType"
	 *        extendedMetaData="name='RuleType:Object' baseType='RuleType'"
	 * @generated
	 */
	EDataType getRuleTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.ScaleUnitType <em>Scale Unit
	 * Type Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Scale Unit Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.ScaleUnitType"
	 *        extendedMetaData="name='ScaleUnitType:Object'
	 *        baseType='ScaleUnitType'"
	 * @generated
	 */
	EDataType getScaleUnitTypeObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.SortOption <em>Sort Option
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Sort Option Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.SortOption"
	 *        extendedMetaData="name='SortOption:Object' baseType='SortOption'"
	 * @generated
	 */
	EDataType getSortOptionObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.Stretch <em>Stretch
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Stretch Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Stretch
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.Stretch"
	 *        extendedMetaData="name='Stretch:Object' baseType='Stretch'"
	 * @generated
	 */
	EDataType getStretchObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.StyledComponent <em>Styled
	 * Component Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Styled Component Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.StyledComponent"
	 *        extendedMetaData="name='StyledComponent:Object'
	 *        baseType='StyledComponent'"
	 * @generated
	 */
	EDataType getStyledComponentObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.TickStyle <em>Tick Style
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Tick Style Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TickStyle
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.TickStyle"
	 *        extendedMetaData="name='TickStyle:Object' baseType='TickStyle'"
	 * @generated
	 */
	EDataType getTickStyleObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.TriggerCondition <em>Trigger
	 * Condition Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Trigger Condition Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.TriggerCondition"
	 *        extendedMetaData="name='TriggerCondition:Object'
	 *        baseType='TriggerCondition'"
	 * @generated
	 */
	EDataType getTriggerConditionObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.TriggerFlow <em>Trigger Flow
	 * Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Trigger Flow Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TriggerFlow
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.TriggerFlow"
	 *        extendedMetaData="name='TriggerFlow:Object' baseType='TriggerFlow'"
	 * @generated
	 */
	EDataType getTriggerFlowObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement <em>Units
	 * Of Measurement Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Units Of Measurement Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement"
	 *        extendedMetaData="name='UnitsOfMeasurement:Object'
	 *        baseType='UnitsOfMeasurement'"
	 * @generated
	 */
	EDataType getUnitsOfMeasurementObject();

	/**
	 * Returns the meta object for data type
	 * '{@link org.eclipse.birt.chart.model.attribute.VerticalAlignment <em>Vertical
	 * Alignment Object</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Vertical Alignment Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.VerticalAlignment"
	 *        extendedMetaData="name='VerticalAlignment:Object'
	 *        baseType='VerticalAlignment'"
	 * @generated
	 */
	EDataType getVerticalAlignmentObject();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AttributeFactory getAttributeFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.AccessibilityValueImpl
		 * <em>Accessibility Value</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.AccessibilityValueImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAccessibilityValue()
		 * @generated
		 */
		EClass ACCESSIBILITY_VALUE = eINSTANCE.getAccessibilityValue();

		/**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ACCESSIBILITY_VALUE__TEXT = eINSTANCE.getAccessibilityValue_Text();

		/**
		 * The meta object literal for the '<em><b>Accessibility</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ACCESSIBILITY_VALUE__ACCESSIBILITY = eINSTANCE.getAccessibilityValue_Accessibility();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.ActionValueImpl
		 * <em>Action Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.ActionValueImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getActionValue()
		 * @generated
		 */
		EClass ACTION_VALUE = eINSTANCE.getActionValue();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ACTION_VALUE__LABEL = eINSTANCE.getActionValue_Label();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl <em>Angle3
		 * D</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAngle3D()
		 * @generated
		 */
		EClass ANGLE3_D = eINSTANCE.getAngle3D();

		/**
		 * The meta object literal for the '<em><b>XAngle</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ANGLE3_D__XANGLE = eINSTANCE.getAngle3D_XAngle();

		/**
		 * The meta object literal for the '<em><b>YAngle</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ANGLE3_D__YANGLE = eINSTANCE.getAngle3D_YAngle();

		/**
		 * The meta object literal for the '<em><b>ZAngle</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ANGLE3_D__ZANGLE = eINSTANCE.getAngle3D_ZAngle();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ANGLE3_D__TYPE = eINSTANCE.getAngle3D_Type();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.AxisOriginImpl <em>Axis
		 * Origin</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.AxisOriginImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAxisOrigin()
		 * @generated
		 */
		EClass AXIS_ORIGIN = eINSTANCE.getAxisOrigin();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute AXIS_ORIGIN__TYPE = eINSTANCE.getAxisOrigin_Type();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference AXIS_ORIGIN__VALUE = eINSTANCE.getAxisOrigin_Value();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.BoundsImpl
		 * <em>Bounds</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.BoundsImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getBounds()
		 * @generated
		 */
		EClass BOUNDS = eINSTANCE.getBounds();

		/**
		 * The meta object literal for the '<em><b>Left</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BOUNDS__LEFT = eINSTANCE.getBounds_Left();

		/**
		 * The meta object literal for the '<em><b>Top</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BOUNDS__TOP = eINSTANCE.getBounds_Top();

		/**
		 * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BOUNDS__WIDTH = eINSTANCE.getBounds_Width();

		/**
		 * The meta object literal for the '<em><b>Height</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BOUNDS__HEIGHT = eINSTANCE.getBounds_Height();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.CallBackValueImpl
		 * <em>Call Back Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.CallBackValueImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getCallBackValue()
		 * @generated
		 */
		EClass CALL_BACK_VALUE = eINSTANCE.getCallBackValue();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CALL_BACK_VALUE__IDENTIFIER = eINSTANCE.getCallBackValue_Identifier();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl
		 * <em>Color Definition</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getColorDefinition()
		 * @generated
		 */
		EClass COLOR_DEFINITION = eINSTANCE.getColorDefinition();

		/**
		 * The meta object literal for the '<em><b>Transparency</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute COLOR_DEFINITION__TRANSPARENCY = eINSTANCE.getColorDefinition_Transparency();

		/**
		 * The meta object literal for the '<em><b>Red</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute COLOR_DEFINITION__RED = eINSTANCE.getColorDefinition_Red();

		/**
		 * The meta object literal for the '<em><b>Green</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute COLOR_DEFINITION__GREEN = eINSTANCE.getColorDefinition_Green();

		/**
		 * The meta object literal for the '<em><b>Blue</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute COLOR_DEFINITION__BLUE = eINSTANCE.getColorDefinition_Blue();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.CursorImpl
		 * <em>Cursor</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.CursorImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getCursor()
		 * @generated
		 */
		EClass CURSOR = eINSTANCE.getCursor();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CURSOR__TYPE = eINSTANCE.getCursor_Type();

		/**
		 * The meta object literal for the '<em><b>Image</b></em>' containment reference
		 * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CURSOR__IMAGE = eINSTANCE.getCursor_Image();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.DataPointImpl <em>Data
		 * Point</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.DataPointImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPoint()
		 * @generated
		 */
		EClass DATA_POINT = eINSTANCE.getDataPoint();

		/**
		 * The meta object literal for the '<em><b>Components</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DATA_POINT__COMPONENTS = eINSTANCE.getDataPoint_Components();

		/**
		 * The meta object literal for the '<em><b>Prefix</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DATA_POINT__PREFIX = eINSTANCE.getDataPoint_Prefix();

		/**
		 * The meta object literal for the '<em><b>Suffix</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DATA_POINT__SUFFIX = eINSTANCE.getDataPoint_Suffix();

		/**
		 * The meta object literal for the '<em><b>Separator</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DATA_POINT__SEPARATOR = eINSTANCE.getDataPoint_Separator();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl
		 * <em>Data Point Component</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPointComponent()
		 * @generated
		 */
		EClass DATA_POINT_COMPONENT = eINSTANCE.getDataPointComponent();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DATA_POINT_COMPONENT__TYPE = eINSTANCE.getDataPointComponent_Type();

		/**
		 * The meta object literal for the '<em><b>Format Specifier</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DATA_POINT_COMPONENT__FORMAT_SPECIFIER = eINSTANCE.getDataPointComponent_FormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Orthogonal Type</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DATA_POINT_COMPONENT__ORTHOGONAL_TYPE = eINSTANCE.getDataPointComponent_OrthogonalType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.DateFormatSpecifierImpl
		 * <em>Date Format Specifier</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.DateFormatSpecifierImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatSpecifier()
		 * @generated
		 */
		EClass DATE_FORMAT_SPECIFIER = eINSTANCE.getDateFormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DATE_FORMAT_SPECIFIER__TYPE = eINSTANCE.getDateFormatSpecifier_Type();

		/**
		 * The meta object literal for the '<em><b>Detail</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DATE_FORMAT_SPECIFIER__DETAIL = eINSTANCE.getDateFormatSpecifier_Detail();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.EmbeddedImageImpl
		 * <em>Embedded Image</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.EmbeddedImageImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getEmbeddedImage()
		 * @generated
		 */
		EClass EMBEDDED_IMAGE = eINSTANCE.getEmbeddedImage();

		/**
		 * The meta object literal for the '<em><b>Data</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute EMBEDDED_IMAGE__DATA = eINSTANCE.getEmbeddedImage_Data();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.EStringToStringMapEntryImpl
		 * <em>EString To String Map Entry</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.EStringToStringMapEntryImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getEStringToStringMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_STRING_MAP_ENTRY = eINSTANCE.getEStringToStringMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ESTRING_TO_STRING_MAP_ENTRY__KEY = eINSTANCE.getEStringToStringMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ESTRING_TO_STRING_MAP_ENTRY__VALUE = eINSTANCE.getEStringToStringMapEntry_Value();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.ExtendedPropertyImpl
		 * <em>Extended Property</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.ExtendedPropertyImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getExtendedProperty()
		 * @generated
		 */
		EClass EXTENDED_PROPERTY = eINSTANCE.getExtendedProperty();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute EXTENDED_PROPERTY__NAME = eINSTANCE.getExtendedProperty_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute EXTENDED_PROPERTY__VALUE = eINSTANCE.getExtendedProperty_Value();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.FillImpl <em>Fill</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.FillImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFill()
		 * @generated
		 */
		EClass FILL = eINSTANCE.getFill();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FILL__TYPE = eINSTANCE.getFill_Type();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl
		 * <em>Font Definition</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFontDefinition()
		 * @generated
		 */
		EClass FONT_DEFINITION = eINSTANCE.getFontDefinition();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FONT_DEFINITION__NAME = eINSTANCE.getFontDefinition_Name();

		/**
		 * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FONT_DEFINITION__SIZE = eINSTANCE.getFontDefinition_Size();

		/**
		 * The meta object literal for the '<em><b>Bold</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FONT_DEFINITION__BOLD = eINSTANCE.getFontDefinition_Bold();

		/**
		 * The meta object literal for the '<em><b>Italic</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FONT_DEFINITION__ITALIC = eINSTANCE.getFontDefinition_Italic();

		/**
		 * The meta object literal for the '<em><b>Strikethrough</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FONT_DEFINITION__STRIKETHROUGH = eINSTANCE.getFontDefinition_Strikethrough();

		/**
		 * The meta object literal for the '<em><b>Underline</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FONT_DEFINITION__UNDERLINE = eINSTANCE.getFontDefinition_Underline();

		/**
		 * The meta object literal for the '<em><b>Word Wrap</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FONT_DEFINITION__WORD_WRAP = eINSTANCE.getFontDefinition_WordWrap();

		/**
		 * The meta object literal for the '<em><b>Alignment</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FONT_DEFINITION__ALIGNMENT = eINSTANCE.getFontDefinition_Alignment();

		/**
		 * The meta object literal for the '<em><b>Rotation</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FONT_DEFINITION__ROTATION = eINSTANCE.getFontDefinition_Rotation();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.FormatSpecifierImpl
		 * <em>Format Specifier</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.FormatSpecifierImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFormatSpecifier()
		 * @generated
		 */
		EClass FORMAT_SPECIFIER = eINSTANCE.getFormatSpecifier();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl
		 * <em>Fraction Number Format Specifier</em>}' class. <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFractionNumberFormatSpecifier()
		 * @generated
		 */
		EClass FRACTION_NUMBER_FORMAT_SPECIFIER = eINSTANCE.getFractionNumberFormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Precise</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FRACTION_NUMBER_FORMAT_SPECIFIER__PRECISE = eINSTANCE.getFractionNumberFormatSpecifier_Precise();

		/**
		 * The meta object literal for the '<em><b>Fraction Digits</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FRACTION_NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS = eINSTANCE
				.getFractionNumberFormatSpecifier_FractionDigits();

		/**
		 * The meta object literal for the '<em><b>Numerator</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FRACTION_NUMBER_FORMAT_SPECIFIER__NUMERATOR = eINSTANCE.getFractionNumberFormatSpecifier_Numerator();

		/**
		 * The meta object literal for the '<em><b>Prefix</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FRACTION_NUMBER_FORMAT_SPECIFIER__PREFIX = eINSTANCE.getFractionNumberFormatSpecifier_Prefix();

		/**
		 * The meta object literal for the '<em><b>Suffix</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FRACTION_NUMBER_FORMAT_SPECIFIER__SUFFIX = eINSTANCE.getFractionNumberFormatSpecifier_Suffix();

		/**
		 * The meta object literal for the '<em><b>Delimiter</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FRACTION_NUMBER_FORMAT_SPECIFIER__DELIMITER = eINSTANCE.getFractionNumberFormatSpecifier_Delimiter();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.GradientImpl
		 * <em>Gradient</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.GradientImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getGradient()
		 * @generated
		 */
		EClass GRADIENT = eINSTANCE.getGradient();

		/**
		 * The meta object literal for the '<em><b>Start Color</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GRADIENT__START_COLOR = eINSTANCE.getGradient_StartColor();

		/**
		 * The meta object literal for the '<em><b>End Color</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GRADIENT__END_COLOR = eINSTANCE.getGradient_EndColor();

		/**
		 * The meta object literal for the '<em><b>Direction</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GRADIENT__DIRECTION = eINSTANCE.getGradient_Direction();

		/**
		 * The meta object literal for the '<em><b>Cyclic</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GRADIENT__CYCLIC = eINSTANCE.getGradient_Cyclic();

		/**
		 * The meta object literal for the '<em><b>Transparency</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute GRADIENT__TRANSPARENCY = eINSTANCE.getGradient_Transparency();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.ImageImpl
		 * <em>Image</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.ImageImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getImage()
		 * @generated
		 */
		EClass IMAGE = eINSTANCE.getImage();

		/**
		 * The meta object literal for the '<em><b>URL</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute IMAGE__URL = eINSTANCE.getImage_URL();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute IMAGE__SOURCE = eINSTANCE.getImage_Source();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.InsetsImpl
		 * <em>Insets</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.InsetsImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getInsets()
		 * @generated
		 */
		EClass INSETS = eINSTANCE.getInsets();

		/**
		 * The meta object literal for the '<em><b>Top</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute INSETS__TOP = eINSTANCE.getInsets_Top();

		/**
		 * The meta object literal for the '<em><b>Left</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute INSETS__LEFT = eINSTANCE.getInsets_Left();

		/**
		 * The meta object literal for the '<em><b>Bottom</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute INSETS__BOTTOM = eINSTANCE.getInsets_Bottom();

		/**
		 * The meta object literal for the '<em><b>Right</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute INSETS__RIGHT = eINSTANCE.getInsets_Right();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl
		 * <em>Interactivity</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getInteractivity()
		 * @generated
		 */
		EClass INTERACTIVITY = eINSTANCE.getInteractivity();

		/**
		 * The meta object literal for the '<em><b>Enable</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute INTERACTIVITY__ENABLE = eINSTANCE.getInteractivity_Enable();

		/**
		 * The meta object literal for the '<em><b>Legend Behavior</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute INTERACTIVITY__LEGEND_BEHAVIOR = eINSTANCE.getInteractivity_LegendBehavior();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl
		 * <em>Java Date Format Specifier</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getJavaDateFormatSpecifier()
		 * @generated
		 */
		EClass JAVA_DATE_FORMAT_SPECIFIER = eINSTANCE.getJavaDateFormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Pattern</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute JAVA_DATE_FORMAT_SPECIFIER__PATTERN = eINSTANCE.getJavaDateFormatSpecifier_Pattern();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl
		 * <em>Java Number Format Specifier</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getJavaNumberFormatSpecifier()
		 * @generated
		 */
		EClass JAVA_NUMBER_FORMAT_SPECIFIER = eINSTANCE.getJavaNumberFormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Pattern</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute JAVA_NUMBER_FORMAT_SPECIFIER__PATTERN = eINSTANCE.getJavaNumberFormatSpecifier_Pattern();

		/**
		 * The meta object literal for the '<em><b>Multiplier</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER = eINSTANCE.getJavaNumberFormatSpecifier_Multiplier();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl
		 * <em>Line Attributes</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineAttributes()
		 * @generated
		 */
		EClass LINE_ATTRIBUTES = eINSTANCE.getLineAttributes();

		/**
		 * The meta object literal for the '<em><b>Style</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LINE_ATTRIBUTES__STYLE = eINSTANCE.getLineAttributes_Style();

		/**
		 * The meta object literal for the '<em><b>Thickness</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LINE_ATTRIBUTES__THICKNESS = eINSTANCE.getLineAttributes_Thickness();

		/**
		 * The meta object literal for the '<em><b>Color</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference LINE_ATTRIBUTES__COLOR = eINSTANCE.getLineAttributes_Color();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LINE_ATTRIBUTES__VISIBLE = eINSTANCE.getLineAttributes_Visible();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.LocationImpl
		 * <em>Location</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.LocationImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLocation()
		 * @generated
		 */
		EClass LOCATION = eINSTANCE.getLocation();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LOCATION__X = eINSTANCE.getLocation_X();

		/**
		 * The meta object literal for the '<em><b>Y</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LOCATION__Y = eINSTANCE.getLocation_Y();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.Location3DImpl
		 * <em>Location3 D</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.Location3DImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLocation3D()
		 * @generated
		 */
		EClass LOCATION3_D = eINSTANCE.getLocation3D();

		/**
		 * The meta object literal for the '<em><b>Z</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LOCATION3_D__Z = eINSTANCE.getLocation3D_Z();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl
		 * <em>Marker</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.MarkerImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMarker()
		 * @generated
		 */
		EClass MARKER = eINSTANCE.getMarker();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MARKER__TYPE = eINSTANCE.getMarker_Type();

		/**
		 * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MARKER__SIZE = eINSTANCE.getMarker_Size();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MARKER__VISIBLE = eINSTANCE.getMarker_Visible();

		/**
		 * The meta object literal for the '<em><b>Fill</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER__FILL = eINSTANCE.getMarker_Fill();

		/**
		 * The meta object literal for the '<em><b>Icon Palette</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER__ICON_PALETTE = eINSTANCE.getMarker_IconPalette();

		/**
		 * The meta object literal for the '<em><b>Outline</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MARKER__OUTLINE = eINSTANCE.getMarker_Outline();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl
		 * <em>Multiple Fill</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMultipleFill()
		 * @generated
		 */
		EClass MULTIPLE_FILL = eINSTANCE.getMultipleFill();

		/**
		 * The meta object literal for the '<em><b>Fills</b></em>' containment reference
		 * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MULTIPLE_FILL__FILLS = eINSTANCE.getMultipleFill_Fills();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl
		 * <em>Multi URL Values</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMultiURLValues()
		 * @generated
		 */
		EClass MULTI_URL_VALUES = eINSTANCE.getMultiURLValues();

		/**
		 * The meta object literal for the '<em><b>URL Values</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MULTI_URL_VALUES__URL_VALUES = eINSTANCE.getMultiURLValues_URLValues();

		/**
		 * The meta object literal for the '<em><b>Tooltip</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MULTI_URL_VALUES__TOOLTIP = eINSTANCE.getMultiURLValues_Tooltip();

		/**
		 * The meta object literal for the '<em><b>Properties Map</b></em>' map feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MULTI_URL_VALUES__PROPERTIES_MAP = eINSTANCE.getMultiURLValues_PropertiesMap();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl
		 * <em>Number Format Specifier</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getNumberFormatSpecifier()
		 * @generated
		 */
		EClass NUMBER_FORMAT_SPECIFIER = eINSTANCE.getNumberFormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Prefix</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute NUMBER_FORMAT_SPECIFIER__PREFIX = eINSTANCE.getNumberFormatSpecifier_Prefix();

		/**
		 * The meta object literal for the '<em><b>Suffix</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute NUMBER_FORMAT_SPECIFIER__SUFFIX = eINSTANCE.getNumberFormatSpecifier_Suffix();

		/**
		 * The meta object literal for the '<em><b>Multiplier</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute NUMBER_FORMAT_SPECIFIER__MULTIPLIER = eINSTANCE.getNumberFormatSpecifier_Multiplier();

		/**
		 * The meta object literal for the '<em><b>Fraction Digits</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS = eINSTANCE.getNumberFormatSpecifier_FractionDigits();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.PaletteImpl
		 * <em>Palette</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.PaletteImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPalette()
		 * @generated
		 */
		EClass PALETTE = eINSTANCE.getPalette();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PALETTE__NAME = eINSTANCE.getPalette_Name();

		/**
		 * The meta object literal for the '<em><b>Entries</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PALETTE__ENTRIES = eINSTANCE.getPalette_Entries();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.PatternImageImpl
		 * <em>Pattern Image</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.PatternImageImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPatternImage()
		 * @generated
		 */
		EClass PATTERN_IMAGE = eINSTANCE.getPatternImage();

		/**
		 * The meta object literal for the '<em><b>Bitmap</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PATTERN_IMAGE__BITMAP = eINSTANCE.getPatternImage_Bitmap();

		/**
		 * The meta object literal for the '<em><b>Fore Color</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATTERN_IMAGE__FORE_COLOR = eINSTANCE.getPatternImage_ForeColor();

		/**
		 * The meta object literal for the '<em><b>Back Color</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PATTERN_IMAGE__BACK_COLOR = eINSTANCE.getPatternImage_BackColor();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl
		 * <em>Rotation3 D</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRotation3D()
		 * @generated
		 */
		EClass ROTATION3_D = eINSTANCE.getRotation3D();

		/**
		 * The meta object literal for the '<em><b>Angles</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ROTATION3_D__ANGLES = eINSTANCE.getRotation3D_Angles();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.ScriptValueImpl
		 * <em>Script Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.ScriptValueImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getScriptValue()
		 * @generated
		 */
		EClass SCRIPT_VALUE = eINSTANCE.getScriptValue();

		/**
		 * The meta object literal for the '<em><b>Script</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SCRIPT_VALUE__SCRIPT = eINSTANCE.getScriptValue_Script();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl
		 * <em>Series Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSeriesValue()
		 * @generated
		 */
		EClass SERIES_VALUE = eINSTANCE.getSeriesValue();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SERIES_VALUE__NAME = eINSTANCE.getSeriesValue_Name();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.SizeImpl <em>Size</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.SizeImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSize()
		 * @generated
		 */
		EClass SIZE = eINSTANCE.getSize();

		/**
		 * The meta object literal for the '<em><b>Height</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SIZE__HEIGHT = eINSTANCE.getSize_Height();

		/**
		 * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute SIZE__WIDTH = eINSTANCE.getSize_Width();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.StringFormatSpecifierImpl
		 * <em>String Format Specifier</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.StringFormatSpecifierImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStringFormatSpecifier()
		 * @generated
		 */
		EClass STRING_FORMAT_SPECIFIER = eINSTANCE.getStringFormatSpecifier();

		/**
		 * The meta object literal for the '<em><b>Pattern</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute STRING_FORMAT_SPECIFIER__PATTERN = eINSTANCE.getStringFormatSpecifier_Pattern();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.StyleImpl
		 * <em>Style</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.StyleImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyle()
		 * @generated
		 */
		EClass STYLE = eINSTANCE.getStyle();

		/**
		 * The meta object literal for the '<em><b>Font</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference STYLE__FONT = eINSTANCE.getStyle_Font();

		/**
		 * The meta object literal for the '<em><b>Color</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference STYLE__COLOR = eINSTANCE.getStyle_Color();

		/**
		 * The meta object literal for the '<em><b>Background Color</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference STYLE__BACKGROUND_COLOR = eINSTANCE.getStyle_BackgroundColor();

		/**
		 * The meta object literal for the '<em><b>Background Image</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference STYLE__BACKGROUND_IMAGE = eINSTANCE.getStyle_BackgroundImage();

		/**
		 * The meta object literal for the '<em><b>Padding</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference STYLE__PADDING = eINSTANCE.getStyle_Padding();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl <em>Style
		 * Map</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyleMap()
		 * @generated
		 */
		EClass STYLE_MAP = eINSTANCE.getStyleMap();

		/**
		 * The meta object literal for the '<em><b>Component Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute STYLE_MAP__COMPONENT_NAME = eINSTANCE.getStyleMap_ComponentName();

		/**
		 * The meta object literal for the '<em><b>Style</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference STYLE_MAP__STYLE = eINSTANCE.getStyleMap_Style();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.TextImpl <em>Text</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.TextImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getText()
		 * @generated
		 */
		EClass TEXT = eINSTANCE.getText();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TEXT__VALUE = eINSTANCE.getText_Value();

		/**
		 * The meta object literal for the '<em><b>Font</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TEXT__FONT = eINSTANCE.getText_Font();

		/**
		 * The meta object literal for the '<em><b>Color</b></em>' containment reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TEXT__COLOR = eINSTANCE.getText_Color();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl
		 * <em>Text Alignment</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTextAlignment()
		 * @generated
		 */
		EClass TEXT_ALIGNMENT = eINSTANCE.getTextAlignment();

		/**
		 * The meta object literal for the '<em><b>Horizontal Alignment</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT = eINSTANCE.getTextAlignment_HorizontalAlignment();

		/**
		 * The meta object literal for the '<em><b>Vertical Alignment</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TEXT_ALIGNMENT__VERTICAL_ALIGNMENT = eINSTANCE.getTextAlignment_VerticalAlignment();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl
		 * <em>Tooltip Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTooltipValue()
		 * @generated
		 */
		EClass TOOLTIP_VALUE = eINSTANCE.getTooltipValue();

		/**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TOOLTIP_VALUE__TEXT = eINSTANCE.getTooltipValue_Text();

		/**
		 * The meta object literal for the '<em><b>Delay</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TOOLTIP_VALUE__DELAY = eINSTANCE.getTooltipValue_Delay();

		/**
		 * The meta object literal for the '<em><b>Format Specifier</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TOOLTIP_VALUE__FORMAT_SPECIFIER = eINSTANCE.getTooltipValue_FormatSpecifier();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl <em>URL
		 * Value</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.URLValueImpl
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getURLValue()
		 * @generated
		 */
		EClass URL_VALUE = eINSTANCE.getURLValue();

		/**
		 * The meta object literal for the '<em><b>Base Url</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute URL_VALUE__BASE_URL = eINSTANCE.getURLValue_BaseUrl();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute URL_VALUE__TARGET = eINSTANCE.getURLValue_Target();

		/**
		 * The meta object literal for the '<em><b>Base Parameter Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute URL_VALUE__BASE_PARAMETER_NAME = eINSTANCE.getURLValue_BaseParameterName();

		/**
		 * The meta object literal for the '<em><b>Value Parameter Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute URL_VALUE__VALUE_PARAMETER_NAME = eINSTANCE.getURLValue_ValueParameterName();

		/**
		 * The meta object literal for the '<em><b>Series Parameter Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute URL_VALUE__SERIES_PARAMETER_NAME = eINSTANCE.getURLValue_SeriesParameterName();

		/**
		 * The meta object literal for the '<em><b>Tooltip</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute URL_VALUE__TOOLTIP = eINSTANCE.getURLValue_Tooltip();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.ActionType <em>Action
		 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.ActionType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getActionType()
		 * @generated
		 */
		EEnum ACTION_TYPE = eINSTANCE.getActionType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.Anchor <em>Anchor</em>}' enum.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.Anchor
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAnchor()
		 * @generated
		 */
		EEnum ANCHOR = eINSTANCE.getAnchor();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.AngleType <em>Angle
		 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.AngleType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAngleType()
		 * @generated
		 */
		EEnum ANGLE_TYPE = eINSTANCE.getAngleType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.AxisType <em>Axis Type</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.AxisType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAxisType()
		 * @generated
		 */
		EEnum AXIS_TYPE = eINSTANCE.getAxisType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.ChartDimension <em>Chart
		 * Dimension</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartDimension()
		 * @generated
		 */
		EEnum CHART_DIMENSION = eINSTANCE.getChartDimension();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.ChartType <em>Chart
		 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.ChartType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartType()
		 * @generated
		 */
		EEnum CHART_TYPE = eINSTANCE.getChartType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.CursorType <em>Cursor
		 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.CursorType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getCursorType()
		 * @generated
		 */
		EEnum CURSOR_TYPE = eINSTANCE.getCursorType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.DataPointComponentType
		 * <em>Data Point Component Type</em>}' enum. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPointComponentType()
		 * @generated
		 */
		EEnum DATA_POINT_COMPONENT_TYPE = eINSTANCE.getDataPointComponentType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.DataType <em>Data Type</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.DataType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataType()
		 * @generated
		 */
		EEnum DATA_TYPE = eINSTANCE.getDataType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatDetail <em>Date
		 * Format Detail</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatDetail()
		 * @generated
		 */
		EEnum DATE_FORMAT_DETAIL = eINSTANCE.getDateFormatDetail();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatType <em>Date Format
		 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatType()
		 * @generated
		 */
		EEnum DATE_FORMAT_TYPE = eINSTANCE.getDateFormatType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.Direction <em>Direction</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.Direction
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDirection()
		 * @generated
		 */
		EEnum DIRECTION = eINSTANCE.getDirection();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.GroupingUnitType <em>Grouping
		 * Unit Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getGroupingUnitType()
		 * @generated
		 */
		EEnum GROUPING_UNIT_TYPE = eINSTANCE.getGroupingUnitType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.HorizontalAlignment
		 * <em>Horizontal Alignment</em>}' enum. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getHorizontalAlignment()
		 * @generated
		 */
		EEnum HORIZONTAL_ALIGNMENT = eINSTANCE.getHorizontalAlignment();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.ImageSourceType <em>Image
		 * Source Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.ImageSourceType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getImageSourceType()
		 * @generated
		 */
		EEnum IMAGE_SOURCE_TYPE = eINSTANCE.getImageSourceType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.IntersectionType
		 * <em>Intersection Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getIntersectionType()
		 * @generated
		 */
		EEnum INTERSECTION_TYPE = eINSTANCE.getIntersectionType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.LeaderLineStyle <em>Leader
		 * Line Style</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLeaderLineStyle()
		 * @generated
		 */
		EEnum LEADER_LINE_STYLE = eINSTANCE.getLeaderLineStyle();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.LegendBehaviorType <em>Legend
		 * Behavior Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.LegendBehaviorType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLegendBehaviorType()
		 * @generated
		 */
		EEnum LEGEND_BEHAVIOR_TYPE = eINSTANCE.getLegendBehaviorType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.LegendItemType <em>Legend Item
		 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLegendItemType()
		 * @generated
		 */
		EEnum LEGEND_ITEM_TYPE = eINSTANCE.getLegendItemType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.LineDecorator <em>Line
		 * Decorator</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineDecorator()
		 * @generated
		 */
		EEnum LINE_DECORATOR = eINSTANCE.getLineDecorator();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.LineStyle <em>Line
		 * Style</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.LineStyle
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineStyle()
		 * @generated
		 */
		EEnum LINE_STYLE = eINSTANCE.getLineStyle();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.MarkerType <em>Marker
		 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.MarkerType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMarkerType()
		 * @generated
		 */
		EEnum MARKER_TYPE = eINSTANCE.getMarkerType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.MenuStylesKeyType <em>Menu
		 * Styles Key Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.MenuStylesKeyType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMenuStylesKeyType()
		 * @generated
		 */
		EEnum MENU_STYLES_KEY_TYPE = eINSTANCE.getMenuStylesKeyType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.Orientation
		 * <em>Orientation</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.Orientation
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getOrientation()
		 * @generated
		 */
		EEnum ORIENTATION = eINSTANCE.getOrientation();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.Position <em>Position</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.Position
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPosition()
		 * @generated
		 */
		EEnum POSITION = eINSTANCE.getPosition();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.RiserType <em>Riser
		 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.RiserType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRiserType()
		 * @generated
		 */
		EEnum RISER_TYPE = eINSTANCE.getRiserType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.RuleType <em>Rule Type</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.RuleType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRuleType()
		 * @generated
		 */
		EEnum RULE_TYPE = eINSTANCE.getRuleType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.ScaleUnitType <em>Scale Unit
		 * Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getScaleUnitType()
		 * @generated
		 */
		EEnum SCALE_UNIT_TYPE = eINSTANCE.getScaleUnitType();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.SortOption <em>Sort
		 * Option</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.SortOption
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSortOption()
		 * @generated
		 */
		EEnum SORT_OPTION = eINSTANCE.getSortOption();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.Stretch <em>Stretch</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.Stretch
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStretch()
		 * @generated
		 */
		EEnum STRETCH = eINSTANCE.getStretch();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.StyledComponent <em>Styled
		 * Component</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyledComponent()
		 * @generated
		 */
		EEnum STYLED_COMPONENT = eINSTANCE.getStyledComponent();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.TickStyle <em>Tick
		 * Style</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.TickStyle
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTickStyle()
		 * @generated
		 */
		EEnum TICK_STYLE = eINSTANCE.getTickStyle();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.TriggerCondition <em>Trigger
		 * Condition</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTriggerCondition()
		 * @generated
		 */
		EEnum TRIGGER_CONDITION = eINSTANCE.getTriggerCondition();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.TriggerFlow <em>Trigger
		 * Flow</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.TriggerFlow
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTriggerFlow()
		 * @generated
		 */
		EEnum TRIGGER_FLOW = eINSTANCE.getTriggerFlow();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement <em>Units
		 * Of Measurement</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getUnitsOfMeasurement()
		 * @generated
		 */
		EEnum UNITS_OF_MEASUREMENT = eINSTANCE.getUnitsOfMeasurement();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.chart.model.attribute.VerticalAlignment <em>Vertical
		 * Alignment</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getVerticalAlignment()
		 * @generated
		 */
		EEnum VERTICAL_ALIGNMENT = eINSTANCE.getVerticalAlignment();

		/**
		 * The meta object literal for the '<em>Action Type Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.ActionType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getActionTypeObject()
		 * @generated
		 */
		EDataType ACTION_TYPE_OBJECT = eINSTANCE.getActionTypeObject();

		/**
		 * The meta object literal for the '<em>Anchor Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.Anchor
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAnchorObject()
		 * @generated
		 */
		EDataType ANCHOR_OBJECT = eINSTANCE.getAnchorObject();

		/**
		 * The meta object literal for the '<em>Angle Type Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.AngleType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAngleTypeObject()
		 * @generated
		 */
		EDataType ANGLE_TYPE_OBJECT = eINSTANCE.getAngleTypeObject();

		/**
		 * The meta object literal for the '<em>Axis Type Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.AxisType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAxisTypeObject()
		 * @generated
		 */
		EDataType AXIS_TYPE_OBJECT = eINSTANCE.getAxisTypeObject();

		/**
		 * The meta object literal for the '<em>Chart Dimension Object</em>' data type.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartDimensionObject()
		 * @generated
		 */
		EDataType CHART_DIMENSION_OBJECT = eINSTANCE.getChartDimensionObject();

		/**
		 * The meta object literal for the '<em>Chart Type Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.ChartType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartTypeObject()
		 * @generated
		 */
		EDataType CHART_TYPE_OBJECT = eINSTANCE.getChartTypeObject();

		/**
		 * The meta object literal for the '<em>Cursor Type Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.CursorType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getCursorTypeObject()
		 * @generated
		 */
		EDataType CURSOR_TYPE_OBJECT = eINSTANCE.getCursorTypeObject();

		/**
		 * The meta object literal for the '<em>Data Point Component Type Object</em>'
		 * data type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPointComponentTypeObject()
		 * @generated
		 */
		EDataType DATA_POINT_COMPONENT_TYPE_OBJECT = eINSTANCE.getDataPointComponentTypeObject();

		/**
		 * The meta object literal for the '<em>Data Type Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.DataType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataTypeObject()
		 * @generated
		 */
		EDataType DATA_TYPE_OBJECT = eINSTANCE.getDataTypeObject();

		/**
		 * The meta object literal for the '<em>Date Format Detail Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatDetailObject()
		 * @generated
		 */
		EDataType DATE_FORMAT_DETAIL_OBJECT = eINSTANCE.getDateFormatDetailObject();

		/**
		 * The meta object literal for the '<em>Date Format Type Object</em>' data type.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatTypeObject()
		 * @generated
		 */
		EDataType DATE_FORMAT_TYPE_OBJECT = eINSTANCE.getDateFormatTypeObject();

		/**
		 * The meta object literal for the '<em>Direction Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.Direction
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDirectionObject()
		 * @generated
		 */
		EDataType DIRECTION_OBJECT = eINSTANCE.getDirectionObject();

		/**
		 * The meta object literal for the '<em>Grouping Unit Type Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getGroupingUnitTypeObject()
		 * @generated
		 */
		EDataType GROUPING_UNIT_TYPE_OBJECT = eINSTANCE.getGroupingUnitTypeObject();

		/**
		 * The meta object literal for the '<em>Horizontal Alignment Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getHorizontalAlignmentObject()
		 * @generated
		 */
		EDataType HORIZONTAL_ALIGNMENT_OBJECT = eINSTANCE.getHorizontalAlignmentObject();

		/**
		 * The meta object literal for the '<em>ID</em>' data type. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @see java.lang.String
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getID()
		 * @generated
		 */
		EDataType ID = eINSTANCE.getID();

		/**
		 * The meta object literal for the '<em>Image Source Type Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.ImageSourceType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getImageSourceTypeObject()
		 * @generated
		 */
		EDataType IMAGE_SOURCE_TYPE_OBJECT = eINSTANCE.getImageSourceTypeObject();

		/**
		 * The meta object literal for the '<em>Intersection Type Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getIntersectionTypeObject()
		 * @generated
		 */
		EDataType INTERSECTION_TYPE_OBJECT = eINSTANCE.getIntersectionTypeObject();

		/**
		 * The meta object literal for the '<em>Leader Line Style Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLeaderLineStyleObject()
		 * @generated
		 */
		EDataType LEADER_LINE_STYLE_OBJECT = eINSTANCE.getLeaderLineStyleObject();

		/**
		 * The meta object literal for the '<em>Legend Behavior Type Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.LegendBehaviorType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLegendBehaviorTypeObject()
		 * @generated
		 */
		EDataType LEGEND_BEHAVIOR_TYPE_OBJECT = eINSTANCE.getLegendBehaviorTypeObject();

		/**
		 * The meta object literal for the '<em>Legend Item Type Object</em>' data type.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLegendItemTypeObject()
		 * @generated
		 */
		EDataType LEGEND_ITEM_TYPE_OBJECT = eINSTANCE.getLegendItemTypeObject();

		/**
		 * The meta object literal for the '<em>Line Decorator Object</em>' data type.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineDecoratorObject()
		 * @generated
		 */
		EDataType LINE_DECORATOR_OBJECT = eINSTANCE.getLineDecoratorObject();

		/**
		 * The meta object literal for the '<em>Line Style Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.LineStyle
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineStyleObject()
		 * @generated
		 */
		EDataType LINE_STYLE_OBJECT = eINSTANCE.getLineStyleObject();

		/**
		 * The meta object literal for the '<em>Marker Type Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.MarkerType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMarkerTypeObject()
		 * @generated
		 */
		EDataType MARKER_TYPE_OBJECT = eINSTANCE.getMarkerTypeObject();

		/**
		 * The meta object literal for the '<em>Menu Styles Key Type Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.MenuStylesKeyType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMenuStylesKeyTypeObject()
		 * @generated
		 */
		EDataType MENU_STYLES_KEY_TYPE_OBJECT = eINSTANCE.getMenuStylesKeyTypeObject();

		/**
		 * The meta object literal for the '<em>Orientation Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.Orientation
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getOrientationObject()
		 * @generated
		 */
		EDataType ORIENTATION_OBJECT = eINSTANCE.getOrientationObject();

		/**
		 * The meta object literal for the '<em>Pattern Bitmap</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPatternBitmap()
		 * @generated
		 */
		EDataType PATTERN_BITMAP = eINSTANCE.getPatternBitmap();

		/**
		 * The meta object literal for the '<em>Pattern Bitmap Object</em>' data type.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.lang.Long
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPatternBitmapObject()
		 * @generated
		 */
		EDataType PATTERN_BITMAP_OBJECT = eINSTANCE.getPatternBitmapObject();

		/**
		 * The meta object literal for the '<em>Percentage</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPercentage()
		 * @generated
		 */
		EDataType PERCENTAGE = eINSTANCE.getPercentage();

		/**
		 * The meta object literal for the '<em>Percentage Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.lang.Double
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPercentageObject()
		 * @generated
		 */
		EDataType PERCENTAGE_OBJECT = eINSTANCE.getPercentageObject();

		/**
		 * The meta object literal for the '<em>Position Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.Position
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPositionObject()
		 * @generated
		 */
		EDataType POSITION_OBJECT = eINSTANCE.getPositionObject();

		/**
		 * The meta object literal for the '<em>RGB Value</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRGBValue()
		 * @generated
		 */
		EDataType RGB_VALUE = eINSTANCE.getRGBValue();

		/**
		 * The meta object literal for the '<em>RGB Value Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see java.lang.Integer
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRGBValueObject()
		 * @generated
		 */
		EDataType RGB_VALUE_OBJECT = eINSTANCE.getRGBValueObject();

		/**
		 * The meta object literal for the '<em>Riser Type Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.RiserType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRiserTypeObject()
		 * @generated
		 */
		EDataType RISER_TYPE_OBJECT = eINSTANCE.getRiserTypeObject();

		/**
		 * The meta object literal for the '<em>Rule Type Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.RuleType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRuleTypeObject()
		 * @generated
		 */
		EDataType RULE_TYPE_OBJECT = eINSTANCE.getRuleTypeObject();

		/**
		 * The meta object literal for the '<em>Scale Unit Type Object</em>' data type.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getScaleUnitTypeObject()
		 * @generated
		 */
		EDataType SCALE_UNIT_TYPE_OBJECT = eINSTANCE.getScaleUnitTypeObject();

		/**
		 * The meta object literal for the '<em>Sort Option Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.SortOption
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSortOptionObject()
		 * @generated
		 */
		EDataType SORT_OPTION_OBJECT = eINSTANCE.getSortOptionObject();

		/**
		 * The meta object literal for the '<em>Stretch Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.Stretch
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStretchObject()
		 * @generated
		 */
		EDataType STRETCH_OBJECT = eINSTANCE.getStretchObject();

		/**
		 * The meta object literal for the '<em>Styled Component Object</em>' data type.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyledComponentObject()
		 * @generated
		 */
		EDataType STYLED_COMPONENT_OBJECT = eINSTANCE.getStyledComponentObject();

		/**
		 * The meta object literal for the '<em>Tick Style Object</em>' data type. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.TickStyle
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTickStyleObject()
		 * @generated
		 */
		EDataType TICK_STYLE_OBJECT = eINSTANCE.getTickStyleObject();

		/**
		 * The meta object literal for the '<em>Trigger Condition Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTriggerConditionObject()
		 * @generated
		 */
		EDataType TRIGGER_CONDITION_OBJECT = eINSTANCE.getTriggerConditionObject();

		/**
		 * The meta object literal for the '<em>Trigger Flow Object</em>' data type.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.TriggerFlow
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTriggerFlowObject()
		 * @generated
		 */
		EDataType TRIGGER_FLOW_OBJECT = eINSTANCE.getTriggerFlowObject();

		/**
		 * The meta object literal for the '<em>Units Of Measurement Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getUnitsOfMeasurementObject()
		 * @generated
		 */
		EDataType UNITS_OF_MEASUREMENT_OBJECT = eINSTANCE.getUnitsOfMeasurementObject();

		/**
		 * The meta object literal for the '<em>Vertical Alignment Object</em>' data
		 * type. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
		 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getVerticalAlignmentObject()
		 * @generated
		 */
		EDataType VERTICAL_ALIGNMENT_OBJECT = eINSTANCE.getVerticalAlignmentObject();

	}

} // AttributePackage
