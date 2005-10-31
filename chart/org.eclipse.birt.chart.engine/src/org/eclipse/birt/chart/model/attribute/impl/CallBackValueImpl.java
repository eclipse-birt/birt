/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.CallBackValue;
import org.eclipse.birt.chart.model.interactivity.ICallBackAction;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Call Back Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class CallBackValueImpl extends ActionValueImpl implements CallBackValue
{

	private ICallBackAction action = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected CallBackValueImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.eINSTANCE.getCallBackValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.CallBackValue#setCallBackAction(org.eclipse.birt.chart.model.interactivity.ICallBackAction)
	 */
	public void setCallBackAction( ICallBackAction action )
	{
		this.action = action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.CallBackValue#getCallBackAction()
	 */
	public ICallBackAction getCallBackAction( )
	{
		return action;
	}

	/**
	 * @param action
	 * @return
	 */
	public static CallBackValue create( ICallBackAction action )
	{
		CallBackValue cv = AttributeFactory.eINSTANCE.createCallBackValue( );
		cv.setCallBackAction( action );
		return cv;
	}

} // CallBackValueImpl
