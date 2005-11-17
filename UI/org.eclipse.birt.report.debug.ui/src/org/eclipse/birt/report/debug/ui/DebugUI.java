// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DebugUI.java

package org.eclipse.birt.report.debug.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class DebugUI extends AbstractUIPlugin
{

    public DebugUI()
    {
        plugin = this;
        try
        {
            resourceBundle = ResourceBundle.getBundle("org.eclipse.bird.report.debug.ui.DebugUIResources");
        }
        catch(MissingResourceException _ex)
        {
            resourceBundle = null;
        }
    }

    public DebugUI(IPluginDescriptor descriptor)
    {
        plugin = this;
        try
        {
            resourceBundle = ResourceBundle.getBundle("org.eclipse.bird.report.debug.ui.DebugUIResources");
        }
        catch(MissingResourceException _ex)
        {
            resourceBundle = null;
        }
    }

    public void start(BundleContext context)
        throws Exception
    {
        super.start(context);
    }

    public void stop(BundleContext context)
        throws Exception
    {
        super.stop(context);
    }

    public static DebugUI getDefault()
    {
        return plugin;
    }

    public static String getResourceString(String key)
    {
        ResourceBundle bundle = getDefault().getResourceBundle();
        try
        {
            return bundle == null ? key : bundle.getString(key);
        }
        catch(MissingResourceException _ex)
        {
            return key;
        }
    }

    public ResourceBundle getResourceBundle()
    {
        return resourceBundle;
    }

    private static DebugUI plugin;
    private ResourceBundle resourceBundle;
}
