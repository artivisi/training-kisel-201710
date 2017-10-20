package id.artivisi.training.kisel.jposweb.webjposclient.controller;

import id.artivisi.training.kisel.jposweb.webjposclient.dto.TopupRequest;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TopupController {

    private SimpleDateFormat formatterBit7 = new SimpleDateFormat("MMddHHmmss");

    @Autowired private QMUX qmux;

    @PostMapping("/topup")
    public Map<String, String> topup(@RequestBody @Valid TopupRequest request){
        Map<String, String> hasil = new HashMap<>();

        try {
            ISOMsg msgRequest = new ISOMsg("0200");
            msgRequest.set(4, request.getNilai().setScale(0).toString());
            msgRequest.set(7, formatterBit7.format(new Date()));
            msgRequest.set(11, "000123");

            String bit48 = request.getMsisdn().substring(0,4);
            bit48 += String.format("%1$" + 13 + "s", request.getMsisdn().substring(4));
            System.out.println("Bit 48 : ["+bit48+"]");
            msgRequest.set(48, bit48);

            msgRequest.set(63, "131001");

            ISOMsg isoResponse = qmux.request(msgRequest, 20 * 1000);

            if(isoResponse == null){
                // buat message reversal
                // kirim reversal
                // kalau masih timeout, ulangi 2x lagi reversal

                hasil.put("success", "false");
                hasil.put("error", "timeout");
                return hasil;
            }

            String response = new String(isoResponse.pack());

            hasil.put("success", "true");
            hasil.put("response_code", isoResponse.getString(39));
            hasil.put("raw_message", response);
        } catch (ISOException e) {
            e.printStackTrace();
        }

        return hasil;
    }
}
