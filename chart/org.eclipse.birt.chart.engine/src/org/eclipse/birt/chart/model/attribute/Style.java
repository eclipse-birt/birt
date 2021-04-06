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
 * '<em><b>Style</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Style provides a holder for all properties that can
 * be styled. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Style#getFont
 * <em>Font</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Style#getColor
 * <em>Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Style#getBackgroundColor
 * <em>Background Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Style#getBackgroundImage
 * <em>Background Image</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Style#getPadding
 * <em>Padding</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyle()
 * @model extendedMetaData="name='Style' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Style extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Font</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Speicifies
	 * the font setting for this style. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Font</em>' containment reference.
	 * @see #setFont(FontDefinition)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyle_Font()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Font'"
	 * @generated
	 */
	FontDefinition getFont();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Style#getFont <em>Font</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Font</em>' containment reference.
	 * @see #getFont()
	 * @generated
	 */
	void setFont(FontDefinition value);

	/**
	 * Returns the value of the '<em><b>Color</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the font color for this style. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Color</em>' containment reference.
	 * @see #setColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyle_Color()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Color'"
	 * @generated
	 */
	ColorDefinition getColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Style#getColor
	 * <em>Color</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Color</em>' containment reference.
	 * @see #getColor()
	 * @generated
	 */
	void setColor(ColorDefinition value);

	/**
	 * Returns the value of the '<em><b>Background Color</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * --> Specifies the background color for this style. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Background Color</em>' containment reference.
	 * @see #setBackgroundColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyle_BackgroundColor()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='BackgroundColor'"
	 * @generated
	 */
	ColorDefinition getBackgroundColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Style#getBackgroundColor
	 * <em>Background Color</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Background Color</em>' containment
	 *              reference.
	 * @see #getBackgroundColor()
	 * @generated
	 */
	void setBackgroundColor(ColorDefinition value);

	/**
	 * Returns the value of the '<em><b>Background Image</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * --> Specifies the background image for this style. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Background Image</em>' containment reference.
	 * @see #setBackgroundImage(Image)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyle_BackgroundImage()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='BackgroundImage'"
	 * @generated
	 */
	Image getBackgroundImage();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Style#getBackgroundImage
	 * <em>Background Image</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Background Image</em>' containment
	 *              reference.
	 * @see #getBackgroundImage()
	 * @generated
	 */
	void setBackgroundImage(Image value);

	/**
	 * Returns the value of the '<em><b>Padding</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * Specifies the padding for this style. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Padding</em>' containment reference.
	 * @see #setPadding(Insets)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyle_Padding()
	 * @model containment="true" extendedMetaData="kind='element' name='Padding'"
	 * @generated
	 */
	Insets getPadding();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Style#getPadding
	 * <em>Padding</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Padding</em>' containment reference.
	 * @see #getPadding()
	 * @generated
	 */
	void setPadding(Insets value);

	/**
	 * @generated
	 */
	Style copyInstance();

} // Style
