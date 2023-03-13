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

package org.eclipse.birt.report.model.css;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.util.ResourceLocatorImpl;

/**
 * Adapter of CssStyleSheet operation.
 *
 */

public class CssStyleSheetAdapter implements ICssStyleSheetOperation {

	private List<CssStyleSheet> csses = null;

	/**
	 * Gets css style sheet by location. Compare two absolute path of file.
	 *
	 * @param module   module
	 * @param csses    list each item is <code>CssStyleSheet</code>
	 * @param url
	 * @param location absolute location
	 * @return css style sheet.
	 */

	public static CssStyleSheet getCssStyleSheetByProperties(Module module, List<CssStyleSheet> csses, URL url,
			String externalCssURI, boolean isUseExteralCss) {
		if (csses == null) {
			return null;
		}

		// do not use Module.findResource to avoid call third-part resource
		// locater

		if (url != null) {
			IResourceLocator locator = new ResourceLocatorImpl();

			for (int i = 0; i < csses.size(); ++i) {
				CssStyleSheet css = csses.get(i);
				String tmpFileName = css.getFileName();
				ModuleOption options = module.getOptions();
				URL tmpurl = locator.findResource((ModuleHandle) module.getHandle(module), tmpFileName,
						IResourceLocator.CASCADING_STYLE_SHEET, options == null ? null : options.getOptions());
				if (tmpurl == null) {
					continue;
				}

				if (url.equals(tmpurl)) {
					String tmpUri = css.getExternalCssURI();
					boolean tmpUse = css.isUseExternalCss();
					if (tmpUse == isUseExteralCss) {
						if (externalCssURI != null && externalCssURI.equals(tmpUri)) {
							return css;
						} else if (externalCssURI == null && tmpUri == null) {
							return css;
						}
					}
				}

			}
		} else {
			for (int i = 0; i < csses.size(); ++i) {
				CssStyleSheet css = csses.get(i);
				String tmpUri = css.getExternalCssURI();
				String tmpFileName = css.getFileName();
				if (tmpFileName == null) {
					if (isUseExteralCss == css.isUseExternalCss()) {
						if (externalCssURI != null) {
							if (externalCssURI.equals(tmpUri)) {
								return css;
							}
						} else if (tmpUri == null) {
							return css;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets css style sheet by location. Compare two absolute path of file.
	 *
	 * @deprecated
	 * @param module module
	 * @param csses  list each item is <code>CssStyleSheet</code>
	 * @param url
	 *
	 *               absolute location
	 * @return css style sheet.
	 */

	@Deprecated
	public static CssStyleSheet getCssStyleSheetByLocation(Module module, List<CssStyleSheet> csses, URL url) {
		if (url == null || csses == null) {
			return null;
		}

		// do not use Module.findResource to avoid call third-part resource
		// locater

		IResourceLocator locator = new ResourceLocatorImpl();

		for (int i = 0; i < csses.size(); ++i) {
			CssStyleSheet css = csses.get(i);
			String tmpFileName = css.getFileName();
			ModuleOption options = module.getOptions();
			URL tmpurl = locator.findResource((ModuleHandle) module.getHandle(module), tmpFileName,
					IResourceLocator.CASCADING_STYLE_SHEET, options == null ? null : options.getOptions());
			if (tmpurl == null) {
				continue;
			}

			if (url.equals(tmpurl)) {
				return css;
			}
		}
		return null;
	}

	/**
	 * Gets position of css style sheet in all sheets. Compare two path of file.
	 *
	 * @param module           module
	 * @param csses            list each item is <code>CssStyleSheet</code>
	 * @param location         absolute location or relative path
	 * @param externalCssURI   exteranl css uri
	 * @param isUseExternalCss
	 * @return css style sheet.
	 */

	public static int getPositionOfCssStyleSheetByProperties(Module module, List<CssStyleSheet> csses, String location,
			String externalCssURI, boolean isUseExternalCss) {
		if (csses == null) {
			return -1;
		}
		if (location != null) {
			URL targetUrl = module.findResource(location, IResourceLocator.CASCADING_STYLE_SHEET);
			String fileLocation = location;

			// if css file found, uses the absolute path to compare two pathes.

			if (targetUrl != null) {
				fileLocation = targetUrl.getFile();
			}

			for (int i = 0; i < csses.size(); ++i) {
				CssStyleSheet css = csses.get(i);
				String tmpFileName = css.getFileName();

				if (targetUrl != null) {
					URL url = module.findResource(tmpFileName, IResourceLocator.CASCADING_STYLE_SHEET);
					if (url != null) {
						tmpFileName = url.getFile();
					}
				}

				// if the css file has been deleted, uses the input value as the
				// comparison value.

				if (fileLocation.equalsIgnoreCase(tmpFileName)) {
					String tmpUri = css.getExternalCssURI();
					boolean tmpUse = css.isUseExternalCss();
					if (tmpUse == isUseExternalCss) {
						if (externalCssURI != null && externalCssURI.equals(tmpUri)) {
							return i;
						} else if (externalCssURI == null && tmpUri == null) {
							return i;
						}
					}
				}
			}
		} else {
			for (int i = 0; i < csses.size(); ++i) {
				CssStyleSheet css = csses.get(i);
				String tmpURI = css.getExternalCssURI();
				String tmpFileName = css.getFileName();
				if (tmpFileName == null) {
					if (isUseExternalCss == css.isUseExternalCss()) {
						if (externalCssURI != null) {
							if (externalCssURI.equals(tmpURI)) {
								return i;
							}
						} else if (tmpURI == null) {
							return i;
						}
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Gets position of css style sheet in all sheets. Compare two path of file.
	 *
	 * @deprecated
	 * @param module   module
	 * @param csses    list each item is <code>CssStyleSheet</code>
	 * @param location absolute location or relative path
	 * @return css style sheet.
	 */

	@Deprecated
	public static int getPositionOfCssStyleSheet(Module module, List<CssStyleSheet> csses, String location) {
		if (location == null || csses == null) {
			return -1;
		}
		URL targetUrl = module.findResource(location, IResourceLocator.CASCADING_STYLE_SHEET);
		String fileLocation = location;

		// if css file found, uses the absolute path to compare two pathes.

		if (targetUrl != null) {
			fileLocation = targetUrl.getFile();
		}

		for (int i = 0; i < csses.size(); ++i) {
			CssStyleSheet css = csses.get(i);
			String tmpFileName = css.getFileName();

			if (targetUrl != null) {
				URL url = module.findResource(tmpFileName, IResourceLocator.CASCADING_STYLE_SHEET);
				if (url != null) {
					tmpFileName = url.getFile();
				}
			}

			// if the css file has been deleted, uses the input value as the
			// comparison value.

			if (fileLocation.equalsIgnoreCase(tmpFileName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Drops the given css from css list.
	 *
	 * @param css the css to drop
	 * @return the position of the css to drop
	 */

	@Override
	public int dropCss(CssStyleSheet css) {
		assert csses != null;
		assert csses.contains(css);

		int posn = csses.indexOf(css);
		if (posn != -1) {
			csses.remove(posn);
		}

		return posn;
	}

	/**
	 * Adds the given css to css style sheets list.
	 *
	 * @param css the css to insert
	 */

	@Override
	public void addCss(CssStyleSheet css) {
		if (csses == null) {
			csses = new ArrayList<>();
		}

		csses.add(css);
	}

	/**
	 * Insert the given css to the given position
	 *
	 * @param css
	 * @param index
	 */

	@Override
	public void insertCss(CssStyleSheet css, int index) {
		if (csses == null) {
			csses = new ArrayList<>();
		}
		if (index < 0 || index > csses.size()) {
			return;
		}
		csses.add(index, css);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.elements.ICssStyleSheetOperation#getCsses()
	 */

	@Override
	public List<CssStyleSheet> getCsses() {
		if (csses == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(csses);
	}

}
