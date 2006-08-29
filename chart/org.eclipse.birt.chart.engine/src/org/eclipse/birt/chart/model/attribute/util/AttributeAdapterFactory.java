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
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory </b> for the model. It
 * provides an adapter <code>createXXX</code> method for each class of the
 * model. <!-- end-user-doc -->
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage
 * @generated
 */
public class AttributeAdapterFactory extends AdapterFactoryImpl
{

	/**
	 * The cached model package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected static AttributePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public AttributeAdapterFactory( )
	{
		if ( modelPackage == null )
		{
			modelPackage = AttributePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc --> This implementation returns <code>true</code>
	 * if the object is either the model's package or is an instance object of
	 * the model. <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	public boolean isFactoryForType( Object object )
	{
		if ( object == modelPackage )
		{
			return true;
		}
		if ( object instanceof EObject )
		{
			return ( (EObject) object ).eClass( ).getEPackage( ) == modelPackage;
		}
		return false;
	}

	/**
	 * The switch the delegates to the <code>createXXX</code> methods. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AttributeSwitch modelSwitch = new AttributeSwitch( ) {

		public Object caseAccessibilityValue( AccessibilityValue object )
		{
			return createAccessibilityValueAdapter( );
		}

		public Object caseActionValue( ActionValue object )
		{
			return createActionValueAdapter( );
		}

		public Object caseAngle3D( Angle3D object )
		{
			return createAngle3DAdapter( );
		}

		public Object caseAxisOrigin( AxisOrigin object )
		{
			return createAxisOriginAdapter( );
		}

		public Object caseBounds( Bounds object )
		{
			return createBoundsAdapter( );
		}

		public Object caseCallBackValue( CallBackValue object )
		{
			return createCallBackValueAdapter( );
		}

		public Object caseColorDefinition( ColorDefinition object )
		{
			return createColorDefinitionAdapter( );
		}

		public Object caseDataPoint( DataPoint object )
		{
			return createDataPointAdapter( );
		}

		public Object caseDataPointComponent( DataPointComponent object )
		{
			return createDataPointComponentAdapter( );
		}

		public Object caseDateFormatSpecifier( DateFormatSpecifier object )
		{
			return createDateFormatSpecifierAdapter( );
		}

		public Object caseEmbeddedImage( EmbeddedImage object )
		{
			return createEmbeddedImageAdapter( );
		}

		public Object caseExtendedProperty( ExtendedProperty object )
		{
			return createExtendedPropertyAdapter( );
		}

		public Object caseFill( Fill object )
		{
			return createFillAdapter( );
		}

		public Object caseFontDefinition( FontDefinition object )
		{
			return createFontDefinitionAdapter( );
		}

		public Object caseFormatSpecifier( FormatSpecifier object )
		{
			return createFormatSpecifierAdapter( );
		}

		public Object caseFractionNumberFormatSpecifier(
				FractionNumberFormatSpecifier object )
		{
			return createFractionNumberFormatSpecifierAdapter( );
		}

		public Object caseGradient( Gradient object )
		{
			return createGradientAdapter( );
		}

		public Object caseImage( Image object )
		{
			return createImageAdapter( );
		}

		public Object caseInsets( Insets object )
		{
			return createInsetsAdapter( );
		}

		public Object caseInteractivity( Interactivity object )
		{
			return createInteractivityAdapter( );
		}

		public Object caseJavaDateFormatSpecifier(
				JavaDateFormatSpecifier object )
		{
			return createJavaDateFormatSpecifierAdapter( );
		}

		public Object caseJavaNumberFormatSpecifier(
				JavaNumberFormatSpecifier object )
		{
			return createJavaNumberFormatSpecifierAdapter( );
		}

		public Object caseLineAttributes( LineAttributes object )
		{
			return createLineAttributesAdapter( );
		}

		public Object caseLocation( Location object )
		{
			return createLocationAdapter( );
		}

		public Object caseLocation3D( Location3D object )
		{
			return createLocation3DAdapter( );
		}

		public Object caseMarker( Marker object )
		{
			return createMarkerAdapter( );
		}

		public Object caseMultipleFill( MultipleFill object )
		{
			return createMultipleFillAdapter( );
		}

		public Object caseNumberFormatSpecifier( NumberFormatSpecifier object )
		{
			return createNumberFormatSpecifierAdapter( );
		}

		public Object casePalette( Palette object )
		{
			return createPaletteAdapter( );
		}

		public Object caseRotation3D( Rotation3D object )
		{
			return createRotation3DAdapter( );
		}

		public Object caseScriptValue( ScriptValue object )
		{
			return createScriptValueAdapter( );
		}

		public Object caseSeriesValue( SeriesValue object )
		{
			return createSeriesValueAdapter( );
		}

		public Object caseSize( Size object )
		{
			return createSizeAdapter( );
		}

		public Object caseStyle( Style object )
		{
			return createStyleAdapter( );
		}

		public Object caseStyleMap( StyleMap object )
		{
			return createStyleMapAdapter( );
		}

		public Object caseText( Text object )
		{
			return createTextAdapter( );
		}

		public Object caseTextAlignment( TextAlignment object )
		{
			return createTextAlignmentAdapter( );
		}

		public Object caseTooltipValue( TooltipValue object )
		{
			return createTooltipValueAdapter( );
		}

		public Object caseURLValue( URLValue object )
		{
			return createURLValueAdapter( );
		}

		public Object defaultCase( EObject object )
		{
			return createEObjectAdapter( );
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	public Adapter createAdapter( Notifier target )
	{
		return (Adapter) modelSwitch.doSwitch( (EObject) target );
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.AccessibilityValue <em>Accessibility Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.AccessibilityValue
	 * @generated
	 */
	public Adapter createAccessibilityValueAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.ActionValue <em>Action Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.ActionValue
	 * @generated
	 */
	public Adapter createActionValueAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Angle3D <em>Angle3 D</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D
	 * @generated
	 */
	public Adapter createAngle3DAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.AxisOrigin <em>Axis Origin</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.AxisOrigin
	 * @generated
	 */
	public Adapter createAxisOriginAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Bounds <em>Bounds</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds
	 * @generated
	 */
	public Adapter createBoundsAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.CallBackValue <em>Call Back Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.CallBackValue
	 * @generated
	 */
	public Adapter createCallBackValueAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition <em>Color Definition</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition
	 * @generated
	 */
	public Adapter createColorDefinitionAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.DataPoint <em>Data Point</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint
	 * @generated
	 */
	public Adapter createDataPointAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.DataPointComponent <em>Data Point Component</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponent
	 * @generated
	 */
	public Adapter createDataPointComponentAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier <em>Date Format Specifier</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatSpecifier
	 * @generated
	 */
	public Adapter createDateFormatSpecifierAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.EmbeddedImage <em>Embedded Image</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.EmbeddedImage
	 * @generated
	 */
	public Adapter createEmbeddedImageAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty <em>Extended Property</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.ExtendedProperty
	 * @generated
	 */
	public Adapter createExtendedPropertyAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Fill <em>Fill</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Fill
	 * @generated
	 */
	public Adapter createFillAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.FontDefinition <em>Font Definition</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition
	 * @generated
	 */
	public Adapter createFontDefinitionAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.FormatSpecifier <em>Format Specifier</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.FormatSpecifier
	 * @generated
	 */
	public Adapter createFormatSpecifierAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier <em>Fraction Number Format Specifier</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier
	 * @generated
	 */
	public Adapter createFractionNumberFormatSpecifierAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Gradient <em>Gradient</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient
	 * @generated
	 */
	public Adapter createGradientAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Image <em>Image</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Image
	 * @generated
	 */
	public Adapter createImageAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Insets <em>Insets</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Insets
	 * @generated
	 */
	public Adapter createInsetsAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Interactivity <em>Interactivity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Interactivity
	 * @generated
	 */
	public Adapter createInteractivityAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier <em>Java Date Format Specifier</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier
	 * @generated
	 */
	public Adapter createJavaDateFormatSpecifierAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier <em>Java Number Format Specifier</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier
	 * @generated
	 */
	public Adapter createJavaNumberFormatSpecifierAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.LineAttributes <em>Line Attributes</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes
	 * @generated
	 */
	public Adapter createLineAttributesAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Location <em>Location</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Location
	 * @generated
	 */
	public Adapter createLocationAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Location3D <em>Location3 D</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Location3D
	 * @generated
	 */
	public Adapter createLocation3DAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Marker <em>Marker</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Marker
	 * @generated
	 */
	public Adapter createMarkerAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.MultipleFill <em>Multiple Fill</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.MultipleFill
	 * @generated
	 */
	public Adapter createMultipleFillAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier <em>Number Format Specifier</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier
	 * @generated
	 */
	public Adapter createNumberFormatSpecifierAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Palette <em>Palette</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Palette
	 * @generated
	 */
	public Adapter createPaletteAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Rotation3D <em>Rotation3 D</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Rotation3D
	 * @generated
	 */
	public Adapter createRotation3DAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.ScriptValue <em>Script Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.ScriptValue
	 * @generated
	 */
	public Adapter createScriptValueAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.SeriesValue <em>Series Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.SeriesValue
	 * @generated
	 */
	public Adapter createSeriesValueAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Size <em>Size</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Size
	 * @generated
	 */
	public Adapter createSizeAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Style <em>Style</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Style
	 * @generated
	 */
	public Adapter createStyleAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.StyleMap <em>Style Map</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.StyleMap
	 * @generated
	 */
	public Adapter createStyleMapAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.Text <em>Text</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.Text
	 * @generated
	 */
	public Adapter createTextAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.TextAlignment <em>Text Alignment</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.TextAlignment
	 * @generated
	 */
	public Adapter createTextAlignmentAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.TooltipValue <em>Tooltip Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.TooltipValue
	 * @generated
	 */
	public Adapter createTooltipValueAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.attribute.URLValue <em>URL Value</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue
	 * @generated
	 */
	public Adapter createURLValueAdapter( )
	{
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc --> This
	 * default implementation returns null. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter( )
	{
		return null;
	}

} // AttributeAdapterFactory
