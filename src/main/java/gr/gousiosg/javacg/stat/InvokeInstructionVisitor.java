package gr.gousiosg.javacg.stat;

import aosp.working.cggenerator.dto.MethodMetadata;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

import java.util.ArrayList;
import java.util.List;

public class InvokeInstructionVisitor extends EmptyVisitor {
    private JavaClass jc;
    private MethodGen mg;
    private ConstantPoolGen cp;
    private String format;

    private String callingMethodSignature;
    private MethodMetadata callingMethod;

    public InvokeInstructionVisitor(JavaClass currentVisitingClass, MethodGen currentVisitingMethod) {
        this.jc = currentVisitingClass;
        this.mg = currentVisitingMethod;
        this.cp = mg.getConstantPool();
        this.format = "M:" + this.jc.getClassName()
                + ":" + this.mg.getName()
                + "(" + this.argumentList(this.mg.getArgumentTypes()) + ")"
                + " " + "(%s)%s:%s(%s)";
    }

    public String getCallingMethodSignature() {
        return this.callingMethodSignature;
    }

    public MethodMetadata getCallingMethod() {
        return this.callingMethod;
    }

    private String argumentList(Type[] arguments) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(arguments[i].toString());
        }
        return sb.toString();
    }

    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL i) {
        this.visitCallingMethod(i, "M");
    }

    @Override
    public void visitINVOKEINTERFACE(INVOKEINTERFACE i) {
        this.visitCallingMethod(i, "I");
    }

    @Override
    public void visitINVOKESPECIAL(INVOKESPECIAL i) {
        this.visitCallingMethod(i, "O");
    }

    @Override
    public void visitINVOKESTATIC(INVOKESTATIC i) {
        this.visitCallingMethod(i, "S");
    }

    @Override
    public void visitINVOKEDYNAMIC(INVOKEDYNAMIC i) {
        this.visitCallingMethod(i, "D");
    }

    /**
     * visitINVOKE 内部统一调用的方法。
     * @param i
     * @param invokeType 按照 invoke 类型分为 M / I / O / S / E
     */
    private void visitCallingMethod(InvokeInstruction i, String invokeType) {
        this.callingMethodSignature = String.format(
                format, invokeType,
                i.getReferenceType(cp),
                i.getMethodName(cp),
                this.argumentList(i.getArgumentTypes(cp)));
        this.callingMethod = this.makeMethodMetadata(i);
    }

    /**
     * 根据一个 Invoke 指令来构造 MethodMetadata
     * @param i invoke method 的指令。
     * @return
     */
    private MethodMetadata makeMethodMetadata(InvokeInstruction i) {
        MethodMetadata methodMetadata = new MethodMetadata();
        methodMetadata.setFullyQualifiedName(i.getClassName(cp) + "." + i.getMethodName(cp));
        methodMetadata.setNonFullyQualifiedName(i.getName(cp));
        methodMetadata.setReturnType(i.getReturnType(cp).toString());
        List<String> paramsType = new ArrayList<>();
        for (Type argumentType : i.getArgumentTypes(cp)) {
            paramsType.add(argumentType.toString());
        }
        methodMetadata.setParamsType(paramsType);
        return methodMetadata;
    }
}
