/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.toc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class TOCView implements ITOCTree {

	protected static final Logger logger = Logger.getLogger(TOCView.class.getName());

	public static final ITOCTree EMPTY_TOC_VIEW = new EmptyTOCView();

	private ViewNode root;
	private ULocale locale;
	private TimeZone timeZone;
	private TOCStyleUtil styleUtil;
	private TOCFormatUtil formatUtil;
	private String format;
	private ViewFilter filter;

	public TOCView(ITreeNode tree, ReportDesignHandle handle, ULocale locale, TimeZone timeZone, String format) {
		this(tree, handle, locale, timeZone, format, null);
	}

	public TOCView(ITreeNode tree, ReportDesignHandle handle, ULocale locale, TimeZone timeZone) {
		this(tree, handle, locale, timeZone, null, null);
	}

	public TOCView(ITreeNode tree, ULocale locale, TimeZone timeZone) {
		this(tree, null, locale, timeZone, null, null);
	}

	public TOCView(ITreeNode tree, ReportDesignHandle handle, ULocale locale, TimeZone timeZone, String format,
			ViewFilter filter) {
		if ("viewer".equals(format)) {
			this.format = "html";
		} else {
			this.format = format;
		}
		this.filter = filter;
		this.locale = locale;
		this.timeZone = timeZone;
		this.formatUtil = new TOCFormatUtil(locale, timeZone);
		if (handle != null) {
			this.styleUtil = new TOCStyleUtil(handle);
		}
		this.root = new ViewNode(this, null, tree);
	}

	@Override
	public TOCNode getRoot() {
		return root;
	}

	@Override
	public TOCNode findTOC(String tocNodeId) {
		if (tocNodeId == null || tocNodeId.equals("/")) {
			return root;
		}
		return findTOC(root, tocNodeId, new TOCComparator());
	}

	protected TOCNode findTOC(TOCNode node, String tocNodeId, TOCComparator comparator) {
		List<ViewNode> children = node.getChildren();
		if (children == null || children.isEmpty()) {
			return null;
		}
		// the TOC id is in pre-visit ordered, the parent is less than all its
		// children,the current node is less than the following siblings
		for (int i = 0; i < children.size(); i++) {
			TOCNode child = (TOCNode) children.get(i);
			int result = comparator.compare(child.getNodeID(), tocNodeId);
			if (result == 0) {
				return child;
			}
			if (result > 0) {
				if (i > 0) {
					TOCNode prevNode = children.get(i - 1);
					return findTOC(prevNode, tocNodeId, comparator);
				}
				return null;
			}
		}

		TOCNode lastChild = children.get(children.size() - 1);
		return findTOC(lastChild, tocNodeId, comparator);
	}

	@Override
	public List<ViewNode> findTOCByValue(Object tocValue) {
		if (tocValue == null) {
			return null;
		}

		List<ViewNode> results = new ArrayList<>();
		doSearch(results, root, new SearchKey(tocValue));
		if (!results.isEmpty()) {
			return results;
		}
		return null;
	}

	private class SearchKey {

		SearchKey(Object value) {
			tocValue = value;
			if (value instanceof String) {
				stringValue = (String) value;
				try {
					numberValue = DataTypeUtil.toDouble(value);
				} catch (BirtException ex) {
					// we can safely ignore this exception
				}
				try {
					dateValue = DataTypeUtil.toDate(stringValue, locale, timeZone);
				} catch (BirtException ex) {
					// we can safely ignore this exception
				}
			}
			if (value instanceof Number) {
				numberValue = (Number) value;
			}
			if (value instanceof Date) {
				dateValue = (Date) value;
			}
		}

		Object tocValue;
		String stringValue;
		Number numberValue;
		Date dateValue;
	}

	private void doSearch(Collection<ViewNode> results, ViewNode node, SearchKey key) {
		if (compareTocValue(node, key)) {
			results.add(node);
		}
		Collection<ViewNode> children = (Collection<ViewNode>) node.getChildren();
		for (ViewNode child : children) {
			doSearch(results, child, key);
		}
	}

	private boolean compareTocValue(ViewNode node, SearchKey key) {
		// first we need compare the value with string to string
		String label = node.getDisplayString();

		if (label != null) {
			if (label.equals(key.stringValue)) {
				return true;
			}
		}

		// then we need compare the toc value directly
		Object value = node.getTOCValue();

		if (value == null) {
			return value == key.tocValue;
		}

		if (value instanceof Number) {
			if (key.numberValue != null) {
				return value.equals(key.numberValue);
			}
			return false;
		}

		if (value instanceof Date) {
			if (key.dateValue != null) {
				return value.equals(key.dateValue);
			}
			return false;
		}

		if (value instanceof String) {
			if (key.stringValue != null) {
				return value.equals(key.stringValue);
			}
		}

		return value.equals(key.tocValue);
	}

	protected String localizeValue(Object value, IScriptStyle style) {
		return formatUtil.localizeValue(value, style);
	}

	protected IScriptStyle getTOCStyle(int level, long elementId)

	{
		if (styleUtil != null) {
			try {
				return styleUtil.getTOCStyle(level, elementId);
			} catch (ScriptException se) {
				logger.log(Level.WARNING, se.getMessage(), se);
			}
		}
		return null;
	}

	protected boolean isHidden(ITreeNode node) {
		String formats = node.getHiddenFormats();
		if (formats == null || format == null) {
			return false;
		}

		if (formats.equals("all")) {
			return true;
		}

		String[] fmts = formats.split(",");
		for (String fmt : fmts) {
			if (format.equalsIgnoreCase(fmt)) {
				return true;
			}
		}

		return false;
	}

	protected boolean isVisible(ITreeNode node) {
		if (filter != null) {
			return filter.isVisible(node);
		}
		return true;
	}
}
