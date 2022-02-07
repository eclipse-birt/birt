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
import org.eclipse.birt.chart.model.layout.util.LayoutValidator;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.type.impl.TypePackageImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package </b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class LayoutPackageImpl extends EPackageImpl implements LayoutPackage {

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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EDataType ellipsisTypeEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EDataType ellipsisTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EDataType titlePercentTypeEDataType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EDataType titlePercentTypeObjectEDataType = null;

	/**
	 * Creates an instance of the model <b>Package </b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry}by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method
	 * {@link #init init()}, which also performs initialization of the package, or
	 * returns the registered package, if one already exists. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private LayoutPackageImpl() {
		super(eNS_URI, LayoutFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and
	 * for any others upon which it depends.
	 * 
	 * <p>
	 * This method is used to initialize {@link LayoutPackage#eINSTANCE} when that
	 * field is accessed. Clients should not invoke it directly. Instead, they
	 * should simply access that field to obtain the package. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static LayoutPackage init() {
		if (isInited)
			return (LayoutPackage) EPackage.Registry.INSTANCE.getEPackage(LayoutPackage.eNS_URI);

		// Obtain or create and register package
		LayoutPackageImpl theLayoutPackage = (LayoutPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof LayoutPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
						: new LayoutPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		AttributePackageImpl theAttributePackage = (AttributePackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(AttributePackage.eNS_URI) instanceof AttributePackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(AttributePackage.eNS_URI)
						: AttributePackage.eINSTANCE);
		ComponentPackageImpl theComponentPackage = (ComponentPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(ComponentPackage.eNS_URI) instanceof ComponentPackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(ComponentPackage.eNS_URI)
						: ComponentPackage.eINSTANCE);
		DataPackageImpl theDataPackage = (DataPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(DataPackage.eNS_URI) instanceof DataPackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(DataPackage.eNS_URI)
						: DataPackage.eINSTANCE);
		TypePackageImpl theTypePackage = (TypePackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(TypePackage.eNS_URI) instanceof TypePackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI)
						: TypePackage.eINSTANCE);
		ModelPackageImpl theModelPackage = (ModelPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(ModelPackage.eNS_URI) instanceof ModelPackageImpl
						? EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI)
						: ModelPackage.eINSTANCE);

		// Create package meta-data objects
		theLayoutPackage.createPackageContents();
		theAttributePackage.createPackageContents();
		theComponentPackage.createPackageContents();
		theDataPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theModelPackage.createPackageContents();

		// Initialize created meta-data
		theLayoutPackage.initializePackageContents();
		theAttributePackage.initializePackageContents();
		theComponentPackage.initializePackageContents();
		theDataPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theModelPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put(theLayoutPackage, new EValidator.Descriptor() {

			public EValidator getEValidator() {
				return LayoutValidator.INSTANCE;
			}
		});

		// Mark meta-data to indicate it can't be changed
		theLayoutPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(LayoutPackage.eNS_URI, theLayoutPackage);
		return theLayoutPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getBlock() {
		return blockEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlock_Children() {
		return (EReference) blockEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlock_Bounds() {
		return (EReference) blockEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBlock_Anchor() {
		return (EAttribute) blockEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBlock_Stretch() {
		return (EAttribute) blockEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlock_Insets() {
		return (EReference) blockEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBlock_Row() {
		return (EAttribute) blockEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBlock_Column() {
		return (EAttribute) blockEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBlock_Rowspan() {
		return (EAttribute) blockEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBlock_Columnspan() {
		return (EAttribute) blockEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlock_MinSize() {
		return (EReference) blockEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlock_Outline() {
		return (EReference) blockEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlock_Background() {
		return (EReference) blockEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBlock_Visible() {
		return (EAttribute) blockEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlock_Triggers() {
		return (EReference) blockEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBlock_WidthHint() {
		return (EAttribute) blockEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBlock_HeightHint() {
		return (EAttribute) blockEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBlock_Cursor() {
		return (EReference) blockEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getClientArea() {
		return clientAreaEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getClientArea_Background() {
		return (EReference) clientAreaEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getClientArea_Outline() {
		return (EReference) clientAreaEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getClientArea_ShadowColor() {
		return (EReference) clientAreaEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getClientArea_Insets() {
		return (EReference) clientAreaEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getClientArea_Visible() {
		return (EAttribute) clientAreaEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLabelBlock() {
		return labelBlockEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLabelBlock_Label() {
		return (EReference) labelBlockEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLegend() {
		return legendEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_HorizontalSpacing() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_VerticalSpacing() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLegend_ClientArea() {
		return (EReference) legendEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLegend_Text() {
		return (EReference) legendEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_Orientation() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_Direction() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLegend_Separator() {
		return (EReference) legendEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_Position() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_ItemType() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLegend_Title() {
		return (EReference) legendEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_TitlePosition() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_ShowValue() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_ShowPercent() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_ShowTotal() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_WrappingSize() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_MaxPercent() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_TitlePercent() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLegend_Ellipsis() {
		return (EAttribute) legendEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getLegend_FormatSpecifier() {
		return (EReference) legendEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getPlot() {
		return plotEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPlot_HorizontalSpacing() {
		return (EAttribute) plotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPlot_VerticalSpacing() {
		return (EAttribute) plotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPlot_ClientArea() {
		return (EReference) plotEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getTitleBlock() {
		return titleBlockEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTitleBlock_Auto() {
		return (EAttribute) titleBlockEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EDataType getEllipsisType() {
		return ellipsisTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EDataType getEllipsisTypeObject() {
		return ellipsisTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EDataType getTitlePercentType() {
		return titlePercentTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EDataType getTitlePercentTypeObject() {
		return titlePercentTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LayoutFactory getLayoutFactory() {
		return (LayoutFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to
	 * have no affect on any invocation but its first. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
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
		createEAttribute(blockEClass, BLOCK__WIDTH_HINT);
		createEAttribute(blockEClass, BLOCK__HEIGHT_HINT);
		createEReference(blockEClass, BLOCK__CURSOR);

		clientAreaEClass = createEClass(CLIENT_AREA);
		createEReference(clientAreaEClass, CLIENT_AREA__BACKGROUND);
		createEReference(clientAreaEClass, CLIENT_AREA__OUTLINE);
		createEReference(clientAreaEClass, CLIENT_AREA__SHADOW_COLOR);
		createEReference(clientAreaEClass, CLIENT_AREA__INSETS);
		createEAttribute(clientAreaEClass, CLIENT_AREA__VISIBLE);

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
		createEReference(legendEClass, LEGEND__TITLE);
		createEAttribute(legendEClass, LEGEND__TITLE_POSITION);
		createEAttribute(legendEClass, LEGEND__SHOW_VALUE);
		createEAttribute(legendEClass, LEGEND__SHOW_PERCENT);
		createEAttribute(legendEClass, LEGEND__SHOW_TOTAL);
		createEAttribute(legendEClass, LEGEND__WRAPPING_SIZE);
		createEAttribute(legendEClass, LEGEND__MAX_PERCENT);
		createEAttribute(legendEClass, LEGEND__TITLE_PERCENT);
		createEAttribute(legendEClass, LEGEND__ELLIPSIS);
		createEReference(legendEClass, LEGEND__FORMAT_SPECIFIER);

		plotEClass = createEClass(PLOT);
		createEAttribute(plotEClass, PLOT__HORIZONTAL_SPACING);
		createEAttribute(plotEClass, PLOT__VERTICAL_SPACING);
		createEReference(plotEClass, PLOT__CLIENT_AREA);

		titleBlockEClass = createEClass(TITLE_BLOCK);
		createEAttribute(titleBlockEClass, TITLE_BLOCK__AUTO);

		// Create data types
		ellipsisTypeEDataType = createEDataType(ELLIPSIS_TYPE);
		ellipsisTypeObjectEDataType = createEDataType(ELLIPSIS_TYPE_OBJECT);
		titlePercentTypeEDataType = createEDataType(TITLE_PERCENT_TYPE);
		titlePercentTypeObjectEDataType = createEDataType(TITLE_PERCENT_TYPE_OBJECT);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This method is
	 * guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		AttributePackage theAttributePackage = (AttributePackage) EPackage.Registry.INSTANCE
				.getEPackage(AttributePackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage) EPackage.Registry.INSTANCE
				.getEPackage(XMLTypePackage.eNS_URI);
		DataPackage theDataPackage = (DataPackage) EPackage.Registry.INSTANCE.getEPackage(DataPackage.eNS_URI);
		ComponentPackage theComponentPackage = (ComponentPackage) EPackage.Registry.INSTANCE
				.getEPackage(ComponentPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		labelBlockEClass.getESuperTypes().add(this.getBlock());
		legendEClass.getESuperTypes().add(this.getBlock());
		plotEClass.getESuperTypes().add(this.getBlock());
		titleBlockEClass.getESuperTypes().add(this.getLabelBlock());

		// Initialize classes and features; add operations and parameters
		initEClass(blockEClass, Block.class, "Block", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getBlock_Children(), this.getBlock(), null, "children", null, 1, -1, Block.class, !IS_TRANSIENT, //$NON-NLS-1$
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getBlock_Bounds(), theAttributePackage.getBounds(), null, "bounds", null, 0, 1, Block.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlock_Anchor(), theAttributePackage.getAnchor(), "anchor", null, 0, 1, Block.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlock_Stretch(), theAttributePackage.getStretch(), "stretch", null, 0, 1, Block.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBlock_Insets(), theAttributePackage.getInsets(), null, "insets", null, 0, 1, Block.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlock_Row(), theXMLTypePackage.getInt(), "row", "-1", 1, 1, Block.class, !IS_TRANSIENT, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlock_Column(), theXMLTypePackage.getInt(), "column", "-1", 1, 1, Block.class, !IS_TRANSIENT, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlock_Rowspan(), theXMLTypePackage.getInt(), "rowspan", "-1", 1, 1, Block.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlock_Columnspan(), theXMLTypePackage.getInt(), "columnspan", "-1", 1, 1, Block.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBlock_MinSize(), theAttributePackage.getSize(), null, "minSize", null, 1, 1, Block.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBlock_Outline(), theAttributePackage.getLineAttributes(), null, "outline", null, 1, 1, //$NON-NLS-1$
				Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBlock_Background(), theAttributePackage.getFill(), null, "background", null, 0, 1, //$NON-NLS-1$
				Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlock_Visible(), theXMLTypePackage.getBoolean(), "visible", "true", 1, 1, Block.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBlock_Triggers(), theDataPackage.getTrigger(), null, "triggers", null, 0, -1, Block.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlock_WidthHint(), theXMLTypePackage.getDouble(), "widthHint", "-1", 0, 1, Block.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlock_HeightHint(), theXMLTypePackage.getDouble(), "heightHint", "-1", 0, 1, Block.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBlock_Cursor(), theAttributePackage.getCursor(), null, "cursor", null, 0, 1, Block.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(clientAreaEClass, ClientArea.class, "ClientArea", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getClientArea_Background(), theAttributePackage.getFill(), null, "background", null, 1, 1, //$NON-NLS-1$
				ClientArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getClientArea_Outline(), theAttributePackage.getLineAttributes(), null, "outline", null, 1, 1, //$NON-NLS-1$
				ClientArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getClientArea_ShadowColor(), theAttributePackage.getColorDefinition(), null, "shadowColor", null, //$NON-NLS-1$
				1, 1, ClientArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getClientArea_Insets(), theAttributePackage.getInsets(), null, "insets", null, 1, 1, //$NON-NLS-1$
				ClientArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getClientArea_Visible(), theXMLTypePackage.getBoolean(), "visible", "true", 1, 1, //$NON-NLS-1$ //$NON-NLS-2$
				ClientArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(labelBlockEClass, LabelBlock.class, "LabelBlock", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLabelBlock_Label(), theComponentPackage.getLabel(), null, "label", null, 0, 1, //$NON-NLS-1$
				LabelBlock.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(legendEClass, Legend.class, "Legend", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getLegend_HorizontalSpacing(), theXMLTypePackage.getInt(), "horizontalSpacing", null, 1, 1, //$NON-NLS-1$
				Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getLegend_VerticalSpacing(), theXMLTypePackage.getInt(), "verticalSpacing", null, 1, 1, //$NON-NLS-1$
				Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getLegend_ClientArea(), this.getClientArea(), null, "clientArea", null, 1, 1, Legend.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLegend_Text(), theAttributePackage.getText(), null, "text", null, 1, 1, Legend.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLegend_Orientation(), theAttributePackage.getOrientation(), "orientation", "Vertical", 1, 1, //$NON-NLS-1$ //$NON-NLS-2$
				Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getLegend_Direction(), theAttributePackage.getDirection(), "direction", "Top_Bottom", 1, 1, //$NON-NLS-1$ //$NON-NLS-2$
				Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getLegend_Separator(), theAttributePackage.getLineAttributes(), null, "separator", null, 1, 1, //$NON-NLS-1$
				Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLegend_Position(), theAttributePackage.getPosition(), "position", "Right", 1, 1, Legend.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLegend_ItemType(), theAttributePackage.getLegendItemType(), "itemType", null, 1, 1, //$NON-NLS-1$
				Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getLegend_Title(), theComponentPackage.getLabel(), null, "title", null, 0, 1, Legend.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLegend_TitlePosition(), theAttributePackage.getPosition(), "titlePosition", null, 0, 1, //$NON-NLS-1$
				Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getLegend_ShowValue(), theXMLTypePackage.getBoolean(), "showValue", null, 0, 1, Legend.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLegend_ShowPercent(), theXMLTypePackage.getBoolean(), "showPercent", null, 0, 1, Legend.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLegend_ShowTotal(), theXMLTypePackage.getBoolean(), "showTotal", null, 0, 1, Legend.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLegend_WrappingSize(), theXMLTypePackage.getDouble(), "wrappingSize", null, 0, 1, //$NON-NLS-1$
				Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getLegend_MaxPercent(), theXMLTypePackage.getDouble(), "maxPercent", "0.33333333", 0, 1, //$NON-NLS-1$ //$NON-NLS-2$
				Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getLegend_TitlePercent(), this.getTitlePercentType(), "titlePercent", "0.6", 1, 1, Legend.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLegend_Ellipsis(), this.getEllipsisType(), "ellipsis", "1", 1, 1, Legend.class, !IS_TRANSIENT, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLegend_FormatSpecifier(), theAttributePackage.getFormatSpecifier(), null, "formatSpecifier", //$NON-NLS-1$
				null, 0, 1, Legend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(plotEClass, Plot.class, "Plot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getPlot_HorizontalSpacing(), theXMLTypePackage.getInt(), "horizontalSpacing", "5", 1, 1, //$NON-NLS-1$ //$NON-NLS-2$
				Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getPlot_VerticalSpacing(), theXMLTypePackage.getInt(), "verticalSpacing", "5", 1, 1, Plot.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPlot_ClientArea(), this.getClientArea(), null, "clientArea", null, 1, 1, Plot.class, //$NON-NLS-1$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(titleBlockEClass, TitleBlock.class, "TitleBlock", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTitleBlock_Auto(), theXMLTypePackage.getBoolean(), "auto", "false", 1, 1, TitleBlock.class, //$NON-NLS-1$ //$NON-NLS-2$
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(ellipsisTypeEDataType, int.class, "EllipsisType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEDataType(ellipsisTypeObjectEDataType, Integer.class, "EllipsisTypeObject", IS_SERIALIZABLE, //$NON-NLS-1$
				!IS_GENERATED_INSTANCE_CLASS);
		initEDataType(titlePercentTypeEDataType, double.class, "TitlePercentType", IS_SERIALIZABLE, //$NON-NLS-1$
				!IS_GENERATED_INSTANCE_CLASS);
		initEDataType(titlePercentTypeObjectEDataType, Double.class, "TitlePercentTypeObject", IS_SERIALIZABLE, //$NON-NLS-1$
				!IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for
	 * <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData </b>. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$
		addAnnotation(blockEClass, source, new String[] { "name", "Block", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Children(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Children" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Bounds(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Bounds" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Anchor(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Anchor" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Stretch(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Stretch" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Insets(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Insets" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Row(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Row" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Column(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Column" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Rowspan(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Rowspan" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Columnspan(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Columnspan" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_MinSize(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "MinSize" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Outline(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Outline" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Background(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Background" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Visible(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Visible" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Triggers(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Triggers" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_WidthHint(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "WidthHint" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_HeightHint(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "HeightHint" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getBlock_Cursor(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Cursor" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(clientAreaEClass, source, new String[] { "name", "ClientArea", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getClientArea_Background(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Background" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getClientArea_Outline(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Outline" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getClientArea_ShadowColor(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ShadowColor" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getClientArea_Insets(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Insets" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getClientArea_Visible(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Visible" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(ellipsisTypeEDataType, source, new String[] { "name", "Ellipsis_._type", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "http://www.eclipse.org/emf/2003/XMLType#int", //$NON-NLS-1$ //$NON-NLS-2$
				"minInclusive", "0" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(ellipsisTypeObjectEDataType, source, new String[] { "name", "Ellipsis_._type:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "Ellipsis_._type" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(labelBlockEClass, source, new String[] { "name", "LabelBlock", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLabelBlock_Label(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Label" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(legendEClass, source, new String[] { "name", "Legend", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_HorizontalSpacing(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "HorizontalSpacing" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_VerticalSpacing(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "VerticalSpacing" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_ClientArea(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ClientArea" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_Text(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Text" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_Orientation(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Orientation" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_Direction(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Direction" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_Separator(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Separator" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_Position(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Position" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_ItemType(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ItemType" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_Title(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Title" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_TitlePosition(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "TitlePosition" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_ShowValue(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ShowValue" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_ShowPercent(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ShowPercent" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_ShowTotal(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ShowTotal" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_WrappingSize(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "WrappingSize" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_MaxPercent(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "MaxPercent" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_TitlePercent(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "TitlePercent" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_Ellipsis(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Ellipsis" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getLegend_FormatSpecifier(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "FormatSpecifier" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(plotEClass, source, new String[] { "name", "Plot", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPlot_HorizontalSpacing(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "HorizontalSpacing" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPlot_VerticalSpacing(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "VerticalSpacing" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getPlot_ClientArea(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "ClientArea" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(titleBlockEClass, source, new String[] { "name", "TitleBlock", //$NON-NLS-1$ //$NON-NLS-2$
				"kind", "elementOnly" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(getTitleBlock_Auto(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
				"name", "Auto" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(titlePercentTypeEDataType, source, new String[] { "name", "TitlePercent_._type", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "http://www.eclipse.org/emf/2003/XMLType#double", //$NON-NLS-1$ //$NON-NLS-2$
				"minInclusive", "0", //$NON-NLS-1$ //$NON-NLS-2$
				"maxInclusive", "1" //$NON-NLS-1$ //$NON-NLS-2$
		});
		addAnnotation(titlePercentTypeObjectEDataType, source, new String[] { "name", "TitlePercent_._type:Object", //$NON-NLS-1$ //$NON-NLS-2$
				"baseType", "TitlePercent_._type" //$NON-NLS-1$ //$NON-NLS-2$
		});
	}

} // LayoutPackageImpl
