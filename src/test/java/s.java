import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class s {
    @Test
    public void ss() {
        String a = "9.26签到打卡\n" +
                "1.王姣  耶：11-14\n" +
                "2.林南 书1-5\n" +
                "3.恩江 \n" +
                "4.梁许箴言1-5\n" +
                "5.谢作曼\n" +
                "6.张艺慧 伯 10-12\n" +
                "7.郑丹丹 启21-22\n" +
                "8.何媛媛\n" +
                "9.薛明惠 林后8\n" +
                "【提后 3:16-17】 圣经都是  神所默示的（或作“凡  神所默示的圣经”），于教训、督责、使人归正、教导人学义都是有益的，叫属  |神的人得以完全，预 备行各样的善事。";
        String[] strings = a.split("\\n");
        for (String info : strings) {
            String nameReg = "(王姣|林南|恩江|梁许|谢作曼|张艺慧|郑丹丹|何媛媛|薛明惠)";
            String jingwenReg = "(.*\\d+)";

            info = info.replace(" ", "");
            Matcher m = Pattern.compile(nameReg + jingwenReg).matcher(info);
            if (m.find()) {
                String jingwen = m.group(2);
                String name = m.group(1);
                System.out.println("name:" + name + ",经文：" + jingwen);
            }

            System.out.println("==============");
        }

    }

    public void validateChineseName(String name) {
        String regEx = "[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(name);
        boolean isMatch = matcher.matches();
        System.out.println(isMatch);
        ;
    }

    @Test
    public void sss() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        System.out.println(formatter.format(new Date()));
    }
}
