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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.metadata.PropertyValueException;

/**
 * This class represents a scalar (single-value) parameter. Scalar parameters
 * can have selection lists. If the user enters no value for a parameter, then
 * the default value is used. If there is no default value, then BIRT checks
 * if nulls are allowed. If so, the value of the parameter is null. If nulls are
 * not allowed, then the user must enter a value.
 * 
 * 
 */

public class ScalarParameter extends Parameter
{

	/**
	 * Name of the default value property.
	 */

	public static final String DEFAULT_VALUE_PROP = "defaultValue"; //$NON-NLS-1$ 

	/**
	 * Name of the data type property.
	 */

	public static final String DATA_TYPE_PROP = "dataType"; //$NON-NLS-1$

	/**
	 * Name of the conceal-value property.
	 */

	public static final String CONCEAL_VALUE_PROP = "concealValue"; //$NON-NLS-1$

	/**
	 * Name of the allow-null property.
	 */

	public static final String ALLOW_NULL_PROP = "allowNull"; //$NON-NLS-1$

	/**
	 * Name of the allow-blank property.
	 */

	public static final String ALLOW_BLANK_PROP = "allowBlank"; //$NON-NLS-1$

	/**
	 * Name of the format property.
	 */

	public static final String FORMAT_PROP = "format"; //$NON-NLS-1$

	/**
	 * Name of the control type property.
	 */

	public static final String CONTROL_TYPE_PROP = "controlType"; //$NON-NLS-1$ 

	/**
	 * Name of the alignment property.
	 */

	public static final String ALIGNMENT_PROP = "alignment"; //$NON-NLS-1$ 

	/**
	 * Name of the DataSet property for a dynamic list.
	 */

	public static final String DATASET_NAME_PROP = "dataSetName"; //$NON-NLS-1$ 

	/**
	 * Name of the value expression property for a dynamic list.
	 */

	public static final String VALUE_EXPR_PROP = "valueExpr"; //$NON-NLS-1$ 

	/**
	 * Name of the label expression property for a dynamic list.
	 */

	public static final String LABEL_EXPR_PROP = "labelExpr"; //$NON-NLS-1$ 

	/**
	 * Name of the muchMatch property for a selection list.
	 */

	public static final String MUCH_MATCH_PROP = "mustMatch"; //$NON-NLS-1$ 

	/**
	 * Name of the fixedOrder property for a selection list.
	 */

	public static final String FIXED_ORDER_PROP = "fixedOrder"; //$NON-NLS-1$ 

	/**
	 * Name of the choice property for a selection list.
	 */

	public static final String SELECTION_LIST_PROP = "selectionList"; //$NON-NLS-1$ 

	/**
	 * The default constructor.
	 */

	public ScalarParameter( )
	{
	}

	/**
	 * Constructs the scalar parameter with a required and unique name.
	 * 
	 * @param theName
	 *            the required name
	 */

	public ScalarParameter( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitScalarParameter( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.SCALAR_PARAMETER_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( design );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design
	 * @return an API handle for this element
	 */

	public ScalarParameterHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new ScalarParameterHandle( design, this );
		}
		return (ScalarParameterHandle) handle;
	}

	/**
	 * Performs semantic check for the scalar parameter. That is, if the dynamic
	 * list tag exists and this tag has attributes for value or label column, it
	 * must have a property for the data set name.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );
		
		if ( ( getLocalProperty( design, ScalarParameter.LABEL_EXPR_PROP ) != null )
				|| ( getLocalProperty( design,
						ScalarParameter.VALUE_EXPR_PROP ) != null ) )
		{
			if ( getLocalProperty( design, ScalarParameter.DATASET_NAME_PROP ) == null )
				list.add( new PropertyValueException( this,
						ScalarParameter.DATASET_NAME_PROP,
						null, PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );

		}

		return list;
	}
}
