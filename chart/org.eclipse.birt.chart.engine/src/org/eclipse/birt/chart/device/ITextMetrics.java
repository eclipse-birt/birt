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

import org.eclipse.birt.chart.model.component.Label;

/**
 * This interface provides a layer of abstraction for text metrics via xserver dependent implementations
 * 
 * @author Actuate Corporation
 */
public interface ITextMetrics
{

    /**
     * Permits reuse of the text metrics instance for a new label with new attributes.
     * 
     * @param la
     */
    void reuse(Label la);

    /**
     * @return The height of a single line of text using the font defined in the contained label
     */
    double getHeight();

    /**
     * @return The descent of a single line of text using the font defined in the contained label
     */
    double getDescent();

    /**
     * @return The full height of all lines of text using the font defined in the contained label
     */
    double getFullHeight();

    /**
     * @return The max width of the widest line of text using the font defined in the contained label
     */
    double getFullWidth();

    /**
     * @return The number of lines of text associated with the label to be rendered
     */
    int getLineCount();

    /**
     * @param iIndex
     *            The line to be retrieved from multi-line text
     * @return A line of text (subset)
     */
    String getLine(int iIndex);

    /**
     * Perform a cleanup when this object is not required anymore
     */
    void dispose();
}