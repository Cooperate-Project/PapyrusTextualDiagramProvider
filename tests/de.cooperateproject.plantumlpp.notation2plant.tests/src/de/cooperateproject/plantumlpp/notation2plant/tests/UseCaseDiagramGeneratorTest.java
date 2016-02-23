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

import de.cooperateproject.notation2plant.UseCaseDiagramGenerator;

public class UseCaseDiagramGeneratorTest extends AbstractDiagramGeneratorTest{

	private static UseCaseDiagramGenerator generator;
	private Iterable<Diagram> diagrams;
	private static final String LINE_SEP = System.getProperty("line.separator");

	@BeforeClass
	public static void setUp() {
		generator = new UseCaseDiagramGenerator();
		ressourceSetUp();
	}
	
	@Before
	public void getDiagram() throws IOException {
		diagrams = getDiagrams("testdiagrams/usecase.notation");
	}
	
	@After
	public void cleanDiagram() {
		diagrams = null;
	}

	@Test
	public void nullDiagramTest() {
		generator.compileUseCaseDiagram(null);
	}

	@Test
	public void ActorTest() {
		Diagram d = getDiagram(diagrams, "Actor");
				
		assertEquals("@startuml" 
				+ LINE_SEP + "title Actor" 
				+ LINE_SEP + ":Actor:" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void UseCaseTest() {
		Diagram d = getDiagram(diagrams, "UseCase");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title UseCase" 
				+ LINE_SEP + "(UseCase)" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void EmptyPackageTest() {
		Diagram d = getDiagram(diagrams, "EmptyPackage");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title EmptyPackage" 
				+ LINE_SEP + "rectangle Package { " 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void UseCaseActorPackageTest() {
		Diagram d = getDiagram(diagrams, "UseCaseActorPackage");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title UseCaseActorPackage" 
				+ LINE_SEP + "rectangle Package { "
				+ LINE_SEP + ":Actor:" 
				+ LINE_SEP + "(UseCase)" 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void AssociationActorUseCaseTest() {
		Diagram d = getDiagram(diagrams, "AssociationActorUseCase");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title AssociationActorUseCase" 
				+ LINE_SEP + ":Actor:" 
				+ LINE_SEP + "(UseCase)" 
				+ LINE_SEP + "(UseCase) -- :Actor:" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void UseCaseUseCaseGeneralizationTest() {
		Diagram d = getDiagram(diagrams, "UseCaseUseCaseGeneralization");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title UseCaseUseCaseGeneralization" 
				+ LINE_SEP + "(A)" 
				+ LINE_SEP + "(B)" 
				+ LINE_SEP + "(B) --|> (A)" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void UseCaseUseCaseRealizationTest() {
		Diagram d = getDiagram(diagrams, "UseCaseUseCaseRealization");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title UseCaseUseCaseRealization" 
				+ LINE_SEP + "(UseCase)" 
				+ LINE_SEP + "(UseCase)" 
				+ LINE_SEP + "(UseCase) ..|> (UseCase) : label" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void ActorUseCaseAssociationLabelTest() {
		Diagram d = getDiagram(diagrams, "ActorUseCaseAssociationLabel");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ActorUseCaseAssociationLabel" 
				+ LINE_SEP + ":Actor:" 
				+ LINE_SEP + "(UseCase)" 
				+ LINE_SEP + "(UseCase) -- :Actor: : label" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void ActorPackageUsageTest() {
		Diagram d = getDiagram(diagrams, "ActorPackageUsage");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ActorPackageUsage" 
				+ LINE_SEP + "rectangle Package { " 
				+ LINE_SEP + "}" 
				+ LINE_SEP + ":Actor:" 
				+ LINE_SEP + ":Actor: ..> Package : use" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void PackagePackageDependencyTest() {
		Diagram d = getDiagram(diagrams, "PackagePackageDependency");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title PackagePackageDependency" 
				+ LINE_SEP + "rectangle A { " 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "rectangle B { " 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "B ..> A : label" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void PackagePackageAbstractionTest() {
		Diagram d = getDiagram(diagrams, "PackagePackageAbstraction");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title PackagePackageAbstraction" 
				+ LINE_SEP + "rectangle A { " 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "rectangle B { " 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "B ..> A : abstraction" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void ActorUseCaseGeneralizationTest() {
		Diagram d = getDiagram(diagrams, "ActorUseCaseGeneralization");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ActorUseCaseGeneralization" 
				+ LINE_SEP + ":Actor:" 
				+ LINE_SEP + ":Actor: --|> (UseCase)" 
				+ LINE_SEP + "(UseCase)" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void UseCaseUseCaseIncludeTest() {
		Diagram d = getDiagram(diagrams, "UseCaseUseCaseInclude");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title UseCaseUseCaseInclude" 
				+ LINE_SEP + "(A)" 
				+ LINE_SEP + "(B) .> (A) : include" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void UseCaseUseCaseExtendTest() {
		Diagram d = getDiagram(diagrams, "UseCaseUseCaseExtend");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title UseCaseUseCaseExtend" 
				+ LINE_SEP + "(B) .> (A) : extend" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void PackageMergeTest() {
		Diagram d = getDiagram(diagrams, "PackageMerge");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title PackageMerge" 
				+ LINE_SEP + "rectangle A { " 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "A ..> B : merge" 
				+ LINE_SEP + "rectangle B { " 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void PackageImportTest() {
		Diagram d = getDiagram(diagrams, "PackageImport");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title PackageImport" 
				+ LINE_SEP + "rectangle A { " 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "A ..> B : import" 
				+ LINE_SEP + "rectangle B { " 
				+ LINE_SEP + "}" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void ActorStereotypeTest() {
		Diagram d = getDiagram(diagrams, "ActorStereotype");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ActorStereotype" 
				+ LINE_SEP + ":Person: << Human >>" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
	
	@Test
	public void ActorCommentTest() {
		Diagram d = getDiagram(diagrams, "ActorComment");
		
		assertEquals("@startuml" 
				+ LINE_SEP + "title ActorComment" 
				+ LINE_SEP + ":Actor:" 
				+ LINE_SEP + "note as Ncom" 
				+ LINE_SEP + "\tcomment" 
				+ LINE_SEP + "end note" 
				+ LINE_SEP + "Ncom .. :Actor:" 
				+ LINE_SEP + "@enduml" + LINE_SEP, 
				generator.compileUseCaseDiagram(d).toString());
	}
		
	@Test
	public void unsupportedEObjectShapeTest() {
		EClass value = mock(EClass.class);
		generator.compileUseCaseDiagram(setUpDiagram(value, value));
	}
	
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}
