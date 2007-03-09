package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.data.dte.ReportQueryBuilder;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IQueryContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ReportElementHandle;


public class QueryContext implements IQueryContext
{
	
	Report report;
	
	ExecutionContext context;

	ReportQueryBuilder builder;
	
	ReportItemDesign design;
	
	public QueryContext( ExecutionContext context, ReportQueryBuilder builder )
	{
		this.report = context.getReport( );
		this.context = context;
		this.builder = builder;
	}
	
	public IBaseQueryDefinition[] buildQuery( IBaseQueryDefinition parent,
			ReportElementHandle handle )
	{
		design = report.findDesign( handle );		
		return builder.build( parent, design );
	}

}
