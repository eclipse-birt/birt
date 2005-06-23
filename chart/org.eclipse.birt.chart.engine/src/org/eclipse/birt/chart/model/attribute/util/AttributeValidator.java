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

import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.Anchor;
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
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.RuleType;
import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.SeriesValue;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.Stretch;
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
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc --> The <b>Validator </b> for the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage
 * @generated
 */
public class AttributeValidator extends EObjectValidator
{

    /**
     * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final AttributeValidator INSTANCE = new AttributeValidator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source}of diagnostic
     * {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes}from this package. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "org.eclipse.birt.chart.model.attribute"; //$NON-NLS-1$

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    //private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

    /**
     * The cached base package validator. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected XMLTypeValidator xmlTypeValidator;

    /**
     * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public AttributeValidator()
    {
        xmlTypeValidator = XMLTypeValidator.INSTANCE;
    }

    /**
     * Returns the package of this validator switch. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected EPackage getEPackage()
    {
        return AttributePackage.eINSTANCE;
    }

    /**
     * Calls <code>validateXXX</code> for the corresonding classifier of the model. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map context)
    {
        switch (classifierID)
        {
            case AttributePackage.ACTION_VALUE:
                return validateActionValue((ActionValue) value, diagnostics, context);
            case AttributePackage.AXIS_ORIGIN:
                return validateAxisOrigin((AxisOrigin) value, diagnostics, context);
            case AttributePackage.BOUNDS:
                return validateBounds((Bounds) value, diagnostics, context);
            case AttributePackage.COLOR_DEFINITION:
                return validateColorDefinition((ColorDefinition) value, diagnostics, context);
            case AttributePackage.DATA_POINT:
                return validateDataPoint((DataPoint) value, diagnostics, context);
            case AttributePackage.DATA_POINT_COMPONENT:
                return validateDataPointComponent((DataPointComponent) value, diagnostics, context);
            case AttributePackage.DATE_FORMAT_SPECIFIER:
                return validateDateFormatSpecifier((DateFormatSpecifier) value, diagnostics, context);
            case AttributePackage.EXTENDED_PROPERTY:
                return validateExtendedProperty((ExtendedProperty) value, diagnostics, context);
            case AttributePackage.FILL:
                return validateFill((Fill) value, diagnostics, context);
            case AttributePackage.FONT_DEFINITION:
                return validateFontDefinition((FontDefinition) value, diagnostics, context);
            case AttributePackage.FORMAT_SPECIFIER:
                return validateFormatSpecifier((FormatSpecifier) value, diagnostics, context);
            case AttributePackage.GRADIENT:
                return validateGradient((Gradient) value, diagnostics, context);
            case AttributePackage.IMAGE:
                return validateImage((Image) value, diagnostics, context);
            case AttributePackage.INSETS:
                return validateInsets((Insets) value, diagnostics, context);
            case AttributePackage.JAVA_DATE_FORMAT_SPECIFIER:
                return validateJavaDateFormatSpecifier((JavaDateFormatSpecifier) value, diagnostics, context);
            case AttributePackage.JAVA_NUMBER_FORMAT_SPECIFIER:
                return validateJavaNumberFormatSpecifier((JavaNumberFormatSpecifier) value, diagnostics, context);
            case AttributePackage.LINE_ATTRIBUTES:
                return validateLineAttributes((LineAttributes) value, diagnostics, context);
            case AttributePackage.LOCATION:
                return validateLocation((Location) value, diagnostics, context);
            case AttributePackage.MARKER:
                return validateMarker((Marker) value, diagnostics, context);
            case AttributePackage.NUMBER_FORMAT_SPECIFIER:
                return validateNumberFormatSpecifier((NumberFormatSpecifier) value, diagnostics, context);
            case AttributePackage.PALETTE:
                return validatePalette((Palette) value, diagnostics, context);
            case AttributePackage.SCRIPT_VALUE:
                return validateScriptValue((ScriptValue) value, diagnostics, context);
            case AttributePackage.SERIES_VALUE:
                return validateSeriesValue((SeriesValue) value, diagnostics, context);
            case AttributePackage.SIZE:
                return validateSize((Size) value, diagnostics, context);
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
                return validateActionType((Object) value, diagnostics, context);
            case AttributePackage.ANCHOR:
                return validateAnchor((Object) value, diagnostics, context);
            case AttributePackage.AXIS_TYPE:
                return validateAxisType((Object) value, diagnostics, context);
            case AttributePackage.CHART_DIMENSION:
                return validateChartDimension((Object) value, diagnostics, context);
            case AttributePackage.CHART_TYPE:
                return validateChartType((Object) value, diagnostics, context);
            case AttributePackage.DATA_POINT_COMPONENT_TYPE:
                return validateDataPointComponentType((Object) value, diagnostics, context);
            case AttributePackage.DATA_TYPE:
                return validateDataType((Object) value, diagnostics, context);
            case AttributePackage.DATE_FORMAT_DETAIL:
                return validateDateFormatDetail((Object) value, diagnostics, context);
            case AttributePackage.DATE_FORMAT_TYPE:
                return validateDateFormatType((Object) value, diagnostics, context);
            case AttributePackage.DIRECTION:
                return validateDirection((Object) value, diagnostics, context);
            case AttributePackage.GROUPING_UNIT_TYPE:
                return validateGroupingUnitType((Object) value, diagnostics, context);
            case AttributePackage.HORIZONTAL_ALIGNMENT:
                return validateHorizontalAlignment((Object) value, diagnostics, context);
            case AttributePackage.INTERSECTION_TYPE:
                return validateIntersectionType((Object) value, diagnostics, context);
            case AttributePackage.LEADER_LINE_STYLE:
                return validateLeaderLineStyle((Object) value, diagnostics, context);
            case AttributePackage.LEGEND_ITEM_TYPE:
                return validateLegendItemType((Object) value, diagnostics, context);
            case AttributePackage.LINE_STYLE:
                return validateLineStyle((Object) value, diagnostics, context);
            case AttributePackage.MARKER_TYPE:
                return validateMarkerType((Object) value, diagnostics, context);
            case AttributePackage.ORIENTATION:
                return validateOrientation((Object) value, diagnostics, context);
            case AttributePackage.POSITION:
                return validatePosition((Object) value, diagnostics, context);
            case AttributePackage.RISER_TYPE:
                return validateRiserType((Object) value, diagnostics, context);
            case AttributePackage.RULE_TYPE:
                return validateRuleType((Object) value, diagnostics, context);
            case AttributePackage.SCALE_UNIT_TYPE:
                return validateScaleUnitType((Object) value, diagnostics, context);
            case AttributePackage.SORT_OPTION:
                return validateSortOption((Object) value, diagnostics, context);
            case AttributePackage.STRETCH:
                return validateStretch((Object) value, diagnostics, context);
            case AttributePackage.STYLED_COMPONENT:
                return validateStyledComponent((Object) value, diagnostics, context);
            case AttributePackage.TICK_STYLE:
                return validateTickStyle((Object) value, diagnostics, context);
            case AttributePackage.TRIGGER_CONDITION:
                return validateTriggerCondition((Object) value, diagnostics, context);
            case AttributePackage.UNITS_OF_MEASUREMENT:
                return validateUnitsOfMeasurement((Object) value, diagnostics, context);
            case AttributePackage.VERTICAL_ALIGNMENT:
                return validateVerticalAlignment((Object) value, diagnostics, context);
            case AttributePackage.ACTION_TYPE_OBJECT:
                return validateActionTypeObject((ActionType) value, diagnostics, context);
            case AttributePackage.ANCHOR_OBJECT:
                return validateAnchorObject((Anchor) value, diagnostics, context);
            case AttributePackage.AXIS_TYPE_OBJECT:
                return validateAxisTypeObject((AxisType) value, diagnostics, context);
            case AttributePackage.CHART_DIMENSION_OBJECT:
                return validateChartDimensionObject((ChartDimension) value, diagnostics, context);
            case AttributePackage.CHART_TYPE_OBJECT:
                return validateChartTypeObject((ChartType) value, diagnostics, context);
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
            case AttributePackage.INTERSECTION_TYPE_OBJECT:
                return validateIntersectionTypeObject((IntersectionType) value, diagnostics, context);
            case AttributePackage.LEADER_LINE_STYLE_OBJECT:
                return validateLeaderLineStyleObject((LeaderLineStyle) value, diagnostics, context);
            case AttributePackage.LEGEND_ITEM_TYPE_OBJECT:
                return validateLegendItemTypeObject((LegendItemType) value, diagnostics, context);
            case AttributePackage.LINE_STYLE_OBJECT:
                return validateLineStyleObject((LineStyle) value, diagnostics, context);
            case AttributePackage.MARKER_TYPE_OBJECT:
                return validateMarkerTypeObject((MarkerType) value, diagnostics, context);
            case AttributePackage.ORIENTATION_OBJECT:
                return validateOrientationObject((Orientation) value, diagnostics, context);
            case AttributePackage.PERCENTAGE:
                return validatePercentage(((Double) value).doubleValue(), diagnostics, context);
            case AttributePackage.PERCENTAGE_OBJECT:
                return validatePercentageObject((Double) value, diagnostics, context);
            case AttributePackage.POSITION_OBJECT:
                return validatePositionObject((Position) value, diagnostics, context);
            case AttributePackage.RGB_VALUE:
                return validateRGBValue(((Integer) value).intValue(), diagnostics, context);
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
    public boolean validateActionValue(ActionValue actionValue, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(actionValue, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateAxisOrigin(AxisOrigin axisOrigin, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(axisOrigin, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateBounds(Bounds bounds, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(bounds, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateColorDefinition(ColorDefinition colorDefinition, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(colorDefinition, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDataPoint(DataPoint dataPoint, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(dataPoint, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDataPointComponent(DataPointComponent dataPointComponent, DiagnosticChain diagnostics,
        Map context)
    {
        return validate_EveryDefaultConstraint(dataPointComponent, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDateFormatSpecifier(DateFormatSpecifier dateFormatSpecifier, DiagnosticChain diagnostics,
        Map context)
    {
        return validate_EveryDefaultConstraint(dateFormatSpecifier, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateExtendedProperty(ExtendedProperty extendedProperty, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(extendedProperty, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateFill(Fill fill, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(fill, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateFontDefinition(FontDefinition fontDefinition, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(fontDefinition, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateFormatSpecifier(FormatSpecifier formatSpecifier, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(formatSpecifier, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateGradient(Gradient gradient, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(gradient, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateImage(Image image, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(image, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateInsets(Insets insets, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(insets, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateJavaDateFormatSpecifier(JavaDateFormatSpecifier javaDateFormatSpecifier,
        DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(javaDateFormatSpecifier, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateJavaNumberFormatSpecifier(JavaNumberFormatSpecifier javaNumberFormatSpecifier,
        DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(javaNumberFormatSpecifier, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateLineAttributes(LineAttributes lineAttributes, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(lineAttributes, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateLocation(Location location, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(location, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateMarker(Marker marker, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(marker, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateNumberFormatSpecifier(NumberFormatSpecifier numberFormatSpecifier,
        DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(numberFormatSpecifier, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validatePalette(Palette palette, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(palette, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateScriptValue(ScriptValue scriptValue, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(scriptValue, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateSeriesValue(SeriesValue seriesValue, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(seriesValue, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateSize(Size size, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(size, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateStyleMap(StyleMap styleMap, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(styleMap, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateText(Text text, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(text, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateTextAlignment(TextAlignment textAlignment, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(textAlignment, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateTooltipValue(TooltipValue tooltipValue, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(tooltipValue, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateURLValue(URLValue urlValue, DiagnosticChain diagnostics, Map context)
    {
        return validate_EveryDefaultConstraint(urlValue, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateActionType(Object actionType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateAnchor(Object anchor, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateAxisType(Object axisType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateChartDimension(Object chartDimension, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateChartType(Object chartType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDataPointComponentType(Object dataPointComponentType, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDataType(Object dataType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDateFormatDetail(Object dateFormatDetail, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDateFormatType(Object dateFormatType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDirection(Object direction, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateGroupingUnitType(Object groupingUnitType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateHorizontalAlignment(Object horizontalAlignment, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateIntersectionType(Object intersectionType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateLeaderLineStyle(Object leaderLineStyle, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateLegendItemType(Object legendItemType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateLineStyle(Object lineStyle, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateMarkerType(Object markerType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateOrientation(Object orientation, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validatePosition(Object position, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateRiserType(Object riserType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateRuleType(Object ruleType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateScaleUnitType(Object scaleUnitType, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateSortOption(Object sortOption, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateStretch(Object stretch, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateStyledComponent(Object styledComponent, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateTickStyle(Object tickStyle, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateTriggerCondition(Object triggerCondition, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateUnitsOfMeasurement(Object unitsOfMeasurement, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateVerticalAlignment(Object verticalAlignment, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateActionTypeObject(ActionType actionTypeObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateAnchorObject(Anchor anchorObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateAxisTypeObject(AxisType axisTypeObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateChartDimensionObject(ChartDimension chartDimensionObject, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateChartTypeObject(ChartType chartTypeObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDataPointComponentTypeObject(DataPointComponentType dataPointComponentTypeObject,
        DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDataTypeObject(DataType dataTypeObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDateFormatDetailObject(DateFormatDetail dateFormatDetailObject, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDateFormatTypeObject(DateFormatType dateFormatTypeObject, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateDirectionObject(Direction directionObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateGroupingUnitTypeObject(GroupingUnitType groupingUnitTypeObject, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateHorizontalAlignmentObject(HorizontalAlignment horizontalAlignmentObject,
        DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateID(String id, DiagnosticChain diagnostics, Map context)
    {
        boolean result = validateID_Pattern(id, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @see #validateID_Pattern
     */
    public static final PatternMatcher[][] ID__PATTERN__VALUES = new PatternMatcher[][]
    {
        new PatternMatcher[]
        {
            XMLTypeUtil.createPatternMatcher("[A-Z]") //$NON-NLS-1$
        }
    };

