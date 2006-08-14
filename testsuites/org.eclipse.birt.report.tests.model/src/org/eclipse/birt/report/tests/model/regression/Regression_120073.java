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

package org.eclipse.birt.report.tests.model.regression;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.metadata.ArgumentInfoList;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * add onPrepare(), onCreate(), and onRender() to the following report
 * elements: label, text, image, grid, dynamictext, table, table header row,
 * table footer row, table group header row, table group footer row table detail
 * row, list.
 * The first argument of these methods will represent the "this" object
 * </p>
 * Test description:
 * <p>
 * 1. Check those elements have methods: onPrepare(), onCreate(), and onRender()
 * </p>
 * 2. Check "this" is the argument of these methods
 */

public class Regression_120073 extends BaseTestCase
{

	public void test_120073( )
	{
		MetaDataDictionary instance = MetaDataDictionary.getInstance( );

		
		// label has methods
			
		IElementDefn label = instance.getElement( "Label" ); //$NON-NLS-1$
		List list = label.getMethods( );
		int i = 0;
		assertEquals( Label.ON_PREPARE_METHOD,
				( (PropertyDefn) list.get( i++ ) ).getName( ) );
		assertEquals( Label.ON_CREATE_METHOD, ( (PropertyDefn) list.get( i++ ) )
				.getName( ) );
		assertEquals( Label.ON_RENDER_METHOD, ( (PropertyDefn) list.get( i++ ) )
				.getName( ) );
		assertEquals( Label.ON_PAGE_BREAK_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );

		IMethodInfo onprepare = label.getProperty( Label.ON_PREPARE_METHOD )
				.getMethodInfo( );
		Iterator iter = onprepare.argumentListIterator( );
		ArgumentInfoList argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		IMethodInfo oncreate = label.getProperty( Label.ON_CREATE_METHOD )
				.getMethodInfo( );
		iter = oncreate.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		IMethodInfo onrender = label.getProperty( Label.ON_RENDER_METHOD )
				.getMethodInfo( );
		iter = onrender.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		IMethodInfo onpagebreak = label.getProperty( Label.ON_PAGE_BREAK_METHOD )
				.getMethodInfo( );
		iter = onpagebreak.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		// text has methods
		IElementDefn text = instance.getElement( "Text" ); //$NON-NLS-1$
		list = text.getMethods( );
		int j = 0;
		assertEquals( TextItem.ON_PREPARE_METHOD, ( (PropertyDefn) list
				.get( j++ ) ).getName( ) );
		assertEquals( TextItem.ON_CREATE_METHOD, ( (PropertyDefn) list
				.get( j++ ) ).getName( ) );
		assertEquals( TextItem.ON_RENDER_METHOD, ( (PropertyDefn) list
				.get( j++ ) ).getName( ) );
		assertEquals( TextItem.ON_PAGE_BREAK_METHOD, ( (PropertyDefn) list
				.get( j++ ) ).getName( ) );

		onprepare = text.getProperty( TextItem.ON_PREPARE_METHOD )
				.getMethodInfo( );
		iter = onprepare.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		oncreate = text.getProperty( TextItem.ON_CREATE_METHOD )
				.getMethodInfo( );
		iter = oncreate.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onrender = text.getProperty( TextItem.ON_RENDER_METHOD )
				.getMethodInfo( );
		iter = onrender.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onpagebreak = text.getProperty( TextItem.ON_PAGE_BREAK_METHOD )
				.getMethodInfo( );
		iter = onpagebreak.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );
		// image has methods
		IElementDefn image = instance.getElement( "Image" ); //$NON-NLS-1$
		list = image.getMethods( );
		int k = 0;
		assertEquals( ImageItem.ON_PREPARE_METHOD, ( (PropertyDefn) list
				.get( k++ ) ).getName( ) );
		assertEquals( ImageItem.ON_CREATE_METHOD, ( (PropertyDefn) list
				.get( k++ ) ).getName( ) );
		assertEquals( ImageItem.ON_RENDER_METHOD, ( (PropertyDefn) list
				.get( k++ ) ).getName( ) );
		assertEquals( ImageItem.ON_PAGE_BREAK_METHOD, ( (PropertyDefn) list
				.get( k++ ) ).getName( ) );

