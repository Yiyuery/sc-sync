package cn.com.showclear.scsync.config.routes;

import com.jfinal.config.Routes;

/**
 * 数据相关，路径指向：/data/...
 * @author YF-XIACHAOYANG
 * @date 2017/9/4 12:50
 */
public class DataRoutes extends Routes {

    public static final String BASE_URI = "/data/";

    @Override
    public void config() {

    }

    private static String uri(String subPath) {
        return BASE_URI + subPath;
    }
}
