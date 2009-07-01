/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rotation3 D</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 			This type defines the list of rotated angles.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.Rotation3D#getAngles <em>Angles</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getRotation3D()
 * @model extendedMetaData="name='Rotation3D' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Rotation3D extends IChartObject
{

	/**
	 * Returns the value of the '<em><b>Angles</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.birt.chart.model.attribute.Angle3D}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Angles</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Angles</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getRotation3D_Angles()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Angles'"
	 * @generated
	 */
	EList<Angle3D> getAngles( );

	/**
	 * @generated
	 */
	Rotation3D copyInstance( );

} // Rotation3D
