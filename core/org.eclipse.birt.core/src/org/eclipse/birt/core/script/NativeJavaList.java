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
import java.util.List;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

/**
 * 
 * @version $Revision: #1 $ $Date: 2005/01/25 $
 */
public class NativeJavaList extends NativeJavaObject
{

    public NativeJavaList() { }

    public NativeJavaList(Scriptable scope, Object javaObject,
                            Class staticType)
    {
    	super(scope, javaObject, staticType);
    }

    public boolean has(int index, Scriptable start) {
    	return index >= 0 && index < ((List)javaObject).size();
    }

    public Object get(int index, Scriptable start) {
    	return ((List)javaObject).get(index);
    }

    public void put(int index, Scriptable start, Object value) {
    	((List)javaObject).add(index, value);
    }
}
