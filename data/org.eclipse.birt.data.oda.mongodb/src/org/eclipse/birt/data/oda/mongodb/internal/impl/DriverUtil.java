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

package org.eclipse.birt.data.oda.mongodb.internal.impl;

import java.util.logging.Logger;

import org.eclipse.birt.data.oda.mongodb.impl.MongoDBDriver;


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

}
