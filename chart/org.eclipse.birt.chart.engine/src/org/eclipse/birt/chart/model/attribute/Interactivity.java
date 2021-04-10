/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.birt.chart.model.IChartObject;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Interactivity</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Interactivity represents the interactivity settings
 * for the chart. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Interactivity#isEnable
 * <em>Enable</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Interactivity#getLegendBehavior
 * <em>Legend Behavior</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getInteractivity()
 * @model extendedMetaData="name='Interactivity' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Interactivity extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Enable</b></em>' attribute. The default
	 * value is <code>"true"</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc --> Attribute "Enable" specifies whether all interactive
	 * features are enabled, by default it's true. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Enable</em>' attribute.
	 * @see #isSetEnable()
	 * @see #unsetEnable()
	 * @see #setEnable(boolean)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getInteractivity_Enable()
	 * @model default="true" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='element' name='Enable'"
	 * @generated
	 */
	boolean isEnable();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Interactivity#isEnable
	 * <em>Enable</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Enable</em>' attribute.
	 * @see #isSetEnable()
	 * @see #unsetEnable()
	 * @see #isEnable()
	 * @generated
	 */
	void setEnable(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Interactivity#isEnable
	 * <em>Enable</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetEnable()
	 * @see #isEnable()
	 * @see #setEnable(boolean)
	 * @generated
	 */
	void unsetEnable();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Interactivity#isEnable
	 * <em>Enable</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Enable</em>' attribute is set.
	 * @see #unsetEnable()
	 * @see #isEnable()
	 * @see #setEnable(boolean)
	 * @generated
	 */
	boolean isSetEnable();

	/**
	 * Returns the value of the '<em><b>Legend Behavior</b></em>' attribute. The
	 * default value is <code>"None"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.LegendBehaviorType}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Three
	 * possible behaviours: None, Toggle the Serie visibility, Highlight the serie.
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Legend Behavior</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.LegendBehaviorType
	 * @see #isSetLegendBehavior()
	 * @see #unsetLegendBehavior()
	 * @see #setLegendBehavior(LegendBehaviorType)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getInteractivity_LegendBehavior()
	 * @model default="None" unsettable="true" extendedMetaData="kind='element'
	 *        name='LegendBehavior'"
	 * @generated
	 */
	LegendBehaviorType getLegendBehavior();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Interactivity#getLegendBehavior
	 * <em>Legend Behavior</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Legend Behavior</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.LegendBehaviorType
	 * @see #isSetLegendBehavior()
	 * @see #unsetLegendBehavior()
	 * @see #getLegendBehavior()
	 * @generated
	 */
	void setLegendBehavior(LegendBehaviorType value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Interactivity#getLegendBehavior
	 * <em>Legend Behavior</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetLegendBehavior()
	 * @see #getLegendBehavior()
	 * @see #setLegendBehavior(LegendBehaviorType)
	 * @generated
	 */
	void unsetLegendBehavior();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Interactivity#getLegendBehavior
	 * <em>Legend Behavior</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Legend Behavior</em>' attribute is set.
	 * @see #unsetLegendBehavior()
	 * @see #getLegendBehavior()
	 * @see #setLegendBehavior(LegendBehaviorType)
	 * @generated
	 */
	boolean isSetLegendBehavior();

	/**
	 * @generated
	 */
	Interactivity copyInstance();

} // Interactivity
