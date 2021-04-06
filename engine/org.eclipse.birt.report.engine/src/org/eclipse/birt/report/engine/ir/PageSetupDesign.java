/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Page Setup
 * 
 */
public class PageSetupDesign {

	/**
	 * sequence of master pages
	 */
	protected ArrayList<MasterPageDesign> masterPages = new ArrayList<MasterPageDesign>();
	/**
	 * maps used to map the page with the name.
	 */
	protected HashMap pageMaps = new HashMap();

	/**
	 * collection used to store the page sequence.
	 */
	protected ArrayList<PageSequenceDesign> pageSequences = new ArrayList<PageSequenceDesign>();
	/**
	 * maps used to map the page sequence with the name
	 */
	protected HashMap sequenceMaps = new HashMap();

	public Collection<MasterPageDesign> getMasterPages() {
		return masterPages;
	}

	/**
	 * get total master pages count.
	 * 
	 * @return total master pages.
	 */
	public int getMasterPageCount() {
		return this.masterPages.size();
	}

	/**
	 * get master page at index
	 * 
	 * @param index page index
	 * @return master page
	 */
	public MasterPageDesign getMasterPage(int index) {
		assert (index >= 0 && index < this.masterPages.size());
		return (MasterPageDesign) this.masterPages.get(index);
	}

	/**
	 * add master page into page setup.
	 * 
	 * @param page page to be added
	 */
	public void addMasterPage(MasterPageDesign page) {
		assert page != null;
		assert page.getName() != null;
		this.pageMaps.put(page.getName(), page);
		this.masterPages.add(page);
	}

	/**
	 * get the page named by pageName.
	 * 
	 * @param pageName page name.
	 * @return master page associated with the name.
	 */
	public MasterPageDesign findMasterPage(String pageName) {
		assert (pageName != null);
		return (MasterPageDesign) this.pageMaps.get(pageName);
	}

	public Collection<PageSequenceDesign> getPageSequences() {
		return pageSequences;
	}

	/**
	 * get total pages sequence count.
	 * 
	 * @return total page sequences.
	 */
	public int getPageSequenceCount() {
		return this.pageSequences.size();
	}

	/**
	 * get page sequence at index
	 * 
	 * @param index sequence index
	 * @return page sequence
	 */
	public PageSequenceDesign getPageSequence(int index) {
		assert (index >= 0 && index < this.pageSequences.size());
		return (PageSequenceDesign) this.pageSequences.get(index);
	}

	/**
	 * add page sequence into page setup.
	 * 
	 * @param sequence page sequence to be added
	 */
	public void addPageSequence(PageSequenceDesign sequence) {
		assert sequence != null;
		assert sequence.getName() != null;
		this.sequenceMaps.put(sequence.getName(), sequence);
		this.pageSequences.add(sequence);
	}

	/**
	 * get the page sequence named by name.
	 * 
	 * @param name page sequence name.
	 * @return page sequence associated with the name.
	 */
	public PageSequenceDesign findPageSequence(String name) {
		assert (name != null);
		return (PageSequenceDesign) this.sequenceMaps.get(name);
	}

}
