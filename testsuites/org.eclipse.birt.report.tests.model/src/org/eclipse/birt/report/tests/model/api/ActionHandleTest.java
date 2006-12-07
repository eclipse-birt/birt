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

package org.eclipse.birt.report.tests.model.api;

import java.io.File;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.SearchKeyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.SearchKey;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * TestCases for ActionHandle class. ActionHandle should be got from the
 * specific ElementHandle that contains an Action.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * 
 * <tr>
 * <td>{@link #testAddDrillthroughParameter()}</td>
 * <td>Add a new DrillthroughParameter to an Action.</td>
 * <td>The Parameter should be added to the Element that contains Action.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testaddDrillthroughSearchKeys()}</td>
 * <td>Add a new DrillthroughSearchKey to an Action.</td>
 * <td>The SearchKey should be added to the Element that contains Action.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetDrillthroughParameters()}</td>
 * <td>The Action is of Drillthrough type and containing 2 parameters.</td>
 * <td>A list contains 2 DrillthroughParameters. And the Expression value of
 * the Parameter is correct</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetDrillthroughSearchKeys()}</td>
 * <td>The Action is of Drillthrough type and containing 2 searchKeys.</td>
 * <td>A list conains 2 SearchKeys. And the Expression value of the SearchKey
 * is correct</td>
 * </tr>
 * 
 * 
 * <tr>
 * <td>{@link #testGetLinkExpr()}</td>
 * <td>Action is represented by a Hyperlink.</td>
 * <td>LinkExpression should be the value of the Hyperlink.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Action is represented by a Drillthrough.</td>
 * <td>LinkExpression should be the value of the BookmarkLink for the
 * Drillthrough.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Action is represented by a BookmarkLink.</td>
 * <td>LinkExpression should be the value of the BookmarkLink.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetLinkType()}</td>
 * <td>Action is represented by a Hyperlink.</td>
 * <td>LinkType should be Hyperlink.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Action is represented by a Drillthrough.</td>
 * <td>LinkType should be Drillthrough.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Action is represented by a BookmarkLink.</td>
 * <td>LinkType should be BookmarkLink.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetLinkExpr()}</td>
 * <td>Action is represented by a Hyperlink. Set its Link Expression.</td>
 * <td>Value of the Expression is properly set.</td>
 * </tr>
 * 
 * </table>
 *  
 */
public class ActionHandleTest extends BaseTestCase
{
  // define two input files
	final static String INPUT = "ActionHandleTest.xml";
	final static String INPUT2 = "ActionHandleTest2.xml";
	final static String goldenfile = "ActionHandleTest2_golden.xml";
	
	ActionHandle actionHandle = null;
    
	public ActionHandleTest(String name){
		super(name);
	}
	
	public static Test suite(){
		
		return new TestSuite(ActionHandleTest.class);
	}
	
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		
		// retrieve two input files from tests-model.jar file
		copyResource_INPUT( INPUT , INPUT );
		copyResource_INPUT( INPUT2 , INPUT2 );
		copyResource_GOLDEN (goldenfile, goldenfile);
		
		openDesign( INPUT ); //$NON-NLS-1$
		assertNotNull( designHandle );
	}

	public void tearDown( )
	{
		removeResource( );
	}
    /**
     * Get an related ActionHandle for the action defined on an image.
     */
    
	private ActionHandle getAction( String imageName )
	{
		ImageHandle imageHandle = (ImageHandle) designHandle
				.findElement( imageName );
		assertNotNull( imageHandle );
		return imageHandle.getActionHandle( );
	}

