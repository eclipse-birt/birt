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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.core.LayoutModule;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.elements.strategy.DummyCopyPolicy;
import org.eclipse.birt.report.model.writer.LibraryWriter;
import org.eclipse.birt.report.model.writer.ModuleWriter;

/**
 * Represents the library module. The library is the container of reusable
 * report items , data sources, styles and so on. One library has its own
 * namespace, which is used to identify which library the element reference
 * refers.
 */

public class Library extends LayoutModule implements ILibraryModel {

	/**
	 * Namespace of the library.
	 */

	private String namespace;

	/**
	 * The host module which includes this module.
	 */

	protected LayoutModule host = null;

	/**
	 * Constructor for loading library from design file.
	 *
	 * @param theSession the session in which this library is involved
	 * @param host       the host module which includes this library
	 */

	public Library(DesignSessionImpl theSession, Module host) {
		super(theSession);
		this.host = (LayoutModule) host;
		initSlots();
		onCreate();
	}

	/**
	 * Constructor for opening library directly.
	 *
	 * @param theSession the session in which this library is involved
	 */

	public Library(DesignSessionImpl theSession) {
		this(theSession, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitLibrary(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.LIBRARY_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle();
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @return an API handle for this element
	 */

	public LibraryHandle handle() {
		if (handle == null) {
			handle = new LibraryHandle(this);
		}
		return (LibraryHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.module#getSlotCount()
	 */
	@Override
	protected int getSlotCount() {
		return SLOT_COUNT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Module#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Sets the library namespace.
	 *
	 * @param namespace The namespace to set.
	 */

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Module#getWriter()
	 */

	@Override
	public ModuleWriter getWriter() {
		return new LibraryWriter(this);
	}

	/**
	 * Returns the host module. If this module is not included by any module, return
	 * null.
	 *
	 * @return the host module.
	 */

	public LayoutModule getHost() {
		return host;
	}

	/**
	 * Sets the host module.
	 *
	 * @param theHost the host module to set
	 */

	public void setHost(LayoutModule theHost) {
		this.host = theHost;
	}

	/**
	 * Returns whether the library with the given namespace can be included in this
	 * module.
	 *
	 * @param namespace the library namespace
	 * @return true, if the library with the given namespace can be included.
	 */

	public boolean isRecursiveNamespace(String namespace) {
		Module module = this;
		while (module instanceof Library) {
			Library library = (Library) module;

			if (namespace.equals(library.getNamespace())) {
				return true;
			}

			module = library.getHost();
		}

		return false;
	}

	/**
	 * Returns whether the library with the given url can be included in this
	 * module.
	 *
	 * @param fileName the library file url
	 * @return true, if the library with the given url can be included.
	 */

	public boolean isRecursiveFile(String fileName) {
		Module module = this;
		while (module instanceof Library) {
			Library library = (Library) module;

			if (fileName.equals(library.getLocation())) {
				return true;
			}

			module = library.getHost();
		}

		return false;
	}

	/**
	 * Finds a theme in this module itself.
	 *
	 * @param name Name of the theme to find.
	 * @return The style, or null if the theme is not found.
	 */

	public Theme findNativeTheme(String name) {
		return (Theme) nameHelper.getNameSpace(THEME_NAME_SPACE).getElement(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getNameForDisplayLabel()
	 */

	@Override
	protected String getNameForDisplayLabel() {
		return namespace;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Module#getOptions()
	 */

	@Override
	public ModuleOption getOptions() {
		if (options != null) {
			return options;
		}

		Module hostModule = host;
		while (hostModule != null) {
			ModuleOption hostOptions = hostModule.getOptions();
			if (hostOptions != null) {
				return hostOptions;
			}

			if (hostModule instanceof Library) {
				hostModule = ((Library) hostModule).host;
			} else {
				break;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.LayoutModule#findOutermostModule()
	 */
	@Override
	public LayoutModule findOutermostModule() {
		LayoutModule tmpModule = this;

		while (tmpModule instanceof Library) {
			LayoutModule tmpHost = ((Library) tmpModule).getHost();

			if (tmpHost == null) {
				break;
			}

			tmpModule = tmpHost;
		}
		return tmpModule;
	}

	/**
	 * @param newHost
	 * @return the library with the context cloned
	 */

	public Library contextClone(Module newHost) {
		Library cloned = null;

		try {
			cloned = (Library) doClone(DummyCopyPolicy.getInstance());
		} catch (CloneNotSupportedException e) {
			assert false;
			return null;
		}

		cloned.setFileName(getFileName());
		cloned.setSystemId(getSystemId());
		cloned.setNamespace(getNamespace());

		cloned.setHost((LayoutModule) newHost);

		return cloned;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Module#needCacheStyles()
	 */
	@Override
	public boolean isCached() {
		if (host == null) {
			return super.isCached();
		}

		Module module = this;
		while (module != null) {
			if (module instanceof Library) {
				Library lib = (Library) module;
				if (lib.getHost() == null) {
					return lib.isCached();
				}
				module = lib.getHost();
			} else {
				return module.isCached();
			}
		}

		return false;
	}

}
