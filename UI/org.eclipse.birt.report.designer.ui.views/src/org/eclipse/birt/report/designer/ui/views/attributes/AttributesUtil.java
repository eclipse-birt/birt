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

package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AdvancePropertyPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AlterPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BaseAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BookMarkExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BordersPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CategoryPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CellPaddingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CommentsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FontPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.HyperLinkPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ItemMarginPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.NamedExpressionsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.SectionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TOCExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.UserPropertiesPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.VisibilityPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Utility class to help creating standard attribute pages.
 */
public class AttributesUtil {

	/**
	 * Category name for standard Font page.
	 */
	public static final String FONT = CategoryProviderFactory.CATEGORY_KEY_FONT;
	/**
	 * Category name for standard Padding page.
	 */
	public static final String PADDING = CategoryProviderFactory.CATEGORY_KEY_PADDING;
	/**
	 * Category name for standard alt_text page.
	 */
	public static final String ALT = CategoryProviderFactory.CATEGORY_KEY_ALTTEXT;
	/**
	 * Category name for standard Border page.
	 */
	public static final String BORDER = CategoryProviderFactory.CATEGORY_KEY_BORDERS;
	/**
	 * Category name for standard Margin page.
	 */
	public static final String MARGIN = CategoryProviderFactory.CATEGORY_KEY_MARGIN;
	/**
	 * Category name for standard Hyperlink page.
	 */
	public static final String HYPERLINK = CategoryProviderFactory.CATEGORY_KEY_HYPERLINK;
	/**
	 * Category name for standard Section page.
	 */
	public static final String SECTION = CategoryProviderFactory.CATEGORY_KEY_SECTION;
	/**
	 * Category name for standard Visibility page.
	 */
	public static final String VISIBILITY = CategoryProviderFactory.CATEGORY_KEY_VISIBILITY;
	/**
	 * Category name for standard TOC page.
	 */
	public static final String TOC = CategoryProviderFactory.CATEGORY_KEY_TOC;
	/**
	 * Category name for standard Bookmark page.
	 */
	public static final String BOOKMARK = CategoryProviderFactory.CATEGORY_KEY_BOOKMARK;
	/**
	 * Category name for standard UserProperties page.
	 */
	public static final String USERPROPERTIES = CategoryProviderFactory.CATEGORY_KEY_USERPROPERTIES;
	/**
	 * Category name for standard NamedExpression page.
	 */
	public static final String NAMEDEXPRESSIONS = CategoryProviderFactory.CATEGORY_KEY_NAMEDEXPRESSIONS;
	/**
	 * Category name for standard Comments page.
	 */
	public static final String COMMENTS = CategoryProviderFactory.CATEGORY_KEY_COMMENTS;
	/**
	 * Category name for standard Advanced page.
	 */
	public static final String ADVANCEPROPERTY = CategoryProviderFactory.CATEGORY_KEY_ADVANCEPROPERTY;
	/**
	 * Category name for standard EventHandler page.
	 */
	public static final String EVENTHANDLER = "EventHandler"; //$NON-NLS-1$

	private static Map<String, String> categoryMap = new HashMap<>();
	private static Map<String, Class<?>> paneClassMap = new HashMap<>();

	static {
		addCategory(FONT, Messages.getString("GridPageGenerator.List.Font"), FontPage.class);//$NON-NLS-1$
		addCategory(PADDING, Messages.getString("DataPageGenerator.List.Padding"), CellPaddingPage.class); //$NON-NLS-1$
		addCategory(ALT, Messages.getString("ImagePageGenerator.List.AltText"), AlterPage.class); //$NON-NLS-1$
		addCategory(BORDER, Messages.getString("DataPageGenerator.List.Borders"), BordersPage.class); //$NON-NLS-1$
		addCategory(MARGIN, Messages.getString("DataPageGenerator.List.Margin"), ItemMarginPage.class); //$NON-NLS-1$
		addCategory(HYPERLINK, Messages.getString("DataPageGenerator.List.HyperLink"), HyperLinkPage.class); //$NON-NLS-1$
		addCategory(SECTION, Messages.getString("DataPageGenerator.List.Section"), SectionPage.class); //$NON-NLS-1$
		addCategory(VISIBILITY, Messages.getString("DataPageGenerator.List.Visibility"), VisibilityPage.class); //$NON-NLS-1$
		addCategory(TOC, Messages.getString("DataPageGenerator.List.TOC"), TOCExpressionPage.class); //$NON-NLS-1$
		addCategory(BOOKMARK, Messages.getString("DataPageGenerator.List.Bookmark"), BookMarkExpressionPage.class); //$NON-NLS-1$
		addCategory(USERPROPERTIES, Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
				UserPropertiesPage.class);
		addCategory(NAMEDEXPRESSIONS, Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
				NamedExpressionsPage.class);
		addCategory(COMMENTS, Messages.getString("ReportPageGenerator.List.Comments"), CommentsPage.class); //$NON-NLS-1$
		addCategory(ADVANCEPROPERTY, Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
				AdvancePropertyPage.class);
	}

