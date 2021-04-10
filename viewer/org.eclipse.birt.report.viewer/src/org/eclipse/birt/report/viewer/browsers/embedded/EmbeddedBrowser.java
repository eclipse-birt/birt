/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.browsers.embedded;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.birt.report.designer.core.CorePlugin;
import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

/**
 * Preview browser employing SWT Browser widget. Original implementation is from
 * HELP
 * <p>
 */
public class EmbeddedBrowser {

	private static final String BROWSER_X = "browser.x"; //$NON-NLS-1$

	private static final String BROWSER_Y = "browser.y"; //$NON-NLS-1$

	private static final String BROWSER_WIDTH = "browser.w"; //$NON-NLS-1$

	private static final String BROWSER_HEIGTH = "browser.h"; //$NON-NLS-1$

	private static final String BROWSER_MAXIMIZED = "browser.maximized"; //$NON-NLS-1$

	private Preferences store;

	private static String initialTitle = getWindowTitle();

	private Shell shell;

	private Browser browser;

	private int x, y, w, h;

	private long modalRequestTime = 0;

	/**
	 * Constructor for embedded browser.
	 */
	public EmbeddedBrowser() {
		store = ViewerPlugin.getDefault().getPluginPreferences();

		shell = new Shell(SWT.SHELL_TRIM | Window.getDefaultOrientation());

		initializeShell(shell);
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				browser.close();
			}
		});
		shell.addControlListener(new ControlListener() {

			public void controlMoved(ControlEvent e) {
				if (!shell.getMaximized()) {
					Point location = shell.getLocation();

					x = location.x;

					y = location.y;
				}
			}

			public void controlResized(ControlEvent e) {
				if (!shell.getMaximized()) {
					Point size = shell.getSize();

					w = size.x;

					h = size.y;
				}
			}
		});

		shell.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				// save position
				store.setValue(BROWSER_X, Integer.toString(x));

				store.setValue(BROWSER_Y, Integer.toString(y));

				store.setValue(BROWSER_WIDTH, Integer.toString(w));

				store.setValue(BROWSER_HEIGTH, Integer.toString(h));

				store.setValue(BROWSER_MAXIMIZED, (Boolean.valueOf(shell.getMaximized()).toString()));
			}
		});
		if (Constants.OS_LINUX.equalsIgnoreCase(Platform.getOS())) {
			browser = new Browser(shell, SWT.MOZILLA);
		} else {
			browser = new Browser(shell, SWT.NONE);
		}

		initialize(shell.getDisplay(), browser);

		// use saved location and size
		x = store.getInt(BROWSER_X);

		y = store.getInt(BROWSER_Y);

		w = store.getInt(BROWSER_WIDTH);

		h = store.getInt(BROWSER_HEIGTH);

		if (w == 0 || h == 0) {
			// first launch, use default size
			w = 1024;

			h = 768;

			x = shell.getLocation().x;

			y = shell.getLocation().y;
		}

		setSafeBounds(shell, x, y, w, h);

		if (store.getBoolean(BROWSER_MAXIMIZED)) {
			shell.setMaximized(true);
		}

		shell.addControlListener(new ControlListener() {

			public void controlMoved(ControlEvent e) {
				if (!shell.getMaximized()) {
					Point location = shell.getLocation();

					x = location.x;

					y = location.y;
				}
			}

			public void controlResized(ControlEvent e) {
				if (!shell.getMaximized()) {
					Point size = shell.getSize();

					w = size.x;

					h = size.y;
				}
			}
		});

		shell.open();
		// browser.setUrl("about:blank");

		browser.addLocationListener(new LocationListener() {

			public void changing(LocationEvent e) {
				// hack to know when help webapp needs modal window
				modalRequestTime = 0;
				if (e.location != null && e.location.startsWith("javascript://needModal")) //$NON-NLS-1$
				{
					modalRequestTime = System.currentTimeMillis();
				}
			}

			public void changed(LocationEvent e) {
				// Do nothing
			}
		});
	}

	/**
	 * Constructor embedded browser.
	 * 
	 * @param event
	 * @param parent Shell or null
	 */
	public EmbeddedBrowser(WindowEvent event, Shell parent) {
		if (parent == null) {
			shell = new Shell(SWT.SHELL_TRIM | Window.getDefaultOrientation());
		} else {
			shell = new Shell(parent, SWT.PRIMARY_MODAL | SWT.DIALOG_TRIM);
		}

		initializeShell(shell);

		Browser browser = null;
		if (Constants.OS_LINUX.equalsIgnoreCase(Platform.getOS())) {
			browser = new Browser(shell, SWT.MOZILLA);
		} else {
			browser = new Browser(shell, SWT.NONE);
		}

		initialize(shell.getDisplay(), browser);

		event.browser = browser;

		browser.addLocationListener(new LocationListener() {

			public void changing(LocationEvent e) {
				// hack to know when help webapp needs modal window
				modalRequestTime = 0;
				if (e.location != null && e.location.startsWith("javascript://needModal")) //$NON-NLS-1$
				{
					modalRequestTime = System.currentTimeMillis();
				}
			}

			public void changed(LocationEvent e) {
				// Do nothing
			}
		});
	}

	private static void initializeShell(Shell s) {
		// need not set title for viewer content will set window title.
		// s.setText( initialTitle );

		Image[] shellImages = createImages();

		if (shellImages != null) {
			s.setImages(shellImages);
		}

		s.setLayout(new FillLayout());
	}

	private void initialize(final Display display, Browser browser) {
		browser.addOpenWindowListener(new OpenWindowListener() {

			public void open(WindowEvent event) {
				if (System.currentTimeMillis() - modalRequestTime <= 1000) {
					new EmbeddedBrowser(event, shell);
				} else {
					new EmbeddedBrowser(event, null);
				}
			}
		});

		browser.addVisibilityWindowListener(new VisibilityWindowListener() {

			public void hide(WindowEvent event) {
				Browser browser = (Browser) event.widget;

				Shell shell = browser.getShell();

				shell.setVisible(false);
			}

			public void show(WindowEvent event) {
				Browser browser = (Browser) event.widget;

				Shell shell = browser.getShell();

				if (event.location != null) {
					shell.setLocation(event.location);
				}

				if (event.size != null) {
					Point size = event.size;

					shell.setSize(shell.computeSize(size.x, size.y));
				}

				shell.open();
			}
		});

		browser.addCloseWindowListener(new CloseWindowListener() {

			public void close(WindowEvent event) {
				Browser browser = (Browser) event.widget;

				Shell shell = browser.getShell();

				shell.close();
			}
		});

		browser.addTitleListener(new TitleListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.browser.TitleListener#changed(org.eclipse.swt
			 * .browser.TitleEvent)
			 */
			public void changed(TitleEvent event) {
				if (event.title != null && event.title.length() > 0) {
					Browser browser = (Browser) event.widget;

					Shell shell = browser.getShell();

					shell.setText(event.title);
				}
			}
		});
	}

	/**
	 * Display arbitary url.
	 * 
	 * @param url
	 */
	public void displayUrl(String url) {
		browser.setUrl(url);

		shell.setMinimized(false);

		shell.forceActive();
	}

	/**
	 * Check whether the browser is disposed or not.
	 * 
	 * @return browser is disposed or not
	 */
	public boolean isDisposed() {
		return shell.isDisposed();
	}

	private static String getWindowTitle() {
		return ViewerPlugin.getResourceString("viewer.browserTitle"); //$NON-NLS-1$
	}

	/**
	 * Create shell images
	 */
	private static Image[] createImages() {
		String[] productImageURLs = getProductImageURLs();

		if (productImageURLs != null) {
			ArrayList shellImgs = new ArrayList();

			for (int i = 0; i < productImageURLs.length; i++) {
				if ("".equals(productImageURLs[i])) //$NON-NLS-1$
				{
					continue;
				}

				URL imageURL = null;

				try {
					imageURL = new URL(productImageURLs[i]);
				} catch (MalformedURLException mue) {
					// must be a path relative to the product bundle
					IProduct product = Platform.getProduct();

					if (product != null) {
						Bundle productBundle = product.getDefiningBundle();

						if (productBundle != null) {
							imageURL = Platform.find(productBundle, new Path(productImageURLs[i]));
						}
					}
				}

				Image image = null;
				if (imageURL != null) {
					String key = imageURL.toString();
					if (CorePlugin.getDefault().getImageRegistry().get(key) != null) {
						image = CorePlugin.getDefault().getImageRegistry().get(key);
					} else {
						image = ImageDescriptor.createFromURL(imageURL).createImage();

						CorePlugin.getDefault().getImageRegistry().put(key, image);
					}
				}

				if (image != null) {
					shellImgs.add(image);
				}
			}

			return (Image[]) shellImgs.toArray(new Image[shellImgs.size()]);
		}

		return new Image[0];
	}

	/**
	 * Obtains URLs to product image
	 * 
	 * @return String[] with URLs as Strings or null
	 */
	private static String[] getProductImageURLs() {
		IProduct product = Platform.getProduct();

		if (product != null) {
			String url = product.getProperty("windowImages"); //$NON-NLS-1$

			if (url != null && url.length() > 0) {
				return url.split(",\\s*"); //$NON-NLS-1$
			}

			url = product.getProperty("windowImage"); //$NON-NLS-1$

			if (url != null && url.length() > 0) {
				return new String[] { url };
			}
		}

		return null;
	}

	/**
	 * Closes the browser.
	 */
	public void close() {
		if (!shell.isDisposed()) {
			shell.dispose();
		}
	}

	private static void setSafeBounds(Shell s, int x, int y, int width, int height) {
		Rectangle clientArea = s.getDisplay().getClientArea();

		width = Math.min(clientArea.width, width);

		height = Math.min(clientArea.height, height);

		x = Math.min(x + width, clientArea.x + clientArea.width) - width;

		y = Math.min(y + height, clientArea.y + clientArea.height) - height;

		x = Math.max(x, clientArea.x);

		y = Math.max(y, clientArea.y);

		s.setBounds(x, y, width, height);
	}

	/**
	 * Set browser window location.
	 * 
	 * @param x X coordinate of browser window's top-left corner
	 * @param y Y coordinate of browser window's top-left corner
	 */
	public void setLocation(int x, int y) {
		shell.setLocation(x, y);
	}

	/**
	 * Set browser window size.
	 * 
	 * @param width  browser window width
	 * @param height browser window height
	 */
	public void setSize(int width, int height) {
		shell.setSize(w, h);
	}
}