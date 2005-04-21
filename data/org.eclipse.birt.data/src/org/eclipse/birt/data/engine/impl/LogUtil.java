/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

/**
 * A utility function 
 * The toString method dump all non-empty fields of the given object
 */
public final class LogUtil
{

	public static String toString( Object source )
	{
		if ( source instanceof Collection ){
			return toString_Collection(source);
		}
		else if(source instanceof FilterDefinition){
			return toString_FilterDefinition(source);
		}
		else if(source instanceof ConditionalExpression){
			return toString_ConditionalExpression(source);
		}
		else if(source instanceof ScriptExpression){
			return toString_ScriptExpression(source);
		}
		else if(source instanceof InputParameterBinding){
			return toString_InputParameterBinding(source);
		}
		else if(source instanceof GroupDefinition){
			return toString_GroupDefinition(source);
		}
		else if(source instanceof SortDefinition){
			return toString_SortDefinition(source);
		}
		else if(source instanceof QueryDefinition){
			return toString_QueryDefinition(source);
		}
		else if(source instanceof OdaDataSetDesign){
			return toString_OdaDataSetDesign(source);
		}
		else if(source instanceof OdaDataSourceDesign){
			return toString_OdaDataSourceDesign(source);
		}
		else if(source == null)
			return "null";
		else
			return source.toString();
	}
	
	private static String toString_Collection(Object source){
		String str = "";
		Iterator iterator = ( (Collection) source ).iterator( );
		while ( iterator.hasNext( ) )
		{
			str += toString( iterator.next( ) )+", \r\n\t";
		}
		if ( str.endsWith( "\t" ) )
		{
			str = str.substring( 0, str.length( ) - 5 );
		}
		return str;
	}
	
	private static String toString_FilterDefinition(Object source){
		return "FilterDefinition("+toString(((FilterDefinition)source).getExpression())+")";
	}

	private static String toString_ConditionalExpression(Object source){
		String str = "ConditionalExpression(";
		ConditionalExpression conditionalExpression = (ConditionalExpression)source;
		str += "Operator : "+conditionalExpression.getOperator()+", ";
		str += "Expression : "+toString(conditionalExpression.getExpression())+", ";
		if ( !isEmpty(conditionalExpression.getOperand1( )) )
			str += "Operand1 : "+toString(conditionalExpression.getOperand1())+", ";
		
		if ( !isEmpty(conditionalExpression.getOperand2( )))
			str += "Operand2 : "+toString(conditionalExpression.getOperand2())+")";
		
		return str;
	}
	
	private static String toString_ScriptExpression(Object source){
		return "ScriptExpression(Text:"+((ScriptExpression)source).getText()+")";
	}
	
