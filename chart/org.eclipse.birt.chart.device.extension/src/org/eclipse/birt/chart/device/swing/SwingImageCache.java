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

package org.eclipse.birt.chart.device.swing;

import java.awt.Image;
import java.awt.MediaTracker;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import org.eclipse.birt.chart.exception.ImageLoadingException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;

/**
 *
 */
public final class SwingImageCache 
{
    /**
     *  
     */
    private final java.awt.Panel p = new java.awt.Panel(); // NEEDED FOR IMAGE

    /**
     * 
     */
    private final Hashtable htCache;
    
    /**
     *
     */
    SwingImageCache()
	{
    	htCache = new Hashtable();
	}
    
    /**
     * 
     * @param url
     * @return
     * @throws ImageLoadingException
     */
    final Image loadImage(URL url) throws ImageLoadingException
    {
    	Image img = (Image) htCache.get(url);
    	if (img != null)
    	{
    		DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Using cached SWING image from " + url);
    	}
    	else
    	{
	        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Loading SWING image from " + url);
	        img = (new ImageIcon(url)).getImage();
	        try
	        {
	            final MediaTracker tracker = new MediaTracker(p);
	            tracker.addImage(img, 0);
	            tracker.waitForAll();
	
	            if ((tracker.statusAll(true) & MediaTracker.ERRORED) != 0)
	            {
	                StringBuffer sb = new StringBuffer();
	                Object[] oa = tracker.getErrorsAny();
	                sb.append('[');
	                for (int i = 0; i < oa.length; i++)
	                {
	                    sb.append(oa[i]);
	                    if (i < oa.length - 1)
	                    {
	                        sb.append(", ");
	                    }
	                }
	                sb.append(']');
	                throw new ImageLoadingException("MediaTracker returned an error in " + sb.toString());
	            }
	        }
	        catch (InterruptedException ex )
	        {
	            throw new ImageLoadingException(ex);
	        }
	        htCache.put(url, img);
    	}
        return img;
    }
	
    /**
     *
     */
    final void flush()
    {
    	if (htCache.isEmpty())
    	{
    		return;
    	}
    	Image img;
    	final int n = htCache.size();
    	Enumeration eV = htCache.elements();
    	while (eV.hasMoreElements())
    	{
    		img = (Image) eV.nextElement();
    		img.flush();
    	}
    	htCache.clear();
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Flushed "+n+" cached SWING image(s)");
    }
    
    /**
     * 
     * @return
     */
    final Object getObserver()
    {
    	return p;
    }
}
