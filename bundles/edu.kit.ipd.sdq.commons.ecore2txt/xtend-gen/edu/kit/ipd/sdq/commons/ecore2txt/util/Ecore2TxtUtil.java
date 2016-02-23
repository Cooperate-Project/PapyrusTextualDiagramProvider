/**
 * Copyright (c) 2015 Software Design and Quality (SDQ) research group
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    M. Kramer - initial API and implementation
 */
package edu.kit.ipd.sdq.commons.ecore2txt.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import edu.kit.ipd.sdq.commons.ecore2txt.generator.Ecore2TxtGenerator;
import edu.kit.ipd.sdq.vitruvius.framework.util.bridges.EMFBridge;
import edu.kit.ipd.sdq.vitruvius.framework.util.bridges.EclipseBridge;
import edu.kit.ipd.sdq.vitruvius.framework.util.datatypes.Quadruple;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

/**
 * A utility class for generating code from Ecore-based models triggered by an entry in the Eclipse navigator.
 */
@SuppressWarnings("all")
public class Ecore2TxtUtil {
  /**
   * Utility classes should not have a public or default constructor.
   */
  private Ecore2TxtUtil() {
  }
  
  public static void generateFromSelectedFilesInFolder(final Iterable<IFile> inputFiles, final Module generatorModule, final Ecore2TxtGenerator generator, final boolean concatOutputToSingleFile, final boolean postProcessContents) {
    final JavaIoFileSystemAccess fsa = Ecore2TxtUtil.createAndInjectFileSystemAccessIntoGeneratorModule(generatorModule);
    Iterable<Quadruple<String, String, String, IProject>> contentsForFolderAndFileNames = generator.generateContentsForFolderAndFileNamesInProject(inputFiles);
    if (concatOutputToSingleFile) {
      Iterable<Quadruple<String, String, String, IProject>> _concatOutputToFirstFile = Ecore2TxtUtil.concatOutputToFirstFile(contentsForFolderAndFileNames, generator, postProcessContents);
      contentsForFolderAndFileNames = _concatOutputToFirstFile;
    }
    Ecore2TxtUtil.outputToFolderFileNameAndProject(contentsForFolderAndFileNames, fsa);
  }
  
  public static JavaIoFileSystemAccess createAndInjectFileSystemAccessIntoGeneratorModule(final Module generatorModule) {
    final JavaIoFileSystemAccess fsa = new JavaIoFileSystemAccess();
    Injector _createInjector = Guice.createInjector(generatorModule);
    _createInjector.injectMembers(fsa);
    return fsa;
  }
  
  public static Iterable<Quadruple<String, String, String, IProject>> concatOutputToFirstFile(final Iterable<Quadruple<String, String, String, IProject>> contentsForFolderAndFileNames, final Ecore2TxtGenerator generator, final boolean postProcessContents) {
    final Iterator<Quadruple<String, String, String, IProject>> iterator = contentsForFolderAndFileNames.iterator();
    boolean _hasNext = iterator.hasNext();
    if (_hasNext) {
      final Quadruple<String, String, String, IProject> contentForFolderAndFileName = iterator.next();
      final String folderName = contentForFolderAndFileName.getSecond();
      final String fileName = contentForFolderAndFileName.getThird();
      final IProject project = contentForFolderAndFileName.getFourth();
      final StringBuilder contentBuilder = new StringBuilder();
      final Consumer<Quadruple<String, String, String, IProject>> _function = new Consumer<Quadruple<String, String, String, IProject>>() {
        public void accept(final Quadruple<String, String, String, IProject> it) {
          String _first = it.getFirst();
          contentBuilder.append(_first);
        }
      };
      contentsForFolderAndFileNames.forEach(_function);
      String contents = contentBuilder.toString();
      if (postProcessContents) {
        String _postProcessGeneratedContents = generator.postProcessGeneratedContents(contents);
        contents = _postProcessGeneratedContents;
      }
      Quadruple<String, String, String, IProject> _quadruple = new Quadruple<String, String, String, IProject>(contents, folderName, fileName, project);
      return Collections.<Quadruple<String, String, String, IProject>>unmodifiableList(CollectionLiterals.<Quadruple<String, String, String, IProject>>newArrayList(_quadruple));
    } else {
      throw new IllegalArgumentException((("Cannot concat the empty output \'" + contentsForFolderAndFileNames) + "\'!"));
    }
  }
  
  public static void outputToFolderFileNameAndProject(final Iterable<Quadruple<String, String, String, IProject>> contentsForFolderAndFileNames, final JavaIoFileSystemAccess fsa) {
    for (final Quadruple<String, String, String, IProject> contentForFolderAndFileName : contentsForFolderAndFileNames) {
      {
        final String content = contentForFolderAndFileName.getFirst();
        final String folderName = contentForFolderAndFileName.getSecond();
        final String fileName = contentForFolderAndFileName.getThird();
        final IProject project = contentForFolderAndFileName.getFourth();
        final IFolder genTargetFolder = EMFBridge.createFolderInProjectIfNecessary(project, folderName);
        final String absoluteGenFolderPath = EclipseBridge.getAbsolutePathString(genTargetFolder);
        fsa.setOutputPath(absoluteGenFolderPath);
        fsa.generateFile(fileName, content);
      }
    }
  }
}
