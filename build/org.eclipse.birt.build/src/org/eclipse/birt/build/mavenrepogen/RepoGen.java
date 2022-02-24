/*******************************************************************************
 * Copyright (c) 2012 Innovent Solutions, Inc.
 * 
 * Unless otherwise indicated, all Content made available 
 * by Innovent Solutions, Inc  is provided to you under the terms and 
 * conditions of the Eclipse Public License Version 2.0 ("EPL"). A copy 
 * of the EPL is provided with this Content and is also available at 
 * http://www.eclipse.org/legal/epl-2.0.html. For purposes of the EPL, 
 * "Program" will mean the Content.
 * 
 * Contributors:
 *   Steve Schafer
 *******************************************************************************/
package org.eclipse.birt.build.mavenrepogen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RepoGen {
	private final File libDir;
	private final String groupId;
	private final String passphrase;
	private final File repoDir;
	private final File groupDir;
	private final File globalSnapshotBuildFile;
	private final File globalSnapshotScriptFile;
	private final File globalReleaseBuildFile;
	private final File globalReleaseScriptFile;
	private final File templateSnapshotPomFile;
	private final File templateReleasePomFile;
	private final String rootFileName;
	private final File readmeFile;
	private final File sourceDir;
	private final String externalFileName = "./externalRepo.properties";

	private final Map<String, ExternalDependency> externalDependencies;

	private RepoGen(final File libDir, final File repoParentDir, final String groupId, final String passphrase,
			final boolean snapshot, final boolean release, final boolean clean, final String rootFileName,
			final String readmeFilePath, final File sourceDir) throws IOException {
		this.libDir = libDir;
		this.groupId = groupId;
		this.passphrase = passphrase;
		this.readmeFile = new File(readmeFilePath);
		this.sourceDir = sourceDir;

		repoDir = new File(repoParentDir, "repository");
		repoDir.mkdir();
		groupDir = new File(repoDir, groupId);
		if (clean)
			deepDelete(groupDir);
		groupDir.mkdir();
		if (snapshot) {
			globalSnapshotBuildFile = new File(groupDir, "buildSnapshot.xml");
			globalSnapshotBuildFile.createNewFile();
			globalSnapshotScriptFile = new File(groupDir, "buildSnapshot.sh");
			globalSnapshotScriptFile.createNewFile();
			templateSnapshotPomFile = new File(groupDir, "templateSnapshotPomFile.xml");
			templateSnapshotPomFile.createNewFile();
		} else {
			globalSnapshotBuildFile = null;
			globalSnapshotScriptFile = null;
			templateSnapshotPomFile = null;
		}
		if (release) {
			globalReleaseBuildFile = new File(groupDir, "buildRelease.xml");
			globalReleaseBuildFile.createNewFile();
			globalReleaseScriptFile = new File(groupDir, "buildRelease.sh");
			globalReleaseScriptFile.createNewFile();
			templateReleasePomFile = new File(groupDir, "templateReleasePomFile.xml");
			templateReleasePomFile.createNewFile();
		} else {
			globalReleaseBuildFile = null;
			globalReleaseScriptFile = null;
			templateReleasePomFile = null;
		}
		this.rootFileName = rootFileName;
		externalDependencies = new HashMap<String, ExternalDependency>();
		readExternalDependency();
		System.out.println(externalDependencies.size() + " external dependencies found.");

	}

	private void addExternalDependency(final String fileName, final String groupId, final String artifactId,
			final String version) {
		externalDependencies.put(fileName, new ExternalDependency(fileName, groupId, artifactId, version));
	}

	public static void main(final String[] args) throws IOException {
		final String propsFileName;

		if (args.length >= 1)
			propsFileName = args[0];
		else
			propsFileName = "./repoGen.properties";
		String passphrase = null;
		if (args.length >= 2)
			passphrase = args[1];

		final Properties properties = new Properties();
		final FileReader fr = new FileReader(propsFileName);
		try {
			properties.load(fr);
		} finally {
			fr.close();
		}
		final String libDirName = properties.getProperty("libDir");
		final String repoDirName = properties.getProperty("repoDir");
		final String groupId = properties.getProperty("groupId");
		if (passphrase == null)
			passphrase = properties.getProperty("passphrase");
		final boolean clean = "true".equalsIgnoreCase(properties.getProperty("clean"));
		final boolean genSnapshot = "true".equalsIgnoreCase(properties.getProperty("snapshot"));
		final boolean genRelease = "true".equalsIgnoreCase(properties.getProperty("release"));
		final String rootFileName = properties.getProperty("rootFile");
		final String readmeFilePath = properties.getProperty("readmeFile");
		final String sourceDir = properties.getProperty("sourceDir");

		final RepoGen repoGen = new RepoGen(new File(libDirName), new File(repoDirName), groupId, passphrase,
				genSnapshot, genRelease, clean, rootFileName, readmeFilePath, new File(sourceDir));
		repoGen.generate();
	}

	private void generate() throws IOException {
		final PrintWriter globalSnapshotBuildFileWriter = createGlobalBuildFileWriter(globalSnapshotBuildFile,
				"deploy");
		final PrintWriter globalSnapshotScriptFileWriter = createGlobalScriptFileWriter(globalSnapshotScriptFile);
		final PrintWriter globalReleaseBuildFileWriter;
		if (globalReleaseBuildFile != null) {
			globalReleaseBuildFileWriter = new PrintWriter(new FileWriter(globalReleaseBuildFile));
			globalReleaseBuildFileWriter.print("<project name=\"");
			globalReleaseBuildFileWriter.print(groupId);
			globalReleaseBuildFileWriter.println(
					"\" default=\"stage\" basedir=\".\" xmlns:artifact=\"antlib:org.apache.maven.artifact.ant\">");
			globalReleaseBuildFileWriter.println(" <target name=\"stage\">");
		} else {
			globalReleaseBuildFileWriter = null;
		}
		final PrintWriter globalReleaseScriptFileWriter = createGlobalScriptFileWriter(globalReleaseScriptFile);
		final PrintWriter templateSnapshotPomWriter = createTemplatePomWriter(templateSnapshotPomFile, true);
		final PrintWriter templateReleasePomWriter = createTemplatePomWriter(templateReleasePomFile, false);
		File rootFile = null;
		final List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		final File[] files = libDir.listFiles();

		/**
		 * Handle the jars under lib folder excluding the root file.
		 */
		if (files != null) {
			for (final File file : files) {
				if (rootFileName != null && rootFileName.equals(file.getName()))
					rootFile = file;
				else if (!externalDependencies.containsKey(file.getName())) {
					final FileInfo fileInfo = getFileInfo(file);
					if (fileInfo != null) {
						fileInfos.add(fileInfo);
						generateFile(fileInfo, true, globalSnapshotBuildFileWriter, globalSnapshotScriptFileWriter,
								templateSnapshotPomWriter, null, null);
						generateFile(fileInfo, false, globalReleaseBuildFileWriter, globalReleaseScriptFileWriter,
								templateReleasePomWriter, null, null);
					}
				}
			}
		}
		/**
		 * Handle the root file birt.runtime
		 */
		if (rootFile != null) {
			final FileInfo fileInfo = getFileInfo(rootFile);
			generateFile(fileInfo, true, globalSnapshotBuildFileWriter, globalSnapshotScriptFileWriter,
					templateSnapshotPomWriter, fileInfos, externalDependencies.values());
			generateFile(fileInfo, false, globalReleaseBuildFileWriter, globalReleaseScriptFileWriter,
					templateReleasePomWriter, fileInfos, externalDependencies.values());
		}

		int tmpCount = fileInfos.size() + 1;
		System.out.println(tmpCount + " jars under runtime lib folder founded.");

		closeTemplatePomWriter(templateSnapshotPomWriter);
		closeTemplatePomWriter(templateReleasePomWriter);
		closeBuildFileWriter(globalSnapshotBuildFileWriter);
		closeBuildFileWriter(globalReleaseBuildFileWriter);
		closeScriptFileWriter(globalSnapshotScriptFileWriter);
		closeScriptFileWriter(globalReleaseScriptFileWriter);
	}

	private PrintWriter createGlobalScriptFileWriter(final File file) throws IOException {
		if (file == null)
			return null;
		final PrintWriter writer = new PrintWriter(new FileWriter(file));
		writer.println("# Execute all the builds.");
		// TODO parameterize these
		writer.println("export ANT_OPTS=\"-XX:MaxPermSize=256m\"");
		writer.println("date > time.log");
		// writer.println("export ANT_HOME=~/java/apache-ant-1.8.2");
		return writer;
	}

	private void closeBuildFileWriter(final PrintWriter writer) {
		if (writer != null) {
			writer.println(" </target>");
			writer.println("</project>");
			writer.close();
		}
	}

	private void closeScriptFileWriter(final PrintWriter writer) {
		if (writer != null) {
			writer.println("# done.");
			writer.println("date >> time.log");
			writer.close();
		}
	}

	private void closeTemplatePomWriter(final PrintWriter writer) {
		if (writer != null) {
			for (final ExternalDependency externalDependency : externalDependencies.values()) {
				writer.println("   <dependency>");
				writer.print("    <groupId>");
				writer.print(externalDependency.getGroupId());
				writer.println("</groupId>");
				writer.print("     <artifactId>");
				writer.print(externalDependency.getArtifactId());
				writer.println("</artifactId>");
				writer.print("     <version>");
				writer.print(externalDependency.getVersion());
				writer.println("</version>");
				writer.println("   </dependency>");
			}
			writer.println(" </dependencies>");
			writer.println("</project>");
			writer.close();
		}
	}

	private PrintWriter createGlobalBuildFileWriter(final File file, final String string) throws IOException {
		if (file == null)
			return null;
		final PrintWriter writer = new PrintWriter(new FileWriter(file));
		writer.print("<project name=\"");
		writer.print(groupId);
		writer.print("\" default=\"");
		writer.print("string");
		writer.print("\" basedir=\".\" xmlns:artifact=\"antlib:org.apache.maven.artifact.ant\">");
		writer.print(" <target name=\"");
		writer.print("string");
		writer.println("\">");
		return writer;
	}

	private PrintWriter createTemplatePomWriter(final File file, final boolean snapshot) throws IOException {
		if (file == null)
			return null;
		final PrintWriter writer = new PrintWriter(new FileWriter(file));
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<project");
		writer.println(" xmlns=\"http://maven.apache.org/POM/4.0.0\"");
		writer.println(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		writer.println(
				" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">");
		writer.println(" <modelVersion>4.0.0</modelVersion>");
		writer.println(" <repositories>");
		writer.println("  <repository>");
		if (snapshot) {
			writer.println("   <id>sonatype-nexus-snapshots</id>");
			writer.println("   <name>Sonatype Nexus Snapshots</name>");
			writer.println("   <url>https://oss.sonatype.org/content/repositories/snapshots/</url>");
		} else {
			// TODO - staging won't work
			writer.println("   <id>sonatype-nexus-staging</id>");
			writer.println("   <name>Sonatype Nexus Staging</name>");
			writer.println("   <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>");
		}
		writer.println("  </repository>");
		writer.println(" </repositories>");
		writer.println(" <dependencies>");
		return writer;
	}

	private FileInfo getFileInfo(final File file) throws IOException {
		if (file.isDirectory())
			return null;
		if (!file.getAbsolutePath().toLowerCase().endsWith(".jar"))
			return null;
		System.out.println(file);
		final Manifest manifest = getManifest(file);
		String artifactId;
		String version;
		if (manifest == null) {
			artifactId = file.getName();
			final int indexOfDot = artifactId.lastIndexOf(".");
			if (indexOfDot >= 0)
				artifactId = artifactId.substring(0, indexOfDot);
			version = "1";
		} else {
			final Attributes mainAttributes = manifest.getMainAttributes();
			artifactId = mainAttributes.getValue("Bundle-SymbolicName");
			if (artifactId != null) {
				final int indexofsemicolon = artifactId.indexOf(";");
				if (indexofsemicolon >= 0)
					artifactId = artifactId.substring(0, indexofsemicolon);
				version = mainAttributes.getValue("Bundle-Version");

				if (file.getName().equals(rootFileName)) {
					// System.out.println( rootFileName + "," + file.getName() + version);
					version = trimVersion(version);
					System.out.println("root file found: " + rootFileName + ", version: " + version);
				}
			} else {
				artifactId = mainAttributes.getValue("Specification-Title");
				if (artifactId != null) {
					version = mainAttributes.getValue("Specification-Version");
				} else {
					artifactId = file.getName();
					final int indexOfDot = artifactId.lastIndexOf(".");
					if (indexOfDot >= 0)
						artifactId = artifactId.substring(0, indexOfDot);
					version = "1";
				}
			}
		}
		return new FileInfo(file, groupId, artifactId, version);
	}

	private void generateFile(final FileInfo fileInfo, final boolean snapshot, final PrintWriter globalBuildFileWriter,
			final PrintWriter globalScriptFileWriter, final PrintWriter templatePomFileWriter,
			final List<FileInfo> dependsOn, final Collection<ExternalDependency> externalDependencies)
			throws IOException {
		if (globalBuildFileWriter == null)
			return;
		if (templatePomFileWriter == null)
			return;
		final File projectDir = new File(groupDir, fileInfo.getArtifactId());
		projectDir.mkdir();
		final File versionDir = new File(projectDir, fileInfo.getVersion(snapshot));
		deepDelete(versionDir);
		versionDir.mkdir();
		final String newFileName = fileInfo.getArtifactId() + "-" + fileInfo.getVersion(snapshot);
		final File jarFile = new File(versionDir, newFileName + ".jar");
		copy(fileInfo.getFile(), jarFile);
		final File pomFile = new File(versionDir, newFileName + ".pom");
		final File sourceFileTarget = new File(versionDir,
				fileInfo.getArtifactId() + "-" + fileInfo.getVersion(snapshot) + "-sources.jar");
		final File sourceFileSource = new File(sourceDir,
				fileInfo.getArtifactId() + "-" + fileInfo.getVersion() + "-sources.jar");
		final File javadocFile = new File(versionDir,
				fileInfo.getArtifactId() + "-" + fileInfo.getVersion(snapshot) + "-javadoc.jar");

		// create fake sources and javadoc jar

		if (sourceFileSource.exists()) {
			createJar(sourceFileTarget, sourceFileSource);
		} else {
			System.out.println("Creating fake source bundles for " + fileInfo.getArtifactId());
			createJar(sourceFileTarget, new File[] { readmeFile });
		}

		createJar(javadocFile, new File[] { readmeFile });

		createPomFile(fileInfo, snapshot, pomFile, dependsOn, externalDependencies);
		createAntFile(new File(versionDir, "build.xml"), pomFile, fileInfo.getArtifactId(), jarFile, snapshot,
				sourceFileTarget, javadocFile);

		globalScriptFileWriter.println("#");
		globalScriptFileWriter.print("pushd ");
		globalScriptFileWriter.println(versionDir);
		globalScriptFileWriter.println("$ANT_HOME/bin/ant --noconfig");
		globalScriptFileWriter.println("popd");
		globalBuildFileWriter.print("  <ant dir=\"");
		globalBuildFileWriter.print(versionDir);
		globalBuildFileWriter.print("\" target=\"");
		globalBuildFileWriter.print(snapshot ? "deploy" : "stage");
		globalBuildFileWriter.println("\" inheritAll=\"false\"/>");
		exec(new String[] { "/usr/bin/gpg", "-ab", "--batch", "--passphrase", passphrase, pomFile.getName() },
				versionDir);
		exec(new String[] { "/usr/bin/gpg", "-ab", "--batch", "--passphrase", passphrase, jarFile.getName() },
				versionDir);
		exec(new String[] { "/usr/bin/gpg", "-ab", "--batch", "--passphrase", passphrase, sourceFileTarget.getName() },
				versionDir);
		exec(new String[] { "/usr/bin/gpg", "-ab", "--batch", "--passphrase", passphrase, javadocFile.getName() },
				versionDir);
		// it would be nice to bundle the entire library in one jar but Sonatype doesn't
		// seem to want to accept multiple POM's in a single bundle.
		createJar(new File(versionDir, "bundle.jar"),
				new File[] { pomFile, jarFile, javadocFile, sourceFileTarget,
						new File(pomFile.getAbsolutePath() + ".asc"), new File(jarFile.getAbsolutePath() + ".asc"),
						new File(javadocFile.getAbsolutePath() + ".asc"),
						new File(sourceFileTarget.getAbsolutePath() + ".asc") });

		templatePomFileWriter.println("   <dependency>");
		templatePomFileWriter.print("    <groupId>");
		templatePomFileWriter.print(groupId);
		templatePomFileWriter.println("</groupId>");
		templatePomFileWriter.print("     <artifactId>");
		templatePomFileWriter.print(fileInfo.getArtifactId());
		templatePomFileWriter.println("</artifactId>");
		templatePomFileWriter.print("     <version>");
		templatePomFileWriter.print(fileInfo.getVersion(snapshot));
		templatePomFileWriter.println("</version>");
		templatePomFileWriter.println("   </dependency>");
	}

	private Manifest getManifest(final File file) throws IOException {
		final ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
		try {
			ZipEntry entry = zis.getNextEntry();
			while (entry != null) {
				// read the manifest to determine the name and version number
				// System.out.println(entry.getName() + " " + entry.isDirectory());
				if ("META-INF/MANIFEST.MF".equals(entry.getName()))
					return new Manifest(zis);
				entry = zis.getNextEntry();
			}
		} finally {
			zis.close();
		}
		return null;
	}

	private void createPomFile(final FileInfo fileInfo, final boolean snapshot, final File pomFile,
			final List<FileInfo> dependsOn, final Collection<ExternalDependency> externalDependencies)
			throws IOException {
		final String artifactName = fileInfo.getFile().getName();
		final PrintWriter pw = new PrintWriter(new FileWriter(pomFile));
		try {
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			pw.print("<project");
			pw.print(" xsi:schemaLocation=\"");
			pw.print("http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd");
			pw.print("\" xmlns=\"");
			pw.print("http://maven.apache.org/POM/4.0.0");
			pw.print("\" xmlns:xsi=\"");
			pw.print("http://www.w3.org/2001/XMLSchema-instance");
			pw.println("\">");
			pw.println("  <parent>");
			pw.println("    <groupId>org.sonatype.oss</groupId>");
			pw.println("    <artifactId>oss-parent</artifactId>");
			pw.println("    <version>7</version>");
			pw.println("  </parent>");
			pw.println("  <modelVersion>4.0.0</modelVersion>");
			pw.print("  <groupId>");
			pw.print(groupId);
			pw.println("</groupId>");
			pw.print("  <artifactId>");
			pw.print(fileInfo.getArtifactId());
			pw.println("</artifactId>");
			pw.print("  <version>");
			pw.print(fileInfo.getVersion(snapshot));
			pw.println("</version>");
			pw.println("  <packaging>jar</packaging>");
			pw.print("  <name>");
			pw.print(artifactName);
			pw.println("</name>");
			pw.println("  <description>A component of the BIRT runtime</description>");
			pw.println("  <url>http://www.eclipse.org/projects/project.php?id=birt</url>");
			pw.println("  <licenses>");
			pw.println("    <license>");
			pw.println("      <name>Eclipse Public License - v 2.0</name>");
			pw.println("      <url>https://www.eclipse.org/legal/epl-2.0.html</url>");
			pw.println("    </license>");
			pw.println("  </licenses>");
			pw.println("  <scm>");
			pw.println("    <url>http://git.eclipse.org/c/birt/org.eclipse.birt.git/</url>");
			pw.println("    <connection>http://git.eclipse.org/c/birt/org.eclipse.birt.git/</connection>");
			pw.println("  </scm>");
			pw.println("  <developers></developers>");
			if (dependsOn != null || externalDependencies != null) {
				pw.println("  <dependencies>");
				if (dependsOn != null) {
					for (final FileInfo childFileInfo : dependsOn) {
						pw.println("    <dependency>");
						pw.print("      <groupId>");
						pw.print(childFileInfo.getGroupId());
						pw.println("</groupId>");
						pw.print("       <artifactId>");
						pw.print(childFileInfo.getArtifactId());
						pw.println("</artifactId>");
						pw.print("       <version>");
						pw.print(childFileInfo.getVersion(snapshot));
						pw.println("</version>");
						pw.println("    </dependency>");
					}
				}
				if (externalDependencies != null) {
					for (final ExternalDependency externalDependency : externalDependencies) {
						pw.println("    <dependency>");
						pw.print("      <groupId>");
						pw.print(externalDependency.getGroupId());
						pw.println("</groupId>");
						pw.print("       <artifactId>");
						pw.print(externalDependency.getArtifactId());
						pw.println("</artifactId>");
						pw.print("       <version>");
						pw.print(externalDependency.getVersion());
						pw.println("</version>");
						pw.println("    </dependency>");
					}
				}
				pw.println("  </dependencies>");
			}
			pw.println("</project>");
		} finally {
			pw.close();
		}
	}

	private void createAntFile(final File antFile, final File pomFile, final String artifactId, final File jarFile,
			final boolean snapshot, final File sourceFile, final File javadocFile) throws IOException {
		final PrintWriter pw = new PrintWriter(new FileWriter(antFile));
		try {
			pw.print("<project name=\"");
			pw.print(artifactId);
			pw.print("\" default=\"");
			pw.print(snapshot ? "deploy" : "stage");
			pw.println("\" basedir=\".\" xmlns:artifact=\"antlib:org.apache.maven.artifact.ant\">");
			if (snapshot) {
				pw.println(" <property name=\"maven-snapshots-repository-id\" value=\"sonatype-nexus-snapshots\"/>");
				pw.println(
						" <property name=\"maven-snapshots-repository-url\" value=\"https://oss.sonatype.org/content/repositories/snapshots\"/>");
				pw.println(" <target name=\"deploy\">");

				pw.println("  <artifact:mvn>");
				pw.println("   <arg value=\"org.apache.maven.plugins:maven-deploy-plugin:2.6:deploy-file\"/>");
				pw.println("   <arg value=\"-Durl=${maven-snapshots-repository-url}\"/>");
				pw.println("   <arg value=\"-DrepositoryId=${maven-snapshots-repository-id}\"/>");
				pw.print("   <arg value=\"-DpomFile=");
				pw.print(pomFile);
				pw.println("\"/>");
				pw.print("   <arg value=\"-Dfile=");
				pw.print(jarFile);
				pw.println("\"/>");
				pw.println("  </artifact:mvn>");
				pw.println(" </target>");
			} else {
				pw.println(" <property name=\"maven-staging-repository-id\" value=\"sonatype-nexus-staging\" />");
				pw.println(
						" <property name=\"maven-staging-repository-url\" value=\"https://oss.sonatype.org/service/local/staging/deploy/maven2/\" />");
				pw.println(" <target name=\"stage\">");

				pw.println("  <artifact:mvn>");
				pw.println("   <arg value=\"org.apache.maven.plugins:maven-gpg-plugin:1.3:sign-and-deploy-file\" />");
				pw.println("   <arg value=\"-Durl=${maven-staging-repository-url}\" />");
				pw.println("   <arg value=\"-DrepositoryId=${maven-staging-repository-id}\" />");
				pw.print("   <arg value=\"-DpomFile=");
				pw.print(pomFile);
				pw.println("\"/>");
				pw.print("   <arg value=\"-Dfile=");
				pw.print(jarFile);
				pw.println("\"/>");
				pw.println("   <arg value=\"-Pgpg\"/>");
				pw.println("  </artifact:mvn>");

				pw.println("");
				pw.println("  <!-- deploy source jars -->");
				pw.println("  <artifact:mvn>");
				pw.println("   <arg value=\"org.apache.maven.plugins:maven-gpg-plugin:1.3:sign-and-deploy-file\" />");
				pw.println("   <arg value=\"-Durl=${maven-staging-repository-url}\"/>");
				pw.println("   <arg value=\"-DrepositoryId=${maven-staging-repository-id}\"/>");
				pw.print("   <arg value=\"-DpomFile=");
				pw.print(pomFile);
				pw.println("\"/>");
				pw.print("   <arg value=\"-Dfile=");
				pw.print(sourceFile);
				pw.println("\"/>");
				pw.println("   <arg value=\"-Dclassifier=sources\"/>");
				pw.println("   <arg value=\"-Pgpg\"/>");
				pw.println("  </artifact:mvn>");
				pw.println("");
				pw.println("  <!-- deploy javadoc jars -->");
				pw.println("  <artifact:mvn>");
				pw.println("   <arg value=\"org.apache.maven.plugins:maven-gpg-plugin:1.3:sign-and-deploy-file\" />");
				pw.println("   <arg value=\"-Durl=${maven-staging-repository-url}\"/>");
				pw.println("   <arg value=\"-DrepositoryId=${maven-staging-repository-id}\"/>");
				pw.print("   <arg value=\"-DpomFile=");
				pw.print(pomFile);
				pw.println("\"/>");
				pw.print("   <arg value=\"-Dfile=");
				pw.print(javadocFile);
				pw.println("\"/>");
				pw.println("   <arg value=\"-Dclassifier=javadoc\"/>");
				pw.println("   <arg value=\"-Pgpg\"/>");
				pw.println("  </artifact:mvn>");
				pw.println(" </target>");
			}
			pw.println("</project>");
		} finally {
			pw.close();
		}
	}

	private void createJar(final File jarFile, final File[] files) throws IOException {
		final Manifest manifest = new Manifest();
		final Attributes attributes = manifest.getMainAttributes();
		attributes.putValue("Manifest-Version", "1.0");
		attributes.putValue("Created-By", "RepoGen 1.0.0");
		final FileOutputStream fos = new FileOutputStream(jarFile);
		final JarOutputStream jos = new JarOutputStream(fos, manifest);
		for (final File file : files) {
			final ZipEntry entry = new ZipEntry(file.getName());
			jos.putNextEntry(entry);
			final FileInputStream fis = new FileInputStream(file);
			pipeStream(fis, jos);
			fis.close();
		}
		jos.close();
	}

	private void createJar(final File jarTargetFile, final File jarSourceFile) throws IOException {
		final FileOutputStream fos = new FileOutputStream(jarTargetFile);
		final FileInputStream fis = new FileInputStream(jarSourceFile);
		pipeStream(fis, fos);

		fis.close();
		fos.close();
	}

	private void exec(final String[] command, final File dir) throws IOException {
		final Process process = Runtime.getRuntime().exec(command, null, dir);
		try {
			process.waitFor();
		} catch (final InterruptedException e) {
		}
		if (process.exitValue() != 0) {
			System.out.println(command + " failed:");
			System.out.println("error stream:");
			pipeStream(process.getErrorStream(), System.out);
			System.out.println("output stream:");
			pipeStream(process.getInputStream(), System.out);
		}
	}

	private void pipeStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
		final byte[] buffer = new byte[0x1000];
		int bytesRead = inputStream.read(buffer);
		while (bytesRead >= 0) {
			outputStream.write(buffer, 0, bytesRead);
			bytesRead = inputStream.read(buffer);
		}
		inputStream.close();
	}

	private void deepDelete(final File file) {
		if (file == null)
			return;
		if (!file.exists())
			return;
		if (file.isDirectory())
			deepDelete(file.listFiles());
		file.delete();
	}

	private void deepDelete(final File[] files) {
		for (final File file : files)
			deepDelete(file);
	}

	private void readExternalDependency() throws IOException {

		File file = new File(externalFileName);

		if (!file.exists() || file.isDirectory())
			throw new FileNotFoundException();

		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp = null;
		temp = br.readLine();

		while (temp != null) {
			if (temp.startsWith("#") || temp.trim().equals("")) {
				temp = br.readLine();
				continue;
			} else {
				String exValue[] = temp.split(",");
				addExternalDependency(exValue[0].trim(), exValue[1].trim(), exValue[2].trim(), exValue[3].trim());
				System.out.println("Adding External Dependency: " + exValue[0]);
				temp = br.readLine();
			}
		}

	}

	private String trimVersion(final String version) {
		if (version == null)
			return "1";

		final String[] parts = version.split("\\.");
		final StringBuilder sb = new StringBuilder();
		String sep = "";
		for (int i = 0; i < parts.length && i < 3; i++) {
			final String part = parts[i];
			sb.append(sep);
			sep = ".";
			sb.append(part);
		}
		return sb.toString();

		// return version;
	}

	private void copy(final File sourceFile, final File destinationFile) throws IOException {
		destinationFile.createNewFile();
		final FileInputStream fis = new FileInputStream(sourceFile);
		try {
			final FileOutputStream fos = new FileOutputStream(destinationFile);
			try {
				final byte[] buffer = new byte[0x4000];
				int bytesRead = fis.read(buffer);
				while (bytesRead >= 0) {
					fos.write(buffer, 0, bytesRead);
					bytesRead = fis.read(buffer);
				}
			} finally {
				fos.close();
			}
		} finally {
			fis.close();
		}
	}
}
