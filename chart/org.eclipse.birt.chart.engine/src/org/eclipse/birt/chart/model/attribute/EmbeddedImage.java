/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Embedded
 * Image</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> EmbeddedImage extends type Image to devote itself to
 * representing an embedded image, by which the image data will be encoded into
 * a string.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.EmbeddedImage#getData
 * <em>Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getEmbeddedImage()
 * @model extendedMetaData="name='EmbeddedImage' kind='elementOnly'"
 * @generated
 */
public interface EmbeddedImage extends Image {

	/**
	 * Returns the value of the '<em><b>Data</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The string
	 * attribute "Data" provides the encoded image data for the image. <!--
	 * end-model-doc -->
	 *
	 * @return the value of the '<em>Data</em>' attribute.
	 * @see #setData(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getEmbeddedImage_Data()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Data'"
	 * @generated
	 */
	String getData();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.EmbeddedImage#getData
	 * <em>Data</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Data</em>' attribute.
	 * @see #getData()
	 * @generated
	 */
	void setData(String value);

	/**
	 * @generated
	 */
	@Override
	EmbeddedImage copyInstance();

} // EmbeddedImage
