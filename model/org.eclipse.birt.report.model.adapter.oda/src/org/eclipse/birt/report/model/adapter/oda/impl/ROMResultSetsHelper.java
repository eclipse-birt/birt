package org.eclipse.birt.report.model.adapter.oda.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;

/**
 * This class is used for helping creating new column in design time.
 * 
 * Find old column:
 * 		1. Match all new column definitions with old columns using jdbc native name and jdbc alias. ( no change case )
 * 		2. Match all rest new column definitions with old columns using jdbc native name only. ( jdbc alias change case )
 * 		3. All the rest new column definitions will be regarded as newly added case.
 * 		4. All the rest old column will be regarded as deleted case.
 * Find old column hint:
 * 		Use matched old column name to find old column hint.
 * Generate new column:
 * 		1. For all no change case, keep the old name, because there may be some bindings in the report using the old name.
 * 		2. After processing no change cases, assign new name to the rest new columns.
 * Generate new name:
 * 		Use jdbc alias as name, if there is any duplicate, add "_1", "_2", etc.
 */

public class ROMResultSetsHelper {
	private List<OdaResultSetColumnHandle> oldColumns;
	private List<ColumnHintHandle> oldColumnHints;
	private List<ColumnDefinition> newColumnDefns;
	private String dataSourceId;
	private String dataSetId;
	private List<ROMResultColumnHelper> newColumnInfos;
	private List<OdaResultSetColumn> newColumns;
	private int size;
	

	public ROMResultSetsHelper(List<ColumnDefinition> oldColumnDefns,
			List<OdaResultSetColumnHandle> oldColumns,
			List<ColumnHintHandle> oldColumnHints,
			List<ColumnDefinition> newColumnDefns,
			String dataSourceId,
			String dataSetId ) {
		this.oldColumns = oldColumns;
		this.oldColumnHints = oldColumnHints;
		this.newColumnDefns = newColumnDefns;
		this.dataSourceId = dataSourceId;
		this.dataSetId = dataSetId;
		this.size = newColumnDefns.size( );
		initialize();
	}
	
	private void initialize( )
	{
		linkWithOldColumnInfos();
		generateNewColumnInfos();
	}

	/**
	 * get the name of column.
	 * 
	 * first try label, then name. for JDBC, the label is always exist, it may
	 * be null sometimes for some other data sources.
	 * 
	 */
	private String getColumnName( ColumnDefinition colDefn )
	{
		String name = colDefn.getUsageHints( ) != null ? colDefn
				.getUsageHints( ).getLabel( ) : null;
		if ( name == null )
		{
			name = colDefn.getAttributes( ).getName( );
		}
		return name;
	}

