package org.se.lab.generator;

import org.se.lab.metamodel.*;

public interface InterfaceGenerator {
    void visit(MPackage mPackage);
    void visit(MInterface mInterface);
    void visit(MOperation mOperation);
    void visit(MParameter mParameter);
    void visit(MType mType);
    void visit(MImport mImport);
    void visit(MNamedElement mNamedElement);
    void visit(MTypedElement mTypedElement);
    void visit(MTypeModifier mTypeModifier);
}
