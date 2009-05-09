/*
 *************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification.ParameterIdentifier;
import org.eclipse.datatools.connectivity.oda.spec.manifest.ExtensionContributor;
import org.eclipse.datatools.connectivity.oda.spec.manifest.ResultExtensionExplorer;
import org.eclipse.datatools.connectivity.oda.spec.util.QuerySpecificationHelper;

/**
 * Internal helper class to locate the appropriate ODA QuerySpecification factory and 
 * to provide general utilities to edit and access the content of a query spec.
 */
@SuppressWarnings("restriction")
public class QuerySpecHelper
{
    QuerySpecificationHelper m_specFactoryHelper;
    
    /**
     * Constructor for a specialized helper of the specified data source and data set types.
     * @param odaDataSourceId
     * @param odaDataSetId
     */
    public QuerySpecHelper( String odaDataSourceId, String odaDataSetId )
    {
        ExtensionContributor[] contributors = null;
        try
        {
            contributors = ResultExtensionExplorer.getInstance().getContributorsOfDataSet( odaDataSourceId, odaDataSetId );
        }
        catch( IllegalArgumentException ex )
        {
            // ignore and use default factory helper
        }
        catch( OdaException ex )
        {
            // ignore and use default factory helper
        }
        
        ExtensionContributor resultSetContributor = null;
        if( contributors != null && contributors.length > 0 )
            resultSetContributor = contributors[0]; // use the first one found
        m_specFactoryHelper = new QuerySpecificationHelper( resultSetContributor );
    }
    
    /**
     * Constructor for a specialized helper of the specified ODA dynamicResultSet extension.
     * @param dynamicResultSetExtnId    may be null
     */
    public QuerySpecHelper( String dynamicResultSetExtnId )
    {
        m_specFactoryHelper = new QuerySpecificationHelper( dynamicResultSetExtnId );
    }
    
    /**
     * Gets the specialized factory helper for creating query specification instances.
     * @return
     */
    public QuerySpecificationHelper getFactoryHelper()
    {
        return m_specFactoryHelper;
    }
    
    /**
     * Sets the specified input ParameterHint and corresponding value in the specified QuerySpecification.
     * @param querySpec a QuerySpecification to which the input parameter value is set 
     * @param paramHint an input QuerySpecification; 
     *                  must contain either the native parameter name and/or position
     * @param inputValue    input parameter value
     * @throws DataException    if specified ParameterHint is invalid
     */
    public static void setParameterValue( QuerySpecification querySpec, ParameterHint paramHint, Object inputValue )
        throws DataException
    {
        if( querySpec == null || paramHint == null )
            return;     // nothing to set
        
        boolean hasNativeName = PreparedStatement.hasValue( paramHint.getNativeName() );
        boolean hasParamPos = ( paramHint.getPosition() > 0 );
        if ( ! paramHint.isInputMode() || !(hasNativeName || hasParamPos) )
            throw new DataException( "ParameterHint.", new IllegalArgumentException(), paramHint ); //$NON-NLS-1$
        
        ParameterIdentifier paramIdentifier = null;
        if( hasNativeName )
        {
            paramIdentifier = hasParamPos ?
                    querySpec.new ParameterIdentifier( paramHint.getNativeName(), paramHint.getPosition() ) :
                    querySpec.new ParameterIdentifier( paramHint.getNativeName() );
        }
        else
            paramIdentifier = querySpec.new ParameterIdentifier( paramHint.getPosition() );
            
        querySpec.setParameterValue( paramIdentifier, inputValue );                    
    }
    
}
