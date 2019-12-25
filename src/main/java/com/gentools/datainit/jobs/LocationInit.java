package com.gentools.datainit.jobs;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.dodo.generate.datainit.BusinessDataInitFace;
import com.dodo.privilege.entity.admin_1.location_6.City;
import com.dodo.privilege.entity.admin_1.location_6.Country;
import com.dodo.privilege.entity.admin_1.location_6.District;
import com.dodo.privilege.entity.admin_1.location_6.Province;
import com.dodo.utils.ExcelUtils;
import com.dodo.utils.ExcelUtils.ExcelRowHander;
import com.dodo.utils.ExcelUtils.ExcelSheetHander;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class LocationInit extends BusinessDataInitFace {
    @Override
    public void doInitBusiness(Session session) throws Exception {
        ClassLoader loader = LocationInit.class.getClassLoader();
        File classFile = new File(loader.getResource("").toURI());
        File projectFile = classFile.getParentFile().getParentFile();
        File dataFile = new File(projectFile, "data/location.xlsx");

        ExcelSheetHander sheetHander = ExcelUtils.getExcelData(dataFile, false);
        Country countryChina = (Country) session.createQuery("from Country c where c.name = '中国'").uniqueResult();
        if (countryChina == null) {
            countryChina = new Country();
            countryChina.setAreaCode("0086");
            countryChina.setCreateDate(new Date());
            countryChina.setModifyDate(new Date());
            countryChina.setInUse(Boolean.TRUE);
            countryChina.setName("中国");
            countryChina.setSortSeq(0);
            session.save(countryChina);
        }

        int provinceSortSeq = 0;
        int citySortSeq = 0;
        int districtSortSeq = 0;

        List<String> rowData = null;
        while (sheetHander.hasNextSheet()) {
            ExcelRowHander rowHander = sheetHander.nextSheet();
            while (rowHander.hasNextRow()) {
                rowData = rowHander.nextRow();
                RowModel rowModel = new RowModel(rowData.get(0), rowData.get(1), rowData.get(2), rowData.get(3),
                        rowData.get(4));
                Province province = (Province) session.createQuery("from Province p where p.name = :provinceName")
                        .setParameter("provinceName", rowModel.provinceName).uniqueResult();
                if (province == null) {
                    province = new Province();
                    province.setCountry(countryChina);
                    province.setCreateDate(new Date());
                    province.setInUse(Boolean.TRUE);
                    province.setModifyDate(new Date());
                    province.setName(rowModel.provinceName);
                    province.setSortSeq(provinceSortSeq++);
                    session.save(province);
                }
                City city = (City) session.createQuery("from City c where c.name = :cityName and c.areaCode=:areaCode")
                        .setParameter("cityName", rowModel.cityName).setParameter("areaCode", rowModel.areaCode)
                        .uniqueResult();
                if (city == null) {
                    city = new City();
                    city.setProvince(province);
                    city.setCreateDate(new Date());
                    city.setInUse(Boolean.TRUE);
                    city.setModifyDate(new Date());
                    city.setName(rowModel.cityName);
                    city.setSortSeq(citySortSeq++);
                    city.setAreaCode(rowModel.areaCode);
                    session.save(city);
                }
                District district = (District) session
                        .createQuery("from District d where d.name = :districtName and d.postCode=:postCode")
                        .setParameter("districtName", rowModel.districtName)
                        .setParameter("postCode", rowModel.postCode).uniqueResult();
                if (district == null) {
                    district = new District();
                    district.setCity(city);
                    district.setCreateDate(new Date());
                    district.setInUse(Boolean.TRUE);
                    district.setModifyDate(new Date());
                    district.setName(rowModel.districtName);
                    district.setSortSeq(districtSortSeq++);
                    district.setPostCode(rowModel.postCode);
                    session.save(district);
                }
            }
        }
    }

    private class RowModel {
        private String provinceName;
        private String cityName;
        private String areaCode;
        private String districtName;
        private String postCode;

        public RowModel(String provinceName, String cityName, String areaCode, String districtName, String postCode) {
            super();
            this.provinceName = provinceName;
            this.cityName = cityName;
            this.areaCode = areaCode;
            this.districtName = districtName;
            this.postCode = postCode;
        }

        @Override
        public String toString() {
            return "RowModel [provinceName=" + provinceName + ", cityName=" + cityName + ", areaCode=" + areaCode
                    + ", districtName=" + districtName + ", postCode=" + postCode + "]";
        }
    }
}
