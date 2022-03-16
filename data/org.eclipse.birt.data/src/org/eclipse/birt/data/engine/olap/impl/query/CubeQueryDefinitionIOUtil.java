
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition.FilterTarget;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.BindingIOUtil;
import org.eclipse.birt.data.engine.impl.document.ExprUtil;
import org.eclipse.birt.data.engine.impl.document.QueryResultInfo;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.api.query.CubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.CubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.DimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.HierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IComputedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition;
import org.eclipse.birt.data.engine.olap.api.query.LevelDefiniton;

import com.ibm.icu.util.ULocale;

/**
 * Utility to save/load {@code CubeQueryDefinition} instance into/from report
 * document
 */

public class CubeQueryDefinitionIOUtil {
	private static String STREAM_FLAG = "_CUBE_QUERY_DEFINITION"; //$NON-NLS-1$

	// filter definition type flags
	private static int FILTER_DEFN_FLAG_COMMON = 0;
	private static int FILTER_DEFN_FLAG_CUBE = 1;

	// sort definition type flags
	private static int SORT_DEFN_FLAG_COMMON = 0;
	private static int SORT_DEFN_FLAG_CUBE = 1;

	// cube operation type flags
	private static int CUBE_OPERATION_FLAG_ADDING_NEST_AGGRS = 0;

	private CubeQueryDefinitionIOUtil() {
	}

	public static boolean existStream(IDocArchiveReader reader, String queryResultID) {
		if (queryResultID == null) {
			return false;
		}
		return reader.exists(queryResultID + STREAM_FLAG);
	}

	/**
	 * Saves {@code CubeQueryDefinition} instance into report document
	 *
	 * @param queryResultID
	 * @param writer
	 * @param qd
	 * @throws DataException
	 * @throws IOException
	 */
	public static void save(String queryResultID, DataEngineContext context, ICubeQueryDefinition qd)
			throws DataException, IOException {
		DataOutputStream dos = null;
		try {
			StreamManager manager = new StreamManager(context, new QueryResultInfo(queryResultID, null, 0));

			int version = manager.getVersion();
			IDocArchiveWriter writer = context.getDocWriter();

			RAOutputStream outputStream = writer.createRandomAccessStream(queryResultID + STREAM_FLAG);
			dos = new DataOutputStream(outputStream);

			// save name
			IOUtil.writeString(dos, qd.getName());

			IOUtil.writeBool(dos, qd.cacheQueryResults());

			IOUtil.writeInt(dos, qd.getFilterOption());

			// save bindings
			saveBindings(dos, qd.getBindings(), version);

			// save filters
			saveFilters(dos, qd.getFilters(), version);

			// save sorts
			saveSortDefns(dos, qd.getSorts());

			// save measures
			saveMeasures(dos, qd.getMeasures());

			// save computed measures
			saveComputedMeasures(dos, qd.getComputedMeasures());

			// save calculated measures
			saveCalculatedMeasures(dos, qd.getDerivedMeasures(), version);

			// save edges
			saveEdges(dos, qd, version);

			// save cube operations
			saveCubeOperations(dos, qd.getCubeOperations(), version);

			dos.flush();
		} finally {
			if (dos != null) {
				dos.close();
			}
		}
	}

	private static void saveCalculatedMeasures(DataOutputStream dos, List<IDerivedMeasureDefinition> derivedMeasures,
			int version) throws IOException, DataException {
		// no calculated measure support
		if (version < VersionManager.VERSION_2_6_3_1) {
			return;
		}
		if (writeSize(dos, derivedMeasures) > 0) {
			for (IDerivedMeasureDefinition m : derivedMeasures) {
				saveCalculatedMeasure(dos, m);
			}
		}
	}

	private static void loadCalculatedMeasures(DataInputStream dis, ICubeQueryDefinition qd, int version)
			throws DataException, IOException {
		// no calculated measure support
		if (version < VersionManager.VERSION_2_6_3_1) {
			return;
		}
		int size = IOUtil.readInt(dis);
		for (int i = 0; i < size; i++) {
			IDerivedMeasureDefinition md = loadCaculatedMeasure(dis);
			IMeasureDefinition md1 = qd.createDerivedMeasure(md.getName(), md.getDataType(), md.getExpression());
			md1.setAggrFunction(md.getAggrFunction());
		}

	}

