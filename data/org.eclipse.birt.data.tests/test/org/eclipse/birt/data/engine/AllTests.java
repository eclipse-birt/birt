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

package org.eclipse.birt.data.engine;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suit for data engine. 
 */
public class AllTests
{
	
	/**
	 * @return
	 */
	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for org.eclipse.birt.data.engine" );
		
		/* in package: org.eclipse.birt.data.engine.aggregation */
		suite.addTestSuite( org.eclipse.birt.data.engine.aggregation.FinanceTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.aggregation.TotalTest.class );
		
		/* in package org.eclipse.birt.data.engine.reg */
		suite.addTestSuite( org.eclipse.birt.data.engine.regre.DataSourceTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.regre.FeatureTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.regre.SortTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.regre.SortHintTest.class);
		
		/* in package org.eclipse.birt.data.engine.api */
		suite.addTestSuite( org.eclipse.birt.data.engine.api.ClobAndBlobTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.DataSetCacheTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.DteLevelDataSetCacheTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.GroupLevelTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.ScriptedDSTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.ScriptTest.class );
		// ?? suite.addTestSuite( org.eclipse.birt.data.engine.api.StoredProcedureTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.UsesDetailFalseTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.ProgressiveViewingTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.NoUpdateAggrFilterTest.class );
		
		/* in package org.eclipse.birt.data.engine.binding */
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.ColumnBindingTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.ColumnHintTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.ComputedColumnTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.DataSetCacheTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.DefineDataSourceTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.DistinctValueTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.FeaturesTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.FilterByRowTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.GroupOnRowTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.InputParameterTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.MaxRowsTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.MultiplePassTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.NestedQueryTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.QueryCacheTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.SubQueryTest.class );
				
		/* in package org.eclipse.birt.data.engine.binding.newbinding */
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.newbinding.MultiplePassTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.newbinding.ColumnBindingTest.class );
		
		/* in package org.eclipse.birt.data.engine.executor.cache */
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.cache.CacheClobAndBlobTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.cache.CacheComputedColumnTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.cache.CachedMultiplePassTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.cache.CacheFeaturesTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.cache.CacheNestedQueryTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.cache.CacheSortTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.cache.CacheSubqueryTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.cache.MemoryCacheTest.class );
		
		/* in package org.eclipse.birt.data.engine.executor.transform */
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.transform.CachedResultSetTest.class );
		
		/* in package org.eclipse.birt.data.engine.executor.transform.group */
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.transform.group.GroupByDistinctValueTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.transform.group.GroupByRowKeyCountTest.class);
		
		/* in package org.eclipse.birt.data.engine.expression */
		suite.addTestSuite( org.eclipse.birt.data.engine.expression.ComplexExpressionCompilerTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.expression.ExpressionCompilerTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.expression.ExpressionCompilerUtilTest.class);
		
		/* in package org.eclipse.birt.data.engine.impl.rd */
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.rd.ViewingTest2.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.rd.ReportDocumentTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.rd.ReportDocumentTest2.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.rd.ViewingTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.rd.SummaryIVTest.class);
		
		/* in package org.eclipse.birt.data.engine.impl */
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.AggregationTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.ExprManagerUtilTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.JointDataSetTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.ResultMetaDataTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.ScriptEvalTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.ConfigFileParserTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.IncreCacheDataSetTest.class);
		
		
		/* in package org.eclipse.birt.data.engine.impl.binding  */
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.binding.AggregationTest.class );
		/* in package org.eclipse.birt.data.engine.impl.document */
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.document.GroupInfoUtilTest.class);
		
		/* in package org.eclipse.birt.data.engine.impl */
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.util.DirectedGraphTest.class );
		
		/* in package org.eclipse.birt.data.engine.olap.api */
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.api.CubeFeaturesTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.api.CubeIVTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.api.CubeDrillFeatureTest.class);
		
		/* in package org.eclipse.birt.data.engine.olap.data.document*/
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.document.BufferedRandomAccessObjectTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.document.CachedDocumentObjectManagerTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.document.DocumentManagerTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.document.FileDocumentManagerTest.class );
		
		/* in package org.eclipse.birt.data.engine.olap.data.impl*/
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.CubeAggregationTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.DimensionKeyTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.LevelMemberTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.TraversalorTest.class );
		
		/* in package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function*/
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.MonthToDateTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.YearToDateFunctionTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.QuarterToDateFunctionTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.PreviousNPeriodsFunctionTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.WeekToDateTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.TrailingTest.class );
		
		/* in package org.eclipse.birt.data.engine.olap.data.impl.dimension */
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionTest2.class );
		
		/* in package org.eclipse.birt.data.engine.olap.data.impl.facttable */
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.facttable.DimensionSegmentsTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableHelperTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableHelperTest2.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableRowTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableRowIteratorWithFilterTest.class );
		
		/* in package org.eclipse.birt.data.engine.olap.data.util */
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArrayTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.util.BufferedRandomAccessFileTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArrayTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.util.DiskIndexTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.util.DiskSortedStackTest.class );

		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.util.ObjectArrayUtilTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.util.PrimaryDiskArrayTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.util.PrimarySortedStackTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.util.SetUtilTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.data.util.StructureDiskArrayTest.class );
		
		/* in package org.eclipse.birt.data.engine.olap.util.filter */
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.util.filter.CubePosFilterTest.class );
		
		/* in package org.eclipse.birt.data.engine.olap.cursor */

		suite.addTestSuite( org.eclipse.birt.data.engine.olap.cursor.CursorNavigatorTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.cursor.CursorModelTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.cursor.MirrorCursorModelTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.cursor.MirrorCursorNavigatorTest.class );
		
		/* in package org.eclipse.birt.data.engine.olap.util */
		suite.addTestSuite( org.eclipse.birt.data.engine.olap.util.OlapExpressionUtilTest.class );
		
		return suite;
	}
	
}