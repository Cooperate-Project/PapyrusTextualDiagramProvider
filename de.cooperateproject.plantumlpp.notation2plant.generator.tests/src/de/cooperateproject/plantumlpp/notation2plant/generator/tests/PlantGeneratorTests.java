package de.cooperateproject.plantumlpp.notation2plant.generator.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.cooperateproject.generator.PlantGenerator;

public class PlantGeneratorTests {

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
	@SuppressWarnings("unchecked")
	public void PlantGeneratorEmptyDiagramTest() {
		Diagram diagram = mock(Diagram.class);
		when(diagram.getType())
			.thenReturn("PapyrusUMLClassDiagram", "PapyrusUMLClassDiagram", "UseCase");
		when(diagram.getElement()).thenReturn(null);
		when(diagram.getChildren()).thenReturn(null);
		
		EList<EObject> l = mock(EList.class);
		Iterator<EObject> i = mock(Iterator.class);
		when(l.iterator()).thenReturn(i);
		
		when(diagram.getEdges()).thenReturn(l);
		when(diagram.getChildren()).thenReturn(l);
		
		assertEquals("@startuml\r\ntitle \r\n@enduml\r\n", 
				generator.compile(diagram).toString());
		assertEquals("@startuml\r\ntitle \r\n@enduml\r\n", 
				generator.compile(diagram).toString());
		assertEquals("@startuml\r\ntitle \r\n@enduml\r\n", 
				generator.compile(diagram).toString());
	}
	
	@Test
	public void doGenerateNullTest() {
		generator.doGenerate(null, null);
	}
	/*@SuppressWarnings("unchecked")
	@Test
	public void doGenerateTest() {
		Resource resource = mock(Resource.class);
		TreeIterator tree = mock(TreeIterator.class);
		when(resource.getAllContents()).thenReturn(tree);		
		
		/*Iterable<EObject> iter = IteratorExtensions.<EObject>toIterable(tree);
		Iterable<Diagram> iter2 = Iterables.<Diagram>filter(iter, Diagram.class);
		
		Diagram d = mock(Diagram.class);	
		Iterator<Diagram> iterD = iter2.iterator();
		when(iterD.next()).thenReturn(d);
		
		
		generator.doGenerate(resource, null);
	}*/
	
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}