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

import java.io.OutputStream;
import java.util.HashMap;

/**
 * Implements a default generation peer that does nothing
 */
public class ReportItemGenerationBase implements IReportItemGeneration {

    /**
     * Constructor that does nothing
     */
    public ReportItemGenerationBase() {
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IGenerationPeer#initialize(java.util.HashMap)
     */
    public void initialize(HashMap parameters) {  
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IGenerationPeer#getReportQuery()
     */
    public Object getReportQuery() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IGenerationPeer#render()
     */
    public void process() {  
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IGenerationPeer#getSize()
     */
    public Object getSize() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IGenerationPeer#serialize(java.io.OutputStream)
     */
    public final long serialize(OutputStream ostream) {
    	// WARNING: This mehod is defined as final for now because engine does 
    	// not call this method. Do not overwrite this method
    	return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.extension.IGenerationPeer#finish()
     */
    public void finish() {
    }
}
