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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IStructure;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.structures.Action;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.xml.sax.SAXException;

/**
 * Parses the action structure.
 */

public class ActionStructureState extends StructureState
{

	ActionStructureState( DesignParserHandler theHandler, DesignElement element )
	{
		super( theHandler, element );
		struct = new Action( );
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

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.EXPRESSION_TAG ) )
			return new ActionExpressionState( handler, element, propDefn,
					struct );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LIST_PROPERTY_TAG ) )
			return new PropertyListState( handler, element, propDefn, struct );

		return super.startElement( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( )
	{
		if ( hasValidDrillThrough( ) )
		{
			PropertyDefn prop = struct.getDefn( ).getMember(
					Action.LINK_TYPE_MEMBER );
			assert prop != null;

			String linkType = (String) struct
					.getProperty( handler.design, prop );

			if ( DesignChoiceConstants.ACTION_LINK_TYPE_NONE.equals( linkType ) )
				setMember( struct, name, Action.LINK_TYPE_MEMBER,
						DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH );
			else
				handler.semanticError( new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_CHOICE_RESTRICTION_VIOLATION ) );
		}

		element.setProperty( name, struct );
	}

	private boolean hasValidDrillThrough( )
	{
		PropertyDefn prop = struct.getDefn( ).getMember(
				Action.DRILLTHROUGH_REPORT_NAME_MEMBER );
		assert prop != null;
		String reportName = (String) struct.getProperty( handler.design, prop );

		prop = struct.getDefn( ).getMember( Action.DRILLTHROUGH_SEARCH_MEMBER );
		assert prop != null;
		Object searchKey = struct.getProperty( handler.design, prop );

		prop = struct.getDefn( ).getMember(
				Action.DRILLTHROUGH_BOOKMARK_LINK_MEMBER );
		assert prop != null;
		Object bookmarkLink = struct.getProperty( handler.design, prop );

		prop = struct.getDefn( ).getMember(
				Action.DRILLTHROUGH_PARAM_BINDINGS_MEMBER );
		assert prop != null;
		Object parameter = struct.getProperty( handler.design, prop );

		if ( reportName == null )
		{
			if ( ( searchKey != null ) || ( bookmarkLink != null )
					|| ( parameter != null ) )
				handler.semanticError( new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_ACTION_REPORTNAME_REQUIRED ) );
			return false;
		}

		if ( ( searchKey != null ) && ( bookmarkLink != null ) )
		{
			handler.semanticError( new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_CHOICE_RESTRICTION_VIOLATION ) );
			return false;
		}
		else if ( searchKey != null )
		{
			setMember( struct, name, Action.DRILLTHROUGH_TYPE_MEMBER,
					DesignChoiceConstants.DRILL_THROUGH_LINK_TYPE_SEARCH );
		}

		else if ( bookmarkLink != null )
		{
			setMember( struct, name, Action.DRILLTHROUGH_TYPE_MEMBER,
					DesignChoiceConstants.DRILL_THROUGH_LINK_TYPE_BOOKMARK_LINK );
		}

		return true;

	}

	class ActionExpressionState extends ExpressionState
	{

		ActionExpressionState( DesignParserHandler theHandler,
				DesignElement element, PropertyDefn propDefn, IStructure struct )
		{
			super( theHandler, element, propDefn, struct );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			PropertyDefn prop = struct.getDefn( ).getMember(
					Action.LINK_TYPE_MEMBER );
			assert prop != null;
			String linkType = (String) struct
					.getProperty( handler.design, prop );

			if ( name.equalsIgnoreCase( Action.HYPERLINK_MEMBER ) )
			{
				if ( !DesignChoiceConstants.ACTION_LINK_TYPE_NONE
						.equals( linkType ) )
				{
					handler
							.semanticError( new DesignParserException(
									DesignParserException.DESIGN_EXCEPTION_CHOICE_RESTRICTION_VIOLATION ) );
					return;
				}
				setMember( struct, propDefn.getName( ),
						Action.LINK_TYPE_MEMBER,
						DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK );
			}
			else if ( name.equalsIgnoreCase( Action.BOOKMARK_LINK_MEMBER ) )
			{
				if ( !DesignChoiceConstants.ACTION_LINK_TYPE_NONE
						.equals( linkType ) )
				{
					handler
							.semanticError( new DesignParserException(
									DesignParserException.DESIGN_EXCEPTION_CHOICE_RESTRICTION_VIOLATION ) );
					return;
				}
				setMember( struct, propDefn.getName( ),
						Action.LINK_TYPE_MEMBER,
						DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK );
			}

			//the value of the expression is set in the super class.
			super.end( );

		}
	}

}