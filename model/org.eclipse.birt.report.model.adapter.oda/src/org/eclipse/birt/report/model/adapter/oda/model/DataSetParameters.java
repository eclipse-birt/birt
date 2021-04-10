/**
 * <copyright>
 * </copyright>
 *
 * $Id: DataSetParameters.java,v 1.1.2.1 2010/11/29 06:23:53 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Data Set
 * Parameters</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameters#getParameter
 * <em>Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDataSetParameters()
 * @model extendedMetaData="name='DataSetParameters' kind='elementOnly'"
 * @generated
 */
public interface DataSetParameters extends EObject {
	/**
	 * Returns the value of the '<em><b>Parameter</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter</em>' containment reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Parameter</em>' containment reference list.
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDataSetParameters_Parameter()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='parameter'"
	 * @generated
	 */
	EList<DataSetParameter> getParameters();

} // DataSetParameters
