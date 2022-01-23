/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.layout.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.nLayout.area.impl.SizeBasedContent;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.engine.presentation.SizeBasedPageSection;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class HTMLLayoutPageHintManager {
	protected HTMLLayoutContext context;

	/**
	 * cache the content is finished or not
	 */
	protected HashMap layoutHint = new HashMap();

	/**
	 * content instanceID to size based content mapping.
	 */
	protected HashMap<String, SizeBasedContent> sizeBasedContentMapping = new HashMap<String, SizeBasedContent>();

	protected ArrayList pageHints = new ArrayList();

	public HTMLLayoutPageHintManager(HTMLLayoutContext context) {
		this.context = context;
	}

	public void setPageHint(List hints) {
		pageHints.addAll(hints);
	}

	public ArrayList getPageHint() {
		ArrayList hints = new ArrayList();
		hints.addAll(pageHints);
		return hints;
	}

	public void reset() {
		layoutHint = new HashMap();
		sizeBasedContentMapping = new HashMap<String, SizeBasedContent>();
		context.setFinish(false);
		context.setAllowPageBreak(true);
		context.setMasterPage(null);
	}

	public void addLayoutHint(IContent content, boolean finished) {
		layoutHint.put(content, Boolean.valueOf(finished));
	}

	public void removeLayoutHint(IContent content) {
		layoutHint.remove(content);
	}

	public boolean getLayoutHint(IContent content) {
		Object finished = layoutHint.get(content);
		if (finished != null && finished instanceof Boolean) {
			return ((Boolean) finished).booleanValue();
		}
		return true;
	}

	public void removeLayoutHint() {
		layoutHint.clear();
	}

	// page hints for last parallel pages.
	protected HashMap<String, UnresolvedRowHint> currentHints = new HashMap<String, UnresolvedRowHint>();
	// page hints for current parallel pages.
	protected HashMap<String, UnresolvedRowHint> hints = new HashMap<String, UnresolvedRowHint>();
	// page hint for last single page, which should be flush to document
	// immediately.
	protected HashMap<String, UnresolvedRowHint> pageRowHint = new HashMap<String, UnresolvedRowHint>();

	public void generatePageRowHints(Collection<String> keys) {
		pageRowHint.clear();
		Iterator<String> iter = keys.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			UnresolvedRowHint hint = hints.get(key);
			if (hint != null) {
				pageRowHint.put(key, hint);
			}
		}
	}

	public HashMap<String, UnresolvedRowHint> getUnresolvedRowHints() {
		return pageRowHint;
	}

	protected ArrayList columnHints = new ArrayList();

	public List getTableColumnHints() {
		return columnHints;
	}

	public void addTableColumnHints(List hints) {
		columnHints.addAll(hints);
	}

	public void addTableColumnHint(TableColumnHint hint) {
		columnHints.add(hint);
	}

	public UnresolvedRowHint getUnresolvedRowHint(String key) {
		if (hints.size() > 0) {
			return hints.get(key);
		}
		return null;
	}

	public void addUnresolvedRowHint(String key, UnresolvedRowHint hint) {
		currentHints.put(key, hint);
	}

	public void clearPageHint() {
		columnHints.clear();
		pageHints.clear();
	}

	public void resetRowHint() {
		if (!context.emptyPage) {
			hints.clear();
			hints.putAll(currentHints);
			currentHints.clear();
		}
	}

	public void setLayoutPageHint(IPageHint pageHint) {
		if (pageHint != null) {
			context.pageNumber = pageHint.getPageNumber();
			context.masterPage = pageHint.getMasterPage();
			// column hints
			int count = pageHint.getTableColumnHintCount();
			for (int i = 0; i < count; i++) {
				columnHints.add(pageHint.getTableColumnHint(i));
			}
			// unresolved row hints
			count = pageHint.getUnresolvedRowCount();
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					UnresolvedRowHint hint = pageHint.getUnresolvedRowHint(i);
					String key = getHintMapKey(hint.getTableId());
					hints.put(key, hint);
				}
			}
			// size based page break hints
			for (int i = 0; i < pageHint.getSectionCount(); i++) {
				PageSection section = pageHint.getSection(i);
				if (section instanceof SizeBasedPageSection) {
					SizeBasedPageSection sizeBasedSection = (SizeBasedPageSection) section;
					if (sizeBasedSection.start.dimension != -1) {
						InstanceID startID = sizeBasedSection.starts[sizeBasedSection.starts.length - 1]
								.getInstanceID();
						if (startID != null) {
							sizeBasedContentMapping.put(startID.toUniqueString(), sizeBasedSection.start);
						}
					}
					if (sizeBasedSection.end.dimension != -1) {
						InstanceID endID = sizeBasedSection.ends[sizeBasedSection.ends.length - 1].getInstanceID();
						if (endID != null) {
							sizeBasedContentMapping.put(endID.toUniqueString(), sizeBasedSection.end);
						}
					}
				}
			}
		}
	}

	public HashMap<String, SizeBasedContent> getSizeBasedContentMapping() {
		return sizeBasedContentMapping;
	}

	public String getHintMapKey(String tableId) {
		String key = tableId;
		List hints = getTableColumnHint(key);
		Iterator iter = hints.iterator();
		StringBuffer keyBuf = new StringBuffer(key);
		while (iter.hasNext()) {
			int[] vs = (int[]) iter.next();
			keyBuf.append('-');
			keyBuf.append(vs[0]);
			keyBuf.append('-');
			keyBuf.append(vs[1]);
		}
		return keyBuf.toString();
	}

	public List getTableColumnHint(String tableId) {
		List list = new ArrayList();
		if (columnHints.size() > 0) {
			Iterator iter = columnHints.iterator();
			while (iter.hasNext()) {
				TableColumnHint hint = (TableColumnHint) iter.next();
				if (tableId.equals(hint.getTableId())) {
					list.add(new int[] { hint.getStart(), hint.getStart() + hint.getColumnCount() });
				}
			}
		}
		return list;
	}

}
