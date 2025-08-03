package com.chatserver.protocol;

public class Protocol {
    // Comandos Cliente -> Servidor
    public static final String CMD_ENTER = "ENTRAR";
    public static final String CMD_MESSAGE = "MENSAJE";
    public static final String CMD_ALL = "TODOS";
    public static final String CMD_USERS = "USUARIOS";
    public static final String CMD_EXIT = "SALIR";
    public static final String CMD_CLEAN_CONSOLE = "LIMPIAR CONSOLA";
    public static final String CMD_RESP_ERROR = "RESP ERROR";
    public static final String CMD_TEXT = "TEXTO";

    // Respuestas Servidor -> Cliente
    public static final String RESP_OK_ENTER = "OK ENTRAR";
    public static final String RESP_ERROR_ENTER = "ERROR ENTRAR";
    public static final String RESP_MESSAGE_FROM = "DE";

    public static final String RESP_USERS = "USUARIOS";
    public static final String RESP_OK_EXIT = "OK SALIR";
    public static final String RESP_OK_CLEAN = "OK LIMPIAR";
    public static final String RESP_CMD = "CMD";
    public static final String RESP_CMD_ERROR = "CMD ERROR";
    public static final String RESP_OK_ALL = "OK TODOS";
    public static final String RESP_ERROR_ALL = "ERROR TODOS";
    public static final String RESP_WRITE_TEXT = "ESCRIBIR TEXTO";

    public static final String RESP_INFO = "INFO";
    public static final String INFO_TYPE_SUCESS = "EXITO";
    public static final String INFO_TYPE_ERROR = "ERROR";
    public static final String INFO_TYPE_WARNING = "ADVERTENCIA";
    public static final String INTO_TYPE_ENTER = "ENTRA";
    public static final String INFO_TYPE_EXIT = "SALE";
}
