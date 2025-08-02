/*******************************************************************************
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.views.SearchInputDialog;
import org.eclipse.birt.report.designer.internal.ui.views.SearchInputDialog.Search;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;

/**
 * @since 4.20
 *
 */
public class SearchAction extends AbstractViewerAction {

	private static final String SEARCH_ACTION_TEXT = Messages.getString("SearchAction.text"); //$NON-NLS-1$

	private static final String PROPERTY_NAME_ID = "id";

	private static SearchInputDialog inputDialog;

	private final LinkedList<TreeItem[]> selectedItemsStack = new LinkedList<>();

	private HashMap<String, String> propNameDisplay = new HashMap<String, String>();

	/**
	 * Create a new search action under the specific viewer.
	 *
	 * @param sourceViewer the source viewer
	 *
	 */
	public SearchAction(TreeViewer sourceViewer) {
		this(sourceViewer, SEARCH_ACTION_TEXT);
	}

	/**
	 * Create a new search action under the specific viewer with the given text.
	 *
	 * @param sourceViewer the source viewer
	 * @param text         the text of the action
	 */
	public SearchAction(TreeViewer sourceViewer, String text) {
		super(sourceViewer, text);
		sourceViewer.getTree().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (inputDialog != null) {
					inputDialog.close();
				}
			}
		});
	}

	@Override
	public boolean isEnabled() {
		for (Object selectedObject : getSelectedObjects()) {
			if (internalIsEnabled(selectedObject)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void run() {
		doSearch();
	}

	private void doSearch() {
		if (inputDialog == null) {
			Shell shell = getSourceViewer().getTree().getShell();
			inputDialog = new SearchInputDialog(shell, null, IHelpContextIds.SEARCH_INPUT_DIALOG_ID,
					new SearchInputDialog.SearchActionServices() {
						private final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage();

						private final IPartListener2 partListener = new IPartListener2() {
							private final IEditorPart activeEditor = page.getActiveEditor();
							private final IWorkbenchPart activePart = page.getActivePart();

							@Override
							public void partHidden(IWorkbenchPartReference partRef) {
								IWorkbenchPart part = partRef.getPart(false);
								if (part == activeEditor || part == activePart) {
									inputDialog.close();
								}
							}
						};

						private final ISelectionChangedListener selectionChangedListener = e -> {
							inputDialog.updateProperties();
						};

						{
							getSourceViewer().addPostSelectionChangedListener(selectionChangedListener);
							page.addPartListener(partListener);
						}

						@Override
						public List<SearchResult> search(Search search) {
							return saveChanges(search);
						}

						@Override
						public void close() {
							inputDialog = null;
							getSourceViewer().removePostSelectionChangedListener(selectionChangedListener);
							page.removePartListener(partListener);
						}

						@Override
						public List<String> getPropertyNames(boolean recursive) {
							return SearchAction.this.getPropertyNames(recursive);
						}

						@Override
						public HashMap<String, String> getPropertyDisplayList() {
							return SearchAction.this.getPropertyDisplayList();
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
		} else {
			inputDialog.getShell().setActive();
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
	 * Returns the property names of the selection.
	 *
	 * @param recursive whether to walk deeply into the structure.
	 * @return the property names of the selection.
	 */
	public List<String> getPropertyNames(boolean recursive) {
		Set<String> propNameSet = new TreeSet<>();
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
		return new ArrayList<String>(propNameSet);
	}

	/**
	 * Get the display name of properties
	 *
	 * @return the display name of properties
	 */
	public HashMap<String, String> getPropertyDisplayList() {
		return propNameDisplay;
	}

	private void getPropertyNames(DesignElementHandle handle, boolean recursive, Set<String> propNameSet) {
		DesignElement element = handle.getElement();
		List<IElementPropertyDefn> defns = element.getPropertyDefns();
		for (IElementPropertyDefn defn : defns) {
			String name = defn.getName();
			propNameSet.add(name);
			propNameDisplay.putIfAbsent(name, defn.getDisplayName());
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
		// element-property "id"
		propNameSet.add(PROPERTY_NAME_ID);
		if (!propNameDisplay.containsKey(PROPERTY_NAME_ID))
			propNameDisplay.put(PROPERTY_NAME_ID, "ID");
	}

	private interface SearchPathMember {
		String getName();

		String getElementType();

		Long getElementId();

		Object getObject();
	}

	private static class SlotSearchPathMember implements SearchPathMember {
		private final SlotHandle slotHandle;

		public SlotSearchPathMember(SlotHandle slotHandle) {
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

		public String getElementType() {
			return slotHandle.getDefn().getDisplayName();
		}

		public Long getElementId() {
			return slotHandle.getElement().getID();
		}

		@Override
		public Object getObject() {
			return slotHandle;
		}
	}

	private static class DesignElementSearchPathMember implements SearchPathMember {
		private final DesignElementHandle handle;

		public DesignElementSearchPathMember(DesignElementHandle handle) {
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
			return handle.getDisplayLabel();
		}

		public String getElementType() {
			return handle.getElement().getDefn().getDisplayName();
		}

		public Long getElementId() {
			return handle.getID();
		}

		@Override
		public Object getObject() {
			return handle;
		}
	}

	/**
	 * Provides access to the search result details.
	 */
	public static class SearchResult {
		private final List<SearchPathMember> path;
		private final String propertyName;

		/**
		 * Creates an instance from the path and property name.
		 *
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
		 * Returns the property name.
		 *
		 * @return the property name.
		 */
		public String getPropertyName() {
			return propertyName;
		}

		/**
		 * Returns the name of the last path element.
		 *
		 * @return the name of the last path element.
		 */
		public String getElementName() {
			if (path.isEmpty()) {
				return null;
			}
			SearchPathMember member = path.get(path.size() - 1);
			return member.getName();
		}

		/**
		 * Returns the element type of the last path element.
		 *
		 * @return the element type of the last path element.
		 */
		public String getElementType() {
			if (path.isEmpty()) {
				return null;
			}
			SearchPathMember member = path.get(path.size() - 1);
			return member.getElementType();
		}

		/**
		 * Returns the id of the last path element.
		 *
		 * @return the id of the last path element.
		 */
		public Long getElementId() {
			if (path.isEmpty()) {
				return null;
			}
			SearchPathMember member = path.get(path.size() - 1);
			return member.getElementId();
		}

		/**
		 * Returns the object of the last path member.
		 *
		 * @return the object of the last path member.
		 */
		public Object getObject() {
			if (path.isEmpty()) {
				return null;
			}
			SearchPathMember member = path.get(path.size() - 1);
			return member.getObject();
		}
	}

	/**
	 * Keep this in sync with {@link #saveChanges(Search)}.
	 */
	private boolean internalIsEnabled(Object obj) {
		if (obj instanceof DesignElementHandle) {
			return true;
		}
		if (obj instanceof SlotHandle) {
			return true;
		}
		// No report element selected
		return false;
	}

	/**
	 * Keep this in sync with {@link SearchAction#internalIsEnabled(Object)}
	 */
	private List<SearchResult> saveChanges(SearchInputDialog.Search search) {
		List<SearchResult> searchResults = new ArrayList<>();
		TreeItem[] selectedItems = getSelectedItems();
		for (TreeItem item : selectedItems) {
			Object data = item.getData();
			if (data instanceof SlotHandle) {
				SlotHandle slotHandle = (SlotHandle) data;
				for (int i = 0, count = slotHandle.getCount(); i < count; i++) {
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
		if (!searchResults.isEmpty()) {
			select(searchResults);
		}
		return searchResults;
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

	private boolean isEntryRegistered(List<SearchResult> searchResults, SearchResult newResult) {
		for (SearchResult entry : searchResults) {
			if (entry.getElementName().equals(newResult.getElementName()) &&
						entry.getElementId().equals(newResult.getElementId())
			)
				return true;
		}
		return false;
	}

	private void search(DesignElementHandle designElementHandle, SearchInputDialog.Search search,
			List<SearchPathMember> path, List<SearchResult> searchResults) {
		path.add(new DesignElementSearchPathMember(designElementHandle));
		String propertyName = search.matches(designElementHandle);
		if (propertyName != null) {
			SearchResult newResultEntry = new SearchResult(path, propertyName);
			if (!isEntryRegistered(searchResults, newResultEntry)) {
				searchResults.add(newResultEntry);
			}
		}
		if (search.isRecursive()) {
			IElementDefn defn = designElementHandle.getDefn();
			for (int i = 0, slotCount = defn.getSlotCount(); i < slotCount; i++) {
				ISlotDefn slotDefn = defn.getSlot(i);
				SlotHandle slotHandle = designElementHandle.getSlot(slotDefn.getSlotID());
				List<SearchPathMember> slotPath = new ArrayList<>(path);
				slotPath.add(new SlotSearchPathMember(slotHandle));
				for (int j = 0, count = slotHandle.getCount(); j < count; j++) {
					DesignElementHandle child = slotHandle.get(j);
					List<SearchPathMember> childPath = new ArrayList<>(slotPath);
					search(child, search, childPath, searchResults);
				}
			}
		}
	}
}
