package cn.com.showclear.scsync.impl.mloc;

import cn.com.showclear.common.AppConstants;
import cn.com.showclear.scsync.mapper.Mapper;
import cn.com.showclear.scsync.mapper.MapperManager;
import cn.com.showclear.scsync.mapper.mloc.OrientateRealDataMapper;
import cn.com.showclear.scsync.mapper.utio.LocationHisMapper;
import cn.com.showclear.scsync.mapper.utio.LocationNowMapper;
import cn.com.showclear.utils.TimeUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;
import java.util.TimerTask;

/**
 * 接入数据同步定时器 - 人员定位
 *
 * @author YF-XIACHAOYANG
 * @date 2017/9/4 16:47
 */
public class MlocSyncTask extends TimerTask {

    private static final Logger log = Logger.getLogger(MlocSyncTask.class);

    /**
     * 现场实时定位数据
     */
    private static OrientateRealDataMapper orientateRealDataMapper;
    /**
     * 历史位置
     */
    private static LocationHisMapper locationHisMapper;

    /**
     * 当前位置
     */
    private static LocationNowMapper locationNowMapper;

    /**
     * 定时器开启标志
     */
    private boolean isFree = true;
    /**
     * 记录同步时间分界线
     */
    private Timestamp lastSyncTime;

    public MlocSyncTask() {
        initMapper();
        initLastSyncTime();
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        if (isFree) {
            isFree = false;
            try {
                syncRuntimeData();
            } catch (Exception e) {
                log.error("MlocSyncTask run error...", e);
            } finally {
                initLastSyncTime();
                isFree = true;
            }
        }
    }

    private void initMapper() {
        if (orientateRealDataMapper == null) {
            orientateRealDataMapper = MapperManager.getInstance().orientateRealDataMapper();
        }
        if (locationHisMapper == null) {
            locationHisMapper = MapperManager.getInstance().locationHisMapper();
        }
        if (locationNowMapper == null) {
            locationNowMapper = MapperManager.getInstance().locationNowMapper();
        }
    }

    /**
     * 初始化同步时间
     */
    private void initLastSyncTime() {
        Record last = locationHisMapper.selectLastLocationHisOrderByTime();
        if(last == null){
            this.lastSyncTime = new Timestamp(1);
        }else{
            this.lastSyncTime = TimeUtils.toTimeStap(last.getDate("gmt_record"));
        }
    }

    /**
     * 同步实时数据
     */
    private void syncRuntimeData() {

        List<Record> list = orientateRealDataMapper.loadNewOriRealData(lastSyncTime);
        if (list.size() == 0) {
            return;
        }
        saveHisLocationList(list);
    }

    /**
     * 更新当前人员的定位信息
     * @param loc
     */
    private void updateNowLocation(Record loc) {
        /**
         * 检查是否存在
         */
        Record now = locationNowMapper.selectLocationNowByTerminalMac(loc.getStr("terminal_mac"));
        if(now == null){
            /**
             * 插入
             */
            locationNowMapper.save(loc);
        }else{
            /**
             * 更新
             */
            loc.set("loc_id",now.getInt("loc_id"));
            locationNowMapper.update(loc);
        }
        /**
         * 更新人员 廊舱区信息
         */
        updateMemAreaInfo(loc);
    }

    /**
     * 更新人员廊舱区信息
     * @param loc
     */
    private void updateMemAreaInfo(Record loc) {
        String apMac = loc.getStr("ap_mac");
        String terminalMac = loc.getStr("terminal_mac");
        Record dev = locationNowMapper.selectDevInfoByApMac(apMac);
        Record user = locationNowMapper.selectLocationMemByTerminalMac(terminalMac);
        if(dev == null){
            return;
        }else{
            user.set("ut_id",dev.getInt("ut_id"))
                    .set("cabin_id",dev.getInt("cabin_id"))
                    .set("area_id",dev.getInt("area_id"))
                    .set("site_statke",dev.getStr("dev_statke"))
                    .set("gmt_update",TimeUtils.getTimestamp());
            locationNowMapper.update("T_LOCATION_MEM","id",user);
        }
    }

    /**
     * 保存同步过来的历史位置信息
     * @param list
     */
    private void saveHisLocationList(List<Record> list) {
        boolean saveHisFlag = true;
        for(Record r:list){
            Record  record = new Record()
                    .set("terminal_mac",r.getStr("StationMAC"))
                    .set("ap_mac",r.getStr("CurrentAP"))
                    .set("signal_strength",r.getInt("StationRssi"))
                    .set("gmt_daq",r.getDate("IOTime"))
                    .set("gmt_record",r.getDate("LUTime"))
                    .set("gmt_create",TimeUtils.getTimestamp());
            saveHisFlag = saveHisFlag && locationHisMapper.save(record);
            updateNowLocation(record);
        }
        if(!saveHisFlag){
            log.info("历史定位数据保存失败！");
        }
    }

}