		onprepare = image.getProperty( ImageItem.ON_PREPARE_METHOD )
				.getMethodInfo( );
		iter = onprepare.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		oncreate = image.getProperty( ImageItem.ON_CREATE_METHOD )
				.getMethodInfo( );
		iter = oncreate.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onrender = image.getProperty( ImageItem.ON_RENDER_METHOD )
				.getMethodInfo( );
		iter = onrender.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onpagebreak = image.getProperty( ImageItem.ON_PAGE_BREAK_METHOD )
				.getMethodInfo( );
		iter = onpagebreak.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );
		
		// grid has methods
		IElementDefn grid = instance.getElement( "Grid" ); //$NON-NLS-1$
		list = grid.getMethods( );
		int m = 0;
		assertEquals( GridItem.ON_PREPARE_METHOD, ( (PropertyDefn) list
				.get( m++ ) ).getName( ) );
		assertEquals( GridItem.ON_CREATE_METHOD, ( (PropertyDefn) list
				.get( m++ ) ).getName( ) );
		assertEquals( GridItem.ON_RENDER_METHOD, ( (PropertyDefn) list
				.get( m++ ) ).getName( ) );
		assertEquals( GridItem.ON_PAGE_BREAK_METHOD, ( (PropertyDefn) list
				.get( m++ ) ).getName( ) );

		onprepare = grid.getProperty( GridItem.ON_PREPARE_METHOD )
				.getMethodInfo( );
		iter = onprepare.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		oncreate = grid.getProperty( GridItem.ON_CREATE_METHOD )
				.getMethodInfo( );
		iter = oncreate.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onrender = grid.getProperty( GridItem.ON_RENDER_METHOD )
				.getMethodInfo( );
		iter = onrender.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onpagebreak = grid.getProperty( GridItem.ON_PAGE_BREAK_METHOD )
				.getMethodInfo( );
		iter = onpagebreak.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );
		
		// dynamic text has methods
		IElementDefn textdata = instance.getElement( "TextData" ); //$NON-NLS-1$
		list = textdata.getMethods( );
		int n = 0;
		assertEquals( TextDataItem.ON_PREPARE_METHOD, ( (PropertyDefn) list
				.get( n++ ) ).getName( ) );
		assertEquals( TextDataItem.ON_CREATE_METHOD, ( (PropertyDefn) list
				.get( n++ ) ).getName( ) );
		assertEquals( TextDataItem.ON_RENDER_METHOD, ( (PropertyDefn) list
				.get( n++ ) ).getName( ) );
		assertEquals( TextDataItem.ON_PAGE_BREAK_METHOD, ( (PropertyDefn) list
				.get( n++ ) ).getName( ) );

		onprepare = textdata.getProperty( TextDataItem.ON_PREPARE_METHOD )
				.getMethodInfo( );
		iter = onprepare.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		oncreate = textdata.getProperty( TextDataItem.ON_CREATE_METHOD )
				.getMethodInfo( );
		iter = oncreate.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onrender = textdata.getProperty( TextDataItem.ON_RENDER_METHOD )
				.getMethodInfo( );
		iter = onrender.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onpagebreak = textdata.getProperty( TextDataItem.ON_PAGE_BREAK_METHOD )
				.getMethodInfo( );
		iter = onpagebreak.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );
		
		// table has methods
		IElementDefn table = instance.getElement( "Table" ); //$NON-NLS-1$
		list = table.getMethods( );
		int l = 0;
		assertEquals( TableItem.ON_PREPARE_METHOD, ( (PropertyDefn) list
				.get( l++ ) ).getName( ) );
		assertEquals( TableItem.ON_CREATE_METHOD, ( (PropertyDefn) list
				.get( l++ ) ).getName( ) );
		assertEquals( TableItem.ON_RENDER_METHOD, ( (PropertyDefn) list
				.get( l++ ) ).getName( ) );
		assertEquals( TableItem.ON_PAGE_BREAK_METHOD, ( (PropertyDefn) list
				.get( l++ ) ).getName( ) );

		onprepare = table.getProperty( TableItem.ON_PREPARE_METHOD )
				.getMethodInfo( );
		iter = onprepare.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		oncreate = table.getProperty( TableItem.ON_CREATE_METHOD )
				.getMethodInfo( );
		iter = oncreate.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onrender = table.getProperty( TableItem.ON_RENDER_METHOD )
				.getMethodInfo( );
		iter = onrender.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onpagebreak = table.getProperty( TableItem.ON_PAGE_BREAK_METHOD )
				.getMethodInfo( );
		iter = onpagebreak.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );
		
		// table header/footer/detail row, group header/footer row has methods
		IElementDefn row = instance.getElement( "Row" ); //$NON-NLS-1$
		list = row.getMethods( );
		int p = 0;
		assertEquals( TableRow.ON_PREPARE_METHOD, ( (PropertyDefn) list
				.get( p++ ) ).getName( ) );
		assertEquals( TableRow.ON_CREATE_METHOD, ( (PropertyDefn) list
				.get( p++ ) ).getName( ) );
		assertEquals( TableRow.ON_RENDER_METHOD, ( (PropertyDefn) list
				.get( p++ ) ).getName( ) );
		
		
		onprepare = row.getProperty( TableRow.ON_PREPARE_METHOD )
				.getMethodInfo( );
		iter = onprepare.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		oncreate = row.getProperty( TableRow.ON_CREATE_METHOD )
				.getMethodInfo( );
		iter = oncreate.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onrender = row.getProperty( TableRow.ON_RENDER_METHOD )
				.getMethodInfo( );
		iter = onrender.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		// list has methods
		IElementDefn listitem = instance.getElement( "List" ); //$NON-NLS-1$
		list = listitem.getMethods( );
		int q = 0;
		assertEquals( ListItem.ON_PREPARE_METHOD, ( (PropertyDefn) list
				.get( q++ ) ).getName( ) );
		assertEquals( ListItem.ON_CREATE_METHOD, ( (PropertyDefn) list
				.get( q++ ) ).getName( ) );
		assertEquals( ListItem.ON_RENDER_METHOD, ( (PropertyDefn) list
				.get( q++ ) ).getName( ) );
		assertEquals( ListItem.ON_PAGE_BREAK_METHOD, ( (PropertyDefn) list
				.get( q++ ) ).getName( ) );
		
		onprepare = listitem.getProperty( ListItem.ON_PREPARE_METHOD )
				.getMethodInfo( );
		iter = onprepare.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		oncreate = listitem.getProperty( ListItem.ON_CREATE_METHOD )
				.getMethodInfo( );
		iter = oncreate.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );

		onrender = listitem.getProperty( ListItem.ON_RENDER_METHOD )
				.getMethodInfo( );
		iter = onrender.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );
		

		onpagebreak = listitem.getProperty( ListItem.ON_PAGE_BREAK_METHOD )
				.getMethodInfo( );
		iter = onpagebreak.argumentListIterator( );
		argumentList = (ArgumentInfoList) iter.next( );
		assertNotNull( argumentList.getArgument( "this" ) );
		
	}

}
