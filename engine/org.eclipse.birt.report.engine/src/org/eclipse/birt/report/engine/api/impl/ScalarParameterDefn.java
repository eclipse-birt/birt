/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IScalarParameterDefn;

/**
 * Defines a scalar parameter
 */
public class ScalarParameterDefn extends ParameterDefn implements IScalarParameterDefn
{
	protected boolean 	cancealValue;
	protected boolean 	allowNull;
	protected boolean 	allowBlank;
	protected String 	displayFormat;
	protected int 		controlType;
	protected int 		alignment;
	
	protected ArrayList selectionList;
	
	protected boolean fixedOrder;
	
	protected int parameterType;
	
	protected int selectionListType;

	protected String valueExpr;
	protected int dataType;
	protected boolean allowNewValues;

	protected String defaultValueExpr;
	
	
	protected Logger log = Logger.getLogger( ScalarParameterDefn.class.getName( ) );
	
	/**
	 * @return
	 */
	public String getDefaultValueExpr()
	{
		return defaultValueExpr;
	}
	
	/**
	 * @param value
	 */
	public void setDefaultValueExpr(String value)
	{
		this.defaultValueExpr = value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#isValueConcealed()
	 */
	public boolean isValueConcealed()
	{
		return cancealValue;
	}
	
	public void setValueConcealed(boolean valueConceal)
	{
		this.cancealValue  = valueConceal;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#allowNull()
	 */
	public boolean allowNull()
	{
		return allowNull;
	}
	
	/**
	 * @param allowNull whether allow null value for the specific parameter
	 */
	public void setAllowNull(boolean allowNull)
	{
		this.allowNull = allowNull;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#allowBlank()
	 */
	public boolean allowBlank()
	{
		return allowBlank;
	}
	/**
	 * 
	 * @param allowBlank
	 */
	public void setAllowBlank(boolean allowBlank)
	{
		this.allowBlank = allowBlank;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getFormat()
	 */
	public String getDisplayFormat()
	{
		return displayFormat;
	}
	
	/**
	 * @param format
	 */
	public void setFormat(String format)
	{
		this.displayFormat = format;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getControlType()
	 */
	public int getControlType()
	{
		return controlType;
	}
	
	/**
	 * @param controlType
	 */
	public void setControlType(int controlType)
	{
		this.controlType = controlType;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getAlignment()
	 */
	public int getAlignment()
	{
		return alignment;
	}
	
	/**
	 * @param align
	 */
	public void setAlignment(int align)
	{
		this.alignment = align;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getSelectionList()
	 */
	public ArrayList getSelectionList()
	{
		return selectionList;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getSelectionListType()
	 */
	public int getSelectionListType()
	{
		return selectionListType;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IParameterDefn#displayInFixedOrder()
	 */
	public boolean displayInFixedOrder()
	{
		return fixedOrder;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IParameterDefnBase#getParameterType()
	 */
	public int getParameterType()
	{
		return parameterType;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IParameterDefnBase#getUserPropertyValues()
	 */
	public Map getUserPropertyValues()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IParameterDefnBase#getUserPropertyValue(java.lang.String)
	 */
	public String getUserPropertyValue(String name)
	{
		return null;
	}

	/**
	 * @param fixedOrder The fixedOrder to set.
	 */
	public void setFixedOrder(boolean fixedOrder)
	{
		this.fixedOrder = fixedOrder;
	}
	/**
	 * @param parameterType The parameterType to set.
	 */
	public void setParameterType(int parameterType)
	{
		this.parameterType = parameterType;
	}
	/**
	 * @param selectionListType The selectionListType to set.
	 */
	public void setSelectionListType(int selectionListType)
	{
		this.selectionListType = selectionListType;
	}

	/**
	 * @param paramSelectionList The paramSelectionList to set.
	 */
	public void setSelectionList(ArrayList paramSelectionList)
	{
		this.selectionList = paramSelectionList;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException
	{
		Object newObj = super.clone();
		ScalarParameterDefn para = (ScalarParameterDefn)newObj;
		ArrayList list = para.getSelectionList();
		if(list==null)
			return para;
		ArrayList newList = new ArrayList();
		for(int i=0; i<list.size(); i++)
		{
			ParameterSelectionChoice select = (ParameterSelectionChoice)list.get(i);
			newList.add(select.clone());
		}
		para.setSelectionList(newList);
		return para;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IScalarParameterDefn#getDataType()
	 */
	public int getDataType( )
	{
		return dataType;
	}

	/**
	 * @param dataType
	 */
	public void setDataType( int dataType )
	{
		this.dataType = dataType;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IParameterDefn#allowNewValues()
	 */
	public boolean allowNewValues()
	{
		return allowNewValues;
	}
	
	/**
	 * @param allowNewValues whether value not in the selection list 
	 * is allowed for this parameter
	 */
	public void setAllowNewValues(boolean allowNewValues)
	{
		this.allowNewValues = allowNewValues;
	}
	
	/**
	 * creates the static selection list 
	 */
	public void evaluateSelectionList()
	{	
		// For now, supports static list only
		if (selectionListType == IScalarParameterDefn.SELECTION_LIST_STATIC)
		{
			boolean sortDisplayValue = true;
			for (int i = 0; i < selectionList.size(); i++)
			{
				ParameterSelectionChoice choice = (ParameterSelectionChoice)selectionList.get(i);
				choice.setLocale(locale);
				if( choice.getLabel() == null )
				{
					sortDisplayValue = false;
					break;
				}
			}
			
			//sort
			if(!fixedOrder)
				Collections.sort(selectionList, new SelectionChoiceCompartor(sortDisplayValue));
		}
	}
		
	protected class SelectionChoiceCompartor implements Comparator
	{
		protected boolean sortDisplayValue;
		
		public SelectionChoiceCompartor(boolean sortDisplayValue)
		{
			this.sortDisplayValue = sortDisplayValue;
		}
		
		public int compare(Object o1, Object o2)
		{
			if ((o1 instanceof ParameterSelectionChoice)
					&& (o2 instanceof ParameterSelectionChoice))
			{
				Object value1;
				Object value2;
				if(sortDisplayValue)
				{
					value1 = ((ParameterSelectionChoice) o1).getLabel();
					value2 = ((ParameterSelectionChoice) o2).getLabel();
				}
				else
				{
					value1 = ((ParameterSelectionChoice) o1).getValue();
					value2 = ((ParameterSelectionChoice) o2).getValue();
				}
				if (value1 != null && value2 != null)
					return ((Comparable) value1).compareTo(value2);
			}
			return -1;
		}
	}
}