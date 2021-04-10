/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.templates.IDynamicTemplateProvider;
import org.eclipse.birt.report.designer.ui.templates.ITemplateAdaptable;
import org.eclipse.birt.report.designer.ui.templates.ITemplateEntry;
import org.eclipse.birt.report.designer.ui.templates.ITemplateFile;
import org.eclipse.birt.report.designer.ui.templates.ITemplateFolder;
import org.eclipse.birt.report.designer.ui.templates.ITemplateProvider;
import org.eclipse.birt.report.designer.ui.templates.IDynamicTemplateProvider.Callback;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * ExtensionTemplateListProvider
 */
public class ExtensionTemplateListProvider implements ILabelProvider, ITreeContentProvider, ITemplateAdaptable {

	private List<ITemplateProvider> providers;
	private List<TemplateNode> list = new ArrayList<TemplateNode>();
	private Map<String, TemplateNode> map = new HashMap<String, TemplateNode>();

	private volatile TemplateUICallback uiCallback;

	private Callback providerCallback = new Callback() {

		public void contentChanged(IDynamicTemplateProvider who) {
			if (uiCallback != null) {
				synchronized (uiCallback) {
					Display disp = Display.getCurrent();
					if (disp == null) {
						disp = Display.getDefault();
					}

					disp.syncExec(new Runnable() {

						public void run() {
							list.clear();
							map.clear();

							buildList();

							uiCallback.contentChanged();
						}
					});
				}
			}
		}
	};

	public ExtensionTemplateListProvider(TemplateUICallback uiCallback) {
		this.uiCallback = uiCallback;

		providers = new ArrayList<ITemplateProvider>();

		Object[] objs = getTemplateProviders();

		if (objs != null) {
			for (int i = 0; i < objs.length; i++) {
				if (objs[i] instanceof ITemplateProvider) {
					providers.add((ITemplateProvider) objs[i]);
				}
			}
		}

		buildList();
	}

	private void buildList() {
		for (int i = 0; i < providers.size(); i++) {
			build(providers.get(i));
		}

		unionOneOrder(list);
		for (int i = 0; i < list.size(); i++) {
			unionSameOrder(list.get(i));
		}
	}

	private void build(ITemplateProvider provider) {
		if (provider instanceof IDynamicTemplateProvider) {
			((IDynamicTemplateProvider) provider).init(providerCallback);
		}

		ITemplateEntry[] entrys = provider.getTemplates();

		if (entrys == null) {
			return;
		}

		String id = provider.getParentBaseName();
		TemplateNode other = map.get(id);

		for (int i = 0; i < entrys.length; i++) {
			ITemplateEntry entry = entrys[i];

			TemplateNode first;
			try {
				first = addNodes(null, entry);
			} catch (InvalidIDException e) {
				continue;
			}

			if (other != null) {
				union(other, first);
			} else {
				list.add(first);
			}
		}

	}

