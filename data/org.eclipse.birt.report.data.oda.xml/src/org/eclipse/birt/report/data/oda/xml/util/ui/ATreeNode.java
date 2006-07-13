/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.xml.util.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.report.data.oda.xml.impl.DataTypes;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * The instance of this class is used as tree node of the xml schema tree that is passed to 
 * gui.
 */
public class ATreeNode
{
	//
	public static final int ELEMENT_TYPE = 1;
	public static final int ATTRIBUTE_TYPE = 2;
	public static final int OTHER_TYPE = 0;
	
	//The value of certain tree node.
	private Object value;
	
	//The children list of certain tree node.
	private ArrayList children;
	
	//The parent of tree node.
	private ATreeNode parent;
	
	//The type of the tree node, may either attribute or element.
	private int type;
	
	//The data type is the complex type that defined in an xsd file.
	private String dataType;

	private static HashMap xmlTypeToDataType = new HashMap();
	
	static
	{
		try
		{
			xmlTypeToDataType.put( "string", DataTypes.getTypeString( DataTypes.STRING ) );
			xmlTypeToDataType.put( "byte", DataTypes.getTypeString( DataTypes.INT ) );
			xmlTypeToDataType.put( "decimal", DataTypes.getTypeString( DataTypes.BIGDECIMAL ) );
			xmlTypeToDataType.put( "double", DataTypes.getTypeString( DataTypes.DOUBLE ) );
			xmlTypeToDataType.put( "float", DataTypes.getTypeString( DataTypes.DOUBLE ) );
			xmlTypeToDataType.put( "int", DataTypes.getTypeString( DataTypes.INT ) );
			xmlTypeToDataType.put( "integer", DataTypes.getTypeString( DataTypes.INT ) );
			xmlTypeToDataType.put( "negativeInteger", DataTypes.getTypeString( DataTypes.INT ) );
			xmlTypeToDataType.put( "nonNegativeInteger", DataTypes.getTypeString( DataTypes.INT ) );
			xmlTypeToDataType.put( "nonPositiveInteger", DataTypes.getTypeString( DataTypes.INT ) );
			xmlTypeToDataType.put( "positiveInteger", DataTypes.getTypeString( DataTypes.INT ) );
			xmlTypeToDataType.put( "short", DataTypes.getTypeString( DataTypes.INT ) );
			xmlTypeToDataType.put( "date", DataTypes.getTypeString( DataTypes.DATE ) );
			xmlTypeToDataType.put( "dateTime", DataTypes.getTypeString( DataTypes.TIME ) );
			xmlTypeToDataType.put( "time", DataTypes.getTypeString( DataTypes.TIME ) );
		}
		catch ( OdaException e )
		{
			//Should not arrive here
		}
	}
	
	private static String getDataType( String type ) throws OdaException
	{
		Object result =  xmlTypeToDataType.get( type );
		if( result == null )
			return type;
		else 
			return result.toString( );
			
	}
	
	/**
	 * 
	 *
	 */
	ATreeNode( )
	{
		children = new ArrayList( );
	}

	/**
	 * Return the value of tree node.
	 * 
	 * @return
	 */
	public Object getValue( )
	{
		return value;
	}

	/**
	 * Return the children of tree node.
	 * 
	 * @return
	 */
	public Object[] getChildren( )
	{
		return children.toArray( );
	}

	/**
	 * Return the parent of tree node.
	 * 
	 * @return
	 */
	public ATreeNode getParent( )
	{
		return parent;
	}

	/**
	 * Set the value to be held by this tree node.
	 * 
	 * @param value
	 */
	public void setValue( Object value )
	{
		this.value = value;
	}

	/**
	 * Add a child to the tree node.
	 * 
	 * @param child
	 */
	public void addChild( Object child )
	{
		this.children.add( child );
	}

	/**
	 * Add a group of child to the tree node.
	 * 
	 * @param children
	 */
	public void addChild( Object[] children)
	{
		for(int i=0;i< children.length;i++)
		{
			this.children.add(children[i]);
		}
	}
	
	/**
	 * Set the parent of the tree node.
	 * 
	 * @param parent
	 */
	public void setParent( ATreeNode parent )
	{
		this.parent = parent;
		parent.addChild( this );
	}
	
	/*
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return value.toString();
	}
	
	/**
	 * Return the type of tree node.
	 * 
	 * @return
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * Set the type of tree node ( either attribute or element)
	 * 
	 * @param type
	 */
	public void setType( int type )
	{
		this.type = type;
	}
	
	/**
	 * Return the data type of tree node. The data type is the complex type that defined in an xsd file.
	 * 
	 * @return
	 */
	public String getDataType()
	{
		return dataType;
	}
	
	/**
	 * Set the data type of tree node ( either attribute or element)
	 * 
	 * @param type
	 * @throws OdaException 
	 */
	public void setDataType( String type ) throws OdaException
	{
		this.dataType = getDataType( type );
	}
}