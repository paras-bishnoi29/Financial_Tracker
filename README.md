# Financial_Tracker

-The database name is expenseTrial which can be changed from application.properties file also with the user and password configuration.

-The Data Base shall have three tables :
1.expenses
2.users
3.salary

-The Structure of tables is as follows:
1.expenses -
+--------------+-------------+------+-----+-----------+-------------------+
| Field        | Type        | Null | Key | Default   | Extra             |
+--------------+-------------+------+-----+-----------+-------------------+
| id           | int         | NO   | PRI | NULL      | auto_increment    |
| name         | varchar(99) | NO   |     | NULL      |                   |
| amount       | double      | NO   |     | NULL      |                   |
| category     | varchar(99) | NO   |     | NULL      |                   |
| user_id      | int         | NO   | MUL | NULL      |                   |
| expense_date | date        | NO   |     | curdate() | DEFAULT_GENERATED |
+--------------+-------------+------+-----+-----------+-------------------+

2.users -
+----------+--------------+------+-----+---------+----------------+
| Field    | Type         | Null | Key | Default | Extra          |
+----------+--------------+------+-----+---------+----------------+
| id       | int          | NO   | PRI | NULL    | auto_increment |
| username | varchar(100) | NO   |     | NULL    |                |
| email    | varchar(255) | NO   | UNI | NULL    |                |
| password | varchar(255) | NO   |     | NULL    |                |
+----------+--------------+------+-----+---------+----------------+

3.salary - 
+---------+--------+------+-----+---------+----------------+
| Field   | Type   | Null | Key | Default | Extra          |
+---------+--------+------+-----+---------+----------------+
| id      | int    | NO   | PRI | NULL    | auto_increment |
| user_id | int    | NO   | UNI | NULL    |                |
| amount  | double | NO   |     | NULL    |                |
+---------+--------+------+-----+---------+----------------+
