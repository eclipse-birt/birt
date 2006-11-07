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

package org.eclipse.birt.report.model.writer;

import java.io.ByteArrayOutputStream;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.util.DocumentUtil;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.metadata.PeerExtensionLoader;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the document related serialization.
 */

public class DocumentUtilTest extends BaseTestCase
{

	/**
	 * Design file name, which tests the element property value localization.
	 */

	private static final String DESIGN_WITH_ELEMENT_EXTENDS = "DocumentUtilTest.xml"; //$NON-NLS-1$

	/**
	 * Design file name, which tests the lib reference with library structures.
	 */

	private static final String DESIGN_WITH_STRUCTURE_REFERENCE = "DocumentUtilTest_1.xml"; //$NON-NLS-1$

	/**
	 * Design file name, which tests indirect element reference of library
	 * elements by inheritance.
	 */

	private static final String DESIGN_WITH_INDIRECT_REFERENCE = "DocumentUtilTest_2.xml"; //$NON-NLS-1$

	/**
	 * Tests the element property value localization.
	 * 
	 * @throws Exception
	 */

	public void testSerializeWithElementExtends( ) throws Exception
	{
		openDesign( DESIGN_WITH_ELEMENT_EXTENDS );
		assertNotNull( designHandle );

		serializeDocument( );
		assertTrue( compareTextFile( "DocumentUtilTest_golden.xml" ) ); //$NON-NLS-1$ 
	}

	/**
	 * Tests the lib reference of embedded images.
	 * 
	 * @throws Exception
	 */

	public void testSerializeWithLibReference( ) throws Exception
	{
		openDesign( DESIGN_WITH_STRUCTURE_REFERENCE );
		assertNotNull( designHandle );

		serializeDocument( );
		assertTrue( compareTextFile( "DocumentUtilTest_golden_1.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the indirect element references by inheritances during the
	 * serialization.
	 * 
	 * @throws Exception
	 */

	public void testSerializeWithIndirectRef( ) throws Exception
	{
		openDesign( DESIGN_WITH_INDIRECT_REFERENCE );
		assertNotNull( designHandle );

		serializeDocument( );
		assertTrue( compareTextFile( "DocumentUtilTest_golden_2.xml" ) ); //$NON-NLS-1$ 
	}

	/**
	 * Tests the serlization of template elements.
	 * 
	 * @throws Exception
	 */

	public void testTemplate( ) throws Exception
	{
		openDesign( "TemplateElementParserTest.xml" ); //$NON-NLS-1$
		assertNotNull( designHandle );

		serializeDocument( );
		assertTrue( compareTextFile( "DocumentUtilTest_golden_3.xml" ) ); //$NON-NLS-1$ 
	}

	/**
	 * when serialize report design, the embedded image from library should be
	 * copied locally.
	 * 
	 * @throws Exception
	 */
	public void testSerializeWithEmbeddedImage( ) throws Exception
	{
		String string = "TestSerializeEmbeddeImage.xml"; //$NON-NLS-1$
		openDesign( string );
		assertNotNull( designHandle );
		serializeDocument( );
		assertTrue( compareTextFile( "DocumentUtilTest_golden_4.xml" ) ); //$NON-NLS-1$ 

	}

	/**
	 * when there is a external resource file sets for this report, all report
	 * properties use the external string value should be saved into the report
	 * file after serialization. And the reource key should be set to null.
	 * 
	 * @throws Exception
	 * 
	 */
	public void testSerializeExternalString( ) throws Exception
	{
		openDesign( "DocumnetUtilTest_ExternalResource.xml" ); //$NON-NLS-1$
		assertNotNull( designHandle );
		serializeDocument( );

		assertTrue( compareTextFile( "DocumentUtilTest_golden_5.xml" ) ); //$NON-NLS-1$ 

	}

	/**
	 * For extended item, the extension name must be set first. So that, other
	 * properties can be set propertyly.
	 * 
	 * @throws Exception
	 */

	public void testExtendedItem( ) throws Exception
	{
		new PeerExtensionLoader( ).load( );

		openDesign( "DocumentUtilTest_ExtendedItem.xml" ); //$NON-NLS-1$

		assertNotNull( designHandle );
		serializeDocument( );
		ExtendedItemHandle matrix1 = (ExtendedItemHandle) designHandle
				.findElement( "matrix1" ); //$NON-NLS-1$
		assertNotNull( ( (ExtendedItem) matrix1.getElement( ) ).getExtDefn( ) );

		assertTrue( compareTextFile( "DocumentUtilTest_golden_6.xml" ) ); //$NON-NLS-1$

	}

	/**
	 * Writes the document to the internal output stream.
	 * 
	 * @throws Exception
	 */

	private void serializeDocument( ) throws Exception
	{
		os = new ByteArrayOutputStream( );
		DocumentUtil.serialize( designHandle, os );
	}
}
