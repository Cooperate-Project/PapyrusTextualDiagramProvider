package de.cooperateproject.plantumlpp.notation2plant.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Iterables;

import de.cooperateproject.notation2plant.ActivityDiagramGenerator;

public class ActivityDiagramGeneratorTest {

	private static ActivityDiagramGenerator generator;
	@BeforeClass
	public static void setUp() {
		generator = new ActivityDiagramGenerator();
	}
	@Test
	public void nullDiagramTest() {
		generator.compileActivityDiagram(null);
	}
	/*@Test
	public void test1() throws IOException {
		List<IFile> list = new ArrayList<IFile>();
		File f = new File("testdiagrams/activityDiagrams.di");
		//list.add((IFile) f);
		
		
		ResourceSet resSet = new ResourceSetImpl();
		IPath modelPath = (new Path(f.getAbsolutePath())).removeFileExtension();	
    	IPath p = modelPath.addFileExtension("uml");
    	String s = p.toOSString();
	    File umlFile = new File(s);
	    String s2 = umlFile.getAbsolutePath();
	    URI inputModelURI = URI.createFileURI(s2);
	    Resource modelResource = resSet.getResource(inputModelURI, true);
	    modelResource.load(null);
	    
	    File notationFile = new File(modelPath.addFileExtension("notation").toOSString());
		URI inputNotationURI = URI.createFileURI(notationFile.getAbsolutePath());
		Resource notationResource = resSet.getResource(inputNotationURI, true);
		notationResource.load(null);
		EcoreUtil.resolveAll(notationResource);  
		
		TreeIterator<EObject> _allContents = notationResource.getAllContents();
	    Iterable<EObject> _iterable = IteratorExtensions.<EObject>toIterable(_allContents);
	    Iterable<Diagram> _filter = Iterables.<Diagram>filter(_iterable, Diagram.class);
	    _filter = _filter;
	}*/
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}
