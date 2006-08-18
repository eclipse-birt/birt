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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTypeDefinition;
import org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer;
import org.eclipse.birt.report.data.oda.xml.util.SaxParser;
import org.eclipse.birt.report.data.oda.xml.util.XMLDataInputStreamCreator;
import org.eclipse.datatools.connectivity.oda.OdaException;


/**
 * This class is used to offer GUI a utility to get an tree from certain xml/xsd
 * file.
 */
public class SchemaPopulationUtil
{
	/**
	 * @param fileName
	 * @throws MalformedURLException 
	 * @throws URISyntaxException 
	 */
	public static ATreeNode getSchemaTree( String fileName, boolean includeAttribute, int numberOfElementsAccessiable )
			throws OdaException, MalformedURLException, URISyntaxException
	{
		if ( fileName.toUpperCase( ).endsWith( ".XSD" ) )
			return XSDFileSchemaTreePopulator.getSchemaTree( fileName,includeAttribute );
		else
			return new XMLFileSchemaTreePopulator( numberOfElementsAccessiable ).getSchemaTree( fileName,includeAttribute );
	}
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
	private boolean includeAttribute = true;
	private int numberOfElementsAccessiable;
	Thread spThread;

	/**
	 * 
	 * 
	 */
	XMLFileSchemaTreePopulator( int numberOfElementsAccessiable )
	{
		this.rowCount = 0;
		this.root = new ATreeNode( );
		this.root.setValue( "ROOT" );
		this.numberOfElementsAccessiable = numberOfElementsAccessiable == 0
				? Integer.MAX_VALUE : numberOfElementsAccessiable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer#manipulateData(java.lang.String,
	 *      java.lang.String)
	 */
	public void manipulateData( String path, String value )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer#detectNewRow(java.lang.String)
	 */
	public void detectNewRow( String path, boolean start )
	{
		String treamedPath = path.replaceAll( "\\Q[\\E\\d+\\Q]\\E", "" ).trim( );
		this.insertNode( treamedPath );
		// If not attribute
		if ( !isAttribute( path ) && start )
		{
			rowCount++;
		}

		// Only parser the first 10000 elements
		if ( rowCount >= numberOfElementsAccessiable )
		{
			assert sp != null;
			sp.setStart( false );
			sp.stopParsing();
		}

	}

	/**
	 * Exam whether given path specified an attribute
	 * 
	 * @param path
	 * @return
	 */
	private boolean isAttribute( String path )
	{
		return path.matches( ".*\\Q[@\\E.+\\Q]\\E.*" );
	}

