package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MavenJarExtractor {
    /**
     * maven自动打包到达目录
     * @param args
     */
    public static void main(String[] args) {

        try {
            // 1. 执行 Maven 命令
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", "mvn clean package");

            // 设置工作目录为 Maven 项目目录
            File projectDir = new File("E:\\works\\demo");
            processBuilder.directory(projectDir);

            // 启动进程
            Process process = processBuilder.start();

            // 获取命令输出，指定字符编码
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK")); // Windows命令提示符默认使用GBK编码
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 获取错误输出
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), "GBK"));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // 等待命令执行完毕
            int exitCode = process.waitFor();
            System.out.println("\nExited with code: " + exitCode);

            if (exitCode == 0) {
                // 2. 查找并读取 target 目录中的 JAR 包
                File targetDir = new File(projectDir, "target");
                File[] files = targetDir.listFiles((dir, name) -> name.endsWith(".jar"));
                System.out.println(files+"111");
                if (files != null && files.length > 0) {
                    // 假设只有一个 JAR 包
                    File jarFile = files[0];
                    System.out.println("Found JAR file: " + jarFile.getAbsolutePath());

                    // 读取 JAR 包内容
                    Path jarPath = Paths.get(jarFile.getAbsolutePath());
                    byte[] jarBytes = Files.readAllBytes(jarPath);
                    
                    // 你可以将 JAR 包内容保存到其他位置，或进一步处理
                    Path outputPath = Paths.get("C:\\Users\\Administrator\\Desktop\\vmware", jarFile.getName());
                    Files.write(outputPath, jarBytes);
                    System.out.println("JAR file extracted to: " + outputPath.toAbsolutePath());
                } else {
                    System.out.println("No JAR files found in target directory.");
                }
            } else {
                System.err.println("Maven command failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
