#include "StdAfx.h"
#include "SqliteWraper.h"


char * szSqlCreateVolume = "create table if not exists volume(vid INTEGER PRIMARY KEY , vname varchar(255), vno INT)";
char * szSqlCreateChapter = "CREATE TABLE if not exists chapter(cid INTEGER PRIMARY KEY AUTOINCREMENT, vid INT, url varchar(255), cname varchar(255), cno INT, content TEXT)";
char * szSqlInsertChapter = "insert into chapter(vid, url, cname, cno, content) values(?, ?, ?, ?, ?)";
char * szSqlInsertVolume = "insert into volume(vid, vname) values(?, ?)";


CSqliteWraper::CSqliteWraper(void)
{
	int rc = sqlite3_open("c:\\liu2.db", &m_db);
	char * charError = NULL;
	rc = sqlite3_exec(m_db, szSqlCreateVolume, NULL, NULL, &charError);
	rc = sqlite3_exec(m_db, szSqlCreateChapter, NULL, NULL, &charError);
	rc= sqlite3_close(m_db);
}

bool CSqliteWraper::Check()
{
	bool bRet = false;
	do 
	{
		int rc = sqlite3_open(m_strDBPath.c_str(), &m_db);
		if (rc)
			break;



		char * charError = NULL;
		rc = sqlite3_exec(m_db, "create table if not exist chap (lname varchar(256))", NULL, NULL, &charError);
		if (rc)
			break;


		rc= sqlite3_close(m_db);

	} while (false);
	return bRet;
}


CSqliteWraper::~CSqliteWraper(void)
{
}


bool CSqliteWraper::AddChapter(const NPVariant * url, const NPVariant * title, const NPVariant * content, const NPVariant * vid)
{
	int rc = sqlite3_open("c:\\liu2.db", &m_db);

	sqlite3_stmt * stmt;
	if (sqlite3_prepare(m_db, szSqlInsertChapter, -1, &stmt, NULL))
	{
		return false;
	}

	sqlite3_bind_int(stmt, 1, vid->value.doubleValue);
	sqlite3_bind_text(stmt, 2, url->value.stringValue.UTF8Characters, url->value.stringValue.UTF8Length, SQLITE_STATIC);
	sqlite3_bind_text(stmt, 3,  title->value.stringValue.UTF8Characters, title->value.stringValue.UTF8Length, SQLITE_STATIC);
	sqlite3_bind_int(stmt, 4, 0);
	sqlite3_bind_text(stmt, 5, content->value.stringValue.UTF8Characters, content->value.stringValue.UTF8Length, SQLITE_STATIC);

	if (sqlite3_step(stmt) != SQLITE_DONE) {
		return false;
	}
	rc= sqlite3_close(m_db);
	return true;
}

bool CSqliteWraper::AddVolume(const NPVariant * npvId, const NPVariant * npVName)
{
	int rc = sqlite3_open("c:\\liu2.db", &m_db);

	sqlite3_stmt * stmt;
	if (sqlite3_prepare(m_db, szSqlInsertVolume, -1, &stmt, NULL))
	{
		return false;
	}

	sqlite3_bind_int(stmt, 1, npvId->value.doubleValue);
	sqlite3_bind_text(stmt, 2, npVName->value.stringValue.UTF8Characters, npVName->value.stringValue.UTF8Length, SQLITE_STATIC);
	if (sqlite3_step(stmt) != SQLITE_DONE) {
		return false;
	}
	rc= sqlite3_close(m_db);
	return true;
	
}