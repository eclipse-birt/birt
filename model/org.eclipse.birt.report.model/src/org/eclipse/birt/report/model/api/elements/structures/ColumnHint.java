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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.List;

import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertyStructure;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * This class represents one column hint used by data sets.
 * <p>
 * This is a managed object, meaning that all changes should be made though the
 * command layer so that they can be undone and redone. Each column hint has the
 * following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Column Name </strong></dt>
 * <dd>a column hint has a required name. It keys the column hint to a column
 * within the result set.</dd>
 * 
 * <dt><strong>Alias </strong></dt>
 * <dd>a column hint has an optional alias. It provides an ¡°alias¡± name used
 * for the column within the report.</dd>
 * 
 * <dt><strong>Searching </strong></dt>
 * <dd>a column hint has an optional searching. It indicates how the column will
 * be used when searching.</dd>
 * 
 * <dt><strong>Export </strong></dt>
 * <dd>a column hint has an optional export. It determines how the column will
 * be used when exporting data.</dd>
 * 
 * <dt><strong>Analysis </strong></dt>
 * <dd>a column hint has an optional analysis. It determines how the column is
 * used when exporting the data to an OLAP cube.</dd>
 * 
 * <dt><strong>Parent Level </strong></dt>
 * <dd>a column hint has an optional parent level. It is used when a column¡¯s
 * <code>ANALYSIS_MEMBER</code> property is set to
 * <code>ANALYSIS_TYPE_DIMENSION</code> or <code>ANALYSIS_TYPE_DETAIL</code>.
 * For <code>ANALYSIS_TYPE_DIMENSION</code>, this property establishes the
 * dimension hierarchy.</dd>
 * 
 * <dt><strong>Format </strong></dt>
 * <dd>a column hint has an optional format. It is used to format the column
 * data when displaying the value in the viewing UI, especially within the
 * search results.</dd>
 * 
 * <dt><strong>Display Name </strong></dt>
 * <dd>a column hint has an optional display name. It provides the an optional
 * localizable display name for the column.</dd>
 * 
 * <dt><strong>Display Name ID </strong></dt>
 * <dd>a column hint has an optional display name ID. It provides the key to
 * localize the display name.</dd>
 * 
 * <dt><strong>Help Text </strong></dt>
 * <dd>a column hint has an optional help text. It provides optional localizable
 * descriptive text that explains the column to the end user.</dd>
 * 
 * <dt><strong>Help Text ID </strong></dt>
 * <dd>a column hint has an optional help text ID. It provides the key to
 * localize the help text.</dd>
 * </dl>
 * 
 */

public class ColumnHint extends PropertyStructure
{

	/**
	 * Name of this structure. Matches the definition in the meta-data
	 * dictionary.
	 */

	public static final String COLUMN_HINT_STRUCT = "ColumnHint"; //$NON-NLS-1$

	/**
	 * Name of the column name member. This member keys the column hint to a
	 * column within the result set.
	 */

	public static final String COLUMN_NAME_MEMBER = "columnName"; //$NON-NLS-1$

	/**
	 * Name of the alias member. This member provides an ¡°alias¡± name used for
	 * the column within the report.
	 */

	public static final String ALIAS_MEMBER = "alias"; //$NON-NLS-1$

	/**
	 * Name of the searching member. This member indicates how the column will
	 * be used when searching.
	 */

	public static final String SEARCHING_MEMBER = "searching"; //$NON-NLS-1$

	/**
	 * Name of the export member. This member determines how the column will be
	 * used when exporting data.
	 */

	public static final String EXPORT_MEMBER = "export"; //$NON-NLS-1$

	/**
	 * Name of the analysis member. This member determines how the column is
	 * used when exporting the data to an OLAP cube. OLAP cubes require that the
	 * columns be structured into dimension hierarchies. Such hierarchies are
	 * formed though a sequence of dimensions. The intersection of hierarchies
	 * have one or more numeric measures. Columns that are neither dimensions or
	 * measures can be details that associated with a dimension. Whether any
	 * given column is actually available for use with analysis depends on the
	 * <code>EXPORT_MEMBER</code> property above.
	 */

	public static final String ANALYSIS_MEMBER = "analysis"; //$NON-NLS-1$

	/**
	 * Name of the parent level member. This member is used when a column's
	 * <code>ANALYSIS_MEMBER</code> property is set to
	 * <code>ANALYSIS_TYPE_DIMENSION</code> or <code>ANALYSIS_TYPE_DETAIL</code>
	 * . For <code>ANALYSIS_TYPE_DIMENSION</code>, this property establishes the
	 * dimension hierarchy. Lower (more detailed) dimensions identify their
	 * parent (more general) dimensions. For <code>ANALYSIS_TYPE_DETAIL</code>,
	 * this property identifies the dimension for which this column is a detail.
	 */

	public static final String PARENT_LEVEL_MEMBER = "parentLevel"; //$NON-NLS-1$

	/**
	 * Name of the format member. This member is used to format the column data
	 * when displaying the value in the viewing UI, especially within the search
	 * results.
	 */

	public static final String FORMAT_MEMBER = "format"; //$NON-NLS-1$

	/**
	 * Name of the display name ID member. This member provides the resource key
	 * for display name.
	 */

	public static final String DISPLAY_NAME_ID_MEMBER = "displayNameID"; //$NON-NLS-1$

	/**
	 * Name of the display name member. This member provides the an optional
	 * localizable display name for the column.
	 */

	public static final String DISPLAY_NAME_MEMBER = "displayName"; //$NON-NLS-1$

	/**
	 * Name of the help text ID member. This member provides the resource key
	 * for help text.
	 */

	public static final String HELP_TEXT_ID_MEMBER = "helpTextID"; //$NON-NLS-1$

	/**
	 * Name of the help text member. This member provides Optional localizable
	 * descriptive text that explains the column to the end user.
	 */

	public static final String HELP_TEXT_MEMBER = "helpText"; //$NON-NLS-1$

	/**
	 * Name of the on column layout member. This member is a hint on how the
	 * dimension data element should be layout on column or row.
	 */

	public static final String ON_COLUMN_LAYOUT_MEMBER = "onColumnLayout"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return COLUMN_HINT_STRUCT;
	}

	/**
	 * Validates this structure. The following are the rules:
	 * <ul>
	 * <li>The column name is required.
	 * </ul>
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(Module,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate( Module module, DesignElement element )
	{
		List list = super.validate( module, element );

		PropertyDefn propDefn = (PropertyDefn) getDefn( ).getMember(
				COLUMN_NAME_MEMBER );
		String columnName = (String) getProperty( module, propDefn );
		if ( StringUtil.isBlank( columnName ) )
		{
			list.add( new PropertyValueException( element, propDefn,
					columnName,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new ColumnHintHandle( valueHandle, index );
	}
}