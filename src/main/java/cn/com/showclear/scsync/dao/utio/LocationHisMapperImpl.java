package cn.com.showclear.scsync.dao.utio;

import cn.com.showclear.common.AppConstants;
import cn.com.showclear.scsync.mapper.utio.LocationHisMapper;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author YF-XIACHAOYANG
 * @date 2017/9/5 10:20
 */
public class LocationHisMapperImpl implements LocationHisMapper {

    private final String TABLE_NAME = "T_LOCATION_HIS";
    /**
     * 历史记录保存
     *
     * @param record
     */
    @Override
    public boolean save(Record record) {
        return Db.use(AppConstants.DB_KEY_UTIO).save(TABLE_NAME, "loc_id", record);
    }

    /**
     * 获取最后一条记录
     *
     * @return
     */
    @Override
    public Record selectLastLocationHisOrderByTime() {
        String sql = Db.use(AppConstants.DB_KEY_UTIO).getSql("selectLastLocationHisOrderByTime");
        return Db.use(AppConstants.DB_KEY_UTIO).findFirst(sql);
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
        return Db.use(AppConstants.DB_KEY_UTIO).update(tName,primKey,record);
    }

}
