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

public class UseCaseDiagramGeneratorTest extends FileRessource{

	private static UseCaseDiagramGenerator generator;
	private Iterable<Diagram> diagrams;

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
				
		assertEquals("@startuml\ntitle Actor\n:Actor:\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void UseCaseTest() {
		Diagram d = getDiagram(diagrams, "UseCase");
		
		assertEquals("@startuml\ntitle UseCase\n(UseCase)\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void EmptyPackageTest() {
		Diagram d = getDiagram(diagrams, "EmptyPackage");
		
		assertEquals("@startuml\ntitle EmptyPackage\nrectangle Package { \n}\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void UseCaseActorPackageTest() {
		Diagram d = getDiagram(diagrams, "UseCaseActorPackage");
		
		assertEquals("@startuml\ntitle UseCaseActorPackage\nrectangle Package { "
				+ "\n:Actor:\n(UseCase)\n}\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void AssociationActorUseCaseTest() {
		Diagram d = getDiagram(diagrams, "AssociationActorUseCase");
		
		assertEquals("@startuml\ntitle AssociationActorUseCase\n:Actor:\n(UseCase)\n"
				+ "(UseCase) -- :Actor:\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void UseCaseUseCaseGeneralizationTest() {
		Diagram d = getDiagram(diagrams, "UseCaseUseCaseGeneralization");
		
		assertEquals("@startuml\ntitle UseCaseUseCaseGeneralization\n(A)\n(B)\n"
				+ "(B) --|> (A)\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void UseCaseUseCaseRealizationTest() {
		Diagram d = getDiagram(diagrams, "UseCaseUseCaseRealization");
		
		assertEquals("@startuml\ntitle UseCaseUseCaseRealization\n(UseCase)\n"
				+ "(UseCase)\n(UseCase) ..|> (UseCase) : label\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void ActorUseCaseAssociationLabelTest() {
		Diagram d = getDiagram(diagrams, "ActorUseCaseAssociationLabel");
		
		assertEquals("@startuml\ntitle ActorUseCaseAssociationLabel\n:Actor:\n"
				+ "(UseCase)\n(UseCase) -- :Actor: : label\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void ActorPackageUsageTest() {
		Diagram d = getDiagram(diagrams, "ActorPackageUsage");
		
		assertEquals("@startuml\ntitle ActorPackageUsage\nrectangle Package { \n}\n"
				+ ":Actor:\n:Actor: ..> Package : use\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void PackagePackageDependencyTest() {
		Diagram d = getDiagram(diagrams, "PackagePackageDependency");
		
		assertEquals("@startuml\ntitle PackagePackageDependency\nrectangle A { \n}\n"
				+ "rectangle B { \n}\nB ..> A : label\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void PackagePackageAbstractionTest() {
		Diagram d = getDiagram(diagrams, "PackagePackageAbstraction");
		
		assertEquals("@startuml\ntitle PackagePackageAbstraction\nrectangle A { \n}\n"
				+ "rectangle B { \n}\nB ..> A : abstraction\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void ActorUseCaseGeneralizationTest() {
		Diagram d = getDiagram(diagrams, "ActorUseCaseGeneralization");
		
		assertEquals("@startuml\ntitle ActorUseCaseGeneralization\n:Actor:\n"
				+ ":Actor: --|> (UseCase)\n(UseCase)\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void UseCaseUseCaseIncludeTest() {
		Diagram d = getDiagram(diagrams, "UseCaseUseCaseInclude");
		
		assertEquals("@startuml\ntitle UseCaseUseCaseInclude\n(A)\n"
				+ "(B) .> (A) : include\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void UseCaseUseCaseExtendTest() {
		Diagram d = getDiagram(diagrams, "UseCaseUseCaseExtend");
		
		assertEquals("@startuml\ntitle UseCaseUseCaseExtend\n(B) .> (A) : extend\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void PackageMergeTest() {
		Diagram d = getDiagram(diagrams, "PackageMerge");
		
		assertEquals("@startuml\ntitle PackageMerge\nrectangle A { \n}\n"
				+ "A ..> B : merge\nrectangle B { \n}\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void PackageImportTest() {
		Diagram d = getDiagram(diagrams, "PackageImport");
		
		assertEquals("@startuml\ntitle PackageImport\nrectangle A { \n}\n"
				+ "A ..> B : import\nrectangle B { \n}\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void ActorStereotypeTest() {
		Diagram d = getDiagram(diagrams, "ActorStereotype");
		
		assertEquals("@startuml\ntitle ActorStereotype\n:Person: << Human >>\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
	}
	
	@Test
	public void ActorCommentTest() {
		Diagram d = getDiagram(diagrams, "ActorComment");
		
		assertEquals("@startuml\ntitle ActorComment\n:Actor:\nnote as Ncom\n\t"
				+ "comment\nend note\nNcom .. :Actor:\n@enduml\n", 
				generator.compileUseCaseDiagram(d).toString().replace("\r", ""));
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
