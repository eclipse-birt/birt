package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.views.SearchInputDialog;
import org.eclipse.birt.report.designer.internal.ui.views.SearchInputDialog.Search;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.widget.ITreeViewerBackup;
import org.eclipse.birt.report.designer.ui.widget.TreeViewerBackup;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @since 3.3
 *
 */
public class SearchAction extends AbstractViewerAction {
	/**
	 * the default text
	 */
	@SuppressWarnings("hiding")
	public static final String TEXT = Messages.getString("SearchAction.text"); //$NON-NLS-1$

	/**
	 * Create a new search action under the specific viewer
	 *
	 * @param sourceViewer the source viewer
	 *
	 */
	public SearchAction(TreeViewer sourceViewer) {
		this(sourceViewer, TEXT);
	}

	/**
	 * Create a new search action under the specific viewer with the given text
	 *
	 * @param sourceViewer the source viewer
	 * @param text         the text of the action
	 */
	public SearchAction(TreeViewer sourceViewer, String text) {
		super(sourceViewer, text);
		setAccelerator(SWT.CTRL | 'F');
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		if (getSelectedObjects().size() == 0) { // no selection
			return false;
		}
		boolean isEnabled = false;
		for (Object selectedObject : super.getSelectedObjects()) {
			if (internalIsEnabled(selectedObject)) {
				isEnabled = true;
				break;
			}
		}
		return isEnabled;
	}

