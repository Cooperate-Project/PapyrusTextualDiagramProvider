package de.cooperateproject.notation2plant

import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.gmf.runtime.notation.BasicCompartment
import org.eclipse.gmf.runtime.notation.Connector
import org.eclipse.gmf.runtime.notation.Diagram
import org.eclipse.gmf.runtime.notation.Shape
import org.eclipse.uml2.uml.AggregationKind
import org.eclipse.uml2.uml.Association
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Classifier
import org.eclipse.uml2.uml.ClassifierTemplateParameter
import org.eclipse.uml2.uml.Comment
import org.eclipse.uml2.uml.ConnectableElement
import org.eclipse.uml2.uml.ConnectableElementTemplateParameter
import org.eclipse.uml2.uml.DataType
import org.eclipse.uml2.uml.DurationObservation
import org.eclipse.uml2.uml.Enumeration
import org.eclipse.uml2.uml.EnumerationLiteral
import org.eclipse.uml2.uml.Generalization
import org.eclipse.uml2.uml.InformationItem
import org.eclipse.uml2.uml.InstanceSpecification
import org.eclipse.uml2.uml.Interface
import org.eclipse.uml2.uml.InterfaceRealization
import org.eclipse.uml2.uml.NamedElement
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.Parameter
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.Signal
import org.eclipse.uml2.uml.TemplateParameter
import org.eclipse.uml2.uml.ValueSpecification
import org.eclipse.uml2.uml.VisibilityKind
import org.apache.log4j.Logger

class ClassDiagramGenerator {
	
	private static final Logger LOG =  Logger.getLogger("PlantGenerator");
	
	
	def compileClassDiagram(Diagram diagram)''' 
		@startuml
		title «diagram.name»
		«FOR s: diagram.children.filter(Shape)»
			«s.element.declaration(s)» 
		«ENDFOR»
		«FOR edge: diagram.edges.filter(Connector)»
			«IF edge.element != null»
				«edge.element.declaration»
			«ELSE»
				«edge.declaration»
			«ENDIF»
		«ENDFOR»
		@enduml
	'''
	
	def dispatch declaration(Connector g){
		LOG.error("encountered unsupported connector " + g.eClass.name);
	}
	
	def dispatch declaration(Generalization g){
		defineGeneralization(g)
	}
	
	def dispatch declaration(Association a){
		defineAssociation(a)
	}
	
	def dispatch declaration(InterfaceRealization a){
		defineRealization(a)
	}
	
	def dispatch CharSequence declaration(Class c, Shape s)'''
		«defineClass(c)» {
		«FOR bc: s.children.filter(BasicCompartment)»«"\t"»
			«FOR innerShape: bc.children.filter(Shape)»
				«innerShape.element.declaration»
			«ENDFOR»
		«ENDFOR»}
	'''
	
	def dispatch CharSequence declaration(Interface i, Shape s)'''
		«defineInterface(i)» {
		«FOR bc: s.children.filter(BasicCompartment)»«"\t"»
			«FOR innerShape: bc.children.filter(Shape)»
				«innerShape.element.declaration»
			«ENDFOR»
		«ENDFOR»}
	'''
	
	def dispatch CharSequence declaration(Package p, Shape s)'''
		«definePackage(p)» {
		«FOR bc: s.children.filter(BasicCompartment)»«"\t"»
			«FOR innerShape: bc.children.filter(Shape)»
				«innerShape.element.declaration(innerShape)»
			«ENDFOR»
		«ENDFOR»}
	'''
	
	def dispatch CharSequence declaration(Enumeration e, Shape s)'''
		«defineEnum(e)» {
		«FOR bc: s.children.filter(BasicCompartment)»«"\t"»
			«FOR innerShape: bc.children.filter(Shape)»
				«innerShape.element.declaration»
			«ENDFOR»
		«ENDFOR»}
	'''
	
	
	def dispatch CharSequence declaration(DataType d, Shape s)'''
		«defineDataType(d)» {
		«FOR bc: s.children.filter(BasicCompartment)»«"\t"»
			«FOR innerShape: bc.children.filter(Shape)»
				«innerShape.element.declaration»
			«ENDFOR»
		«ENDFOR»}
	'''
	
	def dispatch CharSequence declaration(Signal sig, Shape s)'''
		«defineSignal(sig)» {
		«FOR bc: s.children.filter(BasicCompartment)»«"\t"»
			«FOR innerShape: bc.children.filter(Shape)»
				«innerShape.element.declaration»
			«ENDFOR»
		«ENDFOR»}
	'''
	//InformationItem
	def dispatch CharSequence declaration(InformationItem i, Shape s)'''
		class «i.name» << (>,orchid) >> {
		}
	'''

	//InstanceSpecification (no objects in class diagrams)
	def dispatch CharSequence declaration(InstanceSpecification i, Shape s)'''
		class "«i.name»:«i.classifiers.getNames»" << (i,pink) >> {
		}
	'''
	
	def getNames(EList<Classifier> list)'''
	«FOR c : list SEPARATOR ','»«c.name»«ENDFOR»
	'''
	

	
	def dispatch CharSequence declaration(Comment c, Shape s){
		val noteName = "N" + c.body.substring(0,3)
	'''
		note as «noteName»
			«c.body»
		end note
		«FOR ae: c.annotatedElements»«"\t"»
			«noteName»..«(ae as Class).name»
		«ENDFOR»
	'''
	}
	
