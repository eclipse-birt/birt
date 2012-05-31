/*
Copyright (c) 2012 Innovent Solutions, Inc.

Unless otherwise indicated, all Content made available 
by Innovent Solutions, Inc  is provided to you under the terms and 
conditions of the Eclipse Public License Version 1.0 ("EPL"). A copy 
of the EPL is provided with this Content and is also available at 
http://www.eclipse.org/legal/epl-v10.html. For purposes of the EPL, 
"Program" will mean the Content.

Author: Steve Schafer
 */
package org.eclipse.birt.build.mavenrepogen;

import java.io.File;
import java.io.FileInputStream;
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

public class RepoGen
{
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
	
	private final Map<String, ExternalDependency> externalDependencies;

	private RepoGen(final File libDir, final File repoParentDir, final String groupId,
			final String passphrase, final boolean snapshot, final boolean release,
			final boolean clean, final String rootFileName, final String readmeFilePath ) throws IOException
	{
		this.libDir = libDir;
		this.groupId = groupId;
		this.passphrase = passphrase;
		this.readmeFile = new File(readmeFilePath);
		
		repoDir = new File(repoParentDir, "repository");
		repoDir.mkdir();
		groupDir = new File(repoDir, groupId);
		if (clean)
			deepDelete(groupDir);
		groupDir.mkdir();
		if (snapshot)
		{
			globalSnapshotBuildFile = new File(groupDir, "buildSnapshot.xml");
			globalSnapshotBuildFile.createNewFile();
			globalSnapshotScriptFile = new File(groupDir, "buildSnapshot.sh");
			globalSnapshotScriptFile.createNewFile();
			templateSnapshotPomFile = new File(groupDir, "templateSnapshotPomFile.xml");
			templateSnapshotPomFile.createNewFile();
		}
		else
		{
			globalSnapshotBuildFile = null;
			globalSnapshotScriptFile = null;
			templateSnapshotPomFile = null;
		}
		if (release)
		{
			globalReleaseBuildFile = new File(groupDir, "buildRelease.xml");
			globalReleaseBuildFile.createNewFile();
			globalReleaseScriptFile = new File(groupDir, "buildRelease.sh");
			globalReleaseScriptFile.createNewFile();
			templateReleasePomFile = new File(groupDir, "templateReleasePomFile.xml");
			templateReleasePomFile.createNewFile();
		}
		else
		{
			globalReleaseBuildFile = null;
			globalReleaseScriptFile = null;
			templateReleasePomFile = null;
		}
		this.rootFileName = rootFileName;
		externalDependencies = new HashMap<String, ExternalDependency>();
		addExternalDependency("commons-cli-1.0.jar", "commons-cli", "commons-cli", "1.0");
		//addExternalDependency("org.eclipse.core.resources_3.7.101.v20120125-1505.jar","org.jibx.config.3rdparty.org.eclipse", "org.eclipse.core.resources","3.7.101.v20120125-1505");
		addExternalDependency("flute.jar", "org.milyn", "flute", "1.3");
		// below for 3.7.2 release
		addExternalDependency("com.lowagie.text-2.1.7.jar","org.eclipse.birt.runtime.3_7_1","com.lowagie.text","2.1.7");
		addExternalDependency("derby-10.5.1000001.jar","org.eclipse.birt.runtime.3_7_1","derby","10.5.1000001");
		addExternalDependency("org.mozilla.javascript-1.7.2.jar","org.eclipse.birt.runtime.3_7_1","org.mozilla.javascript","1.7.2");
		addExternalDependency("javax.wsdl-1.5.1.jar","org.eclipse.birt.runtime.3_7_1","javax.wsdl","1.5.1");
		addExternalDependency("org.apache.batik.bridge-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.bridge","1.6.0");
		addExternalDependency("org.apache.batik.svggen-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.svggen","1.6.0");
		addExternalDependency("org.apache.batik.ext.awt-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.ext.awt","1.6.0");
		addExternalDependency("org.apache.batik.css-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.css","1.6.0");
		addExternalDependency("org.apache.batik.dom-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.dom","1.6.0");
		addExternalDependency("org.apache.batik.parser-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.parser","1.6.0");
		addExternalDependency("org.apache.batik.util.gui-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.util.gui","1.6.0");
		addExternalDependency("org.apache.batik.dom.svg-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.dom.svg","1.6.0");
		addExternalDependency("org.apache.batik.xml-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.xml","1.6.0");
		addExternalDependency("org.apache.batik.pdf-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.pdf","1.6.0");
        addExternalDependency("org.apache.batik.transcoder-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.transcoder","1.6.0");
        addExternalDependency("org.apache.batik.util-1.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.batik.util","1.6.0");
        addExternalDependency("org.apache.commons.codec-1.3.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.commons.codec","1.3.0");
        addExternalDependency("org.apache.xerces-2.9.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.xerces","2.9.0");
        addExternalDependency("org.apache.xml.resolver-1.2.0.jar","org.eclipse.birt.runtime.3_7_1","org.apache.xml.resolver","1.2.0");
        addExternalDependency("org.apache.xml.serializer-2.7.1.jar","org.eclipse.birt.runtime.3_7_1","org.apache.xml.serializer","2.7.1");
        /*
        addExternalDependency("org.eclipse.core.contenttype-3.4.100.jar","org.eclipse.birt.runtime.3_7_1","org.eclipse.core.contenttype","3.4.100");
        addExternalDependency("org.eclipse.core.expressions-3.4.300.jar","org.eclipse.birt.runtime.3_7_1","org.eclipse.core.expressions","3.4.300");
        addExternalDependency("org.eclipse.core.filesystem-1.3.100.jar","org.eclipse.birt.runtime.3_7_1","org.eclipse.core.filesystem","1.3.100");
        addExternalDependency("org.eclipse.core.runtime-3.7.0.jar","org.eclipse.birt.runtime.3_7_1","org.eclipse.core.runtime","3.7.0");
        addExternalDependency("org.eclipse.datatools.enablement.mysql-1.0.2.jar","org.eclipse.birt.runtime.3_7_1","org.eclipse.datatools.enablement.mysql","1.0.2");
        addExternalDependency("org.eclipse.equinox.app-1.3.100.jar","org.eclipse.birt.runtime.3_7_1","org.eclipse.equinox.app","1.3.100");
        addExternalDependency("org.eclipse.equinox.common-3.6.0.jar","org.eclipse.birt.runtime.3_7_1","org.eclipse.equinox.common","3.6.0");
        addExternalDependency("org.eclipse.equinox.registry-3.5.101.jar","org.eclipse.birt.runtime.3_7_1","org.eclipse.equinox.registry","3.5.101");
        addExternalDependency("org.eclipse.osgi.services-3.3.0.jar","org.eclipse.birt.runtime.3_7_1","org.eclipse.osgi.services","3.3.0");
        addExternalDependency("org.eclipse.update.configurator-3.3.100.jar","org.eclipse.birt.runtime.3_7_1","org.eclipse.update.configurator","3.3.100");
        */
        addExternalDependency("org.w3c.css.sac-1.3.0.jar","org.eclipse.birt.runtime.3_7_1","org.w3c.css.sac","1.3.0");
        addExternalDependency("org.w3c.dom.smil-1.0.0.jar","org.eclipse.birt.runtime.3_7_1","org.w3c.dom.smil","1.0.0");
        addExternalDependency("org.w3c.dom.svg-1.1.0.jar","org.eclipse.birt.runtime.3_7_1","org.w3c.dom.svg","1.1.0");
        addExternalDependency("Tidy-1.jar","org.eclipse.birt.runtime.3_7_1","Tidy","1");
        
        addExternalDependency("com.ibm.icu_4.4.2.v20110823.jar","org.eclipse.birt.runtime","com.ibm.icu","4.4.2.v20110823");
        
        addExternalDependency("org.eclipse.datatools.connectivity.apache.derby.dbdefinition_1.0.2.v201107221459.jar","org.eclipse.birt.runtime","org.eclipse.datatools.connectivity.apache.derby.dbdefinition","1.0.2.v201107221459");
		addExternalDependency("org.eclipse.datatools.connectivity.apache.derby_1.0.102.v201107221459.jar","org.eclipse.birt.runtime","org.eclipse.datatools.connectivity.apache.derby","1.0.102.v201107221459");
		addExternalDependency("org.eclipse.datatools.connectivity.console.profile_1.0.10.v201109250955.jar","org.eclipse.birt.runtime","org.eclipse.datatools.connectivity.console.profile","1.0.10.v201109250955");
		addExternalDependency("org.eclipse.datatools.connectivity.db.generic_1.0.1.v201107221459.jar","org.eclipse.birt.runtime","org.eclipse.datatools.connectivity.db.generic","1.0.1.v201107221459");
		addExternalDependency("org.eclipse.datatools.connectivity.dbdefinition.genericJDBC_1.0.1.v201107221459.jar","org.eclipse.birt.runtime","org.eclipse.datatools.connectivity.dbdefinition.genericJDBC","1.0.1.v201107221459");
		addExternalDependency("org.eclipse.datatools.connectivity.oda.consumer_3.2.5.v201109151100.jar","org.eclipse.birt.runtime","org.eclipse.datatools.connectivity.oda.consumer","3.2.5.v201109151100");
		addExternalDependency("org.eclipse.datatools.connectivity.oda.flatfile_3.1.2.v201112081200.jar","org.eclipse.birt.runtime","org.eclipse.datatools.connectivity.oda.flatfile","3.1.2.v201112081200");
		addExternalDependency("org.eclipse.datatools.connectivity.oda_3.3.3.v201110130935.jar","org.eclipse.birt.runtime","org.eclipse.datatools.connectivity.oda","3.3.3.v201110130935");
		addExternalDependency("org.eclipse.datatools.enablement.hsqldb.dbdefinition_1.0.0.v201107221502.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.hsqldb.dbdefinition","1.0.0.v201107221502");
		addExternalDependency("org.eclipse.datatools.enablement.hsqldb_1.0.0.v201107221502.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.hsqldb","1.0.0.v201107221502");
		addExternalDependency("org.eclipse.datatools.enablement.ibm.db2.luw.dbdefinition_1.0.4.v201107221502.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.ibm.db2.luw.dbdefinition","1.0.4.v201107221502");
		addExternalDependency("org.eclipse.datatools.enablement.ibm.db2.luw_1.0.2.v201107221502.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.ibm.db2.luw","1.0.2.v201107221502");
		addExternalDependency("org.eclipse.datatools.enablement.ibm.informix.dbdefinition_1.0.4.v201107221502.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.ibm.informix.dbdefinition","1.0.4.v201107221502");
		addExternalDependency("org.eclipse.datatools.enablement.ibm.informix_1.0.1.v201107221502.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.ibm.informix","1.0.1.v201107221502");
		addExternalDependency("org.eclipse.datatools.enablement.msft.sqlserver_1.0.1.v201107221504.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.msft.sqlserver","1.0.1.v201107221504");
		addExternalDependency("org.eclipse.datatools.enablement.mysql.dbdefinition_1.0.4.v201109022331.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.mysql.dbdefinition","1.0.4.v201109022331");
		addExternalDependency("org.eclipse.datatools.enablement.oda.xml_1.2.3.v201112061438.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.oda.xml","1.2.3.v201112061438");
		addExternalDependency("org.eclipse.datatools.enablement.oracle_1.0.0.v201107221506.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.oracle","1.0.0.v201107221506");
		addExternalDependency("org.eclipse.datatools.enablement.postgresql.dbdefinition_1.0.2.v201110070445.jar","org.eclipse.birt.runtime","org.eclipse.datatools.enablement.postgresql.dbdefinition","1.0.2.v201110070445");
		addExternalDependency("org.eclipse.datatools.modelbase.dbdefinition_1.0.2.v201107221519.jar","org.eclipse.birt.runtime","org.eclipse.datatools.modelbase.dbdefinition","1.0.2.v201107221519");
		addExternalDependency("org.eclipse.datatools.modelbase.derby_1.0.0.v201107221519.jar","org.eclipse.birt.runtime","org.eclipse.datatools.modelbase.derby","1.0.0.v201107221519");
		addExternalDependency("org.eclipse.datatools.modelbase.sql.query_1.1.2.v201110151315.jar","org.eclipse.birt.runtime","org.eclipse.datatools.modelbase.sql.query","1.1.2.v201110151315");
		addExternalDependency("org.eclipse.datatools.modelbase.sql_1.0.5.v201110151330.jar","org.eclipse.birt.runtime","org.eclipse.datatools.modelbase.sql","1.0.5.v201110151330");
 
	}

