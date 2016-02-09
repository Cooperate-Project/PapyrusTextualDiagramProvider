package de.cooperateproject.plantumlpp.notation2plant.tests;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
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

public class UseCaseDiagramGeneratorTest extends FileRessource{

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
	
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}
