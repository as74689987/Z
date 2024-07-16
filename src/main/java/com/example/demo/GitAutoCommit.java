package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class GitAutoCommit {

    /**
     * git提交推送
     * @param args
     */
    public static void main(String[] args) {
        // 设置Git仓库路径
        String repoPath = "E:\\works\\Z";

        // Git命令的完整路径
        String gitPath = "E:\\Git\\bin\\git.exe"; // 请根据实际安装路径调整

        // 执行Git命令：添加所有更改
        executeGitCommandWiondos(repoPath, gitPath, "add .");
        // 检查是否有更改需要提交
        if (hasChangesToCommit(repoPath, gitPath)) {
            // 执行Git命令：提交更改
            executeGitCommandWiondos(repoPath, gitPath, "commit -m \"Automated commit message\"");
            // 执行Git命令：推送到远程仓库
            executeGitCommandWiondos(repoPath, gitPath, "push origin main"); // 如果你的分支是main
        } else {
            System.out.println("No changes to commit.");
        }
    }

    private static boolean hasChangesToCommit(String repoPath, String gitPath) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(repoPath));
        processBuilder.command(gitPath, "status", "--porcelain");

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            String line = reader.readLine();
            return line != null; // 如果有任何输出，说明有更改
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查看是否提交到暂存区
     * @param repoPath
     * @param gitPath
     * @param command
     */
    private static void executeGitCommandWiondos(String repoPath, String gitPath, String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(repoPath));
        processBuilder.command("cmd.exe", "/c", gitPath + " " + command);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(),"GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Command failed with exit code " + exitCode + ": " + command);
            } else {
                System.out.println("Command executed successfully: " + command);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void executeGitCommandUnix(String repoPath, String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(repoPath));
        processBuilder.command("bash", "-c", command);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Command failed with exit code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