    /**
     * Validates the Pattern constraint of '<em>ID</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateID_Pattern(String id, DiagnosticChain diagnostics, Map context)
    {
        return validatePattern(AttributePackage.eINSTANCE.getID(), id, ID__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateIntersectionTypeObject(IntersectionType intersectionTypeObject, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateLeaderLineStyleObject(LeaderLineStyle leaderLineStyleObject, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateLegendItemTypeObject(LegendItemType legendItemTypeObject, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateLineStyleObject(LineStyle lineStyleObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateMarkerTypeObject(MarkerType markerTypeObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateOrientationObject(Orientation orientationObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validatePercentage(double percentage, DiagnosticChain diagnostics, Map context)
    {
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
     * Validates the Min constraint of '<em>Percentage</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validatePercentage_Min(double percentage, DiagnosticChain diagnostics, Map context)
    {
        boolean result = percentage >= PERCENTAGE__MIN__VALUE;
        if (!result && diagnostics != null)
            reportMinViolation(AttributePackage.eINSTANCE.getPercentage(), new Double(percentage), new Double(
                PERCENTAGE__MIN__VALUE), true, diagnostics, context);
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
     * Validates the Max constraint of '<em>Percentage</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validatePercentage_Max(double percentage, DiagnosticChain diagnostics, Map context)
    {
        boolean result = percentage <= PERCENTAGE__MAX__VALUE;
        if (!result && diagnostics != null)
            reportMaxViolation(AttributePackage.eINSTANCE.getPercentage(), new Double(percentage), new Double(
                PERCENTAGE__MAX__VALUE), true, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validatePercentageObject(Double percentageObject, DiagnosticChain diagnostics, Map context)
    {
        boolean result = validatePercentage_Min(percentageObject.doubleValue(), diagnostics, context);
        if (result || diagnostics != null)
            result &= validatePercentage_Max(percentageObject.doubleValue(), diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validatePositionObject(Position positionObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateRGBValue(int rgbValue, DiagnosticChain diagnostics, Map context)
    {
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
     * Validates the Min constraint of '<em>RGB Value</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateRGBValue_Min(int rgbValue, DiagnosticChain diagnostics, Map context)
    {
        boolean result = rgbValue >= RGB_VALUE__MIN__VALUE;
        if (!result && diagnostics != null)
            reportMinViolation(AttributePackage.eINSTANCE.getRGBValue(), new Integer(rgbValue), new Integer(
                RGB_VALUE__MIN__VALUE), true, diagnostics, context);
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
     * Validates the Max constraint of '<em>RGB Value</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateRGBValue_Max(int rgbValue, DiagnosticChain diagnostics, Map context)
    {
        boolean result = rgbValue <= RGB_VALUE__MAX__VALUE;
        if (!result && diagnostics != null)
            reportMaxViolation(AttributePackage.eINSTANCE.getRGBValue(), new Integer(rgbValue), new Integer(
                RGB_VALUE__MAX__VALUE), true, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateRGBValueObject(Integer rgbValueObject, DiagnosticChain diagnostics, Map context)
    {
        boolean result = validateRGBValue_Min(rgbValueObject.intValue(), diagnostics, context);
        if (result || diagnostics != null)
            result &= validateRGBValue_Max(rgbValueObject.intValue(), diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateRiserTypeObject(RiserType riserTypeObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateRuleTypeObject(RuleType ruleTypeObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateScaleUnitTypeObject(ScaleUnitType scaleUnitTypeObject, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateSortOptionObject(SortOption sortOptionObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateStretchObject(Stretch stretchObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateStyledComponentObject(StyledComponent styledComponentObject, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateTickStyleObject(TickStyle tickStyleObject, DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateTriggerConditionObject(TriggerCondition triggerConditionObject, DiagnosticChain diagnostics,
        Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateUnitsOfMeasurementObject(UnitsOfMeasurement unitsOfMeasurementObject,
        DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean validateVerticalAlignmentObject(VerticalAlignment verticalAlignmentObject,
        DiagnosticChain diagnostics, Map context)
    {
        return true;
    }

} //AttributeValidator
