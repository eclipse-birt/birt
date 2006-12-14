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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.xml.sax.SAXException;

/**
 * Parses the "encrypted-property" tag. The tag may give the property value of
 * the element or the member of the structure.
 */

public class EncryptedPropertyState extends PropertyState
{

	EncryptedPropertyState( ModuleParserHandler theHandler,
			DesignElement element )
	{
		super( theHandler, element );
	}

	EncryptedPropertyState( ModuleParserHandler theHandler,
			DesignElement element, PropertyDefn propDefn, IStructure struct )
	{
		super( theHandler, element );

		this.propDefn = propDefn;
		this.struct = struct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		String value = text.toString( );

		PropertyDefn propDefn = null;
		if ( struct != null )
		{
			StructureDefn structDefn = (StructureDefn) struct.getDefn( );
			assert structDefn != null;

			propDefn = (StructPropertyDefn) structDefn.getMember( name );
		}
		else
		{
			propDefn = element.getPropertyDefn( name );
		}

		if ( propDefn == null )
		{
			DesignParserException e = new DesignParserException(
					new String[]{name},
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY );
			RecoverableError.dealUndefinedProperty( handler, e );

			valid = false;
			return;
		}

		if ( !propDefn.isEncryptable( ) )
		{
			DesignParserException e = new DesignParserException(
					new String[]{propDefn.getName( )},
					DesignParserException.DESIGN_EXCEPTION_PROPERTY_IS_NOT_ENCRYPTABLE );
			handler.getErrorHandler( ).semanticError( e );
			valid = false;
			return;
		}

		String valueToSet = StringUtil.trimString( value );
		if ( null == valueToSet )
			return;

		IEncryptionHelper helper = MetaDataDictionary.getInstance( )
				.getEncryptionHelper( );
		valueToSet = helper.decrypt( valueToSet );

		if ( struct != null )
		{
			doSetMember( struct, propDefn.getName( ),
					(StructPropertyDefn) propDefn, valueToSet );
			return;
		}

		doSetProperty( propDefn, valueToSet );
	}
}
