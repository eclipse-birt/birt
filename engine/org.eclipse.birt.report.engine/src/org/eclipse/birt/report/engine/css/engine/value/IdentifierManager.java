/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Apache - initial API and implementation
 *  Actuate Corporation - changed by Actuate
 *******************************************************************************/
/*

   Copyright 1999-2003  The Apache Software Foundation 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package org.eclipse.birt.report.engine.css.engine.value;

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class provides a manager for the property with support for identifier
 * values.
 * 
 * @version $Id: IdentifierManager.java,v 1.2 2005/11/21 08:22:49 wyan Exp $
 */
public abstract class IdentifierManager extends AbstractValueManager
{

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	public Value createValue( LexicalUnit lu, CSSEngine engine )
			throws DOMException
	{
		switch ( lu.getLexicalUnitType( ) )
		{
			case LexicalUnit.SAC_INHERIT :
				return CSSValueConstants.INHERIT_VALUE;

			case LexicalUnit.SAC_IDENT :
				String s = lu.getStringValue( ).toLowerCase( ).intern( );
				Object v = getIdentifiers( ).get( s );
				if ( v == null )
				{
					throw createInvalidIdentifierDOMException( lu
							.getStringValue( ) );
				}
				return (Value) v;

			default :
				throw createInvalidLexicalUnitDOMException( lu
						.getLexicalUnitType( ) );
		}
	}

	/**
	 * Implements {@link
	 * ValueManager#createStringValue(short,String,CSSEngine)}.
	 */
	public Value createStringValue( short type, String value, CSSEngine engine )
			throws DOMException
	{
		if ( type != CSSPrimitiveValue.CSS_IDENT )
		{
			throw createInvalidStringTypeDOMException( type );
		}
		Object v = getIdentifiers( ).get( value.toLowerCase( ).intern( ) );
		if ( v == null )
		{
			throw createInvalidIdentifierDOMException( value );
		}
		return (Value) v;
	}

	/**
	 * Returns the map that contains the name/value mappings for each possible
	 * identifiers.
	 */
	public abstract StringMap getIdentifiers( );
}
