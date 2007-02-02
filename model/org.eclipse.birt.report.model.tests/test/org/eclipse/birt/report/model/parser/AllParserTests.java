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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in extension package.
 */

public class AllParserTests
{

	/**
	 * @return the test
	 */

	public static Test suite( )
	{
		TestSuite test = new TestSuite( );

		test.addTestSuite( ActionParseTest.class );
		test.addTestSuite( CachedMetaDataParserTest.class );
		test.addTestSuite( CascadingParameterGroupParserTest.class );
		test.addTestSuite( ChoiceParseTest.class );
		test.addTestSuite( CompatibleBoundColumnsTest.class );
		test.addTestSuite( ComponentScratchPadTest.class );
		test.addTestSuite( CompoundExtendParserTest.class );
		test.addTestSuite( CubeParserTest.class );
		test.addTestSuite( DataItemParseTest.class );
		test.addTestSuite( FreeFormParseTest.class );
		test.addTestSuite( GridItemParseTest.class );
		test.addTestSuite( ImageItemParseTest.class );
		test.addTestSuite( JointDataSetParseTest.class );
		test.addTestSuite( LabelItemParserTest.class );
		test.addTestSuite( LineItemParseTest.class );
		test.addTestSuite( ListItemParseTest.class );
		test.addTestSuite( MasterPageParseTest.class );
		test.addTestSuite( OdaDataSetParserTest.class );
		test.addTestSuite( OdaDataSourceParseTest.class );
		test.addTestSuite( ParameterGroupTest.class );
		test.addTestSuite( ParserCompatibilityTest.class );
		test.addTestSuite( PropertyBindingTest.class );
		test.addTestSuite( PropertyStateTest.class );
		test.addTestSuite( RectangleItemParseTest.class );
		test.addTestSuite( ReportDesignParseTest.class );
		test.addTestSuite( ReportElementParseTest.class );
		test.addTestSuite( ReportItemParseTest.class );
		test.addTestSuite( ScalarParameterParseTest.class );
		test.addTestSuite( ScriptDataSetParseTest.class );
		test.addTestSuite( ScriptDataSourceParseTest.class );
		test.addTestSuite( SortingParserTest.class );
		test.addTestSuite( StyleParseTest.class );
		test.addTestSuite( TableItemParseTest.class );
		test.addTestSuite( TemplateElementParserTest.class );
		test.addTestSuite( TextDataItemParseTest.class );
		test.addTestSuite( TextItemParseTest.class );
		test.addTestSuite( UserPropertyTest.class );


		// add all test classes here

		return test;
	}
}