	private void generateNewColumnInfos() {
		// TODO Auto-generated method stub
		ROMResultColumnHelper tmpColumnHelper = null;
		OdaResultSetColumnHandle oldColumn = null;
		ColumnDefinition newColumnDefn = null;
		String newDefinedLabel = null;
		String oldDefinedLabel = null;
		DataElementAttributes dataAttrs = null;
		Set<String> nameSet = new HashSet<String>();
		
		newColumns = new ArrayList<OdaResultSetColumn>( );
		// 1st round, Create new Column for those who have no change
		for ( int i = 0; i < newColumnInfos.size(); i++ )
		{
			tmpColumnHelper = newColumnInfos.get( i );
			OdaResultSetColumn newColumn = StructureFactory.createOdaResultSetColumn( );
			newColumns.add(newColumn);
			
			newColumnDefn = tmpColumnHelper.getNewColumnDefn();
			newDefinedLabel = getColumnName( newColumnDefn );
			oldColumn = tmpColumnHelper.getOldColumn();
			// newly added, wait for 2nd round
			if ( oldColumn == null )
				continue;
			
			oldDefinedLabel = oldColumn.getColumnName();
			// label changed, wait for 2nd round
			if ( !newDefinedLabel.equals( oldDefinedLabel ) )
				continue;
			
			dataAttrs = newColumnDefn.getAttributes();
			newColumn.setColumnName( oldColumn.getColumnName( ) );
			newColumn.setNativeDataType( dataAttrs.getNativeDataTypeCode() );
			newColumn.setPosition( dataAttrs.getPosition( ) );
			newColumn.setNativeName( dataAttrs.getName( ) );
			newColumn.setDataType( getROMDataType( dataSourceId, dataSetId,	newColumn, oldColumn ) );			
			nameSet.add( oldColumn.getColumnName( ) );
		}
		
		// Create new Column for 2nd round*/
		for ( int i = 0; i < newColumnInfos.size(); i++ )
		{
			tmpColumnHelper = newColumnInfos.get( i );
			oldColumn = tmpColumnHelper.getOldColumn();
			newColumnDefn = tmpColumnHelper.getNewColumnDefn();
			
			OdaResultSetColumn newColumn = newColumns.get( i );
			
			if ( newColumn.getColumnName() != null )
				continue;
			
			dataAttrs = newColumnDefn.getAttributes();
			
			String newName = getColumnName( newColumnDefn );
			newName = getUniqueName(nameSet, newName);
			newColumn.setColumnName( newName );
			newColumn.setNativeDataType( dataAttrs.getNativeDataTypeCode() );
			newColumn.setPosition( dataAttrs.getPosition( ) );
			newColumn.setNativeName( dataAttrs.getName( ) );
			newColumn.setDataType( getROMDataType( dataSourceId, dataSetId,	newColumn, oldColumn ) );
		}
	}

	private String getUniqueName(Set<String> nameSet, String newName) {
		if ( nameSet.contains( newName ) )
		{
			int i = 1;
			String tmpName = newName + "_" + i;
			while ( nameSet.contains( tmpName ) )
			{
				i++;
				tmpName = newName + "_" + i;
			}
			newName = tmpName;
		}
		nameSet.add(newName);
		
		return newName;
	}

	private void linkWithOldColumnInfos( )
	{
		this.newColumnInfos= new ArrayList<ROMResultColumnHelper>();
		ColumnDefinition newColumnDefn = null;
		OdaResultSetColumnHandle oldColumn = null;
		ColumnHintHandle oldColumnHint = null;
		ROMResultColumnHelper tmpColumnHelper = null;
		String nativeName = null;
		
		for (int i = 0; i < newColumnDefns.size( ); i++ )
		{
			newColumnDefn = newColumnDefns.get( i );
			tmpColumnHelper = new ROMResultColumnHelper( newColumnDefn );
			this.newColumnInfos.add(tmpColumnHelper);
			if ( newColumnDefn.getAttributes() == null )
			{
				tmpColumnHelper.setup( );
				continue;
			}
		}
		
		// try to find oldColumnDefn 1st round, use nativeName + asName
		String asName = null;
		for ( int i = 0; i < newColumnInfos.size(); i++ )
		{
			tmpColumnHelper = newColumnInfos.get( i );
			if ( tmpColumnHelper.isSetup() )
				continue;
			newColumnDefn = tmpColumnHelper.getNewColumnDefn( );
			// no AS name
			if ( newColumnDefn.getUsageHints() == null )
				continue;
			nativeName = newColumnDefn.getAttributes().getName();
			asName = newColumnDefn.getUsageHints().getLabel();
			oldColumn = findOldColumnByNativeNameAndAs( nativeName, asName );
			if ( oldColumn != null )
			{
				oldColumnHint = findOldColumnHint( oldColumn.getColumnName( ) );
				tmpColumnHelper.setOldColumn(oldColumn);
				tmpColumnHelper.setOldColumnHint(oldColumnHint);
				tmpColumnHelper.setup();
			}
		}
		
		// try to find oldColumnDefn 2nd round, use nativeName
		for ( int i = 0; i < newColumnInfos.size(); i++ )
		{
			tmpColumnHelper = newColumnInfos.get( i );
			if ( tmpColumnHelper.isSetup() )
				continue;
			newColumnDefn = tmpColumnHelper.getNewColumnDefn( );
			nativeName = newColumnDefn.getAttributes().getIdentifier().getName();
			oldColumn = findOldColumnByNativeName( nativeName );
			if ( oldColumn != null )
			{
				oldColumnHint = findOldColumnHint( oldColumn.getColumnName( ) );
			}
			else
			{
				oldColumn = null;
				oldColumnHint = null;
			}
			tmpColumnHelper.setOldColumn(oldColumn);
			tmpColumnHelper.setOldColumnHint(oldColumnHint);
			tmpColumnHelper.setup();
		}
	}
	
