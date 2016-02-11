package de.cooperateproject.plantumlpp.notation2plant.tests;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.cooperateproject.notation2plant.ClassDiagramGenerator;

public class ClassDiagramGeneratorTest extends FileRessource {
	
	private static ClassDiagramGenerator generator;
	//private static Diagram diagram;
	private Iterable<Diagram> diagrams;
	
	@BeforeClass
	public static void setUp() {
		generator = new ClassDiagramGenerator();
		ressourceSetUp();
		//diagrams = getDiagrams("testdiagrams/class.notation");
		
	}
	@Before
	public void getDiagram() throws IOException {
		diagrams = getDiagrams("testdiagrams/class.notation");
	}
	
	@After
	public void cleanDiagram() {
		diagrams = null;
	}
	
	@Test
	public void nullDiagramTest() {
		generator.compileClassDiagram(null);
	}
	
	
	@Test
	public void ClassAttributOperationTest() {
		Diagram d = getDiagram(diagrams, "ClassAttrOp");
		
		assertEquals("@startuml\r\ntitle ClassAttrOp\r\n"
				+ "class A {\r\n\t+Attribute1: String\r\n\t"
				+ "+Attribute2: String[]\r\n\t+Operation1()\r\n}\r\n"
				+ "@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void InterfaceTest() {
		Diagram d = getDiagram(diagrams, "Interface");
		
		assertEquals("@startuml\r\ntitle Interface\r\n"
				+ "interface Interface {\r\n\t"
				+ "+Attribute1\r\n\t+Operation1()\r\n}\r\n"
				+ "@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void EmptyPackageTest() {
		Diagram d = getDiagram(diagrams, "EmptyPackage");
		
		assertEquals("@startuml\r\ntitle EmptyPackage\r\n"
				+ "package Package {\r\n}\r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void PackageWithClassTest() {
		Diagram d = getDiagram(diagrams, "PackageClass");
		
		assertEquals("@startuml\r\ntitle PackageClass\r\n"
				+ "package Package {\r\n\t"
				+ "class Class {\r\n\t}\r\n}\r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void EnumerationTest() {
		Diagram d = getDiagram(diagrams, "Enumeration");		
		
		assertEquals("@startuml\r\ntitle Enumeration\r\n"
				+ "enum Enumeration {\r\n\tEnumerationLiteral1\r\n\t"
				+ "EnumerationLiteral2\r\n}\r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void DataTypeTest() {
		Diagram d = getDiagram(diagrams, "DataType");
		
		assertEquals("@startuml\r\ntitle DataType\r\n"
				+ "class DataType << (D,blue) >> {\r\n\t"
				+ "+Attribute1\r\n\t+Operation1()\r\n}\r\n"
				+ "@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void SignalTest() {
		Diagram d = getDiagram(diagrams, "Signal");
				
		assertEquals("@startuml\r\ntitle Signal\r\n"
				+ "class Signal << (S,red) >> {\r\n\t"
				+ "+Attribute1\r\n\t+Attribute2\r\n}\r\n"
				+ "@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void InformationItemTest() {
		Diagram d = getDiagram(diagrams, "InformationItem");
				
		assertEquals("@startuml\r\ntitle InformationItem\r\n"
				+ "class InformationItem << (>,orchid) >> {\r\n}\r\n"
				+ "@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void InstanceSpecificationTest() {
		Diagram d = getDiagram(diagrams, "InstanceSpecification");
				
		assertEquals("@startuml\r\ntitle InstanceSpecification\r\n"
				+ "class \"InstanceSpecification:\" << (i,pink) >> {\r\n}\r\n"
				+ "@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void CommentTest() {
		Diagram d = getDiagram(diagrams, "Comment");
		
		assertEquals("@startuml\r\ntitle Comment\r\nclass Class {\r\n}\r\n"
				+ "note as Ncom\r\n\tcomment\r\nend note\r\n\tNcom..Class\r\n"
				+ "@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void DurationObservationTest() {
		Diagram d = getDiagram(diagrams, "DurationObservation");
				
		assertEquals("@startuml\r\ntitle DurationObservation\r\n"
				+ "note as NDurationDurationObservation\r\n\t\r\nend note\r\n"
				+ "@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void DurationObservationWithEventTest() {
		Diagram d = getDiagram(diagrams, "DurationObservationWithEvent");

		assertEquals("@startuml\r\ntitle DurationObservationWithEvent\r\n"
				+ "note as NDurationDurationObservation\r\n\t\r\nend note\r\n\t"
				+ "NDurationDurationObservation..A\r\n"
				+ "class A {\r\n}\r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void ClassAssociationTest() {
		Diagram d = getDiagram(diagrams, "ClassAssociation");
		
		assertEquals("@startuml\r\ntitle ClassAssociation\r\n"
				+ "class A {\r\n}\r\nclass B {\r\n}\r\nB \""
				+ "b  *\" <-->  \"a  0..1\" A : label\r\n"
				+ "@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void GeneralizationTest() {
		Diagram d = getDiagram(diagrams, "Generalization");
		
		assertEquals("@startuml\r\ntitle Generalization\r\n"
				+ "abstract class A {\r\n}\r\nclass B {\r\n}\r\n"
				+ "A <|-- B\r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void RealizationTest() {
		Diagram d = getDiagram(diagrams, "Realization");
		
		assertEquals("@startuml\r\ntitle Realization\r\n"
				+ "class B {\r\n}\r\ninterface A {\r\n}\r\n"
				+ "A <|-- B\r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void SharedAssociationTest() {
		Diagram d = getDiagram(diagrams, "SharedAssociation");
		
		assertEquals("@startuml\r\ntitle SharedAssociation\r\n"
				+ "class A {\r\n}\r\nclass B {\r\n}\r\nclass C {\r\n}\r\n"
				+ "class D {\r\n}\r\nA \"a  1..*\" o-->  \"b 1\" B \r\nD \""
				+ "d  1..*\" <--o  \"c 1\" C \r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void CompositeTest() {
		Diagram d = getDiagram(diagrams, "Composite");
		
		assertEquals("@startuml\r\ntitle Composite\r\nclass A {\r\n}\r\n"
				+ "class B {\r\n}\r\nclass C {\r\n}\r\nclass D {\r\n}\r\n"
				+ "B \"b  *\" <--*  \"a  *\" A \r\nD \""
				+ "d 1\" *-->  \"c 1\" C \r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void InstanceSpecificationTwoClassifierTest() {
		Diagram d = getDiagram(diagrams, "InstanceSpecificationTwoClassifier");
		
		assertEquals("@startuml\r\ntitle InstanceSpecificationTwoClassifier\r\n"
				+ "class \"InstanceSpecification:A,B\r\n\" << (i,pink) >> "
				+ "{\r\n}\r\nclass A {\r\n}\r\nclass B {\r\n}\r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void ClassAttributeStaticOperationAbstractStaticTest() {
		Diagram d = getDiagram(diagrams, "ClassAttributeStaticOperationAbstractStatic");
		
		assertEquals("@startuml\r\ntitle ClassAttributeStaticOperationAbstractStatic\r\n"
				+ "class A {\r\n\t{static}\r\n\t+Attribute1\r\n\t{abstract}\r\n\t"
				+ "+Operation1()\r\n\t{static}\r\n\t+Operation2()\r\n}\r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void ClassVisibilityTest() {
		Diagram d = getDiagram(diagrams, "ClassVisibility");		
		
		assertEquals("@startuml\r\ntitle ClassVisibility\r\nclass A {\r\n\t"
				+ "+Attribute1\r\n\t-Attribute2\r\n\t#Attribute3\r\n\t~Attribute4\r\n}\r\n"
				+ "@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void ClassOperationReturnTest() {
		Diagram d = getDiagram(diagrams, "ClassOperationReturn");
		
		assertEquals("@startuml\r\ntitle ClassOperationReturn\r\n"
				+ "class A {\r\n\t+Operation1(): String\r\n\t"
				+ "+Operation2(): String[]\r\n}\r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void ClassOperationParameterTest() {
		Diagram d = getDiagram(diagrams, "ClassOperationParameter");
		
		assertEquals("@startuml\r\ntitle ClassOperationParameter\r\n"
				+ "class A {\r\n\t+Attribute1\r\n\t"
				+ "+Operation1(: String,: String)\r\n\t"
				+ "+Operation2(: String[])\r\n}\r\n@enduml\r\n", 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test 
	public void unsupportedConnectorTest() {
		Diagram diagram = mock(Diagram.class);
		
		EList<EObject> edges = new BasicEList<EObject>();
		EList<EObject> children = new BasicEList<EObject>();
		
		Connector c = mock(Connector.class);
		EClass value = mock(EClass.class);
		
		when(c.eClass()).thenReturn(value);
		when(value.getName()).thenReturn("notSupported");		
		when(diagram.getEdges()).thenReturn(edges);
		when(diagram.getChildren()).thenReturn(children);
		
		edges.add(c);
		generator.compileClassDiagram(diagram);
	}
	
	@Test (expected = UnsupportedOperationException.class)
	public void unsupportedEObjectShapeTest() {
		Diagram diagram = mock(Diagram.class);
		
		EList<EObject> edges = new BasicEList<EObject>();
		EList<EObject> children = new BasicEList<EObject>();
		
		Shape s = mock(Shape.class);
		EClass value = mock(EClass.class);
		
		when(s.getElement()).thenReturn(value);
		when(value.eClass()).thenReturn(value);
		when(value.getName()).thenReturn("notSupported");		
		when(diagram.getEdges()).thenReturn(edges);
		when(diagram.getChildren()).thenReturn(children);
		
		children.add(s);
		generator.compileClassDiagram(diagram);
	}
	
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}
