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
package org.eclipse.birt.chart.factory;

import java.util.Locale;

import org.eclipse.birt.chart.model.ScriptHandler;
import org.eclipse.birt.core.i18n.ResourceHandle;

/**
 *
 */
public final class RunTimeContext
{
    /**
     * 
     */
    private transient Locale lcl = null;
    
    /**
     * 
     */
    private transient ScriptHandler sh = null;
    
    /**
     * 
     */
    private transient ResourceHandle rh = null;
    
    /**
     * Used to lookup externalized messages
     */
    private transient IMessageLookup iml = null;
    
    /**
     * 
     */
    public RunTimeContext()
    {
        
    }

    /**
     * @return
     */
    public final Locale getLocale()
    {
        return lcl;
    }
    

    /**
     * @param lcl
     */
    public final void setLocale(Locale lcl)
    {
        this.lcl = lcl;
    }
    

    /**
     * @return
     */
    public final ResourceHandle getResourceHandle()
    {
        return rh;
    }
    

    /**
     * @param rh
     */
    public final void setResourceHandle(ResourceHandle rh)
    {
        this.rh = rh;
    }
    

    /**
     * @return
     */
    public final ScriptHandler getScriptHandler()
    {
        return sh;
    }
    

    /**
     * @param sh
     */
    public final void setScriptHandler(ScriptHandler sh)
    {
        this.sh = sh;
    }

    /**
     * @param iml
     */
    public void setMessageLookup(IMessageLookup iml)
    {
        this.iml = iml;
    }
    
    /**
     * @param sChartKey
     * @return
     */
    public final String externalizedMessage(String sChartKey)
    {
        if (iml == null)
        {
            return "[ERR]" + sChartKey;
        }
        return iml.getMessageValue(sChartKey, lcl);
    }
}
