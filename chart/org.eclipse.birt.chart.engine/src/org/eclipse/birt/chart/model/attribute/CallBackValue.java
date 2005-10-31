/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.birt.chart.model.interactivity.ICallBackAction;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Call Back Value</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type defines the value for a 'CallBack' action.
 * 
 * <!-- end-model-doc -->
 * 
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getCallBackValue()
 * @model extendedMetaData="name='CallBackValue' kind='empty'"
 * @generated
 */
public interface CallBackValue extends ActionValue
{

	/**
	 * Sets the callback action instance.
	 * 
	 * @param action
	 */
	void setCallBackAction( ICallBackAction action );

	/**
	 * Returns the callback action instance.
	 * 
	 * @return
	 */
	ICallBackAction getCallBackAction( );

} // CallBackValue
