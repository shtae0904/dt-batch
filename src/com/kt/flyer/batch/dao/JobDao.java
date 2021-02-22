package com.kt.flyer.batch.dao;

import java.sql.SQLException;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.kt.flyer.batch.common.util.SqlMapClientConfig;

public class JobDao {

private static JobDao instance;
	
	public static JobDao getInstance() {
		if (instance == null) {
			instance = new JobDao();
		}
		return instance;
	}
	
	public void orgUpdate() throws SQLException {
		SqlMapClientConfig.getSqlMapClient().queryForObject("orgUpdate");
	}
	
	public void cntpntUpdate() throws SQLException {
		SqlMapClientConfig.getSqlMapClient().queryForObject("cntpntUpdate");
	}
	
	public List getOrgPathUpdateSeq() throws SQLException {
		return SqlMapClientConfig.getSqlMapClient().queryForList("getOrgPathUpdateSeq");
	}
	
	public int orgPathDelete() throws SQLException {
		return SqlMapClientConfig.getSqlMapClient().delete("orgPathDelete");
	}
	
	public void orgPathUpdate(List list) throws SQLException {
		
		SqlMapClient sqlMapClient = SqlMapClientConfig.getSqlMapClient();
		try {
			sqlMapClient.startTransaction();
			sqlMapClient.startBatch();
			String EAI_SEQ = "";
			for (int idx = 0; idx < list.size() ; idx++) {
				EAI_SEQ = (String) list.get(idx);
				sqlMapClient.insert("orgPathUpdate",EAI_SEQ);
				
				//1000건 단위 배치실행
				if( idx % 1000 == 0 ) {
					sqlMapClient.executeBatch();
					sqlMapClient.startBatch();
				}
			}
					
			sqlMapClient.executeBatch();
			sqlMapClient.commitTransaction();

		} finally {
			sqlMapClient.endTransaction();
		}
	}
}
