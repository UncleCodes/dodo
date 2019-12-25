package com.dodo.testing.tests;

import java.io.File;
import java.util.List;

import org.testng.annotations.Test;

import com.dodo.utils.ExcelUtils;
import com.dodo.utils.ExcelUtils.ExcelRowHander;
import com.dodo.utils.ExcelUtils.ExcelSheetHander;

/**
 * Excel工具类测试
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ExcelTest {
    @Test
    public void testRead() throws Exception {
        ClassLoader loader = ExcelTest.class.getClassLoader();
        File classFile = new File(loader.getResource("").toURI());
        File projectFile = classFile.getParentFile().getParentFile();
        // 准备Excel文件
        File dataFile = new File(projectFile, "data/location.xlsx");

        // 解析sheet，false=不解析隐藏的sheet
        ExcelSheetHander sheetHander = ExcelUtils.getExcelData(dataFile, false);
        List<String> rowData = null;
        while (sheetHander.hasNextSheet()) {
            // 逐行读取sheet的内容
            ExcelRowHander rowHander = sheetHander.nextSheet();
            while (rowHander.hasNextRow()) {
                // 读取一行的内容
                rowData = rowHander.nextRow();
                System.err.println(rowData);
            }
        }
    }
}
