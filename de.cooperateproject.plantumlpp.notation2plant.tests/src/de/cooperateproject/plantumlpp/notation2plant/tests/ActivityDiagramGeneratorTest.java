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
	private static final String LINE_SEP = System.getProperty("line.separator");
	
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
				
		assertEquals("@startuml" 
				+ LINE_SEP + "title EmptyActivity" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileActivityDiagram(d).toString());
	}
	
	@Test
	public void InitialNodeTest() {
		Diagram d = getDiagram(diagrams, "InitialNode");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title InitialNode" 
				+ LINE_SEP + "start" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileActivityDiagram(d).toString());
	}
	
	@Test
	public void FinalNodeTest() {
		Diagram d = getDiagram(diagrams, "FinalNode");
				
		assertEquals("@startuml" 
				+ LINE_SEP + "title FinalNode" 
				+ LINE_SEP + "start" 
				+ LINE_SEP + "stop" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileActivityDiagram(d).toString());
	}
	
	@Test
	public void SingleForkTest() {
		Diagram d = getDiagram(diagrams, "SingleFork");
				
		assertEquals("@startuml" 
				+ LINE_SEP + "title SingleFork" 
				+ LINE_SEP + "start" 
				+ LINE_SEP + "fork" 
				+ LINE_SEP + " :Action;" 
				+ LINE_SEP + "end fork" 
				+ LINE_SEP + "stop" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileActivityDiagram(d).toString());
	}
	
	@Test
	public void MultipleForkTest() {
		Diagram d = getDiagram(diagrams, "MultipleFork");
				
		assertEquals("@startuml" 
				+ LINE_SEP + "title MultipleFork" 
				+ LINE_SEP + "start" 
				+ LINE_SEP + "fork" 
				+ LINE_SEP + " :Action1;" 
				+ LINE_SEP + "fork again" 
				+ LINE_SEP + " :Action2;" 
				+ LINE_SEP + "end fork" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileActivityDiagram(d).toString());
	}
	
	@Test
	public void DecisionNodeTest() {
		Diagram d = getDiagram(diagrams, "DecisionNode");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title DecisionNode" 
				+ LINE_SEP + "start" 
				+ LINE_SEP + "if() then (p)" 
				+ LINE_SEP + " :Action;" 
				+ LINE_SEP + "else (!p)" 
				+ LINE_SEP + "endif" 
				+ LINE_SEP + "stop" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileActivityDiagram(d).toString());
	}
	
	@Test
	public void MergeNodeTest() {
		Diagram d = getDiagram(diagrams, "MergeNode");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title MergeNode" 
				+ LINE_SEP + "start"
				+ LINE_SEP + "if() then (!p)" 
				+ LINE_SEP + "else (p)" 
				+ LINE_SEP + "endif" 
				+ LINE_SEP + "stop" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileActivityDiagram(d).toString());
	}
	
	/*@Test
	public void MultipleDecisionNodeTest() {
		Diagram d = getDiagram(diagrams, "MultipleDecisionNode");
		
		String s = generator.compileActivityDiagram(d).toString().replace("\r", "");
		s=s;
				
		assertEquals("@startuml" + LINE_SEP +"title MultipleDecisionNode" + LINE_SEP +"start" + LINE_SEP +"if() then (p1)" + LINE_SEP +" "
				+ ":Action1;" + LINE_SEP +"else (!p1)" + LINE_SEP +" if() then (p2)" + LINE_SEP +" :Action2;" + LINE_SEP +"else (!p2)" + LINE_SEP
				+ "endif" + LINE_SEP +"endif" + LINE_SEP +"stop" + LINE_SEP +"@enduml" + LINE_SEP, 
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
