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

package org.eclipse.birt.chart.model.layout.impl;

import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.impl.ComponentPackageImpl;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.impl.DataPackageImpl;
import org.eclipse.birt.chart.model.impl.ModelPackageImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.type.impl.TypePackageImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.ecore.xml.type.impl.XMLTypePackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package </b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class LayoutPackageImpl extends EPackageImpl implements LayoutPackage
{

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass blockEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass clientAreaEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass labelBlockEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass legendEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass plotEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass titleBlockEClass = null;

    /**
     * Creates an instance of the model <b>Package </b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry}by the package package URI value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also
     * performs initialization of the package, or returns the registered package, if one already exists. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.eclipse.birt.chart.model.layout.LayoutPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private LayoutPackageImpl()
    {
        super(eNS_URI, LayoutFactory.eINSTANCE);
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
    public static LayoutPackage init()
    {
        if (isInited)
            return (LayoutPackage) EPackage.Registry.INSTANCE.getEPackage(LayoutPackage.eNS_URI);

        // Obtain or create and register package
        LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof LayoutPackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(eNS_URI)
            : new LayoutPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        XMLTypePackageImpl.init();

        // Obtain or create and register interdependencies
        ModelPackageImpl theModelPackage = (ModelPackageImpl) (EPackage.Registry.INSTANCE
            .getEPackage(ModelPackage.eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(ModelPackage.eNS_URI) : ModelPackageImpl.eINSTANCE);
        TypePackageImpl theTypePackage = (TypePackageImpl) (EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI) instanceof TypePackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(TypePackage.eNS_URI)
            : TypePackageImpl.eINSTANCE);
        ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) (EPackage.Registry.INSTANCE
            .getEPackage(ComponentPackage.eNS_URI) instanceof ComponentPackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(ComponentPackage.eNS_URI) : ComponentPackageImpl.eINSTANCE);
        AttributePackageImpl theAttributePackage = (AttributePackageImpl) (EPackage.Registry.INSTANCE
            .getEPackage(AttributePackage.eNS_URI) instanceof AttributePackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(AttributePackage.eNS_URI) : AttributePackageImpl.eINSTANCE);
        DataPackageImpl theDataPackage = (DataPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(DataPackage.eNS_URI) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE
            .getEPackage(DataPackage.eNS_URI)
            : DataPackageImpl.eINSTANCE);

        // Create package meta-data objects
        theLayoutPackage.createPackageContents();
        theModelPackage.createPackageContents();
        theTypePackage.createPackageContents();
        theComponentPackage.createPackageContents();
        theAttributePackage.createPackageContents();
        theDataPackage.createPackageContents();

        // Initialize created meta-data
        theLayoutPackage.initializePackageContents();
        theModelPackage.initializePackageContents();
        theTypePackage.initializePackageContents();
        theComponentPackage.initializePackageContents();
        theAttributePackage.initializePackageContents();
        theDataPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theLayoutPackage.freeze();

        return theLayoutPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getBlock()
    {
        return blockEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getBlock_Children()
    {
        return (EReference) blockEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getBlock_Bounds()
    {
        return (EReference) blockEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBlock_Anchor()
    {
        return (EAttribute) blockEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBlock_Stretch()
    {
        return (EAttribute) blockEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getBlock_Insets()
    {
        return (EReference) blockEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBlock_Row()
    {
        return (EAttribute) blockEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBlock_Column()
    {
        return (EAttribute) blockEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBlock_Rowspan()
    {
        return (EAttribute) blockEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBlock_Columnspan()
    {
        return (EAttribute) blockEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getBlock_MinSize()
    {
        return (EReference) blockEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getBlock_Outline()
    {
        return (EReference) blockEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getBlock_Background()
    {
        return (EReference) blockEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getBlock_Visible()
    {
        return (EAttribute) blockEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getBlock_Triggers()
    {
        return (EReference) blockEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getClientArea()
    {
        return clientAreaEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getClientArea_Background()
    {
        return (EReference) clientAreaEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getClientArea_Outline()
    {
        return (EReference) clientAreaEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getClientArea_ShadowColor()
    {
        return (EReference) clientAreaEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getClientArea_Insets()
    {
        return (EReference) clientAreaEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getLabelBlock()
    {
        return labelBlockEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getLabelBlock_Label()
    {
        return (EReference) labelBlockEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getLegend()
    {
        return legendEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLegend_HorizontalSpacing()
    {
        return (EAttribute) legendEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLegend_VerticalSpacing()
    {
        return (EAttribute) legendEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getLegend_ClientArea()
    {
        return (EReference) legendEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getLegend_Text()
    {
        return (EReference) legendEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLegend_Orientation()
    {
        return (EAttribute) legendEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLegend_Direction()
    {
        return (EAttribute) legendEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getLegend_Separator()
    {
        return (EReference) legendEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLegend_Position()
    {
        return (EAttribute) legendEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getLegend_ItemType()
    {
        return (EAttribute) legendEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getPlot()
    {
        return plotEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getPlot_HorizontalSpacing()
    {
        return (EAttribute) plotEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EAttribute getPlot_VerticalSpacing()
    {
        return (EAttribute) plotEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EReference getPlot_ClientArea()
    {
        return (EReference) plotEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EClass getTitleBlock()
    {
        return titleBlockEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public LayoutFactory getLayoutFactory()
    {
        return (LayoutFactory) getEFactoryInstance();
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
        blockEClass = createEClass(BLOCK);
        createEReference(blockEClass, BLOCK__CHILDREN);
        createEReference(blockEClass, BLOCK__BOUNDS);
        createEAttribute(blockEClass, BLOCK__ANCHOR);
        createEAttribute(blockEClass, BLOCK__STRETCH);
        createEReference(blockEClass, BLOCK__INSETS);
        createEAttribute(blockEClass, BLOCK__ROW);
        createEAttribute(blockEClass, BLOCK__COLUMN);
        createEAttribute(blockEClass, BLOCK__ROWSPAN);
        createEAttribute(blockEClass, BLOCK__COLUMNSPAN);
        createEReference(blockEClass, BLOCK__MIN_SIZE);
        createEReference(blockEClass, BLOCK__OUTLINE);
        createEReference(blockEClass, BLOCK__BACKGROUND);
        createEAttribute(blockEClass, BLOCK__VISIBLE);
        createEReference(blockEClass, BLOCK__TRIGGERS);

        clientAreaEClass = createEClass(CLIENT_AREA);
        createEReference(clientAreaEClass, CLIENT_AREA__BACKGROUND);
        createEReference(clientAreaEClass, CLIENT_AREA__OUTLINE);
        createEReference(clientAreaEClass, CLIENT_AREA__SHADOW_COLOR);
        createEReference(clientAreaEClass, CLIENT_AREA__INSETS);

        labelBlockEClass = createEClass(LABEL_BLOCK);
        createEReference(labelBlockEClass, LABEL_BLOCK__LABEL);

        legendEClass = createEClass(LEGEND);
        createEAttribute(legendEClass, LEGEND__HORIZONTAL_SPACING);
        createEAttribute(legendEClass, LEGEND__VERTICAL_SPACING);
        createEReference(legendEClass, LEGEND__CLIENT_AREA);
        createEReference(legendEClass, LEGEND__TEXT);
        createEAttribute(legendEClass, LEGEND__ORIENTATION);
        createEAttribute(legendEClass, LEGEND__DIRECTION);
        createEReference(legendEClass, LEGEND__SEPARATOR);
        createEAttribute(legendEClass, LEGEND__POSITION);
        createEAttribute(legendEClass, LEGEND__ITEM_TYPE);

        plotEClass = createEClass(PLOT);
        createEAttribute(plotEClass, PLOT__HORIZONTAL_SPACING);
        createEAttribute(plotEClass, PLOT__VERTICAL_SPACING);
        createEReference(plotEClass, PLOT__CLIENT_AREA);

        titleBlockEClass = createEClass(TITLE_BLOCK);
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
        AttributePackageImpl theAttributePackage = (AttributePackageImpl) EPackage.Registry.INSTANCE
            .getEPackage(AttributePackage.eNS_URI);
        XMLTypePackageImpl theXMLTypePackage = (XMLTypePackageImpl) EPackage.Registry.INSTANCE
            .getEPackage(XMLTypePackage.eNS_URI);
        DataPackageImpl theDataPackage = (DataPackageImpl) EPackage.Registry.INSTANCE.getEPackage(DataPackage.eNS_URI);
        ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) EPackage.Registry.INSTANCE
            .getEPackage(ComponentPackage.eNS_URI);

        // Add supertypes to classes
        labelBlockEClass.getESuperTypes().add(this.getBlock());
        legendEClass.getESuperTypes().add(this.getBlock());
        plotEClass.getESuperTypes().add(this.getBlock());
        titleBlockEClass.getESuperTypes().add(this.getLabelBlock());

        // Initialize classes and features; add operations and parameters
        initEClass(blockEClass, Block.class, "Block", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getBlock_Children(), this.getBlock(), null, "children", null, 1, -1, Block.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);
        initEReference(getBlock_Bounds(), theAttributePackage.getBounds(), null, "bounds", null, 0, 1, Block.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBlock_Anchor(), theAttributePackage.getAnchor(), "anchor", "North", 0, 1, Block.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBlock_Stretch(), theAttributePackage.getStretch(), "stretch", "Horizontal", 0, 1,
            Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);
        initEReference(getBlock_Insets(), theAttributePackage.getInsets(), null, "insets", null, 0, 1, Block.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBlock_Row(), theXMLTypePackage.getInt(), "row", null, 1, 1, Block.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBlock_Column(), theXMLTypePackage.getInt(), "column", null, 1, 1, Block.class, !IS_TRANSIENT,
            !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBlock_Rowspan(), theXMLTypePackage.getInt(), "rowspan", null, 1, 1, Block.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBlock_Columnspan(), theXMLTypePackage.getInt(), "columnspan", null, 1, 1, Block.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBlock_MinSize(), theAttributePackage.getSize(), null, "minSize", null, 1, 1, Block.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEReference(getBlock_Outline(), theAttributePackage.getLineAttributes(), null, "outline", null, 1, 1,
            Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
            IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBlock_Background(), theAttributePackage.getFill(), null, "background", null, 0, 1,
            Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
            IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBlock_Visible(), theXMLTypePackage.getBoolean(), "visible", null, 1, 1, Block.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBlock_Triggers(), theDataPackage.getTrigger(), null, "triggers", null, 0, -1, Block.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(clientAreaEClass, ClientArea.class, "ClientArea", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEReference(getClientArea_Background(), theAttributePackage.getFill(), null, "background", null, 1, 1,
            ClientArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
            !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getClientArea_Outline(), theAttributePackage.getLineAttributes(), null, "outline", null, 1, 1,
            ClientArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
            !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getClientArea_ShadowColor(), theAttributePackage.getColorDefinition(), null, "shadowColor",
            null, 1, 1, ClientArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
            !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getClientArea_Insets(), theAttributePackage.getInsets(), null, "insets", null, 1, 1,
            ClientArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
            !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(labelBlockEClass, LabelBlock.class, "LabelBlock", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLabelBlock_Label(), theComponentPackage.getLabel(), null, "label", null, 0, 1,
            LabelBlock.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
            !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(legendEClass, Legend.class, "Legend", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLegend_HorizontalSpacing(), theXMLTypePackage.getInt(), "horizontalSpacing", null, 1, 1,
            Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);
        initEAttribute(getLegend_VerticalSpacing(), theXMLTypePackage.getInt(), "verticalSpacing", null, 1, 1,
            Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);
        initEReference(getLegend_ClientArea(), this.getClientArea(), null, "clientArea", null, 1, 1, Legend.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEReference(getLegend_Text(), theAttributePackage.getText(), null, "text", null, 1, 1, Legend.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLegend_Orientation(), theAttributePackage.getOrientation(), "orientation", "Horizontal", 1,
            1, Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLegend_Direction(), theAttributePackage.getDirection(), "direction", "Left_Right", 1, 1,
            Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);
        initEReference(getLegend_Separator(), theAttributePackage.getLineAttributes(), null, "separator", null, 1, 1,
            Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
            !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLegend_Position(), theAttributePackage.getPosition(), "position", "Above", 1, 1,
            Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);
        initEAttribute(getLegend_ItemType(), theAttributePackage.getLegendItemType(), "itemType", "Series", 1, 1,
            Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);

        initEClass(plotEClass, Plot.class, "Plot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPlot_HorizontalSpacing(), theXMLTypePackage.getInt(), "horizontalSpacing", null, 1, 1,
            Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);
        initEAttribute(getPlot_VerticalSpacing(), theXMLTypePackage.getInt(), "verticalSpacing", null, 1, 1,
            Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED,
            IS_ORDERED);
        initEReference(getPlot_ClientArea(), this.getClientArea(), null, "clientArea", null, 1, 1, Plot.class,
            !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
            !IS_DERIVED, IS_ORDERED);

        initEClass(titleBlockEClass, TitleBlock.class, "TitleBlock", !IS_ABSTRACT, !IS_INTERFACE,
            IS_GENERATED_INSTANCE_CLASS);

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
        addAnnotation(blockEClass, source, new String[]
        {
            "name", "Block", "kind", "elementOnly"
        });
        addAnnotation(getBlock_Children(), source, new String[]
        {
            "kind", "element", "name", "Children"
        });
        addAnnotation(getBlock_Bounds(), source, new String[]
        {
            "kind", "element", "name", "Bounds"
        });
        addAnnotation(getBlock_Anchor(), source, new String[]
        {
            "kind", "element", "name", "Anchor"
        });
        addAnnotation(getBlock_Stretch(), source, new String[]
        {
            "kind", "element", "name", "Stretch"
        });
        addAnnotation(getBlock_Insets(), source, new String[]
        {
            "kind", "element", "name", "Insets"
        });
        addAnnotation(getBlock_Row(), source, new String[]
        {
            "kind", "element", "name", "Row"
        });
        addAnnotation(getBlock_Column(), source, new String[]
        {
            "kind", "element", "name", "Column"
        });
        addAnnotation(getBlock_Rowspan(), source, new String[]
        {
            "kind", "element", "name", "Rowspan"
        });
        addAnnotation(getBlock_Columnspan(), source, new String[]
        {
            "kind", "element", "name", "Columnspan"
        });
        addAnnotation(getBlock_MinSize(), source, new String[]
        {
            "kind", "element", "name", "MinSize"
        });
        addAnnotation(getBlock_Outline(), source, new String[]
        {
            "kind", "element", "name", "Outline"
        });
        addAnnotation(getBlock_Background(), source, new String[]
        {
            "kind", "element", "name", "Background"
        });
        addAnnotation(getBlock_Visible(), source, new String[]
        {
            "kind", "element", "name", "Visible"
        });
        addAnnotation(getBlock_Triggers(), source, new String[]
        {
            "kind", "element", "name", "Triggers"
        });
        addAnnotation(clientAreaEClass, source, new String[]
        {
            "name", "ClientArea", "kind", "elementOnly"
        });
        addAnnotation(getClientArea_Background(), source, new String[]
        {
            "kind", "element", "name", "Background"
        });
        addAnnotation(getClientArea_Outline(), source, new String[]
        {
            "kind", "element", "name", "Outline"
        });
        addAnnotation(getClientArea_ShadowColor(), source, new String[]
        {
            "kind", "element", "name", "ShadowColor"
        });
        addAnnotation(getClientArea_Insets(), source, new String[]
        {
            "kind", "element", "name", "Insets"
        });
        addAnnotation(labelBlockEClass, source, new String[]
        {
            "name", "LabelBlock", "kind", "elementOnly"
        });
        addAnnotation(getLabelBlock_Label(), source, new String[]
        {
            "kind", "element", "name", "Label"
        });
        addAnnotation(legendEClass, source, new String[]
        {
            "name", "Legend", "kind", "elementOnly"
        });
        addAnnotation(getLegend_HorizontalSpacing(), source, new String[]
        {
            "kind", "element", "name", "HorizontalSpacing"
        });
        addAnnotation(getLegend_VerticalSpacing(), source, new String[]
        {
            "kind", "element", "name", "VerticalSpacing"
        });
        addAnnotation(getLegend_ClientArea(), source, new String[]
        {
            "kind", "element", "name", "ClientArea"
        });
        addAnnotation(getLegend_Text(), source, new String[]
        {
            "kind", "element", "name", "Text"
        });
        addAnnotation(getLegend_Orientation(), source, new String[]
        {
            "kind", "element", "name", "Orientation"
        });
        addAnnotation(getLegend_Direction(), source, new String[]
        {
            "kind", "element", "name", "Direction"
        });
        addAnnotation(getLegend_Separator(), source, new String[]
        {
            "kind", "element", "name", "Separator"
        });
        addAnnotation(getLegend_Position(), source, new String[]
        {
            "kind", "element", "name", "Position"
        });
        addAnnotation(getLegend_ItemType(), source, new String[]
        {
            "kind", "element", "name", "ItemType"
        });
        addAnnotation(plotEClass, source, new String[]
        {
            "name", "Plot", "kind", "elementOnly"
        });
        addAnnotation(getPlot_HorizontalSpacing(), source, new String[]
        {
            "kind", "element", "name", "HorizontalSpacing"
        });
        addAnnotation(getPlot_VerticalSpacing(), source, new String[]
        {
            "kind", "element", "name", "VerticalSpacing"
        });
        addAnnotation(getPlot_ClientArea(), source, new String[]
        {
            "kind", "element", "name", "ClientArea"
        });
        addAnnotation(titleBlockEClass, source, new String[]
        {
            "name", "TitleBlock", "kind", "elementOnly"
        });
    }

} //LayoutPackageImpl
