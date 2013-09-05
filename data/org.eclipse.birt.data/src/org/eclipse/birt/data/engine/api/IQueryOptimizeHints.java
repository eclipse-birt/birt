package org.eclipse.birt.data.engine.api;

import java.util.List;
import java.util.Map;

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
}
