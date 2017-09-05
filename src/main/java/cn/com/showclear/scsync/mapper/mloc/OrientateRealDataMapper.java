package cn.com.showclear.scsync.mapper.mloc;

import cn.com.showclear.scsync.mapper.Mapper;
import com.jfinal.plugin.activerecord.Record;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author YF-XIACHAOYANG
 * @date 2017/9/5 9:37
 */
public interface OrientateRealDataMapper  extends Mapper{

    /**
     * 加载最新 终端实时数据
     * @param lastSyncTime
     * @return
     */
    List<Record> loadNewOriRealData(Timestamp lastSyncTime);
}
