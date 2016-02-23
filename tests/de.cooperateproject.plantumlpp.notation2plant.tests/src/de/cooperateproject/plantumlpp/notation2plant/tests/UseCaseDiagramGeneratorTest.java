package de.cooperateproject.plantumlpp.notation2plant.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.infra.viewpoints.style.StylePackage;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Iterables;

import de.cooperateproject.notation2plant.UseCaseDiagramGenerator;

public class UseCaseDiagramGeneratorTest extends AbstractDiagramGeneratorTest{

	private static UseCaseDiagramGenerator generator;

	@BeforeClass
	public static void setUp() {
		generator = new UseCaseDiagramGenerator();
		ressourceSetUp();
		
	}

	@Test
	public void nullDiagramTest() {
		generator.compileUseCaseDiagram(null);
	}

	@Test
	public void test1() throws IOException {
		String s = "";
		for (final Diagram d : getDiagrams("testdiagrams/usecase.notation")) {
			s = generator.compileUseCaseDiagram(d).toString();
			s = s;
		}
		
	}
	
	@Test
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
		generator.compileUseCaseDiagram(diagram);
	}
	
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}
