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
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Represents the list property state which is used to reading structure list of
 * private/public ODA driver properties.
 * <p>
 * The compatible version is 0.
 * <p>
 * 
 * <pre>
 * 
 * Old design file:
 * 
 *        &lt;oda-data-source name=&quot;myDataSource1&quot;&gt;
 *          &lt;list-property name=&quot;privateDriverProperties&quot;&gt;
 *            &lt;ex-property&gt;
 *              &lt;name&gt;ODA:driver-class&lt;/name&gt;
 *              &lt;value&gt;Driver Class&lt;/value&gt;
 *            &lt;/ex-property&gt;
 *            &lt;ex-property&gt;
 *              &lt;name&gt;ODA:url&lt;/name&gt;
 *              &lt;value&gt;URL&lt;/value&gt;
 *            &lt;/ex-property&gt;
 *            &lt;ex-property&gt;
 *              &lt;name&gt;ODA:data-source&lt;/name&gt;
 *              &lt;value&gt;Data Source&lt;/value&gt;
 *            &lt;/ex-property&gt;
 *            &lt;ex-property&gt;
 *              &lt;name&gt;ODA:user&lt;/name&gt;
 *              &lt;value&gt;User&lt;/value&gt;
 *            &lt;/ex-property&gt;
 *            &lt;ex-property&gt;
 *              &lt;name&gt;ODA:password&lt;/name&gt;
 *              &lt;value&gt;Password&lt;/value&gt;
 *            &lt;/ex-property&gt;
 *          &lt;/list-property&gt;
 *        &lt;/oda-data-source&gt;
 * 
 *  New design file:
 *  
 *        &lt;oda-data-source extensionID=&quot;org.eclipse.birt.report.data.oda.jdbc&quot; name=&quot;myDataSource1&quot;&gt;
 *          &lt;property name=&quot;odaDriverClass&quot;&gt;Driver Class&lt;/property&gt;
 *          &lt;property name=&quot;odaURL&quot;&gt;URL&lt;/property&gt;
 *          &lt;property name=&quot;odaDataSource&quot;&gt;Data Source&lt;/property&gt;
 *          &lt;property name=&quot;odaUser&quot;&gt;User&lt;/property&gt;
 *          &lt;property name=&quot;odaPassword&quot;&gt;Password&lt;/property&gt;
 *        &lt;/oda-data-source&gt;
 *  
 * </pre>
 */

public class CompatibleOdaDriverPropertyStructureListState
		extends
		CompatibleListPropertyState
{

	CompatibleOdaDriverPropertyStructureListState(
			ModuleParserHandler theHandler, DesignElement element )
	{
		super( theHandler, element );

		setProperty( OdaDataSource.EXTENSION_ID_PROP,
				"org.eclipse.birt.report.data.oda.jdbc" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */
	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.EX_PROPERTY_TAG ) )
			return new CompatibleOdaDriverPropertyStructureState( handler,
					element, propDefn, list );

		return super.startElement( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	public void end( ) throws SAXException
	{
	}

	class CompatibleOdaDriverPropertyStructureState extends CompatibleStructureState
	{

		String propertyName = null;
		String propertyValue = null;

		CompatibleOdaDriverPropertyStructureState(
				ModuleParserHandler theHandler, DesignElement element,
				PropertyDefn propDefn, ArrayList theList )
		{
			super( theHandler, element );
			this.list = theList;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.NAME_ATTRIB ) )
				return new CompatibleTextState( handler, true, this );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.VALUE_TAG ) )
				return new CompatibleTextState( handler, false, this );

			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			if ( propertyValue != null && propertyName != null )
			{
				String newPropertyName = CompatibleOdaDataSourcePropertyState
						.getNewOdaDriverProperty( propertyName );

				setProperty( newPropertyName, propertyValue );
			}
		}
	}

	class CompatibleTextState extends DesignParseState
	{

		boolean isPropertyName = false;
		CompatibleOdaDriverPropertyStructureState state;

		CompatibleTextState( ModuleParserHandler handler,
				boolean isPropertyName,
				CompatibleOdaDriverPropertyStructureState state )
		{
			super( handler );

			this.isPropertyName = isPropertyName;
			this.state = state;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
		 */
		public DesignElement getElement( )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			String value = text.toString( );

			if ( isPropertyName )
				state.propertyName = value;
			else
				state.propertyValue = value;
		}

	}
}