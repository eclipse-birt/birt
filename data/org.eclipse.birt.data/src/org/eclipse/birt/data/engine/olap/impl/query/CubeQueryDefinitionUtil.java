
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved.
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.impl.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;
import org.eclipse.birt.data.engine.api.timefunction.ITimePeriod;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriodType;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.ExprUtil;
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

import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class CubeQueryDefinitionUtil {
	private CubeQueryDefinitionUtil() {
	};

	/**
	 * Returns hint info about how newQuery can be executed based on the execution
	 * result of basedQuery.
	 * 
	 * @param basedQuery
	 * @param newQuery
	 * @return null if the execution result of basedQuery is useless for newQuery
	 */
	public static IncrementExecutionHint getIncrementExecutionHint(ICubeQueryDefinition basedQuery,
			ICubeQueryDefinition newQuery) throws DataException {
		if (basedQuery.getFilterOption() != newQuery.getFilterOption()) {
			return null;
		}
		if (!isEqualMeasures(basedQuery.getMeasures(), newQuery.getMeasures())) {
			return null;
		}
		if (!isEqualMeasures(basedQuery.getComputedMeasures(), newQuery.getComputedMeasures())) {
			return null;
		}
		if (!isEqualEdges(basedQuery, newQuery)) {
			return null;
		}
		if (!isEqualCubeOperations(basedQuery.getCubeOperations(), newQuery.getCubeOperations())) {
			return null;
		}
		IncrementExecutionHint ieh = new IncrementExecutionHint();
		IBinding[] bindings = getIncrementBindings(basedQuery, newQuery);
		if (bindings == null) {
			return null;
		} else {
			ieh.setBindings(bindings);
		}
		IFilterDefinition[] filters = getIncrementFilters(basedQuery, newQuery);
		if (filters == null) {
			return null;
		} else {
			ieh.setFilters(filters);
		}
		ISortDefinition[] sorts = getIncrementSorts(basedQuery, newQuery);
		ieh.setSorts(sorts);
		return ieh;
	}

	private static IBinding[] getIncrementBindings(ICubeQueryDefinition basedQuery, ICubeQueryDefinition newQuery)
			throws DataException {
		if (newQuery.getBindings().size() < basedQuery.getBindings().size()) {
			return null;
		}
		Iterator<IBinding> itr1 = basedQuery.getBindings().iterator();
		Iterator<IBinding> itr2 = newQuery.getBindings().iterator();
		Map<String, IBinding> bindings1 = new HashMap<String, IBinding>();
		Map<String, IBinding> bindings2 = new HashMap<String, IBinding>();
		while (itr1.hasNext()) {
			IBinding b1 = itr1.next();
			IBinding b2 = itr2.next();
			bindings1.put(b1.getBindingName(), b1);
			bindings2.put(b2.getBindingName(), b2);
		}
		while (itr2.hasNext()) {
			IBinding b2 = itr2.next();
			bindings2.put(b2.getBindingName(), b2);
		}
		for (String name : bindings1.keySet()) {
			IBinding b2 = bindings2.get(name);
			if (b2 == null) {
				return null;
			} else {
				if (!isEqual(bindings1.get(name), b2)) {
					return null;
				}
				bindings2.remove(name);
			}
		}
		if (!bindings2.isEmpty()) {
			// currently, can't apply increment execution for new added bindings
			return null;
		}
		return new IBinding[0];
	}

	private static IFilterDefinition[] getIncrementFilters(ICubeQueryDefinition basedQuery,
			ICubeQueryDefinition newQuery) throws DataException {
		if (newQuery.getFilters().size() < basedQuery.getFilters().size()) {
			return null;
		}
		List baseFilters = basedQuery.getFilters();
		List newFilters = newQuery.getFilters();

		List<IFilterDefinition> resultFilters = new ArrayList<IFilterDefinition>();
		for (int i = 0; i < newFilters.size(); i++) {
			IFilterDefinition filter = (IFilterDefinition) newFilters.get(i);
			boolean find = false;
			for (int j = 0; j < baseFilters.size(); j++) {
				if (isEqual((IFilterDefinition) newFilters.get(i), (IFilterDefinition) baseFilters.get(j))) {
					if (((IFilterDefinition) newFilters.get(i))
							.updateAggregation() == ((IFilterDefinition) baseFilters.get(j)).updateAggregation()) {
						find = true;
						break;
					} else {
						return null;
					}
				}
			}
			if (!find) {
				if (!filter.updateAggregation() && newQuery.getFilters().size() > basedQuery.getFilters().size()) {
					resultFilters.add(filter);
				} else
					return null;
			} else {
				if (filter.updateAggregation())
					return null;
			}

		}

		return resultFilters.toArray(new IFilterDefinition[0]);
	}

	private static ISortDefinition[] getIncrementSorts(ICubeQueryDefinition basedQuery, ICubeQueryDefinition newQuery)
			throws DataException {
		if (isEqualSorts(basedQuery.getSorts(), newQuery.getSorts())) {
			return new ISortDefinition[0];
		} else {
			List<ISortDefinition> result = newQuery.getSorts();
			return result.toArray(new ISortDefinition[0]);
		}
	}

	private static boolean isEqual(IFilterDefinition fd1, IFilterDefinition fd2) {
		if (fd1 == null) {
			return null == fd2;
		}
		if (fd2 == null) {
			return false;
		}
		if (!fd1.getClass().equals(fd2.getClass())) {
			return false;
		}
		if (!ExprUtil.isEqualExpression(fd1.getExpression(), fd2.getExpression())) {
			return false;
		}
		if (fd1 instanceof ICubeSortDefinition) {
			ICubeFilterDefinition cfd1 = (ICubeFilterDefinition) fd1;
			ICubeFilterDefinition cfd2 = (ICubeFilterDefinition) fd2;
			if (!isEqual(cfd1.getTargetLevel(), cfd2.getTargetLevel())) {
				return false;
			}
			if (!Arrays.deepEquals(cfd1.getAxisQualifierValues(), cfd2.getAxisQualifierValues())) {
				return false;
			}
			if (cfd1.getAxisQualifierLevels() == null) {
				return cfd2.getAxisQualifierLevels() == null;
			}
			if (cfd1.getAxisQualifierLevels().length != cfd2.getAxisQualifierLevels().length) {
				return false;
			}
			int i = 0;
			for (ILevelDefinition ld : cfd1.getAxisQualifierLevels()) {
				if (!isEqual(ld, cfd2.getAxisQualifierLevels()[i])) {
					return false;
				}
				i++;
			}
		}
		return true;
	}

	private static boolean isEqual(IBinding b1, IBinding b2) throws DataException {
		if (!b1.getBindingName().equals(b2.getBindingName())) {
			return false;
		}
		if (b1.getDataType() != b2.getDataType()) {
			return false;
		}
		if (!isEqual(b1.getAggrFunction(), b2.getAggrFunction())) {
			return false;
		}
		if (!ExprUtil.isEqualExpression(b1.getExpression(), b2.getExpression())) {
			return false;
		}
		if (!ExprUtil.isEqualExpression(b1.getFilter(), b2.getFilter())) {
			return false;
		}
		if (b1.getArguments().size() != b2.getArguments().size()) {
			return false;
		}
		Iterator itr1 = b1.getArguments().iterator();
		Iterator itr2 = b2.getArguments().iterator();
		while (itr1.hasNext()) {
			IBaseExpression expr1 = (IBaseExpression) itr1.next();
			IBaseExpression expr2 = (IBaseExpression) itr2.next();
			if (!ExprUtil.isEqualExpression(expr1, expr2)) {
				return false;
			}
		}
		if (!Arrays.deepEquals(b1.getAggregatOns().toArray(), b2.getAggregatOns().toArray())) {
			return false;
		}

		if (!isEqualTimeFunction(b1.getTimeFunction(), b2.getTimeFunction())) {
			return false;
		}

		return true;
	}

	private static boolean isEqual(String s1, String s2) {
		if (s1 == null) {
			return s1 == s2;
		} else {
			return s1.equals(s2);
		}
	}

	private static boolean isEqual(IDimensionDefinition dd1, IDimensionDefinition dd2) {
		if (dd1 == null) {
			return null == dd2;
		}
		if (dd2 == null) {
			return false;
		}
		if (!isEqual(dd1.getName(), dd2.getName())) {
			return false;
		}
		if (dd1.getHierarchy().size() != dd2.getHierarchy().size()) {
			return false;
		}
		int i = 0;
		for (IHierarchyDefinition hd : dd1.getHierarchy()) {
			if (!isEqual(hd.getName(), dd2.getHierarchy().get(i).getName())) {
				return false;
			}
			if (hd.getLevels().size() != dd2.getHierarchy().get(i).getLevels().size()) {
				return false;
			}
			int j = 0;
			for (ILevelDefinition ld : hd.getLevels()) {
				if (!isEqual(ld.getName(), dd2.getHierarchy().get(i).getLevels().get(j).getName())) {
					return false;
				}
				j++;
			}
			i++;
		}
		return true;
	}

	private static boolean isEqual(IHierarchyDefinition hd1, IHierarchyDefinition hd2) {
		if (hd1 == null) {
			return null == hd2;
		}
		if (hd2 == null) {
			return false;
		}
		if (!isEqual(hd1.getName(), hd2.getName())) {
			return false;
		}
		if (hd1.getLevels().size() != hd2.getLevels().size()) {
			return false;
		}
		int j = 0;
		for (ILevelDefinition ld : hd1.getLevels()) {
			if (!isEqual(ld.getName(), hd2.getLevels().get(j).getName())) {
				return false;
			}
			j++;
		}
		return true;
	}

	private static boolean isEqual(ILevelDefinition ld1, ILevelDefinition ld2) {
		if (ld1 == null) {
			return null == ld2;
		}
		if (ld2 == null) {
			return false;
		}
		if (!isEqual(ld1.getName(), ld2.getName())) {
			return false;
		}
		return isEqual(ld1.getHierarchy(), ld2.getHierarchy());
	}

	private static boolean isEqual(IMeasureDefinition md1, IMeasureDefinition md2) throws DataException {
		if (md1 == null) {
			return null == md2;
		}
		if (md2 == null) {
			return false;
		}
		if (!md1.getClass().equals(md2.getClass())) {
			return false;
		}
		if (!isEqual(md1.getName(), md2.getName()) || !isEqual(md1.getAggrFunction(), md2.getAggrFunction())) {
			return false;
		}
		if (md1 instanceof IComputedMeasureDefinition) {
			IComputedMeasureDefinition cmd1 = (IComputedMeasureDefinition) md1;
			IComputedMeasureDefinition cmd2 = (IComputedMeasureDefinition) md1;
			if (!ExprUtil.isEqualExpression(cmd1.getExpression(), cmd2.getExpression())) {
				return false;
			}
			return cmd1.getDataType() == cmd2.getDataType();
		}
		return true;
	}

	private static boolean isEqualMeasures(List<IMeasureDefinition> mds1, List<IMeasureDefinition> mds2)
			throws DataException {
		if (mds1.size() != mds2.size()) {
			return false;
		}
		Map<String, IMeasureDefinition> map1 = new HashMap<String, IMeasureDefinition>();
		Map<String, IMeasureDefinition> map2 = new HashMap<String, IMeasureDefinition>();
		Iterator<IMeasureDefinition> itr1 = mds1.iterator();
		Iterator<IMeasureDefinition> itr2 = mds2.iterator();
		while (itr1.hasNext()) {
			IMeasureDefinition md1 = itr1.next();
			IMeasureDefinition md2 = itr2.next();
			map1.put(md1.getName(), md1);
			map2.put(md2.getName(), md2);
		}
		for (String name : map1.keySet()) {
			IMeasureDefinition md2 = map2.get(name);
			if (md2 == null || !isEqual(map1.get(name), md2)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isEqualEdges(ICubeQueryDefinition cqd1, ICubeQueryDefinition cqd2) {
		return isEqual(cqd1.getEdge(ICubeQueryDefinition.ROW_EDGE), cqd2.getEdge(ICubeQueryDefinition.ROW_EDGE))
				&& isEqual(cqd1.getEdge(ICubeQueryDefinition.COLUMN_EDGE),
						cqd2.getEdge(ICubeQueryDefinition.COLUMN_EDGE))
				&& isEqual(cqd1.getEdge(ICubeQueryDefinition.PAGE_EDGE), cqd2.getEdge(ICubeQueryDefinition.PAGE_EDGE));
	}

	private static boolean isEqual(IEdgeDefinition ed1, IEdgeDefinition ed2) {
		if (ed1 == null) {
			return null == ed2;
		}
		if (ed2 == null) {
			return false;
		}
		if (!isEqual(ed1.getName(), ed2.getName())) {
			return false;
		}
		if (ed1.getDimensions().size() != ed2.getDimensions().size()) {
			return false;
		}
		int i = 0;
		for (IDimensionDefinition dd : ed1.getDimensions()) {
			if (!isEqual(dd, ed2.getDimensions().get(i))) {
				return false;
			}
			i++;
		}
		if (ed1.getMirroredDefinition() == null) {
			if (ed2.getMirroredDefinition() != null) {
				return false;
			}
		} else {
			if (ed2.getMirroredDefinition() == null) {
				return false;
			} else {
				if (!isEqual(ed1.getMirroredDefinition().getMirrorStartingLevel(),
						ed2.getMirroredDefinition().getMirrorStartingLevel())) {
					return false;
				}
				if (ed1.getMirroredDefinition().isBreakHierarchy() != ed2.getMirroredDefinition().isBreakHierarchy()) {
					return false;
				}
			}
		}

		if (ed1.getDrillFilter().size() != ed2.getDrillFilter().size()) {
			return false;
		}
		i = 0;
		for (IEdgeDrillFilter edf : ed1.getDrillFilter()) {
			if (!isEqual(edf, ed2.getDrillFilter().get(i))) {
				return false;
			}
			i++;
		}
		return true;
	}

	private static boolean isEqual(IEdgeDrillFilter edf1, IEdgeDrillFilter edf2) {
		if (edf1 == null) {
			return null == edf2;
		}
		if (edf2 == null) {
			return false;
		}
		if (edf1.getLevelFilter().size() != edf2.getLevelFilter().size()) {
			return false;
		}
		{
			Iterator<IFilterDefinition> itr1 = edf1.getLevelFilter().iterator();
			Iterator<IFilterDefinition> itr2 = edf2.getLevelFilter().iterator();
			while (itr1.hasNext()) {
				if (!isEqual(itr1.next(), itr2.next())) {
					return false;
				}
			}
		}
		if (edf1.getLevelSort().size() != edf2.getLevelSort().size()) {
			return false;
		}
		Iterator<ISortDefinition> itr1 = edf1.getLevelSort().iterator();
		Iterator<ISortDefinition> itr2 = edf2.getLevelSort().iterator();
		while (itr1.hasNext()) {
			if (!isEqual(itr1.next(), itr2.next())) {
				return false;
			}
		}
		if (!isEqual(edf1.getTargetHierarchy(), edf2.getTargetHierarchy())) {
			return false;
		}
		if (!isEqual(edf1.getTargetLevelName(), edf2.getTargetLevelName())) {
			return false;
		}
		return Arrays.deepEquals(edf1.getTuple().toArray(), edf2.getTuple().toArray());
	}

	private static boolean isEqual(ULocale l1, ULocale l2) {
		if (l1 == null) {
			return null == l2;
		}
		if (l2 == null) {
			return false;
		}
		return isEqual(l1.getBaseName(), l2.getBaseName());
	}

	private static boolean isEqual(ISortDefinition sd1, ISortDefinition sd2) {
		if (sd1 == null) {
			return null == sd2;
		}
		if (sd2 == null) {
			return false;
		}
		if (!sd1.getClass().equals(sd2.getClass()) || !isEqual(sd1.getColumn(), sd2.getColumn())
				|| sd1.getSortDirection() != sd2.getSortDirection()
				|| !ExprUtil.isEqualExpression(sd1.getExpression(), sd2.getExpression())
				|| sd1.getSortStrength() != sd2.getSortStrength()
				|| !isEqual(sd1.getSortLocale(), sd2.getSortLocale())) {
			return false;
		}
		if (sd1 instanceof ICubeSortDefinition) {
			ICubeSortDefinition s1 = (ICubeSortDefinition) sd1;
			ICubeSortDefinition s2 = (ICubeSortDefinition) sd2;

			if (!Arrays.equals(s1.getAxisQualifierValues(), s2.getAxisQualifierValues())) {
				return false;
			}
			if (s1.getAxisQualifierLevels() == null) {
				if (null != s2.getAxisQualifierLevels()) {
					return false;
				}
			} else {
				if (s2.getAxisQualifierLevels() == null) {
					return false;
				}
				if (s1.getAxisQualifierLevels().length != s2.getAxisQualifierLevels().length) {
					return false;
				}
				int i = 0;
				for (ILevelDefinition ld : s1.getAxisQualifierLevels()) {
					if (!isEqual(ld, s2.getAxisQualifierLevels()[i])) {
						return false;
					}
					i++;
				}
			}
			if (!isEqual(s1.getTargetLevel(), s2.getTargetLevel())) {
				return false;
			}
		}
		return true;
	}

	private static boolean isEqual(ICubeOperation co1, ICubeOperation co2) throws DataException {
		if (co1 == null) {
			return co2 == null;
		}
		if (co2 == null) {
			return false;
		}
		if (!co1.getClass().equals(co2.getClass())) {
			return false;
		}
		if (co1.getNewBindings().length != co2.getNewBindings().length) {
			return false;
		}
		for (int i = 0; i < co1.getNewBindings().length; i++) {
			if (!isEqual(co1.getNewBindings()[i], co2.getNewBindings()[i])) {
				return false;
			}
		}
		return true;
	}

	private static boolean isEqualCubeOperations(ICubeOperation[] cos1, ICubeOperation[] cos2) throws DataException {
		if (cos1.length != cos2.length) {
			return false;
		}
		for (int i = 0; i < cos1.length; i++) {
			if (!isEqual(cos1[i], cos2[i])) {
				return false;
			}
		}
		return true;
	}

	private static boolean isEqualSorts(List<ISortDefinition> sorts1, List<ISortDefinition> sorts2) {
		if (sorts1.size() != sorts2.size()) {
			return false;
		}
		int i = 0;
		for (ISortDefinition sd : sorts1) {
			if (!isEqual(sd, sorts2.get(i))) {
				return false;
			}
		}
		return true;
	}

	private static boolean isEqualTimeFunction(ITimeFunction f1, ITimeFunction f2) throws DataException {
		if (f1 == f2)
			return true;

		if (f1 == null || f2 == null)
			return false;

		if (!isEqual(f1.getTimeDimension(), f2.getTimeDimension()))
			return false;

		if (f1.getReferenceDate().getDate() != null
				&& !f1.getReferenceDate().getDate().equals(f2.getReferenceDate().getDate()))
			return false;

		if (f2.getReferenceDate().getDate() != null
				&& !f2.getReferenceDate().getDate().equals(f1.getReferenceDate().getDate()))
			return false;

		if (!isEqualTimePeriod(f1.getBaseTimePeriod(), f2.getBaseTimePeriod()))
			return false;

		if (!isEqualTimePeriod(f1.getRelativeTimePeriod(), f2.getRelativeTimePeriod()))
			return false;
		return true;
	}

	private static boolean isEqualTimePeriod(ITimePeriod p1, ITimePeriod p2) {
		if (p1 == p2)
			return true;

		if (p1 == null || p2 == null)
			return false;

		TimePeriodType type1 = p1.getType();
		TimePeriodType type2 = p2.getType();
		if (!type1.equals(type2)) {
			return false;
		}

		int unit1 = p1.countOfUnit();
		int unit2 = p2.countOfUnit();
		if (unit1 != unit2) {
			return false;
		}

		boolean current1 = p1.isCurrent();
		boolean current2 = p2.isCurrent();

		if (current1 != current2) {
			return false;
		}
		return true;
	}
}
