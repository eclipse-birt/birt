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

import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.Anchor;
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
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Gradient;
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
import org.eclipse.birt.chart.model.attribute.SeriesHint;
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
import org.eclipse.emf.ecore.xml.type.impl.XMLTypePackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package </b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class AttributePackageImpl extends EPackageImpl implements AttributePackage
{

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass actionValueEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass axisOriginEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass boundsEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass colorDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass dataPointEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass dataPointComponentEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass dateFormatSpecifierEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass extendedPropertyEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass fillEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass fontDefinitionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass formatSpecifierEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass gradientEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass imageEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass insetsEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass javaDateFormatSpecifierEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass javaNumberFormatSpecifierEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass lineAttributesEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass locationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass markerEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass numberFormatSpecifierEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass paletteEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass scriptValueEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass seriesHintEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass seriesValueEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass sizeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass styleMapEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass textEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass textAlignmentEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass tooltipValueEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass urlValueEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum actionTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum anchorEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum axisTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum chartDimensionEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum chartTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum dataPointComponentTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum dateFormatDetailEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum dateFormatTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum directionEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum horizontalAlignmentEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum intersectionTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum leaderLineStyleEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum legendItemTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum lineStyleEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum markerTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum orientationEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum positionEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum riserTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum ruleTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum scaleUnitTypeEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum sortOptionEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum stretchEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum styledComponentEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum tickStyleEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum triggerConditionEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EEnum verticalAlignmentEEnum = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType actionTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType anchorObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType axisTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType chartDimensionObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType chartTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType dataPointComponentTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType dateFormatDetailObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType dateFormatTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType directionObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType horizontalAlignmentObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType idEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType intersectionTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType leaderLineStyleObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType legendItemTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType lineStyleObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType markerTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType orientationObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType percentageEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType percentageObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType positionObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType rgbValueEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType rgbValueObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType riserTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType ruleTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType scaleUnitTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType sortOptionObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType stretchObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType styledComponentObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType tickStyleObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType triggerConditionObjectEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EDataType verticalAlignmentObjectEDataType = null;

    /**
     * Creates an instance of the model <b>Package </b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry}by the package package URI value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also
     * performs initialization of the package, or returns the registered package, if one already exists. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#eNS_URI
     * @see #init()
     * @generated
     */
    private AttributePackageImpl()
    {
        super(eNS_URI, AttributeFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package </b> for this model, and for any others upon which it depends.
     * Simple dependencies are satisfied by calling this method on all dependent packages before doing anything else.
     * This method drives initialization for interdependent packages directly, in parallel with this package, itself.
     * <p>
     * Of this package and its interdependencies, all packages which have not yet been registered by their URI values
     * are first created and registered. The packages are then initialized in two steps: meta-model objects for all of
     * the packages are created before any are initialized, since one package's meta-model objects may refer to those of
     * another.
     * <p>
     * Invocation of this method will not affect any packages that have already been initialized. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static AttributePackage init()
    {
        if (isInited)
            return (AttributePackage) EPackage.Registry.INSTANCE.getEPackage(AttributePackage.eNS_URI);

        // Obtain or create and register package
        AttributePackageImpl theAttributePackage = (AttributePackageImpl) (EPackage.Registry.INSTANCE
            .getEPackage(eNS_URI) instanceof AttributePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI)
            : new AttributePackageImpl());

        isInited = true;

        // Initialize simple dependencies
        XMLTypePackageImpl.init();

        // Obtain or create and register interdependencies
        TypePackageImpl theTypePackage = (TypePackageImpl) (EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI) instanceof TypePackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(TypePackage.eNS_URI)
            : TypePackageImpl.eINSTANCE);
        ModelPackageImpl theModelPackage = (ModelPackageImpl) (EPackage.Registry.INSTANCE
            .getEPackage(ModelPackage.eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(ModelPackage.eNS_URI) : ModelPackageImpl.eINSTANCE);
        ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) (EPackage.Registry.INSTANCE
            .getEPackage(ComponentPackage.eNS_URI) instanceof ComponentPackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(ComponentPackage.eNS_URI) : ComponentPackageImpl.eINSTANCE);
        DataPackageImpl theDataPackage = (DataPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(DataPackage.eNS_URI) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(DataPackage.eNS_URI)
            : DataPackageImpl.eINSTANCE);
        LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) (EPackage.Registry.INSTANCE
            .getEPackage(LayoutPackage.eNS_URI) instanceof LayoutPackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(LayoutPackage.eNS_URI) : LayoutPackageImpl.eINSTANCE);

        // Create package meta-data objects
        theAttributePackage.createPackageContents();
        theTypePackage.createPackageContents();
        theModelPackage.createPackageContents();
        theComponentPackage.createPackageContents();
        theDataPackage.createPackageContents();
        theLayoutPackage.createPackageContents();

        // Initialize created meta-data
        theAttributePackage.initializePackageContents();
        theTypePackage.initializePackageContents();
        theModelPackage.initializePackageContents();
        theComponentPackage.initializePackageContents();
        theDataPackage.initializePackageContents();
        theLayoutPackage.initializePackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put(theAttributePackage, new EValidator.Descriptor()
        {
            public EValidator getEValidator()
            {
                return AttributeValidator.INSTANCE;
            }
        });

        // Mark meta-data to indicate it can't be changed
        theAttributePackage.freeze();

        return theAttributePackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getActionValue()
    {
        return actionValueEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getAxisOrigin()
    {
        return axisOriginEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getAxisOrigin_Type()
    {
        return (EAttribute) axisOriginEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getAxisOrigin_Value()
    {
        return (EAttribute) axisOriginEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getBounds()
    {
        return boundsEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBounds_Left()
    {
        return (EAttribute) boundsEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBounds_Top()
    {
        return (EAttribute) boundsEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBounds_Width()
    {
        return (EAttribute) boundsEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBounds_Height()
    {
        return (EAttribute) boundsEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getColorDefinition()
    {
        return colorDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getColorDefinition_Transparency()
    {
        return (EAttribute) colorDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getColorDefinition_Red()
    {
        return (EAttribute) colorDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getColorDefinition_Blue()
    {
        return (EAttribute) colorDefinitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getColorDefinition_Green()
    {
        return (EAttribute) colorDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getDataPoint()
    {
        return dataPointEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getDataPoint_Components()
    {
        return (EReference) dataPointEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getDataPoint_Prefix()
    {
        return (EAttribute) dataPointEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getDataPoint_Suffix()
    {
        return (EAttribute) dataPointEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getDataPoint_Separator()
    {
        return (EAttribute) dataPointEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getDataPointComponent()
    {
        return dataPointComponentEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getDataPointComponent_Type()
    {
        return (EAttribute) dataPointComponentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getDataPointComponent_FormatSpecifier()
    {
        return (EReference) dataPointComponentEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getDateFormatSpecifier()
    {
        return dateFormatSpecifierEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getDateFormatSpecifier_Type()
    {
        return (EAttribute) dateFormatSpecifierEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getDateFormatSpecifier_Detail()
    {
        return (EAttribute) dateFormatSpecifierEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getExtendedProperty()
    {
        return extendedPropertyEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getExtendedProperty_Name()
    {
        return (EAttribute) extendedPropertyEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getExtendedProperty_Value()
    {
        return (EAttribute) extendedPropertyEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getFill()
    {
        return fillEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getFill_Type()
    {
        return (EAttribute) fillEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getFontDefinition()
    {
        return fontDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getFontDefinition_Name()
    {
        return (EAttribute) fontDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getFontDefinition_Size()
    {
        return (EAttribute) fontDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getFontDefinition_Bold()
    {
        return (EAttribute) fontDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getFontDefinition_Italic()
    {
        return (EAttribute) fontDefinitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getFontDefinition_Strikethrough()
    {
        return (EAttribute) fontDefinitionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getFontDefinition_Underline()
    {
        return (EAttribute) fontDefinitionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getFontDefinition_WordWrap()
    {
        return (EAttribute) fontDefinitionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getFontDefinition_Alignment()
    {
        return (EReference) fontDefinitionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getFontDefinition_Rotation()
    {
        return (EAttribute) fontDefinitionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getFormatSpecifier()
    {
        return formatSpecifierEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getGradient()
    {
        return gradientEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getGradient_StartColor()
    {
        return (EReference) gradientEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getGradient_EndColor()
    {
        return (EReference) gradientEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getGradient_Direction()
    {
        return (EAttribute) gradientEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getGradient_Cyclic()
    {
        return (EAttribute) gradientEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getGradient_Transparency()
    {
        return (EAttribute) gradientEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getImage()
    {
        return imageEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getImage_URL()
    {
        return (EAttribute) imageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getInsets()
    {
        return insetsEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getInsets_Top()
    {
        return (EAttribute) insetsEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getInsets_Left()
    {
        return (EAttribute) insetsEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getInsets_Bottom()
    {
        return (EAttribute) insetsEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getInsets_Right()
    {
        return (EAttribute) insetsEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getJavaDateFormatSpecifier()
    {
        return javaDateFormatSpecifierEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getJavaDateFormatSpecifier_Pattern()
    {
        return (EAttribute) javaDateFormatSpecifierEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getJavaNumberFormatSpecifier()
    {
        return javaNumberFormatSpecifierEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getJavaNumberFormatSpecifier_Pattern()
    {
        return (EAttribute) javaNumberFormatSpecifierEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getJavaNumberFormatSpecifier_Multiplier()
    {
        return (EAttribute) javaNumberFormatSpecifierEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getLineAttributes()
    {
        return lineAttributesEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLineAttributes_Style()
    {
        return (EAttribute) lineAttributesEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLineAttributes_Thickness()
    {
        return (EAttribute) lineAttributesEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getLineAttributes_Color()
    {
        return (EReference) lineAttributesEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLineAttributes_Visible()
    {
        return (EAttribute) lineAttributesEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getLocation()
    {
        return locationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLocation_X()
    {
        return (EAttribute) locationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLocation_Y()
    {
        return (EAttribute) locationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getMarker()
    {
        return markerEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getMarker_Type()
    {
        return (EAttribute) markerEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getMarker_Size()
    {
        return (EAttribute) markerEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getMarker_Visible()
    {
        return (EAttribute) markerEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getNumberFormatSpecifier()
    {
        return numberFormatSpecifierEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getNumberFormatSpecifier_Prefix()
    {
        return (EAttribute) numberFormatSpecifierEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getNumberFormatSpecifier_Suffix()
    {
        return (EAttribute) numberFormatSpecifierEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getNumberFormatSpecifier_Multiplier()
    {
        return (EAttribute) numberFormatSpecifierEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getNumberFormatSpecifier_FractionDigits()
    {
        return (EAttribute) numberFormatSpecifierEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getPalette()
    {
        return paletteEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getPalette_Name()
    {
        return (EAttribute) paletteEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getPalette_Entries()
    {
        return (EReference) paletteEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getScriptValue()
    {
        return scriptValueEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getScriptValue_Script()
    {
        return (EAttribute) scriptValueEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getSeriesHint()
    {
        return seriesHintEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getSeriesHint_SeriesClass()
    {
        return (EAttribute) seriesHintEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getSeriesHint_Modifier()
    {
        return (EAttribute) seriesHintEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getSeriesValue()
    {
        return seriesValueEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getSeriesValue_Name()
    {
        return (EAttribute) seriesValueEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getSize()
    {
        return sizeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getSize_Height()
    {
        return (EAttribute) sizeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getSize_Width()
    {
        return (EAttribute) sizeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getStyleMap()
    {
        return styleMapEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getStyleMap_ComponentName()
    {
        return (EAttribute) styleMapEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getStyleMap_Style()
    {
        return (EAttribute) styleMapEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getText()
    {
        return textEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getText_Value()
    {
        return (EAttribute) textEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getText_Font()
    {
        return (EReference) textEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getText_Color()
    {
        return (EReference) textEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getTextAlignment()
    {
        return textAlignmentEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getTextAlignment_HorizontalAlignment()
    {
        return (EAttribute) textAlignmentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getTextAlignment_VerticalAlignment()
    {
        return (EAttribute) textAlignmentEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getTooltipValue()
    {
        return tooltipValueEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getTooltipValue_Text()
    {
        return (EAttribute) tooltipValueEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getTooltipValue_Delay()
    {
        return (EAttribute) tooltipValueEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getURLValue()
    {
        return urlValueEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getURLValue_BaseUrl()
    {
        return (EAttribute) urlValueEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getURLValue_Target()
    {
        return (EAttribute) urlValueEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getURLValue_BaseParameterName()
    {
        return (EAttribute) urlValueEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getURLValue_ValueParameterName()
    {
        return (EAttribute) urlValueEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getURLValue_SeriesParameterName()
    {
        return (EAttribute) urlValueEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getActionType()
    {
        return actionTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getAnchor()
    {
        return anchorEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getChartType()
    {
        return chartTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getDataPointComponentType()
    {
        return dataPointComponentTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getDateFormatDetail()
    {
        return dateFormatDetailEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getDateFormatType()
    {
        return dateFormatTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getDirection()
    {
        return directionEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getHorizontalAlignment()
    {
        return horizontalAlignmentEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getIntersectionType()
    {
        return intersectionTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getLeaderLineStyle()
    {
        return leaderLineStyleEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getLegendItemType()
    {
        return legendItemTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getLineStyle()
    {
        return lineStyleEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getMarkerType()
    {
        return markerTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getOrientation()
    {
        return orientationEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getPosition()
    {
        return positionEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getRiserType()
    {
        return riserTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getRuleType()
    {
        return ruleTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getScaleUnitType()
    {
        return scaleUnitTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getSortOption()
    {
        return sortOptionEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getStretch()
    {
        return stretchEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getStyledComponent()
    {
        return styledComponentEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getTickStyle()
    {
        return tickStyleEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getTriggerCondition()
    {
        return triggerConditionEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getVerticalAlignment()
    {
        return verticalAlignmentEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getActionTypeObject()
    {
        return actionTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getAnchorObject()
    {
        return anchorObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getAxisTypeObject()
    {
        return axisTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getChartDimensionObject()
    {
        return chartDimensionObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getAxisType()
    {
        return axisTypeEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EEnum getChartDimension()
    {
        return chartDimensionEEnum;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getChartTypeObject()
    {
        return chartTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getDataPointComponentTypeObject()
    {
        return dataPointComponentTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getDateFormatDetailObject()
    {
        return dateFormatDetailObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getDateFormatTypeObject()
    {
        return dateFormatTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getDirectionObject()
    {
        return directionObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getHorizontalAlignmentObject()
    {
        return horizontalAlignmentObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getID()
    {
        return idEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getIntersectionTypeObject()
    {
        return intersectionTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getLeaderLineStyleObject()
    {
        return leaderLineStyleObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getLegendItemTypeObject()
    {
        return legendItemTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getLineStyleObject()
    {
        return lineStyleObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getMarkerTypeObject()
    {
        return markerTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getOrientationObject()
    {
        return orientationObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getPercentage()
    {
        return percentageEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getPercentageObject()
    {
        return percentageObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getPositionObject()
    {
        return positionObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getRGBValue()
    {
        return rgbValueEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getRGBValueObject()
    {
        return rgbValueObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getRiserTypeObject()
    {
        return riserTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getRuleTypeObject()
    {
        return ruleTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getScaleUnitTypeObject()
    {
        return scaleUnitTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getSortOptionObject()
    {
        return sortOptionObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getStretchObject()
    {
        return stretchObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getStyledComponentObject()
    {
        return styledComponentObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getTickStyleObject()
    {
        return tickStyleObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getTriggerConditionObject()
    {
        return triggerConditionObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EDataType getVerticalAlignmentObject()
    {
        return verticalAlignmentObjectEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public AttributeFactory getAttributeFactory()
    {
        return (AttributeFactory) getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on any invocation but
     * its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void createPackageContents()
    {
        if (isCreated)
            return;
        isCreated = true;

        // Create classes and their features
        actionValueEClass = createEClass(ACTION_VALUE);

        axisOriginEClass = createEClass(AXIS_ORIGIN);
        createEAttribute(axisOriginEClass, AXIS_ORIGIN__TYPE);
        createEAttribute(axisOriginEClass, AXIS_ORIGIN__VALUE);

        boundsEClass = createEClass(BOUNDS);
        createEAttribute(boundsEClass, BOUNDS__LEFT);
        createEAttribute(boundsEClass, BOUNDS__TOP);
        createEAttribute(boundsEClass, BOUNDS__WIDTH);
        createEAttribute(boundsEClass, BOUNDS__HEIGHT);

        colorDefinitionEClass = createEClass(COLOR_DEFINITION);
        createEAttribute(colorDefinitionEClass, COLOR_DEFINITION__TRANSPARENCY);
        createEAttribute(colorDefinitionEClass, COLOR_DEFINITION__RED);
        createEAttribute(colorDefinitionEClass, COLOR_DEFINITION__GREEN);
        createEAttribute(colorDefinitionEClass, COLOR_DEFINITION__BLUE);

        dataPointEClass = createEClass(DATA_POINT);
        createEReference(dataPointEClass, DATA_POINT__COMPONENTS);
        createEAttribute(dataPointEClass, DATA_POINT__PREFIX);
        createEAttribute(dataPointEClass, DATA_POINT__SUFFIX);
        createEAttribute(dataPointEClass, DATA_POINT__SEPARATOR);

        dataPointComponentEClass = createEClass(DATA_POINT_COMPONENT);
        createEAttribute(dataPointComponentEClass, DATA_POINT_COMPONENT__TYPE);
        createEReference(dataPointComponentEClass, DATA_POINT_COMPONENT__FORMAT_SPECIFIER);

        dateFormatSpecifierEClass = createEClass(DATE_FORMAT_SPECIFIER);
        createEAttribute(dateFormatSpecifierEClass, DATE_FORMAT_SPECIFIER__TYPE);
        createEAttribute(dateFormatSpecifierEClass, DATE_FORMAT_SPECIFIER__DETAIL);

        extendedPropertyEClass = createEClass(EXTENDED_PROPERTY);
        createEAttribute(extendedPropertyEClass, EXTENDED_PROPERTY__NAME);
        createEAttribute(extendedPropertyEClass, EXTENDED_PROPERTY__VALUE);

        fillEClass = createEClass(FILL);
        createEAttribute(fillEClass, FILL__TYPE);

        fontDefinitionEClass = createEClass(FONT_DEFINITION);
        createEAttribute(fontDefinitionEClass, FONT_DEFINITION__NAME);
        createEAttribute(fontDefinitionEClass, FONT_DEFINITION__SIZE);
        createEAttribute(fontDefinitionEClass, FONT_DEFINITION__BOLD);
        createEAttribute(fontDefinitionEClass, FONT_DEFINITION__ITALIC);
        createEAttribute(fontDefinitionEClass, FONT_DEFINITION__STRIKETHROUGH);
        createEAttribute(fontDefinitionEClass, FONT_DEFINITION__UNDERLINE);
        createEAttribute(fontDefinitionEClass, FONT_DEFINITION__WORD_WRAP);
        createEReference(fontDefinitionEClass, FONT_DEFINITION__ALIGNMENT);
        createEAttribute(fontDefinitionEClass, FONT_DEFINITION__ROTATION);

        formatSpecifierEClass = createEClass(FORMAT_SPECIFIER);

        gradientEClass = createEClass(GRADIENT);
        createEReference(gradientEClass, GRADIENT__START_COLOR);
        createEReference(gradientEClass, GRADIENT__END_COLOR);
        createEAttribute(gradientEClass, GRADIENT__DIRECTION);
        createEAttribute(gradientEClass, GRADIENT__CYCLIC);
        createEAttribute(gradientEClass, GRADIENT__TRANSPARENCY);

        imageEClass = createEClass(IMAGE);
        createEAttribute(imageEClass, IMAGE__URL);

        insetsEClass = createEClass(INSETS);
        createEAttribute(insetsEClass, INSETS__TOP);
        createEAttribute(insetsEClass, INSETS__LEFT);
        createEAttribute(insetsEClass, INSETS__BOTTOM);
        createEAttribute(insetsEClass, INSETS__RIGHT);

        javaDateFormatSpecifierEClass = createEClass(JAVA_DATE_FORMAT_SPECIFIER);
        createEAttribute(javaDateFormatSpecifierEClass, JAVA_DATE_FORMAT_SPECIFIER__PATTERN);

        javaNumberFormatSpecifierEClass = createEClass(JAVA_NUMBER_FORMAT_SPECIFIER);
        createEAttribute(javaNumberFormatSpecifierEClass, JAVA_NUMBER_FORMAT_SPECIFIER__PATTERN);
        createEAttribute(javaNumberFormatSpecifierEClass, JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER);

        lineAttributesEClass = createEClass(LINE_ATTRIBUTES);
        createEAttribute(lineAttributesEClass, LINE_ATTRIBUTES__STYLE);
        createEAttribute(lineAttributesEClass, LINE_ATTRIBUTES__THICKNESS);
        createEReference(lineAttributesEClass, LINE_ATTRIBUTES__COLOR);
        createEAttribute(lineAttributesEClass, LINE_ATTRIBUTES__VISIBLE);

        locationEClass = createEClass(LOCATION);
        createEAttribute(locationEClass, LOCATION__X);
        createEAttribute(locationEClass, LOCATION__Y);

        markerEClass = createEClass(MARKER);
        createEAttribute(markerEClass, MARKER__TYPE);
        createEAttribute(markerEClass, MARKER__SIZE);
        createEAttribute(markerEClass, MARKER__VISIBLE);

        numberFormatSpecifierEClass = createEClass(NUMBER_FORMAT_SPECIFIER);
        createEAttribute(numberFormatSpecifierEClass, NUMBER_FORMAT_SPECIFIER__PREFIX);
        createEAttribute(numberFormatSpecifierEClass, NUMBER_FORMAT_SPECIFIER__SUFFIX);
        createEAttribute(numberFormatSpecifierEClass, NUMBER_FORMAT_SPECIFIER__MULTIPLIER);
        createEAttribute(numberFormatSpecifierEClass, NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS);

        paletteEClass = createEClass(PALETTE);
        createEAttribute(paletteEClass, PALETTE__NAME);
        createEReference(paletteEClass, PALETTE__ENTRIES);

        scriptValueEClass = createEClass(SCRIPT_VALUE);
        createEAttribute(scriptValueEClass, SCRIPT_VALUE__SCRIPT);

        seriesHintEClass = createEClass(SERIES_HINT);
        createEAttribute(seriesHintEClass, SERIES_HINT__SERIES_CLASS);
        createEAttribute(seriesHintEClass, SERIES_HINT__MODIFIER);

        seriesValueEClass = createEClass(SERIES_VALUE);
        createEAttribute(seriesValueEClass, SERIES_VALUE__NAME);

        sizeEClass = createEClass(SIZE);
        createEAttribute(sizeEClass, SIZE__HEIGHT);
        createEAttribute(sizeEClass, SIZE__WIDTH);

        styleMapEClass = createEClass(STYLE_MAP);
        createEAttribute(styleMapEClass, STYLE_MAP__COMPONENT_NAME);
        createEAttribute(styleMapEClass, STYLE_MAP__STYLE);

        textEClass = createEClass(TEXT);
        createEAttribute(textEClass, TEXT__VALUE);
        createEReference(textEClass, TEXT__FONT);
        createEReference(textEClass, TEXT__COLOR);

        textAlignmentEClass = createEClass(TEXT_ALIGNMENT);
        createEAttribute(textAlignmentEClass, TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT);
        createEAttribute(textAlignmentEClass, TEXT_ALIGNMENT__VERTICAL_ALIGNMENT);

        tooltipValueEClass = createEClass(TOOLTIP_VALUE);
        createEAttribute(tooltipValueEClass, TOOLTIP_VALUE__TEXT);
        createEAttribute(tooltipValueEClass, TOOLTIP_VALUE__DELAY);

        urlValueEClass = createEClass(URL_VALUE);
        createEAttribute(urlValueEClass, URL_VALUE__BASE_URL);
        createEAttribute(urlValueEClass, URL_VALUE__TARGET);
        createEAttribute(urlValueEClass, URL_VALUE__BASE_PARAMETER_NAME);
        createEAttribute(urlValueEClass, URL_VALUE__VALUE_PARAMETER_NAME);
        createEAttribute(urlValueEClass, URL_VALUE__SERIES_PARAMETER_NAME);

        // Create enums
        actionTypeEEnum = createEEnum(ACTION_TYPE);
        anchorEEnum = createEEnum(ANCHOR);
        axisTypeEEnum = createEEnum(AXIS_TYPE);
        chartDimensionEEnum = createEEnum(CHART_DIMENSION);
        chartTypeEEnum = createEEnum(CHART_TYPE);
        dataPointComponentTypeEEnum = createEEnum(DATA_POINT_COMPONENT_TYPE);
        dateFormatDetailEEnum = createEEnum(DATE_FORMAT_DETAIL);
        dateFormatTypeEEnum = createEEnum(DATE_FORMAT_TYPE);
        directionEEnum = createEEnum(DIRECTION);
        horizontalAlignmentEEnum = createEEnum(HORIZONTAL_ALIGNMENT);
        intersectionTypeEEnum = createEEnum(INTERSECTION_TYPE);
        leaderLineStyleEEnum = createEEnum(LEADER_LINE_STYLE);
        legendItemTypeEEnum = createEEnum(LEGEND_ITEM_TYPE);
        lineStyleEEnum = createEEnum(LINE_STYLE);
        markerTypeEEnum = createEEnum(MARKER_TYPE);
        orientationEEnum = createEEnum(ORIENTATION);
        positionEEnum = createEEnum(POSITION);
        riserTypeEEnum = createEEnum(RISER_TYPE);
        ruleTypeEEnum = createEEnum(RULE_TYPE);
        scaleUnitTypeEEnum = createEEnum(SCALE_UNIT_TYPE);
        sortOptionEEnum = createEEnum(SORT_OPTION);
        stretchEEnum = createEEnum(STRETCH);
        styledComponentEEnum = createEEnum(STYLED_COMPONENT);
        tickStyleEEnum = createEEnum(TICK_STYLE);
        triggerConditionEEnum = createEEnum(TRIGGER_CONDITION);
        verticalAlignmentEEnum = createEEnum(VERTICAL_ALIGNMENT);

        // Create data types
        actionTypeObjectEDataType = createEDataType(ACTION_TYPE_OBJECT);
        anchorObjectEDataType = createEDataType(ANCHOR_OBJECT);
        axisTypeObjectEDataType = createEDataType(AXIS_TYPE_OBJECT);
        chartDimensionObjectEDataType = createEDataType(CHART_DIMENSION_OBJECT);
        chartTypeObjectEDataType = createEDataType(CHART_TYPE_OBJECT);
        dataPointComponentTypeObjectEDataType = createEDataType(DATA_POINT_COMPONENT_TYPE_OBJECT);
        dateFormatDetailObjectEDataType = createEDataType(DATE_FORMAT_DETAIL_OBJECT);
        dateFormatTypeObjectEDataType = createEDataType(DATE_FORMAT_TYPE_OBJECT);
        directionObjectEDataType = createEDataType(DIRECTION_OBJECT);
        horizontalAlignmentObjectEDataType = createEDataType(HORIZONTAL_ALIGNMENT_OBJECT);
        idEDataType = createEDataType(ID);
        intersectionTypeObjectEDataType = createEDataType(INTERSECTION_TYPE_OBJECT);
        leaderLineStyleObjectEDataType = createEDataType(LEADER_LINE_STYLE_OBJECT);
        legendItemTypeObjectEDataType = createEDataType(LEGEND_ITEM_TYPE_OBJECT);
        lineStyleObjectEDataType = createEDataType(LINE_STYLE_OBJECT);
        markerTypeObjectEDataType = createEDataType(MARKER_TYPE_OBJECT);
        orientationObjectEDataType = createEDataType(ORIENTATION_OBJECT);
        percentageEDataType = createEDataType(PERCENTAGE);
        percentageObjectEDataType = createEDataType(PERCENTAGE_OBJECT);
        positionObjectEDataType = createEDataType(POSITION_OBJECT);
        rgbValueEDataType = createEDataType(RGB_VALUE);
        rgbValueObjectEDataType = createEDataType(RGB_VALUE_OBJECT);
        riserTypeObjectEDataType = createEDataType(RISER_TYPE_OBJECT);
        ruleTypeObjectEDataType = createEDataType(RULE_TYPE_OBJECT);
        scaleUnitTypeObjectEDataType = createEDataType(SCALE_UNIT_TYPE_OBJECT);
        sortOptionObjectEDataType = createEDataType(SORT_OPTION_OBJECT);
        stretchObjectEDataType = createEDataType(STRETCH_OBJECT);
        styledComponentObjectEDataType = createEDataType(STYLED_COMPONENT_OBJECT);
        tickStyleObjectEDataType = createEDataType(TICK_STYLE_OBJECT);
        triggerConditionObjectEDataType = createEDataType(TRIGGER_CONDITION_OBJECT);
        verticalAlignmentObjectEDataType = createEDataType(VERTICAL_ALIGNMENT_OBJECT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have no affect on any
     * invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void initializePackageContents()
    {
        if (isInitialized)
            return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        XMLTypePackageImpl theXMLTypePackage = (XMLTypePackageImpl) EPackage.Registry.INSTANCE
            .getEPackage(XMLTypePackage.eNS_URI);

        // Add supertypes to classes
        colorDefinitionEClass.getESuperTypes().add(this.getFill());
        dateFormatSpecifierEClass.getESuperTypes().add(this.getFormatSpecifier());
        gradientEClass.getESuperTypes().add(this.getFill());
        imageEClass.getESuperTypes().add(this.getFill());
        javaDateFormatSpecifierEClass.getESuperTypes().add(this.getFormatSpecifier());
        javaNumberFormatSpecifierEClass.getESuperTypes().add(this.getFormatSpecifier());
        numberFormatSpecifierEClass.getESuperTypes().add(this.getFormatSpecifier());
        scriptValueEClass.getESuperTypes().add(this.getActionValue());
        seriesValueEClass.getESuperTypes().add(this.getActionValue());
        tooltipValueEClass.getESuperTypes().add(this.getActionValue());
        urlValueEClass.getESuperTypes().add(this.getActionValue());

        // Initialize classes and features; add operations and parameters
        initEClass(actionValueEClass, ActionValue.class, "ActionValue", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);

        initEClass(axisOriginEClass, AxisOrigin.class, "AxisOrigin", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAxisOrigin_Type(), this.getIntersectionType(), "type", "Min", 1, 1, AxisOrigin.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAxisOrigin_Value(), theXMLTypePackage.getAnySimpleType(), "value", null, 1, 1,
            AxisOrigin.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(boundsEClass, Bounds.class, "Bounds", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBounds_Left(), theXMLTypePackage.getDouble(), "left", null, 1, 1, Bounds.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBounds_Top(), theXMLTypePackage.getDouble(), "top", null, 1, 1, Bounds.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBounds_Width(), theXMLTypePackage.getDouble(), "width", null, 1, 1, Bounds.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBounds_Height(), theXMLTypePackage.getDouble(), "height", null, 1, 1, Bounds.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(colorDefinitionEClass, ColorDefinition.class, "ColorDefinition", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getColorDefinition_Transparency(), theXMLTypePackage.getInt(), "transparency", null, 1, 1,
            ColorDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColorDefinition_Red(), this.getRGBValue(), "red", null, 1, 1, ColorDefinition.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColorDefinition_Green(), this.getRGBValue(), "green", null, 1, 1, ColorDefinition.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getColorDefinition_Blue(), this.getRGBValue(), "blue", null, 1, 1, ColorDefinition.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dataPointEClass, DataPoint.class, "DataPoint", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDataPoint_Components(), this.getDataPointComponent(), null, "components", null, 1, -1,
            DataPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
            !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDataPoint_Prefix(), theXMLTypePackage.getString(), "prefix", null, 1, 1, DataPoint.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDataPoint_Suffix(), theXMLTypePackage.getString(), "suffix", null, 1, 1, DataPoint.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDataPoint_Separator(), theXMLTypePackage.getString(), "separator", null, 1, 1,
            DataPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(dataPointComponentEClass, DataPointComponent.class, "DataPointComponent", !IS_ABSTRACT,
            !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDataPointComponent_Type(), this.getDataPointComponentType(), "type", "Base_Value", 1, 1,
            DataPointComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEReference(getDataPointComponent_FormatSpecifier(), this.getFormatSpecifier(), null, "formatSpecifier",
            null, 1, 1, DataPointComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
            !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dateFormatSpecifierEClass, DateFormatSpecifier.class, "DateFormatSpecifier", !IS_ABSTRACT,
            !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDateFormatSpecifier_Type(), this.getDateFormatType(), "type", "Long", 1, 1,
            DateFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDateFormatSpecifier_Detail(), this.getDateFormatDetail(), "detail", "Date", 1, 1,
            DateFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(extendedPropertyEClass, ExtendedProperty.class, "ExtendedProperty", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getExtendedProperty_Name(), theXMLTypePackage.getString(), "name", null, 1, 1,
            ExtendedProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getExtendedProperty_Value(), theXMLTypePackage.getString(), "value", null, 1, 1,
            ExtendedProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(fillEClass, Fill.class, "Fill", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFill_Type(), theXMLTypePackage.getInt(), "type", null, 1, 1, Fill.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(fontDefinitionEClass, FontDefinition.class, "FontDefinition", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFontDefinition_Name(), theXMLTypePackage.getString(), "name", null, 1, 1,
            FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFontDefinition_Size(), theXMLTypePackage.getFloat(), "size", null, 1, 1,
            FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFontDefinition_Bold(), theXMLTypePackage.getBoolean(), "bold", null, 1, 1,
            FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFontDefinition_Italic(), theXMLTypePackage.getBoolean(), "italic", null, 1, 1,
            FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFontDefinition_Strikethrough(), theXMLTypePackage.getBoolean(), "strikethrough", null, 1, 1,
            FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFontDefinition_Underline(), theXMLTypePackage.getBoolean(), "underline", null, 1, 1,
            FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFontDefinition_WordWrap(), theXMLTypePackage.getBoolean(), "wordWrap", null, 1, 1,
            FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEReference(getFontDefinition_Alignment(), this.getTextAlignment(), null, "alignment", null, 1, 1,
            FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
            !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFontDefinition_Rotation(), theXMLTypePackage.getDouble(), "rotation", null, 1, 1,
            FontDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(formatSpecifierEClass, FormatSpecifier.class, "FormatSpecifier", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);

        initEClass(gradientEClass, Gradient.class, "Gradient", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGradient_StartColor(), this.getColorDefinition(), null, "startColor", null, 1, 1,
            Gradient.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
            !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGradient_EndColor(), this.getColorDefinition(), null, "endColor", null, 1, 1, Gradient.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGradient_Direction(), theXMLTypePackage.getDouble(), "direction", null, 1, 1, Gradient.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGradient_Cyclic(), theXMLTypePackage.getBoolean(), "cyclic", null, 1, 1, Gradient.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGradient_Transparency(), theXMLTypePackage.getInt(), "transparency", null, 1, 1,
            Gradient.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);

        initEClass(imageEClass, Image.class, "Image", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getImage_URL(), theXMLTypePackage.getString(), "uRL", null, 1, 1, Image.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(insetsEClass, Insets.class, "Insets", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getInsets_Top(), theXMLTypePackage.getDouble(), "top", null, 1, 1, Insets.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsets_Left(), theXMLTypePackage.getDouble(), "left", null, 1, 1, Insets.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsets_Bottom(), theXMLTypePackage.getDouble(), "bottom", null, 1, 1, Insets.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsets_Right(), theXMLTypePackage.getDouble(), "right", null, 1, 1, Insets.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(javaDateFormatSpecifierEClass, JavaDateFormatSpecifier.class, "JavaDateFormatSpecifier",
            !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getJavaDateFormatSpecifier_Pattern(), theXMLTypePackage.getString(), "pattern", null, 1, 1,
            JavaDateFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
            !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(javaNumberFormatSpecifierEClass, JavaNumberFormatSpecifier.class, "JavaNumberFormatSpecifier",
            !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getJavaNumberFormatSpecifier_Pattern(), theXMLTypePackage.getString(), "pattern", null, 1, 1,
            JavaNumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
            !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJavaNumberFormatSpecifier_Multiplier(), theXMLTypePackage.getDouble(), "multiplier", null, 1,
            1, JavaNumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID,
            !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(lineAttributesEClass, LineAttributes.class, "LineAttributes", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLineAttributes_Style(), this.getLineStyle(), "style", "Solid", 1, 1, LineAttributes.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLineAttributes_Thickness(), theXMLTypePackage.getInt(), "thickness", null, 1, 1,
            LineAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEReference(getLineAttributes_Color(), this.getColorDefinition(), null, "color", null, 1, 1,
            LineAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
            !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLineAttributes_Visible(), theXMLTypePackage.getBoolean(), "visible", null, 1, 1,
            LineAttributes.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(locationEClass, Location.class, "Location", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLocation_X(), theXMLTypePackage.getDouble(), "x", null, 1, 1, Location.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLocation_Y(), theXMLTypePackage.getDouble(), "y", null, 1, 1, Location.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(markerEClass, Marker.class, "Marker", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMarker_Type(), this.getMarkerType(), "type", "Crosshair", 1, 1, Marker.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMarker_Size(), theXMLTypePackage.getInt(), "size", null, 1, 1, Marker.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMarker_Visible(), theXMLTypePackage.getBoolean(), "visible", null, 1, 1, Marker.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(numberFormatSpecifierEClass, NumberFormatSpecifier.class, "NumberFormatSpecifier", !IS_ABSTRACT,
            !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getNumberFormatSpecifier_Prefix(), theXMLTypePackage.getString(), "prefix", null, 1, 1,
            NumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
            !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNumberFormatSpecifier_Suffix(), theXMLTypePackage.getString(), "suffix", null, 1, 1,
            NumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
            !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNumberFormatSpecifier_Multiplier(), theXMLTypePackage.getDouble(), "multiplier", null, 1, 1,
            NumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNumberFormatSpecifier_FractionDigits(), theXMLTypePackage.getInt(), "fractionDigits", null,
            1, 1, NumberFormatSpecifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID,
            !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(paletteEClass, Palette.class, "Palette", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPalette_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, Palette.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPalette_Entries(), this.getFill(), null, "entries", null, 1, -1, Palette.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(scriptValueEClass, ScriptValue.class, "ScriptValue", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getScriptValue_Script(), theXMLTypePackage.getString(), "script", null, 1, 1, ScriptValue.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(seriesHintEClass, SeriesHint.class, "SeriesHint", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSeriesHint_SeriesClass(), theXMLTypePackage.getString(), "seriesClass", null, 1, 1,
            SeriesHint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSeriesHint_Modifier(), theXMLTypePackage.getString(), "modifier", null, 1, 1,
            SeriesHint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(seriesValueEClass, SeriesValue.class, "SeriesValue", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSeriesValue_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, SeriesValue.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sizeEClass, Size.class, "Size", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSize_Height(), theXMLTypePackage.getDouble(), "height", null, 1, 1, Size.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSize_Width(), theXMLTypePackage.getDouble(), "width", null, 1, 1, Size.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(styleMapEClass, StyleMap.class, "StyleMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStyleMap_ComponentName(), this.getStyledComponent(), "componentName", "Chart_Title", 1, 1,
            StyleMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);
        initEAttribute(getStyleMap_Style(), theXMLTypePackage.getString(), "style", null, 1, 1, StyleMap.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(textEClass, Text.class, "Text", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getText_Value(), theXMLTypePackage.getString(), "value", null, 1, 1, Text.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getText_Font(), this.getFontDefinition(), null, "font", null, 1, 1, Text.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);
        initEReference(getText_Color(), this.getColorDefinition(), null, "color", null, 1, 1, Text.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(textAlignmentEClass, TextAlignment.class, "TextAlignment", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTextAlignment_HorizontalAlignment(), this.getHorizontalAlignment(), "horizontalAlignment",
            "Left", 1, 1, TextAlignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID,
            !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTextAlignment_VerticalAlignment(), this.getVerticalAlignment(), "verticalAlignment", "Top",
            1, 1, TextAlignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(tooltipValueEClass, TooltipValue.class, "TooltipValue", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTooltipValue_Text(), theXMLTypePackage.getString(), "text", null, 1, 1, TooltipValue.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTooltipValue_Delay(), theXMLTypePackage.getInt(), "delay", null, 1, 1, TooltipValue.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(urlValueEClass, URLValue.class, "URLValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getURLValue_BaseUrl(), theXMLTypePackage.getString(), "baseUrl", null, 1, 1, URLValue.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getURLValue_Target(), theXMLTypePackage.getString(), "target", null, 1, 1, URLValue.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getURLValue_BaseParameterName(), theXMLTypePackage.getString(), "baseParameterName", null, 1, 1,
            URLValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getURLValue_ValueParameterName(), theXMLTypePackage.getString(), "valueParameterName", null, 1,
            1, URLValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getURLValue_SeriesParameterName(), theXMLTypePackage.getString(), "seriesParameterName", null,
            1, 1, URLValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(actionTypeEEnum, ActionType.class, "ActionType");
        addEEnumLiteral(actionTypeEEnum, ActionType.URL_REDIRECT_LITERAL);
        addEEnumLiteral(actionTypeEEnum, ActionType.SHOW_TOOLTIP_LITERAL);
        addEEnumLiteral(actionTypeEEnum, ActionType.TOGGLE_VISIBILITY_LITERAL);
        addEEnumLiteral(actionTypeEEnum, ActionType.INVOKE_SCRIPT_LITERAL);

        initEEnum(anchorEEnum, Anchor.class, "Anchor");
        addEEnumLiteral(anchorEEnum, Anchor.NORTH_LITERAL);
        addEEnumLiteral(anchorEEnum, Anchor.NORTH_EAST_LITERAL);
        addEEnumLiteral(anchorEEnum, Anchor.EAST_LITERAL);
        addEEnumLiteral(anchorEEnum, Anchor.SOUTH_EAST_LITERAL);
        addEEnumLiteral(anchorEEnum, Anchor.SOUTH_LITERAL);
        addEEnumLiteral(anchorEEnum, Anchor.SOUTH_WEST_LITERAL);
        addEEnumLiteral(anchorEEnum, Anchor.WEST_LITERAL);
        addEEnumLiteral(anchorEEnum, Anchor.NORTH_WEST_LITERAL);

        initEEnum(axisTypeEEnum, AxisType.class, "AxisType");
        addEEnumLiteral(axisTypeEEnum, AxisType.LINEAR_LITERAL);
        addEEnumLiteral(axisTypeEEnum, AxisType.LOGARITHMIC_LITERAL);
        addEEnumLiteral(axisTypeEEnum, AxisType.TEXT_LITERAL);
        addEEnumLiteral(axisTypeEEnum, AxisType.DATE_TIME_LITERAL);

        initEEnum(chartDimensionEEnum, ChartDimension.class, "ChartDimension");
        addEEnumLiteral(chartDimensionEEnum, ChartDimension.TWO_DIMENSIONAL_LITERAL);
        addEEnumLiteral(chartDimensionEEnum, ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
        addEEnumLiteral(chartDimensionEEnum, ChartDimension.THREE_DIMENSIONAL_LITERAL);

        initEEnum(chartTypeEEnum, ChartType.class, "ChartType");
        addEEnumLiteral(chartTypeEEnum, ChartType.PIE_LITERAL);
        addEEnumLiteral(chartTypeEEnum, ChartType.BAR_LITERAL);
        addEEnumLiteral(chartTypeEEnum, ChartType.LINE_LITERAL);
        addEEnumLiteral(chartTypeEEnum, ChartType.COMBO_LITERAL);
        addEEnumLiteral(chartTypeEEnum, ChartType.SCATTER_LITERAL);
        addEEnumLiteral(chartTypeEEnum, ChartType.STOCK_LITERAL);

        initEEnum(dataPointComponentTypeEEnum, DataPointComponentType.class, "DataPointComponentType");
        addEEnumLiteral(dataPointComponentTypeEEnum, DataPointComponentType.BASE_VALUE_LITERAL);
        addEEnumLiteral(dataPointComponentTypeEEnum, DataPointComponentType.ORTHOGONAL_VALUE_LITERAL);
        addEEnumLiteral(dataPointComponentTypeEEnum, DataPointComponentType.SERIES_VALUE_LITERAL);

        initEEnum(dateFormatDetailEEnum, DateFormatDetail.class, "DateFormatDetail");
        addEEnumLiteral(dateFormatDetailEEnum, DateFormatDetail.DATE_LITERAL);
        addEEnumLiteral(dateFormatDetailEEnum, DateFormatDetail.DATE_TIME_LITERAL);

        initEEnum(dateFormatTypeEEnum, DateFormatType.class, "DateFormatType");
        addEEnumLiteral(dateFormatTypeEEnum, DateFormatType.LONG_LITERAL);
        addEEnumLiteral(dateFormatTypeEEnum, DateFormatType.SHORT_LITERAL);
        addEEnumLiteral(dateFormatTypeEEnum, DateFormatType.MEDIUM_LITERAL);
        addEEnumLiteral(dateFormatTypeEEnum, DateFormatType.FULL_LITERAL);

        initEEnum(directionEEnum, Direction.class, "Direction");
        addEEnumLiteral(directionEEnum, Direction.LEFT_RIGHT_LITERAL);
        addEEnumLiteral(directionEEnum, Direction.TOP_BOTTOM_LITERAL);

        initEEnum(horizontalAlignmentEEnum, HorizontalAlignment.class, "HorizontalAlignment");
        addEEnumLiteral(horizontalAlignmentEEnum, HorizontalAlignment.LEFT_LITERAL);
        addEEnumLiteral(horizontalAlignmentEEnum, HorizontalAlignment.CENTER_LITERAL);
        addEEnumLiteral(horizontalAlignmentEEnum, HorizontalAlignment.RIGHT_LITERAL);

        initEEnum(intersectionTypeEEnum, IntersectionType.class, "IntersectionType");
        addEEnumLiteral(intersectionTypeEEnum, IntersectionType.MIN_LITERAL);
        addEEnumLiteral(intersectionTypeEEnum, IntersectionType.MAX_LITERAL);
        addEEnumLiteral(intersectionTypeEEnum, IntersectionType.VALUE_LITERAL);

        initEEnum(leaderLineStyleEEnum, LeaderLineStyle.class, "LeaderLineStyle");
        addEEnumLiteral(leaderLineStyleEEnum, LeaderLineStyle.FIXED_LENGTH_LITERAL);
        addEEnumLiteral(leaderLineStyleEEnum, LeaderLineStyle.STRETCH_TO_SIDE_LITERAL);

        initEEnum(legendItemTypeEEnum, LegendItemType.class, "LegendItemType");
        addEEnumLiteral(legendItemTypeEEnum, LegendItemType.SERIES_LITERAL);
        addEEnumLiteral(legendItemTypeEEnum, LegendItemType.CATEGORIES_LITERAL);

        initEEnum(lineStyleEEnum, LineStyle.class, "LineStyle");
        addEEnumLiteral(lineStyleEEnum, LineStyle.SOLID_LITERAL);
        addEEnumLiteral(lineStyleEEnum, LineStyle.DASHED_LITERAL);
        addEEnumLiteral(lineStyleEEnum, LineStyle.DOTTED_LITERAL);
        addEEnumLiteral(lineStyleEEnum, LineStyle.DASH_DOTTED_LITERAL);

        initEEnum(markerTypeEEnum, MarkerType.class, "MarkerType");
        addEEnumLiteral(markerTypeEEnum, MarkerType.CROSSHAIR_LITERAL);
        addEEnumLiteral(markerTypeEEnum, MarkerType.TRIANGLE_LITERAL);
        addEEnumLiteral(markerTypeEEnum, MarkerType.BOX_LITERAL);
        addEEnumLiteral(markerTypeEEnum, MarkerType.CIRCLE_LITERAL);

        initEEnum(orientationEEnum, Orientation.class, "Orientation");
        addEEnumLiteral(orientationEEnum, Orientation.HORIZONTAL_LITERAL);
        addEEnumLiteral(orientationEEnum, Orientation.VERTICAL_LITERAL);

        initEEnum(positionEEnum, Position.class, "Position");
        addEEnumLiteral(positionEEnum, Position.ABOVE_LITERAL);
        addEEnumLiteral(positionEEnum, Position.BELOW_LITERAL);
        addEEnumLiteral(positionEEnum, Position.LEFT_LITERAL);
        addEEnumLiteral(positionEEnum, Position.RIGHT_LITERAL);
        addEEnumLiteral(positionEEnum, Position.INSIDE_LITERAL);
        addEEnumLiteral(positionEEnum, Position.OUTSIDE_LITERAL);

        initEEnum(riserTypeEEnum, RiserType.class, "RiserType");
        addEEnumLiteral(riserTypeEEnum, RiserType.RECTANGLE_LITERAL);
        addEEnumLiteral(riserTypeEEnum, RiserType.TRIANGLE_LITERAL);

        initEEnum(ruleTypeEEnum, RuleType.class, "RuleType");
        addEEnumLiteral(ruleTypeEEnum, RuleType.FILTER_LITERAL);
        addEEnumLiteral(ruleTypeEEnum, RuleType.SUPPRESS_LITERAL);
        addEEnumLiteral(ruleTypeEEnum, RuleType.LINK_LITERAL);

        initEEnum(scaleUnitTypeEEnum, ScaleUnitType.class, "ScaleUnitType");
        addEEnumLiteral(scaleUnitTypeEEnum, ScaleUnitType.SECONDS_LITERAL);
        addEEnumLiteral(scaleUnitTypeEEnum, ScaleUnitType.MINUTES_LITERAL);
        addEEnumLiteral(scaleUnitTypeEEnum, ScaleUnitType.HOURS_LITERAL);
        addEEnumLiteral(scaleUnitTypeEEnum, ScaleUnitType.DAYS_LITERAL);
        addEEnumLiteral(scaleUnitTypeEEnum, ScaleUnitType.WEEKS_LITERAL);
        addEEnumLiteral(scaleUnitTypeEEnum, ScaleUnitType.MONTHS_LITERAL);
        addEEnumLiteral(scaleUnitTypeEEnum, ScaleUnitType.YEARS_LITERAL);

        initEEnum(sortOptionEEnum, SortOption.class, "SortOption");
        addEEnumLiteral(sortOptionEEnum, SortOption.ASCENDING_LITERAL);
        addEEnumLiteral(sortOptionEEnum, SortOption.DESCENDING_LITERAL);

        initEEnum(stretchEEnum, Stretch.class, "Stretch");
        addEEnumLiteral(stretchEEnum, Stretch.HORIZONTAL_LITERAL);
        addEEnumLiteral(stretchEEnum, Stretch.VERTICAL_LITERAL);
        addEEnumLiteral(stretchEEnum, Stretch.BOTH_LITERAL);

        initEEnum(styledComponentEEnum, StyledComponent.class, "StyledComponent");
        addEEnumLiteral(styledComponentEEnum, StyledComponent.CHART_TITLE_LITERAL);
        addEEnumLiteral(styledComponentEEnum, StyledComponent.CHART_BACKGROUND_LITERAL);
        addEEnumLiteral(styledComponentEEnum, StyledComponent.PLOT_BACKGROUND_LITERAL);
        addEEnumLiteral(styledComponentEEnum, StyledComponent.LEGEND_BACKGROUND_LITERAL);
        addEEnumLiteral(styledComponentEEnum, StyledComponent.LEGEND_LABEL_LITERAL);
        addEEnumLiteral(styledComponentEEnum, StyledComponent.DATA_LABEL_LITERAL);
        addEEnumLiteral(styledComponentEEnum, StyledComponent.AXIS_TITLE_LITERAL);
        addEEnumLiteral(styledComponentEEnum, StyledComponent.AXIS_LABEL_LITERAL);
        addEEnumLiteral(styledComponentEEnum, StyledComponent.AXIS_LINE_LITERAL);
        addEEnumLiteral(styledComponentEEnum, StyledComponent.SERIES_TITLE_LITERAL);
        addEEnumLiteral(styledComponentEEnum, StyledComponent.SERIES_LABEL_LITERAL);

        initEEnum(tickStyleEEnum, TickStyle.class, "TickStyle");
        addEEnumLiteral(tickStyleEEnum, TickStyle.LEFT_LITERAL);
        addEEnumLiteral(tickStyleEEnum, TickStyle.RIGHT_LITERAL);
        addEEnumLiteral(tickStyleEEnum, TickStyle.ABOVE_LITERAL);
        addEEnumLiteral(tickStyleEEnum, TickStyle.BELOW_LITERAL);
        addEEnumLiteral(tickStyleEEnum, TickStyle.ACROSS_LITERAL);

        initEEnum(triggerConditionEEnum, TriggerCondition.class, "TriggerCondition");
        addEEnumLiteral(triggerConditionEEnum, TriggerCondition.MOUSE_HOVER_LITERAL);
        addEEnumLiteral(triggerConditionEEnum, TriggerCondition.MOUSE_CLICK_LITERAL);

        initEEnum(verticalAlignmentEEnum, VerticalAlignment.class, "VerticalAlignment");
        addEEnumLiteral(verticalAlignmentEEnum, VerticalAlignment.TOP_LITERAL);
        addEEnumLiteral(verticalAlignmentEEnum, VerticalAlignment.CENTER_LITERAL);
        addEEnumLiteral(verticalAlignmentEEnum, VerticalAlignment.BOTTOM_LITERAL);

        // Initialize data types
        initEDataType(actionTypeObjectEDataType, ActionType.class, "ActionTypeObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(anchorObjectEDataType, Anchor.class, "AnchorObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(axisTypeObjectEDataType, AxisType.class, "AxisTypeObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(chartDimensionObjectEDataType, ChartDimension.class, "ChartDimensionObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(chartTypeObjectEDataType, ChartType.class, "ChartTypeObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(dataPointComponentTypeObjectEDataType, DataPointComponentType.class,
            "DataPointComponentTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(dateFormatDetailObjectEDataType, DateFormatDetail.class, "DateFormatDetailObject",
            IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(dateFormatTypeObjectEDataType, DateFormatType.class, "DateFormatTypeObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(directionObjectEDataType, Direction.class, "DirectionObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(horizontalAlignmentObjectEDataType, HorizontalAlignment.class, "HorizontalAlignmentObject",
            IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(idEDataType, String.class, "ID", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(intersectionTypeObjectEDataType, IntersectionType.class, "IntersectionTypeObject",
            IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(leaderLineStyleObjectEDataType, LeaderLineStyle.class, "LeaderLineStyleObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(legendItemTypeObjectEDataType, LegendItemType.class, "LegendItemTypeObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(lineStyleObjectEDataType, LineStyle.class, "LineStyleObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(markerTypeObjectEDataType, MarkerType.class, "MarkerTypeObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(orientationObjectEDataType, Orientation.class, "OrientationObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(percentageEDataType, double.class, "Percentage", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(percentageObjectEDataType, Double.class, "PercentageObject", IS_SERIALIZABLE,
            !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(positionObjectEDataType, Position.class, "PositionObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(rgbValueEDataType, int.class, "RGBValue", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(rgbValueObjectEDataType, Integer.class, "RGBValueObject", IS_SERIALIZABLE,
            !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(riserTypeObjectEDataType, RiserType.class, "RiserTypeObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(ruleTypeObjectEDataType, RuleType.class, "RuleTypeObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(scaleUnitTypeObjectEDataType, ScaleUnitType.class, "ScaleUnitTypeObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(sortOptionObjectEDataType, SortOption.class, "SortOptionObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(stretchObjectEDataType, Stretch.class, "StretchObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(styledComponentObjectEDataType, StyledComponent.class, "StyledComponentObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(tickStyleObjectEDataType, TickStyle.class, "TickStyleObject", IS_SERIALIZABLE,
            IS_GENERATED_INSTANCE_CLASS);
        initEDataType(triggerConditionObjectEDataType, TriggerCondition.class, "TriggerConditionObject",
            IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(verticalAlignmentObjectEDataType, VerticalAlignment.class, "VerticalAlignmentObject",
            IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
    }

    /**
     * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData </b>. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void createExtendedMetaDataAnnotations()
    {
        String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
        addAnnotation(actionTypeEEnum, source, new String[]
        {
            "name", "ActionType"
        });
        addAnnotation(actionTypeObjectEDataType, source, new String[]
        {
            "name", "ActionType:Object", "baseType", "ActionType"
        });
        addAnnotation(actionValueEClass, source, new String[]
        {
            "name", "ActionValue", "kind", "empty"
        });
        addAnnotation(anchorEEnum, source, new String[]
        {
            "name", "Anchor"
        });
        addAnnotation(anchorObjectEDataType, source, new String[]
        {
            "name", "Anchor:Object", "baseType", "Anchor"
        });
        addAnnotation(axisOriginEClass, source, new String[]
        {
            "name", "AxisOrigin", "kind", "elementOnly"
        });
        addAnnotation(getAxisOrigin_Type(), source, new String[]
        {
            "kind", "element", "name", "Type"
        });
        addAnnotation(getAxisOrigin_Value(), source, new String[]
        {
            "kind", "element", "name", "Value"
        });
        addAnnotation(axisTypeEEnum, source, new String[]
        {
            "name", "AxisType"
        });
        addAnnotation(axisTypeObjectEDataType, source, new String[]
        {
            "name", "AxisType:Object", "baseType", "AxisType"
        });
        addAnnotation(boundsEClass, source, new String[]
        {
            "name", "Bounds", "kind", "elementOnly"
        });
        addAnnotation(getBounds_Left(), source, new String[]
        {
            "kind", "element", "name", "Left"
        });
        addAnnotation(getBounds_Top(), source, new String[]
        {
            "kind", "element", "name", "Top"
        });
        addAnnotation(getBounds_Width(), source, new String[]
        {
            "kind", "element", "name", "Width"
        });
        addAnnotation(getBounds_Height(), source, new String[]
        {
            "kind", "element", "name", "Height"
        });
        addAnnotation(chartDimensionEEnum, source, new String[]
        {
            "name", "ChartDimension"
        });
        addAnnotation(chartDimensionObjectEDataType, source, new String[]
        {
            "name", "ChartDimension:Object", "baseType", "ChartDimension"
        });
        addAnnotation(chartTypeEEnum, source, new String[]
        {
            "name", "ChartType"
        });
        addAnnotation(chartTypeObjectEDataType, source, new String[]
        {
            "name", "ChartType:Object", "baseType", "ChartType"
        });
        addAnnotation(colorDefinitionEClass, source, new String[]
        {
            "name", "ColorDefinition", "kind", "elementOnly"
        });
        addAnnotation(getColorDefinition_Transparency(), source, new String[]
        {
            "kind", "element", "name", "Transparency"
        });
        addAnnotation(getColorDefinition_Red(), source, new String[]
        {
            "kind", "element", "name", "Red"
        });
        addAnnotation(getColorDefinition_Green(), source, new String[]
        {
            "kind", "element", "name", "Green"
        });
        addAnnotation(getColorDefinition_Blue(), source, new String[]
        {
            "kind", "element", "name", "Blue"
        });
        addAnnotation(dataPointEClass, source, new String[]
        {
            "name", "DataPoint", "kind", "elementOnly"
        });
        addAnnotation(getDataPoint_Components(), source, new String[]
        {
            "kind", "element", "name", "Components"
        });
        addAnnotation(getDataPoint_Prefix(), source, new String[]
        {
            "kind", "element", "name", "Prefix"
        });
        addAnnotation(getDataPoint_Suffix(), source, new String[]
        {
            "kind", "element", "name", "Suffix"
        });
        addAnnotation(getDataPoint_Separator(), source, new String[]
        {
            "kind", "element", "name", "Separator"
        });
        addAnnotation(dataPointComponentEClass, source, new String[]
        {
            "name", "DataPointComponent", "kind", "elementOnly"
        });
        addAnnotation(getDataPointComponent_Type(), source, new String[]
        {
            "kind", "element", "name", "Type"
        });
        addAnnotation(getDataPointComponent_FormatSpecifier(), source, new String[]
        {
            "kind", "element", "name", "FormatSpecifier"
        });
        addAnnotation(dataPointComponentTypeEEnum, source, new String[]
        {
            "name", "DataPointComponentType"
        });
        addAnnotation(dataPointComponentTypeObjectEDataType, source, new String[]
        {
            "name", "DataPointComponentType:Object", "baseType", "DataPointComponentType"
        });
        addAnnotation(dateFormatDetailEEnum, source, new String[]
        {
            "name", "DateFormatDetail"
        });
        addAnnotation(dateFormatDetailObjectEDataType, source, new String[]
        {
            "name", "DateFormatDetail:Object", "baseType", "DateFormatDetail"
        });
        addAnnotation(dateFormatSpecifierEClass, source, new String[]
        {
            "name", "DateFormatSpecifier", "kind", "elementOnly"
        });
        addAnnotation(getDateFormatSpecifier_Type(), source, new String[]
        {
            "kind", "element", "name", "Type"
        });
        addAnnotation(getDateFormatSpecifier_Detail(), source, new String[]
        {
            "kind", "element", "name", "Detail"
        });
        addAnnotation(dateFormatTypeEEnum, source, new String[]
        {
            "name", "DateFormatType"
        });
        addAnnotation(dateFormatTypeObjectEDataType, source, new String[]
        {
            "name", "DateFormatType:Object", "baseType", "DateFormatType"
        });
        addAnnotation(directionEEnum, source, new String[]
        {
            "name", "Direction"
        });
        addAnnotation(directionObjectEDataType, source, new String[]
        {
            "name", "Direction:Object", "baseType", "Direction"
        });
        addAnnotation(extendedPropertyEClass, source, new String[]
        {
            "name", "ExtendedProperty", "kind", "elementOnly"
        });
        addAnnotation(getExtendedProperty_Name(), source, new String[]
        {
            "kind", "element", "name", "Name"
        });
        addAnnotation(getExtendedProperty_Value(), source, new String[]
        {
            "kind", "element", "name", "Value"
        });
        addAnnotation(fillEClass, source, new String[]
        {
            "name", "Fill", "kind", "elementOnly"
        });
        addAnnotation(getFill_Type(), source, new String[]
        {
            "kind", "element", "name", "Type"
        });
        addAnnotation(fontDefinitionEClass, source, new String[]
        {
            "name", "FontDefinition", "kind", "elementOnly"
        });
        addAnnotation(getFontDefinition_Name(), source, new String[]
        {
            "kind", "element", "name", "Name"
        });
        addAnnotation(getFontDefinition_Size(), source, new String[]
        {
            "kind", "element", "name", "Size"
        });
        addAnnotation(getFontDefinition_Bold(), source, new String[]
        {
            "kind", "element", "name", "Bold"
        });
        addAnnotation(getFontDefinition_Italic(), source, new String[]
        {
            "kind", "element", "name", "Italic"
        });
        addAnnotation(getFontDefinition_Strikethrough(), source, new String[]
        {
            "kind", "element", "name", "Strikethrough"
        });
        addAnnotation(getFontDefinition_Underline(), source, new String[]
        {
            "kind", "element", "name", "Underline"
        });
        addAnnotation(getFontDefinition_WordWrap(), source, new String[]
        {
            "kind", "element", "name", "WordWrap"
        });
        addAnnotation(getFontDefinition_Alignment(), source, new String[]
        {
            "kind", "element", "name", "Alignment"
        });
        addAnnotation(getFontDefinition_Rotation(), source, new String[]
        {
            "kind", "element", "name", "Rotation"
        });
        addAnnotation(formatSpecifierEClass, source, new String[]
        {
            "name", "FormatSpecifier", "kind", "empty"
        });
        addAnnotation(gradientEClass, source, new String[]
        {
            "name", "Gradient", "kind", "elementOnly"
        });
        addAnnotation(getGradient_StartColor(), source, new String[]
        {
            "kind", "element", "name", "StartColor"
        });
        addAnnotation(getGradient_EndColor(), source, new String[]
        {
            "kind", "element", "name", "EndColor"
        });
        addAnnotation(getGradient_Direction(), source, new String[]
        {
            "kind", "element", "name", "Direction"
        });
        addAnnotation(getGradient_Cyclic(), source, new String[]
        {
            "kind", "element", "name", "Cyclic"
        });
        addAnnotation(getGradient_Transparency(), source, new String[]
        {
            "kind", "element", "name", "Transparency"
        });
        addAnnotation(horizontalAlignmentEEnum, source, new String[]
        {
            "name", "HorizontalAlignment"
        });
        addAnnotation(horizontalAlignmentObjectEDataType, source, new String[]
        {
            "name", "HorizontalAlignment:Object", "baseType", "HorizontalAlignment"
        });
        addAnnotation(idEDataType, source, new String[]
        {
            "name", "ID", "baseType", "http://www.eclipse.org/emf/2003/XMLType#string", "pattern", "[A-Z]"
        });
        addAnnotation(imageEClass, source, new String[]
        {
            "name", "Image", "kind", "elementOnly"
        });
        addAnnotation(getImage_URL(), source, new String[]
        {
            "kind", "element", "name", "URL"
        });
        addAnnotation(insetsEClass, source, new String[]
        {
            "name", "Insets", "kind", "elementOnly"
        });
        addAnnotation(getInsets_Top(), source, new String[]
        {
            "kind", "element", "name", "Top"
        });
        addAnnotation(getInsets_Left(), source, new String[]
        {
            "kind", "element", "name", "Left"
        });
        addAnnotation(getInsets_Bottom(), source, new String[]
        {
            "kind", "element", "name", "Bottom"
        });
        addAnnotation(getInsets_Right(), source, new String[]
        {
            "kind", "element", "name", "Right"
        });
        addAnnotation(intersectionTypeEEnum, source, new String[]
        {
            "name", "IntersectionType"
        });
        addAnnotation(intersectionTypeObjectEDataType, source, new String[]
        {
            "name", "IntersectionType:Object", "baseType", "IntersectionType"
        });
        addAnnotation(javaDateFormatSpecifierEClass, source, new String[]
        {
            "name", "JavaDateFormatSpecifier", "kind", "elementOnly"
        });
        addAnnotation(getJavaDateFormatSpecifier_Pattern(), source, new String[]
        {
            "kind", "element", "name", "Pattern"
        });
        addAnnotation(javaNumberFormatSpecifierEClass, source, new String[]
        {
            "name", "JavaNumberFormatSpecifier", "kind", "elementOnly"
        });
        addAnnotation(getJavaNumberFormatSpecifier_Pattern(), source, new String[]
        {
            "kind", "element", "name", "Pattern"
        });
        addAnnotation(getJavaNumberFormatSpecifier_Multiplier(), source, new String[]
        {
            "kind", "element", "name", "Multiplier"
        });
        addAnnotation(leaderLineStyleEEnum, source, new String[]
        {
            "name", "LeaderLineStyle"
        });
        addAnnotation(leaderLineStyleObjectEDataType, source, new String[]
        {
            "name", "LeaderLineStyle:Object", "baseType", "LeaderLineStyle"
        });
        addAnnotation(legendItemTypeEEnum, source, new String[]
        {
            "name", "LegendItemType"
        });
        addAnnotation(legendItemTypeObjectEDataType, source, new String[]
        {
            "name", "LegendItemType:Object", "baseType", "LegendItemType"
        });
        addAnnotation(lineAttributesEClass, source, new String[]
        {
            "name", "LineAttributes", "kind", "elementOnly"
        });
        addAnnotation(getLineAttributes_Style(), source, new String[]
        {
            "kind", "element", "name", "Style"
        });
        addAnnotation(getLineAttributes_Thickness(), source, new String[]
        {
            "kind", "element", "name", "Thickness"
        });
        addAnnotation(getLineAttributes_Color(), source, new String[]
        {
            "kind", "element", "name", "Color"
        });
        addAnnotation(getLineAttributes_Visible(), source, new String[]
        {
            "kind", "element", "name", "Visible"
        });
        addAnnotation(lineStyleEEnum, source, new String[]
        {
            "name", "LineStyle"
        });
        addAnnotation(lineStyleObjectEDataType, source, new String[]
        {
            "name", "LineStyle:Object", "baseType", "LineStyle"
        });
        addAnnotation(locationEClass, source, new String[]
        {
            "name", "Location", "kind", "elementOnly"
        });
        addAnnotation(getLocation_X(), source, new String[]
        {
            "kind", "element", "name", "x"
        });
        addAnnotation(getLocation_Y(), source, new String[]
        {
            "kind", "element", "name", "y"
        });
        addAnnotation(markerEClass, source, new String[]
        {
            "name", "Marker", "kind", "elementOnly"
        });
        addAnnotation(getMarker_Type(), source, new String[]
        {
            "kind", "element", "name", "Type"
        });
        addAnnotation(getMarker_Size(), source, new String[]
        {
            "kind", "element", "name", "Size"
        });
        addAnnotation(getMarker_Visible(), source, new String[]
        {
            "kind", "element", "name", "Visible"
        });
        addAnnotation(markerTypeEEnum, source, new String[]
        {
            "name", "MarkerType"
        });
        addAnnotation(markerTypeObjectEDataType, source, new String[]
        {
            "name", "MarkerType:Object", "baseType", "MarkerType"
        });
        addAnnotation(numberFormatSpecifierEClass, source, new String[]
        {
            "name", "NumberFormatSpecifier", "kind", "elementOnly"
        });
        addAnnotation(getNumberFormatSpecifier_Prefix(), source, new String[]
        {
            "kind", "element", "name", "Prefix"
        });
        addAnnotation(getNumberFormatSpecifier_Suffix(), source, new String[]
        {
            "kind", "element", "name", "Suffix"
        });
        addAnnotation(getNumberFormatSpecifier_Multiplier(), source, new String[]
        {
            "kind", "element", "name", "Multiplier"
        });
        addAnnotation(getNumberFormatSpecifier_FractionDigits(), source, new String[]
        {
            "kind", "element", "name", "FractionDigits"
        });
        addAnnotation(orientationEEnum, source, new String[]
        {
            "name", "Orientation"
        });
        addAnnotation(orientationObjectEDataType, source, new String[]
        {
            "name", "Orientation:Object", "baseType", "Orientation"
        });
        addAnnotation(paletteEClass, source, new String[]
        {
            "name", "Palette", "kind", "elementOnly"
        });
        addAnnotation(getPalette_Name(), source, new String[]
        {
            "kind", "element", "name", "Name"
        });
        addAnnotation(getPalette_Entries(), source, new String[]
        {
            "kind", "element", "name", "Entries"
        });
        addAnnotation(percentageEDataType, source, new String[]
        {
            "name", "Percentage", "baseType", "http://www.eclipse.org/emf/2003/XMLType#double", "minInclusive", "0.0",
            "maxInclusive", "100.0"
        });
        addAnnotation(percentageObjectEDataType, source, new String[]
        {
            "name", "Percentage:Object", "baseType", "Percentage"
        });
        addAnnotation(positionEEnum, source, new String[]
        {
            "name", "Position"
        });
        addAnnotation(positionObjectEDataType, source, new String[]
        {
            "name", "Position:Object", "baseType", "Position"
        });
        addAnnotation(rgbValueEDataType, source, new String[]
        {
            "name", "RGBValue", "baseType", "http://www.eclipse.org/emf/2003/XMLType#int", "minInclusive", "0",
            "maxInclusive", "255"
        });
        addAnnotation(rgbValueObjectEDataType, source, new String[]
        {
            "name", "RGBValue:Object", "baseType", "RGBValue"
        });
        addAnnotation(riserTypeEEnum, source, new String[]
        {
            "name", "RiserType"
        });
        addAnnotation(riserTypeObjectEDataType, source, new String[]
        {
            "name", "RiserType:Object", "baseType", "RiserType"
        });
        addAnnotation(ruleTypeEEnum, source, new String[]
        {
            "name", "RuleType"
        });
        addAnnotation(ruleTypeObjectEDataType, source, new String[]
        {
            "name", "RuleType:Object", "baseType", "RuleType"
        });
        addAnnotation(scaleUnitTypeEEnum, source, new String[]
        {
            "name", "ScaleUnitType"
        });
        addAnnotation(scaleUnitTypeObjectEDataType, source, new String[]
        {
            "name", "ScaleUnitType:Object", "baseType", "ScaleUnitType"
        });
        addAnnotation(scriptValueEClass, source, new String[]
        {
            "name", "ScriptValue", "kind", "elementOnly"
        });
        addAnnotation(getScriptValue_Script(), source, new String[]
        {
            "kind", "element", "name", "Script"
        });
        addAnnotation(seriesHintEClass, source, new String[]
        {
            "name", "SeriesHint", "kind", "elementOnly"
        });
        addAnnotation(getSeriesHint_SeriesClass(), source, new String[]
        {
            "kind", "element", "name", "SeriesClass"
        });
        addAnnotation(getSeriesHint_Modifier(), source, new String[]
        {
            "kind", "element", "name", "Modifier"
        });
        addAnnotation(seriesValueEClass, source, new String[]
        {
            "name", "SeriesValue", "kind", "elementOnly"
        });
        addAnnotation(getSeriesValue_Name(), source, new String[]
        {
            "kind", "element", "name", "Name"
        });
        addAnnotation(sizeEClass, source, new String[]
        {
            "name", "Size", "kind", "elementOnly"
        });
        addAnnotation(getSize_Height(), source, new String[]
        {
            "kind", "element", "name", "Height"
        });
        addAnnotation(getSize_Width(), source, new String[]
        {
            "kind", "element", "name", "Width"
        });
        addAnnotation(sortOptionEEnum, source, new String[]
        {
            "name", "SortOption"
        });
        addAnnotation(sortOptionObjectEDataType, source, new String[]
        {
            "name", "SortOption:Object", "baseType", "SortOption"
        });
        addAnnotation(stretchEEnum, source, new String[]
        {
            "name", "Stretch"
        });
        addAnnotation(stretchObjectEDataType, source, new String[]
        {
            "name", "Stretch:Object", "baseType", "Stretch"
        });
        addAnnotation(styledComponentEEnum, source, new String[]
        {
            "name", "StyledComponent"
        });
        addAnnotation(styledComponentObjectEDataType, source, new String[]
        {
            "name", "StyledComponent:Object", "baseType", "StyledComponent"
        });
        addAnnotation(styleMapEClass, source, new String[]
        {
            "name", "StyleMap", "kind", "elementOnly"
        });
        addAnnotation(getStyleMap_ComponentName(), source, new String[]
        {
            "kind", "element", "name", "ComponentName"
        });
        addAnnotation(getStyleMap_Style(), source, new String[]
        {
            "kind", "element", "name", "Style"
        });
        addAnnotation(textEClass, source, new String[]
        {
            "name", "Text", "kind", "elementOnly"
        });
        addAnnotation(getText_Value(), source, new String[]
        {
            "kind", "element", "name", "Value"
        });
        addAnnotation(getText_Font(), source, new String[]
        {
            "kind", "element", "name", "Font"
        });
        addAnnotation(getText_Color(), source, new String[]
        {
            "kind", "element", "name", "Color"
        });
        addAnnotation(textAlignmentEClass, source, new String[]
        {
            "name", "TextAlignment", "kind", "elementOnly"
        });
        addAnnotation(getTextAlignment_HorizontalAlignment(), source, new String[]
        {
            "kind", "element", "name", "horizontalAlignment"
        });
        addAnnotation(getTextAlignment_VerticalAlignment(), source, new String[]
        {
            "kind", "element", "name", "verticalAlignment"
        });
        addAnnotation(tickStyleEEnum, source, new String[]
        {
            "name", "TickStyle"
        });
        addAnnotation(tickStyleObjectEDataType, source, new String[]
        {
            "name", "TickStyle:Object", "baseType", "TickStyle"
        });
        addAnnotation(tooltipValueEClass, source, new String[]
        {
            "name", "TooltipValue", "kind", "elementOnly"
        });
        addAnnotation(getTooltipValue_Text(), source, new String[]
        {
            "kind", "element", "name", "Text"
        });
        addAnnotation(getTooltipValue_Delay(), source, new String[]
        {
            "kind", "element", "name", "Delay"
        });
        addAnnotation(triggerConditionEEnum, source, new String[]
        {
            "name", "TriggerCondition"
        });
        addAnnotation(triggerConditionObjectEDataType, source, new String[]
        {
            "name", "TriggerCondition:Object", "baseType", "TriggerCondition"
        });
        addAnnotation(urlValueEClass, source, new String[]
        {
            "name", "URLValue", "kind", "elementOnly"
        });
        addAnnotation(getURLValue_BaseUrl(), source, new String[]
        {
            "kind", "element", "name", "BaseUrl"
        });
        addAnnotation(getURLValue_Target(), source, new String[]
        {
            "kind", "element", "name", "Target"
        });
        addAnnotation(getURLValue_BaseParameterName(), source, new String[]
        {
            "kind", "element", "name", "BaseParameterName"
        });
        addAnnotation(getURLValue_ValueParameterName(), source, new String[]
        {
            "kind", "element", "name", "ValueParameterName"
        });
        addAnnotation(getURLValue_SeriesParameterName(), source, new String[]
        {
            "kind", "element", "name", "SeriesParameterName"
        });
        addAnnotation(verticalAlignmentEEnum, source, new String[]
        {
            "name", "VerticalAlignment"
        });
        addAnnotation(verticalAlignmentObjectEDataType, source, new String[]
        {
            "name", "VerticalAlignment:Object", "baseType", "VerticalAlignment"
        });
    }

} //AttributePackageImpl
