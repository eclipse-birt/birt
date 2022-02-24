
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.IOException;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationHelper;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

import testutil.BaseTestCase;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class CubeAggregationTest extends BaseTestCase {
	// private static final String tmpPath = System.getProperty( "java.io.tmpdir" )
	// + File.separator;
	private DimLevel[] dimLevels = new DimLevel[] { new DimLevel("geography", "province"),
			new DimLevel("geography", "city"), new DimLevel("customer", "name"), new DimLevel("product", "productID") };

	AggregationResultSet aggregationResultSet = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void cubeAggregationSetUp() throws Exception {
		createAggregationResult();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testSumAggregation1() throws IOException, DataException {
		AggregationDefinition[] aggregations = new AggregationDefinition[2];
		int[] sortTypes = new int[] { 0, 0 };
		AggregationFunctionDefinition[] aggregationFunctions = new AggregationFunctionDefinition[2];
		aggregationFunctions[0] = new AggregationFunctionDefinition("pSale", "totalSale", "SUM");
		aggregationFunctions[1] = new AggregationFunctionDefinition("pCount", "totalCost", "COUNT");
		DimLevel[] dimLevels = new DimLevel[] { this.dimLevels[0], this.dimLevels[2] };
		aggregations[0] = new AggregationDefinition(dimLevels, sortTypes, aggregationFunctions);
		sortTypes = new int[] { 0 };
		dimLevels = new DimLevel[] { this.dimLevels[0] };
		aggregations[1] = new AggregationDefinition(dimLevels, sortTypes, aggregationFunctions);

		IAggregationResultSet[] resultSets = AggregationHelper.getInstance().execute(aggregationResultSet, aggregations,
				new StopSign());

		assertEquals(resultSets[0].length(), 6);
		assertEquals(resultSets[0].getAggregationCount(), 2);
		assertEquals(resultSets[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSets[0].getAggregationDataType(1), DataType.INTEGER_TYPE);
		assertEquals(resultSets[0].getAllLevels().length, 2);
		assertEquals(resultSets[0].getLevel(0), this.dimLevels[0]);
		assertEquals(resultSets[0].getLevel(1), this.dimLevels[2]);
		assertEquals(resultSets[0].getLevelKeyColCount(0), 1);
		assertEquals(resultSets[0].getLevelKeyColCount(1), 1);

		resultSets[0].seek(0);
		assertEquals(resultSets[0].getLevelKeyValue(0)[0], "AnHui");
		assertEquals(resultSets[0].getLevelKeyValue(1)[0], "li");
		assertEquals(resultSets[0].getAggregationValue(0), new Double(110));
		assertEquals(resultSets[0].getAggregationValue(1), new Integer(1));

		resultSets[0].seek(2);
		assertEquals(resultSets[0].getLevelKeyValue(0)[0], "AnHui");
		assertEquals(resultSets[0].getLevelKeyValue(1)[0], "zhao");
		assertEquals(resultSets[0].getAggregationValue(0), new Double(750));
		assertEquals(resultSets[0].getAggregationValue(1), new Integer(5));

		resultSets[0].seek(1);
		assertEquals(resultSets[0].getLevelKeyValue(0)[0], "AnHui");
		assertEquals(resultSets[0].getLevelKeyValue(1)[0], "wang");
		assertEquals(resultSets[0].getAggregationValue(0), new Double(120));
		assertEquals(resultSets[0].getAggregationValue(1), new Integer(1));

		resultSets[0].seek(3);
		assertEquals(resultSets[0].getLevelKeyValue(0)[0], "HeBei");
		assertEquals(resultSets[0].getLevelKeyValue(1)[0], "li");
		assertEquals(resultSets[0].getAggregationValue(0), new Double(250));
		assertEquals(resultSets[0].getAggregationValue(1), new Integer(2));

		resultSets[0].seek(4);
		assertEquals(resultSets[0].getLevelKeyValue(0)[0], "HeBei");
		assertEquals(resultSets[0].getLevelKeyValue(1)[0], "wang");
		assertEquals(resultSets[0].getAggregationValue(0), new Double(780));
		assertEquals(resultSets[0].getAggregationValue(1), new Integer(5));

		resultSets[0].seek(5);
		assertEquals(resultSets[0].getLevelKeyValue(0)[0], "ShanTong");
		assertEquals(resultSets[0].getLevelKeyValue(1)[0], "wang");
		assertEquals(resultSets[0].getAggregationValue(0), new Double(288));
		assertEquals(resultSets[0].getAggregationValue(1), new Integer(2));

		assertEquals(resultSets[1].length(), 3);
		assertEquals(resultSets[1].getAggregationCount(), 2);
		assertEquals(resultSets[1].getAllLevels().length, 1);
		assertEquals(resultSets[1].getLevel(0), this.dimLevels[0]);
		assertEquals(resultSets[1].getLevelKeyColCount(0), 1);

		resultSets[1].seek(0);
		assertEquals(resultSets[1].getLevelKeyValue(0)[0], "AnHui");
		assertEquals(resultSets[1].getAggregationValue(0), new Double(980));
		assertEquals(resultSets[1].getAggregationValue(1), new Integer(7));

		resultSets[1].seek(1);
		assertEquals(resultSets[1].getLevelKeyValue(0)[0], "HeBei");
		assertEquals(resultSets[1].getAggregationValue(0), new Double(1030));
		assertEquals(resultSets[1].getAggregationValue(1), new Integer(7));

		resultSets[1].seek(2);
		assertEquals(resultSets[1].getLevelKeyValue(0)[0], "ShanTong");
		assertEquals(resultSets[1].getAggregationValue(0), new Double(288));
		assertEquals(resultSets[1].getAggregationValue(1), new Integer(2));
	}

	@Test
	public void testSumAggregation2() throws IOException, DataException {
		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortTypes = new int[] { 0 };
		AggregationFunctionDefinition[] aggregationFunctions = new AggregationFunctionDefinition[1];
		aggregationFunctions[0] = new AggregationFunctionDefinition("saleWeightAvg", "totalSale", this.dimLevels[3],
				"productID", IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC);
		DimLevel[] dimLevels = new DimLevel[] { this.dimLevels[0], this.dimLevels[2] };
		dimLevels = new DimLevel[] { this.dimLevels[0] };
		aggregations[0] = new AggregationDefinition(dimLevels, sortTypes, aggregationFunctions);

		IAggregationResultSet[] resultSets = AggregationHelper.getInstance().execute(aggregationResultSet, aggregations,
				new StopSign());

		resultSets[0].seek(0);
		assertEquals(resultSets[0].getLevelKeyValue(0)[0], "AnHui");
		assertEquals(resultSets[0].getAggregationValue(0), new Double(3170.0 / 22));

		resultSets[0].seek(1);
		assertEquals(resultSets[0].getLevelKeyValue(0)[0], "HeBei");
		assertEquals(resultSets[0].getAggregationValue(0), new Double(1910.0 / 12));

		resultSets[0].seek(2);
		assertEquals(resultSets[0].getLevelKeyValue(0)[0], "ShanTong");
		assertEquals(resultSets[0].getAggregationValue(0), new Double(531.0 / 3));
	}

	@Test
	public void testRunningAggregation1() throws IOException, DataException {
		AggregationDefinition[] aggregations = new AggregationDefinition[2];
		int[] sortTypes = new int[] { 0, 0 };
		AggregationFunctionDefinition[] aggregationFunctions = new AggregationFunctionDefinition[2];
		aggregationFunctions[0] = new AggregationFunctionDefinition("pSale", "totalSale",
				IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC);
		aggregationFunctions[1] = new AggregationFunctionDefinition("pCount", "totalCost",
				IBuildInAggregation.TOTAL_RUNNINGCOUNT_FUNC);
		DimLevel[] dimLevels = new DimLevel[] { this.dimLevels[0], this.dimLevels[2] };
		aggregations[0] = new AggregationDefinition(dimLevels, sortTypes, aggregationFunctions);
		dimLevels = new DimLevel[] { this.dimLevels[0], this.dimLevels[1] };
		aggregations[1] = new AggregationDefinition(dimLevels, sortTypes, aggregationFunctions);

		IAggregationResultSet[] resultSets = AggregationHelper.getInstance().execute(aggregationResultSet, aggregations,
				new StopSign());

		assertEquals(resultSets[0].length(), 16);
		assertEquals(resultSets[0].getAggregationCount(), 2);
		assertEquals(resultSets[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSets[0].getAggregationDataType(1), DataType.INTEGER_TYPE);
		assertEquals(resultSets[0].getAllLevels().length, 4);
		assertEquals(resultSets[0].getLevel(0), this.dimLevels[0]);
		assertEquals(resultSets[0].getLevel(1), this.dimLevels[1]);
		assertEquals(resultSets[0].getLevel(2), this.dimLevels[2]);
		assertEquals(resultSets[0].getLevel(3), this.dimLevels[3]);
		assertEquals(resultSets[0].getLevelKeyColCount(0), 1);
		assertEquals(resultSets[0].getLevelKeyColCount(1), 1);
		Object[][] fieldValues = new Object[][] { { "AnHui", "li", new Double(110), new Integer(1) },
				{ "AnHui", "wang", new Double(120), new Integer(1) },
				{ "AnHui", "zhao", new Double(60), new Integer(1) },
				{ "AnHui", "zhao", new Double(360), new Integer(2) },
				{ "AnHui", "zhao", new Double(510), new Integer(3) },
				{ "AnHui", "zhao", new Double(630), new Integer(4) },
				{ "AnHui", "zhao", new Double(750), new Integer(5) },
				{ "HeBei", "li", new Double(100), new Integer(1) }, { "HeBei", "li", new Double(250), new Integer(2) },
				{ "HeBei", "wang", new Double(110), new Integer(1) },
				{ "HeBei", "wang", new Double(170), new Integer(2) },
				{ "HeBei", "wang", new Double(470), new Integer(3) },
				{ "HeBei", "wang", new Double(570), new Integer(4) },
				{ "HeBei", "wang", new Double(780), new Integer(5) },
				{ "ShanTong", "wang", new Double(45), new Integer(1) },
				{ "ShanTong", "wang", new Double(288), new Integer(2) } };
		for (int i = 0; i < fieldValues.length; i++) {
			resultSets[0].seek(i);
			assertEquals(resultSets[0].getLevelKeyValue(0)[0], fieldValues[i][0]);
			assertEquals(resultSets[0].getLevelKeyValue(2)[0], fieldValues[i][1]);
			assertEquals(resultSets[0].getAggregationValue(0), fieldValues[i][2]);
			assertEquals(resultSets[0].getAggregationValue(1), fieldValues[i][3]);
		}

		assertEquals(resultSets[1].length(), 16);
		assertEquals(resultSets[1].getAggregationCount(), 2);
		assertEquals(resultSets[1].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSets[1].getAggregationDataType(1), DataType.INTEGER_TYPE);
		assertEquals(resultSets[1].getAllLevels().length, 4);
		assertEquals(resultSets[0].getLevel(0), this.dimLevels[0]);
		assertEquals(resultSets[0].getLevel(1), this.dimLevels[1]);
		assertEquals(resultSets[0].getLevel(2), this.dimLevels[2]);
		assertEquals(resultSets[0].getLevel(3), this.dimLevels[3]);
		assertEquals(resultSets[1].getLevelKeyColCount(0), 1);
		assertEquals(resultSets[1].getLevelKeyColCount(1), 1);
		fieldValues = new Object[][] { { "AnHui", "HeFei", new Double(110), new Integer(1) },
				{ "AnHui", "HeFei", new Double(230), new Integer(2) },
				{ "AnHui", "HeFei", new Double(290), new Integer(3) },
				{ "AnHui", "HuaiBei", new Double(300), new Integer(1) },
				{ "AnHui", "HuaiBei", new Double(450), new Integer(2) },
				{ "AnHui", "HuaiBei", new Double(570), new Integer(3) },
				{ "AnHui", "HuaiBei", new Double(690), new Integer(4) },
				{ "HeBei", "ShiJiaZhuang", new Double(100), new Integer(1) },
				{ "HeBei", "ShiJiaZhuang", new Double(250), new Integer(2) },
				{ "HeBei", "ShiJiaZhuang", new Double(360), new Integer(3) },
				{ "HeBei", "ShiJiaZhuang", new Double(420), new Integer(4) },
				{ "HeBei", "ShiJiaZhuang", new Double(720), new Integer(5) },
				{ "HeBei", "XingTai", new Double(100), new Integer(1) },
				{ "HeBei", "XingTai", new Double(310), new Integer(2) },
				{ "ShanTong", "WeiFang", new Double(45), new Integer(1) },
				{ "ShanTong", "WeiFang", new Double(288), new Integer(2) } };
		for (int i = 0; i < fieldValues.length; i++) {
			resultSets[1].seek(i);
			assertEquals(resultSets[1].getLevelKeyValue(0)[0], fieldValues[i][0]);
			assertEquals(resultSets[1].getLevelKeyValue(1)[0], fieldValues[i][1]);
			assertEquals(resultSets[1].getAggregationValue(0), fieldValues[i][2]);
			assertEquals(resultSets[1].getAggregationValue(1), fieldValues[i][3]);
		}

	}

	@Test
	public void testMultipleAggregation1() throws IOException, DataException {
		AggregationDefinition[] aggregations = new AggregationDefinition[2];
		int[] sortTypes = new int[] { 0, 0 };
		AggregationFunctionDefinition[] aggregationFunctions = new AggregationFunctionDefinition[1];
		aggregationFunctions[0] = new AggregationFunctionDefinition("saleRank", "totalSale",
				IBuildInAggregation.TOTAL_RANK_FUNC);
//		aggregationFunctions[0].setParaValue( "true" );
		DimLevel[] dimLevels = new DimLevel[] { this.dimLevels[0], this.dimLevels[2] };
		aggregations[0] = new AggregationDefinition(dimLevels, sortTypes, aggregationFunctions);
		dimLevels = new DimLevel[] { this.dimLevels[0], this.dimLevels[1] };
		aggregations[1] = new AggregationDefinition(dimLevels, sortTypes, aggregationFunctions);

		IAggregationResultSet[] resultSets = AggregationHelper.getInstance().execute(aggregationResultSet, aggregations,
				new StopSign());

		assertEquals(resultSets[0].length(), 16);
		assertEquals(resultSets[0].getAggregationCount(), 1);
		assertEquals(resultSets[0].getAggregationDataType(0), DataType.INTEGER_TYPE);
		assertEquals(resultSets[0].getAllLevels().length, 4);
		assertEquals(resultSets[0].getLevel(0), this.dimLevels[0]);
		assertEquals(resultSets[0].getLevel(1), this.dimLevels[1]);
		assertEquals(resultSets[0].getLevel(2), this.dimLevels[2]);
		assertEquals(resultSets[0].getLevel(3), this.dimLevels[3]);
		assertEquals(resultSets[0].getLevelKeyColCount(0), 1);
		assertEquals(resultSets[0].getLevelKeyColCount(1), 1);
		Object[][] fieldValues = new Object[][] {
				{ "AnHui", "HeFei", "li", new Integer(1), new Double(110), new Integer(1) },
				{ "AnHui", "HeFei", "wang", new Integer(1), new Double(120), new Integer(1) },
				{ "AnHui", "HeFei", "zhao", new Integer(2), new Double(60), new Integer(1) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(3), new Double(300), new Integer(5) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(4), new Double(150), new Integer(4) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(5), new Double(120), new Integer(2) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(6), new Double(120), new Integer(2) },
				{ "HeBei", "ShiJiaZhuang", "li", new Integer(1), new Double(100), new Integer(1) },
				{ "HeBei", "ShiJiaZhuang", "li", new Integer(2), new Double(150), new Integer(2) },
				{ "HeBei", "ShiJiaZhuang", "wang", new Integer(3), new Double(110), new Integer(3) },
				{ "HeBei", "ShiJiaZhuang", "wang", new Integer(1), new Double(60), new Integer(1) },
				{ "HeBei", "ShiJiaZhuang", "wang", new Integer(2), new Double(300), new Integer(5) },
				{ "HeBei", "XingTai", "wang", new Integer(1), new Double(100), new Integer(2) },
				{ "HeBei", "XingTai", "wang", new Integer(2), new Double(210), new Integer(4) },
				{ "ShanTong", "WeiFang", "wang", new Integer(1), new Double(45), new Integer(1) },
				{ "ShanTong", "WeiFang", "wang", new Integer(2), new Double(243), new Integer(2) } };
		for (int i = 0; i < fieldValues.length; i++) {
			resultSets[0].seek(i);
			for (int j = 0; j < 4; j++) {
				assertEquals(resultSets[0].getLevelKeyValue(j)[0], fieldValues[i][j]);
			}
			assertEquals(resultSets[0].getAggregationValue(0), fieldValues[i][5]);
		}

		assertEquals(resultSets[1].length(), 16);
		assertEquals(resultSets[0].getAggregationCount(), 1);
		assertEquals(resultSets[0].getAggregationDataType(0), DataType.INTEGER_TYPE);
		assertEquals(resultSets[0].getAllLevels().length, 4);
		assertEquals(resultSets[1].getLevel(0), this.dimLevels[0]);
		assertEquals(resultSets[1].getLevel(1), this.dimLevels[1]);
		assertEquals(resultSets[1].getLevelKeyColCount(0), 1);
		assertEquals(resultSets[1].getLevelKeyColCount(1), 1);
		fieldValues = new Object[][] { { "AnHui", "HeFei", "li", new Integer(1), new Double(110), new Integer(1) },
				{ "AnHui", "HeFei", "wang", new Integer(1), new Double(120), new Integer(1) },
				{ "AnHui", "HeFei", "zhao", new Integer(2), new Double(60), new Integer(1) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(3), new Double(300), new Integer(5) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(4), new Double(150), new Integer(4) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(5), new Double(120), new Integer(2) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(6), new Double(120), new Integer(2) },
				{ "HeBei", "ShiJiaZhuang", "li", new Integer(1), new Double(100), new Integer(1) },
				{ "HeBei", "ShiJiaZhuang", "li", new Integer(2), new Double(150), new Integer(2) },
				{ "HeBei", "ShiJiaZhuang", "wang", new Integer(3), new Double(110), new Integer(3) },
				{ "HeBei", "ShiJiaZhuang", "wang", new Integer(1), new Double(60), new Integer(1) },
				{ "HeBei", "ShiJiaZhuang", "wang", new Integer(2), new Double(300), new Integer(5) },
				{ "HeBei", "XingTai", "wang", new Integer(1), new Double(100), new Integer(2) },
				{ "HeBei", "XingTai", "wang", new Integer(2), new Double(210), new Integer(4) },
				{ "ShanTong", "WeiFang", "wang", new Integer(1), new Double(45), new Integer(1) },
				{ "ShanTong", "WeiFang", "wang", new Integer(2), new Double(243), new Integer(2) } };
		for (int i = 0; i < fieldValues.length; i++) {
			resultSets[0].seek(i);
			for (int j = 0; j < 4; j++) {
				assertEquals(resultSets[0].getLevelKeyValue(j)[0], fieldValues[i][j]);
			}
			assertEquals(resultSets[0].getAggregationValue(0), fieldValues[i][5]);
		}

	}

	private void createAggregationResult() throws IOException, DataException {
		int[] sortTypes = new int[] { 0, 0, 0, 0 };
		AggregationFunctionDefinition[] aggregationFunctions = new AggregationFunctionDefinition[2];
		aggregationFunctions[0] = new AggregationFunctionDefinition("totalSale", "sale", "SUM");
		aggregationFunctions[1] = new AggregationFunctionDefinition("totalCost", "cost", "SUM");
		AggregationDefinition aggregation = new AggregationDefinition(dimLevels, sortTypes, aggregationFunctions);
		String[][] keyNames = new String[][] { { "province" }, { "city" }, { "name" }, { "productID" } };
		String[][] attrNames = null;
		aggregationResultSet = new AggregationResultSet(aggregation, getAggregationResultRow(), keyNames, attrNames);

	}

	private IDiskArray getAggregationResultRow() throws DataException, IOException {
		int LIST_BUFFER_SIZE = 4000;
		IDiskArray result = new BufferedStructureArray(AggregationResultRow.getCreator(), LIST_BUFFER_SIZE);
		Object[][] fieldValues = new Object[][] {
				{ "AnHui", "HeFei", "li", new Integer(1), new Double(110), new Double(90) },
				{ "AnHui", "HeFei", "wang", new Integer(1), new Double(120), new Double(130) },
				{ "AnHui", "HeFei", "zhao", new Integer(2), new Double(60), new Double(80) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(3), new Double(300), new Double(200) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(4), new Double(150), new Double(100) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(5), new Double(120), new Double(600) },
				{ "AnHui", "HuaiBei", "zhao", new Integer(6), new Double(120), new Double(70) },
				{ "HeBei", "ShiJiaZhuang", "li", new Integer(1), new Double(100), new Double(100) },
				{ "HeBei", "ShiJiaZhuang", "li", new Integer(2), new Double(150), new Double(50) },
				{ "HeBei", "ShiJiaZhuang", "wang", new Integer(3), new Double(110), new Double(120) },
				{ "HeBei", "ShiJiaZhuang", "wang", new Integer(1), new Double(60), new Double(80) },
				{ "HeBei", "ShiJiaZhuang", "wang", new Integer(2), new Double(300), new Double(200) },
				{ "HeBei", "XingTai", "wang", new Integer(1), new Double(100), new Double(100) },
				{ "HeBei", "XingTai", "wang", new Integer(2), new Double(210), new Double(170) },
				{ "ShanTong", "WeiFang", "wang", new Integer(1), new Double(45), new Double(45) },
				{ "ShanTong", "WeiFang", "wang", new Integer(2), new Double(243), new Double(45) } };
		for (int i = 0; i < fieldValues.length; i++) {
			result.add(newAggregationResultRow(fieldValues[i]));
		}
		return result;
	}

	protected AggregationResultRow newAggregationResultRow(Object[] fieldValues) throws DataException, IOException {
		AggregationResultRow resultObj = new AggregationResultRow();
		Member[] members = new Member[4];
		for (int i = 0; i < members.length; i++) {
			members[i] = new Member();
			members[i].setKeyValues(new Object[] { fieldValues[i] });
		}
		resultObj.setLevelMembers(members);
		resultObj.setAggregationValues(new Object[] { fieldValues[4], fieldValues[5] });

		return resultObj;
	}
}
