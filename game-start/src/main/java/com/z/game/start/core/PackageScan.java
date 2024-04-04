package com.z.game.start.core;

import com.z.game.start.config.ConfigManager;
import com.z.game.start.msg.Message;
import com.z.game.start.msg.MessageCmd;
import com.z.game.start.msg.MessageCmdManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageScan {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageScan.class);

    public static void init() {
        String scanPackage = ConfigManager.getServerConfig().getScanPackage();
        Set<String> path = new HashSet<>();
        if (scanPackage != null && !scanPackage.isEmpty()) {
            path.add(scanPackage);
        }
        path.add("com.z.game");
        try {
            Set<Class<?>> classes = find(path);

            for (Class<?> clazz : classes) {
                // MessageCmd 处理
                addMessageCmd(clazz);

            }

        } catch (Exception e) {
            LOGGER.error("PackageScan find class error", e);
        }

    }

    public static void addMessageCmd(Class<?> clazz) {
        if (!Message.class.isAssignableFrom(clazz)) {
            return;
        }
        MessageCmd command = clazz.getAnnotation(MessageCmd.class);
        if (command == null) {
            return;
        }
        MessageCmdManager.registerCmd(command.cmd(), (Class<? extends Message>) clazz);
    }


    private static Set<Class<?>> find(Collection<String> packageName) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        for (String pkg : packageName) {
            classes.addAll(find(pkg));
        }
        return classes;
    }

    private static Set<Class<?>> find(String packageName) throws IOException, ClassNotFoundException {
        Set<Class<?>> result = new HashSet<Class<?>>();
        for (String className : getClassName(packageName)) {
            if (className.isEmpty()) {
                continue;
            }

            Class<?> clazz = Class.forName(className);
            //记录返回值
            result.add(clazz);
        }
        return result;
    }

    private static Set<String> getClassName(String packageName) throws IOException {
        Set<String> fileNames = new HashSet<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = loader.getResources(path);

        if (resources == null) {
            return fileNames;
        }
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();
            if ("file".equals(protocol)) {
                String filePath = URLDecoder.decode(resource.getFile(), String.valueOf(StandardCharsets.UTF_8));
                fileNames.addAll(getClassNameByFile(packageName, filePath));
            } else if ("jar".equals(protocol)) {
                JarFile jar = ((JarURLConnection) resource.openConnection()).getJarFile();
                fileNames.addAll(getClassNameByJar(packageName, jar));
            }
        }

        return fileNames;
    }

    private static Set<String> getClassNameByFile(String packageName, String filePath) throws IOException {
        Set<String> classNames = new HashSet<>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files == null) {
            return classNames;
        }
        for (final File fileEntry : files) {
            if (fileEntry.isDirectory()) {
                classNames.addAll(getClassNameByFile(packageName + "." + fileEntry.getName(), fileEntry.getAbsolutePath()));
            } else {
                String className = fileEntry.getName();
                if (className.endsWith(".class")) {
                    className = className.substring(0, className.length() - 6);
                    classNames.add(packageName + "." + className);
                }
            }
        }
        return classNames;
    }

    private static Set<String> getClassNameByJar(String packageName, JarFile jar) throws IOException {
        Set<String> classNames = new HashSet<>();

        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (entryName.endsWith(".class")) {
                if (entryName.startsWith(packageName)) {
                    entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                    classNames.add(entryName);
                }
            } else {
                int index = entryName.lastIndexOf("/");
                String packagePath = index != -1 ? packageName.substring(0, index) : entryName;
                if (packagePath.equals(packageName)) {
                    entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                    classNames.add(entryName);
                }

            }
        }

        return classNames;
    }


}
