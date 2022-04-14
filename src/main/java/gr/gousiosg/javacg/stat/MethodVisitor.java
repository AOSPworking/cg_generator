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

    private MethodMetadata methodMetadata;
    private List<MethodMetadata> callingMethods = new ArrayList<>(); // all the methods calling this method.
    private List<String> methodCalls = new ArrayList<>();

    public MethodVisitor(MethodGen m, JavaClass jc) {
        visitedClass = jc;
        mg = m;
        cp = mg.getConstantPool();

        this.methodMetadata = new MethodMetadata();
        this.methodMetadata.setFullyQualifiedName(mg.getClassName() + "." + mg.getName());
        this.methodMetadata.setNonFullyQualifiedName(mg.getName());
        this.methodMetadata.setReturnType(mg.getReturnType().toString());
        List<String> paramsType = new ArrayList<>();
        for (Type argType : mg.getArgumentTypes()) {
            paramsType.add(argType.toString());
        }
        this.methodMetadata.setParamsType(paramsType);
    }

    public MethodVisitor start() {
        if (mg.isAbstract() || mg.isNative() || mg.getMethod().getCode() == null) {
            return null;
        }
        // 遍历当前方法下的所有 instruction，对每条 instruction 都看是否需要 accept。
        for (InstructionHandle ih = mg.getInstructionList().getStart(); ih != null; ih = ih.getNext()) {
            Instruction i = ih.getInstruction();
            if (i instanceof InvokeInstruction) {
                InvokeInstructionVisitor visitor
                        = new InvokeInstructionVisitor(this.visitedClass, this.mg);
                i.accept(visitor);
                this.methodCalls.add(visitor.getCallingMethodSignature());
                this.callingMethods.add(visitor.getCallingMethod());
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

}
