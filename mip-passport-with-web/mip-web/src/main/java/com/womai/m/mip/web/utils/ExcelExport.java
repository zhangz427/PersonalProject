package com.womai.m.mip.web.utils;

import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.domain.geolocation.GeolocationInfo;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zheng.zhang on 2016/4/7.
 */
public class ExcelExport {

    public static void exportGeolocationInfo(String fileName) throws Exception {
        Workbook rwb=Workbook.getWorkbook(new File(fileName));
        Sheet rs=rwb.getSheet("Sheet1");//或者rwb.getSheet(0)
        int clos=rs.getColumns();//得到所有的列
        int rows=rs.getRows();//得到所有的行

        System.out.println("column:"+clos+",row:"+rows);

        List<GeolocationInfo> geolocationInfoList = new ArrayList<GeolocationInfo>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < clos; j++) {

                GeolocationInfo geolocationInfo = new GeolocationInfo();
                //第一个是列数，第二个是行数
                String provinceId= StringUtils.trim(rs.getCell(j++, i).getContents());//默认最左边编号也算一列 所以这里得j++
                String provinceName=StringUtils.trim(rs.getCell(j++, i).getContents());
                String cityId=StringUtils.trim(rs.getCell(j++, i).getContents());
                String cityName=StringUtils.trim(rs.getCell(j++, i).getContents());
                String mid=StringUtils.trim(rs.getCell(j++, i).getContents());

                geolocationInfo.setProvinceId(provinceId);
                geolocationInfo.setProvinceName(provinceName);
                geolocationInfo.setCityId(cityId);
                geolocationInfo.setCityName(cityName);
                geolocationInfo.setMid(mid);

                geolocationInfoList.add(geolocationInfo);

            }

        }

        String jsonStr = JacksonUtil.toJson(geolocationInfoList);

        System.out.println(jsonStr);


        List<GeolocationInfo> geolocationInfoList1 = (List<GeolocationInfo>)JacksonUtil.toList(jsonStr, GeolocationInfo.class);
        for (GeolocationInfo geolocationInfo : geolocationInfoList1) {
            System.out.println(geolocationInfo.toString());
        }

    }

    public static void main(String[] args) throws Exception {
        exportGeolocationInfo("D://cityInfo.xls");

    }
}
