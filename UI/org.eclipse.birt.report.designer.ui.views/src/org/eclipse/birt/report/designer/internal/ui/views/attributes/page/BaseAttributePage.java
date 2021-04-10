/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import java.util.HashMap;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.AccordionPropertyList;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.IPropertyList;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.Tab;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.TabbedPropertyList;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.TabbedPropertyTitle;
import org.eclipse.birt.report.designer.internal.ui.util.SortMap;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.ICategoryPage;
import org.eclipse.birt.report.designer.ui.views.attributes.TabPage;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * The container page of AttributePages.
 */
public class BaseAttributePage extends TabPage {

	/**
	 * The list control contains categories of a DE element attributes.
	 */
	private IPropertyList categoryList;

	/**
	 * Container that material category attribute page will reside in.
	 */
	private Composite infoPane;

	/**
	 * ICategoryProvider instance, responds to the selection changing in the
	 * categoryList.
	 */
	private ICategoryProvider categoryProvider;

	/**
	 * The Last Selected index in the list of categories
	 */
	private static int s_lastSelectedIndex = 0;

	private static String s_lastSelectedKey = null;

	/**
	 * The current selection.
	 */
	private Object input;

	/**
	 * The map keeps the relationships between category label & pane
	 */
	private HashMap pageMap;

	private ScrolledComposite sComposite;

	private TabPage currentPage = null;

	private TabbedPropertyTitle title;

	/**
	 * Creates UI control.
	 * 
	 */
	Composite container;

	private int style;

	public BaseAttributePage() {
		this.style = SWT.HORIZONTAL;
	}

	public BaseAttributePage(int style) {
		this.style = style;
	}

