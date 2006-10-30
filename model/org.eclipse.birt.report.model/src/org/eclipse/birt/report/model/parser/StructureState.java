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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.elements.structures.JoinCondition;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.ParameterFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.ScriptLib;
import org.eclipse.birt.report.model.api.elements.structures.SearchKey;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses one structure. The structure can be either a top level structure on an
 * element or a structure in a list.
 * 
 */

public class StructureState extends AbstractPropertyState
{

	/**
	 * The list property value if this state is used to parse one structure in
	 * list.
	 */

	ArrayList list = null;

	/**
	 * The definition of the list property which this structure is in.
	 */

	PropertyDefn propDefn = null;

	/**
	 * The dictionary for structure class and name mapping.
	 */

	private static Map structDict = null;

	/**
	 * The structure which holds this property as a member.
	 */

	protected IStructure parentStruct = null;

	/**
	 * Constructs the state of the structure which is one property.
	 * 
	 * @param theHandler
	 *            the design parser handler
	 * @param element
	 *            the element holding this structure to parse
	 */

	StructureState( ModuleParserHandler theHandler, DesignElement element )
	{
		super( theHandler, element );
	}

	/**
	 * Constructs the state of the structure which is in one structure list.
	 * 
	 * @param theHandler
	 *            the design parser handler
	 * @param element
	 *            the element holding this structure
	 * @param propDefn
	 *            the definition of the property which holds this structure
	 * @param theList
	 *            the structure list
	 */

	StructureState( ModuleParserHandler theHandler, DesignElement element,
			PropertyDefn propDefn, ArrayList theList )
	{
		super( theHandler, element );

		assert propDefn != null;
		assert theList != null;

		this.propDefn = propDefn;
		this.list = theList;
	}

	/**
	 * Constructs the state of the structure which is in one structure list.
	 * 
	 * @param theHandler
	 *            the design parser handler
	 * @param element
	 *            the element holding this structure
	 * @param propDefn
	 *            the definition of the property which holds this structure
	 * @param parentStruct
	 *            the structure that contains the current structure
	 */

	StructureState( ModuleParserHandler theHandler, DesignElement element,
			PropertyDefn propDefn, IStructure parentStruct )
	{
		super( theHandler, element );

		assert propDefn != null;

		this.propDefn = propDefn;
		this.parentStruct = parentStruct;
	}