	private AttributesUtil() {
		// no instantiation
	}

	/**
	 * Add default category for Attribute Page
	 *
	 * @param id
	 * @param displayLabelKey
	 * @param pageClass
	 */
	public static void addCategory(String id, String displayLabelKey, Class<?> pageClass) {
		assert id != null;
		assert pageClass != null;

		categoryMap.put(id, displayLabelKey);
		paneClassMap.put(id, pageClass);
	}

	/**
	 * Creates the standard categoried property page.
	 *
	 * @param parent
	 * @param categories         A standard category id array, the contained value
	 *                           must be the category name constants defined within
	 *                           the AttributeUtil class, such as
	 *                           AttributesUtil.FONT, AttributesUtil.MARGIN. A null
	 *                           value means reserve this position for custom pages.
	 * @param customKeys         A custom key
	 * @param customCategories   A custom categories name array.
	 * @param customPageWrappers A custom page wrapper array.
	 * @param input
	 * @return Page object.
	 */
	public static IPropertyTabUI buildGeneralPage(Composite parent, String[] categories, String[] customKeys,
			String[] customCategories, PageWrapper[] customPageWrappers, Object input) {
		BaseAttributePage basicPage = new BaseAttributePage();
		LinkedHashMap<String, String> categorys = new LinkedHashMap<>();
		List<Object> paneClassList = new ArrayList<>();

		if (categories == null) {
			if (customCategories != null && customPageWrappers != null) {
				for (int i = 0; i < customCategories.length; i++) {
					categorys.put(customKeys[i], customCategories[i]);
					paneClassList.add(customPageWrappers[i]);
				}
			}
		} else {
			int customIndex = 0;

			for (int i = 0; i < categories.length; i++) {
				if (categories[i] == null) {
					if (customCategories != null && customPageWrappers != null
							&& customCategories.length > customIndex) {
						categorys.put(customKeys[customIndex], customCategories[customIndex]);
						paneClassList.add(customPageWrappers[customIndex]);
						customIndex++;
					}
				} else {
					Object cat = categoryMap.get(categories[i]);

					if (cat instanceof String) {
						categorys.put(categories[i], (String) cat);
						paneClassList.add(paneClassMap.get(categories[i]));
					}
				}
			}

			if (customCategories != null && customPageWrappers != null && customCategories.length > customIndex) {
				for (int i = customIndex; i < customCategories.length; i++) {
					categorys.put(customKeys[customIndex], customCategories[customIndex]);
					paneClassList.add(customPageWrappers[customIndex]);
					customIndex++;
				}
			}
		}

		Object[] clss = paneClassList.toArray(new Object[0]);

		basicPage.setCategoryProvider(new ExtendedCategoryProvider(categorys, clss));

		basicPage.setInput(input);
		basicPage.buildUI(parent);

		return basicPage;
	}

	/**
	 * Returns standard category page display name.
	 *
	 * @return Returns standard category page display name.
	 */
	public static String getGeneralPageDisplayName() {
		return Messages.getString("CategoryPageGenerator.TabItem.Attributes"); //$NON-NLS-1$
	}

	/**
	 * Creates the standard data binding page.
	 *
	 * @param parent Parent composite.
	 * @param input  page input
	 * @return Page object.
	 */
	public static IPropertyTabUI buildBindingPage(Composite parent, Object input) {
		GridLayout gl = new GridLayout();
		parent.setLayout(gl);
		BindingPage page = new BindingPage();
		page.setInput(input);
		page.buildUI(parent);
		return page;
	}

