/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.TreeSet;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.impl.GetParameterDefinitionTask.CascadingParameterSelectionChoice;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

import com.ibm.icu.util.ULocale;


public class ParameterHelper
{
	private static final String VALUE_PREFIX = "__VALUE__";

	private static final String LABEL_PREFIX = "__LABEL__";

	private ScalarParameterHandle parameter;
	private boolean distinct;
	private Comparator comparator;
	private String labelColumnName;
	private String valueColumnName;
	private String valueType;

	private static class FixInOrderComparator implements Comparator
	{
		private boolean distinct;
		
		private FixInOrderComparator(boolean distinct )
		{
			this.distinct = distinct;
		}
		
		public int compare( Object arg0, Object arg1 )
		{
			if ( distinct )
			{
				if ( arg0 == null )
				{
					if ( arg0 == null )
						return 0;
				}
				else
				{
					if ( arg0.equals( arg1 ) )
						return 0;
				}
			}
			return 1;
		}
	};
	
	public ParameterHelper( ScalarParameterHandle parameter, Locale locale )
	{
		this.parameter = parameter;
		this.distinct = parameter.distinct( );
		this.labelColumnName = getLabelColumnName( parameter );
		this.valueColumnName = getValueColumnName( parameter );
		this.valueType = parameter.getDataType( );
		if ( parameter.isFixedOrder( ) )
		{
			this.comparator = new FixInOrderComparator( distinct );
		}
		else
		{
			this.comparator = createComparator( parameter, locale, distinct );
		}
	}
	
	public CascadingParameterSelectionChoice createCascadingParameterSelectionChoice(
			IResultIterator iterator ) throws BirtException
	{
		String label = getLabel( iterator );
		Object value = getValue( iterator );
		return new CascadingParameterSelectionChoice( label, value );
	}

	public CascadingParameterSelectionChoice createCascadingParameterSelectionChoice(
			IParameterSelectionChoice choice )
	{
		String label = choice.getLabel( );
		Object value = choice.getValue( );
		return new CascadingParameterSelectionChoice( label, value );
	}

	public CascadingParameterSelectionChoice createCascadingParameterSelectionChoice(
			String label, Object value )
	{
		return new CascadingParameterSelectionChoice( label, value );
	}
	
	public Collection createSelectionCollection( )
	{
		return new TreeSet( comparator );
	}
	
	public String getLabel( IResultIterator resultIterator )
			throws BirtException
	{
		if ( labelColumnName == null )
		{
			return null;
		}
		return resultIterator.getString( labelColumnName );
	}
	
	public Object getValue( IResultIterator resultIterator )
			throws BirtException
	{
		Object value = resultIterator.getString( valueColumnName );
		return EngineTask.convertParameterType( value, valueType );
	}
	
	public static void addParameterBinding( QueryDefinition queryDefn,
			ScalarParameterHandle parameter ) throws DataException
	{
		String labelColumnName = getLabelColumnName( parameter );
		String valueColumnName = getValueColumnName( parameter );
		if ( labelColumnName != null )
		{
			addBinding( queryDefn, labelColumnName, parameter.getLabelExpr( ) );
		}
		addBinding( queryDefn, valueColumnName, parameter.getValueExpr( ) );
	}
	
	public static void addBinding( QueryDefinition queryDefinition,
			String columnName, String expression ) throws DataException
	{
		ScriptExpression labelScriptExpr = new ScriptExpression( expression );
		IBinding binding = new Binding( columnName, labelScriptExpr );
		queryDefinition.addBinding( binding );
	}
	
	private static Comparator createComparator(
			ScalarParameterHandle parameter, Locale locale, boolean distinct )
	{
		boolean sortDirectionValue = "asc".equalsIgnoreCase( parameter.getSortDirection( ) );
		boolean sortByLabel = "label".equalsIgnoreCase( parameter.getSortBy( ) );
		String pattern = parameter.getPattern( );
		SelectionChoiceComparator selectionChoiceComparator = new SelectionChoiceComparator(
				sortByLabel, pattern, sortDirectionValue, ULocale
						.forLocale( locale ) );
		return new DistinctComparatorDecorator(selectionChoiceComparator, distinct);
	}

	static class DistinctComparatorDecorator implements Comparator
	{
		private boolean distinct;
		private Comparator comparator;
		
		public DistinctComparatorDecorator( Comparator comparator, boolean distinct )
		{
			this.comparator = comparator;
			this.distinct = distinct;
		}
		
		public int compare( Object obj1, Object obj2 )
		{
			int result = comparator.compare( obj1, obj2 );
			if ( result == 0 && !distinct)
			{
				result = 1;
			}
			return result;
		}
	}
	
	public static String getValueColumnName( ScalarParameterHandle parameter )
	{
		return VALUE_PREFIX + "_" + parameter.getName( );
	}

	public static String getLabelColumnName(  ScalarParameterHandle parameter )
	{
		if ( parameter.getLabelExpr( ) == null )
		{
			return null;
		}
		return LABEL_PREFIX + "_" + parameter.getName( );
	}
}

