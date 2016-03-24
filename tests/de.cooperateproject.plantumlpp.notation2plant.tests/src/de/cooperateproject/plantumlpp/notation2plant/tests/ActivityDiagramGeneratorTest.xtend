package de.cooperateproject.plantumlpp.notation2plant.tests

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecore.EClass;
import org.junit.Test
import org.junit.BeforeClass
import de.cooperateproject.notation2plant.ActivityDiagramGenerator
import java.util.List
import org.eclipse.gmf.runtime.notation.Diagram
import org.junit.Before
import java.util.Iterator
import org.junit.After
import org.junit.AfterClass

class ActivityDiagramGeneratorTest extends AbstractDiagramGeneratorTest{
	
	private static ActivityDiagramGenerator generator
	private static List<Diagram> diagrams;
	private Iterator<Diagram> diagramIterator;
	
	@BeforeClass
	def static void setUp() {
		generator = new ActivityDiagramGenerator()
		ressourceSetUp()
		diagrams = getDiagrams("testdiagrams/activityDiagrams.notation")
	}
	
	@Before
	def void getDiagramIterator() {
		diagramIterator = diagrams.iterator()
	}
	
	@After
	def void cleanDiagramIterator() {
		diagramIterator = null
	}
	
	@AfterClass
	def static void cleanUp() {
		generator = null
	}
	
	@Test
	def EmptyActivityTest() {
		assertDiagramEquals('''
		@startuml
		title EmptyActivity
		@enduml
		'''.toString, getDiagram(diagramIterator, "EmptyActivity"))
	}
	
	@Test
	def void InitialNodeTest() {
		assertDiagramEquals('''
		@startuml
		title InitialNode
		start
		@enduml
		'''.toString, getDiagram(diagramIterator, "InitialNode"))
	}
	
	@Test
	def void FinalNodeTest() {
		assertDiagramEquals('''
		@startuml
		title FinalNode
		start
		stop
		@enduml
		'''.toString, getDiagram(diagramIterator, "FinalNode"))
	}
	
	@Test
	def void SingleForkTest() {
		assertDiagramEquals('''
		@startuml
		title SingleFork
		start
		fork
		 :Action;
		end fork
		stop
		@enduml
		'''.toString, getDiagram(diagramIterator, "SingleFork"))
	}
	
	@Test
	def void MultipleForkTest() {
		assertDiagramEquals('''
		@startuml
		title MultipleFork
		start
		fork
		 :Action1;
		fork again
		 :Action2;
		end fork
		@enduml
		'''.toString, getDiagram(diagramIterator, "MultipleFork"))
	}
	
	@Test
	def void DecisionNodeTest() {		
		assertDiagramEquals('''
		@startuml
		title DecisionNode
		start
		if() then (p)
		 :Action;
		else (!p)
		endif
		stop
		@enduml
		'''.toString, getDiagram(diagramIterator, "DecisionNode"))
	}
	
	@Test
	def void MergeNodeTest() {
		assertDiagramEquals('''
		@startuml
		title MergeNode
		start
		if() then (!p)
		else (p)
		endif
		stop
		@enduml
		'''.toString, getDiagram(diagramIterator, "MergeNode"))
	}	
	
	@Test (expected = typeof(UnsupportedOperationException))
	def void unsupportedEObjectShapeTest() {
		var value = mock(typeof(EClass))
		generator.compileActivityDiagram(setUpDiagram(value, value))
	}
	
	@Test (expected = typeof(IllegalArgumentException))
	def void IllegalArgumentDeclarationTest() {
		var value = mock(typeof(EClass))
		generator.compileActivityDiagram(setUpDiagram(value, null))
	}
	
	/**
	 * Checks if the compiled diagram is the same as the expected.
	 * @expected the expected output
	 * @diagram the diagram to compile
	 */
	def void assertDiagramEquals(String expected, Diagram diagram) {
		assertEquals(expected, generator.compileActivityDiagram(diagram).toString)
	}
}