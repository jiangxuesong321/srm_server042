package org.cmoc.modules.srm.service;

import org.cmoc.modules.srm.entity.SupBargain;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: sup_bargain
 * @Author: jeecg-boot
 * @Date:   2022-09-28
 * @Version: V1.0
 */
public interface ISupBargainService extends IService<SupBargain> {
    /**
     * 议价
     * @param supBargain
     */
    void saveBargain(SupBargain supBargain);

    /**
     * 批量议价
     * @param supBargain
     */
    void saveBargainAll(List<SupBargain> supBargain);
}
