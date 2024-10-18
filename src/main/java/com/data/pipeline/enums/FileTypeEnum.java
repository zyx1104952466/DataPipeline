package com.data.pipeline.enums;

/**
 * @author zhangyux
 * @since 2024/8/6 14:26
 */
public enum FileTypeEnum {

    CSV(".csv"),
    TXT(".txt"),
    JSON(".json"),
    XML(".xml"),
    XLS(".xls"),
    XLSX(".xlsx");

    private final String type;

    FileTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
