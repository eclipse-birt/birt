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

package org.eclipse.birt.chart.device;

import java.net.URL;

import org.eclipse.birt.chart.exception.ImageLoadingException;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.component.Label;

/**
 *  
 */
public interface IDisplayServer
{

    /**
     * 
     * @param fd
     * @return
     */
    Object createFont(FontDefinition fd);

    /**
     * 
     * @param cd
     * @return
     */
    Object getColor(ColorDefinition cd);

    /**
     * 
     * @return
     */
    int getDpiResolution();

    /**
     * 
     * @param url
     * @return
     * @throws ImageLoadingException
     */
    Object loadImage(URL url) throws ImageLoadingException;

    /**
     * 
     * @param oImage
     * @return
     */
    Size getSize(Object oImage);

    /**
     * 
     * @return
     */
    Object getObserver();

    /**
     * 
     * @param la
     * @return
     */
    ITextMetrics getTextMetrics(Label la);
}