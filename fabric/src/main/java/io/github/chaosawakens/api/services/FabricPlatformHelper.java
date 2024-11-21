package io.github.chaosawakens.api.services;

import com.google.common.reflect.ClassPath;
import io.github.chaosawakens.api.asm.ClassFinder;
import io.github.chaosawakens.api.loader.ModLoader;
import io.github.chaosawakens.api.platform.services.IPlatformHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public ModLoader getPlatform() {
        return ModLoader.FABRIC;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public List<Class<?>> discoverAnnotatedClasses(Class<? extends Annotation> annotationTypeClazz) {
        return getFilteredAvailableClasses(annotationTypeClazz);
    }

    protected List<Class<?>> getFilteredAvailableClasses(Class<? extends Annotation> annotationTypeClazz) { // I'm sorry (CBF to set up compile-time annotations ATM)
        if (isDevelopmentEnvironment()) {
            try {
                return ClassPath.from(getClass().getClassLoader())
                        .getAllClasses()
                        .stream()
                        .filter(curClassInfo -> {
                            final boolean[] foundValidAnnotation = {false};
                            ClassReader reader = null;

                            try {
                                reader = new ClassReader(curClassInfo.getName());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM9) {
                                private String className;

                                @Override
                                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                                    this.className = name.replace('/', '.');
                                }

                                @Override
                                public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                                    if (Type.getDescriptor(annotationTypeClazz).equals(desc)) foundValidAnnotation[0] = true;
                                    return super.visitAnnotation(desc, visible);
                                }
                            };
                            reader.accept(visitor, 0);
                            return foundValidAnnotation[0];
                        })
                        .map(ClassPath.ClassInfo::getName)
                        .map(ClassFinder::forName)
                        .collect(Collectors.toCollection(ObjectArrayList::new));
            } catch (IOException e) {
                e.printStackTrace();
                return ObjectArrayList.of();
            }
        } else return FabricLoader.getInstance().getAllMods().stream()
                .flatMap(modContainer -> modContainer.getRootPaths().stream())
                .map(curPath -> curPath.toString().replaceAll("/", curPath.getFileSystem().getSeparator()))
                .filter(name -> {
                    final boolean[] foundValidAnnotation = {false};
                    ClassReader reader = null;

                    try {
                        reader = new ClassReader(name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ClassVisitor visitor = new ClassVisitor(Opcodes.ASM9) {
                        private String className;

                        @Override
                        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                            this.className = name.replace('/', '.');
                        }

                        @Override
                        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                            if (Type.getDescriptor(annotationTypeClazz).equals(desc)) foundValidAnnotation[0] = true;
                            return super.visitAnnotation(desc, visible);
                        }
                    };
                    reader.accept(visitor, 0);
                    return foundValidAnnotation[0];
                })
                .map(ClassFinder::forName)
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }
}
