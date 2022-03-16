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

package org.eclipse.birt.report.designer.internal.ui.extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.editors.extension.IExtensionConstants;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * The ExtensionPoinyManager is utility class to retrieve IExtendedElementUI
 * extensions by model extension ID, or full list.It caches the information to
 * avoid reading the extensions each time.
 */
public class EditorContributorManager implements IExtensionConstants {

	public static class EditorContributor {

		public String targetEditorId;
		public List formPageList;

		public boolean merge(EditorContributor contributor) {
			assert targetEditorId != null;

			boolean changed = false;

			if (targetEditorId.equals(contributor.targetEditorId)) {
				boolean needResort = false;

				for (Iterator itor = contributor.formPageList.iterator(); itor.hasNext();) {
					FormPageDef incomingPage = (FormPageDef) itor.next();
					FormPageDef exsitPage = getPage(incomingPage.id);
					if (exsitPage == null) {
						formPageList.add(incomingPage);
						needResort = true;
						changed = true;
					} else if (exsitPage.priority <= incomingPage.priority) {
						int index = formPageList.indexOf(exsitPage);
						formPageList.set(index, incomingPage);
						changed = true;
					}
				}

				if (needResort) {
					formPageList = sortFormPageList(formPageList);
				}
			}
			return changed;
		}

		public FormPageDef getPage(int index) {
			return (FormPageDef) formPageList.get(index);
		}

		public FormPageDef getPage(String id) {
			for (Iterator itor = formPageList.iterator(); itor.hasNext();) {
				FormPageDef page = (FormPageDef) itor.next();
				if (page.id.equals(id)) {
					return page;
				}
			}
			return null;
		}
	}

	private volatile HashMap editorContributorMap;

	private volatile static EditorContributorManager instance = null;

	private EditorContributorManager() {
	}

	public static EditorContributorManager getInstance() {
		if (instance == null) {
			synchronized (ExtensionPointManager.class) {
				if (instance == null) {
					instance = new EditorContributorManager();
				}
			}
		}
		return instance;
	}

	/**
	 * Gets the list of all the extended element points.
	 *
	 * @return Returns the list of all the extended element point
	 *         (ExtendedElementUIPoint).
	 */
	public EditorContributor[] getEditorContributors() {
		synchronized (this) {
			if (editorContributorMap == null) {
				createEditorContributorMap();
				if (editorContributorMap == null) {
					return null;
				}
			}
		}
		return (EditorContributor[]) editorContributorMap.values()
				.toArray(new EditorContributor[editorContributorMap.size()]);
	}

	public EditorContributor getEditorContributor(String targetEditorId) {
		assert targetEditorId != null;

		synchronized (this) {
			if (editorContributorMap == null) {
				createEditorContributorMap();
				if (editorContributorMap == null) {
					return null;
				}
			}
		}

		return (EditorContributor) editorContributorMap.get(targetEditorId);
	}

	/**
	 * Gets the extended element point with the specified extension name.
	 *
	 * @param extensionName the extension name of the extended element
	 *
	 * @return Returns the extended element point, or null if any problem exists
	 */
	public FormPageDef getFormPageDef(String targetEditorId, String pageId) {
		assert targetEditorId != null;
		assert pageId != null;

		int index = findFormPageIndex(targetEditorId, pageId);
		if (index != -1) {
			return getFormPageDef(targetEditorId, index);
		}
		return null;
	}

