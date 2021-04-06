/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preferences.OptionsConfigurationBlock.Key;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;

/**
 * Class that plays the role of the domain model in ElementNamesPreferencePage
 * In real life, this class would access a persistent store of some kind.
 */

public class ItemContentList {

	private Vector contents = new Vector();
	private Set changeListeners = new HashSet();

	private ElementNamesConfigurationBlock block;
	private Key[] keies;

	public ItemContentList(ElementNamesConfigurationBlock block, Key[] keies) {
		super();
		this.block = block;
		this.keies = keies;
		this.initData();
	}

	/**
	 * Clear the list of ItemContent
	 */
	public void clearList() {
		contents.clear();
	}

	/*
	 * Initialize the table data. Create COUNT tasks and add them them to the
	 * collection of tasks
	 */
	private void initData() {
		ItemContent content;

		String[] defaultNames = ReportPlugin.convert(block.getValue(keies[0]));
		String[] customNames = ReportPlugin.convert(block.getValue(keies[1]));
		String[] descriptions = ReportPlugin.convert(block.getValue(keies[2]));

		String[] newElement = ReportPlugin.getDefault().getDefaultDefaultNamePreference();

		if (defaultNames.length != customNames.length || defaultNames.length != descriptions.length
				|| customNames.length != descriptions.length) {
			defaultNames = ReportPlugin.getDefault().getDefaultDefaultNamePreference();
			customNames = ReportPlugin.getDefault().getDefaultCustomNamePreference();
			descriptions = ReportPlugin.getDefault().getDefaultDescriptionPreference();
		}

		IElementDefn elementDefn;
		for (int i = 0; i < defaultNames.length; i++) {
			elementDefn = DEUtil.getElementDefn(defaultNames[i]);
			content = new ItemContent(customNames[i]);
			content.setDefaultName(defaultNames[i]);
			content.setDisplayName(
					elementDefn != null ? DEUtil.getElementDefn(defaultNames[i]).getDisplayName() : defaultNames[i]);
			content.setDescription(descriptions[i]);
			contents.add(content);
		}

		if (newElement.length != defaultNames.length) {
			addNewElementName(defaultNames, newElement);
		}

	};

	/**
	 * Add the element to the table if which is not in the preferece table
	 * 
	 * @param oldElement The array of elements in the preference store
	 * @param newElement The array of new elements
	 */
	private void addNewElementName(String[] oldElement, String[] newElement) {
		int oldSize = oldElement.length;
		int newSize = newElement.length;
		int i, j;
		boolean find;
		ItemContent content;
		for (i = 0; i < newSize; i++) {
			find = false;
			for (j = oldSize - 1; j >= 0; j--) {
				if (newElement[i].equals(oldElement[j])) {
					find = true;
					break;
				}
			}
			if (find == false) {
				content = new ItemContent(""); //$NON-NLS-1$
				content.setDefaultName(newElement[i]); // $NON-NLS-1$
				content.setDescription(""); //$NON-NLS-1$
				contents.add(content);
			}
		}
	}

	/**
	 * Return the collection of tasks
	 */
	public Vector getContents() {
		return contents;
	}

	/**
	 * Set content for ItemContent List
	 * 
	 * @param i
	 * @param itemContent
	 * @return
	 */
	public boolean setContent(int i, ItemContent itemContent) {
		if (i < 0 || i >= contents.size()) {
			return false;
		}
		((ItemContent) contents.get(i)).setDefaultName(itemContent.getDefaultName());
		((ItemContent) contents.get(i)).setCustomName(itemContent.getCustomName());
		((ItemContent) contents.get(i)).setDescription(itemContent.getDescription());
		updateKeyValues();
		return true;

	}

	/**
	 * Add a new item to the collection of Items
	 */
	public void addContent() {
		ItemContent content = new ItemContent("New Custom Name"); //$NON-NLS-1$
		contents.add(contents.size(), content);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IItemListViewer) iterator.next()).addContent(content);
		updateKeyValues();
	}

	/**
	 * Add a new item to the collection of Items
	 */
	public void addContent(ItemContent content) {
		contents.add(contents.size(), content);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IItemListViewer) iterator.next()).addContent(content);
		updateKeyValues();
	}

	/**
	 * Remove a content from ItemContent List
	 * 
	 * @param content
	 */
	public void removeContent(ItemContent content) {
		contents.remove(content);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IItemListViewer) iterator.next()).removeContent(content);
		updateKeyValues();
	}

	/**
	 * Change all the ItemContent in List
	 * 
	 * @param content
	 */
	public void contentChanged(ItemContent content) {
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IItemListViewer) iterator.next()).updateContent(content);
		updateKeyValues();
	}

	/**
	 * Remove listener for ItemContent List
	 * 
	 * @param viewer
	 */
	public void removeChangeListener(IItemListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * Add listener on ItemContentList changing
	 * 
	 * @param viewer
	 */
	public void addChangeListener(IItemListViewer viewer) {
		changeListeners.add(viewer);
	}

	private void updateKeyValues() {
		StringBuffer defaultNamePreference = new StringBuffer();
		StringBuffer customNamePreference = new StringBuffer();
		StringBuffer descriptionPreference = new StringBuffer();
		Vector contents = getContents();
		for (int i = 0; i < contents.size(); i++) {
			ItemContent content = (ItemContent) contents.get(i);

			defaultNamePreference.append(content.getDefaultName());
			defaultNamePreference.append(ReportPlugin.PREFERENCE_DELIMITER);

			customNamePreference.append(content.getCustomName());
			customNamePreference.append(ReportPlugin.PREFERENCE_DELIMITER);

			descriptionPreference.append(content.getDescription());
			descriptionPreference.append(ReportPlugin.PREFERENCE_DELIMITER);
		}

		block.setValue(keies[0], defaultNamePreference.toString());
		block.setValue(keies[1], customNamePreference.toString());
		block.setValue(keies[2], descriptionPreference.toString());

	}

}
