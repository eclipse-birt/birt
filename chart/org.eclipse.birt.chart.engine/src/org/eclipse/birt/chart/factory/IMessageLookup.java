package org.eclipse.birt.chart.factory;

import java.util.Locale;

/**
 * 
 */
public interface IMessageLookup
{
    /**
     * @param sKey
     * @param lcl
     * @return
     */
    public String getMessageValue(String sKey, Locale lcl);
}
