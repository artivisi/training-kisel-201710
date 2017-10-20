package id.artivisi.training.kisel.jpos;

import org.jpos.iso.ISOMsg;
import org.jpos.q2.Q2;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AplikasiClient {
    public static void main(String[] args) throws Exception {
        Q2 q2 = new Q2();
        q2.start();

        Thread.sleep(5 * 1000);

        QMUX sender = (QMUX) NameRegistrar.get("mux.kisel");

        DateFormat formatterBit7 = new SimpleDateFormat("MMddHHmmss");
        ISOMsg logonRequest = new ISOMsg("0800");
        logonRequest.set(7, formatterBit7.format(new Date()));
        logonRequest.set(11, "123456");
        logonRequest.set(70, "001");

        System.out.println("Mengirim request");
        ISOMsg response = sender.request(logonRequest, 20 * 1000);
        if (response == null) {
            System.out.println("Tidak dapat response sampai timeout");
            return;
        }

        String data = new String(response.pack());
        System.out.println("Terima response : ["+data+"]");

    }
}
