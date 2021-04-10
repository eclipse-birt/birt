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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.metadata.IObjectDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.ModelUtil;

import com.ibm.icu.util.ULocale;

/**
 * Base class for attributes common to elements and structures. This base class
 * allows code to work generically with these two kinds of objects.
 * 
 */

public class ObjectDefn implements IObjectDefn {

	protected IMessages messages;
	/**
	 * The internal name for the object. This is separate from the display name
	 * shown to users. It is usually the same as the element used to describe the
	 * item in XML.
	 */

	protected String name = null;

	/**
	 * The message catalog ID to use to look up the display name for this object.
	 */

	protected String displayNameKey = null;

	/**
	 * Property definitions.
	 */

	protected Map<String, IPropertyDefn> properties = new LinkedHashMap<String, IPropertyDefn>();

	/**
	 * The BIRT release when this object was introduced.
	 */

	protected String since;

	/**
	 * Default constructor.
	 */

	public ObjectDefn() {
		since = "none"; //$NON-NLS-1$
	}

	/**
	 * Constructs the definition given its name.
	 * 
	 * @param theName the internal name
	 */

	public ObjectDefn(String theName) {
		name = theName;
	}

	/**
	 * Sets the display ID while creating the element type.
	 * 
	 * @param id The display name ID to set.
	 */

	public void setDisplayNameKey(String id) {
		displayNameKey = id;
	}

	/**
	 * Gets the resource key for the display name.
	 * 
	 * @return The display name resource key.
	 */

	public Object getDisplayNameKey() {
		return displayNameKey;
	}

	/**
	 * Gets the display name.
	 * 
	 * @return Returns the display name.
	 */

	public String getDisplayName() {
		if (displayNameKey != null) {
			String displayName = null;
			if (messages == null) {
				displayName = ModelMessages.getMessage(this.displayNameKey);
			} else {
				ULocale locale = ThreadResources.getLocale();
				displayName = messages.getMessage(displayNameKey, locale);
			}
			if (displayName != null) {
				return displayName;
			}
		}
		return name;
	}

	/**
	 * Gets the internal name for the element.
	 * 
	 * @return Returns the name.
	 */

	public String getName() {
		return name;
	}

	/**
	 * Sets the internal name for this element definition. Must be done once, while
	 * building the dictionary.
	 * 
	 * @param theName The name to set.
	 */

	public void setName(String theName) {
		name = theName;
	}

	/**
	 * Adds a property definition. Properties are keyed by their internal name. The
	 * name must be non-empty, and each property must be unique. If these invariants
	 * do not hold, then this is a bad build.
	 * 
	 * @param property The system property to add.
	 * @throws MetaDataException if property name duplicates within the object
	 *                           definition.
	 */

	public void addProperty(PropertyDefn property) throws MetaDataException {
		if (property == null)
			return;
		String name = property.getName();
		assert name != null && name.trim().length() != 0;
		if (properties.containsKey(name))
			throw new MetaDataException(new String[] { name, this.name },
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_PROPERTY);
		properties.put(name, property);
		property.setOwner(this);
	}

	/**
	 * Gets a property definition given the property name.
	 * 
	 * @param propName the name of the property to get
	 * @return the property with that name, or null if the property cannot be found
	 */

	public IPropertyDefn findProperty(String propName) {
		assert propName != null;
		return properties.get(propName);
	}

	/**
	 * Returns an iterator over the property definitions. The
	 * <code>PropertyDefn</code> s in the iterator will be sorted by there localized
	 * names.
	 * 
	 * @return an iterator over the property definitions.
	 */

	public Iterator<IPropertyDefn> getPropertyIterator() {
		List<IPropertyDefn> propDefns = new ArrayList<IPropertyDefn>(properties.values());

		return ModelUtil.sortPropertiesByLocalizedName(propDefns).iterator();

	}

	/**
	 * Returns an iterator over the property definitions. The
	 * <code>PropertyDefn</code> s in the iterator are not sorted.
	 * 
	 * @return an iterator over the property definitions.
	 */

	public Iterator<IPropertyDefn> propertiesIterator() {
		return new ArrayList<IPropertyDefn>(properties.values()).iterator();
	}

	/**
	 * Builds information for this definition itself. Called during the build step.
	 * 
	 * @throws MetaDataException if the definition is invalid
	 */

	protected void buildDefn() throws MetaDataException {
	}

	/**
	 * Set the release in which this object was introduced.
	 * 
	 * @param value the release value
	 */

	public void setSince(String value) {
		if (!StringUtil.isBlank(value))
			since = value;
	}

	/**
	 * @return the release in which this object was introduced. A value of "none"
	 *         means that the feature is experimental and is not yet released.
	 */

	public String getSince() {
		return since;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString() {
		if (!StringUtil.isBlank(getName()))
			return getName();
		return super.toString();
	}

	public void setMessages(IMessages messages) {
		this.messages = messages;
	}

	public IMessages getMessages() {
		return messages;
	}
}
