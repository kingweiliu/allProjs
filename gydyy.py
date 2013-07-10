# -*- coding: utf8 -*-
import MySQLdb

strInsertSql1 = """insert into ff_vod(
vod_cid, vod_name, vod_title, vod_keywords, vod_color,
vod_actor, vod_director, vod_content, vod_pic, vod_area,
vod_language,vod_year, vod_continu, vod_addtime, vod_hits,
vod_hits_day,vod_hits_week, vod_hits_month, vod_hits_lasttime, vod_stars,
vod_status, vod_up, vod_down, vod_play, vod_server,
vod_url, vod_inputer, vod_reurl, vod_jumpurl, vod_letter,
vod_skin, vod_gold, vod_golder) values(
%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,
%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,
%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,
%s,%s,%s)"""

strInsertSql = """insert into ff_vod(
vod_cid, vod_name, vod_title, vod_keywords, vod_color,
vod_actor, vod_director, vod_content, vod_pic, vod_area,
vod_language,vod_year, vod_continu, vod_addtime, vod_hits,
vod_hits_day,vod_hits_week, vod_hits_month, vod_hits_lasttime, vod_stars,
vod_status, vod_up, vod_down, vod_play, vod_server,
vod_url, vod_inputer, vod_reurl, vod_jumpurl, vod_letter,
vod_skin, vod_gold, vod_golder) values(
%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,
%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,
%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,
%s,%s,%s)"""


connSource=MySQLdb.connect(host='localhost',user='root',passwd='',db='gydb',port=3306)
connTarget=MySQLdb.connect(host='localhost',user='root',passwd='',db='feifeicms',port=3306)
curSource=connSource.cursor()
curTarget = connTarget.cursor()
curSource.execute("SET NAMES utf8")
curSource.execute("SET CHARACTER_SET_CLIENT=utf8")
curSource.execute("SET CHARACTER_SET_RESULTS=utf8")
curTarget.execute("SET NAMES utf8")
curTarget.execute("SET CHARACTER_SET_CLIENT=utf8")
curTarget.execute("SET CHARACTER_SET_RESULTS=utf8")
ret = curSource.execute("select * from ff_vod limit 0,30")
for i in range(ret):
    t = curSource.fetchone()
    print t[1:3]
    curTarget.execute(strInsertSql, t[1:])



print str
