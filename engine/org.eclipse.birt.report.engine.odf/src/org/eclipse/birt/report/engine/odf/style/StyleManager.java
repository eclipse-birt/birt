/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.odf.style;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager and container for document styles.
 * 
 * Style entries must be created using the StyleBuilder methods. Before the
 * style entry is added to the style manager, its properties must be modified
 * first. When adding style entries into the manager, they are cloned and a name
 * is assigned to them. So further changes done to the styles won't have any
 * effect. If a style has been changed after adding, it must be re-added to the
 * style manager to have an effect.
 */
public class StyleManager {
	private static final int MAX_TYPES = StyleConstant.TYPE_COUNT;
	private int[] lastIndex;

	private static final String[] styleNamePrefix = { "P", //$NON-NLS-1$
			"T", //$NON-NLS-1$
			"Table", //$NON-NLS-1$
			"Column", //$NON-NLS-1$
			"Row", //$NON-NLS-1$
			"Cell", //$NON-NLS-1$
			"Mpm", //$NON-NLS-1$
			"Draw" //$NON-NLS-1$
	};

	private Map<StyleEntry, String> styleToName;
	private List<StyleEntry> styleList;
	private String namePrefix;

	public StyleManager(String namePrefix) {
		if (namePrefix != null) {
			this.namePrefix = namePrefix;
		} else {
			this.namePrefix = ""; //$NON-NLS-1$
		}

		lastIndex = new int[MAX_TYPES];
		for (int i = 0; i < MAX_TYPES; i++) {
			lastIndex[i] = 0;
		}

		styleToName = new HashMap<StyleEntry, String>();
		styleList = new ArrayList<StyleEntry>();
	}

	public Collection<StyleEntry> getStyles() {
		return Collections.unmodifiableCollection(styleList);
	}

	/**
	 * Adds a style.
	 * 
	 * @param style
	 * @return
	 */
	public String addStyle(StyleEntry style) {
		return addStyle(null, style);
	}

	/**
	 * Returns a style object for the given engine style and type. If no such style
	 * existed before, one is created.
	 * 
	 * @param style
	 * @param type
	 * @return assigned style name
	 */
	public String addStyle(String prefix, StyleEntry newStyleEntry) {
		if (newStyleEntry == null) {
			return null;
		}

		// if such style already exists
		String existingStyleName = styleToName.get(newStyleEntry);
		if (existingStyleName != null) {
			// only if same prefix, else create a new style entry
			if (prefix == null || existingStyleName.startsWith(prefix)) {
				newStyleEntry.setName(existingStyleName);
				return existingStyleName;
			}
		}

		// create new entry
		newStyleEntry.setName(generateName(prefix, newStyleEntry.getType()));
		newStyleEntry = (StyleEntry) newStyleEntry.clone(); // clone to prevent external changes
		styleToName.put(newStyleEntry, newStyleEntry.getName());

		// the list might contain duplicates, with different prefixes
		styleList.add(newStyleEntry);
		return newStyleEntry.getName();
	}

	private String generateName(String prefix, int type) {
		if (prefix == null) {
			prefix = ""; //$NON-NLS-1$
		}
		String stylePrefix = "Dummy"; //$NON-NLS-1$
		if (type < 0 || type > MAX_TYPES) {
			return ""; //$NON-NLS-1$
		} else {
			stylePrefix = styleNamePrefix[type];
		}

		lastIndex[type]++;
		return prefix + namePrefix + stylePrefix + lastIndex[type];
	}
}
