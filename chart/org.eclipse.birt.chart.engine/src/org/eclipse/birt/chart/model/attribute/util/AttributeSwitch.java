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

package org.eclipse.birt.chart.model.attribute.util;

import java.util.Map;
import org.eclipse.birt.chart.model.attribute.*;

import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.SeriesValue;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc --> The <b>Switch </b> for the model's inheritance
 * hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)}to
 * invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object and proceeding up the
 * inheritance hierarchy until a non-null result is returned, which is the
 * result of the switch. <!-- end-user-doc -->
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage
 * @generated
 */
public class AttributeSwitch<T> extends Switch<T>
{

	/**
	 * The cached model package
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected static AttributePackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public AttributeSwitch( )
	{
		if ( modelPackage == null )
		{
			modelPackage = AttributePackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor( EPackage ePackage )
	{
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch( int classifierID, EObject theEObject )
	{
		switch ( classifierID )
		{
			case AttributePackage.ACCESSIBILITY_VALUE :
			{
				AccessibilityValue accessibilityValue = (AccessibilityValue) theEObject;
				T result = caseAccessibilityValue( accessibilityValue );
				if ( result == null )
					result = caseActionValue( accessibilityValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.ACTION_VALUE :
			{
				ActionValue actionValue = (ActionValue) theEObject;
				T result = caseActionValue( actionValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.ANGLE3_D :
			{
				Angle3D angle3D = (Angle3D) theEObject;
				T result = caseAngle3D( angle3D );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.AXIS_ORIGIN :
			{
				AxisOrigin axisOrigin = (AxisOrigin) theEObject;
				T result = caseAxisOrigin( axisOrigin );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.BOUNDS :
			{
				Bounds bounds = (Bounds) theEObject;
				T result = caseBounds( bounds );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.CALL_BACK_VALUE :
			{
				CallBackValue callBackValue = (CallBackValue) theEObject;
				T result = caseCallBackValue( callBackValue );
				if ( result == null )
					result = caseActionValue( callBackValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.COLOR_DEFINITION :
			{
				ColorDefinition colorDefinition = (ColorDefinition) theEObject;
				T result = caseColorDefinition( colorDefinition );
				if ( result == null )
					result = caseFill( colorDefinition );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.CURSOR :
			{
				Cursor cursor = (Cursor) theEObject;
				T result = caseCursor( cursor );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.DATA_POINT :
			{
				DataPoint dataPoint = (DataPoint) theEObject;
				T result = caseDataPoint( dataPoint );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.DATA_POINT_COMPONENT :
			{
				DataPointComponent dataPointComponent = (DataPointComponent) theEObject;
				T result = caseDataPointComponent( dataPointComponent );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.DATE_FORMAT_SPECIFIER :
			{
				DateFormatSpecifier dateFormatSpecifier = (DateFormatSpecifier) theEObject;
				T result = caseDateFormatSpecifier( dateFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( dateFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.EMBEDDED_IMAGE :
			{
				EmbeddedImage embeddedImage = (EmbeddedImage) theEObject;
				T result = caseEmbeddedImage( embeddedImage );
				if ( result == null )
					result = caseImage( embeddedImage );
				if ( result == null )
					result = caseFill( embeddedImage );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.ESTRING_TO_STRING_MAP_ENTRY :
			{
				@SuppressWarnings("unchecked")
				Map.Entry<String, String> eStringToStringMapEntry = (Map.Entry<String, String>) theEObject;
				T result = caseEStringToStringMapEntry( eStringToStringMapEntry );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.EXTENDED_PROPERTY :
			{
				ExtendedProperty extendedProperty = (ExtendedProperty) theEObject;
				T result = caseExtendedProperty( extendedProperty );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.FILL :
			{
				Fill fill = (Fill) theEObject;
				T result = caseFill( fill );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.FONT_DEFINITION :
			{
				FontDefinition fontDefinition = (FontDefinition) theEObject;
				T result = caseFontDefinition( fontDefinition );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.FORMAT_SPECIFIER :
			{
				FormatSpecifier formatSpecifier = (FormatSpecifier) theEObject;
				T result = caseFormatSpecifier( formatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER :
			{
				FractionNumberFormatSpecifier fractionNumberFormatSpecifier = (FractionNumberFormatSpecifier) theEObject;
				T result = caseFractionNumberFormatSpecifier( fractionNumberFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( fractionNumberFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.GRADIENT :
			{
				Gradient gradient = (Gradient) theEObject;
				T result = caseGradient( gradient );
				if ( result == null )
					result = caseFill( gradient );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.IMAGE :
			{
				Image image = (Image) theEObject;
				T result = caseImage( image );
				if ( result == null )
					result = caseFill( image );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.INSETS :
			{
				Insets insets = (Insets) theEObject;
				T result = caseInsets( insets );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.INTERACTIVITY :
			{
				Interactivity interactivity = (Interactivity) theEObject;
				T result = caseInteractivity( interactivity );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.JAVA_DATE_FORMAT_SPECIFIER :
			{
				JavaDateFormatSpecifier javaDateFormatSpecifier = (JavaDateFormatSpecifier) theEObject;
				T result = caseJavaDateFormatSpecifier( javaDateFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( javaDateFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER :
			{
				JavaNumberFormatSpecifier javaNumberFormatSpecifier = (JavaNumberFormatSpecifier) theEObject;
				T result = caseJavaNumberFormatSpecifier( javaNumberFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( javaNumberFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.LINE_ATTRIBUTES :
			{
				LineAttributes lineAttributes = (LineAttributes) theEObject;
				T result = caseLineAttributes( lineAttributes );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.LOCATION :
			{
				Location location = (Location) theEObject;
				T result = caseLocation( location );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.LOCATION3_D :
			{
				Location3D location3D = (Location3D) theEObject;
				T result = caseLocation3D( location3D );
				if ( result == null )
					result = caseLocation( location3D );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.MARKER :
			{
				Marker marker = (Marker) theEObject;
				T result = caseMarker( marker );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.MULTIPLE_FILL :
			{
				MultipleFill multipleFill = (MultipleFill) theEObject;
				T result = caseMultipleFill( multipleFill );
				if ( result == null )
					result = caseFill( multipleFill );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.MULTI_URL_VALUES :
			{
				MultiURLValues multiURLValues = (MultiURLValues) theEObject;
				T result = caseMultiURLValues( multiURLValues );
				if ( result == null )
					result = caseActionValue( multiURLValues );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.NUMBER_FORMAT_SPECIFIER :
			{
				NumberFormatSpecifier numberFormatSpecifier = (NumberFormatSpecifier) theEObject;
				T result = caseNumberFormatSpecifier( numberFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( numberFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.PALETTE :
			{
				Palette palette = (Palette) theEObject;
				T result = casePalette( palette );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.PATTERN_IMAGE :
			{
				PatternImage patternImage = (PatternImage) theEObject;
				T result = casePatternImage( patternImage );
				if ( result == null )
					result = caseImage( patternImage );
				if ( result == null )
					result = caseFill( patternImage );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.ROTATION3_D :
			{
				Rotation3D rotation3D = (Rotation3D) theEObject;
				T result = caseRotation3D( rotation3D );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.SCRIPT_VALUE :
			{
				ScriptValue scriptValue = (ScriptValue) theEObject;
				T result = caseScriptValue( scriptValue );
				if ( result == null )
					result = caseActionValue( scriptValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.SERIES_VALUE :
			{
				SeriesValue seriesValue = (SeriesValue) theEObject;
				T result = caseSeriesValue( seriesValue );
				if ( result == null )
					result = caseActionValue( seriesValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.SIZE :
			{
				Size size = (Size) theEObject;
				T result = caseSize( size );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.STRING_FORMAT_SPECIFIER :
			{
				StringFormatSpecifier stringFormatSpecifier = (StringFormatSpecifier) theEObject;
				T result = caseStringFormatSpecifier( stringFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( stringFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.STYLE :
			{
				Style style = (Style) theEObject;
				T result = caseStyle( style );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.STYLE_MAP :
			{
				StyleMap styleMap = (StyleMap) theEObject;
				T result = caseStyleMap( styleMap );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.TEXT :
			{
				Text text = (Text) theEObject;
				T result = caseText( text );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.TEXT_ALIGNMENT :
			{
				TextAlignment textAlignment = (TextAlignment) theEObject;
				T result = caseTextAlignment( textAlignment );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.TOOLTIP_VALUE :
			{
				TooltipValue tooltipValue = (TooltipValue) theEObject;
				T result = caseTooltipValue( tooltipValue );
				if ( result == null )
					result = caseActionValue( tooltipValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.URL_VALUE :
			{
				URLValue urlValue = (URLValue) theEObject;
				T result = caseURLValue( urlValue );
				if ( result == null )
					result = caseActionValue( urlValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			default :
				return defaultCase( theEObject );
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Accessibility Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Accessibility Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAccessibilityValue( AccessibilityValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Action Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Action Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseActionValue( ActionValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Angle3 D</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Angle3 D</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAngle3D( Angle3D object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Axis Origin</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Axis Origin</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAxisOrigin( AxisOrigin object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Bounds</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Bounds</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBounds( Bounds object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Call Back Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Call Back Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCallBackValue( CallBackValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Color Definition</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Color Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseColorDefinition( ColorDefinition object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cursor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cursor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCursor( Cursor object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Point</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Point</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataPoint( DataPoint object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Point Component</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Point Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataPointComponent( DataPointComponent object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Date Format Specifier</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Date Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDateFormatSpecifier( DateFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Embedded Image</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Embedded Image</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEmbeddedImage( EmbeddedImage object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To String Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To String Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToStringMapEntry( Map.Entry<String, String> object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Extended Property</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Extended Property</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExtendedProperty( ExtendedProperty object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fill</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fill</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFill( Fill object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Font Definition</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Font Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFontDefinition( FontDefinition object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Format Specifier</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFormatSpecifier( FormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fraction Number Format Specifier</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fraction Number Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFractionNumberFormatSpecifier(
			FractionNumberFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Gradient</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Gradient</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGradient( Gradient object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Image</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Image</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseImage( Image object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Insets</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Insets</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInsets( Insets object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Interactivity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Interactivity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInteractivity( Interactivity object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Java Date Format Specifier</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Java Date Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseJavaDateFormatSpecifier( JavaDateFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Java Number Format Specifier</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Java Number Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseJavaNumberFormatSpecifier( JavaNumberFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Line Attributes</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Line Attributes</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLineAttributes( LineAttributes object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Location</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocation( Location object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Location3 D</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Location3 D</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocation3D( Location3D object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Marker</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Marker</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMarker( Marker object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Multiple Fill</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Multiple Fill</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMultipleFill( MultipleFill object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Multi URL Values</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Multi URL Values</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMultiURLValues( MultiURLValues object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Number Format Specifier</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Number Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNumberFormatSpecifier( NumberFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Palette</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Palette</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePalette( Palette object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Pattern Image</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Pattern Image</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePatternImage( PatternImage object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Rotation3 D</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rotation3 D</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRotation3D( Rotation3D object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Script Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Script Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScriptValue( ScriptValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Series Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Series Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSeriesValue( SeriesValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Size</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Size</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSize( Size object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>String Format Specifier</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>String Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStringFormatSpecifier( StringFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Style</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Style</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStyle( Style object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Style Map</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Style Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStyleMap( StyleMap object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Text</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Text</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseText( Text object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Text Alignment</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Text Alignment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTextAlignment( TextAlignment object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tooltip Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tooltip Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTooltipValue( TooltipValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>URL Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>URL Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseURLValue( URLValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch, but this is the last case
	 * anyway. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase( EObject object )
	{
		return null;
	}

} // AttributeSwitch
