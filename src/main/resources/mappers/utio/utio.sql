#sql("selectLastLocationHisOrderByTime")
select * from T_LOCATION_HIS tlh order by tlh.gmt_create desc limit 1
#end

#sql("selectLocationNowByTerminalMac")
select * from T_LOCATION_NOW tln where tln.terminal_mac = ? limit 1
#end

#sql("selectDevInfoByApMac")
SELECT dev.* FROM T_DEV dev LEFT JOIN T_DEV_AP da ON dev.dev_id = da.dev_id WHERE ap_mac = ? limit 1
#end

#sql("selectLocationMemByTerminalMac")
SELECT * FROM T_LOCATION_MEM WHERE terminal_mac = ? limit 1
#end
