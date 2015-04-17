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

package org.eclipse.birt.core;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.core.template.TemplateParserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({org.eclipse.birt.core.archive.ArchiveFlushTest.class,
        org.eclipse.birt.core.archive.DocArchiveLockManagerTest.class,
        org.eclipse.birt.core.archive.FolderToArchiveTest.class,
        org.eclipse.birt.core.archive.SpecialCharacterTest.class,
        org.eclipse.birt.core.archive.compound.ArchivePerformanceTest.class})

public class AllTests
{
    
    public static Test suite( )
    {
        TestSuite suite = new TestSuite( "Test for org.eclipse.birt.core" );
        // $JUnit-BEGIN$

        /* in package: org.eclipse.birt.core.archive */
        suite.addTestSuite( org.eclipse.birt.core.archive.ArchiveFileCacheTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.ArchiveFileSaveTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.ArchiveUtilTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.DocumentArchiveTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.FileArchiveTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.FolderArchiveTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.InputStreamRefreshTest.class );

        /* in package: org.eclipse.birt.core.archive.compound */
        suite.addTestSuite( org.eclipse.birt.core.archive.compound.ArchiveEntryInputStreamTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.compound.ArchiveFileFactoryTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.compound.ArchiveFileTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.compound.ArchiveRemoveTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.compound.ArchiveViewTest.class );
        suite.addTestSuite( org.eclipse.birt.core.archive.compound.UpgradeArchiveTest.class );

        /* in package: org.eclipse.birt.core.btree */
        suite.addTestSuite( org.eclipse.birt.core.btree.BTreeCursorTest.class );
        suite.addTestSuite( org.eclipse.birt.core.btree.BTreeMultipleThreadTest.class );
        suite.addTestSuite( org.eclipse.birt.core.btree.BTreeTest.class );
        suite.addTestSuite( org.eclipse.birt.core.btree.NodeInputStreamTest.class );
        suite.addTestSuite( org.eclipse.birt.core.btree.NodeOutputStreamTest.class );

        /* in package: org.eclipse.birt.core.config */
        suite.addTestSuite( org.eclipse.birt.core.config.FileConfigVarManagerTest.class );

        /* in package: org.eclipse.birt.core.data */
        suite.addTestSuite( org.eclipse.birt.core.data.DataTypeUtilTest.class );
        suite.addTestSuite( org.eclipse.birt.core.data.DateUtilTest.class );
        suite.addTestSuite( org.eclipse.birt.core.data.DateUtilThreadTest.class );
        suite.addTestSuite( org.eclipse.birt.core.data.ExpressionParserUtilityTest.class );
        suite.addTestSuite( org.eclipse.birt.core.data.ExpressionUtilTest.class ); //

        /* in package: org.eclipse.birt.core.exception */
        suite.addTestSuite( org.eclipse.birt.core.exception.BirtExceptionTest.class );

        /* in package: org.eclipse.birt.core.format */
        suite.addTestSuite( org.eclipse.birt.core.format.DateFormatterTest.class );
        suite.addTestSuite( org.eclipse.birt.core.format.NumberFormatterTest.class );
        suite.addTestSuite( org.eclipse.birt.core.format.StringFormatterTest.class );

        /* in package: org.eclipse.birt.core.script.bre */
        suite.addTestSuite( org.eclipse.birt.core.script.bre.BirtCompTest.class );
        suite.addTestSuite( org.eclipse.birt.core.script.bre.BirtDateTimeTest.class );
        suite.addTestSuite( org.eclipse.birt.core.script.bre.BirtDurationTest.class );
        suite.addTestSuite( org.eclipse.birt.core.script.bre.BirtMathTest.class );
        suite.addTestSuite( org.eclipse.birt.core.script.bre.BirtStrTest.class );

        /* in package: org.eclipse.birt.core.script */
        suite.addTestSuite( org.eclipse.birt.core.script.NativeDateTimeSpanTest.class );
        suite.addTestSuite( org.eclipse.birt.core.script.NativeFinanceTest.class );
        suite.addTestSuite( org.eclipse.birt.core.script.NativeJavaMapTest.class );
        suite.addTestSuite( org.eclipse.birt.core.script.NativeNamedListTest.class );
        suite.addTestSuite( org.eclipse.birt.core.script.ScriptableParametersTest.class );
        suite.addTestSuite( org.eclipse.birt.core.script.ScriptContextTest.class );

        /* in package: org.eclipse.birt.core.template */
        suite.addTestSuite( TemplateParserTest.class );

        /* in package: org.eclipse.birt.core.util */
        suite.addTestSuite( org.eclipse.birt.core.util.IOUtilTest.class );

        // $JUnit-END$
        return suite;
    }

}
