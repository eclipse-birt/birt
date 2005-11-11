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

import java.util.List;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer;
import org.eclipse.birt.report.data.oda.xml.util.SaxParser;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * This class is used to offer GUI a utility to get an tree from certain xml/xsd file.
 */
public class SchemaPopulationUtil 
{
	/*private static void printTree( ATreeNode root, int level )
	{
		String space = "  ";
		for(int i = 0; i < level; i++)
		{
			space = space+"  ";
		}
		System.out.println( space + root.getValue().toString()+":"+level);
		for(int i = 0; i < root.getChildren().length; i++)
		{
			printTree( (ATreeNode)(root.getChildren()[i]), level+1);
		}
	}*/
	
	/**
	 * @param fileName
	 */
	public static ATreeNode getSchemaTree( String fileName )
			throws OdaException
	{
		if( fileName.toUpperCase().endsWith(".xsd"))
			return XSDFileSchemaTreePopulator.getSchemaTree( fileName );
		else
			return new XMLFileSchemaTreePopulator().getSchemaTree( fileName );
	}

/*	public static void main( String[] argv )
	{

		try
		{
		//	new XMLFileSchemaTreePopulator().getSchemaTree( TestConstants.SMALL_XML_FILE );
			printTree(new XMLFileSchemaTreePopulator().getSchemaTree( TestConstants.SMALL_XML_FILE ),0);
			System.out.println( "Finish-----------------------" );

		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
		}
	}*/

}
/**
 * This class is used to populate an XML schema tree from an xml file.
 *
 */
final class XMLFileSchemaTreePopulator implements ISaxParserConsumer
{
	//
	private int rowCount;
	private ATreeNode root;
	private SaxParser sp; 
	
