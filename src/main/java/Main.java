
import org.apache.commons.lang.StringUtils;
import util.HttpUtils;
import util.JSONUtils;

import java.util.Optional;


public class Main {
    static String proUrl = "http://www.weather.com.cn/data/city3jdata/china.html";
    static String cityUrl = "http://www.weather.com.cn/data/city3jdata/provshi/${proCode}.html";
    static String countyUrl = "http://www.weather.com.cn/data/city3jdata/station/${countyCode}.html";
    static String weatherUrl = "http://www.weather.com.cn/data/sk/${weatherCode}.html";
    static int tpsSecondNum = 0;
    static int tpsAvgNum = 0;

    /**
     * 返回为null 表示不存在
     * @param province
     * @param city
     * @param county
     * @return
     */
    public static Optional<Integer> getTemperature(String province, String city, String county) {
        //synchronized 代码块
        synchronized (Main.class) {
            String proCode = getProCode(province);
            if (StringUtils.isEmpty(proCode)) {
                return Optional.empty();
            }

            String cityCode = getCityCode(city, proCode);
            if (StringUtils.isEmpty(cityCode)) {
                return Optional.empty();
            }

            //获取county编码
            String countyGetCode = proCode.concat(cityCode);
            String countyCode = getCountyCode(county, countyGetCode);

            String weatherCode = countyGetCode.concat(countyCode);
            String weatherResult = HttpUtils.doGet(weatherUrl.replace("${weatherCode}", weatherCode));
            if (!JSONUtils.validateJson(weatherResult)) {
                return Optional.empty();
            }

            float temp = JSONUtils.getTemp(weatherResult);

            //TODO 要求返回int?
            Optional<Integer> optional = Optional.of((int) temp);

            tpsNum("set");
            return optional;
        }
    }

    /**
     * 用于统计tps次数
     */
    public static void setAvgTpsNum() {
        // 一秒钟执行一次  计算tps
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //获取执行了多少秒
                int second1 = 0;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    second1++;
                    //获取描述并置0
                    int secondNum = tpsNum("get");
                    tpsAvgNum = secondNum / second1;
                }
            }
        });
        thread.start();
    }

    public static int tpsNum(String type) {
        synchronized (HttpUtils.class) {
            if ("get".equals(type)) {
                int num = tpsSecondNum;
                tpsSecondNum = 0;
                return num;
            } else {
                tpsSecondNum++;
            }

        }
        return 0;
    }


    public static Optional<Integer> run(String province, String city, String county) {
        //tps大于100
        if (tpsAvgNum > 100) {
            synchronized (Main.class) {
                return getTemperature(province, city, county);
            }
        } else {
            return getTemperature(province, city, county);
        }
    }

    public static String getProCode(String province) {
        //获取省的编码
        String proJson = HttpUtils.doGet(proUrl);
        if (!JSONUtils.validateJson(proJson)) {
            return null;
        }
        String proCode = JSONUtils.getCityOrProvinceData(proJson, province);
        return proCode;
    }

    public static String getCityCode(String city, String proCode) {
        //获取市编码
        String cityJson = HttpUtils.doGet(cityUrl.replace("${proCode}", proCode + ""));
        if (!JSONUtils.validateJson(cityJson)) {
            return null;
        }
        String cityCode = JSONUtils.getCityOrProvinceData(cityJson, city);
        return cityCode;
    }

    public static String getCountyCode(String county, String countyGetCode) {

        String countyJson = HttpUtils.doGet(countyUrl.replace("${countyCode}", countyGetCode));
        if (!JSONUtils.validateJson(countyJson)) {
            return null;
        }
        String countyCode = JSONUtils.getCityOrProvinceData(countyJson, county);
        return countyCode;
    }


    public static void main(String[] args) {
//        String res = HttpUtils.doGet(cityUrl.replace("${countyCode}","1011904"));
        setAvgTpsNum();
        System.out.println("结果" + run("江苏", "扬州", "扬州"));
    }
}
