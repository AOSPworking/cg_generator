/*
 * Copyright (c) 2011 - Georgios Gousios <gousiosg@gmail.com>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package gr.gousiosg.javacg.stat;

import aosp.working.cggenerator.dto.MethodInfo;
import aosp.working.cggenerator.dto.MethodMetadata;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * MethodVisitor 是对 method 进行 visit。
 * 这并不意味着该 Visitor 可以直接获取到所有的 method invoke statement。
 * MethodVisitor 看到的应该是 class 下的 method definition。
 * 所以需要读出 method 的每条指令，再具体分析语句类别。（如是否为 method invoke 类，继而再 accept）
 */
public class MethodVisitor extends EmptyVisitor {

    JavaClass visitedClass;
    private MethodGen mg;
    private ConstantPoolGen cp;
    private String format;

    private MethodMetadata methodMetadata;
    private List<MethodMetadata> callingMethods = new ArrayList<>(); // all the methods calling this method.
    private List<String> methodCalls = new ArrayList<>();

    public MethodVisitor(MethodGen m, JavaClass jc) {
        visitedClass = jc;
        mg = m;
        cp = mg.getConstantPool();
        format = "M:" + visitedClass.getClassName() + ":" + mg.getName() + "(" + argumentList(mg.getArgumentTypes()) + ")"
            + " " + "(%s)%s:%s(%s)";
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

    public MethodVisitor start() {
        if (mg.isAbstract() || mg.isNative() || mg.getMethod().getCode() == null) {
            return null;
        }
        // 遍历当前方法下的所有 instruction，对每条 instruction 都看是否需要 accept。
        for (InstructionHandle ih = mg.getInstructionList().getStart(); ih != null; ih = ih.getNext()) {
            Instruction i = ih.getInstruction();
            if (i instanceof InvokeInstruction) {
                i.accept(this);
            }
        }
        return this;
    }

    public MethodMetadata getMethodMetadata() {
        return this.methodMetadata;
    }

    public List<MethodMetadata> getCallingMethods() {
        return this.callingMethods;
    }

    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL i) {
        methodCalls.add(String.format(format,"M",i.getReferenceType(cp),i.getMethodName(cp),argumentList(i.getArgumentTypes(cp))));
        this.callingMethods.add(this.makeMethodMetadata(i));
    }

    @Override
    public void visitINVOKEINTERFACE(INVOKEINTERFACE i) {
        methodCalls.add(String.format(format,"I",i.getReferenceType(cp),i.getMethodName(cp),argumentList(i.getArgumentTypes(cp))));
        this.callingMethods.add(this.makeMethodMetadata(i));
    }

    @Override
    public void visitINVOKESPECIAL(INVOKESPECIAL i) {
        methodCalls.add(String.format(format,"O",i.getReferenceType(cp),i.getMethodName(cp),argumentList(i.getArgumentTypes(cp))));
        this.callingMethods.add(this.makeMethodMetadata(i));
    }

    @Override
    public void visitINVOKESTATIC(INVOKESTATIC i) {
        methodCalls.add(String.format(format,"S",i.getReferenceType(cp),i.getMethodName(cp),argumentList(i.getArgumentTypes(cp))));
        this.callingMethods.add(this.makeMethodMetadata(i));
    }

    @Override
    public void visitINVOKEDYNAMIC(INVOKEDYNAMIC i) {
        methodCalls.add(String.format(format,"D",i.getType(cp),i.getMethodName(cp), argumentList(i.getArgumentTypes(cp))));
        this.callingMethods.add(this.makeMethodMetadata(i));
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
