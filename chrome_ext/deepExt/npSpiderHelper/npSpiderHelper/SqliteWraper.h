#pragma once
#include "thirdparty/sqlite/sqlite3.h"
#include <string>

class CSqliteWraper
{
public:
	CSqliteWraper(void);
	~CSqliteWraper(void);

	bool Check();
	 

	sqlite3 * m_db;
	std::string m_strDBPath;
};

