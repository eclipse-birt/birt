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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IFastConsumerProcessor;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.util.SortMap;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.ui.views.attributes.TabPage;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * The sup-class of all attribute page, provides common register/unregister
 * implementation to DE model, and default refresh process after getting a
 * notify from DE.
 */
public abstract class AttributePage extends TabPage implements IFastConsumerProcessor {

	/**
	 * A default quick button height which if different in win32 from other OS.
	 */
	public static final int QUICK_BUTTON_HEIGHT = Platform.getOS().equals(Platform.OS_WIN32) ? 20 : 22;

	/**
	 * The list kept Property & PropertyDescriptor pair.
	 */
	protected HashMap propertiesMap = new HashMap(7);

	/**
	 * The current selection.
	 */
	protected Object input;

	public void refresh() {
		Object element = DEUtil.getInputFirstElement(input);
		if (element == null)
			return;
		if (element instanceof DesignElementHandle && getTopContainer((DesignElementHandle) element) == null) {
			return;
		}

		Section[] sectionArray = getSections();
		for (int i = 0; i < sectionArray.length; i++) {
			Section section = (Section) sectionArray[i];
			section.setInput(input);
			section.load();
		}
		FormWidgetFactory.getInstance().paintFormStyle(container);
		FormWidgetFactory.getInstance().adapt(container);
	}

	public void setInput(Object handle) {
		deRegisterEventManager();
		input = handle;
		registerEventManager();
	}

	/**
	 * Removes model change listener.
	 */
	protected void deRegisterEventManager() {
		if (UIUtil.getModelEventManager() != null)
			UIUtil.getModelEventManager().removeModelEventProcessor(this);
	}

	/**
	 * Registers model change listener to DE elements.
	 */
	protected void registerEventManager() {
		if (UIUtil.getModelEventManager() != null)
			UIUtil.getModelEventManager().addModelEventProcessor(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.event.
	 * IFastConsumerProcessor#isOverdued()
	 */
	public boolean isOverdued() {
		return container == null || container.isDisposed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#dispose()
	 */
	public void dispose() {
		if (container != null && !container.isDisposed()) {
			container.dispose();
		}
		listeners.clear();
		deRegisterEventManager();
	}

	protected SortMap sections = new SortMap();

	public void addSection(String sectionKey, Section section) {
		if (sections == null) {
			sections = new SortMap();
		}
		sections.put(sectionKey, section);
	}

	public void addSectionAfter(String sectionKey, Section section, String key) {
		if (sections == null) {
			sections = new SortMap();
		}
		int index = sections.getIndexOf(key);
		if (index != -1)
			sections.putAt(sectionKey, section, index + 1);
		else
			sections.put(sectionKey, section);
	}

	public void addSectionBefore(String sectionKey, Section section, String key) {
		if (sections == null) {
			sections = new SortMap();
		}
		int index = sections.getIndexOf(key);
		if (index != -1)
			sections.putAt(sectionKey, section, index);
		else
			sections.put(sectionKey, section);
	}

	public void removeSection(String sectionKey) {
		if (sections == null) {
			sections = new SortMap();
		}
		sections.remove(sectionKey);
	}

	/**
	 * Adjust the layout of the field editors so that they are properly aligned.
	 */

	public void createSections() {
		applyCustomSections();
		Section[] sectionArray = getSections();
		for (int i = 0; i < sectionArray.length; i++) {
			Section section = (Section) sectionArray[i];
			section.createSection();
		}
	}

	protected void applyCustomSections() {

	}

	public void layoutSections() {
		Section[] sectionArray = getSections();
		for (int i = 0; i < sectionArray.length; i++) {
			Section section = (Section) sectionArray[i];
			section.layout();
		}
		container.layout(true);
		container.redraw();
	}

	public Section[] getSections() {
		if (sections == null) {
			return new Section[0];
		}
		Section[] sectionArray = new Section[sections.size()];
		for (int i = 0; i < sections.size(); i++) {
			sectionArray[i] = (Section) sections.get(i);
		}
		return sectionArray;
	}

	public Section getSection(String key) {
		if (sections == null) {
			return null;
		}
		return (Section) sections.get(key);
	}

	public String getTabDisplayName() {
		return null;
	}

	protected Composite container;

	public void buildUI(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				deRegisterEventManager();
			}
		});
		if (sections == null)
			sections = new SortMap();
	}

	public Control getControl() {
		return container;
	}

	public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {

	}

	public void clear() {

	}

	private boolean load = false;

	public void postElementEvent() {
		Object element = DEUtil.getInputFirstElement(input);
		if (element == null)
			return;
		if (element instanceof DesignElementHandle && getTopContainer((DesignElementHandle) element) == null) {
			return;
		}

		Section[] sectionArray = getSections();
		for (int i = 0; i < sectionArray.length; i++)
			if (load == false) {
				load = true;

				Display.getDefault().timerExec(100, new Runnable() {

					public void run() {
						Section[] sectionArray = getSections();
						for (int i = 0; i < sectionArray.length; i++) {
							Section section = (Section) sectionArray[i];
							section.load();
						}
						load = false;
					}
				});
			}
	}

	protected ModuleHandle getTopContainer(DesignElementHandle element) {
		if (element instanceof ModuleHandle)
			return (ModuleHandle) element;
		while (!(element.getContainer() instanceof ModuleHandle)) {
			element = element.getContainer();
			if (element == null)
				return null;
		}
		return (ModuleHandle) element.getContainer();
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	protected List<IPropertyChangeListener> listeners = new ArrayList<IPropertyChangeListener>();

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}

}
