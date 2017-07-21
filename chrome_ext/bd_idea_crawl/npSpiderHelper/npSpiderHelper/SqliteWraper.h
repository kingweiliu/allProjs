#pragma once
#include "thirdparty/sqlite/sqlite3.h"
#include <string>
#include "plugin/npapi.h"
#include "plugin/npruntime.h"



class CSqliteWraper
{
public:
	CSqliteWraper(void);
	~CSqliteWraper(void);

	bool Check();
	 
	bool AddChapter(const NPVariant * url, const NPVariant * title, const NPVariant * content, const NPVariant* vid);
	bool AddVolume(const NPVariant * npvId, const NPVariant * npVName);

	sqlite3 * m_db;
	std::string m_strDBPath;
};

