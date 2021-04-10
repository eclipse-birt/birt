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

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.script.internal.element.ScriptAPIBaseFactory;

/**
 * Class to create some structure instances.
 * 
 * @deprecated
 */
public class ScriptAPIFactory implements IScriptAPIFactory {/*
															 * Factory used to create the structure instance.
															 */

	private static IScriptAPIFactory baseFactory = null;

	/*
	 * The only one ScriptAPIFactory instance.
	 */
	private static ScriptAPIFactory instance = null;

	/**
	 * Returns the ODAProviderFactory instance.
	 * 
	 * @return ODAProviderFactory instance.
	 */

	public static ScriptAPIFactory getInstance() {
		if (instance == null) {
			synchronized (ScriptAPIFactory.class) {
				if (instance == null)
					instance = new ScriptAPIFactory();
			}
		}
		return instance;
	}

	public static IScriptAPIFactory getBaseFactory() {
		if (baseFactory != null) {
			return baseFactory;
		}
		synchronized (ScriptAPIFactory.class) {
			if (baseFactory == null) {
				baseFactory = new ScriptAPIBaseFactory();
			}
		}
		return baseFactory;
	}

	/**
	 * Set the base factory for this class. This method should be called before any
	 * other operation.
	 * 
	 * @param base The real factory class used to create the script structure.
	 */

	public synchronized static void initeTheFactory(IScriptAPIFactory base) {
		if (baseFactory != null)
			return;

		baseFactory = base;
	}

	/**
	 * Singleton instance release method.
	 */
	public static void releaseInstance() {
		baseFactory = null;
		instance = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IScriptAPIFactory#
	 * createDataBinding()
	 */
	public IDataBinding createDataBinding() {
		if (baseFactory != null)
			return baseFactory.createDataBinding();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IScriptAPIFactory#
	 * createFilterCondition()
	 */
	public IFilterCondition createFilterCondition() {
		if (baseFactory != null)
			return baseFactory.createFilterCondition();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IScriptAPIFactory#
	 * createHideRule()
	 */
	public IHideRule createHideRule() {
		if (baseFactory != null)
			return baseFactory.createHideRule();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IScriptAPIFactory#
	 * createHighLightRule()
	 */
	public IHighlightRule createHighLightRule() {
		if (baseFactory != null)
			return baseFactory.createHighLightRule();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IScriptAPIFactory#
	 * createSortCondition()
	 */
	public ISortCondition createSortCondition() {
		if (baseFactory != null)
			return baseFactory.createSortCondition();
		return null;
	}

}
