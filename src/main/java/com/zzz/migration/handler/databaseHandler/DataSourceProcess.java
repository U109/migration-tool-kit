package com.zzz.migration.handler.databaseHandler;

import com.zzz.migration.common.constants.FilePathContent;
import com.zzz.migration.common.vo.ConnectionVO;
import com.zzz.migration.common.vo.DataSourceVO;
import com.zzz.migration.entity.dataSourceEmtity.*;
import com.zzz.migration.server.InitContext;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/4 14:12
 * @description: 管理系统缓存信息
 */
@Slf4j
public class DataSourceProcess {

    public static void initDataBaseConnections() {

    }


    /**
     * 读取DB连接
     *
     * @return Map
     */
    public static Map<String, Map<String, DataSourceProperties>> readDataSourceFromXml() {
        /*
         *         |-- nameA:DataSourceProperties
         *  Oracle |
         *         |-- nameB:DataSourceProperties
         */
        Map<String, Map<String, DataSourceProperties>> result = new HashMap<>();
        DocumentBuilder docParser;
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try {
            docParser = domFactory.newDocumentBuilder();
            Document doc = docParser.parse(FilePathContent.TASK_DB_CONNECTION);
            // 获取根节点
            Element root = (Element) doc.getElementsByTagName("dbs").item(0);
            NodeList dbList = root.getElementsByTagName("db");
            for (int i = 0; i < dbList.getLength(); i++) {
                Element dbElement = (Element) dbList.item(i);
                // 数据库连接标识
                String dbType = dbElement.getAttribute("id");
                String connName = dbElement.getAttribute("name");
                String driverPath = dbElement.getAttribute("driverPath");
                // 获取数据库连接实体对象的属性Set方法和相应的值
                Map<String, String> attrValueMap = new HashMap<>();
                NodeList childNodes = dbElement.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node node = childNodes.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element attrElement = (Element) node;
                        String nodeName = attrElement.getNodeName();
                        String nodeValue = attrElement.getTextContent();
                        attrValueMap.put(nodeName, nodeValue);
                    }
                }
                DataSourceProperties dataSourceProperties = xmlToObj(driverPath, attrValueMap);
                dataSourceProperties.setDbType(dbType);
                //计算如果不存在
                if (result.containsKey(dbType)) {
                    Map<String, DataSourceProperties> dataSourceMap = result.get(dbType);
                    dataSourceMap.put(connName, dataSourceProperties);
                } else {
                    Map<String, DataSourceProperties> dataSourceMap = new HashMap<>();
                    dataSourceMap.put(connName, dataSourceProperties);
                    result.put(dbType, dataSourceMap);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error("initContext error :: " + e.getMessage());
        }
        return result;
    }


    private static DataSourceProperties xmlToObj(String driverPath, Map<String, String> attrValueMap) {
        DataSourceProperties properties = new DataSourceProperties();
        String dbName = attrValueMap.get("dbName");
        String url = dbName + attrValueMap.get("host") + ":" + attrValueMap.get("port") +
                "/" + attrValueMap.get("dbName");
        properties.setUrl(url);
        properties.setUsername(attrValueMap.get("username"));
        properties.setPassword(attrValueMap.get("password"));
        properties.setDriverPath(driverPath);
        properties.setDbName(dbName);
        properties.setDriverClassName(attrValueMap.get("dbDriver"));
        properties.setSchemaName(dbName);
        return properties;
    }

    /**
     * 测试数据库连接
     *
     * @param connectionVO ConnectionVO
     * @return boolean
     * @throws Exception 异常
     */
    public static boolean testConnection(ConnectionVO connectionVO) throws Exception {
        return false;
    }

    /**
     * 写入xml文件
     *
     * @param connection connection
     */
    public static void writeDBConnectionXML(ConnectionVO connection) throws Exception {

        //判断name是否已存在
        Map<String, DataSourceProperties> dataSourceMap = InitContext.DataSourceMap.get(connection.getDbtype());

        if (dataSourceMap.containsKey(connection.getName())) {
            throw new RuntimeException("该名称已存在，请更换！");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(FilePathContent.TASK_DB_CONNECTION);
        // 获取根元素
        Element rootElement = doc.getDocumentElement();
        String driverPath = "src/main/resources/drivers/" + connection.getDriverPath();
        // 创建 <db> 元素并设置属性
        Element dbElement = doc.createElement("db");
        dbElement.setAttribute("id", connection.getDbtype());
        dbElement.setAttribute("name", connection.getName());
        dbElement.setAttribute("driverPath", driverPath);
        //设置子标签
        Element dbDriverElement = doc.createElement("dbDriver");
        dbDriverElement.setTextContent("com.mysql.cj.jdbc.Driver");
        Element dbUrlElement = doc.createElement("dbUrl");
        dbUrlElement.setTextContent("jdbc:mysql://");
        Element hostElement = doc.createElement("host");
        hostElement.setTextContent(connection.getHost());
        Element portElement = doc.createElement("port");
        portElement.setTextContent(connection.getPort());
        Element usernameElement = doc.createElement("username");
        usernameElement.setTextContent(connection.getUsername());
        Element passwordElement = doc.createElement("password");
        passwordElement.setTextContent(connection.getPassword());
        Element dbNameElement = doc.createElement("dbName");
        dbNameElement.setTextContent(connection.getDbname());
        Element paramStrElement = doc.createElement("paramStr");
        paramStrElement.setTextContent(connection.getConnParam());
        Element commentElement = doc.createElement("comment");
        commentElement.setTextContent(connection.getComment());
        // 将元素添加到 <db> 元素中
        dbElement.appendChild(dbDriverElement);
        dbElement.appendChild(dbUrlElement);
        dbElement.appendChild(hostElement);
        dbElement.appendChild(portElement);
        dbElement.appendChild(usernameElement);
        dbElement.appendChild(passwordElement);
        dbElement.appendChild(dbNameElement);
        dbElement.appendChild(paramStrElement);
        dbElement.appendChild(commentElement);
        // 将 <db> 元素添加到 <dbs> 根元素中
        rootElement.appendChild(dbElement);
        // 将文档写回XML文件
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(FilePathContent.TASK_DB_CONNECTION);
        transformer.transform(source, result);

    }

    public static List<DataSourceVO> getDataSourceList() {
        List<DataSourceVO> dataSourceList = new ArrayList<>();

        for (Map.Entry<String, Map<String, DataSourceProperties>> listEntry : InitContext.DataSourceMap.entrySet()) {
            Map<String, DataSourceProperties> dataSourceMap = listEntry.getValue();
            for (Map.Entry<String, DataSourceProperties> dataSourceEntry : dataSourceMap.entrySet()) {
                DataSourceProperties dataSourceProperties = dataSourceEntry.getValue();
                //解析出来的databaseConnInfo是没有connName属性的
                String connName = dataSourceEntry.getKey();
                //前端需要dbName/Schema
                String dataName = dataSourceProperties.getDbName() + "/" + dataSourceProperties.getSchemaName();
                DataSourceVO dataSourceVO = new DataSourceVO();
                dataSourceVO.setConnName(connName);
                dataSourceVO.setUrl(dataSourceProperties.getUrl());
                dataSourceVO.setDbName(dataName);
                dataSourceVO.setDriverName(dataSourceProperties.getDriverClassName());
                dataSourceVO.setUsername(dataSourceProperties.getUsername());
                dataSourceVO.setPassword(dataSourceProperties.getPassword());
                dataSourceList.add(dataSourceVO);
            }
        }
        return dataSourceList;
    }
}
