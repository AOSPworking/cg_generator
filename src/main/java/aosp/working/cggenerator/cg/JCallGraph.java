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

package aosp.working.cggenerator.cg;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import aosp.working.cggenerator.dto.ClassInfo;
import aosp.working.cggenerator.dto.JarInfo;
import gr.gousiosg.javacg.stat.ClassVisitor;
import org.apache.bcel.classfile.ClassParser;

public class JCallGraph {
    private Function<ClassParser, ClassVisitor> getClassVisitor = (ClassParser cp) -> {
        try { return new ClassVisitor(cp.parse()); }
        catch (IOException e) { throw new UncheckedIOException(e); }
    };

    public JarInfo getAllInfoOfJar(String jarPath) {
        File f = new File(jarPath);
        if (!f.exists()) {
            System.err.println("Jar file " + jarPath + " does not exist");
        }

        try (JarFile jar = new JarFile(f)) {
            JarInfo jarInfo = new JarInfo();
            jarInfo.setJarPath(f.getPath());

            List<ClassInfo> classesInfo = new ArrayList<>();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                ClassInfo classInfo = new ClassInfo();
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    continue;
                }
                ClassParser cp = new ClassParser(jarPath, entry.getName());
                ClassVisitor visitor = getClassVisitor.apply(cp).start();
                classInfo.setFullyQualifiedName(visitor.getFullyQualifiedName());
                classInfo.setMethodsInfo(visitor.getMethodsInfo());
                classesInfo.add(classInfo);
            }
            jarInfo.setClassesInfo(classesInfo);

            return jarInfo;
        } catch (IOException e) {
            System.err.println("Error while processing jar: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
