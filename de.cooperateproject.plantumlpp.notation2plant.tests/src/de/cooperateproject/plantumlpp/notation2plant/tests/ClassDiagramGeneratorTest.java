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
		
		assertEquals("@startuml\ntitle ClassAttrOp\n"
				+ "class A {\n\t+Attribute1: String\n\t"
				+ "+Attribute2: String[]\n\t+Operation1()\n}\n"
				+ "@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void InterfaceTest() {
		Diagram d = getDiagram(diagrams, "Interface");
		
		assertEquals("@startuml\ntitle Interface\n"
				+ "interface Interface {\n\t"
				+ "+Attribute1\n\t+Operation1()\n}\n"
				+ "@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void EmptyPackageTest() {
		Diagram d = getDiagram(diagrams, "EmptyPackage");
		
		assertEquals("@startuml\ntitle EmptyPackage\n"
				+ "package Package {\n}\n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void PackageWithClassTest() {
		Diagram d = getDiagram(diagrams, "PackageClass");
		
		assertEquals("@startuml\ntitle PackageClass\n"
				+ "package Package {\n\t"
				+ "class Class {\n\t}\n}\n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void EnumerationTest() {
		Diagram d = getDiagram(diagrams, "Enumeration");		
		
		assertEquals("@startuml\ntitle Enumeration\n"
				+ "enum Enumeration {\n\tEnumerationLiteral1\n\t"
				+ "EnumerationLiteral2\n}\n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void DataTypeTest() {
		Diagram d = getDiagram(diagrams, "DataType");
		
		assertEquals("@startuml\ntitle DataType\n"
				+ "class DataType << (D,blue) >> {\n\t"
				+ "+Attribute1\n\t+Operation1()\n}\n"
				+ "@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void SignalTest() {
		Diagram d = getDiagram(diagrams, "Signal");
				
		assertEquals("@startuml\ntitle Signal\n"
				+ "class Signal << (S,red) >> {\n\t"
				+ "+Attribute1\n\t+Attribute2\n}\n"
				+ "@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void InformationItemTest() {
		Diagram d = getDiagram(diagrams, "InformationItem");
				
		assertEquals("@startuml\ntitle InformationItem\n"
				+ "class InformationItem << (>,orchid) >> {\n}\n"
				+ "@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void InstanceSpecificationTest() {
		Diagram d = getDiagram(diagrams, "InstanceSpecification");
				
		assertEquals("@startuml\ntitle InstanceSpecification\n"
				+ "class \"InstanceSpecification:\" << (i,pink) >> {\n}\n"
				+ "@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void CommentTest() {
		Diagram d = getDiagram(diagrams, "Comment");
		
		assertEquals("@startuml\ntitle Comment\nclass Class {\n}\n"
				+ "note as Ncom\n\tcomment\nend note\n\tNcom..Class\n"
				+ "@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void DurationObservationTest() {
		Diagram d = getDiagram(diagrams, "DurationObservation");
				
		assertEquals("@startuml\ntitle DurationObservation\n"
				+ "note as NDurationDurationObservation\n\t\nend note\n"
				+ "@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void DurationObservationWithEventTest() {
		Diagram d = getDiagram(diagrams, "DurationObservationWithEvent");

		assertEquals("@startuml\ntitle DurationObservationWithEvent\n"
				+ "note as NDurationDurationObservation\n\t\nend note\n\t"
				+ "NDurationDurationObservation..A\n"
				+ "class A {\n}\n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void ClassAssociationTest() {
		Diagram d = getDiagram(diagrams, "ClassAssociation");
		
		assertEquals("@startuml\ntitle ClassAssociation\n"
				+ "class A {\n}\nclass B {\n}\nB \""
				+ "b  *\" <-->  \"a  0..1\" A : label\n"
				+ "@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void GeneralizationTest() {
		Diagram d = getDiagram(diagrams, "Generalization");
		
		assertEquals("@startuml\ntitle Generalization\n"
				+ "abstract class A {\n}\nclass B {\n}\n"
				+ "A <|-- B\n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void RealizationTest() {
		Diagram d = getDiagram(diagrams, "Realization");
		
		assertEquals("@startuml\ntitle Realization\n"
				+ "class B {\n}\ninterface A {\n}\n"
				+ "A <|-- B\n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void SharedAssociationTest() {
		Diagram d = getDiagram(diagrams, "SharedAssociation");
		
		assertEquals("@startuml\ntitle SharedAssociation\n"
				+ "class A {\n}\nclass B {\n}\nclass C {\n}\n"
				+ "class D {\n}\nA \"a  1..*\" o-->  \"b 1\" B \nD \""
				+ "d  1..*\" <--o  \"c 1\" C \n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void CompositeTest() {
		Diagram d = getDiagram(diagrams, "Composite");
		
		assertEquals("@startuml\ntitle Composite\nclass A {\n}\n"
				+ "class B {\n}\nclass C {\n}\nclass D {\n}\n"
				+ "B \"b  *\" <--*  \"a  *\" A \nD \""
				+ "d 1\" *-->  \"c 1\" C \n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void InstanceSpecificationTwoClassifierTest() {
		Diagram d = getDiagram(diagrams, "InstanceSpecificationTwoClassifier");
		
		assertEquals("@startuml\ntitle InstanceSpecificationTwoClassifier\n"
				+ "class \"InstanceSpecification:A,B\n\" << (i,pink) >> "
				+ "{\n}\nclass A {\n}\nclass B {\n}\n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void ClassAttributeStaticOperationAbstractStaticTest() {
		Diagram d = getDiagram(diagrams, "ClassAttributeStaticOperationAbstractStatic");
		
		assertEquals("@startuml\ntitle ClassAttributeStaticOperationAbstractStatic\n"
				+ "class A {\n\t{static}\n\t+Attribute1\n\t{abstract}\n\t"
				+ "+Operation1()\n\t{static}\n\t+Operation2()\n}\n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void ClassVisibilityTest() {
		Diagram d = getDiagram(diagrams, "ClassVisibility");		
		
		assertEquals("@startuml\ntitle ClassVisibility\nclass A {\n\t"
				+ "+Attribute1\n\t-Attribute2\n\t#Attribute3\n\t~Attribute4\n}\n"
				+ "@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void ClassOperationReturnTest() {
		Diagram d = getDiagram(diagrams, "ClassOperationReturn");
		
		assertEquals("@startuml\ntitle ClassOperationReturn\n"
				+ "class A {\n\t+Operation1(): String\n\t"
				+ "+Operation2(): String[]\n}\n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void ClassOperationParameterTest() {
		Diagram d = getDiagram(diagrams, "ClassOperationParameter");
		
		assertEquals("@startuml\ntitle ClassOperationParameter\n"
				+ "class A {\n\t+Attribute1\n\t"
				+ "+Operation1(: String,: String)\n\t"
				+ "+Operation2(: String[])\n}\n@enduml\n", 
				generator.compileClassDiagram(d).toString().replace("\r", ""));
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
