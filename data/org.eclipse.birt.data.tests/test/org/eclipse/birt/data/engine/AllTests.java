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
		
		/* in package org.eclipse.birt.data.engine.api */
		suite.addTestSuite( org.eclipse.birt.data.engine.api.ClobAndBlobTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.DataSetCacheTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.GroupLevelTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.ScriptedDSTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.ScriptTest.class );
		// ?? suite.addTestSuite( org.eclipse.birt.data.engine.api.StoredProcedureTest.class );
		suite.addTestSuite( org.eclipse.birt.data.engine.api.UsesDetailFalseTest.class );
		
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
		suite.addTestSuite( org.eclipse.birt.data.engine.binding.SubQueryTest.class );
				
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
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.transform.group.GroupByNumberRangeTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.transform.group.GroupByPositionRangeTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.executor.transform.group.GroupByStringRangeTest.class);
		
		/* in package org.eclipse.birt.data.engine.expression */
		suite.addTestSuite( org.eclipse.birt.data.engine.expression.ComplexExpressionCompilerTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.expression.ExpressionCompilerTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.expression.ExpressionCompilerUtilTest.class);
		
		/* in package org.eclipse.birt.data.engine.impl */
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.AggregationTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.ExprManagerUtilTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.JointDataSetTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.ResultMetaDataTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.ScriptEvalTest.class);
		
		/* in package org.eclipse.birt.data.engine.impl.document */
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.document.GroupInfoUtilTest.class);
		
		/* in package org.eclipse.birt.data.engine.impl.rd */
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.rd.ReportDocumentTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.rd.ReportDocumentTest2.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.rd.ViewingTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.impl.rd.ViewingTest2.class);
		
		/* in package org.eclipse.birt.data.engine.reg */
		suite.addTestSuite( org.eclipse.birt.data.engine.regre.DataSourceTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.regre.FeatureTest.class);
		suite.addTestSuite( org.eclipse.birt.data.engine.regre.SortTest.class);

		return suite;
	}
	
}