	/**
	 * Returns standard data binding page display name.
	 *
	 * @return Returns standard data binding page display name.
	 */
	public static String getBindingPageDisplayName() {
		return Messages.getString("TablePageGenerator.TabItem.Binding"); //$NON-NLS-1$
	}

	/**
	 * Creates standard data filter page.
	 *
	 * @param parent Parent composite.
	 * @param input  page input
	 * @return Page object.
	 */
	public static IPropertyTabUI buildFilterPage(Composite parent, Object input) {
		GridLayout gl = new GridLayout();
		parent.setLayout(gl);
		FormPage page = new FormPage(FormPropertyDescriptor.FULL_FUNCTION, new FilterHandleProvider(), true, true);
		page.setInput(input);
		page.buildUI(parent);

		return page;
	}

	/**
	 * Returns standard data filter page display name.
	 *
	 * @return Returns standard data filter page display name.
	 */
	public static String getFilterPageDisplayName() {
		return Messages.getString("TablePageGenerator.TabItem.Filters"); //$NON-NLS-1$
	}

	/**
	 * Creates standard data filter page.
	 *
	 * @param parent Parent composite.
	 * @param input  Page input
	 * @return Page object.
	 */
	public static IPropertyTabUI buildHighlightPage(Composite parent, Object input) {
		GridLayout gl = new GridLayout();
		parent.setLayout(gl);
		PreviewPage page = new PreviewPage(true);
		page.setPreview(new HighlightPropertyDescriptor(true));
		page.setProvider(new HighlightDescriptorProvider());
		page.setInput(input);
		page.buildUI(parent);

		return page;
	}

	/**
	 * Returns standard data filter page display name.
	 *
	 * @return Returns standard data filter page display name.
	 */
	public static String getHighlightPageDisplayName() {
		return Messages.getString("TablePageGenerator.TabItem.Highlights"); //$NON-NLS-1$
	}

	/**
	 * Sets input to the page.
	 *
	 * @param page  This must be the result object returned by
	 *              AttributesUtil.buildXXXPage().
	 * @param input input objects.
	 *
	 * @deprecated should not be used anymore
	 */
	@Deprecated
	public static void setPageInput(IPropertyTabUI page, Object input) {
		page.setInput(input);
		if (page instanceof TabPage && page.getControl() != null) {
			((TabPage) page).refresh();
		}
	}

	/**
	 * Convenient method to handle property page exceptions.
	 *
	 * @param e
	 *
	 * @deprecated see {@link ExceptionUtil}
	 */
	@Deprecated
	public static void handleError(Throwable e) {
		ExceptionUtil.handle(e);
	}

	/**
	 * ExtendedCategoryProvider
	 */
	static class ExtendedCategoryProvider implements ICategoryProvider {

		private LinkedHashMap<String, String> categorieLabels;
		private LinkedHashMap<String, String> categorieTitles;
		private Object[] paneObjects;

		ExtendedCategoryProvider(LinkedHashMap<String, String> categorieLabels,
				LinkedHashMap<String, String> categorieTitles, Object[] paneObjects) {
			assert categorieLabels != null;
			assert paneObjects != null;
			assert categorieLabels.size() == paneObjects.length;
			assert categorieTitles.size() == paneObjects.length;
			this.categorieLabels = categorieLabels;
			this.categorieTitles = categorieTitles;
			this.paneObjects = paneObjects;
		}

		ExtendedCategoryProvider(LinkedHashMap<String, String> categorieLabels, Object[] paneObjects) {
			assert categorieLabels != null;
			assert paneObjects != null;
			assert categorieLabels.size() == paneObjects.length;
			this.categorieLabels = categorieLabels;
			this.categorieTitles = categorieLabels;
			this.paneObjects = paneObjects;
		}

