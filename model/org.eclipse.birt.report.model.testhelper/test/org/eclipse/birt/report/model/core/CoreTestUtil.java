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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.elements.ReportDesign;

import com.ibm.icu.util.ULocale;

/**
 * Wrapper class for all core tests to access some members and methods, which
 * are invisible to external projects.
 */

public class CoreTestUtil {

	/**
	 * Gets the intrinsic property value with the given name and element.
	 * 
	 * @param element
	 * @param propName
	 * @return the intrinsic value if set, otherwise <code>null</code>
	 */

	public static Object getIntrinsicProperty(DesignElement element, String propName) {
		if (element == null || propName == null)
			return null;
		return element.getIntrinsicProperty(propName);
	}

	/**
	 * Gets the listener list in the element.
	 * 
	 * @param element
	 * @return the listener list added in the element
	 */

	public static List getListeners(DesignElement element) {
		if (element == null)
			return null;
		return element.listeners;
	}

	/**
	 * Adds a child to the parent's derived list.
	 * 
	 * @param parent
	 * @param child
	 */

	public static void addDerived(DesignElement parent, DesignElement child) {
		if (parent == null)
			return;
		parent.addDerived(child);
	}

	/**
	 * Gets the session locale.
	 * 
	 * @param session
	 * @return the session locale
	 */

	public static ULocale getSessionLocale(DesignSession session) {
		if (session == null)
			return ULocale.getDefault();
		return session.locale;
	}

	/**
	 * Gets the session of the module.
	 * 
	 * @param module
	 * @return the session of the module
	 */

	public static DesignSession getDesignSession(Module module) {
		if (module == null)
			return null;
		return (DesignSession) module.session;
	}

	/**
	 * Gets the designs opened in the session.
	 * 
	 * @param session
	 * @return the design list in the session
	 */

	public static List getDesigns(DesignSession session) {
		if (session == null)
			return null;
		Iterator<ReportDesign> iter = session.getDesignIterator();
		List<ReportDesign> designs = new ArrayList<ReportDesign>();
		while (iter.hasNext()) {
			designs.add(iter.next());
		}
		return designs;
	}

	/**
	 * Gets the cached element definition.
	 * 
	 * @param element
	 * @return
	 */

	public static IElementDefn getCachedElementDefn(DesignElement element) {
		if (element == null)
			return null;
		return element.cachedDefn;
	}

	/**
	 * 
	 * @param designElement
	 * @param container
	 * @param i
	 */
	public static void setContainer(DesignElement designElement, DesignElement container, int i) {
		if (designElement != null)
			designElement.setContainer(container, i);

	}
}