/*	public void testNewFile()
	{
		try
		{
			String tempDir = System.getProperty("java.io.tmpdir");
			tempDir = tempDir + java.io.File.separator + "newFile";
			File f = new File(tempDir);
			

			String ff = "C:\\DOCUME~1\\jqiu\\LOCALS~1\\Temp\\ActionHandleTest2.out";
			File fff = new File(ff);
			
			if(fff.exists())
			{
				System.out.println("file exist");
			}
			else
			{
				fff.createNewFile();
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	*/
	public void testAdd( ) throws Exception
	{
		 
		openDesign( INPUT2 ); //$NON-NLS-1$
       
		ImageHandle imageHandle = (ImageHandle) designHandle
				.findElement( "Image1" ); //$NON-NLS-1$
		ActionHandle actionHandle = imageHandle.getActionHandle( );

		MemberHandle memberHandle = actionHandle.getParamBindings( );
		assertEquals( 1, memberHandle.getListValue( ).size( ) );
		assertEquals(
		"exp0", ( (ParamBindingHandle) memberHandle.getAt( 0 ) ).getExpression( ) ); //$NON-NLS-1$
		
//	    Iterator iter = imageHandle.getColumnBindings( ).iterator( );
//	    while( iter.hasNext( ) )
//	    {
//	    	ComputedColumnHandle ccHandle = (ComputedColumnHandle) iter.next( );
//	    	String expr = ccHandle.getExpression( );
//	    	String colName = ccHandle.getName( );
//	    	System.out.println( "Column name: [" + colName + "]	Expr: [" + expr + "]");
//	    }
//		
		
		memberHandle.removeItem( 0 );
		assertEquals( 0, memberHandle.getListValue( ).size( ) );

		Action action = StructureFactory.createAction( );
		actionHandle = imageHandle.setAction( action );
        
        // default is hyperlink.
		assertEquals( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK, actionHandle
				.getLinkType( ) ); //$NON-NLS-1$
        actionHandle.setReportName( "report-name1" ); //$NON-NLS-1$

        // switch to drill-through
        actionHandle.setLinkType( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH );
		SearchKey key1 = StructureFactory.createSearchKey( );
		key1.setExpression( "Key1" ); //$NON-NLS-1$
		SearchKey key2 = StructureFactory.createSearchKey( );
		key2.setExpression( "Key2" ); //$NON-NLS-1$

		actionHandle.addSearch( key1 );
		actionHandle.addSearch( key2 );

		ParamBinding param1 = StructureFactory.createParamBinding( );
		param1.setExpression( "exp1" ); //$NON-NLS-1$
		param1.setParamName( "param1" ); //$NON-NLS-1$

		ParamBinding param2 = StructureFactory.createParamBinding( );
		param2.setExpression( "exp2" ); //$NON-NLS-1$
		param2.setParamName( "param2" ); //$NON-NLS-1$

		actionHandle.addParamBinding( param1 ); // one way
		actionHandle.getParamBindings( ).addItem( param2 ); // another way.
		
        
		super.saveAs( goldenfile ); //$NON-NLS-1$
		
		assertTrue( compareTextFile(
				goldenfile, "ActionHandleTest2.out" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		
	}

	/**
	 * Case1: Action is represented by a Hyperlink. LinkExpression should be the
	 * value of the Hyperlink.
	 * <p>
	 * Case2: Action is represented by a Drillthrough. LinkExpression should be
	 * the value of the BookmarkLink for the Drillthrough.
	 * <p>
	 * Case3: Action is represented by a BookmarkLink. LinkExpression should be
	 * the value of the BookmarkLink.
	 * 
	 * @throws Exception
	 */

	public void testGetLinkExpr( ) throws Exception
	{
		//1
		actionHandle = getAction( "Image1" ); //$NON-NLS-1$

		assertEquals(
				"www.rock.com.cn/haha/test.html", actionHandle.getURI() ); //$NON-NLS-1$
		assertNull( actionHandle.getTargetBookmark() );

		Iterator iter = actionHandle.paramBindingsIterator( );
		assertFalse( iter.hasNext( ) );

		iter = actionHandle.searchIterator( );
		assertFalse( iter.hasNext( ) );

		//2
		actionHandle = getAction( "Image2" ); //$NON-NLS-1$
		assertEquals(
				"www.rock.com/bookmarks/1.jsp", actionHandle.getTargetBookmark() ); //$NON-NLS-1$

		//3
		actionHandle = getAction( "Image4" ); //$NON-NLS-1$
		assertEquals(
				"www.rock.com.cn/haha/index.html/bookmarklink1", actionHandle.getTargetBookmark() ); //$NON-NLS-1$
	}

	/**
	 * test setLinkExpr().
	 * 
	 * @throws SemanticException
	 */

	public void testSetLinkExpr( ) throws SemanticException
	{
		actionHandle = getAction( "Image1" ); //$NON-NLS-1$

        // hyperlink
        actionHandle.setURI( "http://birt.eclipse.org/" ); //$NON-NLS-1$
        actionHandle.setLinkType( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK ); 
        assertEquals( "http://birt.eclipse.org/", actionHandle.getURI() ); //$NON-NLS-1$
        assertNull( actionHandle.getTargetBookmark() );
        
        // bookmark
		actionHandle.setTargetBookmark( "Bookmark1" ); //$NON-NLS-1$
        actionHandle.setLinkType( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK );
		assertEquals( "Bookmark1", actionHandle.getTargetBookmark() ); //$NON-NLS-1$
        assertEquals( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK,
                actionHandle.getLinkType( ) );
		assertNull( actionHandle.getURI() );
		
        // drill-through as bookmark.
		actionHandle.setTargetBookmark( "report1#section 1" ); //$NON-NLS-1$
        actionHandle.setLinkType( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH );
		assertEquals(
				"report1#section 1", actionHandle.getTargetBookmark() ); //$NON-NLS-1$
		assertNull( actionHandle.getURI() );

	}

	/**
	 * Test getLinkType(). Case1: Action is represented by a Hyperlink. LinkType
	 * should be Hyperlink.
	 * <p>
	 * Case2: Action is represented by a Drillthrough. LinkType should be
	 * Drillthrough.
	 * <p>
	 * Case3: Action is represented by a BookmarkLink. LinkType should be
	 * BookmarkLink.
	 * 
	 * @throws Exception
	 */
	public void testGetLinkType( ) throws Exception
	{
		//1.
		actionHandle = getAction( "Image1" ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK,
				actionHandle.getLinkType( ) ); //$NON-NLS-1$

		//2.
		actionHandle = getAction( "Image2" ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH,
				actionHandle.getLinkType( ) ); //$NON-NLS-1$

		//3.
		actionHandle = getAction( "Image4" ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK,
				actionHandle.getLinkType( ) ); //$NON-NLS-1$

	}

	/**
	 * Test getDrillthroughParameters(). Get a List from the element that
	 * conains the Action. The list conains 2 DrillthroughParameters.
	 * 
	 * @throws Exception
	 */

	public void testGetDrillthroughParameters( ) throws Exception
	{
		actionHandle = getAction( "Image2" ); //$NON-NLS-1$
		Iterator iter = actionHandle.paramBindingsIterator( );

		ParamBindingHandle p1 = (ParamBindingHandle) iter.next( );

		assertEquals( "1+1=3", p1.getExpression( ) ); //$NON-NLS-1$		
		assertEquals( "param1", p1.getParamName( ) ); //$NON-NLS-1$

		assertNotNull( iter.next( ) );
		assertNull( iter.next( ) );

		p1.setExpression( "hello 1" ); //$NON-NLS-1$
		p1.setParamName( "hello name 1" ); //$NON-NLS-1$

		assertEquals( "hello 1", p1.getExpression( ) ); //$NON-NLS-1$		
		assertEquals( "hello name 1", p1.getParamName( ) ); //$NON-NLS-1$

	}

	/**
	 * test addDrillthroughtParameter(). Add a new DrillthroughParameter to an
	 * Action.
	 * 
	 * @throws Exception
	 */

	public void testAddDrillthroughParameter( ) throws Exception
	{
		actionHandle = getAction( "Image2" ); //$NON-NLS-1$
		ParamBinding p = new ParamBinding( );

		PropertyDefn nameProp = (PropertyDefn) p.getDefn( ).getMember(
				ParamBinding.PARAM_NAME_MEMBER );
		PropertyDefn exprProp = (PropertyDefn) p.getDefn( ).getMember(
				ParamBinding.EXPRESSION_MEMBER );

		p.setProperty( nameProp, "ParamX" ); //$NON-NLS-1$
		p.setProperty( exprProp, "ExprX" ); //$NON-NLS-1$

		// PropertyHandle paramHandle = actionHandle.getParamBindings();

		MemberHandle paramHandle = actionHandle.getParamBindings( );
		paramHandle.addItem( p );

		Iterator iter = actionHandle.paramBindingsIterator( );
		int count = 0;
		for ( ; iter.hasNext( ); iter.next( ) )
		{
			count++;
		}

		assertEquals( 3, count );
	}

	/**
	 * test getDrillthroughSearchKeys().
	 * 
	 * @throws Exception
	 */

	public void testGetDrillthroughSearchKeys( ) throws Exception
	{
		actionHandle = getAction( "Image3" ); //$NON-NLS-1$
		Iterator searchKeys = actionHandle.searchIterator( );

		SearchKeyHandle key1 = (SearchKeyHandle) searchKeys.next( );
		assertEquals( "searchKey1", key1.getExpression( ) ); //$NON-NLS-1$

		key1.setExpression( "new expression" ); //$NON-NLS-1$
		assertEquals( "new expression", key1.getExpression( ) ); //$NON-NLS-1$

		assertNotNull( searchKeys.next( ) );
		assertNull( searchKeys.next( ) );

	}

	/**
	 * test addDrillthroughtSearchKeys(). Add a new DrillthroughSearchKey to an
	 * Action.
	 * 
	 * @throws Exception
	 */
	public void testaddDrillthroughSearchKeys( ) throws Exception
	{
		actionHandle = getAction( "Image3" ); //$NON-NLS-1$

		SearchKey key = new SearchKey( );
		PropertyDefn exprProp = (PropertyDefn) key.getDefn( ).getMember(
				SearchKey.EXPRESSION_MEMBER );
		key.setProperty( exprProp, "new Key3" ); //$NON-NLS-1$

		// PropertyHandle searchHandle = actionHandle.getSearch();

		MemberHandle searchHandle = actionHandle.getSearch( );
		searchHandle.addItem( key );

		Iterator iter = actionHandle.searchIterator( );

		int count = 0;
		for ( ; iter.hasNext( ); iter.next( ) )
			count++;
		assertEquals( 3, count );

	}

	/**
	 * Test methods like get/set targetwindow.
	 * 
	 * @throws Exception
	 */

	public void testOtherMethods( ) throws Exception
	{
		actionHandle = getAction( "Image3" ); //$NON-NLS-1$
		assertEquals( "Window3", actionHandle.getTargetWindow( ) ); //$NON-NLS-1$

		actionHandle = getAction( "Image1" ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.TARGET_NAMES_TYPE_BLANK, actionHandle.getTargetWindow( ) ); //$NON-NLS-1$

		actionHandle.setTargetWindow( "new target windows" ); //$NON-NLS-1$
		assertEquals( "new target windows", actionHandle.getTargetWindow( ) ); //$NON-NLS-1$

	}
	public void testTargetFileType( ) throws Exception
	{
		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		LibraryHandle library = sessionHandle.createLibrary();
		ElementFactory factory = new ElementFactory(library.getModule());
		ImageHandle image1 = factory.newImage("image1");
		ImageHandle image2 = factory.newImage("image2");
		LabelHandle label1 = factory.newLabel("label1");
		LabelHandle label2 = factory.newLabel("label2");
		library.getComponents().add(image1);
		library.getComponents().add(image2);
		library.getComponents().add(label1);
		library.getComponents().add(label2);
		
		//TargetBookMark only support for bookmark link and drill-through link
		Action action = StructureFactory.createAction( );
		image1.setAction(action);
		image1.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK);
		image1.getActionHandle().setTargetBookmarkType(DesignChoiceConstants.ACTION_BOOKMARK_TYPE_BOOKMARK);
		assertNull(image1.getActionHandle().getTargetBookmarkType());
		
		image2.setAction(action);
		image2.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK);
		image2.getActionHandle().setTargetBookmarkType(DesignChoiceConstants.ACTION_BOOKMARK_TYPE_TOC);
		assertEquals(DesignChoiceConstants.ACTION_BOOKMARK_TYPE_TOC,image2.getActionHandle().getTargetBookmarkType());
	    
		
	    //TargetFileType only support for URI link and drill-through link
		label1.setAction(action);
	    label1.getActionHandle().setTargetFileType(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DESIGN);
	    assertNull(label1.getActionHandle().getTargetFileType());
	    image1.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK);
	    assertEquals(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DESIGN,label1.getActionHandle().getTargetFileType());
	    library.getCommandStack().undo();
	    assertNull(label1.getActionHandle().getTargetFileType());
	    library.getCommandStack().redo();
	    assertEquals(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DESIGN,label1.getActionHandle().getTargetFileType());
	    
	    label2.setAction(action);
	    label2.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH);
	    label2.getActionHandle().setTargetFileType(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT);
	    assertEquals(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT,label2.getActionHandle().getTargetFileType());
	    label2.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK);
	    assertNull(label2.getActionHandle().getTargetFileType());
	    
	}
	
}

