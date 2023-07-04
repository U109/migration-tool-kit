package com.zzz.migrationtoolkit.handler.dataBaseHandler;

import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.server.InitContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/4 14:12
 * @description: 管理系统缓存信息
 */
public class DataSourceProcess {

    public static void initDBConnections() {
        InitContext.DBConnectionMap = readDBConnection();
    }

    private static Map<String, DataBaseConnInfo> readDBConnection() {
        Map<String, DataBaseConnInfo> dbMap = new HashMap<>();
        DocumentBuilder docParser;
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try {
            docParser = domFactory.newDocumentBuilder();
            Document doc = docParser.parse("src/main/resources/conf/DBConnections.xml");

            //获取根节点
            Element root = (Element) doc.getElementsByTagName("dbs").item(0);

            NodeList dbList = root.getElementsByTagName("db");

            DataBaseConnInfo dbci = new DataBaseConnInfo();

            for (int i = 0; i < dbList.getLength(); i++) {
                Element dbElement = (Element) dbList.item(i);
                //数据库连接标识
                String dbType = dbElement.getAttribute("id");
                String clazzName = dbElement.getAttribute("class");
                //获取数据库连接实体对象的属性Set方法和相应的值
                Map<String, String> attrValueMap = new HashMap<>();
                NodeList childNodes = dbElement.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node node = childNodes.item(j);
                    String nodeName = node.getNodeName();
                    NodeList elementsByTagName = dbElement.getElementsByTagName(nodeName);
                    if (elementsByTagName.getLength() == 1) {
                        Element attrElement = (Element) elementsByTagName.item(0);
                        attrValueMap.put(nodeName, attrElement.getTextContent());
                    }
                }
                dbci = (DataBaseConnInfo) xmlToObj(clazzName, attrValueMap);
                dbci.setConnName(dbElement.getAttribute("name"));
                dbci.setDbType(dbType);
                dbMap.put(dbType, dbci);
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return dbMap;
    }

    private static Object xmlToObj(String clazzName, Map<String, String> attrValueMap) {
        Object obj = null;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(clazzName);
            obj = clazz.getDeclaredConstructor().newInstance();
            //对象赋值
            for (Map.Entry<String, String> entry : attrValueMap.entrySet()) {
                String setMethod = "set" + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1);
                clazz.getDeclaredMethod(setMethod, String.class).invoke(obj, entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
