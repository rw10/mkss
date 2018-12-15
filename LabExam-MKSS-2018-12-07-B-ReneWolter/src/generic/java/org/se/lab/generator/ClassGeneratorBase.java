package org.se.lab.generator;

import org.se.lab.business.ServiceException;
import org.se.lab.metamodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public abstract class ClassGeneratorBase implements InterfaceGenerator {

    protected StringBuilder codeBuilder;

    public String generateSourceCode(MPackage mPackage){
        // initialize
        codeBuilder = new StringBuilder();

        // visit model
        visit(mPackage);

        // build and return
        String sourcecode = codeBuilder.toString();
        return sourcecode;
    }

    public void generateSourceCodeFile(MPackage mPackage){
        if (mPackage.getInterfaces().size() == 0){
            throw new IllegalArgumentException("Package has no Interface!");
        }

        String sourcecode = generateSourceCode(mPackage);
        String path = "src/generated/java/org/se/lab/business/";
        String filename = path + generateInterfaceName(mPackage.getInterfaces().get(0)) + ".java";

        // write to file
        try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
            out.print(sourcecode);
        } catch (FileNotFoundException e) {
            throw new ServiceException("IO-Error while writing file", e);
        }

        System.out.println("\nwritten to file: " + filename);
    }

    /*
     * Default visitor logic
     */

    @Override
    public void visit(MPackage mPackage) {
        // mark as generated
        codeBuilder.append("// Generated Class\n\n");

        // define package
        codeBuilder.append("package ").append(mPackage.getName()).append(";\n\n");

        // imports
        for (MImport mImport : mPackage.getImports()){
            codeBuilder.append("import ");
            visit(mImport);
            codeBuilder.append(";\n");
        }
        if (mPackage.getImports().size() != 0){
            codeBuilder.append("\n");
        }

        // visit interfaces
        for (MInterface mInterface : mPackage.getInterfaces()){
            visit(mInterface);
        }
    }

    @Override
    public void visit(MType mType) {
        for (MTypeModifier mTypeModifier : mType.getModifiers()){
            visit(mTypeModifier);
            codeBuilder.append(" ");
        }
        visit((MNamedElement) mType);
    }

    @Override
    public void visit(MImport mImport) {
        visit((MNamedElement) mImport);
    }

    @Override
    public void visit(MOperation mOperation) {
        // build the operation declaration, without body!!

        codeBuilder.append("\n")
                .append("\t/*\n")
                .append("\t * Operation: ");
        visit((MNamedElement) mOperation);
        codeBuilder.append("\n")
                .append("\t */\n");

        codeBuilder.append("\t@Override\n")
                .append("\tpublic ");

        // return type
        visit(mOperation.getType());

        // operation name
        codeBuilder.append(" ");
        visit((MNamedElement) mOperation);
        codeBuilder.append("(");

        // parameters
        int lastIndex = mOperation.getParameters().size() - 1;
        for (int index = 0; index <= lastIndex; index++) {
            visit(mOperation.getParameters().get(index));
            if (index != lastIndex) {
                codeBuilder.append(", ");
            }
        }
        codeBuilder.append(")\n");
    }

    @Override
    public void visit(MNamedElement mNamedElement) {
        codeBuilder.append(mNamedElement.getName());
    }

    @Override
    public void visit(MParameter mParameter) {
        visit((MTypedElement) mParameter);
    }

    @Override
    public void visit(MTypedElement mTypedElement) {
        visit(mTypedElement.getType());
        codeBuilder.append(" ").append(mTypedElement.getName());
    }

    @Override
    public void visit(MTypeModifier mTypeModifier){
        visit((MNamedElement) mTypeModifier);
    }

    protected abstract String generateInterfaceName(MInterface mInterface);
}
