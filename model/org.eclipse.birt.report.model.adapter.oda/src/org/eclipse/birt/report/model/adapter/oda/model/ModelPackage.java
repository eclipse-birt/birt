/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.birt.report.model.adapter.oda.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 * 		Schema file for the model.oda.adapter package.
 * 		
 * <!-- end-model-doc -->
 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage
{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/birt/report/model/adapter/odaModel";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Oda Values</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__ODA_VALUES = 3;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl <em>Design Values</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl
	 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDesignValues()
	 * @generated
	 */
	int DESIGN_VALUES = 1;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_VALUES__VERSION = 0;

	/**
	 * The feature id for the '<em><b>Data Set Parameters</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_VALUES__DATA_SET_PARAMETERS = 1;

	/**
	 * The feature id for the '<em><b>Result Sets</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_VALUES__RESULT_SETS = 2;

	/**
	 * The number of structural features of the '<em>Design Values</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESIGN_VALUES_FEATURE_COUNT = 3;


	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getOdaValues <em>Oda Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Oda Values</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot#getDesignValues()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_OdaValues();

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues <em>Design Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Design Values</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DesignValues
	 * @generated
	 */
	EClass getDesignValues();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getVersion()
	 * @see #getDesignValues()
	 * @generated
	 */
	EAttribute getDesignValues_Version();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getDataSetParameters <em>Data Set Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data Set Parameters</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getDataSetParameters()
	 * @see #getDesignValues()
	 * @generated
	 */
	EReference getDesignValues_DataSetParameters();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getResultSets <em>Result Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Result Sets</em>'.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getResultSets()
	 * @see #getDesignValues()
	 * @generated
	 */
	EReference getDesignValues_ResultSets();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals 
	{
		/**
		 * The meta object literal for the '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DocumentRootImpl
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDocumentRoot()
		 * @generated
		 */
		EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Oda Values</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__ODA_VALUES = eINSTANCE.getDocumentRoot_OdaValues();

		/**
		 * The meta object literal for the '{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl <em>Design Values</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.DesignValuesImpl
		 * @see org.eclipse.birt.report.model.adapter.oda.model.impl.ModelPackageImpl#getDesignValues()
		 * @generated
		 */
		EClass DESIGN_VALUES = eINSTANCE.getDesignValues();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DESIGN_VALUES__VERSION = eINSTANCE.getDesignValues_Version();

		/**
		 * The meta object literal for the '<em><b>Data Set Parameters</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DESIGN_VALUES__DATA_SET_PARAMETERS = eINSTANCE.getDesignValues_DataSetParameters();

		/**
		 * The meta object literal for the '<em><b>Result Sets</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DESIGN_VALUES__RESULT_SETS = eINSTANCE.getDesignValues_ResultSets();

	}

} //ModelPackage
