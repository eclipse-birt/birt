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

package org.eclipse.birt.report.engine.toc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.TOCNode;

public class TOCBuilderTest extends EngineCase
{

	static final String TOC_GOLDEN_RESOURCE = "test/org/eclipse/birt/report/engine/toc/toc.golden";
	static final String TOC_GOLDEN_RESOURCE_1 = "toc.golden";

	TOCTreeNode golden;
	TOCTree tocTree;
	TOCBuilder builder;

	public void setUp( )
	{
		createGolden( );
	}

	public void tearDown( )
	{
	}

	public void testBuildTOC( )
	{

		TOCTree tree = createTOC( );
		checkTOC( golden, tree.getTOCRoot( ) );
	}

	public void testRWTOC( ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		DataOutputStream output = new DataOutputStream( out );
		TOCTree expectedtree = createTOC( );
		TOCBuilder.write( expectedtree, output );

		TOCTree actualTree = new TOCTree( );
		ByteArrayInputStream in = new ByteArrayInputStream( out.toByteArray( ) );
		DataInputStream input = new DataInputStream( in );
		TOCBuilder.read( actualTree, input );
		checkTOC( expectedtree, actualTree );
	}

	/**
	 * create the toc node. the design contains :
	 * 
	 * <pre>
	 *                     the create toc node should be:
	 *                     
	 *                     report-header
	 *                     group
	 *                     		list(dummy, hidden in html)
	 *                     		    detail 1
	 *                     			detail 2
	 * </pre>
	 */
	public void testHiddenFormat1( )
	{
		long elementId = -1;
		tocTree = new TOCTree( );
		builder = new TOCBuilder( tocTree );
		TOCEntry rootEntry = builder.getTOCEntry( );
		TOCEntry reportHeader = startEntry( rootEntry, "report-header", elementId++ );
		closeEntry( reportHeader );

		TOCEntry group = startGroupEntry( rootEntry, elementId++ ); // open group

		TOCEntry list = startDummyEntry( group, "html" ); // open list
		TOCEntry detail1 = startEntry( list, "detail1", elementId++ ); // open detail
		closeEntry( detail1 ); // close detail

		TOCEntry detail2 = startEntry( list, "detail2", elementId++ ); // open detai2
		closeEntry( detail2 ); // close detai2

		closeEntry( list ); // close list
		closeGroupEntry( group );
		TOCTreeNode detailNode1 = detail1.getNode( );
		TOCTreeNode detailNode2 = detail2.getNode( );
		assertEquals( "html", detailNode1.getHiddenFormats( ) );
		assertTrue( detailNode1.isHidden( "html" ) );
		assertEquals( "html", detailNode1.getHiddenFormats( ) );
		assertTrue( detailNode2.isHidden( "html" ) );
		assertEquals( group.getNode( ), detailNode1.getParent( ) );
		assertEquals( group.getNode( ), detailNode2.getParent( ) );
	}

	/**
	 * create the toc node. the design contains :
	 * 
	 * <pre>
	 *                    the create toc node should be:
	 *                    
	 *                    report-header
	 *                    group(hidden in html)
	 *                  		    detail 1
	 *                  			detail 2
	 * </pre>
	 */
	public void testHiddenFormat2( )
	{
		long elementId = -1;
		tocTree = new TOCTree( );
		builder = new TOCBuilder( tocTree );
		TOCEntry rootEntry = builder.getTOCEntry( );
		TOCEntry reportHeader = startEntry( rootEntry, "report-header", elementId++ );
		closeEntry( reportHeader );

		TOCEntry group = startGroupEntry( rootEntry, "html", elementId++ ); // open
		// group

		TOCEntry detail1 = startEntry( group, "detail1", elementId++ ); // open detail
		closeEntry( detail1 ); // close detail

		TOCEntry detail2 = startEntry( group, "detail2", elementId++ ); // open detai2
		closeEntry( detail2 ); // close detai2

		closeGroupEntry( group );
		TOCTreeNode detailNode1 = detail1.getNode( );
		TOCTreeNode detailNode2 = detail2.getNode( );
		assertEquals( "html", detailNode1.getHiddenFormats( ) );
		assertTrue( detailNode1.isHidden( "html" ) );
		assertEquals( "html", detailNode1.getHiddenFormats( ) );
		assertTrue( detailNode2.isHidden( "html" ) );
		assertEquals( group.getNode( ), detailNode1.getParent( ) );
		assertEquals( group.getNode( ), detailNode2.getParent( ) );
	}

