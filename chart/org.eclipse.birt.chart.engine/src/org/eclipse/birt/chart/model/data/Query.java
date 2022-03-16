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

package org.eclipse.birt.chart.model.data;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Query</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type represents design-time definition for the dataset in a series.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.Query#getDefinition
 * <em>Definition</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.Query#getRules
 * <em>Rules</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.Query#getGrouping
 * <em>Grouping</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getQuery()
 * @model extendedMetaData="name='Query' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Query extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Definition</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Definition</em>' attribute.
	 * @see #setDefinition(String)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getQuery_Definition()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	String getDefinition();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Query#getDefinition
	 * <em>Definition</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Definition</em>' attribute.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition(String value);

	/**
	 * Returns the value of the '<em><b>Rules</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.birt.chart.model.data.Rule}.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Rules</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getQuery_Rules()
	 * @model type="org.eclipse.birt.chart.model.data.Rule" containment="true"
	 *        required="true" extendedMetaData="kind='element' name='Rules'"
	 * @deprecated only reserved for compatibility
	 */
	@Deprecated
	EList<Rule> getRules();

	/**
	 * Returns the value of the '<em><b>Grouping</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Grouping</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Grouping</em>' containment reference.
	 * @see #setGrouping(SeriesGrouping)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getQuery_Grouping()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Grouping'"
	 * @generated
	 */
	SeriesGrouping getGrouping();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Query#getGrouping
	 * <em>Grouping</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Grouping</em>' containment reference.
	 * @see #getGrouping()
	 * @generated
	 */
	void setGrouping(SeriesGrouping value);

	/**
	 *
	 * @return
	 */
	boolean isDefined();

	/**
	 * @generated
	 */
	@Override
	Query copyInstance();

	/*
	 * Get definition expression.
	 *
	 * @return expression the definition expression.
	 */
	ScriptExpression getDefinitionExpression();

	/*
	 * Set definition expression.
	 *
	 * @param expression the definition expression.
	 */
	void setDefinitionExpression(ScriptExpression expression);

} // Query
