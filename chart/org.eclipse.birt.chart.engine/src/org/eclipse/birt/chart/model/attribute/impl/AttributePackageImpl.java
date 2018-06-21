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

import java.util.Map;

import org.eclipse.birt.chart.model.ModelPackage;
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
import org.eclipse.birt.chart.model.attribute.util.AttributeValidator;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.impl.DataPackageImpl;
import org.eclipse.birt.chart.model.impl.ModelPackageImpl;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.impl.LayoutPackageImpl;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.type.impl.TypePackageImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package </b>. <!--
 * end-user-doc -->
 * @generated
 */
public class AttributePackageImpl extends EPackageImpl implements
		AttributePackage
{

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass accessibilityValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass actionValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass angle3DEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass axisOriginEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass boundsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass callBackValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass colorDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cursorEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataPointEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataPointComponentEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dateFormatSpecifierEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass embeddedImageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eStringToStringMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass extendedPropertyEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fillEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fontDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass formatSpecifierEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fractionNumberFormatSpecifierEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass gradientEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass imageEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass insetsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass interactivityEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass javaDateFormatSpecifierEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass javaNumberFormatSpecifierEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lineAttributesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass locationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass location3DEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass markerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass multipleFillEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass multiURLValuesEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass numberFormatSpecifierEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass paletteEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass patternImageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass rotation3DEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scriptValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass seriesValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sizeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stringFormatSpecifierEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass styleEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass styleMapEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass textEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass textAlignmentEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tooltipValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass urlValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum actionTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum anchorEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum angleTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum axisTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum chartDimensionEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum chartTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum cursorTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum dataPointComponentTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum dataTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum dateFormatDetailEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum dateFormatTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum directionEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum groupingUnitTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum horizontalAlignmentEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum imageSourceTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum intersectionTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum leaderLineStyleEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum legendBehaviorTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum legendItemTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum lineDecoratorEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum lineStyleEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum markerTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum menuStylesKeyTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum orientationEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum positionEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum riserTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum ruleTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum scaleUnitTypeEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum sortOptionEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum stretchEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum styledComponentEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum tickStyleEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum triggerConditionEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum triggerFlowEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum unitsOfMeasurementEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum verticalAlignmentEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType actionTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType anchorObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType angleTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType axisTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType chartDimensionObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType chartTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType cursorTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType dataPointComponentTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType dataTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType dateFormatDetailObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType dateFormatTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType directionObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType groupingUnitTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType horizontalAlignmentObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType idEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType imageSourceTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType intersectionTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType leaderLineStyleObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType legendBehaviorTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType legendItemTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType lineDecoratorObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType lineStyleObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType markerTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType menuStylesKeyTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType orientationObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType patternBitmapEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType patternBitmapObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType percentageEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType percentageObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType positionObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType rgbValueEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType rgbValueObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType riserTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType ruleTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType scaleUnitTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType sortOptionObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType stretchObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType styledComponentObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType tickStyleObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType triggerConditionObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType triggerFlowObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType unitsOfMeasurementObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType verticalAlignmentObjectEDataType = null;

	/**
	 * Creates an instance of the model <b>Package </b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry}by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory
	 * method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AttributePackageImpl( )
	{
		super( eNS_URI, AttributeFactory.eINSTANCE );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link AttributePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AttributePackage init( )
	{
		if ( isInited )
			return (AttributePackage) EPackage.Registry.INSTANCE.getEPackage( AttributePackage.eNS_URI );

		// Obtain or create and register package
		AttributePackageImpl theAttributePackage = (AttributePackageImpl) ( EPackage.Registry.INSTANCE.get( eNS_URI ) instanceof AttributePackageImpl ? EPackage.Registry.INSTANCE.get( eNS_URI )
				: new AttributePackageImpl( ) );

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass( );

		// Obtain or create and register interdependencies
		ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI ) instanceof ComponentPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI )
				: ComponentPackage.eINSTANCE );
		DataPackageImpl theDataPackage = (DataPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( DataPackage.eNS_URI ) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( DataPackage.eNS_URI )
				: DataPackage.eINSTANCE );
		TypePackageImpl theTypePackage = (TypePackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( TypePackage.eNS_URI ) instanceof TypePackageImpl ? EPackage.Registry.INSTANCE.getEPackage( TypePackage.eNS_URI )
				: TypePackage.eINSTANCE );
		LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI ) instanceof LayoutPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( LayoutPackage.eNS_URI )
				: LayoutPackage.eINSTANCE );
		ModelPackageImpl theModelPackage = (ModelPackageImpl) ( EPackage.Registry.INSTANCE.getEPackage( ModelPackage.eNS_URI ) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage( ModelPackage.eNS_URI )
				: ModelPackage.eINSTANCE );

		// Create package meta-data objects
		theAttributePackage.createPackageContents( );
		theComponentPackage.createPackageContents( );
		theDataPackage.createPackageContents( );
		theTypePackage.createPackageContents( );
		theLayoutPackage.createPackageContents( );
		theModelPackage.createPackageContents( );

		// Initialize created meta-data
		theAttributePackage.initializePackageContents( );
		theComponentPackage.initializePackageContents( );
		theDataPackage.initializePackageContents( );
		theTypePackage.initializePackageContents( );
		theLayoutPackage.initializePackageContents( );
		theModelPackage.initializePackageContents( );

		// Register package validator
		EValidator.Registry.INSTANCE.put( theAttributePackage,
				new EValidator.Descriptor( ) {

					public EValidator getEValidator( )
					{
						return AttributeValidator.INSTANCE;
					}
				} );

		// Mark meta-data to indicate it can't be changed
		theAttributePackage.freeze( );

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put( AttributePackage.eNS_URI,
				theAttributePackage );
		return theAttributePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAccessibilityValue( )
	{
		return accessibilityValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAccessibilityValue_Text( )
	{
		return (EAttribute) accessibilityValueEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAccessibilityValue_Accessibility( )
	{
		return (EAttribute) accessibilityValueEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getActionValue( )
	{
		return actionValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getActionValue_Label( )
	{
		return (EReference) actionValueEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAngle3D( )
	{
		return angle3DEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAngle3D_XAngle( )
	{
		return (EAttribute) angle3DEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAngle3D_YAngle( )
	{
		return (EAttribute) angle3DEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAngle3D_ZAngle( )
	{
		return (EAttribute) angle3DEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAngle3D_Type( )
	{
		return (EAttribute) angle3DEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAxisOrigin( )
	{
		return axisOriginEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAxisOrigin_Type( )
	{
		return (EAttribute) axisOriginEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAxisOrigin_Value( )
	{
		return (EReference) axisOriginEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBounds( )
	{
		return boundsEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBounds_Left( )
	{
		return (EAttribute) boundsEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBounds_Top( )
	{
		return (EAttribute) boundsEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBounds_Width( )
	{
		return (EAttribute) boundsEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBounds_Height( )
	{
		return (EAttribute) boundsEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCallBackValue( )
	{
		return callBackValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCallBackValue_Identifier( )
	{
		return (EAttribute) callBackValueEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getColorDefinition( )
	{
		return colorDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getColorDefinition_Transparency( )
	{
		return (EAttribute) colorDefinitionEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getColorDefinition_Red( )
	{
		return (EAttribute) colorDefinitionEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getColorDefinition_Blue( )
	{
		return (EAttribute) colorDefinitionEClass.getEStructuralFeatures( )
				.get( 3 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCursor( )
	{
		return cursorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCursor_Type( )
	{
		return (EAttribute) cursorEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCursor_Image( )
	{
		return (EReference) cursorEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getColorDefinition_Green( )
	{
		return (EAttribute) colorDefinitionEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataPoint( )
	{
		return dataPointEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataPoint_Components( )
	{
		return (EReference) dataPointEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataPoint_Prefix( )
	{
		return (EAttribute) dataPointEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataPoint_Suffix( )
	{
		return (EAttribute) dataPointEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataPoint_Separator( )
	{
		return (EAttribute) dataPointEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataPointComponent( )
	{
		return dataPointComponentEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataPointComponent_Type( )
	{
		return (EAttribute) dataPointComponentEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataPointComponent_FormatSpecifier( )
	{
		return (EReference) dataPointComponentEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataPointComponent_OrthogonalType( )
	{
		return (EAttribute) dataPointComponentEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDateFormatSpecifier( )
	{
		return dateFormatSpecifierEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDateFormatSpecifier_Type( )
	{
		return (EAttribute) dateFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDateFormatSpecifier_Detail( )
	{
		return (EAttribute) dateFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEmbeddedImage( )
	{
		return embeddedImageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEmbeddedImage_Data( )
	{
		return (EAttribute) embeddedImageEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEStringToStringMapEntry( )
	{
		return eStringToStringMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEStringToStringMapEntry_Key( )
	{
		return (EAttribute) eStringToStringMapEntryEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEStringToStringMapEntry_Value( )
	{
		return (EAttribute) eStringToStringMapEntryEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExtendedProperty( )
	{
		return extendedPropertyEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExtendedProperty_Name( )
	{
		return (EAttribute) extendedPropertyEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExtendedProperty_Value( )
	{
		return (EAttribute) extendedPropertyEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFill( )
	{
		return fillEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFill_Type( )
	{
		return (EAttribute) fillEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFontDefinition( )
	{
		return fontDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFontDefinition_Name( )
	{
		return (EAttribute) fontDefinitionEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFontDefinition_Size( )
	{
		return (EAttribute) fontDefinitionEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFontDefinition_Bold( )
	{
		return (EAttribute) fontDefinitionEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFontDefinition_Italic( )
	{
		return (EAttribute) fontDefinitionEClass.getEStructuralFeatures( )
				.get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFontDefinition_Strikethrough( )
	{
		return (EAttribute) fontDefinitionEClass.getEStructuralFeatures( )
				.get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFontDefinition_Underline( )
	{
		return (EAttribute) fontDefinitionEClass.getEStructuralFeatures( )
				.get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFontDefinition_WordWrap( )
	{
		return (EAttribute) fontDefinitionEClass.getEStructuralFeatures( )
				.get( 6 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFontDefinition_Alignment( )
	{
		return (EReference) fontDefinitionEClass.getEStructuralFeatures( )
				.get( 7 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFontDefinition_Rotation( )
	{
		return (EAttribute) fontDefinitionEClass.getEStructuralFeatures( )
				.get( 8 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFormatSpecifier( )
	{
		return formatSpecifierEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFractionNumberFormatSpecifier( )
	{
		return fractionNumberFormatSpecifierEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFractionNumberFormatSpecifier_Precise( )
	{
		return (EAttribute) fractionNumberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFractionNumberFormatSpecifier_FractionDigits( )
	{
		return (EAttribute) fractionNumberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFractionNumberFormatSpecifier_Numerator( )
	{
		return (EAttribute) fractionNumberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFractionNumberFormatSpecifier_Prefix( )
	{
		return (EAttribute) fractionNumberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 3 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFractionNumberFormatSpecifier_Suffix( )
	{
		return (EAttribute) fractionNumberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 4 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFractionNumberFormatSpecifier_Delimiter( )
	{
		return (EAttribute) fractionNumberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGradient( )
	{
		return gradientEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGradient_StartColor( )
	{
		return (EReference) gradientEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGradient_EndColor( )
	{
		return (EReference) gradientEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGradient_Direction( )
	{
		return (EAttribute) gradientEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGradient_Cyclic( )
	{
		return (EAttribute) gradientEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGradient_Transparency( )
	{
		return (EAttribute) gradientEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getImage( )
	{
		return imageEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getImage_URL( )
	{
		return (EAttribute) imageEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getImage_Source( )
	{
		return (EAttribute) imageEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInsets( )
	{
		return insetsEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInsets_Top( )
	{
		return (EAttribute) insetsEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInsets_Left( )
	{
		return (EAttribute) insetsEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInsets_Bottom( )
	{
		return (EAttribute) insetsEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInsets_Right( )
	{
		return (EAttribute) insetsEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInteractivity( )
	{
		return interactivityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInteractivity_Enable( )
	{
		return (EAttribute) interactivityEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInteractivity_LegendBehavior( )
	{
		return (EAttribute) interactivityEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJavaDateFormatSpecifier( )
	{
		return javaDateFormatSpecifierEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJavaDateFormatSpecifier_Pattern( )
	{
		return (EAttribute) javaDateFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJavaNumberFormatSpecifier( )
	{
		return javaNumberFormatSpecifierEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJavaNumberFormatSpecifier_Pattern( )
	{
		return (EAttribute) javaNumberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJavaNumberFormatSpecifier_Multiplier( )
	{
		return (EAttribute) javaNumberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLineAttributes( )
	{
		return lineAttributesEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLineAttributes_Style( )
	{
		return (EAttribute) lineAttributesEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLineAttributes_Thickness( )
	{
		return (EAttribute) lineAttributesEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLineAttributes_Color( )
	{
		return (EReference) lineAttributesEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLineAttributes_Visible( )
	{
		return (EAttribute) lineAttributesEClass.getEStructuralFeatures( )
				.get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLocation( )
	{
		return locationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocation_X( )
	{
		return (EAttribute) locationEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocation_Y( )
	{
		return (EAttribute) locationEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLocation3D( )
	{
		return location3DEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocation3D_Z( )
	{
		return (EAttribute) location3DEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMarker( )
	{
		return markerEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMarker_Type( )
	{
		return (EAttribute) markerEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMarker_Size( )
	{
		return (EAttribute) markerEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMarker_Visible( )
	{
		return (EAttribute) markerEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMarker_Fill( )
	{
		return (EReference) markerEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMarker_IconPalette( )
	{
		return (EReference) markerEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMarker_Outline( )
	{
		return (EReference) markerEClass.getEStructuralFeatures( ).get( 5 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMultipleFill( )
	{
		return multipleFillEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMultipleFill_Fills( )
	{
		return (EReference) multipleFillEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMultiURLValues( )
	{
		return multiURLValuesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMultiURLValues_URLValues( )
	{
		return (EReference) multiURLValuesEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMultiURLValues_Tooltip( )
	{
		return (EAttribute) multiURLValuesEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMultiURLValues_PropertiesMap( )
	{
		return (EReference) multiURLValuesEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNumberFormatSpecifier( )
	{
		return numberFormatSpecifierEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNumberFormatSpecifier_Prefix( )
	{
		return (EAttribute) numberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNumberFormatSpecifier_Suffix( )
	{
		return (EAttribute) numberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNumberFormatSpecifier_Multiplier( )
	{
		return (EAttribute) numberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNumberFormatSpecifier_FractionDigits( )
	{
		return (EAttribute) numberFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPalette( )
	{
		return paletteEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPalette_Name( )
	{
		return (EAttribute) paletteEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPalette_Entries( )
	{
		return (EReference) paletteEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPatternImage( )
	{
		return patternImageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPatternImage_Bitmap( )
	{
		return (EAttribute) patternImageEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPatternImage_ForeColor( )
	{
		return (EReference) patternImageEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPatternImage_BackColor( )
	{
		return (EReference) patternImageEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRotation3D( )
	{
		return rotation3DEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRotation3D_Angles( )
	{
		return (EReference) rotation3DEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScriptValue( )
	{
		return scriptValueEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScriptValue_Script( )
	{
		return (EAttribute) scriptValueEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSeriesValue( )
	{
		return seriesValueEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSeriesValue_Name( )
	{
		return (EAttribute) seriesValueEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSize( )
	{
		return sizeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSize_Height( )
	{
		return (EAttribute) sizeEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSize_Width( )
	{
		return (EAttribute) sizeEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStringFormatSpecifier( )
	{
		return stringFormatSpecifierEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringFormatSpecifier_Pattern( )
	{
		return (EAttribute) stringFormatSpecifierEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStyle( )
	{
		return styleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStyle_Font( )
	{
		return (EReference) styleEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStyle_Color( )
	{
		return (EReference) styleEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStyle_BackgroundColor( )
	{
		return (EReference) styleEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStyle_BackgroundImage( )
	{
		return (EReference) styleEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStyle_Padding( )
	{
		return (EReference) styleEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStyleMap( )
	{
		return styleMapEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStyleMap_ComponentName( )
	{
		return (EAttribute) styleMapEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStyleMap_Style( )
	{
		return (EReference) styleMapEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getText( )
	{
		return textEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getText_Value( )
	{
		return (EAttribute) textEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getText_Font( )
	{
		return (EReference) textEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getText_Color( )
	{
		return (EReference) textEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTextAlignment( )
	{
		return textAlignmentEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTextAlignment_HorizontalAlignment( )
	{
		return (EAttribute) textAlignmentEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTextAlignment_VerticalAlignment( )
	{
		return (EAttribute) textAlignmentEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTooltipValue( )
	{
		return tooltipValueEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTooltipValue_Text( )
	{
		return (EAttribute) tooltipValueEClass.getEStructuralFeatures( )
				.get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTooltipValue_Delay( )
	{
		return (EAttribute) tooltipValueEClass.getEStructuralFeatures( )
				.get( 1 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTooltipValue_FormatSpecifier( )
	{
		return (EReference) tooltipValueEClass.getEStructuralFeatures( )
				.get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getURLValue( )
	{
		return urlValueEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getURLValue_BaseUrl( )
	{
		return (EAttribute) urlValueEClass.getEStructuralFeatures( ).get( 0 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getURLValue_Target( )
	{
		return (EAttribute) urlValueEClass.getEStructuralFeatures( ).get( 1 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getURLValue_BaseParameterName( )
	{
		return (EAttribute) urlValueEClass.getEStructuralFeatures( ).get( 2 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getURLValue_ValueParameterName( )
	{
		return (EAttribute) urlValueEClass.getEStructuralFeatures( ).get( 3 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getURLValue_SeriesParameterName( )
	{
		return (EAttribute) urlValueEClass.getEStructuralFeatures( ).get( 4 );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getURLValue_Tooltip( )
	{
		return (EAttribute) urlValueEClass.getEStructuralFeatures( ).get( 5 );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getActionType( )
	{
		return actionTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getAnchor( )
	{
		return anchorEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getAngleType( )
	{
		return angleTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getChartType( )
	{
		return chartTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getCursorType( )
	{
		return cursorTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDataPointComponentType( )
	{
		return dataPointComponentTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDataType( )
	{
		return dataTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDateFormatDetail( )
	{
		return dateFormatDetailEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDateFormatType( )
	{
		return dateFormatTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDirection( )
	{
		return directionEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getGroupingUnitType( )
	{
		return groupingUnitTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getHorizontalAlignment( )
	{
		return horizontalAlignmentEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getImageSourceType( )
	{
		return imageSourceTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getIntersectionType( )
	{
		return intersectionTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLeaderLineStyle( )
	{
		return leaderLineStyleEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLegendBehaviorType( )
	{
		return legendBehaviorTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLegendItemType( )
	{
		return legendItemTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLineDecorator( )
	{
		return lineDecoratorEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLineStyle( )
	{
		return lineStyleEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getMarkerType( )
	{
		return markerTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getMenuStylesKeyType( )
	{
		return menuStylesKeyTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getOrientation( )
	{
		return orientationEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getPosition( )
	{
		return positionEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getRiserType( )
	{
		return riserTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getRuleType( )
	{
		return ruleTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getScaleUnitType( )
	{
		return scaleUnitTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSortOption( )
	{
		return sortOptionEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getStretch( )
	{
		return stretchEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getStyledComponent( )
	{
		return styledComponentEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getTickStyle( )
	{
		return tickStyleEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getTriggerCondition( )
	{
		return triggerConditionEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getTriggerFlow( )
	{
		return triggerFlowEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getUnitsOfMeasurement( )
	{
		return unitsOfMeasurementEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getVerticalAlignment( )
	{
		return verticalAlignmentEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getActionTypeObject( )
	{
		return actionTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getAnchorObject( )
	{
		return anchorObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getAngleTypeObject( )
	{
		return angleTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getAxisTypeObject( )
	{
		return axisTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getChartDimensionObject( )
	{
		return chartDimensionObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getAxisType( )
	{
		return axisTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getChartDimension( )
	{
		return chartDimensionEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getChartTypeObject( )
	{
		return chartTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getCursorTypeObject( )
	{
		return cursorTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDataPointComponentTypeObject( )
	{
		return dataPointComponentTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDataTypeObject( )
	{
		return dataTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDateFormatDetailObject( )
	{
		return dateFormatDetailObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDateFormatTypeObject( )
	{
		return dateFormatTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDirectionObject( )
	{
		return directionObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getGroupingUnitTypeObject( )
	{
		return groupingUnitTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getHorizontalAlignmentObject( )
	{
		return horizontalAlignmentObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getID( )
	{
		return idEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getImageSourceTypeObject( )
	{
		return imageSourceTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIntersectionTypeObject( )
	{
		return intersectionTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getLeaderLineStyleObject( )
	{
		return leaderLineStyleObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getLegendBehaviorTypeObject( )
	{
		return legendBehaviorTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getLegendItemTypeObject( )
	{
		return legendItemTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getLineDecoratorObject( )
	{
		return lineDecoratorObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getLineStyleObject( )
	{
		return lineStyleObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getMarkerTypeObject( )
	{
		return markerTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getMenuStylesKeyTypeObject( )
	{
		return menuStylesKeyTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getOrientationObject( )
	{
		return orientationObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPatternBitmap( )
	{
		return patternBitmapEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPatternBitmapObject( )
	{
		return patternBitmapObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPercentage( )
	{
		return percentageEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPercentageObject( )
	{
		return percentageObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPositionObject( )
	{
		return positionObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getRGBValue( )
	{
		return rgbValueEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getRGBValueObject( )
	{
		return rgbValueObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getRiserTypeObject( )
	{
		return riserTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getRuleTypeObject( )
	{
		return ruleTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getScaleUnitTypeObject( )
	{
		return scaleUnitTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getSortOptionObject( )
	{
		return sortOptionObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getStretchObject( )
	{
		return stretchObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getStyledComponentObject( )
	{
		return styledComponentObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTickStyleObject( )
	{
		return tickStyleObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTriggerConditionObject( )
	{
		return triggerConditionObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTriggerFlowObject( )
	{
		return triggerFlowObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getUnitsOfMeasurementObject( )
	{
		return unitsOfMeasurementObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getVerticalAlignmentObject( )
	{
		return verticalAlignmentObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeFactory getAttributeFactory( )
	{
		return (AttributeFactory) getEFactoryInstance( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents( )
	{
		if ( isCreated )
			return;
		isCreated = true;

		// Create classes and their features
		accessibilityValueEClass = createEClass( ACCESSIBILITY_VALUE );
		createEAttribute( accessibilityValueEClass, ACCESSIBILITY_VALUE__TEXT );
		createEAttribute( accessibilityValueEClass,
				ACCESSIBILITY_VALUE__ACCESSIBILITY );

		actionValueEClass = createEClass( ACTION_VALUE );
		createEReference( actionValueEClass, ACTION_VALUE__LABEL );

		angle3DEClass = createEClass( ANGLE3_D );
		createEAttribute( angle3DEClass, ANGLE3_D__XANGLE );
		createEAttribute( angle3DEClass, ANGLE3_D__YANGLE );
		createEAttribute( angle3DEClass, ANGLE3_D__ZANGLE );
		createEAttribute( angle3DEClass, ANGLE3_D__TYPE );

		axisOriginEClass = createEClass( AXIS_ORIGIN );
		createEAttribute( axisOriginEClass, AXIS_ORIGIN__TYPE );
		createEReference( axisOriginEClass, AXIS_ORIGIN__VALUE );

		boundsEClass = createEClass( BOUNDS );
		createEAttribute( boundsEClass, BOUNDS__LEFT );
		createEAttribute( boundsEClass, BOUNDS__TOP );
		createEAttribute( boundsEClass, BOUNDS__WIDTH );
		createEAttribute( boundsEClass, BOUNDS__HEIGHT );

		callBackValueEClass = createEClass( CALL_BACK_VALUE );
		createEAttribute( callBackValueEClass, CALL_BACK_VALUE__IDENTIFIER );

		colorDefinitionEClass = createEClass( COLOR_DEFINITION );
		createEAttribute( colorDefinitionEClass, COLOR_DEFINITION__TRANSPARENCY );
		createEAttribute( colorDefinitionEClass, COLOR_DEFINITION__RED );
		createEAttribute( colorDefinitionEClass, COLOR_DEFINITION__GREEN );
		createEAttribute( colorDefinitionEClass, COLOR_DEFINITION__BLUE );

		cursorEClass = createEClass( CURSOR );
		createEAttribute( cursorEClass, CURSOR__TYPE );
		createEReference( cursorEClass, CURSOR__IMAGE );

		dataPointEClass = createEClass( DATA_POINT );
		createEReference( dataPointEClass, DATA_POINT__COMPONENTS );
		createEAttribute( dataPointEClass, DATA_POINT__PREFIX );
		createEAttribute( dataPointEClass, DATA_POINT__SUFFIX );
		createEAttribute( dataPointEClass, DATA_POINT__SEPARATOR );

		dataPointComponentEClass = createEClass( DATA_POINT_COMPONENT );
		createEAttribute( dataPointComponentEClass, DATA_POINT_COMPONENT__TYPE );
		createEReference( dataPointComponentEClass,
				DATA_POINT_COMPONENT__FORMAT_SPECIFIER );
		createEAttribute( dataPointComponentEClass,
				DATA_POINT_COMPONENT__ORTHOGONAL_TYPE );

		dateFormatSpecifierEClass = createEClass( DATE_FORMAT_SPECIFIER );
		createEAttribute( dateFormatSpecifierEClass,
				DATE_FORMAT_SPECIFIER__TYPE );
		createEAttribute( dateFormatSpecifierEClass,
				DATE_FORMAT_SPECIFIER__DETAIL );

		embeddedImageEClass = createEClass( EMBEDDED_IMAGE );
		createEAttribute( embeddedImageEClass, EMBEDDED_IMAGE__DATA );

		eStringToStringMapEntryEClass = createEClass( ESTRING_TO_STRING_MAP_ENTRY );
		createEAttribute( eStringToStringMapEntryEClass,
				ESTRING_TO_STRING_MAP_ENTRY__KEY );
		createEAttribute( eStringToStringMapEntryEClass,
				ESTRING_TO_STRING_MAP_ENTRY__VALUE );

		extendedPropertyEClass = createEClass( EXTENDED_PROPERTY );
		createEAttribute( extendedPropertyEClass, EXTENDED_PROPERTY__NAME );
		createEAttribute( extendedPropertyEClass, EXTENDED_PROPERTY__VALUE );

		fillEClass = createEClass( FILL );
		createEAttribute( fillEClass, FILL__TYPE );

		fontDefinitionEClass = createEClass( FONT_DEFINITION );
		createEAttribute( fontDefinitionEClass, FONT_DEFINITION__NAME );
		createEAttribute( fontDefinitionEClass, FONT_DEFINITION__SIZE );
		createEAttribute( fontDefinitionEClass, FONT_DEFINITION__BOLD );
		createEAttribute( fontDefinitionEClass, FONT_DEFINITION__ITALIC );
		createEAttribute( fontDefinitionEClass, FONT_DEFINITION__STRIKETHROUGH );
		createEAttribute( fontDefinitionEClass, FONT_DEFINITION__UNDERLINE );
		createEAttribute( fontDefinitionEClass, FONT_DEFINITION__WORD_WRAP );
		createEReference( fontDefinitionEClass, FONT_DEFINITION__ALIGNMENT );
		createEAttribute( fontDefinitionEClass, FONT_DEFINITION__ROTATION );

		formatSpecifierEClass = createEClass( FORMAT_SPECIFIER );

		fractionNumberFormatSpecifierEClass = createEClass( FRACTION_NUMBER_FORMAT_SPECIFIER );
		createEAttribute( fractionNumberFormatSpecifierEClass,
				FRACTION_NUMBER_FORMAT_SPECIFIER__PRECISE );
		createEAttribute( fractionNumberFormatSpecifierEClass,
				FRACTION_NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS );
		createEAttribute( fractionNumberFormatSpecifierEClass,
				FRACTION_NUMBER_FORMAT_SPECIFIER__NUMERATOR );
		createEAttribute( fractionNumberFormatSpecifierEClass,
				FRACTION_NUMBER_FORMAT_SPECIFIER__PREFIX );
		createEAttribute( fractionNumberFormatSpecifierEClass,
				FRACTION_NUMBER_FORMAT_SPECIFIER__SUFFIX );
		createEAttribute( fractionNumberFormatSpecifierEClass,
				FRACTION_NUMBER_FORMAT_SPECIFIER__DELIMITER );

		gradientEClass = createEClass( GRADIENT );
		createEReference( gradientEClass, GRADIENT__START_COLOR );
		createEReference( gradientEClass, GRADIENT__END_COLOR );
		createEAttribute( gradientEClass, GRADIENT__DIRECTION );
		createEAttribute( gradientEClass, GRADIENT__CYCLIC );
		createEAttribute( gradientEClass, GRADIENT__TRANSPARENCY );

		imageEClass = createEClass( IMAGE );
		createEAttribute( imageEClass, IMAGE__URL );
		createEAttribute( imageEClass, IMAGE__SOURCE );

		insetsEClass = createEClass( INSETS );
		createEAttribute( insetsEClass, INSETS__TOP );
		createEAttribute( insetsEClass, INSETS__LEFT );
		createEAttribute( insetsEClass, INSETS__BOTTOM );
		createEAttribute( insetsEClass, INSETS__RIGHT );

		interactivityEClass = createEClass( INTERACTIVITY );
		createEAttribute( interactivityEClass, INTERACTIVITY__ENABLE );
		createEAttribute( interactivityEClass, INTERACTIVITY__LEGEND_BEHAVIOR );

		javaDateFormatSpecifierEClass = createEClass( JAVA_DATE_FORMAT_SPECIFIER );
		createEAttribute( javaDateFormatSpecifierEClass,
				JAVA_DATE_FORMAT_SPECIFIER__PATTERN );

		javaNumberFormatSpecifierEClass = createEClass( JAVA_NUMBER_FORMAT_SPECIFIER );
		createEAttribute( javaNumberFormatSpecifierEClass,
				JAVA_NUMBER_FORMAT_SPECIFIER__PATTERN );
		createEAttribute( javaNumberFormatSpecifierEClass,
				JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER );

		lineAttributesEClass = createEClass( LINE_ATTRIBUTES );
		createEAttribute( lineAttributesEClass, LINE_ATTRIBUTES__STYLE );
		createEAttribute( lineAttributesEClass, LINE_ATTRIBUTES__THICKNESS );
		createEReference( lineAttributesEClass, LINE_ATTRIBUTES__COLOR );
		createEAttribute( lineAttributesEClass, LINE_ATTRIBUTES__VISIBLE );

		locationEClass = createEClass( LOCATION );
		createEAttribute( locationEClass, LOCATION__X );
		createEAttribute( locationEClass, LOCATION__Y );

		location3DEClass = createEClass( LOCATION3_D );
		createEAttribute( location3DEClass, LOCATION3_D__Z );

		markerEClass = createEClass( MARKER );
		createEAttribute( markerEClass, MARKER__TYPE );
		createEAttribute( markerEClass, MARKER__SIZE );
		createEAttribute( markerEClass, MARKER__VISIBLE );
		createEReference( markerEClass, MARKER__FILL );
		createEReference( markerEClass, MARKER__ICON_PALETTE );
		createEReference( markerEClass, MARKER__OUTLINE );

		multipleFillEClass = createEClass( MULTIPLE_FILL );
		createEReference( multipleFillEClass, MULTIPLE_FILL__FILLS );

		multiURLValuesEClass = createEClass( MULTI_URL_VALUES );
		createEReference( multiURLValuesEClass, MULTI_URL_VALUES__URL_VALUES );
		createEAttribute( multiURLValuesEClass, MULTI_URL_VALUES__TOOLTIP );
		createEReference( multiURLValuesEClass,
				MULTI_URL_VALUES__PROPERTIES_MAP );

		numberFormatSpecifierEClass = createEClass( NUMBER_FORMAT_SPECIFIER );
		createEAttribute( numberFormatSpecifierEClass,
				NUMBER_FORMAT_SPECIFIER__PREFIX );
		createEAttribute( numberFormatSpecifierEClass,
				NUMBER_FORMAT_SPECIFIER__SUFFIX );
		createEAttribute( numberFormatSpecifierEClass,
				NUMBER_FORMAT_SPECIFIER__MULTIPLIER );
		createEAttribute( numberFormatSpecifierEClass,
				NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS );

		paletteEClass = createEClass( PALETTE );
		createEAttribute( paletteEClass, PALETTE__NAME );
		createEReference( paletteEClass, PALETTE__ENTRIES );

		patternImageEClass = createEClass( PATTERN_IMAGE );
		createEAttribute( patternImageEClass, PATTERN_IMAGE__BITMAP );
		createEReference( patternImageEClass, PATTERN_IMAGE__FORE_COLOR );
		createEReference( patternImageEClass, PATTERN_IMAGE__BACK_COLOR );

		rotation3DEClass = createEClass( ROTATION3_D );
		createEReference( rotation3DEClass, ROTATION3_D__ANGLES );

		scriptValueEClass = createEClass( SCRIPT_VALUE );
		createEAttribute( scriptValueEClass, SCRIPT_VALUE__SCRIPT );

		seriesValueEClass = createEClass( SERIES_VALUE );
		createEAttribute( seriesValueEClass, SERIES_VALUE__NAME );

		sizeEClass = createEClass( SIZE );
		createEAttribute( sizeEClass, SIZE__HEIGHT );
		createEAttribute( sizeEClass, SIZE__WIDTH );

		stringFormatSpecifierEClass = createEClass( STRING_FORMAT_SPECIFIER );
		createEAttribute( stringFormatSpecifierEClass,
				STRING_FORMAT_SPECIFIER__PATTERN );

		styleEClass = createEClass( STYLE );
		createEReference( styleEClass, STYLE__FONT );
		createEReference( styleEClass, STYLE__COLOR );
		createEReference( styleEClass, STYLE__BACKGROUND_COLOR );
		createEReference( styleEClass, STYLE__BACKGROUND_IMAGE );
		createEReference( styleEClass, STYLE__PADDING );

		styleMapEClass = createEClass( STYLE_MAP );
		createEAttribute( styleMapEClass, STYLE_MAP__COMPONENT_NAME );
		createEReference( styleMapEClass, STYLE_MAP__STYLE );

		textEClass = createEClass( TEXT );
		createEAttribute( textEClass, TEXT__VALUE );
		createEReference( textEClass, TEXT__FONT );
		createEReference( textEClass, TEXT__COLOR );

		textAlignmentEClass = createEClass( TEXT_ALIGNMENT );
		createEAttribute( textAlignmentEClass,
				TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT );
		createEAttribute( textAlignmentEClass,
				TEXT_ALIGNMENT__VERTICAL_ALIGNMENT );

		tooltipValueEClass = createEClass( TOOLTIP_VALUE );
		createEAttribute( tooltipValueEClass, TOOLTIP_VALUE__TEXT );
		createEAttribute( tooltipValueEClass, TOOLTIP_VALUE__DELAY );
		createEReference( tooltipValueEClass, TOOLTIP_VALUE__FORMAT_SPECIFIER );

		urlValueEClass = createEClass( URL_VALUE );
		createEAttribute( urlValueEClass, URL_VALUE__BASE_URL );
		createEAttribute( urlValueEClass, URL_VALUE__TARGET );
		createEAttribute( urlValueEClass, URL_VALUE__BASE_PARAMETER_NAME );
		createEAttribute( urlValueEClass, URL_VALUE__VALUE_PARAMETER_NAME );
		createEAttribute( urlValueEClass, URL_VALUE__SERIES_PARAMETER_NAME );
		createEAttribute( urlValueEClass, URL_VALUE__TOOLTIP );

		// Create enums
		actionTypeEEnum = createEEnum( ACTION_TYPE );
		anchorEEnum = createEEnum( ANCHOR );
		angleTypeEEnum = createEEnum( ANGLE_TYPE );
		axisTypeEEnum = createEEnum( AXIS_TYPE );
		chartDimensionEEnum = createEEnum( CHART_DIMENSION );
		chartTypeEEnum = createEEnum( CHART_TYPE );
		cursorTypeEEnum = createEEnum( CURSOR_TYPE );
		dataPointComponentTypeEEnum = createEEnum( DATA_POINT_COMPONENT_TYPE );
		dataTypeEEnum = createEEnum( DATA_TYPE );
		dateFormatDetailEEnum = createEEnum( DATE_FORMAT_DETAIL );
		dateFormatTypeEEnum = createEEnum( DATE_FORMAT_TYPE );
		directionEEnum = createEEnum( DIRECTION );
		groupingUnitTypeEEnum = createEEnum( GROUPING_UNIT_TYPE );
		horizontalAlignmentEEnum = createEEnum( HORIZONTAL_ALIGNMENT );
		imageSourceTypeEEnum = createEEnum( IMAGE_SOURCE_TYPE );
		intersectionTypeEEnum = createEEnum( INTERSECTION_TYPE );
		leaderLineStyleEEnum = createEEnum( LEADER_LINE_STYLE );
		legendBehaviorTypeEEnum = createEEnum( LEGEND_BEHAVIOR_TYPE );
		legendItemTypeEEnum = createEEnum( LEGEND_ITEM_TYPE );
		lineDecoratorEEnum = createEEnum( LINE_DECORATOR );
		lineStyleEEnum = createEEnum( LINE_STYLE );
		markerTypeEEnum = createEEnum( MARKER_TYPE );
		menuStylesKeyTypeEEnum = createEEnum( MENU_STYLES_KEY_TYPE );
		orientationEEnum = createEEnum( ORIENTATION );
		positionEEnum = createEEnum( POSITION );
		riserTypeEEnum = createEEnum( RISER_TYPE );
		ruleTypeEEnum = createEEnum( RULE_TYPE );
		scaleUnitTypeEEnum = createEEnum( SCALE_UNIT_TYPE );
		sortOptionEEnum = createEEnum( SORT_OPTION );
		stretchEEnum = createEEnum( STRETCH );
		styledComponentEEnum = createEEnum( STYLED_COMPONENT );
		tickStyleEEnum = createEEnum( TICK_STYLE );
		triggerConditionEEnum = createEEnum( TRIGGER_CONDITION );
		triggerFlowEEnum = createEEnum( TRIGGER_FLOW );
		unitsOfMeasurementEEnum = createEEnum( UNITS_OF_MEASUREMENT );
		verticalAlignmentEEnum = createEEnum( VERTICAL_ALIGNMENT );

		// Create data types
		actionTypeObjectEDataType = createEDataType( ACTION_TYPE_OBJECT );
		anchorObjectEDataType = createEDataType( ANCHOR_OBJECT );
		angleTypeObjectEDataType = createEDataType( ANGLE_TYPE_OBJECT );
		axisTypeObjectEDataType = createEDataType( AXIS_TYPE_OBJECT );
		chartDimensionObjectEDataType = createEDataType( CHART_DIMENSION_OBJECT );
		chartTypeObjectEDataType = createEDataType( CHART_TYPE_OBJECT );
		cursorTypeObjectEDataType = createEDataType( CURSOR_TYPE_OBJECT );
		dataPointComponentTypeObjectEDataType = createEDataType( DATA_POINT_COMPONENT_TYPE_OBJECT );
		dataTypeObjectEDataType = createEDataType( DATA_TYPE_OBJECT );
		dateFormatDetailObjectEDataType = createEDataType( DATE_FORMAT_DETAIL_OBJECT );
		dateFormatTypeObjectEDataType = createEDataType( DATE_FORMAT_TYPE_OBJECT );
		directionObjectEDataType = createEDataType( DIRECTION_OBJECT );
		groupingUnitTypeObjectEDataType = createEDataType( GROUPING_UNIT_TYPE_OBJECT );
		horizontalAlignmentObjectEDataType = createEDataType( HORIZONTAL_ALIGNMENT_OBJECT );
		idEDataType = createEDataType( ID );
		imageSourceTypeObjectEDataType = createEDataType( IMAGE_SOURCE_TYPE_OBJECT );
		intersectionTypeObjectEDataType = createEDataType( INTERSECTION_TYPE_OBJECT );
		leaderLineStyleObjectEDataType = createEDataType( LEADER_LINE_STYLE_OBJECT );
		legendBehaviorTypeObjectEDataType = createEDataType( LEGEND_BEHAVIOR_TYPE_OBJECT );
		legendItemTypeObjectEDataType = createEDataType( LEGEND_ITEM_TYPE_OBJECT );
		lineDecoratorObjectEDataType = createEDataType( LINE_DECORATOR_OBJECT );
		lineStyleObjectEDataType = createEDataType( LINE_STYLE_OBJECT );
		markerTypeObjectEDataType = createEDataType( MARKER_TYPE_OBJECT );
		menuStylesKeyTypeObjectEDataType = createEDataType( MENU_STYLES_KEY_TYPE_OBJECT );
		orientationObjectEDataType = createEDataType( ORIENTATION_OBJECT );
		patternBitmapEDataType = createEDataType( PATTERN_BITMAP );
		patternBitmapObjectEDataType = createEDataType( PATTERN_BITMAP_OBJECT );
		percentageEDataType = createEDataType( PERCENTAGE );
		percentageObjectEDataType = createEDataType( PERCENTAGE_OBJECT );
		positionObjectEDataType = createEDataType( POSITION_OBJECT );
		rgbValueEDataType = createEDataType( RGB_VALUE );
		rgbValueObjectEDataType = createEDataType( RGB_VALUE_OBJECT );
		riserTypeObjectEDataType = createEDataType( RISER_TYPE_OBJECT );
		ruleTypeObjectEDataType = createEDataType( RULE_TYPE_OBJECT );
		scaleUnitTypeObjectEDataType = createEDataType( SCALE_UNIT_TYPE_OBJECT );
		sortOptionObjectEDataType = createEDataType( SORT_OPTION_OBJECT );
		stretchObjectEDataType = createEDataType( STRETCH_OBJECT );
		styledComponentObjectEDataType = createEDataType( STYLED_COMPONENT_OBJECT );
		tickStyleObjectEDataType = createEDataType( TICK_STYLE_OBJECT );
		triggerConditionObjectEDataType = createEDataType( TRIGGER_CONDITION_OBJECT );
		triggerFlowObjectEDataType = createEDataType( TRIGGER_FLOW_OBJECT );
		unitsOfMeasurementObjectEDataType = createEDataType( UNITS_OF_MEASUREMENT_OBJECT );
		verticalAlignmentObjectEDataType = createEDataType( VERTICAL_ALIGNMENT_OBJECT );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents( )
	{
		if ( isInitialized )
			return;
		isInitialized = true;

		// Initialize package
		setName( eNAME );
		setNsPrefix( eNS_PREFIX );
		setNsURI( eNS_URI );

		// Obtain other dependent packages
		XMLTypePackage theXMLTypePackage = (XMLTypePackage) EPackage.Registry.INSTANCE.getEPackage( XMLTypePackage.eNS_URI );
		ComponentPackage theComponentPackage = (ComponentPackage) EPackage.Registry.INSTANCE.getEPackage( ComponentPackage.eNS_URI );
		DataPackage theDataPackage = (DataPackage) EPackage.Registry.INSTANCE.getEPackage( DataPackage.eNS_URI );

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		accessibilityValueEClass.getESuperTypes( ).add( this.getActionValue( ) );
		callBackValueEClass.getESuperTypes( ).add( this.getActionValue( ) );
		colorDefinitionEClass.getESuperTypes( ).add( this.getFill( ) );
		dateFormatSpecifierEClass.getESuperTypes( )
				.add( this.getFormatSpecifier( ) );
		embeddedImageEClass.getESuperTypes( ).add( this.getImage( ) );
		fractionNumberFormatSpecifierEClass.getESuperTypes( )
				.add( this.getFormatSpecifier( ) );
		gradientEClass.getESuperTypes( ).add( this.getFill( ) );
		imageEClass.getESuperTypes( ).add( this.getFill( ) );
		javaDateFormatSpecifierEClass.getESuperTypes( )
				.add( this.getFormatSpecifier( ) );
		javaNumberFormatSpecifierEClass.getESuperTypes( )
				.add( this.getFormatSpecifier( ) );
		location3DEClass.getESuperTypes( ).add( this.getLocation( ) );
		multipleFillEClass.getESuperTypes( ).add( this.getFill( ) );
		multiURLValuesEClass.getESuperTypes( ).add( this.getActionValue( ) );
		numberFormatSpecifierEClass.getESuperTypes( )
				.add( this.getFormatSpecifier( ) );
		patternImageEClass.getESuperTypes( ).add( this.getImage( ) );
		scriptValueEClass.getESuperTypes( ).add( this.getActionValue( ) );
		seriesValueEClass.getESuperTypes( ).add( this.getActionValue( ) );
		stringFormatSpecifierEClass.getESuperTypes( )
				.add( this.getFormatSpecifier( ) );
		tooltipValueEClass.getESuperTypes( ).add( this.getActionValue( ) );
		urlValueEClass.getESuperTypes( ).add( this.getActionValue( ) );

		// Initialize classes and features; add operations and parameters
		initEClass( accessibilityValueEClass,
				AccessibilityValue.class,
				"AccessibilityValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getAccessibilityValue_Text( ),
				theXMLTypePackage.getString( ),
				"text", null, 0, 1, AccessibilityValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getAccessibilityValue_Accessibility( ),
				theXMLTypePackage.getString( ),
				"accessibility", null, 0, 1, AccessibilityValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( actionValueEClass,
				ActionValue.class,
				"ActionValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getActionValue_Label( ),
				theComponentPackage.getLabel( ),
				null,
				"label", null, 1, 1, ActionValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( angle3DEClass,
				Angle3D.class,
				"Angle3D", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getAngle3D_XAngle( ),
				theXMLTypePackage.getDouble( ),
				"xAngle", null, 1, 1, Angle3D.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getAngle3D_YAngle( ),
				theXMLTypePackage.getDouble( ),
				"yAngle", null, 1, 1, Angle3D.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getAngle3D_ZAngle( ),
				theXMLTypePackage.getDouble( ),
				"zAngle", null, 1, 1, Angle3D.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getAngle3D_Type( ),
				this.getAngleType( ),
				"type", "None", 1, 1, Angle3D.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass( axisOriginEClass,
				AxisOrigin.class,
				"AxisOrigin", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getAxisOrigin_Type( ),
				this.getIntersectionType( ),
				"type", "Min", 1, 1, AxisOrigin.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference( getAxisOrigin_Value( ),
				theDataPackage.getDataElement( ),
				null,
				"value", null, 1, 1, AxisOrigin.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( boundsEClass,
				Bounds.class,
				"Bounds", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getBounds_Left( ),
				theXMLTypePackage.getDouble( ),
				"left", null, 1, 1, Bounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getBounds_Top( ),
				theXMLTypePackage.getDouble( ),
				"top", null, 1, 1, Bounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getBounds_Width( ),
				theXMLTypePackage.getDouble( ),
				"width", null, 1, 1, Bounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getBounds_Height( ),
				theXMLTypePackage.getDouble( ),
				"height", null, 1, 1, Bounds.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( callBackValueEClass,
				CallBackValue.class,
				"CallBackValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getCallBackValue_Identifier( ),
				theXMLTypePackage.getString( ),
				"identifier", null, 1, 1, CallBackValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( colorDefinitionEClass,
				ColorDefinition.class,
				"ColorDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getColorDefinition_Transparency( ),
				theXMLTypePackage.getInt( ),
				"transparency", "255", 1, 1, ColorDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute( getColorDefinition_Red( ),
				this.getRGBValue( ),
				"red", null, 1, 1, ColorDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getColorDefinition_Green( ),
				this.getRGBValue( ),
				"green", null, 1, 1, ColorDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getColorDefinition_Blue( ),
				this.getRGBValue( ),
				"blue", null, 1, 1, ColorDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( cursorEClass,
				Cursor.class,
				"Cursor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getCursor_Type( ),
				this.getCursorType( ),
				"type", null, 1, 1, Cursor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getCursor_Image( ),
				this.getImage( ),
				null,
				"image", null, 0, -1, Cursor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( dataPointEClass,
				DataPoint.class,
				"DataPoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getDataPoint_Components( ),
				this.getDataPointComponent( ),
				null,
				"components", null, 1, -1, DataPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getDataPoint_Prefix( ),
				theXMLTypePackage.getString( ),
				"prefix", null, 1, 1, DataPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getDataPoint_Suffix( ),
				theXMLTypePackage.getString( ),
				"suffix", null, 1, 1, DataPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getDataPoint_Separator( ),
				theXMLTypePackage.getString( ),
				"separator", null, 1, 1, DataPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( dataPointComponentEClass,
				DataPointComponent.class,
				"DataPointComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getDataPointComponent_Type( ),
				this.getDataPointComponentType( ),
				"type", "Base_Value", 1, 1, DataPointComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference( getDataPointComponent_FormatSpecifier( ),
				this.getFormatSpecifier( ),
				null,
				"formatSpecifier", null, 1, 1, DataPointComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getDataPointComponent_OrthogonalType( ),
				theXMLTypePackage.getString( ),
				"orthogonalType", "", 1, 1, DataPointComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass( dateFormatSpecifierEClass,
				DateFormatSpecifier.class,
				"DateFormatSpecifier", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getDateFormatSpecifier_Type( ),
				this.getDateFormatType( ),
				"type", "Long", 1, 1, DateFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute( getDateFormatSpecifier_Detail( ),
				this.getDateFormatDetail( ),
				"detail", "Date", 1, 1, DateFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass( embeddedImageEClass,
				EmbeddedImage.class,
				"EmbeddedImage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getEmbeddedImage_Data( ),
				theXMLTypePackage.getString( ),
				"data", null, 1, 1, EmbeddedImage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( eStringToStringMapEntryEClass,
				Map.Entry.class,
				"EStringToStringMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getEStringToStringMapEntry_Key( ),
				theXMLTypePackage.getString( ),
				"key", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getEStringToStringMapEntry_Value( ),
				theXMLTypePackage.getString( ),
				"value", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( extendedPropertyEClass,
				ExtendedProperty.class,
				"ExtendedProperty", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getExtendedProperty_Name( ),
				theXMLTypePackage.getString( ),
				"name", null, 1, 1, ExtendedProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getExtendedProperty_Value( ),
				theXMLTypePackage.getString( ),
				"value", null, 1, 1, ExtendedProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( fillEClass,
				Fill.class,
				"Fill", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getFill_Type( ),
				theXMLTypePackage.getInt( ),
				"type", null, 1, 1, Fill.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( fontDefinitionEClass,
				FontDefinition.class,
				"FontDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getFontDefinition_Name( ),
				theXMLTypePackage.getString( ),
				"name", null, 1, 1, FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFontDefinition_Size( ),
				theXMLTypePackage.getFloat( ),
				"size", null, 1, 1, FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFontDefinition_Bold( ),
				theXMLTypePackage.getBoolean( ),
				"bold", null, 1, 1, FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFontDefinition_Italic( ),
				theXMLTypePackage.getBoolean( ),
				"italic", null, 1, 1, FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFontDefinition_Strikethrough( ),
				theXMLTypePackage.getBoolean( ),
				"strikethrough", null, 1, 1, FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFontDefinition_Underline( ),
				theXMLTypePackage.getBoolean( ),
				"underline", null, 1, 1, FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFontDefinition_WordWrap( ),
				theXMLTypePackage.getBoolean( ),
				"wordWrap", null, 1, 1, FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getFontDefinition_Alignment( ),
				this.getTextAlignment( ),
				null,
				"alignment", null, 1, 1, FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFontDefinition_Rotation( ),
				theXMLTypePackage.getDouble( ),
				"rotation", null, 1, 1, FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( formatSpecifierEClass,
				FormatSpecifier.class,
				"FormatSpecifier", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		initEClass( fractionNumberFormatSpecifierEClass,
				FractionNumberFormatSpecifier.class,
				"FractionNumberFormatSpecifier", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getFractionNumberFormatSpecifier_Precise( ),
				theXMLTypePackage.getBoolean( ),
				"precise", null, 1, 1, FractionNumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFractionNumberFormatSpecifier_FractionDigits( ),
				theXMLTypePackage.getInt( ),
				"fractionDigits", null, 1, 1, FractionNumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFractionNumberFormatSpecifier_Numerator( ),
				theXMLTypePackage.getDouble( ),
				"numerator", null, 1, 1, FractionNumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFractionNumberFormatSpecifier_Prefix( ),
				theXMLTypePackage.getString( ),
				"prefix", null, 1, 1, FractionNumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFractionNumberFormatSpecifier_Suffix( ),
				theXMLTypePackage.getString( ),
				"suffix", null, 1, 1, FractionNumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getFractionNumberFormatSpecifier_Delimiter( ),
				theXMLTypePackage.getString( ),
				"delimiter", "/", 1, 1, FractionNumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass( gradientEClass,
				Gradient.class,
				"Gradient", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getGradient_StartColor( ),
				this.getColorDefinition( ),
				null,
				"startColor", null, 1, 1, Gradient.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getGradient_EndColor( ),
				this.getColorDefinition( ),
				null,
				"endColor", null, 1, 1, Gradient.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getGradient_Direction( ),
				theXMLTypePackage.getDouble( ),
				"direction", null, 1, 1, Gradient.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getGradient_Cyclic( ),
				theXMLTypePackage.getBoolean( ),
				"cyclic", null, 1, 1, Gradient.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getGradient_Transparency( ),
				theXMLTypePackage.getInt( ),
				"transparency", null, 1, 1, Gradient.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( imageEClass,
				Image.class,
				"Image", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getImage_URL( ),
				theXMLTypePackage.getString( ),
				"uRL", null, 1, 1, Image.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getImage_Source( ),
				this.getImageSourceType( ),
				"source", null, 1, 1, Image.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( insetsEClass,
				Insets.class,
				"Insets", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getInsets_Top( ),
				theXMLTypePackage.getDouble( ),
				"top", null, 1, 1, Insets.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getInsets_Left( ),
				theXMLTypePackage.getDouble( ),
				"left", null, 1, 1, Insets.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getInsets_Bottom( ),
				theXMLTypePackage.getDouble( ),
				"bottom", null, 1, 1, Insets.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getInsets_Right( ),
				theXMLTypePackage.getDouble( ),
				"right", null, 1, 1, Insets.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( interactivityEClass,
				Interactivity.class,
				"Interactivity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getInteractivity_Enable( ),
				theXMLTypePackage.getBoolean( ),
				"enable", "true", 0, 1, Interactivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute( getInteractivity_LegendBehavior( ),
				this.getLegendBehaviorType( ),
				"legendBehavior", "None", 0, 1, Interactivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass( javaDateFormatSpecifierEClass,
				JavaDateFormatSpecifier.class,
				"JavaDateFormatSpecifier", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getJavaDateFormatSpecifier_Pattern( ),
				theXMLTypePackage.getString( ),
				"pattern", null, 1, 1, JavaDateFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( javaNumberFormatSpecifierEClass,
				JavaNumberFormatSpecifier.class,
				"JavaNumberFormatSpecifier", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getJavaNumberFormatSpecifier_Pattern( ),
				theXMLTypePackage.getString( ),
				"pattern", null, 1, 1, JavaNumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getJavaNumberFormatSpecifier_Multiplier( ),
				theXMLTypePackage.getDouble( ),
				"multiplier", null, 1, 1, JavaNumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( lineAttributesEClass,
				LineAttributes.class,
				"LineAttributes", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getLineAttributes_Style( ),
				this.getLineStyle( ),
				"style", "Solid", 1, 1, LineAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute( getLineAttributes_Thickness( ),
				theXMLTypePackage.getInt( ),
				"thickness", "1", 1, 1, LineAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference( getLineAttributes_Color( ),
				this.getColorDefinition( ),
				null,
				"color", null, 1, 1, LineAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getLineAttributes_Visible( ),
				theXMLTypePackage.getBoolean( ),
				"visible", null, 1, 1, LineAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( locationEClass,
				Location.class,
				"Location", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getLocation_X( ),
				theXMLTypePackage.getDouble( ),
				"x", null, 1, 1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getLocation_Y( ),
				theXMLTypePackage.getDouble( ),
				"y", null, 1, 1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( location3DEClass,
				Location3D.class,
				"Location3D", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getLocation3D_Z( ),
				theXMLTypePackage.getDouble( ),
				"z", null, 1, 1, Location3D.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( markerEClass,
				Marker.class,
				"Marker", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getMarker_Type( ),
				this.getMarkerType( ),
				"type", "Crosshair", 1, 1, Marker.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute( getMarker_Size( ),
				theXMLTypePackage.getInt( ),
				"size", null, 1, 1, Marker.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getMarker_Visible( ),
				theXMLTypePackage.getBoolean( ),
				"visible", null, 1, 1, Marker.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getMarker_Fill( ),
				this.getFill( ),
				null,
				"fill", null, 0, 1, Marker.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getMarker_IconPalette( ),
				this.getPalette( ),
				null,
				"iconPalette", null, 0, 1, Marker.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getMarker_Outline( ),
				this.getLineAttributes( ),
				null,
				"outline", null, 0, 1, Marker.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( multipleFillEClass,
				MultipleFill.class,
				"MultipleFill", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getMultipleFill_Fills( ),
				this.getFill( ),
				null,
				"fills", null, 0, -1, MultipleFill.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( multiURLValuesEClass,
				MultiURLValues.class,
				"MultiURLValues", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getMultiURLValues_URLValues( ),
				this.getURLValue( ),
				null,
				"uRLValues", null, 0, -1, MultiURLValues.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getMultiURLValues_Tooltip( ),
				theXMLTypePackage.getString( ),
				"tooltip", null, 1, 1, MultiURLValues.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getMultiURLValues_PropertiesMap( ),
				this.getEStringToStringMapEntry( ),
				null,
				"propertiesMap", null, 0, -1, MultiURLValues.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( numberFormatSpecifierEClass,
				NumberFormatSpecifier.class,
				"NumberFormatSpecifier", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getNumberFormatSpecifier_Prefix( ),
				theXMLTypePackage.getString( ),
				"prefix", null, 1, 1, NumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getNumberFormatSpecifier_Suffix( ),
				theXMLTypePackage.getString( ),
				"suffix", null, 1, 1, NumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getNumberFormatSpecifier_Multiplier( ),
				theXMLTypePackage.getDouble( ),
				"multiplier", null, 1, 1, NumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getNumberFormatSpecifier_FractionDigits( ),
				theXMLTypePackage.getInt( ),
				"fractionDigits", null, 1, 1, NumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( paletteEClass,
				Palette.class,
				"Palette", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getPalette_Name( ),
				theXMLTypePackage.getString( ),
				"name", null, 1, 1, Palette.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getPalette_Entries( ),
				this.getFill( ),
				null,
				"entries", null, 1, -1, Palette.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( patternImageEClass,
				PatternImage.class,
				"PatternImage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getPatternImage_Bitmap( ),
				this.getPatternBitmap( ),
				"bitmap", null, 1, 1, PatternImage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getPatternImage_ForeColor( ),
				this.getColorDefinition( ),
				null,
				"foreColor", null, 1, 1, PatternImage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getPatternImage_BackColor( ),
				this.getColorDefinition( ),
				null,
				"backColor", null, 1, 1, PatternImage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( rotation3DEClass,
				Rotation3D.class,
				"Rotation3D", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getRotation3D_Angles( ),
				this.getAngle3D( ),
				null,
				"angles", null, 0, -1, Rotation3D.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( scriptValueEClass,
				ScriptValue.class,
				"ScriptValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getScriptValue_Script( ),
				theXMLTypePackage.getString( ),
				"script", null, 1, 1, ScriptValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( seriesValueEClass,
				SeriesValue.class,
				"SeriesValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getSeriesValue_Name( ),
				theXMLTypePackage.getString( ),
				"name", null, 1, 1, SeriesValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( sizeEClass,
				Size.class,
				"Size", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getSize_Height( ),
				theXMLTypePackage.getDouble( ),
				"height", null, 1, 1, Size.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getSize_Width( ),
				theXMLTypePackage.getDouble( ),
				"width", null, 1, 1, Size.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( stringFormatSpecifierEClass,
				StringFormatSpecifier.class,
				"StringFormatSpecifier", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getStringFormatSpecifier_Pattern( ),
				theXMLTypePackage.getString( ),
				"pattern", null, 1, 1, StringFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( styleEClass,
				Style.class,
				"Style", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEReference( getStyle_Font( ),
				this.getFontDefinition( ),
				null,
				"font", null, 1, 1, Style.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getStyle_Color( ),
				this.getColorDefinition( ),
				null,
				"color", null, 1, 1, Style.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getStyle_BackgroundColor( ),
				this.getColorDefinition( ),
				null,
				"backgroundColor", null, 0, 1, Style.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getStyle_BackgroundImage( ),
				this.getImage( ),
				null,
				"backgroundImage", null, 0, 1, Style.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getStyle_Padding( ),
				this.getInsets( ),
				null,
				"padding", null, 0, 1, Style.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( styleMapEClass,
				StyleMap.class,
				"StyleMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getStyleMap_ComponentName( ),
				this.getStyledComponent( ),
				"componentName", "Chart_All", 1, 1, StyleMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference( getStyleMap_Style( ),
				this.getStyle( ),
				null,
				"style", null, 1, 1, StyleMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( textEClass,
				Text.class,
				"Text", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getText_Value( ),
				theXMLTypePackage.getString( ),
				"value", null, 1, 1, Text.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getText_Font( ),
				this.getFontDefinition( ),
				null,
				"font", null, 1, 1, Text.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getText_Color( ),
				this.getColorDefinition( ),
				null,
				"color", null, 1, 1, Text.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( textAlignmentEClass,
				TextAlignment.class,
				"TextAlignment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getTextAlignment_HorizontalAlignment( ),
				this.getHorizontalAlignment( ),
				"horizontalAlignment", "Left", 1, 1, TextAlignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute( getTextAlignment_VerticalAlignment( ),
				this.getVerticalAlignment( ),
				"verticalAlignment", "Top", 1, 1, TextAlignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass( tooltipValueEClass,
				TooltipValue.class,
				"TooltipValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getTooltipValue_Text( ),
				theXMLTypePackage.getString( ),
				"text", null, 1, 1, TooltipValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getTooltipValue_Delay( ),
				theXMLTypePackage.getInt( ),
				"delay", null, 1, 1, TooltipValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEReference( getTooltipValue_FormatSpecifier( ),
				this.getFormatSpecifier( ),
				null,
				"formatSpecifier", null, 1, 1, TooltipValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		initEClass( urlValueEClass,
				URLValue.class,
				"URLValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEAttribute( getURLValue_BaseUrl( ),
				theXMLTypePackage.getString( ),
				"baseUrl", null, 1, 1, URLValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getURLValue_Target( ),
				theXMLTypePackage.getString( ),
				"target", null, 1, 1, URLValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getURLValue_BaseParameterName( ),
				theXMLTypePackage.getString( ),
				"baseParameterName", null, 1, 1, URLValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getURLValue_ValueParameterName( ),
				theXMLTypePackage.getString( ),
				"valueParameterName", null, 1, 1, URLValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getURLValue_SeriesParameterName( ),
				theXMLTypePackage.getString( ),
				"seriesParameterName", null, 1, 1, URLValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$
		initEAttribute( getURLValue_Tooltip( ),
				theXMLTypePackage.getString( ),
				"tooltip", null, 1, 1, URLValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED ); //$NON-NLS-1$

		// Initialize enums and add enum literals
		initEEnum( actionTypeEEnum, ActionType.class, "ActionType" ); //$NON-NLS-1$
		addEEnumLiteral( actionTypeEEnum, ActionType.URL_REDIRECT_LITERAL );
		addEEnumLiteral( actionTypeEEnum, ActionType.SHOW_TOOLTIP_LITERAL );
		addEEnumLiteral( actionTypeEEnum, ActionType.TOGGLE_VISIBILITY_LITERAL );
		addEEnumLiteral( actionTypeEEnum, ActionType.INVOKE_SCRIPT_LITERAL );
		addEEnumLiteral( actionTypeEEnum, ActionType.HIGHLIGHT_LITERAL );
		addEEnumLiteral( actionTypeEEnum, ActionType.CALL_BACK_LITERAL );
		addEEnumLiteral( actionTypeEEnum,
				ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL );

		initEEnum( anchorEEnum, Anchor.class, "Anchor" ); //$NON-NLS-1$
		addEEnumLiteral( anchorEEnum, Anchor.NORTH_LITERAL );
		addEEnumLiteral( anchorEEnum, Anchor.NORTH_EAST_LITERAL );
		addEEnumLiteral( anchorEEnum, Anchor.EAST_LITERAL );
		addEEnumLiteral( anchorEEnum, Anchor.SOUTH_EAST_LITERAL );
		addEEnumLiteral( anchorEEnum, Anchor.SOUTH_LITERAL );
		addEEnumLiteral( anchorEEnum, Anchor.SOUTH_WEST_LITERAL );
		addEEnumLiteral( anchorEEnum, Anchor.WEST_LITERAL );
		addEEnumLiteral( anchorEEnum, Anchor.NORTH_WEST_LITERAL );

		initEEnum( angleTypeEEnum, AngleType.class, "AngleType" ); //$NON-NLS-1$
		addEEnumLiteral( angleTypeEEnum, AngleType.NONE_LITERAL );
		addEEnumLiteral( angleTypeEEnum, AngleType.X_LITERAL );
		addEEnumLiteral( angleTypeEEnum, AngleType.Y_LITERAL );
		addEEnumLiteral( angleTypeEEnum, AngleType.Z_LITERAL );

		initEEnum( axisTypeEEnum, AxisType.class, "AxisType" ); //$NON-NLS-1$
		addEEnumLiteral( axisTypeEEnum, AxisType.LINEAR_LITERAL );
		addEEnumLiteral( axisTypeEEnum, AxisType.LOGARITHMIC_LITERAL );
		addEEnumLiteral( axisTypeEEnum, AxisType.TEXT_LITERAL );
		addEEnumLiteral( axisTypeEEnum, AxisType.DATE_TIME_LITERAL );

		initEEnum( chartDimensionEEnum, ChartDimension.class, "ChartDimension" ); //$NON-NLS-1$
		addEEnumLiteral( chartDimensionEEnum,
				ChartDimension.TWO_DIMENSIONAL_LITERAL );
		addEEnumLiteral( chartDimensionEEnum,
				ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL );
		addEEnumLiteral( chartDimensionEEnum,
				ChartDimension.THREE_DIMENSIONAL_LITERAL );

		initEEnum( chartTypeEEnum, ChartType.class, "ChartType" ); //$NON-NLS-1$
		addEEnumLiteral( chartTypeEEnum, ChartType.PIE_LITERAL );
		addEEnumLiteral( chartTypeEEnum, ChartType.BAR_LITERAL );
		addEEnumLiteral( chartTypeEEnum, ChartType.LINE_LITERAL );
		addEEnumLiteral( chartTypeEEnum, ChartType.COMBO_LITERAL );
		addEEnumLiteral( chartTypeEEnum, ChartType.SCATTER_LITERAL );
		addEEnumLiteral( chartTypeEEnum, ChartType.STOCK_LITERAL );

		initEEnum( cursorTypeEEnum, CursorType.class, "CursorType" ); //$NON-NLS-1$
		addEEnumLiteral( cursorTypeEEnum, CursorType.AUTO );
		addEEnumLiteral( cursorTypeEEnum, CursorType.CROSSHAIR );
		addEEnumLiteral( cursorTypeEEnum, CursorType.DEFAULT );
		addEEnumLiteral( cursorTypeEEnum, CursorType.POINTER );
		addEEnumLiteral( cursorTypeEEnum, CursorType.MOVE );
		addEEnumLiteral( cursorTypeEEnum, CursorType.TEXT );
		addEEnumLiteral( cursorTypeEEnum, CursorType.WAIT );
		addEEnumLiteral( cursorTypeEEnum, CursorType.ERESIZE );
		addEEnumLiteral( cursorTypeEEnum, CursorType.NE_RESIZE );
		addEEnumLiteral( cursorTypeEEnum, CursorType.NW_RESIZE );
		addEEnumLiteral( cursorTypeEEnum, CursorType.NRESIZE );
		addEEnumLiteral( cursorTypeEEnum, CursorType.SE_RESIZE );
		addEEnumLiteral( cursorTypeEEnum, CursorType.SW_RESIZE );
		addEEnumLiteral( cursorTypeEEnum, CursorType.SRESIZE );
		addEEnumLiteral( cursorTypeEEnum, CursorType.WRESIZE );
		addEEnumLiteral( cursorTypeEEnum, CursorType.CUSTOM );

		initEEnum( dataPointComponentTypeEEnum,
				DataPointComponentType.class,
				"DataPointComponentType" ); //$NON-NLS-1$
		addEEnumLiteral( dataPointComponentTypeEEnum,
				DataPointComponentType.BASE_VALUE_LITERAL );
		addEEnumLiteral( dataPointComponentTypeEEnum,
				DataPointComponentType.ORTHOGONAL_VALUE_LITERAL );
		addEEnumLiteral( dataPointComponentTypeEEnum,
				DataPointComponentType.SERIES_VALUE_LITERAL );
		addEEnumLiteral( dataPointComponentTypeEEnum,
				DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL );

		initEEnum( dataTypeEEnum, DataType.class, "DataType" ); //$NON-NLS-1$
		addEEnumLiteral( dataTypeEEnum, DataType.NUMERIC_LITERAL );
		addEEnumLiteral( dataTypeEEnum, DataType.DATE_TIME_LITERAL );
		addEEnumLiteral( dataTypeEEnum, DataType.TEXT_LITERAL );

		initEEnum( dateFormatDetailEEnum,
				DateFormatDetail.class,
				"DateFormatDetail" ); //$NON-NLS-1$
		addEEnumLiteral( dateFormatDetailEEnum, DateFormatDetail.DATE_LITERAL );
		addEEnumLiteral( dateFormatDetailEEnum,
				DateFormatDetail.DATE_TIME_LITERAL );

		initEEnum( dateFormatTypeEEnum, DateFormatType.class, "DateFormatType" ); //$NON-NLS-1$
		addEEnumLiteral( dateFormatTypeEEnum, DateFormatType.LONG_LITERAL );
		addEEnumLiteral( dateFormatTypeEEnum, DateFormatType.SHORT_LITERAL );
		addEEnumLiteral( dateFormatTypeEEnum, DateFormatType.MEDIUM_LITERAL );
		addEEnumLiteral( dateFormatTypeEEnum, DateFormatType.FULL_LITERAL );

		initEEnum( directionEEnum, Direction.class, "Direction" ); //$NON-NLS-1$
		addEEnumLiteral( directionEEnum, Direction.LEFT_RIGHT_LITERAL );
		addEEnumLiteral( directionEEnum, Direction.TOP_BOTTOM_LITERAL );

		initEEnum( groupingUnitTypeEEnum,
				GroupingUnitType.class,
				"GroupingUnitType" ); //$NON-NLS-1$
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.SECONDS_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.MINUTES_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum, GroupingUnitType.HOURS_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum, GroupingUnitType.DAYS_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum, GroupingUnitType.WEEKS_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum, GroupingUnitType.MONTHS_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.QUARTERS_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum, GroupingUnitType.YEARS_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum, GroupingUnitType.STRING_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.STRING_PREFIX_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.WEEK_OF_MONTH_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.WEEK_OF_YEAR_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.DAY_OF_WEEK_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.DAY_OF_MONTH_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.DAY_OF_YEAR_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.WEEK_OF_QUARTER_LITERAL );
		addEEnumLiteral( groupingUnitTypeEEnum,
				GroupingUnitType.DAY_OF_QUARTER_LITERAL );

		initEEnum( horizontalAlignmentEEnum,
				HorizontalAlignment.class,
				"HorizontalAlignment" ); //$NON-NLS-1$
		addEEnumLiteral( horizontalAlignmentEEnum,
				HorizontalAlignment.LEFT_LITERAL );
		addEEnumLiteral( horizontalAlignmentEEnum,
				HorizontalAlignment.CENTER_LITERAL );
		addEEnumLiteral( horizontalAlignmentEEnum,
				HorizontalAlignment.RIGHT_LITERAL );

		initEEnum( imageSourceTypeEEnum,
				ImageSourceType.class,
				"ImageSourceType" ); //$NON-NLS-1$
		addEEnumLiteral( imageSourceTypeEEnum, ImageSourceType.STATIC );
		addEEnumLiteral( imageSourceTypeEEnum, ImageSourceType.REPORT );
		addEEnumLiteral( imageSourceTypeEEnum, ImageSourceType.FILE );

		initEEnum( intersectionTypeEEnum,
				IntersectionType.class,
				"IntersectionType" ); //$NON-NLS-1$
		addEEnumLiteral( intersectionTypeEEnum, IntersectionType.MIN_LITERAL );
		addEEnumLiteral( intersectionTypeEEnum, IntersectionType.MAX_LITERAL );
		addEEnumLiteral( intersectionTypeEEnum, IntersectionType.VALUE_LITERAL );

		initEEnum( leaderLineStyleEEnum,
				LeaderLineStyle.class,
				"LeaderLineStyle" ); //$NON-NLS-1$
		addEEnumLiteral( leaderLineStyleEEnum,
				LeaderLineStyle.FIXED_LENGTH_LITERAL );
		addEEnumLiteral( leaderLineStyleEEnum,
				LeaderLineStyle.STRETCH_TO_SIDE_LITERAL );

		initEEnum( legendBehaviorTypeEEnum,
				LegendBehaviorType.class,
				"LegendBehaviorType" ); //$NON-NLS-1$
		addEEnumLiteral( legendBehaviorTypeEEnum,
				LegendBehaviorType.NONE_LITERAL );
		addEEnumLiteral( legendBehaviorTypeEEnum,
				LegendBehaviorType.TOGGLE_SERIE_VISIBILITY_LITERAL );
		addEEnumLiteral( legendBehaviorTypeEEnum,
				LegendBehaviorType.HIGHLIGHT_SERIE_LITERAL );

		initEEnum( legendItemTypeEEnum, LegendItemType.class, "LegendItemType" ); //$NON-NLS-1$
		addEEnumLiteral( legendItemTypeEEnum, LegendItemType.SERIES_LITERAL );
		addEEnumLiteral( legendItemTypeEEnum, LegendItemType.CATEGORIES_LITERAL );

		initEEnum( lineDecoratorEEnum, LineDecorator.class, "LineDecorator" ); //$NON-NLS-1$
		addEEnumLiteral( lineDecoratorEEnum, LineDecorator.ARROW_LITERAL );
		addEEnumLiteral( lineDecoratorEEnum, LineDecorator.CIRCLE_LITERAL );
		addEEnumLiteral( lineDecoratorEEnum, LineDecorator.NONE_LITERAL );

		initEEnum( lineStyleEEnum, LineStyle.class, "LineStyle" ); //$NON-NLS-1$
		addEEnumLiteral( lineStyleEEnum, LineStyle.SOLID_LITERAL );
		addEEnumLiteral( lineStyleEEnum, LineStyle.DASHED_LITERAL );
		addEEnumLiteral( lineStyleEEnum, LineStyle.DOTTED_LITERAL );
		addEEnumLiteral( lineStyleEEnum, LineStyle.DASH_DOTTED_LITERAL );

		initEEnum( markerTypeEEnum, MarkerType.class, "MarkerType" ); //$NON-NLS-1$
		addEEnumLiteral( markerTypeEEnum, MarkerType.CROSSHAIR_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.TRIANGLE_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.BOX_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.CIRCLE_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.ICON_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.NABLA_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.DIAMOND_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.FOUR_DIAMONDS_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.ELLIPSE_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.SEMI_CIRCLE_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.HEXAGON_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.RECTANGLE_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.STAR_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.COLUMN_LITERAL );
		addEEnumLiteral( markerTypeEEnum, MarkerType.CROSS_LITERAL );

		initEEnum( menuStylesKeyTypeEEnum,
				MenuStylesKeyType.class,
				"MenuStylesKeyType" ); //$NON-NLS-1$
		addEEnumLiteral( menuStylesKeyTypeEEnum, MenuStylesKeyType.MENU );
		addEEnumLiteral( menuStylesKeyTypeEEnum, MenuStylesKeyType.MENU_ITEM );
		addEEnumLiteral( menuStylesKeyTypeEEnum,
				MenuStylesKeyType.ON_MOUSE_OVER );
		addEEnumLiteral( menuStylesKeyTypeEEnum, MenuStylesKeyType.ON_MOUSE_OUT );

		initEEnum( orientationEEnum, Orientation.class, "Orientation" ); //$NON-NLS-1$
		addEEnumLiteral( orientationEEnum, Orientation.HORIZONTAL_LITERAL );
		addEEnumLiteral( orientationEEnum, Orientation.VERTICAL_LITERAL );

		initEEnum( positionEEnum, Position.class, "Position" ); //$NON-NLS-1$
		addEEnumLiteral( positionEEnum, Position.ABOVE_LITERAL );
		addEEnumLiteral( positionEEnum, Position.BELOW_LITERAL );
		addEEnumLiteral( positionEEnum, Position.LEFT_LITERAL );
		addEEnumLiteral( positionEEnum, Position.RIGHT_LITERAL );
		addEEnumLiteral( positionEEnum, Position.INSIDE_LITERAL );
		addEEnumLiteral( positionEEnum, Position.OUTSIDE_LITERAL );

		initEEnum( riserTypeEEnum, RiserType.class, "RiserType" ); //$NON-NLS-1$
		addEEnumLiteral( riserTypeEEnum, RiserType.RECTANGLE_LITERAL );
		addEEnumLiteral( riserTypeEEnum, RiserType.TRIANGLE_LITERAL );
		addEEnumLiteral( riserTypeEEnum, RiserType.TUBE_LITERAL );
		addEEnumLiteral( riserTypeEEnum, RiserType.CONE_LITERAL );

		initEEnum( ruleTypeEEnum, RuleType.class, "RuleType" ); //$NON-NLS-1$
		addEEnumLiteral( ruleTypeEEnum, RuleType.FILTER_LITERAL );
		addEEnumLiteral( ruleTypeEEnum, RuleType.SUPPRESS_LITERAL );
		addEEnumLiteral( ruleTypeEEnum, RuleType.LINK_LITERAL );

		initEEnum( scaleUnitTypeEEnum, ScaleUnitType.class, "ScaleUnitType" ); //$NON-NLS-1$
		addEEnumLiteral( scaleUnitTypeEEnum, ScaleUnitType.SECONDS_LITERAL );
		addEEnumLiteral( scaleUnitTypeEEnum, ScaleUnitType.MINUTES_LITERAL );
		addEEnumLiteral( scaleUnitTypeEEnum, ScaleUnitType.HOURS_LITERAL );
		addEEnumLiteral( scaleUnitTypeEEnum, ScaleUnitType.DAYS_LITERAL );
		addEEnumLiteral( scaleUnitTypeEEnum, ScaleUnitType.WEEKS_LITERAL );
		addEEnumLiteral( scaleUnitTypeEEnum, ScaleUnitType.MONTHS_LITERAL );
		addEEnumLiteral( scaleUnitTypeEEnum, ScaleUnitType.YEARS_LITERAL );
		addEEnumLiteral( scaleUnitTypeEEnum, ScaleUnitType.QUARTERS_LITERAL );

		initEEnum( sortOptionEEnum, SortOption.class, "SortOption" ); //$NON-NLS-1$
		addEEnumLiteral( sortOptionEEnum, SortOption.ASCENDING_LITERAL );
		addEEnumLiteral( sortOptionEEnum, SortOption.DESCENDING_LITERAL );

		initEEnum( stretchEEnum, Stretch.class, "Stretch" ); //$NON-NLS-1$
		addEEnumLiteral( stretchEEnum, Stretch.HORIZONTAL_LITERAL );
		addEEnumLiteral( stretchEEnum, Stretch.VERTICAL_LITERAL );
		addEEnumLiteral( stretchEEnum, Stretch.BOTH_LITERAL );

		initEEnum( styledComponentEEnum,
				StyledComponent.class,
				"StyledComponent" ); //$NON-NLS-1$
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.CHART_ALL_LITERAL );
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.CHART_TITLE_LITERAL );
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.CHART_BACKGROUND_LITERAL );
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.PLOT_BACKGROUND_LITERAL );
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.LEGEND_BACKGROUND_LITERAL );
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.LEGEND_LABEL_LITERAL );
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.AXIS_TITLE_LITERAL );
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.AXIS_LABEL_LITERAL );
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.AXIS_LINE_LITERAL );
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.SERIES_TITLE_LITERAL );
		addEEnumLiteral( styledComponentEEnum,
				StyledComponent.SERIES_LABEL_LITERAL );

		initEEnum( tickStyleEEnum, TickStyle.class, "TickStyle" ); //$NON-NLS-1$
		addEEnumLiteral( tickStyleEEnum, TickStyle.LEFT_LITERAL );
		addEEnumLiteral( tickStyleEEnum, TickStyle.RIGHT_LITERAL );
		addEEnumLiteral( tickStyleEEnum, TickStyle.ABOVE_LITERAL );
		addEEnumLiteral( tickStyleEEnum, TickStyle.BELOW_LITERAL );
		addEEnumLiteral( tickStyleEEnum, TickStyle.ACROSS_LITERAL );

		initEEnum( triggerConditionEEnum,
				TriggerCondition.class,
				"TriggerCondition" ); //$NON-NLS-1$
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.MOUSE_HOVER_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.MOUSE_CLICK_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONCLICK_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONDBLCLICK_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONMOUSEDOWN_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONMOUSEUP_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONMOUSEOVER_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONMOUSEMOVE_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONMOUSEOUT_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONFOCUS_LITERAL );
		addEEnumLiteral( triggerConditionEEnum, TriggerCondition.ONBLUR_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONKEYDOWN_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONKEYPRESS_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONKEYUP_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ACCESSIBILITY_LITERAL );
		addEEnumLiteral( triggerConditionEEnum, TriggerCondition.ONLOAD_LITERAL );
		addEEnumLiteral( triggerConditionEEnum,
				TriggerCondition.ONRIGHTCLICK_LITERAL );

		initEEnum( triggerFlowEEnum, TriggerFlow.class, "TriggerFlow" ); //$NON-NLS-1$
		addEEnumLiteral( triggerFlowEEnum, TriggerFlow.CAPTURE_LITERAL );
		addEEnumLiteral( triggerFlowEEnum, TriggerFlow.BUBBLE_LITERAL );
		addEEnumLiteral( triggerFlowEEnum, TriggerFlow.BUBBLE_AND_STOP_LITERAL );

		initEEnum( unitsOfMeasurementEEnum,
				UnitsOfMeasurement.class,
				"UnitsOfMeasurement" ); //$NON-NLS-1$
		addEEnumLiteral( unitsOfMeasurementEEnum,
				UnitsOfMeasurement.PIXELS_LITERAL );
		addEEnumLiteral( unitsOfMeasurementEEnum,
				UnitsOfMeasurement.POINTS_LITERAL );
		addEEnumLiteral( unitsOfMeasurementEEnum,
				UnitsOfMeasurement.INCHES_LITERAL );
		addEEnumLiteral( unitsOfMeasurementEEnum,
				UnitsOfMeasurement.CENTIMETERS_LITERAL );

		initEEnum( verticalAlignmentEEnum,
				VerticalAlignment.class,
				"VerticalAlignment" ); //$NON-NLS-1$
		addEEnumLiteral( verticalAlignmentEEnum, VerticalAlignment.TOP_LITERAL );
		addEEnumLiteral( verticalAlignmentEEnum,
				VerticalAlignment.CENTER_LITERAL );
		addEEnumLiteral( verticalAlignmentEEnum,
				VerticalAlignment.BOTTOM_LITERAL );

		// Initialize data types
		initEDataType( actionTypeObjectEDataType,
				ActionType.class,
				"ActionTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( anchorObjectEDataType,
				Anchor.class,
				"AnchorObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( angleTypeObjectEDataType,
				AngleType.class,
				"AngleTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( axisTypeObjectEDataType,
				AxisType.class,
				"AxisTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( chartDimensionObjectEDataType,
				ChartDimension.class,
				"ChartDimensionObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( chartTypeObjectEDataType,
				ChartType.class,
				"ChartTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( cursorTypeObjectEDataType,
				CursorType.class,
				"CursorTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( dataPointComponentTypeObjectEDataType,
				DataPointComponentType.class,
				"DataPointComponentTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( dataTypeObjectEDataType,
				DataType.class,
				"DataTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( dateFormatDetailObjectEDataType,
				DateFormatDetail.class,
				"DateFormatDetailObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( dateFormatTypeObjectEDataType,
				DateFormatType.class,
				"DateFormatTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( directionObjectEDataType,
				Direction.class,
				"DirectionObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( groupingUnitTypeObjectEDataType,
				GroupingUnitType.class,
				"GroupingUnitTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( horizontalAlignmentObjectEDataType,
				HorizontalAlignment.class,
				"HorizontalAlignmentObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( idEDataType,
				String.class,
				"ID", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( imageSourceTypeObjectEDataType,
				ImageSourceType.class,
				"ImageSourceTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( intersectionTypeObjectEDataType,
				IntersectionType.class,
				"IntersectionTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( leaderLineStyleObjectEDataType,
				LeaderLineStyle.class,
				"LeaderLineStyleObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( legendBehaviorTypeObjectEDataType,
				LegendBehaviorType.class,
				"LegendBehaviorTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( legendItemTypeObjectEDataType,
				LegendItemType.class,
				"LegendItemTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( lineDecoratorObjectEDataType,
				LineDecorator.class,
				"LineDecoratorObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( lineStyleObjectEDataType,
				LineStyle.class,
				"LineStyleObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( markerTypeObjectEDataType,
				MarkerType.class,
				"MarkerTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( menuStylesKeyTypeObjectEDataType,
				MenuStylesKeyType.class,
				"MenuStylesKeyTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( orientationObjectEDataType,
				Orientation.class,
				"OrientationObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( patternBitmapEDataType,
				long.class,
				"PatternBitmap", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( patternBitmapObjectEDataType,
				Long.class,
				"PatternBitmapObject", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( percentageEDataType,
				double.class,
				"Percentage", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( percentageObjectEDataType,
				Double.class,
				"PercentageObject", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( positionObjectEDataType,
				Position.class,
				"PositionObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( rgbValueEDataType,
				int.class,
				"RGBValue", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( rgbValueObjectEDataType,
				Integer.class,
				"RGBValueObject", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( riserTypeObjectEDataType,
				RiserType.class,
				"RiserTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( ruleTypeObjectEDataType,
				RuleType.class,
				"RuleTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( scaleUnitTypeObjectEDataType,
				ScaleUnitType.class,
				"ScaleUnitTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( sortOptionObjectEDataType,
				SortOption.class,
				"SortOptionObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( stretchObjectEDataType,
				Stretch.class,
				"StretchObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( styledComponentObjectEDataType,
				StyledComponent.class,
				"StyledComponentObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( tickStyleObjectEDataType,
				TickStyle.class,
				"TickStyleObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( triggerConditionObjectEDataType,
				TriggerCondition.class,
				"TriggerConditionObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( triggerFlowObjectEDataType,
				TriggerFlow.class,
				"TriggerFlowObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( unitsOfMeasurementObjectEDataType,
				UnitsOfMeasurement.class,
				"UnitsOfMeasurementObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$
		initEDataType( verticalAlignmentObjectEDataType,
				VerticalAlignment.class,
				"VerticalAlignmentObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS ); //$NON-NLS-1$

		// Create resource
		createResource( eNS_URI );

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations( );
	}

	/**
	 * Initializes the annotations for
	 * <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData </b>. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations( )
	{
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$				
		addAnnotation( accessibilityValueEClass, source, new String[]{
				"name", "AccessibilityValue", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getAccessibilityValue_Text( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Text" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getAccessibilityValue_Accessibility( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Accessibility" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( actionTypeEEnum, source, new String[]{
				"name", "ActionType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( actionTypeObjectEDataType, source, new String[]{
				"name", "ActionType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "ActionType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( actionValueEClass, source, new String[]{
				"name", "ActionValue", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getActionValue_Label( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Label" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( anchorEEnum, source, new String[]{
				"name", "Anchor" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( anchorObjectEDataType, source, new String[]{
				"name", "Anchor:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "Anchor" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( angle3DEClass, source, new String[]{
				"name", "Angle3D", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getAngle3D_XAngle( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "XAngle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getAngle3D_YAngle( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "YAngle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getAngle3D_ZAngle( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ZAngle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getAngle3D_Type( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Type" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( angleTypeEEnum, source, new String[]{
				"name", "AngleType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( angleTypeObjectEDataType, source, new String[]{
				"name", "AngleType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "AngleType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( axisOriginEClass, source, new String[]{
				"name", "AxisOrigin", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getAxisOrigin_Type( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Type" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getAxisOrigin_Value( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Value" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( axisTypeEEnum, source, new String[]{
				"name", "AxisType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( axisTypeObjectEDataType, source, new String[]{
				"name", "AxisType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "AxisType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( boundsEClass, source, new String[]{
				"name", "Bounds", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getBounds_Left( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Left" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getBounds_Top( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Top" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getBounds_Width( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Width" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getBounds_Height( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Height" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( callBackValueEClass, source, new String[]{
				"name", "CallBackValue", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getCallBackValue_Identifier( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Identifier" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( chartDimensionEEnum, source, new String[]{
				"name", "ChartDimension" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( chartDimensionObjectEDataType, source, new String[]{
				"name", "ChartDimension:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "ChartDimension" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( chartTypeEEnum, source, new String[]{
				"name", "ChartType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( chartTypeObjectEDataType, source, new String[]{
				"name", "ChartType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "ChartType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( colorDefinitionEClass, source, new String[]{
				"name", "ColorDefinition", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getColorDefinition_Transparency( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Transparency" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getColorDefinition_Red( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Red" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getColorDefinition_Green( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Green" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getColorDefinition_Blue( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Blue" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( cursorEClass, source, new String[]{
				"name", "Cursor", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getCursor_Type( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Type" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getCursor_Image( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Image" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( cursorTypeEEnum, source, new String[]{
				"name", "CursorType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( cursorTypeObjectEDataType, source, new String[]{
				"name", "CursorType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "CursorType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dataPointEClass, source, new String[]{
				"name", "DataPoint", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDataPoint_Components( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Components" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDataPoint_Prefix( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Prefix" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDataPoint_Suffix( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Suffix" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDataPoint_Separator( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Separator" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dataPointComponentEClass, source, new String[]{
				"name", "DataPointComponent", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDataPointComponent_Type( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Type" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDataPointComponent_FormatSpecifier( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "FormatSpecifier" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getDataPointComponent_OrthogonalType( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "OrthogonalType" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( dataPointComponentTypeEEnum, source, new String[]{
				"name", "DataPointComponentType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dataPointComponentTypeObjectEDataType,
				source,
				new String[]{
						"name", "DataPointComponentType:Object", //$NON-NLS-1$ //$NON-NLS-2$
						"baseType", "DataPointComponentType" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( dataTypeEEnum, source, new String[]{
				"name", "DataType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dataTypeObjectEDataType, source, new String[]{
				"name", "DataType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "DataType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dateFormatDetailEEnum, source, new String[]{
				"name", "DateFormatDetail" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dateFormatDetailObjectEDataType, source, new String[]{
				"name", "DateFormatDetail:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "DateFormatDetail" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dateFormatSpecifierEClass, source, new String[]{
				"name", "DateFormatSpecifier", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDateFormatSpecifier_Type( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Type" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getDateFormatSpecifier_Detail( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Detail" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dateFormatTypeEEnum, source, new String[]{
				"name", "DateFormatType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( dateFormatTypeObjectEDataType, source, new String[]{
				"name", "DateFormatType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "DateFormatType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( directionEEnum, source, new String[]{
				"name", "Direction" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( directionObjectEDataType, source, new String[]{
				"name", "Direction:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "Direction" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( embeddedImageEClass, source, new String[]{
				"name", "EmbeddedImage", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getEmbeddedImage_Data( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Data" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( eStringToStringMapEntryEClass, source, new String[]{
				"name", "EStringToStringMapEntry", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getEStringToStringMapEntry_Key( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Key" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getEStringToStringMapEntry_Value( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Value" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( extendedPropertyEClass, source, new String[]{
				"name", "ExtendedProperty", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getExtendedProperty_Name( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Name" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getExtendedProperty_Value( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Value" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( fillEClass, source, new String[]{
				"name", "Fill", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getFill_Type( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Type" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( fontDefinitionEClass, source, new String[]{
				"name", "FontDefinition", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getFontDefinition_Name( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Name" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getFontDefinition_Size( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Size" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getFontDefinition_Bold( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Bold" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getFontDefinition_Italic( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Italic" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getFontDefinition_Strikethrough( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Strikethrough" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getFontDefinition_Underline( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Underline" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getFontDefinition_WordWrap( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "WordWrap" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getFontDefinition_Alignment( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Alignment" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getFontDefinition_Rotation( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Rotation" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( formatSpecifierEClass, source, new String[]{
				"name", "FormatSpecifier", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "empty" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( fractionNumberFormatSpecifierEClass,
				source,
				new String[]{
						"name", "FractionNumberFormatSpecifier", //$NON-NLS-1$ //$NON-NLS-2$
						"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getFractionNumberFormatSpecifier_Precise( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Precise" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getFractionNumberFormatSpecifier_FractionDigits( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "FractionDigits" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getFractionNumberFormatSpecifier_Numerator( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Numerator" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getFractionNumberFormatSpecifier_Prefix( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Prefix" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getFractionNumberFormatSpecifier_Suffix( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Suffix" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getFractionNumberFormatSpecifier_Delimiter( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Delimiter" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( gradientEClass, source, new String[]{
				"name", "Gradient", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getGradient_StartColor( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "StartColor" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getGradient_EndColor( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "EndColor" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getGradient_Direction( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Direction" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getGradient_Cyclic( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Cyclic" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getGradient_Transparency( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Transparency" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( groupingUnitTypeEEnum, source, new String[]{
				"name", "GroupingUnitType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( groupingUnitTypeObjectEDataType, source, new String[]{
				"name", "GroupingUnitType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "GroupingUnitType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( horizontalAlignmentEEnum, source, new String[]{
				"name", "HorizontalAlignment" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( horizontalAlignmentObjectEDataType,
				source,
				new String[]{
						"name", "HorizontalAlignment:Object", //$NON-NLS-1$ //$NON-NLS-2$
						"baseType", "HorizontalAlignment" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( idEDataType, source, new String[]{
				"name", "ID", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "http://www.eclipse.org/emf/2003/XMLType#string", //$NON-NLS-1$ //$NON-NLS-2$
				"pattern", "[A-Z]" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( imageEClass, source, new String[]{
				"name", "Image", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getImage_URL( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "URL" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getImage_Source( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "source" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( imageSourceTypeEEnum, source, new String[]{
				"name", "ImageSourceType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( imageSourceTypeObjectEDataType, source, new String[]{
				"name", "ImageSourceType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "ImageSourceType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( insetsEClass, source, new String[]{
				"name", "Insets", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getInsets_Top( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Top" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getInsets_Left( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Left" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getInsets_Bottom( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Bottom" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getInsets_Right( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Right" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( interactivityEClass, source, new String[]{
				"name", "Interactivity", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getInteractivity_Enable( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Enable" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getInteractivity_LegendBehavior( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "LegendBehavior" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( intersectionTypeEEnum, source, new String[]{
				"name", "IntersectionType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( intersectionTypeObjectEDataType, source, new String[]{
				"name", "IntersectionType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "IntersectionType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( javaDateFormatSpecifierEClass, source, new String[]{
				"name", "JavaDateFormatSpecifier", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getJavaDateFormatSpecifier_Pattern( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Pattern" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( javaNumberFormatSpecifierEClass, source, new String[]{
				"name", "JavaNumberFormatSpecifier", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getJavaNumberFormatSpecifier_Pattern( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Pattern" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getJavaNumberFormatSpecifier_Multiplier( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Multiplier" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( leaderLineStyleEEnum, source, new String[]{
				"name", "LeaderLineStyle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( leaderLineStyleObjectEDataType, source, new String[]{
				"name", "LeaderLineStyle:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "LeaderLineStyle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( legendBehaviorTypeEEnum, source, new String[]{
				"name", "LegendBehaviorType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( legendBehaviorTypeObjectEDataType, source, new String[]{
				"name", "LegendBehaviorType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "LegendBehaviorType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( legendItemTypeEEnum, source, new String[]{
				"name", "LegendItemType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( legendItemTypeObjectEDataType, source, new String[]{
				"name", "LegendItemType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "LegendItemType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( lineAttributesEClass, source, new String[]{
				"name", "LineAttributes", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLineAttributes_Style( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Style" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLineAttributes_Thickness( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Thickness" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLineAttributes_Color( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Color" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLineAttributes_Visible( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Visible" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( lineDecoratorEEnum, source, new String[]{
				"name", "LineDecorator" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( lineDecoratorObjectEDataType, source, new String[]{
				"name", "LineDecorator:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "LineDecorator" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( lineStyleEEnum, source, new String[]{
				"name", "LineStyle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( lineStyleObjectEDataType, source, new String[]{
				"name", "LineStyle:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "LineStyle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( locationEClass, source, new String[]{
				"name", "Location", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLocation_X( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "x" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLocation_Y( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "y" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( location3DEClass, source, new String[]{
				"name", "Location3D", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getLocation3D_Z( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "z" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( markerEClass, source, new String[]{
				"name", "Marker", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getMarker_Type( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Type" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getMarker_Size( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Size" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getMarker_Visible( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Visible" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getMarker_Fill( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Fill" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getMarker_IconPalette( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "IconPalette" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getMarker_Outline( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Outline" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( markerTypeEEnum, source, new String[]{
				"name", "MarkerType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( markerTypeObjectEDataType, source, new String[]{
				"name", "MarkerType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "MarkerType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( menuStylesKeyTypeEEnum, source, new String[]{
				"name", "MenuStylesKeyType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( menuStylesKeyTypeObjectEDataType, source, new String[]{
				"name", "MenuStylesKeyType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "MenuStylesKeyType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( multipleFillEClass, source, new String[]{
				"name", "MultipleFill", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getMultipleFill_Fills( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Fills" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( multiURLValuesEClass, source, new String[]{
				"name", "MultiURLValues", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getMultiURLValues_URLValues( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "URLValues" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getMultiURLValues_Tooltip( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Tooltip" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getMultiURLValues_PropertiesMap( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "PropertiesMap" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( numberFormatSpecifierEClass, source, new String[]{
				"name", "NumberFormatSpecifier", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getNumberFormatSpecifier_Prefix( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Prefix" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getNumberFormatSpecifier_Suffix( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Suffix" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getNumberFormatSpecifier_Multiplier( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Multiplier" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getNumberFormatSpecifier_FractionDigits( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "FractionDigits" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( orientationEEnum, source, new String[]{
				"name", "Orientation" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( orientationObjectEDataType, source, new String[]{
				"name", "Orientation:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "Orientation" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( paletteEClass, source, new String[]{
				"name", "Palette", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPalette_Name( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Name" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPalette_Entries( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Entries" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( patternBitmapEDataType, source, new String[]{
				"name", "PatternBitmap", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "http://www.eclipse.org/emf/2003/XMLType#long" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( patternBitmapObjectEDataType, source, new String[]{
				"name", "PatternBitmap:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "PatternBitmap" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( patternImageEClass, source, new String[]{
				"name", "PatternImage", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPatternImage_Bitmap( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Bitmap" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPatternImage_ForeColor( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ForeColor" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getPatternImage_BackColor( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "BackColor" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( percentageEDataType, source, new String[]{
				"name", "Percentage", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "http://www.eclipse.org/emf/2003/XMLType#double", //$NON-NLS-1$ //$NON-NLS-2$
				"minInclusive", "0.0", //$NON-NLS-1$ //$NON-NLS-2$
				"maxInclusive", "100.0" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( percentageObjectEDataType, source, new String[]{
				"name", "Percentage:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "Percentage" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( positionEEnum, source, new String[]{
				"name", "Position" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( positionObjectEDataType, source, new String[]{
				"name", "Position:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "Position" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( rgbValueEDataType, source, new String[]{
				"name", "RGBValue", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "http://www.eclipse.org/emf/2003/XMLType#int", //$NON-NLS-1$ //$NON-NLS-2$
				"minInclusive", "0", //$NON-NLS-1$ //$NON-NLS-2$
				"maxInclusive", "255" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( rgbValueObjectEDataType, source, new String[]{
				"name", "RGBValue:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "RGBValue" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( riserTypeEEnum, source, new String[]{
				"name", "RiserType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( riserTypeObjectEDataType, source, new String[]{
				"name", "RiserType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "RiserType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( rotation3DEClass, source, new String[]{
				"name", "Rotation3D", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getRotation3D_Angles( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Angles" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( ruleTypeEEnum, source, new String[]{
				"name", "RuleType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( ruleTypeObjectEDataType, source, new String[]{
				"name", "RuleType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "RuleType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( scaleUnitTypeEEnum, source, new String[]{
				"name", "ScaleUnitType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( scaleUnitTypeObjectEDataType, source, new String[]{
				"name", "ScaleUnitType:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "ScaleUnitType" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( scriptValueEClass, source, new String[]{
				"name", "ScriptValue", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getScriptValue_Script( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Script" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( seriesValueEClass, source, new String[]{
				"name", "SeriesValue", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSeriesValue_Name( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Name" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( sizeEClass, source, new String[]{
				"name", "Size", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSize_Height( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Height" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getSize_Width( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Width" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( sortOptionEEnum, source, new String[]{
				"name", "SortOption" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( sortOptionObjectEDataType, source, new String[]{
				"name", "SortOption:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "SortOption" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( stretchEEnum, source, new String[]{
				"name", "Stretch" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( stretchObjectEDataType, source, new String[]{
				"name", "Stretch:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "Stretch" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( stringFormatSpecifierEClass, source, new String[]{
				"name", "StringFormatSpecifier", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getStringFormatSpecifier_Pattern( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "Pattern" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( styleEClass, source, new String[]{
				"name", "Style", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getStyle_Font( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Font" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getStyle_Color( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Color" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getStyle_BackgroundColor( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "BackgroundColor" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getStyle_BackgroundImage( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "BackgroundImage" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getStyle_Padding( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Padding" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( styledComponentEEnum, source, new String[]{
				"name", "StyledComponent" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( styledComponentObjectEDataType, source, new String[]{
				"name", "StyledComponent:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "StyledComponent" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( styleMapEClass, source, new String[]{
				"name", "StyleMap", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getStyleMap_ComponentName( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ComponentName" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getStyleMap_Style( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Style" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( textEClass, source, new String[]{
				"name", "Text", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getText_Value( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Value" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getText_Font( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Font" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getText_Color( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Color" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( textAlignmentEClass, source, new String[]{
				"name", "TextAlignment", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getTextAlignment_HorizontalAlignment( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "horizontalAlignment" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getTextAlignment_VerticalAlignment( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "verticalAlignment" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( tickStyleEEnum, source, new String[]{
				"name", "TickStyle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( tickStyleObjectEDataType, source, new String[]{
				"name", "TickStyle:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "TickStyle" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( tooltipValueEClass, source, new String[]{
				"name", "TooltipValue", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getTooltipValue_Text( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Text" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getTooltipValue_Delay( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Delay" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getTooltipValue_FormatSpecifier( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "FormatSpecifier" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( triggerConditionEEnum, source, new String[]{
				"name", "TriggerCondition" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( triggerConditionObjectEDataType, source, new String[]{
				"name", "TriggerCondition:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "TriggerCondition" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( triggerFlowEEnum, source, new String[]{
				"name", "TriggerFlow" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( triggerFlowObjectEDataType, source, new String[]{
				"name", "TriggerFlow:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "TriggerFlow" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( unitsOfMeasurementEEnum, source, new String[]{
				"name", "UnitsOfMeasurement" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( unitsOfMeasurementObjectEDataType, source, new String[]{
				"name", "UnitsOfMeasurement:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "UnitsOfMeasurement" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( urlValueEClass, source, new String[]{
				"name", "URLValue", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getURLValue_BaseUrl( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "BaseUrl" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getURLValue_Target( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Target" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getURLValue_BaseParameterName( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "BaseParameterName" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getURLValue_ValueParameterName( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ValueParameterName" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( getURLValue_SeriesParameterName( ),
				source,
				new String[]{
						"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
						"name", "SeriesParameterName" //$NON-NLS-1$ //$NON-NLS-2$
				} );
		addAnnotation( getURLValue_Tooltip( ), source, new String[]{
				"kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Tooltip" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( verticalAlignmentEEnum, source, new String[]{
				"name", "VerticalAlignment" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		addAnnotation( verticalAlignmentObjectEDataType, source, new String[]{
				"name", "VerticalAlignment:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "VerticalAlignment" //$NON-NLS-1$ //$NON-NLS-2$
		} );
	}

} // AttributePackageImpl
