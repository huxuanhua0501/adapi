package com.racetime.xsad;

import org.lionsoul.ip2region.*;

/**
 * Ip2Region测试类
 * @author Zebe
 */
public class Ip2RegionTest {

    /**
     * 程序入口
     * @param args 运行参数
     */
    public static void main(String[] args) throws Exception {
        String ip = "112.96.254.214";
        // 判断是否为IP地址
        boolean isIpAddress = Util.isIpAddress("12123.34"); // false
        isIpAddress = Util.isIpAddress(ip); // true
        System.out.println(isIpAddress);
        // IP地址与long互转
        long ipLong = Util.ip2long(ip); // 794805406
        String strIp = Util.long2ip(ipLong); // 47.95.196.158

        // 根据IP搜索地址信息
        DbConfig config = new DbConfig();
        String dbfile = "D:\\ip2region.db"; // 这个文件若没有请到以下地址下载：
        // https://gitee.com/lionsoul/ip2region/tree/master/data
        DbSearcher searcher = new DbSearcher(config, dbfile);

        // 二分搜索
        long start = System.currentTimeMillis();
        DataBlock block1 = searcher.binarySearch(ip);
        long end = System.currentTimeMillis();
        System.out.println(block1.getRegion()); // 中国|华东|浙江省|杭州市|阿里巴巴
        System.out.println("使用二分搜索，耗时：" + (end - start) + " ms"); // 1ms

        // B树搜索（更快）
        start = System.currentTimeMillis();
        DataBlock block2 = searcher.btreeSearch(ip);
        end = System.currentTimeMillis();
        System.out.println(block2.getRegion());
        System.out.println("使用B树搜索，耗时：" + (end - start) + " ms"); // 0ms

        
        // memory搜索（更快）
        start = System.currentTimeMillis();
        DataBlock block3 = searcher.memorySearch(ip);
        end = System.currentTimeMillis();
        System.out.println(block3.getRegion());
        System.out.println("使用memory搜索，耗时：" + (end - start) + " ms"); // 0ms

        
    }
}
