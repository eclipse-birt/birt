/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.exception;

import java.util.ResourceBundle;

import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.core.exception.BirtException;

/**
 * 
 */
public class ChartException extends BirtException
{
    
    
    /**
     * 
     * @param cause
     */
    public ChartException(Throwable cause)
    {
        super(getResourceKey(cause), getArguments(cause),getResourceBundle(cause), cause);
        logThis();
    }
    

    private static String getResourceKey( Throwable cause )
    {
        if ( cause instanceof ChartException )
        {
            return ((ChartException)cause).sResourceKey;
        }
        else
        {
            return cause.getLocalizedMessage();
        }
    }
    private static ResourceBundle getResourceBundle( Throwable cause )
    {
        if ( cause instanceof ChartException )
        {
            return ((ChartException)cause).rb;
        }
        else
        {
            return null;
        }
    }
    private static Object[] getArguments( Throwable cause )
    {
        if ( cause instanceof ChartException )
        {
            return ((ChartException)cause).oaMessageArguments;
        }
        else
        {
            return null;
        }
    }
    /**
     * 
     * @param sResourceKey
     * @param rb
     */
    public ChartException(String sResourceKey, ResourceBundle rb)
    {
        super(sResourceKey, rb);
        logThis();
    }

    /**
     * 
     * @param sResourceKey
     * @param oaArgs
     * @param rb
     */
    public ChartException(String sResourceKey, Object[] oaArgs, ResourceBundle rb)
    {
        super(sResourceKey, rb);
        logThis();
    }
    
    /**
     * 
     * @param sResourceKey
     * @param rb
     * @param thCause
     */
    public ChartException(String sResourceKey, ResourceBundle rb, Throwable thCause)
    {
        super(sResourceKey, rb);
        logThis();
    }

    /**
     * 
     * @param sResourceKey
     * @param oaArgs
     * @param rb
     * @param thCause
     */
    public ChartException(String sResourceKey, Object[] oaArgs, ResourceBundle rb, Throwable thCause)
    {
        super(sResourceKey, oaArgs, rb, thCause);
        logThis();
    }
    
    /**
     *  
     */
    private final void logThis()
    {
        ILogger il = DefaultLoggerImpl.instance();
        il.log(this);
    }
}
