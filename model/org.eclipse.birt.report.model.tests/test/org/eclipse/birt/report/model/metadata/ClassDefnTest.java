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

package org.eclipse.birt.report.model.metadata;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;

/**
 * Test class definition.
 */

public class ClassDefnTest extends AbstractMetaTest
{
	/**
	 * Test the meta-data parser for class definition.
	 * 
	 * @throws MetaDataParserException
	 *             if any exception.
	 */

	public void testParseRomDef( ) throws MetaDataParserException
	{
		loadMetaData( ClassDefnTest.class
				.getResourceAsStream( "input/ClassDefnTest.def" ) ); //$NON-NLS-1$

		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		IClassInfo classDefn = dd.getClass( "MyClass" ); //$NON-NLS-1$
		assertNotNull( classDefn );

		assertEquals( "MyClass", classDefn.getName( ) ); //$NON-NLS-1$
		assertEquals( "Class.MyClass", classDefn.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals( "Class.MyClass.toolTip", classDefn.getToolTipKey( ) ); //$NON-NLS-1$
		assertTrue( classDefn.isNative( ) );

		IMethodInfo constructor = classDefn.getConstructor( );
		assertNotNull( constructor );
		assertEquals( "MyConstructor", constructor.getName( ) ); //$NON-NLS-1$
		assertEquals(
				"Class.MyClass.Constructor", constructor.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals(
				"Class.MyClass.Constructor.toolTip", constructor.getToolTipKey( ) ); //$NON-NLS-1$

		Iterator iter = constructor.argumentListIterator( );
		ArgumentInfoList arg1 = (ArgumentInfoList) iter.next( );

		iter = arg1.argumentsIterator( );

		ArgumentInfo arg = (ArgumentInfo) iter.next( );
		assertNotNull( arg );
		assertFalse( iter.hasNext( ) );
		assertEquals( "pos", arg.getName( ) ); //$NON-NLS-1$
		assertEquals( "Number", arg.getType( ) ); //$NON-NLS-1$
		assertEquals( "Class.MyClass.Constructor.pos", arg.getDisplayNameKey( ) ); //$NON-NLS-1$

		List members = classDefn.getMembers( );
		assertEquals( 2, members.size( ) );

		MemberInfo member = (MemberInfo) members.get( 0 );
		assertEquals( "columnName", member.getName( ) ); //$NON-NLS-1$
		assertEquals( "String", member.getDataType( ) ); //$NON-NLS-1$
		assertEquals( "Class.MyClass.columnName", member.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals(
				"Class.MyClass.columnName.toolTip", member.getToolTipKey( ) ); //$NON-NLS-1$

		member = (MemberInfo) members.get( 1 );
		assertEquals( "columnType", member.getName( ) ); //$NON-NLS-1$
		assertEquals( "String", member.getDataType( ) ); //$NON-NLS-1$
		assertEquals( "Class.MyClass.columnType", member.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals(
				"Class.MyClass.columnType.toolTip", member.getToolTipKey( ) ); //$NON-NLS-1$

		List methods = classDefn.getMethods( );
		assertEquals( 2, methods.size( ) );

		MethodInfo method = (MethodInfo) methods.get( 0 );
		assertEquals( "getColumnName", method.getName( ) ); //$NON-NLS-1$
		assertEquals( "String", method.getReturnType( ) ); //$NON-NLS-1$
		assertEquals( "Class.MyClass.getColumnName", method.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals(
				"Class.MyClass.getColumnName.toolTip", method.getToolTipKey( ) ); //$NON-NLS-1$
		assertFalse( method.isStatic( ) );

		method = (MethodInfo) methods.get( 1 );
		assertEquals( "addColumn", method.getName( ) ); //$NON-NLS-1$
		assertEquals( null, method.getReturnType( ) );
		assertEquals( "Class.MyClass.addColumn", method.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals( "Class.MyClass.addColumn.toolTip", method.getToolTipKey( ) ); //$NON-NLS-1$
		assertTrue( method.isStatic( ) );

		iter = method.argumentListIterator( );
		iter = ( (ArgumentInfoList) iter.next( ) ).argumentsIterator( );

		assertTrue( iter.hasNext( ) );
		assertTrue( iter.hasNext( ) );

		classDefn = dd.getClass( "MyClass1" ); //$NON-NLS-1$
		assertNotNull( classDefn );

		assertEquals( "MyClass1", classDefn.getName( ) ); //$NON-NLS-1$
		assertEquals( "Class.MyClass1", classDefn.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals( "Class.MyClass1.toolTip", classDefn.getToolTipKey( ) ); //$NON-NLS-1$
		assertFalse( classDefn.isNative( ) );
	}

	/**
	 * Test whether optional argument can be displayed correctly.
	 * 
	 */

	public void testOptionalArgument( )
	{
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		IClassInfo classDefn = dd.getClass( "String" ); //$NON-NLS-1$
		assertNotNull( classDefn );

		IMethodInfo method = classDefn.getMethod( "fromCharCode" ); //$NON-NLS-1$

		for ( Iterator iter1 = method.argumentListIterator( ); iter1.hasNext( ); )
		{
			ArgumentInfoList argumentList = (ArgumentInfoList) iter1.next( );
			for ( Iterator iter2 = argumentList.argumentsIterator( ); iter2
					.hasNext( ); )
			{
				ArgumentInfo argu = (ArgumentInfo) iter2.next( );
				if ( ArgumentInfo.OPTIONAL_ARGUMENT_NAME.equalsIgnoreCase( argu
						.getName( ) ) )
					assertEquals( "...", argu.getDisplayName( ) ); //$NON-NLS-1$
			}
		}

	}

	/**
	 * Test the meta-data parser for class definition.
	 */

	public void testMethodsWithDifferentArgumentLists( )
	{
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );

		assertEquals( 11, dd.getClasses( ).size( ) );

		IClassInfo classInfo = dd.getClass( "Array" ); //$NON-NLS-1$
		IMethodInfo constructor = classInfo.getConstructor( );
		assertNotNull( constructor );

		Iterator argumentList = constructor.argumentListIterator( );
		assertTrue( argumentList.hasNext( ) );

		// first constructor without arguments

		Iterator arguments = ( (ArgumentInfoList) argumentList.next( ) )
				.argumentsIterator( );
		assertFalse( arguments.hasNext( ) );

		// second constructor with one argument,

		arguments = ( (ArgumentInfoList) argumentList.next( ) )
				.argumentsIterator( );
		assertTrue( arguments.hasNext( ) );
		arguments.next( );
		assertFalse( arguments.hasNext( ) );

		// third constructor with two arguments,

		arguments = ( (ArgumentInfoList) argumentList.next( ) )
				.argumentsIterator( );
		assertTrue( arguments.hasNext( ) );
		arguments.next( );
		assertTrue( arguments.hasNext( ) );
		arguments.next( );
		assertFalse( argumentList.hasNext( ) );

		classInfo = dd.getClass( "Total" ); //$NON-NLS-1$
		IMethodInfo method = classInfo.getMethod( "rank" ); //$NON-NLS-1$

		argumentList = method.argumentListIterator( );

		arguments = ( (ArgumentInfoList) argumentList.next( ) )
				.argumentsIterator( );
		assertTrue( arguments.hasNext( ) );
		ArgumentInfo argument = (ArgumentInfo) arguments.next( );
		assertEquals( "boolean", argument.getType( ) ); //$NON-NLS-1$ 
		//for bug 189573,184808,remove filter and group method.
		assertFalse( arguments.hasNext( ) );

		assertFalse( argumentList.hasNext( ) );

	}

	/**
	 * Test the classes defined in rom.def.
	 * 
	 */

	public void testClassesInRom( )
	{
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );

		assertEquals( 11, dd.getClasses( ).size( ) );

		IClassInfo classInfo = dd.getClass( "String" ); //$NON-NLS-1$ 
		assertNotNull( classInfo );
		assertNotNull( classInfo.getConstructor( ) );
		assertEquals( 1, classInfo.getMembers( ).size( ) );
		assertEquals( 19, classInfo.getMethods( ).size( ) );

		classInfo = dd.getClass( "Global" ); //$NON-NLS-1$ 
		assertNotNull( classInfo );
		assertNull( classInfo.getConstructor( ) );
		assertEquals( 3, classInfo.getMembers( ).size( ) );
		assertEquals( 11, classInfo.getMethods( ).size( ) );

		classInfo = dd.getClass( "GlobalBirt" ); //$NON-NLS-1$ 
		assertNull( classInfo );
		// assertNull( classInfo.getConstructor( ) );
		// assertEquals( 6, classInfo.getMembers( ).size( ) );
		// assertEquals( 0, classInfo.getMethods( ).size( ) );
		// assertFalse( classInfo.isNative( ) );

		classInfo = dd.getClass( "Array" ); //$NON-NLS-1$ 
		assertNotNull( classInfo );
		assertNotNull( classInfo.getConstructor( ) );
		assertEquals( 1, classInfo.getMembers( ).size( ) );
		assertEquals( 12, classInfo.getMethods( ).size( ) );

		classInfo = dd.getClass( "Number" ); //$NON-NLS-1$ 
		assertNotNull( classInfo );
		assertNotNull( classInfo.getConstructor( ) );
		assertEquals( 5, classInfo.getMembers( ).size( ) );
		assertEquals( 6, classInfo.getMethods( ).size( ) );

		IMemberInfo memberInfo = classInfo.getMember( "NaN" ); //$NON-NLS-1$
		assertTrue( memberInfo.isStatic( ) );

		assertNotNull( dd.getClass( "Date" ) ); //$NON-NLS-1$
		classInfo = dd.getClass( "Date" ); //$NON-NLS-1$ 
		assertNotNull( classInfo );
		assertNotNull( classInfo.getConstructor( ) );
		assertEquals( 0, classInfo.getMembers( ).size( ) );
		assertEquals( 46, classInfo.getMethods( ).size( ) );

		assertNotNull( dd.getClass( "Boolean" ) ); //$NON-NLS-1$
		classInfo = dd.getClass( "Boolean" ); //$NON-NLS-1$ 
		assertNotNull( classInfo );
		assertNotNull( classInfo.getConstructor( ) );
		assertEquals( 0, classInfo.getMembers( ).size( ) );
		assertEquals( 2, classInfo.getMethods( ).size( ) );
		assertTrue( classInfo.isNative( ) );

		assertNotNull( dd.getClass( "Math" ) ); //$NON-NLS-1$
		classInfo = dd.getClass( "Math" ); //$NON-NLS-1$ 
		assertNotNull( classInfo );
		assertNull( classInfo.getConstructor( ) );
		assertEquals( 8, classInfo.getMembers( ).size( ) );
		assertEquals( 18, classInfo.getMethods( ).size( ) );

		classInfo = dd.getClass( "Object" ); //$NON-NLS-1$ 
		assertNotNull( classInfo );
		assertNotNull( classInfo.getConstructor( ) );
		assertEquals( 0, classInfo.getMembers( ).size( ) );
		assertEquals( 6, classInfo.getMethods( ).size( ) );

		assertNotNull( dd.getClass( "RegExp" ) ); //$NON-NLS-1$
		classInfo = dd.getClass( "RegExp" ); //$NON-NLS-1$ 
		assertNotNull( classInfo );
		assertNotNull( classInfo.getConstructor( ) );
		assertEquals( 4, classInfo.getMembers( ).size( ) );
		assertEquals( 3, classInfo.getMethods( ).size( ) );
		
		assertNotNull( dd.getClass( "JSON" ) ); //$NON-NLS-1$
		classInfo = dd.getClass( "JSON" ); //$NON-NLS-1$ 
		assertNotNull( classInfo );
		assertNull( classInfo.getConstructor( ) );
		assertEquals( 2, classInfo.getMethods( ).size( ) );
	}
}