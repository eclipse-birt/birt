/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.SemanticTriggerDefn;

/**
 * Performs one semantic validation on element. It contains the element to
 * validate and the validation trigger.
 */

public class ValidationNode
{

	/**
	 * The element to validate
	 */

	protected DesignElement element;

	/**
	 * The trigger definition for this semantic validation
	 */

	protected SemanticTriggerDefn triggerDefn = null;

	/**
	 * Constructs the validation node.
	 * 
	 * @param element
	 *            the element to validate
	 * @param triggerDefn
	 *            the trigger definition
	 */

	ValidationNode( DesignElement element, SemanticTriggerDefn triggerDefn )
	{
		assert element != null;
		assert triggerDefn != null;

		this.element = element;
		this.triggerDefn = triggerDefn;
	}

	/**
	 * Returns the element to validate
	 * 
	 * @return the element to validate
	 */

	DesignElement getElement( )
	{
		return element;
	}

	/**
	 * Performs the validation of this node.
	 * 
	 * @param design
	 *            the report design
	 * @param sendEvent
	 *            indicates whether to send event is needed
	 * @return error list. Each one is the instance of
	 *         <code>SemanticException</code>.
	 */

	final List perform( ReportDesign design, boolean sendEvent )
	{
		// Locate the target element of validation.

		DesignElement toValidate = element;
		if ( !StringUtil.isBlank( triggerDefn.getTargetElement( ) ) )
		{
			ElementDefn targetDefn = (ElementDefn) MetaDataDictionary
					.getInstance( )
					.getElement( triggerDefn.getTargetElement( ) );
			while ( toValidate != null )
			{
				ElementDefn elementDefn = (ElementDefn) toValidate.getDefn( );
				if ( targetDefn.isKindOf( elementDefn ) )
					break;

				toValidate = toValidate.getContainer( );
			}
		}

		// If the target is not found, no validation is needed. This case is
		// usually for the element which is not added into report.

		if ( toValidate == null )
			return Collections.EMPTY_LIST;

		// Perform validation.

		List errors = null;
		AbstractSemanticValidator validator = triggerDefn.getValidator( );
		if ( validator instanceof AbstractPropertyValidator )
		{
			errors = ( (AbstractPropertyValidator) validator ).validate(
					design, toValidate, triggerDefn.getPropertyName( ) );
		}
		else if ( validator instanceof AbstractElementValidator )
		{
			errors = ( (AbstractElementValidator) validator ).validate( design,
					toValidate );
		}

		assert errors != null;

		// Send validation event if it's needed

		if ( sendEvent )
		{
			List errorDetailList = new ArrayList( );

			Iterator iter = errors.iterator( );
			while ( iter.hasNext( ) )
			{
				SemanticException e = (SemanticException) iter.next( );

				ErrorDetail errorDetail = new ErrorDetail( e );
				errorDetail.setValidationID( triggerDefn.getValidationID( ) );
				errorDetailList.add( errorDetail );
			}

			ValidationEvent event = new ValidationEvent( toValidate,
					triggerDefn.getValidationID( ), errorDetailList );

			design.broadcastValidationEvent( toValidate, event );
		}

		return errors;
	}

	/**
	 * Returns the trigger definition.
	 * 
	 * @return the trigger definition
	 */

	SemanticTriggerDefn getTriggerDefn( )
	{
		return triggerDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString( )
	{
		StringBuffer sb = new StringBuffer( );

		sb.append( "element=" ); //$NON-NLS-1$
		sb.append( element.getElementName( ) );
		sb.append( ", " ); //$NON-NLS-1$
		sb.append( "name=" ); //$NON-NLS-1$
		if ( element != null )
			sb.append( element.getName( ) );
		else
			sb.append( "[null]" ); //$NON-NLS-1$
		sb.append( ", " ); //$NON-NLS-1$
		sb.append( "id=" ); //$NON-NLS-1$
		sb.append( element.toString( ) ); //$NON-NLS-1$

		sb.append( ", " ); //$NON-NLS-1$
		sb.append( "validator=" ); //$NON-NLS-1$
		sb.append( triggerDefn.getValidatorName( ) );

		return sb.toString( );
	}

}