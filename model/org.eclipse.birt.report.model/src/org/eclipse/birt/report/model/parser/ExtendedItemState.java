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

import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses the Extended Item (extended item) tag.
 */

public class ExtendedItemState extends ReportItemState
{

	/**
	 * The extended item being created.
	 */

	public ExtendedItem element;

	/**
	 * Constructs the extended item state with the design parser handler, the
	 * container element and the container slot of the extended item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ExtendedItemState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new ExtendedItem( );
		boolean oldDesign = false;

		String extension = getAttrib( attrs,
				DesignSchemaConstants.EXTENSION_NAME_ATTRIB );

		// check if the design file is old, uses the extension but not
		// extension_name
		if ( StringUtil.isBlank( extension ) )
		{
			extension = getAttrib( attrs,
					DesignSchemaConstants.EXTENSION_ATTRIB );
			oldDesign = true;
		}

		if ( StringUtil.isBlank( extension ) )
		{

			RecoverableError.dealMissingInvalidExtension( handler,
					new SemanticError( element,
							SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION ) );
		}
		else
		{
			MetaDataDictionary dd = MetaDataDictionary.getInstance( );
			ExtensionElementDefn extDefn = (ExtensionElementDefn) dd
					.getExtension( extension );
			if ( extDefn == null )
			{

				RecoverableError
						.dealMissingInvalidExtension(
								handler,
								new SemanticError(
										element,
										new String[]{extension},
										SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND ) );
			}
		}

		// here is for the deprecate property name "extension"
		if ( oldDesign )
			setProperty( ExtendedItem.EXTENSION_PROP, attrs,
					DesignSchemaConstants.EXTENSION_ATTRIB );
		else
			setProperty( ExtendedItem.EXTENSION_PROP, attrs,
					DesignSchemaConstants.EXTENSION_NAME_ATTRIB );

		initElement( attrs );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}
}