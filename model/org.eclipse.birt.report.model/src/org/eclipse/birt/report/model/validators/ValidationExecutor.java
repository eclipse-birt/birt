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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.SemanticTriggerDefn;
import org.eclipse.birt.report.model.metadata.SemanticTriggerDefnSet;

/**
 * Represents the validation executor, which executes validation over a
 * validation node list.
 */

public class ValidationExecutor
{

	/**
	 * The report design which is associated with this validation executor.
	 */

	private ReportDesign design;

	/**
	 * Constructs the validation executor with one opened report.
	 * 
	 * @param design
	 *            the report design containing this validation executor
	 */

	public ValidationExecutor( ReportDesign design )
	{
		this.design = design;
	}

	/**
	 * Performs all validation in the given validation node list. Each of the
	 * list is the instance of <code>ValidationNode</code>. This method is
	 * used for element's semantic check.
	 * 
	 * @param targetElement
	 *            the target element on which the validation is performed.
	 * @param nodes
	 *            list of validation nodes
	 * @return error list. Each one is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List perform( DesignElement targetElement, List nodes )
	{
		List allErrors = new ArrayList( );

		Iterator iter = reorganize( nodes ).iterator( );
		while ( iter.hasNext( ) )
		{
			ValidationNode node = (ValidationNode) iter.next( );

			List errors = node.perform( design, false );
			if ( targetElement == node.getElement( ) )
				allErrors.addAll( errors );

			// If error is found in one pre-requisite validator, the following
			// validation is not performed. This is because some of the
			// following validators will depend on this pre-requisite validator.
			// Currently, the pre-requisite validator is not allowed to depend
			// on other validator.

			if ( node.getTriggerDefn( ).isPreRequisite( ) && !errors.isEmpty( ) )
				break;
		}

		List errorDetailList = ErrorDetail.convertExceptionList( allErrors );

		if ( !MetaDataDictionary.getInstance( ).useValidationTrigger( ) )
		{
			ValidationEvent event = new ValidationEvent( targetElement, null,
					errorDetailList );

			targetElement.broadcast( event );
		}

		return allErrors;
	}

	/**
	 * Performs all validation in the given validation node list. Each of the
	 * list is the instance of <code>ValidationNode</code>.
	 * 
	 * @param nodes
	 *            list of validation nodes
	 * @param sendEvent
	 *            indicates whether it is needed to send event
	 * @return error list. Each one is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List perform( List nodes, boolean sendEvent )
	{
		List allErrors = new ArrayList( );

		Iterator iter = reorganize( nodes ).iterator( );
		while ( iter.hasNext( ) )
		{
			ValidationNode node = (ValidationNode) iter.next( );

			List errors = node.perform( design, sendEvent );

			allErrors.addAll( errors );

			// If error is found in one pre-requisite validator, the following
			// validation is not performed. This is because some of the
			// following validators will depend on this pre-requisite validator.
			// Currently, the pre-requisite validator is not allowed to depend
			// on other validator.

			if ( node.getTriggerDefn( ).isPreRequisite( ) && !errors.isEmpty( ) )
				break;
		}

//		Iterator iterElement = elementErrorMap.keySet( ).iterator( );
//		while ( iterElement.hasNext( ) )
//		{
//			DesignElement toValidate = (DesignElement) iterElement.next( );
//
//			List errors = (List) elementErrorMap.get( toValidate );
//			allErrors.addAll( errors );
//
//			List errorDetailList = ErrorDetail.convertExceptionList( errors );
//
//			if ( !MetaDataDictionary.getInstance( ).useValidationTrigger( ) )
//			{
//				ValidationEvent event = new ValidationEvent( toValidate, null,
//						errorDetailList );
//
//				toValidate.broadcast( event );
//			}
//		}

		return allErrors;
	}

	/**
	 * Reorganizes the nodes in the following aspects in order to improve the
	 * efficiency:
	 * <ul>
	 * <li>The duplicate validation is removed from the node list.
	 * <li>The pre-requisite validator is shifted to the beginning of the node
	 * list.
	 * </ul>
	 * 
	 * @param nodes
	 *            the validation nodes to reorganize
	 * @return the reorganized nodes
	 */

	private List reorganize( List nodes )
	{
		List newList = new ArrayList( );

		Iterator iter = nodes.iterator( );
		while ( iter.hasNext( ) )
		{
			ValidationNode node = (ValidationNode) iter.next( );

			boolean found = false;
			int count = newList.size( );
			for ( int i = 0; i < count; i++ )
			{
				ValidationNode nodeInList = (ValidationNode) newList.get( i );

				// The validation is just done once on the same element with the
				// same validator.

				String validationID1 = node.getTriggerDefn( ).getValidationID( );
				String validationID2 = nodeInList.getTriggerDefn( )
						.getValidationID( );

				if ( StringUtil.isEqual( validationID1, validationID2 ) )
				{
					found = true;
					break;
				}
			}

			if ( !found )
			{
				if ( node.getTriggerDefn( ).isPreRequisite( ) )
					newList.add( 0, node );
				else
					newList.add( node );
			}
		}

		return newList;
	}

	/**
	 * Returns the list of validation nodes.
	 * 
	 * @param element
	 *            the element to validate
	 * @param triggers
	 *            the validation triggers
	 * @param onlyOnSelf
	 *            whether the validator is applied on the given element itself
	 * @return the list of validation nodes
	 */

	public static List getValidationNodes( DesignElement element,
			SemanticTriggerDefnSet triggers, boolean onlyOnSelf )
	{
		List nodes = new ArrayList( );

		List validatorDefns = triggers.getTriggerList( );
		if ( validatorDefns != null && !validatorDefns.isEmpty( ) )
		{
			Iterator iter = validatorDefns.iterator( );
			while ( iter.hasNext( ) )
			{
				SemanticTriggerDefn triggerDefn = (SemanticTriggerDefn) iter
						.next( );
				String targetName = triggerDefn.getTargetElement( );

				if ( StringUtil.isBlank( targetName ) )
				{
					ValidationNode node = new ValidationNode( element,
							triggerDefn );
					nodes.add( node );
				}
				else
				{
					ElementDefn targetDefn = (ElementDefn) MetaDataDictionary
							.getInstance( ).getElement( targetName );
					ElementDefn elementDefn = (ElementDefn) element.getDefn( );

					if ( targetDefn.isKindOf( elementDefn ) || !onlyOnSelf )
					{
						ValidationNode node = new ValidationNode( element,
								triggerDefn );
						nodes.add( node );
					}
				}
			}
		}

		return nodes;
	}

}