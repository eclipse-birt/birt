/*******************************************************************************
 * Copyright (c) 2026 Rahul Pal and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Rahul Pal - initial implementation (issue #2444)
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.pdf;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

/**
 * Concurrency test for the PDF emitter font path (issue #2444).
 *
 * <p>
 * Renders the same report design to PDF from several threads in parallel,
 * sharing the single {@code engine} created by {@link EngineCase} (the
 * realistic multi-threaded usage). The test asserts that no render throws and
 * that every output PDF is produced and non-empty.
 * </p>
 *
 * <p>
 * This guards concurrent rendering through the font path after the removal of
 * the process-global {@code LayoutProcessor} state. As with the other tests in
 * this module, it asserts that rendering completes without error rather than
 * verifying glyph-level output. Note that, by the nature of race conditions, a
 * passing run does not by itself prove the absence of a race - the primary
 * safety argument is the design (per-document state, no static state); this
 * test is a regression guard and demonstration.
 * </p>
 */
public class PdfConcurrentRenderTest extends EngineCase {

	/** Reused existing design from this test module. */
	private static final String DESIGN = "test/org/eclipse/birt/report/engine/emitter/pdf/issue-2429.rptdesign";

	/** Number of concurrent render tasks. */
	private static final int THREADS = 8;

	/** Upper bound on total render time before the test gives up. */
	private static final int TIMEOUT_SECONDS = 120;

	/**
	 * Output directory, cleaned at the start of each run so the PDFs from the last
	 * run remain available for manual inspection.
	 */
	private File outputDir;

	@Override
	protected void setUp() throws Exception {
		super.setUp(); // creates the shared engine (and configures fonts)
		outputDir = new File(System.getProperty("java.io.tmpdir"), "birt-pdf-concurrent");
		deleteRecursively(outputDir);
		outputDir.mkdirs();
	}

	/**
	 * Renders the same design concurrently on a shared engine and verifies that
	 * every render succeeds and produces a non-empty PDF.
	 *
	 * @throws Exception
	 */
	public void testConcurrentPdfRenders() throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(THREADS);
		List<Future<File>> futures = new ArrayList<>();
		List<String> failures = Collections.synchronizedList(new ArrayList<>());

		try {
			for (int i = 0; i < THREADS; i++) {
				final int id = i;
				Callable<File> task = () -> {
					try {
						return renderOnce(id);
					} catch (Throwable t) {
						failures.add("thread " + id + ": " + t.getClass().getSimpleName() + ": " + t.getMessage());
						t.printStackTrace();
						return null;
					}
				};
				futures.add(pool.submit(task));
			}

			pool.shutdown();
			boolean finished = pool.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS);
			assertTrue("Concurrent renders did not finish within " + TIMEOUT_SECONDS + "s", finished);
		} finally {
			pool.shutdownNow();
		}

		if (!failures.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append(failures.size()).append(" of ").append(THREADS).append(" concurrent renders failed:");
			for (String failure : failures) {
				sb.append("\n  ").append(failure);
			}
			fail(sb.toString());
		}

		// Every task must have produced a real, non-empty PDF.
		for (Future<File> future : futures) {
			File pdf = future.get();
			assertNotNull("Render produced no output", pdf);
			assertTrue("Missing output: " + pdf, pdf.isFile());
			assertTrue("Empty output: " + pdf, pdf.length() > 0L);
		}
	}

	/**
	 * Renders the shared design to a unique per-thread PDF against the shared
	 * engine. Uses {@link EngineCase#createRunAndRenderTask(String)}, which opens
	 * the design by path directly (no shared design file), so it is safe to call
	 * concurrently.
	 */
	private File renderOnce(int id) throws Exception {
		File out = new File(outputDir, "render-" + id + ".pdf");

		IRunAndRenderTask task = createRunAndRenderTask(DESIGN);
		try {
			PDFRenderOption options = new PDFRenderOption();
			options.setOutputFormat("pdf");
			options.setOutputFileName(out.getAbsolutePath());
			task.setRenderOption(options);
			task.run();
		} finally {
			task.close();
		}
		return out;
	}

	/** Deletes a file or directory tree, ignoring files that cannot be removed. */
	private static void deleteRecursively(File file) {
		if (file == null || !file.exists()) {
			return;
		}
		File[] children = file.listFiles();
		if (children != null) {
			for (File child : children) {
				deleteRecursively(child);
			}
		}
		file.delete();
	}
}