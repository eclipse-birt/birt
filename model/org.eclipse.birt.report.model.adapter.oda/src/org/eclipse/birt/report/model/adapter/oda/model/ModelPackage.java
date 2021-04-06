/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelPackage.java,v 1.1.28.1 2010/11/29 06:23:53 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * Schema file for the model.oda.model package.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/birt/report/model/adapter/odaModel";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "model";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	ModelPackage eINSTANCE = org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DataSetParameterImpl
	 * <em>Data Set Parameter</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DataSetParameterImpl
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDataSetParameter()
	 * @generated
	 */
	int DATA_SET_PARAMETER = 0;

	/**
	 * The feature id for the '<em><b>Parameter Definition</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_SET_PARAMETER__PARAMETER_DEFINITION = 0;

	/**
	 * The feature id for the '<em><b>Dynamic List</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_SET_PARAMETER__DYNAMIC_LIST = 1;

	/**
	 * The number of structural features of the '<em>Data Set Parameter</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_SET_PARAMETER_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DataSetParametersImpl
	 * <em>Data Set Parameters</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DataSetParametersImpl
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDataSetParameters()
	 * @generated
	 */
	int DATA_SET_PARAMETERS = 1;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_SET_PARAMETERS__PARAMETER = 0;

	/**
	 * The number of structural features of the '<em>Data Set Parameters</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_SET_PARAMETERS_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl
	 * <em>Design Values</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDesignValues()
	 * @generated
	 */
	int DESIGN_VALUES = 2;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DESIGN_VALUES__VERSION = 0;

	/**
	 * The feature id for the '<em><b>Data Set Parameters</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DESIGN_VALUES__DATA_SET_PARAMETERS = 1;

	/**
	 * The feature id for the '<em><b>Data Set Parameters1</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DESIGN_VALUES__DATA_SET_PARAMETERS1 = 2;

	/**
	 * The feature id for the '<em><b>Result Sets</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DESIGN_VALUES__RESULT_SETS = 3;

	/**
	 * The number of structural features of the '<em>Design Values</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DESIGN_VALUES_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl
	 * <em>Document Root</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 3;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Data Set Parameter</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DATA_SET_PARAMETER = 3;

	/**
	 * The feature id for the '<em><b>Data Set Parameters</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DATA_SET_PARAMETERS = 4;

	/**
	 * The feature id for the '<em><b>Design Values</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DESIGN_VALUES = 5;

	/**
	 * The feature id for the '<em><b>Dynamic List</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DYNAMIC_LIST = 6;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DynamicListImpl
	 * <em>Dynamic List</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DynamicListImpl
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDynamicList()
	 * @generated
	 */
	int DYNAMIC_LIST = 4;

	/**
	 * The feature id for the '<em><b>Data Set Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_LIST__DATA_SET_NAME = 0;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_LIST__ENABLED = 1;

	/**
	 * The feature id for the '<em><b>Label Column</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_LIST__LABEL_COLUMN = 2;

	/**
	 * The feature id for the '<em><b>Value Column</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_LIST__VALUE_COLUMN = 3;

	/**
	 * The number of structural features of the '<em>Dynamic List</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_LIST_FEATURE_COUNT = 4;

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter
	 * <em>Data Set Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Data Set Parameter</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter
	 * @generated
	 */
	EClass getDataSetParameter();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter#getParameterDefinition
	 * <em>Parameter Definition</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the containment reference '<em>Parameter
	 *         Definition</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter#getParameterDefinition()
	 * @see #getDataSetParameter()
	 * @generated
	 */
	EReference getDataSetParameter_ParameterDefinition();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter#getDynamicList
	 * <em>Dynamic List</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Dynamic
	 *         List</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter#getDynamicList()
	 * @see #getDataSetParameter()
	 * @generated
	 */
	EReference getDataSetParameter_DynamicList();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameters
	 * <em>Data Set Parameters</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Data Set Parameters</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DataSetParameters
	 * @generated
	 */
	EClass getDataSetParameters();

	/**
	 * Returns the meta object for the containment reference list
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameters#getParameter
	 * <em>Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list
	 *         '<em>Parameter</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DataSetParameters#getParameters()
	 * @see #getDataSetParameters()
	 * @generated
	 */
	EReference getDataSetParameters_Parameter();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues
	 * <em>Design Values</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Design Values</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DesignValues
	 * @generated
	 */
	EClass getDesignValues();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getVersion
	 * <em>Version</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getVersion()
	 * @see #getDesignValues()
	 * @generated
	 */
	EAttribute getDesignValues_Version();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getDataSetParameters
	 * <em>Data Set Parameters</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Data Set
	 *         Parameters</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getDataSetParameters()
	 * @see #getDesignValues()
	 * @generated
	 */
	EReference getDesignValues_DataSetParameters();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getResultSets
	 * <em>Result Sets</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Result Sets</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getResultSets()
	 * @see #getDesignValues()
	 * @generated
	 */
	EReference getDesignValues_ResultSets();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getDataSetParameters1
	 * <em>Data Set Parameters1</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the containment reference '<em>Data Set
	 *         Parameters1</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getDataSetParameters1()
	 * @see #getDesignValues()
	 * @generated
	 */
	EReference getDesignValues_DataSetParameters1();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot
	 * <em>Document Root</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getMixed
	 * <em>Mixed</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getXMLNSPrefixMap
	 * <em>XMLNS Prefix Map</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getXSISchemaLocation
	 * <em>XSI Schema Location</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDataSetParameter
	 * <em>Data Set Parameter</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Data Set
	 *         Parameter</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDataSetParameter()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DataSetParameter();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDataSetParameters
	 * <em>Data Set Parameters</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Data Set
	 *         Parameters</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDataSetParameters()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DataSetParameters();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDesignValues
	 * <em>Design Values</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Design
	 *         Values</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDesignValues()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DesignValues();

	/**
	 * Returns the meta object for the containment reference
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDynamicList
	 * <em>Dynamic List</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Dynamic
	 *         List</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDynamicList()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DynamicList();

	/**
	 * Returns the meta object for class
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList
	 * <em>Dynamic List</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Dynamic List</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DynamicList
	 * @generated
	 */
	EClass getDynamicList();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getDataSetName
	 * <em>Data Set Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Data Set Name</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getDataSetName()
	 * @see #getDynamicList()
	 * @generated
	 */
	EAttribute getDynamicList_DataSetName();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getEnabled
	 * <em>Enabled</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Enabled</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getEnabled()
	 * @see #getDynamicList()
	 * @generated
	 */
	EAttribute getDynamicList_Enabled();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getLabelColumn
	 * <em>Label Column</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label Column</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getLabelColumn()
	 * @see #getDynamicList()
	 * @generated
	 */
	EAttribute getDynamicList_LabelColumn();

	/**
	 * Returns the meta object for the attribute
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getValueColumn
	 * <em>Value Column</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Value Column</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getValueColumn()
	 * @see #getDynamicList()
	 * @generated
	 */
	EAttribute getDynamicList_ValueColumn();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DataSetParameterImpl
		 * <em>Data Set Parameter</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DataSetParameterImpl
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDataSetParameter()
		 * @generated
		 */
		EClass DATA_SET_PARAMETER = eINSTANCE.getDataSetParameter();

		/**
		 * The meta object literal for the '<em><b>Parameter Definition</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DATA_SET_PARAMETER__PARAMETER_DEFINITION = eINSTANCE.getDataSetParameter_ParameterDefinition();

		/**
		 * The meta object literal for the '<em><b>Dynamic List</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DATA_SET_PARAMETER__DYNAMIC_LIST = eINSTANCE.getDataSetParameter_DynamicList();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DataSetParametersImpl
		 * <em>Data Set Parameters</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DataSetParametersImpl
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDataSetParameters()
		 * @generated
		 */
		EClass DATA_SET_PARAMETERS = eINSTANCE.getDataSetParameters();

		/**
		 * The meta object literal for the '<em><b>Parameter</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DATA_SET_PARAMETERS__PARAMETER = eINSTANCE.getDataSetParameters_Parameter();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl
		 * <em>Design Values</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDesignValues()
		 * @generated
		 */
		EClass DESIGN_VALUES = eINSTANCE.getDesignValues();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DESIGN_VALUES__VERSION = eINSTANCE.getDesignValues_Version();

		/**
		 * The meta object literal for the '<em><b>Data Set Parameters</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DESIGN_VALUES__DATA_SET_PARAMETERS = eINSTANCE.getDesignValues_DataSetParameters();

		/**
		 * The meta object literal for the '<em><b>Result Sets</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DESIGN_VALUES__RESULT_SETS = eINSTANCE.getDesignValues_ResultSets();

		/**
		 * The meta object literal for the '<em><b>Data Set Parameters1</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DESIGN_VALUES__DATA_SET_PARAMETERS1 = eINSTANCE.getDesignValues_DataSetParameters1();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl
		 * <em>Document Root</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDocumentRoot()
		 * @generated
		 */
		EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Data Set Parameter</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DATA_SET_PARAMETER = eINSTANCE.getDocumentRoot_DataSetParameter();

		/**
		 * The meta object literal for the '<em><b>Data Set Parameters</b></em>'
		 * containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DATA_SET_PARAMETERS = eINSTANCE.getDocumentRoot_DataSetParameters();

		/**
		 * The meta object literal for the '<em><b>Design Values</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DESIGN_VALUES = eINSTANCE.getDocumentRoot_DesignValues();

		/**
		 * The meta object literal for the '<em><b>Dynamic List</b></em>' containment
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DYNAMIC_LIST = eINSTANCE.getDocumentRoot_DynamicList();

		/**
		 * The meta object literal for the
		 * '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DynamicListImpl
		 * <em>Dynamic List</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DynamicListImpl
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDynamicList()
		 * @generated
		 */
		EClass DYNAMIC_LIST = eINSTANCE.getDynamicList();

		/**
		 * The meta object literal for the '<em><b>Data Set Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DYNAMIC_LIST__DATA_SET_NAME = eINSTANCE.getDynamicList_DataSetName();

		/**
		 * The meta object literal for the '<em><b>Enabled</b></em>' attribute feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DYNAMIC_LIST__ENABLED = eINSTANCE.getDynamicList_Enabled();

		/**
		 * The meta object literal for the '<em><b>Label Column</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DYNAMIC_LIST__LABEL_COLUMN = eINSTANCE.getDynamicList_LabelColumn();

		/**
		 * The meta object literal for the '<em><b>Value Column</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DYNAMIC_LIST__VALUE_COLUMN = eINSTANCE.getDynamicList_ValueColumn();

	}

} // ModelPackage
