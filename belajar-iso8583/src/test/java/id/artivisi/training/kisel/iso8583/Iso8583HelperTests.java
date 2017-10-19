package id.artivisi.training.kisel.iso8583;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Iso8583HelperTests {
    @Test
    public void testGenerateMessage(){
        DateFormat formatterBit7 = new SimpleDateFormat("MMddHHmmss");
        Map<Integer, String> logonRequest = new LinkedHashMap<Integer, String>();
        logonRequest.put(7, formatterBit7.format(new Date()));
        logonRequest.put(11, "834624");
        logonRequest.put(70, "001");

        String msg = Iso8583Helper.messageString("0800", logonRequest);
        System.out.println("Message : "+msg);
        System.out.println("Length : "+msg.length());
    }
}