	private boolean internalIsEnabled(Object obj) {
		/*
		 * how to search SharedStyleHandle? if (obj instanceof SharedStyleHandle &&
		 * ((SharedStyleHandle) obj).getContainer() instanceof ReportItemThemeHandle) {
		 * return false; }
		 */
		if (obj instanceof ReportElementHandle) {
			return true;
		}
		if (obj instanceof SlotHandle) {
			return true;
		}
//		if (obj instanceof ContentElementHandle) {
//			return ((ContentElementHandle) obj).getDefn().getNameOption() != MetaDataConstants.NO_NAME
//					&& ((ContentElementHandle) obj).canEdit();
//		}
		// No report element selected
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	@Override
	public void run() {
		doSearch();
	}

	static SearchInputDialog inputDialog = null;

	private void doSearch() {
		TreeItem[] selectedItems = getSelectedItems();
		if (selectedItems != null && selectedItems.length > 0) {
			TreeItem selectedItem = selectedItems[0];
			Shell shell = selectedItem.getParent().getShell();
			if (inputDialog == null) {
				inputDialog = new SearchInputDialog(shell, null, IHelpContextIds.SEARCH_INPUT_DIALOG_ID,
						new SearchInputDialog.SearchActionServices() {

							@Override
							public void search(Search search) {
								saveChanges(search);
							}

							@Override
							public void close() {
								inputDialog = null;
							}

							@Override
							public List<String> getPropertyNames(boolean recursive) {
								return SearchAction.this.getPropertyNames(recursive);
							}
						});
				inputDialog.create();
				inputDialog.open();
			}
		}
	}

	/**
	 * @param recursive
	 * @return List<String>
	 */
	public List<String> getPropertyNames(boolean recursive) {
		Set<String> propNameSet = new HashSet<>();
		for (TreeItem item : getSelectedItems()) {
			Object data = item.getData();
			if (data instanceof SlotHandle) {
				SlotHandle slotHandle = (SlotHandle) data;
				int count = slotHandle.getCount();
				for (int i = 0; i < count; i++) {
					DesignElementHandle handle = slotHandle.get(i);
					getPropertyNames(handle, recursive, propNameSet);
				}
			} else if (data instanceof DesignElementHandle) {
				DesignElementHandle handle = (DesignElementHandle) data;
				getPropertyNames(handle, recursive, propNameSet);
			}
		}
		List<String> propNames = new ArrayList<>(propNameSet);
		Collections.sort(propNames);
		return propNames;
	}

	/**
	 * @param handle
	 * @param propNameSet
	 */
	private void getPropertyNames(DesignElementHandle handle, boolean recursive, Set<String> propNameSet) {
		DesignElement element = handle.getElement();
		List<IElementPropertyDefn> defns = element.getPropertyDefns();
		for (IElementPropertyDefn defn : defns) {
			String name = defn.getName();
			propNameSet.add(name);
		}
		if (recursive) {
			IElementDefn defn = handle.getDefn();
			int slotCount = defn.getSlotCount();
			for (int i = 0; i < slotCount; i++) {
				ISlotDefn slotDefn = defn.getSlot(i);
				SlotHandle slotHandle = handle.getSlot(slotDefn.getSlotID());
				int count = slotHandle.getCount();
				for (int j = 0; j < count; j++) {
					DesignElementHandle child = slotHandle.get(j);
					getPropertyNames(child, recursive, propNameSet);
				}
			}
		}
	}

	private interface SearchPathMember {
		public String toString();

		/**
		 * @param tree
		 * @return TreeItem
		 */
		public TreeItem findItem(Tree tree);

		/**
		 * @param item
		 * @return TreeItem
		 */
		public TreeItem findItem(TreeItem item);

		/**
		 * @return Object
		 */
		public Object getObject();
	}

	private static class SlotSearchPathMember implements SearchPathMember {
		private final SlotHandle slotHandle;

		public SlotSearchPathMember(final SlotHandle slotHandle) {
			this.slotHandle = slotHandle;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("slot ");
			sb.append(slotHandle.getSlotID());
			sb.append(" ");
			sb.append(slotHandle.getDefn().getDisplayName());
			return sb.toString();
		}

		@Override
		public TreeItem findItem(Tree tree) {
			TreeItem[] items = tree.getItems();
			if (items.length != 1) {
				return null;
			}
			TreeItem item = items[0];
			Object data = item.getData();
			if (data instanceof DesignElementHandle) {
				return findItem(item);
			}
			return null;
		}

		@Override
		public TreeItem findItem(TreeItem item) {
			int slotID = slotHandle.getSlotID();
			TreeItem[] items = item.getItems();
			return items[slotID - 1];
		}

		@Override
		public Object getObject() {
			return slotHandle;
		}
	}

	private static class DesignElementSearchPathMember implements SearchPathMember {
		private final TreeViewer treeViewer;
		private final DesignElementHandle handle;

		public DesignElementSearchPathMember(final TreeViewer treeViewer, final DesignElementHandle handle) {
			this.treeViewer = treeViewer;
			this.handle = handle;

		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("element #");
			sb.append(handle.getIndex());
			sb.append(" ");
			sb.append(handle.getClass().getName());
			sb.append(" ID=");
			sb.append(handle.getID());
			sb.append(" ");
			sb.append(handle.getDisplayLabel());
			return sb.toString();
		}

		@Override
		public TreeItem findItem(Tree tree) {
			TreeItem[] items = tree.getItems();
			for (TreeItem childItem : items) {
				return childItem; // top of tree is the report element
			}
			return null;
		}

		@Override
		public TreeItem findItem(TreeItem item) {
			treeViewer.setExpandedState(item, true);
			TreeItem[] items = item.getItems();
			for (TreeItem childItem : items) {
				Object data = childItem.getData();
				if (handle.equals(data)) {
					return item;
				}
			}
			return null;
		}

		@Override
		public Object getObject() {
			return handle;
		}
	}

	private static class SearchResult {
		private final List<SearchPathMember> path;
		private final String propertyName;

		public SearchResult(final List<SearchPathMember> path, final String propertyName) {
			this.path = path;
			this.propertyName = propertyName;
		}

		/**
		 * @param index
		 * @return string
		 */
		public String toDebugString(int index) {
			StringBuilder sb = new StringBuilder();
			sb.append("Search result " + index + ":\n");
			sb.append("  path:\n");
			for (SearchPathMember member : path) {
				sb.append("    ");
				sb.append(member.toString());
				sb.append("\n");
			}
			sb.append("  property: " + propertyName);
			return sb.toString();
		}
	}

	/**
	 * @param b
	 * @param regex
	 * @param wholeWord
	 * @param trim
	 */
	private void saveChanges(SearchInputDialog.Search search) {
		List<SearchResult> searchResults = new ArrayList<>();
		for (TreeItem item : getSelectedItems()) {
			Object data = item.getData();
			if (data instanceof SlotHandle) {
				SlotHandle slotHandle = (SlotHandle) data;
				int count = slotHandle.getCount();
				for (int i = 0; i < count; i++) {
					DesignElementHandle handle = slotHandle.get(i);
					List<SearchPathMember> path = new ArrayList<>();
					path.add(new SlotSearchPathMember(slotHandle));
					search(handle, search, path, searchResults);
				}
			} else if (data instanceof DesignElementHandle) {
				DesignElementHandle handle = (DesignElementHandle) data;
				List<SearchPathMember> path = new ArrayList<>();
				search(handle, search, path, searchResults);
			}
		}
		print(searchResults);
		Tree tree = getSourceViewer().getTree();
		print(tree);
		select(searchResults);
	}

	/**
	 * @param searchResults
	 */
	@SuppressWarnings("unused")
	private void expand(List<SearchResult> searchResults) {
		for (SearchResult result : searchResults) {
			ITreeViewerBackup backup = new TreeViewerBackup();
			for (SearchPathMember member : result.path) {
				backup.updateExpandedStatus(getSourceViewer(), member.getObject());
			}
		}
	}

	/**
	 * @param searchResults
	 */
	private void select(List<SearchResult> searchResults) {
		List<Object> list = new ArrayList<>();
		for (SearchResult result : searchResults) {
			SearchPathMember member = result.path.get(result.path.size() - 1);
			list.add(member.getObject());
		}
		ISelection selection = new StructuredSelection(list);
		getSourceViewer().setSelection(selection, true);
	}

	/**
	 * @param searchResults
	 */
	private void print(List<SearchResult> searchResults) {
		System.out.println("search results: " + searchResults.size());
		int searchResultsIndex = 0;
		for (SearchResult result : searchResults) {
			System.out.println(result.toDebugString(searchResultsIndex));
			searchResultsIndex++;
		}
	}

	/**
	 * @param tree
	 */
	private void print(Tree tree) {
		System.out.println("tree = " + tree);
		Control[] children = tree.getChildren();
		System.out.println("tree.children = " + children.length);
		int itemCount = tree.getItemCount();
		System.out.println("tree item count = " + itemCount);
		for (int i = 0; i < itemCount; i++) {
			TreeItem item = tree.getItem(i);
			print(0, item);
		}
	}

	/**
	 * @param item
	 */
	private void print(int level, TreeItem item) {
		String text = item.getText();
		Object data = item.getData();
		String className = data == null ? "null" : data.getClass().getName();
		int childItemCount = item.getItemCount();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append(' ');
		}
		if (text != null && text.trim().length() > 0) {
			sb.append(text + " ");
		}
		System.out.println(sb.toString() + className + " " + childItemCount);
		for (int i = 0; i < childItemCount; i++) {
			TreeItem childItem = item.getItem(i);
			print(level + 1, childItem);
		}
	}

	/**
	 */
	private void search(DesignElementHandle designElementHandle, SearchInputDialog.Search search,
			List<SearchPathMember> path, List<SearchResult> searchResults) {
		path.add(new DesignElementSearchPathMember(getSourceViewer(), designElementHandle));
		if (search.matches(designElementHandle)) {
			searchResults.add(new SearchResult(path, search.getSearchProp()));
		}
		if (search.isRecursive()) {
			IElementDefn defn = designElementHandle.getDefn();
			int slotCount = defn.getSlotCount();
			for (int i = 0; i < slotCount; i++) {
				ISlotDefn slotDefn = defn.getSlot(i);
				SlotHandle slotHandle = designElementHandle.getSlot(slotDefn.getSlotID());
				List<SearchPathMember> slotPath = new ArrayList<>(path);
				slotPath.add(new SlotSearchPathMember(slotHandle));
				int count = slotHandle.getCount();
				for (int j = 0; j < count; j++) {
					DesignElementHandle child = slotHandle.get(j);
					List<SearchPathMember> childPath = new ArrayList<>(slotPath);
					search(child, search, childPath, searchResults);
				}
			}
		}
	}

}
