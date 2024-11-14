package com.data.pipeline.file.business;

/**
 * @author zhangyux
 * @since 2024/11/11 下午7:47
 */
public interface SqlConverter {
    String convertToSql(String sqlTemplate, String[] split);
}
