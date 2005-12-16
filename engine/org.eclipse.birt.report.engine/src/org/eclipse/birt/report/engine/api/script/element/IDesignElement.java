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
package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;

public interface IDesignElement
{
	IScriptStyleDesign getStyle( );

	/**
	 * Returns the name of this element. Returns <code>null</code> if the
	 * element does not have a name. Many elements do not require a name. The
	 * name does not inherit. If this element does not have a name, it will not
	 * inherit the name of its parent element.
	 * 
	 * @return the element name, or null if the name is not set
	 */

	String getName( );

	/**
	 * Gets the name of this element. The returned element name will be the same
	 * as <CODE>getName()</CODE>, plus the namespace of the module that the
	 * elment is contained, if any. If the element is existed in the current
	 * module,this method and <CODE>getName()</CODE> will return identical
	 * results.
	 * 
	 * @return the qualified name of thie element.
	 */

	String getQualifiedName( );

	/**
	 * Sets the name of this element. If the name is <code>null</code>, then
	 * the name is cleared if this element does not require a name.
	 * 
	 * @param name
	 *            the new name
	 * @throws ScriptException
	 *             if the name is duplicate, or if the name is <code>null</code>
	 *             and this element requires a name.
	 */

	void setName( String name ) throws ScriptException;

	String getNamedExpression( String name );

	void setNamedExpression( String name, String exp ) throws ScriptException;

	Object getUserProperty( String name );

	void setUserProperty( String name, Object value ) throws ScriptException;

	/**
	 * Return the parent of this element
	 * 
	 * @return the parent
	 */
	IDesignElement getParent( );
}
