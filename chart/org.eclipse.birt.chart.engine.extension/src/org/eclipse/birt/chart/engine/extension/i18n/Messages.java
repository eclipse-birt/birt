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
package org.eclipse.birt.chart.engine.extension.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 */
public final class Messages
{
    public static final String ENGINE_EXTENSION = "org.eclipse.birt.chart.engine.extension.i18n.messages";
    
    private Messages()
    {
    }

    /**
     * @param key
     * @param lcl
     * @return
     */
    public static String getString(String key, Locale lcl)
    {
        final ResourceBundle rb = ResourceBundle.getBundle(ENGINE_EXTENSION, lcl);
        try
        {
            return rb.getString(key);
        }
        catch (MissingResourceException e )
        {
            return '!' + key + '!';
        }
    }
    
    /**
     * @param key
     * @param oa
     * @param lcl
     * @return
     */
    public static String getString(String key, Object[] oa, Locale lcl)
    {
        final ResourceBundle rb = ResourceBundle.getBundle(ENGINE_EXTENSION, lcl);
        try
        {
            return MessageFormat.format(rb.getString(key), oa);
        }
        catch (MissingResourceException e )
        {
            return '!' + key + '!';
        }
    }
}
