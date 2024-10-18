package com.data.pipeline.file.business;

import com.data.pipeline.file.BaseFileOperations;
import org.junit.Test;

/**
 * 同时配置有银联二维码和消费的商户处理类
 *
 * @author zhangyux
 * @since 2024/9/2 下午2:21
 */
public class MercBusiType08Test extends BaseFileOperations {

    /**
     * .xlsx文件路径
     */
    public static final String SOURCE_FILE = "temp/20240706.xlsx";

    @Test
    public void test() {
        System.out.println("Test");
    }
}
