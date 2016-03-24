package de.cooperateproject.plantumlpp.notation2plant.tests

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import de.cooperateproject.notation2plant.ClassDiagramGenerator

import org.eclipse.emf.ecore.EClass;
import org.junit.Test
import org.junit.BeforeClass
import java.util.List
import org.eclipse.gmf.runtime.notation.Diagram
import org.junit.Before
import java.util.Iterator
import org.junit.After
import org.junit.AfterClass
import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.ecore.EObject
import org.eclipse.gmf.runtime.notation.Connector

class ClassDiagramGeneratorTest extends AbstractDiagramGeneratorTest{
	private static ClassDiagramGenerator generator
	private static List<Diagram> diagrams
	private Iterator<Diagram> diagramIterator
	
	@BeforeClass
	def static void setUp() {
		generator = new ClassDiagramGenerator()
		ressourceSetUp()
		diagrams = getDiagrams("testdiagrams/class.notation")		
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
	def void nullDiagramTest() {
		generator.compileClassDiagram(null)
	}
	
	
	@Test
	def void ClassAttributOperationTest() {
		assertDiagramEquals('''
		@startuml
		title ClassAttrOp
		class A {
			+Attribute1: String
			+Attribute2: String[]
			+Operation1()
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "ClassAttrOp"))
	}
	
	@Test
	def void InterfaceTest() {		
		assertDiagramEquals('''
		@startuml
		title Interface
		interface Interface {
			+Attribute1
			+Operation1()
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "Interface"))
	}
	
	@Test
	def void EmptyPackageTest() {		
		assertDiagramEquals('''
		@startuml
		title EmptyPackage
		package Package {
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "EmptyPackage"));
	}
	
	@Test
	def void PackageWithClassTest() {
		assertDiagramEquals('''
		@startuml
		title PackageClass
		package Package {
			class Class {
			}
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "PackageClass"));
	}
	
	@Test
	def void EnumerationTest() {
		assertDiagramEquals('''
		@startuml
		title Enumeration
		enum Enumeration {
			EnumerationLiteral1
			EnumerationLiteral2
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "Enumeration"));
	}
	
	@Test
	def void DataTypeTest() {
		assertDiagramEquals('''
		@startuml
		title DataType
		class DataType << (D,blue) >> {
			+Attribute1
			+Operation1()
		}
		@enduml
		''', getDiagram(diagramIterator, "DataType"));
	}
	
	@Test
	def void SignalTest() {				
		assertDiagramEquals('''
		@startuml
		title Signal
		class Signal << (S,red) >> {
			+Attribute1
			+Attribute2
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "Signal"));
	}
	
	@Test
	def void InformationItemTest() {				
		assertDiagramEquals('''
		@startuml
		title InformationItem
		class InformationItem << (>,orchid) >> {
		}
		@enduml
		'''.toString,	getDiagram(diagramIterator, "InformationItem"));
	}
	
	@Test
	def void InstanceSpecificationTest() {
		assertDiagramEquals('''
		@startuml
		title InstanceSpecification
		class "InstanceSpecification:" << (i,pink) >> {
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "InstanceSpecification"));
	}
	
	@Test
	def void CommentTest() {
		assertDiagramEquals('''
		@startuml
		title Comment
		class Class {
		}
		note as Ncom
			comment
		end note
			Ncom..Class
		@enduml
		'''.toString, getDiagram(diagramIterator, "Comment"));
	}
	
	@Test
	def void DurationObservationTest() {
		assertDiagramEquals('''
		@startuml
		title DurationObservation
		note as NDurationDurationObservation
			
		end note
		@enduml
		'''.toString, getDiagram(diagramIterator, "DurationObservation"));
	}
	
	@Test
	def void DurationObservationWithEventTest() {
		assertDiagramEquals('''
		@startuml
		title DurationObservationWithEvent
		note as NDurationDurationObservation
			
		end note
			NDurationDurationObservation..A
		class A {
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "DurationObservationWithEvent"));
	}
	
	@Test
	def void ClassAssociationTest() {		
		assertDiagramEquals('''
		@startuml
		title ClassAssociation
		class A {
		}
		class B {
		}
		B "b  *" <-->  "a  0..1" A : label
		@enduml
		'''.toString, getDiagram(diagramIterator, "ClassAssociation"));
	}
	
	@Test
	def void GeneralizationTest() {
		assertDiagramEquals('''
		@startuml
		title Generalization
		abstract class A {
		}
		class B {
		}
		A <|-- B
		@enduml
		'''.toString, getDiagram(diagramIterator, "Generalization"));
	}
	
	@Test
	def void RealizationTest() {
		assertDiagramEquals('''
		@startuml
		title Realization
		class B {
		}
		interface A {
		}
		A <|-- B
		@enduml
		'''.toString, getDiagram(diagramIterator, "Realization"));
	}
	
	@Test
	def void SharedAssociationTest() {
		assertDiagramEquals('''
		@startuml
		title SharedAssociation
		class A {
		}
		class B {
		}
		class C {
		}
		class D {
		}
		A "a  1..*" o-->  "b 1" B 
		D "d  1..*" <--o  "c 1" C 
		@enduml
		'''.toString, getDiagram(diagramIterator, "SharedAssociation"));
	}
	
	@Test
	def void CompositeTest() {
		assertDiagramEquals('''
		@startuml
		title Composite
		class A {
		}
		class B {
		}
		class C {
		}
		class D {
		}
		B "b  *" <--*  "a  *" A 
		D "d 1" *-->  "c 1" C 
		@enduml
		'''.toString, getDiagram(diagramIterator, "Composite"));
	}
	
	@Test
	def void InstanceSpecificationTwoClassifierTest() {
		assertDiagramEquals('''
		@startuml
		title InstanceSpecificationTwoClassifier
		class "InstanceSpecification:A,B
		" << (i,pink) >> {
		}
		class A {
		}
		class B {
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "InstanceSpecificationTwoClassifier"));
	}
	
	@Test
	def void ClassAttributeStaticOperationAbstractStaticTest() {
		assertDiagramEquals('''
		@startuml
		title ClassAttributeStaticOperationAbstractStatic
		class A {
			{static}
			+Attribute1
			{abstract}
			+Operation1()
			{static}
			+Operation2()
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "ClassAttributeStaticOperationAbstractStatic"));
	}
	
	@Test
	def void ClassVisibilityTest() {
		assertDiagramEquals('''
		@startuml
		title ClassVisibility
		class A {
			+Attribute1
			-Attribute2
			#Attribute3
			~Attribute4
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "ClassVisibility"));
	}
	
	@Test
	def void ClassOperationReturnTest() {
		assertDiagramEquals('''
		@startuml
		title ClassOperationReturn
		class A {
			+Operation1(): String
			+Operation2(): String[]
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "ClassOperationReturn"));
	}
	
	@Test
	def void ClassOperationParameterTest() {
		assertDiagramEquals('''
		@startuml
		title ClassOperationParameter
		class A {
			+Attribute1
			+Operation1(: String,: String)
			+Operation2(: String[])
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "ClassOperationParameter"));
	}
	
	@Test 
	def void unsupportedConnectorTest() {
		var diagram = mock(typeof(Diagram))
		
		var edges = new BasicEList<EObject>()
		var children = new BasicEList<EObject>()
		
		var c = mock(typeof(Connector))
		var value = mock(typeof(EClass))
		
		when(c.eClass).thenReturn(value)
		when(value.name).thenReturn("notSupported")	
		when(diagram.edges).thenReturn(edges)
		when(diagram.children).thenReturn(children)
		
		edges.add(c)
		generator.compileClassDiagram(diagram)
	}
	
	@Test (expected = typeof(UnsupportedOperationException))
	def void unsupportedEObjectShapeTest() {
		var value = mock(typeof(EClass))
		generator.compileClassDiagram(setUpDiagram(value, value))
	}
	
	/**
	 * Checks if the compiled diagram is the same as the expected.
	 * @expected the expected output
	 * @diagram the diagram to compile
	 */
	def void assertDiagramEquals(String expected, Diagram diagram) {
		assertEquals(expected, generator.compileClassDiagram(diagram).toString)
	}
}