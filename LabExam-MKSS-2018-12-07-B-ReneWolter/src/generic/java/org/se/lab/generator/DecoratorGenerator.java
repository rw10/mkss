package org.se.lab.generator;

import org.se.lab.metamodel.*;

public class DecoratorGenerator extends ClassGeneratorBase {

    @Override
    public void visit(MInterface mInterface) {
        String name = mInterface.getName();

        // class definition
        for (MTypeModifier mTypeModifier : mInterface.getModifiers()){
            visit(mTypeModifier);
            codeBuilder.append(" ");
        }
        codeBuilder.append("abstract class ").append(generateInterfaceName(mInterface)).append("\n")
                .append("\timplements ").append(name).append("\n")
                .append("{\n");

        // reference to service object
        codeBuilder.append("\t/*\n")
                .append("\t * Reference: service:").append(name).append("\n")
                .append("\t */\n")
                .append("\tprotected ").append(name).append(" service;\n\n");

        // Constructor
        codeBuilder.append("\t/*\n")
                .append("\t * Constructor: ").append(generateInterfaceName(mInterface)).append("\n")
                .append("\t */\n")
                .append("\tpublic ").append(generateInterfaceName(mInterface)).append("(").append(name).append(" service)\n")
                .append("\t{\n")
                .append("\t\tif(service == null)\n")
                .append("\t\t\tthrow new IllegalArgumentException(\"").append(name).append(" is null!\");\n")
                .append("\t\tthis.service = service;\n")
                .append("\t}\n\n");

        // visit operations
        for(MOperation mOperation : mInterface.getOperations()){
            visit(mOperation);
        }

        // interface-closing bracket
        codeBuilder.append("}\n");
    }

    @Override
    public void visit(MOperation mOperation) {
        // create operation header
        super.visit(mOperation);

        // operation body
        codeBuilder.append("\t{\n")
                .append("\t\t");
        if (!mOperation.getType().getName().equals("void")){
            codeBuilder.append("return ");
        }
        codeBuilder.append("service.").append(mOperation.getName()).append("(");
        // parameters in method call
        int lastIndex = mOperation.getParameters().size() - 1;
        for (int index = 0; index <= lastIndex; index++) {
            // only use the parameter name here
            visit((MNamedElement) mOperation.getParameters().get(index));
            if (index != lastIndex) {
                codeBuilder.append(", ");
            }
        }
        codeBuilder.append(");\n")
                .append("\t}\n");
    }

    @Override
    protected String generateInterfaceName(MInterface mInterface) {
        return mInterface.getName() + "Decorator";
    }
}
