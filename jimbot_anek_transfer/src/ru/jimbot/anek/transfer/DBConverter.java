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

package ru.jimbot.anek.transfer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ru.jimbot.modules.anek.db.AdsLogStore;
import ru.jimbot.modules.anek.db.AdsStore;
import ru.jimbot.modules.anek.db.AneksStore;
import ru.jimbot.modules.anek.db.AneksTempStore;
import ru.jimbot.modules.anek.db.DbManager;

import com.amazon.carbonado.ConfigurationException;
import com.amazon.carbonado.RepositoryException;
import com.amazon.carbonado.Storage;

/**
 * @author Prolubnikov Dmitry
 *
 */
public class DBConverter {
	private Connection db;

	public DBConverter() {
		
	}
	
	public void openMysql(String host, String name, String user, String pass) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		db = DriverManager.getConnection("jdbc:mysql://" + host + "/" + name, user, pass);
	}
	
	public void convertAdsLog(String path) throws ConfigurationException, RepositoryException, SQLException {
		System.out.println("Start convert ads_log");
		DbManager dbm = new DbManager("ads_log", new File(path));
		Statement stmt = db.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		ResultSet rst = stmt.executeQuery("select time, ads_id, uin from ads_log");
		Storage<AdsLogStore> str = dbm.getRepository().storageFor(AdsLogStore.class);
		while(rst.next()) {
			AdsLogStore t = str.prepare();
			t.setId(rst.getTimestamp(1).getTime());
			t.setAdsId(rst.getInt(2));
			t.setUin(rst.getString(3));
			if(!t.tryInsert())
				t.update();
			System.out.println(t.getId());
		}
		rst.close();
		dbm.close();
	}
	
	public void convertAneks(String path) throws ConfigurationException, RepositoryException, SQLException {
		System.out.println("Start convert aneks");
		DbManager dbm = new DbManager("aneks", new File(path));
		Statement stmt = db.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rst = stmt.executeQuery("select id, text from aneks");
		Storage<AneksStore> str = dbm.getRepository().storageFor(AneksStore.class);
		while(rst.next()) {
			AneksStore t = str.prepare();
			t.setText(rst.getString(2));
			t.insert();
		}
		rst.close();
		dbm.close();
	}
	
	public void convertAneksTemp(String path) throws ConfigurationException, RepositoryException, SQLException {
		System.out.println("Start convert aneks_tmp");
		DbManager dbm = new DbManager("aneks", new File(path));
		Statement stmt = db.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rst = stmt.executeQuery("select id, text, uin from aneks_tmp");
		Storage<AneksTempStore> str = dbm.getRepository().storageFor(AneksTempStore.class);
		while(rst.next()) {
			AneksTempStore t = str.prepare();
			t.setId(rst.getLong(1));
			t.setText(rst.getString(2));
			t.setUin(rst.getString(3));
			t.insert();
		}
		rst.close();
		dbm.close();
	}
	
	public void convertAds(String path) throws ConfigurationException, RepositoryException, SQLException {
		System.out.println("Start convert ads");
		DbManager dbm = new DbManager("aneks", new File(path));
		Statement stmt = db.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rst = stmt.executeQuery("select id, txt, enable, note, client_id, exp_date, max_count from ads");
		Storage<AdsStore> str = dbm.getRepository().storageFor(AdsStore.class);
		while(rst.next()) {
			AdsStore t = str.prepare();
			t.setTxt(rst.getString(2));
			t.setEnable(rst.getInt(3)==1);
			t.setNote(rst.getString(4)==null ? "" : rst.getString(4));
			t.setClientId(String.valueOf(rst.getInt(5)));
			t.setExpDate(rst.getTimestamp(6)==null ? 0 : rst.getTimestamp(6).getTime());
			t.setMaxCount(rst.getInt(7));
			t.insert();
		}
	}
}
