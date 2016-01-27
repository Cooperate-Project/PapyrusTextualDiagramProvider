package de.cooperateproject.plantumlpp.notation2plant.generator.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.cooperateproject.generator.PlantGenerator; 

public class PlantGeneratorTest {

	private static PlantGenerator generator;
	@BeforeClass
	public static void setUp() { 
		generator = new PlantGenerator();
	}
	@Test
	public void PlantGeneratorCompileNullTest() {		
		assertEquals("(empty)", generator.compile(null));
	}
	
	@Test
	public void PlantGeneratorEmptyDiagramTest() {
		Diagram diagram = mock(Diagram.class);
		when(diagram.getType())
			.thenReturn("PapyrusUMLClassDiagram", "PapyrusUMLActivityDiagram", "UseCase", "");
		when(diagram.getElement()).thenReturn(null);
		when(diagram.getChildren()).thenReturn(null);		
		
		EList<EObject> list = new BasicEList<EObject>();
		
		when(diagram.getEdges()).thenReturn(list);
		when(diagram.getChildren()).thenReturn(list);
		
		for(int i = 0; i< 3; i++) {
			assertEquals("@startuml\ntitle \n@enduml\n", 
					generator.compile(diagram).toString().replace("\r", ""));
		}	
		assertEquals("(empty)", generator.compile(diagram).toString().replace("\r", ""));
		
	}
	
	@Test
	public void doGenerateResourceNullTest() {
		IFileSystemAccess fsa = mock(IFileSystemAccess.class);
		generator.doGenerate(null, fsa);
	}
	@Test
	public void doGenerateFileSystemNullTest() {
		Resource resource = mock(Resource.class);
		generator.doGenerate(resource, null);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void doGenerateTest() {
		Resource resource = mock(Resource.class);
		IFileSystemAccess fsa = mock(IFileSystemAccess.class);
		TreeIterator<EObject> tree = mock(TreeIterator.class);
		Diagram diagram = mock(Diagram.class);
		
		when(resource.getAllContents()).thenReturn(tree);		
		when(diagram.getName()).thenReturn("");
		when(tree.next()).thenReturn(diagram);
		when(tree.hasNext()).thenReturn(true, false);
		
		generator.doGenerate(resource, fsa);
	}
	
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}