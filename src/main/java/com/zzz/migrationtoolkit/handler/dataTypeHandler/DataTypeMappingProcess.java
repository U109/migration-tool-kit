package com.zzz.migrationtoolkit.handler.dataTypeHandler;

import com.zzz.migrationtoolkit.common.constants.FilePathContent;
import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataType;
import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataTypeMapped;
import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataTypeMapping;
import com.zzz.migrationtoolkit.server.InitContext;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/18 18:09
 * @description:
 */
@Slf4j
public class DataTypeMappingProcess {

    public static void initDataTypeMapping() {
        //初始化源数据类型对应表
        InitContext.SourceDataTypeMapping = readSourceDataTypeMapping();
        //初始化用户定制数据库对应类型
        InitContext.UserDataTypeMapping = readSourceDataTypeMapping();
        //初始化指定数据库所有数据类型
        InitContext.DataType = readDataType();
    }

    private static Map<String, DataTypeMapping> readSourceDataTypeMapping() {
        Map<String, DataTypeMapping> dataTypeMappingMap = new HashMap<>();
        Map<String, DataTypeMapped> dataTypeMappedMap = new HashMap<>();

        DocumentBuilder docParser;
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try {
            docParser = domFactory.newDocumentBuilder();
            Document doc = docParser.parse(FilePathContent.DATA_TYPE_MAPPING_PATH);
            // 获取根节点
            Element root = (Element) doc.getElementsByTagName("dtms").item(0);
            NodeList dtmList = root.getElementsByTagName("dtm");

            DataTypeMapping dtm;
            for (int i = 0; i < dtmList.getLength(); i++) {
                Element dtmElement = (Element) dtmList.item(i);
                String key = dtmElement.getAttribute("id");
                dtm = new DataTypeMapping();
                dtm.setMappingId(key);
                dtm.setMappingName(key);
                dtm.setSourceDBName(dtmElement.getAttribute("source"));
                dtm.setDestDBName(dtmElement.getAttribute("target"));
                dataTypeMappedMap = new HashMap<>();
                NodeList dtList = dtmElement.getElementsByTagName("dt");

                for (int j = 0; j < dtList.getLength(); j++) {
                    Element dtElement = (Element) dtList.item(j);
                    DataTypeMapped dtmd = new DataTypeMapped();
                    dtmd.setSourceDataType(new DataType(dtElement.getAttribute("source")));
                    dtmd.setDestDataType(new DataType(dtElement.getAttribute("target")));
                    dtmd.setLength(checkValue(dtElement.getAttribute("length")));
                    dtmd.setPrecision(checkValue(dtElement.getAttribute("precision")));
                    dataTypeMappedMap.put(dtElement.getAttribute("source").toUpperCase(), dtmd);
                }
                dtm.setDataTypeMapped(dataTypeMappedMap);
                dataTypeMappingMap.put(key, dtm);
            }
        } catch (Exception e) {
            log.error("Init DataTypeMapping error : " + e.getMessage());
        }
        return dataTypeMappingMap;
    }

    private static Map<String, List<DataType>> readDataType() {

        Map<String, List<DataType>> dataTypeMap = new HashMap<>();
        DocumentBuilder docParser;
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try {
            List<DataType> dataTypeList = new ArrayList<>();

            docParser = domFactory.newDocumentBuilder();
            Document doc = docParser.parse(FilePathContent.DATA_TYPE_PATH);
            // 获取根节点
            Element root = (Element) doc.getElementsByTagName("dbs").item(0);
            NodeList dbList = root.getElementsByTagName("db");
            for (int i = 0; i < dbList.getLength(); i++) {
                Element dbElement = (Element) dbList.item(i);
                String key = dbElement.getAttribute("dbType");
                dataTypeList = new ArrayList<>();

                NodeList dtList = dbElement.getElementsByTagName("dt");

                for (int j = 0; j < dtList.getLength(); j++) {
                    Element element = (Element) dtList.item(j);
                    dataTypeList.add(new DataType(element.getAttribute("name")));
                }
                dataTypeMap.put(key, dataTypeList);
            }
        } catch (Exception e) {
            log.error("Init DataTypeMapping error : " + e.getMessage());
        }
        return dataTypeMap;
    }
    private static String checkValue(String attribute) {
        try {
            if (attribute != null && Integer.parseInt(attribute) >= 0) {
                return attribute;
            }
        } catch (NumberFormatException e) {
            return "";
        }
        return "";
    }

}