	/*
	 * (non-Javadoc)
	 * 
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
	public ATreeNode getSchemaTree( String fileName, boolean includeAttribute )
	{
		this.includeAttribute = includeAttribute;
		try
		{
			sp = new SaxParser( XMLDataInputStreamCreator.getCreator( fileName )
					.createXMLDataInputStream( ), this );

			spThread = new Thread( sp );
			spThread.start( );
			while ( sp.isAlive( ) && !sp.isSuspended( ) )
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
		}
		catch ( OdaException e1 )
		{
			// TODO Auto-generated catch block
			e1.printStackTrace( );
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
		boolean isAttribute = isAttribute( treatedPath );
		
		// Remove the leading "/" then split the path.
		String[] path = treatedPath.replaceFirst( "/", "" ).split( "/" );

		// If the path specified an attribute then re-build the path array so
		// that it can divid element and
		// its attribute to two array items.
		if ( isAttribute )
		{
			String[] temp = path[path.length - 1].split( "\\Q[@\\E" );

			assert temp.length == 2;

			String[] temp1 = new String[path.length + 1];
			for ( int i = 0; i < path.length - 1; i++ )
			{
				temp1[i] = path[i];
			}
			temp1[temp1.length - 2] = temp[0];
			temp1[temp1.length - 1] = temp[1].replaceAll( "\\Q]\\E", "" );
			path = temp1;
		}

		// The parentNode
		ATreeNode parentNode = root;

		// Iterate each path array element, find or create its countpart node
		// instance.
		for ( int i = 0; i < path.length; i++ )
		{
			// This variable hosts the node instance that matches the given path
			// array item value.
			ATreeNode matchedNode = null;

			for ( int j = 0; j < parentNode.getChildren( ).length; j++ )
			{
				if ( ( (ATreeNode) parentNode.getChildren( )[j] ).getValue( )
						.equals( path[i] ) )
				{
					matchedNode = (ATreeNode) parentNode.getChildren( )[j];
					break;
				}
			}
			if ( matchedNode != null )
			{
				parentNode = matchedNode;
			}
			else
			{
				matchedNode = new ATreeNode( );

				if ( ( i == path.length - 1 ) && isAttribute )
				{
					if ( isAttribute && !this.includeAttribute )
						continue;
					matchedNode.setType( ATreeNode.ATTRIBUTE_TYPE );
				}
				else
				{
					matchedNode.setType( ATreeNode.ELEMENT_TYPE );
				}

				matchedNode.setValue( path[i] );
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
	private static boolean includeAttribute = true;

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
			Object value = ( (ATreeNode) toBeIterated[i] ).getDataType( );// .getValue(
																			// );
			List container = new ArrayList( );
			findNodeWithValue( root, value.toString( ), container );
			for ( int j = 0; j < container.size( ); j++ )
			{
				if ( ( (ATreeNode) container.get( j ) ).getChildren( ).length == 0 )
				{
					Object[] os = ( (ATreeNode) toBeIterated[i] ).getChildren( );
					for ( int k = 0; k < os.length; k++)
					{
						if( !(((ATreeNode)os[k]).getDataType()!= null && ((ATreeNode)os[k]).getDataType().equals(((ATreeNode) container.get( j )).getDataType())))
							( (ATreeNode) container.get( j ) ).addChild( os[k]);
					}
			
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
		if ( root.getDataType( ) != null && root.getDataType( ).equals( value ) )
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
	 * @throws MalformedURLException 
	 * @throws URISyntaxException 
	 */
	public static ATreeNode getSchemaTree( String fileName, boolean incAttr )
			throws OdaException, MalformedURLException, URISyntaxException
	{
		includeAttribute = incAttr;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance( );
		factory.setNamespaceAware( true );
		URI uri = null;
		File f = new File(fileName);
		if( f.exists( ))
			uri = f.toURI();
		else
		{
			URL url = new URL(fileName);
			uri = new URI(url.getProtocol( ), url.getUserInfo( ), url.getHost( ), url.getPort( ), url.getPath( ), url.getQuery( ), url.getRef( ));
		}
		
		//Then try to parse the input string as a url in web.
		if ( uri == null )
		{
			uri = new URI( fileName );
		}
		
		XSLoader xsLoader = new XMLSchemaLoader( );
		XSModel xsModel = xsLoader.loadURI( uri.toString( ) );
		ATreeNode complexTypesRoot = populateComplexTypeTree( xsModel );

		XSNamedMap map = xsModel.getComponents( XSConstants.ELEMENT_DECLARATION );

		ATreeNode root = new ATreeNode( );

		root.setValue( "ROOT" );
		for ( int i = 0; i < map.getLength( ); i++ )
		{
			ATreeNode node = new ATreeNode( );
			XSElementDecl element = (XSElementDecl) map.item( i );

			node.setValue( element.getName( ) );
			node.setType( ATreeNode.ELEMENT_TYPE );
			node.setDataType( element.getName( ) );
			if ( element.getTypeDefinition( ) instanceof XSComplexTypeDecl )
			{
				XSComplexTypeDecl complexType = (XSComplexTypeDecl) element.getTypeDefinition( );
				//If the complex type is explicitly defined, that is, it has name.
				if ( complexType.getName( ) != null )
				{
					node.setDataType( complexType.getName( ) );
					ATreeNode n = findComplexElement( complexTypesRoot,	complexType.getName( ) );
					if( n!= null)
						node.addChild( n.getChildren( ) );
				}
				//If the complex type is implicitly defined, that is, it has no name.
				else
				{
					
					addParticleAndAttributeInfo( node, complexType, complexTypesRoot );
				}
			}
			root.addChild( node );
		}

		populateRoot( root );
		return root;

	}

	/**
	 * Add the particles and attributes that defined in an implicitly defined ComplexType to the node.
	 * 
	 * @param node
	 * @param complexType
	 * @throws OdaException 
	 */
	private static void addParticleAndAttributeInfo( ATreeNode node,
			XSComplexTypeDecl complexType, ATreeNode complexTypesRoot  ) throws OdaException
	{
		XSParticle particle = complexType.getParticle( );
		if ( particle != null )
		{
			addElementToNode( node, complexTypesRoot, (XSModelGroupImpl) particle.getTerm( ) );
		}
		if(!includeAttribute)
			return;
		XSAttributeGroupDecl group = complexType.getAttrGrp( );
		if ( group != null )
		{
			XSObjectList list = group.getAttributeUses( );
			for ( int j = 0; j < list.getLength( ); j++ )
			{
				ATreeNode childNode = new ATreeNode( );
				childNode.setValue( ( (XSAttributeUseImpl) list.item( j ) ).getAttrDeclaration( )
						.getName( ) );
				childNode.setType( ATreeNode.ATTRIBUTE_TYPE );
				node.addChild( childNode );
			}
		}
	}