	protected void setName( String name )
	{
		super.setName( name );

		propDefn = element.getPropertyDefn( name );

		if ( struct == null )
		{
			assert propDefn != null;

			// If the structure has its specific state, the structure will be
			// created by the specific state.

			struct = createStructure( (StructureDefn) propDefn.getStructDefn( ) );

			Structure.StructureContext structContext = new Structure.StructureContext(
					element, propDefn.getName( ) );
			( (Structure) struct ).setContext( structContext );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		if ( name == null )
			name = getAttrib( attrs, DesignSchemaConstants.NAME_ATTRIB );

		if ( list == null )
		{
			if ( StringUtil.isBlank( name ) )
			{
				handler
						.getErrorHandler( )
						.semanticError(
								new DesignParserException(
										DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				valid = false;
				return;
			}

			propDefn = element.getPropertyDefn( name );
			if ( propDefn == null )
			{
				handler
						.getErrorHandler( )
						.semanticError(
								new DesignParserException(
										DesignParserException.DESIGN_EXCEPTION_INVALID_STRUCTURE_NAME ) );
				valid = false;
				return;
			}
		}

		if ( struct == null )
		{
			assert propDefn != null;

			// If the structure has its specific state, the structure will be
			// created by the specific state.

			struct = createStructure( (StructureDefn) propDefn.getStructDefn( ) );

			Structure.StructureContext structContext = new Structure.StructureContext(
					element, propDefn.getName( ) );
			( (Structure) struct ).setContext( structContext );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_TAG ) )
			return new PropertyState( handler, element, propDefn, struct );

		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.ENCRYPTED_PROPERTY_TAG ) )
			return new EncryptedPropertyState( handler, element, propDefn,
					struct );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.EXPRESSION_TAG ) )
			return new ExpressionState( handler, element, propDefn, struct );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.XML_PROPERTY_TAG ) )
			return new XmlPropertyState( handler, element, propDefn, struct );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LIST_PROPERTY_TAG ) )
			return new ListPropertyState( handler, element, propDefn, struct );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_PROPERTY_TAG ) )
			return new TextPropertyState( handler, element, struct );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HTML_PROPERTY_TAG ) )
			return new TextPropertyState( handler, element, struct );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STRUCTURE_TAG ) )
			return new StructureState( handler, element, propDefn, struct );

		return super.startElement( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		if ( struct != null )
		{
			if ( parentStruct != null )
			{
				parentStruct.setProperty( propDefn, struct );
			}
			else if ( list != null )
			{
				// structure in a list property.

				list.add( struct );
			}
			else
			{
				// structure property.

				element.setProperty( name, struct );
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#generalJumpTo()
	 */

	protected AbstractParseState generalJumpTo( )
	{
		if ( element instanceof Label
				&& Label.ACTION_PROP.equalsIgnoreCase( name )
				|| element instanceof ImageItem
				&& ImageItem.ACTION_PROP.equalsIgnoreCase( name )
				|| element instanceof DataItem
				&& DataItem.ACTION_PROP.equalsIgnoreCase( name ) )
		{
			ActionStructureState state = new ActionStructureState( handler,
					element );
			state.setName( name );
			return state;
		}

		String propName = propDefn == null ? null : propDefn.getName( );

		if ( ( element instanceof DataSet ) )
		{
			if ( DataSet.COMPUTED_COLUMNS_PROP.equalsIgnoreCase( propName ) )
			{
				CompatibleComputedColumnStructureState state = new CompatibleComputedColumnStructureState(
						handler, element, propDefn, list );
				state.setName( propName );

				return state;
			}
		}
		return super.generalJumpTo( );
	}

	/**
	 * Creates structure instance given the structure name.
	 * 
	 * @param structDefn
	 *            the definition of the structure to create
	 * @return the structure instance created.
	 */

	static IStructure createStructure( StructureDefn structDefn )
	{
		synchronized ( StructureState.class )
		{
			if ( structDict == null )
				populateStructDict( );
		}
		assert structDefn != null;
		assert structDict != null;

		IStructure struct = null;

		try
		{
			Class c = (Class) structDict.get( structDefn.getName( )
					.toLowerCase( ) );
			assert c != null;

			struct = (IStructure) c.newInstance( );
		}
		catch ( InstantiationException e )
		{
			assert false;
		}
		catch ( IllegalAccessException e )
		{
			assert false;
		}

		return struct;
	}

	/**
	 * Populates the dictionary for the structure class and name mapping.
	 * 
	 */

	private static void populateStructDict( )
	{
		assert structDict == null;

		structDict = new HashMap( );

		structDict.put( Action.ACTION_STRUCT.toLowerCase( ), Action.class );

		structDict.put( ColumnHint.COLUMN_HINT_STRUCT.toLowerCase( ),
				ColumnHint.class );

		structDict.put( ComputedColumn.COMPUTED_COLUMN_STRUCT.toLowerCase( ),
				ComputedColumn.class );

		structDict.put( ConfigVariable.CONFIG_VAR_STRUCT.toLowerCase( ),
				ConfigVariable.class );

		structDict.put( CustomColor.CUSTOM_COLOR_STRUCT.toLowerCase( ),
				CustomColor.class );

		structDict.put( EmbeddedImage.EMBEDDED_IMAGE_STRUCT.toLowerCase( ),
				EmbeddedImage.class );

		structDict.put( FilterCondition.FILTER_COND_STRUCT.toLowerCase( ),
				FilterCondition.class );

		structDict.put( HideRule.STRUCTURE_NAME.toLowerCase( ), HideRule.class );

		structDict.put( HighlightRule.STRUCTURE_NAME.toLowerCase( ),
				HighlightRule.class );

		structDict.put( IncludedLibrary.INCLUDED_LIBRARY_STRUCT.toLowerCase( ),
				IncludedLibrary.class );

		structDict.put( IncludeScript.INCLUDE_SCRIPT_STRUCT.toLowerCase( ),
				IncludeScript.class );

		structDict.put( DataSetParameter.STRUCT_NAME.toLowerCase( ),
				DataSetParameter.class );

		structDict.put( OdaDataSetParameter.STRUCT_NAME.toLowerCase( ),
				OdaDataSetParameter.class );

		structDict.put( MapRule.STRUCTURE_NAME.toLowerCase( ), MapRule.class );

		structDict.put( ParamBinding.PARAM_BINDING_STRUCT.toLowerCase( ),
				ParamBinding.class );

		structDict.put( PropertyMask.STRUCTURE_NAME.toLowerCase( ),
				PropertyMask.class );

		structDict.put(
				ResultSetColumn.RESULT_SET_COLUMN_STRUCT.toLowerCase( ),
				ResultSetColumn.class );

		structDict.put( SearchKey.SEARCHKEY_STRUCT.toLowerCase( ),
				SearchKey.class );

		structDict.put( SelectionChoice.STRUCTURE_NAME.toLowerCase( ),
				SelectionChoice.class );

		structDict.put( SortKey.SORT_STRUCT.toLowerCase( ), SortKey.class );

		structDict.put( CachedMetaData.CACHED_METADATA_STRUCT.toLowerCase( ),
				CachedMetaData.class );

		structDict.put( StringFormatValue.FORMAT_VALUE_STRUCT.toLowerCase( ),
				StringFormatValue.class );

		structDict.put( NumberFormatValue.FORMAT_VALUE_STRUCT.toLowerCase( ),
				NumberFormatValue.class );

		structDict.put( DateTimeFormatValue.FORMAT_VALUE_STRUCT.toLowerCase( ),
				DateTimeFormatValue.class );

		structDict.put(
				ParameterFormatValue.FORMAT_VALUE_STRUCT.toLowerCase( ),
				ParameterFormatValue.class );

		structDict.put( PropertyBinding.PROPERTY_BINDING_STRUCT.toLowerCase( ),
				PropertyBinding.class );

		structDict.put( JoinCondition.STRUCTURE_NAME.toLowerCase( ),
				JoinCondition.class );

		structDict.put( OdaDesignerState.STRUCTURE_NAME.toLowerCase( ),
				OdaDesignerState.class );

		structDict.put( OdaResultSetColumn.STRUCTURE_NAME.toLowerCase( ),
				OdaResultSetColumn.class );

		structDict.put( ScriptLib.STRUCTURE_NAME.toLowerCase( ),
				ScriptLib.class );
	}
}