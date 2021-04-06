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

import org.eclipse.birt.chart.model.data.ScriptExpression;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Script
 * Value</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type defines the value for a 'Invoke_Script' action.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.ScriptValue#getScript
 * <em>Script</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getScriptValue()
 * @model
 * @generated
 */
public interface ScriptValue extends ActionValue {

	/**
	 * Returns the value of the '<em><b>Script</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Script" specifies the actual script string to be invoked.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Script</em>' attribute.
	 * @see #setScript(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getScriptValue_Script()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Script'"
	 * @generated
	 */
	String getScript();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ScriptValue#getScript
	 * <em>Script</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Script</em>' attribute.
	 * @see #getScript()
	 * @generated
	 */
	void setScript(String value);

	/**
	 * @generated
	 */
	ScriptValue copyInstance();

	/*
	 * Get script expression.
	 * 
	 * @return expression the script expression.
	 */
	ScriptExpression getScriptExpression();

	/*
	 * Set script expression.
	 * 
	 * @param expression the script expression.
	 */
	void setScriptExpression(ScriptExpression expression);

} // ScriptValue
