package com.cmoc.modules.quartz.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cmoc.modules.system.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.cmoc.common.system.vo.DictModel;
import com.cmoc.common.util.DateUtils;
import com.cmoc.modules.srm.entity.BasRateDetail;
import com.cmoc.modules.srm.entity.BasRateMain;
import com.cmoc.modules.srm.service.IBasRateDetailService;
import com.cmoc.modules.srm.service.IBasRateMainService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 汇率获取
 *
 * 此处的同步是指 当定时任务的执行时间大于任务的时间间隔时
 * 会等待第一个任务执行完成才会走第二个任务
 *
 *
 * @author: taoyan
 * @date: 2020年06月19日
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
public class ExchangeRateJob implements Job {
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IBasRateMainService iBasRateMainService;
    @Autowired
    private IBasRateDetailService iBasRateDetailService;
    /**
     * 若参数变量名修改 QuartzJobController中也需对应修改
     */
    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(" --- 汇率获取 --- ");
        //当前日期字符串，格式：yyyy-MM-dd
        String today = DateUtil.today();
        if(StringUtils.isNotEmpty(parameter)){
            today = parameter;
        }
        //当月
        String toMonth = today.substring(0,7);
        Date nowTime = new Date();

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("date", today);

        String responseResult = HttpUtil.get("https://www.safe.gov.cn/AppStructured/hlw/jsonRmb.do", paramMap);

        if (responseResult != null && !responseResult.equals("")) {
            JSONArray array = JSONUtil.parseArray(responseResult);

            if (array != null && array.size() > 0) {
                //币种字典
                Map<String,String> map = new HashMap<>();
                try {
                    List<DictModel> ls = iSysDictService.getDictItems("currency_type");
                    map = ls.stream().collect((Collectors.toMap(DictModel::getText, DictModel::getValue)));
                } catch (Exception ex) {
                    log.error("查询数据字典:币种出错" + ex.getMessage());
                }

                //当天汇率集合(只取 美元、欧元、日元)
                List<BasRateDetail> addList = new ArrayList<>();
                List<BasRateMain> updList = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {

                    JSONArray obj = array.getJSONArray(i);
                    BigDecimal valueB = obj.getBigDecimal(0);
                    String currencyB = obj.getStr(1);
                    if("美元".equals(currencyB) || "欧元".equals(currencyB) || "日元".equals(currencyB)){
                        BigDecimal valueA = obj.getBigDecimal(2);
                        String currencyA = obj.getStr(3);

                        String keyB = map.get(currencyB.trim());
                        String keyA = map.get(currencyA.trim());

                        List<BasRateMain> mainList = iBasRateMainService.list(Wrappers.<BasRateMain>query().lambda().
                                eq(BasRateMain :: getMonth,toMonth).
                                eq(BasRateMain :: getCurrencyA,keyA).
                                eq(BasRateMain :: getCurrencyB,keyB));

                        String id = String.valueOf(IdWorker.getId());
                        if(mainList != null && mainList.size() > 0){
                            id = mainList.get(0).getId();
                        }else{
                            BasRateMain main = new BasRateMain();
                            main.setId(id);
                            main.setCreateTime(nowTime);
                            main.setUpdateTime(nowTime);
                            main.setMonth(toMonth);
                            main.setCurrencyA(keyA);
                            main.setCurrencyB(keyB);
                            main.setValueA(new BigDecimal("1"));
                            main.setValueB(BigDecimal.ZERO);
                            updList.add(main);
                        }

                        List<BasRateDetail> rateList = iBasRateDetailService.list(Wrappers.<BasRateDetail>query().lambda().
                                eq(BasRateDetail :: getDay,today).
                                eq(BasRateDetail :: getCurrencyA,keyA).
                                eq(BasRateDetail :: getCurrencyB,keyB));
                        if(rateList == null || rateList.size() == 0){
                            BasRateDetail detail = new BasRateDetail();
                            detail.setCreateTime(nowTime);
                            detail.setUpdateTime(nowTime);
                            detail.setId(String.valueOf(IdWorker.getId()));
                            detail.setDay(today);
                            detail.setCurrencyA(keyA);
                            detail.setCurrencyB(keyB);
                            detail.setValueA(new BigDecimal("1"));
                            BigDecimal valB = valueB.divide(valueA,6,BigDecimal.ROUND_HALF_UP);
                            detail.setValueB(valB);
                            detail.setMid(id);
                            addList.add(detail);
                        }
                    }
                }
                if(updList != null && updList.size() > 0){
                    iBasRateMainService.saveBatch(updList);
                }
                if(addList != null && addList.size() > 0){
                    iBasRateDetailService.saveBatch(addList);
                }

                //计算平均汇率
                List<BasRateDetail> allList = iBasRateDetailService.list(Wrappers.<BasRateDetail>query().lambda().eq(BasRateDetail :: getDay,today));
                Map<String,List<BasRateDetail>> maps = allList.stream().collect(Collectors.groupingBy(BasRateDetail :: getCurrencyB));
                for(Map.Entry<String,List<BasRateDetail>> entry : maps.entrySet()){
                    String currencyB = entry.getKey();
                    List<BasRateDetail> values = entry.getValue();
                    BigDecimal value = BigDecimal.ZERO;
                    for(BasRateDetail brd : values){
                        value = value.add(brd.getValueB());
                    }
                    value = value.divide(new BigDecimal(values.size()),6,BigDecimal.ROUND_HALF_UP);

                    BasRateMain main = iBasRateMainService.getOne(Wrappers.<BasRateMain>query().lambda().
                            eq(BasRateMain :: getMonth,toMonth).
                            eq(BasRateMain :: getCurrencyB,currencyB));
                    main.setValueB(value);
                    iBasRateMainService.updateById(main);
                }
            }
        }

        //测试发现 每5秒执行一次
        log.info(" --- 执行完毕，时间："+DateUtils.now()+"---");
    }

}
