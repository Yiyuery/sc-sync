# 项目简介

> NAME

    sc-sync

> ENV

    1、JDK 7
    2、Tomcat 7.0.78
    3、jFinal 3.2
    4、Mysql & SqlServer
    5、Maven 3.3

> package

    sc-sync.war

# 人员定位功能说明

    1、AP机和手持终端的 MAC映射关系（人员在管廊中运动时，会切换终端和AP机的绑定关系） --> 第三方实时定位数据记录生成
    2、根据时间节点分割并同步数据
        a. 先放入T_LOCATION_HIS -> list
        b. 遍历同步过来的list,根据手持终端的Mac地址，判断相关记录是否存在：
            存在：更新数据
            不存在：保存数据
        c. 关联 T_DEV 和 T_DEV_AP 获取 AP 的位置信息
        d. 根据实时登录中的 T_LOCATION_MEM 的手持终端Mac地址,将AP的位置信息更新到人员信息中

# 第三方数据库说明

    1、SqlServer:

    `ip`        : 172.16.253.251
    `user`      : sa
    `password`  : sa

    2、模拟对接

    `ip`        : 192.168.103.157
    `user`      : sa
    `password`  : Aa1111


# 配置说明

> 配置文件存放目录

    Linux   ：/icooper/config/sc-sync
    Windows  ：C:\Users\{yourName}\scooper\sc-sync

```

/*db.properties*/

# utio
db.utio.jdbcUrl=jdbc:mysql://192.168.106.104:3306/DB_UTIO?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull
db.utio.username=showclear
db.utio.password=showclear

# mloc
db.mloc.jdbcUrl=jdbc:sqlserver://192.168.103.157:1433;databaseName=gl_newpd
db.mloc.username=sa
db.mloc.password=Aa1111

/*config.properties*/

# 数据同步间隔和周期
timer.access.period=10
timer.access.delay=60

# 线程池数目上限
thread.max.task=10
```