/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * TODO: Please document
 * 
 * @version $Revision: #2 $ $Date: 2005/02/05 $
 */
public final class Utility
{

    /**
     * 
     */
    private Utility()
    {
    }
    
    public static void setProperty(Object editor, String propertyName, Object value) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        PropertyDescriptor descriptor = getPropertyDescriptor(editor, propertyName);
        if(descriptor != null)
        {
            Method method = descriptor.getWriteMethod();
            if(method != null)
            {
                method.invoke(editor, new Object[]{value});
            }
        }
    }
    
    public static Object getProperty(Object editor, String propertyName) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        Object value = null;
        PropertyDescriptor descriptor = getPropertyDescriptor(editor, propertyName);
        if(descriptor != null)
        {
            Method method = descriptor.getReadMethod();
            if(method != null)
            {
                value = method.invoke(editor, null);
            }
        }
        
        return value;
    }
    
    private static PropertyDescriptor getPropertyDescriptor(Object editor, String propertyName) throws IntrospectionException
    {
        PropertyDescriptor[] descriptors = getPropertyDescriptors(editor);
        for(int n = 0; n < descriptors.length; n++)
        {
            if(descriptors[n].getName().equals(propertyName))
            {
                return descriptors[n];
            }
        }
        return null;
    }
    
    private static PropertyDescriptor[] getPropertyDescriptors(Object editor) throws IntrospectionException
    {
        BeanInfo info = Introspector.getBeanInfo(editor.getClass());
        return info.getPropertyDescriptors();
    }
    
}
