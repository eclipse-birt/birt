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

import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.oda.ODAProvider;
import org.eclipse.birt.report.model.extension.oda.ODAProviderFactory;
import org.eclipse.birt.report.model.extension.oda.OdaDummyProvider;
import org.eclipse.birt.report.model.parser.OdaDataSourceState.DummyPropertyState;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
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
	 * <code>true</code> if the extension can be found. Otherwise
	 * <code>false</code>.
	 */

	private boolean isValidExtensionId = true;

	/**
	 * The dummy provider of the element.
	 */

	private OdaDummyProvider provider = null;

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
			if ( handler.isVersion( VersionUtil.VERSION_0 )
					|| handler.isVersion( VersionUtil.VERSION_1_0_0 ) )
			{
				return new CompatibleOdaDataSetPropertyState( handler,
						getElement( ) );
			}
		}

		// if the extension id is OK, use normal procedure to parse the design
		// file. Otherwise, use dummy state to parse.

		if ( isValidExtensionId )
			return super.startElement( tagName );

		return startDummyElement( tagName );

	}

	/**
	 * Parses dummy properties. Do not apply any validation procedure.
	 * 
	 * 
	 */

	protected AbstractParseState startDummyElement( String tagName )
	{
		if ( DesignSchemaConstants.PROPERTY_TAG.equalsIgnoreCase( tagName )
				|| DesignSchemaConstants.XML_PROPERTY_TAG
						.equalsIgnoreCase( tagName )
				|| DesignSchemaConstants.METHOD_TAG.equalsIgnoreCase( tagName )
				|| DesignSchemaConstants.EXPRESSION_TAG
						.equalsIgnoreCase( tagName ) )
			return new DummyPropertyState( handler, getElement( ), provider );

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
			return;
		}
		if ( handler.versionNumber < VersionUtil.VERSION_3_0_0 )
		{
			if ( OBSOLETE_FLAT_FILE_ID.equalsIgnoreCase( extensionID ) )
				extensionID = NEW_FLAT_FILE_ID;
		}

		ODAProvider tmpProvider = ODAProviderFactory.getInstance( )
				.createODAProvider( element, extensionID );

		if ( tmpProvider == null )
			return;

		if ( !tmpProvider.isValidODADataSetExtensionID( extensionID ) )
		{
			SemanticError e = new SemanticError( element,
					new String[]{extensionID},
					SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND );
			RecoverableError.dealMissingInvalidExtension( handler, e );
			isValidExtensionId = false;
		}

		setProperty( IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionID );

		if ( !isValidExtensionId )
			provider = (OdaDummyProvider) ( (OdaDataSet) element )
					.getProvider( );
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
		mergeResultSetAndResultSetHints( (OdaDataSet) tmpElement );

		TemplateParameterDefinition refTemplateParam = tmpElement
				.getTemplateParameterElement( handler.getModule( ) );
		if ( refTemplateParam == null )
			return;

		doCompatibleDataSetProperty( refTemplateParam.getDefaultElement( ) );

		mergeResultSetAndResultSetHints( (OdaDataSet) refTemplateParam
				.getDefaultElement( ) );
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

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_2 )
		{
			List dataSetColumns = (List) dataSet.getLocalProperty(
					handler.module, IDataSetModel.RESULT_SET_PROP );
			Object dataSetHints = dataSet.getLocalProperty( handler.module,
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

	/**
	 * Parses the old resultSets and resultSetHints list to the new resultSets
	 * list.
	 * <p>
	 * resultSetsHints maps to new result set name. resultSet maps to new result
	 * set native name.
	 * <p>
	 * The conversion is done from the file version 3.2.5. It is a part of
	 * automatic conversion for BIRT 2.1.1.
	 * 
	 * @param resultSets
	 *            the result sets
	 * @param resultSetHints
	 *            the result set hints
	 */

	private void mergeResultSetAndResultSetHints( OdaDataSet dataSet )
	{
		if ( handler.versionNumber >= VersionUtil.VERSION_3_2_6
				|| handler.versionNumber < VersionUtil.VERSION_3_2_2 )
		{
			return;
		}

		List resultSets = (List) dataSet.getLocalProperty( handler.module,
				IDataSetModel.RESULT_SET_PROP );
		List resultSetHints = (List) dataSet.getLocalProperty( handler.module,
				IDataSetModel.RESULT_SET_HINTS_PROP );

		if ( resultSetHints == null )
		{
			updateOdaResultSetColumn( resultSets );
			return;
		}

		for ( int i = 0; i < resultSetHints.size( ); i++ )
		{
			ResultSetColumn hint = (ResultSetColumn) resultSetHints.get( i );

			// use both position and name to match, this can avoid position was
			// not matched and the column name existed already.

			OdaResultSetColumn currentColumn = findResultSet( resultSets, hint
					.getColumnName( ), hint.getPosition( ) );
			if ( currentColumn == null )
			{
				currentColumn = convertResultSetColumnToOdaResultSetColumn( hint );
				resultSets.add( currentColumn );
			}
			else
			{
				String nativeName = currentColumn.getColumnName( );
				String columnName = hint.getColumnName( );

				currentColumn.setColumnName( columnName );
				currentColumn.setNativeName( nativeName );

				// already in the list, do not add again then.

				if ( currentColumn.getDataType( ) == null )
					currentColumn.setDataType( hint.getDataType( ) );

				if ( currentColumn.getNativeDataType( ) == null )
					currentColumn.setNativeDataType( hint.getNativeDataType( ) );

				if ( currentColumn.getNativeName( ) == null )
					currentColumn
							.setNativeName( currentColumn.getColumnName( ) );

				if ( currentColumn.getColumnName( ) == null )
					currentColumn
							.setColumnName( currentColumn.getNativeName( ) );
			}

		}
	}

	/**
	 * Returns the result set column in the given position.
	 * 
	 * @param pos
	 *            the position
	 * @return the matched result set column
	 */

	private static OdaResultSetColumn findResultSet( List resultSets,
			String columnName, Integer pos )
	{
		for ( int i = 0; i < resultSets.size( ); i++ )
		{
			OdaResultSetColumn setColumn = (OdaResultSetColumn) resultSets
					.get( i );

			// position is the first preference. column name is the second.

			if ( ( pos != null && pos.equals( setColumn.getPosition( ) ) )
					|| ( columnName != null && columnName.equals( setColumn
							.getColumnName( ) ) ) )
				return setColumn;
		}
		return null;
	}

	/**
	 * Returns a OdaResultSetColumn that maps from ResultSetColumn.
	 * 
	 * @param oldColumn
	 *            the result set column to convert
	 * @return the new OdaResultSetColumn
	 */

	private static OdaResultSetColumn convertResultSetColumnToOdaResultSetColumn(
			ResultSetColumn oldColumn )
	{
		assert oldColumn != null;

		OdaResultSetColumn newColumn = StructureFactory
				.createOdaResultSetColumn( );
		newColumn.setColumnName( oldColumn.getColumnName( ) );
		newColumn.setDataType( oldColumn.getDataType( ) );
		newColumn.setNativeDataType( oldColumn.getNativeDataType( ) );

		// in default, native name is equal to name

		newColumn.setNativeName( oldColumn.getColumnName( ) );

		newColumn.setPosition( oldColumn.getPosition( ) );
		return newColumn;
	}

	/**
	 * Updates the native name in the oda result set columns
	 * 
	 * @param resultSets
	 *            a list containing ODA result set columns
	 */

	private static void updateOdaResultSetColumn( List resultSets )
	{
		if ( resultSets == null )
			return;

		for ( int i = 0; i < resultSets.size( ); i++ )
		{
			OdaResultSetColumn newColumn = (OdaResultSetColumn) resultSets
					.get( i );
			newColumn.setNativeName( newColumn.getColumnName( ) );
		}
	}
}
