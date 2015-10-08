package org.eclipse.birt.data.engine.api;

import java.util.Set;


/**
 * Special Oda data set, containing multiple oda data set design
 * @author xzhao
 *
 */
public interface ICombinedOdaDataSetDesign extends IOdaDataSetDesign
{
    public void addDataSetDesign( IOdaDataSetDesign dataSetDesign );
    
    public Set<IOdaDataSetDesign> getDataSetDesigns( );
}
