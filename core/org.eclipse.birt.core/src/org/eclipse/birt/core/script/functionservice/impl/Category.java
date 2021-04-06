
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.script.functionservice.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;

/**
 * This is an implementation of IScriptFunctionCategory interface.
 */

public class Category implements IScriptFunctionCategory {
	//
	private String name;
	private String desc;
	private boolean isVisible;
	private List<IScriptFunction> functions;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param desc
	 */
	public Category(String name, String desc) {
		this.name = name;
		this.desc = desc;
		this.isVisible = true;
		this.functions = new ArrayList<IScriptFunction>();
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param desc
	 */
	public Category(String name, String desc, boolean isVisible) {
		this.name = name;
		this.desc = desc;
		this.isVisible = isVisible;
		this.functions = new ArrayList<IScriptFunction>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.functionservice.INamedObject#getName()
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.core.script.functionservice.IDescribable#getDescription()
	 */
	public String getDescription() {
		return this.desc;
	}

	/**
	 * Add a function to this category.
	 * 
	 */
	public void addFunction(IScriptFunction function) {
		this.functions.add(function);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory#
	 * getFunctions()
	 */
	public IScriptFunction[] getFunctions() {
		return this.functions.toArray(new IScriptFunction[0]);
	}

	/**
	 * Returns whether the category is visible.
	 * 
	 */
	public boolean isVisible() {
		return this.isVisible;
	}
}