	private static String toString_InputParameterBinding( Object source )
	{
		InputParameterBinding inputParameterBinding =(InputParameterBinding)source;
		String str="InputParameterBinding(";
		if ( !isEmpty(inputParameterBinding.getName()))
			str+= "Name : " +inputParameterBinding.getName()+", ";
		
		str+= "Position : " +inputParameterBinding.getPosition()+", ";
		str+= "Expression : " + toString(inputParameterBinding.getExpr());
		str+=")";
		return str;
	}
	private static String toString_SortDefinition(Object source){
		SortDefinition sort = (SortDefinition)source;
		String str="SortDefinition(";
		if ( !isEmpty(sort.getColumn()))
			str += "Column : " + sort.getColumn( )+", ";
		
		if ( !isEmpty(sort.getExpression()))
			str += "getExpression : " + sort.getExpression( )+", ";
		
		str += "SortDirection : " + sort.getSortDirection( );
		str+=")";
		return str; 
	}
	private static String toString_GroupDefinition(Object source){
		GroupDefinition group = (GroupDefinition)source;
		String str="GroupDefinition(";
		if ( !isEmpty( group.getName( ) ) )
			str += "Name : " + group.getName( )+", ";

		if ( !isEmpty( group.getKeyColumn( ) ) )
			str += "KeyColumn : " + group.getKeyColumn( )+", ";
		
		if ( !isEmpty( group.getKeyExpression( ) ) )
			str += "KeyExpression : " + group.getKeyExpression( )+", ";

		str += "SortDirection : " + group.getSortDirection( )+", ";
		
		if ( !isEmpty( group.getAfterExpressions( ) ) )
			str += "AfterExpressions : " + toString(group.getAfterExpressions( ))+", ";
		
		if ( !isEmpty( group.getBeforeExpressions( ) ) )
			str += "BeforeExpressions : " + toString(group.getBeforeExpressions( ))+", ";
		
		if ( !isEmpty( group.getRowExpressions( ) ) )
			str += "RowExpressions : " + toString(group.getRowExpressions( ))+", ";
		
		str += "Interval : " + group.getInterval( )+", ";
		str += "IntervalRange : " + group.getIntervalRange( )+", ";
		
		if ( !isEmpty( group.getIntervalStart( ) ) )
			str += "IntervalStart : " + group.getIntervalStart( )+", ";
		
		if ( !isEmpty( group.getSubqueries( ) ) )
			str += "Subqueries : " + toString(group.getSubqueries( ))+", ";
		
		if ( !isEmpty( group.getSorts( ) ) )
			str += "Sorts : " + toString(group.getSorts( ))+", ";
		
		if ( !isEmpty( group.getFilters( ) ) )
			str += "Filters : " + toString(group.getFilters( ));
		str+=")";
		return str; 
	}
	private static String toString_QueryDefinition(Object source){
		QueryDefinition querySpec = (QueryDefinition)source;
		String str="QueryDefinition(";
		str+="DataSetName : "+querySpec.getDataSetName()+"\r\n";
		if ( !isEmpty( querySpec.getAfterExpressions( ) ) )
			str += "AfterExpressions : " + LogUtil.toString( querySpec.getAfterExpressions( ) )	+ "\r\n";
		
		if ( !isEmpty( querySpec.getBeforeExpressions( ) ) )
			str+="BeforeExpressions : "+LogUtil.toString(querySpec.getBeforeExpressions())+"\r\n";
		
		if ( !isEmpty( querySpec.getRowExpressions( ) ) )
			str+="RowExpressions : "+LogUtil.toString(querySpec.getRowExpressions())+"\r\n";
		
		if ( !isEmpty( querySpec.getParentQuery( ) ) )
			str+="ParentQuery : "+LogUtil.toString(querySpec.getParentQuery())+"\r\n";
		
		if ( !isEmpty( querySpec.getSubqueries( ) ) )
			str+="Subqueries : "+LogUtil.toString(querySpec.getSubqueries())+"\r\n";
		
		str+="MaxRows : "+querySpec.getMaxRows()+"\r\n";
		
		if ( !isEmpty( querySpec.getColumnProjection( ) ) )
			str+="ColumnProjection : "+querySpec.getColumnProjection()+"\r\n";
		
		if ( !isEmpty( querySpec.getGroups( ) ) )
			str+="Groups : "+LogUtil.toString(querySpec.getGroups())+"\r\n";
		
		if ( !isEmpty( querySpec.getFilters( ) ) )
			str += "Filters : " + LogUtil.toString( querySpec.getFilters( ) ) + "\r\n";
		
		if ( !isEmpty( querySpec.getSorts( ) ) )
			str+="Sorts : "+LogUtil.toString(querySpec.getSorts())+"\r\n";
		
		if ( !isEmpty( querySpec.getInputParamBindings( ) ) )
			str+="InputParamBindings : "+LogUtil.toString(querySpec.getInputParamBindings())+")\r\n";

		return str; 
		
	}
	
