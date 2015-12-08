/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved.
 *******************************************************************************/

package org.eclipse.birt.report.model.api.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * 
 */

@SuppressWarnings("rawtypes")
public class ExpressionUtility
{

	private static class ParseResult
	{

		boolean usesDataSetRow = false;
		boolean usesMeasure = false;
	}

	/**
	 * 
	 * @param reportItem
	 * @param expression
	 * @return boolean true if uses dataSetRow or does not use measure; else
	 *         false
	 * @throws BirtException
	 */
	public static boolean useDataSetRow( ReportItemHandle reportItem,
			String expression ) throws BirtException
	{
		ParseResult result = new ParseResult( );
		parseExpression( reportItem, expression, result );
		return result.usesDataSetRow || !result.usesMeasure;
	}

	/**
	 * Parsing algorithm is as follows:
	 * 
	 * Find all references to bindings by binding column name
	 * Need check independently for binding by each type
	 * Types are dataSetRow, row, data and measure
	 * Get expression associated with each binding
	 * Recursively parse each nested expressions
	 * Set usesMeasure flag true if we find measure
	 * Set usesDataSetRow flag true if we find dataSetRow
	 * Also if we find dataSetRow, we stop algorithm immediately
	 * Also, we do not recursively parse measure expressions
	 * 
	 * @param reportItem
	 * @param expression
	 * @param result
	 * @throws BirtException
	 */
	private static void parseExpression( ReportItemHandle reportItem,
			String expression, ParseResult result ) throws BirtException
	{
		// Get all referenced "bindings" that are "dataSetRow" expressions
		List dataSetRows = ExpressionUtil.extractColumnExpressions( expression,
				ExpressionUtil.DATASET_ROW_INDICATOR );
		if ( dataSetRows.size( ) > 0 )
		{
			// If we have dataSetRow, set to true and return immediately
			result.usesDataSetRow = true;
			return;
		}

		List<String> bindingColumnNames = new ArrayList<String>( );

		// Get all referenced "binding column names" that are "row" expressions
		bindingColumnNames.addAll( getReferencedColumnNames( expression,
				ExpressionUtil.ROW_INDICATOR ) );

		// Get all referenced "binding column names" that are "data" expressions
		bindingColumnNames.addAll( getReferencedColumnNames( expression,
				ExpressionUtil.DATA_INDICATOR ) );

		// Parse expressions of all referenced bindings
		if ( bindingColumnNames.size( ) > 0 )
		{
			Iterator iterator = reportItem.getAvailableBindings( );
			while ( iterator.hasNext( ) )
			{
				Object next = iterator.next( );
				ComputedColumnHandle handle = (ComputedColumnHandle) next;
				String columnName = handle.getName( );
				if ( bindingColumnNames.contains( columnName ) )
				{
					String expr = handle.getExpression( );
					// Recursion
					parseExpression( reportItem, expr, result );
					if ( result.usesDataSetRow )
					{
						// If we have dataSetRow, return immediately
						return;
					}
				}
			}
		}

		// Get all referenced "bindings" that are "measure" expressions
		List measures = ExpressionUtil.extractColumnExpressions( expression,
				ExpressionUtil.MEASURE_INDICATOR );
		if ( measures.size( ) > 0 )
		{
			// If we have measure, set to true, but, continue parsing
			result.usesMeasure = true;
		}

	}

	/**
	 * Get a list of all the binding column names for the given indicator
	 * Indicator is dataSetRow, row, measure, etc...
	 * 
	 * @param expression
	 * @param indicator
	 * @return
	 * @throws BirtException
	 */
	private static List<String> getReferencedColumnNames( String expression,
			String indicator ) throws BirtException
	{
		List list = ExpressionUtil.extractColumnExpressions( expression,
				indicator );
		List<String> resultSetColumnNames = new ArrayList<String>( );
		for ( Object object : list )
		{
			IColumnBinding binding = (IColumnBinding) object;
			resultSetColumnNames.add( binding.getResultSetColumnName( ) );
		}
		return resultSetColumnNames;
	}

}
