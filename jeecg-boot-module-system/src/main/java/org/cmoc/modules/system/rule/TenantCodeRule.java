package org.cmoc.modules.system.rule;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.cmoc.common.handler.IFillRuleHandler;
import org.cmoc.common.util.SpringContextUtils;
import org.cmoc.modules.system.entity.BasSequence;
import org.cmoc.modules.system.service.IBasSequenceService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author scott
 * @Date 2019/12/9 11:32
 * @Description: 分类字典编码生成规则
 */
public class TenantCodeRule implements IFillRuleHandler {

    public static final String ROOT_PID_VALUE = "0";

    @Override
    public Object execute(JSONObject params, JSONObject formData) {

        String prefix = "", period = "",tenantid = "";
        int seq = 1;
        Integer seqLen = 0;
        if (params != null) {

            Object obj = formData.get("prefix");
            if (obj != null){
                prefix = obj.toString();
            }else {
                obj = params.get("prefix");
                if (obj != null) {
                    prefix = obj.toString();
                }
            }

            obj = params.get("period");
            if (obj != null){
                period = obj.toString();
            }

            obj = params.get("seqLen");
            if (obj != null){
                seqLen = Integer.parseInt(obj.toString());
            }
            /* 租户ID */

            obj = params.get("tenantid");
            if (obj != null) {
                tenantid = obj.toString();
            }

            obj = params.getInteger("seqNo");
            if (obj != null) {
                seq = (Integer) obj;
            }

        }
        String k = prefix;
        if(StringUtils.isNotEmpty(period)){
            SimpleDateFormat format = new SimpleDateFormat(period);
            k = k + format.format(new Date());
        }

        if (k == null || k.length() == 0) {
            return null;
        }

        IBasSequenceService basSequenceService = (IBasSequenceService) SpringContextUtils.getBean("basSequenceServiceImpl");
        BasSequence basSequence = basSequenceService.getById(k);
        if (basSequence == null) {
            basSequence = new BasSequence();
            basSequence.setK(k);
            basSequence.setV(seq);
            basSequenceService.save(basSequence);
        } else {
            seq = basSequence.getV() + 1;
            basSequence.setV(seq);
            basSequenceService.updateById(basSequence);
        }

        return k + String.format("%0" + seqLen + "d", seq);
    }
}