	/**
	 * 
	 * @param node
	 * @param complexTypesRoot
	 * @param group
	 * @throws OdaException
	 */
	private static void addElementToNode( ATreeNode node, ATreeNode complexTypesRoot, XSModelGroupImpl group ) throws OdaException
	{
		XSObjectList list = group.getParticles( );
		for ( int j = 0; j < list.getLength( ); j++ )
		{
			if(  ( (XSParticleDecl) list.item( j ) ).getTerm( ) instanceof XSModelGroupImpl )
			{
				addElementToNode ( node, complexTypesRoot, (XSModelGroupImpl)( (XSParticleDecl) list.item( j ) ).getTerm( )  );
				continue;
			}
			ATreeNode childNode = new ATreeNode( );
			childNode.setValue( ( (XSParticleDecl) list.item( j ) ).getTerm( )
					.getName( ) );
			String dataType = ( (XSElementDecl) ( (XSParticleDecl) list.item( j ) ).getTerm( ) ).getTypeDefinition( )
					.getName( );
			if ( dataType == null || dataType.length( ) == 0 )
				dataType = childNode.getValue( ).toString( );
			childNode.setDataType( dataType );
			childNode.setType( ATreeNode.ELEMENT_TYPE );
			XSTypeDefinition xstype = ((XSElementDecl) ((XSParticleDecl) list.item(j))
					.getTerm()).getTypeDefinition();
			//Populate the complex data types under node.
			if ((!dataType.equals("anyType"))
					&&  xstype instanceof XSComplexTypeDecl)
			{	
				//First do a recursive call to populate all child complex type of current node.
				if( xstype.getName() == null)
					addParticleAndAttributeInfo( childNode, (XSComplexTypeDecl)xstype, complexTypesRoot );
				ATreeNode n = findComplexElement(complexTypesRoot,dataType);
				if( n!= null )
				{
					childNode.addChild(n.getChildren());
				}
			}
			node.addChild(childNode);
		}
	}

	/**
	 * Return the tree node instance that represents to the ComplexElement that featured by the given value.
	 * 
	 * @param root the tree node from which the search begin
	 * @param value the name of the ComplexElement
	 * @return
	 */
	private static ATreeNode findComplexElement( ATreeNode root, String value )
	{
		Object[] os = root.getChildren( );
		for ( int i = 0; i < os.length; i++ )
		{
			if ( ( (ATreeNode) os[i] ).getValue( ).equals( value ) )
				return (ATreeNode) os[i];
		}
		return null;
	}

	/**
	 * Populate a tree of ComplexTypes defined in an XSD file.
	 * 
	 * @param xsModel
	 * @return	the root node of the tree.
	 * @throws OdaException 
	 */
	private static ATreeNode populateComplexTypeTree( XSModel xsModel ) throws OdaException
	{
		XSNamedMap map = xsModel.getComponents( XSTypeDefinition.COMPLEX_TYPE );

		ATreeNode root = new ATreeNode( );

		root.setValue( "ROOT" );
		root.setDataType( "" );
		for ( int i = 0; i < map.getLength( ); i++ )
		{
			ATreeNode node = new ATreeNode( );
			XSComplexTypeDecl element = (XSComplexTypeDecl) map.item( i );
			if ( element.getName( ).equals( "anyType" ) )
				continue;
			node.setValue( element.getName( ) );
			node.setType( ATreeNode.ELEMENT_TYPE );
			node.setDataType( element.getTypeName( ) );
			root.addChild( node );

			XSParticle particle = element.getParticle( );
			if ( particle != null )
			{
				XSObjectList list = ( (XSModelGroupImpl) particle.getTerm( ) ).getParticles( );
				populateTreeNodeWithParticles( node, list );
			}
			
			if(!includeAttribute)
				continue;
			XSAttributeGroupDecl group = element.getAttrGrp( );
			if ( group != null )
			{
				XSObjectList list = group.getAttributeUses( );
				for ( int j = 0; j < list.getLength( ); j++ )
				{
					ATreeNode childNode = new ATreeNode( );
					childNode.setValue( ( (XSAttributeUseImpl) list.item( j ) ).getAttrDeclaration( )
							.getName( ) );
					childNode.setType( ATreeNode.ATTRIBUTE_TYPE );
					node.addChild( childNode );
				}
			}

		}

		populateRoot( root );
		return root;
	}

	/**
	 * 
	 * @param node the node to which the elements defined in particles should be populated into
	 * @param list the XSObjectList which lists all particles.
	 * @throws OdaException 
	 */
	private static void populateTreeNodeWithParticles( ATreeNode node, XSObjectList list ) throws OdaException
	{
		for ( int j = 0; j < list.getLength( ); j++ )
		{
			ATreeNode childNode = new ATreeNode( );
			childNode.setValue( ( (XSParticleDecl) list.item( j ) ).getTerm( )
					.getName( ) );
			if ( ( (XSParticleDecl) list.item( j ) ).getTerm( ) instanceof XSElementDecl)
			{
				String dataType = ( (XSElementDecl) ( (XSParticleDecl) list.item( j ) ).getTerm( ) ).getTypeDefinition( ).getName( );
				if ( dataType == null || dataType.length( ) == 0 )
					dataType = childNode.getValue( ).toString( );
				childNode.setDataType( dataType );
				childNode.setType( ATreeNode.ELEMENT_TYPE );
				node.addChild( childNode );
			}
			//If the particle itself is of XSModelGroupImpl type, which means that they have child types, then do recursive job
			//until all the children are added to the node.
			else if ( ( (XSParticleDecl) list.item( j ) ).getTerm( ) instanceof XSModelGroupImpl)
			{
				XSModelGroupImpl mGroup = (XSModelGroupImpl)( (XSParticleDecl) list.item( j ) ).getTerm( );
				XSObjectList obs = mGroup.getParticles();
				populateTreeNodeWithParticles( node, obs );
			}
		}
	}
}