		@Override
		public ICategoryPage[] getCategories() {
			List<ICategoryPage> pageList = new ArrayList<>(paneObjects.length);

			int i = 0;
			for (Iterator<Entry<String, String>> itr = categorieLabels.entrySet().iterator(); itr.hasNext(); i++) {
				Entry<String, String> entry = itr.next();
				final String categoryKey = entry.getKey();
				final String displayLabel = entry.getValue();
				String displayTitle = categorieTitles.get(categoryKey);
				if (displayTitle == null) {
					displayTitle = displayLabel;
				}

				Object pane = paneObjects[i];

				if (pane instanceof Class && PageWrapper.class.isAssignableFrom((Class<?>) pane)) {
					try {
						pane = ((Class) pane).getConstructor((Class[]) null).newInstance((Object[]) null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (pane instanceof Class) {
					pageList.add(new CategoryPage(categoryKey, displayLabel, displayTitle, (Class<?>) pane));
				} else if (pane instanceof PageWrapper) {
					final PageWrapper wrapper = (PageWrapper) pane;

					pageList.add(new ICategoryPage() {

						@Override
						public String getDisplayLabel() {
							return displayLabel;
						}

						@Override
						public TabPage createPage() {
							return wrapper.getPage();
						}

						@Override
						public String getCategoryKey() {
							return categoryKey;
						}
					});
				}
			}
			return pageList.toArray(new ICategoryPage[pageList.size()]);
		}
	}

	/**
	 * This class wraps the custom page content and communicates with internal
	 * pages. It also provide the capability to integrate the PropertyProcessor
	 * mechanism with user customized pages.
	 */
	public abstract static class PageWrapper implements IModelEventProcessor {

		private AttributePage page;

		/**
		 * Internal used to return the wrapped page.
		 *
		 * @return Return the wrapped page.
		 */
		public TabPage getPage() {
			if (page == null) {
				page = new AttributePage() {

					@Override
					public void buildUI(Composite parent) {
						super.buildUI(parent);
						PageWrapper.this.buildUI(parent);
					}

					@Override
					public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {
						PageWrapper.this.addElementEvent(focus, ev);
					}

					@Override
					public void clear() {
						PageWrapper.this.clear();
					}

					@Override
					public void postElementEvent() {
						PageWrapper.this.postElementEvent();
					}

					@Override
					public Object getAdapter(Class adapter) {
						return PageWrapper.this.getAdapter(adapter);
					}

					@Override
					public void refresh() {
						PageWrapper.this.refresh();
						super.refresh();
					}

					@Override
					public void setInput(Object elements) {
						super.setInput(elements);
						PageWrapper.this.setInput(elements);
					}

					@Override
					public void dispose() {
						PageWrapper.this.dispose();
						super.dispose();
					}

				};
			}
			return page;
		}

		@Override
		public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {

		}

		@Override
		public void clear() {

		}

		@Override
		public void postElementEvent() {

		}

		@Override
		public Object getAdapter(Class adapter) {
			return null;
		}

		/**
		 * Creates property page user content.
		 *
		 * @param parent      property UI parent.
		 */
		public abstract void buildUI(Composite parent);

		/**
		 * Sets page input.
		 *
		 * @param input input object
		 */
		public void setInput(Object input) {
			// default doing nothing;
		}

		/**
		 * Refresh
		 */
		public void refresh() {
			// default doing nothing;
		}

		/**
		 * Notifies if parent UI disposed.
		 */
		public void dispose() {
			// default doing nothing.
		}

	}

	/**
	 * Creates the category provider for attribute page.
	 *
	 * @param categories        An array of category keys, the contained value must
	 *                          be the category key constants defined within the
	 *                          AttributeUtil class, such as AttributesUtil.FONT,
	 *                          AttributesUtil.MARGIN. The array elememt can be
	 *                          null, which means this position is reserved for
	 *                          custom category pages.
	 * @param customKeys        An array of custom category page key name.
	 * @param customLabels      An array of custom category page display labels.
	 * @param customPageClasses An array of custom category page class, each class
	 *                          should extend from either
	 *                          <code>{@link TabPage}</code> class or
	 *                          <code>{@link PageWrapper}</code> class and have a
	 *                          public non-argument constructor.
	 * @return Returns a new category provider instance
	 *
	 * @since 2.5
	 */
	public static ICategoryProvider createCategoryProvider(String[] categories, String[] customKeys,
			String[] customLabels, Class<?>[] customPageClasses) {
		return createCategoryProvider(categories, customKeys, customLabels, null, customPageClasses);
	}

	/**
	 * Creates the category provider for attribute page.
	 *
	 * @param categories        An array of category keys, the contained value must
	 *                          be the category key constants defined within the
	 *                          AttributeUtil class, such as AttributesUtil.FONT,
	 *                          AttributesUtil.MARGIN. The array elememt can be
	 *                          null, which means this position is reserved for
	 *                          custom category pages.
	 * @param customKeys        An array of custom category page key name.
	 * @param customLabels      An array of custom category page display labels.
	 * @param customTitles
	 * @param customPageClasses An array of custom category page class, each class
	 *                          should extend from either
	 *                          <code>{@link TabPage}</code> class or
	 *                          <code>{@link PageWrapper}</code> class and have a
	 *                          public non-argument constructor.
	 * @return Returns a new category provider instance
	 */
	public static ICategoryProvider createCategoryProvider(String[] categories, String[] customKeys,
			String[] customLabels, String[] customTitles, Class<?>[] customPageClasses) {
		LinkedHashMap<String, String> categoryLabels = new LinkedHashMap<>();
		LinkedHashMap<String, String> categoryTitles = new LinkedHashMap<>();
		List<Class<?>> paneClassList = new ArrayList<>();

		if (categories == null) {
			// use only custom categories
			if (customKeys != null && customPageClasses != null) {
				for (int i = 0; i < customKeys.length; i++) {
					if (customLabels != null) {
						categoryLabels.put(customKeys[i], customLabels[i]);
					}
					if (customTitles != null) {
						categoryTitles.put(customKeys[i], customTitles[i]);
					}
					paneClassList.add(customPageClasses[i]);
				}
			}
		} else {
			int currentCustomIndex = 0;

			for (int i = 0; i < categories.length; i++) {
				if (categories[i] == null) {
					if (customKeys != null && customPageClasses != null && customKeys.length > currentCustomIndex) {
						if (customLabels != null) {
							categoryLabels.put(customKeys[currentCustomIndex], customLabels[currentCustomIndex]);
						}
						if (customTitles != null) {
							categoryTitles.put(customKeys[currentCustomIndex], customTitles[currentCustomIndex]);
						}
						paneClassList.add(customPageClasses[currentCustomIndex]);
						currentCustomIndex++;
					}
				} else {
					Object cat = categoryMap.get(categories[i]);

					if (cat instanceof String) {
						categoryLabels.put(categories[i], (String) cat);
						categoryTitles.put(categories[i], (String) cat);
						paneClassList.add(paneClassMap.get(categories[i]));
					}
				}
			}

			if (customKeys != null && customPageClasses != null && customKeys.length > currentCustomIndex) {
				for (int i = currentCustomIndex; i < customKeys.length; i++) {
					if (customLabels != null) {
						categoryLabels.put(customKeys[i], customLabels[i]);
					}
					if (customTitles != null) {
						categoryTitles.put(customKeys[i], customTitles[i]);
					}
					paneClassList.add(customPageClasses[i]);
				}
			}
		}

		return new ExtendedCategoryProvider(categoryLabels, categoryTitles,
				paneClassList.toArray(new Object[paneClassList.size()]));
	}

	/**
	 * Checks if specified category already exists by default
	 *
	 * @param categoryId
	 * @return Return the check result if specified category already exists by
	 *         default
	 */
	public static boolean containCategory(String categoryId) {
		return categoryMap.containsKey(categoryId);
	}

	/**
	 * Returns a new instance of specified category page.
	 *
	 * @param categoryId
	 * @return Return the category page.
	 */
	public static ICategoryPage getCategory(String categoryId) {
		if (containCategory(categoryId)) {
			return new CategoryPage(categoryId, categoryMap.get(categoryId), paneClassMap.get(categoryId));
		}
		return null;
	}

	/**
	 * Get the category display name
	 *
	 * @param categoryId category id
	 * @return Return the category display name
	 */
	public static String getCategoryDisplayName(String categoryId) {
		if (containCategory(categoryId)) {
			return categoryMap.get(categoryId);
		}
		return categoryId;
	}
}
