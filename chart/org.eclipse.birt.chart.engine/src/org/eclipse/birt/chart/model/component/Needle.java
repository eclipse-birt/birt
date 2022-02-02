/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.component;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineDecorator;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Needle</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This type defines a needle of a dial. <!--
 * end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.Needle#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Needle#getDecorator
 * <em>Decorator</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getNeedle()
 * @model extendedMetaData="name='Needle' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Needle extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * --> Specify the line properties for the needle. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Line Attributes</em>' containment reference.
	 * @see #setLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getNeedle_LineAttributes()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='LineAttributes'"
	 * @generated
	 */
	LineAttributes getLineAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Needle#getLineAttributes
	 * <em>Line Attributes</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Line Attributes</em>' containment
	 *              reference.
	 * @see #getLineAttributes()
	 * @generated
	 */
	void setLineAttributes(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Decorator</b></em>' attribute. The literals
	 * are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.LineDecorator}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specify the
	 * line decorator for the needle. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Decorator</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
	 * @see #isSetDecorator()
	 * @see #unsetDecorator()
	 * @see #setDecorator(LineDecorator)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getNeedle_Decorator()
	 * @model unsettable="true" required="true" extendedMetaData="kind='element'
	 *        name='Decorator'"
	 * @generated
	 */
	LineDecorator getDecorator();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Needle#getDecorator
	 * <em>Decorator</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Decorator</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
	 * @see #isSetDecorator()
	 * @see #unsetDecorator()
	 * @see #getDecorator()
	 * @generated
	 */
	void setDecorator(LineDecorator value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Needle#getDecorator
	 * <em>Decorator</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetDecorator()
	 * @see #getDecorator()
	 * @see #setDecorator(LineDecorator)
	 * @generated
	 */
	void unsetDecorator();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Needle#getDecorator
	 * <em>Decorator</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Decorator</em>' attribute is set.
	 * @see #unsetDecorator()
	 * @see #getDecorator()
	 * @see #setDecorator(LineDecorator)
	 * @generated
	 */
	boolean isSetDecorator();

	/**
	 * @generated
	 */
	Needle copyInstance();

} // Needle
