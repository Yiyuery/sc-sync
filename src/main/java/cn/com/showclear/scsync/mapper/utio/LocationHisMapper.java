package cn.com.showclear.scsync.mapper.utio;

import cn.com.showclear.scsync.mapper.Mapper;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author YF-XIACHAOYANG
 * @date 2017/9/5 10:17
 */
public interface LocationHisMapper extends Mapper {

    /**
     * 历史记录保存
     * @param hisRecord
     */
    boolean save(Record hisRecord);

    /**
     * 获取最后一条记录
     * @return
     */
    Record selectLastLocationHisOrderByTime();
}
