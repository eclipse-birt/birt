/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.attribute;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Gradient</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Gradient extends type Fill specialized to represent
 * a two-color gradient.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Gradient#getStartColor
 * <em>Start Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Gradient#getEndColor
 * <em>End Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Gradient#getDirection
 * <em>Direction</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Gradient#isCyclic
 * <em>Cyclic</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Gradient#getTransparency
 * <em>Transparency</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getGradient()
 * @model extendedMetaData="name='Gradient' kind='elementOnly'"
 * @generated
 */
public interface Gradient extends Fill {

	/**
	 * Returns the value of the '<em><b>Start Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> Gets the start color of the gradient. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the start color of the gradient.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Start Color</em>' containment reference.
	 * @see #setStartColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getGradient_StartColor()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	ColorDefinition getStartColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#getStartColor
	 * <em>Start Color</em>}' containment reference. <!-- begin-user-doc --> Sets
	 * the start color of the gradient. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Start Color</em>' containment
	 *              reference.
	 * @see #getStartColor()
	 * @generated
	 */
	void setStartColor(ColorDefinition value);

	/**
	 * Returns the value of the '<em><b>End Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> Gets the end color of the gradient. <!-- end-user-doc
	 * --> <!-- begin-model-doc -->
	 * 
	 * Specifies the end color of the gradient.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>End Color</em>' containment reference.
	 * @see #setEndColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getGradient_EndColor()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	ColorDefinition getEndColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#getEndColor <em>End
	 * Color</em>}' containment reference. <!-- begin-user-doc --> Sets the end
	 * color of the gradient. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>End Color</em>' containment reference.
	 * @see #getEndColor()
	 * @generated
	 */
	void setEndColor(ColorDefinition value);

	/**
	 * Returns the value of the '<em><b>Direction</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the angle of the gradient. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Defines the angle of the gradient. e.g. If Direction is 45, the Gradient is
	 * at an angle of 45 degrees to vertical. With start color being on the left.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Direction</em>' attribute.
	 * @see #isSetDirection()
	 * @see #unsetDirection()
	 * @see #setDirection(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getGradient_Direction()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 * @generated
	 */
	double getDirection();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#getDirection
	 * <em>Direction</em>}' attribute. <!-- begin-user-doc --> Sets the angle of the
	 * gradient. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Direction</em>' attribute.
	 * @see #isSetDirection()
	 * @see #unsetDirection()
	 * @see #getDirection()
	 * @generated
	 */
	void setDirection(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#getDirection
	 * <em>Direction</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetDirection()
	 * @see #getDirection()
	 * @see #setDirection(double)
	 * @generated
	 */
	void unsetDirection();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.Gradient#getDirection
	 * <em>Direction</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Direction</em>' attribute is set.
	 * @see #unsetDirection()
	 * @see #getDirection()
	 * @see #setDirection(double)
	 * @generated
	 */
	boolean isSetDirection();

	/**
	 * Returns the value of the '<em><b>Cyclic</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Cyclic" specifies whether the gradient is cyclic or linear.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Cyclic</em>' attribute.
	 * @see #isSetCyclic()
	 * @see #unsetCyclic()
	 * @see #setCyclic(boolean)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getGradient_Cyclic()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element' name='Cyclic'"
	 * @generated
	 */
	boolean isCyclic();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#isCyclic
	 * <em>Cyclic</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Cyclic</em>' attribute.
	 * @see #isSetCyclic()
	 * @see #unsetCyclic()
	 * @see #isCyclic()
	 * @generated
	 */
	void setCyclic(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#isCyclic
	 * <em>Cyclic</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetCyclic()
	 * @see #isCyclic()
	 * @see #setCyclic(boolean)
	 * @generated
	 */
	void unsetCyclic();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.Gradient#isCyclic
	 * <em>Cyclic</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Cyclic</em>' attribute is set.
	 * @see #unsetCyclic()
	 * @see #isCyclic()
	 * @see #setCyclic(boolean)
	 * @generated
	 */
	boolean isSetCyclic();

	/**
	 * Returns the value of the '<em><b>Transparency</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the transparency for the gradient colors. <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the transparency for the gradient.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Transparency</em>' attribute.
	 * @see #isSetTransparency()
	 * @see #unsetTransparency()
	 * @see #setTransparency(int)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getGradient_Transparency()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getTransparency();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.Gradient#getTransparency
	 * <em>Transparency</em>}' attribute. <!-- begin-user-doc --> Sets the
	 * transparency for the gradient. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Transparency</em>' attribute.
	 * @see #isSetTransparency()
	 * @see #unsetTransparency()
	 * @see #getTransparency()
	 * @generated
	 */
	void setTransparency(int value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.Gradient#getTransparency
	 * <em>Transparency</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetTransparency()
	 * @see #getTransparency()
	 * @see #setTransparency(int)
	 * @generated
	 */
	void unsetTransparency();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Gradient#getTransparency
	 * <em>Transparency</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Transparency</em>' attribute is set.
	 * @see #unsetTransparency()
	 * @see #getTransparency()
	 * @see #setTransparency(int)
	 * @generated
	 */
	boolean isSetTransparency();

	/**
	 * @generated
	 */
	Gradient copyInstance();

} // Gradient
