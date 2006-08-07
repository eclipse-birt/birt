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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;

/**
 * Represents the handle of column hint. The column hint provides the hint about
 * result set columns.
 * 
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
 * <dd>a column hint has an optional searching. It indicates how the column
 * will be used when searching.</dd>
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
 * <dd>a column hint has an optional help text. It provides optional
 * localizable descriptive text that explains the column to the end user.</dd>
 * 
 * <dt><strong>Help Text ID </strong></dt>
 * <dd>a column hint has an optional help text ID. It provides the key to
 * localize the help text.</dd>
 * </dl>
 * 
 */
public class ColumnHintHandle extends StructureHandle
{

	/**
	 * Constructs the handle of computed column.
	 * 
	 * @param valueHandle
	 *            the value handle for computed column list of one property
	 * @param index
	 *            the position of this computed column in the list
	 */

	public ColumnHintHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Returns the alias name of this column.
	 * 
	 * @return the alias name of this column
	 */

	public String getAlias( )
	{
		return getStringProperty( ColumnHint.ALIAS_MEMBER );
	}

	/**
	 * Sets the alias name of this column.
	 * 
	 * @param alias
	 *            the alias name to set
	 */

	public void setAlias( String alias )
	{
		setPropertySilently( ColumnHint.ALIAS_MEMBER, alias );
	}

	/**
	 * Returns the analysis option. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are
	 * <ul>
	 * <li>ANALYSIS_TYPE_AUTO
	 * <li>ANALYSIS_TYPE_DIMENSION
	 * <li>ANALYSIS_TYPE_MEASURE
	 * <li>ANALYSIS_TYPE_DETAIL
	 * <li>ANALYSIS_TYPE_NONE
	 * </ul>
	 * 
	 * @return the analysis option
	 */

	public String getAnalysis( )
	{
		return getStringProperty( ColumnHint.ANALYSIS_MEMBER );
	}

	/**
	 * Sets the analysis option. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are
	 * <ul>
	 * <li>ANALYSIS_TYPE_AUTO
	 * <li>ANALYSIS_TYPE_DIMENSION
	 * <li>ANALYSIS_TYPE_MEASURE
	 * <li>ANALYSIS_TYPE_DETAIL
	 * <li>ANALYSIS_TYPE_NONE
	 * </ul>
	 * 
	 * @param analysis
	 *            the analysis option to set
	 * @throws SemanticException
	 *             if the analysis is not in the choice list.
	 */

	public void setAnalysis( String analysis ) throws SemanticException
	{
		setProperty( ColumnHint.ANALYSIS_MEMBER, analysis );
	}

	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */

	public String getColumnName( )
	{
		return getStringProperty( ColumnHint.COLUMN_NAME_MEMBER );
	}

	/**
	 * Sets the column name.
	 * 
	 * @param columnName
	 *            the column name to set
	 * @throws SemanticException
	 *             value required exception.
	 * 
	 */

	public void setColumnName( String columnName ) throws SemanticException
	{
		setProperty( ColumnHint.COLUMN_NAME_MEMBER, columnName );
	}

	/**
	 * Returns the display name.
	 * 
	 * @return the display name
	 */

	public String getDisplayName( )
	{
		return getStringProperty( ColumnHint.DISPLAY_NAME_MEMBER );
	}

	/**
	 * Sets the display name.
	 * 
	 * @param displayName
	 *            the display name to set
	 */

	public void setDisplayName( String displayName )
	{
		setPropertySilently( ColumnHint.DISPLAY_NAME_MEMBER, displayName );
	}

	/**
	 * Returns the resource key for display name.
	 * 
	 * @return the resource key for display name
	 */

	public String getDisplayNameKey( )
	{
		return getStringProperty( ColumnHint.DISPLAY_NAME_ID_MEMBER );
	}

	/**
	 * Sets the resource key for display name.
	 * 
	 * @param displayNameResourceKey
	 *            the resource key to set
	 */

