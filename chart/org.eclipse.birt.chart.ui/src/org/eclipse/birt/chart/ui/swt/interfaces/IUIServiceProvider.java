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

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.Chart;

/**
 * @author Actuate Corporation
 *  
 */
public interface IUIServiceProvider
{
    /**
     * Constant indicating a fatal error in the model
     */
    public static final int FATAL_ERROR = -10;

    /**
     * Constant indicating a major (though not fatal) problem in the model
     */
    public static final int CRITICAL_ERROR = -5;

    /**
     * Constant indicating a minor error in the model
     */
    public static final int MINOR_ERROR = -1;

    /**
     * Constant indicating no detectable problems exist in the model
     */
    public static final int NO_PROBLEMS = 0;

    /**
     * Constant indicating possible problems detected in the model
     */
    public static final int POTENTIAL_PROBLEMS = 1;

    /**
     * This method will be used by the Chart Builder UI to invoke the expression builder with any previously defined
     * expression. The parameter may be null if a new expression is to be built.
     * 
     * @param sExpression
     *            the expression to be displayed in the builder (after re-entry)
     * @param Context
     *            the application-specific context used by the Expression Builder for each invocation
     * 
     * @return The final expression string built by the user in the expression builder
     */
    public String invoke(String sExpression, Object Context);

    /**
     * This method will be used by the Chart Builder UI to validate the model and show any error messages before the
     * user leaves the dialog.
     * 
     * @param chartModel
     *            the model to be validated
     * @return a constant value that will be used by the UI to determine how to proceed
     */
    public int validate(Chart chartModel);

    /**
     * This method will be called by the Chart Builder only after a 'validate' call is made. It returns an array of
     * user-friendly messages indicating any potential problems or errors in the chart model that was last validated.
     * 
     * @return an array of user-friendly messages indicating problems with the model
     */
    public String[] getMessages();
}