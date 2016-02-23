package edu.kit.ipd.sdq.vitruvius.framework.util.doers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class ForwardChangeRecorder extends ChangeRecorder {
  private Map<EObject, URI> eObjectToProxyURIMap;
  
  private final ResourceSet resourceSet;
  
  public ForwardChangeRecorder(final ResourceSet resourceSet) {
    this.resourceSet = resourceSet;
    this.setRecordingTransientFeatures(false);
  }
  
  public void beginRec() {
    HashMap<EObject, URI> _hashMap = new HashMap<EObject, URI>();
    this.setEObjectToProxyURIMap(this.eObjectToProxyURIMap = _hashMap);
    this.beginRecording(Collections.<ResourceSet>unmodifiableList(CollectionLiterals.<ResourceSet>newArrayList(this.resourceSet)));
  }
  
  public ChangeDescription endRec() {
    return this.endRec(true);
  }
  
  public ChangeDescription endRec(final boolean copy) {
    final ChangeDescription cd = this.endRecording();
    if (copy) {
      cd.copyAndReverse(this.eObjectToProxyURIMap);
    } else {
      cd.applyAndReverse();
    }
    return cd;
  }
  
  public ChangeDescription restartRec() {
    return this.restartRec(true);
  }
  
  public ChangeDescription restartRec(final boolean copy) {
    final ChangeDescription cd = this.endRec(copy);
    this.beginRec();
    return cd;
  }
}
