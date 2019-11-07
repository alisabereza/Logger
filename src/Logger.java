
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public enum Logger {
    INSTANCE;

    enum WorkMode {
        RElEASE, DEBUG;
    }

    final WorkMode workMode;
    private File[] logFiles;
    private FileOutputStream[] fosArr;

    Logger() {

        WorkMode temp = WorkMode.RElEASE;

        // In this option Resource Bundle is used
        try {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("logger");
        if (resourceBundle.getString("mode").equals("debug")) {temp = WorkMode.DEBUG;};}
        catch (MissingResourceException e) {
            System.out.println("Config file with Logging Mode was not found. Logs will be captured in Release Mode");
        };

        //... and this option is for regular config file
/*        File file = new File("/logConfig.cfg");
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] arrByte = new byte[fis.available()];
                fis.read(arrByte);
                String strOne = new String(arrByte);
                if ("debug".equals(strOne.toLowerCase())) {
                    temp = WorkMode.DEBUG;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        workMode = temp;
        System.out.println(workMode.toString());
        fileInitializer();
        fileOutputStreamsInitializer();
    }

    private void fileInitializer() {
        LogTypes[] types = LogTypes.values();
        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = format.format(currentDate);
        logFiles = new File[types.length + 1];
        for (int i = 0; i < logFiles.length - 1; i++) {
            logFiles[i] = new File("logs/"+types[i] + "_" + dateString + "_log.log");
            if (!logFiles[i].exists()) {
                try {
                    logFiles[i].createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        logFiles[logFiles.length - 1] = new File("logs/All_" + dateString + "_log.log");
        if (!logFiles[logFiles.length - 1].exists()) {
            try {
                logFiles[logFiles.length - 1].createNewFile();
             } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void fileOutputStreamsInitializer() {
        fosArr = new FileOutputStream[logFiles.length];
        for (int i = 0; i < fosArr.length; i++) {
            try {
                fosArr[i] = new FileOutputStream(logFiles[i],true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void log(LogTypes logType, String message){
        byte index = getIndex(logType);
        if(index==-1){
            return;
        }
        try {
            fosArr[index].write(messageFormalizer(logType, message).getBytes());
            fosArr[fosArr.length-1].write(messageFormalizer(logType, message).getBytes());
            fosArr[index].flush();
            fosArr[fosArr.length-1].flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte getIndex(LogTypes logType){
        LogTypes logTypes[] = LogTypes.values();
        for (byte i = 0; i < logTypes.length; i++) {
            if(logTypes[i].equals(logType))
                return i;
        }
        return -1;
    }

    private String messageFormalizer(LogTypes type, String message){
        return "\n" + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " + Thread.currentThread().getName() + " " + type.toString() + " " + message;
    }

    public void close(){
        for (FileOutputStream stream:fosArr) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}