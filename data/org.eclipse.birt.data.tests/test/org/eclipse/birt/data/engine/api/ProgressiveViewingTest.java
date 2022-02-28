/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.junit.Test;

import testutil.BaseTestCase;

/**
 * Test case for scripted data source/data set
 */

public class ProgressiveViewingTest extends BaseTestCase {
	/**
	 * No looking ahead at all.
	 *
	 * @throws BirtException
	 */
	@Test
	public void testProgressiveViewing1() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(this.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(this.getTempDir());
		DataEngine dataEngine = DataEngine.newDataEngine(platformConfig, context);

		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign("ds");
		dataSource.setOpenScript("i = 0;");
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign("test");
		dataSet.setDataSource("ds");

		dataSet.addResultSetHint(new ColumnDefinition("column1"));

		dataSet.setFetchScript(" i++; if ( i % 11 == 0 ) return false; row.column1 = i;" + "return true;");

		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(dataSet);

		QueryDefinition qd = new QueryDefinition();

		qd.addBinding(new Binding("column1", new ScriptExpression("i", DataType.INTEGER_TYPE)));
		qd.setDataSetName("test");
		Map appContextMap = new HashMap();
		IResultIterator ri1 = dataEngine.prepare(qd, appContextMap).execute(null).getResultIterator();

		assertFalse(((DataEngineImpl) dataEngine).getSession().getDataSetCacheManager().doesLoadFromCache());
		// Please note here the progressive viewing feature is invoked.
		int i = 0;
		while (ri1.next()) {
			assertEquals(((Integer) ri1.getValue("column1")).intValue(), ++i);
		}
		dataEngine.shutdown();

	}

	/**
	 * Looking ahead for 1 row.
	 *
	 * @throws BirtException
	 */
	@Test
	public void testProgressiveViewing2() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(this.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(this.getTempDir());
		DataEngine dataEngine = DataEngine.newDataEngine(platformConfig, context);

		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign("ds");
		dataSource.setOpenScript("i = 0;");
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign("test");
		dataSet.setDataSource("ds");

		dataSet.addResultSetHint(new ColumnDefinition("column1"));

		dataSet.setFetchScript(" i++; if ( i % 11 == 0 ) return false; row.column1 = i;" + "return true;");

		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(dataSet);

		QueryDefinition qd = new QueryDefinition();
		// Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults(true);
		qd.addBinding(new Binding("column1", new ScriptExpression("i", DataType.INTEGER_TYPE)));
		qd.setDataSetName("test");
		Map appContextMap = new HashMap();
		IResultIterator ri1 = dataEngine.prepare(qd, appContextMap).execute(null).getResultIterator();

		assertFalse(((DataEngineImpl) dataEngine).getSession().getDataSetCacheManager().doesLoadFromCache());
		// Please note here the progressive viewing feature is invoked.
		int i = 0;
		while (ri1.next()) {
			assertEquals(((Integer) ri1.getValue("column1")).intValue(), ++i + 1);
		}
		dataEngine.shutdown();

	}

	/**
	 * Looking ahead for all row because exist overall aggregation, and the
	 * aggregation value is fetched in the beginning.
	 *
	 * @throws BirtException
	 */
	@Test
	public void testProgressiveViewing3() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(this.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(this.getTempDir());
		DataEngine dataEngine = DataEngine.newDataEngine(platformConfig, context);

		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign("ds");
		dataSource.setOpenScript("i = 0;");
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign("test");
		dataSet.setDataSource("ds");

		dataSet.addResultSetHint(new ColumnDefinition("column1"));

		dataSet.setFetchScript(" i++; if ( i % 11 == 0 ) return false; row.column1 = i;" + "return true;");

		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(dataSet);

		QueryDefinition qd = new QueryDefinition();
		// Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults(true);
		Binding aggregation = new Binding("aggr", new ScriptExpression("row[\"column1\"]"));
		aggregation.setAggrFunction("count");

		qd.addBinding(new Binding("column1", new ScriptExpression("i", DataType.INTEGER_TYPE)));
		qd.addBinding(aggregation);
		qd.setDataSetName("test");
		Map appContextMap = new HashMap();
		IResultIterator ri1 = dataEngine.prepare(qd, appContextMap).execute(null).getResultIterator();

		assertFalse(((DataEngineImpl) dataEngine).getSession().getDataSetCacheManager().doesLoadFromCache());
		// Please note here the progressive viewing feature is invoked.
		int i = 0;
		while (ri1.next()) {
			assertEquals(((Integer) ri1.getValue("aggr")).intValue(), 10);
			assertEquals(((Integer) ri1.getValue("column1")).intValue(), 11);
		}
		dataEngine.shutdown();

	}

