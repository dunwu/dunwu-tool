package io.github.dunwu.tool;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author peng.zhang
 * @date 2020/12/11
 */
public class ScanUtil {

    private static final String REGEX = "^\\s*private\\s*\\w*\\s*[a-z][A-Z]\\w*;$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static void main(String[] args) {
        final String rootPath = "D:\\Codes\\vivo\\music-zp\\music-mng";
        List<File> files =
            FileUtil.loopFiles(rootPath, pathname -> {
                String filename = pathname.getAbsolutePath();
                if ("java".equalsIgnoreCase(FileTypeUtil.getType(pathname))) {
                    if (StrUtil.contains(filename, "ServiceImpl.java")) {
                        return false;
                    } else if (StrUtil.contains(filename, "Controller.java")) {
                        return false;
                    }
                    return true;
                }
                return false;
            });
        if (CollectionUtil.isEmpty(files)) {
            System.out.printf("%s 目录下没有文件\n", rootPath);
            return;
        }

        files.parallelStream().forEach(ScanUtil::scanFileContentByRegex);
    }

    private static void scanFileContentByRegex(File file) {
        int lineNum = 0;
        List<String> lines = FileUtil.readUtf8Lines(file);
        if (CollectionUtil.isEmpty(lines)) {
            return;
        }
        for (String line : lines) {
            lineNum++;
            boolean flag = RegexUtil.checkMatches(PATTERN, line);
            if (flag) {
                System.out.printf("【%s】 【第%d行】 内容：%s\n", file.getAbsoluteFile(), lineNum, line);
            }
        }
    }

}
