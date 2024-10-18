package com.data.pipeline.file.business;

import com.data.pipeline.enums.FilterKeywordEnum;
import com.data.pipeline.file.AbstractFileReader;
import com.data.pipeline.file.excel.ExcelFileWriter;
import com.data.pipeline.file.parser.OptimizedDataProcessor;
import com.data.pipeline.file.parser.OrderTradeFeeAmtV1Parser;
import com.data.pipeline.file.parser.OrderTradeFeeParser;
import com.data.pipeline.file.parser.OutOrgFeeParser;
import com.data.pipeline.file.parser.PositiveTradeFeeParser;
import com.data.pipeline.file.txt.TextFileReader;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OfflineCalFeeLogOperationsTest {

    AbstractFileReader<String> fileReader = new TextFileReader();
    ExcelFileWriter excelFileWriter = new ExcelFileWriter();
    OptimizedDataProcessor dataProcessor = new OptimizedDataProcessor();

    public static final String SOURCE_FILE = "E:\\日常迭代\\计费重构二期\\生产日志数据\\app.20240802.1.log";
    public static final String TARGET_FILE = "E:\\日常迭代\\计费重构二期\\生产日志数据\\app.20240802.2.xlsx";

    /**
     * Test reading the content of a file
     */
    @Test
    public void testReadFileContent() throws IOException {

        // 读取文件内容
        List<String> dataList = fileReader.readFile(SOURCE_FILE);

        // 进行数据处理
        dataProcessor.processFileData(this.filterData(dataList));

        // 写入Excel文件
        excelFileWriter.writeExcelFile(TARGET_FILE, this.buildRowData(dataProcessor.getDataList()), false);
    }

    @Test
    public void testGroupData() throws IOException {

        // 读取文件内容
        List<String> dataList = fileReader.readFile(SOURCE_FILE);

        // 过滤数据
        dataList = this.filterData(dataList);

        // 对数据进行分组
        Map<String, List<String>> groupedDataMap = this.groupData(dataList);

        for (FilterKeywordEnum value : FilterKeywordEnum.values()) {
            List<String> data = groupedDataMap.get(value.getKeyword());
            List<Map<String, String>> result;
            switch (value) {
                case START_TRANSACTION_DETAIL:
                    System.out.printf("START_TRANSACTION_DETAIL: %s\n", data.size());
                    result = data.stream().map(new OrderTradeFeeAmtV1Parser()::parseData).collect(Collectors.toList());
                    System.out.println(result);
                    break;
                case EXTERNAL_CARD_FEE_CALCULATION:
                    System.out.printf("EXTERNAL_CARD_FEE_CALCULATION: %s\n", data.size());
                    result = data.stream().map(new OrderTradeFeeParser()::parseData).collect(Collectors.toList());
                    System.out.println(result);
                    break;
                case MERCHANT_FEE_CONTENT:
                    System.out.printf("MERCHANT_FEE_CONTENT: %s\n", data.size());
                    result = data.stream().map(new PositiveTradeFeeParser()::parseData).collect(Collectors.toList());
                    System.out.println(result);
                    break;
                case OUT_ORG_FEE_CONTENT:
                    System.out.printf("OUT_ORG_FEE_CONTENT: %s\n", data.size());
                    result = data.stream().map(new OutOrgFeeParser()::parseData).collect(Collectors.toList());
                    System.out.println(result);
                    break;
                default:
                    System.out.print("未知");
                    break;
            }
        }
    }

    /**
     * 过滤数据
     * @param dataList 原始数据
     * @return 过滤后的数据
     */
    private List<String> filterData(List<String> dataList) {

        // 进行数据过滤，保留符合条件的数据
        Set<FilterKeywordEnum> filterKeywords = EnumSet.of(
            FilterKeywordEnum.START_TRANSACTION_DETAIL,
            FilterKeywordEnum.EXTERNAL_CARD_FEE_CALCULATION,
            FilterKeywordEnum.MERCHANT_FEE_CONTENT,
            FilterKeywordEnum.OUT_ORG_FEE_CONTENT
        );

        return dataList.stream()
            .filter(line -> filterKeywords.stream()
                .anyMatch(keyword -> line.contains(keyword.getKeyword())))
            .collect(Collectors.toList());
    }

    /**
     * 对数据进行分组
     * @param dataList 数据
     * @return 分组后的数据
     */
    private Map<String, List<String>> groupData(List<String> dataList) {

        return dataList.stream().collect(Collectors.groupingBy(line -> {
            for (FilterKeywordEnum value : FilterKeywordEnum.values()) {
                if (line.contains(value.getKeyword())) {
                    return value.getKeyword();
                }
            }
            return "未知";
        }));
    }

    public List<List<String>> buildRowData(List<Map<String, String>> data) {
        List<List<String>> rowData = new ArrayList<>();
//        List<String> header = Arrays.stream(FeeDataKeyEnum.values()).map(FeeDataKeyEnum::getKey).collect(Collectors.toList());
        List<String> header = new ArrayList<>();
        header.add("busiCode");
        header.add("busiType");
        header.add("cardAreaFlag");
        header.add("outOrgno");
        header.add("payMode");
        header.add("standardChecked");
        header.add("userAccountType");
        header.add("userCardOrg");
        rowData.add(header);

        for (Map<String, String> map : data) {
//            List<String> row = Arrays.stream(FeeDataKeyEnum.values()).map(dataKey -> map.get(dataKey.getKey())).collect(Collectors.toList());
            List<String> row = header.stream().map(map::get).collect(Collectors.toList());
            rowData.add(row);
        }

        return rowData;
    }
}
