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

import org.eclipse.birt.chart.model.attribute.*;

import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ChartType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.Polygon;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.Rotation3D;
import org.eclipse.birt.chart.model.attribute.RuleType;
import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.SeriesValue;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory </b>. <!--
 * end-user-doc -->
 * @generated
 */
public class AttributeFactoryImpl extends EFactoryImpl implements
		AttributeFactory
{

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public AttributeFactoryImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create( EClass eClass )
	{
		switch ( eClass.getClassifierID( ) )
		{
			case AttributePackage.ACTION_VALUE :
				return createActionValue( );
			case AttributePackage.ANGLE3_D :
				return createAngle3D( );
			case AttributePackage.AXIS_ORIGIN :
				return createAxisOrigin( );
			case AttributePackage.BOUNDS :
				return createBounds( );
			case AttributePackage.CALL_BACK_VALUE :
				return createCallBackValue( );
			case AttributePackage.COLOR_DEFINITION :
				return createColorDefinition( );
			case AttributePackage.DATA_POINT :
				return createDataPoint( );
			case AttributePackage.DATA_POINT_COMPONENT :
				return createDataPointComponent( );
			case AttributePackage.DATE_FORMAT_SPECIFIER :
				return createDateFormatSpecifier( );
			case AttributePackage.EMBEDDED_IMAGE :
				return createEmbeddedImage( );
			case AttributePackage.EXTENDED_PROPERTY :
				return createExtendedProperty( );
			case AttributePackage.FILL :
				return createFill( );
			case AttributePackage.FONT_DEFINITION :
				return createFontDefinition( );
			case AttributePackage.FORMAT_SPECIFIER :
				return createFormatSpecifier( );
			case AttributePackage.GRADIENT :
				return createGradient( );
			case AttributePackage.IMAGE :
				return createImage( );
			case AttributePackage.INSETS :
				return createInsets( );
			case AttributePackage.INTERACTIVITY :
				return createInteractivity( );
			case AttributePackage.JAVA_DATE_FORMAT_SPECIFIER :
				return createJavaDateFormatSpecifier( );
			case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER :
				return createJavaNumberFormatSpecifier( );
			case AttributePackage.LINE_ATTRIBUTES :
				return createLineAttributes( );
			case AttributePackage.LOCATION :
				return createLocation( );
			case AttributePackage.LOCATION3_D :
				return createLocation3D( );
			case AttributePackage.MARKER :
				return createMarker( );
			case AttributePackage.NUMBER_FORMAT_SPECIFIER :
				return createNumberFormatSpecifier( );
			case AttributePackage.PALETTE :
				return createPalette( );
			case AttributePackage.POLYGON :
				return createPolygon( );
			case AttributePackage.ROTATION3_D :
				return createRotation3D( );
			case AttributePackage.SCRIPT_VALUE :
				return createScriptValue( );
			case AttributePackage.SERIES_VALUE :
				return createSeriesValue( );
			case AttributePackage.SIZE :
				return createSize( );
			case AttributePackage.STYLE :
				return createStyle( );
			case AttributePackage.STYLE_MAP :
				return createStyleMap( );
			case AttributePackage.TEXT :
				return createText( );
			case AttributePackage.TEXT_ALIGNMENT :
				return createTextAlignment( );
			case AttributePackage.TOOLTIP_VALUE :
				return createTooltipValue( );
			case AttributePackage.URL_VALUE :
				return createURLValue( );
			default :
				throw new IllegalArgumentException( "The class '" + eClass.getName( ) + "' is not a valid classifier" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString( EDataType eDataType, String initialValue )
	{
		switch ( eDataType.getClassifierID( ) )
		{
			case AttributePackage.ACTION_TYPE :
			{
				ActionType result = ActionType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.ANCHOR :
			{
				Anchor result = Anchor.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.ANGLE_TYPE :
			{
				AngleType result = AngleType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.AXIS_TYPE :
			{
				AxisType result = AxisType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.CHART_DIMENSION :
			{
				ChartDimension result = ChartDimension.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.CHART_TYPE :
			{
				ChartType result = ChartType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.DATA_POINT_COMPONENT_TYPE :
			{
				DataPointComponentType result = DataPointComponentType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.DATA_TYPE :
			{
				DataType result = DataType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.DATE_FORMAT_DETAIL :
			{
				DateFormatDetail result = DateFormatDetail.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.DATE_FORMAT_TYPE :
			{
				DateFormatType result = DateFormatType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.DIRECTION :
			{
				Direction result = Direction.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.GROUPING_UNIT_TYPE :
			{
				GroupingUnitType result = GroupingUnitType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.HORIZONTAL_ALIGNMENT :
			{
				HorizontalAlignment result = HorizontalAlignment.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.INTERSECTION_TYPE :
			{
				IntersectionType result = IntersectionType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.LEADER_LINE_STYLE :
			{
				LeaderLineStyle result = LeaderLineStyle.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.LEGEND_BEHAVIOR_TYPE :
			{
				LegendBehaviorType result = LegendBehaviorType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.LEGEND_ITEM_TYPE :
			{
				LegendItemType result = LegendItemType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.LINE_DECORATOR :
			{
				LineDecorator result = LineDecorator.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.LINE_STYLE :
			{
				LineStyle result = LineStyle.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.MARKER_TYPE :
			{
				MarkerType result = MarkerType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.ORIENTATION :
			{
				Orientation result = Orientation.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.POSITION :
			{
				Position result = Position.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.RISER_TYPE :
			{
				RiserType result = RiserType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.RULE_TYPE :
			{
				RuleType result = RuleType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.SCALE_UNIT_TYPE :
			{
				ScaleUnitType result = ScaleUnitType.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.SORT_OPTION :
			{
				SortOption result = SortOption.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.STRETCH :
			{
				Stretch result = Stretch.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.STYLED_COMPONENT :
			{
				StyledComponent result = StyledComponent.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.TICK_STYLE :
			{
				TickStyle result = TickStyle.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.TRIGGER_CONDITION :
			{
				TriggerCondition result = TriggerCondition.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.TRIGGER_FLOW :
			{
				TriggerFlow result = TriggerFlow.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.UNITS_OF_MEASUREMENT :
			{
				UnitsOfMeasurement result = UnitsOfMeasurement.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.VERTICAL_ALIGNMENT :
			{
				VerticalAlignment result = VerticalAlignment.get( initialValue );
				if ( result == null )
					throw new IllegalArgumentException( "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			}
			case AttributePackage.ACTION_TYPE_OBJECT :
				return createActionTypeObjectFromString( eDataType,
						initialValue );
			case AttributePackage.ANCHOR_OBJECT :
				return createAnchorObjectFromString( eDataType, initialValue );
			case AttributePackage.ANGLE_TYPE_OBJECT :
				return createAngleTypeObjectFromString( eDataType, initialValue );
			case AttributePackage.AXIS_TYPE_OBJECT :
				return createAxisTypeObjectFromString( eDataType, initialValue );
			case AttributePackage.CHART_DIMENSION_OBJECT :
				return createChartDimensionObjectFromString( eDataType,
						initialValue );
			case AttributePackage.CHART_TYPE_OBJECT :
				return createChartTypeObjectFromString( eDataType, initialValue );
			case AttributePackage.DATA_POINT_COMPONENT_TYPE_OBJECT :
				return createDataPointComponentTypeObjectFromString( eDataType,
						initialValue );
			case AttributePackage.DATA_TYPE_OBJECT :
				return createDataTypeObjectFromString( eDataType, initialValue );
			case AttributePackage.DATE_FORMAT_DETAIL_OBJECT :
				return createDateFormatDetailObjectFromString( eDataType,
						initialValue );
			case AttributePackage.DATE_FORMAT_TYPE_OBJECT :
				return createDateFormatTypeObjectFromString( eDataType,
						initialValue );
			case AttributePackage.DIRECTION_OBJECT :
				return createDirectionObjectFromString( eDataType, initialValue );
			case AttributePackage.GROUPING_UNIT_TYPE_OBJECT :
				return createGroupingUnitTypeObjectFromString( eDataType,
						initialValue );
			case AttributePackage.HORIZONTAL_ALIGNMENT_OBJECT :
				return createHorizontalAlignmentObjectFromString( eDataType,
						initialValue );
			case AttributePackage.ID :
				return createIDFromString( eDataType, initialValue );
			case AttributePackage.INTERSECTION_TYPE_OBJECT :
				return createIntersectionTypeObjectFromString( eDataType,
						initialValue );
			case AttributePackage.LEADER_LINE_STYLE_OBJECT :
				return createLeaderLineStyleObjectFromString( eDataType,
						initialValue );
			case AttributePackage.LEGEND_BEHAVIOR_TYPE_OBJECT :
				return createLegendBehaviorTypeObjectFromString( eDataType,
						initialValue );
			case AttributePackage.LEGEND_ITEM_TYPE_OBJECT :
				return createLegendItemTypeObjectFromString( eDataType,
						initialValue );
			case AttributePackage.LINE_DECORATOR_OBJECT :
				return createLineDecoratorObjectFromString( eDataType,
						initialValue );
			case AttributePackage.LINE_STYLE_OBJECT :
				return createLineStyleObjectFromString( eDataType, initialValue );
			case AttributePackage.MARKER_TYPE_OBJECT :
				return createMarkerTypeObjectFromString( eDataType,
						initialValue );
			case AttributePackage.ORIENTATION_OBJECT :
				return createOrientationObjectFromString( eDataType,
						initialValue );
			case AttributePackage.PERCENTAGE :
				return createPercentageFromString( eDataType, initialValue );
			case AttributePackage.PERCENTAGE_OBJECT :
				return createPercentageObjectFromString( eDataType,
						initialValue );
			case AttributePackage.POSITION_OBJECT :
				return createPositionObjectFromString( eDataType, initialValue );
			case AttributePackage.RGB_VALUE :
				return createRGBValueFromString( eDataType, initialValue );
			case AttributePackage.RGB_VALUE_OBJECT :
				return createRGBValueObjectFromString( eDataType, initialValue );
			case AttributePackage.RISER_TYPE_OBJECT :
				return createRiserTypeObjectFromString( eDataType, initialValue );
			case AttributePackage.RULE_TYPE_OBJECT :
				return createRuleTypeObjectFromString( eDataType, initialValue );
			case AttributePackage.SCALE_UNIT_TYPE_OBJECT :
				return createScaleUnitTypeObjectFromString( eDataType,
						initialValue );
			case AttributePackage.SORT_OPTION_OBJECT :
				return createSortOptionObjectFromString( eDataType,
						initialValue );
			case AttributePackage.STRETCH_OBJECT :
				return createStretchObjectFromString( eDataType, initialValue );
			case AttributePackage.STYLED_COMPONENT_OBJECT :
				return createStyledComponentObjectFromString( eDataType,
						initialValue );
			case AttributePackage.TICK_STYLE_OBJECT :
				return createTickStyleObjectFromString( eDataType, initialValue );
			case AttributePackage.TRIGGER_CONDITION_OBJECT :
				return createTriggerConditionObjectFromString( eDataType,
						initialValue );
			case AttributePackage.TRIGGER_FLOW_OBJECT :
				return createTriggerFlowObjectFromString( eDataType,
						initialValue );
			case AttributePackage.UNITS_OF_MEASUREMENT_OBJECT :
				return createUnitsOfMeasurementObjectFromString( eDataType,
						initialValue );
			case AttributePackage.VERTICAL_ALIGNMENT_OBJECT :
				return createVerticalAlignmentObjectFromString( eDataType,
						initialValue );
			default :
				throw new IllegalArgumentException( "The datatype '" + eDataType.getName( ) + "' is not a valid classifier" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString( EDataType eDataType, Object instanceValue )
	{
		switch ( eDataType.getClassifierID( ) )
		{
			case AttributePackage.ACTION_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.ANCHOR :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.ANGLE_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.AXIS_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.CHART_DIMENSION :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.CHART_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.DATA_POINT_COMPONENT_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.DATA_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.DATE_FORMAT_DETAIL :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.DATE_FORMAT_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.DIRECTION :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.GROUPING_UNIT_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.HORIZONTAL_ALIGNMENT :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.INTERSECTION_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.LEADER_LINE_STYLE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.LEGEND_BEHAVIOR_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.LEGEND_ITEM_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.LINE_DECORATOR :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.LINE_STYLE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.MARKER_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.ORIENTATION :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.POSITION :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.RISER_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.RULE_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.SCALE_UNIT_TYPE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.SORT_OPTION :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.STRETCH :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.STYLED_COMPONENT :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.TICK_STYLE :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.TRIGGER_CONDITION :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.TRIGGER_FLOW :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.UNITS_OF_MEASUREMENT :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.VERTICAL_ALIGNMENT :
				return instanceValue == null ? null : instanceValue.toString( );
			case AttributePackage.ACTION_TYPE_OBJECT :
				return convertActionTypeObjectToString( eDataType,
						instanceValue );
			case AttributePackage.ANCHOR_OBJECT :
				return convertAnchorObjectToString( eDataType, instanceValue );
			case AttributePackage.ANGLE_TYPE_OBJECT :
				return convertAngleTypeObjectToString( eDataType, instanceValue );
			case AttributePackage.AXIS_TYPE_OBJECT :
				return convertAxisTypeObjectToString( eDataType, instanceValue );
			case AttributePackage.CHART_DIMENSION_OBJECT :
				return convertChartDimensionObjectToString( eDataType,
						instanceValue );
			case AttributePackage.CHART_TYPE_OBJECT :
				return convertChartTypeObjectToString( eDataType, instanceValue );
			case AttributePackage.DATA_POINT_COMPONENT_TYPE_OBJECT :
				return convertDataPointComponentTypeObjectToString( eDataType,
						instanceValue );
			case AttributePackage.DATA_TYPE_OBJECT :
				return convertDataTypeObjectToString( eDataType, instanceValue );
			case AttributePackage.DATE_FORMAT_DETAIL_OBJECT :
				return convertDateFormatDetailObjectToString( eDataType,
						instanceValue );
			case AttributePackage.DATE_FORMAT_TYPE_OBJECT :
				return convertDateFormatTypeObjectToString( eDataType,
						instanceValue );
			case AttributePackage.DIRECTION_OBJECT :
				return convertDirectionObjectToString( eDataType, instanceValue );
			case AttributePackage.GROUPING_UNIT_TYPE_OBJECT :
				return convertGroupingUnitTypeObjectToString( eDataType,
						instanceValue );
			case AttributePackage.HORIZONTAL_ALIGNMENT_OBJECT :
				return convertHorizontalAlignmentObjectToString( eDataType,
						instanceValue );
			case AttributePackage.ID :
				return convertIDToString( eDataType, instanceValue );
			case AttributePackage.INTERSECTION_TYPE_OBJECT :
				return convertIntersectionTypeObjectToString( eDataType,
						instanceValue );
			case AttributePackage.LEADER_LINE_STYLE_OBJECT :
				return convertLeaderLineStyleObjectToString( eDataType,
						instanceValue );
			case AttributePackage.LEGEND_BEHAVIOR_TYPE_OBJECT :
				return convertLegendBehaviorTypeObjectToString( eDataType,
						instanceValue );
			case AttributePackage.LEGEND_ITEM_TYPE_OBJECT :
				return convertLegendItemTypeObjectToString( eDataType,
						instanceValue );
			case AttributePackage.LINE_DECORATOR_OBJECT :
				return convertLineDecoratorObjectToString( eDataType,
						instanceValue );
			case AttributePackage.LINE_STYLE_OBJECT :
				return convertLineStyleObjectToString( eDataType, instanceValue );
			case AttributePackage.MARKER_TYPE_OBJECT :
				return convertMarkerTypeObjectToString( eDataType,
						instanceValue );
			case AttributePackage.ORIENTATION_OBJECT :
				return convertOrientationObjectToString( eDataType,
						instanceValue );
			case AttributePackage.PERCENTAGE :
				return convertPercentageToString( eDataType, instanceValue );
			case AttributePackage.PERCENTAGE_OBJECT :
				return convertPercentageObjectToString( eDataType,
						instanceValue );
			case AttributePackage.POSITION_OBJECT :
				return convertPositionObjectToString( eDataType, instanceValue );
			case AttributePackage.RGB_VALUE :
				return convertRGBValueToString( eDataType, instanceValue );
			case AttributePackage.RGB_VALUE_OBJECT :
				return convertRGBValueObjectToString( eDataType, instanceValue );
			case AttributePackage.RISER_TYPE_OBJECT :
				return convertRiserTypeObjectToString( eDataType, instanceValue );
			case AttributePackage.RULE_TYPE_OBJECT :
				return convertRuleTypeObjectToString( eDataType, instanceValue );
			case AttributePackage.SCALE_UNIT_TYPE_OBJECT :
				return convertScaleUnitTypeObjectToString( eDataType,
						instanceValue );
			case AttributePackage.SORT_OPTION_OBJECT :
				return convertSortOptionObjectToString( eDataType,
						instanceValue );
			case AttributePackage.STRETCH_OBJECT :
				return convertStretchObjectToString( eDataType, instanceValue );
			case AttributePackage.STYLED_COMPONENT_OBJECT :
				return convertStyledComponentObjectToString( eDataType,
						instanceValue );
			case AttributePackage.TICK_STYLE_OBJECT :
				return convertTickStyleObjectToString( eDataType, instanceValue );
			case AttributePackage.TRIGGER_CONDITION_OBJECT :
				return convertTriggerConditionObjectToString( eDataType,
						instanceValue );
			case AttributePackage.TRIGGER_FLOW_OBJECT :
				return convertTriggerFlowObjectToString( eDataType,
						instanceValue );
			case AttributePackage.UNITS_OF_MEASUREMENT_OBJECT :
				return convertUnitsOfMeasurementObjectToString( eDataType,
						instanceValue );
			case AttributePackage.VERTICAL_ALIGNMENT_OBJECT :
				return convertVerticalAlignmentObjectToString( eDataType,
						instanceValue );
			default :
				throw new IllegalArgumentException( "The datatype '" + eDataType.getName( ) + "' is not a valid classifier" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ActionValue createActionValue( )
	{
		ActionValueImpl actionValue = new ActionValueImpl( );
		return actionValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Angle3D createAngle3D( )
	{
		Angle3DImpl angle3D = new Angle3DImpl( );
		return angle3D;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AxisOrigin createAxisOrigin( )
	{
		AxisOriginImpl axisOrigin = new AxisOriginImpl( );
		return axisOrigin;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Bounds createBounds( )
	{
		BoundsImpl bounds = new BoundsImpl( );
		return bounds;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CallBackValue createCallBackValue( )
	{
		CallBackValueImpl callBackValue = new CallBackValueImpl( );
		return callBackValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ColorDefinition createColorDefinition( )
	{
		ColorDefinitionImpl colorDefinition = new ColorDefinitionImpl( );
		return colorDefinition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataPoint createDataPoint( )
	{
		DataPointImpl dataPoint = new DataPointImpl( );
		return dataPoint;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataPointComponent createDataPointComponent( )
	{
		DataPointComponentImpl dataPointComponent = new DataPointComponentImpl( );
		return dataPointComponent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DateFormatSpecifier createDateFormatSpecifier( )
	{
		DateFormatSpecifierImpl dateFormatSpecifier = new DateFormatSpecifierImpl( );
		return dateFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmbeddedImage createEmbeddedImage( )
	{
		EmbeddedImageImpl embeddedImage = new EmbeddedImageImpl( );
		return embeddedImage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ExtendedProperty createExtendedProperty( )
	{
		ExtendedPropertyImpl extendedProperty = new ExtendedPropertyImpl( );
		return extendedProperty;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Fill createFill( )
	{
		FillImpl fill = new FillImpl( );
		return fill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public FontDefinition createFontDefinition( )
	{
		FontDefinitionImpl fontDefinition = new FontDefinitionImpl( );
		return fontDefinition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public FormatSpecifier createFormatSpecifier( )
	{
		FormatSpecifierImpl formatSpecifier = new FormatSpecifierImpl( );
		return formatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Gradient createGradient( )
	{
		GradientImpl gradient = new GradientImpl( );
		return gradient;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Image createImage( )
	{
		ImageImpl image = new ImageImpl( );
		return image;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Insets createInsets( )
	{
		InsetsImpl insets = new InsetsImpl( );
		return insets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Interactivity createInteractivity( )
	{
		InteractivityImpl interactivity = new InteractivityImpl( );
		return interactivity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public JavaDateFormatSpecifier createJavaDateFormatSpecifier( )
	{
		JavaDateFormatSpecifierImpl javaDateFormatSpecifier = new JavaDateFormatSpecifierImpl( );
		return javaDateFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public JavaNumberFormatSpecifier createJavaNumberFormatSpecifier( )
	{
		JavaNumberFormatSpecifierImpl javaNumberFormatSpecifier = new JavaNumberFormatSpecifierImpl( );
		return javaNumberFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public LineAttributes createLineAttributes( )
	{
		LineAttributesImpl lineAttributes = new LineAttributesImpl( );
		return lineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Location createLocation( )
	{
		LocationImpl location = new LocationImpl( );
		return location;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Location3D createLocation3D( )
	{
		Location3DImpl location3D = new Location3DImpl( );
		return location3D;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Marker createMarker( )
	{
		MarkerImpl marker = new MarkerImpl( );
		return marker;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NumberFormatSpecifier createNumberFormatSpecifier( )
	{
		NumberFormatSpecifierImpl numberFormatSpecifier = new NumberFormatSpecifierImpl( );
		return numberFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Palette createPalette( )
	{
		PaletteImpl palette = new PaletteImpl( );
		return palette;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Polygon createPolygon( )
	{
		PolygonImpl polygon = new PolygonImpl( );
		return polygon;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rotation3D createRotation3D( )
	{
		Rotation3DImpl rotation3D = new Rotation3DImpl( );
		return rotation3D;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ScriptValue createScriptValue( )
	{
		ScriptValueImpl scriptValue = new ScriptValueImpl( );
		return scriptValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public SeriesValue createSeriesValue( )
	{
		SeriesValueImpl seriesValue = new SeriesValueImpl( );
		return seriesValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Size createSize( )
	{
		SizeImpl size = new SizeImpl( );
		return size;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Style createStyle( )
	{
		StyleImpl style = new StyleImpl( );
		return style;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public StyleMap createStyleMap( )
	{
		StyleMapImpl styleMap = new StyleMapImpl( );
		return styleMap;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Text createText( )
	{
		TextImpl text = new TextImpl( );
		return text;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public TextAlignment createTextAlignment( )
	{
		TextAlignmentImpl textAlignment = new TextAlignmentImpl( );
		return textAlignment;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public TooltipValue createTooltipValue( )
	{
		TooltipValueImpl tooltipValue = new TooltipValueImpl( );
		return tooltipValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public URLValue createURLValue( )
	{
		URLValueImpl urlValue = new URLValueImpl( );
		return urlValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ActionType createActionTypeObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (ActionType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getActionType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertActionTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getActionType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Anchor createAnchorObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (Anchor) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getAnchor( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAnchorObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getAnchor( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AngleType createAngleTypeObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (AngleType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getAngleType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAngleTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getAngleType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AxisType createAxisTypeObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (AxisType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getAxisType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAxisTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getAxisType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ChartDimension createChartDimensionObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (ChartDimension) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getChartDimension( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChartDimensionObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getChartDimension( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ChartType createChartTypeObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (ChartType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getChartType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChartTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getChartType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataPointComponentType createDataPointComponentTypeObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (DataPointComponentType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getDataPointComponentType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDataPointComponentTypeObjectToString(
			EDataType eDataType, Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getDataPointComponentType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataType createDataTypeObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (DataType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getDataType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDataTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getDataType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DateFormatDetail createDateFormatDetailObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (DateFormatDetail) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getDateFormatDetail( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDateFormatDetailObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getDateFormatDetail( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DateFormatType createDateFormatTypeObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (DateFormatType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getDateFormatType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDateFormatTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getDateFormatType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Direction createDirectionObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (Direction) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getDirection( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDirectionObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getDirection( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public GroupingUnitType createGroupingUnitTypeObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (GroupingUnitType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getGroupingUnitType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertGroupingUnitTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getGroupingUnitType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public HorizontalAlignment createHorizontalAlignmentObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (HorizontalAlignment) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getHorizontalAlignment( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertHorizontalAlignmentObjectToString(
			EDataType eDataType, Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getHorizontalAlignment( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String createIDFromString( EDataType eDataType, String initialValue )
	{
		return (String) XMLTypeFactory.eINSTANCE.createFromString( XMLTypePackage.eINSTANCE.getString( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIDToString( EDataType eDataType, Object instanceValue )
	{
		return XMLTypeFactory.eINSTANCE.convertToString( XMLTypePackage.eINSTANCE.getString( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public IntersectionType createIntersectionTypeObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (IntersectionType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getIntersectionType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIntersectionTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getIntersectionType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public LeaderLineStyle createLeaderLineStyleObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (LeaderLineStyle) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getLeaderLineStyle( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLeaderLineStyleObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getLeaderLineStyle( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LegendBehaviorType createLegendBehaviorTypeObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (LegendBehaviorType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getLegendBehaviorType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLegendBehaviorTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getLegendBehaviorType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public LegendItemType createLegendItemTypeObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (LegendItemType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getLegendItemType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLegendItemTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getLegendItemType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public LineDecorator createLineDecoratorObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (LineDecorator) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getLineDecorator( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLineDecoratorObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getLineDecorator( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public LineStyle createLineStyleObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (LineStyle) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getLineStyle( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLineStyleObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getLineStyle( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public MarkerType createMarkerTypeObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (MarkerType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getMarkerType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMarkerTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getMarkerType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Orientation createOrientationObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (Orientation) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getOrientation( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOrientationObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getOrientation( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Double createPercentageFromString( EDataType eDataType,
			String initialValue )
	{
		return (Double) XMLTypeFactory.eINSTANCE.createFromString( XMLTypePackage.eINSTANCE.getDouble( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPercentageToString( EDataType eDataType,
			Object instanceValue )
	{
		return XMLTypeFactory.eINSTANCE.convertToString( XMLTypePackage.eINSTANCE.getDouble( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Double createPercentageObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (Double) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getPercentage( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPercentageObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getPercentage( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Position createPositionObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (Position) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getPosition( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPositionObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getPosition( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createRGBValueFromString( EDataType eDataType,
			String initialValue )
	{
		return (Integer) XMLTypeFactory.eINSTANCE.createFromString( XMLTypePackage.eINSTANCE.getInt( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRGBValueToString( EDataType eDataType,
			Object instanceValue )
	{
		return XMLTypeFactory.eINSTANCE.convertToString( XMLTypePackage.eINSTANCE.getInt( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createRGBValueObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (Integer) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getRGBValue( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRGBValueObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getRGBValue( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RiserType createRiserTypeObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (RiserType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getRiserType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRiserTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getRiserType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RuleType createRuleTypeObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (RuleType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getRuleType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRuleTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getRuleType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ScaleUnitType createScaleUnitTypeObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (ScaleUnitType) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getScaleUnitType( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertScaleUnitTypeObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getScaleUnitType( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public SortOption createSortOptionObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (SortOption) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getSortOption( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSortOptionObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getSortOption( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Stretch createStretchObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (Stretch) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getStretch( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertStretchObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getStretch( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public StyledComponent createStyledComponentObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (StyledComponent) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getStyledComponent( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertStyledComponentObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getStyledComponent( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public TickStyle createTickStyleObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (TickStyle) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getTickStyle( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTickStyleObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getTickStyle( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public TriggerCondition createTriggerConditionObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (TriggerCondition) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getTriggerCondition( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTriggerConditionObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getTriggerCondition( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TriggerFlow createTriggerFlowObjectFromString( EDataType eDataType,
			String initialValue )
	{
		return (TriggerFlow) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getTriggerFlow( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTriggerFlowObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getTriggerFlow( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UnitsOfMeasurement createUnitsOfMeasurementObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (UnitsOfMeasurement) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getUnitsOfMeasurement( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertUnitsOfMeasurementObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getUnitsOfMeasurement( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public VerticalAlignment createVerticalAlignmentObjectFromString(
			EDataType eDataType, String initialValue )
	{
		return (VerticalAlignment) AttributeFactory.eINSTANCE.createFromString( AttributePackage.eINSTANCE.getVerticalAlignment( ),
				initialValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVerticalAlignmentObjectToString( EDataType eDataType,
			Object instanceValue )
	{
		return AttributeFactory.eINSTANCE.convertToString( AttributePackage.eINSTANCE.getVerticalAlignment( ),
				instanceValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AttributePackage getAttributePackage( )
	{
		return (AttributePackage) getEPackage( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static AttributePackage getPackage( )
	{
		return AttributePackage.eINSTANCE;
	}

} // AttributeFactoryImpl
