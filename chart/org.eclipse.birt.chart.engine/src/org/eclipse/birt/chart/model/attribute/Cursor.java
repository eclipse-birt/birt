/**
 * <copyright>
 * </copyright>
 *
 * $Id: Cursor.java,v 1.6 2009/07/01 01:56:37 ywang1 Exp $
 */

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Cursor</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This type represents a mouse cursor being displayed
 * in chart. When mouse is moving over the hotspot area, the mouse cursor will
 * become the specified cursor. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Cursor#getType
 * <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Cursor#getImage
 * <em>Image</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getCursor()
 * @model extendedMetaData="name='Cursor' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Cursor extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. The literals are
	 * from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.CursorType}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc --> <!-- begin-model-doc --> The type attribute indicates
	 * the cursor type. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.CursorType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(CursorType)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getCursor_Type()
	 * @model unsettable="true" required="true" extendedMetaData="kind='element'
	 *        name='Type'"
	 * @generated
	 */
	CursorType getType();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Cursor#getType <em>Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.CursorType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	void setType(CursorType value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Cursor#getType <em>Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(CursorType)
	 * @generated
	 */
	void unsetType();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Cursor#getType <em>Type</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(CursorType)
	 * @generated
	 */
	boolean isSetType();

	/**
	 * Returns the value of the '<em><b>Image</b></em>' containment reference list.
	 * The list contents are of type
	 * {@link org.eclipse.birt.chart.model.attribute.Image}. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc --> The attribute stores custom
	 * cursor images, it might be image uri or embedded image. <!-- end-model-doc
	 * -->
	 * 
	 * @return the value of the '<em>Image</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getCursor_Image()
	 * @model containment="true" extendedMetaData="kind='element' name='Image'"
	 * @generated
	 */
	EList<Image> getImage();

	/**
	 * @generated
	 */
	Cursor copyInstance();

} // Cursor
