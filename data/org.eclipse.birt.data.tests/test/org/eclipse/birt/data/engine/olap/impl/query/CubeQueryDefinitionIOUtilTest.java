
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.impl.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.ExprUtil;
import org.eclipse.birt.data.engine.olap.api.query.CubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.CubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.DimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IComputedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.icu.util.ULocale;

/**
 *
 */

public class CubeQueryDefinitionIOUtilTest {
	private File tempFile;
	private String queryResultID = "qurs0"; //$NON-NLS-1$

	@Before
	public void cubeQueryDefinitionIOUtilSetUp() throws Exception {
		tempFile = File.createTempFile(CubeQueryDefinitionIOUtilTest.class.getSimpleName(), null);
		tempFile.deleteOnExit();
	}

	@After
	public void cubeQueryDefinitionIOUtilTearDown() throws Exception {
		tempFile.delete();
	}

	@Test
	public void testSaveAndLoad() throws IOException, BirtException {
		IDocArchiveWriter writer = new FileArchiveWriter(tempFile.getAbsolutePath());
		ICubeQueryDefinition toSave = createQueryDefn();
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.MODE_GENERATION, null, null,
				writer);
		CubeQueryDefinitionIOUtil.save(queryResultID, context, toSave);
		writer.flush();
		writer.finish();

		IDocArchiveReader reader = new FileArchiveReader(tempFile.getAbsolutePath());
		ICubeQueryDefinition loaded = CubeQueryDefinitionIOUtil.load(queryResultID,
				DataEngineContext.newInstance(DataEngineContext.MODE_UPDATE, null, reader, writer));
		reader.close();

		assertEquals(toSave.getName(), loaded.getName());
		assertEquals(toSave.cacheQueryResults(), loaded.cacheQueryResults());
		assertEquals(toSave.getFilterOption(), loaded.getFilterOption());