	/**
	 * create the toc node. the design contains :
	 * 
	 * <pre>
	 *                    the create toc node should be:
	 *                    
	 *                    report-header
	 *                    group(hidden in pdf)
	 *                    		list(dummy, hidden in html)
	 *                    		    detail 1
	 *                    			detail 2
	 * </pre>
	 */
	public void testHiddenFormat3( )
	{
		long elementId = -1;
		tocTree = new TOCTree( );
		builder = new TOCBuilder( tocTree );
		TOCEntry rootEntry = builder.getTOCEntry( );
		TOCEntry reportHeader = startEntry( rootEntry, "report-header", elementId++ );
		closeEntry( reportHeader );

		TOCEntry group = startGroupEntry( rootEntry, "pdf", elementId++ ); // open
		// group
		TOCEntry groupHeader = startEntry( group, "group-header", elementId++ ); // open
		// group-header
		TOCEntry list = startDummyEntry( groupHeader, "html" ); // open list
		TOCEntry detail1 = startEntry( list, "detail1", elementId++ ); // open detail
		closeEntry( detail1 ); // close detail

		TOCEntry detail2 = startEntry( list, "detail2", elementId++ ); // open detai2
		closeEntry( detail2 ); // close detai2

		closeEntry( list ); // close list
		closeGroupEntry( group );
		closeEntry( groupHeader ); // close group-header
		closeGroupEntry( group );
		TOCTreeNode detailNode1 = detail1.getNode( );
		TOCTreeNode detailNode2 = detail2.getNode( );
		assertTrue( detailNode1.isHidden( "html" ) );
		assertTrue( detailNode1.isHidden( "pdf" ) );
		assertTrue( detailNode2.isHidden( "html" ) );
		assertTrue( detailNode2.isHidden( "pdf" ) );

		assertEquals( groupHeader.getNode( ), detailNode1.getParent( ) );
		assertEquals( groupHeader.getNode( ), detailNode2.getParent( ) );
	}

	/**
	 * create the toc node. the design contains :
	 * 
	 * <pre>
	 *                    the create toc node should be:
	 *                    
	 *                    report-header
	 *                    group
	 *                    	detail 1(hidden in html);
	 *                    	detail 2
	 * </pre>
	 */
	public void testHiddenFormat4( )
	{
		long elementId = -1;
		tocTree = new TOCTree( );
		builder = new TOCBuilder( tocTree );
		TOCEntry rootEntry = builder.getTOCEntry( );
		TOCEntry reportHeader = startEntry( rootEntry, "report-header", elementId++ );
		closeEntry( reportHeader );

		TOCEntry group = startGroupEntry( rootEntry, elementId++ ); // open group

		TOCEntry detail1 = startEntry( group, "detail1", "html", elementId++ ); // open
		// detail
		closeEntry( detail1 ); // close detail

		TOCEntry detail2 = startEntry( group, "detail2", elementId++ ); // open detai2
		closeEntry( detail2 ); // close detai2

		closeGroupEntry( group );

		TOCTreeNode detailNode1 = detail1.getNode( );
		assertEquals( "html", detailNode1.getHiddenFormats( ) );
		assertTrue( detailNode1.isHidden( "html" ) );
	}

