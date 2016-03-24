package de.cooperateproject.plantumlpp.notation2plant.tests

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

import de.cooperateproject.notation2plant.UseCaseDiagramGenerator
import java.util.List
import org.eclipse.gmf.runtime.notation.Diagram
import java.util.Iterator
import org.junit.BeforeClass
import org.junit.Before
import org.junit.After
import org.junit.AfterClass
import org.junit.Test
import org.eclipse.emf.ecore.EClass

class UseCaseDiagramGeneratorTest extends AbstractDiagramGeneratorTest {
	private static UseCaseDiagramGenerator generator
	private static List<Diagram> diagrams
	private Iterator<Diagram> diagramIterator
	
	@BeforeClass
	def static void setUp() {
		generator = new UseCaseDiagramGenerator()
		ressourceSetUp()
		diagrams = getDiagrams("testdiagrams/usecase.notation")		
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
		generator.compileUseCaseDiagram(null);
	}

	@Test
	def void ActorTest() {
		assertDiagramEquals('''
		@startuml
		title Actor
		:Actor:
		@enduml
		'''.toString, getDiagram(diagramIterator, "Actor"));
	}
	
	@Test
	def void UseCaseTest() {
		assertDiagramEquals('''
		@startuml
		title UseCase
		(UseCase)
		@enduml
		'''.toString, getDiagram(diagramIterator, "UseCase"));
	}
	
	@Test
	def void EmptyPackageTest() {
		assertDiagramEquals('''
		@startuml
		title EmptyPackage
		rectangle Package {
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "EmptyPackage"));
	}
	
	@Test
	def void UseCaseActorPackageTest() {
		assertDiagramEquals('''
		@startuml
		title UseCaseActorPackage
		rectangle Package {
		:Actor:
		(UseCase)
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "UseCaseActorPackage"));
	}
	
	@Test
	def void AssociationActorUseCaseTest() {
		assertDiagramEquals('''
		@startuml
		title AssociationActorUseCase
		:Actor:
		(UseCase)
		(UseCase) -- :Actor:
		@enduml
		'''.toString, getDiagram(diagramIterator, "AssociationActorUseCase"));
	}
	
	@Test
	def void UseCaseUseCaseGeneralizationTest() {
		assertDiagramEquals('''
		@startuml
		title UseCaseUseCaseGeneralization
		(A)
		(B)
		(B) --|> (A)
		@enduml
		'''.toString, getDiagram(diagramIterator, "UseCaseUseCaseGeneralization"));
	}
	
	@Test
	def void UseCaseUseCaseRealizationTest() {
		assertDiagramEquals('''
		@startuml
		title UseCaseUseCaseRealization
		(UseCase)
		(UseCase)
		(UseCase) ..|> (UseCase) : label
		@enduml
		'''.toString, getDiagram(diagramIterator, "UseCaseUseCaseRealization"));
	}
	
	@Test
	def void ActorUseCaseAssociationLabelTest() {
		assertDiagramEquals('''
		@startuml
		title ActorUseCaseAssociationLabel
		:Actor:
		(UseCase)
		(UseCase) -- :Actor: : label
		@enduml
		'''.toString, getDiagram(diagramIterator, "ActorUseCaseAssociationLabel"));
	}
	
	@Test
	def void ActorPackageUsageTest() {
		assertDiagramEquals('''
		@startuml
		title ActorPackageUsage
		rectangle Package {
		}
		:Actor:
		:Actor: ..> Package : use
		@enduml
		'''.toString, getDiagram(diagramIterator, "ActorPackageUsage"));
	}
	
	@Test
	def void PackagePackageDependencyTest() {
		assertDiagramEquals('''
		@startuml
		title PackagePackageDependency
		rectangle A {
		}
		rectangle B {
		}
		B ..> A : label
		@enduml
		'''.toString, getDiagram(diagramIterator, "PackagePackageDependency"));
	}
	
	@Test
	def void PackagePackageAbstractionTest() {
		assertDiagramEquals('''
		@startuml
		title PackagePackageAbstraction
		rectangle A {
		}
		rectangle B {
		}
		B ..> A : abstraction
		@enduml
		'''.toString, getDiagram(diagramIterator, "PackagePackageAbstraction"));
	}
	
	@Test
	def void ActorUseCaseGeneralizationTest() {
		assertDiagramEquals('''
		@startuml
		title ActorUseCaseGeneralization
		:Actor:
		:Actor: --|> (UseCase)
		(UseCase)
		@enduml
		'''.toString, getDiagram(diagramIterator, "ActorUseCaseGeneralization"));
	}
	
	@Test
	def void UseCaseUseCaseIncludeTest() {
		assertDiagramEquals('''
		@startuml
		title UseCaseUseCaseInclude
		(A)
		(B) .> (A) : include
		@enduml
		'''.toString, getDiagram(diagramIterator, "UseCaseUseCaseInclude"));
	}
	
	@Test
	def void UseCaseUseCaseExtendTest() {
		assertDiagramEquals('''
		@startuml
		title UseCaseUseCaseExtend
		(B) .> (A) : extend
		@enduml
		'''.toString, getDiagram(diagramIterator, "UseCaseUseCaseExtend"));
	}
	
	@Test
	def void PackageMergeTest() {
		assertDiagramEquals('''
		@startuml
		title PackageMerge
		rectangle A {
		}
		A ..> B : merge
		rectangle B {
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "PackageMerge"));
	}
	
	@Test
	def void PackageImportTest() {		
		assertDiagramEquals('''
		@startuml
		title PackageImport
		rectangle A {
		}
		A ..> B : import
		rectangle B {
		}
		@enduml
		'''.toString, getDiagram(diagramIterator, "PackageImport"));
	}
	
	@Test
	def void ActorStereotypeTest() {
		assertDiagramEquals('''
		@startuml
		title ActorStereotype
		:Person: << Human >>
		@enduml
		'''.toString, getDiagram(diagramIterator, "ActorStereotype"));
	}
	
	@Test
	def void ActorCommentTest() {
		assertDiagramEquals('''
		@startuml
		title ActorComment
		:Actor:
		note as Ncom
			comment
		end note
		Ncom .. :Actor:
		@enduml
		'''.toString, getDiagram(diagramIterator, "ActorComment"));
	}
		
	@Test
	def void unsupportedEObjectShapeTest() {
		var value = mock(typeof(EClass));
		generator.compileUseCaseDiagram(setUpDiagram(value, value));
	}
	
	/**
	 * Checks if the compiled diagram is the same as the expected.
	 * @expected the expected output
	 * @diagram the diagram to compile
	 */
	def void assertDiagramEquals(String expected, Diagram diagram) {
		assertEquals(expected, generator.compileUseCaseDiagram(diagram).toString)
	}
}