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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Color
 * Definition</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> ColorDefinition extends the type Fill to devote
 * itself to representing a ARGB color. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getTransparency
 * <em>Transparency</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getRed
 * <em>Red</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getGreen
 * <em>Green</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getBlue
 * <em>Blue</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getColorDefinition()
 * @model extendedMetaData="name='ColorDefinition' kind='elementOnly'"
 * @generated
 */
public interface ColorDefinition extends Fill {

	/**
	 * Returns the value of the '<em><b>Transparency</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the transparency for the color <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * Specifies the transparency for the color.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Transparency</em>' attribute.
	 * @see #isSetTransparency()
	 * @see #unsetTransparency()
	 * @see #setTransparency(int)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getColorDefinition_Transparency()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getTransparency();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getTransparency
	 * <em>Transparency</em>}' attribute. <!-- begin-user-doc --> Sets the
	 * transparency for the color. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Transparency</em>' attribute.
	 * @see #isSetTransparency()
	 * @see #unsetTransparency()
	 * @see #getTransparency()
	 * @generated
	 */
	void setTransparency(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getTransparency
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
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getTransparency
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
	 * Returns the value of the '<em><b>Red</b></em>' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the 'Red' component for the color.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Red</em>' attribute.
	 * @see #isSetRed()
	 * @see #unsetRed()
	 * @see #setRed(int)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getColorDefinition_Red()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.birt.chart.model.attribute.RGBValue"
	 *        required="true"
	 * @generated
	 */
	int getRed();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getRed
	 * <em>Red</em>}' attribute. <!-- begin-user-doc --> Sets the 'Red' component
	 * for the color. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Red</em>' attribute.
	 * @see #isSetRed()
	 * @see #unsetRed()
	 * @see #getRed()
	 * @generated
	 */
	void setRed(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getRed
	 * <em>Red</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetRed()
	 * @see #getRed()
	 * @see #setRed(int)
	 * @generated
	 */
	void unsetRed();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getRed
	 * <em>Red</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Red</em>' attribute is set.
	 * @see #unsetRed()
	 * @see #getRed()
	 * @see #setRed(int)
	 * @generated
	 */
	boolean isSetRed();

	/**
	 * Returns the value of the '<em><b>Blue</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the 'Blue' component for the color.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Blue</em>' attribute.
	 * @see #isSetBlue()
	 * @see #unsetBlue()
	 * @see #setBlue(int)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getColorDefinition_Blue()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.birt.chart.model.attribute.RGBValue"
	 *        required="true" extendedMetaData="kind='element' name='Blue'"
	 * @generated
	 */
	int getBlue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getBlue
	 * <em>Blue</em>}' attribute. <!-- begin-user-doc --> Sets the 'Blue' component
	 * for the color. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Blue</em>' attribute.
	 * @see #isSetBlue()
	 * @see #unsetBlue()
	 * @see #getBlue()
	 * @generated
	 */
	void setBlue(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getBlue
	 * <em>Blue</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetBlue()
	 * @see #getBlue()
	 * @see #setBlue(int)
	 * @generated
	 */
	void unsetBlue();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getBlue
	 * <em>Blue</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Blue</em>' attribute is set.
	 * @see #unsetBlue()
	 * @see #getBlue()
	 * @see #setBlue(int)
	 * @generated
	 */
	boolean isSetBlue();

	/**
	 * Returns the value of the '<em><b>Green</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the 'Green' component for the color.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Green</em>' attribute.
	 * @see #isSetGreen()
	 * @see #unsetGreen()
	 * @see #setGreen(int)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getColorDefinition_Green()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.birt.chart.model.attribute.RGBValue"
	 *        required="true" extendedMetaData="kind='element' name='Green'"
	 * @generated
	 */
	int getGreen();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getGreen
	 * <em>Green</em>}' attribute. <!-- begin-user-doc --> Sets the 'Green'
	 * component for the color. <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Green</em>' attribute.
	 * @see #isSetGreen()
	 * @see #unsetGreen()
	 * @see #getGreen()
	 * @generated
	 */
	void setGreen(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getGreen
	 * <em>Green</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetGreen()
	 * @see #getGreen()
	 * @see #setGreen(int)
	 * @generated
	 */
	void unsetGreen();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getGreen
	 * <em>Green</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Green</em>' attribute is set.
	 * @see #unsetGreen()
	 * @see #getGreen()
	 * @see #setGreen(int)
	 * @generated
	 */
	boolean isSetGreen();

	/**
	 * Convenience method to set all three components for the color.
	 * 
	 * NOTE: Manually written
	 * 
	 * @param iRed
	 * @param iGreen
	 * @param iBlue
	 */
	public void set(int iRed, int iGreen, int iBlue);

	/**
	 * Convenience method to set the three components as well as transparency for
	 * the color. NOTE: Manually written
	 * 
	 * @param iRed
	 * @param iGreen
	 * @param iBlue
	 * @param iAlpha
	 */
	public void set(int iRed, int iGreen, int iBlue, int iAlpha);

	/**
	 * NOTE: Manually written
	 * 
	 * @return An instance of a brighter color relative to this color
	 */
	public ColorDefinition brighter();

	/**
	 * NOTE: Manually written
	 * 
	 * @return An instance of a darker color relative to this color
	 */
	public ColorDefinition darker();

	/**
	 * NOTE: Manually written
	 * 
	 * @return A copy of the existing color but with alpha=127
	 */
	public ColorDefinition translucent();

	/**
	 * NOTE: Manually written
	 * 
	 * @return A copy of the existing color but with alpha=255
	 */
	public ColorDefinition opaque();

	/**
	 * Inverts the existing color (XORed with 0xFF).
	 * 
	 * @return
	 */
	public void invert();

	/**
	 * NOTE: Manually written
	 * 
	 * @return A copy of the existing color but with alpha=0
	 */
	public ColorDefinition transparent();

	/**
	 * @generated
	 */
	ColorDefinition copyInstance();

} // ColorDefinition
