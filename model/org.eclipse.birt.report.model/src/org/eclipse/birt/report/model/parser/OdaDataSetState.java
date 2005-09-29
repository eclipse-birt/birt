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
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.oda.ODAManifestUtil;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataSetType;
import org.xml.sax.Attributes;

/**
 * This class parses an extended data set. Note: this is temporary syntax, the
 * structure of a data set will be defined by a different team later.
 * 
 */

public class OdaDataSetState extends DataSetState
{

	/**
	 * Constructs the oda data set with the design file parser handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public OdaDataSetState( ModuleParserHandler handler )
	{
		super( handler );

		element = new OdaDataSet( );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		parseODADataSetExtensionID( attrs, false );

		initElement( attrs, true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */
	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_TAG ) )
		{
			if ( handler.isVersion( "0" ) || handler.isVersion( "1" ) )//$NON-NLS-1$//$NON-NLS-2$
			{
				return new CompatibleOdaDataSetPropertyState( handler,
						getElement( ) );
			}
		}

		return super.startElement( tagName );
	}

	/**
	 * Parse the attribute of "extensionId" for extendable element.
	 * 
	 * @param attrs
	 *            the SAX attributes object
	 * @param extensionNameRequired
	 *            whether extension name is required
	 */

	private void parseODADataSetExtensionID( Attributes attrs,
			boolean extensionNameRequired )
	{
		String extensionID = getAttrib( attrs,
				DesignSchemaConstants.EXTENSION_ID_ATTRIB );

		if ( StringUtil.isBlank( extensionID ) )
		{
			if ( !extensionNameRequired )
				return;

			SemanticError e = new SemanticError( element,
					SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION );
			RecoverableError.dealMissingInvalidExtension( handler, e );
		}
		else
		{
			DataSetType dataSetType = ODAManifestUtil
					.getDataSetExtension( extensionID );

			if ( dataSetType == null )
			{
				SemanticError e = new SemanticError( element,
						new String[]{extensionID},
						SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND );
				RecoverableError.dealMissingInvalidExtension( handler, e );
			}
		}

		setProperty( IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionID );
	}

}