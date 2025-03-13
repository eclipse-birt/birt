package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
	private LinkedList<TreeItem[]> selectedItemsStack = new LinkedList<>();

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
		sourceViewer.getTree().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (inputDialog != null) {
					inputDialog.close();
				}
			}
		});
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
							public List<SearchResult> search(Search search) {
								return saveChanges(search);
							}

							@Override
							public void close() {
								inputDialog = null;
							}

							@Override
							public List<String> getPropertyNames(boolean recursive) {
								return SearchAction.this.getPropertyNames(recursive);
							}

							@Override
							public boolean back() {
								return SearchAction.this.back();
							}

							@Override
							public void select(Object data) {
								SearchAction.this.select(data);
							}
						});
				inputDialog.create();
				inputDialog.open();
			}
		}
	}

	protected boolean back() {
		LinkedList<TreeItem[]> selectedItemsStack = this.selectedItemsStack;
		if (!selectedItemsStack.isEmpty()) {
			TreeItem[] selectedItems = selectedItemsStack.pop();
			List<Object> list = new ArrayList<>();
			for (TreeItem item : selectedItems) {
				list.add(item.getData());
			}
			ISelection selection = new StructuredSelection(list);
			getSourceViewer().setSelection(selection, true);
		}
		return selectedItemsStack.isEmpty();
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
		String toString();

		String getName();

		/**
		 * @return Object
		 */
		Object getObject();
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

		public String getName() {
			return slotHandle.getDefn().getDisplayName();
		}

		@Override
		public Object getObject() {
			return slotHandle;
		}
	}

	private static class DesignElementSearchPathMember implements SearchPathMember {
		private final DesignElementHandle handle;

		public DesignElementSearchPathMember(final DesignElementHandle handle) {
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

		public String getName() {
			return handle.getDisplayLabel() + " " + handle.getIndex();
		}

		@Override
		public Object getObject() {
			return handle;
		}
	}

	/**
	 * @since 3.3
	 *
	 */
	public static class SearchResult {
		private final List<SearchPathMember> path;
		private final String propertyName;

		/**
		 * @param path
		 * @param propertyName
		 */
		public SearchResult(final List<SearchPathMember> path, final String propertyName) {
			this.path = path;
			this.propertyName = propertyName;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			String sep = "";
			for (SearchPathMember member : path) {
				sb.append(sep);
				sep = member instanceof SlotSearchPathMember ? ":" : " / ";
				sb.append(member.getName());
			}
			return sb.toString();
		}

		/**
		 * @return Returns the propertyName.
		 */
		public String getPropertyName() {
			return propertyName;
		}

		/**
		 * @return String
		 */
		public String getElementName() {
			if (path.isEmpty()) {
				return null;
			}
			SearchPathMember member = path.get(path.size() - 1);
			return member.getName();
		}

		/**
		 * @return Object
		 */
		public Object getObject() {
			if (path.isEmpty()) {
				return null;
			}
			SearchPathMember member = path.get(path.size() - 1);
			return member.getObject();
		}
	}

	private List<SearchResult> saveChanges(SearchInputDialog.Search search) {
		List<SearchResult> searchResults = new ArrayList<>();
		TreeItem[] selectedItems;
		selectedItems = getSelectedItems();
		for (TreeItem item : selectedItems) {
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
//		Tree tree = getSourceViewer().getTree();
//		print(tree);
		if (!searchResults.isEmpty()) {
			select(searchResults);
		}
		return searchResults;
	}

	@SuppressWarnings("unused")
	private void expand(List<SearchResult> searchResults) {
		for (SearchResult result : searchResults) {
			ITreeViewerBackup backup = new TreeViewerBackup();
			for (SearchPathMember member : result.path) {
				backup.updateExpandedStatus(getSourceViewer(), member.getObject());
			}
		}
	}

	private void select(List<SearchResult> searchResults) {
		List<Object> list = new ArrayList<>();
		for (SearchResult result : searchResults) {
			SearchPathMember member = result.path.get(result.path.size() - 1);
			list.add(member.getObject());
		}
		this.selectedItemsStack.push(getSelectedItems());
		ISelection selection = new StructuredSelection(list);
		getSourceViewer().setSelection(selection, true);
	}

	private void select(Object object) {
		List<Object> list = new ArrayList<>();
		if (object instanceof SearchResult) {
			SearchResult searchResult = (SearchResult) object;
			object = searchResult.getObject();
		}
		list.add(object);
		this.selectedItemsStack.push(getSelectedItems());
		ISelection selection = new StructuredSelection(list);
		getSourceViewer().setSelection(selection, true);
	}

	private void print(List<SearchResult> searchResults) {
		System.out.println("search results: " + searchResults.size());
		int searchResultsIndex = 0;
		for (SearchResult result : searchResults) {
			System.out.print(searchResultsIndex);
			System.out.print(" ");
			System.out.println(result.toString());
			searchResultsIndex++;
		}
	}

	@SuppressWarnings("unused")
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

	private void search(DesignElementHandle designElementHandle, SearchInputDialog.Search search,
			List<SearchPathMember> path, List<SearchResult> searchResults) {
		path.add(new DesignElementSearchPathMember(designElementHandle));
		String propertyName = search.matches(designElementHandle);
		if (propertyName != null) {
			searchResults.add(new SearchResult(path, propertyName));
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
