package cn.com.showclear.scsync.mapper;

import com.jfinal.plugin.activerecord.Record;

/**
 * Mapper 接口定义
 * @author YF-XIACHAOYANG
 * @date 2017/9/5 9:36
 */
public interface Mapper {

    /**
     * 指定表进行 update 操作
     * @param tName
     * @param primKey
     * @param record
     * @return
     */
    boolean update(String tName,String primKey,Record record);
}