	private TemplateNode addNodes(TemplateNode parent, ITemplateEntry entry) throws InvalidIDException {
		TemplateNode current = new TemplateNode(parent, entry.getName(), entry.getImage());
		if (entry instanceof ITemplateFile) {
			current.setHandle(((ITemplateFile) entry).getReportHandle());
		} else if (entry instanceof ITemplateFolder)

		{
			ITemplateFolder folder = (ITemplateFolder) entry;
			String folderName = folder.getBaseName();
			if (folderName == null) {
				throw new InvalidIDException("Invalid ID"); //$NON-NLS-1$
			}
			boolean isExist = false;
			current.setBaseName(folderName);
			TemplateNode node = map.get(folderName);
			if (node != null) {
				isExist = true;
				// throw new InvaliIDException( "Invalid ID" );
			}
			if (!isExist) {
				map.put(folderName, current);
			}

			ITemplateEntry[] children = folder.getChildren();

			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					try {
						addNodes(current, children[i]);
					} catch (InvalidIDException e) {
						throw e;
					}
				}
			}
		} else {
			// do nothing now
			// throw new RuntimeException("Don't support this type");
		}
		return current;
	}

	private void unionSameOrder(TemplateNode root) {
		List<TemplateNode> list = root.getChildren();

		unionOneOrder(list);

		for (int i = 0; i < list.size(); i++) {
			TemplateNode node = list.get(i);
			unionSameOrder(node);
		}
	}

	private void unionOneOrder(List<TemplateNode> list) {
		List<TemplateNode> temp = new ArrayList<TemplateNode>(list);
		Map<String, TemplateNode> names = new HashMap<String, TemplateNode>();
		for (int i = 0; i < temp.size(); i++) {
			TemplateNode node = temp.get(i);
			if (names.keySet().contains(node.getBaseName())) {
				TemplateNode parent = map.get(node.getBaseName());
				List<TemplateNode> children = node.getChildren();
				for (int j = 0; j < children.size(); j++) {
					TemplateNode child = children.get(j);
					child.setParent(parent);
					list.remove(node);
				}
			} else if (node.getBaseName() != null) {
				names.put(node.getBaseName(), node);
			}
		}
	}

	private void union(TemplateNode owner, TemplateNode folder) {
		// List<TemplateNode> list = folder.getChildren( );
		folder.setParent(owner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if (element instanceof TemplateNode) {
			return ((TemplateNode) element).getImage();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		if (element instanceof TemplateNode) {
			String str = ((TemplateNode) element).getName();
			if (str == null) {
				ReportDesignHandle handle = ((TemplateNode) element).getHandle();
				str = handle.getDisplayName();
			}
			str = Messages.getString(str);
			return str;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.
	 * jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
		// do nothing
	}

	public void dispose() {
		uiCallback = null;

		for (int i = 0; i < providers.size(); i++) {
			providers.get(i).release();
		}

		providers.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang
	 * .Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
		// do nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
	 * Object)
	 */
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof TemplateNode) {
			return ((TemplateNode) parentElement).getChildren().toArray();
		}
		return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object )
	 */
	public Object getParent(Object element) {
		if (element instanceof TemplateNode) {
			return ((TemplateNode) element).getParent();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing

	}

	/**
	 * @param element
	 * @return
	 */
	public ReportDesignHandle getReportDesignHandle(Object element) {
		if (element instanceof TemplateNode) {
			return ((TemplateNode) element).getHandle();
		}

		return null;
	}

	/**
	 * @return
	 */
	public Object[] getRootElements() {
		return list.toArray();
	}

	private static class TemplateNode {

		private TemplateNode parent;
		private List<TemplateNode> children = new ArrayList<TemplateNode>();
		private String name;
		private String baseName;
		private ReportDesignHandle handle;
		private Image image;

		TemplateNode(TemplateNode parent, String name, Image image) {
			super();
			if (parent != null && !parent.isLeaf()) {
				throw new RuntimeException("Add a node to the a no leaf node"); //$NON-NLS-1$
			}
			this.parent = parent;
			if (parent != null) {
				parent.addChild(this);
			}
			this.name = name;
			this.image = image;
		}

		public TemplateNode getParent() {
			return parent;
		}

		public String getName() {
			return name;
		}

		public String getBaseName() {
			return baseName;
		}

		public void setBaseName(String baseName) {
			this.baseName = baseName;
		}

		public ReportDesignHandle getHandle() {
			return handle;
		}

		public void setHandle(ReportDesignHandle handle) {
			this.handle = handle;
		}

		public Image getImage() {
			return image;
		}

		public boolean isRoot() {
			return parent == null;
		}

		void addChild(TemplateNode node) {
			children.add(node);
		}

		void removeChild(TemplateNode node) {
			children.remove(node);
		}

		public boolean isLeaf() {
			return handle == null && baseName != null;
		}

		public List<TemplateNode> getChildren() {
			return children;
		}

		public void setParent(TemplateNode parent) {
			this.parent = parent;
			if (parent != null) {
				parent.addChild(this);
			}
		}
	}

	private Object[] getTemplateProviders() {
		Object[] retValue = ElementAdapterManager.getAdapters(this, ITemplateProvider.class);
		if (retValue == null) {
			retValue = new ITemplateProvider[0];
		}
		return retValue;
	}

	private static class InvalidIDException extends Exception {

		private static final long serialVersionUID = 1L;

		InvalidIDException(String message) {
			super(message);
		}
	}

	// private void debug( )
	// {
	// for ( int i = 0; i < list.size( ); i++ )
	// {
	// TemplateNode node = list.get( i );
	// displayDebug( node, "" ); //$NON-NLS-1$
	// System.out.println( "///////////////////////////" ); //$NON-NLS-1$
	// }
	// }

	// private void displayDebug( TemplateNode node, String space )
	// {
	// if ( !node.isLeaf( ) )
	// {
	// System.out.println( space + "File == " + node.getName( ) ); //$NON-NLS-1$
	// }
	// else
	// {
	// System.out.println( space + "Folder == " + node.getName( ) ); //$NON-NLS-1$
	// List<TemplateNode> children = node.getChildren( );
	// space = space + " "; //$NON-NLS-1$
	// for ( int i = 0; i < children.size( ); i++ )
	// {
	// displayDebug( children.get( i ), space );
	// }
	// }
	// }

	/**
	 * TemplateUICallback
	 */
	static interface TemplateUICallback {

		void contentChanged();
	}

}
