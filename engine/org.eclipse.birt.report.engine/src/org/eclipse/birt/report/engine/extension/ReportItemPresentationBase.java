/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.extension;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Implements a default presentation peer that does nothing
 */
public class ReportItemPresentationBase implements IReportItemPresentation {
    
    /**
     * Constructor that does nothing
     */
    public ReportItemPresentationBase() {
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IPresentationPeer#initialize(java.util.HashMap)
     */
    public void initialize(HashMap parameters) {
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IPresentationPeer#restore(java.io.InputStream)
     */
    public final void restore(InputStream instream) {
    	// WARNING: This mehod is defined as final for now because engine does 
    	// not call this method. Do not overwrite this method
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IPresentationPeer#getOutputType(java.lang.String, int[], java.lang.String)
     */
    public int getOutputType(String format, String mimeType) {
        return OUTPUT_NONE;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IPresentationPeer#getSize()
     */
    public Object getSize() {
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IPresentationPeer#render()
     */
    public Object process() {
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IPresentationPeer#finish()
     */
    public void finish() {
    }
}
