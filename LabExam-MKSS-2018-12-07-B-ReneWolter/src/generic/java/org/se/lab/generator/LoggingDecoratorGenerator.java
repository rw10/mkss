package org.se.lab.generator;

import org.se.lab.metamodel.*;

import java.util.List;

public class LoggingDecoratorGenerator extends ClassGeneratorBase {

    @Override
    public void visit(MPackage mPackage) {
        // add log4j package to import
        List<MImport> imports = mPackage.getImports();
        imports.add(new MImport("org.apache.log4j.Logger"));
        super.visit(mPackage);
    }

    @Override
    public void visit(MInterface mInterface) {
        String name = mInterface.getName();

        // class definition
        for (MTypeModifier mTypeModifier : mInterface.getModifiers()){
            visit(mTypeModifier);
            codeBuilder.append(" ");
        }
        codeBuilder.append("class ").append(generateInterfaceName(mInterface)).append("\n")
                .append("\textends ").append(name).append("Decorator").append("\n")
                .append("{\n");

        // reference to logger
        codeBuilder.append("\t/*\n")
                .append("\t * Reference: LOG:Logger\n")
                .append("\t */\n")
                .append("\tprivate final Logger LOG = Logger.getLogger(").append(name).append("Impl.class);\n\n");

        // Constructor
        codeBuilder.append("\t/*\n")
                .append("\t * Constructor: ").append(generateInterfaceName(mInterface)).append("\n")
                .append("\t */\n")
                .append("\tpublic ").append(generateInterfaceName(mInterface)).append("(").append(name).append(" service)\n")
                .append("\t{\n")
                .append("\t\tsuper(service);\n")
                .append("\t}\n");

        // commentary
        codeBuilder.append("\n")
                .append("\t/*\n")
                .append("\t * Business methods\n")
                .append("\t */\n");

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
        // first line -> logging
        codeBuilder.append("\t{\n")
                .append("\t\tLOG.debug(\"").append(mOperation.getName()).append("(");


        int lastIndex = mOperation.getParameters().size() - 1;
        // count used for not putting a ',' on first element
        // not trivial, because 'password'-called parameter are skipped
        int count = 0;
        for (int index = 0; index <= lastIndex; index++) {
            // skip, if parameter is called password
            if (!(mOperation.getParameters().get(index).getName().toLowerCase().equals("password"))) {
                if (count == 0) {
                    codeBuilder.append("\" + ");
                } else {
                    codeBuilder.append("\",\" + ");
                }
                count++;
                // only use the parameter name here
                visit((MNamedElement) mOperation.getParameters().get(index));
                codeBuilder.append(" + ");
            }
        }
        if (count > 0){
            codeBuilder.append("\"");
        }
        codeBuilder.append(")\");\n");

        // second line -> call function
        codeBuilder.append("\t\t");
        boolean opHasReturnValue = !(mOperation.getType().getName().equals("void"));
        if (opHasReturnValue) {
            // store return object
            visit(mOperation.getType());
            codeBuilder.append(" obj = ");
        }
        codeBuilder.append("super.").append(mOperation.getName()).append("(");
        // parameters in method call
        for (int index = 0; index <= lastIndex; index++) {
            codeBuilder.append(mOperation.getParameters().get(index).getName());
            if (index != lastIndex) {
                codeBuilder.append(", ");
            }
        }
        codeBuilder.append(");\n");
        if (opHasReturnValue) {
            codeBuilder.append("\t\tLOG.debug(\"    \" + obj);\n")
                    .append("\t\treturn obj;\n");
        }

        // operation closing bracket
        codeBuilder.append("\t}\n");
    }

    @Override
    protected String generateInterfaceName(MInterface mInterface) {
        return mInterface.getName() + "LoggingDecorator";
    }
}
