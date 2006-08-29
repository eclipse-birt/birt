/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multiple Fill</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 			This type represents a list of fill object.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.MultipleFill#getFills <em>Fills</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMultipleFill()
 * @model extendedMetaData="name='MultipleFill' kind='elementOnly'"
 * @generated
 */
public interface MultipleFill extends Fill
{

	/**
	 * Returns the value of the '<em><b>Fills</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.birt.chart.model.attribute.Fill}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fills</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fills</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMultipleFill_Fills()
	 * @model type="org.eclipse.birt.chart.model.attribute.Fill" containment="true"
	 *        extendedMetaData="kind='element' name='Fills'"
	 * @generated
	 */
	EList getFills( );

} // MultipleFill