	public int findFormPageIndex(String targetEditorId, String pageId) {
		assert targetEditorId != null;
		assert pageId != null;

		EditorContributor editorContributor = getEditorContributor(targetEditorId);
		if (editorContributor != null) {
			List formPageDefList = editorContributor.formPageList;
			if (formPageDefList != null) {
				for (int i = 0; i < formPageDefList.size(); i++) {
					FormPageDef formPageDef = (FormPageDef) formPageDefList.get(i);
					if (formPageDef != null && pageId.equals(formPageDef.id)) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Gets the extended element point with the specified extension name.
	 *
	 * @param extensionName the extension name of the extended element
	 *
	 * @return Returns the extended element point, or null if any problem exists
	 */
	public FormPageDef getFormPageDef(String targetEditorId, int index) {
		assert targetEditorId != null;

		EditorContributor editorContributor = getEditorContributor(targetEditorId);
		if (editorContributor != null && editorContributor.formPageList != null && index >= 0
				&& index < editorContributor.formPageList.size()) {
			return (FormPageDef) editorContributor.formPageList.get(index);
		}
		return null;
	}

	private void createEditorContributorMap() {
		synchronized (this) {
			editorContributorMap = new HashMap();
			for (Iterator iter = getExtensionElements(EXTENSION_MULTIPAGE_EDITOR_CONTRIBUTOR).iterator(); iter
					.hasNext();) {
				IExtension extension = (IExtension) iter.next();
				IConfigurationElement[] elements = extension.getConfigurationElements();
				for (int i = 0; i < elements.length; i++) {
					EditorContributor editorContributor = createEditorContributor(elements[i]);
					if (!editorContributorMap.containsKey(editorContributor.targetEditorId)) {
						editorContributorMap.put(editorContributor.targetEditorId, editorContributor);
					} else {
						EditorContributor exsitContributor = (EditorContributor) editorContributorMap
								.get(editorContributor.targetEditorId);
						exsitContributor.merge(editorContributor);
					}
				}
			}
		}
	}

	private EditorContributor createEditorContributor(IConfigurationElement element) {
		EditorContributor editorContributor = new EditorContributor();
		editorContributor.targetEditorId = loadStringAttribute(element, ATTRIBUTE_TARGET_EDITOR_ID);
		editorContributor.formPageList = sortFormPageList(createFormPageDefList(element));
		return editorContributor;
	}

	/**
	 * Sort all form pages in the relative order.
	 *
	 * @param formPageList
	 * @return
	 */
	private static List sortFormPageList(List formPageList) {
		// get a copy of original list
		List olist = new ArrayList(formPageList);
		// create the result list
		List rlist = new ArrayList(olist.size());

		// create the temp list for unresolved elements
		List unlist = new ArrayList();

		boolean resolved = true;

		while (olist.size() > 0 && resolved) {
			resolved = false;

			// iterate original list
			for (int i = 0; i < olist.size(); i++) {
				FormPageDef element = (FormPageDef) olist.get(i);

				if (element.relative == null) {
					// no relative element, just resovled.
					rlist.add(element);
					resolved = true;
				} else {
					// get relative index from resolved list first
					int relativePosition = getRelativeElementPosition(element, rlist);

					if (relativePosition == -1) {
						// get relative index from original list
						relativePosition = getRelativeElementPosition(element, olist);// get relatived page position

						if (relativePosition == -1) {
							// the relative elemnt if not in current list, just
							// append it as resolved.
							rlist.add(element);
							resolved = true;
						} else {
							// add to unresolved list
							unlist.add(element);
						}
					} else {
						// find relative element, resolve it
						rlist.add(relativePosition + element.position, element);
						resolved = true;
					}
				}
			}

			// clear resolved element, ensure unlist only contains unresolved
			// elements.
			unlist.removeAll(rlist);

			// scan unresolved list
			for (Iterator itr = unlist.iterator(); itr.hasNext();) {
				FormPageDef element = (FormPageDef) itr.next();

				// check relative element from resovled list
				int relativePosition = getRelativeElementPosition(element, rlist);

				if (relativePosition != -1) {
					// find relative element, resolve it and remove from
					// unresolved list
					rlist.add(relativePosition + element.position, element);
					itr.remove();
					resolved = true;
				}
			}

			// remove all resovled elements from original list
			olist.removeAll(rlist);
		}

		// ensure still pertain all unresolved elements
		rlist.addAll(unlist);

		return rlist;
	}

	private static int getRelativeElementPosition(FormPageDef element, List list) {
		String relativeId = element.relative;
		int i = 0;
		for (Iterator iter = list.iterator(); iter.hasNext(); i++) {
			FormPageDef formPage = (FormPageDef) iter.next();
			if (formPage.id.equals(relativeId)) {
				return list.indexOf(formPage);
			}
		}
		return -1;
	}

	private List createFormPageDefList(IConfigurationElement element) {
		ArrayList formPageDefList = new ArrayList();
		ArrayList keyList = new ArrayList();
		IConfigurationElement[] elements = element.getChildren();
		for (int i = 0; i < elements.length; i++) {
			FormPageDef formPageDef = new FormPageDef(elements[i]);
			if (!keyList.contains(formPageDef.id)) {
				formPageDefList.add(formPageDef);
				keyList.add(formPageDef.id);
			}
		}
		return formPageDefList;
	}

	private List getExtensionElements(String id) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry == null) {// extension registry cannot be resolved
			return Collections.EMPTY_LIST;
		}
		IExtensionPoint extensionPoint = registry.getExtensionPoint(id);
		if (extensionPoint == null) {// extension point cannot be resolved
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList(extensionPoint.getExtensions());
	}

	// /**
	// * @param newPoint
	// * the extension point instance
	// * @param element
	// * the configuration element
	// * @param className
	// * the name of the class attribute
	// */
	// private Object loadClass( IConfigurationElement element,
	// String attributeName )
	// {
	// Object clazz = null;
	// try
	// {
	//
	// clazz = element.createExecutableExtension( attributeName );
	// }
	// catch ( CoreException e )
	// {
	// ExceptionHandler.handle( e );
	// }
	// return clazz;
	// }

	// private ImageDescriptor getImageDescriptor( IConfigurationElement element
	// )
	// {
	// return getImageDescriptor( element, ATTRIBUTE_ICON );
	// }
	//
	// private ImageDescriptor getImageDescriptor( IConfigurationElement
	// element,
	// String attributeName )
	// {
	// Assert.isLegal( element != null );
	// IExtension extension = element.getDeclaringExtension( );
	// String iconPath = element.getAttribute( attributeName );
	// if ( iconPath == null )
	// {
	// return null;
	// }
	// URL path = Platform.getBundle( extension.getNamespace( ) )
	// .getEntry( "/" ); //$NON-NLS-1$
	// try
	// {
	// return ImageDescriptor.createFromURL( new URL( path, iconPath ) );
	// }
	// catch ( MalformedURLException e )
	// {
	// }
	// return null;
	// }

	private String loadStringAttribute(IConfigurationElement element, String attributeName) {
		return element.getAttribute(attributeName);
	}

	// private boolean loadBooleanAttribute( IConfigurationElement element,
	// String attributeName )
	// {
	// String value = element.getAttribute( attributeName );
	// if ( value != null )
	// {
	// return Boolean.valueOf( value ).booleanValue( );
	// }
	// return false;
	// }

	// private ImageDescriptor loadIconAttribute( IConfigurationElement element,
	// String attributeName, String key )
	// {
	// ImageDescriptor imageDescriptor = getImageDescriptor( element );
	// if ( imageDescriptor != null && key != null )
	// {
	// ReportPlatformUIImages.declareImage( key, imageDescriptor );
	// }
	// return imageDescriptor;
	// }
}
