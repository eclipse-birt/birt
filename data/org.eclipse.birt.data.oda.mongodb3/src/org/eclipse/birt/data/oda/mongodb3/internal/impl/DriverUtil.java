/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.mongodb3.internal.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.mongodb3.impl.MongoDBDriver;
import org.eclipse.birt.data.oda.mongodb3.nls.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;


/**
 * Internal constant variables and utilities.
 */
public final class DriverUtil
{
    public static final String EMPTY_STRING = ""; //$NON-NLS-1$

    // trace logging
    private static Logger sm_logger = Logger.getLogger( MongoDBDriver.ODA_DATA_SOURCE_ID );

    public static Logger getLogger()
    {
        return sm_logger;
    }

    static Object parseJSONExpr( String jsonExpr ) throws OdaException
    {
        try
        {
            return JSON.parse( jsonExpr );
        }
        catch( JSONParseException ex )
        {
            String errMsg = Messages.bind( Messages.driverUtil_parsingError,
                    jsonExpr );
            DriverUtil.getLogger().log( Level.INFO, errMsg, ex ); // caller may choose to ignore it; log at INFO level

            OdaException newEx = new OdaException( errMsg );
            newEx.initCause( ex );
            throw newEx;
        }
    }

    static DBObject parseExprToDBObject( String jsonExpr ) throws OdaException
    {
        Object parsedObj = parseJSONExpr( jsonExpr );
        if( parsedObj instanceof DBObject )
            return (DBObject)parsedObj;            
        throw new OdaException( Messages.bind( Messages.driverUtil_invalidExpr, parsedObj.getClass().getSimpleName() ));
    }

}
