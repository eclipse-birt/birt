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

import java.util.List;

import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.oda.ODAProviderFactory;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses an extended data set. Note: this is temporary syntax, the
 * structure of a data set will be defined by a different team later.
 * 
 */

public class OdaDataSetState extends SimpleDataSetState
{

	/**
	 * Old extension id of flat file in BIRT 1.0 or before.
	 */

	private static final String OBSOLETE_FLAT_FILE_ID = "org.eclipse.birt.report.data.oda.flatfile.dataSet"; //$NON-NLS-1$

	/**
	 * Extension id of flat file in BIRT 2.0.
	 */

	private static final String NEW_FLAT_FILE_ID = "org.eclipse.datatools.connectivity.oda.flatfile.dataSet"; //$NON-NLS-1$

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

	/**
	 * Constructs the data set state with the design parser handler, the
	 * container element and the container slot of the data set.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public OdaDataSetState( ModuleParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
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
			if ( StringUtil.compareVersion( handler.getVersion( ), "3" ) < 0 ) //$NON-NLS-1$
			{
				if ( OBSOLETE_FLAT_FILE_ID.equalsIgnoreCase( extensionID ) )
					extensionID = NEW_FLAT_FILE_ID;
			}

			if ( ODAProviderFactory.getInstance( ).createODAProvider( element,
					extensionID ) == null )
				return;

			if ( !ODAProviderFactory.getInstance( ).createODAProvider( element,
					extensionID ).isValidODADataSetExtensionID( extensionID ) )
			{
				SemanticError e = new SemanticError( element,
						new String[]{extensionID},
						SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND );
				RecoverableError.dealMissingInvalidExtension( handler, e );
			}
		}

		setProperty( IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.SimpleDataSetState#end()
	 */

	public void end( ) throws SAXException
	{
		super.end( );
		
		DesignElement tmpElement = getElement( );		
		doCompatibleDataSetProperty( tmpElement );
		
		TemplateParameterDefinition refTemplateParam = tmpElement
				.getTemplateParameterElement( handler.getModule( ) );
		if ( refTemplateParam == null )
			return;

		doCompatibleDataSetProperty( refTemplateParam.getDefaultElement( ) );
	}

	/**
	 * Copies the value from resultSet to resultSetHints.
	 * 
	 * @param dataSet
	 *            the data set element
	 */

	private void doCompatibleDataSetProperty( DesignElement dataSet )
	{
		if ( dataSet == null )
			return;

		assert dataSet instanceof OdaDataSet;

		if ( ( StringUtil.compareVersion( handler.getVersion( ), "3.2.2" ) < 0 ) ) //$NON-NLS-1$
		{
			List dataSetColumns = (List) dataSet.getProperty( null,
					IDataSetModel.RESULT_SET_PROP );
			Object dataSetHints = dataSet.getProperty( null,
					IDataSetModel.RESULT_SET_HINTS_PROP );
			if ( dataSetHints == null && dataSetColumns != null )
				dataSet
						.setProperty(
								IDataSetModel.RESULT_SET_HINTS_PROP,
								ModelUtil
										.copyValue(
												dataSet
														.getPropertyDefn( IDataSetModel.RESULT_SET_HINTS_PROP ),
												dataSetColumns ) );
		}
	}
}
