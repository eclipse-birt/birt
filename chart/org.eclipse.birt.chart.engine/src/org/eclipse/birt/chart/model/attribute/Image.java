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
 * '<em><b>Image</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Image extends Fill specialized to represent an
 * image.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Image#getURL
 * <em>URL</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Image#getSource
 * <em>Source</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getImage()
 * @model extendedMetaData="name='Image' kind='elementOnly'"
 * @generated
 */
public interface Image extends Fill {

	/**
	 * Returns the value of the '<em><b>URL</b></em>' attribute. <!-- begin-user-doc
	 * --> Gets the URL for the image. <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the URL for the image.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>URL</em>' attribute.
	 * @see #setURL(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getImage_URL()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	String getURL();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Image#getURL <em>URL</em>}'
	 * attribute. <!-- begin-user-doc --> Sets the URL for the image. <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>URL</em>' attribute.
	 * @see #getURL()
	 * @generated
	 */
	void setURL(String value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' attribute. The literals are
	 * from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.ImageSourceType}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The string
	 * attribute "source" specifies the source of the URL. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Source</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.ImageSourceType
	 * @see #isSetSource()
	 * @see #unsetSource()
	 * @see #setSource(ImageSourceType)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getImage_Source()
	 * @model unsettable="true" required="true" extendedMetaData="kind='element'
	 *        name='source'"
	 * @generated
	 */
	ImageSourceType getSource();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Image#getSource
	 * <em>Source</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Source</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.ImageSourceType
	 * @see #isSetSource()
	 * @see #unsetSource()
	 * @see #getSource()
	 * @generated
	 */
	void setSource(ImageSourceType value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Image#getSource
	 * <em>Source</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetSource()
	 * @see #getSource()
	 * @see #setSource(ImageSourceType)
	 * @generated
	 */
	void unsetSource();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Image#getSource
	 * <em>Source</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Source</em>' attribute is set.
	 * @see #unsetSource()
	 * @see #getSource()
	 * @see #setSource(ImageSourceType)
	 * @generated
	 */
	boolean isSetSource();

	/**
	 * @generated
	 */
	Image copyInstance();

} // Image