	private static String toString_OdaDataSetDesign(Object source){
		OdaDataSetDesign dataSet = (OdaDataSetDesign)source;
		String str="OdaDataSetDesign(";
		//BaseDataSetDesign
		if ( !isEmpty( dataSet.getName( ) ) )
			str+="Name : "+dataSet.getName()+"\r\n";
		
		if ( !isEmpty( dataSet.getDataSourceName( ) ) )
			str+="DataSourceName : "+dataSet.getDataSourceName()+"\r\n";
		
		if ( !isEmpty( dataSet.getAfterCloseScript( ) ) )
			str+="AfterCloseScript : "+dataSet.getAfterCloseScript()+"\r\n";

		if ( !isEmpty( dataSet.getAfterOpenScript( ) ) )
			str+="AfterOpenScript : "+dataSet.getAfterOpenScript()+"\r\n";
		
		if ( !isEmpty( dataSet.getBeforeCloseScript( ) ) )
			str+="BeforeCloseScript : "+dataSet.getBeforeCloseScript()+"\r\n";
		
		if ( !isEmpty( dataSet.getBeforeOpenScript( ) ) )
			str+="BeforeOpenScript : "+dataSet.getBeforeOpenScript()+"\r\n";
		
		if ( !isEmpty( dataSet.getOnFetchScript( ) ) )
			str+="OnFetchScript : "+dataSet.getOnFetchScript()+"\r\n";
		
		if ( !isEmpty( dataSet.getComputedColumns( ) ) )
			str+="ComputedColumns : "+ toString(dataSet.getComputedColumns())+"\r\n";
		
		if ( !isEmpty( dataSet.getFilters( ) ) )
			str+="Filters : "+ toString(dataSet.getFilters())+"\r\n";
		
		if ( !isEmpty( dataSet.getParameters( ) ) )
			str+="Parameters : "+ toString(dataSet.getParameters())+"\r\n";
		
		if ( !isEmpty( dataSet.getInputParamBindings( ) ) )
			str+="InputParamBindings : "+ toString(dataSet.getInputParamBindings())+"\r\n";

		if ( !isEmpty( dataSet.getResultSetHints( ) ) )
			str+="ResultSetHints : "+ toString(dataSet.getResultSetHints())+"\r\n";
		//OdaDataSetDesign
		if ( !isEmpty( dataSet.getDataSetType( ) ) )
			str+="DataSetType : "+dataSet.getDataSetType()+"\r\n";
		
		if ( !isEmpty( dataSet.getPrimaryResultSetName( ) ) )
			str+="PrimaryResultSetName : "+dataSet.getPrimaryResultSetName()+"\r\n";
		
		if ( !isEmpty( dataSet.getQueryScript( ) ) )
			str+="QueryScript : "+dataSet.getQueryScript()+"\r\n";
		
		if ( !isEmpty( dataSet.getQueryText( ) ) )
			str+="QueryText : "+dataSet.getQueryText()+"\r\n";
		
		if ( !isEmpty( dataSet.getPrivateProperties( ) ) )
			str+="PrivateProperties : "+toString(dataSet.getPrivateProperties())+"\r\n";
		
		if ( !isEmpty( dataSet.getPublicProperties( ) ) )
			str+="PublicProperties : "+toString(dataSet.getPublicProperties())+"\r\n";
				
		str+=")";
		return str;
	}
	
	private static String toString_OdaDataSourceDesign(Object source){
		OdaDataSourceDesign dataSource = (OdaDataSourceDesign)source;
		String str="OdaDataSourceDesign(";
		//BaseDataSourceDesign
		if ( !isEmpty( dataSource.getName( ) ) )
		str+="Name : "+dataSource.getName()+"\r\n";
		if ( !isEmpty( dataSource.getAfterCloseScript( ) ) )
		str+="AfterCloseScript : "+dataSource.getAfterCloseScript()+"\r\n";
		if ( !isEmpty( dataSource.getAfterOpenScript( ) ) )
		str+="AfterOpenScript : "+dataSource.getAfterOpenScript()+"\r\n";
		if ( !isEmpty( dataSource.getBeforeCloseScript( ) ) )
		str+="BeforeCloseScript : "+dataSource.getBeforeCloseScript()+"\r\n";
		if ( !isEmpty( dataSource.getBeforeOpenScript( ) ) )
		str+="BeforeOpenScript : "+dataSource.getBeforeOpenScript()+"\r\n";
		//OdaDataSourceDesign
		if ( !isEmpty( dataSource.getDriverName( ) ) )
			str+="DriverName : "+dataSource.getDriverName()+"\r\n";
		if ( !isEmpty( dataSource.getPrivateProperties( ) ) )
			str+="PrivateProperties : "+dataSource.getPrivateProperties()+"\r\n";
		if ( !isEmpty( dataSource.getPublicProperties( ) ) )
			str+="PublicProperties : "+dataSource.getPublicProperties()+"\r\n";
		
		str+=")";
		return str;
	}

	private static boolean isEmpty( Object source )
	{
		if ( source == null || "".equals( toString( source ) ) )
			return true;
		else
			return false;
	}
}