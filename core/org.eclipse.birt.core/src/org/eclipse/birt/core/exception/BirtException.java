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
package org.eclipse.birt.core.exception;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Define BIRT's Exception framework. Every BIRT exception has to include an error
 * code, which is a string. Different BIRT modules use different prefix for error 
 * codes. For example,  
 * 
 * <li>DE uses DESIGN_EXCEPTION_
 * <li>DtE uses DATA_EXCEPTION_
 * <li>FPE uses GENERATION_EXCEPTION_ and VIEW_EXCEPTION_
 * <li>UI uses UI_EXCEPTION_
 * <li>Chart used CHART_EXCEPTION_
 * <li>viewer uses VIERER_EXCEPTION_</li>
 * 
 * as prefix. An error code is used for retrieving error message, which is 
 * externalizable, and can be seen by end users. The error code itself allows
 * the identification of the subcomponent that generates the exception, 
 * avoiding the need to create exceltion subclasses such as BirtEngineException, 
 * BirtDtEException, etc.    
 *  
 */
public class BirtException extends Exception {

    /**
     * the error code
     */
    protected String errorCode;  
    protected ResourceBundle resourceBundle = null;
    protected String[] args = null;
    
    /**
     * Constructs a new Birt exception with no cause object.
     * @param errorCode used to retrieve a piece of externalized message displayed to end user.
     * @param resourceBundle the resourceBundle used to translate the message.    
     */
    BirtException(String errorCode, ResourceBundle bundle) {
        super();
        this.errorCode = errorCode;
        this.resourceBundle = bundle;
    }
    
    /**
     * @param errorCode used to retrieve a piece of externalized message displayed to end user.
     * @param resourceBundle the resourceBundle used to translate the message.        
     * @param cause the nested exception
      */
    BirtException(String errorCode, ResourceBundle bundle, Throwable cause)
    {
        super(cause);
        this.errorCode = errorCode;
        this.resourceBundle = bundle;
    }

    /**
     * @param errorCode used to retrieve a piece of externalized message displayed to end user.
     * @param resourceBundle the resourceBundle used to translate the message.            
     * @param args string arguments used to format error messages
      */
    BirtException(String errorCode, ResourceBundle bundle, Throwable cause, String[] args)
    {
        super(cause);
        this.errorCode = errorCode;
        this.args = args;
        this.resourceBundle = bundle;
    }

    /**
     * @param errorCode used to retrieve a piece of externalized message displayed to end user.    
     * @param resourceBundle the resourceBundle used to translate the message. 
     * @param cause the nested exception
     * @param arg0 first argument used to format error messages
     */
    BirtException(String errorCode, ResourceBundle bundle, Throwable cause, String arg0)
    {
        super(cause);
        this.errorCode = errorCode;
        this.resourceBundle = bundle;
        
        this.args = new String[1];
        this.args[0] = arg0;
    }
    
    /**
     * @param errorCode used to retrieve a piece of externalized message displayed to end user.
     * @param resourceBundle the resourceBundle used to translate the message.            
     * @param args string arguments used to format error messages
      */
    BirtException(String errorCode, ResourceBundle bundle, String[] args)
    {
        super();
        this.errorCode = errorCode;
        this.args = args;
        this.resourceBundle = bundle;
    }

    /**
     * @param errorCode used to retrieve a piece of externalized message displayed to end user.    
     * @param resourceBundle the resourceBundle used to translate the message. 
     * @param cause the nested exception
     * @param arg0 first argument used to format error messages
     */
    BirtException(String errorCode, ResourceBundle bundle, String arg0)
    {
        super();
        this.errorCode = errorCode;
        this.resourceBundle = bundle;
        
        this.args = new String[1];
        this.args[0] = arg0;
    }
    
    /**
     * @param errorCode used to retrieve a piece of externalized message displayed to end user.    
     * @param arg0 first argument used to format error messages
      */
    BirtException(String errorCode, String arg0)
    {
        super();
        
        this.errorCode = errorCode;
        this.args = new String[1];
        this.args[0] = arg0;
    }
    
    /**
     * @param errorCode used to retrieve a piece of externalized message displayed to end user.    
     * @param cause the nested exception
     * @param args string arguments used to format error messages
      */
    BirtException(String errorCode, Throwable cause, String[] args)
    {
        super(cause);
        this.errorCode = errorCode;
        this.args = args;
    }
    
    /**
     * @return Returns the errorCode.
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * @param errorCode The errorCode to set.
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
     /* (non-Javadoc)
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() {
        return getLocalizedMessage(errorCode);
    }
    
    /**
     * Returns a localized message based on an error code.
     * Overwrite this method if you do not want to pass in the resource bundle
     * 
     * @param errorCode the error code
     * @return Localized display message.
     */
    protected String getLocalizedMessage(String errorCode)
    {
        String localizedMessage;
        if (resourceBundle == null)
            return "";	// $NON-NLS-1$
        else
        {
            try
            {
                localizedMessage = resourceBundle.getString(errorCode);
            }
            catch (Exception e)
            {
                return ""; // $NON-NLS-1$
            }
        }     
        MessageFormat form = new MessageFormat(localizedMessage);
        return form.format(args);
    }
}