	public void setDisplayNameKey( String displayNameResourceKey )
	{
		setPropertySilently( ColumnHint.DISPLAY_NAME_ID_MEMBER,
				displayNameResourceKey );
	}

	/**
	 * Returns the export option. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are
	 * <ul>
	 * <li>EXPORT_TYPE_NONE
	 * <li>EXPORT_TYPE_IF_REALIZED
	 * <li>EXPORT_TYPE_ALWAYS
	 * </ul>
	 * 
	 * @return the export option
	 */

	public String getExport( )
	{
		return getStringProperty( ColumnHint.EXPORT_MEMBER );
	}

	/**
	 * Sets the export option. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are
	 * <ul>
	 * <li>EXPORT_TYPE_NONE
	 * <li>EXPORT_TYPE_IF_REALIZED
	 * <li>EXPORT_TYPE_ALWAYS
	 * </ul>
	 * 
	 * @param export
	 *            the export option to set
	 * @throws SemanticException
	 *             if the export is not in the choice list.
	 */

	public void setExport( String export ) throws SemanticException
	{
		setProperty( ColumnHint.EXPORT_MEMBER, export );
	}

	/**
	 * Returns the format option.
	 * 
	 * @return the format option
	 */

	public String getFormat( )
	{
		return getStringProperty( ColumnHint.FORMAT_MEMBER );
	}

	/**
	 * Sets the format option.
	 * 
	 * @param format
	 *            the format option to set
	 */

	public void setFormat( String format )
	{
		setPropertySilently( ColumnHint.FORMAT_MEMBER, format );
	}

	/**
	 * Returns the help text.
	 * 
	 * @return the help text.
	 */

	public String getHelpText( )
	{
		return getStringProperty( ColumnHint.HELP_TEXT_MEMBER );
	}

	/**
	 * Sets the help text.
	 * 
	 * @param helpText
	 *            the help text to set
	 */

	public void setHelpText( String helpText )
	{
		setPropertySilently( ColumnHint.HELP_TEXT_MEMBER, helpText );
	}

	/**
	 * Returns the resource key for help text.
	 * 
	 * @return the resource key for help text
	 */

	public String getHelpTextKey( )
	{
		return getStringProperty( ColumnHint.HELP_TEXT_ID_MEMBER );
	}

	/**
	 * Sets the resource key for help text.
	 * 
	 * @param helpTextResourceKey
	 *            the resource key to set
	 */

	public void setHelpTextKey( String helpTextResourceKey )
	{
		setPropertySilently( ColumnHint.HELP_TEXT_ID_MEMBER,
				helpTextResourceKey );
	}

	/**
	 * Returns the parent level.
	 * 
	 * @return the parent level
	 */

	public String getParentLevel( )
	{
		return getStringProperty( ColumnHint.PARENT_LEVEL_MEMBER );
	}

	/**
	 * Sets the parent level.
	 * 
	 * @param parentLevel
	 *            the parent level to set
	 */

	public void setParentLevel( String parentLevel )
	{
		setPropertySilently( ColumnHint.PARENT_LEVEL_MEMBER, parentLevel );
	}

	/**
	 * Returns the searching option. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are
	 * <ul>
	 * <li>SEARCH_TYPE_ANY
	 * <li>SEARCH_TYPE_INDEXED
	 * <li>SEARCH_TYPE_NONE
	 * </ul>
	 * 
	 * @return the searching option
	 */

	public String getSearching( )
	{
		return getStringProperty( ColumnHint.SEARCHING_MEMBER );
	}

	/**
	 * Sets the searching option. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are
	 * <ul>
	 * <li>SEARCH_TYPE_ANY
	 * <li>SEARCH_TYPE_INDEXED
	 * <li>SEARCH_TYPE_NONE
	 * </ul>
	 * 
	 * @param searching
	 *            the searching option to set
	 * @throws SemanticException
	 *             if the searching is not in the choice list.
	 */

	public void setSearching( String searching ) throws SemanticException
	{
		setProperty( ColumnHint.SEARCHING_MEMBER, searching );
	}

}