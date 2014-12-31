package org.eclipse.birt.data.engine.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;


public interface IQueryOptimizeHints
{
	public static final String QUERY_OPTIMIZE_HINT = "org.eclipse.birt.data.internal.optimize.hints";
	
	public boolean enablePushDownForTransientQuery( );
	
	public Map<String, List<String>> getOptimizedFilterExpr( );
	
	public Map<String, QuerySpecification> getOptimizedCombinedQuerySpec( );
	
	public Map<String, List<IColumnDefinition>> getTrimmedColumns( );
	
	public Map<String, List<String>> getPushedDownComputedColumns( );
	
	public List<IColumnDefinition> getResultSetsForCombinedQuery( );
	
	public Map<String, List<IFilterDefinition>> getFiltersInAdvance( );
	
	public Map<String, Set<Integer>> getPositionsInCombinedQuery( );
	
	public List<IComputedColumn> getUnpushedDownComputedColumnInCombinedQuery( );
	
	public Map<String, List<String>> getCombinedDataSets( );
	
	public Map<String, Set<String>> getInvalidAliasDataSetNames( );
	
	public Map<String, List<Integer>> getPushedDownDataSetFilters( );
	
	public List<IFilterDefinition> getFilterNeededMerge( );
}