	/**
	 * 
	 *
	 */
	XMLFileSchemaTreePopulator( )
	{
		rowCount = 0;
		root = new ATreeNode();
		root.setValue("ROOT");
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer#manipulateData(java.lang.String, java.lang.String)
	 */
	public void manipulateData( String path, String value )
	{
		String treamedPath = path.replaceAll( "\\Q[\\E\\d+\\Q]\\E", "" ).trim();
		this.insertNode( treamedPath );
		
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer#detectNewRow(java.lang.String)
	 */
	public void detectNewRow( String path )
	{
		// If not attribute
		if ( !isAttribute( path ) )
		{
			rowCount++;
		}
		
		//Only parser the first 10000 elements
		if ( rowCount > 10000 )
		{
			assert sp != null;
			sp.setStart( false );
		}

	}

	/**
	 * Exam whether given path specified an attribute
	 * @param path
	 * @return
	 */
	private boolean isAttribute( String path )
	{
		return path.matches( ".*\\Q[@\\E.+\\Q]\\E.*" );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer#wakeup()
	 */
	public synchronized void wakeup( )
	{
		notify( );
	}

	/**
	 * Return the root node of a schema tree.
	 * 
	 * @param fileName
	 * @return
	 */
	public ATreeNode getSchemaTree( String fileName )
	{
		sp = new SaxParser( fileName, this );
		Thread spThread = new Thread( sp );
		spThread.start( );
		while( sp.isAlive() )
		{
			try
			{
				synchronized ( this )
				{
					wait( );
				}
			}
			catch ( InterruptedException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace( );
			}
		}
		return root;
	}
	
	/**
	 * Insert a node specified by the path.
	 * 
	 * @param treatedPath
	 */	
	private void insertNode( String treatedPath )
	{
		boolean isAttribute = isAttribute(treatedPath);
		
		//Remove the leading "/" then split the path.
		String[] path = treatedPath.replaceFirst("/","").split("/");
		
		//If the path specified an attribute then re-build the path array so that it can divid element and 
		//its attribute to two array items.
		if( isAttribute )
		{
			String[] temp = path[path.length - 1].split("\\Q[@\\E");
			
			assert temp.length == 2;
			
			String[] temp1 = new String[ path.length + 1];
			for( int i = 0; i < path.length - 1; i ++)
			{
				temp1[i] = path[i];
			}
			temp1[temp1.length - 2] = temp[0];
			temp1[temp1.length - 1] = temp[1].replaceAll("\\Q]\\E","");
			path = temp1;
		}
		
		//The parentNode 
		ATreeNode parentNode = root;
		
		//Iterate each path array element, find or create its countpart node instance. 
		for( int i = 0; i < path.length; i ++)
		{
			//This variable hosts the node instance that matches the given path array item value.
			ATreeNode matchedNode = null;
			
			for( int j = 0; j < parentNode.getChildren().length; j++)
			{
				if( ((ATreeNode)parentNode.getChildren()[j]).getValue().equals( path[i] ))
				{
					matchedNode = (ATreeNode)parentNode.getChildren()[j];
					break;
				}
			}
			if( matchedNode != null )
			{
				parentNode = matchedNode;
			}else
			{
				matchedNode = new ATreeNode();
				
				if((i == path.length - 1) && isAttribute)
				{
					matchedNode.setType( ATreeNode.ATTRIBUTE_TYPE );
				}
				else
				{
					matchedNode.setType( ATreeNode.ELEMENT_TYPE);
				}
				
				matchedNode.setValue( path[i]);
				matchedNode.setParent( parentNode );
				parentNode = matchedNode;
			}
		}
	}
}

/**
 * This class is used to populate an XML schema tree from an xml file.
 *
 */
final class XSDFileSchemaTreePopulator
{

	/**
	 * Populate the node list of a tree node in certain level. TODO add comments
	 * 
	 * @param nodeList
	 * @param root
	 * @param level
	 * @throws XPathExpressionException
	 */
	private static void populateNodeList( NodeList nodeList, ATreeNode root,
			int level ) throws XPathExpressionException
	{

		for ( int i = 0; i < nodeList.getLength( ); i++ )
		{
			boolean goToNextLevel = false;
			String space = "";
			for ( int t = 0; t < level; t++ )
			{
				space += "   ";
			}
			Node item = nodeList.item( i );

			if ( item.getLocalName( ) != null )
			{
				ATreeNode node = new ATreeNode( );

				if ( item.getLocalName( ).equals( "element" )
						|| item.getLocalName( ).equals( "attribute" ) )
				{
					String name = "<NULL>";
					//String type = "<NULL>";
					if ( item.getAttributes( ).getNamedItem( "name" ) != null )
					{
						name = item.getAttributes( )
								.getNamedItem( "name" )
								.getNodeValue( );
						//type = "name";
					}
					else
					{
						name = item.getAttributes( )
								.getNamedItem( "ref" )
								.getNodeValue( );
						//type = "ref";
					}
					node.setParent( root );
					node.setValue( name );

					if ( item.getLocalName( ).equals( "element" ) )
						node.setType( ATreeNode.ELEMENT_TYPE );
					else
						node.setType( ATreeNode.ATTRIBUTE_TYPE );
					goToNextLevel = true;
				}
				if ( item.getChildNodes( ).getLength( ) != 0 )
				{
					populateNodeList( item.getChildNodes( ), goToNextLevel
							? node : root, goToNextLevel ? level + 1 : level );
				}
			}

		}
	}

	/**
	 * Populate the whole tree until all the leaves have no child.
	 * 
	 * @param root
	 */
	private static void populateRoot( ATreeNode root )
	{
		Object[] toBeIterated = root.getChildren( );
		for ( int i = 0; i < toBeIterated.length; i++ )
		{
			Object value = ( (ATreeNode) toBeIterated[i] ).getValue( );
			List container = new ArrayList( );
			findNodeWithValue( root, value.toString( ), container );
			for ( int j = 0; j < container.size( ); j++ )
			{
				if ( ( (ATreeNode) container.get( j ) ).getChildren( ).length == 0 )
				{
					( (ATreeNode) container.get( j ) ).addChild( ( (ATreeNode) toBeIterated[i] ).getChildren( ) );
				}
			}
		}
	}

	/**
	 * Starting from a tree node, find all nodes with given value, and put it to
	 * container.
	 * 
	 * @param root
	 * @param value
	 * @param container
	 */
	private static void findNodeWithValue( ATreeNode root, String value,
			List container )
	{
		if ( root.getValue( ).toString( ).equals( value ) )
			container.add( root );
		Object[] children = root.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			findNodeWithValue( (ATreeNode) children[i], value, container );
		}
	}

	/**
	 * Return the root node of a schema tree.
	 * 
	 * @param fileName
	 * @return
	 * @throws OdaException
	 */
	public static ATreeNode getSchemaTree( String fileName )
			throws OdaException
	{
		try
		{
			XPath xpath = XPathFactory.newInstance( ).newXPath( );
			String expression = "/*";
			InputSource inputSource = new InputSource( fileName );
			NodeList nodes = (NodeList) xpath.evaluate( expression,
					inputSource,
					XPathConstants.NODESET );
			ATreeNode root = new ATreeNode( );
			root.setValue( "ROOT" );
			populateNodeList( nodes, root, 0 );
			populateRoot( root );
			return root;
		}
		catch ( XPathExpressionException e )
		{
			throw new OdaException( e.getMessage( ) );
		}
	}
}