	/**
	 * create the toc node. the design contains :
	 * 
	 * <pre>
	 *                    the create toc node should be:
	 *                    
	 *                    report-header
	 *                    list
	 *                    		list-header
	 *                    		group 1 header
	 *                    			group 2 header
	 *                    				detail 1
	 *                    				detail 2
	 *                    				group 2 footer
	 *                    			group 2 header
	 *                    				detail 1
	 *                    				detail 2
	 *                    				group 2 footer
	 *                    			group 1 footer
	 *                    		group 2 header
	 *                    			detail
	 *                    			detail
	 *                    			group 2 footer
	 *                    		list footer
	 *                    report footer
	 * </pre>
	 * 
	 * @param design
	 *            design
	 * @return TOC node created.
	 */
	protected TOCTree createTOC( )
	{
		long elementId = -1;
		tocTree = new TOCTree( );
		builder = new TOCBuilder( tocTree );
		TOCEntry rootEntry = builder.getTOCEntry( );
		TOCEntry reportHeader = startEntry( rootEntry, "report-header", elementId++ );
		closeEntry( reportHeader );

		TOCEntry list = startEntry( rootEntry, "list", elementId++ );

		{
			TOCEntry listHeader = startEntry( list, "list-header", elementId++ );
			closeEntry( listHeader );

			TOCEntry group1 = startGroupEntry( list, elementId++ );
			{
				TOCEntry group1Header = startEntry( group1,
						"list-group1-header", elementId++ );
				closeEntry( group1Header );

				TOCEntry group11 = startGroupEntry( group1, elementId++ );
				{
					TOCEntry group11Header = startEntry( group11,
							"list-group11-header", elementId++ );
					closeEntry( group11Header );
					TOCEntry detail111 = startEntry( group11, "detail111", elementId++ );
					closeEntry( detail111 );
					TOCEntry detail112 = startEntry( group11, "detail112", elementId++ );
					closeEntry( detail112 );

					TOCEntry group11Footer = startEntry( group11,
							"group11-footer", elementId++ );
					closeEntry( group11Footer );
				}
				closeGroupEntry( group11 );

				TOCEntry group12 = startGroupEntry( group1, elementId++ );
				{
					TOCEntry group12Header = startEntry( group12,
							"list-group12-header", elementId++ );
					closeEntry( group12Header );

					TOCEntry detail121 = startEntry( group12, "detail121", elementId++ );
					closeEntry( detail121 );

					TOCEntry detail122 = startEntry( group12, "detail122", elementId++ );
					closeEntry( detail122 );

					TOCEntry group12Footer = startEntry( group12,
							"group12-footer", elementId++ );
					closeEntry( group12Footer );
				}
				closeGroupEntry( group12 );

				TOCEntry group1Footer = startEntry( group1, "group1-footer", elementId++ );
				closeEntry( group1Footer );
			}
			closeGroupEntry( group1 );

			TOCEntry group2 = startGroupEntry( list, "group2", null, elementId++ );
			{
				TOCEntry group21 = startGroupEntry( group2, elementId++ );
				{
					TOCEntry group21Header = startEntry( group21,
							"list-group21-header", elementId++ );
					closeEntry( group21Header );

					TOCEntry detail211 = startEntry( group21, "detail211", elementId++ );
					closeEntry( detail211 );

					TOCEntry detail212 = startEntry( group21, "detail212", elementId++ );
					closeEntry( detail212 );

					TOCEntry group21Footer = startEntry( group21,
							"group21-footer", elementId++ );
					closeEntry( group21Footer );
				}
				closeGroupEntry( group21 );
			}
			closeGroupEntry( group2 );

			TOCEntry listFooter = startEntry( list, "list-footer", elementId++ );
			closeEntry( listFooter );
		}
		closeEntry( list );

		TOCEntry footer = startEntry( rootEntry, "footer", elementId++ );
		closeEntry( footer );

		return tocTree;
	}

	private TOCEntry startGroupEntry( TOCEntry parent, long elementId )
	{
		return startGroupEntry( parent, null, elementId );
	}

	private TOCEntry startGroupEntry( TOCEntry parent, String hiddenFormats, long elementId )
	{
		return startGroupEntry( parent, null, hiddenFormats, elementId );
	}

	private TOCEntry startGroupEntry( TOCEntry parent, String displayString,
			String hiddenFormats, long elementId )
	{
		return builder.startGroupEntry( parent, displayString, null,
				hiddenFormats, elementId );
	}

	private void closeGroupEntry( TOCEntry entry )
	{
		builder.closeGroupEntry( entry );
	}

	private TOCEntry startEntry( TOCEntry parent, String label, long elementId )
	{
		return startEntry( parent, label, null, elementId );
	}

	private TOCEntry startEntry( TOCEntry parent, String label,
			String hiddenFormats, long elementId )
	{
		return builder.startEntry( parent, label, null, hiddenFormats, elementId );
	}

	private TOCEntry startDummyEntry( TOCEntry parent, String hiddenFormats )
	{
		return builder.startDummyEntry( parent, hiddenFormats );
	}

	private void closeEntry( TOCEntry entry )
	{
		builder.closeEntry( entry );
	}

	private TOCTreeNode createTOCNode( TOCNode parent, String label, long elementId )
	{
		return createTOCNode( parent, label, null, elementId );
	}

	protected TOCTreeNode createTOCNode( TOCNode parent, String id, String label, long elementId )
	{
		return createTOCNode( parent, id, id, label, elementId );
	}

