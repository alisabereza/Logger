public class Main {

    {
        Logger.INSTANCE.log(LogTypes.ERROR, "File not found");
    }


    static {
        Logger.INSTANCE.log(LogTypes.WARNINGS, "Connection terminated");
    }

    public static void main(String[] args) {
        Logger.INSTANCE.log(LogTypes.INFO, "There are duplicate values in system");
        Logger.INSTANCE.log(LogTypes.SYSTEM, "System is OK");
        Logger.INSTANCE.close();
    }

}