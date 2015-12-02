package de.cooperateproject.notation2plant

import org.eclipse.uml2.uml.UseCase
import org.eclipse.uml2.uml.Include
import org.eclipse.uml2.uml.Actor
import org.eclipse.uml2.uml.Association
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.Generalization
import org.eclipse.uml2.uml.Dependency
import org.eclipse.uml2.uml.Realization
import org.eclipse.uml2.uml.NamedElement
import java.util.List
import org.eclipse.uml2.uml.Comment
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Extend
import org.eclipse.uml2.uml.Usage
import org.eclipse.gmf.runtime.notation.Diagram
import org.eclipse.gmf.runtime.notation.Shape
import org.eclipse.emf.ecore.EObject
import org.eclipse.gmf.runtime.notation.Connector
import org.eclipse.uml2.uml.Abstraction
import org.apache.log4j.Logger

class UseCaseDiagramGenerator {
	
	private static final Logger LOG =  Logger.getLogger("UseCaseGenerator");
	
	def compileUseCaseDiagram(Diagram diagram) {
	var shapes = diagram.children.filter(Shape).filter[x|!(x.element instanceof Comment)]
	var comments = diagram.children.filter(Shape).filter[x|x.element instanceof Comment]
	var edges = diagram.edges.filter(Connector)
	'''
	@startuml
	title «diagram.name»
	«FOR shape : shapes»
		«shape.element.declaration(shape)»
	«ENDFOR»
	«FOR edge : edges»
		«IF edge.element != null»
			«edge.element.declaration»
		«ELSE» 
			«edge.declaration»
		«ENDIF»		
	«ENDFOR»
	«FOR comment : comments»
		«comment.element.declaration(comment)»
	«ENDFOR»
	@enduml
	'''		
	}
	
	private def dispatch declaration(Actor actor, Shape s) '''
	«actor.printElement»
	«IF actor.generalizations.size > 0»«actor.generalizations.printGeneralizations(actor)»
	«ENDIF»
	'''
	
	private def dispatch declaration(Package pack, Shape s) '''
	rectangle «pack.name» {
	«FOR element : pack.packagedElements.filter(UseCase)»«element.declaration»
	«ENDFOR»	
	}
	«pack.printPackageImports»
	«pack.printPackageMerges»
	'''
	
	private def dispatch declaration(UseCase use, Shape s) '''
	«use.declaration»
	'''
		
	private def dispatch declaration(Comment comment, Shape s) {
		val noteName = "N" + comment.body.substring(0,3)
	'''
		note as «noteName»
			«comment.body»
		end note
		«FOR ae: comment.annotatedElements»
			«noteName» .. «ae.printElement»
		«ENDFOR»
	'''
	}	
		
	private def dispatch declaration(Association a){
		defineAssociation(a)
	}
	private def dispatch declaration(Realization r){
		defineRealization(r)
	}
	private def dispatch declaration(Dependency d){
		defineRealization(d)
	}
	private def dispatch declaration(Usage u){
		defineRealization(u)
	}
	private def dispatch declaration(Abstraction a){
		defineRealization(a)
	}
	
	private def defineAssociation(Association association) {
		if (association.binary) {
			val head = association.memberEnds.head
			val tail = association.memberEnds.last
			'''
			«head.type.printElement» -- «tail.type.printElement»«association.name.printLabel»
			'''
		}
	} 
	
	private def defineRealization(Realization realization) {
		val head = realization.clients.head;
		val tail = realization.suppliers.head;
		if (head != null && tail != null) {
			'''
			«head.printElement» ..|> «tail.printElement»«realization.name.printLabel»
			'''	
		}	
	} 
	
	private def defineRealization(Dependency dependency) {
		val head = dependency.clients.head;
		val tail = dependency.suppliers.head;
		if (head != null && tail != null) {
			'''
			«head.printElement» ..> «tail.printElement»«dependency.name.printLabel»
			'''	
		}	
	} 
	
	private def defineRealization(Usage usage) {
		val head = usage.clients.head;
		val tail = usage.suppliers.head;
		if (head != null && tail != null) {
			'''
			«head.printElement» ..> «tail.printElement» : use
			'''	
		}	
	} 
	private def defineRealization(Abstraction abstraction) {
		val head = abstraction.clients.head;
		val tail = abstraction.suppliers.head;
		if (head != null && tail != null) {
			'''
			«head.printElement» ..> «tail.printElement» : abstraction
			'''	
		}	
	} 
	
	private def dispatch declaration(UseCase use) '''
	«IF use.members.size > 0»«use.members.printMembers(use)»
	«ELSE»«use.printElement»
	«ENDIF»
	«IF use.generalizations.size > 0»«use.generalizations.printGeneralizations(use)»
	«ENDIF»	
	'''
	
	def printElement(Element element) {
		if (element instanceof NamedElement) 
		'''«element.printElement»'''
	}
	
	private def printElement(NamedElement element) {
		var cs = ""; 
		if (element instanceof Actor) {
			cs += ":" + element.name + ":"
		}
		else if (element instanceof UseCase) {
			cs += "(" + element.name + ")"
		}
		else if (element instanceof Package) {
			cs += element.name
		}
		for (st: element.stereotypeApplications) {
			cs += st.printStereotypeName
		}
		cs
	}	
	
	def printStereotypeName(EObject object)'''
	«" "»<< «object.eClass.name» >>
	'''
	
	private def printLabel(String label) '''
	«IF label != null» : «label»
	«ENDIF»
	'''
	
	private def printGeneralizations(List<Generalization> elements, NamedElement parent) '''
	«FOR element : elements»
		«parent.printElement» --|> «element.general.printElement»
	«ENDFOR»
	'''
	
	private def printMembers(List<NamedElement> elements, NamedElement parent) '''
	«FOR element : elements»
		«IF element instanceof Include»«parent.printElement» .> («element.addition.name») : include
			«ELSE»
			«IF element instanceof Extend»«parent.printElement» .> («element.extendedCase.name») : extend
			«ENDIF»
		«ENDIF»
	«ENDFOR»
	'''

	private def printPackageImports(Package pack) '''
	«IF pack.packageImports.size > 0»
		«FOR imp : pack.packageImports»«pack.name» ..> «imp.importedPackage.name» : import
		«ENDFOR»
	«ENDIF»	
	'''
	
	private def printPackageMerges(Package pack) '''
	«IF pack.packageMerges.size > 0»
		«FOR imp : pack.packageMerges»«pack.name» ..> «imp.mergedPackage.name» : merge
		«ENDFOR»
	«ENDIF»
	'''
	
	private def dispatch declaration(EObject o, Shape s) {
		LOG.warn("Element ignored: " + o.eClass.name);
	}
	
	private def dispatch declaration(EObject o) {
		LOG.warn("Element ignored: " + o.eClass.name);
	}
	private def dispatch declaration(Void a) {
		LOG.error("Element is " + a);
	}
	private def dispatch declaration(Void a, Shape s) {
		LOG.error("Element is " + a);
	}
}