	public void buildUI(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = 2;
		container.setLayout(layout);

		if (style == SWT.HORIZONTAL)
			createHorizontalView();
		else
			createVerticalView();

		container.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				if (pageMap != null) {
					for (Object value : pageMap.values()) {
						TabPage page = (TabPage) value;
						if (page != null)
							page.dispose();
					}
				}

			}
		});
	}

	protected void createVerticalView() {
		categoryList = new AccordionPropertyList(container);
		GridData gd = new GridData(GridData.FILL_BOTH);
		categoryList.getControl().setLayoutData(gd);
		categoryList.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (categoryList.getSelectionIndex() > -1) {
					BaseAttributePage.s_lastSelectedIndex = categoryList.getSelectionIndex();
					BaseAttributePage.s_lastSelectedKey = categoryList.getSelectionKey();
				}
				processListSelected();

			}
		});
		setCategoryProvider(categoryProvider);
	}

	protected void createHorizontalView() {
		categoryList = new TabbedPropertyList(container);
		GridData gd = new GridData(GridData.FILL_VERTICAL);
		gd.verticalSpan = 2;
		categoryList.getControl().setLayoutData(gd);
		categoryList.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (categoryList.getSelectionIndex() > -1) {
					BaseAttributePage.s_lastSelectedIndex = categoryList.getSelectionIndex();
					BaseAttributePage.s_lastSelectedKey = categoryList.getSelectionKey();
				}
				processListSelected();

			}
		});

		setCategoryProvider(categoryProvider);
		title = new TabbedPropertyTitle(container, FormWidgetFactory.getInstance());
		title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		title.addListener(SWT.SELECTED, new Listener() {

			public void handleEvent(Event event) {
				if (currentPage != null && currentPage instanceof ResetAttributePage) {
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(Messages.getString("BaseAttributePage.CommandStack.ResetStyle.Message")); //$NON-NLS-1$

					((ResetAttributePage) currentPage).reset();

					stack.commit();
				}
			}
		});

		sComposite = new ScrolledComposite(container, SWT.H_SCROLL | SWT.V_SCROLL);
		sComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		sComposite.setExpandHorizontal(true);
		sComposite.setExpandVertical(true);
		sComposite.addControlListener(new ControlAdapter() {

			public void controlResized(ControlEvent e) {
				computeSize();
			}
		});

		infoPane = new Composite(sComposite, SWT.NONE);
		sComposite.setContent(infoPane);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		infoPane.setLayout(layout);
		container.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				if (pageMap != null) {
					for (Object value : pageMap.values()) {
						TabPage page = (TabPage) value;
						if (page != null)
							page.dispose();
					}
				}

			}
		});
	}

	private void computeSize() {
		if (style == SWT.HORIZONTAL) {
			sComposite.setMinSize(infoPane.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			infoPane.layout();
		}
	}

	private TabPage oldPage = null;

	/**
	 * When selection changed in the category list, re-sets the top-level category
	 * pane correspond to the current selected category.
	 */
	private void processListSelected() {
		if (categoryProvider == null) {
			return;
		}
		int index = categoryList.getSelectionIndex();
		if (index == -1) {
			return;
		}
		if (style == SWT.HORIZONTAL) {
			title.setTitle(getCategoryTitle(categoryList.getSelectionIndex()), null);
		}
		TabPage page = getCategoryPane(categoryList.getSelectionIndex());
		if (page == null) {
			return;
		}

		if (oldPage != page) {
			if (style == SWT.HORIZONTAL) {
				if (page instanceof AttributePage) {
					((AttributePage) page).addPropertyChangeListener(title);
					Object adapter = ((AttributePage) page).getAdapter(IAction.class);
					if (adapter instanceof IAction[])
						title.setActions((IAction[]) adapter);
					else
						title.setActions(null);
				} else
					title.setActions(null);
			}
			showPage(page);
			oldPage = page;
		}
	}

	private void showPage(TabPage page) {
		if (page != currentPage) {
			if (currentPage != null) {
				((GridData) currentPage.getControl().getLayoutData()).exclude = true;
				currentPage.getControl().setVisible(false);
			}
			((GridData) page.getControl().getLayoutData()).exclude = false;
			page.getControl().setVisible(true);
			currentPage = page;
			computeSize();
		}
	}

	protected void selectStickyCategory() {
		if (s_lastSelectedKey != null && s_lastSelectedIndex != -1) {
			categoryList.setSelection(s_lastSelectedKey, s_lastSelectedIndex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.attributes.component.TabPage#setInput
	 * (java.util.List)
	 */
	public void setInput(Object input) {
		this.input = input;
	}

	public void refresh() {
		selectStickyCategory();
		processListSelected();
	}

	/**
	 * Sets CategoryProvider
	 * 
	 * @param categoryProvider The categoryListener to set.
	 */
	public void setCategoryProvider(ICategoryProvider categoryProvider) {
		this.categoryProvider = categoryProvider;
		if (categoryProvider == null) {
			return;
		}
		if (categoryList == null)
			return;
		ICategoryPage[] pages = categoryProvider.getCategories();
		if (pages.length != 0) {
			SortMap categoryLabels = new SortMap();
			for (int i = 0; i < pages.length; i++) {
				Tab tab = new Tab();
				tab.setText(pages[i].getDisplayLabel());
				categoryLabels.put(pages[i].getCategoryKey(), tab);
			}
			categoryList.setElements(categoryLabels);
			if (((Composite) categoryList.getControl()).getTabList().length > 0) {
				categoryList.setSelection(null, 0);
			}
		}
	}

	private TabPage getCategoryPane(int index) {
		if (pageMap == null) {
			pageMap = new HashMap(categoryProvider.getCategories().length);
		}
		String key = Integer.toString(index);
		TabPage page = (TabPage) pageMap.get(key);
		if (page == null) {
			page = categoryProvider.getCategories()[index].createPage();
			page.setInput(input);
			if (this.style == SWT.HORIZONTAL) {
				page.buildUI(infoPane);
			} else {
				infoPane = (Composite) categoryList.getItem(index).getData();
				GridLayout layout = new GridLayout();
				layout.marginWidth = 0;
				layout.marginHeight = 0;
				infoPane.setLayout(layout);
				page.buildUI(infoPane);

				FormData fd = (FormData) infoPane.getLayoutData();
				int height = infoPane.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
				if (height > 0)
					fd.height = height;
				infoPane.layout();
			}
		} else
			page.setInput(input);
		page.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		page.refresh();
		pageMap.put(key, page);
		return page;
	}

	private String getCategoryTitle(int index) {
		ICategoryPage page = categoryProvider.getCategories()[index];
		String title = null;
		if (page instanceof CategoryPage) {
			title = ((CategoryPage) page).getDisplayTitle();
		}
		if (title == null)
			title = page.getDisplayLabel();
		return title;
	}

	public void dispose() {
		container.dispose();
	}

	public Control getControl() {
		return container;
	}

}