	/**
	 * Looking ahead for 1 even there exist overall aggregation, and the aggregation
	 * value is fetched in the beginning.
	 *
	 * @throws BirtException
	 */
	@Test
	public void testProgressiveViewing4() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(this.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(this.getTempDir());
		DataEngine dataEngine = DataEngine.newDataEngine(platformConfig, context);

		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign("ds");
		dataSource.setOpenScript("i = 0;");
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign("test");
		dataSet.setDataSource("ds");

		dataSet.addResultSetHint(new ColumnDefinition("column1"));

		dataSet.setFetchScript(" i++; if ( i % 11 == 0 ) return false; row.column1 = i;" + "return true;");

		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(dataSet);

		QueryDefinition qd = new QueryDefinition();
		// Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults(true);
		Binding aggregation = new Binding("aggr", new ScriptExpression("row[\"column1\"]"));
		aggregation.setAggrFunction("count");

		qd.addBinding(new Binding("column1", new ScriptExpression("i", DataType.INTEGER_TYPE)));
		qd.addBinding(aggregation);
		qd.setDataSetName("test");
		Map appContextMap = new HashMap();
		IResultIterator ri1 = dataEngine.prepare(qd, appContextMap).execute(null).getResultIterator();

		assertFalse(((DataEngineImpl) dataEngine).getSession().getDataSetCacheManager().doesLoadFromCache());
		// Please note here the progressive viewing feature is invoked.
		int i = 0;
		while (ri1.next()) {
			assertEquals(((Integer) ri1.getValue("column1")).intValue(), 11);
		}
		assertEquals(((Integer) ri1.getValue("aggr")).intValue(), 10);
		dataEngine.shutdown();

	}

	/**
	 * Looking ahead for 1 even there exist overall aggregation, and the aggregation
	 * value is fetched in the beginning.
	 *
	 * @throws BirtException
	 */
	@Test
	public void testProgressiveViewing5() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(this.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(this.getTempDir());
		DataEngine dataEngine = DataEngine.newDataEngine(platformConfig, context);

		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign("ds");
		dataSource.setOpenScript("i = 0;");
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign("test");
		dataSet.setDataSource("ds");

		dataSet.addResultSetHint(new ColumnDefinition("column1"));

		dataSet.setFetchScript(" i++; if ( i % 11 == 0 ) return false; row.column1 = i;" + "return true;");

		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(dataSet);

		QueryDefinition qd = new QueryDefinition();
		// Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults(true);
		Binding aggregation = new Binding("aggr", new ScriptExpression("row[\"column1\"]"));
		aggregation.setAggrFunction("runningcount");

		qd.addBinding(new Binding("column1", new ScriptExpression("i", DataType.INTEGER_TYPE)));
		qd.addBinding(aggregation);
		qd.setDataSetName("test");
		Map appContextMap = new HashMap();
		IResultIterator ri1 = dataEngine.prepare(qd, appContextMap).execute(null).getResultIterator();

		assertFalse(((DataEngineImpl) dataEngine).getSession().getDataSetCacheManager().doesLoadFromCache());
		// Please note here the progressive viewing feature is invoked.
		int i = 1;
		while (ri1.next()) {
			assertEquals(((Integer) ri1.getValue("aggr")).intValue(), i);
			i++;
		}
		dataEngine.shutdown();
	}

	/**
	 * Filter on non-aggr column
	 *
	 * @throws BirtException
	 */
	@Test
	public void testProgressiveViewing6() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(this.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(this.getTempDir());
		DataEngine dataEngine = DataEngine.newDataEngine(platformConfig, context);

		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign("ds");
		dataSource.setOpenScript("i = 0;");
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign("test");
		dataSet.setDataSource("ds");

		dataSet.addResultSetHint(new ColumnDefinition("column1"));

		dataSet.setFetchScript(" i++; if ( i % 11 == 0 ) return false; row.column1 = i;" + "return true;");

		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(dataSet);

		QueryDefinition qd = new QueryDefinition();
		// Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults(true);
		qd.addBinding(new Binding("column1", new ScriptExpression("i", DataType.INTEGER_TYPE)));
		qd.setDataSetName("test");
		FilterDefinition filter = new FilterDefinition(new ScriptExpression("row.column1 != 5;"));
		qd.addFilter(filter);
		Map appContextMap = new HashMap();
		IResultIterator ri1 = dataEngine.prepare(qd, appContextMap).execute(null).getResultIterator();

		assertFalse(((DataEngineImpl) dataEngine).getSession().getDataSetCacheManager().doesLoadFromCache());
		// Please note here the progressive viewing feature is invoked.
		int i = 0;
		while (ri1.next()) {
			int resultValue = ((Integer) ri1.getValue("column1")).intValue();
			if (resultValue == 6) {
				++i;
			}
			int targetValue = ++i + 1;
			assertEquals(resultValue, targetValue);
		}
		dataEngine.shutdown();

	}

