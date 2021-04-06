/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.preview;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This class is used to manage chart live preview, makes no-UI task and UI task
 * running in different threads and controls correct invoking orders.
 * 
 * @since 2.5.2
 */

public class ChartLivePreviewThread extends Thread {
	private volatile LivePreviewTask task = null;

	private boolean threadSuspended = true;

	private volatile Thread blinker;

	private Shell parentShell;

	PreviewTimerTask timeTask = null;

	private IDataServiceProvider dataProvider;

	private boolean hasDataEngine = false;

	private static final int DELAY_TIME = 2000;

	public static final String PARAM_CHART_MODEL = "Chart Model"; //$NON-NLS-1$

	private volatile boolean initFinished = false;

	/**
	 * Constructor.
	 */
	public ChartLivePreviewThread() {
		this.setName("Chart live preview thread");//$NON-NLS-1$
	}

	/**
	 * Constructor.
	 */
	public ChartLivePreviewThread(IDataServiceProvider provider) {
		this.setName("Chart live preview thread");//$NON-NLS-1$
		dataProvider = provider;
	}

	/**
	 * Sets parent shell that is used to create a progress dialog.
	 * 
	 * @param shell
	 */
	public void setParentShell(Shell shell) {
		this.parentShell = shell;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#start()
	 */
	public void start() {
		blinker = this;
		super.start();

		// We must wait for some initial work to be finished in this thread and
		// then return, the initial works include the data engine and data session
		// initialization.
		int i = 1;
		while (!initFinished && i < 6) {
			try {
				Thread.sleep(i * 10);
				i++;
			} catch (InterruptedException e) {
				// Here do not do anything.
			}
		}
	}

	/**
	 * Ends this thread.
	 */
	public synchronized void end() {
		blinker = null;
		this.interrupt();
	}

	/**
	 * Adds a task.
	 * 
	 * @param atask
	 */
	public synchronized void add(LivePreviewTask atask) {
		this.task = atask;
		threadSuspended = false;
		this.notify();
	}

	/**
	 * Removes a task from inactive task.
	 * 
	 * @return
	 */
	private synchronized LivePreviewTask remove() {
		LivePreviewTask tp = this.task;
		this.task = null;
		threadSuspended = true;
		return tp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		initDataEngine();
		initFinished = true;
		super.run();
		Timer fPaintTimer = null;
		timeTask = null;
		Thread thisThread = Thread.currentThread();
		while (blinker == thisThread) {

			try {
				LivePreviewTask tp = null;
				synchronized (this) {
					while (threadSuspended)
						this.wait();
					tp = remove();
				}

				if (!hasDataEngine) {
					Display.getDefault().asyncExec(new Runnable() {

						public void run() {
							WizardBase.showException(
									Messages.getString("ChartLivePreviewThread.ErrorMessage.NoDataEngine")); //$NON-NLS-1$
						}
					});
					continue;
				}

				fPaintTimer = new Timer();
				timeTask = new PreviewTimerTask(tp.getName(), parentShell);
				fPaintTimer.schedule(timeTask, DELAY_TIME);

				if (tp != null) {
					Map<String, Object> parameters = null;
					for (LivePreviewTask lpt : tp.getTasks()) {
						if (blinker != thisThread) {
							break;
						}
						if (parameters != null) {
							lpt.setParameters(parameters);
						}
						lpt.run();
						parameters = lpt.getParameters();
					}
				}
			} catch (Exception e) {
				// Don't do nothing, it just catches unexpected exception
				// 'java.lang.InterruptedException' while interrupt this thread.
			} finally {
				if (fPaintTimer != null) {
					fPaintTimer.cancel();
				}

				if (timeTask != null) {
					timeTask.dispose();
				}
			}

		}

		disposeDataEngine();
	}

	/**
	 * 
	 */
	private void initDataEngine() {
		if (dataProvider != null) {
			try {
				dataProvider.initialize();
				hasDataEngine = true;
			} catch (ChartException e) {
				final Exception exp = e;
				Display.getDefault().asyncExec(new Runnable() {

					public void run() {
						WizardBase.displayException(exp);
					}
				});
			}
		}
	}

	/**
	 * 
	 */
	private void disposeDataEngine() {
		if (dataProvider != null) {
			dataProvider.dispose();
		}
	}

	/**
	 * This timer task is used to show/hide a progress dialog in UI for running
	 * task.
	 */
	static class PreviewTimerTask extends TimerTask {

		static ImageLoader loader;
		static ImageData[] imageDatas;

		Shell parentShell = null;
		Shell shell = null;
		private String taskName;

		PreviewTimerTask(String taskName, Shell parentShell) throws IOException {
			this.taskName = taskName;
			this.parentShell = parentShell;
			if (imageDatas == null) {
				loader = new ImageLoader();
				imageDatas = loader.load(UIHelper.getURL("icons/obj16/progress_animation.gif").openStream()); //$NON-NLS-1$
			}
		}

		public void run() {
			Display.getDefault().syncExec(new Runnable() {

				public void run() {
					Display display = null;
					if (parentShell != null && !parentShell.isDisposed()) {
						shell = new Shell(parentShell, SWT.ON_TOP);
						display = shell.getDisplay();
					} else {
						display = Display.getDefault();
						parentShell = display.getActiveShell();
						shell = new Shell(display, SWT.ON_TOP);
					}

					ImageViewer ic = new ImageViewer(shell, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE);
					Label l = new Label(shell, SWT.NONE);
					l.setText(taskName + Messages.getString("ChartLivePreviewThread_Text.PleaseWait")); //$NON-NLS-1$

					GridLayout gl = new GridLayout();
					gl.numColumns = 2;
					gl.marginLeft = 20;
					gl.marginRight = 20;
					gl.marginTop = 20;
					gl.marginBottom = 20;
					shell.setLayout(gl);
					ic.setImages(imageDatas, loader.repeatCount);

					ic.pack();
					shell.pack();

					// Move to center in parent shell.
					Rectangle parentBounds = parentShell.getBounds();
					Rectangle shellBounds = shell.getBounds();
					shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
							parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

					shell.open();
					while (!shell.isDisposed()) {
						if (!display.readAndDispatch())
							display.sleep();
					}
				}
			});
		}

		public void dispose() {
			Display.getDefault().syncExec(new Runnable() {

				public void run() {
					try {
						if (shell != null) {
							shell.close();
						}
					} catch (Exception e) {
						// Don't do nothing, it just avoids unexpected exception.
					}
				}
			});
		}
	}
}
