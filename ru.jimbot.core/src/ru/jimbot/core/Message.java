/**
 * 
 */
package ru.jimbot.core;

/**
 * Сообщение
 * @author Prolubnikov Dmitry
 */
public class Message {
    public static final int TYPE_TEXT = 0;            // Текстовое сообщение
    public static final int TYPE_STATUS = 1;          // Сообщение о статусе
    public static final int TYPE_INFO = 2;
    public static final int TYPE_FLOOD_NOTICE = 3;    // Сообщение помечено как флуд (для принятия решения о блокировке юзера)

    private String snIn = "";  // От кого
    private String snOut = ""; // Кому
    private String msg = "";   // Текст сообщения
    private int type = 0;      // Тип сообщения
    private int status = 0;    // Статус, для сообщения о статусе
    private long time = 0;     // Время получения сообщения (для входящих)

    public Message(String snIn, String snOut, String msg, int type) {
        this.snIn = snIn;
        this.snOut = snOut;
        this.msg = msg;
        this.type = type;
        this.time = System.currentTimeMillis();
    }

    public Message(String snIn, String snOut, String msg) {
        this.snIn = snIn;
        this.snOut = snOut;
        this.msg = msg;
        this.type = TYPE_TEXT;
        this.time = System.currentTimeMillis();
    }

    /**
     * Возвращает копию себя, но с другим текстом
     * (Использую для разбивки сообщения на части или создания серии сообщений одному адресату)
     * @return
     */
    public Message getCopy(String s) {
        Message m = new Message(snIn, snOut, s, type);
        m.setTime(time);
        return m;
    }

    public String getSnIn() {
        return snIn;
    }

    public void setSnIn(String snIn) {
        this.snIn = snIn;
    }

    public String getSnOut() {
        return snOut;
    }

    public void setSnOut(String snOut) {
        this.snOut = snOut;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
