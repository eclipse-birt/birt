/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.core.model.views.outline.LibraryNode;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntryFilter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceFilter;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.DesignElementEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.EmbeddedImagesEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.LibraryNodeEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.PropertyEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportElementEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportResourceEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ResourceEntryWrapper;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.SlotEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.StructureEntry;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * LibraryExplorerProvider LibraryExplorer tree viewer label and content
 * provider adapter. this provider will list all library files in BIRT resource
 * folder.
 */
public class LibraryExplorerProvider extends ViewsTreeProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#
	 * getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ReportResourceEntry) {
			return getElementChildren(parentElement, ((ReportResourceEntry) parentElement).getReportElement());
		}

		if (parentElement instanceof ResourceEntryWrapper) {
			if (((ResourceEntryWrapper) parentElement).getType() == ResourceEntryWrapper.RPTDESIGN) {
				return new Object[] {};
			} else if (((ResourceEntryWrapper) parentElement).getType() == ResourceEntryWrapper.LIBRARY) {
				Object object = ((ResourceEntryWrapper) parentElement).getAdapter(LibraryHandle.class);
				return getElementChildren(parentElement, object);
			} else if (((ResourceEntryWrapper) parentElement).getType() == ResourceEntryWrapper.CSS_STYLE_SHEET) {
				Object object = ((ResourceEntryWrapper) parentElement).getAdapter(CssStyleSheetHandle.class);
				return getElementChildren(parentElement, object);
			}
		}

		if (parentElement instanceof ResourceEntry) {
			ResourceEntry[] children = ((ResourceEntry) parentElement).getChildren(new ResourceEntryFilter(
					(ResourceFilter[]) LibraryExplorerPlugin.getFilterMap().values().toArray(new ResourceFilter[0])));
			List childrenList = new ArrayList();
			for (int i = 0; i < children.length; i++) {
				if (children[i].getAdapter(ReportDesignHandle.class) != null) {
					childrenList.add(new ResourceEntryWrapper(ResourceEntryWrapper.RPTDESIGN, children[i]));
				} else if (children[i].getAdapter(LibraryHandle.class) != null) {
					childrenList.add(new ResourceEntryWrapper(ResourceEntryWrapper.LIBRARY, children[i]));
				} else if (children[i].getAdapter(CssStyleSheetHandle.class) != null) {
					childrenList.add(new ResourceEntryWrapper(ResourceEntryWrapper.CSS_STYLE_SHEET, children[i]));
				} else {
					childrenList.add(children[i]);
				}
			}
			return childrenList.toArray();
		}
		return super.getChildren(parentElement);
	}

	/**
	 * Returns all children in the specified parent.
	 * 
	 * @param parentElement the specified parent element.
	 * @param object        the specified parent entry.
	 * @return all children in the specified parent.
	 */
	protected Object[] getElementChildren(Object parentElement, Object object) {
		Object[] children = super.getChildren(object);
		List entryList = new ArrayList();
		int index = 0;

		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof DesignElementHandle) {
				entryList.add(new DesignElementEntry((DesignElementHandle) children[i], (ResourceEntry) parentElement));
			} else if (children[i] instanceof SlotHandle) {
				entryList.add(new SlotEntry((SlotHandle) children[i], (ResourceEntry) parentElement));
			} else if (children[i] instanceof EmbeddedImageNode) {
				entryList.add(new EmbeddedImagesEntry((EmbeddedImageNode) children[i], (ResourceEntry) parentElement));
			} else if (children[i] instanceof LibraryNode) {
				entryList.add(new LibraryNodeEntry((LibraryNode) children[i], (ResourceEntry) parentElement));
			} else if (children[i] instanceof PropertyHandle) {
				entryList.add(new PropertyEntry((PropertyHandle) children[i], (ResourceEntry) parentElement));
			} else if (children[i] instanceof StructureHandle) {
				entryList
						.add(new StructureEntry((StructureHandle) children[i], (ResourceEntry) parentElement, index++));
			} else {
				entryList.add(new ReportElementEntry(children[i], (ResourceEntry) parentElement));
			}
		}
		return entryList.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#
	 * getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if (element instanceof ResourceEntryWrapper) {
			if (((ResourceEntryWrapper) element).getType() == ResourceEntryWrapper.RPTDESIGN) {
				return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_REPORT_FILE);
			} else if (((ResourceEntryWrapper) element).getType() == ResourceEntryWrapper.LIBRARY) {
				Object object = ((ResourceEntryWrapper) element).getAdapter(LibraryHandle.class);
				return super.getImage(object);
			} else if (((ResourceEntryWrapper) element).getType() == ResourceEntryWrapper.CSS_STYLE_SHEET) {
				Object object = ((ResourceEntryWrapper) element).getAdapter(CssStyleSheetHandle.class);
				return super.getImage(object);
			}
		}

		if (element instanceof ResourceEntry) {
			return ((ResourceEntry) element).getImage();
		}
		return super.getImage(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#
	 * getText(java.lang.Object)
	 */
	public String getText(Object element) {
		if (element instanceof ResourceEntryWrapper
				&& ((ResourceEntryWrapper) element).getType() == ResourceEntryWrapper.RPTDESIGN) {
			String rptdesignPath = (String) ((ResourceEntryWrapper) element).getAdapter(ReportDesignHandle.class);
			File file = new File(rptdesignPath);
			if (file.exists())
				return new File(rptdesignPath).getName();
			else
				return rptdesignPath;
		} else if (element instanceof ResourceEntryWrapper
				&& ((ResourceEntryWrapper) element).getType() == ResourceEntryWrapper.LIBRARY) {
			LibraryHandle lib = (LibraryHandle) ((ResourceEntryWrapper) element).getAdapter(LibraryHandle.class);
			// fileName of the LibraryHandle is a relative path.
			String fileName = lib.getFileName();
			// fileName is a URL string.
			return new File(fileName).getName();
		} else if (element instanceof ResourceEntryWrapper
				&& ((ResourceEntryWrapper) element).getType() == ResourceEntryWrapper.CSS_STYLE_SHEET) {
			CssStyleSheetHandle css = (CssStyleSheetHandle) ((ResourceEntryWrapper) element)
					.getAdapter(CssStyleSheetHandle.class);
			String fileName = css.getFileName();
			// should be removed later -- begin ---
			if (fileName == null || fileName.length() == 0) {
				fileName = "base.css"; //$NON-NLS-1$
			}
			// should be removed later -- end ---
			return new File(fileName).getName();
		}

		if (element instanceof ResourceEntry) {
			if (!((ResourceEntry) element).isRoot()) {
				return ((ResourceEntry) element).getName();
			}
			if (element instanceof FragmentResourceEntry) {
				return Messages.getString("FragmentResourceEntry.RootDisplayName");//$NON-NLS-1$
			} else if (element instanceof PathResourceEntry) {
				return Messages.getString("PathResourceEntry.RootDisplayName");//$NON-NLS-1$
			}
		} else if (element instanceof String) {
			return element.toString();
		}
		return super.getText(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#
	 * hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof ResourceEntry) {
			return getChildren(element).length > 0;
		}
		return super.hasChildren(element);
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (oldInput instanceof Object[]) {
			Object[] array = (Object[]) oldInput;
			for (int i = 0; i < array.length; i++) {
				if (array[i] instanceof ResourceEntry)
					((ResourceEntry) array[i]).dispose();
			}
		}
		super.inputChanged(viewer, oldInput, newInput);
	}

	@Override
	public Color getForeground(Object resource) {
		Color gray = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);

		if (resource instanceof ResourceEntry) {
			ResourceEntry node = (ResourceEntry) resource;
			ResourceEntry parent = node.getParent();

			while (parent != null) {
				node = parent;
				parent = node.getParent();
			}

			if (node instanceof FragmentResourceEntry) {
				return gray;
			}
		}
		return super.getForeground(resource);
	}
}
