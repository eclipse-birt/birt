package org.eclipse.birt.chart.factory;

import java.util.Locale;

/**
 * 
 */
public interface IMessageLookup
{
    public static final char KEY_SEPARATOR = '=';
    
    /**
     * @param sKey
     * @param lcl
     * @return
     */
    public String getMessageValue(String sKey, Locale lcl);
}
