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

package ru.jimbot.modules.anek.commands;

import ru.jimbot.core.Message;
import ru.jimbot.core.api.DefaultCommand;
import ru.jimbot.core.api.Parser;

import java.util.List;
import java.util.Arrays;
import java.util.Vector;

/**
 * @author Prolubnikov Dmitry
 */
public class CmdHelp extends DefaultCommand {
    public CmdHelp(Parser p) {
        super(p);
    }

    /**
     * Список ключевых слов, по которым можно вызвать эту команду
     *
     * @return
     */
    public List<String> getCommandPatterns() {
        return Arrays.asList(new String[] {"!help"});
    }

    /**
     * Выводит короткую помощь по команде (1 строка)
     *
     * @return
     */
    public String getHelp() {
        return " - помощь по командам\n!help <команда> - подробности использования команды";
    }

    /**
     * Выводит подробную помощь по команде
     *
     * @return
     */
    public String getXHelp() {
        return " <команда> - подробная информация по использованию команды.";
    }

    /**
     * Выполнение команды
     *
     * @param sn    - от кого?
     * @param param - вектор параметров (могут быть как строки, так и числа)
     * @return - результат (если нужен)
     */
    public String exec(String sn, Vector param) {
        String s = "";
        String c = "";
        try {
            c = (String)param.get(0);
        } catch (Exception e) {}
        try {
            if(c.equals("")){
                s += "Список доступных команд бота:\nво всех командах символы <> указывать не нужно\n";
                for(String i:p.getCommands()) {
                    if(p.getCommand(i).authorityCheck(sn)) {
                        String s1 = p.getCommand(i).getHelp();
                        s += s1.equals("") ? "" : (i + s1 + '\n');
                    }
                }
            } else {
                if(p.getCommands().contains(c)) {
                    s += c + p.getCommand(c).getXHelp();
                } else {
                    s += "Команда " + c + " не найдена.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Выполнение команды
     *
     * @param m - обрабатываемое сообщение с командой
     * @return - результат (если нужен)
     */
    public Message exec(Message m) {
        return new Message(m.getSnOut(), m.getSnIn(), exec(m.getSnIn(), p.getArgs(m, "$c")));
    }
}
