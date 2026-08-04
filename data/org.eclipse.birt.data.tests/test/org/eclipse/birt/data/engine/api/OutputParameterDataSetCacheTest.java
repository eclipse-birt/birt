/*******************************************************************************
 * Copyright (c) 2026 Eclipse contributors and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.birt.data.engine.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.IEngineExecutionHints;
import org.junit.Test;

import testutil.BaseTestCase;

/**
 * Data sets with output parameters must not be implicitly cached (issue #2440).
 */
public class OutputParameterDataSetCacheTest extends BaseTestCase {

	@Test
	public void testOutputParameterDataSetIsNotImplicitlyCached() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(this.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(this.getTempDir());
		DataEngine dataEngine = DataEngine.newDataEngine(platformConfig, context);

		OdaDataSourceDesign dataSource = new OdaDataSourceDesign("ds");
		dataSource.setExtensionID("org.eclipse.birt.report.data.oda.jdbc");

		OdaDataSetDesign spDataSet = new OdaDataSetDesign("spWithOutputParam", "ds");
		spDataSet.setExtensionID("org.eclipse.birt.report.data.oda.jdbc.SPSelectDataSet");
		spDataSet.setQueryText("{call sp_part_weight(?,?)}");
		spDataSet.addParameter(new ParameterDefinition("p_part", DataType.INTEGER_TYPE, true, false));
		spDataSet.addParameter(new ParameterDefinition("p_weight", DataType.INTEGER_TYPE, false, true));

		OdaDataSetDesign plainDataSet = new OdaDataSetDesign("inputOnly", "ds");
		plainDataSet.setExtensionID("org.eclipse.birt.report.data.oda.jdbc");
		plainDataSet.setQueryText("select part_no from parts where part_no = ?");
		plainDataSet.addParameter(new ParameterDefinition("p_part", DataType.INTEGER_TYPE, true, false));

		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(spDataSet);
		dataEngine.defineDataSet(plainDataSet);

		// Two queries per data set make it an implicit-cache candidate.
		IDataQueryDefinition[] queryDefinitions = new IDataQueryDefinition[4];
		queryDefinitions[0] = newQuery("spWithOutputParam");
		queryDefinitions[1] = newQuery("spWithOutputParam");
		queryDefinitions[2] = newQuery("inputOnly");
		queryDefinitions[3] = newQuery("inputOnly");
		dataEngine.registerQueries(queryDefinitions);

		IEngineExecutionHints hints = ((DataEngineImpl) dataEngine).getExecutionHints();
		assertTrue(hints.needCacheDataSet("inputOnly"));
		assertFalse(hints.needCacheDataSet("spWithOutputParam"));

		dataEngine.shutdown();
	}

	private static QueryDefinition newQuery(String dataSetName) throws BirtException {
		QueryDefinition qd = new QueryDefinition();
		qd.addBinding(new Binding("column1", new ScriptExpression("dataSetRow[\"part_no\"]", DataType.INTEGER_TYPE)));
		qd.setDataSetName(dataSetName);
		return qd;
	}
}
