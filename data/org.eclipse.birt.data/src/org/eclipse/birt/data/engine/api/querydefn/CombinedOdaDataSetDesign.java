package org.eclipse.birt.data.engine.api.querydefn;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.ICombinedOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;

import com.ibm.icu.util.ULocale;


public class CombinedOdaDataSetDesign extends OdaDataSetDesign implements ICombinedOdaDataSetDesign
{
    private IOdaDataSetDesign masterDesign;
    private Set<IOdaDataSetDesign> dataSetDesigns = new HashSet<IOdaDataSetDesign>( );

    public CombinedOdaDataSetDesign( IOdaDataSetDesign masterDesign )
    {
        super( masterDesign.getName( ), masterDesign.getDataSourceName( ) );
        this.masterDesign = masterDesign;
        addDataSetDesign( masterDesign );
    }
    
    
    public void addDataSetDesign( IOdaDataSetDesign dataSetDesign )
    {
        dataSetDesigns.add( dataSetDesign );
    }
    
    public Set<IOdaDataSetDesign> getDataSetDesigns( )
    {
        return dataSetDesigns;
    }

    @Override
    public String getQueryText( )
    {
        return masterDesign.getQueryText( );
    }

    @Override
    public String getExtensionID( )
    {
        return masterDesign.getExtensionID( );
    }

    @Override
    public String getPrimaryResultSetName( )
    {
        return masterDesign.getPrimaryResultSetName( );
    }

    @Override
    public Map getPublicProperties( )
    {
        return masterDesign.getPublicProperties( );
    }

    @Override
    public Map getPrivateProperties( )
    {
        return masterDesign.getPublicProperties( );
    }

    @Override
    public int getPrimaryResultSetNumber( )
    {
        return masterDesign.getPrimaryResultSetNumber( );
    }

    @Override
    public String getName( )
    {
        return masterDesign.getName( );
    }

    @Override
    public int getCacheRowCount( )
    {
        return masterDesign.getCacheRowCount( );
    }

    @Override
    public boolean needDistinctValue( )
    {
        return masterDesign.needDistinctValue( );
    }

    @Override
    public String getDataSourceName( )
    {
        return masterDesign.getDataSourceName( );
    }

    @Override
    public List getComputedColumns( )
    {
        return masterDesign.getComputedColumns( );
    }

    @Override
    public List getFilters( )
    {
        return masterDesign.getFilters( );
    }

    @Override
    public List<ISortDefinition> getSortHints( )
    {
        return masterDesign.getSortHints( );
    }

    @Override
    public List getParameters( )
    {
        return masterDesign.getParameters( );
    }

    @Override
    public List getResultSetHints( )
    {
        return masterDesign.getResultSetHints( );
    }

    @Override
    public Collection getInputParamBindings( )
    {
        return masterDesign.getInputParamBindings( );
    }

    @Override
    public String getBeforeOpenScript( )
    {
        return masterDesign.getBeforeOpenScript( );
    }

    @Override
    public String getAfterOpenScript( )
    {
        return masterDesign.getAfterOpenScript( );
    }

    @Override
    public String getOnFetchScript( )
    {
        return masterDesign.getOnFetchScript( );
    }

    @Override
    public String getBeforeCloseScript( )
    {
        return masterDesign.getBeforeCloseScript( );
    }

    @Override
    public String getAfterCloseScript( )
    {
        return masterDesign.getAfterCloseScript( );
    }

    @Override
    public IBaseDataSetEventHandler getEventHandler( )
    {
        return masterDesign.getEventHandler( );
    }

    @Override
    public void setRowFetchLimit( int max )
    {
        masterDesign.setRowFetchLimit( max );
    }

    @Override
    public int getRowFetchLimit( )
    {
        return masterDesign.getRowFetchLimit( );
    }

    @Override
    public ULocale getCompareLocale( )
    {
        return masterDesign.getCompareLocale( );
    }

    @Override
    public String getNullsOrdering( )
    {
        return masterDesign.getNullsOrdering( );
    }
}