	private static void saveCalculatedMeasure(DataOutputStream dos, IDerivedMeasureDefinition m)
			throws IOException, DataException {
		if (m == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		saveMeasure(dos, m);
		IOUtil.writeInt(dos, m.getDataType());
		ExprUtil.saveBaseExpr(dos, m.getExpression());
	}

	private static IDerivedMeasureDefinition loadCaculatedMeasure(DataInputStream dis)
			throws DataException, IOException {
		IMeasureDefinition md = loadMeasure(dis);
		if (md == null) {
			return null;
		}
		String name = md.getName();
		int type = IOUtil.readInt(dis);
		IBaseExpression expr = ExprUtil.loadBaseExpr(dis);
		IDerivedMeasureDefinition dmd = new DerivedMeasureDefinition(name, type, expr);
		dmd.setAggrFunction(md.getAggrFunction());
		return dmd;
	}

	/**
	 * Loads {@code CubeQueryDefinition} instance from report document
	 *
	 * @param queryResultID
	 * @param reader
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	public static ICubeQueryDefinition load(String queryResultID, DataEngineContext context)
			throws DataException, IOException {
		DataInputStream dis = null;
		IDocArchiveReader reader = context.getDocReader();

		VersionManager vm = new VersionManager(context);
		int version = vm.getVersion(queryResultID);

		try {
			RAInputStream inputStream = reader.getStream(queryResultID + STREAM_FLAG);
			dis = new DataInputStream(inputStream);

			// load name
			String name = IOUtil.readString(dis);
			ICubeQueryDefinition cqd = new CubeQueryDefinition(name);

			cqd.setCacheQueryResults(IOUtil.readBool(dis));
			cqd.setFilterOption(IOUtil.readInt(dis));

			// load bindings
			loadBindings(dis, cqd, version);

			// load filters
			loadFilters(dis, cqd, version);

			// load sorts
			loadSortDefns(dis, cqd);

			// load measures
			loadMeasures(dis, cqd);

			// load computed measures
			loadComputedMeasures(dis, cqd);

			// load calculated measures
			loadCalculatedMeasures(dis, cqd, version);

			// load edges
			loadEdges(dis, cqd, version);

			// load cube operations
			loadCubeOperations(dis, cqd, version);

			return cqd;
		} finally {
			if (dis != null) {
				dis.close();
			}
		}
	}

	private static void saveFilters(DataOutputStream dos, List<IFilterDefinition> filters, int version)
			throws DataException, IOException {
		if (writeSize(dos, filters) > 0) {
			for (IFilterDefinition fd : filters) {
				saveFilterDefn(dos, fd, version);
			}
		}
	}

	private static void loadFilters(DataInputStream dis, ICubeQueryDefinition qd, int version)
			throws DataException, IOException {
		int size = IOUtil.readInt(dis);
		for (int i = 0; i < size; i++) {
			IFilterDefinition fd = loadFilterDefn(dis, version);
			qd.addFilter(fd);
		}
	}

	private static void loadFilters(DataInputStream dis, IEdgeDrillFilter edf, int version)
			throws DataException, IOException {
		int size = IOUtil.readInt(dis);
		for (int i = 0; i < size; i++) {
			IFilterDefinition fd = loadFilterDefn(dis, version);
			edf.addLevelFilter(fd);
		}
	}

	private static void saveCubeOperations(DataOutputStream dos, ICubeOperation[] cos, int version)
			throws DataException, IOException {
		if (writeSize(dos, cos) > 0) {
			for (ICubeOperation co : cos) {
				saveCubeOperation(dos, co, version);
			}
		}
	}

	private static void loadCubeOperations(DataInputStream dis, ICubeQueryDefinition qd, int version)
			throws DataException, IOException {
		int size = IOUtil.readInt(dis);
		for (int i = 0; i < size; i++) {
			ICubeOperation co = loadCubeOperation(dis, version);
			qd.addCubeOperation(co);
		}
	}

	private static void saveCubeOperation(DataOutputStream dos, ICubeOperation co, int version)
			throws DataException, IOException {
		if (co == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		IOUtil.writeBool(dos, true);
		if (co instanceof AddingNestAggregations) {
			IOUtil.writeInt(dos, CUBE_OPERATION_FLAG_ADDING_NEST_AGGRS);
			saveBindings(dos, Arrays.asList(co.getNewBindings()), version);
		}
	}

	private static ICubeOperation loadCubeOperation(DataInputStream dis, int version)
			throws DataException, IOException {
		if (!IOUtil.readBool(dis)) {
			return null;
		}
		int type = IOUtil.readInt(dis);
		if (type == CUBE_OPERATION_FLAG_ADDING_NEST_AGGRS) {
			int count = IOUtil.readInt(dis);
			IBinding[] bs = new IBinding[count];
			for (int i = 0; i < count; i++) {
				bs[i] = BindingIOUtil.loadBinding(dis, version);
			}
			return new AddingNestAggregations(bs);
		}
		// Currently, only adding nest aggregations operation is supported
		assert false;
		return null;

	}

	private static void saveEdges(DataOutputStream dos, ICubeQueryDefinition qd, int version)
			throws DataException, IOException {
		saveEdge(dos, qd.getEdge(ICubeQueryDefinition.ROW_EDGE), version);
		saveEdge(dos, qd.getEdge(ICubeQueryDefinition.COLUMN_EDGE), version);
		saveEdge(dos, qd.getEdge(ICubeQueryDefinition.PAGE_EDGE), version);
	}

	private static void loadEdges(DataInputStream dis, ICubeQueryDefinition qd, int version)
			throws DataException, IOException {
		IEdgeDefinition ed = loadEdge(dis, version);
		if (ed != null) {
			IEdgeDefinition ed1 = qd.createEdge(ICubeQueryDefinition.ROW_EDGE);
			copy(ed, ed1);
		}
		ed = loadEdge(dis, version);
		if (ed != null) {
			IEdgeDefinition ed1 = qd.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
			copy(ed, ed1);
		}
		ed = loadEdge(dis, version);
		if (ed != null) {
			IEdgeDefinition ed1 = qd.createEdge(ICubeQueryDefinition.PAGE_EDGE);
			copy(ed, ed1);
		}
	}

	private static void saveEdge(DataOutputStream dos, IEdgeDefinition ed, int version)
			throws DataException, IOException {
		if (ed == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		IOUtil.writeBool(dos, true);
		IOUtil.writeString(dos, ed.getName());
		if (writeSize(dos, ed.getDimensions()) > 0) {
			for (IDimensionDefinition dd : ed.getDimensions()) {
				saveDimensionDefinition(dos, dd);
			}
		}
		if (writeSize(dos, ed.getDrillFilter()) > 0) {
			for (IEdgeDrillFilter edf : ed.getDrillFilter()) {
				saveEdgeDrillFilter(dos, edf, version);
			}
		}
		saveMirroredDefn(dos, ed.getMirroredDefinition());
	}

	private static IEdgeDefinition loadEdge(DataInputStream dis, int version) throws DataException, IOException {
		if (!IOUtil.readBool(dis)) {
			return null;
		}
		String name = IOUtil.readString(dis);
		EdgeDefinition ed = new EdgeDefinition(name);

		// load dimension definitions
		int count = IOUtil.readInt(dis);
		for (int i = 0; i < count; i++) {
			IDimensionDefinition dd = loadDimensionDefinition(dis);
			IDimensionDefinition dd1 = ed.createDimension(dd.getName());
			copy(dd, dd1);
		}

		// load edge drill filters
		count = IOUtil.readInt(dis);
		for (int i = 0; i < count; i++) {
			IEdgeDrillFilter edf = loadEdgeDrillFilter(dis, version);
			IEdgeDrillFilter edf1 = ed.createDrillFilter(edf.getName());
			copy(edf, edf1);
		}
		IMirroredDefinition md = loadMirroredDefn(dis);
		if (md != null) {
			ed.creatMirrorDefinition(md.getMirrorStartingLevel(), md.isBreakHierarchy());
		}
		return ed;
	}

	private static void saveMirroredDefn(DataOutputStream dos, IMirroredDefinition md)
			throws DataException, IOException {
		if (md == null) {
			IOUtil.writeBool(dos, false);
		} else {
			IOUtil.writeBool(dos, true);
			saveLevelDefinition(dos, md.getMirrorStartingLevel());
			IOUtil.writeBool(dos, md.isBreakHierarchy());
		}
	}

	private static IMirroredDefinition loadMirroredDefn(DataInputStream dis) throws DataException, IOException {
		if (!IOUtil.readBool(dis)) {
			return null;
		}
		ILevelDefinition ld = loadLevelDefinition(dis);
		boolean b = IOUtil.readBool(dis);
		IMirroredDefinition md = new MirroredDefinition(ld, b);
		return md;
	}

	private static void saveEdgeDrillFilter(DataOutputStream dos, IEdgeDrillFilter edf, int version)
			throws DataException, IOException {
		if (edf == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		IOUtil.writeBool(dos, true);
		IOUtil.writeString(dos, edf.getName());
		saveHierarchyDefinition(dos, edf.getTargetHierarchy());
		IOUtil.writeString(dos, edf.getTargetLevelName());
		saveFilters(dos, edf.getLevelFilter(), version);
		saveSortDefns(dos, edf.getLevelSort());
		if (writeSize(dos, edf.getTuple()) > 0) {
			for (Object[] tuple : edf.getTuple()) {
				if (writeSize(dos, tuple) > 0) {
					for (Object o : tuple) {
						IOUtil.writeObject(dos, o);
					}
				}
			}
		}
	}

	private static IEdgeDrillFilter loadEdgeDrillFilter(DataInputStream dis, int version)
			throws DataException, IOException {
		if (!IOUtil.readBool(dis)) {
			return null;
		}
		String name = IOUtil.readString(dis);
		EdgeDrillingFilterDefinition edf = new EdgeDrillingFilterDefinition(name);
		edf.setTargetHierarchy(loadHierarchyDefinition(dis));
		edf.setTargetLevelName(IOUtil.readString(dis));
		// load filters
		loadFilters(dis, edf, version);
		loadSortDefns(dis, edf);
		int count = IOUtil.readInt(dis);
		List<Object[]> tuples = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			int size = IOUtil.readInt(dis);
			Object[] tuple = new Object[size];
			for (int j = 0; j < size; j++) {
				tuple[j] = IOUtil.readObject(dis,
						org.eclipse.birt.data.engine.impl.DataEngineSession.getCurrentClassLoader());
			}
			tuples.add(tuple);
		}
		edf.setTuple(tuples);
		return edf;
	}

	private static void saveBindings(DataOutputStream dos, List<IBinding> bindings, int version)
			throws DataException, IOException {
		if (writeSize(dos, bindings) > 0) {
			for (IBinding b : bindings) {
				BindingIOUtil.saveBinding(dos, b, version);
			}
		}
	}

	private static void loadBindings(DataInputStream dis, ICubeQueryDefinition qd, int version)
			throws DataException, IOException {
		int size = IOUtil.readInt(dis);
		for (int i = 0; i < size; i++) {
			IBinding b = BindingIOUtil.loadBinding(dis, version);
			qd.addBinding(b);
		}
	}

	private static void saveMeasures(DataOutputStream dos, List<IMeasureDefinition> measures)
			throws DataException, IOException {
		if (writeSize(dos, measures) > 0) {
			for (IMeasureDefinition m : measures) {
				saveMeasure(dos, m);
			}
		}
	}

	private static void loadMeasures(DataInputStream dis, ICubeQueryDefinition qd) throws DataException, IOException {
		int size = IOUtil.readInt(dis);
		for (int i = 0; i < size; i++) {
			IMeasureDefinition md = loadMeasure(dis);
			IMeasureDefinition md1 = qd.createMeasure(md.getName());
			md1.setAggrFunction(md.getAggrFunction());
		}
	}

	private static void saveMeasure(DataOutputStream dos, IMeasureDefinition m) throws DataException, IOException {
		if (m == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		IOUtil.writeBool(dos, true);
		IOUtil.writeString(dos, m.getName());
		IOUtil.writeString(dos, m.getAggrFunction());
	}

	private static IMeasureDefinition loadMeasure(DataInputStream dis) throws DataException, IOException {
		if (!IOUtil.readBool(dis)) {
			return null;
		}
		String name = IOUtil.readString(dis);
		MeasureDefinition md = new MeasureDefinition(name);
		String aggrFunc = IOUtil.readString(dis);
		md.setAggrFunction(aggrFunc);
		return md;
	}

	private static void saveComputedMeasures(DataOutputStream dos, List<IComputedMeasureDefinition> measures)
			throws DataException, IOException {
		if (writeSize(dos, measures) > 0) {
			for (IComputedMeasureDefinition m : measures) {
				saveComputedMeasure(dos, m);
			}
		}
	}

	private static void loadComputedMeasures(DataInputStream dis, ICubeQueryDefinition qd)
			throws DataException, IOException {
		int size = IOUtil.readInt(dis);
		for (int i = 0; i < size; i++) {
			IComputedMeasureDefinition md = loadComputedMeasure(dis);
			IMeasureDefinition md1 = qd.createComputedMeasure(md.getName(), md.getDataType(), md.getExpression());
			md1.setAggrFunction(md.getAggrFunction());
		}
	}

	private static void saveComputedMeasure(DataOutputStream dos, IComputedMeasureDefinition m)
			throws DataException, IOException {
		if (m == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		saveMeasure(dos, m);
		IOUtil.writeInt(dos, m.getDataType());
		ExprUtil.saveBaseExpr(dos, m.getExpression());
	}

	private static IComputedMeasureDefinition loadComputedMeasure(DataInputStream dis)
			throws DataException, IOException {
		IMeasureDefinition md = loadMeasure(dis);
		if (md == null) {
			return null;
		}
		String name = md.getName();
		int type = IOUtil.readInt(dis);
		IBaseExpression expr = ExprUtil.loadBaseExpr(dis);
		IComputedMeasureDefinition cmd = new ComputedMeasureDefinition(name, type, expr);
		cmd.setAggrFunction(md.getAggrFunction());
		return cmd;
	}

	private static void saveSortDefns(DataOutputStream dos, List<ISortDefinition> sorts)
			throws DataException, IOException {
		if (writeSize(dos, sorts) > 0) {
			for (ISortDefinition sort : sorts) {
				saveSortDefn(dos, sort);
			}
		}
	}

	private static void loadSortDefns(DataInputStream dis, ICubeQueryDefinition cqd) throws DataException, IOException {
		int count = IOUtil.readInt(dis);
		for (int i = 0; i < count; i++) {
			ISortDefinition csd = loadSortDefn(dis);
			cqd.addSort(csd);
		}
	}

	private static void loadSortDefns(DataInputStream dis, IEdgeDrillFilter edf) throws DataException, IOException {
		int count = IOUtil.readInt(dis);
		for (int i = 0; i < count; i++) {
			ISortDefinition sd = loadSortDefn(dis);
			edf.addLevelSort(sd);
		}
	}

	private static void saveHierarchyDefinition(DataOutputStream dos, IHierarchyDefinition hd) throws IOException {
		if (hd == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		IOUtil.writeBool(dos, true);
		IOUtil.writeString(dos, hd.getName());
		saveDimensionDefinition(dos, hd.getDimension());
		if (writeSize(dos, hd.getLevels()) > 0) {
			for (ILevelDefinition ld : hd.getLevels()) {
				IOUtil.writeString(dos, ld.getName());
			}
		}
	}

	private static IHierarchyDefinition loadHierarchyDefinition(DataInputStream dis) throws IOException {
		if (!IOUtil.readBool(dis)) {
			return null;
		}
		String name = IOUtil.readString(dis);
		IDimensionDefinition dd = loadDimensionDefinition(dis);
		HierarchyDefinition hd = new HierarchyDefinition(dd, name);
		int levelCount = IOUtil.readInt(dis);
		for (int i = 0; i < levelCount; i++) {
			hd.createLevel(IOUtil.readString(dis));
		}
		return hd;
	}

	private static void saveLevelDefinition(DataOutputStream dos, ILevelDefinition ld) throws IOException {
		if (ld == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		IOUtil.writeBool(dos, true);
		IOUtil.writeString(dos, ld.getName());
		saveHierarchyDefinition(dos, ld.getHierarchy());
	}

	private static ILevelDefinition loadLevelDefinition(DataInputStream dis) throws IOException {
		if (!IOUtil.readBool(dis)) {
			return null;
		}
		String name = IOUtil.readString(dis);
		IHierarchyDefinition hd = loadHierarchyDefinition(dis);
		return new LevelDefiniton(hd, name);
	}

	private static void saveDimensionDefinition(DataOutputStream dos, IDimensionDefinition dd) throws IOException {
		if (dd == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		IOUtil.writeBool(dos, true);
		IOUtil.writeString(dos, dd.getName());
		if (writeSize(dos, dd.getHierarchy()) > 0) {
			for (IHierarchyDefinition hd : dd.getHierarchy()) {
				IOUtil.writeString(dos, hd.getName());
				if (writeSize(dos, hd.getLevels()) > 0) {
					for (ILevelDefinition ld : hd.getLevels()) {
						IOUtil.writeString(dos, ld.getName());
					}
				}
			}
		}
	}

	private static IDimensionDefinition loadDimensionDefinition(DataInputStream dis) throws IOException {
		if (!IOUtil.readBool(dis)) {
			return null;
		}
		String name = IOUtil.readString(dis);
		DimensionDefinition dd = new DimensionDefinition(name);
		int hierarchyCount = IOUtil.readInt(dis);
		for (int i = 0; i < hierarchyCount; i++) {
			String hierarchyName = IOUtil.readString(dis);
			IHierarchyDefinition hd = dd.createHierarchy(hierarchyName);
			int levelCount = IOUtil.readInt(dis);
			for (int j = 0; j < levelCount; j++) {
				String levelName = IOUtil.readString(dis);
				hd.createLevel(levelName);
			}
		}
		return dd;
	}

	private static <T> int writeSize(DataOutputStream dos, Collection<T> c) throws IOException {
		int size = 0;
		if (c != null) {
			size = c.size();
		}
		IOUtil.writeInt(dos, size);
		return size;
	}

	private static <T> int writeSize(DataOutputStream dos, Object[] os) throws IOException {
		int size = 0;
		if (os != null) {
			size = os.length;
		}
		IOUtil.writeInt(dos, size);
		return size;
	}

	private static void copy(IDimensionDefinition from, IDimensionDefinition to) {
		if (from.getHierarchy() != null) {
			for (IHierarchyDefinition hd : from.getHierarchy()) {
				IHierarchyDefinition hd1 = to.createHierarchy(hd.getName());
				if (hd.getLevels() != null) {
					for (ILevelDefinition ld : hd.getLevels()) {
						hd1.createLevel(ld.getName());
					}
				}
			}
		}
	}

	private static void copy(IEdgeDefinition from, IEdgeDefinition to) {
		if (from.getDimensions() != null) {
			for (IDimensionDefinition dd : from.getDimensions()) {
				IDimensionDefinition dd1 = to.createDimension(dd.getName());
				copy(dd, dd1);
			}
		}
		if (from.getMirroredDefinition() != null) {
			to.creatMirrorDefinition(from.getMirroredDefinition().getMirrorStartingLevel(),
					from.getMirroredDefinition().isBreakHierarchy());
		}
		if (from.getDrillFilter() != null) {
			for (IEdgeDrillFilter edf : from.getDrillFilter()) {
				IEdgeDrillFilter edf1 = to.createDrillFilter(edf.getName());
				copy(edf, edf1);
			}
		}

	}

	private static void copy(IEdgeDrillFilter from, IEdgeDrillFilter to) {
		if (from.getLevelSort() != null) {
			for (ISortDefinition sd : from.getLevelSort()) {
				to.addLevelSort(sd);
			}
		}
		if (from.getLevelFilter() != null) {
			for (IFilterDefinition fd : from.getLevelFilter()) {
				to.addLevelFilter(fd);
			}
		}
		to.setTargetHierarchy(from.getTargetHierarchy());
		to.setTargetLevelName(from.getTargetLevelName());
		to.setTuple(from.getTuple());
	}

	private static void saveFilterDefn(DataOutputStream dos, IFilterDefinition fd, int version) throws IOException {
		if (fd == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		IOUtil.writeBool(dos, true);
		ExprUtil.saveBaseExpr(dos, fd.getExpression());
		if (!(fd instanceof ICubeFilterDefinition)) {
			IOUtil.writeInt(dos, FILTER_DEFN_FLAG_COMMON);
		} else {
			IOUtil.writeInt(dos, FILTER_DEFN_FLAG_CUBE);
			ICubeFilterDefinition cfd = (ICubeFilterDefinition) fd;
			saveLevelDefinition(dos, cfd.getTargetLevel());
			if (writeSize(dos, cfd.getAxisQualifierLevels()) > 0) {
				for (ILevelDefinition ld : cfd.getAxisQualifierLevels()) {
					saveLevelDefinition(dos, ld);
				}
			}
			if (writeSize(dos, cfd.getAxisQualifierValues()) > 0) {
				for (Object o : cfd.getAxisQualifierValues()) {
					IOUtil.writeObject(dos, o);
				}
			}
		}
		if (version >= VersionManager.VERSION_2_6_3_2) {
			IOUtil.writeBool(dos, fd.updateAggregation());
		}
		if (version >= VersionManager.VERSION_4_2_2_1) {
			IOUtil.writeString(dos, fd.getFilterTarget() == null ? null : fd.getFilterTarget().toString());
		}
	}

	private static IFilterDefinition loadFilterDefn(DataInputStream dis, int version) throws IOException {
		if (!IOUtil.readBool(dis)) {
			return null;
		}
		IBaseExpression expr = ExprUtil.loadBaseExpr(dis);
		int type = IOUtil.readInt(dis);
		if (type != FILTER_DEFN_FLAG_CUBE) {
			FilterDefinition fd = new FilterDefinition(expr);
			if (version >= VersionManager.VERSION_2_6_3_2) {
				fd.setUpdateAggregation(IOUtil.readBool(dis));
			}
			if (version >= VersionManager.VERSION_4_2_2_1) {
				String filterTarget = IOUtil.readString(dis);
				if (FilterTarget.DATASET.equals(filterTarget)) {
					fd.setFilterTarget(FilterTarget.DATASET);
				} else if (FilterTarget.RESULTSET.equals(filterTarget)) {
					fd.setFilterTarget(FilterTarget.RESULTSET);
				}
			}
			return fd;
		}
		assert type == FILTER_DEFN_FLAG_CUBE;
		CubeFilterDefinition cfd = new CubeFilterDefinition(expr);
		ILevelDefinition targetLevel = loadLevelDefinition(dis);
		cfd.setTargetLevel(targetLevel);
		int count = IOUtil.readInt(dis);
		ILevelDefinition[] lds = new ILevelDefinition[count];
		for (int i = 0; i < count; i++) {
			lds[i] = loadLevelDefinition(dis);
		}
		cfd.setAxisQualifierLevels(lds);
		count = IOUtil.readInt(dis);
		Object[] os = new Object[count];
		for (int i = 0; i < count; i++) {
			os[i] = IOUtil.readObject(dis, org.eclipse.birt.data.engine.impl.DataEngineSession.getCurrentClassLoader());
		}
		cfd.setAxisQualifierValues(os);
		if (version >= VersionManager.VERSION_2_6_3_2) {
			cfd.setUpdateAggregation(IOUtil.readBool(dis));
		}
		if (version >= VersionManager.VERSION_4_2_2_1) {
			String filterTarget = IOUtil.readString(dis);
			if (FilterTarget.DATASET.equals(filterTarget)) {
				cfd.setFilterTarget(FilterTarget.DATASET);
			} else if (FilterTarget.RESULTSET.equals(filterTarget)) {
				cfd.setFilterTarget(FilterTarget.RESULTSET);
			}
		}
		return cfd;
	}

	private static void saveSortDefn(DataOutputStream dos, ISortDefinition sort) throws IOException {
		if (sort == null) {
			IOUtil.writeBool(dos, false);
			return;
		}
		IOUtil.writeBool(dos, true);

		// save info inherited from {@code ISortDefinition}
		IOUtil.writeString(dos, sort.getColumn());
		ExprUtil.saveBaseExpr(dos, sort.getExpression());
		IOUtil.writeInt(dos, sort.getSortDirection());
		IOUtil.writeInt(dos, sort.getSortStrength());
		IOUtil.writeString(dos, sort.getSortLocale() == null ? null : sort.getSortLocale().getBaseName());

		if (!(sort instanceof ICubeSortDefinition)) {
			IOUtil.writeInt(dos, SORT_DEFN_FLAG_COMMON);
		} else {
			IOUtil.writeInt(dos, SORT_DEFN_FLAG_CUBE);

			ICubeSortDefinition sort1 = (ICubeSortDefinition) sort;

			// save info directly from {@code ICubeSortDefinition}
			saveLevelDefinition(dos, sort1.getTargetLevel());
			if (writeSize(dos, sort1.getAxisQualifierLevels()) > 0) {
				for (ILevelDefinition ld : sort1.getAxisQualifierLevels()) {
					saveLevelDefinition(dos, ld);
				}
			}
			if (writeSize(dos, sort1.getAxisQualifierValues()) > 0) {
				for (Object o : sort1.getAxisQualifierValues()) {
					IOUtil.writeObject(dos, o);
				}
			}
		}
	}

	private static ISortDefinition loadSortDefn(DataInputStream dis) throws IOException {
		if (!IOUtil.readBool(dis)) {
			return null;
		}
		SortDefinition sd = new SortDefinition();
		sd.setColumn(IOUtil.readString(dis));
		sd.setExpression((IScriptExpression) ExprUtil.loadBaseExpr(dis));
		sd.setSortDirection(IOUtil.readInt(dis));
		sd.setSortStrength(IOUtil.readInt(dis));
		String localeName = IOUtil.readString(dis);
		if (localeName != null) {
			sd.setSortLocale(new ULocale(localeName));
		}
		int type = IOUtil.readInt(dis);
		if (type != SORT_DEFN_FLAG_CUBE) {
			return sd;
		}
		assert type == FILTER_DEFN_FLAG_CUBE;

		CubeSortDefinition csd = new CubeSortDefinition();

		// load info inherited from {@code ISortDefinition}
		csd.setColumn(sd.getColumn());
		csd.setExpression(sd.getExpression());
		csd.setSortDirection(sd.getSortDirection());
		csd.setSortLocale(sd.getSortLocale());

		// load info directly from {@code ICubeSortDefinition}
		csd.setTargetLevel(loadLevelDefinition(dis));
		int count = IOUtil.readInt(dis);
		ILevelDefinition[] levels = new ILevelDefinition[count];
		for (int i = 0; i < count; i++) {
			levels[i] = loadLevelDefinition(dis);
		}
		csd.setAxisQualifierLevels(levels);
		count = IOUtil.readInt(dis);
		Object[] values = new Object[count];
		for (int i = 0; i < count; i++) {
			values[i] = IOUtil.readObject(dis,
					org.eclipse.birt.data.engine.impl.DataEngineSession.getCurrentClassLoader());
		}
		csd.setAxisQualifierValues(values);
		return csd;
	}
}
