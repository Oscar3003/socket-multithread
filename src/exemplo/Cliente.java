package exemplo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Cliente {
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Servidor:");
        String servidor = scan.nextLine();
        System.out.print("Nick:");
        String nick = scan.nextLine();
        
        try (Socket socket = new Socket(servidor, 9000)){
            String cmd;
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            
            new Thread(){
            	public void run() {
            		try {
						byte [] buffer = new byte[1024];
						int numRead = -1;
						do{
							numRead = is.read(buffer);
							if (numRead != -1) {
								String linha = new String(buffer, 0, numRead);
								System.out.println(linha);
							}
						}while(numRead != -1);
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
            }.start();
            
            do{
                cmd = scan.nextLine();
                String msg = String.format("%s: %s\n", nick, cmd);
                os.write(msg.getBytes("utf-8"));
                os.flush();
            }while(!"sair".equalsIgnoreCase(cmd));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
