package com.data.pipeline.file.business;

import com.data.pipeline.entity.FeeRequestBO;
import com.data.pipeline.file.BaseFileOperations;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangyux
 * @since 2024/11/6 下午1:25
 */
public class OnlineFeeConfigDeficiencyTest extends BaseFileOperations {

    /**
     * .xlsx文件路径
     */
    public static final String SOURCE_FILE = "temp/计费配置匹配失败(有模板未命中)";

    @Test
    public void test() throws IOException {
        List<String> list = textFileReader.readFile(SOURCE_FILE);
        list = list.stream().filter(s -> s.toString().contains("计费配置匹配失败(有模板未命中)")).collect(Collectors.toList());
        for (String log : list) {
            FeeRequestBO feeRequestBO = FeeRequestBO.parseFromLog(log);
            System.out.println(feeRequestBO);
        }
    }
}
