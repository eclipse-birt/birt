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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a scalar parameter.
 * 
 */

public class ScalarParameterState extends ParameterState
{

	/**
	 * The scalar parameter being created.
	 */

	protected ScalarParameter param;

	/**
	 * Constructs the scalar parameter state with the design parser handler, the
	 * container element and the container slot of the scalar parameter.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the container of this parameter.
	 * @param slot
	 *            the slot ID of the slot where the parameter is stored.
	 */

	public ScalarParameterState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/**
	 * Constructs the scalar parameter state with the design file parser handler.
	 * 
	 * @param theHandler
	 *            the parse handler
	 */

	ScalarParameterState( DesignParserHandler theHandler )
	{
		super( theHandler );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		// First we create the ScalarParameter.

		param = new ScalarParameter( );

		// Then we initialize the properties derived from the
		// the Report Item element. The name is required for a parameter.
		// <code>initElement</code> adds the parameter to the parameters slot
		// of the report design.

		initElement( attrs, true );

		// Deal with the properties all parameters have.

		initParameter( attrs );

		// Get the properties specific to a scalar parameter.

		setProperty( ScalarParameter.DATA_TYPE_PROP, attrs,
				DesignSchemaConstants.DATA_TYPE_ATTRIB );
		setProperty( ScalarParameter.CONCEAL_VALUE_PROP, attrs,
				DesignSchemaConstants.CONCEAL_VALUE_ATTRIB );
		setProperty( ScalarParameter.ALLOW_NULL_PROP, attrs,
				DesignSchemaConstants.ALLOW_NULL_ATTRIB );
		setProperty( ScalarParameter.ALLOW_BLANK_PROP, attrs,
				DesignSchemaConstants.ALLOW_BLANK_ATTRIB );
		setProperty( ScalarParameter.FORMAT_PROP, attrs,
				DesignSchemaConstants.FORMAT_ATTRIB );
		setProperty( ScalarParameter.CONTROL_TYPE_PROP, attrs,
				DesignSchemaConstants.CONTROL_TYPE_ATTRIB );
		setProperty( ScalarParameter.ALIGNMENT_PROP, attrs,
				DesignSchemaConstants.ALIGNMENT_ATTRIB );
	}

	/**
	 * Returns the scalar parameter being built.
	 * 
	 * @return the parameter instance
	 */

	public DesignElement getElement( )
	{
		return param;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DEFAULT_VALUE_TAG ) )
			return new TextState( handler, param,
					ScalarParameter.DEFAULT_VALUE_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DYNAMIC_LIST_TAG ) )
			return new DynamicListState( handler );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.SELECTION_LIST_TAG ) )
			return new SelectionListState( handler );
		return super.startElement( tagName );
	}

	/**
	 * This class parses the dynamic list.
	 *  
	 */

	class DynamicListState extends InnerParseState
	{

		/**
		 * Constructs a dynamic list with the given parser handler.
		 * 
		 * @param handler
		 */

		public DynamicListState( DesignParserHandler handler )
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			setProperty( ScalarParameter.DATASET_NAME_PROP, attrs,
					DesignSchemaConstants.DATA_SET_NAME_ATTRIB );
			setProperty( ScalarParameter.VALUE_EXPR_PROP, attrs,
					DesignSchemaConstants.VALUE_EXPR_ATTRIB );
			setProperty( ScalarParameter.LABEL_EXPR_PROP, attrs,
					DesignSchemaConstants.LABEL_EXPR_ATTRIB );

			super.parseAttrs( attrs );
		}
	}

	/**
	 * This class parses the selection list.
	 *  
	 */

	class SelectionListState extends InnerParseState
	{

		/**
		 * Temporary storage of the list of choices of the selection list.
		 */

		protected ArrayList selectionList = null;

		/**
		 * Constructs a selection list with the given parser handler.
		 * 
		 * @param handler
		 */

		public SelectionListState( DesignParserHandler handler )
		{
			selectionList = (ArrayList) getElement( ).getLocalProperty(
					handler.design, ScalarParameter.SELECTION_LIST_PROP );
			if ( selectionList == null )
				selectionList = new ArrayList( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.CHOICE_TAG ) )
				return new ChoiceState( );

			return super.startElement( tagName );

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			setProperty( ScalarParameter.MUCH_MATCH_PROP, attrs,
					DesignSchemaConstants.MUST_MATCH_ATTRIB );
			setProperty( ScalarParameter.FIXED_ORDER_PROP, attrs,
					DesignSchemaConstants.FIXED_ORDER_ATTRIB );

			super.parseAttrs( attrs );
		}

		public void end( ) throws SAXException
		{
			getElement( ).setProperty( ScalarParameter.SELECTION_LIST_PROP,
					selectionList );
			selectionList = null;
			super.end( );
		}

		/**
		 * This class parse a choice in the selection list.
		 */

		private class ChoiceState extends InnerParseState
		{

			private SelectionChoice choice = null;

			/**
			 * Constructs a choice state to parse one choice in selection list.
			 */

			public ChoiceState( )
			{
				super( );
				choice = new SelectionChoice( );
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
			 */

			public AbstractParseState startElement( String tagName )
			{
				if ( DesignSchemaConstants.LABEL_TAG.equalsIgnoreCase( tagName ) )
					return new ExternalTextState( handler, choice,
							SelectionChoice.LABEL_MEMBER );
				if ( DesignSchemaConstants.VALUE_TAG.equalsIgnoreCase( tagName ) )
					return new TextState( handler, choice,
							SelectionChoice.VALUE_MEMBER );
				return super.startElement( tagName );
			}

			/*
			 *  (non-Javadoc)
			 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
			 */
			
			public void end( ) throws SAXException
			{
				if ( selectionList != null )
					selectionList.add( choice );
				super.end( );
			}
		}
	}
}
