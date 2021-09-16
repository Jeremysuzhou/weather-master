import org.junit.Assert;

import java.util.Optional;

public class Test {

    //测试正常情况
    @org.junit.Test
    public void testCommon() {
        Optional<Integer> resutlt = Main.getTemperature("江苏", "苏州", "苏州");
        Assert.assertNotNull(resutlt.get());
    }

    //测试省对 城市对 区不对的
    @org.junit.Test
    public void testNotMatch1() {
        Optional<Integer> resutlt = Main.getTemperature("江苏", "苏州", "扬州");
        Assert.assertTrue(resutlt.equals(Optional.empty()));
    }


    //测试省不对 城市对 区对的
    @org.junit.Test
    public void testNotMatch2() {
        Optional<Integer> resutlt = Main.getTemperature("北京", "苏州", "扬州");
        Assert.assertTrue(resutlt.equals(Optional.empty()));
    }

    //测试省对 城市不对 区对的
    @org.junit.Test
    public void testNotMatch3() {
        Optional<Integer> resutlt = Main.getTemperature("江苏", "扬州", "苏州");
        Assert.assertTrue(resutlt.equals(Optional.empty()));
    }

    //测试省不存在 城市对 区对的
    @org.junit.Test
    public void testNotMatch4() {
        Optional<Integer> resutlt = Main.getTemperature("巴黎", "扬州", "苏州");
        Assert.assertTrue(resutlt.equals(Optional.empty()));
    }

    //测试省存在且对 城市不存在 区对的
    @org.junit.Test
    public void testNotMatch5() {
        Optional<Integer> resutlt = Main.getTemperature("江苏", "巴黎", "苏州");
        Assert.assertTrue(resutlt.equals(Optional.empty()));
    }

    //测试省存在且对 城市存在且对 区不存在
    @org.junit.Test
    public void testNotMatch6() {
        Optional<Integer> resutlt = Main.getTemperature("江苏", "扬州", "巴黎");
        Assert.assertTrue(resutlt.equals(Optional.empty()));
    }
}
