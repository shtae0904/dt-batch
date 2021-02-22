/***********************************************************************
[A4S] version [v2.0] Copyright ⓒ [2015] kt corp. All rights reserved.
This is a proprietary software of kt corp, and you may not use this file except in compliance with license agreement with kt corp.
Any redistribution or use of this software, with or without modification shall be strictly prohibited without prior written approval of kt corp,
and the copyright notice above does not evidence any actual or intended publication of such software.
 ************************************************************************/
/***********************************************************************
 * 프로그램명 : DB 접속 DAO   
 * 설명       : DB 접속 정의 
 * 개발자     : 유기상
 * 최초작성일 : 2015.09.10
 * 수정이력   :
 ************************************************************************/
package com.kt.flyer.batch.common.util;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SqlMapClientConfig {

	private static SqlMapClient sqlMap;
	
	static {

		Reader reader = null;
		Logger logger = Logger.getLogger(SqlMapClientConfig.class);
		try {
			logger.info("##### sqlMap :" + sqlMap);
			if (sqlMap == null) {
				Charset charset = Charset.forName("UTF-8");
				Resources.setCharset(charset);
				String resource = "com/kt/flyer/batch/common/conf/sqlMapConfig.xml";
				reader = Resources.getResourceAsReader(resource);
				sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
				logger.info("##### SqlMapClient Init !!");
			}
		} catch (Exception e) {
			logger.error("##### SqlMapClient Init ERROR !!", e);
			sqlMap = null;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("##### SqlMapIOException !!",e);
				//e.printStackTrace();
			}
		}
	}

	public static SqlMapClient getSqlMapClient() {
		return sqlMap;
	}

}
