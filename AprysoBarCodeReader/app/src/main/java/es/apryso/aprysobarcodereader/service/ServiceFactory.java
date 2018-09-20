package es.apryso.aprysobarcodereader.service;

public class ServiceFactory {

    public enum SERVICE_INSTANCE { SEND_SERVICE, CONFIGURATION_SERVICE } ;

    private static SendService sendService;
    private static ConfigurationService configurationService;

    public static Object getInstance(SERVICE_INSTANCE instance) {

        switch (instance) {
            case SEND_SERVICE:
                if (sendService == null) {
                    sendService = new SendService();
                }
                return sendService;
            case CONFIGURATION_SERVICE:
                if (configurationService == null) {
                    configurationService = new ConfigurationService();
                }
                return configurationService;
            default:
                return null;
        }
    }

}
