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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.validators.AbstractSemanticValidator;

/**
 * Represents the collection of semantic validation triggers. Each one is the
 * instance of <code>SemanticTriggerDefn</code>.
 */

public class SemanticTriggerDefnSet
{

	/**
	 * List of the definitions for semantic validator applied to this property.
	 */

	protected List triggerList = null;

	/**
	 * Adds the definition for semantic validator.
	 * 
	 * @param validatorDefn
	 *            the definition to add
	 */

	void add( SemanticTriggerDefn validatorDefn )
	{
		if ( triggerList == null )
			triggerList = new ArrayList( );

		triggerList.add( validatorDefn );
	}

	/**
	 * Adds all trigger definition into this trigger collection.
	 * 
	 * @param triggers
	 */

	public void add( SemanticTriggerDefnSet triggers )
	{
		if ( triggers != null && triggers.triggerList != null )
		{
			Iterator iter = triggers.triggerList.iterator( );
			while ( iter.hasNext( ) )
			{
				SemanticTriggerDefn trigger = (SemanticTriggerDefn) iter.next( );

				add( trigger );
			}
		}
	}

	/**
	 * Returns the list of semantic validator's definitions. Each of the list is
	 * the instance of <code>TriggerDefn</code>.
	 * 
	 * @return the list of semantic validator's definitions.
	 */

	public List getTriggerList( )
	{
		return triggerList;
	}

	/**
	 * Builds all semantic validation triggers.
	 * 
	 * @throws MetaDataException
	 *             if the validator is not found.
	 */

	public void build( ) throws MetaDataException
	{
		if ( triggerList != null )
		{
			Iterator iter = triggerList.iterator( );
			while ( iter.hasNext( ) )
			{
				SemanticTriggerDefn validatorDefn = (SemanticTriggerDefn) iter
						.next( );

				if ( validatorDefn.getValidator( ) == null )
				{
					AbstractSemanticValidator validator = MetaDataDictionary
							.getInstance( ).getSemanticValidator(
									validatorDefn.getValidatorName( ) );
					if ( validator == null )
					{
						throw new MetaDataException(
								new String[]{validatorDefn.getValidatorName( )},
								MetaDataException.DESIGN_EXCEPTION_VALIDATOR_NOT_FOUND );
					}

					validatorDefn.setValidator( validator );
				}
			}
		}
	}

}