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

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.ScriptDataSet;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses the "property-list" tag. We use the "property-list" tag if the element
 * property or structure member is defined as structure list type, the
 * user-defined properties, user-defined property values and the value choices
 * of user-defined property.
 */

public class ListPropertyState extends AbstractPropertyState
{

	/**
	 * The temporary list which holds the structures in one structure list.
	 */

	ArrayList list = new ArrayList( );

	/**
	 * The definition of the property of this list property.
	 */

	PropertyDefn propDefn = null;

	/**
	 * Constructs the design parse state with the design file parser handler.
	 * This constructor is used when this list property to parse is a property
	 * of one element.
	 * 
	 * @param theHandler
	 *            the design file parser handler
	 * @param element
	 *            the element which holds this property
	 */

	ListPropertyState( ModuleParserHandler theHandler, DesignElement element )
	{
		super( theHandler, element );
	}

	/**
	 * Constructs the design parse state with the design file parser handler.
	 * This constructor is used when this list property to parse is a member of
	 * one structure.
	 * 
	 * @param theHandler
	 *            the design parser handler
	 * @param element
	 *            the element holding this list property
	 * @param propDefn
	 *            the definition of the property which is structure list
	 * @param struct
	 *            the structure which holds this list property
	 */

	ListPropertyState( ModuleParserHandler theHandler, DesignElement element,
			PropertyDefn propDefn, IStructure struct )
	{
		super( theHandler, element );

		this.propDefn = propDefn;
		this.struct = struct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#setName(java.lang.String)
	 */

	protected void setName( String name )
	{
		super.setName( name );

		if ( struct != null )
		{
			propDefn = (PropertyDefn) struct.getDefn( ).getMember( name );
		}
		else
		{
			propDefn = element.getPropertyDefn( name );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{

		super.parseAttrs( attrs );

		if ( StringUtil.isBlank( name ) )
			return;

		// compatible for old Action list property.
		if ( this.struct instanceof Action )
		{
			if ( ( ActionStructureState.DRILLTHROUGH_PARAM_BINDINGS_MEMBER
					.equals( name ) ) )
			{
				name = Action.PARAM_BINDINGS_MEMBER;
			}
			else if ( ActionStructureState.DRILLTHROUGH_SEARCH_MEMBER
					.equals( name ) )
			{
				name = Action.SEARCH_MEMBER;
			}
		}

		if ( struct != null )
		{
			propDefn = (PropertyDefn) struct.getDefn( ).getMember( name );
		}
		else
		{
			propDefn = element.getPropertyDefn( name );
		}

		// prop maybe is null, for example, user properties.

		if ( !DesignElement.USER_PROPERTIES_PROP.equals( name ) )
		{
			if ( propDefn == null )
			{
				// ROM does not contain public driver properties any more.

				if ( OdaDataSource.PUBLIC_DRIVER_PROPERTIES_PROP.equals( name ) )
					return;

				// compatible for "includeLibrary" in the module

				if ( element instanceof Module
						&& "includeLibraries".equalsIgnoreCase( name ) ) //$NON-NLS-1$
					return;

				if ( element instanceof ScriptDataSet
						&& "resultSet".equalsIgnoreCase( name ) ) //$NON-NLS-1$
					return;

				// If the property name is invalid, no error will be reported.

				DesignParserException e = new DesignParserException(
						new String[]{name},
						DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY );
				RecoverableError.dealUndefinedProperty( handler, e );

				valid = false;
			}
			else
			{
				if ( PropertyType.STRUCT_TYPE != propDefn.getTypeCode( ) )
				{
					DesignParserException e = new DesignParserException(
							DesignParserException.DESIGN_EXCEPTION_WRONG_STRUCTURE_LIST_TYPE );
					handler.getErrorHandler( ).semanticError( e );
					valid = false;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STRUCTURE_TAG ) )
			return new StructureState( handler, element, propDefn, list );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.EX_PROPERTY_TAG ) )
			return new ExtendedPropertyState( handler, element, propDefn, list );

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
			// Ensure that the member is defined.

			PropertyDefn memberDefn = (PropertyDefn) struct.getDefn( )
					.getMember( name );
			struct.setProperty( memberDefn, list );

		}
		else
			element.setProperty( name, list );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#jumpTo()
	 */
	public AbstractParseState jumpTo( )
	{

		// the property has been removed. It must be handled before checking the
		// validation of <code>valid</code>.

		if ( ( StringUtil.compareVersion( handler.getVersion( ), "3" ) > 0 ) && //$NON-NLS-1$
				( StringUtil.compareVersion( handler.getVersion( ), "3.2.1" ) <= 0 ) //$NON-NLS-1$
				&& ( "boundDataColumns".equals( name ) ) //$NON-NLS-1$ 
				&& ( element instanceof GroupElement ) )
		{

			CompatibleGroupBoundColumnsState state = new CompatibleGroupBoundColumnsState(
					handler, element.getContainer( ), (GroupElement) element );
			state.setName( name );
			return state;

		}

		if ( !valid )
			return new AnyElementState( getHandler( ) );

		if ( DesignElement.USER_PROPERTIES_PROP.equalsIgnoreCase( name ) )
		{
			AbstractPropertyState state = new UserPropertyListState( handler,
					element );
			state.setName( name );
			return state;
		}

		if ( element instanceof Module )
		{
			if ( Module.IMAGES_PROP.equalsIgnoreCase( name ) )
			{
				AbstractPropertyState state = new EmbeddedImagePropertyListState(
						handler, element );
				state.setName( name );
				return state;
			}

			if ( Module.INCLUDE_SCRIPTS_PROP.equalsIgnoreCase( name ) )
			{
				SimpleStructureListState state = new SimpleStructureListState(
						handler, element );
				state.setName( name );
				state.setMemberName( IncludeScript.FILE_NAME_MEMBER );
				return state;
			}

			if ( ( Module.LIBRARIES_PROP.equalsIgnoreCase( name ) )
					|| ( "includeLibraries".equals( name ) ) ) //$NON-NLS-1$
			{
				AbstractPropertyState state = new IncludedLibrariesStructureListState(
						handler, element );
				state.setName( Module.LIBRARIES_PROP );
				return state;
			}

		}

		if ( element instanceof OdaDataSource )
		{
			if ( OdaDataSource.PRIVATE_DRIVER_PROPERTIES_PROP
					.equalsIgnoreCase( name )
					|| OdaDataSource.PUBLIC_DRIVER_PROPERTIES_PROP
							.equalsIgnoreCase( name ) )
			{
				if ( handler.isVersion( "0" ) ) //$NON-NLS-1$
				{
					CompatibleOdaDriverPropertyStructureListState state = new CompatibleOdaDriverPropertyStructureListState(
							handler, element );
					state.setName( name );
					return state;
				}
			}
		}

		if ( StringUtil.compareVersion( handler.getVersion( ), "3.2.0" ) < 0 //$NON-NLS-1$
				&& ( ReportItem.BOUND_DATA_COLUMNS_PROP.equalsIgnoreCase( name )
						|| ScalarParameter.BOUND_DATA_COLUMNS_PROP
								.equalsIgnoreCase( name ) || "boundDataColumns" //$NON-NLS-1$
				.equalsIgnoreCase( name ) ) )
		{
			CompatibleBoundColumnState state = new CompatibleBoundColumnState(
					handler, element );
			state.setName( name );
			return state;
		}

		if ( StringUtil.compareVersion( handler.getVersion( ), "3.2.3" ) < 0 //$NON-NLS-1$
				&& element instanceof ScriptDataSet
				&& "resultSet".equals( name ) ) //$NON-NLS-1$
		{
			CompatibleRenameListPropertyState state = new CompatibleRenameListPropertyState(
					handler, element, name );
			state.setName( ScriptDataSet.RESULT_SET_HINTS_PROP );
			return state;
		}

		return super.jumpTo( );
	}
}