package id.artivisi.training.kisel.iso8583;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class AplikasiServer {
    public static void main(String[] args) throws Exception {
        Integer port = 12345;
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server siap di port "+port);

        Socket koneksi = server.accept();
        System.out.println("Client terhubung");

        DataInputStream in = new DataInputStream(koneksi.getInputStream());
        short length = in.readShort();
        System.out.println("Message length : "+length);

        byte[] data = new byte[length - 2];
        in.readFully(data);
        String message = new String(data);
        System.out.println("Request : ["+message+"]");

        String mti = message.substring(0, 4);
        String strBitmap = message.substring(4, 32 + 4);

        System.out.println("MTI : " + mti);
        System.out.println("Bitmap Hex: " + strBitmap);

        BigInteger bitmap = new BigInteger(strBitmap, 16);
        String strBitmapBin = bitmap.toString(2);
        System.out.println("Bitmap Bin : " + strBitmapBin);

        for(int i=0; i<128; i++){
            if (bitmap.testBit(128 - i)) {
                System.out.println("Bit "+i+" aktif");
            }
        }

        DateFormat formatterBit7 = new SimpleDateFormat("MMddHHmmss");
        Map<Integer, String> logonResponse = new LinkedHashMap<Integer, String>();
        logonResponse.put(7, formatterBit7.format(new Date()));
        logonResponse.put(11, "834624");
        logonResponse.put(39, "00");
        logonResponse.put(70, "001");

        String strLogonResponse = Iso8583Helper.messageString("0810", logonResponse);
        System.out.println("Response Data : " + strLogonResponse);
        DataOutputStream out = new DataOutputStream(koneksi.getOutputStream());
        out.writeShort((strLogonResponse.length() + 2));
        out.writeBytes(strLogonResponse);
        out.flush();
        System.out.println("Response terkirim");
        Thread.sleep(3 * 1000);
        koneksi.close();
    }
}
