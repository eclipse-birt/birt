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

import java.util.Map;

import org.eclipse.birt.chart.model.attribute.AccessibilityValue;
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
import org.eclipse.birt.chart.model.attribute.CallBackValue;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ChartType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.ImageSourceType;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.MenuStylesKeyType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.PatternImage;
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
import org.eclipse.birt.chart.model.attribute.StringFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.TriggerFlow;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory </b>. <!--
 * end-user-doc -->
 *
 * @generated
 */
public class AttributeFactoryImpl extends EFactoryImpl implements AttributeFactory {

	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 */
	public static AttributeFactory init() {
		try {
			AttributeFactory theAttributeFactory = (AttributeFactory) EPackage.Registry.INSTANCE
					.getEFactory("http://www.birt.eclipse.org/ChartModelAttribute"); //$NON-NLS-1$
			if (theAttributeFactory != null) {
				return theAttributeFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AttributeFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @generated
	 */
	public AttributeFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case AttributePackage.ACCESSIBILITY_VALUE:
			return (EObject) createAccessibilityValue();
		case AttributePackage.ACTION_VALUE:
			return (EObject) createActionValue();
		case AttributePackage.ANGLE3_D:
			return (EObject) createAngle3D();
		case AttributePackage.AXIS_ORIGIN:
			return (EObject) createAxisOrigin();
		case AttributePackage.BOUNDS:
			return (EObject) createBounds();
		case AttributePackage.CALL_BACK_VALUE:
			return (EObject) createCallBackValue();
		case AttributePackage.COLOR_DEFINITION:
			return (EObject) createColorDefinition();
		case AttributePackage.CURSOR:
			return (EObject) createCursor();
		case AttributePackage.DATA_POINT:
			return (EObject) createDataPoint();
		case AttributePackage.DATA_POINT_COMPONENT:
			return (EObject) createDataPointComponent();
		case AttributePackage.DATE_FORMAT_SPECIFIER:
			return (EObject) createDateFormatSpecifier();
		case AttributePackage.EMBEDDED_IMAGE:
			return (EObject) createEmbeddedImage();
		case AttributePackage.ESTRING_TO_STRING_MAP_ENTRY:
			return (EObject) createEStringToStringMapEntry();
		case AttributePackage.EXTENDED_PROPERTY:
			return (EObject) createExtendedProperty();
		case AttributePackage.FILL:
			return (EObject) createFill();
		case AttributePackage.FONT_DEFINITION:
			return (EObject) createFontDefinition();
		case AttributePackage.FORMAT_SPECIFIER:
			return (EObject) createFormatSpecifier();
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER:
			return (EObject) createFractionNumberFormatSpecifier();
		case AttributePackage.GRADIENT:
			return (EObject) createGradient();
		case AttributePackage.IMAGE:
			return (EObject) createImage();
		case AttributePackage.INSETS:
			return (EObject) createInsets();
		case AttributePackage.INTERACTIVITY:
			return (EObject) createInteractivity();
		case AttributePackage.JAVA_DATE_FORMAT_SPECIFIER:
			return (EObject) createJavaDateFormatSpecifier();
		case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER:
			return (EObject) createJavaNumberFormatSpecifier();
		case AttributePackage.LINE_ATTRIBUTES:
			return (EObject) createLineAttributes();
		case AttributePackage.LOCATION:
			return (EObject) createLocation();
		case AttributePackage.LOCATION3_D:
			return (EObject) createLocation3D();
		case AttributePackage.MARKER:
			return (EObject) createMarker();
		case AttributePackage.MULTIPLE_FILL:
			return (EObject) createMultipleFill();
		case AttributePackage.MULTI_URL_VALUES:
			return (EObject) createMultiURLValues();
		case AttributePackage.NUMBER_FORMAT_SPECIFIER:
			return (EObject) createNumberFormatSpecifier();
		case AttributePackage.PALETTE:
			return (EObject) createPalette();
		case AttributePackage.PATTERN_IMAGE:
			return (EObject) createPatternImage();
		case AttributePackage.ROTATION3_D:
			return (EObject) createRotation3D();
		case AttributePackage.SCRIPT_VALUE:
			return (EObject) createScriptValue();
		case AttributePackage.SERIES_VALUE:
			return (EObject) createSeriesValue();
		case AttributePackage.SIZE:
			return (EObject) createSize();
		case AttributePackage.STRING_FORMAT_SPECIFIER:
			return (EObject) createStringFormatSpecifier();
		case AttributePackage.STYLE:
			return (EObject) createStyle();
		case AttributePackage.STYLE_MAP:
			return (EObject) createStyleMap();
		case AttributePackage.TEXT:
			return (EObject) createText();
		case AttributePackage.TEXT_ALIGNMENT:
			return (EObject) createTextAlignment();
		case AttributePackage.TOOLTIP_VALUE:
			return (EObject) createTooltipValue();
		case AttributePackage.URL_VALUE:
			return (EObject) createURLValue();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
		case AttributePackage.ACTION_TYPE:
			return createActionTypeFromString(eDataType, initialValue);
		case AttributePackage.ANCHOR:
			return createAnchorFromString(eDataType, initialValue);
		case AttributePackage.ANGLE_TYPE:
			return createAngleTypeFromString(eDataType, initialValue);
		case AttributePackage.AXIS_TYPE:
			return createAxisTypeFromString(eDataType, initialValue);
		case AttributePackage.CHART_DIMENSION:
			return createChartDimensionFromString(eDataType, initialValue);
		case AttributePackage.CHART_TYPE:
			return createChartTypeFromString(eDataType, initialValue);
		case AttributePackage.CURSOR_TYPE:
			return createCursorTypeFromString(eDataType, initialValue);
		case AttributePackage.DATA_POINT_COMPONENT_TYPE:
			return createDataPointComponentTypeFromString(eDataType, initialValue);
		case AttributePackage.DATA_TYPE:
			return createDataTypeFromString(eDataType, initialValue);
		case AttributePackage.DATE_FORMAT_DETAIL:
			return createDateFormatDetailFromString(eDataType, initialValue);
		case AttributePackage.DATE_FORMAT_TYPE:
			return createDateFormatTypeFromString(eDataType, initialValue);
		case AttributePackage.DIRECTION:
			return createDirectionFromString(eDataType, initialValue);
		case AttributePackage.GROUPING_UNIT_TYPE:
			return createGroupingUnitTypeFromString(eDataType, initialValue);
		case AttributePackage.HORIZONTAL_ALIGNMENT:
			return createHorizontalAlignmentFromString(eDataType, initialValue);
		case AttributePackage.IMAGE_SOURCE_TYPE:
			return createImageSourceTypeFromString(eDataType, initialValue);
		case AttributePackage.INTERSECTION_TYPE:
			return createIntersectionTypeFromString(eDataType, initialValue);
		case AttributePackage.LEADER_LINE_STYLE:
			return createLeaderLineStyleFromString(eDataType, initialValue);
		case AttributePackage.LEGEND_BEHAVIOR_TYPE:
			return createLegendBehaviorTypeFromString(eDataType, initialValue);
		case AttributePackage.LEGEND_ITEM_TYPE:
			return createLegendItemTypeFromString(eDataType, initialValue);
		case AttributePackage.LINE_DECORATOR:
			return createLineDecoratorFromString(eDataType, initialValue);
		case AttributePackage.LINE_STYLE:
			return createLineStyleFromString(eDataType, initialValue);
		case AttributePackage.MARKER_TYPE:
			return createMarkerTypeFromString(eDataType, initialValue);
		case AttributePackage.MENU_STYLES_KEY_TYPE:
			return createMenuStylesKeyTypeFromString(eDataType, initialValue);
		case AttributePackage.ORIENTATION:
			return createOrientationFromString(eDataType, initialValue);
		case AttributePackage.POSITION:
			return createPositionFromString(eDataType, initialValue);
		case AttributePackage.RISER_TYPE:
			return createRiserTypeFromString(eDataType, initialValue);
		case AttributePackage.RULE_TYPE:
			return createRuleTypeFromString(eDataType, initialValue);
		case AttributePackage.SCALE_UNIT_TYPE:
			return createScaleUnitTypeFromString(eDataType, initialValue);
		case AttributePackage.SORT_OPTION:
			return createSortOptionFromString(eDataType, initialValue);
		case AttributePackage.STRETCH:
			return createStretchFromString(eDataType, initialValue);
		case AttributePackage.STYLED_COMPONENT:
			return createStyledComponentFromString(eDataType, initialValue);
		case AttributePackage.TICK_STYLE:
			return createTickStyleFromString(eDataType, initialValue);
		case AttributePackage.TRIGGER_CONDITION:
			return createTriggerConditionFromString(eDataType, initialValue);
		case AttributePackage.TRIGGER_FLOW:
			return createTriggerFlowFromString(eDataType, initialValue);
		case AttributePackage.UNITS_OF_MEASUREMENT:
			return createUnitsOfMeasurementFromString(eDataType, initialValue);
		case AttributePackage.VERTICAL_ALIGNMENT:
			return createVerticalAlignmentFromString(eDataType, initialValue);
		case AttributePackage.ACTION_TYPE_OBJECT:
			return createActionTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.ANCHOR_OBJECT:
			return createAnchorObjectFromString(eDataType, initialValue);
		case AttributePackage.ANGLE_TYPE_OBJECT:
			return createAngleTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.AXIS_TYPE_OBJECT:
			return createAxisTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.CHART_DIMENSION_OBJECT:
			return createChartDimensionObjectFromString(eDataType, initialValue);
		case AttributePackage.CHART_TYPE_OBJECT:
			return createChartTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.CURSOR_TYPE_OBJECT:
			return createCursorTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.DATA_POINT_COMPONENT_TYPE_OBJECT:
			return createDataPointComponentTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.DATA_TYPE_OBJECT:
			return createDataTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.DATE_FORMAT_DETAIL_OBJECT:
			return createDateFormatDetailObjectFromString(eDataType, initialValue);
		case AttributePackage.DATE_FORMAT_TYPE_OBJECT:
			return createDateFormatTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.DIRECTION_OBJECT:
			return createDirectionObjectFromString(eDataType, initialValue);
		case AttributePackage.GROUPING_UNIT_TYPE_OBJECT:
			return createGroupingUnitTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.HORIZONTAL_ALIGNMENT_OBJECT:
			return createHorizontalAlignmentObjectFromString(eDataType, initialValue);
		case AttributePackage.ID:
			return createIDFromString(eDataType, initialValue);
		case AttributePackage.IMAGE_SOURCE_TYPE_OBJECT:
			return createImageSourceTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.INTERSECTION_TYPE_OBJECT:
			return createIntersectionTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.LEADER_LINE_STYLE_OBJECT:
			return createLeaderLineStyleObjectFromString(eDataType, initialValue);
		case AttributePackage.LEGEND_BEHAVIOR_TYPE_OBJECT:
			return createLegendBehaviorTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.LEGEND_ITEM_TYPE_OBJECT:
			return createLegendItemTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.LINE_DECORATOR_OBJECT:
			return createLineDecoratorObjectFromString(eDataType, initialValue);
		case AttributePackage.LINE_STYLE_OBJECT:
			return createLineStyleObjectFromString(eDataType, initialValue);
		case AttributePackage.MARKER_TYPE_OBJECT:
			return createMarkerTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.MENU_STYLES_KEY_TYPE_OBJECT:
			return createMenuStylesKeyTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.ORIENTATION_OBJECT:
			return createOrientationObjectFromString(eDataType, initialValue);
		case AttributePackage.PATTERN_BITMAP:
			return createPatternBitmapFromString(eDataType, initialValue);
		case AttributePackage.PATTERN_BITMAP_OBJECT:
			return createPatternBitmapObjectFromString(eDataType, initialValue);
		case AttributePackage.PERCENTAGE:
			return createPercentageFromString(eDataType, initialValue);
		case AttributePackage.PERCENTAGE_OBJECT:
			return createPercentageObjectFromString(eDataType, initialValue);
		case AttributePackage.POSITION_OBJECT:
			return createPositionObjectFromString(eDataType, initialValue);
		case AttributePackage.RGB_VALUE:
			return createRGBValueFromString(eDataType, initialValue);
		case AttributePackage.RGB_VALUE_OBJECT:
			return createRGBValueObjectFromString(eDataType, initialValue);
		case AttributePackage.RISER_TYPE_OBJECT:
			return createRiserTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.RULE_TYPE_OBJECT:
			return createRuleTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.SCALE_UNIT_TYPE_OBJECT:
			return createScaleUnitTypeObjectFromString(eDataType, initialValue);
		case AttributePackage.SORT_OPTION_OBJECT:
			return createSortOptionObjectFromString(eDataType, initialValue);
		case AttributePackage.STRETCH_OBJECT:
			return createStretchObjectFromString(eDataType, initialValue);
		case AttributePackage.STYLED_COMPONENT_OBJECT:
			return createStyledComponentObjectFromString(eDataType, initialValue);
		case AttributePackage.TICK_STYLE_OBJECT:
			return createTickStyleObjectFromString(eDataType, initialValue);
		case AttributePackage.TRIGGER_CONDITION_OBJECT:
			return createTriggerConditionObjectFromString(eDataType, initialValue);
		case AttributePackage.TRIGGER_FLOW_OBJECT:
			return createTriggerFlowObjectFromString(eDataType, initialValue);
		case AttributePackage.UNITS_OF_MEASUREMENT_OBJECT:
			return createUnitsOfMeasurementObjectFromString(eDataType, initialValue);
		case AttributePackage.VERTICAL_ALIGNMENT_OBJECT:
			return createVerticalAlignmentObjectFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case AttributePackage.ACTION_TYPE:
			return convertActionTypeToString(eDataType, instanceValue);
		case AttributePackage.ANCHOR:
			return convertAnchorToString(eDataType, instanceValue);
		case AttributePackage.ANGLE_TYPE:
			return convertAngleTypeToString(eDataType, instanceValue);
		case AttributePackage.AXIS_TYPE:
			return convertAxisTypeToString(eDataType, instanceValue);
		case AttributePackage.CHART_DIMENSION:
			return convertChartDimensionToString(eDataType, instanceValue);
		case AttributePackage.CHART_TYPE:
			return convertChartTypeToString(eDataType, instanceValue);
		case AttributePackage.CURSOR_TYPE:
			return convertCursorTypeToString(eDataType, instanceValue);
		case AttributePackage.DATA_POINT_COMPONENT_TYPE:
			return convertDataPointComponentTypeToString(eDataType, instanceValue);
		case AttributePackage.DATA_TYPE:
			return convertDataTypeToString(eDataType, instanceValue);
		case AttributePackage.DATE_FORMAT_DETAIL:
			return convertDateFormatDetailToString(eDataType, instanceValue);
		case AttributePackage.DATE_FORMAT_TYPE:
			return convertDateFormatTypeToString(eDataType, instanceValue);
		case AttributePackage.DIRECTION:
			return convertDirectionToString(eDataType, instanceValue);
		case AttributePackage.GROUPING_UNIT_TYPE:
			return convertGroupingUnitTypeToString(eDataType, instanceValue);
		case AttributePackage.HORIZONTAL_ALIGNMENT:
			return convertHorizontalAlignmentToString(eDataType, instanceValue);
		case AttributePackage.IMAGE_SOURCE_TYPE:
			return convertImageSourceTypeToString(eDataType, instanceValue);
		case AttributePackage.INTERSECTION_TYPE:
			return convertIntersectionTypeToString(eDataType, instanceValue);
		case AttributePackage.LEADER_LINE_STYLE:
			return convertLeaderLineStyleToString(eDataType, instanceValue);
		case AttributePackage.LEGEND_BEHAVIOR_TYPE:
			return convertLegendBehaviorTypeToString(eDataType, instanceValue);
		case AttributePackage.LEGEND_ITEM_TYPE:
			return convertLegendItemTypeToString(eDataType, instanceValue);
		case AttributePackage.LINE_DECORATOR:
			return convertLineDecoratorToString(eDataType, instanceValue);
		case AttributePackage.LINE_STYLE:
			return convertLineStyleToString(eDataType, instanceValue);
		case AttributePackage.MARKER_TYPE:
			return convertMarkerTypeToString(eDataType, instanceValue);
		case AttributePackage.MENU_STYLES_KEY_TYPE:
			return convertMenuStylesKeyTypeToString(eDataType, instanceValue);
		case AttributePackage.ORIENTATION:
			return convertOrientationToString(eDataType, instanceValue);
		case AttributePackage.POSITION:
			return convertPositionToString(eDataType, instanceValue);
		case AttributePackage.RISER_TYPE:
			return convertRiserTypeToString(eDataType, instanceValue);
		case AttributePackage.RULE_TYPE:
			return convertRuleTypeToString(eDataType, instanceValue);
		case AttributePackage.SCALE_UNIT_TYPE:
			return convertScaleUnitTypeToString(eDataType, instanceValue);
		case AttributePackage.SORT_OPTION:
			return convertSortOptionToString(eDataType, instanceValue);
		case AttributePackage.STRETCH:
			return convertStretchToString(eDataType, instanceValue);
		case AttributePackage.STYLED_COMPONENT:
			return convertStyledComponentToString(eDataType, instanceValue);
		case AttributePackage.TICK_STYLE:
			return convertTickStyleToString(eDataType, instanceValue);
		case AttributePackage.TRIGGER_CONDITION:
			return convertTriggerConditionToString(eDataType, instanceValue);
		case AttributePackage.TRIGGER_FLOW:
			return convertTriggerFlowToString(eDataType, instanceValue);
		case AttributePackage.UNITS_OF_MEASUREMENT:
			return convertUnitsOfMeasurementToString(eDataType, instanceValue);
		case AttributePackage.VERTICAL_ALIGNMENT:
			return convertVerticalAlignmentToString(eDataType, instanceValue);
		case AttributePackage.ACTION_TYPE_OBJECT:
			return convertActionTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.ANCHOR_OBJECT:
			return convertAnchorObjectToString(eDataType, instanceValue);
		case AttributePackage.ANGLE_TYPE_OBJECT:
			return convertAngleTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.AXIS_TYPE_OBJECT:
			return convertAxisTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.CHART_DIMENSION_OBJECT:
			return convertChartDimensionObjectToString(eDataType, instanceValue);
		case AttributePackage.CHART_TYPE_OBJECT:
			return convertChartTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.CURSOR_TYPE_OBJECT:
			return convertCursorTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.DATA_POINT_COMPONENT_TYPE_OBJECT:
			return convertDataPointComponentTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.DATA_TYPE_OBJECT:
			return convertDataTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.DATE_FORMAT_DETAIL_OBJECT:
			return convertDateFormatDetailObjectToString(eDataType, instanceValue);
		case AttributePackage.DATE_FORMAT_TYPE_OBJECT:
			return convertDateFormatTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.DIRECTION_OBJECT:
			return convertDirectionObjectToString(eDataType, instanceValue);
		case AttributePackage.GROUPING_UNIT_TYPE_OBJECT:
			return convertGroupingUnitTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.HORIZONTAL_ALIGNMENT_OBJECT:
			return convertHorizontalAlignmentObjectToString(eDataType, instanceValue);
		case AttributePackage.ID:
			return convertIDToString(eDataType, instanceValue);
		case AttributePackage.IMAGE_SOURCE_TYPE_OBJECT:
			return convertImageSourceTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.INTERSECTION_TYPE_OBJECT:
			return convertIntersectionTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.LEADER_LINE_STYLE_OBJECT:
			return convertLeaderLineStyleObjectToString(eDataType, instanceValue);
		case AttributePackage.LEGEND_BEHAVIOR_TYPE_OBJECT:
			return convertLegendBehaviorTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.LEGEND_ITEM_TYPE_OBJECT:
			return convertLegendItemTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.LINE_DECORATOR_OBJECT:
			return convertLineDecoratorObjectToString(eDataType, instanceValue);
		case AttributePackage.LINE_STYLE_OBJECT:
			return convertLineStyleObjectToString(eDataType, instanceValue);
		case AttributePackage.MARKER_TYPE_OBJECT:
			return convertMarkerTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.MENU_STYLES_KEY_TYPE_OBJECT:
			return convertMenuStylesKeyTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.ORIENTATION_OBJECT:
			return convertOrientationObjectToString(eDataType, instanceValue);
		case AttributePackage.PATTERN_BITMAP:
			return convertPatternBitmapToString(eDataType, instanceValue);
		case AttributePackage.PATTERN_BITMAP_OBJECT:
			return convertPatternBitmapObjectToString(eDataType, instanceValue);
		case AttributePackage.PERCENTAGE:
			return convertPercentageToString(eDataType, instanceValue);
		case AttributePackage.PERCENTAGE_OBJECT:
			return convertPercentageObjectToString(eDataType, instanceValue);
		case AttributePackage.POSITION_OBJECT:
			return convertPositionObjectToString(eDataType, instanceValue);
		case AttributePackage.RGB_VALUE:
			return convertRGBValueToString(eDataType, instanceValue);
		case AttributePackage.RGB_VALUE_OBJECT:
			return convertRGBValueObjectToString(eDataType, instanceValue);
		case AttributePackage.RISER_TYPE_OBJECT:
			return convertRiserTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.RULE_TYPE_OBJECT:
			return convertRuleTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.SCALE_UNIT_TYPE_OBJECT:
			return convertScaleUnitTypeObjectToString(eDataType, instanceValue);
		case AttributePackage.SORT_OPTION_OBJECT:
			return convertSortOptionObjectToString(eDataType, instanceValue);
		case AttributePackage.STRETCH_OBJECT:
			return convertStretchObjectToString(eDataType, instanceValue);
		case AttributePackage.STYLED_COMPONENT_OBJECT:
			return convertStyledComponentObjectToString(eDataType, instanceValue);
		case AttributePackage.TICK_STYLE_OBJECT:
			return convertTickStyleObjectToString(eDataType, instanceValue);
		case AttributePackage.TRIGGER_CONDITION_OBJECT:
			return convertTriggerConditionObjectToString(eDataType, instanceValue);
		case AttributePackage.TRIGGER_FLOW_OBJECT:
			return convertTriggerFlowObjectToString(eDataType, instanceValue);
		case AttributePackage.UNITS_OF_MEASUREMENT_OBJECT:
			return convertUnitsOfMeasurementObjectToString(eDataType, instanceValue);
		case AttributePackage.VERTICAL_ALIGNMENT_OBJECT:
			return convertVerticalAlignmentObjectToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AccessibilityValue createAccessibilityValue() {
		AccessibilityValueImpl accessibilityValue = new AccessibilityValueImpl();
		return accessibilityValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ActionValue createActionValue() {
		ActionValueImpl actionValue = new ActionValueImpl();
		return actionValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Angle3D createAngle3D() {
		Angle3DImpl angle3D = new Angle3DImpl();
		return angle3D;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AxisOrigin createAxisOrigin() {
		AxisOriginImpl axisOrigin = new AxisOriginImpl();
		return axisOrigin;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Bounds createBounds() {
		BoundsImpl bounds = new BoundsImpl();
		return bounds;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public CallBackValue createCallBackValue() {
		CallBackValueImpl callBackValue = new CallBackValueImpl();
		return callBackValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ColorDefinition createColorDefinition() {
		ColorDefinitionImpl colorDefinition = new ColorDefinitionImpl();
		return colorDefinition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Cursor createCursor() {
		CursorImpl cursor = new CursorImpl();
		return cursor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DataPoint createDataPoint() {
		DataPointImpl dataPoint = new DataPointImpl();
		return dataPoint;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DataPointComponent createDataPointComponent() {
		DataPointComponentImpl dataPointComponent = new DataPointComponentImpl();
		return dataPointComponent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DateFormatSpecifier createDateFormatSpecifier() {
		DateFormatSpecifierImpl dateFormatSpecifier = new DateFormatSpecifierImpl();
		return dateFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EmbeddedImage createEmbeddedImage() {
		EmbeddedImageImpl embeddedImage = new EmbeddedImageImpl();
		return embeddedImage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, String> createEStringToStringMapEntry() {
		EStringToStringMapEntryImpl eStringToStringMapEntry = new EStringToStringMapEntryImpl();
		return eStringToStringMapEntry;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ExtendedProperty createExtendedProperty() {
		ExtendedPropertyImpl extendedProperty = new ExtendedPropertyImpl();
		return extendedProperty;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Fill createFill() {
		FillImpl fill = new FillImpl();
		return fill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public FontDefinition createFontDefinition() {
		FontDefinitionImpl fontDefinition = new FontDefinitionImpl();
		return fontDefinition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public FormatSpecifier createFormatSpecifier() {
		FormatSpecifierImpl formatSpecifier = new FormatSpecifierImpl();
		return formatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public FractionNumberFormatSpecifier createFractionNumberFormatSpecifier() {
		FractionNumberFormatSpecifierImpl fractionNumberFormatSpecifier = new FractionNumberFormatSpecifierImpl();
		return fractionNumberFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Gradient createGradient() {
		GradientImpl gradient = new GradientImpl();
		return gradient;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Image createImage() {
		ImageImpl image = new ImageImpl();
		return image;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Insets createInsets() {
		InsetsImpl insets = new InsetsImpl();
		return insets;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Interactivity createInteractivity() {
		InteractivityImpl interactivity = new InteractivityImpl();
		return interactivity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public JavaDateFormatSpecifier createJavaDateFormatSpecifier() {
		JavaDateFormatSpecifierImpl javaDateFormatSpecifier = new JavaDateFormatSpecifierImpl();
		return javaDateFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public JavaNumberFormatSpecifier createJavaNumberFormatSpecifier() {
		JavaNumberFormatSpecifierImpl javaNumberFormatSpecifier = new JavaNumberFormatSpecifierImpl();
		return javaNumberFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public LineAttributes createLineAttributes() {
		LineAttributesImpl lineAttributes = new LineAttributesImpl();
		return lineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Location createLocation() {
		LocationImpl location = new LocationImpl();
		return location;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Location3D createLocation3D() {
		Location3DImpl location3D = new Location3DImpl();
		return location3D;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Marker createMarker() {
		MarkerImpl marker = new MarkerImpl();
		return marker;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public MultipleFill createMultipleFill() {
		MultipleFillImpl multipleFill = new MultipleFillImpl();
		return multipleFill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public MultiURLValues createMultiURLValues() {
		MultiURLValuesImpl multiURLValues = new MultiURLValuesImpl();
		return multiURLValues;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NumberFormatSpecifier createNumberFormatSpecifier() {
		NumberFormatSpecifierImpl numberFormatSpecifier = new NumberFormatSpecifierImpl();
		return numberFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Palette createPalette() {
		PaletteImpl palette = new PaletteImpl();
		return palette;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public PatternImage createPatternImage() {
		PatternImageImpl patternImage = new PatternImageImpl();
		return patternImage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Rotation3D createRotation3D() {
		Rotation3DImpl rotation3D = new Rotation3DImpl();
		return rotation3D;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ScriptValue createScriptValue() {
		ScriptValueImpl scriptValue = new ScriptValueImpl();
		return scriptValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public SeriesValue createSeriesValue() {
		SeriesValueImpl seriesValue = new SeriesValueImpl();
		return seriesValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Size createSize() {
		SizeImpl size = new SizeImpl();
		return size;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StringFormatSpecifier createStringFormatSpecifier() {
		StringFormatSpecifierImpl stringFormatSpecifier = new StringFormatSpecifierImpl();
		return stringFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Style createStyle() {
		StyleImpl style = new StyleImpl();
		return style;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StyleMap createStyleMap() {
		StyleMapImpl styleMap = new StyleMapImpl();
		return styleMap;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Text createText() {
		TextImpl text = new TextImpl();
		return text;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public TextAlignment createTextAlignment() {
		TextAlignmentImpl textAlignment = new TextAlignmentImpl();
		return textAlignment;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public TooltipValue createTooltipValue() {
		TooltipValueImpl tooltipValue = new TooltipValueImpl();
		return tooltipValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public URLValue createURLValue() {
		URLValueImpl urlValue = new URLValueImpl();
		return urlValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ActionType createActionTypeFromString(EDataType eDataType, String initialValue) {
		ActionType result = ActionType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertActionTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Anchor createAnchorFromString(EDataType eDataType, String initialValue) {
		Anchor result = Anchor.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertAnchorToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AngleType createAngleTypeFromString(EDataType eDataType, String initialValue) {
		AngleType result = AngleType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertAngleTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AxisType createAxisTypeFromString(EDataType eDataType, String initialValue) {
		AxisType result = AxisType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertAxisTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ChartDimension createChartDimensionFromString(EDataType eDataType, String initialValue) {
		ChartDimension result = ChartDimension.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertChartDimensionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ChartType createChartTypeFromString(EDataType eDataType, String initialValue) {
		ChartType result = ChartType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertChartTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public CursorType createCursorTypeFromString(EDataType eDataType, String initialValue) {
		CursorType result = CursorType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertCursorTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DataPointComponentType createDataPointComponentTypeFromString(EDataType eDataType, String initialValue) {
		DataPointComponentType result = DataPointComponentType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertDataPointComponentTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DataType createDataTypeFromString(EDataType eDataType, String initialValue) {
		DataType result = DataType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertDataTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DateFormatDetail createDateFormatDetailFromString(EDataType eDataType, String initialValue) {
		DateFormatDetail result = DateFormatDetail.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertDateFormatDetailToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DateFormatType createDateFormatTypeFromString(EDataType eDataType, String initialValue) {
		DateFormatType result = DateFormatType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertDateFormatTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Direction createDirectionFromString(EDataType eDataType, String initialValue) {
		Direction result = Direction.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertDirectionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public GroupingUnitType createGroupingUnitTypeFromString(EDataType eDataType, String initialValue) {
		GroupingUnitType result = GroupingUnitType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertGroupingUnitTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public HorizontalAlignment createHorizontalAlignmentFromString(EDataType eDataType, String initialValue) {
		HorizontalAlignment result = HorizontalAlignment.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertHorizontalAlignmentToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ImageSourceType createImageSourceTypeFromString(EDataType eDataType, String initialValue) {
		ImageSourceType result = ImageSourceType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertImageSourceTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public IntersectionType createIntersectionTypeFromString(EDataType eDataType, String initialValue) {
		IntersectionType result = IntersectionType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertIntersectionTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public LeaderLineStyle createLeaderLineStyleFromString(EDataType eDataType, String initialValue) {
		LeaderLineStyle result = LeaderLineStyle.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertLeaderLineStyleToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public LegendBehaviorType createLegendBehaviorTypeFromString(EDataType eDataType, String initialValue) {
		LegendBehaviorType result = LegendBehaviorType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertLegendBehaviorTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public LegendItemType createLegendItemTypeFromString(EDataType eDataType, String initialValue) {
		LegendItemType result = LegendItemType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertLegendItemTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public LineDecorator createLineDecoratorFromString(EDataType eDataType, String initialValue) {
		LineDecorator result = LineDecorator.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertLineDecoratorToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public LineStyle createLineStyleFromString(EDataType eDataType, String initialValue) {
		LineStyle result = LineStyle.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertLineStyleToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MarkerType createMarkerTypeFromString(EDataType eDataType, String initialValue) {
		MarkerType result = MarkerType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertMarkerTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MenuStylesKeyType createMenuStylesKeyTypeFromString(EDataType eDataType, String initialValue) {
		MenuStylesKeyType result = MenuStylesKeyType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertMenuStylesKeyTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Orientation createOrientationFromString(EDataType eDataType, String initialValue) {
		Orientation result = Orientation.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertOrientationToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Position createPositionFromString(EDataType eDataType, String initialValue) {
		Position result = Position.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertPositionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public RiserType createRiserTypeFromString(EDataType eDataType, String initialValue) {
		RiserType result = RiserType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertRiserTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public RuleType createRuleTypeFromString(EDataType eDataType, String initialValue) {
		RuleType result = RuleType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertRuleTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ScaleUnitType createScaleUnitTypeFromString(EDataType eDataType, String initialValue) {
		ScaleUnitType result = ScaleUnitType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertScaleUnitTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public SortOption createSortOptionFromString(EDataType eDataType, String initialValue) {
		SortOption result = SortOption.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertSortOptionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Stretch createStretchFromString(EDataType eDataType, String initialValue) {
		Stretch result = Stretch.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertStretchToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public StyledComponent createStyledComponentFromString(EDataType eDataType, String initialValue) {
		StyledComponent result = StyledComponent.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertStyledComponentToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TickStyle createTickStyleFromString(EDataType eDataType, String initialValue) {
		TickStyle result = TickStyle.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertTickStyleToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TriggerCondition createTriggerConditionFromString(EDataType eDataType, String initialValue) {
		TriggerCondition result = TriggerCondition.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertTriggerConditionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TriggerFlow createTriggerFlowFromString(EDataType eDataType, String initialValue) {
		TriggerFlow result = TriggerFlow.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertTriggerFlowToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public UnitsOfMeasurement createUnitsOfMeasurementFromString(EDataType eDataType, String initialValue) {
		UnitsOfMeasurement result = UnitsOfMeasurement.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertUnitsOfMeasurementToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public VerticalAlignment createVerticalAlignmentFromString(EDataType eDataType, String initialValue) {
		VerticalAlignment result = VerticalAlignment.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertVerticalAlignmentToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ActionType createActionTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createActionTypeFromString(AttributePackage.Literals.ACTION_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertActionTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertActionTypeToString(AttributePackage.Literals.ACTION_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Anchor createAnchorObjectFromString(EDataType eDataType, String initialValue) {
		return createAnchorFromString(AttributePackage.Literals.ANCHOR, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertAnchorObjectToString(EDataType eDataType, Object instanceValue) {
		return convertAnchorToString(AttributePackage.Literals.ANCHOR, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AngleType createAngleTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createAngleTypeFromString(AttributePackage.Literals.ANGLE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertAngleTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertAngleTypeToString(AttributePackage.Literals.ANGLE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AxisType createAxisTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createAxisTypeFromString(AttributePackage.Literals.AXIS_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertAxisTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertAxisTypeToString(AttributePackage.Literals.AXIS_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ChartDimension createChartDimensionObjectFromString(EDataType eDataType, String initialValue) {
		return createChartDimensionFromString(AttributePackage.Literals.CHART_DIMENSION, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertChartDimensionObjectToString(EDataType eDataType, Object instanceValue) {
		return convertChartDimensionToString(AttributePackage.Literals.CHART_DIMENSION, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ChartType createChartTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createChartTypeFromString(AttributePackage.Literals.CHART_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertChartTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertChartTypeToString(AttributePackage.Literals.CHART_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public CursorType createCursorTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createCursorTypeFromString(AttributePackage.Literals.CURSOR_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertCursorTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertCursorTypeToString(AttributePackage.Literals.CURSOR_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DataPointComponentType createDataPointComponentTypeObjectFromString(EDataType eDataType,
			String initialValue) {
		return createDataPointComponentTypeFromString(AttributePackage.Literals.DATA_POINT_COMPONENT_TYPE,
				initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertDataPointComponentTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertDataPointComponentTypeToString(AttributePackage.Literals.DATA_POINT_COMPONENT_TYPE,
				instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DataType createDataTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createDataTypeFromString(AttributePackage.Literals.DATA_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertDataTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertDataTypeToString(AttributePackage.Literals.DATA_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DateFormatDetail createDateFormatDetailObjectFromString(EDataType eDataType, String initialValue) {
		return createDateFormatDetailFromString(AttributePackage.Literals.DATE_FORMAT_DETAIL, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertDateFormatDetailObjectToString(EDataType eDataType, Object instanceValue) {
		return convertDateFormatDetailToString(AttributePackage.Literals.DATE_FORMAT_DETAIL, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DateFormatType createDateFormatTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createDateFormatTypeFromString(AttributePackage.Literals.DATE_FORMAT_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertDateFormatTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertDateFormatTypeToString(AttributePackage.Literals.DATE_FORMAT_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Direction createDirectionObjectFromString(EDataType eDataType, String initialValue) {
		return createDirectionFromString(AttributePackage.Literals.DIRECTION, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertDirectionObjectToString(EDataType eDataType, Object instanceValue) {
		return convertDirectionToString(AttributePackage.Literals.DIRECTION, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public GroupingUnitType createGroupingUnitTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createGroupingUnitTypeFromString(AttributePackage.Literals.GROUPING_UNIT_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertGroupingUnitTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertGroupingUnitTypeToString(AttributePackage.Literals.GROUPING_UNIT_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public HorizontalAlignment createHorizontalAlignmentObjectFromString(EDataType eDataType, String initialValue) {
		return createHorizontalAlignmentFromString(AttributePackage.Literals.HORIZONTAL_ALIGNMENT, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertHorizontalAlignmentObjectToString(EDataType eDataType, Object instanceValue) {
		return convertHorizontalAlignmentToString(AttributePackage.Literals.HORIZONTAL_ALIGNMENT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String createIDFromString(EDataType eDataType, String initialValue) {
		return (String) XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertIDToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ImageSourceType createImageSourceTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createImageSourceTypeFromString(AttributePackage.Literals.IMAGE_SOURCE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertImageSourceTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertImageSourceTypeToString(AttributePackage.Literals.IMAGE_SOURCE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public IntersectionType createIntersectionTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createIntersectionTypeFromString(AttributePackage.Literals.INTERSECTION_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertIntersectionTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertIntersectionTypeToString(AttributePackage.Literals.INTERSECTION_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public LeaderLineStyle createLeaderLineStyleObjectFromString(EDataType eDataType, String initialValue) {
		return createLeaderLineStyleFromString(AttributePackage.Literals.LEADER_LINE_STYLE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertLeaderLineStyleObjectToString(EDataType eDataType, Object instanceValue) {
		return convertLeaderLineStyleToString(AttributePackage.Literals.LEADER_LINE_STYLE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public LegendBehaviorType createLegendBehaviorTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createLegendBehaviorTypeFromString(AttributePackage.Literals.LEGEND_BEHAVIOR_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertLegendBehaviorTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertLegendBehaviorTypeToString(AttributePackage.Literals.LEGEND_BEHAVIOR_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public LegendItemType createLegendItemTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createLegendItemTypeFromString(AttributePackage.Literals.LEGEND_ITEM_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertLegendItemTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertLegendItemTypeToString(AttributePackage.Literals.LEGEND_ITEM_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public LineDecorator createLineDecoratorObjectFromString(EDataType eDataType, String initialValue) {
		return createLineDecoratorFromString(AttributePackage.Literals.LINE_DECORATOR, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertLineDecoratorObjectToString(EDataType eDataType, Object instanceValue) {
		return convertLineDecoratorToString(AttributePackage.Literals.LINE_DECORATOR, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public LineStyle createLineStyleObjectFromString(EDataType eDataType, String initialValue) {
		return createLineStyleFromString(AttributePackage.Literals.LINE_STYLE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertLineStyleObjectToString(EDataType eDataType, Object instanceValue) {
		return convertLineStyleToString(AttributePackage.Literals.LINE_STYLE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MarkerType createMarkerTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createMarkerTypeFromString(AttributePackage.Literals.MARKER_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertMarkerTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertMarkerTypeToString(AttributePackage.Literals.MARKER_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MenuStylesKeyType createMenuStylesKeyTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createMenuStylesKeyTypeFromString(AttributePackage.Literals.MENU_STYLES_KEY_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertMenuStylesKeyTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertMenuStylesKeyTypeToString(AttributePackage.Literals.MENU_STYLES_KEY_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Orientation createOrientationObjectFromString(EDataType eDataType, String initialValue) {
		return createOrientationFromString(AttributePackage.Literals.ORIENTATION, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertOrientationObjectToString(EDataType eDataType, Object instanceValue) {
		return convertOrientationToString(AttributePackage.Literals.ORIENTATION, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Long createPatternBitmapFromString(EDataType eDataType, String initialValue) {
		return (Long) XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.LONG, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertPatternBitmapToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.LONG, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Long createPatternBitmapObjectFromString(EDataType eDataType, String initialValue) {
		return createPatternBitmapFromString(AttributePackage.Literals.PATTERN_BITMAP, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertPatternBitmapObjectToString(EDataType eDataType, Object instanceValue) {
		return convertPatternBitmapToString(AttributePackage.Literals.PATTERN_BITMAP, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Double createPercentageFromString(EDataType eDataType, String initialValue) {
		return (Double) XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DOUBLE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertPercentageToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DOUBLE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Double createPercentageObjectFromString(EDataType eDataType, String initialValue) {
		return createPercentageFromString(AttributePackage.Literals.PERCENTAGE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertPercentageObjectToString(EDataType eDataType, Object instanceValue) {
		return convertPercentageToString(AttributePackage.Literals.PERCENTAGE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Position createPositionObjectFromString(EDataType eDataType, String initialValue) {
		return createPositionFromString(AttributePackage.Literals.POSITION, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertPositionObjectToString(EDataType eDataType, Object instanceValue) {
		return convertPositionToString(AttributePackage.Literals.POSITION, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Integer createRGBValueFromString(EDataType eDataType, String initialValue) {
		return (Integer) XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INT, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertRGBValueToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Integer createRGBValueObjectFromString(EDataType eDataType, String initialValue) {
		return createRGBValueFromString(AttributePackage.Literals.RGB_VALUE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertRGBValueObjectToString(EDataType eDataType, Object instanceValue) {
		return convertRGBValueToString(AttributePackage.Literals.RGB_VALUE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public RiserType createRiserTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createRiserTypeFromString(AttributePackage.Literals.RISER_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertRiserTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertRiserTypeToString(AttributePackage.Literals.RISER_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public RuleType createRuleTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createRuleTypeFromString(AttributePackage.Literals.RULE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertRuleTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertRuleTypeToString(AttributePackage.Literals.RULE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ScaleUnitType createScaleUnitTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createScaleUnitTypeFromString(AttributePackage.Literals.SCALE_UNIT_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertScaleUnitTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertScaleUnitTypeToString(AttributePackage.Literals.SCALE_UNIT_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public SortOption createSortOptionObjectFromString(EDataType eDataType, String initialValue) {
		return createSortOptionFromString(AttributePackage.Literals.SORT_OPTION, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertSortOptionObjectToString(EDataType eDataType, Object instanceValue) {
		return convertSortOptionToString(AttributePackage.Literals.SORT_OPTION, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Stretch createStretchObjectFromString(EDataType eDataType, String initialValue) {
		return createStretchFromString(AttributePackage.Literals.STRETCH, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertStretchObjectToString(EDataType eDataType, Object instanceValue) {
		return convertStretchToString(AttributePackage.Literals.STRETCH, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public StyledComponent createStyledComponentObjectFromString(EDataType eDataType, String initialValue) {
		return createStyledComponentFromString(AttributePackage.Literals.STYLED_COMPONENT, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertStyledComponentObjectToString(EDataType eDataType, Object instanceValue) {
		return convertStyledComponentToString(AttributePackage.Literals.STYLED_COMPONENT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TickStyle createTickStyleObjectFromString(EDataType eDataType, String initialValue) {
		return createTickStyleFromString(AttributePackage.Literals.TICK_STYLE, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertTickStyleObjectToString(EDataType eDataType, Object instanceValue) {
		return convertTickStyleToString(AttributePackage.Literals.TICK_STYLE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TriggerCondition createTriggerConditionObjectFromString(EDataType eDataType, String initialValue) {
		return createTriggerConditionFromString(AttributePackage.Literals.TRIGGER_CONDITION, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertTriggerConditionObjectToString(EDataType eDataType, Object instanceValue) {
		return convertTriggerConditionToString(AttributePackage.Literals.TRIGGER_CONDITION, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TriggerFlow createTriggerFlowObjectFromString(EDataType eDataType, String initialValue) {
		return createTriggerFlowFromString(AttributePackage.Literals.TRIGGER_FLOW, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertTriggerFlowObjectToString(EDataType eDataType, Object instanceValue) {
		return convertTriggerFlowToString(AttributePackage.Literals.TRIGGER_FLOW, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public UnitsOfMeasurement createUnitsOfMeasurementObjectFromString(EDataType eDataType, String initialValue) {
		return createUnitsOfMeasurementFromString(AttributePackage.Literals.UNITS_OF_MEASUREMENT, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertUnitsOfMeasurementObjectToString(EDataType eDataType, Object instanceValue) {
		return convertUnitsOfMeasurementToString(AttributePackage.Literals.UNITS_OF_MEASUREMENT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public VerticalAlignment createVerticalAlignmentObjectFromString(EDataType eDataType, String initialValue) {
		return createVerticalAlignmentFromString(AttributePackage.Literals.VERTICAL_ALIGNMENT, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertVerticalAlignmentObjectToString(EDataType eDataType, Object instanceValue) {
		return convertVerticalAlignmentToString(AttributePackage.Literals.VERTICAL_ALIGNMENT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AttributePackage getAttributePackage() {
		return (AttributePackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AttributePackage getPackage() {
		return AttributePackage.eINSTANCE;
	}

} // AttributeFactoryImpl
