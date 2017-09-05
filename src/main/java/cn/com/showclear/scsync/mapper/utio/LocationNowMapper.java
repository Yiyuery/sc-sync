package cn.com.showclear.scsync.mapper.utio;

import cn.com.showclear.scsync.mapper.Mapper;
import com.jfinal.plugin.activerecord.Record;

/**
 * 实时位置
 * @author YF-XIACHAOYANG
 * @date 2017/9/5 10:17
 */
public interface LocationNowMapper extends Mapper {

    /**
     * 判断当前表中是否含有 该终端地址的位置记录
     * @param terminalMac
     * @return
     */
    Record selectLocationNowByTerminalMac(String terminalMac);

    /**
     * 保存当前记录
     * @param loc
     * @return
     */
    boolean save(Record loc);

    /**
     * 更新记录
     * @param loc
     * @return
     */
    boolean update(Record loc);


    /**
     * 根据AP Mac地址获取AP 区域信息
     * @param apMac AP Mac地址
     * @return
     */
    Record selectDevInfoByApMac(String apMac);

    /**
     * 根据使用终端的Mac的地址 查询人员定位关联记录
     * @param terminalMac 使用终端的Mac的地址
     * @return
     */
    Record selectLocationMemByTerminalMac(String terminalMac);
}
