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

import java.util.List;

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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

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
public class AttributeSwitch
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
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public Object doSwitch( EObject theEObject )
	{
		return doSwitch( theEObject.eClass( ), theEObject );
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch( EClass theEClass, EObject theEObject )
	{
		if ( theEClass.eContainer( ) == modelPackage )
		{
			return doSwitch( theEClass.getClassifierID( ), theEObject );
		}
		else
		{
			List eSuperTypes = theEClass.getESuperTypes( );
			return eSuperTypes.isEmpty( ) ? defaultCase( theEObject )
					: doSwitch( (EClass) eSuperTypes.get( 0 ), theEObject );
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch( int classifierID, EObject theEObject )
	{
		switch ( classifierID )
		{
			case AttributePackage.ACCESSIBILITY_VALUE :
			{
				AccessibilityValue accessibilityValue = (AccessibilityValue) theEObject;
				Object result = caseAccessibilityValue( accessibilityValue );
				if ( result == null )
					result = caseActionValue( accessibilityValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.ACTION_VALUE :
			{
				ActionValue actionValue = (ActionValue) theEObject;
				Object result = caseActionValue( actionValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.ANGLE3_D :
			{
				Angle3D angle3D = (Angle3D) theEObject;
				Object result = caseAngle3D( angle3D );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.AXIS_ORIGIN :
			{
				AxisOrigin axisOrigin = (AxisOrigin) theEObject;
				Object result = caseAxisOrigin( axisOrigin );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.BOUNDS :
			{
				Bounds bounds = (Bounds) theEObject;
				Object result = caseBounds( bounds );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.CALL_BACK_VALUE :
			{
				CallBackValue callBackValue = (CallBackValue) theEObject;
				Object result = caseCallBackValue( callBackValue );
				if ( result == null )
					result = caseActionValue( callBackValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.COLOR_DEFINITION :
			{
				ColorDefinition colorDefinition = (ColorDefinition) theEObject;
				Object result = caseColorDefinition( colorDefinition );
				if ( result == null )
					result = caseFill( colorDefinition );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.DATA_POINT :
			{
				DataPoint dataPoint = (DataPoint) theEObject;
				Object result = caseDataPoint( dataPoint );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.DATA_POINT_COMPONENT :
			{
				DataPointComponent dataPointComponent = (DataPointComponent) theEObject;
				Object result = caseDataPointComponent( dataPointComponent );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.DATE_FORMAT_SPECIFIER :
			{
				DateFormatSpecifier dateFormatSpecifier = (DateFormatSpecifier) theEObject;
				Object result = caseDateFormatSpecifier( dateFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( dateFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.EMBEDDED_IMAGE :
			{
				EmbeddedImage embeddedImage = (EmbeddedImage) theEObject;
				Object result = caseEmbeddedImage( embeddedImage );
				if ( result == null )
					result = caseImage( embeddedImage );
				if ( result == null )
					result = caseFill( embeddedImage );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.EXTENDED_PROPERTY :
			{
				ExtendedProperty extendedProperty = (ExtendedProperty) theEObject;
				Object result = caseExtendedProperty( extendedProperty );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.FILL :
			{
				Fill fill = (Fill) theEObject;
				Object result = caseFill( fill );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.FONT_DEFINITION :
			{
				FontDefinition fontDefinition = (FontDefinition) theEObject;
				Object result = caseFontDefinition( fontDefinition );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.FORMAT_SPECIFIER :
			{
				FormatSpecifier formatSpecifier = (FormatSpecifier) theEObject;
				Object result = caseFormatSpecifier( formatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER :
			{
				FractionNumberFormatSpecifier fractionNumberFormatSpecifier = (FractionNumberFormatSpecifier) theEObject;
				Object result = caseFractionNumberFormatSpecifier( fractionNumberFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( fractionNumberFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.GRADIENT :
			{
				Gradient gradient = (Gradient) theEObject;
				Object result = caseGradient( gradient );
				if ( result == null )
					result = caseFill( gradient );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.IMAGE :
			{
				Image image = (Image) theEObject;
				Object result = caseImage( image );
				if ( result == null )
					result = caseFill( image );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.INSETS :
			{
				Insets insets = (Insets) theEObject;
				Object result = caseInsets( insets );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.INTERACTIVITY :
			{
				Interactivity interactivity = (Interactivity) theEObject;
				Object result = caseInteractivity( interactivity );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.JAVA_DATE_FORMAT_SPECIFIER :
			{
				JavaDateFormatSpecifier javaDateFormatSpecifier = (JavaDateFormatSpecifier) theEObject;
				Object result = caseJavaDateFormatSpecifier( javaDateFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( javaDateFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER :
			{
				JavaNumberFormatSpecifier javaNumberFormatSpecifier = (JavaNumberFormatSpecifier) theEObject;
				Object result = caseJavaNumberFormatSpecifier( javaNumberFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( javaNumberFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.LINE_ATTRIBUTES :
			{
				LineAttributes lineAttributes = (LineAttributes) theEObject;
				Object result = caseLineAttributes( lineAttributes );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.LOCATION :
			{
				Location location = (Location) theEObject;
				Object result = caseLocation( location );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.LOCATION3_D :
			{
				Location3D location3D = (Location3D) theEObject;
				Object result = caseLocation3D( location3D );
				if ( result == null )
					result = caseLocation( location3D );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.MARKER :
			{
				Marker marker = (Marker) theEObject;
				Object result = caseMarker( marker );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.MULTIPLE_FILL :
			{
				MultipleFill multipleFill = (MultipleFill) theEObject;
				Object result = caseMultipleFill( multipleFill );
				if ( result == null )
					result = caseFill( multipleFill );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.NUMBER_FORMAT_SPECIFIER :
			{
				NumberFormatSpecifier numberFormatSpecifier = (NumberFormatSpecifier) theEObject;
				Object result = caseNumberFormatSpecifier( numberFormatSpecifier );
				if ( result == null )
					result = caseFormatSpecifier( numberFormatSpecifier );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.PALETTE :
			{
				Palette palette = (Palette) theEObject;
				Object result = casePalette( palette );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.ROTATION3_D :
			{
				Rotation3D rotation3D = (Rotation3D) theEObject;
				Object result = caseRotation3D( rotation3D );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.SCRIPT_VALUE :
			{
				ScriptValue scriptValue = (ScriptValue) theEObject;
				Object result = caseScriptValue( scriptValue );
				if ( result == null )
					result = caseActionValue( scriptValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.SERIES_VALUE :
			{
				SeriesValue seriesValue = (SeriesValue) theEObject;
				Object result = caseSeriesValue( seriesValue );
				if ( result == null )
					result = caseActionValue( seriesValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.SIZE :
			{
				Size size = (Size) theEObject;
				Object result = caseSize( size );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.STYLE :
			{
				Style style = (Style) theEObject;
				Object result = caseStyle( style );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.STYLE_MAP :
			{
				StyleMap styleMap = (StyleMap) theEObject;
				Object result = caseStyleMap( styleMap );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.TEXT :
			{
				Text text = (Text) theEObject;
				Object result = caseText( text );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.TEXT_ALIGNMENT :
			{
				TextAlignment textAlignment = (TextAlignment) theEObject;
				Object result = caseTextAlignment( textAlignment );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.TOOLTIP_VALUE :
			{
				TooltipValue tooltipValue = (TooltipValue) theEObject;
				Object result = caseTooltipValue( tooltipValue );
				if ( result == null )
					result = caseActionValue( tooltipValue );
				if ( result == null )
					result = defaultCase( theEObject );
				return result;
			}
			case AttributePackage.URL_VALUE :
			{
				URLValue urlValue = (URLValue) theEObject;
				Object result = caseURLValue( urlValue );
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
	 * Returns the result of interpretting the object as an instance of '<em>Accessibility Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Accessibility Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAccessibilityValue( AccessibilityValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Action Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Action Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseActionValue( ActionValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Angle3 D</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Angle3 D</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAngle3D( Angle3D object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Axis Origin</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Axis Origin</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAxisOrigin( AxisOrigin object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Bounds</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Bounds</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseBounds( Bounds object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Call Back Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Call Back Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseCallBackValue( CallBackValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Color Definition</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Color Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseColorDefinition( ColorDefinition object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Data Point</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Data Point</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDataPoint( DataPoint object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Data Point Component</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Data Point Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDataPointComponent( DataPointComponent object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Date Format Specifier</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Date Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDateFormatSpecifier( DateFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Embedded Image</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Embedded Image</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseEmbeddedImage( EmbeddedImage object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Extended Property</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Extended Property</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseExtendedProperty( ExtendedProperty object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Fill</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Fill</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFill( Fill object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Font Definition</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Font Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFontDefinition( FontDefinition object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Format Specifier</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFormatSpecifier( FormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Fraction Number Format Specifier</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Fraction Number Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFractionNumberFormatSpecifier(
			FractionNumberFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Gradient</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Gradient</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseGradient( Gradient object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Image</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Image</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseImage( Image object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Insets</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Insets</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseInsets( Insets object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Interactivity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Interactivity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseInteractivity( Interactivity object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Java Date Format Specifier</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Java Date Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseJavaDateFormatSpecifier( JavaDateFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Java Number Format Specifier</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Java Number Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseJavaNumberFormatSpecifier(
			JavaNumberFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Line Attributes</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Line Attributes</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseLineAttributes( LineAttributes object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Location</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseLocation( Location object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Location3 D</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Location3 D</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseLocation3D( Location3D object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Marker</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Marker</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseMarker( Marker object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Multiple Fill</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Multiple Fill</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseMultipleFill( MultipleFill object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Number Format Specifier</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Number Format Specifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseNumberFormatSpecifier( NumberFormatSpecifier object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Palette</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Palette</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object casePalette( Palette object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Rotation3 D</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Rotation3 D</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseRotation3D( Rotation3D object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Script Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Script Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseScriptValue( ScriptValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Series Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Series Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseSeriesValue( SeriesValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Size</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Size</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseSize( Size object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Style</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Style</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseStyle( Style object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Style Map</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Style Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseStyleMap( StyleMap object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Text</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Text</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseText( Text object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Text Alignment</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Text Alignment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTextAlignment( TextAlignment object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Tooltip Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Tooltip Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTooltipValue( TooltipValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>URL Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>URL Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseURLValue( URLValue object )
	{
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch, but this is the last case
	 * anyway. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public Object defaultCase( EObject object )
	{
		return null;
	}

} // AttributeSwitch
