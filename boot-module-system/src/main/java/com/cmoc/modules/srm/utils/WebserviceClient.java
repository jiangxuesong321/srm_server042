package com.cmoc.modules.srm.utils;

/**
 * @author ：Kevin.Wang
 * @date ：Created in 2022年09月09日 19:45
 * @description：
 * @modified By：
 * @version:
 */

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.stereotype.Component;

@Component
public class WebserviceClient {

    /**
     * 动态调用webservice接口 返回xml格式的string字符串
     * @author Kevin.Wang
     * @date : 2022/9/13 15:27
     * @param wsdUrl
     * @param operationName
     * @param params
     * @return String
     **/
    public String callWebSV(String wsdUrl, String operationName, String... params) throws Exception {
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(wsdUrl);
        // 需要密码的情况需要加上用户名和密码
        /*client.getOutInterceptors().add(new ClientLoginInterceptor(USER_NAME, PASS_WORD));*/
        Object[] objects;
        // invoke("方法名",参数1,参数2,参数3....);
        objects = client.invoke(operationName, params);
        return objects[0].toString();
    }

    /**
     * 调用webservice 返回对象
     * @author Kevin.Wang
     * @date : 2022/9/13 15:29
     * @param wsdUrl  方法url
     * @param operationName  对应的方法名
     * @param tClass 返回的泛型对象
     * @param params 多个参数
     * @return T 根据传入的泛型对象返回接口返回的对象数据
     **/
    public <T> T callWebSvForObject(String wsdUrl, String operationName, Class<T> tClass, Object... params) throws Exception {
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(wsdUrl);
        Object[] objects;
        // invoke("方法名",参数1,参数2,参数3....);
        objects = client.invoke(operationName, params);
        if (objects!=null&&objects.length>0){
            T t;

            /**
             * objects 对象组中返回的是Object对象
             * 转换成传入的泛型 实体类对象
             */
           /* Object obj = objects[0];
            t = new ObjectMapper().convertValue(obj, tClass);*/

            t = DataConvertUtil.objectArrayToBean(objects, tClass);
            return t;
        }
        return null;
    }

}
