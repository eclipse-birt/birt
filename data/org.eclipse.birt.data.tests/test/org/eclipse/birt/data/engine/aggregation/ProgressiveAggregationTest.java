/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.aggregation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.junit.Test;

import testutil.BaseTestCase;

/**
 * @since 3.3
 *
 */
public class ProgressiveAggregationTest extends BaseTestCase {

	/**
	 * Tests the return value of the "count" aggregation function when called on an
	 * empty result set.
	 * 
	 * @throws BirtException
	 */
	@Test
	public void testEmptyResultSet() throws BirtException {

		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(BaseTestCase.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(BaseTestCase.getTempDir());
		DataEngine dataEngine = DataEngine.newDataEngine(platformConfig, context);

		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign("ds");
		dataSource.setOpenScript("i = 0;");
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign("test");
		dataSet.setDataSource("ds");

		dataSet.addResultSetHint(new ColumnDefinition("column1"));

		dataSet.setFetchScript(" i++; return false");

		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(dataSet);

		QueryDefinition qd = new QueryDefinition();

		qd.addBinding(new Binding("column1", new ScriptExpression("dataSetRow[\"column1\"]", DataType.INTEGER_TYPE)));
		IBinding total1 = new Binding("column2", new ScriptExpression(null));
		total1.setAggrFunction("count");
		qd.addBinding(total1);

		qd.setDataSetName("test");
		Map<?, ?> appContextMap = new HashMap<>();
		IResultIterator ri1 = dataEngine.prepare(qd, appContextMap).execute(null).getResultIterator();

		assertFalse(ri1.next()); /* ResultSet should be empty */
		assertEquals(ri1.getInteger("column2").intValue(), 0); /* Count should return default value 0 */

		dataEngine.shutdown();
	}

}
