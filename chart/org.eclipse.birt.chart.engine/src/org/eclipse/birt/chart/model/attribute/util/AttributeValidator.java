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

package org.eclipse.birt.chart.model.attribute.util;

import java.util.Map;

import org.eclipse.birt.chart.model.attribute.*;

import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.CallBackValue;
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
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Image;
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
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Palette;
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
import org.eclipse.birt.chart.model.attribute.TriggerFlow;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc --> The <b>Validator </b> for the model. <!--
 * end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage
 * @generated
 */
public class AttributeValidator extends EObjectValidator {

	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final AttributeValidator INSTANCE = new AttributeValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource()
	 * source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode()
	 * codes} from this package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "org.eclipse.birt.chart.model.attribute"; //$NON-NLS-1$

	/**
	 * A constant with a fixed name that can be used as the base value for
	 * additional hand written constants. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for
	 * additional hand written constants in a derived class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * The cached base package validator. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected XMLTypeValidator xmlTypeValidator;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public AttributeValidator() {
		super();
		xmlTypeValidator = XMLTypeValidator.INSTANCE;
	}

	/**
	 * Returns the package of this validator switch. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
		return AttributePackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		switch (classifierID) {
		case AttributePackage.ACCESSIBILITY_VALUE:
			return validateAccessibilityValue((AccessibilityValue) value, diagnostics, context);
		case AttributePackage.ACTION_VALUE:
			return validateActionValue((ActionValue) value, diagnostics, context);
		case AttributePackage.ANGLE3_D:
			return validateAngle3D((Angle3D) value, diagnostics, context);
		case AttributePackage.AXIS_ORIGIN:
			return validateAxisOrigin((AxisOrigin) value, diagnostics, context);
		case AttributePackage.BOUNDS:
			return validateBounds((Bounds) value, diagnostics, context);
		case AttributePackage.CALL_BACK_VALUE:
			return validateCallBackValue((CallBackValue) value, diagnostics, context);
		case AttributePackage.COLOR_DEFINITION:
			return validateColorDefinition((ColorDefinition) value, diagnostics, context);
		case AttributePackage.CURSOR:
			return validateCursor((Cursor) value, diagnostics, context);
		case AttributePackage.DATA_POINT:
			return validateDataPoint((DataPoint) value, diagnostics, context);
		case AttributePackage.DATA_POINT_COMPONENT:
			return validateDataPointComponent((DataPointComponent) value, diagnostics, context);
		case AttributePackage.DATE_FORMAT_SPECIFIER:
			return validateDateFormatSpecifier((DateFormatSpecifier) value, diagnostics, context);
		case AttributePackage.EMBEDDED_IMAGE:
			return validateEmbeddedImage((EmbeddedImage) value, diagnostics, context);
		case AttributePackage.ESTRING_TO_STRING_MAP_ENTRY:
			return validateEStringToStringMapEntry((Map.Entry<?, ?>) value, diagnostics, context);
		case AttributePackage.EXTENDED_PROPERTY:
			return validateExtendedProperty((ExtendedProperty) value, diagnostics, context);
		case AttributePackage.FILL:
			return validateFill((Fill) value, diagnostics, context);
		case AttributePackage.FONT_DEFINITION:
			return validateFontDefinition((FontDefinition) value, diagnostics, context);
		case AttributePackage.FORMAT_SPECIFIER:
			return validateFormatSpecifier((FormatSpecifier) value, diagnostics, context);
		case AttributePackage.FRACTION_NUMBER_FORMAT_SPECIFIER:
			return validateFractionNumberFormatSpecifier((FractionNumberFormatSpecifier) value, diagnostics, context);
		case AttributePackage.GRADIENT:
			return validateGradient((Gradient) value, diagnostics, context);
		case AttributePackage.IMAGE:
			return validateImage((Image) value, diagnostics, context);
		case AttributePackage.INSETS:
			return validateInsets((Insets) value, diagnostics, context);
		case AttributePackage.INTERACTIVITY:
			return validateInteractivity((Interactivity) value, diagnostics, context);
		case AttributePackage.JAVA_DATE_FORMAT_SPECIFIER:
			return validateJavaDateFormatSpecifier((JavaDateFormatSpecifier) value, diagnostics, context);
		case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER:
			return validateJavaNumberFormatSpecifier((JavaNumberFormatSpecifier) value, diagnostics, context);
		case AttributePackage.LINE_ATTRIBUTES:
			return validateLineAttributes((LineAttributes) value, diagnostics, context);
		case AttributePackage.LOCATION:
			return validateLocation((Location) value, diagnostics, context);
		case AttributePackage.LOCATION3_D:
			return validateLocation3D((Location3D) value, diagnostics, context);
		case AttributePackage.MARKER:
			return validateMarker((Marker) value, diagnostics, context);
		case AttributePackage.MULTIPLE_FILL:
			return validateMultipleFill((MultipleFill) value, diagnostics, context);
		case AttributePackage.MULTI_URL_VALUES:
			return validateMultiURLValues((MultiURLValues) value, diagnostics, context);
		case AttributePackage.NUMBER_FORMAT_SPECIFIER:
			return validateNumberFormatSpecifier((NumberFormatSpecifier) value, diagnostics, context);
		case AttributePackage.PALETTE:
			return validatePalette((Palette) value, diagnostics, context);
		case AttributePackage.PATTERN_IMAGE:
			return validatePatternImage((PatternImage) value, diagnostics, context);
		case AttributePackage.ROTATION3_D:
			return validateRotation3D((Rotation3D) value, diagnostics, context);
		case AttributePackage.SCRIPT_VALUE:
			return validateScriptValue((ScriptValue) value, diagnostics, context);
		case AttributePackage.SERIES_VALUE:
			return validateSeriesValue((SeriesValue) value, diagnostics, context);
		case AttributePackage.SIZE:
			return validateSize((Size) value, diagnostics, context);
		case AttributePackage.STRING_FORMAT_SPECIFIER:
			return validateStringFormatSpecifier((StringFormatSpecifier) value, diagnostics, context);
		case AttributePackage.STYLE:
			return validateStyle((Style) value, diagnostics, context);
		case AttributePackage.STYLE_MAP:
			return validateStyleMap((StyleMap) value, diagnostics, context);
		case AttributePackage.TEXT:
			return validateText((Text) value, diagnostics, context);
		case AttributePackage.TEXT_ALIGNMENT:
			return validateTextAlignment((TextAlignment) value, diagnostics, context);
		case AttributePackage.TOOLTIP_VALUE:
			return validateTooltipValue((TooltipValue) value, diagnostics, context);
		case AttributePackage.URL_VALUE:
			return validateURLValue((URLValue) value, diagnostics, context);
		case AttributePackage.ACTION_TYPE:
			return validateActionType((ActionType) value, diagnostics, context);
		case AttributePackage.ANCHOR:
			return validateAnchor((Anchor) value, diagnostics, context);
		case AttributePackage.ANGLE_TYPE:
			return validateAngleType((AngleType) value, diagnostics, context);
		case AttributePackage.AXIS_TYPE:
			return validateAxisType((AxisType) value, diagnostics, context);
		case AttributePackage.CHART_DIMENSION:
			return validateChartDimension((ChartDimension) value, diagnostics, context);
		case AttributePackage.CHART_TYPE:
			return validateChartType((ChartType) value, diagnostics, context);
		case AttributePackage.CURSOR_TYPE:
			return validateCursorType((CursorType) value, diagnostics, context);
		case AttributePackage.DATA_POINT_COMPONENT_TYPE:
			return validateDataPointComponentType((DataPointComponentType) value, diagnostics, context);
		case AttributePackage.DATA_TYPE:
			return validateDataType((DataType) value, diagnostics, context);
		case AttributePackage.DATE_FORMAT_DETAIL:
			return validateDateFormatDetail((DateFormatDetail) value, diagnostics, context);
		case AttributePackage.DATE_FORMAT_TYPE:
			return validateDateFormatType((DateFormatType) value, diagnostics, context);
		case AttributePackage.DIRECTION:
			return validateDirection((Direction) value, diagnostics, context);
		case AttributePackage.GROUPING_UNIT_TYPE:
			return validateGroupingUnitType((GroupingUnitType) value, diagnostics, context);
		case AttributePackage.HORIZONTAL_ALIGNMENT:
			return validateHorizontalAlignment((HorizontalAlignment) value, diagnostics, context);
		case AttributePackage.IMAGE_SOURCE_TYPE:
			return validateImageSourceType((ImageSourceType) value, diagnostics, context);
		case AttributePackage.INTERSECTION_TYPE:
			return validateIntersectionType((IntersectionType) value, diagnostics, context);
		case AttributePackage.LEADER_LINE_STYLE:
			return validateLeaderLineStyle((LeaderLineStyle) value, diagnostics, context);
		case AttributePackage.LEGEND_BEHAVIOR_TYPE:
			return validateLegendBehaviorType((LegendBehaviorType) value, diagnostics, context);
		case AttributePackage.LEGEND_ITEM_TYPE:
			return validateLegendItemType((LegendItemType) value, diagnostics, context);
		case AttributePackage.LINE_DECORATOR:
			return validateLineDecorator((LineDecorator) value, diagnostics, context);
		case AttributePackage.LINE_STYLE:
			return validateLineStyle((LineStyle) value, diagnostics, context);
		case AttributePackage.MARKER_TYPE:
			return validateMarkerType((MarkerType) value, diagnostics, context);
		case AttributePackage.MENU_STYLES_KEY_TYPE:
			return validateMenuStylesKeyType((MenuStylesKeyType) value, diagnostics, context);
		case AttributePackage.ORIENTATION:
			return validateOrientation((Orientation) value, diagnostics, context);
		case AttributePackage.POSITION:
			return validatePosition((Position) value, diagnostics, context);
		case AttributePackage.RISER_TYPE:
			return validateRiserType((RiserType) value, diagnostics, context);
		case AttributePackage.RULE_TYPE:
			return validateRuleType((RuleType) value, diagnostics, context);
		case AttributePackage.SCALE_UNIT_TYPE:
			return validateScaleUnitType((ScaleUnitType) value, diagnostics, context);
		case AttributePackage.SORT_OPTION:
			return validateSortOption((SortOption) value, diagnostics, context);
		case AttributePackage.STRETCH:
			return validateStretch((Stretch) value, diagnostics, context);
		case AttributePackage.STYLED_COMPONENT:
			return validateStyledComponent((StyledComponent) value, diagnostics, context);
		case AttributePackage.TICK_STYLE:
			return validateTickStyle((TickStyle) value, diagnostics, context);
		case AttributePackage.TRIGGER_CONDITION:
			return validateTriggerCondition((TriggerCondition) value, diagnostics, context);
		case AttributePackage.TRIGGER_FLOW:
			return validateTriggerFlow((TriggerFlow) value, diagnostics, context);
		case AttributePackage.UNITS_OF_MEASUREMENT:
			return validateUnitsOfMeasurement((UnitsOfMeasurement) value, diagnostics, context);
		case AttributePackage.VERTICAL_ALIGNMENT:
			return validateVerticalAlignment((VerticalAlignment) value, diagnostics, context);
		case AttributePackage.ACTION_TYPE_OBJECT:
			return validateActionTypeObject((ActionType) value, diagnostics, context);
		case AttributePackage.ANCHOR_OBJECT:
			return validateAnchorObject((Anchor) value, diagnostics, context);
		case AttributePackage.ANGLE_TYPE_OBJECT:
			return validateAngleTypeObject((AngleType) value, diagnostics, context);
		case AttributePackage.AXIS_TYPE_OBJECT:
			return validateAxisTypeObject((AxisType) value, diagnostics, context);
		case AttributePackage.CHART_DIMENSION_OBJECT:
			return validateChartDimensionObject((ChartDimension) value, diagnostics, context);
		case AttributePackage.CHART_TYPE_OBJECT:
			return validateChartTypeObject((ChartType) value, diagnostics, context);
		case AttributePackage.CURSOR_TYPE_OBJECT:
			return validateCursorTypeObject((CursorType) value, diagnostics, context);
		case AttributePackage.DATA_POINT_COMPONENT_TYPE_OBJECT:
			return validateDataPointComponentTypeObject((DataPointComponentType) value, diagnostics, context);
		case AttributePackage.DATA_TYPE_OBJECT:
			return validateDataTypeObject((DataType) value, diagnostics, context);
		case AttributePackage.DATE_FORMAT_DETAIL_OBJECT:
			return validateDateFormatDetailObject((DateFormatDetail) value, diagnostics, context);
		case AttributePackage.DATE_FORMAT_TYPE_OBJECT:
			return validateDateFormatTypeObject((DateFormatType) value, diagnostics, context);
		case AttributePackage.DIRECTION_OBJECT:
			return validateDirectionObject((Direction) value, diagnostics, context);
		case AttributePackage.GROUPING_UNIT_TYPE_OBJECT:
			return validateGroupingUnitTypeObject((GroupingUnitType) value, diagnostics, context);
		case AttributePackage.HORIZONTAL_ALIGNMENT_OBJECT:
			return validateHorizontalAlignmentObject((HorizontalAlignment) value, diagnostics, context);
		case AttributePackage.ID:
			return validateID((String) value, diagnostics, context);
		case AttributePackage.IMAGE_SOURCE_TYPE_OBJECT:
			return validateImageSourceTypeObject((ImageSourceType) value, diagnostics, context);
		case AttributePackage.INTERSECTION_TYPE_OBJECT:
			return validateIntersectionTypeObject((IntersectionType) value, diagnostics, context);
		case AttributePackage.LEADER_LINE_STYLE_OBJECT:
			return validateLeaderLineStyleObject((LeaderLineStyle) value, diagnostics, context);
		case AttributePackage.LEGEND_BEHAVIOR_TYPE_OBJECT:
			return validateLegendBehaviorTypeObject((LegendBehaviorType) value, diagnostics, context);
		case AttributePackage.LEGEND_ITEM_TYPE_OBJECT:
			return validateLegendItemTypeObject((LegendItemType) value, diagnostics, context);
		case AttributePackage.LINE_DECORATOR_OBJECT:
			return validateLineDecoratorObject((LineDecorator) value, diagnostics, context);
		case AttributePackage.LINE_STYLE_OBJECT:
			return validateLineStyleObject((LineStyle) value, diagnostics, context);
		case AttributePackage.MARKER_TYPE_OBJECT:
			return validateMarkerTypeObject((MarkerType) value, diagnostics, context);
		case AttributePackage.MENU_STYLES_KEY_TYPE_OBJECT:
			return validateMenuStylesKeyTypeObject((MenuStylesKeyType) value, diagnostics, context);
		case AttributePackage.ORIENTATION_OBJECT:
			return validateOrientationObject((Orientation) value, diagnostics, context);
		case AttributePackage.PATTERN_BITMAP:
			return validatePatternBitmap((Long) value, diagnostics, context);
		case AttributePackage.PATTERN_BITMAP_OBJECT:
			return validatePatternBitmapObject((Long) value, diagnostics, context);
		case AttributePackage.PERCENTAGE:
			return validatePercentage((Double) value, diagnostics, context);
		case AttributePackage.PERCENTAGE_OBJECT:
			return validatePercentageObject((Double) value, diagnostics, context);
		case AttributePackage.POSITION_OBJECT:
			return validatePositionObject((Position) value, diagnostics, context);
		case AttributePackage.RGB_VALUE:
			return validateRGBValue((Integer) value, diagnostics, context);
		case AttributePackage.RGB_VALUE_OBJECT:
			return validateRGBValueObject((Integer) value, diagnostics, context);
		case AttributePackage.RISER_TYPE_OBJECT:
			return validateRiserTypeObject((RiserType) value, diagnostics, context);
		case AttributePackage.RULE_TYPE_OBJECT:
			return validateRuleTypeObject((RuleType) value, diagnostics, context);
		case AttributePackage.SCALE_UNIT_TYPE_OBJECT:
			return validateScaleUnitTypeObject((ScaleUnitType) value, diagnostics, context);
		case AttributePackage.SORT_OPTION_OBJECT:
			return validateSortOptionObject((SortOption) value, diagnostics, context);
		case AttributePackage.STRETCH_OBJECT:
			return validateStretchObject((Stretch) value, diagnostics, context);
		case AttributePackage.STYLED_COMPONENT_OBJECT:
			return validateStyledComponentObject((StyledComponent) value, diagnostics, context);
		case AttributePackage.TICK_STYLE_OBJECT:
			return validateTickStyleObject((TickStyle) value, diagnostics, context);
		case AttributePackage.TRIGGER_CONDITION_OBJECT:
			return validateTriggerConditionObject((TriggerCondition) value, diagnostics, context);
		case AttributePackage.TRIGGER_FLOW_OBJECT:
			return validateTriggerFlowObject((TriggerFlow) value, diagnostics, context);
		case AttributePackage.UNITS_OF_MEASUREMENT_OBJECT:
			return validateUnitsOfMeasurementObject((UnitsOfMeasurement) value, diagnostics, context);
		case AttributePackage.VERTICAL_ALIGNMENT_OBJECT:
			return validateVerticalAlignmentObject((VerticalAlignment) value, diagnostics, context);
		default:
			return true;
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateAccessibilityValue(AccessibilityValue accessibilityValue, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) accessibilityValue, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateActionValue(ActionValue actionValue, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) actionValue, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateAngle3D(Angle3D angle3D, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) angle3D, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateAxisOrigin(AxisOrigin axisOrigin, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) axisOrigin, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateBounds(Bounds bounds, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) bounds, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateCallBackValue(CallBackValue callBackValue, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) callBackValue, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateColorDefinition(ColorDefinition colorDefinition, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) colorDefinition, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateCursor(Cursor cursor, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) cursor, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDataPoint(DataPoint dataPoint, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) dataPoint, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDataPointComponent(DataPointComponent dataPointComponent, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) dataPointComponent, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDateFormatSpecifier(DateFormatSpecifier dateFormatSpecifier, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) dateFormatSpecifier, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateEmbeddedImage(EmbeddedImage embeddedImage, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) embeddedImage, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateEStringToStringMapEntry(Map.Entry<?, ?> eStringToStringMapEntry, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) eStringToStringMapEntry, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateExtendedProperty(ExtendedProperty extendedProperty, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) extendedProperty, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateFill(Fill fill, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) fill, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateFontDefinition(FontDefinition fontDefinition, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) fontDefinition, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateFormatSpecifier(FormatSpecifier formatSpecifier, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) formatSpecifier, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateFractionNumberFormatSpecifier(FractionNumberFormatSpecifier fractionNumberFormatSpecifier,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) fractionNumberFormatSpecifier, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateGradient(Gradient gradient, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) gradient, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateImage(Image image, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) image, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateInsets(Insets insets, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) insets, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateInteractivity(Interactivity interactivity, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) interactivity, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateJavaDateFormatSpecifier(JavaDateFormatSpecifier javaDateFormatSpecifier,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) javaDateFormatSpecifier, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateJavaNumberFormatSpecifier(JavaNumberFormatSpecifier javaNumberFormatSpecifier,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) javaNumberFormatSpecifier, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLineAttributes(LineAttributes lineAttributes, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) lineAttributes, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLocation(Location location, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) location, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLocation3D(Location3D location3D, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) location3D, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateMarker(Marker marker, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) marker, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateMultipleFill(MultipleFill multipleFill, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) multipleFill, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateMultiURLValues(MultiURLValues multiURLValues, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) multiURLValues, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateNumberFormatSpecifier(NumberFormatSpecifier numberFormatSpecifier,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) numberFormatSpecifier, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validatePalette(Palette palette, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) palette, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validatePatternImage(PatternImage patternImage, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) patternImage, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateRotation3D(Rotation3D rotation3D, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) rotation3D, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateScriptValue(ScriptValue scriptValue, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) scriptValue, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateSeriesValue(SeriesValue seriesValue, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) seriesValue, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateSize(Size size, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) size, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateStringFormatSpecifier(StringFormatSpecifier stringFormatSpecifier,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) stringFormatSpecifier, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateStyle(Style style, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) style, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateStyleMap(StyleMap styleMap, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) styleMap, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateText(Text text, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) text, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateTextAlignment(TextAlignment textAlignment, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) textAlignment, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateTooltipValue(TooltipValue tooltipValue, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) tooltipValue, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateURLValue(URLValue urlValue, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) urlValue, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateActionType(ActionType actionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateAnchor(Anchor anchor, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateAngleType(AngleType angleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateAxisType(AxisType axisType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateChartDimension(ChartDimension chartDimension, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateChartType(ChartType chartType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateCursorType(CursorType cursorType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDataPointComponentType(DataPointComponentType dataPointComponentType,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDataType(DataType dataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDateFormatDetail(DateFormatDetail dateFormatDetail, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDateFormatType(DateFormatType dateFormatType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDirection(Direction direction, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateGroupingUnitType(GroupingUnitType groupingUnitType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateHorizontalAlignment(HorizontalAlignment horizontalAlignment, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateImageSourceType(ImageSourceType imageSourceType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateIntersectionType(IntersectionType intersectionType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLeaderLineStyle(LeaderLineStyle leaderLineStyle, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLegendBehaviorType(LegendBehaviorType legendBehaviorType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLegendItemType(LegendItemType legendItemType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLineDecorator(LineDecorator lineDecorator, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLineStyle(LineStyle lineStyle, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateMarkerType(MarkerType markerType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateMenuStylesKeyType(MenuStylesKeyType menuStylesKeyType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateOrientation(Orientation orientation, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validatePosition(Position position, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateRiserType(RiserType riserType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateRuleType(RuleType ruleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateScaleUnitType(ScaleUnitType scaleUnitType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateSortOption(SortOption sortOption, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateStretch(Stretch stretch, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateStyledComponent(StyledComponent styledComponent, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateTickStyle(TickStyle tickStyle, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateTriggerCondition(TriggerCondition triggerCondition, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateTriggerFlow(TriggerFlow triggerFlow, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateUnitsOfMeasurement(UnitsOfMeasurement unitsOfMeasurement, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateVerticalAlignment(VerticalAlignment verticalAlignment, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateActionTypeObject(ActionType actionTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateAnchorObject(Anchor anchorObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateAngleTypeObject(AngleType angleTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateAxisTypeObject(AxisType axisTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateChartDimensionObject(ChartDimension chartDimensionObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateChartTypeObject(ChartType chartTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateCursorTypeObject(CursorType cursorTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDataPointComponentTypeObject(DataPointComponentType dataPointComponentTypeObject,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDataTypeObject(DataType dataTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDateFormatDetailObject(DateFormatDetail dateFormatDetailObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDateFormatTypeObject(DateFormatType dateFormatTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateDirectionObject(Direction directionObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateGroupingUnitTypeObject(GroupingUnitType groupingUnitTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateHorizontalAlignmentObject(HorizontalAlignment horizontalAlignmentObject,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateID(String id, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateID_Pattern(id, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @see #validateID_Pattern
	 */
	public static final PatternMatcher[][] ID__PATTERN__VALUES = new PatternMatcher[][] {
			new PatternMatcher[] { XMLTypeUtil.createPatternMatcher("[A-Z]") } };

	/**
	 * Validates the Pattern constraint of '<em>ID</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateID_Pattern(String id, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validatePattern(AttributePackage.Literals.ID, id, ID__PATTERN__VALUES, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateImageSourceTypeObject(ImageSourceType imageSourceTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateIntersectionTypeObject(IntersectionType intersectionTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLeaderLineStyleObject(LeaderLineStyle leaderLineStyleObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLegendBehaviorTypeObject(LegendBehaviorType legendBehaviorTypeObject,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLegendItemTypeObject(LegendItemType legendItemTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLineDecoratorObject(LineDecorator lineDecoratorObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateLineStyleObject(LineStyle lineStyleObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateMarkerTypeObject(MarkerType markerTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateMenuStylesKeyTypeObject(MenuStylesKeyType menuStylesKeyTypeObject,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateOrientationObject(Orientation orientationObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validatePatternBitmap(long patternBitmap, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validatePatternBitmapObject(Long patternBitmapObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validatePercentage(double percentage, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validatePercentage_Min(percentage, diagnostics, context);
		if (result || diagnostics != null)
			result &= validatePercentage_Max(percentage, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @see #validatePercentage_Min
	 */
	public static final double PERCENTAGE__MIN__VALUE = 0.0;

	/**
	 * Validates the Min constraint of '<em>Percentage</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validatePercentage_Min(double percentage, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = percentage >= PERCENTAGE__MIN__VALUE;
		if (!result && diagnostics != null)
			reportMinViolation(AttributePackage.Literals.PERCENTAGE, percentage, PERCENTAGE__MIN__VALUE, true,
					diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @see #validatePercentage_Max
	 */
	public static final double PERCENTAGE__MAX__VALUE = 100.0;

	/**
	 * Validates the Max constraint of '<em>Percentage</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validatePercentage_Max(double percentage, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = percentage <= PERCENTAGE__MAX__VALUE;
		if (!result && diagnostics != null)
			reportMaxViolation(AttributePackage.Literals.PERCENTAGE, percentage, PERCENTAGE__MAX__VALUE, true,
					diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validatePercentageObject(Double percentageObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = validatePercentage_Min(percentageObject, diagnostics, context);
		if (result || diagnostics != null)
			result &= validatePercentage_Max(percentageObject, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validatePositionObject(Position positionObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateRGBValue(int rgbValue, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateRGBValue_Min(rgbValue, diagnostics, context);
		if (result || diagnostics != null)
			result &= validateRGBValue_Max(rgbValue, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @see #validateRGBValue_Min
	 */
	public static final int RGB_VALUE__MIN__VALUE = 0;

	/**
	 * Validates the Min constraint of '<em>RGB Value</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateRGBValue_Min(int rgbValue, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = rgbValue >= RGB_VALUE__MIN__VALUE;
		if (!result && diagnostics != null)
			reportMinViolation(AttributePackage.Literals.RGB_VALUE, rgbValue, RGB_VALUE__MIN__VALUE, true, diagnostics,
					context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @see #validateRGBValue_Max
	 */
	public static final int RGB_VALUE__MAX__VALUE = 255;

	/**
	 * Validates the Max constraint of '<em>RGB Value</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateRGBValue_Max(int rgbValue, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = rgbValue <= RGB_VALUE__MAX__VALUE;
		if (!result && diagnostics != null)
			reportMaxViolation(AttributePackage.Literals.RGB_VALUE, rgbValue, RGB_VALUE__MAX__VALUE, true, diagnostics,
					context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateRGBValueObject(Integer rgbValueObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = validateRGBValue_Min(rgbValueObject, diagnostics, context);
		if (result || diagnostics != null)
			result &= validateRGBValue_Max(rgbValueObject, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateRiserTypeObject(RiserType riserTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateRuleTypeObject(RuleType ruleTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateScaleUnitTypeObject(ScaleUnitType scaleUnitTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateSortOptionObject(SortOption sortOptionObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateStretchObject(Stretch stretchObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateStyledComponentObject(StyledComponent styledComponentObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateTickStyleObject(TickStyle tickStyleObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateTriggerConditionObject(TriggerCondition triggerConditionObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateTriggerFlowObject(TriggerFlow triggerFlowObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateUnitsOfMeasurementObject(UnitsOfMeasurement unitsOfMeasurementObject,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean validateVerticalAlignmentObject(VerticalAlignment verticalAlignmentObject,
			DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this
	 * validator's diagnostics. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		// TODO
		// Specialize this to return a resource locator for messages specific to this
		// validator.
		// Ensure that you remove @generated or mark it @generated NOT
		return super.getResourceLocator();
	}

} // AttributeValidator
