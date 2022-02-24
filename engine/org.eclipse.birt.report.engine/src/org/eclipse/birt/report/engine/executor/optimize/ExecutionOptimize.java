/*******************************************************************************
 * Copyright (c) 2007,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor.optimize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.DynamicTextItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.w3c.dom.css.CSSValue;

public class ExecutionOptimize {

	public ExecutionOptimize() {

	}

	public ExecutionPolicy optimize(Report report) {
		return new OptimizeVisitor(report).optimize();
	}

	private static class PolicyNode {

		PolicyNode parent;
		ArrayList children = new ArrayList();
		ReportItemDesign design;
		boolean execute;
		boolean breakBefore;
		boolean breakAfter;
		boolean executeAll;
	}

	private static class OptimizeVisitor extends DefaultReportItemVisitorImpl {

		Report report;
		boolean disableOptimization;

		boolean suppressDuplicate;
		PolicyNode currentNode;
		PolicyNode parentNode;

		LinkedList rows = new LinkedList();
		HashMap<ReportItemDesign, PolicyNode> itemNodeMap = new HashMap<>();

		OptimizeVisitor(Report report) {
			this.report = report;
		}

		ExecutionPolicy optimize() {
			ExecutionPolicy policies = new ExecutionPolicy();

			if (report.getOnPageStart() != null || report.getOnPageEnd() != null || report.getJavaClass() != null) {
				disableOptimization = true;
				return null;
			}
			handleContent(policies);
			// add all page content
			handleMasterPage(policies);

			if (disableOptimization) {
				return null;
			}
			return policies;
		}

		protected void handleContent(ExecutionPolicy policy) {
			PolicyNode root = new PolicyNode();
			parentNode = root;
			PolicyNode dummyFirst = new PolicyNode();
			dummyFirst.parent = root;
			dummyFirst.breakAfter = true;
			root.children.add(dummyFirst);
			currentNode = dummyFirst;
			int count = report.getContentCount();
			for (int i = 0; i < count; i++) {
				ReportItemDesign design = report.getContent(i);
				design.accept(this, null);
			}
			PolicyNode dummyLast = new PolicyNode();
			dummyLast.parent = root;
			dummyLast.breakBefore = true;
			root.children.add(dummyLast);
			// generate the policies;
			generateExecutionPolicy(policy, root);
		}

		protected void handleMasterPage(ExecutionPolicy policy) {
			PolicyNode root = new PolicyNode();
			parentNode = root;
			currentNode = root;
			PageSetupDesign pageSetup = report.getPageSetup();
			int count = pageSetup.getMasterPageCount();
			for (int i = 0; i < count; i++) {
				MasterPageDesign masterPage = pageSetup.getMasterPage(i);
				if (masterPage.getOnPageStart() != null || masterPage.getOnPageEnd() != null) {
					// disable the whole optimization as the user can user
					// getInstancessByElementId to access any content in run
					// task
					disableOptimization = true;
				}

				// FIXME handle others masterpage
				if (masterPage instanceof SimpleMasterPageDesign) {
					SimpleMasterPageDesign simple = (SimpleMasterPageDesign) masterPage;
					ArrayList headerList = simple.getHeaders();
					Iterator iter = headerList.iterator();
					while (iter.hasNext()) {
						ReportItemDesign design = (ReportItemDesign) iter.next();
						design.accept(this, null);
					}

					ArrayList footerList = simple.getFooters();
					iter = footerList.iterator();
					while (iter.hasNext()) {
						ReportItemDesign design = (ReportItemDesign) iter.next();
						design.accept(this, null);
					}
				}
			}
			generateMasterPageExecutionPolicy(policy, root);
		}

		protected void generateMasterPageExecutionPolicy(ExecutionPolicy policy, PolicyNode root) {
			ArrayList children = root.children;
			for (int i = 0; i < children.size(); i++) {
				PolicyNode child = (PolicyNode) children.get(i);
				child.executeAll = true;
			}

			for (int i = 0; i < children.size(); i++) {
				PolicyNode child = (PolicyNode) children.get(i);
				analysisExecutionPolicy(policy, child);
			}
		}

		protected void generateExecutionPolicy(ExecutionPolicy policy, PolicyNode root) {
			ArrayList children = root.children;
			for (int i = 0; i < children.size(); i++) {
				PolicyNode child = (PolicyNode) children.get(i);
				handlePageBreak(child);
			}

			for (int i = 0; i < children.size(); i++) {
				PolicyNode child = (PolicyNode) children.get(i);
				analysisExecutionPolicy(policy, child);
			}
			if (suppressDuplicate) {
				policy.enableSuppressDuplicate();
			}
		}

		protected boolean analysisExecutionPolicy(ExecutionPolicy policy, PolicyNode node) {
			for (int i = 0; i < node.children.size(); i++) {
				PolicyNode child = (PolicyNode) node.children.get(i);
				if (node.executeAll) {
					child.execute = true;
					child.executeAll = true;
				}

				if (analysisExecutionPolicy(policy, child)) {
					node.execute = true;
				}

			}
			if (node.execute || node.executeAll) {
				if (node.design != null) {
					policy.setExecute(node.design);
				}
			}
			return node.execute;
		}

		protected PolicyNode findLastLeafNode(PolicyNode root) {
			if (root != null && root.children.size() > 0) {
				return findLastLeafNode((PolicyNode) root.children.get(root.children.size() - 1));
			} else {
				return root;
			}
		}

		protected PolicyNode findPreviousNode(PolicyNode node) {
			if (node == null || node.parent == null) {
				return null;
			}
			int index = node.parent.children.indexOf(node);
			if (index < 1) {
				return findPreviousNode(node.parent);
			} else {
				return (PolicyNode) node.parent.children.get(index - 1);
			}
		}

		protected PolicyNode findNextNode(PolicyNode node) {
			if (node == null || node.parent == null) {
				return null;
			}
			int index = node.parent.children.indexOf(node);
			int count = node.parent.children.size();

			if (index < count - 1) {
				return (PolicyNode) node.parent.children.get(index + 1);

			} else {
				return findPreviousNode(node.parent);
			}
		}

		protected void handlePageBreak(PolicyNode node) {
			if (node.breakBefore) {
				node.execute = true;
				PolicyNode leaf = findLastLeafNode(findPreviousNode(node));
				if (leaf != null) {
					leaf.execute = true;
				}
			}

			if (node.breakAfter) {
				node.execute = true;
				PolicyNode leaf = findLastLeafNode(node);
				if (leaf != null) {
					leaf.execute = true;
				}
				PolicyNode next = findNextNode(node);
				if (next != null) {
					next.execute = true;
				}
			}

			for (int i = 0; i < node.children.size(); i++) {
				PolicyNode child = (PolicyNode) node.children.get(i);
				handlePageBreak(child);
			}
		}

		@Override
		public Object visitExtendedItem(ExtendedItemDesign item, Object value) {
			PolicyNode parent = parentNode;
			// setupPageBreaking...
			visitReportItem(item, Boolean.TRUE);
			parentNode = currentNode;
			// we need execute all its children...
			List children = item.getChildren();
			if (children != null) {
				Iterator iter = children.iterator();
				while (iter.hasNext()) {
					ReportItemDesign child = (ReportItemDesign) iter.next();
					child.accept(this, Boolean.TRUE);
				}
			}
			parentNode = parent;
			return Boolean.TRUE;
		}

		@Override
		public Object visitTemplate(TemplateDesign template, Object value) {
			visitReportItem(template, Boolean.TRUE);
			return value;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#
		 * visitFreeFormItem(org.eclipse.birt.report.engine.ir.FreeFormItemDesign,
		 * java.lang.Object)
		 */
		@Override
		public Object visitFreeFormItem(FreeFormItemDesign container, Object value) {
			PolicyNode parent = parentNode;
			visitReportItem(container, value);
			parentNode = currentNode;
			// setup the previous line...
			int count = container.getItemCount();
			for (int i = 0; i < count; i++) {
				ReportItemDesign item = container.getItem(i);
				item.accept(this, null);
			}
			parentNode = parent;
			return Boolean.TRUE;
		}

		@Override
		public Object visitListing(ListingDesign listing, Object value) {
			PolicyNode parent = parentNode;
			// visit listing itself.
			visitReportItem(listing, Boolean.TRUE);
			parentNode = currentNode;
			// support horizontal page break
			currentNode.breakAfter = true;
			// visit listing header
			BandDesign header = listing.getHeader();
			if (header != null) {
				header.accept(this, null);
			}
			// the last leaf should be execute. set as true;
			currentNode.execute = true;
			// visit listing groups
			int groupCount = listing.getGroupCount();
			if (groupCount > 0) {
				processGroup(listing, 0, header != null);
				processGroup(listing, 0, false);
			} else {
				processDetail(listing, header != null);
				processDetail(listing, false);
			}
			// visit listing footer
			BandDesign footer = listing.getFooter();
			if (footer != null) {
				footer.accept(this, Boolean.TRUE);
			}
			// we need execute the next element in-case there is a
			// page-break-after in the group footer.
			parentNode = parent;
			return Boolean.TRUE;
		}

		protected void processDetail(ListingDesign listing, boolean breakBefore) {
			BandDesign detail = listing.getDetail();
			if (detail != null) {
				PolicyNode parent = parentNode;
				visitReportItem(detail, true);
				parentNode = currentNode;
				int pageBreakInterval = listing.getPageBreakInterval();
				if (pageBreakInterval > 0) {
					currentNode.breakAfter = true;
				}
				if (breakBefore) {
					currentNode.breakBefore = true;
				}
				int count = detail.getContentCount();
				for (int i = 0; i < count; i++) {
					ReportItemDesign item = detail.getContent(i);
					item.accept(this, Boolean.TRUE);
				}
				parentNode = parent;
			}
		}

