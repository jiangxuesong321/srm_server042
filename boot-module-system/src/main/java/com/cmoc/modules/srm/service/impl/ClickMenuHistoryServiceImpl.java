package com.cmoc.modules.srm.service.impl;

import com.cmoc.modules.srm.entity.ClickMenuHistory;
import com.cmoc.modules.srm.mapper.ClickMenuHistoryMapper;
import com.cmoc.modules.srm.service.IClickMenuHistoryService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: click_menu_history
 * @Author: jeecg-boot
 * @Date:   2022-10-21
 * @Version: V1.0
 */
@Service
public class ClickMenuHistoryServiceImpl extends ServiceImpl<ClickMenuHistoryMapper, ClickMenuHistory> implements IClickMenuHistoryService {
    /**
     * 查询
     * @param clickMenuHistory
     * @return
     */
    @Override
    public List<ClickMenuHistory> queryList(ClickMenuHistory clickMenuHistory) {
        return baseMapper.queryList(clickMenuHistory);
    }

//    public static void main(String[] args) {
//        String url = "yxproject01/ZHLX-XM-XM-2022-001/contract/[合同-扫描]1台膜厚检测-661.05万元-上海精测.pdf";
//        String[] str = url.split("/");
//        System.out.println(str[str.length - 1]);
//    }
}
