/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.type;

import org.eclipse.birt.chart.model.attribute.LineAttributes;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Difference Series</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.type.DifferenceSeries#getNegativeMarkers <em>Negative Markers</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.DifferenceSeries#getNegativeLineAttributes <em>Negative Line Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage#getDifferenceSeries()
 * @model extendedMetaData="name='DifferenceSeries' kind='elementOnly'"
 * @generated
 */
public interface DifferenceSeries extends AreaSeries
{

	/**
	 * Returns the value of the '<em><b>Negative Markers</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.birt.chart.model.attribute.Marker}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies the marker to be used for displaying the data point on the negative line in the chart.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Negative Markers</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getDifferenceSeries_NegativeMarkers()
	 * @model type="org.eclipse.birt.chart.model.attribute.Marker" containment="true"
	 *        extendedMetaData="kind='element' name='NegativeMarkers'"
	 * @generated
	 */
	EList getNegativeMarkers( );

	/**
	 * Returns the value of the '<em><b>Negative Line Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies the attributes for the negative line used to represent this series.
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Negative Line Attributes</em>' containment reference.
	 * @see #setNegativeLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getDifferenceSeries_NegativeLineAttributes()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='NegativeLineAttributes'"
	 * @generated
	 */
	LineAttributes getNegativeLineAttributes( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.DifferenceSeries#getNegativeLineAttributes <em>Negative Line Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Negative Line Attributes</em>' containment reference.
	 * @see #getNegativeLineAttributes()
	 * @generated
	 */
	void setNegativeLineAttributes( LineAttributes value );

} // DifferenceSeries