package org.jeecg.modules.srm.utils;


import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * xml解析工具类
 * @author KevinWmz
 */
public class XmlUtils {
    /**
     * xml字符串转对象
     * @param clazz
     * @param xmlStr
     * @return
     */
    public static Object xmlStrToObject(Class clazz, String xmlStr) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlObject;
    }

    /**
     * 对象转xml字符串
     * @param obj
     * @param load
     * @return
     * @throws JAXBException
     */
    public static String objectToXmlStr(Object obj,Class<?> load){
        String result = "";
        try{
            JAXBContext context = JAXBContext.newInstance(load);
            Marshaller marshaller = context.createMarshaller();
            // xml格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // 去掉生成xml的默认报文头
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");

            //自定义前缀 自定义为空的情况下默认还是会返回原来的默认
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
                @Override
                public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
                    if (namespaceUri.equals("http://tempuri.org/")) return "";
                    if (namespaceUri.contains("http://www.cnblogs.com")) return "blog";
                    return suggestion;
                }
            });


            StringWriter writer = new StringWriter();
            marshaller.marshal(obj,writer);
            result = writer.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        //替换掉ns2标签内容
        result =result.replace(":ns2","").replace("ns2:","");

        return result;
    }
}

