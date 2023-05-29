package org.cmoc.modules.srm.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import javax.jws.WebService;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.Map;



@Slf4j
public class PostWsdl {

    public static String toContractInterface(String endpoint, Map<String,Object> param) throws RemoteException, ServiceException {
        //直接引用远程的wsdl文件
        //以下都是套路
        Service service = new Service();
        Call call = (Call) service.createCall();
        call.setTargetEndpointAddress(endpoint);
        call.setOperationName("getUserName");//WSDL里面描述的接口名称

        call.addParameter("getUserName", XMLType.XSD_STRING,
                javax.xml.rpc.ParameterMode.IN);//接口的参数
//        call.addParameter("creatId", XMLType.XSD_STRING,
//                javax.xml.rpc.ParameterMode.IN);//接口的参数
//        call.addParameter("workflowid", XMLType.XSD_STRING,
//                javax.xml.rpc.ParameterMode.IN);//接口的参数
//        call.addParameter("ht", XMLType.XSD_STRING,
//                javax.xml.rpc.ParameterMode.IN);//接口的参数
//        call.addParameter("qtzc", XMLType.XSD_STRING,
//                javax.xml.rpc.ParameterMode.IN);//接口的参数
//        call.addParameter("Childs1", XMLType.XSD_ANYTYPE,
//                javax.xml.rpc.ParameterMode.IN);//接口的参数

        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);//设置返回类型

        log.info("===========调用OA接口传入参数" + param);
        String result = (String) call.invoke("getUserName",new Object[]{"2308"});//给方法传递参数，并且调用方法
        log.info("===========调用OA接口返回参数" + result);

        return result;
    }

}
