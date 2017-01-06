package cn.lawliex;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Terence on 2017/1/5.
 */
public class Count {
    public static BlockingQueue<File> queue = new LinkedBlockingQueue<>();
    public static int fileCount = 0;
    public static int dirCount = 0;
    public static int lineCount = 0;
    public static void handleFile(File file){
        String fileName = file.getName();
        if(!fileName.endsWith(".java") && !fileName.endsWith(".xml") )
            return;
        fileCount++;
        String encoding = "UTF-8";
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt;
            while((lineTxt = bufferedReader.readLine())!= null){
//                if(lineTxt.length() > 0)
                    lineCount ++;
            }
            bufferedReader.close();

        }catch (Exception e){
            e.getMessage();
        }
    }
    public static void handle(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    while (queue.peek() != null) {
                        File file = queue.peek();
                        handleFile(file);
                        queue.remove();
                    }
                    System.out.println(lineCount);
                    System.out.println("file:"+fileCount);
                    break;
                }
            }
        }).start();
    }
    public static void handleDirtory(File file){
        dirCount ++;
        File[] files = file.listFiles();
        for(int i = 0; i < files.length; i++){
            if(files[i].isDirectory()){
                handleDirtory(files[i]);
            }else{
                queue.add(files[i]);
            }
        }
    }
    public static void countLine(File file){
        try {

            if(file.isDirectory()){
                handleDirtory(file);
            }else {
                handleFile(file);
            }
            handle();
            System.out.println("dir:"+dirCount);
        }catch (Exception e){
            e.getMessage();
        }
    }
    public static void main(String args[]){
        countLine(new File("D:\\Code\\Java\\ask\\src\\main"));
    }
}