	private OdaResultSetColumnHandle findOldColumnByNativeNameAndAs(
			String nativeName, String asName) {
		OdaResultSetColumnHandle tmpColumn = null;
		for ( int i = 0; i < oldColumns.size(); i++ )
		{
			tmpColumn = oldColumns.get( i );
			if ( tmpColumn.getColumnName().equals( asName )
					&& tmpColumn.getNativeName().equals( nativeName ) )
			{
				oldColumns.remove( i );
				return tmpColumn;
			}
		}
		return null;
	}

	private OdaResultSetColumnHandle findOldColumnByNativeName(String nativeName) {
		OdaResultSetColumnHandle tmpColumn = null;
		for ( int i = 0; i < oldColumns.size(); i++ )
		{
			tmpColumn = oldColumns.get( i );
			if ( tmpColumn.getNativeName().equals( nativeName ) )
			{
				oldColumns.remove( i );
				return tmpColumn;
			}
		}
		return null;
	}

	private ColumnHintHandle findOldColumnHint(String columnName) {
		for ( int i = 0 ; i < oldColumnHints.size( ); i++ )
			if ( oldColumnHints.get(i).getColumnName().equals( columnName ) )
				return oldColumnHints.get(i);
		return null;
	}
	
	public ROMResultColumnHelper getColumnHelper( int i )
	{
		if ( i < size )
			return newColumnInfos.get( i );
		return null;
	}
	
	public OdaResultSetColumn getNewColumn( int i )
	{
		if ( i < size )
			return newColumns.get( i );
		return null;
	}
	
	/**
	 * Returns the rom data type in string.
	 * 
	 * @param dataSourceId
	 *            the id of the data source
	 * @param dataSetId
	 *            the ide of the data set
	 * @param column
	 *            the rom data set parameter
	 * @param setHandleParams
	 *            params defined in data set handle
	 * @return the rom data type in string
	 */

	private String getROMDataType( String dataSourceId, String dataSetId,
			OdaResultSetColumn newColumn,  OdaResultSetColumnHandle oldColumn )
	{

		if ( oldColumn == null )
			return AdapterUtil.convertNativeTypeToROMDataType( dataSourceId,
					dataSetId, newColumn.getNativeDataType( ).intValue( ), null );

		Integer tmpPosition = oldColumn.getPosition( );
		if ( tmpPosition == null )
			return AdapterUtil.convertNativeTypeToROMDataType( dataSourceId,
					dataSetId, newColumn.getNativeDataType( ).intValue( ), null );

		if ( !tmpPosition.equals( newColumn.getPosition( ) ) )
			return AdapterUtil.convertNativeTypeToROMDataType( dataSourceId,
					dataSetId, newColumn.getNativeDataType( ).intValue( ), null );

		Integer tmpNativeCodeType = oldColumn.getNativeDataType( );
		if ( tmpNativeCodeType == null
				|| tmpNativeCodeType.equals( newColumn.getNativeDataType( ) ) )
			return oldColumn.getDataType( );

		String oldDataType = oldColumn.getDataType( );
		return AdapterUtil
				.convertNativeTypeToROMDataType( dataSourceId, dataSetId,
						newColumn.getNativeDataType( ).intValue( ), oldDataType );
	}
}