	private void addExternalDependency(final String fileName, final String groupId,
			final String artifactId, final String version)
	{
		externalDependencies.put(fileName, new ExternalDependency(fileName, groupId, artifactId,
				version));
	}

	public static void main(final String[] args) throws IOException
	{
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
		try
		{
			properties.load(fr);
		}
		finally
		{
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
		
		final RepoGen repoGen = new RepoGen(new File(libDirName), new File(repoDirName), groupId,
				passphrase, genSnapshot, genRelease, clean, rootFileName,readmeFilePath);
		repoGen.generate();
	}

	private void generate() throws IOException
	{
		final PrintWriter globalSnapshotBuildFileWriter = createGlobalBuildFileWriter(
			globalSnapshotBuildFile, "deploy");
		final PrintWriter globalSnapshotScriptFileWriter = createGlobalScriptFileWriter(globalSnapshotScriptFile);
		final PrintWriter globalReleaseBuildFileWriter;
		if (globalReleaseBuildFile != null)
		{
			globalReleaseBuildFileWriter = new PrintWriter(new FileWriter(globalReleaseBuildFile));
			globalReleaseBuildFileWriter.print("<project name=\"");
			globalReleaseBuildFileWriter.print(groupId);
			globalReleaseBuildFileWriter.println("\" default=\"stage\" basedir=\".\" xmlns:artifact=\"antlib:org.apache.maven.artifact.ant\">");
			globalReleaseBuildFileWriter.println(" <target name=\"stage\">");
		}
		else
		{
			globalReleaseBuildFileWriter = null;
		}
		final PrintWriter globalReleaseScriptFileWriter = createGlobalScriptFileWriter(globalReleaseScriptFile);
		final PrintWriter templateSnapshotPomWriter = createTemplatePomWriter(
			templateSnapshotPomFile, true);
		final PrintWriter templateReleasePomWriter = createTemplatePomWriter(
			templateReleasePomFile, false);
		File rootFile = null;
		final List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		final File[] files = libDir.listFiles();
		if (files != null)
		{
			for (final File file : files)
			{
				if (rootFileName != null && rootFileName.equals(file.getName()))
					rootFile = file;
				else if (!externalDependencies.containsKey(file.getName()))
				{
					final FileInfo fileInfo = getFileInfo(file);
					if (fileInfo != null)
					{
						fileInfos.add(fileInfo);
						generateFile(fileInfo, true, globalSnapshotBuildFileWriter,
							globalSnapshotScriptFileWriter, templateSnapshotPomWriter, null, null);
						generateFile(fileInfo, false, globalReleaseBuildFileWriter,
							globalReleaseScriptFileWriter, templateReleasePomWriter, null, null);
					}
				}
			}
		}
		if (rootFile != null)
		{
			final FileInfo fileInfo = getFileInfo(rootFile);
			generateFile(fileInfo, true, globalSnapshotBuildFileWriter,
				globalSnapshotScriptFileWriter, templateSnapshotPomWriter, fileInfos,
				externalDependencies.values());
			generateFile(fileInfo, false, globalReleaseBuildFileWriter,
				globalReleaseScriptFileWriter, templateReleasePomWriter, fileInfos,
				externalDependencies.values());
		}
		closeTemplatePomWriter(templateSnapshotPomWriter);
		closeTemplatePomWriter(templateReleasePomWriter);
		closeBuildFileWriter(globalSnapshotBuildFileWriter);
		closeBuildFileWriter(globalReleaseBuildFileWriter);
		closeScriptFileWriter(globalSnapshotScriptFileWriter);
		closeScriptFileWriter(globalReleaseScriptFileWriter);
	}

	private PrintWriter createGlobalScriptFileWriter(final File file) throws IOException
	{
		if (file == null)
			return null;
		final PrintWriter writer = new PrintWriter(new FileWriter(file));
		writer.println("# Execute all the builds.");
		// TODO parameterize these
		writer.println("export ANT_OPTS=\"-XX:MaxPermSize=256m\"");
		//writer.println("export ANT_HOME=~/java/apache-ant-1.8.2");
		return writer;
	}

	private void closeBuildFileWriter(final PrintWriter writer)
	{
		if (writer != null)
		{
			writer.println(" </target>");
			writer.println("</project>");
			writer.close();
		}
	}

	private void closeScriptFileWriter(final PrintWriter writer)
	{
		if (writer != null)
		{
			writer.println("# done.");
			writer.close();
		}
	}

	private void closeTemplatePomWriter(final PrintWriter writer)
	{
		if (writer != null)
		{
			for (final ExternalDependency externalDependency : externalDependencies.values())
			{
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

	private PrintWriter createGlobalBuildFileWriter(final File file, final String string)
			throws IOException
	{
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

	private PrintWriter createTemplatePomWriter(final File file, final boolean snapshot)
			throws IOException
	{
		if (file == null)
			return null;
		final PrintWriter writer = new PrintWriter(new FileWriter(file));
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<project");
		writer.println(" xmlns=\"http://maven.apache.org/POM/4.0.0\"");
		writer.println(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		writer.println(" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">");
		writer.println(" <modelVersion>4.0.0</modelVersion>");
		writer.println(" <repositories>");
		writer.println("  <repository>");
		if (snapshot)
		{
			writer.println("   <id>sonatype-nexus-snapshots</id>");
			writer.println("   <name>Sonatype Nexus Snapshots</name>");
			writer.println("   <url>https://oss.sonatype.org/content/repositories/snapshots/</url>");
		}
		else
		{
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

	private FileInfo getFileInfo(final File file) throws IOException
	{
		if (file.isDirectory())
			return null;
		if (!file.getAbsolutePath().toLowerCase().endsWith(".jar"))
			return null;
		System.out.println(file);
		final Manifest manifest = getManifest(file);
		String artifactId;
		String version;
		if (manifest == null)
		{
			artifactId = file.getName();
			final int indexOfDot = artifactId.lastIndexOf(".");
			if (indexOfDot >= 0)
				artifactId = artifactId.substring(0, indexOfDot);
			version = "1";
		}
		else
		{
			final Attributes mainAttributes = manifest.getMainAttributes();
			artifactId = mainAttributes.getValue("Bundle-SymbolicName");
			if (artifactId != null)
			{
				final int indexofsemicolon = artifactId.indexOf(";");
				if (indexofsemicolon >= 0)
					artifactId = artifactId.substring(0, indexofsemicolon);
				version = trimVersion(mainAttributes.getValue("Bundle-Version"));
			}
			else
			{
				artifactId = mainAttributes.getValue("Specification-Title");
				if (artifactId != null)
				{
					version = mainAttributes.getValue("Specification-Version");
				}
				else
				{
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

	private void generateFile(final FileInfo fileInfo, final boolean snapshot,
			final PrintWriter globalBuildFileWriter, final PrintWriter globalScriptFileWriter,
			final PrintWriter templatePomFileWriter, final List<FileInfo> dependsOn,
			final Collection<ExternalDependency> externalDependencies) throws IOException
	{
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
		final File sourceFile = new File(versionDir, fileInfo.getArtifactId() + "-" + fileInfo.getVersion(snapshot)+"-sources.jar");
		final File javadocFile = new File(versionDir, fileInfo.getArtifactId() + "-" + fileInfo.getVersion(snapshot)+"-javadoc.jar");
		//create fake sources and javadoc jar
		createJar( sourceFile, new File[]{readmeFile} );
		createJar( javadocFile, new File[]{readmeFile} );
		
		createPomFile(fileInfo, snapshot, pomFile, dependsOn, externalDependencies);
		createAntFile(new File(versionDir, "build.xml"), pomFile, fileInfo.getArtifactId(),
			jarFile, snapshot, sourceFile, javadocFile);
		
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
		exec(
			new String[] { "/usr/bin/gpg", "-ab", "--batch", "--passphrase", passphrase,
				pomFile.getName() }, versionDir);
		exec(
			new String[] { "/usr/bin/gpg", "-ab", "--batch", "--passphrase", passphrase,
				jarFile.getName() }, versionDir);
		exec(
				new String[] { "/usr/bin/gpg", "-ab", "--batch", "--passphrase", passphrase,
					sourceFile.getName() }, versionDir);
		exec(
				new String[] { "/usr/bin/gpg", "-ab", "--batch", "--passphrase", passphrase,
					javadocFile.getName() }, versionDir);
		// it would be nice to bundle the entire library in one jar but Sonatype doesn't
		// seem to want to accept multiple POM's in a single bundle.
		createJar(new File(versionDir, "bundle.jar"), new File[] { pomFile, jarFile,javadocFile,sourceFile,
			new File(pomFile.getAbsolutePath() + ".asc"),
			new File(jarFile.getAbsolutePath() + ".asc"),
			new File(javadocFile.getAbsolutePath() + ".asc"),
			new File(sourceFile.getAbsolutePath() + ".asc")});
		

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

	private Manifest getManifest(final File file) throws IOException
	{
		final ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
		try
		{
			ZipEntry entry = zis.getNextEntry();
			while (entry != null)
			{
				// read the manifest to determine the name and version number
				// System.out.println(entry.getName() + " " + entry.isDirectory());
				if ("META-INF/MANIFEST.MF".equals(entry.getName()))
					return new Manifest(zis);
				entry = zis.getNextEntry();
			}
		}
		finally
		{
			zis.close();
		}
		return null;
	}

	private void createPomFile(final FileInfo fileInfo, final boolean snapshot, final File pomFile,
			final List<FileInfo> dependsOn,
			final Collection<ExternalDependency> externalDependencies) throws IOException
	{
		final String artifactName = fileInfo.getFile().getName();
		final PrintWriter pw = new PrintWriter(new FileWriter(pomFile));
		try
		{
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
			pw.println("      <name>Eclipse Public License - v 1.0</name>");
			pw.println("      <url>http://www.eclipse.org/org/documents/epl-v10.html</url>");
			pw.println("    </license>");
			pw.println("  </licenses>");
			pw.println("  <scm>");
			pw.println("    <url>http://git.eclipse.org/c/birt/org.eclipse.birt.git/</url>");
			pw.println("    <connection>http://git.eclipse.org/c/birt/org.eclipse.birt.git/</connection>");
			pw.println("  </scm>");
			pw.println("  <developers></developers>");
			if (dependsOn != null || externalDependencies != null)
			{
				pw.println("  <dependencies>");
				if (dependsOn != null)
				{
					for (final FileInfo childFileInfo : dependsOn)
					{
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
				if (externalDependencies != null)
				{
					for (final ExternalDependency externalDependency : externalDependencies)
					{
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
		}
		finally
		{
			pw.close();
		}
	}

	private void createAntFile(final File antFile, final File pomFile, final String artifactId,
			final File jarFile, final boolean snapshot, final File sourceFile, final File javadocFile) throws IOException
	{
		final PrintWriter pw = new PrintWriter(new FileWriter(antFile));
		try
		{
			pw.print("<project name=\"");
			pw.print(artifactId);
			pw.print("\" default=\"");
			pw.print(snapshot ? "deploy" : "stage");
			pw.println("\" basedir=\".\" xmlns:artifact=\"antlib:org.apache.maven.artifact.ant\">");
			if (snapshot)
			{
				pw.println(" <property name=\"maven-snapshots-repository-id\" value=\"sonatype-nexus-snapshots\"/>");
				pw.println(" <property name=\"maven-snapshots-repository-url\" value=\"https://oss.sonatype.org/content/repositories/snapshots\"/>");
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
			}
			else
			{
				pw.println(" <property name=\"maven-staging-repository-id\" value=\"sonatype-nexus-staging\" />");
				pw.println(" <property name=\"maven-staging-repository-url\" value=\"https://oss.sonatype.org/service/local/staging/deploy/maven2/\" />");
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
		}
		finally
		{
			pw.close();
		}
	}

	private void createJar(final File jarFile, final File[] files) throws IOException
	{
		final Manifest manifest = new Manifest();
		final Attributes attributes = manifest.getMainAttributes();
		attributes.putValue("Manifest-Version", "1.0");
		attributes.putValue("Created-By", "RepoGen 1.0.0");
		final FileOutputStream fos = new FileOutputStream(jarFile);
		final JarOutputStream jos = new JarOutputStream(fos, manifest);
		for (final File file : files)
		{
			final ZipEntry entry = new ZipEntry(file.getName());
			jos.putNextEntry(entry);
			final FileInputStream fis = new FileInputStream(file);
			pipeStream(fis, jos);
			fis.close();
		}
		jos.close();
	}

	private void exec(final String[] command, final File dir) throws IOException
	{
		final Process process = Runtime.getRuntime().exec(command, null, dir);
		try
		{
			process.waitFor();
		}
		catch (final InterruptedException e)
		{
		}
		if (process.exitValue() != 0)
		{
			System.out.println(command + " failed:");
			System.out.println("error stream:");
			pipeStream(process.getErrorStream(), System.out);
			System.out.println("output stream:");
			pipeStream(process.getInputStream(), System.out);
		}
	}

	private void pipeStream(final InputStream inputStream, final OutputStream outputStream)
			throws IOException
	{
		final byte[] buffer = new byte[0x1000];
		int bytesRead = inputStream.read(buffer);
		while (bytesRead >= 0)
		{
			outputStream.write(buffer, 0, bytesRead);
			bytesRead = inputStream.read(buffer);
		}
		inputStream.close();
	}

	private void deepDelete(final File file)
	{
		if (file == null)
			return;
		if (!file.exists())
			return;
		if (file.isDirectory())
			deepDelete(file.listFiles());
		file.delete();
	}

	private void deepDelete(final File[] files)
	{
		for (final File file : files)
			deepDelete(file);
	}

	private String trimVersion(final String version)
	{
		if (version == null)
			return "1";
		/*
		final String[] parts = version.split("\\.");
		final StringBuilder sb = new StringBuilder();
		String sep = "";
		for (int i = 0; i < parts.length && i < 3; i++)
		{
			final String part = parts[i];
			sb.append(sep);
			sep = ".";
			sb.append(part);
		}
		return sb.toString();
		*/
		return version;
	}

	private void copy(final File sourceFile, final File destinationFile) throws IOException
	{
		destinationFile.createNewFile();
		final FileInputStream fis = new FileInputStream(sourceFile);
		try
		{
			final FileOutputStream fos = new FileOutputStream(destinationFile);
			try
			{
				final byte[] buffer = new byte[0x4000];
				int bytesRead = fis.read(buffer);
				while (bytesRead >= 0)
				{
					fos.write(buffer, 0, bytesRead);
					bytesRead = fis.read(buffer);
				}
			}
			finally
			{
				fos.close();
			}
		}
		finally
		{
			fis.close();
		}
	}
}
