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

package org.eclipse.birt.core.script;
import java.util.Map;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

/**
 * 
 * @version $Revision: #1 $ $Date: 2005/01/25 $
 */
class NativeJavaMap extends NativeJavaObject 
{

    public NativeJavaMap() { }

    public NativeJavaMap(Scriptable scope, Object javaObject,
                            Class staticType)
    {
    	super(scope, javaObject, staticType);
    }

    public boolean has(String name, Scriptable start) {
    	
    	return ((Map)javaObject).containsKey(name);
    }

    public Object get(String name, Scriptable start) {
    	return ((Map)javaObject).get(name);
    }

    public void put(String name, Scriptable start, Object value) {
    	((Map)javaObject).put(name, value);
    }

    public void delete(String name) {
    	((Map)javaObject).remove(name);
    }

}
