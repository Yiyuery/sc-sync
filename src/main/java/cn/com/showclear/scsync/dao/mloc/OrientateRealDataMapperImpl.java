package cn.com.showclear.scsync.dao.mloc;

import cn.com.showclear.common.AppConstants;
import cn.com.showclear.scsync.mapper.mloc.OrientateRealDataMapper;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import java.sql.Timestamp;
import java.util.List;

/**
 * 终端实时数据 Mapper
 * @author YF-XIACHAOYANG
 * @date 2017/9/5 9:38
 */
public class OrientateRealDataMapperImpl implements OrientateRealDataMapper {

    /**
     * 加载最新 终端实时数据
     *
     * @param lastSyncTime
     * @return
     */
    @Override
    public List<Record> loadNewOriRealData(Timestamp lastSyncTime) {
        String sql = Db.use(AppConstants.DB_KEY_MLOC).getSql("findOriRealData");
        return Db.use(AppConstants.DB_KEY_MLOC).find(sql,lastSyncTime);
    }

    /**
     * 指定表进行 update 操作
     *
     * @param tName
     * @param primKey
     * @param record
     * @return
     */
    @Override
    public boolean update(String tName, String primKey, Record record) {
        return Db.use(AppConstants.DB_KEY_MLOC).update(tName,primKey,record);
    }
}
