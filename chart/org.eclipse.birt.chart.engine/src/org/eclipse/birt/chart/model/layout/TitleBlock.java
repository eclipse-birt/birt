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

package org.eclipse.birt.chart.model.layout;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Title
 * Block</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type defines the Title in a chart.
 *
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getTitleBlock()
 * @model
 * @generated
 */
public interface TitleBlock extends LabelBlock {

	/**
	 * Returns the value of the '<em><b>Auto</b></em>' attribute. The default value
	 * is <code>"false"</code>. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc --> Attribute "Auto" of type boolean specifies whether the
	 * text of the TitleBlock could be automatically determined by the system. <!--
	 * end-model-doc -->
	 *
	 * @return the value of the '<em>Auto</em>' attribute.
	 * @see #isSetAuto()
	 * @see #unsetAuto()
	 * @see #setAuto(boolean)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getTitleBlock_Auto()
	 * @model default="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='Auto'"
	 * @generated
	 */
	boolean isAuto();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.TitleBlock#isAuto <em>Auto</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Auto</em>' attribute.
	 * @see #isSetAuto()
	 * @see #unsetAuto()
	 * @see #isAuto()
	 * @generated
	 */
	void setAuto(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.TitleBlock#isAuto <em>Auto</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetAuto()
	 * @see #isAuto()
	 * @see #setAuto(boolean)
	 * @generated
	 */
	void unsetAuto();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.TitleBlock#isAuto <em>Auto</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return whether the value of the '<em>Auto</em>' attribute is set.
	 * @see #unsetAuto()
	 * @see #isAuto()
	 * @see #setAuto(boolean)
	 * @generated
	 */
	boolean isSetAuto();

	/**
	 * @generated
	 */
	@Override
	TitleBlock copyInstance();

} // TitleBlock
