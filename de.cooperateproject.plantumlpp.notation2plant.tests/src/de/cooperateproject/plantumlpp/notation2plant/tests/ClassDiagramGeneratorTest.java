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
	private static final String LINE_SEP = System.getProperty("line.separator");
	
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
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ClassAttrOp" 
				+ LINE_SEP + "class A {" 
				+ LINE_SEP + "\t+Attribute1: String" 
				+ LINE_SEP + "\t+Attribute2: String[]" 
				+ LINE_SEP + "\t+Operation1()" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void InterfaceTest() {
		Diagram d = getDiagram(diagrams, "Interface");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title Interface" 
				+ LINE_SEP + "interface Interface {" 
				+ LINE_SEP + "\t+Attribute1" 
				+ LINE_SEP + "\t+Operation1()" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void EmptyPackageTest() {
		Diagram d = getDiagram(diagrams, "EmptyPackage");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title EmptyPackage" 
				+ LINE_SEP + "package Package {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void PackageWithClassTest() {
		Diagram d = getDiagram(diagrams, "PackageClass");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title PackageClass" 
				+ LINE_SEP + "package Package {" 
				+ LINE_SEP + "\tclass Class {" 
				+ LINE_SEP + "\t}" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void EnumerationTest() {
		Diagram d = getDiagram(diagrams, "Enumeration");		
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title Enumeration" 
				+ LINE_SEP + "enum Enumeration {" 
				+ LINE_SEP + "\tEnumerationLiteral1" 
				+ LINE_SEP + "\tEnumerationLiteral2" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void DataTypeTest() {
		Diagram d = getDiagram(diagrams, "DataType");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title DataType" 
				+ LINE_SEP + "class DataType << (D,blue) >> {" 
				+ LINE_SEP + "\t+Attribute1" 
				+ LINE_SEP + "\t+Operation1()" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void SignalTest() {
		Diagram d = getDiagram(diagrams, "Signal");
				
		assertEquals("@startuml" 
				+ LINE_SEP + "title Signal" 
				+ LINE_SEP + "class Signal << (S,red) >> {" 
				+ LINE_SEP + "\t+Attribute1" 
				+ LINE_SEP + "\t+Attribute2" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void InformationItemTest() {
		Diagram d = getDiagram(diagrams, "InformationItem");
				
		assertEquals("@startuml" 
				+ LINE_SEP + "title InformationItem" 
				+ LINE_SEP + "class InformationItem << (>,orchid) >> {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void InstanceSpecificationTest() {
		Diagram d = getDiagram(diagrams, "InstanceSpecification");
				
		assertEquals("@startuml" 
				+ LINE_SEP + "title InstanceSpecification" 
				+ LINE_SEP + "class \"InstanceSpecification:\" << (i,pink) >> {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void CommentTest() {
		Diagram d = getDiagram(diagrams, "Comment");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title Comment" 
				+ LINE_SEP + "class Class {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "note as Ncom" 
				+ LINE_SEP + "\tcomment" 
				+ LINE_SEP + "end note" 
				+ LINE_SEP + "\tNcom..Class" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void DurationObservationTest() {
		Diagram d = getDiagram(diagrams, "DurationObservation");
				
		assertEquals("@startuml" 
				+ LINE_SEP + "title DurationObservation" 
				+ LINE_SEP + "note as NDurationDurationObservation" 
				+ LINE_SEP + "\t" 
				+ LINE_SEP + "end note" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void DurationObservationWithEventTest() {
		Diagram d = getDiagram(diagrams, "DurationObservationWithEvent");

		assertEquals("@startuml" 
				+ LINE_SEP + "title DurationObservationWithEvent" 
				+ LINE_SEP + "note as NDurationDurationObservation" 
				+ LINE_SEP + "\t" 
				+ LINE_SEP + "end note" 
				+ LINE_SEP + "\tNDurationDurationObservation..A" 
				+ LINE_SEP + "class A {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void ClassAssociationTest() {
		Diagram d = getDiagram(diagrams, "ClassAssociation");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ClassAssociation" 
				+ LINE_SEP + "class A {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "class B {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "B \"b  *\" <-->  \"a  0..1\" A : label" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void GeneralizationTest() {
		Diagram d = getDiagram(diagrams, "Generalization");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title Generalization" 
				+ LINE_SEP + "abstract class A {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "class B {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "A <|-- B" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void RealizationTest() {
		Diagram d = getDiagram(diagrams, "Realization");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title Realization" 
				+ LINE_SEP + "class B {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "interface A {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "A <|-- B" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void SharedAssociationTest() {
		Diagram d = getDiagram(diagrams, "SharedAssociation");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title SharedAssociation" 
				+ LINE_SEP + "class A {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "class B {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "class C {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "class D {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "A \"a  1..*\" o-->  \"b 1\" B " 
				+ LINE_SEP + "D \"d  1..*\" <--o  \"c 1\" C " 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void CompositeTest() {
		Diagram d = getDiagram(diagrams, "Composite");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title Composite" 
				+ LINE_SEP + "class A {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "class B {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "class C {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "class D {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "B \"b  *\" <--*  \"a  *\" A " 
				+ LINE_SEP + "D \"d 1\" *-->  \"c 1\" C " 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void InstanceSpecificationTwoClassifierTest() {
		Diagram d = getDiagram(diagrams, "InstanceSpecificationTwoClassifier");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title InstanceSpecificationTwoClassifier" 
				+ LINE_SEP + "class \"InstanceSpecification:A,B" 
				+ LINE_SEP + "\" << (i,pink) >> {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "class A {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "class B {" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void ClassAttributeStaticOperationAbstractStaticTest() {
		Diagram d = getDiagram(diagrams, "ClassAttributeStaticOperationAbstractStatic");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ClassAttributeStaticOperationAbstractStatic" 
				+ LINE_SEP + "class A {" 
				+ LINE_SEP + "\t{static}" 
				+ LINE_SEP + "\t+Attribute1" 
				+ LINE_SEP + "\t{abstract}" 
				+ LINE_SEP + "\t+Operation1()" 
				+ LINE_SEP + "\t{static}" 
				+ LINE_SEP + "\t+Operation2()" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void ClassVisibilityTest() {
		Diagram d = getDiagram(diagrams, "ClassVisibility");		
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ClassVisibility" 
				+ LINE_SEP + "class A {"
				+ LINE_SEP + "\t+Attribute1" 
				+ LINE_SEP + "\t-Attribute2" 
				+ LINE_SEP + "\t#Attribute3" 
				+ LINE_SEP + "\t~Attribute4" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void ClassOperationReturnTest() {
		Diagram d = getDiagram(diagrams, "ClassOperationReturn");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ClassOperationReturn" 
				+ LINE_SEP + "class A {" 
				+ LINE_SEP + "\t+Operation1(): String" 
				+ LINE_SEP + "\t+Operation2(): String[]" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileClassDiagram(d).toString());
	}
	
	@Test
	public void ClassOperationParameterTest() {
		Diagram d = getDiagram(diagrams, "ClassOperationParameter");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ClassOperationParameter" 
				+ LINE_SEP + "class A {" 
				+ LINE_SEP + "\t+Attribute1" 
				+ LINE_SEP + "\t+Operation1(: String,: String)" 
				+ LINE_SEP + "\t+Operation2(: String[])" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
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
		EClass value = mock(EClass.class);
		generator.compileClassDiagram(setUpDiagram(value, value));
	}
	
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}