	//Hacky duration commetn
	def dispatch CharSequence declaration(DurationObservation c, Shape s){
		val noteName = "NDuration" + c.name
	'''
		note as «noteName»
			
		end note
		«FOR ae: c.events»«"\t"»
			«noteName»..«ae.name»
		«ENDFOR»
	'''
	}
	
	def dispatch declaration(EObject e, Shape s){
		LOG.error("encountered a " + e.eClass.name);
		throw new UnsupportedOperationException		
	}
	
	
	def dispatch declaration(Property element)'''
		«IF element.static»{static}«ENDIF»
		«element.visibility.declareVisibility»«element.name»«element.declareType»
	'''
	
	def dispatch declaration(Operation element)'''
		«IF element.abstract»{abstract}«ENDIF»
		«IF element.static»{static}«ENDIF»
		«element.visibility.declareVisibility»«element.name»(«element.inputParameters.declareParameters»)«element.declareReturnType»
	'''
	
	def dispatch declaration(EnumerationLiteral element)'''
		«element.name»
	'''
	
	def declareReturnType(Operation o){
		val retTypes = o.outputParameters.filter(p | p.direction == ParameterDirectionKind.RETURN_LITERAL)
		val retType = retTypes.head	
		if(retType != null)
		''': «retType.type.name»«IF retType.multivalued»[]«ENDIF»'''
	}
	
	def declareType(Property p){
	val dataType = p.type 	
	if(dataType != null)
	''': «dataType.name»«IF p.multivalued»[]«ENDIF»'''
	}
	
	
	def dispatch declaration(EObject element){
		element.toString
	}
	
	def declareParameters(EList<Parameter> parameters)'''
		«FOR p : parameters SEPARATOR ","»«p.name»: «p.type.name»«IF p.multivalued»[]«ENDIF»«ENDFOR»'''
	
	def declareVisibility(VisibilityKind vis){
		switch vis{
			case PRIVATE_LITERAL : "-"
			case PROTECTED_LITERAL : "#"
			case PUBLIC_LITERAL : "+"
			case PACKAGE_LITERAL : "~"
			default : ""
		}
	}
	
	def defineAssociation(Association association) '''
	«IF association.binary»
	 «val head = association.memberEnds.head»
	 «val tail = association.memberEnds.last»
«head.type.name» "«head.name» «head.cardinality»" «head.leftArrow»--«tail.rightArrow»  "«tail.name» «tail.cardinality»" «tail.type.name» «IF association.name != null»: «association.name»«ENDIF»
	«ENDIF»
	'''
	
	def leftArrow(Property property){
		val opp = property.otherEnd 
		switch opp.aggregation.value{
			case AggregationKind.COMPOSITE : "*"
		 	case AggregationKind.SHARED : "o"
		 	default :
		 		if(!opp.navigable)
		 			"<"
				else
					""
			}
	}
	
	def rightArrow(Property property){
		val opp = property.otherEnd
		switch opp.aggregation.value{
			case AggregationKind.COMPOSITE : "*"
		 	case AggregationKind.SHARED : "o"
		 	default :
		 		if(!opp.navigable)
		 			">"
				else
					""
			}
	}
		
	def cardinality(Property p){
	if(p.lowerBound == 0 && p.upperBound == -1)
		''' *'''
	else if(p.lowerBound == 1 && p.upperBound == -1)
		''' 1..*'''
	else if(p.lowerBound == p.upperBound)
		'''«p.lowerBound»'''
	else
		''' «p.lowerBound»..«p.upperBound»'''	
	}
	
	
	def defineGeneralization(Generalization generalization)'''
	«generalization.general.name» <|-- «generalization.specific.name»
	'''
	
	def defineRealization(InterfaceRealization i)'''
	«i.contract.name» <|-- «i.implementingClassifier.name»
	'''
		
	def definePackage(Package p)'''
	package «p.name»'''
	
	def defineEnum(Enumeration e)'''
	enum «e.name»'''
	
	def defineDataType(DataType d)'''
	class «d.name» << (D,blue) >>'''
	
	def defineSignal(Signal sig)'''
	class «sig.name» << (S,red) >>'''
	
	def defineInterface(Interface i)'''
	interface «i.name»«for(st: i.stereotypeApplications){st.stereotypeName}»'''
	
	def defineClass(Class clazz)'''
	«IF clazz.abstract»abstract «ENDIF»class «clazz.name»«clazz.templateName»«for(st: clazz.stereotypeApplications){st.stereotypeName}»'''
	
	def getTemplateName(Class c){
		if(c.template)
			'''<«FOR p: c.ownedTemplateSignature.parameters SEPARATOR ", "»«p.templateParDef»«ENDFOR»>'''
	}
	
	def dispatch templateParDef(TemplateParameter p){
		(p.parameteredElement as NamedElement).name
	}
	
	def dispatch templateParDef(ConnectableElementTemplateParameter p){
		val conEl = p.parameteredElement as ConnectableElement
	'''«conEl.name»: «conEl.type.name»«IF p.^default != null» = «(p.^default as ValueSpecification).stringValue»«ENDIF»'''
	}
	
	def dispatch templateParDef(ClassifierTemplateParameter p)'''
		«(p.parameteredElement as NamedElement).name»«IF !p.constrainingClassifiers.empty» extends «p.constrainingClassifiers.head.name»«ENDIF»'''
	
	
	def getStereotypeName(EObject object)'''
		<<«(object as NamedElement).name»>>
	'''
}