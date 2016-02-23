package edu.kit.ipd.sdq.commons.ecore2txt.generator;

import edu.kit.ipd.sdq.vitruvius.framework.util.datatypes.Quadruple;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

@SuppressWarnings("all")
public interface Ecore2TxtGenerator {
  /**
   * @return an iterable of quadruples that bundle a content string that should be generated together with a folder name, a file name, and a project to specify where the content shall be put
   */
  public abstract Iterable<Quadruple<String, String, String, IProject>> generateContentsForFolderAndFileNamesInProject(final Iterable<IFile> inputFiles);
  
  public abstract String postProcessGeneratedContents(final String contents);
}
