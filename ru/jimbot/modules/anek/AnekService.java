/**
 * JimBot - Java IM Bot
 * Copyright (C) 2006-2009 JimBot project
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package ru.jimbot.modules.anek;

import ru.jimbot.core.*;
import ru.jimbot.db.DBAdaptor;
import ru.jimbot.util.Log;
import ru.jimbot.protocol.IcqProtocol;

/**
 * Реализация сервиса анекдотного бота
 * 
 * @author Prolubnikov Dmitry
 */
public class AnekService extends DefaultService implements DbStatusListener {
    private String name = ""; // Имя сервиса
//    private HashMap<String, Protocol> prots = new HashMap<String, Protocol>(); // Ссылки на протоколы
    private AnekProps props;
    private DBAneks db;
    private AnekWork aw;
    private boolean start = false;
    private AnekCommandParser cmd;

    public AnekService(String name) {
        this.name = name;
        props = AnekProps.getInstance(name);
        aw = new AnekWork(name, this);
    }

    public AnekWork getAnekWork() {
        return aw;
    }

    /**
     * Запуск сервиса
     */
    public void start() {
        inq = new MsgInQueue(this);
        for(int i=0;i<props.uinCount();i++) {
            protocols.put(props.getUin(i), new IcqProtocol(this, i));
        }
        outq = new MsgOutQueue(this);
        inq.start();
        outq.start();
        cmd = new AnekCommandParser(this);
        // TODO ...
        addDbStatusListener(this);
        start = true;
        aw.initDB();
        db = aw.db;
    }

    /**
     * Остановка сервиса
     */
    public void stop() {
        inq.stop();
        inq = null;
        outq.stop();
        outq = null;
        aw.closeDB();
        for(CommandProtocolListener i:getCommandProtocolListeners()) {
            try{
                i.logOff();
            } catch (Exception e) {}
        }
        start = false;
        // TODO Подумать как лучше убрать ссылки и слушатели очередей, парсеров команд, протоколом и т.п.
    }

    /**
     * Сервис запущен?
     *
     * @return
     */
    public boolean isRun() {
        return start;
    }

    /**
     * Возвращает имя данного сервиса
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Набор настроек сервиса
     *
     * @return
     */
    public AbstractProps getProps() {
        return props;
    }

    /**
     * Экземпляр класса для работы с БД
     *
     * @return
     */
    public DBAdaptor getDB() {
        return db;
    }

    /**
     * Соединение с базой произошло, можно запускать УИНы
     * @param db - ссылка на базу
     */
    public void onConnect(DBAdaptor db) {
        for(CommandProtocolListener i:getCommandProtocolListeners()) {
            i.logOn();
        }
    }

    /**
     * При подключении к базе произошла ошибка
     * @param e
     */
    public void onError(String e) {
        Log.error("Ошибка соединения с базой данных. Отключаю сервис " + name);
        stop();
    }
}