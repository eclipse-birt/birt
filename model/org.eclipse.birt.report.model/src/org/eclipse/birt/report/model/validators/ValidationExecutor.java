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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
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

	private Module module;

	/**
	 * Constructs the validation executor with one opened report.
	 * 
	 * @param module
	 *            the report design containing this validation executor
	 */

	public ValidationExecutor( Module module )
	{
		this.module = module;
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
		List exceptionList = new ArrayList( );

		Iterator iter = reorganize( nodes ).iterator( );
		while ( iter.hasNext( ) )
		{
			ValidationNode node = (ValidationNode) iter.next( );

			List errors = node.perform( module, false );
			if ( targetElement == node.getElement( ) )
				exceptionList.addAll( errors );

			// If error is found in one pre-requisite validator, the following
			// validation is not performed. This is because some of the
			// following validators will depend on this pre-requisite validator.
			// Currently, the pre-requisite validator is not allowed to depend
			// on other validator.

			if ( node.getTriggerDefn( ).isPreRequisite( ) && !errors.isEmpty( ) )
				break;
		}

		return exceptionList;
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

			List errors = node.perform( module, sendEvent );

			allErrors.addAll( errors );

			// If error is found in one pre-requisite validator, the following
			// validation is not performed. This is because some of the
			// following validators will depend on this pre-requisite validator.
			// Currently, the pre-requisite validator is not allowed to depend
			// on other validator.

			if ( node.getTriggerDefn( ).isPreRequisite( ) && !errors.isEmpty( ) )
				break;
		}

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

		Set validationIDs = new HashSet( );
		while ( iter.hasNext( ) )
		{
			ValidationNode node = (ValidationNode) iter.next( );
			String id = node.getTriggerDefn( ).getValidationID( );

			if ( !validationIDs.contains( id ) )
			{
				validationIDs.add( id );

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

		if ( validatorDefns == null || validatorDefns.isEmpty( ) )
			return nodes;

		Iterator iter = validatorDefns.iterator( );
		while ( iter.hasNext( ) )
		{
			SemanticTriggerDefn triggerDefn = (SemanticTriggerDefn) iter.next( );
			String targetName = triggerDefn.getTargetElement( );

			if ( StringUtil.isBlank( targetName ) )
			{
				nodes.add( new ValidationNode( element, triggerDefn ) );
				continue;
			}

			// if the target name is not empty, check its element definition

			ElementDefn targetDefn = (ElementDefn) MetaDataDictionary
					.getInstance( ).getElement( targetName );
			ElementDefn elementDefn = (ElementDefn) element.getDefn( );

			if ( elementDefn.isKindOf( targetDefn ) || !onlyOnSelf )
				nodes.add( new ValidationNode( element, triggerDefn ) );

		}

		return nodes;
	}

}