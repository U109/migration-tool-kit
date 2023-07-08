package com.zzz.migrationtoolkit.handler.dataBaseHandler;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.common.vo.ConnectionVO;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.MySqlConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.OracleConnInfo;
import com.zzz.migrationtoolkit.server.InitContext;
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
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
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

    public static void initDBConnections() {
        InitContext.DBConnectionMap = readDataBaseConnection();
    }

    /**
     * 读取DB连接
     *
     * @return Map
     */
    private static Map<String, List<Map<String, DataBaseConnInfo>>> readDataBaseConnection() {
        Map<String, List<Map<String, DataBaseConnInfo>>> result = new HashMap<>();
        DocumentBuilder docParser;
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try {
            docParser = domFactory.newDocumentBuilder();
            Document doc = docParser.parse("src/main/resources/conf/DBConnections.xml");
            // 获取根节点
            Element root = (Element) doc.getElementsByTagName("dbs").item(0);

            NodeList dbList = root.getElementsByTagName("db");

            for (int i = 0; i < dbList.getLength(); i++) {
                Element dbElement = (Element) dbList.item(i);
                // 数据库连接标识
                String dbType = dbElement.getAttribute("id");
                String connName = dbElement.getAttribute("name");
                String clazzName = dbElement.getAttribute("class");
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
                DataBaseConnInfo dbci = (DataBaseConnInfo) xmlToObj(clazzName, attrValueMap);
                dbci.setDbType(dbType);

                Map<String, DataBaseConnInfo> dbMap = new HashMap<>();
                dbMap.put(connName, dbci);
                //计算如果不存在
                List<Map<String, DataBaseConnInfo>> mapList = result.computeIfAbsent(dbType, k -> new ArrayList<>());
                mapList.add(dbMap);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error("initContext error :: " + e.getMessage());
        }
        return result;
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
            log.error("initContext error :: " + e.getMessage());
        }
        return obj;
    }

    /**
     * 测试数据库连接
     *
     * @param connectionVO ConnectionVO
     * @return boolean
     * @throws Exception 异常
     */
    public static boolean testConnection(ConnectionVO connectionVO) throws Exception {
        String dbType = connectionVO.getDbtype();
        Connection connection = null;
        if (DataBaseConstant.MYSQL.equals(dbType)) {
            MySqlConnInfo connInfo = new MySqlConnInfo(connectionVO);
            Class.forName(connInfo.getDbDriver());
            connection = DriverManager.getConnection(connInfo.getUrl(), connInfo.getUsername(), connInfo.getPassword());
        } else if (DataBaseConstant.ORACLE.equals(dbType)) {
            OracleConnInfo connInfo = new OracleConnInfo(connectionVO);
            Class.forName(connInfo.getDbDriver());
            connection = DriverManager.getConnection(connInfo.getUrl(), connInfo.getUsername(), connInfo.getPassword());
        }
        return connection != null;
    }

    /**
     * 写入xml文件
     * @param connection connection
     * @return boolean
     */
    public static boolean writeDataBaseConnection(ConnectionVO connection) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("src/main/resources/conf/DBConnections.xml");
            // 获取根元素
            Element rootElement = doc.getDocumentElement();
            String className = "com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity." + connection.getDbtype() + "ConnInfo";
            // 创建 <db> 元素并设置属性
            Element dbElement = doc.createElement("db");
            dbElement.setAttribute("id", connection.getDbtype());
            dbElement.setAttribute("name", connection.getConnname());
            dbElement.setAttribute("class", className);
            //设置子标签
            Element dbDriverElement = doc.createElement("dbDriver");
            //反射获取属性值
            String driverName = connection.getDbtype().toUpperCase() + "_DB_DRIVER";
            Field dbDriverField = DataBaseConstant.class.getField(driverName);
            //此处是静态变量，所以不需要实例对象，因此传入的参数为 null
            String dbDriverValue = (String) dbDriverField.get(null);
            dbDriverElement.setTextContent(dbDriverValue);
            Element dbUrlElement = doc.createElement("dbUrl");
            String urlName = connection.getDbtype().toUpperCase() + "_DB_URL";
            Field dbUrlField = DataBaseConstant.class.getField(urlName);
            String dbUrlValue = (String) dbUrlField.get(null);
            dbUrlElement.setTextContent(dbUrlValue);
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
            StreamResult result = new StreamResult("src/main/resources/conf/DBConnections.xml");
            transformer.transform(source, result);
            log.info("database connection info save success!");
        } catch (Exception e) {
            log.error("database connection info save error : " + e);
            return false;
        }
        return true;
    }

}
