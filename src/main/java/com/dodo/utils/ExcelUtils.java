package com.dodo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public final class ExcelUtils {
    private static final Logger        LOGGER     = LoggerFactory.getLogger(ExcelUtils.class);
    private static final DecimalFormat readFormat = new DecimalFormat("#.######");

    /** Excel 批量导出使用 */
    public static Workbook writeWorkbookTitle(Workbook workbook, List<String> titles, List<String> oneRecord) {
        return writeWorkbookTitle(workbook, titles.toArray(new String[] {}), oneRecord);
    }

    /** Excel 批量导出使用 */
    public static Workbook writeWorkbookTitle(Workbook workbook, String[] titles, List<String> oneRecord) {
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        titleStyle.setFont(boldFont);

        Sheet sheet = workbook.createSheet();
        Row row1 = sheet.createRow(0);
        Row row2 = sheet.createRow(1);
        int subBegin = -1;
        String subTitle = "";
        String tempString = null;
        int j = 0;
        for (int i = 0; i < titles.length; i++) {
            if (titles[i].contains("|")) {
                row2.createCell(j).setCellValue(StringUtils.substringAfter(titles[i], "|"));
            } else {
                sheet.addMergedRegion(new CellRangeAddress(0, 1, j, j));
                Cell cell = row1.createCell(j);
                cell.setCellValue(titles[i]);
                cell.setCellStyle(titleStyle);
            }
            sheet.autoSizeColumn(j++, true);

            if (subBegin == -1 && titles[i].contains("|")) {
                subBegin = j - 1;
                subTitle = StringUtils.substringBefore(titles[i], "|");
            } else if (subBegin != -1) {
                tempString = null;
                if (titles[i].contains("|")) {
                    tempString = StringUtils.substringBefore(titles[i], "|");
                }
                if (tempString == null || !tempString.equals(subTitle)) {
                    Cell cell = row1.createCell(subBegin);
                    cell.setCellValue(subTitle);
                    cell.setCellStyle(titleStyle);
                    if (subBegin != j - 2) {
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, subBegin, j - 2));
                    }
                    subBegin = -1;
                    subTitle = "";
                    if (tempString != null) {
                        subBegin = j - 1;
                        subTitle = tempString;
                    }
                }
            }
        }

        if (subBegin != -1) {
            Cell cell = row1.createCell(subBegin);
            cell.setCellValue(subTitle);
            cell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, subBegin, j - 1));
        }

        sheet.createFreezePane(0, 2, 0, 2);
        return workbook;
    }

    /** Excel 批量导出使用 */
    public static Workbook writeWorkbookContent(Workbook workbook, int sheetIndex, List<List<String>> contents,
            int beginRow) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        int beginRowTemp = beginRow;
        for (List<String> list : contents) {
            Row row = sheet.createRow(beginRowTemp++);
            for (int i = 0, j = 0; i < list.size(); i++) {
                row.createCell(j++).setCellValue(list.get(i));
            }
        }
        return workbook;
    }

    /** Excel 批量导入/批量更新 模板使用 */
    public static Workbook writeWorkbookTitle(Workbook workbook, List<String> titles, List<String> comments,
            Map<String, String[]> selectValues) {
        return writeWorkbookTitle(workbook, titles.toArray(new String[] {}), comments.toArray(new String[] {}),
                selectValues);
    }

    /** Excel 批量导入/批量更新 模板使用 */
    public static Workbook writeWorkbookTitle(Workbook workbook, String[] titles, String[] comments,
            Map<String, String[]> selectValues) {
        CreationHelper helper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        Row rowData = sheet.createRow(1);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            if (StringUtils.isNotBlank(comments[i])) {
                //				String[] strings = comments[i].split("\n");
                //				int maxLength = 0;
                //				for(String string:strings){
                //					if(string.length()>maxLength){
                //						maxLength = string.length(); 
                //					}
                //				}
                //				maxLength = Math.round(maxLength/3);
                //				if(maxLength<5){
                //					maxLength = 8;
                //				}
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(cell.getColumnIndex());
                anchor.setCol2(cell.getColumnIndex() + 3);
                anchor.setRow1(row.getRowNum());
                anchor.setRow2(row.getRowNum() + 15);

                Drawing<?> patr = sheet.createDrawingPatriarch();
                //HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,(short) 3, 3, (short) (maxLength), 4+strings.length);
                Comment comment = patr.createCellComment(anchor);
                comment.setString(helper.createRichTextString(comments[i]));
                comment.setAuthor("Dodo System");
                cell.setCellComment(comment);
            }
            cell.setCellValue(titles[i]);
            String[] selectValue = selectValues.get(titles[i]);
            if (selectValue != null && selectValue.length > 0) {
                //              way one 				
                Sheet hiddenSheet = workbook.createSheet("hiddenSheet" + i);
                for (int j = 0, length = selectValue.length; j < length; j++) {
                    hiddenSheet.createRow(j).createCell(0).setCellValue(selectValue[j]);
                }
                Name namedCell = workbook.createName();
                namedCell.setNameName("hiddenSheet" + i);
                namedCell.setRefersToFormula("hiddenSheet" + i + "!$A$1:$A$" + (selectValue.length + 1));
                CellRangeAddressList addressList = new CellRangeAddressList(rowData.getRowNum(), (Short.MAX_VALUE * 2),
                        i, i);
                if (workbook.getSpreadsheetVersion() == SpreadsheetVersion.EXCEL97) {
                    //                    DVConstraint dvConstraint = DVConstraint.createFormulaListConstraint("hiddenSheet" + i);
                    //                    HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);

                    HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper((HSSFSheet) sheet);
                    DVConstraint dvConstraint = (DVConstraint) dvHelper.createFormulaListConstraint("hiddenSheet" + i);
                    HSSFDataValidation dataValidation = (HSSFDataValidation) dvHelper.createValidation(dvConstraint,
                            addressList);
                    dataValidation.setSuppressDropDownArrow(false);
                    dataValidation.setEmptyCellAllowed(false);
                    dataValidation.setShowErrorBox(true);
                    dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
                    sheet.addValidationData(dataValidation);
                } else {
                    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
                    XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                            .createFormulaListConstraint("hiddenSheet" + i);
                    XSSFDataValidation dataValidation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint,
                            addressList);
                    dataValidation.setSuppressDropDownArrow(true);
                    dataValidation.setEmptyCellAllowed(false);
                    dataValidation.setShowErrorBox(true);
                    dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
                    sheet.addValidationData(dataValidation);
                }

                workbook.setSheetHidden(workbook.getSheetIndex("hiddenSheet" + i), true);
            }
        }
        sheet.createFreezePane(0, 1, 0, 1);
        sheet.autoSizeColumn(1, true);
        return workbook;
    }

    /** 读取Excel数据 */
    public static ExcelSheetHander getExcelData(File file, Boolean isReturnHidden) {
        if (file == null || (!file.exists())) {
            return null;
        }
        LOGGER.info(file.getAbsolutePath());
        try {
            return new ExcelSheetHander(WorkbookFactory.create(file), isReturnHidden);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            CellType cellType = cell.getCellType();
            switch (cellType) {
            case NUMERIC:
            case FORMULA:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellvalue = CommonUtil.getSpecialDateStr(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
                    break;
                }
                try {
                    cellvalue = readFormat.format(cell.getNumericCellValue());
                } catch (Exception e) {
                    try {
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    } catch (Exception e1) {
                        cellvalue = cell.getCellFormula();
                    }
                }
                break;
            case STRING:
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            case BOOLEAN:
                cellvalue = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                cellvalue = "";
            }
        }
        return cellvalue;
    }

    public static class ExcelSheetHander {
        private CellStyle            successCellStyle    = null;
        private CellStyle            failCellStyle       = null;
        private CellStyle            infoTipCellStyle    = null;
        private Workbook             workbook;
        private int                  currentSheetIndex   = -1;
        private Sheet                currentSheet;
        private int                  totalDealRow        = 0;
        private int                  successRow          = 0;
        private int                  checkErrorRow       = 0;
        private boolean              titleError          = false;
        private boolean              isReturnHidden      = false;

        private int                  initTotalRow        = 0;

        private Map<String, Integer> sheetSuccessCounter = new HashMap<String, Integer>();

        private ExcelSheetHander(Workbook workbook, boolean isReturnHidden) {
            this.workbook = workbook;
            this.isReturnHidden = isReturnHidden;
            Font checkSuccessFont = this.workbook.createFont();
            checkSuccessFont.setBold(true);
            checkSuccessFont.setFontHeightInPoints((short) 10);
            checkSuccessFont.setColor(HSSFColorPredefined.GREEN.getIndex());
            successCellStyle = this.workbook.createCellStyle();
            successCellStyle.setFont(checkSuccessFont);
            Font checkFailFont = this.workbook.createFont();
            checkFailFont.setBold(true);
            checkFailFont.setFontHeightInPoints((short) 10);
            checkFailFont.setColor(HSSFColorPredefined.RED.getIndex());
            failCellStyle = this.workbook.createCellStyle();
            failCellStyle.setFont(checkFailFont);
            infoTipCellStyle = this.workbook.createCellStyle();
            infoTipCellStyle.setFillForegroundColor(HSSFColorPredefined.RED.getIndex());
            infoTipCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Sheet tempSheet = null;
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                tempSheet = workbook.getSheetAt(i);
                initTotalRow = initTotalRow + tempSheet.getLastRowNum();
                if (!isReturnHidden && workbook.isSheetHidden(i)) {
                    initTotalRow = initTotalRow - tempSheet.getLastRowNum();
                }
            }
        }

        public boolean hasNextSheet() {
            if (this.isReturnHidden) {
                return currentSheetIndex + 1 < workbook.getNumberOfSheets();
            }
            while (currentSheetIndex + 1 < workbook.getNumberOfSheets()) {
                if (workbook.isSheetHidden(currentSheetIndex + 1)) {
                    ++currentSheetIndex;
                    continue;
                }
                return true;
            }
            return false;
        }

        public String getSuccessPercent() {
            if (initTotalRow == 0) {
                return "0.00";
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format((Double.valueOf(successRow) / initTotalRow) * 100);
        }

        public String getDealPercent() {
            if (initTotalRow == 0) {
                return "0.00";
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format((Double.valueOf(totalDealRow) / initTotalRow) * 100);
        }

        public Integer getTotalSheetCount() {
            int sheetCount = 0;
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                ++sheetCount;
                if (!isReturnHidden && workbook.isSheetHidden(i)) {
                    --sheetCount;
                }
            }
            return sheetCount;
        }

        public Integer getTotalRowCount() {
            return initTotalRow;
        }

        public ExcelRowHander nextSheet() {
            currentSheet = workbook.getSheetAt(++currentSheetIndex);
            LOGGER.info("[{}-->{}]", getCurrentSheetIndex(), getCurrentSheetName());
            if (workbook.isSheetHidden(currentSheetIndex) && !isReturnHidden) {
                LOGGER.info("[Sheet {}] is hidden,continue...", getCurrentSheetName());
                return nextSheet();
            } else {
                sheetSuccessCounter.put(currentSheet.getSheetName(), 0);
                return new ExcelRowHander(currentSheet, this);
            }
        }

        public void reset() {
            currentSheetIndex = -1;
            currentSheet = null;
            totalDealRow = 0;
            successRow = 0;
            checkErrorRow = 0;
            titleError = false;
            sheetSuccessCounter.clear();
        }

        public int getCurrentSheetIndex() {
            return currentSheetIndex;
        }

        public String getCurrentSheetName() {
            return currentSheet == null ? "" : currentSheet.getSheetName();
        }

        public int getTotalDealRow() {
            return totalDealRow;
        }

        public void addTotalDealRow() {
            this.totalDealRow++;
        }

        public int getSuccessRow() {
            return this.successRow;
        }

        public void addSuccessRow() {
            this.successRow++;
            sheetSuccessCounter.put(currentSheet.getSheetName(),
                    sheetSuccessCounter.get(currentSheet.getSheetName()) + 1);
        }

        public Map<String, Integer> getSheetSuccessCounter() {
            return sheetSuccessCounter;
        }

        public int getCheckErrorRow() {
            return this.checkErrorRow;
        }

        public void addCheckErrorRow() {
            this.checkErrorRow++;
        }

        public void exportToFile(File file) {
            if (workbook == null)
                return;
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                workbook.write(fileOutputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void setTitleError(boolean titleError) {
            this.titleError = titleError;
        }

        public boolean isTitleError() {
            return titleError;
        }

        public CellStyle getSuccessCellStyle() {
            return successCellStyle;
        }

        public CellStyle getFailCellStyle() {
            return failCellStyle;
        }

        public void setSuccessCellStyle(CellStyle successCellStyle) {
            this.successCellStyle = successCellStyle;
        }

        public void setFailCellStyle(CellStyle failCellStyle) {
            this.failCellStyle = failCellStyle;
        }

        public CellStyle getInfoTipCellStyle() {
            return infoTipCellStyle;
        }

        public void setInfoTipCellStyle(CellStyle infoTipCellStyle) {
            this.infoTipCellStyle = infoTipCellStyle;
        }
    }

    public static class ExcelRowHander {
        private Sheet            sheet;
        private int              currentRowIndex = -1;
        private Row              currentRow;
        private List<String>     rowData;
        private int              columnLength;
        private ExcelSheetHander sheetHander;
        private boolean          isError         = false;

        private ExcelRowHander(Sheet sheet, ExcelSheetHander sheetHander) {
            this.sheet = sheet;
            this.sheetHander = sheetHander;
            if (sheet != null && sheet.getRow(0) != null) {
                columnLength = sheet.getRow(0).getPhysicalNumberOfCells();
            }
        }

        public boolean isTitle() {
            return currentRowIndex == 0;
        }

        public void markErrorRow() {
            if (!isError) {
                isError = true;
                sheetHander.addCheckErrorRow();
            }
        }

        public void markTitleError() {
            sheetHander.setTitleError(true);
        }

        public void markSuccessRow() {
            sheetHander.addSuccessRow();
        }

        private void markInfoTip(String infoTip, int offset, boolean isSuccess, Integer rowIndex) {
            if (offset < 0 || StringUtils.isBlank(infoTip) || rowIndex == null || sheetHander == null)
                return;
            Row tempRow = sheet.getRow(rowIndex);
            Cell cell = tempRow.getCell(columnLength + offset);
            if (cell == null) {
                cell = tempRow.createCell(columnLength + offset);
            }
            if (isSuccess) {
                cell.setCellStyle(sheetHander.getSuccessCellStyle());
            } else {
                cell.setCellStyle(sheetHander.getFailCellStyle());
            }
            cell.setCellValue(infoTip);
        }

        public void markSuccess(String infoTip, int offset) {
            markInfoTip(infoTip, offset, true, currentRowIndex);
        }

        public void markSuccess(String infoTip, int offset, int rowIndex) {
            markInfoTip(infoTip, offset, true, rowIndex);
        }

        public void markFail(String infoTip, int offset) {
            markInfoTip(infoTip, offset, false, currentRowIndex);
        }

        public void markFail(String infoTip, int offset, int rowIndex) {
            markInfoTip(infoTip, offset, false, rowIndex);
        }

        public void addErrorComment(String comment, int cellIndex) {
            if (cellIndex > columnLength - 1 || cellIndex < 0 || StringUtils.isBlank(comment) || currentRow == null
                    || sheetHander == null || sheet == null)
                return;
            Cell cell = currentRow.getCell(cellIndex);
            if (cell == null) {
                cell = currentRow.createCell(cellIndex);
            }
            cell.setCellStyle(sheetHander.getInfoTipCellStyle());
            //			String[] strings = comment.split("\n");
            //			int maxLength = 0;
            //			for(String string:strings){
            //				if(string.length()>maxLength){
            //					maxLength = string.length(); 
            //				}
            //			}
            //			maxLength = Math.round(maxLength/3);
            //			if(maxLength<5){
            //				maxLength = 8;
            //			}

            ClientAnchor anchor = sheet.getWorkbook().getCreationHelper().createClientAnchor();
            anchor.setCol1(cell.getColumnIndex());
            anchor.setCol2(cell.getColumnIndex() + 3);
            anchor.setRow1(currentRow.getRowNum());
            anchor.setRow2(currentRow.getRowNum() + 5);

            Drawing<?> patr = sheet.createDrawingPatriarch();
            //HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,(short) 3, 3, (short) (maxLength), 4+strings.length);
            Comment hssfComment = patr.createCellComment(anchor);
            hssfComment.setString(sheet.getWorkbook().getCreationHelper().createRichTextString(comment));
            hssfComment.setAuthor("Dodo System");
            cell.setCellComment(hssfComment);
        }

        public boolean hasNextRow() {
            return (currentRowIndex + 1) <= sheet.getLastRowNum();
        }

        public List<String> nextRow() {
            isError = false;
            currentRow = sheet.getRow(++currentRowIndex);
            rowData = new ArrayList<String>();
            String cellValue = null;
            if (currentRow != null) {
                for (int i = 0; i < columnLength; i++) {
                    try {
                        cellValue = getCellFormatValue(currentRow.getCell(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cellValue = (cellValue == null ? "" : cellValue.trim());
                    rowData.add(cellValue);
                }
            } else {
                markErrorRow();
                if (hasNextRow()) {
                    return nextRow();
                }
            }
            if (!isTitle()) {
                sheetHander.addTotalDealRow();
            }
            LOGGER.info("[Sheet {},Row {}-->{}]:{}", sheet.getSheetName(), getCurrentRowIndex(), getColumnLength(),
                    ArrayUtils.toString(rowData));
            return rowData;
        }

        public void reset() {
            currentRowIndex = -1;
            currentRow = null;
            rowData = null;
        }

        public Integer getCurrSheetTotalRow() {
            return sheet.getLastRowNum();
        }

        public String getCurrSheetName() {
            return sheet.getSheetName();
        }

        public Integer getCurrSheetSuccessRow() {
            return sheetHander.getSheetSuccessCounter().get(sheet.getSheetName());
        }

        public int getCurrentRowIndex() {
            return currentRowIndex;
        }

        public int getColumnLength() {
            return columnLength;
        }

        public boolean isError() {
            return isError;
        }
    }
}