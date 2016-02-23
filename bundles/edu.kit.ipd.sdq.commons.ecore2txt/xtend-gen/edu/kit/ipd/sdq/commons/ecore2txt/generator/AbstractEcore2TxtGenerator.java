package edu.kit.ipd.sdq.commons.ecore2txt.generator;

import edu.kit.ipd.sdq.commons.ecore2txt.generator.Ecore2TxtGenerator;
import edu.kit.ipd.sdq.vitruvius.framework.util.bridges.EMFBridge;
import edu.kit.ipd.sdq.vitruvius.framework.util.bridges.EcoreResourceBridge;
import edu.kit.ipd.sdq.vitruvius.framework.util.datatypes.Pair;
import edu.kit.ipd.sdq.vitruvius.framework.util.datatypes.Quadruple;
import java.util.ArrayList;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

@SuppressWarnings("all")
public abstract class AbstractEcore2TxtGenerator implements Ecore2TxtGenerator {
  public Iterable<Quadruple<String, String, String, IProject>> generateContentsForFolderAndFileNamesInProject(final Iterable<IFile> inputFiles) {
    final ResourceSetImpl resourceSet = new ResourceSetImpl();
    final ArrayList<Quadruple<String, String, String, IProject>> results = new ArrayList<Quadruple<String, String, String, IProject>>();
    for (final IFile inputFile : inputFiles) {
      {
        final URI inputURI = EMFBridge.getEMFPlatformUriForIResource(inputFile);
        final Resource inputResource = EcoreResourceBridge.loadResourceAtURI(inputURI, resourceSet);
        final String folderName = this.getFolderNameForResource(inputResource);
        final IProject project = this.getProjectForFile(inputFile);
        final Iterable<Pair<String, String>> contentsAndFileNames = this.generateContentsFromResource(inputResource);
        for (final Pair<String, String> contentAndFileName : contentsAndFileNames) {
          {
            final String content = contentAndFileName.getFirst();
            final String fileName = contentAndFileName.getSecond();
            final Quadruple<String, String, String, IProject> result = new Quadruple<String, String, String, IProject>(content, folderName, fileName, project);
            results.add(result);
          }
        }
      }
    }
    return results;
  }
  
  /**
   * @return an iterable of pairs of generated contents and file names
   */
  public abstract Iterable<Pair<String, String>> generateContentsFromResource(final Resource inputResource);
  
  public abstract String getFolderNameForResource(final Resource inputResource);
  
  public abstract String getFileNameForResource(final Resource inputResource);
  
  public IProject getProjectForFile(final IFile inputFile) {
    return inputFile.getProject();
  }
}
