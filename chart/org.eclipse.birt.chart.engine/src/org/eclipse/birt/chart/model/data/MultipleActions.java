/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.data;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Multiple
 * Actions</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.MultipleActions#getActions
 * <em>Actions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.MultipleActions#getPropertiesMap
 * <em>Properties Map</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getMultipleActions()
 * @model extendedMetaData="name='MultipleActions' kind='elementOnly'"
 * @generated
 */
public interface MultipleActions extends Action {

	/**
	 * Returns the value of the '<em><b>Actions</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.Action}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actions</em>' containment reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Actions</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getMultipleActions_Actions()
	 * @model containment="true" extendedMetaData="kind='element' name='Actions'"
	 * @generated
	 */
	EList<Action> getActions();

	/**
	 * Returns the value of the '<em><b>Properties Map</b></em>' map. The key is of
	 * type {@link java.lang.String}, and the value is of type
	 * {@link java.lang.String}, <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties Map</em>' map isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Properties Map</em>' map.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getMultipleActions_PropertiesMap()
	 * @model mapType="org.eclipse.birt.chart.model.attribute.EStringToStringMapEntry<org.eclipse.emf.ecore.xml.type.String,
	 *        org.eclipse.emf.ecore.xml.type.String>"
	 *        extendedMetaData="kind='element' name='PropertiesMap'"
	 * @generated
	 */
	EMap<String, String> getPropertiesMap();

	/**
	 * @generated
	 */
	MultipleActions copyInstance();

} // MultipleActions
