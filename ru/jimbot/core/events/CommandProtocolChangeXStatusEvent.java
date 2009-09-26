/*
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

package ru.jimbot.core.events;

import ru.jimbot.core.Service;
import ru.jimbot.core.CommandProtocolListener;

/**
 * Событие. Команда - сменить Х-статус
 * @author Prolubnikov Dmitry
 */
public class CommandProtocolChangeXStatusEvent implements Event {
    private Service srv;
    private int id;
    private String s1;
    private String s2;

    public CommandProtocolChangeXStatusEvent(Service srv, int id, String s1, String s2) {
        this.srv = srv;
        this.id = id;
        this.s1 = s1;
        this.s2 = s2;
    }

    public void handle() {
        for(CommandProtocolListener i:srv.getCommandProtocolListeners()) {
            i.onChangeXStatus(id, s1, s2);
        }
    }
}