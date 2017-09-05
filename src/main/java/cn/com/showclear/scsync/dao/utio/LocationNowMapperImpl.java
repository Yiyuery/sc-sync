package cn.com.showclear.scsync.dao.utio;


import cn.com.showclear.common.AppConstants;
import cn.com.showclear.scsync.mapper.utio.LocationNowMapper;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author YF-XIACHAOYANG
 * @date 2017/9/5 10:20
 */
public class LocationNowMapperImpl implements LocationNowMapper {

    private final String TABLE_NAME = "T_LOCATION_NOW";

    /**
     * 判断当前表中是否含有 该终端地址的位置记录
     *
     * @param terminalMac
     * @return
     */
    @Override
    public Record selectLocationNowByTerminalMac(String terminalMac) {
        String sql = Db.use(AppConstants.DB_KEY_UTIO).getSql("selectLocationNowByTerminalMac");
        return Db.use(AppConstants.DB_KEY_UTIO).findFirst(sql,terminalMac);
    }

    /**
     * 保存当前记录
     *
     * @param record
     * @return
     */
    @Override
    public boolean save(Record record) {
        record.set("loc_id",null);
        return Db.use(AppConstants.DB_KEY_UTIO).save(TABLE_NAME, "loc_id", record);
    }

    /**
     * 更新记录
     *
     * @param loc
     * @return
     */
    @Override
    public boolean update(Record loc) {
        return Db.use(AppConstants.DB_KEY_UTIO).update(TABLE_NAME, "loc_id", loc);
    }

    /**
     * 根据实时同步的定位数据的 AP Mac地址获取AP 区域信息
     *
     * @param apMac
     * @return
     */
    @Override
    public Record selectDevInfoByApMac(String apMac) {
        String sql = Db.use(AppConstants.DB_KEY_UTIO).getSql("selectDevInfoByApMac");
        return Db.use(AppConstants.DB_KEY_UTIO).findFirst(sql,apMac);
    }

    /**
     * 根据使用终端的Mac的地址 查询人员定位关联记录
     *
     * @param terminalMac 使用终端的Mac的地址
     * @return
     */
    @Override
    public Record selectLocationMemByTerminalMac(String terminalMac) {
        String sql = Db.use(AppConstants.DB_KEY_UTIO).getSql("selectLocationMemByTerminalMac");
        return Db.use(AppConstants.DB_KEY_UTIO).findFirst(sql,terminalMac);
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
