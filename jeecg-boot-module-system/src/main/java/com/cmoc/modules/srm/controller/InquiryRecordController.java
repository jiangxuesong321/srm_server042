package com.cmoc.modules.srm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.InquiryRecord;
import com.cmoc.modules.srm.service.IInquiryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
* @Description: 询价单主表
* @Author: jeecg-boot
* @Date:   2022-06-18
* @Version: V1.0
*/
@RestController
@RequestMapping("/srm/inquiryRecordList")
@Slf4j
public class InquiryRecordController {

    @Autowired
    private IInquiryRecordService inquiryRecordService;

   /**
    * 分页列表查询
    *
    * @param inquiryRecord
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "询价单主表-分页列表查询")
   @ApiOperation(value="询价单主表-分页列表查询", notes="询价单主表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<InquiryRecord>> queryPageList(InquiryRecord inquiryRecord,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<InquiryRecord> queryWrapper = QueryGenerator.initQueryWrapper(inquiryRecord, req.getParameterMap());
       Page<InquiryRecord> page = new Page<InquiryRecord>(pageNo, pageSize);
       IPage<InquiryRecord> pageList = inquiryRecordService.page(page, queryWrapper);
       return Result.OK(pageList);
   }


}
