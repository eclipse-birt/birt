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

package org.eclipse.birt.doc.romdoc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.metadata.ObjectDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

public abstract class DocComposite extends DocObject {
	ObjectDefn defn;
	protected ArrayList properties = new ArrayList();
	protected String xmlSummary;

	public DocComposite(ObjectDefn d) {
		defn = d;
	}

	public ObjectDefn getDefn() {
		return defn;
	}

	public String getName() {
		return defn.getName();
	}

	public String getDisplayName() {
		return defn.getDisplayName();
	}

	public String getXmlSummary() {
		return xmlSummary;
	}

	public boolean hasProperties() {
		return !properties.isEmpty();
	}

	public List getProperties() {
		return properties;
	}

	static class PropComparator implements Comparator {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object arg0, Object arg1) {
			PropertyDefn prop1 = (PropertyDefn) arg0;
			PropertyDefn prop2 = (PropertyDefn) arg1;
			return prop1.getName().compareTo(prop2.getName());
		}
	}

	public DocProperty getProperty(String name) {
		Iterator iter = properties.iterator();
		while (iter.hasNext()) {
			DocProperty prop = (DocProperty) iter.next();
			if (prop.getName().equals(name))
				return prop;
		}
		return null;
	}

	public String getSince() {
		return defn.getSince();
	}

	public abstract boolean isElement();
}
