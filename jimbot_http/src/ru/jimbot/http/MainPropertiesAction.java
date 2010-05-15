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

package ru.jimbot.http;

import org.apache.commons.beanutils.PropertyUtils;
import ru.jimbot.MainConfig;
import ru.jimbot.Manager;
import ru.jimbot.core.Password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Редактирование основных свойств программы
 *
 * @author Prolubnikov Dmitry
 */
public class MainPropertiesAction extends MainPageServletActions {
    public String perform(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String save = request.getParameter("save");
        if(save == null) {
            print(response, HTML_HEAD + "<TITLE>JimBot " + MainConfig.VERSION + " </TITLE></HEAD>" + BODY +
                    "<H2>Панель управления ботом</H2>" +
                    "<H3>Основные настройки бота</H3>");
            print(response, "<FORM METHOD=POST ACTION=\"main\">" +
                    "<INPUT TYPE=hidden NAME=\"page\" VALUE=\"main_props\">" +
                    "<INPUT TYPE=hidden NAME=\"save\" VALUE=\"1\">" +
                    prefToHtml(MainConfig.getInstance()) +
                    "<P><INPUT TYPE=submit VALUE=\"Сохранить\"></FORM>");
            print(response, "<P><A HREF=\"?page=index\">" +
                    "Назад</A><br>");
            print(response, "</FONT></BODY></HTML>");

        } else {
            PropertyDescriptor[] pr = PropertyUtils.getPropertyDescriptors(MainConfig.getInstance());
            for(PropertyDescriptor p : pr) {
                String c = p.getPropertyType().getSimpleName();
                if(p.isHidden()) {
                    // Пропускаем
                } else if(c.equals("boolean")) {
                    try {
                        PropertyUtils.setProperty(MainConfig.getInstance(), p.getName(),
                                getBoolVal(request, p.getName()));
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } else if(c.equals("String")) {
                    try {
                        PropertyUtils.setProperty(MainConfig.getInstance(), p.getName(),
                                getStringVal(request, p.getName()));
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } else if(c.equals("int") || (c.equals("long"))) {
                    try {
                        PropertyUtils.setProperty(MainConfig.getInstance(), p.getName(),
                                Integer.parseInt(getStringVal(request, p.getName())));
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } else if(c.equals("Password")) {
                    try {
                        String s = getStringVal(request, p.getName());
                        if(!"".equals(s)) PropertyUtils.setProperty(MainConfig.getInstance(),
                                p.getName(), new Password(s));
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
            MainConfig.getInstance().save();
//            UserPreference[] p = MainProps.getUserPreference();
//            for (int i = 0; i < p.length; i++) {
//                if (p[i].getType() == UserPreference.BOOLEAN_TYPE) {
//                    boolean b = getBoolVal(request, p[i].getKey());
//                    if (b != (Boolean) p[i].getValue()) {
//                        p[i].setValue(b);
//                        MainProps.setBooleanProperty(p[i].getKey(), b);
//                    }
//                } else if (p[i].getType() == UserPreference.INTEGER_TYPE) {
//                    int c = Integer.parseInt(getStringVal(request, p[i].getKey()));
//                    if (c != (Integer) p[i].getValue()) {
//                        p[i].setValue(c);
//                        MainProps.setIntProperty(p[i].getKey(), c);
//                    }
//                } else if (p[i].getType() != UserPreference.CATEGORY_TYPE) {
//                    String s = getStringVal(request, p[i].getKey());
//                    if (!s.equals((String) p[i].getValue())) {
//                        p[i].setValue(s);
//                        MainProps.setStringProperty(p[i].getKey(), s);
//                    }
//                }
//            }
//            MainProps.save();
            return "/main?page=message&id=0&ret=index";
        }
        return null;
    }
}