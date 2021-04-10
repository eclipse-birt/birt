/**
 * <copyright>
 * </copyright>
 *
 * $Id: DesignValues.java,v 1.1.28.1 2010/11/29 06:23:53 rlu Exp $
 */

package org.eclipse.birt.report.model.adapter.oda.model;

import org.eclipse.datatools.connectivity.oda.design.ResultSets;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object ' <em><b>Design
 * Values</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc --> A collection of ODA related values. Includes data
 * set parameters and result set columns. <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getVersion
 * <em>Version</em>}</li>
 * <li>
 * {@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getDataSetParameters
 * <em>Data Set Parameters</em>}</li>
 * <li>
 * {@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getDataSetParameters1
 * <em>Data Set Parameters1</em>}</li>
 * <li>
 * {@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getResultSets
 * <em>Result Sets</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDesignValues()
 * @model extendedMetaData="name='DesignValues' kind='elementOnly'"
 * @generated
 */
public interface DesignValues extends EObject {

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute. The default
	 * value is <code>"2.0.0"</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * Specifies the version number of these values.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #isSetVersion()
	 * @see #unsetVersion()
	 * @see #setVersion(String)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDesignValues_Version()
	 * @model default="2.0.0" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Version'"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getVersion
	 * <em>Version</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #isSetVersion()
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getVersion
	 * <em>Version</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
	void unsetVersion();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getVersion
	 * <em>Version</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Version</em>' attribute is set.
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
	boolean isSetVersion();

	/**
	 * Returns the value of the '<em><b>Data Set Parameters</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Data Set Parameters</em>' containment
	 *         reference.
	 * @see #setDataSetParameters(DataSetParameters)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDesignValues_DataSetParameters()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='DataSetParameters'"
	 * @generated
	 */
	DataSetParameters getDataSetParameters();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getDataSetParameters
	 * <em>Data Set Parameters</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Data Set Parameters</em>' containment
	 *              reference.
	 * @see #getDataSetParameters()
	 * @generated
	 */
	void setDataSetParameters(DataSetParameters value);

	/**
	 * Returns the value of the '<em><b>Result Sets</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> A
	 * collection of result sets' definition and metadata. If the metadata can be
	 * derived, i.e. can be obtained by an ODA driver in each design session, an ODA
	 * host designer is not required to include the derived metadata in the next
	 * design session request. An ODA designer may ignore such metadata in a
	 * Request. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Result Sets</em>' containment reference.
	 * @see #setResultSets(ResultSets)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDesignValues_ResultSets()
	 * @model containment="true" required="true" extendedMetaData= "kind='element'
	 *        name='ResultSets'
	 *        namespace='http://www.eclipse.org/datatools/connectivity/oda/design'"
	 * @generated
	 */
	ResultSets getResultSets();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getResultSets
	 * <em>Result Sets</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Result Sets</em>' containment
	 *              reference.
	 * @see #getResultSets()
	 * @generated
	 */
	void setResultSets(ResultSets value);

	/**
	 * Returns the value of the '<em><b>Data Set Parameters1</b></em>' containment
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Set Parameters1</em>' containment reference
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc --> <!-- begin-model-doc --> A collection of top-level
	 * parameters defined for a data set. If the metadata can be derived, i.e. can
	 * be obtained by an ODA driver in each design session, an ODA host designer is
	 * not required to include the derived metadata in the next design session
	 * request. An ODA designer may ignore such metadata in a Request. <!--
	 * end-model-doc -->
	 * 
	 * @return the value of the '<em>Data Set Parameters1</em>' containment
	 *         reference.
	 * @see #setDataSetParameters1(org.eclipse.datatools.connectivity.oda.design.DataSetParameters)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDesignValues_DataSetParameters1()
	 * @model containment="true" required="true" extendedMetaData= "kind='element'
	 *        name='DataSetParameters'
	 *        namespace='http://www.eclipse.org/datatools/connectivity/oda/design'"
	 * @generated
	 */
	org.eclipse.datatools.connectivity.oda.design.DataSetParameters getDataSetParameters1();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.report.model.adapter.oda.model.DesignValues#getDataSetParameters1
	 * <em>Data Set Parameters1</em>}' containment reference. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Data Set Parameters1</em>' containment
	 *              reference.
	 * @see #getDataSetParameters1()
	 * @generated
	 */
	void setDataSetParameters1(org.eclipse.datatools.connectivity.oda.design.DataSetParameters value);

} // DesignValues
