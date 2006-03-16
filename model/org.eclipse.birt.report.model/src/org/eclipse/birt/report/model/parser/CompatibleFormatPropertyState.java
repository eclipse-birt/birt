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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.ParameterFormatValue;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.xml.sax.SAXException;

/**
 * Represents the property state which is used to reading format properties. The
 * compatible version is 0.
 * <p>
 * Can translate the obsolete format
 * 
 * <pre>
 *    
 *    Old design file:
 *    
 *       &lt;property name=&quot;dateTimeFormat&quot;&gt;:yyyy/mm/dd&lt;/property&gt;
 *       &lt;property name=&quot;numberFormat&quot;&gt;:&lt;/property&gt;
 *       &lt;property name=&quot;stringFormat&quot;&gt;noformat:&lt;/property&gt;
 *       
 *    New design file:
 *    
 *    	&lt;structure name=&quot;dateTimeFormat&quot;&gt;
 *    	  &lt;property name=&quot;category&quot;&gt;Custom&lt;/property&gt;
 *    	  &lt;property name=&quot;pattern&quot;&gt;yyyy/mm/dd&lt;/property&gt;
 *    	&lt;/structure&gt;
 *    	&lt;structure name=&quot;numberFormat&quot;&gt;
 *    	  &lt;property name=&quot;category&quot;&gt;Currency&lt;/property&gt;
 *    	  &lt;property name=&quot;pattern&quot;&gt;$###,###.##&lt;/property&gt;
 *    	&lt;/structure&gt;
 *    	&lt;structure name=&quot;stringFormat&quot;&gt;
 *    	  &lt;property name=&quot;category&quot;&gt;noformat&lt;/property&gt;
 *    	  &lt;property name=&quot;pattern&quot;&gt;***&lt;/property&gt;
 *    	&lt;/structure&gt;
 *    
 * </pre>
 */

public class CompatibleFormatPropertyState extends PropertyState
{

	/**
	 * The structure which holds this property as a member.
	 */

	protected IStructure parentStruct = null;

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
	 *            the structure that contains format structures.
	 */

	CompatibleFormatPropertyState( ModuleParserHandler theHandler,
			DesignElement element, PropertyDefn propDefn,
			IStructure parentStruct )
	{
		super( theHandler, element );

		this.propDefn = propDefn;
		this.parentStruct = parentStruct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		String value = text.toString( );
		if ( StringUtil.isBlank( value ) )
			return;

		assert struct != null;

		String category = null;
		String pattern = null;
		int index = value.indexOf( ':' );
		if ( index != -1 )
		{
			category = value.substring( 0, index );
			pattern = value.substring( index + 1 );
		}
		else
		{
			if ( ParameterFormatValue.FORMAT_VALUE_STRUCT
					.equalsIgnoreCase( propDefn.getStructDefn( ).getName( ) ) )
				category = value;
			else
				pattern = value;
		}

		if ( StringUtil.isBlank( category ) )
			category = DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM;

		FormatValue formatValue = (FormatValue) struct;
		setMember( formatValue, propDefn.getName( ),
				FormatValue.CATEGORY_MEMBER, category );
		setMember( formatValue, propDefn.getName( ),
				FormatValue.PATTERN_MEMBER, pattern );

		if ( parentStruct != null )
		{
			parentStruct.setProperty( propDefn, struct );
		}
		else
		{
			// structure property.

			element.setProperty( name, struct );
		}
	}

	protected void createStructure( )
	{
		assert propDefn != null;

		struct = StructureState.createStructure( (StructureDefn) propDefn
				.getStructDefn( ) );

	}

}
