/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id: DynamicList.java,v 1.1.2.1 2010/11/29 06:23:53 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Dynamic
 * List</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getDataSetName
 * <em>Data Set Name</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getEnabled
 * <em>Enabled</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getLabelColumn
 * <em>Label Column</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getValueColumn
 * <em>Value Column</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDynamicList()
 * @model extendedMetaData="name='DynamicList' kind='empty'"
 * @generated
 */
public interface DynamicList extends EObject {
	/**
	 * Returns the value of the '<em><b>Data Set Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Set Name</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Data Set Name</em>' attribute.
	 * @see #setDataSetName(String)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDynamicList_DataSetName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='dataSetName'"
	 * @generated
	 */
	String getDataSetName();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getDataSetName
	 * <em>Data Set Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Data Set Name</em>' attribute.
	 * @see #getDataSetName()
	 * @generated
	 */
	void setDataSetName(String value);

	/**
	 * Returns the value of the '<em><b>Enabled</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enabled</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Enabled</em>' attribute.
	 * @see #setEnabled(String)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDynamicList_Enabled()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='enabled'"
	 * @generated
	 */
	String getEnabled();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getEnabled
	 * <em>Enabled</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Enabled</em>' attribute.
	 * @see #getEnabled()
	 * @generated
	 */
	void setEnabled(String value);

	/**
	 * Returns the value of the '<em><b>Label Column</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label Column</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Label Column</em>' attribute.
	 * @see #setLabelColumn(String)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDynamicList_LabelColumn()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='labelColumn'"
	 * @generated
	 */
	String getLabelColumn();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getLabelColumn
	 * <em>Label Column</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Label Column</em>' attribute.
	 * @see #getLabelColumn()
	 * @generated
	 */
	void setLabelColumn(String value);

	/**
	 * Returns the value of the '<em><b>Value Column</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value Column</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Value Column</em>' attribute.
	 * @see #setValueColumn(String)
	 * @see org.eclipse.birt.report.model.adapter.oda.model.ModelPackage#getDynamicList_ValueColumn()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='valueColumn'"
	 * @generated
	 */
	String getValueColumn();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.report.model.adapter.oda.model.DynamicList#getValueColumn
	 * <em>Value Column</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Value Column</em>' attribute.
	 * @see #getValueColumn()
	 * @generated
	 */
	void setValueColumn(String value);

} // DynamicList
