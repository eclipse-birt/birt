/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.birt.chart.model.IChartObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Angle3
 * D</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Angle3D represents the angle in 3D coordinate
 * system, it's composed of 3 angle values, each of which coresponds to an axis.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Angle3D#getXAngle
 * <em>XAngle</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Angle3D#getYAngle
 * <em>YAngle</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Angle3D#getZAngle
 * <em>ZAngle</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Angle3D#getType
 * <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAngle3D()
 * @model extendedMetaData="name='Angle3D' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Angle3D extends IChartObject {

	/**
	 * Returns the value of the '<em><b>XAngle</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XAngle</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>XAngle</em>' attribute.
	 * @see #isSetXAngle()
	 * @see #unsetXAngle()
	 * @see #setXAngle(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAngle3D_XAngle()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='XAngle'"
	 * @generated
	 */
	double getXAngle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getXAngle
	 * <em>XAngle</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>XAngle</em>' attribute.
	 * @see #isSetXAngle()
	 * @see #unsetXAngle()
	 * @see #getXAngle()
	 * @generated
	 */
	void setXAngle(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getXAngle
	 * <em>XAngle</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetXAngle()
	 * @see #getXAngle()
	 * @see #setXAngle(double)
	 * @generated
	 */
	void unsetXAngle();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getXAngle
	 * <em>XAngle</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>XAngle</em>' attribute is set.
	 * @see #unsetXAngle()
	 * @see #getXAngle()
	 * @see #setXAngle(double)
	 * @generated
	 */
	boolean isSetXAngle();

	/**
	 * Returns the value of the '<em><b>YAngle</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>YAngle</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>YAngle</em>' attribute.
	 * @see #isSetYAngle()
	 * @see #unsetYAngle()
	 * @see #setYAngle(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAngle3D_YAngle()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='YAngle'"
	 * @generated
	 */
	double getYAngle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getYAngle
	 * <em>YAngle</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>YAngle</em>' attribute.
	 * @see #isSetYAngle()
	 * @see #unsetYAngle()
	 * @see #getYAngle()
	 * @generated
	 */
	void setYAngle(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getYAngle
	 * <em>YAngle</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetYAngle()
	 * @see #getYAngle()
	 * @see #setYAngle(double)
	 * @generated
	 */
	void unsetYAngle();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getYAngle
	 * <em>YAngle</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>YAngle</em>' attribute is set.
	 * @see #unsetYAngle()
	 * @see #getYAngle()
	 * @see #setYAngle(double)
	 * @generated
	 */
	boolean isSetYAngle();

	/**
	 * Returns the value of the '<em><b>ZAngle</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZAngle</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZAngle</em>' attribute.
	 * @see #isSetZAngle()
	 * @see #unsetZAngle()
	 * @see #setZAngle(double)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAngle3D_ZAngle()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='ZAngle'"
	 * @generated
	 */
	double getZAngle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getZAngle
	 * <em>ZAngle</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>ZAngle</em>' attribute.
	 * @see #isSetZAngle()
	 * @see #unsetZAngle()
	 * @see #getZAngle()
	 * @generated
	 */
	void setZAngle(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getZAngle
	 * <em>ZAngle</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetZAngle()
	 * @see #getZAngle()
	 * @see #setZAngle(double)
	 * @generated
	 */
	void unsetZAngle();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getZAngle
	 * <em>ZAngle</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>ZAngle</em>' attribute is set.
	 * @see #unsetZAngle()
	 * @see #getZAngle()
	 * @see #setZAngle(double)
	 * @generated
	 */
	boolean isSetZAngle();

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. The default value
	 * is <code>"None"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.AngleType}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc --> <!-- begin-model-doc --> Attribute "Type" specifies how
	 * will the angle values be used. It can be X, Y, Z or None, which indicate that
	 * the angle value is devoted to representing an angle corresponding to x,y or z
	 * axis or a 3D angle.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.AngleType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(AngleType)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAngle3D_Type()
	 * @model default="None" unsettable="true" required="true"
	 *        extendedMetaData="kind='element' name='Type'"
	 * @generated
	 */
	AngleType getType();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getType
	 * <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.AngleType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	void setType(AngleType value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getType
	 * <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(AngleType)
	 * @generated
	 */
	void unsetType();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getType
	 * <em>Type</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(AngleType)
	 * @generated
	 */
	boolean isSetType();

	/**
	 * Sets the x,y,z angle in one punch.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	void set(double x, double y, double z);

	/**
	 * Returns the specific axis angle value if axis type specified, or just returns
	 * Zero.
	 * 
	 * @return
	 */
	double getAxisAngle();

	/**
	 * @generated
	 */
	Angle3D copyInstance();

} // Angle3D