		assertEqualBindings(toSave.getBindings(), loaded.getBindings());
		assertEquals(toSave.getFilters().size(), loaded.getFilters().size());
		for (int i = 0; i < toSave.getFilters().size(); i++) {
			assertEqualFilter((IFilterDefinition) toSave.getFilters().get(i),
					(IFilterDefinition) loaded.getFilters().get(i));
		}
		assertEquals(toSave.getSorts().size(), loaded.getSorts().size());
		for (int i = 0; i < toSave.getSorts().size(); i++) {
			assertEqualSortDefinition((ISortDefinition) toSave.getSorts().get(i),
					(ISortDefinition) loaded.getSorts().get(i));
		}
		assertEquals(toSave.getMeasures().size(), loaded.getMeasures().size());
		for (int i = 0; i < toSave.getMeasures().size(); i++) {
			assertEqualMeasure((IMeasureDefinition) toSave.getMeasures().get(i),
					(IMeasureDefinition) loaded.getMeasures().get(i));
		}
		assertEquals(toSave.getComputedMeasures().size(), loaded.getComputedMeasures().size());
		for (int i = 0; i < toSave.getComputedMeasures().size(); i++) {
			assertEqualComputedMeasure((IComputedMeasureDefinition) toSave.getComputedMeasures().get(i),
					(IComputedMeasureDefinition) loaded.getComputedMeasures().get(i));
		}
		assertEqualEdge(toSave.getEdge(ICubeQueryDefinition.ROW_EDGE), loaded.getEdge(ICubeQueryDefinition.ROW_EDGE));
		assertEqualEdge(toSave.getEdge(ICubeQueryDefinition.COLUMN_EDGE),
				loaded.getEdge(ICubeQueryDefinition.COLUMN_EDGE));
		assertEqualEdge(toSave.getEdge(ICubeQueryDefinition.PAGE_EDGE), loaded.getEdge(ICubeQueryDefinition.PAGE_EDGE));
		assertEquals(toSave.getCubeOperations().length, loaded.getCubeOperations().length);
		for (int i = 0; i < toSave.getCubeOperations().length; i++) {
			assertEqualCubeOperation(toSave.getCubeOperations()[i], loaded.getCubeOperations()[i]);
		}
	}

	private ICubeQueryDefinition createQueryDefn() throws DataException {
		CubeQueryDefinition cqd = new CubeQueryDefinition("query");
		cqd.setCacheQueryResults(true);
		cqd.setFilterOption(1);

		// add bindings
		for (IBinding b : createBindings()) {
			cqd.addBinding(b);
		}

		// add filters
		cqd.addFilter(new FilterDefinition(new ScriptExpression("true")));
		cqd.addFilter(new CubeFilterDefinition(new ScriptExpression("false"),
				createDimension("dim1").getHierarchy().get(0).getLevels().get(0),
				createDimension("dim1").getHierarchy().get(0).getLevels().toArray(new ILevelDefinition[0]),
				new Object[] { null, 89, "tt" }));

		// add sorts
		for (ISortDefinition sort : createSortDefns()) {
			cqd.addSort(sort);
		}

		// add measures
		IMeasureDefinition md = cqd.createMeasure("md1");
		initMeasue(md, "SUM");
		md = cqd.createMeasure("md2");
		initMeasue(md, null);

		// add computed measures
		IComputedMeasureDefinition cmd = cqd.createComputedMeasure("cmd1", DataType.INTEGER_TYPE,
				new ScriptExpression("5"));
		initComputedMeasure(cmd);
		cmd = cqd.createComputedMeasure("cmd1", DataType.BOOLEAN_TYPE, new ScriptExpression("false"));
		initComputedMeasure(cmd);

		// add edges
		IEdgeDefinition ed = cqd.createEdge(ICubeQueryDefinition.ROW_EDGE);
		initEdge(ed, true);
		ed = cqd.createEdge(ICubeQueryDefinition.PAGE_EDGE);
		initEdge(ed, false);

		// add cube operations
		cqd.addCubeOperation(new AddingNestAggregations(createBindings().toArray(new IBinding[0])));
		cqd.addCubeOperation(new AddingNestAggregations(createBindings().toArray(new IBinding[0])));

		return cqd;
	}

	private List<IBinding> createBindings() throws DataException {
		List<IBinding> result = new ArrayList<>();
		for (int i = 0; i <= 1; i++) {
			Binding b = new Binding(String.valueOf(i));
			b.setAggrFunction("SUM");
			b.setDataType(DataType.INTEGER_TYPE);
			b.setExpression(new ScriptExpression("5+5"));
			b.setFilter(new ScriptExpression("true"));

			for (int j = 0; j <= 1; j++) {
				b.addArgument(new ScriptExpression("5+5"));
			}
			for (int j = 0; j <= 1; j++) {
				b.addAggregateOn("level" + j);
			}
			result.add(b);
		}
		return result;
	}

	private List<ISortDefinition> createSortDefns() throws DataException {
		List<ISortDefinition> result = new ArrayList<>();
		SortDefinition sd = new SortDefinition();
		sd.setColumn("col");
		sd.setExpression("true");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		sd.setSortStrength(-1);
		sd.setSortLocale(new ULocale("CN"));
		result.add(sd);
		for (int i = 0; i <= 1; i++) {
			CubeSortDefinition csd = new CubeSortDefinition();
			if (i == 1) {
				csd.setSortStrength(-2);
				csd.setAxisQualifierLevels(
						createDimension("dim1").getHierarchy().get(0).getLevels().toArray(new ILevelDefinition[0]));
				csd.setAxisQualifierValues(new Object[] { 1, "tt" });
			}
			csd.setColumn("col");
			csd.setExpression("true");
			csd.setSortDirection(ISortDefinition.SORT_DESC);
			if (i == 1) {
				csd.setSortLocale(new ULocale("CN"));
			}
			if (i == 1) {
				csd.setTargetLevel(csd.getAxisQualifierLevels()[0]);
			}
		}
		return result;
	}

	private IDimensionDefinition createDimension(String name) {
		DimensionDefinition dd = new DimensionDefinition(name);
		initDim(dd);
		return dd;
	}

	private void initDim(IDimensionDefinition dd) {
		IHierarchyDefinition hd1 = dd.createHierarchy("hierarchy1");
		hd1.createLevel("level1");
		hd1.createLevel("level2");
		IHierarchyDefinition hd2 = dd.createHierarchy("hierarchy2");
		hd2.createLevel("level1");
		hd2.createLevel("level2");
	}

	private void assertEqualDim(IDimensionDefinition dd1, IDimensionDefinition dd2) {
		if (dd1 == null) {
			assertEquals(null, dd2);
			return;
		}
		assertEquals(dd1.getName(), dd2.getName());
		int i = 0;
		for (IHierarchyDefinition hd : dd1.getHierarchy()) {
			assertEquals(hd.getName(), dd2.getHierarchy().get(i).getName());
			int j = 0;
			for (ILevelDefinition ld : hd.getLevels()) {
				assertEquals(ld.getName(), dd2.getHierarchy().get(i).getLevels().get(j).getName());
				j++;
			}
			i++;
		}
	}

	private void assertEqualHierarchy(IHierarchyDefinition hd1, IHierarchyDefinition hd2) {
		if (hd1 == null) {
			assertEquals(null, hd2);
			return;
		}
		assertEquals(hd1.getName(), hd2.getName());
		assertEqualDim(hd1.getDimension(), hd2.getDimension());
		int j = 0;
		for (ILevelDefinition ld : hd1.getLevels()) {
			assertEquals(ld.getName(), hd2.getLevels().get(j).getName());
			j++;
		}
	}

	private void assertEqualLevel(ILevelDefinition ld1, ILevelDefinition ld2) {
		if (ld1 == null) {
			assertEquals(null, ld2);
			return;
		}
		assertTrue(ld2 != null);
		assertEquals(ld1.getName(), ld2.getName());
		assertEqualHierarchy(ld1.getHierarchy(), ld2.getHierarchy());
	}

	private void assertEqualBinding(IBinding b1, IBinding b2) throws DataException {
		assertEquals(b1.getBindingName(), b2.getBindingName());
		assertEquals(b1.getDataType(), b2.getDataType());
		assertEquals(b1.getAggrFunction(), b2.getAggrFunction());
		assertEqualExpr(b1.getExpression(), b2.getExpression());
		assertEqualExpr(b1.getFilter(), b2.getFilter());
		for (int i = 0; i < b1.getArguments().size(); i++) {
			assertEqualExpr((IBaseExpression) b1.getArguments().get(i), (IBaseExpression) b2.getArguments().get(i));
		}
		for (int i = 0; i < b1.getAggregatOns().size(); i++) {
			assertEquals(b1.getAggregatOns().get(i), b2.getAggregatOns().get(i));
		}
	}

	private void assertEqualLocale(ULocale u1, ULocale u2) {
		if (u1 == null) {
			assertEquals(null, u2);
			return;
		}
		assertEquals(u1.getBaseName(), u2.getBaseName());
	}

	private void assertEqualExpr(IBaseExpression expr1, IBaseExpression expr2) {
		if (expr1 == null) {
			assertEquals(null, expr2);
			return;
		}
		assertTrue(expr1 instanceof IScriptExpression);
		assertTrue(expr2 instanceof IScriptExpression);
		assertEquals(((IScriptExpression) expr1).getText(), ((IScriptExpression) expr2).getText());
	}

	private void assertEqualBindings(List<IBinding> bs1, List<IBinding> bs2) throws DataException {
		assertEquals(bs1.size(), bs2.size());
		int i = 0;
		for (IBinding b1 : bs1) {
			assertEqualBinding(b1, bs2.get(i));
			i++;
		}
	}

	private void initMeasue(IMeasureDefinition md, String aggrFunc) {
		md.setAggrFunction(aggrFunc);
	}

	private void initComputedMeasure(IComputedMeasureDefinition cmd) {
		cmd.setAggrFunction("SUM");
	}

	private void assertEqualMeasure(IMeasureDefinition md1, IMeasureDefinition md2) {
		assertEquals(md1.getName(), md2.getName());
		assertEquals(md1.getAggrFunction(), md2.getAggrFunction());
	}

	private void assertEqualComputedMeasure(IComputedMeasureDefinition md1, IComputedMeasureDefinition md2)
			throws DataException {
		assertEqualMeasure(md1, md2);
		assertEquals(md1.getDataType(), md2.getDataType());
		assertEqualExpr(md1.getExpression(), md2.getExpression());
	}

	private void initEdge(IEdgeDefinition ed, boolean createMirrorDefn) throws DataException {
		IDimensionDefinition dd = ed.createDimension("dim1");
		initDim(dd);
		dd = ed.createDimension("dim2");
		initDim(dd);
		if (createMirrorDefn) {
			ed.setMirrorStartingLevel(dd.getHierarchy().get(0).getLevels().get(0));
		}
		IEdgeDrillFilter edf = ed.createDrillFilter("edf1");
		initEdgeDrillFilter(edf);
		edf = ed.createDrillFilter("edf2");
		initEdgeDrillFilter(edf);
	}

	private void initEdgeDrillFilter(IEdgeDrillFilter edf) throws DataException {
		edf.addLevelFilter(new FilterDefinition(new ScriptExpression("true")));
		edf.addLevelFilter(new FilterDefinition(new ScriptExpression("false")));
		for (ISortDefinition sd : createSortDefns()) {
			edf.addLevelSort(sd);
		}
		edf.setTargetHierarchy(createDimension("dim1").getHierarchy().get(0));
		edf.setTargetLevelName(edf.getTargetHierarchy().getLevels().get(0).getName());
		List<Object[]> tuples = new ArrayList<>();
		tuples.add(new Object[] { "45", 45, true });
		tuples.add(new Object[0]);
		tuples.add(new Object[] { 1, "tt" });
		edf.setTuple(tuples);
	}

	private void assertEqualEdgeDrillFilter(IEdgeDrillFilter edf1, IEdgeDrillFilter edf2) {
		assertEquals(edf1.getLevelFilter().size(), edf2.getLevelFilter().size());
		for (int i = 0; i < edf1.getLevelFilter().size(); i++) {
			assertEqualExpr(((FilterDefinition) edf1.getLevelFilter().get(i)).getExpression(),
					((FilterDefinition) edf2.getLevelFilter().get(i)).getExpression());
		}
		assertEquals(edf1.getLevelSort().size(), edf2.getLevelSort().size());
		for (int i = 0; i < edf1.getLevelSort().size(); i++) {
			ISortDefinition s1 = edf1.getLevelSort().get(i);
			ISortDefinition s2 = edf2.getLevelSort().get(i);
			assertEqualSortDefinition(s1, s2);
		}
		assertEqualHierarchy(edf1.getTargetHierarchy(), edf2.getTargetHierarchy());
		assertEquals(edf1.getTargetLevelName(), edf2.getTargetLevelName());
		assertTrue(Arrays.deepEquals(edf1.getTuple().toArray(), edf2.getTuple().toArray()));
	}

	private void assertEqualEdge(IEdgeDefinition ed1, IEdgeDefinition ed2) {
		if (ed1 == null) {
			assertEquals(null, ed2);
			return;
		}
		assertEquals(ed1.getName(), ed2.getName());
		assertEquals(ed1.getDimensions().size(), ed2.getDimensions().size());
		int i = 0;
		for (IDimensionDefinition dd : ed1.getDimensions()) {
			assertEqualDim(dd, ed2.getDimensions().get(i));
			i++;
		}
		assertEqualMirroredDefn(ed1.getMirroredDefinition(), ed2.getMirroredDefinition());
		assertEquals(ed1.getDrillFilter().size(), ed2.getDrillFilter().size());
		i = 0;
		for (IEdgeDrillFilter edf : ed1.getDrillFilter()) {
			assertEqualEdgeDrillFilter(edf, ed2.getDrillFilter().get(i));
			i++;
		}
	}

	private void assertEqualMirroredDefn(IMirroredDefinition md1, IMirroredDefinition md2) {
		if (md1 == null) {
			assertTrue(null == md2);
			return;
		} else {
			assertTrue(null != md2);
		}
		assertEqualLevel(md1.getMirrorStartingLevel(), md2.getMirrorStartingLevel());
		assertEquals(md1.isBreakHierarchy(), md2.isBreakHierarchy());
	}

	private void assertEqualCubeOperation(ICubeOperation co1, ICubeOperation co2) throws DataException {
		assertEquals(co1.getClass(), co2.getClass());
		assertEqualBindings(Arrays.asList(co1.getNewBindings()), Arrays.asList(co1.getNewBindings()));
	}

	private void assertEqualFilter(IFilterDefinition fd1, IFilterDefinition fd2) throws DataException {
		if (fd1 == null) {
			assertEquals(null, fd2);
			return;
		}
		assertEquals(fd1.getClass(), fd2.getClass());
		assertEquals(true, ExprUtil.isEqualExpression(fd1.getExpression(), fd2.getExpression()));
		if (fd1 instanceof ICubeFilterDefinition) {
			ICubeFilterDefinition csd1 = (ICubeFilterDefinition) fd1;
			ICubeFilterDefinition csd2 = (ICubeFilterDefinition) fd2;
			assertEquals(csd1.getAxisQualifierLevels().length, csd2.getAxisQualifierLevels().length);
			int i = 0;
			for (ILevelDefinition ld : csd1.getAxisQualifierLevels()) {
				assertEqualLevel(ld, csd2.getAxisQualifierLevels()[i]);
				i++;
			}
			assertTrue(Arrays.deepEquals(csd1.getAxisQualifierValues(), csd2.getAxisQualifierValues()));
		}
	}

	private void assertEqualSortDefinition(ISortDefinition sd1, ISortDefinition sd2) {
		if (sd1 == null) {
			assertEquals(null, sd2);
			return;
		}
		assertEquals(sd1.getClass(), sd2.getClass());
		assertEquals(sd1.getColumn(), sd2.getColumn());
		assertEquals(sd1.getSortDirection(), sd2.getSortDirection());
		assertEquals(sd1.getSortStrength(), sd2.getSortStrength());
		assertEqualExpr(sd1.getExpression(), sd2.getExpression());
		assertEqualLocale(sd1.getSortLocale(), sd2.getSortLocale());
		if (sd1 instanceof ICubeSortDefinition) {
			ICubeSortDefinition s1 = (ICubeSortDefinition) sd1;
			ICubeSortDefinition s2 = (ICubeSortDefinition) sd2;
			assertTrue(Arrays.equals(s1.getAxisQualifierValues(), s2.getAxisQualifierValues()));
			if (s1.getAxisQualifierLevels() == null) {
				assertEquals(null, s2.getAxisQualifierLevels());
			} else {
				assertTrue(s2.getAxisQualifierLevels() != null);
				assertEquals(s1.getAxisQualifierLevels().length, s2.getAxisQualifierLevels().length);
				int i = 0;
				for (ILevelDefinition ld : s1.getAxisQualifierLevels()) {
					assertEqualLevel(ld, s2.getAxisQualifierLevels()[i]);
					i++;
				}
			}
			assertEqualLevel(s1.getTargetLevel(), s2.getTargetLevel());
		}
	}
}
