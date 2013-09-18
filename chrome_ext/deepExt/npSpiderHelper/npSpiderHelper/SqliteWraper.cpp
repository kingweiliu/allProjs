#include "StdAfx.h"
#include "SqliteWraper.h"



CSqliteWraper::CSqliteWraper(void)
{
	int rc = sqlite3_open("c:\\liu.db", &m_db);
	char * charError = NULL;
	rc = sqlite3_exec(m_db, "create table chap(lname varchar(256))", NULL, NULL, &charError);
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