//      Reported Issue 56398
//      The pre-order traversal runs O(2^n) time to determine the page breaks (before and after).
//		Each child is also assigned (creates) are a node to execute.
//      While the current traversal logic has room to improve the original traversal creates more than 2^n new node which caused heap overflow.
//      The fix of 56398 addresses that issue.
		protected void processGroup(ListingDesign listing, int groupLevel, boolean breakBefore) {
			GroupDesign group = listing.getGroup(groupLevel);
			PolicyNode parent = parentNode;
			// visit group header
			visitReportItem(group, Boolean.TRUE);
			parentNode = currentNode;

			if (group.getPageBreakAfter() != null) {
				currentNode.breakAfter = true;
			}

			if (breakBefore || group.getPageBreakBefore() != null) {
				currentNode.breakBefore = true;
			}

			// visit listing header
			BandDesign header = group.getHeader();
			if (header != null) {
				header.accept(this, null);
			}
			if (++groupLevel < listing.getGroupCount()) {
				processGroup(listing, groupLevel, header != null);
				processGroup(listing, groupLevel, false);
			} else {
				processDetail(listing, true);
				processDetail(listing, false);
			}
			// visit group footer
			BandDesign footer = group.getFooter();
			if (footer != null) {
				footer.accept(this, Boolean.TRUE);
			}
			parentNode = parent;
		}

		@Override
		public Object visitBand(BandDesign band, Object value) {
			PolicyNode parent = parentNode;
			visitReportItem(band, value);
			parentNode = currentNode;
			int count = band.getContentCount();
			for (int i = 0; i < count; i++) {
				ReportItemDesign item = band.getContent(i);
				item.accept(this, null);
			}
			parentNode = parent;
			return Boolean.TRUE;
		}

		@Override
		public Object visitGridItem(GridItemDesign grid, Object value) {
			PolicyNode parent = parentNode;
			visitReportItem(grid, value);
			parentNode = currentNode;
			// support horizontal page break
			currentNode.breakAfter = true;
			int count = grid.getRowCount();
			for (int i = 0; i < count; i++) {
				RowDesign row = grid.getRow(i);
				row.accept(this, null);
			}
			parentNode = parent;
			return Boolean.TRUE;
		}

		protected boolean hasListingObject(RowDesign row) {
			int cellCount = row.getCellCount();
			for (int i = 0; i < cellCount; i++) {
				CellDesign cell = row.getCell(i);
				for (int j = 0; j < cell.getContentCount(); j++) {
					ReportItemDesign item = cell.getContent(j);
					if (item instanceof ListingDesign || item instanceof ExtendedItemDesign) {
						return true;
					}
				}
			}
			return false;
		}

		protected void setRowExecute(PolicyNode node) {
			node.execute = true;
			for (int i = 0; i < node.children.size(); i++) {
				PolicyNode child = (PolicyNode) node.children.get(i);
				child.execute = true;
				for (int j = 0; j < child.children.size(); j++) {
					PolicyNode grandson = (PolicyNode) child.children.get(j);
					grandson.execute = true;
				}
			}
		}

		@Override
		public Object visitRow(RowDesign row, Object value) {
			PolicyNode parent = parentNode;
			visitReportItem(row, true);
			PolicyNode rowNode = currentNode;
			parentNode = currentNode;
			rows.addLast(currentNode);
			int cellCount = row.getCellCount();
			for (int i = 0; i < cellCount; i++) {
				CellDesign cell = row.getCell(i);
				cell.accept(this, null);
			}
			if (hasListingObject(row)) {
				setRowExecute(rowNode);
			}
			parentNode = parent;
			rows.removeLast();
			return value;
		}

		@Override
		public Object visitCell(CellDesign cell, Object value) {
			PolicyNode parent = parentNode;
			visitReportItem(cell, value);
			parentNode = currentNode;
			if (cell.getRowSpan() != 1 || cell.getColSpan() != 1 || needProcessDrop(cell.getDrop())) {
				currentNode.execute = true;
			}
			int count = cell.getContentCount();
			for (int i = 0; i < count; i++) {
				ReportItemDesign item = cell.getContent(i);
				item.accept(this, null);
			}
			parentNode = parent;
			return value;
		}

		private boolean needProcessDrop(String drop) {
			return drop != null && !"none".equals(drop);
		}

		@Override
		public Object visitImageItem(ImageItemDesign image, Object value) {
			visitReportItem(image, Boolean.TRUE);
			return value;
		}

		@Override
		public Object visitDynamicTextItem(DynamicTextItemDesign multiLine, Object value) {
			visitReportItem(multiLine, Boolean.TRUE);
			return value;
		}

		@Override
		public Object visitTextItem(TextItemDesign text, Object value) {
			visitReportItem(text, Boolean.TRUE);
			return value;
		}

		@Override
		public Object visitDataItem(DataItemDesign data, Object value) {
			visitReportItem(data, value);
			if (data.getSuppressDuplicate()) {
				suppressDuplicate = true;
			}
			return value;
		}