	/**
	 * Filter on aggregation, not qualify for progressive viewing
	 *
	 * @throws BirtException
	 */
	@Test
	public void testProgressiveViewing7() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(this.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(this.getTempDir());
		DataEngine dataEngine = DataEngine.newDataEngine(platformConfig, context);

		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign("ds");
		dataSource.setOpenScript("i = 0;");
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign("test");
		dataSet.setDataSource("ds");

		dataSet.addResultSetHint(new ColumnDefinition("column1"));

		dataSet.setFetchScript(" i++; if ( i % 11 == 0 ) return false; row.column1 = i;" + "return true;");

		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(dataSet);

		QueryDefinition qd = new QueryDefinition();
		// Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults(true);
		Binding aggregation = new Binding("aggr", new ScriptExpression("row[\"column1\"]"));
		aggregation.setAggrFunction("count");

		qd.addBinding(new Binding("column1", new ScriptExpression("i", DataType.INTEGER_TYPE)));
		qd.addBinding(aggregation);
		qd.setDataSetName("test");

		FilterDefinition filter = new FilterDefinition(new ScriptExpression("row.aggr != 5;"));
		qd.addFilter(filter);

		Map appContextMap = new HashMap();
		IResultIterator ri1 = dataEngine.prepare(qd, appContextMap).execute(null).getResultIterator();

		assertFalse(((DataEngineImpl) dataEngine).getSession().getDataSetCacheManager().doesLoadFromCache());

		while (ri1.next()) {
			assertEquals(((Integer) ri1.getValue("column1")).intValue(), 11);
		}
		assertEquals(((Integer) ri1.getValue("aggr")).intValue(), 10);
		dataEngine.shutdown();

	}

	/**
	 * Filter on aggregation, not qualify for progressive viewing
	 *
	 * @throws BirtException
	 */
	@Test
	public void testProgressiveViewing8() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(this.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(this.getTempDir());
		DataEngine dataEngine = DataEngine.newDataEngine(platformConfig, context);

		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign("ds");
		dataSource.setOpenScript("i = 0;");
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign("test");
		dataSet.setDataSource("ds");

		dataSet.addResultSetHint(new ColumnDefinition("column1"));

		dataSet.setFetchScript(" i++; if ( i % 11 == 0 ) return false; row.column1 = i;" + "return true;");

		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(dataSet);

		QueryDefinition qd = new QueryDefinition();
		// Use the cache query results setting to ensure 1 row looking ahead
		qd.setCacheQueryResults(true);
		Binding aggregation = new Binding("aggr", new ScriptExpression("row[\"column1\"]"));
		aggregation.setAggrFunction("count");

		Binding indirectAggregation = new Binding("column2", new ScriptExpression("row[\"aggr\"]"));
		qd.addBinding(new Binding("column1", new ScriptExpression("i", DataType.INTEGER_TYPE)));
		qd.addBinding(aggregation);
		qd.addBinding(indirectAggregation);
		qd.setDataSetName("test");

		FilterDefinition filter = new FilterDefinition(new ScriptExpression("row.column2 != 5;"));
		qd.addFilter(filter);

		Map appContextMap = new HashMap();
		IResultIterator ri1 = dataEngine.prepare(qd, appContextMap).execute(null).getResultIterator();

		assertFalse(((DataEngineImpl) dataEngine).getSession().getDataSetCacheManager().doesLoadFromCache());

		while (ri1.next()) {
			assertEquals(((Integer) ri1.getValue("column1")).intValue(), 11);
		}
		assertEquals(((Integer) ri1.getValue("aggr")).intValue(), 10);
		dataEngine.shutdown();

	}
}
