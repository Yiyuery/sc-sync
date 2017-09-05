package cn.com.showclear.scsync.mapper;

import cn.com.showclear.scsync.dao.mloc.OrientateRealDataMapperImpl;
import cn.com.showclear.scsync.dao.utio.LocationHisMapperImpl;
import cn.com.showclear.scsync.dao.utio.LocationNowMapperImpl;
import cn.com.showclear.scsync.mapper.mloc.OrientateRealDataMapper;
import cn.com.showclear.scsync.mapper.utio.LocationHisMapper;
import cn.com.showclear.scsync.mapper.utio.LocationNowMapper;

/**
 * Mapper 数据交互接口管理
 * @author YF-XIACHAOYANG
 * @date 2017/9/5 9:38
 */
public class MapperManager {

    /**
     * 终端实时数据
     */
    private static OrientateRealDataMapper _orientateRealDataMapper ;

    /**
     * 历史记录
     */
    private static LocationHisMapper _locationHisMapper ;

    /**
     * 实时记录
     */
    private static LocationNowMapper _locationNowMapper ;

    /**
     * 私有构造方法，防止被实例化
     */
    private MapperManager(){}

    /**
     * 此处使用一个内部类来维护单例
     */
    private static final class App{
        private static MapperManager INSTANCE = new MapperManager();
    }

    /**
     * 获取实例
     * @return
     */
    public static  MapperManager getInstance() {
        return App.INSTANCE;
    }


    public  OrientateRealDataMapper orientateRealDataMapper(){
        if (_orientateRealDataMapper == null) {
            _orientateRealDataMapper =  new OrientateRealDataMapperImpl();
        }
        return _orientateRealDataMapper;
    }


    public LocationHisMapper locationHisMapper() {
        if (_locationHisMapper == null) {
            _locationHisMapper =  new LocationHisMapperImpl();
        }
        return _locationHisMapper;
    }

    public LocationNowMapper locationNowMapper() {
        if (_locationNowMapper == null) {
            _locationNowMapper =  new LocationNowMapperImpl();
        }
        return _locationNowMapper;
    }

}