//      Fix of TED 56398 using a map to retrieve the previously visited node. Reduce the number of node objects.
		@Override
		public Object visitReportItem(ReportItemDesign item, Object value) {
			PolicyNode node = null;
			if (itemNodeMap.containsKey(item)) {
				node = itemNodeMap.get(item);
			} else {
				node = new PolicyNode();
				node.parent = parentNode;
				node.design = item;
				itemNodeMap.put(item, node);
				parentNode.children.add(node);
			}
			boolean needExecute = value == Boolean.TRUE;
			if (needExecute) {
				node.execute = true;
			}
			setupPageBreak(node);
			currentNode = node;
			return Boolean.valueOf(needExecute);
		}

		protected void setupPageBreak(PolicyNode node) {
			ReportItemDesign item = node.design;
			// test if it changes the pagination
			String styleClass = item.getStyleName();
			if (styleClass != null) {
				IStyle style = report.findStyle(styleClass);
				CSSValue masterPage = style.getProperty(IStyle.STYLE_MASTER_PAGE);
				CSSValue pageBreakBefore = style.getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE);
				CSSValue pageBreakAfter = style.getProperty(IStyle.STYLE_PAGE_BREAK_AFTER);
				if (masterPage != null || (pageBreakBefore != null) && !pageBreakBefore.equals(IStyle.AUTO_VALUE)) {
					node.breakBefore = true;
					node.execute = true;
				}
				if (pageBreakAfter != null && !pageBreakAfter.equals(IStyle.AUTO_VALUE)) {
					node.breakAfter = true;
					node.execute = true;
				}
			}

			// if the item has scripts, it must be executed
			if (item.getJavaClass() != null || item.getOnCreate() != null || item.getOnPageBreak() != null) {
				node.breakBefore = true;
				node.breakAfter = true;
				node.execute = true;
				// since table script can access column, so page-break on columns can be
				// changed.
				if (item instanceof TableItemDesign) {
					node.executeAll = true;
				}
			}

			if (node.breakBefore || node.breakAfter) {
				Iterator iter = rows.iterator();
				while (iter.hasNext()) {
					PolicyNode row = (PolicyNode) iter.next();
					row.executeAll = true;
				}
				return;
			}

			// if it has map or highlight, it must be executed
			if (item.getHighlight() != null || item.getMap() != null) {
				node.execute = true;
			}
			// if it has TOC/book mark/hyper link, it must be executed
			if (item.getTOC() != null || item.getBookmark() != null || item.getAction() != null) {
				node.execute = true;
			}
			// if it has queries, it must be executed
			if (item.getQueries() != null) {
				node.execute = true;
			}
			// if it has page-break, it must be executed
			if (item.getVisibility() != null) {
				node.execute = true;
			}
			// support alt text on report item
			if (item.getAltText() != null || item.getAltTextKey() != null) {
				node.execute = true;
			}
		}

	}
}
