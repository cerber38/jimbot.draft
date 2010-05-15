/*
 * JimBot - Java IM Bot
 * Copyright (C) 2006-2010 JimBot project
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

package ru.jimbot.core.api;

import java.util.Map;

import javax.servlet.http.HttpServlet;

/**
 * Интерфейс для сервисов, реализующих сервлеты
 * 
 * @author Prolubnikov Dmitry
 *
 */
public interface IHTTPService {
	public HttpServlet getServlet();
	public String getPath();
	public String getName(); // Название, которое нужно показать в меню
	public boolean isAuth(); // Требуется авторизация для доступа к сервлету?
	public Map<String, String> getInitParams(); // Начальные параметры для сервлета
}