	protected TOCTreeNode createTOCNode( TOCNode parent, String id,
			String bookmark, String label, long elementId )
	{
		TOCTreeNode node = new TOCTreeNode( );
		node.setNodeID( id );
		node.setBookmark( bookmark );
		node.setTOCValue( label );
		node.setElementId( elementId );
		node.setParent( parent );
		if ( parent != null )
		{
			parent.getChildren( ).add( node );
		}
		return node;
	}

	protected void createGolden( )
	{
		long elementId = -1;
		golden = createTOCNode( null, null, elementId );
		createTOCNode( golden, "__TOC_0", "report-header", elementId++ );
		TOCTreeNode list = createTOCNode( golden, "__TOC_1", "list", elementId++ );
		{
			createTOCNode( list, "__TOC_1_0", "list-header", elementId++ );
			TOCTreeNode group1 = createTOCNode( list, "__TOC_1_1", elementId++ );
			{
				createTOCNode( group1, "__TOC_1_1_0", "list-group1-header", elementId++ );
				TOCTreeNode group11 = createTOCNode( group1, "__TOC_1_1_1", elementId++ );
				{
					createTOCNode( group11, "__TOC_1_1_1_0",
							"list-group11-header", elementId++ );
					createTOCNode( group11, "__TOC_1_1_1_1", "detail111", elementId++ );
					createTOCNode( group11, "__TOC_1_1_1_2", "detail112", elementId++ );
					createTOCNode( group11, "__TOC_1_1_1_3", "group11-footer", elementId++ );
				}
				TOCTreeNode group12 = createTOCNode( group1, "__TOC_1_1_2", elementId++ );
				{
					createTOCNode( group12, "__TOC_1_1_2_0",
							"list-group12-header", elementId++ );
					createTOCNode( group12, "__TOC_1_1_2_1", "detail121", elementId++ );
					createTOCNode( group12, "__TOC_1_1_2_2", "detail122", elementId++ );
					createTOCNode( group12, "__TOC_1_1_2_3", "group12-footer", elementId++ );
				}
				createTOCNode( group1, "__TOC_1_1_3", "group1-footer", elementId++ );
			}
			TOCTreeNode group2 = createTOCNode( list, "__TOC_1_2", "group2", elementId++ );
			{
				TOCNode group21 = createTOCNode( group2, "__TOC_1_2_0", elementId++ );
				{
					createTOCNode( group21, "__TOC_1_2_0_0",
							"list-group21-header", elementId++ );
					createTOCNode( group21, "__TOC_1_2_0_1", "detail211", elementId++ );
					createTOCNode( group21, "__TOC_1_2_0_2", "detail212", elementId++ );
					createTOCNode( group21, "__TOC_1_2_0_3", "group21-footer", elementId++ );
				}
			}
			createTOCNode( list, "__TOC_1_3", "list-footer", elementId++ );
		}
		createTOCNode( golden, "__TOC_2", "footer", elementId++ );
	}

	private void checkTOC( TOCTree expected, TOCTree actual )
	{
		checkTOC( (TOCTreeNode) getTOCNode( expected ),
				(TOCTreeNode) getTOCNode( actual ) );
	}
	
	private TOCTreeNode getTOCNode( TOCTree tree )
	{
		tree.setFormat( "viewer" );
		return tree.getTOCRoot( );
	}
	/**
	 * output the root to an xml file, and compare it with the golden file.
	 */
	private void checkTOC( TOCTreeNode golden, TOCTreeNode node )
	{
		assertEquals( golden, node );
	}

	private void assertEquals( TOCTreeNode golden, TOCTreeNode node )
	{
		assertEquals( golden.getNodeID( ), node.getNodeID( ) );
		assertEquals( golden.getTOCValue( ), node.getTOCValue( ) );
		assertEquals( golden.getBookmark( ), node.getBookmark( ) );
		assertEquals( golden.getElementId( ), node.getElementId( ) );
		assertEquals( golden.getChildren( ).size( ), node.getChildren( ).size( ) );
		for ( int i = 0; i < golden.getChildren( ).size( ); i++ )
		{
			TOCTreeNode node1 = (TOCTreeNode) golden.getChildren( ).get( i );
			TOCTreeNode node2 = (TOCTreeNode) node.getChildren( ).get( i );
			assertEquals( node1, node2 );
		}
	}
}
