import sqlite3

conn = sqlite3.connect("database.db")

conn.execute("drop table feedback")
conn.execute("CREATE TABLE feedback (\n"
    "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n"
    "\tcomment TEXT,\n"
    "\timpression TEXT,\n"
    "\tcontentshowtocoach TEXT,\n"
    "\tleader INTEGER NOT NULL,\n"
    "\tcoach INTEGER NOT NULL,\n"
    "\tq1 INTEGER ,\n"
    "\tq2 INTEGER ,\n"
    "\tq3 INTEGER ,\n"
    "\tq4 INTEGER ,\n"
    "\tq5 INTEGER ,\n"
    "\tq6 INTEGER ,\n"
    "\tq7 INTEGER ,\n"
    "\tq8 INTEGER ,\n"
    "\tq9 INTEGER ,\n"
    "\tq10 INTEGER ,\n"
    "\tsendtocoach INTEGER NOT NULL DEFAULT 0,\n"
    "\tFOREIGN KEY(leader) REFERENCES Users(user_id),\n"
    "\tFOREIGN KEY(coach) REFERENCES Users(user_id)\n);"
)

# test
# conn.execute("INSERT INTO messages values(1,'hello',2,1,'2021-04-04 11:24:54');")
# conn.execute("INSERT INTO messages values(2,'hi',1,2,'2021-04-04 11:35:54');")
# conn.execute("INSERT INTO messages values(3,'Can I have your number?',2,1,'2021-04-04 11:36:54');")
