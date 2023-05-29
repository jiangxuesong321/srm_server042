package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.api.vo.Result;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.system.vo.DictModelMany;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.common.util.ObjectCheck;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.modules.srm.entity.*;
import org.cmoc.modules.srm.mapper.BasMaterialMapper;
import org.cmoc.modules.srm.service.*;
import org.cmoc.modules.system.service.ISysDictService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 设备管理
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class BasMaterialServiceImpl extends ServiceImpl<BasMaterialMapper, BasMaterial> implements IBasMaterialService {
    @Autowired
    private IBasMaterialCategoryService iBasMaterialCategoryService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IBasMaterialChildService iBasMaterialChildService;
    @Autowired
    private IBasMaterialFieldService iBasMaterialFieldService;
    @Value("${jeecg.path.upload}")
    private String upLoadPath;
    @Autowired
    private IContractObjectService iContractObjectService;
    /**
     * 添加设备
     * @param basMaterial
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(BasMaterial basMaterial) throws Exception {
        Date nowTime = new Date();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String[] categoryList = basMaterial.getCategoryId().split(",");
        String categoryId = categoryList[categoryList.length - 1];
        //末级分类
        BasMaterialCategory category = iBasMaterialCategoryService.getById(categoryId);
        if(category == null){
            throw new Exception("分类不存在,请检查");
        }
        basMaterial.setLastCategoryId(category.getId());
        List<BasMaterialCategory> cateList = iBasMaterialCategoryService.list(Wrappers.<BasMaterialCategory>query().lambda().
                in(BasMaterialCategory :: getId,categoryList).
                orderByAsc(BasMaterialCategory :: getVersion));

        if(cateList != null && cateList.size() > 0){
            List<String> categoryNames = new ArrayList<>();
            for(BasMaterialCategory cate : cateList){
                categoryNames.add(cate.getName());
            }
            basMaterial.setCategoryName(String.join("-",categoryNames));
        }

        //生成编码,获取当前改分类的设备最大code
        List<BasMaterial> existList = baseMapper.getMaxCodeByCode(category.getCode());
        //不存在
        String code = null;
        if(existList == null || existList.size() == 0){
            code = category.getCode() + "0001";
        }else{
            String pCode = existList.get(0).getCode();
            Integer suffix = Integer.parseInt(pCode.substring(pCode.length() - 4)) + 1;
            code = category.getCode() + String.format("%4d", suffix).replace(" ", "0");
        }
        basMaterial.setCode(code);
        basMaterial.setCreateBy(loginUser.getUsername());
        basMaterial.setIsEnabled(1);
        this.save(basMaterial);

        List<BasMaterialChild> childList = basMaterial.getChildList();
        if(childList != null && childList.size() > 0){
            for(BasMaterialChild child : childList){
                child.setId(String.valueOf(IdWorker.getId()));
                child.setProdId(basMaterial.getId());
                child.setCreateBy(loginUser.getUsername());
                child.setUpdateBy(loginUser.getUsername());
                child.setCreateTime(nowTime);
                child.setUpdateTime(nowTime);
            }
            iBasMaterialChildService.saveBatch(childList);
        }
        List<BasMaterialField> basFieldList = basMaterial.getBasFieldList();
        if(basFieldList != null && basFieldList.size() > 0){
            int i = 0;
            for(BasMaterialField f : basFieldList){
                f.setId(String.valueOf(IdWorker.getId()));
                f.setProdId(basMaterial.getId());
                f.setCreateBy(loginUser.getUsername());
                f.setUpdateBy(loginUser.getUsername());
                f.setCreateTime(nowTime);
                f.setUpdateTime(nowTime);
                f.setSort(i);
                i++;
            }
            iBasMaterialFieldService.saveBatch(basFieldList);
        }
        List<BasMaterialField> otherFieldList = basMaterial.getOtherFieldList();
        if(otherFieldList != null && otherFieldList.size() > 0){
            int i = 0;
            for(BasMaterialField f : otherFieldList){
                f.setId(String.valueOf(IdWorker.getId()));
                f.setProdId(basMaterial.getId());
                f.setCreateBy(loginUser.getUsername());
                f.setUpdateBy(loginUser.getUsername());
                f.setCreateTime(nowTime);
                f.setUpdateTime(nowTime);
                f.setSort(i);
                i++;
            }
            iBasMaterialFieldService.saveBatch(otherFieldList);
        }
    }

    /**
     * 导入
     * @param request
     * @param response
     * @param clazz
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<BasMaterial> clazz) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(0);
            params.setHeadRows(1);
            params.setNeedSave(false);
            try {
                int i = 1;
                List<BasMaterial> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                if(list != null && list.size() > 0){
                    LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    List<BasMaterialCategory> categoryList = iBasMaterialCategoryService.getCategoryList();
                    List<BasMaterial> bmList = baseMapper.getMaxCode();
                    Map<String,String> maxCode = new HashMap<>();
                    if(bmList != null && bmList.size() > 0){
                        maxCode = bmList.stream().collect((Collectors.toMap(BasMaterial::getLastCategoryId, BasMaterial::getCode)));
                    }
                    for(BasMaterial bm : list){
                        Boolean flag = false;
                        if(ObjectCheck.checkObjAllFieldsIsNull(bm)){
                            continue;
                        }
                        String name = bm.getName();
                        if(StringUtils.isEmpty(name)){
                            return Result.error("文件导入失败:第" + i + "行,设备名称为空" );
                        }
                        String model = bm.getModel();
                        if(StringUtils.isEmpty(model)){
                            return Result.error("文件导入失败:第" + i + "行,设备类型为空" );
                        }
                        String categoryName = bm.getCategoryName();
                        if(StringUtils.isEmpty(categoryName)){
                            return Result.error("文件导入失败:第" + i + "行,设备分类为空" );
                        }

                        BasMaterialCategory exist = null;
                        for(BasMaterialCategory bc : categoryList){
                            if(bm.getCategoryName().equals(bc.getName())){
                                flag = true;
                                exist = bc;
                                break;
                            }
                        }
                        if(!flag){
                            return Result.error("文件导入失败:第" + i + "行,设备分类不存在" );
                        }
                        bm.setCategoryName(exist.getName());
                        bm.setCategoryId(exist.getId());
                        //抽取最后一层分类ID
                        String[] categoryIds = exist.getId().split(",");
                        String categoryId = categoryIds[categoryIds.length - 1];
                        bm.setLastCategoryId(categoryId);
                        //抽取最后一层分类编码
                        String[] categoryCodes = exist.getCode().split(",");
                        String categoryCode = categoryCodes[categoryCodes.length - 1];
                        //获取该分类最大的设备Code
                        String code = maxCode.get(categoryId);
                        if(StringUtils.isEmpty(code)){
                            code = categoryCode + "0001";
                        }else{
                            String pCode = code;
                            Integer suffix = Integer.parseInt(pCode.substring(pCode.length() - 4)) + 1;
                            code = categoryCode + String.format("%4d", suffix).replace(" ", "0");
                        }
                        bm.setCode(code);
                        maxCode.put(categoryId,code);
                        bm.setCreateBy(loginUser.getUsername());
                        bm.setIsEnabled(1);

                        i++;
                    }
                    this.saveBatch(list);
                }else{
                    return Result.error("文件导入失败:文件为空" );
                }
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 历史报价计量
     * @param page
     * @param basMaterial
     * @return
     */
    @Override
    public IPage<BiddingRecord> fetchHistoryQuote(Page<BasMaterial> page, BasMaterial basMaterial) {
        IPage<BiddingRecord> iPage = baseMapper.fetchHistoryQuote(page,basMaterial);
        List<BiddingRecord> pageList = iPage.getRecords();
        List<String> codeList = new ArrayList<>();
        codeList.add("supp_type");
        List<DictModelMany> dictList = iSysDictService.getDictItemsByCodeList(codeList);
        Map<String,String> map = dictList.stream().collect(Collectors.toMap(DictModelMany::getValue, DictModelMany::getText));
        if(pageList != null && pageList.size() > 0){
            for(BiddingRecord bs : pageList){
                if(map != null && !map.isEmpty()){
                    List<String> suppTypes = new ArrayList<>();
                    String[] types = bs.getSupplierType().split(",");
                    for(String str : types){
                        String value = map.get(str);
                        if(StringUtils.isNotEmpty(value)){
                            suppTypes.add(value);
                        }
                    }
                    if(suppTypes != null && suppTypes.size() > 0){
                        bs.setSupplierType(String.join(",",suppTypes));
                    }
                }
            }
        }
        return iPage;
    }

    /**
     * 更新
     * @param basMaterial
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntity(BasMaterial basMaterial) {
        Date nowTime = new Date();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String[] categoryList = basMaterial.getCategoryId().split(",");
        String categoryId = categoryList[categoryList.length - 1];
        //末级分类
        BasMaterialCategory category = iBasMaterialCategoryService.getById(categoryId);
        basMaterial.setLastCategoryId(category.getId());

        List<BasMaterialCategory> cateList = iBasMaterialCategoryService.list(Wrappers.<BasMaterialCategory>query().lambda().
                in(BasMaterialCategory :: getId,categoryList).
                orderByAsc(BasMaterialCategory :: getVersion));

        if(cateList != null && cateList.size() > 0){
            List<String> categoryNames = new ArrayList<>();
            for(BasMaterialCategory cate : cateList){
                categoryNames.add(cate.getName());
            }
            basMaterial.setCategoryName(String.join("-",categoryNames));
        }

        basMaterial.setUpdateBy(loginUser.getUsername());
        basMaterial.setUpdateTime(nowTime);
        this.updateById(basMaterial);

        //删除子项
        iBasMaterialChildService.remove(Wrappers.<BasMaterialChild>query().lambda().eq(BasMaterialChild :: getProdId,basMaterial.getId()));
        iBasMaterialFieldService.remove(Wrappers.<BasMaterialField>query().lambda().eq(BasMaterialField :: getProdId,basMaterial.getId()));

        List<BasMaterialChild> childList = basMaterial.getChildList();
        if(childList != null && childList.size() > 0){
            for(BasMaterialChild child : childList){
                child.setId(String.valueOf(IdWorker.getId()));
                child.setProdId(basMaterial.getId());
                child.setCreateBy(loginUser.getUsername());
                child.setUpdateBy(loginUser.getUsername());
                child.setCreateTime(nowTime);
                child.setUpdateTime(nowTime);
            }
            iBasMaterialChildService.saveBatch(childList);
        }

        List<BasMaterialField> basFieldList = basMaterial.getBasFieldList();
        if(basFieldList != null && basFieldList.size() > 0){
            int i = 0;
            for(BasMaterialField f : basFieldList){
                f.setId(String.valueOf(IdWorker.getId()));
                f.setProdId(basMaterial.getId());
                f.setCreateBy(loginUser.getUsername());
                f.setUpdateBy(loginUser.getUsername());
                f.setCreateTime(nowTime);
                f.setUpdateTime(nowTime);
                f.setSort(i);
                i++;
            }
            iBasMaterialFieldService.saveBatch(basFieldList);
        }
        List<BasMaterialField> otherFieldList = basMaterial.getOtherFieldList();
        if(otherFieldList != null && otherFieldList.size() > 0){
            int i = 0;
            for(BasMaterialField f : otherFieldList){
                f.setId(String.valueOf(IdWorker.getId()));
                f.setProdId(basMaterial.getId());
                f.setCreateBy(loginUser.getUsername());
                f.setUpdateBy(loginUser.getUsername());
                f.setCreateTime(nowTime);
                f.setUpdateTime(nowTime);
                f.setSort(i);
                i++;
            }
            iBasMaterialFieldService.saveBatch(otherFieldList);
        }
    }

    /**
     * 导出
     * @param request
     * @param basMaterial
     * @param clazz
     * @param title
     * @return
     */
    @Override
    public ModelAndView exportXls(HttpServletRequest request, BasMaterial basMaterial, Class<BasMaterial> clazz, String title) {
        // Step.1 组装查询条件
        QueryWrapper<BasMaterial> queryWrapper = QueryGenerator.initQueryWrapper(basMaterial, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<BasMaterial> pageList = this.list(queryWrapper);
        List<BasMaterial> exportList = pageList;

        // 过滤选中数据

        if(pageList != null && pageList.size() > 0){
            List<String> prodCodes = new ArrayList<>();
            for(BasMaterial bm : pageList){
                bm.setCategoryIds(bm.getCategoryId().split(","));
                prodCodes.add(bm.getCode());
            }
            List<ContractObject> objList = iContractObjectService.fetchTotalByEqp(prodCodes);
            for(BasMaterial bm : pageList){
                for(ContractObject co : objList){
                    if(bm.getCode().equals(co.getProdCode())){
                        bm.setQty(co.getQty());
                        break;
                    }
                }
            }
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams=new ExportParams(title, "导出人:" + sysUser.getRealname(), title);
        exportParams.setImageBasePath(upLoadPath);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS,exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }
    /**
     * 获取对象ID
     *
     * @return
     */
    private String getId(T item) {
        try {
            return PropertyUtils.getProperty(item, "id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
