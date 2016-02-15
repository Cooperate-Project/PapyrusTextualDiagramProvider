package de.cooperateproject.plantumlpp.notation2plant.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.cooperateproject.notation2plant.ActivityDiagramGenerator;

public class ActivityDiagramGeneratorTest extends FileRessource{

	private static ActivityDiagramGenerator generator;
	private Iterable<Diagram> diagrams;
	
	@BeforeClass
	public static void setUp() {
		generator = new ActivityDiagramGenerator();
		ressourceSetUp();
	}
	
	@Before
	public void getDiagram() throws IOException {
		diagrams = getDiagrams("testdiagrams/activityDiagrams.notation");
	}
	
	@After
	public void cleanDiagram() {
		diagrams = null;
	}

	@Test
	public void nullDiagramTest() {
		generator.compileActivityDiagram(null);
	}
	
	@Test
	public void EmptyActivityTest() {
		Diagram d = getDiagram(diagrams, "EmptyActivity");
				
		assertEquals("@startuml\ntitle EmptyActivity\n@enduml\n", 
				generator.compileActivityDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void InitialNodeTest() {
		Diagram d = getDiagram(diagrams, "InitialNode");
		
		assertEquals("@startuml\ntitle InitialNode\nstart\n@enduml\n", 
				generator.compileActivityDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void FinalNodeTest() {
		Diagram d = getDiagram(diagrams, "FinalNode");
				
		assertEquals("@startuml\ntitle FinalNode\nstart\nstop\n@enduml\n", 
				generator.compileActivityDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void SingleForkTest() {
		Diagram d = getDiagram(diagrams, "SingleFork");
				
		assertEquals("@startuml\ntitle SingleFork\nstart\nfork\n :Action;\n"
				+ "end fork\nstop\n@enduml\n", 
				generator.compileActivityDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void MultipleForkTest() {
		Diagram d = getDiagram(diagrams, "MultipleFork");
				
		assertEquals("@startuml\ntitle MultipleFork\nstart\nfork\n :Action1;\n"
				+ "fork again\n :Action2;\nend fork\n@enduml\n", 
				generator.compileActivityDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void DecisionNodeTest() {
		Diagram d = getDiagram(diagrams, "DecisionNode");
				
		assertEquals("@startuml\ntitle DecisionNode\nstart\nif() then (p)\n "
				+ ":Action;\nelse (!p)\nendif\nstop\n@enduml\n", 
				generator.compileActivityDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void MergeNodeTest() {
		Diagram d = getDiagram(diagrams, "MergeNode");
		
		String s = generator.compileActivityDiagram(d).toString().replace("\r", "");
		s=s;
				
		assertEquals("@startuml\ntitle MergeNode\nstart\n"
				+ "if() then (!p)\nelse (p)\nendif\nstop\n@enduml\n", 
				generator.compileActivityDiagram(d).toString().replace("\r", ""));
	}
	
	/*@Test
	public void MultipleDecisionNodeTest() {
		Diagram d = getDiagram(diagrams, "MultipleDecisionNode");
		
		String s = generator.compileActivityDiagram(d).toString().replace("\r", "");
		s=s;
				
		assertEquals("@startuml\ntitle MultipleDecisionNode\nstart\nif() then (p1)\n "
				+ ":Action1;\nelse (!p1)\n if() then (p2)\n :Action2;\nelse (!p2)\n"
				+ "endif\nendif\nstop\n@enduml\n", 
				generator.compileActivityDiagram(d).toString().replace("\r", ""));
	}*/
	
	@Test (expected = UnsupportedOperationException.class)
	public void unsupportedEObjectShapeTest() {
		EClass value = mock(EClass.class);
		generator.compileActivityDiagram(setUpDiagram(value, value));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void IllegalArgumentDeclarationTest() {
		EClass value = mock(EClass.class);
		generator.compileActivityDiagram(setUpDiagram(value, null));
	}
	
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}
